package cn.cdjzxy.monitoringassistant.mvp.ui.module.wander;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SearchAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SearchHistoryAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WanderTaskAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskSearchActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.HawkUtil;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 流转任务搜索界面
 */
public class WanderTaskSearchActivity extends BaseTitileActivity<ApiPresenter> implements IView {

    private EditText mEtSearch;
    private ImageView mBtnSearch;
    @BindView(R.id.rv_type)
    RecyclerView rvType;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<Tab> mTaskTypes;
    private List<String> mHistoryList;
    private List<ProjectSampleStorage.DataBean> wanderList;
    private SearchAdapter mSearchTypeAdapter;
    private SearchHistoryAdapter mSearchHistoryAdapter;
    private WanderTaskAdapter wanderAdapter;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wander_task_search;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mTaskTypes = new ArrayList<>();
        mHistoryList = new ArrayList<>();
        wanderList = new ArrayList<>();
        initTypeView();
        initHistoryView();
        initWanderView();
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
        titleBar.addCenterAction(titleBar.new ViewAction(getSearchView()));
    }

    /**
     * 获取标题右边View
     *
     * @return
     */
    private View getSearchView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_search, null);
        mEtSearch = view.findViewById(R.id.et_search);
        mBtnSearch = view.findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckUtil.isEmpty(mEtSearch.getText().toString())) {
                    Map<String, String> map = new HashMap<>();
                    map.put("sampleStorageProjectParam.keyWord", mEtSearch.getText().toString());
                    map.put("sampleStorageProjectParam.status", 20 + "");
                    search(map);
                    saveHistory(mEtSearch.getText().toString());
                    //search(mEtSearch.getText().toString());
                } else {
                    ArtUtils.makeText(WanderTaskSearchActivity.this, "请输入任务名称或编号");
                }
            }
        });
        return view;
    }

    private void initTypeView() {

        ArtUtils.configRecyclerView(rvType, new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mTaskTypes = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Tab tab = new Tab();
            tab.setResId(R.drawable.shape_project_item);
            tab.setSelectedResId(R.drawable.shape_project_item);
            tab.setSelected(false);
            if (i == 0) {
                tab.setTabName("委托监测");
            } else if (i == 1) {
                tab.setTabName("污染源监测");
            } else if (i == 2) {
                tab.setTabName("环境质量");
            } else if (i == 3) {
                tab.setTabName("应急监测");
            } else if (i == 4) {
                tab.setTabName("专项监测");
            } else if (i == 5) {
                tab.setTabName("辐射监测");
            }
            mTaskTypes.add(tab);
        }

        mSearchTypeAdapter = new SearchAdapter(mTaskTypes);
        mSearchTypeAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Map<String, String> map = new HashMap<>();
                map.put("sampleStorageProjectParam.type", position + 1 + "");
                map.put("sampleStorageProjectParam.status", 20 + "");
                search(map);
            }
        });
        rvType.setAdapter(mSearchTypeAdapter);
    }

    private void initHistoryView() {
        ArtUtils.configRecyclerView(rvHistory, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mHistoryList.addAll(HawkUtil.getJsonArray("searchHistory", String.class));
        mSearchHistoryAdapter = new SearchHistoryAdapter(mHistoryList, new SearchHistoryAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(int position) {
                mHistoryList.remove(position);
                mSearchHistoryAdapter.notifyDataSetChanged();
            }
        });
        mSearchHistoryAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                mEtSearch.setText((String) data);
            }
        });
        rvHistory.setAdapter(mSearchHistoryAdapter);
    }

    /**
     * 搜索后的界面
     */
    private void initWanderView() {
        wanderList = new ArrayList<>();
        wanderAdapter = new WanderTaskAdapter(wanderList);
        wanderAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.setClass(WanderTaskSearchActivity.this, WanderTaskListDetailActivity.class);
                intent.putExtra(WanderTaskListDetailActivity.INTENT_PROJECT_ID, wanderList.get(position).getId());
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(wanderAdapter);
    }

    /**
     * 搜索
     *
     * @param map
     */
    private void search(Map<String, String> map) {
        showLoading();
        if (NetworkUtil.isNetworkAvailable(this)) {
            showLoading();
            mPresenter.getSampleStorageProject(map, Message.obtain(this, new Object()), true);
        } else {
            showMessage("无网络，请检查您的网络是否正常");
        }
    }

    /**
     * 保存历史记录
     *
     * @param keyword
     */
    private void saveHistory(String keyword) {
        mHistoryList.remove(keyword);

        mHistoryList.add(0, keyword);

        if (mHistoryList.size() > 5) {
            for (int i = 5; i < mHistoryList.size(); i++) {
                mHistoryList.remove(i);
            }

        }
        HawkUtil.putJsonArray("searchHistory", mHistoryList);
        mSearchHistoryAdapter.notifyDataSetChanged();
    }


    @Override
    public void showLoading() {
        showLoadingDialog("正在查找请稍后。。。", false);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
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
                ProjectSampleStorage sampleStorage = (ProjectSampleStorage) message.obj;
                if (sampleStorage != null) {
                    if (wanderList != null) {
                        wanderList.clear();
                    } else {
                        wanderList = new ArrayList<>();
                    }
                    wanderList.addAll(sampleStorage.getData());
                    wanderAdapter.notifyDataSetChanged();
                }
                if (wanderList == null || wanderList.size() == 0) {
                    showMessage("未搜索到结果");
                }
                break;
            case Message.RESULT_FAILURE://加载失败
                showMessage(message.str);
                break;
        }
    }
}
