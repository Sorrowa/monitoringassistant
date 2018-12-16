package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.project;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.ProjectAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.widgets.GridItemDecoration;

/**
 * 监测项目
 */

public class ProjectActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.rv_project)
    RecyclerView rvProject;
    @BindView(R.id.rv_project_selected)
    RecyclerView rvProjectSelected;

    private ProjectAdapter mProjectAdapter;

    private ProjectAdapter mProjectAdapter1;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("监测项目");
        //        titleBar.addCenterAction(titleBar.new ViewAction(getSearchView()));
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_project;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);
        initTimeView();
        initTypeView();

    }

    /**
     * 获取标题右边View
     *
     * @return
     */
    private View getSearchView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_search, null);


        return view;
    }


    private void initTimeView() {
        ArtUtils.configRecyclerView(rvProject, new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });


        List<Tab> mTabs = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            mTabs.add(new Tab());
        }

        mProjectAdapter = new ProjectAdapter(mTabs);
        mProjectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {

            }
        });
        rvProject.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProject.setAdapter(mProjectAdapter);
    }


    private void initTypeView() {

        ArtUtils.configRecyclerView(rvProjectSelected, new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        List<Tab> mTabs = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            mTabs.add(new Tab());
        }

        mProjectAdapter1 = new ProjectAdapter(mTabs);
        mProjectAdapter1.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {

            }
        });
        rvProjectSelected.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProjectSelected.setAdapter(mProjectAdapter1);
    }

}
