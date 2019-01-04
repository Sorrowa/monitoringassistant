package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import org.simple.eventbus.EventBus;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.FormPrintActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BasicFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BottleSplitDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BottleSplitFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.CollectionDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.CollectionFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.SiteMonitoringFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.NoScrollViewPager;

public class WastewaterActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.tabview)
    CustomTab         tabview;
    @BindView(R.id.layout)
    LinearLayout      layout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    private String projectId;
    private String formSelectId;
    private String  samplingId;
    private boolean isNewCreate;
    public static Sampling mSample;

    private List<Fragment>  mFragments;
    private FragmentAdapter mFragmentAdapter;

    private BasicFragment             mBasicFragment;
    //private SiteMonitoringFragment    mSiteMonitoringFragment;
    private BottleSplitFragment       mBottleSplitFragment;
    private BottleSplitDetailFragment mBottleSplitDetailFragment;
    private CollectionFragment        mCollectionFragment;
    private CollectionDetailFragment  mCollectionDetailFragment;

    private TitleBarView mTitleBarView;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mTitleBarView=titleBar;
        titleBar.setTitleMainText("水和废水采样及交接记录");
        /*
        titleBar.addRightAction(titleBar.new ImageAction(R.mipmap.ic_print, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(FormPrintActivity.class);
            }
        }));

        titleBar.addRightAction(titleBar.new ImageAction(R.mipmap.ic_save, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.makeText(getApplicationContext(), "保存");
            }
        }));
        */
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
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
        return R.layout.activity_wastewater_record;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFragments.get(viewPager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        layout.scrollTo(0, StatusBarUtil.getStatusBarHeight(this));

        projectId = getIntent().getStringExtra("projectId");
        formSelectId = getIntent().getStringExtra("formSelectId");
        samplingId = getIntent().getStringExtra("samplingId");
        isNewCreate = getIntent().getBooleanExtra("isNewCreate", false);
        if (isNewCreate) {
            mSample = createSample();
        } else {
            mSample = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(samplingId)).unique();
            List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
            mSample.setSamplingFiless(samplingFiles);
            List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
            mSample.setSamplingDetailResults(samplingDetails);
        }

        if (mSample.getIsCanEdit()) {
            mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_print, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArtUtils.startActivity(FormPrintActivity.class);
                }
            }));
            mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_save, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!CheckUtil.isEmpty(mSample.getSamplingFiless())) {
                        List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
                        if (!CheckUtil.isEmpty(samplingFiles)) {
                            DBHelper.get().getSamplingFileDao().deleteInTx(samplingFiles);
                        }
                        mSample.getSamplingFiless().remove(0);
                        DBHelper.get().getSamplingFileDao().insertInTx(mSample.getSamplingFiless());
                    }

                    if (!CheckUtil.isEmpty(mSample.getSamplingDetailResults())) {
                        List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
                        if (!CheckUtil.isEmpty(samplingDetails)) {
                            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetails);
                        }
                        DBHelper.get().getSamplingDetailDao().insertInTx(mSample.getSamplingDetailResults());
                    }

                    mSample.setIsFinish(isSamplingFinish());
                    mSample.setStatusName(isSamplingFinish() ? "已完成" : "进行中");
                    if (isNewCreate) {
                        DBHelper.get().getSamplingDao().insert(mSample);
                        isNewCreate = false;
                    } else {
                        DBHelper.get().getSamplingDao().update(mSample);
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
        //tabview.setTabs("基本信息", "样品采集", "现场检测", "分瓶信息");
        tabview.setTabs("基本信息", "样品采集", "分瓶信息");
        tabview.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                openFragment(position);
            }
        });

        mBasicFragment = new BasicFragment();
        //mSiteMonitoringFragment = new SiteMonitoringFragment();
        mBottleSplitFragment = new BottleSplitFragment();
        mBottleSplitDetailFragment = new BottleSplitDetailFragment();
        mCollectionFragment = new CollectionFragment();
        mCollectionDetailFragment = new CollectionDetailFragment();

        mFragments = new ArrayList<>();
        mFragments.add(mBasicFragment);
        //mFragments.add(mSiteMonitoringFragment);
        mFragments.add(mCollectionFragment);
        mFragments.add(mBottleSplitFragment);
        mFragments.add(mCollectionDetailFragment);
        mFragments.add(mBottleSplitDetailFragment);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(mFragmentAdapter);
        //viewPager.setOffscreenPageLimit(6);
        viewPager.setOffscreenPageLimit(5);
    }


    /**
     * 切换Fragment页面
     *
     * @param position
     */
    private void openFragment(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @Subscriber(tag = EventBusTags.TAG_WASTEWATER_COLLECTION)
    private void updateCollectFragment(int position) {
        openFragment(position);
    }

    @Subscriber(tag = EventBusTags.TAG_WASTEWATER_BOTTLE)
    private void updateBottleFragment(int position) {
        openFragment(position);
    }

    private void onBack() {
        /*
        if (viewPager.getCurrentItem() == 5) {
            openFragment(1);
            return;
        }

        if (viewPager.getCurrentItem() == 4) {
            openFragment(3);
            return;
        }
        */
        if (viewPager.getCurrentItem() == 4) {
            openFragment(2);
            return;
        }

        if (viewPager.getCurrentItem() == 3) {
            openFragment(1);
            return;
        }
        finish();
    }


    /**
     * 创建采样单
     * @return
     */
    private Sampling createSample() {
        Project project = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
        FormSelect formSelect = DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.FormId.eq(formSelectId)).unique();
        Sampling sampling = new Sampling();
        sampling.setId("FS-" + UUID.randomUUID().toString());
        sampling.setSamplingNo(createSamplingNo());
        sampling.setProjectId(project.getId());
        sampling.setProjectName(project.getName());
        sampling.setProjectNo(project.getProjectNo());
        sampling.setTagId(formSelect.getTagId());
        sampling.setMontype(project.getTypeCode() + "");
        //sampling.setTagName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagId())).unique().getName());
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
        if (CheckUtil.isEmpty(mSample.getSamplingDetailResults())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getSamplingUserName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getTagName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getAddressName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getPrivateData())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getMethodName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getDeviceName())) {
            return false;
        }
        return true;
    }

}
