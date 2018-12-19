package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.FormHolder;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.TagHolder;


/**
 * 主页tab
 */

public class FormAdapter extends DefaultAdapter<FormSelect> {


    public FormAdapter(List<FormSelect> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point_select;
    }

    @Override
    public BaseHolder<FormSelect> getHolder(View v, int viewType) {
        return new FormHolder(v);
    }
}
