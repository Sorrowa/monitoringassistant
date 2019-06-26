package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetailYQFs;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.InstrumentalTestRecordHolder;


/**
 * 降水样品采集
 */

public class InstrumentalTestRecordAdapter extends DefaultAdapter<SamplingDetailYQFs> {


    public InstrumentalTestRecordAdapter(List<SamplingDetailYQFs> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_instrumental_test_record;
    }

    @Override
    public BaseHolder<SamplingDetailYQFs> getHolder(View v, int viewType) {
        return new InstrumentalTestRecordHolder(v);
    }

}
