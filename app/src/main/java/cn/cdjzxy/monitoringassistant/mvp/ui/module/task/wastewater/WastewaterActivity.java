package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;
import com.yinghe.whiteboardlib.MultiImageSelectorActivity;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FsExtends;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.autograph.AutographActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.autograph.fragment.AutographFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.FormPrintActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BasicFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BottleSplitDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BottleSplitFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.CollectionDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.CollectionFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.HelpUtil;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.NoScrollViewPager;

public class WastewaterActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.tabview)
    CustomTab tabview;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    private String projectId;//项目id
    private String formSelectId;//采样要素id（TagId）
    private String samplingId;//采样单id
    private boolean isNewCreate;//是否是新增采样单
    public static Sampling mSample;
    public static Project mProject;

    private List<Fragment> mFragments;
    private FragmentAdapter mFragmentAdapter;

    private BasicFragment mBasicFragment;
    private BottleSplitFragment mBottleSplitFragment;
    private BottleSplitDetailFragment mBottleSplitDetailFragment;
    private CollectionFragment mCollectionFragment;
    private CollectionDetailFragment mCollectionDetailFragment;
    private AutographFragment autographFragment;

    private TitleBarView mTitleBarView;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mTitleBarView = titleBar;
        titleBar.setTitleMainText("水和废水采样及交接记录");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
        mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_print, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(FormPrintActivity.class);
            }
        }));

        mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_save
                , new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSample.getIsCanEdit()) {
                    if (!checkBaseInfo()) {
                        return;
                    }
                    //保存文件
                    if (!CheckUtil.isEmpty(mSample.getSamplingFiless())) {
                        List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(mSample.getId())).list();
                        if (!CheckUtil.isEmpty(samplingFiles)) {
                            DBHelper.get().getSamplingFileDao().deleteInTx(samplingFiles);
                        }
                        if (CheckUtil.isNull(mSample.getSamplingFiless().get(0).getId())) {
                            //将补充位置的第一个图片去除,添加图片的标志
                            mSample.getSamplingFiless().remove(0);
                        }
                        DBHelper.get().getSamplingFileDao().insertInTx(mSample.getSamplingFiless());
                    }
                    //保存样品
                    if (!CheckUtil.isEmpty(mSample.getSamplingDetailResults())) {
                        List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(mSample.getId())).list();
                        if (!CheckUtil.isEmpty(samplingDetails)) {
                            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetails);
                        }
                        DBHelper.get().getSamplingDetailDao().insertInTx(mSample.getSamplingDetailResults());
                    }
                    if (!CheckUtil.isEmpty(mSample.getAutographList())) {
                        //todo  保存签名文件暂时等后台接口实现了在保存
                    }
                    //保存分瓶信息
                    if (!CheckUtil.isEmpty(mSample.getSamplingFormStandResults())) {
                        List<SamplingFormStand> samplingFormStands = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).list();
                        if (!CheckUtil.isEmpty(samplingFormStands)) {
                            DBHelper.get().getSamplingFormStandDao().deleteInTx(samplingFormStands);
                        }
                        DBHelper.get().getSamplingFormStandDao().insertInTx(mSample.getSamplingFormStandResults());
                    }

                    mSample.setIsFinish(HelpUtil.isSamplingFinish(mSample));
                    mSample.setStatusName(HelpUtil.isSamplingFinish(mSample) ? "已完成" : "进行中");
                    //保存基本信息
                    saveBaseInfo();
                    EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                    ArtUtils.makeText(getApplicationContext(), "数据保存成功");
                }
            }
        }));

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
        mProject = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
        if (isNewCreate) {
            mSample = createSample();
        } else {
            mSample = DBHelper.get().getSamplingDao().queryBuilder().
                    where(SamplingDao.Properties.Id.eq(samplingId)).unique();
            List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().
                    where(SamplingFileDao.Properties.SamplingId.eq(mSample.getId())).list();
            mSample.setSamplingFiless(samplingFiles);
            List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().
                    where(SamplingDetailDao.Properties.SamplingId.eq(mSample.getId())).list();
            mSample.setSamplingDetailResults(samplingDetails);
            List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().
                    where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).
                    orderAsc(SamplingFormStandDao.Properties.Index).list();
            if (!CheckUtil.isEmpty(formStantdsList)) {
                WastewaterActivity.mSample.setSamplingFormStandResults(formStantdsList);
            }
            List<SamplingContent> samplingContentList = DBHelper.get().getSamplingContentDao().
                    queryBuilder().where(SamplingContentDao.Properties.SamplingId.eq(mSample.getId())).
                    orderAsc(SamplingContentDao.Properties.OrderIndex).list();
            if (!CheckUtil.isEmpty(samplingContentList)) {
                WastewaterActivity.mSample.setSamplingContentResults(samplingContentList);
            }

        }

        initTabData();
        openFragment(0);
    }

    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        tabview.setTabs("基本信息", "样品采集", "分瓶信息", "签名");
        tabview.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                openFragment(position);
            }
        });

        mBasicFragment = new BasicFragment();
        mCollectionFragment = new CollectionFragment();
        mBottleSplitFragment = new BottleSplitFragment();
        autographFragment = new AutographFragment();

        mCollectionDetailFragment = new CollectionDetailFragment();
        mBottleSplitDetailFragment = new BottleSplitDetailFragment();


        mFragments = new ArrayList<>();
        mFragments.add(mBasicFragment);//0
        mFragments.add(mCollectionFragment);//1;
        mFragments.add(mBottleSplitFragment);//2
        mFragments.add(autographFragment);//3
        mFragments.add(mCollectionDetailFragment);//4
        mFragments.add(mBottleSplitDetailFragment);//5


        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(mFragmentAdapter);
        viewPager.setOffscreenPageLimit(5);
    }


    private void addFile(String path) {
        Log.e(TAG, "addFile: " + path);
        SamplingFile sampling = new SamplingFile();
        File file = new File(path);
        sampling.setLocalId("LC-" + UUID.randomUUID().toString());
        sampling.setId("");
        sampling.setFilePath(path);
        sampling.setFileName(file.getName());
        sampling.setSamplingId(mSample.getId());
        sampling.setUpdateTime(DateUtils.getTime(new Date().getTime()));
        Log.e(TAG, "addFile: " + sampling.toString());
        mSample.getSamplingFiless().add(sampling);
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
        if (viewPager.getCurrentItem() == 4) {
            openFragment(1);
            return;
        }

        if (viewPager.getCurrentItem() == 5) {
            openFragment(2);
            return;
        }
        finish();
    }


    /**
     * 创建采样单
     *
     * @return
     */
    private Sampling createSample() {
        Project project = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
        FormSelect formSelect = DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.FormId.eq(formSelectId)).unique();
        Sampling sampling = new Sampling();
        sampling.setId(UUID.randomUUID().toString());//唯一标志
        sampling.setSamplingNo(createSamplingNo());
        sampling.setProjectId(project.getId());
        sampling.setProjectName(project.getName());
        sampling.setProjectNo(project.getProjectNo());
        //sampling.setTagId(formSelect.getTagId());
        //sampling.setMontype(project.getMonType() + "");
        sampling.setMontype(project.getTypeCode());
        //sampling.setTagName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagId())).unique().getName());
        sampling.setFormType(formSelect.getTagParentId());
//        sampling.setFormTypeName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagParentId())).unique().getName());
        sampling.setFormTypeName("水");//Tip:毛阳说写死
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
        sampling.setSamplingContentResults(new ArrayList<>());
        sampling.setIsLocal(true);
        sampling.setIsUpload(false);
        sampling.setIsCanEdit(true);
        return sampling;
    }

    /**
     * 创建采样单编号:年月日+账号+流水号
     *
     * @return
     */
    private String createSamplingNo() {
        StringBuilder samplingNo = new StringBuilder("");
        String dateStr = DateUtils.getDate().replace("-", "").substring(2);
        samplingNo.append(dateStr);
        samplingNo.append(UserInfoHelper.get().getUser().getIntId());
//        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%"), SamplingDao.Properties.ProjectId.eq(projectId)).orderAsc(SamplingDao.Properties.SamplingNo).list();
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%")).orderAsc(SamplingDao.Properties.SamplingNo).list();

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
     * 基本信息校验
     *
     * @return
     */
    private boolean checkBaseInfo() {
        if (CheckUtil.isEmpty(mSample.getSamplingTimeBegin())) {
            ArtUtils.makeText(getApplicationContext(), "请选择采样日期");
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getSamplingUserId())) {
            ArtUtils.makeText(getApplicationContext(), "请选择采样人");
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getTagId())) {
            ArtUtils.makeText(getApplicationContext(), "请选择样品性质");
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getAddressId())) {
            ArtUtils.makeText(getApplicationContext(), "请选择样监测点位");
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getMethodId())) {
            ArtUtils.makeText(getApplicationContext(), "请选择样采样方法");
            return false;
        }
        return true;
    }

    /**
     * 保存基本信息
     */
    private void saveBaseInfo() {
        if (mBasicFragment != null) {
            mBasicFragment.saveSamplingData();
        }
        if (isNewCreate) {
            //DBHelper.get().getSamplingDao().insert(mSample);
            DBHelper.get().getSamplingDao().insertOrReplace(mSample);
            isNewCreate = false;
        } else {
            DBHelper.get().getSamplingDao().update(mSample);
        }
    }

}
