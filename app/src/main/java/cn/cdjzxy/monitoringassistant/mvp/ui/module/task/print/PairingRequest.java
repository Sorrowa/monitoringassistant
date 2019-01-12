package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print;


import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PairingRequest extends BroadcastReceiver {
    String strPsw = "0000";
    final String ACTION_PAIRING_REQUEST = "android.bluetooth.device.action.PAIRING_REQUEST";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_PAIRING_REQUEST)) {

            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

                try
                {
                    ClsUtils.setPin(device.getClass(), device, strPsw); // 手机和蓝牙采集器配对
                    ClsUtils.createBond(device.getClass(), device);
                    ClsUtils.cancelPairingUserInput(device.getClass(), device);
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
