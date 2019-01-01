package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.InstrumentalTestRecordHolder;


/**
 * 降水样品采集
 */

public class InstrumentalTestRecordAdapter extends DefaultAdapter<Object> {


    public InstrumentalTestRecordAdapter(List<Object> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_instrumental_test_record;
    }

    @Override
    public BaseHolder<Object> getHolder(View v, int viewType) {
        return new InstrumentalTestRecordHolder(v);
    }

}
