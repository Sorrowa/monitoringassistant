package cn.cdjzxy.monitoringassistant.mvp.ui.module;

import android.graphics.Color;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.BuildConfig;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.AppPresenter;
import cn.cdjzxy.monitoringassistant.mvp.presenter.BasicDataPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MainTabAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.management.ManagementFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.msg.MsgActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.pointMap.PointMapFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.progress.ProgressFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.repository.RepositoryFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.scanCode.ScanCodeFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.setting.PwdModifyActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.setting.SettingFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.webview.WebFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.ExitHelper;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class MainActivity extends BaseTitileActivity<BasicDataPresenter> implements IView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_container)
    FrameLayout  layoutContainer;
    private TitleBarView titleBar;

    private ExitHelper.TwicePressHolder mExitHelper;

    private MainTabAdapter mMainTabAdapter;
    private List<Tab>      mTabs;

    private FragmentManager mFragmentManager;

    private WebFragment        mWebFragment;
    private TaskFragment       mTaskFragment;
    private ScanCodeFragment   mScanCodeFragment;
    private PointMapFragment   mPointMapFragment;
    private ProgressFragment   mProgressFragment;
    private RepositoryFragment mRepositoryFragment;
    private SettingFragment    mSettingFragment;
    private ManagementFragment mManagementFragment;


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        this.titleBar = titleBar;
        titleBar.getLinearLayout(Gravity.LEFT).removeViewAt(1);
        titleBar.setLeftVisible(false);
        titleBar.setTitleMainText("首页");
        titleBar.setTitleMainTextColor(Color.WHITE);
        titleBar.addLeftAction(titleBar.new ViewAction(getTitleLeftView()));
        titleBar.addRightAction(titleBar.new ViewAction(getTitleRightView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(MsgActivity.class);
            }
        }));

    }

    @Nullable
    @Override
    public BasicDataPresenter obtainPresenter() {
        return new BasicDataPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);
        mFragmentManager = this.getSupportFragmentManager();
        initTabData();
        openFragment(0);
        titleBar.setTitleMainText("嘉泽云监测");

        // 双击退出
        mExitHelper = new ExitHelper.TwicePressHolder(new ExitHelper.IExitInterface() {

            @Override
            public void showExitTip() {
                ArtUtils.makeText(getApplicationContext(), "再按一次退出程序");

            }

            @Override
            public void exit() {
                finish();
                ArtUtils.exitApp();
            }
        }, 3000);

        mPresenter.getDevices(Message.obtain(this, new Object()));
        mPresenter.getMethods(Message.obtain(this, new Object()));
        mPresenter.getMonItems(Message.obtain(this, new Object()));
        mPresenter.getTags(Message.obtain(this, new Object()));
        mPresenter.GetMonItemTagRelation(Message.obtain(this, new Object()));
        mPresenter.GetMethodTagRelation(Message.obtain(this, new Object()));
        mPresenter.GetMonItemMethodRelation(Message.obtain(this, new Object()));
        mPresenter.GetMethodDevRelation(Message.obtain(this, new Object()));
        mPresenter.GetRight(Message.obtain(this, new Object()));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mExitHelper.onKeyDown(keyCode, event);
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
     * 获取标题右边View
     *
     * @return
     */
    private View getTitleRightView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_main_title_right, null);


        return view;
    }

    /**
     * 获取标题左边View
     *
     * @return
     */
    private View getTitleLeftView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_main_title_left, null);
        TextView mTvUserName = view.findViewById(R.id.tv_username);
        TextView mTvDate = view.findViewById(R.id.tv_date);
        String userName = UserInfoHelper.get().getUserName();
        mTvUserName.setText(CheckUtil.isEmpty(userName) ? "测试数据" : userName + ", 欢迎你");
        mTvDate.setText(DateUtils.getDate() + " " + DateUtils.getWeek());
        return view;
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

        for (int i = 0; i < 7; i++) {
            Tab tab = new Tab();
            if (i == 0) {
                tab.setTabName("任务");
                tab.setResId(R.mipmap.ic_task_nor);
                tab.setSelectedResId(R.mipmap.ic_task_hov);
                tab.setSelected(true);
            } else if (i == 1) {
                tab.setTabName("扫码");
                tab.setResId(R.mipmap.ic_scan_nor);
                tab.setSelectedResId(R.mipmap.ic_scan_hov);
                tab.setSelected(false);
            } else if (i == 2) {
                tab.setTabName("点位地图");
                tab.setResId(R.mipmap.ic_point_nor);
                tab.setSelectedResId(R.mipmap.ic_point_hov);
                tab.setSelected(false);
            } else if (i == 3) {
                tab.setTabName("企业管理");
                tab.setResId(R.mipmap.ic_manager_nor);
                tab.setSelectedResId(R.mipmap.ic_manager_hov);
                tab.setSelected(false);
            } else if (i == 4) {
                tab.setTabName("知识库");
                tab.setResId(R.mipmap.ic_knowledge_nor);
                tab.setSelectedResId(R.mipmap.ic_knowledge_hov);
                tab.setSelected(false);
            } else if (i == 5) {
                tab.setTabName("进度");
                tab.setResId(R.mipmap.ic_progress_nor);
                tab.setSelectedResId(R.mipmap.ic_progress_hov);
                tab.setSelected(false);
            } else if (i == 6) {
                tab.setTabName("设置");
                tab.setResId(R.mipmap.ic_setting_nor);
                tab.setSelectedResId(R.mipmap.ic_setting_hov);
                tab.setSelected(false);
            }

            mTabs.add(tab);
        }

        mMainTabAdapter = new MainTabAdapter(mTabs);
        mMainTabAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateTitle(position);
                updateTabStatus(position);
                openFragment(position);

            }
        });
        recyclerView.setAdapter(mMainTabAdapter);
    }

    /**
     * 更新标题
     *
     * @param position
     */
    private void updateTitle(int position) {
        if (position == 0) {
            titleBar.setTitleMainText("嘉泽云监测");
        } else {
            titleBar.setTitleMainText(mTabs.get(position).getTabName());
        }

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
        mMainTabAdapter.notifyDataSetChanged();

    }

    /**
     * 切换Fragment页面
     *
     * @param position
     */
    private void openFragment(int position) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Bundle mBundle = new Bundle();
        switch (position) {

            case 0://任务
                ft.replace(R.id.layout_container, mTaskFragment = new TaskFragment(), TaskFragment.class.getName());
                break;

            case 1://扫码
                ft.replace(R.id.layout_container, mScanCodeFragment = new ScanCodeFragment(), ScanCodeFragment.class.getName());
                break;

            case 2://点位地图
                ft.replace(R.id.layout_container, mWebFragment = WebFragment.getInstance(mBundle), WebFragment.class.getName());
                //                mBundle.putString(WebFragment.URL_KEY, BuildConfig.SERVER_IP + "/GIS/APPGIS");
                mBundle.putString(WebFragment.URL_KEY, "https://www.amap.com/");
                break;

            case 3://企业管理

                ft.replace(R.id.layout_container, mWebFragment = WebFragment.getInstance(mBundle), WebFragment.class.getName());
                //                mBundle.putString(WebFragment.URL_KEY, BuildConfig.SERVER_IP + "/Enterprise/APP");
                mBundle.putString(WebFragment.URL_KEY, "https://www.baidu.com");
                break;

            case 4://知识库
                ft.replace(R.id.layout_container, mRepositoryFragment = new RepositoryFragment(), RepositoryFragment.class.getName());
                break;

            case 5://进度
                ft.replace(R.id.layout_container, mProgressFragment = new ProgressFragment(), ProgressFragment.class.getName());
                break;

            case 6://设置
                ft.replace(R.id.layout_container, mSettingFragment = new SettingFragment(), SettingFragment.class.getName());
                break;
            default:
                break;
        }
        ft.commit();
    }


}
