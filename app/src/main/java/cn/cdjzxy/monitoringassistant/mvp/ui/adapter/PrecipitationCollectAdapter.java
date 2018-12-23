package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.PrecipitationCollectHolder;


/**
 * 降水样品采集
 */

public class PrecipitationCollectAdapter extends DefaultAdapter<SamplingDetail> {


    public PrecipitationCollectAdapter(List<SamplingDetail> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_precipitation_collect;
    }

    @Override
    public BaseHolder<SamplingDetail> getHolder(View v, int viewType) {
        return new PrecipitationCollectHolder(v);
    }
}
