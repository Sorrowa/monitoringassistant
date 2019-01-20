package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print;

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

import com.aries.ui.view.title.TitleBarView;
import com.gprinter.aidl.GpService;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.service.GpPrintService;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.BluetoothDeviceAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

/**
 * 标签打印
 */

public class LabelPrintDeviceActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private TitleBarView titleBar;

    private BluetoothDeviceAdapter mDeviceAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private List<DeviceInfo> mDeviceList = new ArrayList<>();
    private List<BluetoothDevice> devices = new ArrayList<>();
    private boolean isStart = false;
    private DeviceInfo currDevice = null;

    public static final int MESSAGE_CONNECT = 1;
    public static final String CONNECT_STATUS = "connect.status";
    public static final String PRINTER_ADDR = "printer.addr";
    private static PortParameters mPortParam = new PortParameters();

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
        initPortParam();
        initDeviceData();

        //自动开始刷新
        discoveryDevice();
    }

    private void initPortParam() {
        mPortParam.setPortOpenState(getConnectState());
        mPortParam.setPortType(PortParameters.BLUETOOTH);
    }

    public boolean getConnectState() {
        try {
            if (LabelPrintActivity.GpService.getPrinterConnectStatus(LabelPrintActivity.PrinterIndex) == GpDevice.STATE_CONNECTED) {
                return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return false;
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

        if (PrinterStatusBroadcastReceiver != null) {
            this.unregisterReceiver(PrinterStatusBroadcastReceiver);
        }
    }

    /**
     * 初始化数据
     */
    private void initDeviceData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
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
                DeviceInfo device = mDeviceList.get(position);

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
                mDeviceList.add(new DeviceInfo(device.getName(), device.getAddress(), getDevieStatus(device.getAddress())));
            }
        }
    }

    private int getDevieStatus(String addr) {
        int status = GpDevice.STATE_NONE;

        if (addr != null && addr.equals(mPortParam.getBluetoothAddr())) {
            status = mPortParam.getPortOpenState() ? GpDevice.STATE_VALID_PRINTER : GpDevice.STATE_NONE;
        }

        return status;
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
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
//                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                if (!devices.contains(device)) {
                    devices.add(device);
                    mDeviceList.add(new DeviceInfo(device.getName(), device.getAddress(), getDevieStatus(device.getAddress())));
                } else {
                    DeviceInfo info = findDevice(device.getAddress());
                    if (info != null) {
                        info.setName(device.getName());
                        info.setAddress(device.getAddress());
                        info.setStatus(getDevieStatus(device.getAddress()));
                    }
                }
                mDeviceAdapter.notifyDataSetChanged();
//                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i("tag", "finish discovery " + mDeviceList.size());
                if (mDeviceList.size() == 0) {
                    ArtUtils.makeText(LabelPrintDeviceActivity.this, "未找到可用设备！");
                }
            }
        }
    };

    private DeviceInfo findDevice(String address) {
        for (DeviceInfo deviceInfo : mDeviceList) {
            if (deviceInfo.getAddress().equals(address)) {
                return deviceInfo;
            }
        }

        return null;
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GpCom.ACTION_CONNECT_STATUS);
        this.registerReceiver(PrinterStatusBroadcastReceiver, filter);

        // Register for broadcasts when a device is discovered
        filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    void connectToDevice() {
        if (currDevice == null) {
            return;
        }

        if (mPortParam.getBluetoothAddr().equals(currDevice.getAddress()) && mPortParam.getPortOpenState()) {
            return;
        }

        try {
            LabelPrintActivity.GpService.closePort(LabelPrintActivity.PrinterIndex);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        int rel = 0;
        try {
            rel = LabelPrintActivity.GpService.openPort(LabelPrintActivity.PrinterIndex, PortParameters.BLUETOOTH, currDevice.getAddress(), 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
        Log.e(TAG, "result :" + String.valueOf(r));
        if (r == GpCom.ERROR_CODE.SUCCESS) {
            mPortParam.setBluetoothAddr(currDevice.getAddress());
        } else {
            ArtUtils.makeText(LabelPrintDeviceActivity.this, "连接错误：" + GpCom.getErrorText(r));
        }
    }

    private void disConnectToDevice() {
        Log.d(TAG, "DisconnectToDevice ");
        try {
            LabelPrintActivity.GpService.closePort(LabelPrintActivity.PrinterIndex);
            currDevice = null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MESSAGE_CONNECT:
                    connectToDevice();
            }
            return false;
        }
    });

    /**
     * 设备状态更新
     */
    private BroadcastReceiver PrinterStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (GpCom.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
//                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                Log.d(TAG, "connect status " + type);
                if (currDevice != null) {
                    currDevice.setStatus(type);
                }
                mDeviceAdapter.notifyDataSetChanged();

                switch (type) {
                    case GpDevice.STATE_NONE:
                        mPortParam.setPortOpenState(false);
                        break;

                    case GpDevice.STATE_CONNECTED:
                    case GpDevice.STATE_VALID_PRINTER:
                        mPortParam.setPortOpenState(true);
                        break;
                }
            }
        }
    };

    public class DeviceInfo {
        private String name;
        private String address;
        private int status;

        public String getName() {
            if (TextUtils.isEmpty(name)) {
                return "";
            }

            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public DeviceInfo() {
        }

        public DeviceInfo(String name, String address, int status) {
            this.name = name;
            this.address = address;
            this.status = status;
        }

        public String getStatusName() {
            switch (this.status) {
                case GpDevice.STATE_NONE:
                    return "未连接";

                case GpDevice.STATE_LISTEN:
                    return "等待中";

                case GpDevice.STATE_CONNECTING:
                case GpDevice.STATE_CONNECTED:
                    return "连接中...";

                case GpDevice.STATE_VALID_PRINTER:
                    return "连接成功";

                case GpDevice.STATE_INVALID_PRINTER:
                    return "不支持的蓝牙设备";
            }

            return "";
        }
    }
}
