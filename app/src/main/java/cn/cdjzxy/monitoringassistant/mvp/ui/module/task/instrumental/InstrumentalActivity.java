package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetailYQFs;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment.BasicInfoFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment.TestRecordDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment.TestRecordFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.FormPrintActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.NoScrollViewPager;

/**
 * 现场监测仪器法
 */
public class InstrumentalActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.tabview)
    CustomTab tabview;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    private String projectId;
    private String formSelectId;
    private String samplingId;
    public static boolean isNewCreate;

    private List<Fragment> mFragments;
    private FragmentAdapter mFragmentAdapter;

    private BasicInfoFragment mBasicFragment;
    private TestRecordFragment mTestRecordFragment;
    private TestRecordDetailFragment mTestRecordDetailFragment;

    public static Sampling mSampling;

    private TitleBarView mTitleBarView;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mTitleBarView = titleBar;
        mTitleBarView.setTitleMainText("仪器法原始记录");
        mTitleBarView.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
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
        return R.layout.activity_instrumental;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragments.get(viewPager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        layout.scrollTo(0, StatusBarUtil.getStatusBarHeight(this));
        projectId = getIntent().getStringExtra("projectId");
        formSelectId = getIntent().getStringExtra("formSelectId");
        samplingId = getIntent().getStringExtra("samplingId");
        isNewCreate = getIntent().getBooleanExtra("isNewCreate", false);

        if (isNewCreate) {
            //是否是新建
            mSampling = SamplingUtil.createInstrumentalSampling(projectId, formSelectId);
        } else {
            //从数据库加载
            Sampling dbSample = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(samplingId)).unique();
            Gson gson = new Gson();
            String clone = gson.toJson(dbSample);
            mSampling = gson.fromJson(clone, Sampling.class);
            //加载详细信息
            List<SamplingDetailYQFs> samplingDetails = DbHelpUtils.getSamplingDetailYQFsList(mSampling.getId());
            mSampling.setSamplingDetailYQFs(samplingDetails);
        }

        if (mSampling.getIsCanEdit()) {
            //可编辑
            mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_print, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArtUtils.startActivity(FormPrintActivity.class);
                }
            }));
            mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_save, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    mSampling.setFormType("d877c7d5-6bb8-42d4-a79c-0f644e130a62");
//                    mSampling.setFormTypeName("水质");
//                    mSampling.setAddTime("2019-01-23 21:24:51");
//                    mSampling.setMonitorPerson("Admin");
//                    Project project = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
//                    mSampling.setProjectNo(project.getProjectNo());
//                    mSampling.setAddressNo("");
                    if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
                        showNoPermissionDialog("才能进行表单保存。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
                        return;
                    }
                    mSampling.setIsFinish((SamplingUtil.isSamplingFinsh(mSampling)));
                    mSampling.setStatusName(mSampling.getIsFinish() ? "已完成" : "进行中");

                    if (isNewCreate) {
                        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().
                                where(SamplingDao.Properties.Id.eq(mSampling.getId())).unique();
                        if (CheckUtil.isNull(sampling)) {
                            DBHelper.get().getSamplingDao().insert(mSampling);
                        } else {
                            DBHelper.get().getSamplingDao().update(mSampling);
                        }
                        isNewCreate = false;
                    } else {
                        DBHelper.get().getSamplingDao().update(mSampling);
                    }
                    List<SamplingDetailYQFs> dbYQList = DbHelpUtils.getSamplingDetailYQFsList(mSampling.getId());
                    if (CheckUtil.isEmpty(dbYQList)) {
                        DBHelper.get().getSamplingDetailYQFsDao().deleteInTx(dbYQList);
                    }

                    if (!CheckUtil.isEmpty(mSampling.getSamplingDetailYQFs())) {
                        for (SamplingDetailYQFs yqFs : mSampling.getSamplingDetailYQFs()) {
                            SamplingDetailYQFs dbYqfs = DbHelpUtils.getSamplingDetailYQFs(yqFs.getId());
                            if (dbYqfs != null) {
                                DBHelper.get().getSamplingDetailYQFsDao().update(yqFs);
                            } else {
                                DBHelper.get().getSamplingDetailYQFsDao().insert(yqFs);
                            }
                        }

                    }
                    EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                    ArtUtils.makeText(getApplicationContext(), "数据保存成功");
                }
            }));
        }

        initTabData();
        openFragment(0);
    }

    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        List<Tab> tabs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Tab tab = new Tab();
            if (i == 0) {
                tab.setTabName("基本信息");
                tab.setSelected(true);
                tab.setResId(R.mipmap.icon_basic);
            } else if (i == 1) {
                tab.setTabName("监测结果");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_monitor_result);
            }
            tabs.add(tab);
        }
        tabview.setTabs(tabs);
        tabview.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                openFragment(position);
            }
        });

        mBasicFragment = new BasicInfoFragment();
        mTestRecordFragment = new TestRecordFragment();
        mTestRecordDetailFragment = new TestRecordDetailFragment();

        mFragments = new ArrayList<>();
        mFragments.add(mBasicFragment);
        mFragments.add(mTestRecordFragment);
        mFragments.add(mTestRecordDetailFragment);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(mFragmentAdapter);
        viewPager.setOffscreenPageLimit(3);
    }

    /**
     * 切换Fragment页面
     *
     * @param position
     */
    private void openFragment(int position) {
        hideSoftInput();
        viewPager.setCurrentItem(position);
    }


    @Subscriber(tag = EventBusTags.TAG_INSTRUMENTAL_RECORD)
    private void updateCollectFragment(int position) {
        openFragment(position);
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        hideSoftInput();
        if (viewPager.getCurrentItem() == 2) {
            openFragment(1);
            return;
        }
        finish();
    }
}
