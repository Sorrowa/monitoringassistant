package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.WasteWaterCollectHolder;


/**
 * 水和废水采样
 */

public class WasteWaterCollectAdapter extends DefaultAdapter<SamplingContent> {

    private OnWasteWaterCollectListener collectListener;

    public WasteWaterCollectAdapter(List<SamplingContent> details, OnWasteWaterCollectListener collectListener) {
        super(details);
        this.collectListener = collectListener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_wastewater_collect;
    }

    @Override
    public BaseHolder<SamplingContent> getHolder(View v, int viewType) {
        return new WasteWaterCollectHolder(v, collectListener);
    }

    public interface OnWasteWaterCollectListener {
        void onSelected(View view, int position, boolean isSelected);
    }

    @Override
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        super.setOnItemClickListener(listener);
    }
}
