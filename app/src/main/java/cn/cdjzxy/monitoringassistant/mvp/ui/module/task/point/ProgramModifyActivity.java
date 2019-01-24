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
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TagAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

/**
 * 修改方案
 */

public class ProgramModifyActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.tv_tag)
    TextView       tvTag;
    @BindView(R.id.tv_point)
    TextView       tvPoint;
    @BindView(R.id.tv_monitem)
    TextView       tvMonitem;
    @BindView(R.id.et_days)
    EditText       etDays;
    @BindView(R.id.et_period)
    EditText       etPeriod;
    @BindView(R.id.et_comment)
    EditText       etComment;
    @BindView(R.id.tv_add_parallel)
    TextView       tvAddParallel;
    @BindView(R.id.btn_add_parallel)
    RelativeLayout btnAddParallel;
    @BindView(R.id.tv_add_blank)
    TextView       tvAddBlank;
    @BindView(R.id.btn_add_blank)
    RelativeLayout btnAddBlank;
    @BindView(R.id.btn_add_new)
    RelativeLayout btnAddNew;
    @BindView(R.id.tv_print_label)
    TextView       tvPrintLabel;
    @BindView(R.id.btn_print_label)
    RelativeLayout btnPrintLabel;

    private Project       mProject;
    private ProjectDetial mProjectDetial;

    private DialogPlus mDialogPlus;

    private List<Tags> mFirstTags = new ArrayList<>();
    private List<Tab>  mTagNames  = new ArrayList<>();
    private List<Tags> mTags      = new ArrayList<>();

    private CustomTab    mCustomTab;
    private RecyclerView mRecyclerView;
    private TagAdapter   mTagAdapter;

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
        mProject = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(getIntent().getStringExtra("projectId"))).unique();
        mProjectDetial = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.Id.eq(getIntent().getStringExtra("projectDetailId"))).unique();

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
        switch (view.getId()) {
            case R.id.tv_tag:
                showTagDialog();
                break;
            case R.id.tv_point:
                Intent intent = new Intent(this, PointSelectActivity.class);
                intent.putExtra("projectId", mProjectDetial.getProjectId());
                intent.putExtra("tagId", mProjectDetial.getTagId());
                new AvoidOnResult(this).startForResult(intent, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            mProjectDetial.setAddress(data.getStringExtra("Address"));
                            mProjectDetial.setAddressId(data.getStringExtra("AddressId"));
                            bindView(mProjectDetial);
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
                DBHelper.get().getProjectDetialDao().delete(mProjectDetial);

                mProject.setIsSamplingEidt(true);
                DBHelper.get().getProjectDao().update(mProject);
                EventBus.getDefault().post(true, EventBusTags.TAG_PROGRAM_MODIFY);
                ArtUtils.makeText(this, "删除采样点位数据成功");
                finish();
                break;
            case R.id.btn_add_blank:
                mProjectDetial.setDays(Integer.parseInt(etDays.getText().toString()));
                mProjectDetial.setPeriod(Integer.parseInt(etPeriod.getText().toString()));
                mProjectDetial.setComment(etComment.getText().toString());
                mProjectDetial.setUpdateTime(DateUtils.getWholeDate());
                DBHelper.get().getProjectDetialDao().update(mProjectDetial);
                mProject.setIsSamplingEidt(true);
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
        List<Tags> tags1 = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.ParentId.eq(tagParentId)).list();
        if (!CheckUtil.isEmpty(tags1)) {
            mTags.addAll(tags1);
        }
        mTagAdapter.notifyDataSetChanged();
    }


}
