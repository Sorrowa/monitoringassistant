package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.WeatherHolder;


public class PlaceAdapter extends DefaultAdapter<String> {


    public PlaceAdapter(List<String> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<String> getHolder(View v, int viewType) {
        return new WeatherHolder(v);
    }
}
