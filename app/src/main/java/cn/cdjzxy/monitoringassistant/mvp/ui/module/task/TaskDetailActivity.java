package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskDetailAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;

/**
 * 任务详情
 */

public class TaskDetailActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tab_layout)
    TabLayout    tabLayout;

    private TaskDetailAdapter mTaskDetailAdapter;

    private String[] titles = new String[]{"全部", "水质", "空气", "废气", "降水", "固废", "土壤", "噪声"};

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("XX采样任务");
        titleBar.setRightText("采样完结");
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        return R.layout.activity_task_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);
        initTabLayout();
        initTaskData();
    }

    private void initTabLayout() {

        for (String title : titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }

//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                IndicatorLineUtil.setIndicator(tabLayout, 8, 8);
//            }
//        });


    }

    /**
     * 初始化Tab数据
     */
    private void initTaskData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        List<Tab> mTabs = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mTabs.add(new Tab());
        }

        mTaskDetailAdapter = new TaskDetailAdapter(mTabs);
        mTaskDetailAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                ArtUtils.startActivity(PrecipitationActivity.class);
            }
        });
        recyclerview.setAdapter(mTaskDetailAdapter);
    }

    @OnClick({R.id.btn_sampling_point, R.id.btn_add_sampling})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sampling_point:
                ArtUtils.startActivity(PointActivity.class);
                break;
            case R.id.btn_add_sampling:

                break;
        }
    }
}
