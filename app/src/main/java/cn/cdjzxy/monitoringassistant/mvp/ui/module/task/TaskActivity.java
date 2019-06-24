package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.services.TraceService;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;
import cn.cdjzxy.monitoringassistant.utils.RxLocationTool;

/**
 * 任务列表
 */

public class TaskActivity extends BaseTitileActivity<ApiPresenter> {

    public static final int TASK_REQUEST_CODE = 10;
    public static final int TASK_RESULT_CODE = 11;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.check_box)
    CheckBox checkBox;

    private TaskAdapter mTaskAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("采样任务");
        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(TaskSearchActivity.class);
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
        return R.layout.activity_task;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initTaskData();
        initTraceCheckBox();
    }

    /**
     * 初始化导航控件
     */
    private void initTraceCheckBox() {
        //权限检查
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }

        checkBox.setChecked(UserInfoHelper.get().getUserTraceOpen());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "onCheckedChanged: " + isChecked);
                if (isChecked) {
                    showDialog("开启轨迹提示", "轨迹开启后，“嘉泽云小助手”将在后台记录的轨迹信息。", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if ((RxLocationTool.hasGPSDevice(TaskActivity.this) ||       //设备支持gps
                                    RxLocationTool.isGpsEnabled(TaskActivity.this)) ||    //设备已经开启gps
                                    NetworkUtil.isNetworkAvailable(TaskActivity.this)    //设备有网络
                                    ) {
                                startTraceService();
                                //startUploadTrace(true);
                                UserInfoHelper.get().saveUserTraceOpen(true);
                            } else {
                                showDialog("轨迹开启失败", "请保持设备有网或者设备支持GPS定位，才能开启轨迹", null);
                                checkBox.setChecked(false);
                                UserInfoHelper.get().saveUserTraceOpen(false);
                            }
                        }
                    });
                } else {
                    stopTraceService();
                    UserInfoHelper.get().saveUserTraceOpen(false);
                }
            }
        });
    }


    /**
     * 关闭轨迹
     */
    private void stopTraceService() {
        //开启轨迹服务
        Intent intent = new Intent();
        intent.setClass(this, TraceService.class);
        stopService(intent);
        ArtUtils.makeText(this, "轨迹导航已关闭");
    }


    /**
     * 初始化Tab数据
     */
    private void initTaskData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this));
        final List<Project> projects = DbHelpUtils.getProjectListDesc();
        if (!CheckUtil.isEmpty(projects)) {
            mTaskAdapter = new TaskAdapter(projects);
            mTaskAdapter.setOnItemClickListener((view, viewType, data, position) -> {
                if (UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_See_Num)) {
                    Intent intent = new Intent(TaskActivity.this, TaskDetailActivity.class);
                    intent.putExtra("taskId", projects.get(position).getId());
                    startActivityForResult(intent, TASK_REQUEST_CODE);
                } else {
                    showNoPermissionDialog("才能进行表单查看。", UserInfoAppRight.APP_Permission_Sampling_See_Name);
                }

            });
            recyclerview.setAdapter(mTaskAdapter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TASK_REQUEST_CODE && resultCode == TASK_RESULT_CODE) {
            final List<Project> projects = DBHelper.get().getProjectDao().queryBuilder().orderAsc(ProjectDao.Properties.PlanEndTime).list();
            mTaskAdapter.refreshInfos(projects);
        }
    }

}


