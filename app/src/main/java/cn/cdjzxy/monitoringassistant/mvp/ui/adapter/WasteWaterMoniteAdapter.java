package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.UnitHolder;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.WasteWaterMoniteHolder;


/**
 * 主页tab
 */

public class WasteWaterMoniteAdapter extends DefaultAdapter<MonItems> {


    public WasteWaterMoniteAdapter(List<MonItems> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<MonItems> getHolder(View v, int viewType) {
        return new WasteWaterMoniteHolder(v);
    }
}
