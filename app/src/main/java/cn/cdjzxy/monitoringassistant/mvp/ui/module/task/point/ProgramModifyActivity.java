package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;
import com.wonders.health.lib.base.widget.dialogplus.DialogPlus;
import com.wonders.health.lib.base.widget.dialogplus.DialogPlusBuilder;
import com.wonders.health.lib.base.widget.dialogplus.ViewHolder;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TagAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

/**
 * 修改方案
 */

public class ProgramModifyActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.tv_monitem)
    TextView tvMonitem;
    @BindView(R.id.et_days)
    EditText etDays;
    @BindView(R.id.et_period)
    EditText etPeriod;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.tv_add_parallel)
    TextView tvAddParallel;
    @BindView(R.id.btn_add_parallel)
    RelativeLayout btnAddParallel;
    @BindView(R.id.tv_add_blank)
    TextView tvAddBlank;
    @BindView(R.id.btn_add_blank)
    RelativeLayout btnAddBlank;
    @BindView(R.id.btn_add_new)
    RelativeLayout btnAddNew;
    @BindView(R.id.tv_print_label)
    TextView tvPrintLabel;
    @BindView(R.id.btn_print_label)
    RelativeLayout btnPrintLabel;

    private Project mProject;
    private ProjectDetial mProjectDetial;

    private DialogPlus mDialogPlus;

    private List<Tags> mFirstTags = new ArrayList<>();
    private List<Tab> mTagNames = new ArrayList<>();
    private List<Tags> mTags = new ArrayList<>();

    private CustomTab mCustomTab;
    private RecyclerView mRecyclerView;
    private TagAdapter mTagAdapter;
    private boolean isAdd;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("修改方案");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_program_modify;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        btnPrintLabel.setVisibility(View.GONE);
        btnAddNew.setVisibility(View.GONE);
        tvAddParallel.setText("删除");
        tvAddBlank.setText("保存");
        if (getIntent() != null) {
            isAdd = getIntent().getBooleanExtra("isAdd", false);
            mProject = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.
                    Id.eq(getIntent().getStringExtra("projectId"))).unique();
        }
        if (isAdd) {
            mProjectDetial = new ProjectDetial();
        } else {
            mProjectDetial = getIntent().getParcelableExtra("projectDetail");
        }
//        mProjectDetial = DBHelper.get().getProjectDetialDao().queryBuilder().
//                where(ProjectDetialDao.Properties.Id.eq(getIntent().getStringExtra("projectDetailId"))).unique();

        bindView(mProjectDetial);
    }


    private void bindView(ProjectDetial projectDetial) {
        if (!CheckUtil.isNull(projectDetial)) {
            tvTag.setText(CheckUtil.isEmpty(projectDetial.getTagName()) ? "" : projectDetial.getTagName());
            tvPoint.setText(CheckUtil.isEmpty(projectDetial.getAddress()) ? "" : projectDetial.getAddress());
            tvMonitem.setText(CheckUtil.isEmpty(projectDetial.getMonItemName()) ? "" : projectDetial.getMonItemName());
            etDays.setText(projectDetial.getDays() + "");
            etPeriod.setText(projectDetial.getPeriod() + "");
            etComment.setText(CheckUtil.isEmpty(projectDetial.getComment()) ? "" : projectDetial.getComment());
        }
    }


    @OnClick({R.id.tv_tag, R.id.tv_point, R.id.tv_monitem, R.id.btn_add_parallel, R.id.btn_add_blank})
    public void onClick(View view) {
        if (!isAdd && !UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Plan_Modify_Num)) {
            showNoPermissionDialog("才能进行采样方案编辑。", UserInfoAppRight.APP_Permission_Plan_Modify_Name);
            return;
        }
        switch (view.getId()) {
            case R.id.tv_tag:
                showTagDialog();
                break;
            case R.id.tv_point:
                //Intent intent = new Intent(this, PointSelectActivity.class);
                if (mProjectDetial == null || mProjectDetial.getTagId() == null) {
                    ArtUtils.makeText(this, "请先选择采样点位");
                    return;
                }
                Intent intent = new Intent(this, ProgramPointSelectActivity.class);
                if (mProject.getTypeCode() != 3) {//污染源
                    intent.putExtra("isRcv", true);
                    intent.putExtra("rcvId", mProject.getRcvId());
                }
                intent.putExtra("tagId", mProjectDetial.getTagId());
                intent.putExtra("projectId", mProject.getId());
                intent.putExtra("addressId", mProjectDetial.getAddressId());
                intent.putExtra("addressName", mProjectDetial.getAddress());
                new AvoidOnResult(this).startForResult(intent, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (!CheckUtil.isEmpty(data.getStringExtra("AddressId")) && !CheckUtil.isEmpty(data.getStringExtra("Address"))) {
                                mProjectDetial.setAddress(data.getStringExtra("Address"));
                                mProjectDetial.setAddressId(data.getStringExtra("AddressId"));
                                bindView(mProjectDetial);
                            }
                        }
                    }
                });
                break;
            case R.id.tv_monitem:
                Intent intent1 = new Intent(this, MonItemActivity.class);
                intent1.putExtra("tagId", mProjectDetial.getTagParentId());
                intent1.putExtra("monItemId", mProjectDetial.getMonItemId());
                intent1.putExtra("selectItems", mProjectDetial.getMonItemId());

                new AvoidOnResult(this).startForResult(intent1, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (!CheckUtil.isEmpty(data.getStringExtra("MonItemId")) && !CheckUtil.isEmpty(data.getStringExtra("MonItemName"))) {
                                mProjectDetial.setMonItemName(data.getStringExtra("MonItemName"));
                                mProjectDetial.setMonItemId(data.getStringExtra("MonItemId"));
                                bindView(mProjectDetial);
                            }
                        }
                    }
                });
                break;
            case R.id.btn_add_parallel:
                //根据ProjectContentId来删除数据，每一个ProjectContentId代表采样点位的一大行数据
                if (isAdd) {
                    finish();
                    return;
                }
                deleteProgramPoitRowData();
                mProject.setIsEditProjectContent(true);
                DBHelper.get().getProjectDao().update(mProject);
                EventBus.getDefault().post(true, EventBusTags.TAG_PROGRAM_MODIFY);
                ArtUtils.makeText(this, "删除采样点位数据成功");
                finish();
                break;
            case R.id.btn_add_blank:
                if (CheckUtil.isEmpty(mProjectDetial.getAddressId())) {
                    ArtUtils.makeText(this, "采样点位不能为空");
                    return;
                }

                if (CheckUtil.isEmpty(mProjectDetial.getMonItemId())) {
                    ArtUtils.makeText(this, "监测项目不能为空");
                    return;
                }

                mProjectDetial.setDays(Integer.parseInt(etDays.getText().toString()));
                mProjectDetial.setPeriod(Integer.parseInt(etPeriod.getText().toString()));
                mProjectDetial.setComment(etComment.getText().toString());
                mProjectDetial.setUpdateTime(DateUtils.getWholeDate());
                if (isAdd) {
                    mProjectDetial.setProjectId(mProject.getId());
                    mProjectDetial.setId(UUID.randomUUID().toString());
                    DBHelper.get().getProjectDetialDao().insert(mProjectDetial);
                } else {
                    DBHelper.get().getProjectDetialDao().update(mProjectDetial);
                    //删除之前旧的ProjectDetials
                    deleteProgramPoitRowData();
                    //生成新的ProjectDetials
                }
                generateProjectDetials();
                mProject.setIsEditProjectContent(true);
                DBHelper.get().getProjectDao().update(mProject);
                EventBus.getDefault().post(true, EventBusTags.TAG_PROGRAM_MODIFY);
                ArtUtils.makeText(this, "保存采样点位数据成功");
                finish();
                break;
        }
    }

    /**
     * 显示要素选择框
     */
    private void showTagDialog() {
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
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogPlus.dismiss();
            }
        });

        //清空数据
        mFirstTags.clear();
        mTagNames.clear();
        mTags.clear();

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

        mTagNames.get(0).setSelected(true);
        mCustomTab.setTabs(mTagNames);
        mCustomTab.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                updateTags(mFirstTags.get(position).getId());
            }
        });

        ArtUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(this));
        mTagAdapter = new TagAdapter(mTags);
        mTagAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Tags tags = mTags.get(position);
                mProjectDetial.setTagParentId(tags.getParentId());
                mProjectDetial.setTagId(tags.getId());
                mProjectDetial.setTagName(tags.getName());
                bindView(mProjectDetial);

                mDialogPlus.dismiss();
            }
        });
        mRecyclerView.setAdapter(mTagAdapter);
        updateTags(mFirstTags.get(0).getId());
        return view;
    }

    private void updateTags(String tagParentId) {
        mTags.clear();
        List<Tags> tags1 = DBHelper.get().getTagsDao().queryBuilder().
                where(TagsDao.Properties.ParentId.eq(tagParentId), TagsDao.Properties.Level.eq(1)).list();
        if (!CheckUtil.isEmpty(tags1)) {
            mTags.addAll(tags1);
        }
        mTagAdapter.notifyDataSetChanged();
    }


    private void generateProjectDetials() {
        String monitorIds = mProjectDetial.getMonItemId();
        String addressIds = mProjectDetial.getAddressId();
        if (!CheckUtil.isEmpty(monitorIds) && !CheckUtil.isEmpty(addressIds)) {
            String[] montorIdArray = monitorIds.split(",");
            String[] addressIdArray = addressIds.split(",");
            String monItemId = "";
            ProjectDetial projectDetial = null;
            for (String montorId : montorIdArray) {
                monItemId = montorId;
                for (String addressId : addressIdArray) {
                    projectDetial = generateProjectDetial(monItemId, addressId);
                    if (!CheckUtil.isNull(projectDetial)) {
                        DBHelper.get().getProjectDetialDao().insertOrReplace(projectDetial);
                    }
                }
            }
        }

    }

    /**
     * 生成ProjectDetial
     *
     * @param monItemId
     * @param addressId
     * @return
     */
    private ProjectDetial generateProjectDetial(String monItemId, String addressId) {
        if (!CheckUtil.isNull(mProjectDetial)) {
            ProjectDetial projectDetial = new ProjectDetial();
            projectDetial.setId(UUID.randomUUID().toString());
            projectDetial.setAddress(getEnvirPointAddressById(addressId));
            projectDetial.setAddressId(addressId);
            projectDetial.setComment(mProjectDetial.getComment());
            projectDetial.setDays(mProjectDetial.getDays());
            projectDetial.setMethodId(mProjectDetial.getMethodId());
            projectDetial.setMethodName(mProjectDetial.getMethodName());
            projectDetial.setMonItemId(monItemId);
            projectDetial.setMonItemName(getMonItemNameById(monItemId));
            projectDetial.setPeriod(mProjectDetial.getPeriod());
            projectDetial.setProjectContentId(mProjectDetial.getProjectContentId());
            projectDetial.setProjectId(mProjectDetial.getProjectId());
            projectDetial.setTagId(mProjectDetial.getTagId());
            projectDetial.setTagName(mProjectDetial.getTagName());
            projectDetial.setTagParentId(mProjectDetial.getTagParentId());
            projectDetial.setTagParentName(mProjectDetial.getTagParentName());
            projectDetial.setUpdateTime(mProjectDetial.getUpdateTime());
            return projectDetial;
        }
        return null;
    }

    private String getMonItemNameById(String itemId) {
        Tags tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(mProjectDetial.getTagParentId())).unique();
        List<MonItems> monItems = tags.getMMonItems();
        if (!CheckUtil.isEmpty(monItems)) {
            for (MonItems monItem : monItems) {
                if (!CheckUtil.isEmpty(monItem.getId()) && !CheckUtil.isEmpty(itemId) && monItem.getId().equals(itemId)) {
                    return monItem.getName();
                }
            }
        }
        return "";
    }

    /**
     * 通过addressId获取name
     *
     * @param addressId
     * @return
     */
    private String getEnvirPointAddressById(String addressId) {
        List<EnvirPoint> envirPointList = DBHelper
                .get()
                .getEnvirPointDao()
                .queryBuilder()
                .where(EnvirPointDao.Properties.Id.eq(addressId))
                .list();
        if (!CheckUtil.isEmpty(envirPointList)) {
            return envirPointList.get(0).getName();
        }
        return "";
    }

    /**
     * 删除采样点位对应的一行数据
     * 根据ProjectContentId来删除数据，每一个ProjectContentId代表采样点位的一大行数据
     */
    private void deleteProgramPoitRowData() {
        List<ProjectDetial> deleteProjectDetialsList = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(mProject.getId()), ProjectDetialDao.Properties.ProjectContentId.eq(mProjectDetial.getProjectContentId())).list();
        DBHelper.get().getProjectDetialDao().deleteInTx(deleteProjectDetialsList);
    }

}
