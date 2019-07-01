package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.baidu.mapapi.NetworkUtil;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import org.simple.eventbus.EventBus;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectPlan;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.ProjectUtils;
import cn.cdjzxy.monitoringassistant.utils.RxDataTool;
import cn.cdjzxy.monitoringassistant.utils.TagsUtils;
import cn.cdjzxy.monitoringassistant.utils.UploadDataUtil;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;


/**
 * 修改方案
 */

public class ProgramModifyActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.my_layout_tag)
    MyDrawableLinearLayout tvTag;
    @BindView(R.id.my_layout_point)
    MyDrawableLinearLayout tvPoint;
    @BindView(R.id.my_layout_monItem)
    MyDrawableLinearLayout tvMonitem;
    @BindView(R.id.my_layout_days)
    MyDrawableLinearLayout etDays;
    @BindView(R.id.my_layout_period)
    MyDrawableLinearLayout etPeriod;
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
    private ProjectContent mProjectContent;

    private boolean isAdd;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("修改方案");
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
            String projectId = getIntent().getStringExtra("projectId");
            mProject = DbHelpUtils.getDbProject(projectId);
        }
        if (isAdd) {
            mProjectContent = new ProjectContent();
        } else {
            String projectDetailId = getIntent().getStringExtra("projectContentId");
            mProjectContent = DbHelpUtils.getProjectContent(projectDetailId);
        }
        bindView(mProjectContent);
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }


    private void bindView(ProjectContent projectContent) {
        if (!RxDataTool.isNull(projectContent)) {
            tvTag.setRightTextStr(RxDataTool.isEmpty(projectContent.getTagName()) ? "" :
                    projectContent.getTagName());

            setTvMonItem(projectContent);
//            etComment.setText(RxDataTool.isEmpty(projectContent.getComment()) ? "" :
//                    projectContent.getComment());
            etDays.setEditTextStr(projectContent.getDays() + "");
            etPeriod.setEditTextStr(projectContent.getPeriod() + "");
            tvPoint.setRightTextStr(RxDataTool.isEmpty(projectContent.getAddress()) ? "" :
                    projectContent.getAddress());
        }
        boolean isCanClick = UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Plan_Modify_Num)
                && mProject.getCanSamplingEidt();

        etDays.setClickable(isCanClick);
        etPeriod.setClickable(isCanClick);
        etComment.setClickable(isCanClick);
    }

    /**
     * 设置监测项目
     *
     * @param data ProjectContent
     */
    private void setTvMonItem(ProjectContent data) {
        data = ProjectUtils.setProjectContentMonItemsData(data);
        tvMonitem.setRightTextStr(RxDataTool.isEmpty(data.getMonItemNames()) ? "" : data.getMonItemNames());
    }


    @OnClick({R.id.my_layout_tag, R.id.my_layout_point, R.id.my_layout_monItem, R.id.btn_add_parallel,
            R.id.btn_add_blank})
    public void onClick(View view) {
        if (!mProject.getCanSamplingEidt()) {
            showMessage("该方案不可编辑");
            return;
        }
        if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Plan_Modify_Num)) {
            showNoPermissionDialog("才能进行采样方案编辑。", UserInfoAppRight.APP_Permission_Plan_Modify_Name);
            return;
        }
        switch (view.getId()) {
            case R.id.my_layout_tag:
                showTagDialog();
                break;
            case R.id.my_layout_point:
                selectPoint();
                break;
            case R.id.my_layout_monItem:
                selectMonItem();
                break;
            case R.id.btn_add_parallel:
                delete();
                break;
            case R.id.btn_add_blank:
                edit();
                break;
        }
    }

    private void showMessage(String s) {
        ArtUtils.makeText(this, s);
    }

    /**
     * 选择tag
     */
    private void showTagDialog() {

        TagsUtils.showTagDialog(mContext, new TagsUtils.TagSelectAdapterOnItemClick() {
            @Override
            public void onItemClick(Tags tags, Form form) {
                tvTag.setRightTextStr(tags.getName());
                mProjectContent.setTagParentName(form.getTagName());
                mProjectContent.setTagParentId(form.getTagId());
                mProjectContent.setTagId(tags.getId());
                mProjectContent.setTagName(tags.getName());
                mProjectContent.setAddress("");
                mProjectContent.setAddressIds("");
                tvPoint.setRightTextStr("");
            }
        });
    }

    /**
     * 更改采样方案  保存或者新增操作
     */
    private void edit() {
        if (RxDataTool.isEmpty(mProjectContent.getAddressIds())) {
            ArtUtils.makeText(this, "采样点位不能为空");
            return;
        }

        if (RxDataTool.isEmpty(tvMonitem.getRightTextViewStr())) {
            ArtUtils.makeText(this, "监测项目不能为空");
            return;
        }
        showLoadingDialog("正在保存数据，请稍后");
        mProjectContent.setDays(Integer.parseInt(etDays.getEditText().getText().toString()));
        mProjectContent.setPeriod(Integer.parseInt(etPeriod.getEditText().getText().toString()));
        //  mProjectContent.setComment(etComment.getText().toString());
        mProjectContent.setUpdateTime(DateUtils.getDate());
        if (isAdd) {
            mProjectContent.setProjectId(mProject.getId());
            mProjectContent.setId(UUID.randomUUID().toString());
            DBHelper.get().getProjectContentDao().insert(mProjectContent);
        } else {
            DBHelper.get().getProjectContentDao().update(mProjectContent);
            //删除之前旧的ProjectDetials
            deleteProjectDetail();
        }
        //生成新的ProjectDetials
        ProjectUtils.generateProjectDetails(mProject, mProjectContent);
        uploadData(false);
    }

    /**
     * 上传数据到服务器
     */
    private void uploadData(boolean isDelete) {
        mProject.setIsEditProjectContent(true);
        if (NetworkUtil.isNetworkAvailable(mContext)) {
            showLoadingDialog("正在提交数据。。。请稍后", true);
            updateProjectDetailData(true);
        } else if (isDelete) {
            showMessage("删除成功");
            onBack();
        } else {
            showMessage("保存成功");
            onBack();
        }
    }

    /**
     * 退出当前页面
     */
    private void onBack() {
        closeLoadingDialog();
        DBHelper.get().getProjectDao().update(mProject);
        EventBus.getDefault().post(true, EventBusTags.TAG_PROGRAM_MODIFY);
        finish();
    }


    /**
     * 删除之前的projectDetail  数据
     */
    private void deleteProjectDetail() {
        List<ProjectDetial> detailList = DbHelpUtils.getProjectDetailList(mProjectContent.getProjectId(),
                mProjectContent.getId());
        if (!RxDataTool.isEmpty(detailList))
            DBHelper.get().getProjectDetialDao().deleteInTx(detailList);
    }

    /**
     * 删除
     */
    private void delete() {
        //根据ProjectContentId来删除数据，每一个ProjectContentId代表采样点位的一大行数据
        if (isAdd) {
            showMessage("您正在新增，无法删除");
            return;
        }
        deleteProjectDetail();
        DBHelper.get().getProjectContentDao().delete(mProjectContent);
        uploadData(true);

    }

    /**
     * 选择监测项目
     */
    private void selectMonItem() {
        Intent intent1 = new Intent(this, MonItemActivity.class);
        intent1.putExtra("tagId", mProjectContent.getTagParentId());
        intent1.putExtra("selectItems", mProjectContent.getMonItemIds());

        new AvoidOnResult(this).startForResult(intent1, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    String monItemName = data.getStringExtra("MonItemName");
                    String monItemId = data.getStringExtra("MonItemId");
                    tvMonitem.setRightTextStr(monItemName);
                    mProjectContent.setMonItemIds(monItemId);
                    mProjectContent.setMonItemNames(monItemName);
                }
            }
        });
    }

    /**
     * 选择点位
     */
    private void selectPoint() {
        if (mProjectContent == null || mProjectContent.getTagId() == null) {
            ArtUtils.makeText(this, "请先选择采样要素");
            return;
        }
        Intent intent = new Intent(this, ProgramPointSelectActivity.class);
        if (mProject.getTypeCode() != 3) {//污染源
            intent.putExtra("isRcv", true);
            intent.putExtra("rcvId", mProject.getRcvId());
        }
        intent.putExtra("tagId", mProjectContent.getTagId());
        intent.putExtra("projectId", mProject.getId());
        intent.putExtra("addressId", mProjectContent.getAddressIds());
        intent.putExtra("addressName", mProjectContent.getAddress());
        new AvoidOnResult(this).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!RxDataTool.isEmpty(data.getStringExtra("AddressId")) &&
                            !RxDataTool.isEmpty(data.getStringExtra("Address"))) {
                        mProjectContent.setAddress(data.getStringExtra("Address"));
                        mProjectContent.setAddressIds(data.getStringExtra("AddressId"));
                        tvPoint.setRightTextStr(RxDataTool.isEmpty(mProjectContent.getAddress()) ? "" :
                                mProjectContent.getAddress());
                    }
                }
            }
        });
    }


    /**
     * 上传采样方案数据
     *
     * @param isSubmit 是否强制提交
     */
    private void updateProjectDetailData(boolean isSubmit) {
        ProjectPlan projectPlan = UploadDataUtil.setUploadProjectContextData(mProject.getId());
        projectPlan.setIsCompelSubmit(isSubmit);
        if (RxDataTool.isNull(projectPlan) || RxDataTool.isEmpty(projectPlan.getProjectContents())) {
            closeLoadingDialog();
            showMessage("数据异常");
            return;
        }

        mPresenter.putProjectContent(Message.obtain(new IView() {
            @Override
            public void showMessage(@NonNull String message) {
                closeLoadingDialog();
                showNetMessage(message);
            }

            @Override
            public void handleMessage(@NonNull Message message) {
                switch (message.what) {
                    case Message.RESULT_OK:
                        mProject.setIsEditProjectContent(false);
                        onBack();
                        break;
                    case Message.RESULT_FAILURE:
                        showMessage(message.str);
                        break;
                }
            }
        }), projectPlan);
    }

    private void showNetMessage(String message) {
        showMessage(message);
    }
}
