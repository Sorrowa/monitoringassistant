package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SearchAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SearchHistoryAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

/**
 * 任务搜索
 */

public class TaskSearchActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.rv_time)
    RecyclerView rvTime;
    @BindView(R.id.rv_type)
    RecyclerView rvType;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;

    private SearchAdapter mSearchAdapter;

    private SearchAdapter mSearchAdapter1;

    private SearchHistoryAdapter mSearchHistoryAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.addCenterAction(titleBar.new ViewAction(getSearchView()));
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_task_search;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initTimeView();
        initTypeView();
        initHistoryView();
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
        ArtUtils.configRecyclerView(rvTime, new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        List<Tab> mTabs = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            mTabs.add(new Tab());
        }

        mSearchAdapter = new SearchAdapter(mTabs);
        mSearchAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {

            }
        });
        rvTime.setAdapter(mSearchAdapter);
    }


    private void initTypeView() {

        ArtUtils.configRecyclerView(rvType, new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        List<Tab> mTabs = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            mTabs.add(new Tab());
        }

        mSearchAdapter1 = new SearchAdapter(mTabs);
        mSearchAdapter1.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {

            }
        });
        rvType.setAdapter(mSearchAdapter1);
    }

    private void initHistoryView() {
        ArtUtils.configRecyclerView(rvHistory, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        List<Tab> mTabs = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            mTabs.add(new Tab());
        }

        mSearchHistoryAdapter = new SearchHistoryAdapter(mTabs);
        mSearchHistoryAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {

            }
        });
        rvHistory.setAdapter(mSearchHistoryAdapter);
    }


}
