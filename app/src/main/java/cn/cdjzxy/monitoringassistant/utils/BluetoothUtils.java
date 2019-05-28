package cn.cdjzxy.monitoringassistant.utils;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class BluetoothUtils {
    private BluetoothUtils() {
    }

    private static class BluetoothUtilsInner {
        static BluetoothUtils INSTANCE = new BluetoothUtils();
    }

    public static BluetoothUtils getInstance() {
        return BluetoothUtilsInner.INSTANCE;
    }

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning = true;
    private List<BluetoothDevice> mBlueList;
    private Context mContext;
    private BluetoothAdapter.LeScanCallback mLesanCall;
    private BluetoothUtils.BlueCallback callback;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void initBluetooth(Context context) {
        mContext = context.getApplicationContext();
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        //mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
        mBlueList = new ArrayList<>();
        //bluetooth scan callback
        mLesanCall = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
                if (!mBlueList.contains(bluetoothDevice)) {
                    mBlueList.add(bluetoothDevice);
                    callback.CallbackList(mBlueList);
                }
            }
        };
    }

    /**
     * get bluetooth adapter
     * @return
     */
    public BluetoothAdapter getBluetoothAdapter(){
        return mBluetoothAdapter;
    }

    /**
     * start scan bluetooth devices
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void startScanDevices() {
        if (mScanning) {
            mScanning = false;
            mBluetoothAdapter.startLeScan(mLesanCall);
            //every scan 10s
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScanDevices();
                }
            }, 10000);
        }
    }

    /**
     * stop scan bluetooth devices
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void stopScanDevices() {
        if (!mScanning) {
            mScanning=true;
            mBluetoothAdapter.stopLeScan(mLesanCall);
        }
    }

    /**
     * set callback
     *
     * @param callback
     */
    public void setCallback(BlueCallback callback) {
        this.callback = callback;
    }

    /**
     * @return
     */
    public boolean isSupportBlueTooth() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }


    public interface BlueCallback {
        void CallbackList(List<BluetoothDevice> mBlueList);
    }
}
