package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DevicesDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MethodsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.DeviceAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class DeviceActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.recyclerView_point)
    RecyclerView recyclerViewPoint;
    @BindView(R.id.tabview)
    CustomTab tabview;


    private String methodId;

    private List<Devices> mDevices = new ArrayList<>();
    private DeviceAdapter mDeviceAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("设备选择");
        //        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        //        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                ArtUtils.makeText(getApplicationContext(), "搜索");
        //            }
        //        });

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
        methodId = getIntent().getStringExtra("methodId");
        initTabData();
        initDevicesData();
        getDeviceData(true);
    }

    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        tabview.setTabs("关联设备", "其他设备");
        tabview.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                getDeviceData(position == 0);
            }
        });
    }

    /**
     * 初始化Tab数据
     */
    private void initDevicesData() {
        ArtUtils.configRecyclerView(recyclerViewPoint, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mDeviceAdapter = new DeviceAdapter(mDevices);
        mDeviceAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Devices device = mDevices.get(position);
                Intent intent = new Intent();
                intent.putExtra("DeviceId", device.getId());
                intent.putExtra("DeviceName", device.getName());
                intent.putExtra("SourceWay", TextUtils.isEmpty(device.getSourceWay()) ? "" : device.getSourceWay());
                intent.putExtra("ExpireDate", TextUtils.isEmpty(device.getSourceDate()) ? "" : device.getSourceDate());
                intent.putExtra("DeviceCode", TextUtils.isEmpty(device.getDevCode()) ? "" : device.getDevCode());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerViewPoint.setAdapter(mDeviceAdapter);
    }


    private void getDeviceData(boolean isRelationDevice) {
        List<Devices> temp= DBHelper.get().getDevicesDao().loadAll();
        List<Devices> devices = null;
        if (isRelationDevice) {
            Methods methods = DBHelper.get().getMethodsDao().queryBuilder().where(MethodsDao.Properties.Id.eq(methodId)).unique();
            if (!CheckUtil.isNull(methods)) {
                devices = methods.getMDevices();
            }
        } else {
            Methods methods = DBHelper.get().getMethodsDao().queryBuilder().where(MethodsDao.Properties.Id.eq(methodId)).unique();
            if (!CheckUtil.isNull(methods)) {
                List<Devices> reDevices = methods.getMDevices();
                if (CheckUtil.isEmpty(reDevices)) {
                    devices = DBHelper.get().getDevicesDao().loadAll();
                } else {
                    List<String> deviceIds = new ArrayList<>();
                    for (Devices reDevice : reDevices) {
                        deviceIds.add(reDevice.getId());
                    }
                    devices = DBHelper.get().getDevicesDao().queryBuilder().where(DevicesDao.Properties.Id.notIn(deviceIds)).list();
                }
            }
        }

        mDevices.clear();
        if (!CheckUtil.isEmpty(devices)) {
            mDevices.addAll(devices);
        }

        mDeviceAdapter.notifyDataSetChanged();
    }


}
