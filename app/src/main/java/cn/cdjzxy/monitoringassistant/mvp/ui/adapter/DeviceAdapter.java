package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.DeviceHolder;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;


/**
 * 主页tab
 */

public class DeviceAdapter extends DefaultAdapter<Devices> {


    public DeviceAdapter(List<Devices> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_device;
    }

    @Override
    public BaseHolder<Devices> getHolder(View v, int viewType) {
        return new DeviceHolder(v);
    }
}
