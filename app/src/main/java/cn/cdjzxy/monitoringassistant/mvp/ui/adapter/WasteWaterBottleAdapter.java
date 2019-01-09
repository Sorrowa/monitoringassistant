package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.WasteWaterBottleHolder;


public class WasteWaterBottleAdapter extends DefaultAdapter<SamplingFormStand> {


    public WasteWaterBottleAdapter(List<SamplingFormStand> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_wastewater_bottle_split;
    }

    @Override
    public BaseHolder<SamplingFormStand> getHolder(View v, int viewType) {
        return new WasteWaterBottleHolder(v);
    }
}
