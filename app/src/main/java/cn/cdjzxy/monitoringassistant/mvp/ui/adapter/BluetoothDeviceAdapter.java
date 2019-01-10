package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.BluetoothDeviceHolder;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.LabelPrintDeviceActivity;


/**
 * 蓝牙设备适配器
 */

public class BluetoothDeviceAdapter extends DefaultAdapter<LabelPrintDeviceActivity.DeviceInfo> {


    public BluetoothDeviceAdapter(List<LabelPrintDeviceActivity.DeviceInfo> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_bluetooth_device;
    }

    @Override
    public BaseHolder<LabelPrintDeviceActivity.DeviceInfo> getHolder(View v, int viewType) {
        return new BluetoothDeviceHolder(v);
    }
}
