package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.WanderTaskHolder;

public class WanderTaskAdapter extends DefaultAdapter<ProjectSampleStorage.DataBean> {
    public WanderTaskAdapter(List<ProjectSampleStorage.DataBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<ProjectSampleStorage.DataBean> getHolder(View v, int viewType) {
        return new WanderTaskHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_task;
    }
}
