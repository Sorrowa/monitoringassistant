package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SearchAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SearchHistoryAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.HawkUtil;

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

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.img_search)
    ImageView imgSearch;

    private List<Tab>    mLastTimes = new ArrayList<>();
    private List<Tab>    mTaskTypes = new ArrayList<>();
    private List<String> mHistorys  = new ArrayList<>();
    private SearchAdapter mSearchTimeAdapter;
    private SearchAdapter mSearchTypeAdapter;

    private SearchHistoryAdapter mSearchHistoryAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
       titleBar.setTitleMainText("任务搜索");
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




    private void initTimeView() {
        ArtUtils.configRecyclerView(rvTime, new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mLastTimes = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            Tab tab = new Tab();
            tab.setResId(R.drawable.shape_search_condition_nor);
            tab.setSelectedResId(R.drawable.shape_search_condition_selected);
            tab.setSelected(false);
            if (i == 0) {
                tab.setTabName("3天内");
            } else if (i == 1) {
                tab.setTabName("7天内");
            } else if (i == 2) {
                tab.setTabName("15天内");
            } else if (i == 3) {
                tab.setTabName("不限");
                tab.setSelected(true);
            }
            mLastTimes.add(tab);
        }

        mSearchTimeAdapter = new SearchAdapter(mLastTimes);
        mSearchTimeAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateLastTimeState(position);
            }
        });
        rvTime.setAdapter(mSearchTimeAdapter);
    }


    private void initTypeView() {

        ArtUtils.configRecyclerView(rvType, new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mTaskTypes = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Tab tab = new Tab();
            tab.setResId(R.drawable.shape_search_condition_nor);
            tab.setSelectedResId(R.drawable.shape_search_condition_selected);
            tab.setSelected(false);
            if (i == 0) {
                tab.setTabName("环境质量监测");
            } else if (i == 1) {
                tab.setTabName("委托监测");
            } else if (i == 2) {
                tab.setTabName("应急监测");
            } else if (i == 3) {
                tab.setTabName("污染源监测");
            } else if (i == 4) {
                tab.setTabName("专项监测");
            }
            mTaskTypes.add(tab);
        }

        mSearchTypeAdapter = new SearchAdapter(mTaskTypes);
        mSearchTypeAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                upateTaskTypeState(position);
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

        mHistorys.addAll(HawkUtil.getJsonArray("searchHistory", String.class));
        mSearchHistoryAdapter = new SearchHistoryAdapter(mHistorys, new SearchHistoryAdapter.OnDeleteClickListener() {
            @Override
            public void onDelete(int position) {
                mHistorys.remove(position);
                mSearchHistoryAdapter.notifyDataSetChanged();
            }
        });
        mSearchHistoryAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                etSearch.setText((String) data);
            }
        });
        rvHistory.setAdapter(mSearchHistoryAdapter);
    }


    private void updateLastTimeState(int position) {
        for (int i = 0; i < mLastTimes.size(); i++) {
            if (i == position) {
                mLastTimes.get(i).setSelected(true);
            } else {
                mLastTimes.get(i).setSelected(false);
            }
        }
        mSearchTimeAdapter.notifyDataSetChanged();
    }

    private void upateTaskTypeState(int position) {
        if (mTaskTypes.get(position).isSelected()) {
            mTaskTypes.get(position).setSelected(false);
        } else {
            mTaskTypes.get(position).setSelected(true);
        }

        mSearchTypeAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.img_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_search:
                hideSoftInput();
                search();
                break;
        }
    }
    /**
     * 搜索
     */
    public void search() {
        if (!CheckUtil.isEmpty(etSearch.getText().toString())) {
            search(etSearch.getText().toString());
        } else {
            showMessage("请输入任务名称或编号");
        }
    }

    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }
    /**
     * 搜素
     *
     * @param keyword
     */
    private void search(String keyword) {
        saveHistory(keyword);
        //设置检测时间
        String startDate="";
        String endDate="";
        for (int i = 0; i < mLastTimes.size(); i++) {
            if (mLastTimes.get(i).isSelected()) {
                if (i == 0) {
                    endDate = DateUtils.getDate(3);
                } else if (i == 1) {
                    endDate = DateUtils.getDate(7);
                } else if (i == 2) {
                    endDate = DateUtils.getDate(15);
                } else {
                    endDate = "";
                }
            }
        }

        if (!CheckUtil.isEmpty(endDate)){
            startDate=DateUtils.getDate()+" 00:00:00";
            endDate += " 23:59:59";
        }


        ArrayList<String> types = new ArrayList<>();
        for (Tab taskType : mTaskTypes) {
            if (taskType.isSelected()) {
                types.add(taskType.getTabName());
            }
        }
        etSearch.setText("");

        Intent intent = new Intent(this, TaskSearchResultActivity.class);
        intent.putExtra("keyword", "%" + keyword + "%");
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
        intent.putStringArrayListExtra("types", types);
        ArtUtils.startActivity(intent);
        hideSoftInput();
    }


    private void saveHistory(String keyword) {
        mHistorys.remove(keyword);

        mHistorys.add(0, keyword);

        if (mHistorys.size() > 5) {
            for (int i = 5; i < mHistorys.size(); i++) {
                mHistorys.remove(i);
            }

        }
        HawkUtil.putJsonArray("searchHistory", mHistorys);

        mSearchHistoryAdapter.notifyDataSetChanged();
    }


}
