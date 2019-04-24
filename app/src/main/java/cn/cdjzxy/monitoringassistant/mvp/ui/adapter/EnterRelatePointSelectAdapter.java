package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.EnterRelatePointSelectHolder;


public class EnterRelatePointSelectAdapter  extends DefaultAdapter<EnterRelatePoint> {


    public EnterRelatePointSelectAdapter(List<EnterRelatePoint> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<EnterRelatePoint> getHolder(View v, int viewType) {
        return new EnterRelatePointSelectHolder(v);
    }
}