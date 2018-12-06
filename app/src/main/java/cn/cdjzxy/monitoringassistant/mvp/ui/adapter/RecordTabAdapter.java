package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.RecordTabHolder;


/**
 * 主页tab
 */

public class RecordTabAdapter extends DefaultAdapter<Tab> {


    public RecordTabAdapter(List<Tab> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_record_tab;
    }

    @Override
    public BaseHolder<Tab> getHolder(View v, int viewType) {
        return new RecordTabHolder(v);
    }
}
