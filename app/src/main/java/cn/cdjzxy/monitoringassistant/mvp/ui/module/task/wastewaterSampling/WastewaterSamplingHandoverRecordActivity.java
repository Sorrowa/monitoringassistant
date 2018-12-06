package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewaterSampling;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.Tab;
import cn.cdjzxy.monitoringassistant.mvp.presenter.AppPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.RecordTabAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.scanCode.ScanCodeFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewaterSampling.fragment.BasicInformationFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewaterSampling.fragment.BottleSplitInformationFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewaterSampling.fragment.SampleCollectionFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewaterSampling.fragment.SiteMonitoringFragment;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class WastewaterSamplingHandoverRecordActivity extends BaseTitileActivity<AppPresenter> implements IView {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_container)
    FrameLayout  layoutContainer;

    private List<Tab>        mTabs;
    private RecordTabAdapter mRecordTabAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.getLinearLayout(Gravity.LEFT).removeViewAt(1);
        titleBar.setTitleMainText("水和废水采样及交接记录");
        titleBar.setTitleMainTextColor(Color.WHITE);
        titleBar.setActionPadding(24);
        titleBar.addRightAction(titleBar.new ImageAction(R.mipmap.ic_save, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.makeText(getApplicationContext(), "保存");
            }
        }));

        titleBar.addRightAction(titleBar.new ImageAction(R.mipmap.ic_print, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.makeText(getApplicationContext(), "打印");
            }
        }));
    }

    @Nullable
    @Override
    public AppPresenter obtainPresenter() {
        return new AppPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wastewater_sampling_record;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);
        initTabData();
        openFragment(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        switch (message.what) {
            case 0:

                break;
            case Message.RESULT_OK:

                break;
        }
    }

    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        ArtUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return false;
            }
        });

        mTabs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Tab tab = new Tab();
            if (i == 0) {
                tab.setTabName("基本信息");
                tab.setSelected(true);
            } else if (i == 1) {
                tab.setTabName("样品采集");
                tab.setSelected(false);
            } else if (i == 2) {
                tab.setTabName("现场检测");
                tab.setSelected(false);
            } else if (i == 3) {
                tab.setTabName("分瓶信息");
                tab.setSelected(false);
            }
            mTabs.add(tab);
        }

        mRecordTabAdapter = new RecordTabAdapter(mTabs);
        mRecordTabAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateTabStatus(position);
                openFragment(position);

            }
        });
        recyclerView.setAdapter(mRecordTabAdapter);
    }

    /**
     * 更新tabView选中状态
     *
     * @param position
     */
    private void updateTabStatus(int position) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (i == position) {
                mTabs.get(i).setSelected(true);
            } else {
                mTabs.get(i).setSelected(false);
            }
        }
        mRecordTabAdapter.notifyDataSetChanged();

    }

    /**
     * 切换Fragment页面
     *
     * @param position
     */
    private void openFragment(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle mBundle = new Bundle();
        switch (position) {
            case 0://基本信息
                ft.replace(R.id.layout_container, new BasicInformationFragment(), BasicInformationFragment.class.getName());
                break;
            case 1://样品采集
                ft.replace(R.id.layout_container, new SampleCollectionFragment(), SampleCollectionFragment.class.getName());
                break;
            case 2://现场监测
                ft.replace(R.id.layout_container, new SiteMonitoringFragment(), SiteMonitoringFragment.class.getName());
                break;
            case 3://分瓶信息
                ft.replace(R.id.layout_container, new BottleSplitInformationFragment(), BottleSplitInformationFragment.class.getName());
                break;
            default:
                break;
        }
        ft.commit();
    }


}
