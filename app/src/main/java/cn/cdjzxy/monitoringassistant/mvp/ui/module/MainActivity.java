package cn.cdjzxy.monitoringassistant.mvp.ui.module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.widget.badgeview.BadgeView;
import com.wonders.health.lib.base.widget.dialogplus.DialogPlus;
import com.wonders.health.lib.base.widget.dialogplus.DialogPlusBuilder;
import com.wonders.health.lib.base.widget.dialogplus.ViewHolder;

import org.simple.eventbus.Subscriber;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MsgDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MainTabAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.launch.LoginActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.management.ManagementFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.msg.MsgActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.pointMap.PointMapFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.progress.ProgressFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.repository.RepositoryFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.scanCode.ScanCodeFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.setting.PwdModifyFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.setting.SettingFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.webview.WebFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.ExitHelper;
import cn.cdjzxy.monitoringassistant.utils.HawkUtil;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class MainActivity extends BaseTitileActivity<ApiPresenter> implements IView {

    public static final int TYPE_TASK = 2222;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.layout_container)
    FrameLayout layoutContainer;

    private BadgeView mBadgeView;

    private NumberProgressBar mNumberProgressBar;
    private TextView mTvHint;
    private DialogPlus mDialogPlus;

    private TitleBarView mTitleBarView;

    private ExitHelper.TwicePressHolder mExitHelper;

    private MainTabAdapter mMainTabAdapter;
    private List<Tab> mTabs;

    private FragmentManager mFragmentManager;

    private WebFragment mWebFragment;
    private TaskFragment mTaskFragment;
    private ScanCodeFragment mScanCodeFragment;
    private PointMapFragment mPointMapFragment;
    private ProgressFragment mProgressFragment;
    private RepositoryFragment mRepositoryFragment;
    private SettingFragment mSettingFragment;
    private ManagementFragment mManagementFragment;


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        this.mTitleBarView = titleBar;
        mTitleBarView.setLeftVisible(false);
        mTitleBarView.setTitleMainText("首页");
        mTitleBarView.addLeftAction(titleBar.new ViewAction(getTitleLeftView()));
        mTitleBarView.addRightAction(titleBar.new ViewAction(getTitleRightView(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtUtils.startActivity(MsgActivity.class);
            }
        }));

    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mFragmentManager = this.getSupportFragmentManager();

        initTabData();
        openFragment(0);
        mTitleBarView.setTitleMainText("嘉泽云监测");

        // 双击退出
        mExitHelper = new ExitHelper.TwicePressHolder(new ExitHelper.IExitInterface() {

            @Override
            public void showExitTip() {
                showMessage("再按一次退出程序");

            }

            @Override
            public void exit() {
                finish();
                ArtUtils.exitApp();
            }
        }, 3000);


    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Msg> msgs = DBHelper.get().getMsgDao().queryBuilder().where(MsgDao.Properties.MsgStatus.eq(0)).list();
        if (!CheckUtil.isEmpty(msgs)) {
            mBadgeView.setVisibility(View.VISIBLE);
        } else {
            mBadgeView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (NetworkUtil.isNetworkAvailable(this) && !HawkUtil.getBoolean("isUpdated")) {
            showDialog();
            mPresenter.getDevices(Message.obtain(this, new Object()));
            mPresenter.getMethods(Message.obtain(this, new Object()));
            mPresenter.getMonItems(Message.obtain(this, new Object()));
            mPresenter.getTags(Message.obtain(this, new Object()));
            mPresenter.getMonItemTagRelation(Message.obtain(this, new Object()));
            mPresenter.getMethodTagRelation(Message.obtain(this, new Object()));
            mPresenter.getMonItemMethodRelation(Message.obtain(this, new Object()));
            mPresenter.getMethodDevRelation(Message.obtain(this, new Object()));
            mPresenter.getRight(Message.obtain(this, new Object()));
            mPresenter.getEnvirPoint(Message.obtain(this, new Object()));
            mPresenter.getEnterRelatePoint(Message.obtain(this, new Object()));
            mPresenter.getEnterprise(Message.obtain(this, new Object()));
            mPresenter.getDic(Message.obtain(this, new Object()), 7);
            mPresenter.getWeather(Message.obtain(this, new Object()));
            mPresenter.getUser(Message.obtain(this, new Object()));
            mPresenter.getUnit(Message.obtain(this, new Object()));
            mPresenter.getMsgs(Message.obtain(this, new Object()));
            mPresenter.getFormSelect(Message.obtain(this, new Object()));
            mPresenter.getSamplingStantd(Message.obtain(this, new Object()));
            mPresenter.getMyTasks(Message.obtain(this, new Object()));
            HawkUtil.putBoolean("isUpdated", true);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return onBack() ? false : mExitHelper.onKeyDown(keyCode, event);
        }
        return mExitHelper.onKeyDown(keyCode, event);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        switch (message.what) {
            case Message.RESULT_FAILURE:
                if (mDialogPlus != null) {
                    mDialogPlus.dismiss();
                }
                break;
            case Message.RESULT_OK:
                updateProgress((int) message.obj);
                break;
            case TYPE_TASK:
                updateProgress(ApiPresenter.PROGRESS);
                mPresenter.getSampling(Message.obtain(this, new Object()), (List<String>) message.obj);
                break;
        }
    }

    /**
     * 更新进度
     *
     * @param addValue
     */
    private void updateProgress(int addValue) {
        if (!mDialogPlus.isShowing()) {
            return;
        }

        int progress = mNumberProgressBar.getProgress() + addValue;
        mNumberProgressBar.setProgress(progress);

        if (progress >= 100) {
            mDialogPlus.dismiss();
        }
    }

    /**
     * 获取标题右边View
     *
     * @return
     */
    private View getTitleRightView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_main_title_right, null);
        mBadgeView = view.findViewById(R.id.red_hot);
        return view;
    }

    /**
     * 获取标题左边View
     *
     * @return
     */
    private View getTitleLeftView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_main_title_left, null);
        TextView mTvUserName = view.findViewById(R.id.tv_username);
        TextView mTvDate = view.findViewById(R.id.tv_date);
        String userName = UserInfoHelper.get().getUserName();
        mTvUserName.setText(CheckUtil.isEmpty(userName) ? "测试数据" : userName + ", 欢迎你");
        mTvDate.setText(DateUtils.getDate() + " " + DateUtils.getWeek());
        return view;
    }

    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        ArtUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return false;
            }
        });

        mTabs = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Tab tab = new Tab();
            if (i == 0) {
                tab.setTabName("任务");
                tab.setResId(R.mipmap.ic_task_nor);
                tab.setSelectedResId(R.mipmap.ic_task_hov);
                tab.setSelected(true);
            } else if (i == 1) {
                tab.setTabName("扫码");
                tab.setResId(R.mipmap.ic_scan_nor);
                tab.setSelectedResId(R.mipmap.ic_scan_hov);
                tab.setSelected(false);
            } else if (i == 2) {
                tab.setTabName("点位地图");
                tab.setResId(R.mipmap.ic_point_nor);
                tab.setSelectedResId(R.mipmap.ic_point_hov);
                tab.setSelected(false);
            } else if (i == 3) {
                tab.setTabName("企业管理");
                tab.setResId(R.mipmap.ic_manager_nor);
                tab.setSelectedResId(R.mipmap.ic_manager_hov);
                tab.setSelected(false);
            } else if (i == 4) {
                tab.setTabName("知识库");
                tab.setResId(R.mipmap.ic_knowledge_nor);
                tab.setSelectedResId(R.mipmap.ic_knowledge_hov);
                tab.setSelected(false);
            } else if (i == 5) {
                tab.setTabName("进度");
                tab.setResId(R.mipmap.ic_progress_nor);
                tab.setSelectedResId(R.mipmap.ic_progress_hov);
                tab.setSelected(false);
            } else if (i == 6) {
                tab.setTabName("设置");
                tab.setResId(R.mipmap.ic_setting_nor);
                tab.setSelectedResId(R.mipmap.ic_setting_hov);
                tab.setSelected(false);
            }

            mTabs.add(tab);
        }

        mMainTabAdapter = new MainTabAdapter(mTabs);
        mMainTabAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateTitle(position);
                updateTabStatus(position);
                openFragment(position);

            }
        });
        recyclerView.setAdapter(mMainTabAdapter);
    }

    /**
     * 更新标题
     *
     * @param position
     */
    private void updateTitle(int position) {
        if (position == 0) {
            mTitleBarView.setTitleMainText("嘉泽云监测");
        } else {
            mTitleBarView.setTitleMainText(mTabs.get(position).getTabName());
        }

    }


    /**
     * 更新tabView选中状态
     *
     * @param position
     */
    private void updateTabStatus(int position) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (i == position) {
                mTabs.get(i).setSelected(true);
            } else {
                mTabs.get(i).setSelected(false);
            }
        }
        mMainTabAdapter.notifyDataSetChanged();

    }

    /**
     * 切换Fragment页面
     *
     * @param position
     */
    private void openFragment(int position) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();

        Bundle mBundle = new Bundle();
        switch (position) {

            case 0://任务
                ft.replace(R.id.layout_container, mTaskFragment = new TaskFragment(), TaskFragment.class.getName());
                break;

            case 1://扫码
                ft.replace(R.id.layout_container, mScanCodeFragment = new ScanCodeFragment(), ScanCodeFragment.class.getName());
                break;

            case 2://点位地图
                ft.replace(R.id.layout_container, mWebFragment = WebFragment.getInstance(mBundle), WebFragment.class.getName());
                //                mBundle.putString(WebFragment.URL_KEY, BuildConfig.SERVER_IP + "/GIS/APPGIS");
                mBundle.putString(WebFragment.URL_KEY, "https://www.amap.com/");
                break;

            case 3://企业管理

                ft.replace(R.id.layout_container, mWebFragment = WebFragment.getInstance(mBundle), WebFragment.class.getName());
                //                mBundle.putString(WebFragment.URL_KEY, BuildConfig.SERVER_IP + "/Enterprise/APP");
                mBundle.putString(WebFragment.URL_KEY, "https://www.baidu.com");
                break;

            case 4://知识库
                ft.replace(R.id.layout_container, mRepositoryFragment = new RepositoryFragment(), RepositoryFragment.class.getName());
                break;

            case 5://进度
                ft.replace(R.id.layout_container, mProgressFragment = new ProgressFragment(), ProgressFragment.class.getName());
                break;

            case 6://设置
                ft.replace(R.id.layout_container, mSettingFragment = new SettingFragment(), SettingFragment.class.getName());

//                String path = getSystemFilePath(this) + "/test4.jpeg";
//                Log.e("Path", path);
//                File file = new File(path);
//                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//
//                MultipartBody.Part fileData = MultipartBody.Part.createFormData("File", file.getName(), requestBody);
//
//                HashMap<String, RequestBody> map = new HashMap<>();
//                map.put("token", RequestBody.create(MediaType.parse("text/plain"), UserInfoHelper.get().getUser().getToken()));
//
//                mPresenter.uploadFile(Message.obtain(this, new Object()), fileData, map);
                break;
            case 7://修改密码
                ft.replace(R.id.layout_container, new PwdModifyFragment(), PwdModifyFragment.class.getName());
                break;
            default:
                break;
        }
        ft.commit();
    }

    private static String getSystemFilePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            Log.e("getSystemFilePath1", cachePath);
//            cachePath = context.getExternalCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
//            Log.e("getSystemFilePath2",cachePath);
        } else {
            cachePath = context.getFilesDir().getAbsolutePath();
//            Log.e("getSystemFilePath3",cachePath);
            cachePath = context.getCacheDir().getPath();//也可以这么写，只是返回的路径不一样，具体打log看
//            Log.e("getSystemFilePath4",cachePath);
        }

        return cachePath;
    }


    @Subscriber(tag = EventBusTags.TAG_MODIFY_PWD)
    private void updateSettingFragment(int position) {
        openFragment(position);
    }

    private boolean onBack() {
        Fragment fragment = null;
        fragment = getSupportFragmentManager().findFragmentByTag(PwdModifyFragment.class.getName());
        if (!CheckUtil.isNull(fragment)) {
            openFragment(6);
            return true;
        }
        return false;
    }

    private void showDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_download, null);
        mNumberProgressBar = view.findViewById(R.id.progressbar);
        mTvHint = view.findViewById(R.id.tv_hint);
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(this);
        dialogPlusBuilder.setContentHolder(new ViewHolder(view));
        dialogPlusBuilder.setGravity(Gravity.CENTER);
        dialogPlusBuilder.setCancelable(false);
        dialogPlusBuilder.setContentWidth(700);
        mDialogPlus = dialogPlusBuilder.create();
        mDialogPlus.show();
    }


}
