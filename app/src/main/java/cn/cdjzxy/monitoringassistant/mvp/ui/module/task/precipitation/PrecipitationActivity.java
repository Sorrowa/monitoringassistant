package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
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
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.BasicFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.CollectionDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.CollectionFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.FormPrintActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;
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
    private boolean isNewCreate;

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
            mSampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(samplingId)).unique();
            List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
            mSampling.setSamplingFiless(CheckUtil.isEmpty(samplingFiles) ? new ArrayList<>() : samplingFiles);
            List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
            mSampling.setSamplingDetailResults(CheckUtil.isEmpty(samplingDetails) ? new ArrayList<>() : samplingDetails);
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

                    mSampling.setIsFinish(isSamplingFinish());
                    mSampling.setStatusName(isSamplingFinish() ? "已完成" : "进行中");
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
     * 初始化Tab数据
     */
    private void initTabData() {
        tabview.setTabs("基本信息", "样品采集");
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

    /**
     * 采样是否完成
     *
     * @return
     */
    private boolean isSamplingFinish() {
        if (CheckUtil.isEmpty(mSampling.getSamplingDetailResults())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getSamplingUserName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getTagName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getAddressName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getPrivateData())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getMethodName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getDeviceName())) {
            return false;
        }
        return true;
    }


}
