package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.WasteWaterBottleHolder;


/**
 * 主页tab
 */

public class WasteWaterCollectAdapter extends DefaultAdapter<Tab> {


    public WasteWaterCollectAdapter(List<Tab> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_wastewater_collect;
    }

    @Override
    public BaseHolder<Tab> getHolder(View v, int viewType) {
        return new WasteWaterBottleHolder(v);
    }
}