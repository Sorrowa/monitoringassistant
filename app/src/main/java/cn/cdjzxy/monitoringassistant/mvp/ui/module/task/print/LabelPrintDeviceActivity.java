package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;

import com.micheal.print.bean.BleDeviceInfo;
import com.micheal.print.manager.MyPrinterPlusConnManager;
import com.micheal.print.manager.MyPrinterPlusManager;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

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
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    private List<BleDeviceInfo> deviceInfoList = new ArrayList<>();
    private BleDeviceInfo currDevice = null;

    public static final int MESSAGE_CONNECT = 1;//连接设备
    private static final int REQUEST_CODE = 0x004;
    private String[] PERMISSIONS_STORAGE = {
            //读写权限
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //蓝牙权限
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH
    };
    private MyPrinterPlusManager myPrinterPlusManager;

    /**
     * 蓝牙状态
     */
    private enum BLE_DEVICE_STATE {
        DEVICE_NO_SUPPORTED("设备不支持蓝牙"),
        DEVICE_CLOSE("蓝牙关闭"),
        DEVICE_OPEN("蓝牙打开"),
        DEVICE_OPENING("蓝牙正在打开..."),
        DEVICE_Shutting_down("蓝牙正在关闭..."),
        DEVICE_STATE_Discovery("正在刷新列表...");

        private String name;

        BLE_DEVICE_STATE(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    //private static PortParameters mPortParam = new PortParameters();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        this.titleBar = titleBar;
        titleBar.setTitleMainText("连接打印机");
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPrinterPlusManager.isOpenBle()) {
                    titleBar.setRightText(BLE_DEVICE_STATE.DEVICE_STATE_Discovery.toString());
                    discoveryDevice();
                } else {
                    myPrinterPlusManager.openMyBle(LabelPrintDeviceActivity.this);
                }
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
        initPermission();//请求权限
        initPrinterPlusManager();//初始化蓝牙打印机信息
        registerBroadcast();//注册广播
        initDeviceData();
        //自动开始刷新
        //discoveryDevice();

    }

    private void initPrinterPlusManager() {
        myPrinterPlusManager = MyPrinterPlusManager.getInstance(this);
        mBluetoothAdapter = myPrinterPlusManager.getBluetoothAdapter();
        if (mBluetoothAdapter == null) {
            titleBar.setRightText(BLE_DEVICE_STATE.DEVICE_NO_SUPPORTED.toString());
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                titleBar.setRightText(BLE_DEVICE_STATE.DEVICE_STATE_Discovery.toString());
                discoveryDevice();//刷新当前设备列表
            } else {
                titleBar.setRightText(BLE_DEVICE_STATE.DEVICE_OPENING.toString());
                myPrinterPlusManager.openMyBle(this);//打开蓝牙
            }
        }
    }

    /**
     * 初始化打印服务
     */
    private void initPermission() {
        ArrayList<String> perList = new ArrayList<>();
        for (String permission : PERMISSIONS_STORAGE) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
                perList.add(permission);
            }
        }
        if (perList.size() > 0) {
            String[] p = new String[perList.size()];
            ActivityCompat.requestPermissions(this, perList.toArray(p), REQUEST_CODE);
        }
    }

    /**
     * 注册广播
     */
    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);//发现蓝牙广播
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙打开状态广播
        filter.addAction(MyPrinterPlusManager.BLUE_TOOTH_DEVICE_NO_SUPPORTED);//设备不支持蓝牙
        filter.addAction(MyPrinterPlusConnManager.ACTION_CONN_STATE);//设备连接状态广播
        filter.addAction(MyPrinterPlusConnManager.ACTION_QUERY_PRINTER_STATE);//设备当前状态
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//蓝牙搜索结束
        registerReceiver(mBlueToothReceiver, filter);
        // Get the local Bluetooth adapter
        //mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        myPrinterPlusManager.stopGetDeviceList();
        // Unregister broadcast listeners
        if (mBlueToothReceiver != null)
            this.unregisterReceiver(mBlueToothReceiver);
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

        mDeviceAdapter = new BluetoothDeviceAdapter(deviceInfoList);
        mDeviceAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                myPrinterPlusManager.stopGetDeviceList();
                //先断开连接
                myPrinterPlusManager.disConnectToDevice(deviceList.get(position));
                //获取设备信息
                BleDeviceInfo device = deviceInfoList.get(position);
                //设备不一样则重新连接
                if (device != currDevice) {
                    //记录当前设备
                    currDevice = device;
                }
                //异步连接
                myPrinterPlusManager.connect(deviceList.get(position));
            }
        });

        recyclerview.setAdapter(mDeviceAdapter);
    }

    /**
     * 扫描设备
     */
    private void discoveryDevice() {
        deviceList.clear();
        mDeviceAdapter.notifyDataSetChanged();
        myPrinterPlusManager.getDeviceList();
    }

    /**
     * 扫描结果接收
     * changes the title when discovery is finished
     */
    private final BroadcastReceiver mBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int openState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.STATE_OFF);
                    switch (openState) {
                        case BluetoothAdapter.STATE_OFF:              //蓝牙关闭
                            titleBar.setRightText(BLE_DEVICE_STATE.DEVICE_CLOSE.toString());
                            break;
                        case BluetoothAdapter.STATE_ON:               //蓝牙打开
                            titleBar.setRightText(BLE_DEVICE_STATE.DEVICE_OPEN.toString());
                            discoveryDevice();
                            break;
                        case BluetoothAdapter.STATE_TURNING_ON:       //蓝牙正在打开
                            titleBar.setRightText(BLE_DEVICE_STATE.DEVICE_OPENING.toString());
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:      //蓝牙正在关闭
                            titleBar.setRightText(BLE_DEVICE_STATE.DEVICE_Shutting_down.toString());
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        deviceList.add(device);
                        BleDeviceInfo deviceInfo = new BleDeviceInfo();
                        deviceInfo.setAddress(device.getAddress());
                        deviceInfo.setName(device.getName());
                        deviceInfo.setStatus(MyPrinterPlusConnManager.CONN_STATE_DISCONNECT);
                        deviceInfoList.add(deviceInfo);
                    }
                    mDeviceAdapter.notifyDataSetChanged();
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.i("tag", "finish discovery " + deviceList.size());
                    if (deviceList.size() == 0) {
                        ArtUtils.makeText(LabelPrintDeviceActivity.this, "未找到可用设备！");
                    }
                    break;
                case MyPrinterPlusConnManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra(MyPrinterPlusManager.DEVICE_STATE, -1);
                    if (currDevice != null) {
                        currDevice.setStatus(state);
                    }
                    mDeviceAdapter.notifyDataSetChanged();
                    break;
                case MyPrinterPlusConnManager.ACTION_QUERY_PRINTER_STATE:
                    break;
            }
        }
    };




//    public Handler mHandler = new Handler(new Handler.Callback() {
//        @Override
//        public boolean handleMessage(Message message) {
//            switch (message.what) {
//                case MESSAGE_CONNECT:
//                    connectToDevice();
//                    break;
//            }
//            return false;
//        }
//    });

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
