package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.TaskDetailHolder;


/**
 * 主页tab
 */

public class TaskDetailAdapter extends DefaultAdapter<Sampling> {

    private OnSamplingListener onSamplingListener;

    public TaskDetailAdapter(List<Sampling> infos, OnSamplingListener onSamplingListener) {
        super(infos);
        this.onSamplingListener = onSamplingListener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_task_detail;
    }

    @Override
    public BaseHolder<Sampling> getHolder(View v, int viewType) {
        return new TaskDetailHolder(v, onSamplingListener);
    }


    public interface OnSamplingListener {
        void onSelected(View view, int position);

        void onClick(View view, int position);

        void onUpload(View view, int position);
    }
}
