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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aries.ui.view.title.TitleBarView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.FileInfoData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectPlan;
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
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.Constants;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;
import cn.cdjzxy.monitoringassistant.utils.SubmitDataUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;
import cn.cdjzxy.monitoringassistant.widgets.IosDialog;
import cn.cdjzxy.monitoringassistant.widgets.OperateTipsDialog;
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

    /**
     * 废水表单路径/路径
     */
    public static final String PATH_WASTEWATER = "/FormTemplate/FILL_WATER_NEW_XD";

    /**
     * 仪器法表单路径/路径
     */
    public static final String PATH_INSTRUMENTAL = "/FormTemplate/FILL_YQF_WATER";

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
    private int samplingIndex;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mTitleBarView = titleBar;
        mTitleBarView.setTitleMainText("采样任务");
        mTitleBarView.setRightText("采样完结");
        mTitleBarView.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFinishDialog();
                //                putSamplingFinish("测试");
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
                isBatchUpload = false;
                showMessage("操作失败！");
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
                sampling.setIsCanEdit(false);
                sampling.setIsUpload(true);
                sampling.setStatusName("已提交");
                sampling.setStatus(7);
                sampling.setSubmitDate(DateUtils.getDate());
                DBHelper.get().getSamplingDao().update(sampling);

                if (!uploadNextSampling()) {
                    showMessage("数据提交成功");
                    getSampling(mTagId);
                }
                break;
            case Constants.NET_RESPONSE_SAMPLING_DIFFER:
                if (isBatchUpload) {
                    isBatchUpload = false;
                    multiCommitTipsOperate();
                } else {
                    commitSamplingDataConflictOperate();
                }
                break;
            default:
                break;
        }
    }

    private void initTask() {
        mProject = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(getIntent().getStringExtra("taskId"))).unique();
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

            StringBuilder users = new StringBuilder("");
            List<String> userIds = data.getSamplingUser();
            if (!CheckUtil.isEmpty(userIds)) {
                List<User> userList = DBHelper.get().getUserDao().queryBuilder().where(UserDao.Properties.Id.in(userIds)).list();
                if (!CheckUtil.isEmpty(userList)) {
                    for (User user : userList) {
                        users.append(user.getName() + ",");
                    }
                }
            }

            StringBuilder monItems = new StringBuilder("");
            StringBuilder points = new StringBuilder("");

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

        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
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
                if (PATH_PRECIPITATION.equals(mSamplings.get(position).getFormPath())) {
                    //降水采样及样品交接记录（新都）
                    Intent intent = new Intent(TaskDetailActivity.this, PrecipitationActivity.class);
                    intent.putExtra("projectId", mProject.getId());
                    intent.putExtra("samplingId", mSamplings.get(position).getId());
                    intent.putExtra("isNewCreate", false);
                    ArtUtils.startActivity(intent);
                } else if (PATH_WASTEWATER.equals(mSamplings.get(position).getFormPath())) {
                    //水和废水样品采集与交接记录（新都）
                    Intent intent = new Intent(TaskDetailActivity.this, WastewaterActivity.class);
                    intent.putExtra("projectId", mProject.getId());
                    intent.putExtra("samplingId", mSamplings.get(position).getId());
                    intent.putExtra("isNewCreate", false);
                    ArtUtils.startActivity(intent);
                } else if (PATH_INSTRUMENTAL.equals(mSamplings.get(position).getFormPath())) {
                    //现场监测仪器法
                    Intent intent = new Intent(TaskDetailActivity.this, InstrumentalActivity.class);
                    intent.putExtra("projectId", mProject.getId());
                    intent.putExtra("samplingId", mSamplings.get(position).getId());
                    intent.putExtra("isNewCreate", false);
                    ArtUtils.startActivity(intent);
                } else {
                    ArtUtils.makeText(TaskDetailActivity.this, "功能开发中");
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                showDeleteDialog(position);
            }

            @Override
            public void onUpload(View view, int position) {
                isBatchUpload = false;

                if (mProject.getCanSamplingEidt() && mProject.getIsSamplingEidt()) {
                    uploadProjecteContentData(true);
                }

                Sampling sampling = mSamplings.get(position);
                if (sampling == null || !sampling.getIsFinish()) {
                    showMessage("请先完善采样单信息！");
                    return;
                }

                //上传数据
                uploadSampFormData(sampling, false, false);
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
                Intent intent = new Intent(this, PointActivity.class);
                intent.putExtra("projectId", mProject.getId());
                ArtUtils.startActivity(intent);
                break;
            case R.id.btn_add_sampling:
                showAddDialog();
                break;
            case R.id.btn_submit:
                if (!isSelecteAll) {
                    showMessage("请先勾选需要提交的采样单！");
                    return;
                }
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
                if (PATH_PRECIPITATION.equals(mDialogFormSelects.get(position).getPath())) {
                    //降水采样及样品交接记录（新都）
                    Intent intent = new Intent(TaskDetailActivity.this, PrecipitationActivity.class);
                    intent.putExtra("projectId", mProject.getId());
                    intent.putExtra("formSelectId", mDialogFormSelects.get(position).getFormId());
                    intent.putExtra("isNewCreate", true);
                    ArtUtils.startActivity(intent);
                } else if (PATH_WASTEWATER.equals(mDialogFormSelects.get(position).getPath())) {
                    //水和废水样品采集与交接记录（新都）
                    Intent intent = new Intent(TaskDetailActivity.this, WastewaterActivity.class);
                    intent.putExtra("projectId", mProject.getId());
                    intent.putExtra("formSelectId", mDialogFormSelects.get(position).getFormId());
                    intent.putExtra("isNewCreate", true);
                    ArtUtils.startActivity(intent);
                } else if (PATH_INSTRUMENTAL.equals(mDialogFormSelects.get(position).getPath())) {
                    //现场监测仪器法
                    Intent intent = new Intent(TaskDetailActivity.this, InstrumentalActivity.class);
                    intent.putExtra("projectId", mProject.getId());
                    intent.putExtra("formSelectId", mDialogFormSelects.get(position).getFormId());
                    intent.putExtra("isNewCreate", true);
                    ArtUtils.startActivity(intent);
                } else {
                    ArtUtils.makeText(TaskDetailActivity.this, "功能开发中");
                }
                mDialogPlus.dismiss();
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
        ProjectPlan projectPlan = SubmitDataUtil.setUpProjectPlan(mProject);
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
                        DBHelper.get().getSamplingDao().delete(sampling);
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
                serverDataChooseOperate();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                appDataChooseOperate();
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

        IosDialog.showDialog(mContext, title, msg, pBtnStr, nBtnStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                uploadSampFormData(sampling, isBatchUpload, true);
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

        IosDialog.showDialog(mContext, title, msg, pBtnStr, nBtnStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //需要从服务端拉去数据
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

        if (mProject.getCanSamplingEidt() && mProject.getIsSamplingEidt()) {
            uploadProjecteContentData(true);
        }

        //开始批量上传
        uploadNextSampling();
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
                isBatchUpload = false;
                return false;
            }

            //获取采样单
            sampling = mSamplings.get(samplingIndex);

            //如果未选中、已经提交、未完成，则跳过
            if (!sampling.isSelected() || sampling.getIsUpload() || !sampling.getIsFinish()) {
                continue;
            }

            //上传采样单
            uploadSampFormData(sampling, true, false);
            break;
        } while (true);

        return true;
    }

    /**
     * 上传采样单数据
     *
     * @param itemSampling   采样单
     * @param isBatch        是否批量上传
     * @param isCompelSubmit 是否强制提交
     */
    private void uploadSampFormData(Sampling itemSampling, boolean isBatch, boolean isCompelSubmit) {
        sampling = itemSampling;

        //上传采样单对应的文件，文件上传成功后上传采样单
        uploadFiles(sampling, new FileUploadHandler() {
            @Override
            public void onSuccess() {
                //文件上传完成后，组装采样单数据。文件ID已更改
                PreciptationSampForm preciptationSampForm = null;
                if (PATH_PRECIPITATION.equals(sampling.getFormPath())) {
                    preciptationSampForm = SubmitDataUtil.setUpJSData(sampling);
                } else if (PATH_WASTEWATER.equals(sampling.getFormPath())) {
                    preciptationSampForm = SubmitDataUtil.setUpFSData(sampling);
                } else if (PATH_INSTRUMENTAL.equals(sampling.getFormPath())) {
                    preciptationSampForm = SubmitDataUtil.setUpYQFData(sampling);
                }

                //错误处理
                if (preciptationSampForm == null) {
                    isBatchUpload = false;
                    showMessage(String.format("未实现提交功能的采样单！[%s]", sampling.getSamplingNo()));
                    return;
                }

                if (sampling.getStatus() == 0) {
                    preciptationSampForm.setIsAdd(true);
                    preciptationSampForm.setCompelSubmit(false);
                } else {
                    preciptationSampForm.setIsAdd(false);
                    preciptationSampForm.setCompelSubmit(false);
                }

                if (isCompelSubmit) {
                    preciptationSampForm.setCompelSubmit(isCompelSubmit);
                }

                Log.e("SampFormData", JSONObject.toJSONString(preciptationSampForm));
                //文件上传成功，上传采样单
                mPresenter.createTable(Message.obtain(TaskDetailActivity.this, new Object()), preciptationSampForm);
            }

            @Override
            public void onFailed(String msg) {
                ArtUtils.makeText(TaskDetailActivity.this, msg);
            }
        });
    }

//    /**
//     * 批量提交采样单
//     */
//    private void multiCommitDatas() {
//        if (!isSelecteAll) {
//            showMessage("请先勾选需要提交的采样单！");
//            return;
//        }
//
//<<<<<<< HEAD
//        if (!isBatch) {
//            showLoading();
//        }
//
//        //上传文件，文件上传成功后上传采样单
//        uploadFiles(sampling, new FileUploadHandler() {
//            @Override
//            public void onSuccess() {
//                PreciptationSampForm preciptationSampForm = SubmitDataUtil.setUpFSData(sampling);
//                if (sampling.getStatus() == 0) {
//                    preciptationSampForm.setIsAdd(true);
//                    preciptationSampForm.setCompelSubmit(false);
//                } else {
//                    preciptationSampForm.setIsAdd(false);
//                    preciptationSampForm.setCompelSubmit(false);
//                }
//
//                if (isCompelSubmit) {
//                    preciptationSampForm.setCompelSubmit(isCompelSubmit);
//                }
//
//                mPresenter.createTable(Message.obtain(TaskDetailActivity.this, new Object()), preciptationSampForm);
//            }
//
//            @Override
//            public void onFailed(String msg) {
//                ArtUtils.makeText(TaskDetailActivity.this, msg);
//            }
//        });
//    }
//=======
//        if (mSamplings != null && mSamplings.size() > 0) {
//            List<Sampling> selectedSamplings = new ArrayList<>();
//            for (Sampling itemSampling : mSamplings) {
//                if (itemSampling.isSelected()) {
//                    selectedSamplings.add(itemSampling);
//                }
//            }
//
//            if (!CheckUtil.isEmpty(selectedSamplings)) {
//                for (Sampling itemSampl : selectedSamplings) {
//                    uploadSampFormData(itemSampl, false);
//                }
//>>>>>>> 6347eb7cd97320fc1635fb837ef9058b73770b66
//
//            } else {
//                showMessage("无采样单需要提交！");
//            }
//
//        } else {
//            showMessage("请先勾选需要提交的采样单！");
//        }
//<<<<<<< HEAD
//
//        if (isCompelSubmit) {
//            preciptationSampForm.setCompelSubmit(isCompelSubmit);
//        }
//
////        Log.e("uploadYQFData", JSONObject.toJSONString(preciptationSampForm));
//        mPresenter.createTable(Message.obtain(this, new Object()), preciptationSampForm);
//    }

    /**
     * 上传文件，重新设置文件ID
     *
     * @param sampling
     * @param handler
     */
    private void uploadFiles(Sampling sampling, FileUploadHandler handler) {
        //从数据库加载数据
        List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(sampling.getId()), SamplingFileDao.Properties.Id.eq("")).list();
        if (CheckUtil.isEmpty(samplingFiles)) {
            if (handler != null) {
                handler.onSuccess();
            }
            return;
        }

        //文件集合
        List<MultipartBody.Part> parts = new ArrayList<>();
        HashMap<String, SamplingFile> fileSet = new HashMap<>();

        for (SamplingFile sf : samplingFiles) {
            File file = new File(sf.getFilePath());
            if (!file.exists()) {
                if (handler != null) {
                    handler.onFailed(String.format("文件上传失败，文件不存在[%s]！", sf.getFilePath()));
                }
                return;//文件不存在
            }

            //文件上传Body
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            //添加文件数据
            parts.add(MultipartBody.Part.createFormData("File", file.getName(), requestBody));

            //记录文件
            fileSet.put(sf.getFileName(), sf);
        }

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("token", RequestBody.create(MediaType.parse("text/plain"), UserInfoHelper.get().getUser().getToken()));

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
                        if (message.obj == null || !(message.obj instanceof List)) {
                            if (handler != null) {
                                handler.onFailed("文件上传失败，数据错误！");
                            }
                            return;
                        }

                        List<FileInfoData> fileInfoData = (List<FileInfoData>) message.obj;
                        if (fileInfoData == null || fileInfoData.size() == 0) {
                            if (handler != null) {
                                handler.onFailed("文件上传失败，未返回文件信息！");
                            }
                            return;
                        }

                        //重新设置文件ID
                        for (FileInfoData item : fileInfoData) {
                            if (!fileSet.containsKey(item.getFileName())) {
                                continue;
                            }

                            //获取文件信息
                            SamplingFile samplingFile = fileSet.get(item.getFileName());
                            //更新文件ID
                            samplingFile.setId(item.getId());

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
                            handler.onFailed("文件上传失败！");
                        }
                        break;
                }
            }
        }), parts, map);
    }

//    /**
//     * 提交采样单数据冲突处理
//     */
//    private void commitSamplingDataConflictOperate() {
//        String title = "数据选择";
//        String msg = "采样单数据同步中止，服务器存在数据不一致，请选择数据标准";
//        String pBtnStr = "移动端数据";
//        String nBtnStr = "服务端数据";
//
//        IosDialog.showDialog(mContext, title, msg, pBtnStr, nBtnStr, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                serverDataChooseOperate();
//            }
//        }, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                appDataChooseOperate();
//            }
//        });
//    }

//    /**
//     * 批量提交采样单，不能直接上传的采样单的提示
//     */
//    private void multiCommitTipsOperate() {
//        String title = "数据未同步";
//        String msg = "部分表单数据未同步，请单独上传表单后再次提交";
//        String pBtnStr = "确认";
//>>>>>>> 18e9179154ea6bd565943ca3ce6670ca80e4a5d3
//
//        PreciptationSampForm preciptationSampForm = SubmitDataUtil.setUpYQFData(sampling);
//        if (sampling.getStatus() == 0) {
//            preciptationSampForm.setIsAdd(true);
//            preciptationSampForm.setCompelSubmit(false);
//        } else {
//            preciptationSampForm.setIsAdd(false);
//            preciptationSampForm.setCompelSubmit(false);
//        }
//
//<<<<<<< HEAD
//        if (isCompelSubmit) {
//            preciptationSampForm.setCompelSubmit(isCompelSubmit);
//=======
//    /**
//     * 上传仪器法数据
//     *
//     * @param itemSampling
//     */
//    private void uploadYQFData(Sampling itemSampling) {
//        sampling = itemSampling;
//        if (!sampling.getIsFinish()) {
//            showMessage("请先完善采样单信息！");
//            return;
//>>>>>>> 18e9179154ea6bd565943ca3ce6670ca80e4a5d3
//        }
//
//        Log.e("uploadYQFData", JSONObject.toJSONString(preciptationSampForm));
//        mPresenter.createTable(Message.obtain(this, new Object()), preciptationSampForm);
//    }

    //    /**
//     * 批量提交采样单
//     */
//    private void multiCommitDatas(){
//        if (!isSelecteAll){
//            showMessage("请先勾选需要提交的采样单！");
//            return;
//        }
//
//        if (mSamplings!=null && mSamplings.size()>0){
//            List<Sampling> selectedSamplings = new ArrayList<>();
//            for (Sampling itemSampling:mSamplings){
//                if (itemSampling.isSelected()){
//                    selectedSamplings.add(itemSampling);
//                }
//            }
//
//            if (!CheckUtil.isEmpty(selectedSamplings)){
//                for (Sampling itemSampl:selectedSamplings){
//                    if (PATH_PRECIPITATION.equals(itemSampl.getFormPath())) {
//                        uploadSamplingData(itemSampl);
//                    } else if (PATH_WASTEWATER.equals(itemSampl.getFormPath())) {
//                        uploadFsData(itemSampl, false);
//                    } else if (PATH_INSTRUMENTAL.equals(itemSampl.getFormPath())) {
//                        uploadYQFData(itemSampl);
//                    }
//                }
//
//            }else {
//                showMessage("无采样单需要提交！");
//            }
//
//        }else {
//            showMessage("请先勾选需要提交的采样单！");
//        }
//    }
    interface FileUploadHandler {
        void onSuccess();

        void onFailed(String message);
    }
}
