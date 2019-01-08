package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.MethodHolder;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.UnitHolder;


/**
 * 主页tab
 */

public class UnitAdapter extends DefaultAdapter<Unit> {


    public UnitAdapter(List<Unit> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<Unit> getHolder(View v, int viewType) {
        return new UnitHolder(v);
    }
}
