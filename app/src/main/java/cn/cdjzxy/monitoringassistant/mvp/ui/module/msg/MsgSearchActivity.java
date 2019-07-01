package cn.cdjzxy.monitoringassistant.mvp.ui.module.msg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.apache.poi.util.ArrayUtil;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MsgDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MsgAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SearchAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SearchHistoryAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.HawkUtil;

public class MsgSearchActivity extends BaseTitileActivity {

    @BindView(R.id.rv_time)
    RecyclerView rvTime;
    @BindView(R.id.rv_history)
    RecyclerView rvHistory;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.img_search)
    ImageView imgSearch;

    private List<Tab> mLastTimes;
    private List<String> mHistoryList;
    private SearchAdapter mSearchTimeAdapter;
    private SearchHistoryAdapter mSearchHistoryAdapter;
    List<Msg> mMsgList;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_msg_search;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        etSearch.setHint("请输入关键字");
        mLastTimes = new ArrayList<>();
        mHistoryList = new ArrayList<>();
        mMsgList = new ArrayList<>();
        initTimeView();
        initHistoryView();
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

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

    /**
     * 搜素
     *
     * @param s
     */
    private void search(String s) {
        saveHistory(s);
        //设置检测时间
        String startDate = "";
        String endDate = "";
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
        if (!CheckUtil.isEmpty(endDate)) {
            startDate = DateUtils.getDate() + " 00:00:00";
            endDate += " 23:59:59";
        }
        String key = "%" + s + "%";
        Intent intent = new Intent();
        intent.setClass(this, MsgSearchResultActivity.class);
        intent.putExtra("key", key);
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
        startActivity(intent);
    }

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

    private void showMessage(String s) {
        ArtUtils.makeText(this, s);
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

    private void initHistoryView() {
        ArtUtils.configRecyclerView(rvHistory, new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mHistoryList.addAll(HawkUtil.getJsonArray("searchHistory", String.class));
        mSearchHistoryAdapter = new SearchHistoryAdapter(mHistoryList,
                new SearchHistoryAdapter.OnDeleteClickListener() {
                    @Override
                    public void onDelete(int position) {
                        mHistoryList.remove(position);
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
}
