package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.TaskHolder;


/**
 * 主页tab
 */

public class TaskAdapter extends DefaultAdapter<Project> {


    public TaskAdapter(List<Project> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_task;
    }

    @Override
    public BaseHolder<Project> getHolder(View v, int viewType) {
        return new TaskHolder(v);
    }
}
