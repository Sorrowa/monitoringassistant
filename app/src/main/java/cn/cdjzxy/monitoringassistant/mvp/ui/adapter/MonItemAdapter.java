package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.MonItemHolder;


/**
 * 主页tab
 */

public class MonItemAdapter extends DefaultAdapter<MonItems> {


    public MonItemAdapter(List<MonItems> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_project;
    }

    @Override
    public BaseHolder<MonItems> getHolder(View v, int viewType) {
        return new MonItemHolder(v);
    }
}
