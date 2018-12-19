package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

/**
 * 任务列表
 */

public class TaskActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private TaskAdapter mTaskAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("采样任务");
        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(TaskSearchActivity.class);
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_task;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initTaskData();
    }

    /**
     * 初始化Tab数据
     */
    private void initTaskData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this));

        final List<Project> projects = DBHelper.get().getProjectDao().queryBuilder().list();
        if (!CheckUtil.isEmpty(projects)) {
            mTaskAdapter = new TaskAdapter(projects);
            mTaskAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int viewType, Object data, int position) {
                    Intent intent = new Intent(TaskActivity.this, TaskDetailActivity.class);
                    intent.putExtra("taskId", projects.get(position).getId());
                    ArtUtils.startActivity(intent);
                }
            });
            recyclerview.setAdapter(mTaskAdapter);
        }
    }

}
