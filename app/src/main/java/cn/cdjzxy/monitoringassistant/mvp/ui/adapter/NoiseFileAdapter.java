package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseSamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.NoiseFileHolder;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.SamplingFileHolder;

public class NoiseFileAdapter extends DefaultAdapter<NoiseSamplingFile> {

    private ItemClickListener itemClickLinter;

    public NoiseFileAdapter(List<NoiseSamplingFile> infos, ItemClickListener listener) {
        super(infos);
        this.itemClickLinter = listener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_noise_photo;
    }

    @Override
    public BaseHolder<NoiseSamplingFile> getHolder(View v, int viewType) {
        return new NoiseFileHolder(v,itemClickLinter);
    }

    public interface ItemClickListener {
        void selectLister(int p, View v);

        void itemLister(int p, View view);
    }
}
