package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.LabelInfo;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.BluetoothDeviceHolder;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.LabelHolder;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.LabelPrintDeviceActivity;


/**
 * 标签适配器
 */

public class LabelAdapter extends DefaultAdapter<LabelInfo> {


    public LabelAdapter(List<LabelInfo> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_label;
    }

    @Override
    public BaseHolder<LabelInfo> getHolder(View v, int viewType) {
        return new LabelHolder(v);
    }
}
