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
    public static GpService mGpService;
    private List<DeviceInfo> mDeviceList = new ArrayList<>();
    private List<BluetoothDevice> devices = new ArrayList<>();
    private boolean isStart = false;
    private DeviceInfo currDevice = null;

    private PrinterServiceConnection conn = null;
    public static final int MESSAGE_CONNECT = 1;

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
        getDeviceList();
        registerBroadcast();
        connection();
        initDeviceData();

        //自动开始刷新
        discoveryDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Unregister broadcast listeners
        if (mFindBlueToothReceiver != null)
            this.unregisterReceiver(mFindBlueToothReceiver);
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
                    currDevice.setIndex(position);
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
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                devices.add(device);
                mDeviceList.add(new DeviceInfo(device.getName(), device.getAddress(), GpDevice.STATE_NONE));
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
        titleBar.setRightText("刷新中...");

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scaning);
        // Turn on sub-title for new devices

        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothDeviceAdapter
        mBluetoothAdapter.startDiscovery();
        isStart = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isStart = false;
                titleBar.setRightText("刷新");
                mBluetoothAdapter.cancelDiscovery();
            }
        }, 10000);

    }

//    // The on-click listener for all devices in the ListViews
//    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
//            // Cancel discovery because it's costly and we're about to connect
//
//            mBluetoothAdapter.cancelDiscovery();
//            // Get the device MAC address, which is the last 17 chars in the View
//            String info = ((TextView) v).getText().toString();
//            String noDevices = getResources().getText(R.string.none_paired).toString();
//            String noNewDevice = getResources().getText(R.string.none_bluetooth_device_found).toString();
//            Log.i("tag", info);
//            if (!info.equals(noDevices) && !info.equals(noNewDevice)) {
//                String address = info.substring(info.length() - 17);
//                // Create the result Intent and include the MAC address
//                Intent intent = new Intent();
//                intent.putExtra(GpPrintService.PORT_TYPE, PortParameters.BLUETOOTH);
//                intent.putExtra(GpPrintService.BLUETOOT_ADDR, address);
//                intent.putExtra("Device_Name", info.split("\n")[0]);
//                // Set result and finish this Activity
//                setResult(Activity.RESULT_OK, intent);
//                finish();
//            }
//        }
//    };

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
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (!devices.contains(device)) {
                        devices.add(device);
                        mDeviceList.add(new DeviceInfo(device.getName(), device.getAddress(), GpDevice.STATE_NONE));
                        mDeviceAdapter.notifyDataSetChanged();
                    }
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                Log.i("tag", "finish discovery " + mDeviceList.size());
                if (mDeviceList.size() == 0) {
                    ArtUtils.makeText(LabelPrintDeviceActivity.this, "未找到可用设备！");
                }
            }
        }
    };

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(GpCom.ACTION_CONNECT_STATUS);
        this.registerReceiver(PrinterStatusBroadcastReceiver, filter);
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Log.i(TAG, "connection");
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    void connectToDevice() {
        BluetoothDevice btDev = findDevice(currDevice);
        if (btDev == null) {
            return;
        }

        int rel = 0;
        try {
            rel = mGpService.openPort(currDevice.index, PortParameters.BLUETOOTH, currDevice.getAddress(), 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
        Log.e(TAG, "result :" + String.valueOf(r));
        if (r != GpCom.ERROR_CODE.SUCCESS) {
            if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
                mDeviceAdapter.notifyDataSetChanged();
            } else {
                ArtUtils.makeText(LabelPrintDeviceActivity.this, "连接错误：" + GpCom.getErrorText(r));
            }
        }
    }

    BluetoothDevice findDevice(DeviceInfo deviceInfo) {
        for (BluetoothDevice device : devices) {
            if (device.getAddress().equals(deviceInfo.getAddress())) {
                return device;
            }
        }

        return null;
    }

    private void disConnectToDevice() {
        if (currDevice == null) {
            return;
        }

        Log.d(TAG, "DisconnectToDevice ");
        try {
            mGpService.closePort(this.currDevice.index);
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
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                Log.d(TAG, "connect status " + type);
                if (currDevice != null) {
                    currDevice.setStatus(type);
                }
                mDeviceAdapter.notifyDataSetChanged();
            }
        }
    };

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.i(TAG, "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }


    public class DeviceInfo {
        private String name;
        private String address;
        private int status;
        private int index;

        public String getName() {
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

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
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
                    return "连接中...";

                case GpDevice.STATE_CONNECTED:
                case GpDevice.STATE_VALID_PRINTER:
                    return "连接成功";

                case GpDevice.STATE_INVALID_PRINTER:
                    return "不支持的蓝牙设备";
            }

            return "";
        }
    }
}
