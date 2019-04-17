package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseMonitorPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.NoiseMonitorHolder;

public class NoiseMonitorAdapter extends DefaultAdapter<NoiseMonitorPrivateData> {
    private ItemClickListener listener;

    public NoiseMonitorAdapter(List<NoiseMonitorPrivateData> infos, ItemClickListener listener) {
        super(infos);
        this.listener = listener;
    }

    @Override
    public BaseHolder<NoiseMonitorPrivateData> getHolder(View v, int viewType) {
        return new NoiseMonitorHolder(v, listener);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_noise_adapter;

    }

    public interface ItemClickListener {
        void onSelected(View view, int position);

        void onClick(View view, int position);
    }
}
