package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.google.gson.Gson;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.widget.dialogplus.DialogPlus;
import com.wonders.health.lib.base.widget.dialogplus.DialogPlusBuilder;
import com.wonders.health.lib.base.widget.dialogplus.OnClickListener;
import com.wonders.health.lib.base.widget.dialogplus.ViewHolder;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseSamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetailYQFs;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.FileInfoData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectPlan;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.UserDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FormAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskDetailAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.Constants;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.MyTextUtils;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.utils.SubmitDataUtil;
import cn.cdjzxy.monitoringassistant.utils.UploadDataUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.IosDialog;
import cn.cdjzxy.monitoringassistant.widgets.OperateTipsDialog;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 任务详情
 */

public class TaskDetailActivity extends BaseTitileActivity<ApiPresenter> implements IView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.tv_task_name)
    TextView tvTaskName;
    @BindView(R.id.tv_task_time_range)
    TextView tvTaskTimeRange;
    @BindView(R.id.tv_task_num)
    TextView tvTaskNum;
    @BindView(R.id.tv_task_point)
    TextView tvTaskPoint;
    @BindView(R.id.tv_task_project_num)
    TextView tvTaskProjectNum;
    @BindView(R.id.tv_task_type)
    TextView tvTaskType;
    @BindView(R.id.tv_task_person)
    TextView tvTaskPerson;
    @BindView(R.id.tv_task_start_time)
    TextView tvTaskStartTime;
    @BindView(R.id.tv_sampling_point_count)
    TextView tvSamplingPointCount;
    @BindView(R.id.cb_all)
    ImageView cbAll;

    /**
     * 降水表单路径/路径
     */
    public static final String PATH_PRECIPITATION = "/FormTemplate/FILL_JS_GAS_XD";
    public static final String NAME_PRECIPITATION = "降水采样及样品交接记录（新都）";

    /**
     * 废水表单路径/路径
     */
    public static final String PATH_WASTEWATER = "/FormTemplate/FILL_WATER_NEW_XD";
    public static final String NAME_WASTEWATER = "水和废水样品采集与交接记录（新都）";

    /**
     * 仪器法表单路径/路径
     */
    public static final String PATH_INSTRUMENTAL = "/FormTemplate/FILL_YQF_WATER";
    public static final String NAME_INSTRUMENTAL = "现场监测仪器法";

    /**
     * 工业企业厂界噪声监测记录
     */
    public static final String PATH_NOISE_FACTORY = "/FormTemplate/FILL_GYZS_VOICE_XD";
    public static final String NAME_NOISE_FACTORY = "工业企业厂界噪声监测记录";

    /**
     * 最大文件上传大小，单位（KB）
     */
    public static final int MaxFileUploadSize = 100;

    private TitleBarView mTitleBarView;
    private TaskDetailAdapter mTaskDetailAdapter;

    private List<Tags> mTags = new ArrayList<>();
    private List<Sampling> mSamplings = new ArrayList<>();

    private Project mProject;

    private DialogPlus mDialogPlus;
    private CustomTab mCustomTab;
    private RecyclerView mRecyclerView;
    private ImageView mBtnClose;

    private List<Tags> mFirstTags = new ArrayList<>();
    private List<Tab> mTagNames = new ArrayList<>();

    private List<FormSelect> mDialogFormSelects = new ArrayList<>();
    private FormAdapter mFormAdapter;


    private String mTagId;


    private boolean isSelecteAll = false;
    //提交采样单时候使用
    private Sampling sampling;

    private EditText mEtComment;
    private TextView mTvCancel;
    private TextView mTvOk;
    //是否批量提交
    private boolean isBatchUpload = false;
    //是否提交
    private int samplingIndex;
    private boolean isSubmit;//true 提交 false 保存

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mTitleBarView = titleBar;
        mTitleBarView.setTitleMainText("采样任务");
        //2019年5月17日 项目经理：敬蓉说采样完结 没有这个功能了
//        mTitleBarView.setRightText("采样完结");
//        mTitleBarView.setOnRightTextClickListener(v -> showFinishDialog());
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_task_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initTask();
        initTabLayout();
        initTaskFormData();
    }

    @Override
    public void showLoading() {
        showLoadingDialog("数据提交中...", false);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
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
                batchUploadFinish();
//                showMessage("操作失败！");
                break;
            case Message.RESULT_OK:
                showMessage("采样完结！");
                //更改Project状态
                if (!CheckUtil.isNull(mProject)) {

                }
                Intent intent = new Intent();
                setResult(TaskActivity.TASK_RESULT_CODE, intent);
                EventBus.getDefault().post(true, EventBusTags.TAG_PROJECT_FINISH);
                finish();
                break;
            case Constants.NET_RESPONSE_CODE_259:
                String id = message.str == null || message.equals("") ? sampling.getId() : message.str;//服务端id
                updateSampling(id);
                setUploadAltInfo(String.format("采样单[%s]上传成功！", sampling.getSamplingNo()));
                if (!uploadNextSampling()) {
                    batchUploadFinish();
                    showMessage("数据提交成功");
                    getSampling(mTagId);
                }
                break;
            case Constants.NET_RESPONSE_SAMPLING_DIFFER:
                batchUploadFinish();
                if (isBatchUpload) {
                    multiCommitTipsOperate();
                } else {
                    commitSamplingDataConflictOperate();
                }
                break;
            case Constants.VIEW_UPDATE_LOADING_TXT:
                if (message.obj != null) {
                    setLoadingDialogText(message.obj.toString());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 更新表单
     *
     * @param id 服务端的id:  因为app端id是主键，所以这里不能直接替换  要先把之前的文件或者其他信息保存后才能本地id相关的数据
     */
    private void updateSampling(String id) {
        if (isSubmit) {
            sampling.setStatusName("已提交");
            sampling.setIsCanEdit(false);
            sampling.setStatus(7);
            sampling.setUploadSave(true);
            sampling.setIsUpload(true);
        } else {
            sampling.setIsUpload(false);
            sampling.setStatus(0);//app端新增 代表已经保存到服务器
            sampling.setUploadSave(true);
            sampling.setStatusName("服务器已保存,等待提交");
        }
        sampling.setSubmitDate(DateUtils.getDate());
        sampling.setDeleteFiles("");//清空删除文件ID字符串
        sampling.setSelected(false);
        sampling.setIsCanEdit(SamplingUtil.setSampIsCanEdit(sampling));

        //标记文件已上传
        List<SamplingFile> updateFiles = new ArrayList<>();
        List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().
                where(SamplingFileDao.Properties.SamplingId.eq(sampling.getId()),
                        SamplingFileDao.Properties.IsUploaded.eq(false)).list();
        for (SamplingFile sf : samplingFiles) {
            if (!sf.getIsUploaded()) {
                sf.setIsUploaded(true);
            }
            sf.setSamplingId(id);
            updateFiles.add(sf);
        }
        if (id.equals(sampling.getId())) {//服务端返回的id和本地id一致  直接更新
            if (updateFiles.size() > 0) {
                DBHelper.get().getSamplingFileDao().updateInTx(updateFiles);
            }
            if (DbHelpUtils.getDbSampling(sampling.getId()) != null)
                DBHelper.get().getSamplingDao().update(sampling);
        } else {////服务端返回的id和本地id一致 生成新的数据  删除之前的
            if (!CheckUtil.isEmpty(samplingFiles)) {
                DBHelper.get().getSamplingFileDao().deleteInTx(samplingFiles);
            }
            DBHelper.get().getSamplingFileDao().insertInTx(updateFiles);
            List<SamplingDetail> samplingDetailsList = DbHelpUtils.getSamplingDetaiList(sampling.getId());
            if (!CheckUtil.isEmpty(samplingDetailsList)) {
                for (SamplingDetail detail : samplingDetailsList) {
                    detail.setSamplingId(id);
                }
                DBHelper.get().getSamplingDetailDao().updateInTx(samplingDetailsList);
            }
            List<SamplingFormStand> samplingFormStandsList = DbHelpUtils.getSamplingFormStandListForSampId(sampling.getId());
            if (!CheckUtil.isEmpty(samplingFormStandsList)) {
                for (SamplingFormStand stand : samplingFormStandsList) {
                    stand.setSamplingId(id);
                }
                DBHelper.get().getSamplingFormStandDao().updateInTx(samplingFormStandsList);
            }
            List<SamplingContent> samplingContentList = DbHelpUtils.getSamplingContentList(sampling.getId());
            if (!CheckUtil.isEmpty(samplingContentList)) {
                for (SamplingContent content : samplingContentList) {
                    content.setSamplingId(id);
                }
                DBHelper.get().getSamplingContentDao().updateInTx(samplingContentList);
            }
            List<SamplingDetailYQFs> yqFsList = DbHelpUtils.getSamplingDetailYQFsList(sampling.getId());
            if (!CheckUtil.isEmpty(yqFsList)) {
                for (SamplingDetailYQFs yqFs : yqFsList) {
                    yqFs.setSamplingId(id);
                    DBHelper.get().getSamplingDetailYQFsDao().update(yqFs);
                }

            }
            if (DbHelpUtils.getDbSampling(sampling.getId()) != null) {
                DBHelper.get().getSamplingDao().delete(sampling);
            }
            sampling.setId(id);
            if (DbHelpUtils.getDbSampling(id) != null) {
                DBHelper.get().getSamplingDao().update(sampling);
            } else
                DBHelper.get().getSamplingDao().insert(sampling);
        }
    }

    private void initTask() {
        mProject = DBHelper.get().getProjectDao().queryBuilder().
                where(ProjectDao.Properties.Id.eq(getIntent().
                        getStringExtra("taskId"))).unique();
        bindView(mProject);
    }

    private void bindView(Project data) {
        if (!CheckUtil.isNull(data)) {
            mTitleBarView.setTitleMainText(data.getName() + "采样任务");
            tvTaskName.setText(data.getName());
            if (CheckUtil.isEmpty(data.getPlanBeginTime()) || CheckUtil.isEmpty(data.getPlanEndTime())) {
                tvTaskTimeRange.setText("未设置采样计划");
            } else {
                String currentTime = DateUtils.getDate();
                String endTime = data.getPlanEndTime();
                int lastDays = DateUtils.getLastDays(currentTime, endTime.split(" ")[0]);
                if (lastDays <= 1) {
                    tvTaskTimeRange.setTextColor(Color.parseColor("#ff0000"));
                } else if (lastDays <= 3) {
                    tvTaskTimeRange.setTextColor(Color.parseColor("#ffbe00"));
                } else {
                    tvTaskTimeRange.setTextColor(Color.parseColor("#333333"));
                }
                tvTaskTimeRange.setText(data.getPlanBeginTime().split(" ")[0].replace("-", "/") + "~" + data.getPlanEndTime().split(" ")[0].replace("-", "/"));
            }

            StringBuilder users = new StringBuilder();
            List<String> userIds = data.getSamplingUser();
            if (!CheckUtil.isEmpty(userIds)) {
                List<User> userList = DBHelper.get().getUserDao().queryBuilder().where(UserDao.Properties.Id.in(userIds)).list();
                if (!CheckUtil.isEmpty(userList)) {
                    for (User user : userList) {
                        users.append(user.getName() + ",");
                    }
                }
            }

            StringBuilder monItems = new StringBuilder();
            StringBuilder points = new StringBuilder();

            List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(data.getId())).list();
            if (!CheckUtil.isEmpty(projectDetials)) {
                for (ProjectDetial projectDetial : projectDetials) {
                    if (!monItems.toString().contains(projectDetial.getMonItemName())) {
                        monItems.append(projectDetial.getMonItemName() + ",");
                    }

                    if (!points.toString().contains(projectDetial.getAddress())) {
                        points.append(projectDetial.getAddress() + ",");
                    }
                }
            }

            if (users.lastIndexOf(",") > 0) {
                users.deleteCharAt(users.lastIndexOf(","));
            }

            if (monItems.lastIndexOf(",") > 0) {
                monItems.deleteCharAt(monItems.lastIndexOf(","));
            }

            if (points.lastIndexOf(",") > 0) {
                points.deleteCharAt(points.lastIndexOf(","));
            }

            tvTaskNum.setText("任务编号:" + data.getProjectNo());
            tvTaskPoint.setText("点位:" + points.toString());
            tvTaskProjectNum.setText("项目:" + monItems.toString());
            tvTaskType.setText("样品性质:" + data.getMonType());
            tvTaskPerson.setText("人员：" + users.toString());
            tvTaskStartTime.setText("下达:" + data.getAssignDate().split(" ")[0].replace("-", "/"));

            if (points.toString().contains(",")) {
                tvSamplingPointCount.setText("共" + points.toString().split(",").length + "个");
            } else {
                if (CheckUtil.isEmpty(points.toString())) {
                    tvSamplingPointCount.setText("共" + 0 + "个");
                } else {
                    tvSamplingPointCount.setText("共" + 1 + "个");
                }
            }
        }
    }

    private void initTabLayout() {
        List<Tags> tags = DBHelper.get().getTagsDao().loadAll();
        if (!CheckUtil.isEmpty(tags)) {
            for (Tags tag : tags) {
                if (tag.getLevel() == 0) {
                    tabLayout.addTab(tabLayout.newTab().setText(tag.getName()));
                    mTags.add(tag);
                }
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (isSelecteAll) {
                    cbAll.setImageResource(R.mipmap.ic_cb_nor);
                    //updateSamplingAllStatus(false);
                    isSelecteAll = false;
                }
                mTagId = mTags.get(tab.getPosition()).getId();
                getSampling(mTagId);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(() -> {
            try {
                //拿到tabLayout的mTabStrip属性
                Field mTabStripField = tabLayout.getClass().getDeclaredField("mTabStrip");
                mTabStripField.setAccessible(true);

                LinearLayout mTabStrip = (LinearLayout) mTabStripField.get(tabLayout);

                int dp10 = getResources().getDimensionPixelSize(R.dimen.dp_12);

                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);

                    //拿到tabView的mTextView属性
                    Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                    mTextViewField.setAccessible(true);

                    TextView mTextView = (TextView) mTextViewField.get(tabView);

                    tabView.setPadding(0, 0, 0, 0);

                    //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                    int width = 0;
                    width = mTextView.getWidth();
                    if (width == 0) {
                        mTextView.measure(0, 0);
                        width = mTextView.getMeasuredWidth();
                    }

                    //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                    params.width = width + getResources().getDimensionPixelSize(R.dimen.dp_8);
                    params.leftMargin = dp10;
                    params.rightMargin = dp10;
                    tabView.setLayoutParams(params);

                    tabView.invalidate();
                }

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        });

    }


    /**
     * 初始化Tab数据
     */
    private void initTaskFormData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mTaskDetailAdapter = new TaskDetailAdapter(mSamplings, new TaskDetailAdapter.OnSamplingListener() {
            @Override
            public void onSelected(View view, int position) {
                updateSamplingStatus(position);
            }

            @Override
            public void onClick(View view, int position) {
                //这里改用switch
                Intent intent = new Intent();
                switch (mSamplings.get(position).getFormPath()) {
                    case PATH_PRECIPITATION:
                        //降水采样及样品交接记录（新都）
                        intent.setClass(TaskDetailActivity.this, PrecipitationActivity.class);
                        intent.putExtra("samplingId", mSamplings.get(position).getId());
                        break;
                    case PATH_WASTEWATER:
                        //水和废水样品采集与交接记录（新都）
                        intent.setClass(TaskDetailActivity.this, WastewaterActivity.class);
                        intent.putExtra("samplingId", mSamplings.get(position).getId());
                        break;
                    case PATH_INSTRUMENTAL:
                        //现场监测仪器法
                        intent.setClass(TaskDetailActivity.this, InstrumentalActivity.class);
                        intent.putExtra("samplingId", mSamplings.get(position).getId());
                        break;
                    case PATH_NOISE_FACTORY://工业企业厂界噪声监测记录
                        intent.setClass(TaskDetailActivity.this, NoiseFactoryActivity.class);
                        intent.putExtra("samplingId", mSamplings.get(position).getId());
                        break;
                    default:
                        ArtUtils.makeText(TaskDetailActivity.this, "功能开发中");
                        return;
                }
                intent.putExtra("projectId", mProject.getId());
                intent.putExtra("isNewCreate", false);
                ArtUtils.startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {
                showDeleteDialog(position);
            }

            @Override
            public void onUpload(View view, int position) {
                if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Upload_Num)) {
                    showNoPermissionDialog("才能进行表单上传。", UserInfoAppRight.APP_Permission_Sampling_Upload_Name);
                    return;
                }
                isBatchUpload = false;
                isSubmit = false;
                if (mProject.getCanSamplingEidt() && mProject.getIsEditProjectContent()) {
                    uploadProjecteContentData(true);
                }

                Sampling sampling = mSamplings.get(position);
                if (sampling == null || !sampling.getIsFinish()) {
                    String finishAlt = "";
                    if (PATH_PRECIPITATION.equals(sampling.getFormPath())) {
                    } else if (PATH_WASTEWATER.equals(sampling.getFormPath())) {
                    } else if (PATH_INSTRUMENTAL.equals(sampling.getFormPath())) {
                        finishAlt = SamplingUtil.isInstrumentSamplingFinish(sampling);
                    }

                    showMessage("请先完善采样单信息后在进行保存！" + finishAlt);

                    return;
                }

                // 上传数据
                uploadSampFormData(sampling, false);
//                MyTextUtils.uploadMySampleForData(mPresenter,mContext);
            }
        });
        recyclerview.setAdapter(mTaskDetailAdapter);

        mTagId = mTags.get(0).getId();
        getSampling(mTagId);
    }


    @OnClick({R.id.btn_sampling_point, R.id.btn_add_sampling, R.id.btn_submit, R.id.cb_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sampling_point:
                if (UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Plan_See_Num)) {
                    Intent intent = new Intent(this, PointActivity.class);
                    intent.putExtra("projectId", mProject.getId());
                    startActivity(intent);
                } else {
                    showNoPermissionDialog("才能进行采样方案查看。", UserInfoAppRight.APP_Permission_Plan_See_Name);
                }
                break;
            case R.id.btn_add_sampling:
                if (UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Add_Num)) {
                    showAddDialog();
                } else {
                    showNoPermissionDialog("才能进行表单新增。", UserInfoAppRight.APP_Permission_Sampling_Add_Name);
                }
                break;
            case R.id.btn_submit:
                if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Submit_Num)) {
                    showNoPermissionDialog("才能进行表单提交。", UserInfoAppRight.APP_Permission_Sampling_Submit_Name);
                    return;
                }
                if (!hasSelectSample()) {
                    showMessage("请勾选已完成并未提交的采样单！");
                    return;
                }
                isSubmit = true;
                batchUploadSampling();
                break;
            case R.id.cb_all:
                if (!isSelecteAll) {
                    cbAll.setImageResource(R.mipmap.ic_cb_checked);
                    updateSamplingAllStatus(!isSelecteAll);
                    isSelecteAll = true;
                } else {
                    cbAll.setImageResource(R.mipmap.ic_cb_nor);
                    updateSamplingAllStatus(!isSelecteAll);
                    isSelecteAll = false;
                }
                break;
        }
    }


    private void showFinishDialog() {
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(this);
        dialogPlusBuilder.setContentHolder(new ViewHolder(createFinishDialogContentView()));
        dialogPlusBuilder.setGravity(Gravity.CENTER);
        dialogPlusBuilder.setContentWidth(700);
        dialogPlusBuilder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(@NonNull DialogPlus dialog, @NonNull View view) {
                mDialogPlus.dismiss();
                if (view.getId() == R.id.btn_ok) {
                    putSamplingFinish(mEtComment.getText().toString());
                }
            }
        });
        dialogPlusBuilder.setContentHeight(400);
        mDialogPlus = dialogPlusBuilder.create();
        mDialogPlus.show();
    }

    private View createFinishDialogContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_sampling_finish, null);
        mEtComment = view.findViewById(R.id.et_comment);
        return view;
    }

    /**
     * 显示要素选择框
     */
    private void showAddDialog() {
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(this);
        dialogPlusBuilder.setContentHolder(new ViewHolder(createDialogContentView()));
        dialogPlusBuilder.setGravity(Gravity.CENTER);
        dialogPlusBuilder.setContentWidth(700);
        dialogPlusBuilder.setContentHeight(800);
        mDialogPlus = dialogPlusBuilder.create();
        mDialogPlus.show();
    }

    private View createDialogContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_dialog_tag, null);
        mCustomTab = view.findViewById(R.id.tabview);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        //清空数据
        mFirstTags.clear();
        mTagNames.clear();

        List<Tags> tags = DBHelper.get().getTagsDao().loadAll();
        if (!CheckUtil.isEmpty(tags)) {
            for (Tags tag : tags) {
                if (tag.getLevel() == 0) {
                    mFirstTags.add(tag);
                    Tab tab = new Tab();
                    tab.setTabName(tag.getName());
                    mTagNames.add(tab);
                }
            }
        }

        for (Tab tagName : mTagNames) {
            tagName.setSelected(false);
        }

        mTagNames.get(0).setSelected(true);
        mCustomTab.setTabs(mTagNames);
        mCustomTab.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                updateForms(mFirstTags.get(position).getId());
            }
        });

        ArtUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(this));
        mFormAdapter = new FormAdapter(mDialogFormSelects);
        mFormAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                //这里改用switch
                Intent intent = new Intent();
                switch (mDialogFormSelects.get(position).getPath()) {
                    case PATH_PRECIPITATION:
                        //降水采样及样品交接记录（新都）
                        intent.setClass(TaskDetailActivity.this, PrecipitationActivity.class);
                        break;
                    case PATH_WASTEWATER:
                        //水和废水样品采集与交接记录（新都）
                        intent.setClass(TaskDetailActivity.this, WastewaterActivity.class);
                        break;
                    case PATH_INSTRUMENTAL:
                        //现场监测仪器法
                        intent.setClass(TaskDetailActivity.this, InstrumentalActivity.class);
                        break;
                    case PATH_NOISE_FACTORY://工业企业厂界噪声监测记录
                        intent.setClass(TaskDetailActivity.this, NoiseFactoryActivity.class);
                        break;
                    default:
                        ArtUtils.makeText(TaskDetailActivity.this, "功能开发中");
                        return;
                }
                mDialogPlus.dismiss();
                intent.putExtra("projectId", mProject.getId());
                intent.putExtra("isNewCreate", true);
                intent.putExtra("formSelectId", mDialogFormSelects.get(position).getFormId());
                ArtUtils.startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mFormAdapter);
        updateForms(mFirstTags.get(0).getId());
        return view;
    }

    private void updateForms(String tagId) {
        mDialogFormSelects.clear();
        List<FormSelect> formSelects = DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.TagParentId.eq(tagId)).list();
        if (!CheckUtil.isEmpty(formSelects)) {
            mDialogFormSelects.addAll(formSelects);
        }
        mFormAdapter.notifyDataSetChanged();
    }

    private void getSampling(String tagId) {
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.ProjectId.eq(mProject.getId()), SamplingDao.Properties.ParentTagId.eq(tagId)).orderDesc(SamplingDao.Properties.SamplingNo).list();
        mSamplings.clear();
        if (!CheckUtil.isEmpty(samplings)) {
            mSamplings.addAll(samplings);
        }
        mTaskDetailAdapter.notifyDataSetChanged();
    }


    /**
     * 更新sampling选中状态
     *
     * @param position
     */
    private void updateSamplingStatus(int position) {
        if (mSamplings.get(position).isSelected()) {
            mSamplings.get(position).setSelected(false);
        } else {
            mSamplings.get(position).setSelected(true);
        }
        mTaskDetailAdapter.notifyDataSetChanged();
    }

    /**
     * 更新所有sampling选中状态
     *
     * @param isSelecteAll
     */
    private void updateSamplingAllStatus(boolean isSelecteAll) {
        for (Sampling sampling : mSamplings) {
            if (isSelecteAll) {
                if (sampling.getStatus() == 0 || sampling.getStatus() == 4 || sampling.getStatus() == 9) {
                    sampling.setSelected(isSelecteAll);
                } else {
                    sampling.setSelected(!isSelecteAll);
                }
            } else {
                sampling.setSelected(isSelecteAll);
            }
        }
        mTaskDetailAdapter.notifyDataSetChanged();
    }

    @Subscriber(tag = EventBusTags.TAG_PROGRAM_MODIFY)
    private void updateData(boolean isModified) {
        mProject = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(getIntent().getStringExtra("taskId"))).unique();
        bindView(mProject);
    }

    @Subscriber(tag = EventBusTags.TAG_SAMPLING_UPDATE)
    private void updateSamplingData(boolean isModified) {
        getSampling(mTagId);
    }

    /**
     * 采样完结
     *
     * @param comment
     */
    private void putSamplingFinish(String comment) {
        if (!NetworkUtil.isNetworkAvailable(mContext)) {
            ArtUtils.makeText(mContext, "请检查网络，该操作需在有网络情况下使用！");
            return;
        }
        if (CheckUtil.isEmpty(comment)) {
            ArtUtils.makeText(mContext, "请输入完结说明！");
            return;
        }
        //判断是否有未提交的采样单
        if (hasUncommittedSampling()) {
            ArtUtils.makeText(mContext, "有未提交的采样单，请先提交！");
            return;
        }

        showLoading();
        //接口提交数据
        mPresenter.putSamplingFinish(Message.obtain(this, new Object()), mProject.getId(), CheckUtil.isEmpty(comment) ? "" : comment);
    }

    /**
     * 提交方案数据
     *
     * @param isCompelSubmit:标志是否强制提交
     */
    private void uploadProjecteContentData(boolean isCompelSubmit) {
        ProjectPlan projectPlan = UploadDataUtil.setUploadProjectContextData(mProject.getId());
        projectPlan.setIsCompelSubmit(isCompelSubmit);
        mPresenter.putProjectContent(Message.obtain(this, new Object()), projectPlan);
    }

    private void showDeleteDialog(int position) {

        final Sampling sampling = mSamplings.get(position);

        final Dialog dialog = new AlertDialog.Builder(this)
                .setMessage("确定删除采样单？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (DbHelpUtils.getDbSampling(sampling.getId()) != null) {
                            DBHelper.get().getSamplingDao().delete(sampling);
                        }
                        mSamplings.remove(position);
                        getSampling(mTagId);
                        showMessage("删除采样单成功");
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 判断是否有未提交的单子
     *
     * @return
     */
    private boolean hasUncommittedSampling() {
        boolean flag = false;
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.ProjectId.eq(mProject.getId())).list();
        if (!CheckUtil.isEmpty(samplings)) {
            for (Sampling sampling : samplings) {
                if (sampling.getStatus() == 0 || sampling.getStatus() == 4 || sampling.getStatus() == 9) {
                    flag = true;
                    break;
                }
            }
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 提交采样单数据冲突处理
     */
    private void commitSamplingDataConflictOperate() {
        String title = "数据选择";
        String msg = "采样单数据同步中止，服务器存在数据不一致，请选择数据标准";
        String pBtnStr = "移动端数据";
        String nBtnStr = "服务端数据";

        IosDialog.showDialog(mContext, title, msg, pBtnStr, nBtnStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                appDataChooseOperate();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                serverDataChooseOperate();
            }
        });
    }

    /**
     * 使用移动端数据覆盖服务端数据提示
     */
    private void appDataChooseOperate() {
        String title = "确认选择";
        String msg = "您确定使用当前移动端表单数据覆盖服务端表单数据吗？";
        String pBtnStr = "确认";
        String nBtnStr = "取消";

        IosDialog.showDialog(mContext, title, msg, nBtnStr, pBtnStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                uploadSampFormData(sampling, true);
            }
        });
    }

    /**
     * 使用服务端数据覆盖移动端数据提示
     */
    private void serverDataChooseOperate() {
        String title = "确认选择";
        String msg = "您确定使用服务端表单数据覆盖当前移动端表单数据吗？移动端表单数据将丢失";
        String pBtnStr = "确认";
        String nBtnStr = "取消";

        IosDialog.showDialog(mContext, title, msg, nBtnStr, pBtnStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                showLoadingDialog("正在拉取服务端表单数据...");
                //去服务器拉取采样单信息，覆盖到本地
                mPresenter.getSamplinByID(Message.obtain(new IView() {
                    @Override
                    public void showMessage(@NonNull String message) {
                        if (TextUtils.isEmpty(message)) {
                            return;
                        }

                        ArtUtils.makeText(TaskDetailActivity.this, message);
                    }

                    @Override
                    public void handleMessage(@NonNull Message message) {
                        TaskDetailActivity.this.hideLoading();

                        switch (message.what) {
                            case Message.RESULT_OK:
                                showMessage("表单数据拉取成功！");
                                //刷新采样单数据
                                getSampling(mTagId);
                                break;

                            case Message.RESULT_FAILURE:
                                showMessage("表单数据拉取失败：" + message.obj.toString());
                                break;
                        }
                    }
                }), sampling.getId());
            }
        });
    }

    /**
     * 批量提交采样单，不能直接上传的采样单的提示
     */
    private void multiCommitTipsOperate() {
        String title = "数据未同步";
        String msg = "部分表单数据未同步，请单独上传表单后再次提交";
        String pBtnStr = "确认";

        OperateTipsDialog.showDialog(mContext, title, msg, pBtnStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 批量上传采样单
     */
    private void batchUploadSampling() {
        //重置上传索引
        samplingIndex = -1;
        isBatchUpload = true;

        showLoadingDialog("开始批量上传");

        if (mProject.getCanSamplingEidt() && mProject.getIsEditProjectContent()) {
            uploadProjecteContentData(true);
        }

        //开始批量上传
        uploadNextSampling();
    }

    private void batchUploadFinish() {
        this.isBatchUpload = false;
        this.hideLoading();
    }

    /**
     * 上传下一个采样单
     */
    private boolean uploadNextSampling() {
        if (!isBatchUpload) {
            return false;
        }

        do {
            //更新上传索引
            samplingIndex = Math.max(0, ++samplingIndex);

            //是否到达结尾
            if (samplingIndex >= mSamplings.size()) {
                return false;
            }

            //获取采样单
            sampling = mSamplings.get(samplingIndex);

            //如果未选中、已经提交、未完成，则跳过
            if (!sampling.isSelected() || sampling.getIsUpload() || !sampling.getIsFinish()) {
                continue;
            }

            //上传采样单
            uploadSampFormData(sampling, false);
            break;
        } while (true);

        return true;
    }

    /**
     * 上传采样单数据
     *
     * @param itemSampling   采样单
     * @param isCompelSubmit 是否强制提交
     */
    private void uploadSampFormData(Sampling itemSampling, boolean isCompelSubmit) {
        sampling = itemSampling;
        showLoadingDialog(String.format("开始上传采样单[%s]", sampling.getSamplingNo()));

        //上传采样单对应的文件，文件上传成功后上传采样单
        uploadSamplingFiles(sampling, new FileUploadHandler() {
            @Override
            public void onSuccess() {
                setUploadAltInfo(String.format("正在上传采样单[%s]", sampling.getSamplingNo()));

                if (TextUtils.isEmpty(sampling.getAddTime())) {
                    sampling.setAddTime(DateUtils.getTime(new Date().getTime()));
                }
                //文件上传完成后，组装采样单数据。文件ID已更改
                PreciptationSampForm preciptationSampForm = null;
                if (PATH_PRECIPITATION.equals(sampling.getFormPath())) {
                    preciptationSampForm = SubmitDataUtil.setUpJSData(sampling);
                } else if (PATH_WASTEWATER.equals(sampling.getFormPath())) {
                    preciptationSampForm = SubmitDataUtil.setUpFSData(sampling);
                } else if (PATH_INSTRUMENTAL.equals(sampling.getFormPath())) {
                    preciptationSampForm = SubmitDataUtil.setUpYQFData(sampling);
                } else if (PATH_NOISE_FACTORY.equals(sampling.getFormPath())) {
                    preciptationSampForm = SubmitDataUtil.setNoiseIndustralData(sampling);
                }

                //错误处理
                if (preciptationSampForm == null) {
                    batchUploadFinish();
                    showMessage(String.format("未实现提交功能的采样单！[%s]", sampling.getSamplingNo()));
                    return;
                }

                //上传文件组装
                preciptationSampForm.setUploadFiles(SubmitDataUtil.setUpSamplingFileDataList(sampling));
                //删除文件组装
                preciptationSampForm.setDelFiles(sampling.getSubmitDeleteFileIdList());

                if (sampling.getStatus() == 0 && sampling.getIsLocal() && !sampling.isUploadSave()) {
                    preciptationSampForm.setIsAdd(true);
                } else {
                    preciptationSampForm.setIsAdd(false);
                }
                preciptationSampForm.setIsSubmit(isSubmit);
                preciptationSampForm.setCompelSubmit(isCompelSubmit);
                Gson gson = new Gson();
                String str = gson.toJson(preciptationSampForm);
                PreciptationSampForm form = gson.fromJson(str, PreciptationSampForm.class);
                //文件上传成功，上传采样单
                mPresenter.createTable(Message.obtain(TaskDetailActivity.this, new Object()), form);
            }

            @Override
            public void onFailed(String msg) {
                closeLoadingDialog();
                ArtUtils.makeText(TaskDetailActivity.this, msg);
            }
        });
    }

    private void setUploadAltInfo(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }

        handleMessage(Message.obtain(this, Constants.VIEW_UPDATE_LOADING_TXT, str));
    }

    /**
     * 上传文件，重新设置文件ID
     *
     * @param sampling
     * @param handler
     */
    private void uploadSamplingFiles(Sampling sampling, FileUploadHandler handler) {
        //从数据库加载数据
        List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().
                where(SamplingFileDao.Properties.SamplingId.eq(sampling.getId()),
                        SamplingFileDao.Properties.Id.eq(""),
                        SamplingFileDao.Properties.IsUploaded.eq(false)).list();
        //噪声表 测点示意图  图片
        if (sampling.getFormPath().equals(PATH_NOISE_FACTORY) && sampling.getPrivateData() != null) {
            NoisePrivateData privateData = new Gson().fromJson(sampling.getPrivateData(), NoisePrivateData.class);
            if (privateData.getImageSYT() != null && !privateData.getImageSYT().equals("")
                    && !privateData.getImageSYT().startsWith("/Upload")) {
                NoiseSamplingFile samplingFile = new NoiseSamplingFile();
                File file = new File(privateData.getImageSYT());
                samplingFile.setId(UUID.randomUUID().toString());
                samplingFile.setFilePath(privateData.getImageSYT());
                samplingFile.setFileName(file.getName());
                samplingFile.setSamplingId(sampling.getId());
                samplingFile.setUpdateTime(DateUtils.getTime(new Date().getTime()));
                samplingFile.setIsSelect(false);
                samplingFiles.add(samplingFile);
            }
        }
        if (CheckUtil.isEmpty(samplingFiles)) {
            if (handler != null) {
                handler.onSuccess();
            }
            return;
        }

        //文件集合
        HashMap<String, SamplingFile> fileSet = new HashMap<>();

        List<File> sourceFiles = new ArrayList<>();
        List<File> compressFiles = new ArrayList<>();

        for (SamplingFile sf : samplingFiles) {
            if (!TextUtils.isEmpty(sf.getId())) {
                continue;//已经有文件ID，上传成功的文件
            }

            File file = new File(sf.getFilePath());
            if (!file.exists()) {
                if (handler != null) {
                    handler.onFailed(String.format("图片上传失败，图片不存在[%s]！", sf.getFilePath()));
                }
                return;//图片不存在
            }

            sourceFiles.add(file);

            //记录图片
            fileSet.put(sf.getFileName(), sf);
        }


        if (CheckUtil.isEmpty(sourceFiles)) {
            if (handler != null) {
                handler.onSuccess();
            }
            return;
        }

        setUploadAltInfo(String.format("正在压缩采样单[%s]图片[%d/%d]", sampling.getSamplingNo(), compressFiles.size(), sourceFiles.size()));

        for (File source : sourceFiles) {
            //异步压缩图片
            new Compressor(this)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(30)
//                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFileAsFlowable(source)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            // 压缩成功后调用，返回压缩后的图片
                            compressFiles.add(file);

                            setUploadAltInfo(String.format("正在压缩采样单[%s]图片[%d/%d]", sampling.getSamplingNo(), compressFiles.size(), sourceFiles.size()));

                            //全部压缩完成，上传图片
                            if (compressFiles.size() >= sourceFiles.size()) {
                                uploadFiles(sampling, handler, compressFiles, fileSet);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
//                            throwable.printStackTrace();
                            //当压缩过程出现问题时调用
                            setUploadAltInfo("图片压缩失败，正在准备上传原图");
                            uploadFiles(sampling, handler, sourceFiles, fileSet);
                        }
                    });
            // uploadFiles(sampling, handler, sourceFiles, fileSet);
        }

//        //鲁班异步压缩，压缩结果图片还是太大
//        Luban.with(this)
//                .load(sourceFiles)
//                .ignoreBy(MaxFileUploadSize)
//                .setTargetDir(getCacheDir().getPath())
//                .setFocusAlpha(false)
//                .filter(new CompressionPredicate() {
//                    @Override
//                    public boolean apply(String path) {
//                        return !TextUtils.isEmpty(path);
//                    }
//                })
//                .setCompressListener(new OnCompressListener() {
//                    @Override
//                    public void onStart() {
//
//                    }
//
//                    @Override
//                    public void onSuccess(File file) {
//                        // 压缩成功后调用，返回压缩后的图片
//                        compressFiles.add(file);
//
//                        setUploadAltInfo(String.format("正在压缩采样单[%s]图片[%d/%d]", sampling.getSamplingNo(), compressFiles.size(), sourceFiles.size()));
//
//                        //全部压缩完成，上传图片
//                        if (compressFiles.size() >= sourceFiles.size()) {
//                            uploadFiles(sampling, handler, compressFiles, fileSet);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //当压缩过程出现问题时调用
//                        if (handler != null) {
//                            handler.onFailed("图片压缩失败！");
//                        }
//                    }
//                }).launch();
    }

    /**
     * 上传文件
     *
     * @param sampling
     * @param handler
     * @param files
     */
    private void uploadFiles(Sampling sampling, FileUploadHandler handler, List<File> files,
                             HashMap<String, SamplingFile> fileSet) {
        List<MultipartBody.Part> parts = new ArrayList<>();

        for (File file : files) {
            //文件上传Body
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            //添加文件数据
            parts.add(MultipartBody.Part.createFormData("File", file.getName(), requestBody));
        }

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", RequestBody.create(MediaType.parse("text/plain"), UserInfoHelper.get().getUser().getToken()));

        setUploadAltInfo(String.format("正在上传采样单[%s]图片[%d]", sampling.getSamplingNo(), files.size()));

        //上传文件
        mPresenter.uploadFile(Message.obtain(new IView() {
            @Override
            public void showMessage(@NonNull String message) {
                if (TextUtils.isEmpty(message)) {
                    return;
                }

                ArtUtils.makeText(TaskDetailActivity.this, message);
            }

            @Override
            public void handleMessage(@NonNull Message message) {
                switch (message.what) {
                    case Message.RESULT_OK:
                        //上传成功
                        if (message.obj == null) {
                            if (handler != null) {
                                handler.onFailed("图片上传失败，data为空！");
                            }
                            return;
                        }

                        if (!(message.obj instanceof List)) {
                            String error = "";
                            if (message.obj instanceof String) {
                                error = message.obj.toString();
                            } else {
                                error = message.obj.toString();
                            }

                            if (handler != null) {
                                handler.onFailed("图片上传失败，数据错误！" + error);
                            }
                            return;
                        }

                        List<FileInfoData> fileInfoData = (List<FileInfoData>) message.obj;
                        if (fileInfoData == null || fileInfoData.size() == 0) {
                            if (handler != null) {
                                handler.onFailed("图片上传失败，未返回图片信息！");
                            }
                            return;
                        }

                        setUploadAltInfo(String.format("采样单[%s]图片上传成功！", sampling.getSamplingNo()));

                        //重新设置文件ID
                        for (FileInfoData item : fileInfoData) {
                            if (!fileSet.containsKey(item.getFileName())) {
                                continue;
                            }

                            //获取文件信息
                            SamplingFile samplingFile = fileSet.get(item.getFileName());
                            //更新文件ID
                            if (sampling.getFormPath().equals(PATH_NOISE_FACTORY) &&
                                    sampling.getPrivateData() != null) {
                                NoisePrivateData privateData = new Gson().fromJson(sampling.getPrivateData(), NoisePrivateData.class);
                                if (privateData.getImageSYT() != null
                                        && privateData.getImageSYT().equals(samplingFile.getFilePath())) {
                                    privateData.setImageSYT(item.getFilePath());
                                    sampling.setPrivateData(new Gson().toJson(privateData));
                                    DBHelper.get().getSamplingDao().update(sampling);
                                    continue;
                                }
                            }
                            samplingFile.setId(item.getId());
                            samplingFile.setIsUploaded(false);

                            //更新到数据库
                            DBHelper.get().getSamplingFileDao().update(samplingFile);
                        }

                        if (handler != null) {
                            handler.onSuccess();
                        }
                        break;

                    case Message.RESULT_FAILURE:
                        //上传失败
                        if (handler != null) {
                            handler.onFailed("图片上传失败：" + (message.obj != null ? message.obj.toString() : ""));
                        }
                        break;
                }
            }
        }), parts, map);
    }


    /**
     * 判断是否有选中的单子
     *
     * @return
     */
    private boolean hasSelectSample() {
        boolean flag = false;
        if (!CheckUtil.isNull(mTaskDetailAdapter)) {
            List<Sampling> samplingList = mTaskDetailAdapter.getInfos();
            if (!CheckUtil.isEmpty(samplingList)) {
                for (Sampling sampling : samplingList) {
                    //samplingList里面：选中 完成 未提交
                    if (sampling.getIsFinish() && sampling.isSelected() && !sampling.getIsUpload()) {
                        return true;
                    }
                }
            }

        }
        return flag;
    }

    interface FileUploadHandler {
        void onSuccess();

        void onFailed(String message);
    }
}
