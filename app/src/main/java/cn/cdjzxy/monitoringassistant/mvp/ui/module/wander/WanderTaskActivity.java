package cn.cdjzxy.monitoringassistant.mvp.ui.module.wander;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WanderTaskAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 流转列表 包含流转已收样{@INTENT_FROM_WAIT} 和 流转待收样{@INTENT_FROM_ALREADY}界面
 */
public class WanderTaskActivity extends BaseTitileActivity<ApiPresenter> implements IView {
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.header)
    BezierRadarHeader mRefreshHeader;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private WanderTaskAdapter adapter;

    public static final String INTENT_WANDER_FROM = "intent_from";
    public static final String INTENT_FROM_WAIT = "0";//流转待收样
    public static final String INTENT_FROM_ALREADY = "1";//流转已收样
    private String intentWanderFrom;//流转单状态（0待流转，1已流转，10自送样，20待流转已流转一起查）
    private int page = 1;
    private boolean isRefresh = true;
    private List<ProjectSampleStorage.DataBean> list;
    private TitleBarView mTitleBarView;


    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        if (getIntent() != null) {
            intentWanderFrom = getIntent().getStringExtra(INTENT_WANDER_FROM);
        } else {
            intentWanderFrom = "0";
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        this.mTitleBarView = titleBar;
        if (intentWanderFrom.equals(INTENT_FROM_ALREADY)) {
            mTitleBarView.setTitleMainText("流转任务");
        } else if (intentWanderFrom.equals(INTENT_FROM_WAIT)) {
            mTitleBarView.setTitleMainText("收样记录");
        }
        mTitleBarView.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_scan_hov, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WanderTaskActivity.this, WanderScanActivity.class);
                startActivity(intent);
            }
        }));
        mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_search_white, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(INTENT_WANDER_FROM, intentWanderFrom);
                intent.setClass(WanderTaskActivity.this, WanderTaskSearchActivity.class);
                startActivity(intent);
            }
        }));
    }


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wander_task;
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        list = new ArrayList<>();
        page = 1;
        isRefresh = true;
        showLoading();
        getData();//第一次不执行刷新动画，直接请求数据
        //mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setEnableHeaderTranslationContent(true);//内容跟随偏移
        mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new WanderTaskAdapter(list);
        mRecyclerView.setAdapter(adapter);
        initLister();

    }

    private void initLister() {
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page += 1;
                isRefresh = false;
                getData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                isRefresh = true;
                getData();
            }
        });
        adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.setClass(WanderTaskActivity.this, WanderTaskListDetailActivity.class);
                intent.putExtra(WanderTaskListDetailActivity.INTENT_PROJECT_ID, list.get(position).getId());
                intent.putExtra(INTENT_WANDER_FROM, intentWanderFrom);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            Map<String, String> map = new HashMap<>();
            map.put("sampleStorageProjectParam.status", intentWanderFrom);
            map.put("sampleStorageProjectParam.page", page + "");
            mPresenter.getSampleStorageProject(map, Message.obtain(this, new Object()), isRefresh);
        } else {
            hideLoading();
            showMessage("无网络，请检查您的网络是否正常");
        }
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        hideLoading();
        ProjectSampleStorage sampleStorage;
        switch (message.what) {
            case Message.RESULT_FAILURE://加载失败
                if (isRefresh) {
                    mRefreshLayout.finishRefresh(page);
                } else {
                    mRefreshLayout.finishLoadMore(page);
                }
                page--;
                showMessage(message.str);
                break;
            case Message.RESULT_OK://下拉刷新
                sampleStorage = (ProjectSampleStorage) message.obj;
                mRefreshLayout.finishRefresh(page);
                if (list != null) {
                    list.clear();
                } else {
                    list = new ArrayList<>();
                }
                if (sampleStorage != null) {
                    list.addAll(sampleStorage.getData());
                    adapter.notifyDataSetChanged();
                    if (page == sampleStorage.getPagerInfo().getPageSize()) {
                        mRefreshLayout.setEnableLoadMore(false);
                    }
                }
                break;
            case 1001://上拉加载
                sampleStorage = (ProjectSampleStorage) message.obj;
                mRefreshLayout.finishLoadMore(page);
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (sampleStorage != null) {
                    list.addAll(sampleStorage.getData());
                    adapter.notifyDataSetChanged();
                    if (page == sampleStorage.getPagerInfo().getPageSize()) {//没有下一页了
                        mRefreshLayout.setEnableLoadMore(false);
                    }
                }
                break;
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog("正在加载请稍后。。。", false);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }
}
