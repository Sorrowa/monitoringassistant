package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

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
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.BasicFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.CollectionDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment.CollectionFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.FormPrintActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.NoScrollViewPager;

public class PrecipitationActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.tabview)
    CustomTab         tabview;
    @BindView(R.id.layout)
    LinearLayout      layout;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;

    private String projectId;
    private String formSelectId;

    private List<Fragment>  mFragments;
    private FragmentAdapter mFragmentAdapter;

    private BasicFragment            mBasicFragment;
    private CollectionFragment       mCollectionFragment;
    private CollectionDetailFragment mCollectionDetailFragment;

    private Sampling mSampling;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("降水采样交接记录单");
        titleBar.addRightAction(titleBar.new ImageAction(R.mipmap.ic_print, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(FormPrintActivity.class);
            }
        }));
        titleBar.addRightAction(titleBar.new ImageAction(R.mipmap.ic_save, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.get().getSamplingDao().insert(mSampling);
                ArtUtils.makeText(getApplicationContext(), "保存成功");
            }
        }));

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
        return R.layout.activity_precipitation_record;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        layout.scrollTo(0, StatusBarUtil.getStatusBarHeight(this));
        projectId = getIntent().getStringExtra("projectId");
        formSelectId = getIntent().getStringExtra("formSelectId");
        createSampling();
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
        Fragment fragment = null;
        fragment = getSupportFragmentManager().findFragmentByTag(CollectionDetailFragment.class.getName());
        if (!CheckUtil.isNull(fragment)) {
            openFragment(1);
            return;
        }
        finish();
    }


    private void createSampling() {
        Project project = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
        FormSelect formSelect = DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.FormId.eq(formSelectId)).unique();
        mSampling = new Sampling();
        mSampling.setId(UUID.randomUUID().toString());
        mSampling.setSamplingNo("FS1809110101");
        mSampling.setProjectId(project.getId());
        mSampling.setProjectName(project.getName());
        mSampling.setProjectNo(project.getProjectNo());
        mSampling.setTagId(formSelect.getTagId());
        mSampling.setFormName(formSelect.getFormName());
        mSampling.setFormPath(formSelect.getPath());
        mSampling.setParentTagId(formSelect.getTagParentId());
        mSampling.setStatusName("进行中");
        mSampling.setStatus(0);
    }


}
