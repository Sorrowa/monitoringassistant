package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment.BasicInfoFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment.TestRecordDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment.TestRecordFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.BasicFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.CollectionDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.CollectionFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.NoScrollViewPager;

public class InstrumentalActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.tabview)
    CustomTab         tabview;
    @BindView(R.id.layout)
    LinearLayout      layout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    private String  projectId;
    private String  formSelectId;
    private String  samplingId;
    private boolean isNewCreate;

    private List<Fragment>  mFragments;
    private FragmentAdapter mFragmentAdapter;

    private BasicInfoFragment mBasicFragment;
    private TestRecordFragment mTestRecordFragment;
    private TestRecordDetailFragment mTestRecordDetailFragment;

//    public static Sampling mSampling;

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
//        projectId = getIntent().getStringExtra("projectId");
//        formSelectId = getIntent().getStringExtra("formSelectId");
//        samplingId = getIntent().getStringExtra("samplingId");
//        isNewCreate = getIntent().getBooleanExtra("isNewCreate", false);
//        if (isNewCreate) {
//            mSampling = createSampling();
//        } else {
//            mSampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(samplingId)).unique();
//            List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
//            mSampling.setSamplingFiless(samplingFiles);
//            List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
//            mSampling.setSamplingDetailResults(samplingDetails);
//        }
//
//        if (mSampling.getIsCanEdit()) {
//            mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_print, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ArtUtils.startActivity(FormPrintActivity.class);
//                }
//            }));
//            mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_save, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if (!CheckUtil.isEmpty(mSampling.getSamplingFiless())) {
//                        List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
//                        if (!CheckUtil.isEmpty(samplingFiles)) {
//                            DBHelper.get().getSamplingFileDao().deleteInTx(samplingFiles);
//                        }
//                        mSampling.getSamplingFiless().remove(0);
//                        DBHelper.get().getSamplingFileDao().insertInTx(mSampling.getSamplingFiless());
//                    }
//
//                    if (!CheckUtil.isEmpty(mSampling.getSamplingDetailResults())) {
//                        List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
//                        if (!CheckUtil.isEmpty(samplingDetails)) {
//                            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetails);
//                        }
//                        DBHelper.get().getSamplingDetailDao().insertInTx(mSampling.getSamplingDetailResults());
//                    }
//
//                    mSampling.setIsFinish(isSamplingFinish());
//                    mSampling.setStatusName(isSamplingFinish() ? "已完成" : "进行中");
//                    if (isNewCreate) {
//                        DBHelper.get().getSamplingDao().insert(mSampling);
//                        isNewCreate = false;
//                    } else {
//                        DBHelper.get().getSamplingDao().update(mSampling);
//                    }
//
//                    EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
//                    ArtUtils.makeText(getApplicationContext(), "数据保存成功");
//                }
//            }));
//        }
//
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


    @Subscriber(tag = EventBusTags.TAG_PRECIPITATION_COLLECTION)
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
        FormSelect formSelect = DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.FormId.eq(formSelectId)).unique();
        Sampling sampling = new Sampling();
        sampling.setId("LC-" + UUID.randomUUID().toString());
        sampling.setSamplingNo(createSamplingNo());
        sampling.setProjectId(project.getId());
        sampling.setProjectName(project.getName());
        sampling.setProjectNo(project.getProjectNo());
        sampling.setTagId(formSelect.getTagId());
        sampling.setMontype(project.getTypeCode() + "");
        sampling.setTagName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagId())).unique().getName());
        sampling.setFormType(formSelect.getTagParentId());
        sampling.setFormTypeName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagParentId())).unique().getName());
        sampling.setFormName(formSelect.getFormName());
        sampling.setFormPath(formSelect.getPath());
        //        sampling.setFormFlows(formSelect.getFormFlows().toString());
        sampling.setParentTagId(formSelect.getTagParentId());
        sampling.setStatusName("进行中");
        sampling.setStatus(0);
        sampling.setSamplingUserId(UserInfoHelper.get().getUser().getId());
        sampling.setSamplingUserName(UserInfoHelper.get().getUser().getName());
        sampling.setSamplingTimeBegin(DateUtils.getDate());
        sampling.setSamplingDetailResults(new ArrayList<>());
        sampling.setIsLocal(true);
        sampling.setIsUpload(false);
        sampling.setIsCanEdit(true);
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
    private boolean isSamplingFinish() {
//        if (CheckUtil.isEmpty(mSampling.getSamplingDetailResults())) {
//            return false;
//        }
//        if (CheckUtil.isEmpty(mSampling.getSamplingUserName())) {
//            return false;
//        }
//        if (CheckUtil.isEmpty(mSampling.getTagName())) {
//            return false;
//        }
//        if (CheckUtil.isEmpty(mSampling.getAddressName())) {
//            return false;
//        }
//        if (CheckUtil.isEmpty(mSampling.getPrivateData())) {
//            return false;
//        }
//        if (CheckUtil.isEmpty(mSampling.getMethodName())) {
//            return false;
//        }
//        if (CheckUtil.isEmpty(mSampling.getDeviceName())) {
//            return false;
//        }
        return true;
    }

}
