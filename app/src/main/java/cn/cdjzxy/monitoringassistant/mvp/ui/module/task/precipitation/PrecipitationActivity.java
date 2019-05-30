package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.BasicFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.CollectionDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.CollectionFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.FormPrintActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.NoScrollViewPager;

public class PrecipitationActivity extends BaseTitileActivity<ApiPresenter> {

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

    private BasicFragment mBasicFragment;
    private CollectionFragment mCollectionFragment;
    private CollectionDetailFragment mCollectionDetailFragment;

    public static Sampling mSampling;
    public static Project mProject;

    private TitleBarView mTitleBarView;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mTitleBarView = titleBar;
        mTitleBarView.setTitleMainText("降水采样交接记录单");
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
        return R.layout.activity_precipitation_record;
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

        mProject = DbHelpUtils.getDbProject(projectId);
        if (isNewCreate) {
            mSampling = SamplingUtil.createPrecipitationSample(mProject, formSelectId);
        } else {
            mSampling = DbHelpUtils.getDbSampling(samplingId);
            List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao()
                    .queryBuilder().where(SamplingFileDao.Properties.SamplingId.
                            eq(mSampling.getId())).list();
            mSampling.setSamplingFiless(CheckUtil.isEmpty(samplingFiles) ? new ArrayList<>() : samplingFiles);
            getJsData();
        }

        mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_print, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(FormPrintActivity.class);
            }
        }));

        if (mSampling.getIsCanEdit()) {
            mTitleBarView.addRightAction(mTitleBarView.new ImageAction(R.mipmap.ic_save, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isNewCreate && !UserInfoHelper.get()
                            .isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
                        showNoPermissionDialog("才能进行表单编辑。",
                                UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
                        return;
                    }
                    if (!CheckUtil.isEmpty(mSampling.getSamplingFiless())) {
                        List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
                        if (!CheckUtil.isEmpty(samplingFiles)) {
                            DBHelper.get().getSamplingFileDao().deleteInTx(samplingFiles);
                        }
                        if (CheckUtil.isNull(mSampling.getSamplingFiless().get(0).getId())) {
                            //将补充位置的第一个图片去除,添加图片的标志
                            mSampling.getSamplingFiless().remove(0);
                        }

                        DBHelper.get().getSamplingFileDao().insertInTx(mSampling.getSamplingFiless());
                    }
//                    mSampling.setMonitemName(SamplingUtil.setPrecipiationMonitemName(mSampling));
//                    mSampling.setMonitemName(SamplingUtil.setPrecipiationMonitemId(mSampling));

                    if (!CheckUtil.isEmpty(mSampling.getSamplingDetailResults())) {
                        List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
                        if (!CheckUtil.isEmpty(samplingDetails)) {
                            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetails);
                        }
                        DBHelper.get().getSamplingDetailDao().insertInTx(mSampling.getSamplingDetailResults());
                    }

                    mSampling.setIsFinish(SamplingUtil.isSamplingFinsh(mSampling));
                    mSampling.setStatusName(mSampling.getIsFinish() ? "已完成" : "进行中");
                    if (isNewCreate) {
                        DBHelper.get().getSamplingDao().insertOrReplace(mSampling);
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
     * 获取降水数据
     * 因为服务器返回很多非降水的数据在，app暂时用不着 所以直接删除
     * 积累后就会很多  app只用了降水的数据
     */
    private void getJsData() {
        try {
            List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder()
                    .where(SamplingDetailDao.Properties.SamplingId.eq(mSampling.getId()))
                    .orderAsc(SamplingDetailDao.Properties.FrequecyNo).list();
            List<SamplingDetail> detailList = new ArrayList<>();
            List<SamplingDetail> deleteList = new ArrayList<>();
            if (!CheckUtil.isEmpty(samplingDetails)) {
                for (SamplingDetail detail : samplingDetails) {
                    if (detail != null && detail.getMonitemName() != null && detail.getMonitemName().equals("降水量")) {
                        detailList.add(detail);
                    } else {
                        deleteList.add(detail);
                    }
                }
            }
            mSampling.setSamplingDetailResults(detailList);
            DBHelper.get().getSamplingDetailDao().deleteInTx(deleteList);
        } catch (Exception e) {
            Log.e(TAG, "getJsData: " + e.toString());
        }
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
                tab.setTabName("样品采集");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_source);
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

        mBasicFragment = new BasicFragment();
        mCollectionFragment = new CollectionFragment();
        mCollectionDetailFragment = new CollectionDetailFragment();

        mFragments = new ArrayList<>();
        mFragments.add(mBasicFragment);
        mFragments.add(mCollectionFragment);
        mFragments.add(mCollectionDetailFragment);

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


    @Subscriber(tag = EventBusTags.TAG_PRECIPITATION_COLLECTION)
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
