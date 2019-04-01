package com.micheal.print.manager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.micheal.print.thread.ThreadPool;

import java.util.Set;
import java.util.Vector;

import static com.micheal.print.manager.MyPrinterPlusConnManager.ACTION_CONN_STATE;

/**
 * 2019/2/29：嘉泽@android向  重构：打印机蓝牙扫描——连接——打印逻辑
 */
public class MyPrinterPlusManager {
    private static MyPrinterPlusManager myPrinterPlusManager;
    private Context myConText;

    private MyPrinterPlusManager() {
    }

    private MyPrinterPlusManager(Context context) {
        this.myConText = context;
    }

    /********************* ******************* 蓝牙扫描模块 ******************* *************************/
    public static MyPrinterPlusManager getInstance(Context context) {
        if (myPrinterPlusManager == null) {
            synchronized (MyPrinterPlusManager.class) {
                if (myPrinterPlusManager == null) {
                    myPrinterPlusManager = new MyPrinterPlusManager(context);
                }
            }
        }
        return myPrinterPlusManager;
    }


    /**
     * 蓝牙适配器
     */
    private BluetoothAdapter bluetoothAdapter;

    public BluetoothAdapter getBluetoothAdapter() {
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        return bluetoothAdapter;
    }

    /**
     * 蓝牙是否打卡
     *
     * @return
     */
    public boolean isOpenBle() {
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        if (bluetoothAdapter == null) return false;
        return bluetoothAdapter.isEnabled();
    }

    /**
     * 打开蓝牙————清先请求位置权限
     * 请注册广播 {@link BluetoothAdapter.ACTION_STATE_CHANGED 监听蓝牙打开状态}
     *
     * @param context
     */
    public void openMyBle(Context context) {
        if (myConText == null) myConText = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            sendBroadcast(0, BLUE_TOOTH_DEVICE_NO_SUPPORTED);
        } else {
            if (!bluetoothAdapter.isEnabled()) {//判断是否打开
                bluetoothAdapter.enable();
            }
        }
    }

    /**
     * 获取附近蓝牙设备列表——通过广播回传{@link BluetoothDevice.ACTION_FOUND}
     */
    public void getDeviceList() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothAdapter.startDiscovery();
        }
    }

    /**
     * 获取已经配对的设备列表
     *
     * @return
     */
    public Set<BluetoothDevice> getPaired() {
        return bluetoothAdapter.getBondedDevices();//已配对的设备列表
    }

    /**
     * 停止附近蓝牙设备列表——
     */
    public void stopGetDeviceList() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        }
    }

    /********************* ******************* 蓝牙连接模块 ******************* *************************/
    private MyPrinterPlusConnManager connManager;

    private void initMyConnectManager() {
        if (myConText != null) {
            connManager = MyPrinterPlusConnManager.getInstance(myConText);
        }
    }

    private MyPrinterPlusConnManager getConnManager() {
        return connManager;
    }

    /**
     * 连接打印机设备
     *
     * @param device
     */
    public void connect(final BluetoothDevice device) {
        if (device != null) {
            if (connManager == null) initMyConnectManager();
            sendBroadcast(MyPrinterPlusConnManager.CONN_STATE_CONNECTING, ACTION_CONN_STATE);//发生广播：蓝牙连接状态——正在连接
            ThreadPool.getInstantiation()
                    .addTask(new Runnable() {
                        @Override
                        public void run() {
                            if (connManager != null) {
                                connManager.openPort(device);
                            } else {
                                sendBroadcast(MyPrinterPlusConnManager.CONN_STATE_FAILED, ACTION_CONN_STATE);
                            }
                        }
                    });
        }
    }

    /**
     * 查询打印机状态
     */
    public void queryPrinterState() {
        if (connManager != null) ;
        connManager.queryPrinterState();
    }

    public void printData(Vector<Byte> data) {
        if (connManager!=null){
            connManager.sendDataImmediately(data);
        }
    }

    public void disConnectToDevice() {
        if (connManager != null) {
            connManager.closePort();
        }
    }

    public static final String DEVICE_STATE = "STATE";
    public static final String BLUE_TOOTH_DEVICE_NO_SUPPORTED = "device_no_supported";//设备不支持蓝牙
//    public static final String BlUE_TOOTH_DEVICE_OPEN_STATE = "device_open_state";
//
//    public enum DEVICE_OPEN_STATE {
//        OPENING,//正在打开
//        OPEN_FAIL,//打开失败
//        OPENED,//打开
//    }

    /**
     * 发生广播
     *
     * @param state  状态
     * @param action 广播类型
     */
    private void sendBroadcast(int state, String action) {
        Intent intent = new Intent(action);
        intent.putExtra(DEVICE_STATE, state);
        if (myConText != null) {
            myConText.sendBroadcast(intent);
        }
    }

    private void sendBroadcast(String state, String action) {
        Intent intent = new Intent(action);
        intent.putExtra(DEVICE_STATE, state);
        if (myConText != null) {
            myConText.sendBroadcast(intent);
        }
    }
}
