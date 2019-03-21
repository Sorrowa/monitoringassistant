package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PointAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

/**
 * 采样点位
 */

public class PointActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private String projectId;

    private PointAdapter mPointAdapter;

    private Project mProject;
    private List<ProjectDetial> mProjectDetials = new ArrayList<>();
    private Map<String, ProjectDetial> mStringProjectDetialMap = new HashMap<>();


    private static final int authBaseRequestCode = 1;
    private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";
    static final String ROUTE_PLAN_NODE = "routePlanNode";
    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private String mSDCardPath = null;

    private LocationClient locationClient;
    public BDLocation bdLocation;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("采样点位");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_point;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        projectId = getIntent().getStringExtra("projectId");
        mProject = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
        initPointData();
        getData();

        //初始化导航
        if (initDirs()) {
            initNavi();
        }
        //初始化定位
        initLocation();
    }

    /**
     * 初始化Tab数据
     */
    private void initPointData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this));
        mPointAdapter = new PointAdapter(this, mProjectDetials, mProject.getCanSamplingEidt());
        mPointAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (mProject.getCanSamplingEidt()) {
                //if (!mProject.getCanSamplingEidt()) {
                    Intent intent = new Intent(PointActivity.this, ProgramModifyActivity.class);
                    intent.putExtra("projectDetailId", mProjectDetials.get(position).getId());
                    intent.putExtra("projectId", projectId);
                    ArtUtils.startActivity(intent);
                }

            }
        });
        recyclerview.setAdapter(mPointAdapter);
    }


    @Subscriber(tag = EventBusTags.TAG_PROGRAM_MODIFY)
    private void updateData(boolean isModified) {
        getData();
    }

    private void getData() {
        mProjectDetials.clear();
        mStringProjectDetialMap.clear();
        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId)).list();
        if (!CheckUtil.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials) {
                if (CheckUtil.isNull(mStringProjectDetialMap.get(projectDetial.getProjectContentId()))) {
                    mStringProjectDetialMap.put(projectDetial.getProjectContentId(), projectDetial);
                } else {
                    ProjectDetial projectDetial1 = mStringProjectDetialMap.get(projectDetial.getProjectContentId());

                    if (!projectDetial1.getAddressId().contains(projectDetial.getAddressId())) {
                        projectDetial1.setAddressId(projectDetial1.getAddressId() + "," + projectDetial.getAddressId());
                        projectDetial1.setAddress(projectDetial1.getAddress() + "," + projectDetial.getAddress());
                    }

                    if (!projectDetial1.getMonItemId().contains(projectDetial.getMonItemId())) {
                        projectDetial1.setMonItemId(projectDetial1.getMonItemId() + "," + projectDetial.getMonItemId());
                        projectDetial1.setMonItemName(projectDetial1.getMonItemName() + "," + projectDetial.getMonItemName());
                    }

                    mStringProjectDetialMap.put(projectDetial1.getProjectContentId(), projectDetial1);
                }
            }

            for (String key : mStringProjectDetialMap.keySet()) {
                mProjectDetials.add(mStringProjectDetialMap.get(key));
            }

        }
        mPointAdapter.refreshInfos(mProjectDetials);
        //mPointAdapter.notifyDataSetChanged();
    }


    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void initNavi() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(this,
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        ArtUtils.makeText(PointActivity.this, result);
                    }

                    @Override
                    public void initStart() {
                        ArtUtils.makeText(PointActivity.this, "百度导航引擎初始化开始");
                    }

                    @Override
                    public void initSuccess() {
                        ArtUtils.makeText(PointActivity.this, "百度导航引擎初始化成功");
                        //hasInitSuccess = true;
                    }

                    @Override
                    public void initFailed() {
                        ArtUtils.makeText(PointActivity.this, "百度导航引擎初始化失败");
                    }
                });

    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = this.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == authBaseRequestCode) {
            for (int ret : grantResults) {
                if (ret == 0) {
                    continue;
                } else {
                    ArtUtils.makeText(this, "缺少导航基本的权限!");
                    return;
                }
            }
            initNavi();
        }
    }

    private void initLocation() {
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(new MyLocationListener());
        //设置option
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        //option.setCoorType("GCJ02");
        option.setScanSpan(3000);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(true);
        locationClient.setLocOption(option);
        locationClient.start();

    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            PointActivity.this.bdLocation = location;
        }
    }


}
