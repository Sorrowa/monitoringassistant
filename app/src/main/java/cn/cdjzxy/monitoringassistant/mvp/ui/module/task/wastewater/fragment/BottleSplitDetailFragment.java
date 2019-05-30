package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import org.simple.eventbus.EventBus;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.PlaceActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.BottleMonItemActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.HelpUtil;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity.mProject;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity.mSample;

/**
 * 分瓶信息详情
 */

public class BottleSplitDetailFragment extends BaseFragment {

    @BindView(R.id.my_layout_sample_project)
    MyDrawableLinearLayout sample_project;//监测项目
    @BindView(R.id.my_layout_sample_quantity)
    MyDrawableLinearLayout sample_quantity;//采样量
    @BindView(R.id.my_layout_sample_vessel)
    MyDrawableLinearLayout sample_vessel;//容器
    @BindView(R.id.my_layout_sample_number)
    MyDrawableLinearLayout sample_number;//数量
    @BindView(R.id.my_layout_sample_method)
    MyDrawableLinearLayout sample_method;//保存方法
    @BindView(R.id.my_layout_sample_date)
    MyDrawableLinearLayout sample_date;//保存时间
    @BindView(R.id.my_layout_sample_place)
    MyDrawableLinearLayout sample_place;//分析地点
    @BindView(R.id.operate_layout)
    View operate_layout;


    Unbinder unbinder;

    private int bottleListPosition;
    private SamplingFormStand bottleSplit;
    private String monItemId;//未选中的itemId


    public BottleSplitDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_bottle_split_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!CheckUtil.isNull(mSample) && !mSample.getIsCanEdit()) {
            operate_layout.setVisibility(View.INVISIBLE);

        } else {
            operate_layout.setVisibility(View.VISIBLE);
        }

    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            sample_project.setRightTextStr("");
            sample_quantity.setEditTextStr("");
            sample_vessel.setEditTextStr("");
            sample_number.setEditTextStr("");
            sample_method.setEditTextStr("");
            sample_place.setRightTextStr("");
            setBottleSplitDetail();
        }
    }

    private void setBottleSplitDetail() {
        List<SamplingFormStand> samplingFormStandResults = mSample.getSamplingFormStandResults();
        SharedPreferences collectListSettings = getActivity().getSharedPreferences("setting", 0);
        bottleListPosition = collectListSettings.getInt("bottleListPosition", -1);
        if (bottleListPosition == -1) {
            bottleSplit = new SamplingFormStand();
            bottleSplit.setId(UUID.randomUUID().toString());
        } else {
            bottleSplit = samplingFormStandResults.get(bottleListPosition);
            sample_project.setRightTextStr(CheckUtil.isEmpty(bottleSplit.getMonitemName()) ? "" : bottleSplit.getMonitemName());
            sample_quantity.setEditTextStr(CheckUtil.isEmpty(bottleSplit.getSamplingAmount()) ? "" : bottleSplit.getSamplingAmount());
            sample_vessel.setEditTextStr(CheckUtil.isEmpty(bottleSplit.getContainer()) ? "" : bottleSplit.getContainer());
            sample_number.setEditTextStr(bottleSplit.getCount() + "");
            sample_method.setEditTextStr(CheckUtil.isEmpty(bottleSplit.getSaveMehtod()) ? "" : bottleSplit.getSaveMehtod());
            sample_date.setEditTextStr(CheckUtil.isEmpty(bottleSplit.getSaveTimes()) ? "" : bottleSplit.getSaveTimes());
            sample_place.setRightTextStr(CheckUtil.isEmpty(bottleSplit.getAnalysisSite()) ? "" : bottleSplit.getAnalysisSite());
        }
        //设置初始化的MonitemIds

    }

    @OnClick({R.id.btn_back, R.id.my_layout_sample_place, R.id.my_layout_sample_project, R.id.btn_save, R.id.btn_delete})
    public void onClick(View view) {
        hideSoftInput();
        if (view.getId() == R.id.btn_back) {
            EventBus.getDefault().post(2, EventBusTags.TAG_WASTEWATER_BOTTLE);
            return;
        }
        if (!WastewaterActivity.isNewCreate && !UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
            showNoPermissionDialog("才能进行表单编辑。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
            return;
        }
        switch (view.getId()) {
            case R.id.btn_back:
                EventBus.getDefault().post(2, EventBusTags.TAG_WASTEWATER_BOTTLE);
                break;
            case R.id.my_layout_sample_project:
                showMonitorItems();
                break;
            case R.id.my_layout_sample_place:
                showAnalysisPlace();
                break;
            case R.id.btn_delete:
                operateDelete();
                break;
            case R.id.btn_save:
                operateSave();
                break;
            default:
                break;

        }
    }


    /**
     * delete operate
     */
    private void operateDelete() {
        if (CheckUtil.isEmpty(mSample.getSamplingFormStandResults()) || bottleListPosition == -1) {
            ArtUtils.makeText(getContext(), "请先添加分瓶数据");
            return;
        }
        showDeleteDialog();
    }


    /**
     * 弹出删除对话框
     */
    private void showDeleteDialog() {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("确定删除数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SamplingFormStand bottleSplit = mSample.getSamplingFormStandResults().get(bottleListPosition);

                        SamplingFormStand samplingBottleSplit = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.Id.eq(bottleSplit.getId())).unique();
                        if (!CheckUtil.isNull(samplingBottleSplit)) {
                            DBHelper.get().getSamplingFormStandDao().delete(samplingBottleSplit);
                        }

                        mSample.getSamplingFormStandResults().remove(bottleListPosition);

                        mSample.setIsFinish(SamplingUtil.isSamplingFinsh(mSample));
                        mSample.setStatusName(mSample.getIsFinish() ? "已完成" : "进行中");
                        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSample.getId())).unique();
                        if (CheckUtil.isNull(sampling)) {
                            DBHelper.get().getSamplingDao().insert(mSample);
                        } else {
                            DBHelper.get().getSamplingDao().update(mSample);
                        }

                        //更新index
                        updateAllBottleIndex();

                        ArtUtils.makeText(getContext(), "删除成功");
                        EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                        EventBus.getDefault().post(2, EventBusTags.TAG_WASTEWATER_BOTTLE);
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
     * 2019年5月10   这里的保存 应该这样做：最容易理解的逻辑
     * 拿到用户选择后的监测项目组装一个分瓶里面 剩下的按照监测标准分瓶
     * save
     */
    private void operateSave() {
        if (isSaveChecked()) {
            bottleSplit.setSamplingId(mSample.getId());
            bottleSplit.setContainer(sample_vessel.getEditTextStr());
            bottleSplit.setCount(Integer.parseInt(sample_number.getEditTextStr()));
            bottleSplit.setSaveMehtod(sample_method.getEditTextStr());
            bottleSplit.setSaveTimes(sample_date.getEditTextStr());
            bottleSplit.setAnalysisSite(sample_place.getRightTextViewStr());
            bottleSplit.setSamplingAmount(sample_quantity.getEditTextStr());
            bottleSplit.setStandNo(HelpUtil.generateNewBottleIndex(mSample.getId()));

            if (monItemId == null || monItemId.equals("")) {
                if (DbHelpUtils.getSamplingFormStandDaoForId(bottleSplit.getId()) != null) {
                    DBHelper.get().getSamplingFormStandDao().update(bottleSplit);
                }
                mSample.getSamplingFormStandResults().set(bottleListPosition, bottleSplit);
            } else {
                //生成分瓶信息
                List<SamplingFormStand> list = DbHelpUtils.getSamplingFormStandListForSampId(mSample.getId());
                if (!CheckUtil.isEmpty(list)) {
                    DBHelper.get().getSamplingFormStandDao().deleteInTx(list);
                }
                if (mSample.getSamplingFormStandResults() != null) {
                    mSample.getSamplingFormStandResults().clear();
                }
                if (monItemId != null && !monItemId.equals("")) {
                    String[] monItemIds = monItemId.split(",");
                    for (String s : monItemIds) {
                        HelpUtil.createAndUpdateBottle(s, mSample);
                    }
                }
                bottleSplit.setIndex(HelpUtil.generateNewBottleIndex(mSample.getId()));
                DBHelper.get().getSamplingFormStandDao().insert(bottleSplit);
                //这里要从新生成样品数量
                List<SamplingContent> samplingContentList = DbHelpUtils.getSamplingContentList(mSample.getId(), mProject.getId());
                if (mSample.getSamplingContentResults() == null || mSample.getSamplingContentResults().size() == 0) {
                    mSample.setSamplingContentResults(samplingContentList);
                }
                if (!CheckUtil.isEmpty(samplingContentList))
                    DBHelper.get().getSamplingContentDao().deleteInTx(samplingContentList);
                List<SamplingContent> contentList = HelpUtil.setSamplingCountList(mSample);
                mSample.setSamplingContentResults(contentList);
                if (!CheckUtil.isEmpty(contentList))
                    DBHelper.get().getSamplingContentDao().insertInTx(contentList);
            }
            mSample.setSamplingFormStandResults(DbHelpUtils.getSamplingFormStandListForSampId(mSample.getId()));
            mSample.setIsFinish(SamplingUtil.isSamplingFinsh(mSample));
            mSample.setStatusName(mSample.getIsFinish() ? "已完成" : "进行中");
            Sampling sampling = DbHelpUtils.getDbSampling(mSample.getId());
            if (CheckUtil.isNull(sampling)) {
                mSample.setId(UUID.randomUUID().toString());
                DBHelper.get().getSamplingDao().insert(mSample);
            } else {
                DBHelper.get().getSamplingDao().update(mSample);
            }
            EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
            EventBus.getDefault().post(2, EventBusTags.TAG_WASTEWATER_BOTTLE);
            ArtUtils.makeText(getContext(), "保存成功");
        }
    }


    /**
     * 判断状态是否可保存
     *
     * @return
     */
    private boolean isSaveChecked() {
        if (TextUtils.isEmpty(bottleSplit.getMonitemIds())) {
            ArtUtils.makeText(getContext(), "请选择监测项目");
            return false;
        }

        return true;
    }

    /**
     * 选择监测项目
     */
    private void showMonitorItems() {
        Intent intent = new Intent(getContext(), BottleMonItemActivity.class);
        intent.putExtra("tagId", mSample.getParentTagId());
        intent.putExtra("samplingId", mSample.getId());
        if (!CheckUtil.isEmpty(bottleSplit.getMonitemIds())) {
            intent.putExtra("selectItems", bottleSplit.getMonitemIds());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    bottleSplit.setMonitemName(data.getStringExtra("selectMonItemName"));
                    bottleSplit.setMonitemIds(data.getStringExtra("selectMonItemId"));
                    sample_project.setRightTextStr(bottleSplit.getMonitemName());
                    monItemId = data.getStringExtra("MonItemId");//用户没选择的监测项目
                }
            }
        });
    }

    /**
     * 分析地点选择
     */
    private void showAnalysisPlace() {
        Intent intent = new Intent(getContext(), PlaceActivity.class);
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!CheckUtil.isEmpty(data.getStringExtra("place"))) {
                        bottleSplit.setAnalysisSite(data.getStringExtra("place"));
                        sample_place.setRightTextStr(bottleSplit.getAnalysisSite());
                    }
                }
            }
        });
    }


    /**
     * 更新分瓶信息的index
     */
    private void updateAllBottleIndex() {
        List<SamplingFormStand> samplingFormStandList = mSample.getSamplingFormStandResults();
        if (!CheckUtil.isEmpty(samplingFormStandList)) {
            for (int i = 0; i < samplingFormStandList.size(); i++) {
                samplingFormStandList.get(i).setIndex(i + 1);
                samplingFormStandList.get(i).setStandNo(i + 1);
            }
        }
        DBHelper.get().getSamplingFormStandDao().updateInTx(samplingFormStandList);
    }
}
