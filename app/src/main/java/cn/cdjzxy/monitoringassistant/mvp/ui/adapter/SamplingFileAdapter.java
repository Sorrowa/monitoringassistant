package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.SamplingFileHolder;


/**
 * 主页tab
 */

public class SamplingFileAdapter extends DefaultAdapter<SamplingFile> {

    private OnSamplingFileListener onSamplingFileListener;

    public SamplingFileAdapter(List<SamplingFile> infos, OnSamplingFileListener onSamplingFileListener) {
        super(infos);
        this.onSamplingFileListener = onSamplingFileListener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_photo;
    }

    @Override
    public BaseHolder<SamplingFile> getHolder(View v, int viewType) {
        return new SamplingFileHolder(v, onSamplingFileListener);
    }


    public interface OnSamplingFileListener {
        void onChoosePhoto();

        void onDeletePhoto(int position);
    }
}
