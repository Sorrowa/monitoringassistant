package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.NoiseSourceHolder;

public class NoiseSourceAdapter extends DefaultAdapter<NoisePrivateData.MianNioseSourceBean> {
    private ItemClickListener listener;

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public NoiseSourceAdapter(List<NoisePrivateData.MianNioseSourceBean> infos, ItemClickListener listener) {
        super(infos);
        this.listener = listener;
    }

    @Override
    public BaseHolder<NoisePrivateData.MianNioseSourceBean> getHolder(View v, int viewType) {
        return new NoiseSourceHolder(v,listener);
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
