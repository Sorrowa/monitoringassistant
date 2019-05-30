package cn.cdjzxy.monitoringassistant.mvp.ui.module.wander;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.WanderSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WanderSampleStorageAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.webview.WebActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.webview.WebFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.wander.WanderTaskActivity.INTENT_WANDER_FROM;
import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 流转任务列表详情
 */
public class WanderTaskListDetailActivity extends BaseTitileActivity<ApiPresenter> implements IView {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private int page;//页数
    private boolean isRefresh;//是否刷新
    private String tagId;
    private List<Tags> mTags = new ArrayList<>();
    public static final String INTENT_PROJECT_ID = "intent_project_id";
    private String projectId;
    private String state;
    private List<WanderSampleStorage> list;
    private WanderSampleStorageAdapter adapter;


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wander_task_list_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        page = 1;
        isRefresh = true;
        projectId = getIntent().getStringExtra(INTENT_PROJECT_ID);
        state = getIntent().getStringExtra(INTENT_WANDER_FROM);
        if (CheckUtil.isEmpty(state)) state = "20";//20待流转已流转一起查
        initTabLayout();
        getAllSampleStorageData();
        initRefresh();
    }

    /**
     * 初始化上拉加载，下拉刷新控件
     */
    private void initRefresh() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setEnableHeaderTranslationContent(true);//内容跟随偏移
        mRefreshLayout.setEnableLoadMore(true);//是否启用上拉加载功能
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        list = new ArrayList<>();
        adapter = new WanderSampleStorageAdapter(list);
        mRecyclerView.setAdapter(adapter);
        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page += 1;
                isRefresh = false;
                getSampleStorageForId();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                isRefresh = true;
                getSampleStorageForId();
            }
        });
        adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
//                Intent intent = new Intent();
//
//                intent.setClass(WanderTaskListDetailActivity.this, WanderTaskDetailActivity.class);
//                startActivity(intent);

                Intent intent = new Intent();
                intent.setClass(WanderTaskListDetailActivity.this, WebActivity.class);
                UserInfo user = UserInfoHelper.get().getUserInfo();
                String url = user.getWebUrl() + "/DevExtend/SampleBill?samplingID=" +
                        list.get(position).getSamplingId() + "&userId=" + user.getId();
                intent.putExtra(WebFragment.URL_KEY, url);
                startActivity(intent);
            }
        });
    }

    /**
     * 获取全部的流转任务
     */
    private void getAllSampleStorageData() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("SampleStorageParam.projectId", projectId);//项目id
            map.put("SampleStorageParam.page", page + "");
            map.put("SampleStorageParam.status", state);
            mPresenter.getSampleStorageList(map, Message.obtain(this, new Object()), isRefresh);
        } else {
            showMessage("无网络，请检查您的网络是否正常");
        }
    }

    /**
     * 根据要素id获取流转任务分页清单
     */
    private void getSampleStorageForId() {
        if (NetworkUtil.isNetworkAvailable(this)) {
            showLoading();
            Map<String, String> map = new HashMap<>();
            map.put("SampleStorageParam.projectId", projectId);//项目id
            map.put("SampleStorageParam.tagId", tagId);
            map.put("SampleStorageParam.page", page + "");
            map.put("SampleStorageParam.status", state);
            mPresenter.getSampleStorageList(map, Message.obtain(this, new Object()), isRefresh);
        } else {
            showMessage("无网络，请检查您的网络是否正常");
        }
    }

    private void initTabLayout() {
        Tags allTag = new Tags();
        allTag.setName("全部");
        allTag.setLevel(0);
        allTag.setId("all");
        allTag.setParentId("all");
        tabLayout.addTab(tabLayout.newTab().setText(allTag.getName()));
        mTags.add(allTag);
        List<Tags> tags = DBHelper.get().getTagsDao().loadAll();
        if (!CheckUtil.isEmpty(tags)) {
            for (Tags tag : tags) {
                if (tag.getLevel() == 0) {
                    tabLayout.addTab(tabLayout.newTab().setText(tag.getName()));
                    mTags.add(tag);
                }
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                isRefresh = true;
                page = 1;
                if (tab.getText().equals("全部")) {
                    getAllSampleStorageData();
                } else {
                    tagId = mTags.get(tab.getPosition()).getId();
                    getSampleStorageForId();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(() -> {
            try {
                //拿到tabLayout的mTabStrip属性
                Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                mTabStripField.setAccessible(true);

                LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);

                int dp10 = getResources().getDimensionPixelSize(R.dimen.dp_12);

                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);

                    //拿到tabView的mTextView属性
                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    mTextViewField.setAccessible(true);

                    TextView mTextView = (TextView) mTextViewField.get(tabView);

                    tabView.setPadding(0, 0, 0, 0);

                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    int width = 0;
                    width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }

                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width + getResources().getDimensionPixelSize(R.dimen.dp_8);
                    params.leftMargin = dp10;
                    params.rightMargin = dp10;
                    tabView.setLayoutParams(params);

                    tabView.invalidate();
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        });

    }


    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("流转任务详情");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.addRightAction(titleBar.new ImageAction(R.mipmap.ic_scan_hov, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WanderTaskListDetailActivity.this, WanderScanActivity.class);
                startActivity(intent);
            }
        }));
    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        hideLoading();

        switch (message.what) {
            case Message.RESULT_OK:
                if (list == null) {
                    list = new ArrayList<>();
                } else {
                    list.clear();
                }
                list.addAll((ArrayList<WanderSampleStorage>) message.obj);
                mRefreshLayout.finishRefresh(page);
                adapter.notifyDataSetChanged();
                break;
            case 1001:
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.addAll((ArrayList<WanderSampleStorage>) message.obj);
                mRefreshLayout.finishLoadMore(page);
                adapter.notifyDataSetChanged();
                break;
            case Message.RESULT_FAILURE:
                if (isRefresh) {
                    mRefreshLayout.finishRefresh(page);
                } else {
                    mRefreshLayout.finishLoadMore(page);
                }
                page--;
                showMessage(message.str);
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
