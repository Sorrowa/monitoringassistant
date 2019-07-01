package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

/**
 * 任务列表
 */

public class TaskSearchResultActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.check_box)
    CheckBox checkBox;
    private TaskAdapter mTaskAdapter;

    private String keyword;
    private String startDate;
    private String endDate;
    private ArrayList<String> types;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("搜索结果");
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
        checkBox.setVisibility(View.GONE);
        keyword = getIntent().getStringExtra("keyword");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        types = getIntent().getStringArrayListExtra("types");
        initTaskData();
    }

    /**
     * 初始化Tab数据
     */
    private void initTaskData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this));
        QueryBuilder<Project> queryBuilder = DBHelper.get().getProjectDao().queryBuilder();

        if (CheckUtil.isEmpty(startDate)) {
            if (CheckUtil.isEmpty(types)) {
                queryBuilder.whereOr(ProjectDao.Properties.ProjectNo.like(keyword),
                        ProjectDao.Properties.Name.like(keyword));
            } else {
                queryBuilder.whereOr(ProjectDao.Properties.ProjectNo.like(keyword),
                        ProjectDao.Properties.Type.in(types)
                        , ProjectDao.Properties.Name.like(keyword));
            }
        } else {
            if (CheckUtil.isEmpty(types)) {
                queryBuilder.whereOr(ProjectDao.Properties.ProjectNo.like(keyword),
                        ProjectDao.Properties.PlanEndTime.between(startDate, endDate),
                        ProjectDao.Properties.Name.like(keyword));

            } else {
                queryBuilder.whereOr(ProjectDao.Properties.ProjectNo.like(keyword),
                        ProjectDao.Properties.PlanEndTime.between(startDate, endDate),
                        ProjectDao.Properties.Type.in(types),
                        ProjectDao.Properties.Name.like(keyword));
            }
        }

        final List<Project> projects = queryBuilder.list();
        if (CheckUtil.isNull(projects)) {
            ArtUtils.makeText(this, "未搜索到结果");
        }
        mTaskAdapter = new TaskAdapter(projects);
        mTaskAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent(TaskSearchResultActivity.this, TaskDetailActivity.class);
                intent.putExtra("taskId", projects.get(position).getId());
                ArtUtils.startActivity(intent);
            }
        });
        recyclerview.setAdapter(mTaskAdapter);
    }

}
