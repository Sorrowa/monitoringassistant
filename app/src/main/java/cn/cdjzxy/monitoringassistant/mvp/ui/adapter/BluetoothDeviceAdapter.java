package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.BleDeviceInfo;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.BluetoothDeviceHolder;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.LabelPrintDeviceActivity;


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
