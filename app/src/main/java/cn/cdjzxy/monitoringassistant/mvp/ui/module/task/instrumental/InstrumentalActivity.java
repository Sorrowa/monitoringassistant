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
            mSampling = createSampling();
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
                    mSampling.setIsFinish(IsSamplingFinish());
                    mSampling.setStatusName(mSampling.getIsFinish() ? "已完成" : "进行中");
                    if (isNewCreate) {
                        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSampling.getId())).unique();
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

    private Sampling createSampling() {
        Project project = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId)).list();

        FormSelect formSelect = DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.FormId.eq(formSelectId)).unique();

        Sampling sampling = new Sampling();
        sampling.setId("LC-" + UUID.randomUUID().toString());
        sampling.setSamplingNo(createSamplingNo());
        sampling.setProjectId(project.getId());
        sampling.setProjectName(project.getName());
        sampling.setProjectNo(project.getProjectNo());
        sampling.setTagId(formSelect.getTagId());
        sampling.setMontype(project.getTypeCode() + "");
        sampling.setTagId(project.getMonType());//TODO:
        sampling.setTagName(project.getMonType());
        sampling.setAddressNo("");
        sampling.setFormType(formSelect.getTagParentId());
        sampling.setFormTypeName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagParentId())).unique().getName());
        sampling.setFormName(formSelect.getFormName());
        sampling.setFormPath(formSelect.getPath());
        sampling.setParentTagId(formSelect.getTagParentId());
        sampling.setStatus(0);
        sampling.setSamplingUserId(UserInfoHelper.get().getUser().getId());
        sampling.setSamplingUserName(UserInfoHelper.get().getUser().getName());
        sampling.setMonitorPerson(UserInfoHelper.get().getUser().getName());
        sampling.setSamplingTimeBegin(DateUtils.getDate());
        sampling.setSamplingTimeEnd(DateUtils.getDate());
        sampling.setSamplingDetailResults(new ArrayList());
        sampling.setSamplingFiless(new ArrayList());
        sampling.setSamplingDetailYQFs(new ArrayList());
        sampling.setSamplingFormStandResults(new ArrayList<>());
        sampling.setIsLocal(true);
        sampling.setIsUpload(false);
        sampling.setIsCanEdit(true);
        sampling.setStatusName("等待提交");
        sampling.setSendSampTime("");
        sampling.setLayTableCheckbox("on");
        sampling.setTransStatus(3);
        sampling.setTransStatusName("等待提交");
        sampling.setCurUserId(UserInfoHelper.get().getUser().getId());
        sampling.setCurUserName(UserInfoHelper.get().getUser().getName());
        sampling.setAddTime(DateUtils.getTime(new Date().getTime()));
        sampling.setUpdateTime(DateUtils.getTime(new Date().getTime()));
        sampling.setVersion(0);
        sampling.setFormFlows("");
        sampling.setTagId("");
        sampling.setTagName("");
        sampling.setAddressId("");
        sampling.setAddressName("");
        sampling.setComment("");

//        if (!CheckUtil.isEmpty(projectDetials)) {
//            for (ProjectDetial projectDetial : projectDetials) {
//                if (!sampling.getTagId().contains(projectDetial.getMonItemId())) {
//                    if (!TextUtils.isEmpty(sampling.getTagId())) {
//                        sampling.setTagId(sampling.getTagId() + ",");
//                    }
//                    sampling.setTagId(sampling.getTagId() + projectDetial.getMonItemId());
//                }
//
//                if (!sampling.getTagName().contains(projectDetial.getMonItemName())) {
//                    if (!TextUtils.isEmpty(sampling.getTagName())) {
//                        sampling.setTagName(sampling.getTagName() + ",");
//                    }
//                    sampling.setTagName(sampling.getTagName() + projectDetial.getMonItemName());
//                }
//                if (!sampling.getAddressId().contains(projectDetial.getAddressId())) {
//                    if (!TextUtils.isEmpty(sampling.getAddressId())) {
//                        sampling.setAddressId(sampling.getAddressId() + ",");
//                    }
//                    sampling.setAddressId(sampling.getAddressId() + projectDetial.getAddressId());
//                }
//
//                if (!sampling.getAddressName().contains(projectDetial.getAddress())) {
//                    if (!TextUtils.isEmpty(sampling.getAddressName())) {
//                        sampling.setAddressName(sampling.getAddressName() + ",");
//                    }
//                    sampling.setAddressName(sampling.getAddressName() + projectDetial.getAddress());
//                }
//            }
//        }

        HashMap<String, String> privateData = new HashMap<>();
        privateData.put("CaleValue", "");
        privateData.put("RPDValue", "");
        privateData.put("SamplingOnTime", "");
        privateData.put("HasPX", "false");
        privateData.put("FormTypeName", "地下水");
        privateData.put("SourceWay", "检定");
        privateData.put("SourceDate", "2028-02-28");
        privateData.put("DeviceText", "css1(1201012)(检定2028-02-28)");
        sampling.setPrivateData(com.alibaba.fastjson.JSONObject.toJSONString(privateData));

        return sampling;
    }

    /**
     * 创建采样单编号
     *
     * @return
     */
    private String createSamplingNo() {
        StringBuilder samplingNo = new StringBuilder("");
        String dateStr = DateUtils.getDate().replace("-", "").substring(2);
        samplingNo.append(dateStr);
        samplingNo.append(UserInfoHelper.get().getUser().getIntId());

        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like("%" + samplingNo.toString() + "%"), SamplingDao.Properties.ProjectId.eq(projectId)).orderAsc(SamplingDao.Properties.SamplingNo).list();

        if (CheckUtil.isEmpty(samplings)) {
            samplingNo.append(StringUtil.autoGenericCode(1, 2));
        } else {
            String lastSamlingNo = samplings.get(samplings.size() - 1).getSamplingNo();
            if (!CheckUtil.isEmpty(lastSamlingNo)) {
                int serialNumber = Integer.parseInt(lastSamlingNo.substring(lastSamlingNo.length() - 2)) + 1;
                samplingNo.append(StringUtil.autoGenericCode(serialNumber, 2));
            } else {
                samplingNo.append(StringUtil.autoGenericCode(1, 2));
            }
        }
        return samplingNo.toString();
    }


    /**
     * 采样是否完成
     *
     * @return
     */
    public static boolean IsSamplingFinish() {
        if (CheckUtil.isEmpty(mSampling.getSamplingUserName())) {
            return false;
        }

        if (CheckUtil.isEmpty(mSampling.getSamplingTimeBegin())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getSamplingTimeEnd())) {
            return false;
        }

        if (CheckUtil.isEmpty(mSampling.getMonitemId())) {
            return false;
        }

        if (CheckUtil.isEmpty(mSampling.getMethodName())) {
            return false;
        }

        if (CheckUtil.isEmpty(mSampling.getDeviceName())) {
            return false;
        }

        if (CheckUtil.isEmpty(mSampling.getSamplingDetailYQFs())) {
            return false;
        }

        for (SamplingDetail detail : mSampling.getSamplingDetailYQFs()) {
            if (CheckUtil.isEmpty(detail.getPrivateDataStringValue("SamplingOnTime"))) {
                return false;//没有填写分析时间
            }
            if (CheckUtil.isEmpty(detail.getPrivateDataStringValue("CaleValue"))) {
                return false;//没有填写分析结果
            }
            if (CheckUtil.isEmpty(detail.getPrivateDataStringValue("ValueUnit"))) {
                return false;//没有填写结果单位
            }
        }

        return true;
    }

}
