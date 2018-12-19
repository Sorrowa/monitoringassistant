package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PointAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

/**
 * 采样点位
 */

public class PointActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private String projectId;
    private List<ProjectDetial> mProjectDetials = new ArrayList<>();
    private PointAdapter mPointAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("采样点位");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_point;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        projectId = getIntent().getStringExtra("projectId");
        initPointData();
        getData();
    }

    /**
     * 初始化Tab数据
     */
    private void initPointData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this));
        mPointAdapter = new PointAdapter(this, mProjectDetials);
        mPointAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent(PointActivity.this, ProgramModifyActivity.class);
                intent.putExtra("projectDetailId", mProjectDetials.get(position).getId());
                ArtUtils.startActivity(intent);
            }
        });
        recyclerview.setAdapter(mPointAdapter);
    }


    @Subscriber(tag = EventBusTags.TAG_PROGRAM_MODIFY)
    private void updateData(boolean isModified) {
        getData();
    }

    private void getData() {
        mProjectDetials.clear();
        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId)).list();
        if (!CheckUtil.isEmpty(projectDetials)) {
            mProjectDetials.addAll(projectDetials);
        }
        mPointAdapter.notifyDataSetChanged();
    }

}
