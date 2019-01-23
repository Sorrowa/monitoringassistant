package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.WasteWaterSamplingHolder;


/**
 * 水和废水采样单适配器
 */

public class WasteWaterSamplingAdapter extends DefaultAdapter<SamplingContent> {


    public WasteWaterSamplingAdapter(List<SamplingContent> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_waste_water_sampling;
    }

    @Override
    public BaseHolder<SamplingContent> getHolder(View v, int viewType) {
        return new WasteWaterSamplingHolder(v);
    }
}
