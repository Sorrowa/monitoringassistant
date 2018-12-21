package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.lidroid.xutils.db.annotation.Check;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FragmentAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.FormPrintActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BasicFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BottleSplitDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BottleSplitFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.CollectionDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.CollectionFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.SiteMonitoringFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.keyboard.KeyboardWatcher;
import cn.cdjzxy.monitoringassistant.utils.keyboard.callback.OnKeyboardStateChangeListener;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.NoScrollViewPager;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class WastewaterActivity extends BaseTitileActivity<ApiPresenter> {

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

    private BasicFragment             mBasicFragment;
    private SiteMonitoringFragment    mSiteMonitoringFragment;
    private BottleSplitFragment       mBottleSplitFragment;
    private BottleSplitDetailFragment mBottleSplitDetailFragment;
    private CollectionFragment        mCollectionFragment;
    private CollectionDetailFragment  mCollectionDetailFragment;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("水和废水采样及交接记录");
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
    public void initData(@Nullable Bundle savedInstanceState) {
        layout.scrollTo(0, StatusBarUtil.getStatusBarHeight(this));
        initTabData();
        openFragment(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BasicFragment basicFragment = (BasicFragment) getSupportFragmentManager().findFragmentByTag(BasicFragment.class.getName());
        if (!CheckUtil.isNull(basicFragment)) {
            basicFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        tabview.setTabs("基本信息", "样品采集", "现场检测", "分瓶信息");
        tabview.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                openFragment(position);
            }
        });

        mBasicFragment = new BasicFragment();
        mSiteMonitoringFragment = new SiteMonitoringFragment();
        mBottleSplitFragment = new BottleSplitFragment();
        mBottleSplitDetailFragment = new BottleSplitDetailFragment();
        mCollectionFragment = new CollectionFragment();
        mCollectionDetailFragment = new CollectionDetailFragment();

        mFragments = new ArrayList<>();
        mFragments.add(mBasicFragment);
        mFragments.add(mBottleSplitFragment);
        mFragments.add(mSiteMonitoringFragment);
        mFragments.add(mCollectionFragment);
        mFragments.add(mCollectionDetailFragment);
        mFragments.add(mBottleSplitDetailFragment);

        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(mFragmentAdapter);
        viewPager.setOffscreenPageLimit(6);
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
        Fragment fragment = null;
        fragment = getSupportFragmentManager().findFragmentByTag(CollectionDetailFragment.class.getName());
        if (!CheckUtil.isNull(fragment)) {
            openFragment(1);
            return;
        }

        fragment = getSupportFragmentManager().findFragmentByTag(BottleSplitDetailFragment.class.getName());
        if (!CheckUtil.isNull(fragment)) {
            openFragment(3);
            return;
        }

        finish();
    }

}
