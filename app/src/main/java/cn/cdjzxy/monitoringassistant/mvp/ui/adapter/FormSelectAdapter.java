package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.FormSelectHolder;


/**
 * 主页tab
 */

public class FormSelectAdapter extends DefaultAdapter<FormSelect> {


    public FormSelectAdapter(List<FormSelect> infos ) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<FormSelect> getHolder(View v, int viewType) {
        return new FormSelectHolder(v);
    }
}
