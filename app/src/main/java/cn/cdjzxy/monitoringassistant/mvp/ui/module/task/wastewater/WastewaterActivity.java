package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.FormPrintActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BasicFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BottleSplitDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.BottleSplitFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.CollectionDetailFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.CollectionFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment.SiteMonitoringFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class WastewaterActivity extends BaseTitileActivity<ApiPresenter> implements IView {


    @BindView(R.id.layout_container)
    FrameLayout layoutContainer;
    @BindView(R.id.tabview)
    CustomTab   tabview;


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
        StatusBarUtil.darkMode(this, false);
        initTabData();
        openFragment(0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        switch (message.what) {
            case 0:

                break;
            case Message.RESULT_OK:

                break;
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
                ArtUtils.makeText(WastewaterActivity.this, tab.getTabName());
            }
        });
    }


    /**
     * 切换Fragment页面
     *
     * @param position
     */
    private void openFragment(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Bundle mBundle = new Bundle();
        switch (position) {
            case 0://基本信息
                ft.replace(R.id.layout_container, new BasicFragment(), BasicFragment.class.getName());
                break;
            case 1://样品采集
                ft.replace(R.id.layout_container, new CollectionFragment(), CollectionFragment.class.getName());
                break;
            case 2://现场监测
                ft.replace(R.id.layout_container, new SiteMonitoringFragment(), SiteMonitoringFragment.class.getName());
                break;
            case 3://分瓶信息
                ft.replace(R.id.layout_container, new BottleSplitFragment(), BottleSplitFragment.class.getName());
                break;
            case 4://样品采集详情
                ft.replace(R.id.layout_container, new CollectionDetailFragment(), CollectionDetailFragment.class.getName());
                break;
            case 5://分瓶信息详情
                ft.replace(R.id.layout_container, new BottleSplitDetailFragment(), BottleSplitDetailFragment.class.getName());
                break;
            default:
                break;
        }
        ft.commit();


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
