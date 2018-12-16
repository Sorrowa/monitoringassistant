package cn.cdjzxy.monitoringassistant.mvp.ui.module.msg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MsgAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

public class MsgActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.btn_read_all)
    TextView     btnReadAll;
    @BindView(R.id.recyclerView_msg)
    RecyclerView recyclerViewMsg;
    @BindView(R.id.tabview)
    CustomTab    tabview;

    private MsgAdapter mMsgAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("消息中心");
        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.makeText(getApplicationContext(), "搜索");
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
        return R.layout.activity_msg;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);
        initTabData();
        initMsgData();
    }


    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        tabview.setTabs("全部", "任务通知", "审核通知", "报警消息");
        tabview.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                ArtUtils.makeText(MsgActivity.this, tab.getTabName());
            }
        });

    }

    /**
     * 初始化Tab数据
     */
    private void initMsgData() {
        ArtUtils.configRecyclerView(recyclerViewMsg, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        List<Tab> mTabs = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Tab tab = new Tab();
            mTabs.add(tab);
        }

        mMsgAdapter = new MsgAdapter(mTabs);
        mMsgAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                ArtUtils.startActivity(MsgDetailActivity.class);
            }
        });
        recyclerViewMsg.setAdapter(mMsgAdapter);
    }


    @OnClick(R.id.btn_read_all)
    public void onClick() {


    }
}
