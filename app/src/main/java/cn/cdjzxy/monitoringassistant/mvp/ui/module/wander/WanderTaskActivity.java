package cn.cdjzxy.monitoringassistant.mvp.ui.module.wander;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;

import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wonders.health.lib.base.base.activity.BaseTitleActivity;
import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WanderTaskAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseActivity;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 流转任务activity 包含流转已收样和 流转待收样界面
 */
public class WanderTaskActivity extends BaseTitleActivity<ApiPresenter> implements IView {
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.header)
    BezierRadarHeader mRefreshHeader;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private WanderTaskAdapter adapter;

    public static final String INTENT_WANDER_FROM = "intent_from";
    public static final String INTENT_FROM_ALREADY = "0";//流转待收样
    public static final String INTENT_FROM_WAIT = "1";//流转已收样
    private String intentWanderFrom;//流转单状态（0待流转，1已流转，10自送样，20待流转已流转一起查）
    private AppComponent mAppComponent;
    private int page = 0;
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

        mTitleBarView.addRightAction(titleBar.new ViewAction(getTitleRightView()));

    }


    private View getTitleRightView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_wander_title_right, null);
        ImageView imgScan=view.findViewById(R.id.img_scan);//二维码扫描
//        ImageView
        return view;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wander_task;
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        list = new ArrayList<>();
     //   mRefreshLayout.autoRefresh();//第一次进入触发自动刷新，演示效果
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setEnableHeaderTranslationContent(true);//内容跟随偏移
        mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new WanderTaskAdapter(list);
        mRecyclerView.setAdapter(adapter);
        initLister();
        getData();


    }

    private void initLister() {
//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//
//            }
//        });
//        mRefreshLayout.setOnLoadMoreListener(new OnRefreshLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//
//            }
//
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//
//            }
//        });
    }

    private void getData() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            Map<String, String> map = new HashMap<>();
            map.put("sampleStorageProjectParam.status", intentWanderFrom);
            map.put("sampleStorageProjectParam.page", page + "");
            mPresenter.getSampleStorageProject(map, Message.obtain(this, new Object()), true);
        } else {
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
        switch (message.what) {
            case Message.RESULT_FAILURE://加载失败
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
                showMessage(message.str);
                break;
            case Message.RESULT_OK://下拉刷新
                mRefreshLayout.finishRefresh();
                if (list != null) {
                    list.clear();
                } else {
                    list = new ArrayList<>();
                }
                if (message.obj != null) {
                    list.addAll((ArrayList<ProjectSampleStorage.DataBean>) message.obj);
                }
                adapter.notifyDataSetChanged();
                break;
            case 1001://上拉加载
                mRefreshLayout.finishLoadMore();
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (message.obj != null) {
                    list.addAll((ArrayList<ProjectSampleStorage.DataBean>) message.obj);
                }
                adapter.notifyDataSetChanged();
                break;
        }
    }


}
