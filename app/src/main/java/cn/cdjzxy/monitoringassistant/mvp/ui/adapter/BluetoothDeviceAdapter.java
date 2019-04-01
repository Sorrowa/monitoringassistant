package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.bluetooth.BluetoothDevice;
import android.view.View;

import com.micheal.print.bean.BleDeviceInfo;
import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.BluetoothDeviceHolder;


/**
 * 蓝牙设备适配器
 */

public class BluetoothDeviceAdapter extends DefaultAdapter<BleDeviceInfo> {


    public BluetoothDeviceAdapter(List<BleDeviceInfo> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_bluetooth_device;
    }

    @Override
    public BaseHolder<BleDeviceInfo> getHolder(View v, int viewType) {
        return new BluetoothDeviceHolder(v);
    }
}
