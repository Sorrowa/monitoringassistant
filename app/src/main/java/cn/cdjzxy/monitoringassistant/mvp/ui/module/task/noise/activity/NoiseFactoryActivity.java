package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoiseBasicFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoiseMonitorEditFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoisePointEditFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoiseMonitorListFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoiseOtherFileFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoisePointListFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoiseSourceEditFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoiseSourceListFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.NoScrollViewPager;

/**
 * 噪声污染——"工业企业厂界噪声监测记录"
 */
public class NoiseFactoryActivity extends BaseTitileActivity<ApiPresenter> implements IView {

    @BindView(R.id.tab_view)
    CustomTab tabView;
    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    private String projectId;//项目id
    private boolean isNewCreate;//是否是新增采样单
    private String samplingId;//采样单id
    private String formSelectId;//采样要素id（TagId）

    public static Sampling mSample;
    public static Project mProject;
    public static NoisePrivateData mPrivateData;

    private List<Fragment> mFragments;
    private FragmentAdapter mFragmentAdapter;


    @Subscriber(tag = EventBusTags.TAG_NOISE_FRAGMENT_TYPE_SOURCE_EDIT)
    public void upSourceFragment(int position) {
        openFragment(position);
    }

    @Subscriber(tag = EventBusTags.TAG_NOISE_FRAGMENT_TYPE_POINT_EDIT)
    public void upPointFragment(int position) {
        openFragment(position);
    }
    @Subscriber(tag = EventBusTags.TAG_NOISE_FRAGMENT_TYPE_MONITOR_EDIT)
    public void upMonitorFragment(int position) {
        openFragment(position);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_noise_factory;
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("工业企业厂界噪声监测记录");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.addRightAction(titleBar.new ImageAction(R.mipmap.ic_save, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("待开发");
            }
        }));
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        getIntentData();
        mProject = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
        if (isNewCreate) {
            mSample = SamplingUtil.createSample(projectId, formSelectId);
            mPrivateData = new NoisePrivateData();
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
                mSample.setSamplingFormStandResults(formStantdsList);
            }
            List<SamplingContent> samplingContentList = DBHelper.get().getSamplingContentDao().
                    queryBuilder().where(SamplingContentDao.Properties.SamplingId.eq(mSample.getId())).
                    orderAsc(SamplingContentDao.Properties.OrderIndex).list();
            if (!CheckUtil.isEmpty(samplingContentList)) {
                mSample.setSamplingContentResults(samplingContentList);
            }
            Gson gson = new Gson();
            mPrivateData = gson.fromJson(mSample.getPrivateData(), NoisePrivateData.class);
            if (mPrivateData == null) {
                mPrivateData = new NoisePrivateData();
            }
        }
        initTabData();
        openFragment(0);

    }

    /**
     * 获取传递的数据
     */
    private void getIntentData() {
        projectId = getIntent().getStringExtra("projectId");
        formSelectId = getIntent().getStringExtra("formSelectId");
        samplingId = getIntent().getStringExtra("samplingId");
        isNewCreate = getIntent().getBooleanExtra("isNewCreate", false);
    }

    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        tabView.setTabs("基本信息", "主要噪声源", "监听点位", "监测数据", "附件");
        tabView.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                openFragment(position);
            }
        });

        NoiseBasicFragment mBasicFragment = new NoiseBasicFragment();
        NoiseSourceListFragment sourceListFragment = new NoiseSourceListFragment();
        NoisePointListFragment pointListFragment = new NoisePointListFragment();
        NoiseMonitorListFragment monitorListFragment = new NoiseMonitorListFragment();
        NoiseOtherFileFragment otherFileFragment = new NoiseOtherFileFragment();
        NoiseSourceEditFragment sourceEditFragment = new NoiseSourceEditFragment();
        NoisePointEditFragment pointEditFragment = new NoisePointEditFragment();
        NoiseMonitorEditFragment monitorEditFragment = new NoiseMonitorEditFragment();

        mFragments = new ArrayList<>();
        mFragments.add(mBasicFragment);//0
        mFragments.add(sourceListFragment);//1
        mFragments.add(pointListFragment);//2
        mFragments.add(monitorListFragment);//3
        mFragments.add(otherFileFragment);//4
        mFragments.add(sourceEditFragment);//5
        mFragments.add(pointEditFragment);//6
        mFragments.add(monitorEditFragment);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(mFragmentAdapter);
        viewPager.setOffscreenPageLimit(0);
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
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
