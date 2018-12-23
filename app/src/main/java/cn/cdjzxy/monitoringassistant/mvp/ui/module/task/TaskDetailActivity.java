package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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

import org.simple.eventbus.Subscriber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectPlan;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.UserDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FormAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskDetailAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 任务详情
 */

public class TaskDetailActivity extends BaseTitileActivity<ApiPresenter> implements IView {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tab_layout)
    TabLayout    tabLayout;
    @BindView(R.id.tv_task_name)
    TextView     tvTaskName;
    @BindView(R.id.tv_task_time_range)
    TextView     tvTaskTimeRange;
    @BindView(R.id.tv_task_num)
    TextView     tvTaskNum;
    @BindView(R.id.tv_task_point)
    TextView     tvTaskPoint;
    @BindView(R.id.tv_task_project_num)
    TextView     tvTaskProjectNum;
    @BindView(R.id.tv_task_type)
    TextView     tvTaskType;
    @BindView(R.id.tv_task_person)
    TextView     tvTaskPerson;
    @BindView(R.id.tv_task_start_time)
    TextView     tvTaskStartTime;
    @BindView(R.id.tv_sampling_point_count)
    TextView     tvSamplingPointCount;
    @BindView(R.id.cb_all)
    ImageView    cbAll;

    private TitleBarView      mTitleBarView;
    private TaskDetailAdapter mTaskDetailAdapter;

    private List<Tags>     mTags      = new ArrayList<>();
    private List<Sampling> mSamplings = new ArrayList<>();

    private Project mProject;

    private DialogPlus   mDialogPlus;
    private CustomTab    mCustomTab;
    private RecyclerView mRecyclerView;
    private ImageView    mBtnClose;

    private List<Tags> mFirstTags = new ArrayList<>();
    private List<Tab>  mTagNames  = new ArrayList<>();

    private List<FormSelect> mDialogFormSelects = new ArrayList<>();
    private FormAdapter mFormAdapter;


    private String mTagId;


    private boolean isSelecteAll = false;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        mTitleBarView = titleBar;
        mTitleBarView.setTitleMainText("采样任务");
        mTitleBarView.setRightText("采样完结");
        mTitleBarView.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putSamplingFinish("测试");
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
        showLoadingDialog("数据提交中...");
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

                break;
            case Message.RESULT_OK:
                showMessage("数据提交成功");
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
                int lastDays = DateUtils.getLastDays(currentTime, endTime.split("T")[0]);
                if (lastDays <= 1) {
                    tvTaskTimeRange.setTextColor(Color.parseColor("#ff0000"));
                } else if (lastDays <= 3) {
                    tvTaskTimeRange.setTextColor(Color.parseColor("#ffbe00"));
                } else {
                    tvTaskTimeRange.setTextColor(Color.parseColor("#333333"));
                }
                tvTaskTimeRange.setText(data.getPlanBeginTime().split("T")[0].replace("-", "/") + "~" + data.getPlanEndTime().split("T")[0].replace("-", "/"));
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
                    monItems.append(projectDetial.getMonItemName() + ",");
                    points.append(projectDetial.getAddress() + ",");
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
            tvTaskStartTime.setText("下达:" + data.getAssignDate().split("T")[0].replace("-", "/"));

            tvSamplingPointCount.setText("共" + projectDetials.size() + "个");
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
                if ("降水采样及样品交接记录（新都）".equals(mSamplings.get(position).getFormName())) {
                    Intent intent = new Intent(TaskDetailActivity.this, PrecipitationActivity.class);
                    intent.putExtra("projectId", mProject.getId());
                    intent.putExtra("samplingId", mSamplings.get(position).getId());
                    intent.putExtra("isNewCreate", false);
                    ArtUtils.startActivity(intent);
                } else if ("水和废水样品采集与交接记录（新都）".equals(mSamplings.get(position).getFormName())) {
                    Intent intent = new Intent(TaskDetailActivity.this, WastewaterActivity.class);
                    intent.putExtra("projectId", mProject.getId());
                    intent.putExtra("samplingId", mSamplings.get(position).getId());
                    intent.putExtra("isNewCreate", false);
                    ArtUtils.startActivity(intent);
                } else {
                    ArtUtils.makeText(TaskDetailActivity.this, "功能开发中");
                }
            }

            @Override
            public void onUpload(View view, int position) {
                if ("降水采样及样品交接记录（新都）".equals(mSamplings.get(position).getFormName())) {
                    uploadProjecteContentData();
                    //                    uploadSamplingData(position);
                }

            }
        });
        recyclerview.setAdapter(mTaskDetailAdapter);

        getSampling(mTags.get(0).getId());
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
                showMessage("功能开发中");
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


    /**
     * 显示要素选择框
     */
    private void showAddDialog() {
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(this);
        dialogPlusBuilder.setContentHolder(new ViewHolder(createDialogContentView()));
        dialogPlusBuilder.setGravity(Gravity.CENTER);
        dialogPlusBuilder.setContentWidth(700);
        dialogPlusBuilder.setContentHeight(800);
        dialogPlusBuilder.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(@NonNull DialogPlus dialog, @NonNull View view) {
                mDialogPlus.dismiss();
            }
        });
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
                if ("降水采样及样品交接记录（新都）".equals(mDialogFormSelects.get(position).getFormName())) {
                    Intent intent = new Intent(TaskDetailActivity.this, PrecipitationActivity.class);
                    intent.putExtra("projectId", mProject.getId());
                    intent.putExtra("formSelectId", mDialogFormSelects.get(position).getFormId());
                    intent.putExtra("isNewCreate", true);
                    ArtUtils.startActivity(intent);
                } else if ("水和废水样品采集与交接记录（新都）".equals(mDialogFormSelects.get(position).getFormName())) {
                    Intent intent = new Intent(TaskDetailActivity.this, WastewaterActivity.class);
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
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.ProjectId.eq(mProject.getId()), SamplingDao.Properties.ParentTagId.eq(tagId)).list();
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
            sampling.setSelected(isSelecteAll);
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
        showLoading();
        //接口提交数据
        mPresenter.putSamplingFinish(Message.obtain(this, new Object()), mProject.getId(), comment);
    }

    /**
     * 提交方案数据
     */
    private void uploadProjecteContentData() {
        showLoading();
        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(mProject.getId())).list();
        List<ProjectContent> projectContents = new ArrayList<>();
        if (!CheckUtil.isEmpty(projectDetials)) {//开始组装数据
            for (ProjectDetial projectDetial : projectDetials) {
                ProjectContent projectContent = new ProjectContent();
                projectContent.setId(projectDetial.getProjectContentId());
                projectContent.setIsChecked(false);
                projectContent.setMonItemsName(projectDetial.getMonItemName());
                projectContent.setTagId(projectDetial.getTagId());
                projectContent.setTagName(projectDetial.getTagName());
                projectContent.setAddress(projectDetial.getAddress());
                projectContent.setAddressIds(projectDetial.getAddressId());
                projectContent.setDays(projectDetial.getDays());
                projectContent.setPeriod(projectDetial.getPeriod());
                projectContent.setComment(projectDetial.getComment());
                projectContent.setPeriodShow(false);
                projectContent.setTagParentId(projectDetial.getTagParentId());
                projectContent.setTagParentName(projectDetial.getTagParentName());
                projectContent.setGuid("");
                List<MonItems> monItemsList = new ArrayList<>();
                List<EnvirPoint> envirPoints = new ArrayList<>();
                if (!CheckUtil.isEmpty(projectDetial.getMethodId())) {
                    if (projectDetial.getMethodId().contains(",")) {
                        monItemsList = DBHelper.get().getMonItemsDao().queryBuilder().where(MonItemsDao.Properties.Id.in(projectDetial.getMonItemId().split(","))).list();
                    } else {
                        monItemsList = DBHelper.get().getMonItemsDao().queryBuilder().where(MonItemsDao.Properties.Id.eq(projectDetial.getMonItemId())).list();
                    }
                }

                if (!CheckUtil.isEmpty(projectDetial.getAddressId())) {
                    if (projectDetial.getAddressId().contains(",")) {
                        envirPoints = DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.Properties.Id.in(projectDetial.getAddressId().split(","))).list();
                    } else {
                        envirPoints = DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.Properties.Id.eq(projectDetial.getAddressId())).list();
                    }
                }

                List<ProjectContent.MonItemsBean> monItems = new ArrayList<>();
                List<ProjectContent.AddressArrBean> addressArrs = new ArrayList<>();

                if (!CheckUtil.isEmpty(monItemsList)) {
                    for (MonItems items : monItemsList) {
                        ProjectContent.MonItemsBean monItemsBean = new ProjectContent.MonItemsBean();
                        monItemsBean.setId(items.getId());
                        monItemsBean.setName(items.getName());
                        monItemsBean.setMethodId(projectDetial.getMethodId());
                        monItemsBean.setMethodName(projectDetial.getMethodName());
                        monItemsBean.setHaveCert("");
                        monItemsBean.setTagId(projectDetial.getTagId());
                        monItemsBean.setTagName(projectDetial.getTagName());
                        monItemsBean.setIsOutsourcing(false);
                        monItemsBean.setIsOutsourcingtext("");
                        monItems.add(monItemsBean);
                    }
                }

                if (!CheckUtil.isEmpty(envirPoints)) {
                    for (EnvirPoint envirPoint : envirPoints) {
                        ProjectContent.AddressArrBean addressArrBean = new ProjectContent.AddressArrBean();
                        addressArrBean.setId(envirPoint.getId());
                        addressArrBean.setName(envirPoint.getName());
                        addressArrBean.setType(0);
                        addressArrBean.setIstemp(false);
                        addressArrBean.setPoint("");
                        addressArrBean.setLevel("");
                        addressArrBean.setESLimt(new ArrayList<>());
                        addressArrs.add(addressArrBean);
                    }
                }

                projectContent.setMonItems(monItems);
                projectContent.setMonItemCount(monItems.size());
                projectContent.setAddressArr(addressArrs);
                projectContent.setAddressCount(addressArrs.size());
                projectContent.setProjectDetials(new ArrayList<>());
                projectContents.add(projectContent);
            }
        }

        ProjectPlan projectPlan = new ProjectPlan();
        projectPlan.setId(mProject.getId());
        projectPlan.setIsCompelSubmit(true);
        projectPlan.setProjectContents(projectContents);

        String json = JSONObject.toJSONString(projectPlan);

        mPresenter.putProjectContent(Message.obtain(this, new Object()), projectPlan);
    }

    /**
     * 提交采样单数据
     */
    private void uploadSamplingData(int position) {
        Sampling sampling = mSamplings.get(position);
        //采样单 图片文件
        List<SamplingFile> samplingFiles = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();
        //采样单 样品采集
        List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(PrecipitationActivity.mSampling.getId())).list();

        PreciptationSampForm preciptationSampForm = new PreciptationSampForm();
        //开始组装数据
        preciptationSampForm.setIsAdd(true);
        preciptationSampForm.setIsSubmit(true);


        //接口提交数据
        mPresenter.createTable(Message.obtain(this, new Object()), preciptationSampForm);
    }

}
