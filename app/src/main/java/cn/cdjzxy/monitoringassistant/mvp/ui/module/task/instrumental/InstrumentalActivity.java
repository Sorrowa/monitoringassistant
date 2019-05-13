package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
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
import cn.cdjzxy.monitoringassistant.utils.DateUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.NoScrollViewPager;

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
    private boolean isNewCreate;

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
            mSampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(samplingId)).unique();

            //加载详细信息
            List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(InstrumentalActivity.mSampling.getId())).list();
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

                    mSampling.setIsFinish(IsSamplingFinish());
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
        tabview.setTabs("基本信息", "监测结果");
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
        if (viewPager.getCurrentItem() == 2) {
            openFragment(1);
            return;
        }
        finish();
    }




    /**
     * 采样是否完成
     *
     * @return
     */
    public static boolean IsSamplingFinish() {
        return TextUtils.isEmpty(CheckIsSamplingFinish(mSampling));
    }

    /**
     * 采样是否完成
     *
     * @return
     */
    public static String CheckIsSamplingFinish(Sampling sampling) {
        if (CheckUtil.isEmpty(sampling.getSamplingUserName())) {
            return "请选择“监测人”";
        }

        if (CheckUtil.isEmpty(sampling.getSamplingTimeBegin())) {
            return "请选择“分析开始日期”";
        }
        if (CheckUtil.isEmpty(sampling.getSamplingTimeEnd())) {
            return "请选择“分析结束日期”";
        }

        if (CheckUtil.isEmpty(sampling.getMonitemId())) {
            return "请选择“监测项目”";
        }

        if (CheckUtil.isEmpty(sampling.getMethodName())) {
            return "请选择“监测方法”";
        }

        if (CheckUtil.isEmpty(sampling.getDeviceName())) {
            return "请选择“监测设备”";
        }

        if (CheckUtil.isEmpty(sampling.getSamplingDetailYQFs())) {
            return "请添加监测结果";
        }

        for (SamplingDetail detail : sampling.getSamplingDetailYQFs()) {
            if (CheckUtil.isEmpty(detail.getPrivateDataStringValue("SamplingOnTime"))) {
                return String.format("监测结果[%s]需选择分析时间", detail.getSampingCode());//没有填写分析时间
            }
            if (CheckUtil.isEmpty(detail.getPrivateDataStringValue("CaleValue"))) {
                return String.format("监测结果[%s]需填写分析结果", detail.getSampingCode());//没有填写分析结果
            }
            if (CheckUtil.isEmpty(detail.getPrivateDataStringValue("ValueUnit"))) {
                return String.format("监测结果[%s]需填写结果单位", detail.getSampingCode());//没有填写结果单位
            }
        }

        return "";
    }

}
