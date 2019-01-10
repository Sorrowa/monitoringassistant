package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.WasteWaterCollectHolder;


/**
 * 水和废水采样
 */

public class WasteWaterCollectAdapter extends DefaultAdapter<SamplingDetail> {


    public WasteWaterCollectAdapter(List<SamplingDetail> details) {
        super(details);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_wastewater_collect;
    }

    @Override
    public BaseHolder<SamplingDetail> getHolder(View v, int viewType) {
        return new WasteWaterCollectHolder(v);
    }
}
