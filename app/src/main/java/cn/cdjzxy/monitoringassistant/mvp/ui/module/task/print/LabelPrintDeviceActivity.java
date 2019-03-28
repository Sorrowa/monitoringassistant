package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.aries.ui.view.title.TitleBarView;
import com.micheal.print.DeviceConnFactoryManager;

import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.BleDeviceInfo;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.BluetoothDeviceAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

import static com.micheal.print.DeviceConnFactoryManager.CONN_STATE_FAILED;

/**
 * 标签打印
 */

public class LabelPrintDeviceActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private TitleBarView titleBar;

    private BluetoothDeviceAdapter mDeviceAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private List<BleDeviceInfo> mDeviceList = new ArrayList<>();
    private List<BluetoothDevice> devices = new ArrayList<>();
    private boolean isStart = false;
    private BleDeviceInfo currDevice = null;

    public static final int MESSAGE_CONNECT = 1;
    private int id = 0;
    private DeviceConnFactoryManager manager;


    //private static PortParameters mPortParam = new PortParameters();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        this.titleBar = titleBar;
        titleBar.setTitleMainText("连接打印机");
        titleBar.setRightText("刷新");
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoveryDevice();
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_label_print_device;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        registerBroadcast();
        //initPortParam();
        initDeviceData();

        //自动开始刷新
        discoveryDevice();
        manager = new DeviceConnFactoryManager.Build()
                .setId(0)
                //设置连接方式
                .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                .setContext(this)
                //设置连接的蓝牙mac地址
                .setMacAddress(currDevice.getAddress()).build();
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            isStart = false;
            mBluetoothAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        if (mFindBlueToothReceiver != null)
            this.unregisterReceiver(mFindBlueToothReceiver);
//        if (PrinterStatusBroadcastReceiver != null) {
//            this.unregisterReceiver(PrinterStatusBroadcastReceiver);
//        }
    }

    /**
     * 初始化数据
     */
    private void initDeviceData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mDeviceAdapter = new BluetoothDeviceAdapter(mDeviceList);
        mDeviceAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                //先断开连接
                disConnectToDevice();
                //获取设备信息
                BleDeviceInfo device = mDeviceList.get(position);
                //设备不一样则重新连接
                if (device != currDevice) {
                    //记录当前设备
                    currDevice = device;
                    //异步连接
                    mHandler.sendEmptyMessage(MESSAGE_CONNECT);
                }
            }
        });

        recyclerview.setAdapter(mDeviceAdapter);
    }

    /**
     * 获取设备列表
     */
    protected void getDeviceList() {
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devices.add(device);

                mDeviceList.add(new BleDeviceInfo(device.getName(), device.getAddress(),
                        0));
            }
        }
    }


    /**
     * 扫描设备
     */
    private void discoveryDevice() {
        if (isStart) {
            titleBar.setRightText("刷新");
            isStart = false;
            mBluetoothAdapter.cancelDiscovery();
            return;
        }
        isStart = true;

        titleBar.setRightText("刷新中...");
        devices.clear();
        mDeviceList.clear();
        mDeviceAdapter.notifyDataSetChanged();

        getDeviceList();

        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothDeviceAdapter
        mBluetoothAdapter.startDiscovery();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isStart) {
                    isStart = false;
                    titleBar.setRightText("刷新");
                    mBluetoothAdapter.cancelDiscovery();
                }
            }
        }, 10000);

    }

    /**
     * 扫描结果接收
     * changes the title when discovery is finished
     */
    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (!devices.contains(device)) {
                        devices.add(device);
                        mDeviceList.add(new BleDeviceInfo(device.getName(), device.getAddress(),
                                DeviceConnFactoryManager.CONN_STATE_DISCONNECT));
                    } else {
                        BleDeviceInfo info = findDevice(device.getAddress());
                        if (info != null) {
                            info.setName(device.getName());
                            info.setAddress(device.getAddress());
                            info.setStatus(DeviceConnFactoryManager.CONN_STATE_DISCONNECT);
                        }
                    }
                    mDeviceAdapter.notifyDataSetChanged();
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.i("tag", "finish discovery " + mDeviceList.size());
                    if (mDeviceList.size() == 0) {
                        ArtUtils.makeText(LabelPrintDeviceActivity.this, "未找到可用设备！");
                    }
                    break;
                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    if (currDevice != null) {
                        currDevice.setStatus(state);
                    }
                    mDeviceAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    /**
     * @param address
     * @return
     */
    private BleDeviceInfo findDevice(String address) {
        for (BleDeviceInfo BleDeviceInfo : mDeviceList) {
            if (BleDeviceInfo.getAddress().equals(address)) {//判断地址相等  就是同一个蓝牙   地址不等则是不同设备
                return BleDeviceInfo;
            }
        }
        return null;
    }


    /**
     * 连接设备
     */
    private void connectToDevice() {
        if (currDevice == null) {
            return;
        }

        manager.openPort();
    }

    /**
     * 重新连接回收上次连接的对象，避免内存泄漏
     */
    private void disConnectToDevice() {
        manager.closePort();

    }

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MESSAGE_CONNECT:
                    connectToDevice();
                    break;
            }
            return false;
        }
    });

    @Override
    public void onBackPressed() {
        if (currDevice != null) {
            Intent intent = new Intent();
            intent.putExtra("device_name", currDevice.getName());
            intent.putExtra("is_connect", true);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }
}
