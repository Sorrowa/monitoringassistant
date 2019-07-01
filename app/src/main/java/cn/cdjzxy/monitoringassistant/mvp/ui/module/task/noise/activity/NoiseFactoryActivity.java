package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;
import com.micheal.print.thread.ThreadPool;
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
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoisePointSketchMapFragment;

import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoiseSourceEditFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoiseSourceListFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.NoScrollViewPager;

/**
 * 噪声污染——"工业企业厂界噪声监测记录"
 */
public class NoiseFactoryActivity extends BaseTitileActivity<ApiPresenter> implements IView {

    @BindView(R.id.tab_view)
    CustomTab tabView;
    //    @BindView(R.id.layout)
//    LinearLayout layout;
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

    //噪声fragment对应值
    public static final int NOISE_FRAGMENT_INT_BASIC = 0;
    public static final int NOISE_FRAGMENT_INT_SOURCE = 1;
    public static final int NOISE_FRAGMENT_INT_SOURCE_EDIT = 2;
    public static final int NOISE_FRAGMENT_INT_POINT = 3;
    public static final int NOISE_FRAGMENT_INT_POINT_EDIT = 4;
    public static final int NOISE_FRAGMENT_INT_MONITOR = 5;
    public static final int NOISE_FRAGMENT_INT_MONITOR_EDIT = 6;
    public static final int NOISE_FRAGMENT_INT_MAP = 7;
    public static final int NOISE_FRAGMENT_INT_OTHER_FILE = 8;

    private int NOISE_FRAGMENT_INT = 0;

    //SharedPreferences
    public static final String NOISE_FRAGMENT_SOURCE_SHARE = "sourceShare";
    public static final String NOISE_FRAGMENT_POINT_SHARE = "pointShare";
    public static final String NOISE_FRAGMENT_MONITOR_SHARE = "monitorShare";
    public static final String NOISE_FRAGMENT_SHARE = "noiseShare";
    private NoiseBasicFragment mBasicFragment;
    NoiseSourceListFragment sourceListFragment;
    NoisePointListFragment pointListFragment;
    NoiseMonitorListFragment monitorListFragment;
    NoiseOtherFileFragment otherFileFragment;
    public static boolean isNeedSave = false;
    private FragmentManager mFragmentManager;


    @Subscriber(tag = EventBusTags.TAG_NOISE_FRAGMENT_TYPE)
    public void upFragment(int position) {
        openFragment(position);
    }

//    @Subscriber(tag = EventBusTags.TAG_NOISE_FRAGMENT_TYPE_POINT_EDIT)
//    public void upPointFragment(int position) {
//        openFragment(position);
//    }
//
//    @Subscriber(tag = EventBusTags.TAG_NOISE_FRAGMENT_TYPE_MONITOR_EDIT)
//    public void upMonitorFragment(int position) {
//        openFragment(position);
//    }

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
                back();
            }
        });

        titleBar.addRightAction(titleBar.new ImageAction(R.mipmap.ic_print, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage("待开发");
            }
        }));
        titleBar.addRightAction(titleBar.new ImageAction(R.mipmap.ic_save, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSample.getIsCanEdit()) {
                    saveMySample(false);
                } else {
                    showMessage("提示：当前采样单，不支持编辑");
                }
            }
        }));

    }


    private void showSaveDataDialog() {
        if (!mSample.getIsCanEdit()) {
            isNeedSave = false;
            finish();
        }

        final Dialog dialog = new AlertDialog.Builder(this)
                .setMessage("有数据更改，是否本地保存？")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveMySample(true);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).create();
        dialog.show();
    }

    @Override

    public void onBackPressed() {
        back();
    }

    private void back() {
        hideSoftInput();
        if (viewPager.getCurrentItem() == NOISE_FRAGMENT_INT_SOURCE_EDIT) {
            openFragment(NOISE_FRAGMENT_INT_SOURCE);
        } else if (viewPager.getCurrentItem() == NOISE_FRAGMENT_INT_POINT_EDIT) {
            openFragment(NOISE_FRAGMENT_INT_POINT);
        } else if (viewPager.getCurrentItem() == NOISE_FRAGMENT_INT_MONITOR_EDIT) {
            openFragment(NOISE_FRAGMENT_INT_MONITOR);
        } else if (isNeedSave) {
            showSaveDataDialog();
        } else {
            finish();
        }
    }

    /**
     * 动态隐藏软键盘
     */
    public void hideSoftInput() {
        View view = getCurrentFocus();
        if (view == null) view = new View(this);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mFragmentManager = this.getSupportFragmentManager();
        getIntentData();
        mProject = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
        if (isNewCreate) {
            mSample = SamplingUtil.createNoiseSample(projectId, formSelectId);
            mPrivateData = new NoisePrivateData();
        } else {
            mSample = DBHelper.get().getSamplingDao().queryBuilder().
                    where(SamplingDao.Properties.Id.eq(samplingId)).unique();
            if (mSample == null) {
                mSample = SamplingUtil.createNoiseSample(projectId, formSelectId);
                mPrivateData = new NoisePrivateData();
            } else {
                List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().
                        where(SamplingFileDao.Properties.SamplingId.eq(samplingId)).list();
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

        }
        initTabData();


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
        List<Tab> tabs = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Tab tab = new Tab();
            if (i == 0) {
                tab.setTabName("基本信息");
                tab.setSelected(true);
                tab.setResId(R.mipmap.icon_basic);
            } else if (i == 1) {
                tab.setTabName("主要噪声源");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_source);
            } else if (i == 2) {
                tab.setTabName("监测点位");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_point);
            } else if (i == 3) {
                tab.setTabName("监测数据");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_monotor);
            } else if (i == 4) {
                tab.setTabName("测点示意图");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_sketch_map);
            } else if (i == 5) {
                tab.setTabName("附件");
                tab.setSelected(false);
                tab.setResId(R.mipmap.icon_other);
            }
            tabs.add(tab);
        }
        tabView.setTabs(tabs);
        tabView.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                switch (position) {
                    case 0:
                        openFragment(NOISE_FRAGMENT_INT_BASIC);
                        break;
                    case 1:
                        openFragment(NOISE_FRAGMENT_INT_SOURCE);
                        break;
                    case 2:
                        openFragment(NOISE_FRAGMENT_INT_POINT);
                        break;
                    case 3:
                        openFragment(NOISE_FRAGMENT_INT_MONITOR);
                        break;
                    case 4:
                        openFragment(NOISE_FRAGMENT_INT_MAP);
                        break;
                    case 5:
                        openFragment(NOISE_FRAGMENT_INT_OTHER_FILE);
                        break;
                }
            }
        });

        mBasicFragment = new NoiseBasicFragment();
        sourceListFragment = new NoiseSourceListFragment();
        pointListFragment = new NoisePointListFragment();
        monitorListFragment = new NoiseMonitorListFragment();
        otherFileFragment = new NoiseOtherFileFragment();
        NoiseSourceEditFragment sourceEditFragment = new NoiseSourceEditFragment();
        NoisePointEditFragment pointEditFragment = new NoisePointEditFragment();
        NoiseMonitorEditFragment monitorEditFragment = new NoiseMonitorEditFragment();
        NoisePointSketchMapFragment sketchMap = new NoisePointSketchMapFragment();


        mFragments = new ArrayList<>();
        mFragments.add(mBasicFragment);//0
        mFragments.add(sourceListFragment);//1
        mFragments.add(sourceEditFragment);//2
        mFragments.add(pointListFragment);//3
        mFragments.add(pointEditFragment);//4
        mFragments.add(monitorListFragment);//5
        mFragments.add(monitorEditFragment);//6
        mFragments.add(sketchMap);//7
        mFragments.add(otherFileFragment);//8

//        mFragments = new ArrayList<>();
//        mFragments.add(mBasicFragment);//0
//        mFragments.add(sourceListFragment);//1
//        mFragments.add(pointListFragment);//3
//        mFragments.add(monitorListFragment);//5
//        mFragments.add(sketchMap);//7
//        mFragments.add(otherFileFragment);//8
//        mFragments.add(sourceEditFragment);//2
//        mFragments.add(pointEditFragment);//4
//        mFragments.add(monitorEditFragment);//6


        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(mFragmentAdapter);
        viewPager.setOffscreenPageLimit(1);
        openFragment(0);
    }

    /**
     * 切换Fragment页面
     *
     * @param position
     */
    private void openFragment(int position) {
        viewPager.setCurrentItem(position);
        NOISE_FRAGMENT_INT = position;
//        FragmentTransaction ft = mFragmentManager.beginTransaction();
//        Bundle mBundle = new Bundle();
//        switch (position) {
//            case NOISE_FRAGMENT_INT_BASIC:
//                ft.replace(R.id.frame_layout, mBasicFragment = new NoiseBasicFragment(), NoiseBasicFragment.class.getName());
//                ft.commit();
//                break;
//            case NOISE_FRAGMENT_INT_SOURCE:
//                ft.replace(R.id.frame_layout, sourceListFragment = new NoiseSourceListFragment(), NoiseBasicFragment.class.getName());
//                ft.commit();
//                break;
//            case NOISE_FRAGMENT_INT_POINT:
//                ft.replace(R.id.frame_layout, pointListFragment = new NoisePointListFragment(), NoiseBasicFragment.class.getName());
//                ft.commit();
//                break;
//            case NOISE_FRAGMENT_INT_MONITOR:
//                ft.replace(R.id.frame_layout, monitorListFragment = new NoiseMonitorListFragment(), NoiseBasicFragment.class.getName());
//                ft.commit();
//                break;
//            case NOISE_FRAGMENT_ING_MAP:
//                ft.replace(R.id.frame_layout, new NoisePonintSketchMap(), NoiseBasicFragment.class.getName());
//                ft.commit();
//                break;
//
//            case NOISE_FRAGMENT_INT_OTHER_FILE:
//                ft.replace(R.id.frame_layout, otherFileFragment = new NoiseOtherFileFragment(), NoiseBasicFragment.class.getName());
//                ft.commit();
//                break;
//            case NOISE_FRAGMENT_INT_SOURCE_EDIT:
//                ft.replace(R.id.frame_layout, new NoiseSourceEditFragment(), NoiseBasicFragment.class.getName());
//                ft.commit();
//                break;
//            case NOISE_FRAGMENT_INT_POINT_EDIT:
//                ft.replace(R.id.frame_layout, new NoisePointEditFragment(), NoiseBasicFragment.class.getName());
//                ft.commit();
//                break;
//            case NOISE_FRAGMENT_INT_MONITOR_EDIT:
//                ft.replace(R.id.frame_layout, new NoiseMonitorEditFragment(), NoiseBasicFragment.class.getName());
//                ft.commit();
//                break;
//        }

    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        openFragment(NOISE_FRAGMENT_INT);
        if (!mSample.getIsCanEdit()) {
            showMessage("提示：当前采样单，不支持编辑");
        }
    }

    @Override
    public void handleMessage(@NonNull Message message) {
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }


    public void saveMySample(boolean isFinish) {
        showLoading("保存中，请稍后");
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                saveMySample();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        showMessage("保存成功");
                        isNeedSave = false;
                        if (isFinish) {
                            finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

    public void showLoading(String msg) {
        showLoadingDialog(msg);
    }


    /**
     * 保存数据
     */
    public static void saveMySample() {

        mSample.setIsFinish(SamplingUtil.isSamplingFinsh(mSample));

        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.Id.eq(mSample.getId())).unique();
        mSample.setPrivateData(new Gson().toJson(mPrivateData));
        if (sampling != null) {
            DBHelper.get().getSamplingDao().update(mSample);
        } else {
            DBHelper.get().getSamplingDao().insertOrReplace(mSample);
        }
        List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().
                queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(mSample.getId())).list();
        if (!CheckUtil.isEmpty(samplingFiles)) {
            DBHelper.get().getSamplingFileDao().deleteInTx(samplingFiles);
        }
        if (!CheckUtil.isEmpty(mSample.getSamplingFiless())) {
            DBHelper.get().getSamplingFileDao().insertInTx(mSample.getSamplingFiless());
        }
        List<SamplingDetail> detailList = DbHelpUtils.getSamplingDetaiList(mSample.getId());
        if (!CheckUtil.isEmpty(detailList)) {
            DBHelper.get().getSamplingDetailDao().deleteInTx(detailList);
        }
        if (!CheckUtil.isEmpty(mSample.getSamplingDetailResults())) {
            DBHelper.get().getSamplingDetailDao().insertInTx(mSample.getSamplingDetailResults());
        }
        updateData();
    }

    /**
     * 更新保存的数据
     */
    public static void updateData() {
        mSample = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.Id.eq(mSample.getId())).unique();
        if (mSample == null) return;
        Gson gson = new Gson();
        mPrivateData = gson.fromJson(mSample.getPrivateData(), NoisePrivateData.class);
        if (mPrivateData == null) {
            mPrivateData = new NoisePrivateData();
        }
    }
}
