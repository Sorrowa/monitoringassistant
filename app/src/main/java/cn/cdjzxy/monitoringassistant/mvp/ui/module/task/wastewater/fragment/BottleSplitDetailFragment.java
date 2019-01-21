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

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.PlaceActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.MonItemActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;

/**
 * 分瓶信息详情
 */

public class BottleSplitDetailFragment extends BaseFragment {

    @BindView(R.id.sample_project)
    TextView sample_project;
    @BindView(R.id.sample_quantity)
    EditText sample_quantity;
    @BindView(R.id.sample_vessel)
    EditText sample_vessel;
    @BindView(R.id.sample_number)
    EditText sample_number;
    @BindView(R.id.sample_method)
    EditText sample_method;
    @BindView(R.id.sample_date)
    EditText sample_date;
    @BindView(R.id.sample_place)
    TextView sample_place;


    Unbinder unbinder;

    private Sampling mSample;
    private int bottleListPosition;
    private SamplingFormStand bottleSplit;


    public BottleSplitDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_bottle_split_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

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
            sample_project.setText("");
            sample_quantity.setText("");
            sample_vessel.setText("");
            sample_number.setText("");
            sample_method.setText("");
            sample_place.setText("");
            setBottleSplitDetail();
        }
    }

    private void setBottleSplitDetail() {
        mSample = WastewaterActivity.mSample;
        List<SamplingFormStand> samplingFormStandResults = mSample.getSamplingFormStandResults();
        SharedPreferences collectListSettings = getActivity().getSharedPreferences("setting", 0);
        bottleListPosition = collectListSettings.getInt("bottleListPosition", -1);
        if (bottleListPosition == -1) {
            bottleSplit = new SamplingFormStand();
            String samplingNo;

            String snDate = DateUtils.getDate().replace("-", "").substring(2);
            String snPointPosition = "";
            if (!CheckUtil.isEmpty(mSample.getAddressNo())) {
                snPointPosition = mSample.getAddressNo();
            }
            String snUserId = UserInfoHelper.get().getUser().getIntId() + "";
            int snFrequency = 1;
            if (samplingFormStandResults != null && samplingFormStandResults.size() > 0) {
                snFrequency = samplingFormStandResults.get(samplingFormStandResults.size() - 1).getIndex() + 1;
            }
            samplingNo = "FS" + snDate + "-" + snPointPosition + snUserId + "-" + StringUtil.autoGenericCode(snFrequency, 2);

        } else {
            bottleSplit = samplingFormStandResults.get(bottleListPosition);
            if (!CheckUtil.isEmpty(bottleSplit.getMonitemName())) {
                sample_project.setText(bottleSplit.getMonitemName());
            }
            sample_quantity.setText(bottleSplit.getSamplingAmount());
            sample_vessel.setText(bottleSplit.getContainer());
            sample_number.setText(bottleSplit.getCount()+"");
            sample_method.setText(bottleSplit.getSaveMehtod());
            sample_date.setText(bottleSplit.getSaveTimes());
            sample_place.setText(bottleSplit.getAnalysisSite());
        }

    }

    @OnClick({R.id.btn_back, R.id.sample_place, R.id.sample_project, R.id.btn_save, R.id.btn_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                EventBus.getDefault().post(2, EventBusTags.TAG_WASTEWATER_BOTTLE);
                break;
            case R.id.sample_project:
                showMonitorItems();
                break;
            case R.id.sample_place:
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

                        mSample.setIsFinish(isSamplingFinish());
                        mSample.setStatusName(isSamplingFinish() ? "已完成" : "进行中");
                        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSample.getId())).unique();
                        if (CheckUtil.isNull(sampling)) {
                            DBHelper.get().getSamplingDao().insert(mSample);
                        } else {
                            DBHelper.get().getSamplingDao().update(mSample);
                        }

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
     * 采样是否完成
     *
     * @return
     */
    private boolean isSamplingFinish() {
        if (CheckUtil.isEmpty(mSample.getSamplingDetailResults())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getSamplingUserName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getTagName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getAddressName())) {
            return false;
        }
//        if (CheckUtil.isEmpty(mSample.getPrivateData())) {
//            return false;
//        }
//        if (CheckUtil.isEmpty(mSample.getMethodName())) {
//            return false;
//        }
//        if (CheckUtil.isEmpty(mSample.getDeviceName())) {
//            return false;
//        }
        return true;

    }

    /**
     * save
     */
    private void operateSave() {
        if (isSaveChecked()) {
//            SamplingBottleSplit bottleSplit;
//            if (bottleListPosition == -1) {
//                bottleSplit = new SamplingBottleSplit();
//            } else {
//                bottleSplit = mSample.getSamplingBottleResults().get(bottleListPosition);
//            }
            //bottleSplit.setId("FS-" + UUID.randomUUID().toString());
            bottleSplit.setSamplingId(mSample.getId());
            bottleSplit.setContainer(sample_vessel.getText().toString());
            bottleSplit.setCount(Integer.parseInt(sample_number.getText().toString()));
            bottleSplit.setSaveMehtod(sample_method.getText().toString());
            bottleSplit.setSaveTimes(sample_date.getText().toString());
            bottleSplit.setAnalysisSite(sample_place.getText().toString());
            bottleSplit.setSamplingAmount(sample_quantity.getText().toString());

            /*
            if (bottleListPosition == -1) {
                if (mSample.getSamplingFormStandResults() == null) {
                    mSample.setSamplingFormStandResults(new ArrayList<>());
                }
                List<SamplingFormStand> samplingDetailResults = mSample.getSamplingFormStandResults();
                samplingDetailResults.add(bottleSplit);
                mSample.setSamplingFormStandResults(samplingDetailResults);
            }
            */


            mSample.setIsFinish(isSamplingFinish());
            mSample.setStatusName(isSamplingFinish() ? "已完成" : "进行中");
            Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSample.getId())).unique();
            if (CheckUtil.isNull(sampling)) {
                DBHelper.get().getSamplingDao().insert(mSample);
            } else {
                DBHelper.get().getSamplingDao().update(mSample);
            }

            //SamplingFormStand samplingBottleSplit = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.Id.eq(bottleSplit.getId())).unique();
//            if (bottleListPosition == -1) {
//                bottleSplit.setId(UUID.randomUUID().toString());
//                DBHelper.get().getSamplingFormStandDao().insert(bottleSplit);
//            }else {
//                DBHelper.get().getSamplingFormStandDao().updateInTx(bottleSplit);
//            }

            DBHelper.get().getSamplingFormStandDao().updateInTx(bottleSplit);
            //设置分瓶信息
            List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).orderAsc(SamplingFormStandDao.Properties.Index).list();
            if (!CheckUtil.isEmpty(formStantdsList)){
                WastewaterActivity.mSample.setSamplingFormStandResults(formStantdsList);
            }

            EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
            EventBus.getDefault().post(2, EventBusTags.TAG_WASTEWATER_BOTTLE);
            ArtUtils.makeText(getContext(), "保存成功");
        }
    }


    private boolean isSaveChecked() {
        if (TextUtils.isEmpty(sample_project.getText().toString())) {
            ArtUtils.makeText(getContext(), "请选择监测项目");
            return false;
        }
//        if (TextUtils.isEmpty(sample_monitor.getText().toString())) {
//            ArtUtils.makeText(getContext(), "请选择");
//            return false;
//        }
        return true;
    }

    /**
     * 选择监测项目
     */
    private void showMonitorItems() {
        Intent intent = new Intent(getContext(), MonItemActivity.class);
        intent.putExtra("tagId", mSample.getParentTagId());
        if (!CheckUtil.isEmpty(bottleSplit.getMonitemIds())) {
            intent.putExtra("selectItems", bottleSplit.getMonitemIds());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!CheckUtil.isEmpty(data.getStringExtra("MonItemId")) && !CheckUtil.isEmpty(data.getStringExtra("MonItemName"))) {
                        bottleSplit.setMonitemName(data.getStringExtra("MonItemName"));
                        bottleSplit.setMonitemIds(data.getStringExtra("MonItemId"));
                        sample_project.setText(bottleSplit.getMonitemName());
                    }
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
                        sample_place.setText(bottleSplit.getAnalysisSite());
                    }
                }
            }
        });
    }
}
