package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.DeviceAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TabAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.msg.MsgActivity;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class DeviceActivity extends BaseTitileActivity<ApiPresenter> implements IView {


    @BindView(R.id.recyclerView_point)
    RecyclerView recyclerViewPoint;
    @BindView(R.id.tabview)
    CustomTab    tabview;


    private DeviceAdapter mDeviceAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("设备选择");
        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.makeText(getApplicationContext(), "搜索");
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
        return R.layout.activity_device;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);
        initTabData();
        initMsgData();
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
        tabview.setTabs("关联设备", "其他设备");
        tabview.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                ArtUtils.makeText(DeviceActivity.this, tab.getTabName());
            }
        });
    }

    /**
     * 初始化Tab数据
     */
    private void initMsgData() {
        ArtUtils.configRecyclerView(recyclerViewPoint, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        List<Devices> devices = DBHelper.get().getDevicesDao().loadAll();
        mDeviceAdapter = new DeviceAdapter(devices);
        mDeviceAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {

            }
        });
        recyclerViewPoint.setAdapter(mDeviceAdapter);
    }


}
