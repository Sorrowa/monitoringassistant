package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.PointItemHolder;


/**
 * 主页tab
 */

public class PointItemAdapter extends DefaultAdapter<EnvirPoint> {


    public PointItemAdapter(List<EnvirPoint> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_item;
    }

    @Override
    public BaseHolder<EnvirPoint> getHolder(View v, int viewType) {
        return new PointItemHolder(v);
    }
}
