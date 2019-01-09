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
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.MonItemActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;

/**
 * 采集样品详情
 */

public class CollectionDetailFragment extends BaseFragment {
    @BindView(R.id.sample_code)
    TextView sample_code;
    @BindView(R.id.sample_frequency)
    EditText sample_frequency;
    @BindView(R.id.sample_quality)
    EditText sample_quality;
    @BindView(R.id.sample_monitor_items)
    TextView sample_monitor_items;
    @BindView(R.id.sample_monitor)
    TextView sample_monitor;
    @BindView(R.id.sample_add_preserve)
    CheckedTextView sample_add_preserve;
    @BindView(R.id.sample_compare_monitor)
    CheckedTextView sample_compare_monitor;
    @BindView(R.id.sample_mark)
    TextView sample_mark;
    @BindView(R.id.sample_monitor_items_title)
    TextView sample_monitor_items_title;
    @BindView(R.id.sample_monitor_title)
    TextView sample_monitor_title;

    Unbinder unbinder;

    private int fsListPosition;
    private Sampling mSample;
    SamplingDetail samplingDetail;


    public CollectionDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_collect_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        sample_add_preserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_add_preserve.setChecked(!sample_add_preserve.isChecked());
            }
        });

        sample_compare_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_compare_monitor.setChecked(!sample_compare_monitor.isChecked());
            }
        });

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            sample_code.setText("");
            sample_frequency.setText("");
            sample_quality.setText("");
            sample_monitor_items.setText("");
            sample_monitor.setText("");
            sample_mark.setText("");
            setSamplingDetail();
        }

    }

    private void setSamplingDetail() {
        mSample = WastewaterActivity.mSample;
        List<SamplingDetail> samplingDetailResults = mSample.getSamplingDetailResults();
        SharedPreferences collectListSettings = getActivity().getSharedPreferences("setting", 0);
        fsListPosition = collectListSettings.getInt("fsListPosition", -1);
        if (fsListPosition == -1) {
            samplingDetail = new SamplingDetail();

            String samplingNo;

            String snDate = DateUtils.getDate().replace("-", "").substring(2);
            String snPointPosition = "";
            if (!CheckUtil.isEmpty(mSample.getAddressNo())) {
                snPointPosition = mSample.getAddressNo();
            }
            String snUserId = UserInfoHelper.get().getUser().getIntId() + "";
            int snFrequency = 1;
            if (samplingDetailResults != null && samplingDetailResults.size() > 0) {
                snFrequency = samplingDetailResults.get(samplingDetailResults.size() - 1).getFrequecyNo() + 1;
            }

            samplingNo = "FS" + snDate + "-" + snPointPosition + snUserId + "-" + StringUtil.autoGenericCode(snFrequency, 2);

            sample_code.setText(samplingNo);
            sample_frequency.setText(snFrequency + "");

        } else {
            samplingDetail = samplingDetailResults.get(fsListPosition);
            sample_code.setText(samplingDetail.getSampingCode());
            sample_frequency.setText(samplingDetail.getFrequecyNo() + "");
            sample_frequency.setText(samplingDetail.getFrequecyNo() + "");

            sample_monitor_items.setText(samplingDetail.getMonitemName());
            sample_monitor.setText(samplingDetail.getAddressName());

            sample_add_preserve.setChecked(samplingDetail.getIsAddPreserve());
            sample_compare_monitor.setChecked(samplingDetail.getIsCompare());
            sample_mark.setText(samplingDetail.getDescription());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.sample_monitor_items, R.id.sample_monitor, R.id.btn_delete, R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                EventBus.getDefault().post(1, EventBusTags.TAG_WASTEWATER_COLLECTION);
                break;
            case R.id.sample_monitor_items:
                showMonitorItems();
                break;
            case R.id.sample_monitor:
                showAddressItems();
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
        if (CheckUtil.isEmpty(mSample.getSamplingDetailResults()) || fsListPosition == -1) {
            ArtUtils.makeText(getContext(), "请先添加采样数据");
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
                        SamplingDetail currentDetail = mSample.getSamplingDetailResults().get(fsListPosition);

                        SamplingDetail samplingDetail = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.Id.eq(currentDetail.getId())).unique();
                        if (!CheckUtil.isNull(samplingDetail)) {
                            DBHelper.get().getSamplingDetailDao().delete(samplingDetail);
                        }

                        mSample.getSamplingDetailResults().remove(fsListPosition);

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
                        EventBus.getDefault().post(1, EventBusTags.TAG_WASTEWATER_COLLECTION);
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
//            SamplingDetail samplingDetail;
//            if (fsListPosition == -1) {
//                samplingDetail = new SamplingDetail();
//            } else {
//                samplingDetail = mSample.getSamplingDetailResults().get(fsListPosition);
//            }
            samplingDetail.setProjectId(mSample.getProjectId());
            samplingDetail.setId("FS-" + UUID.randomUUID().toString());
            samplingDetail.setSamplingId(mSample.getId());
            samplingDetail.setSampingCode(sample_code.getText().toString());
            samplingDetail.setFrequecyNo(Integer.parseInt(sample_frequency.getText().toString()));

            samplingDetail.setValueUnitNname(sample_add_preserve.getText().toString());
            samplingDetail.setValue1(sample_monitor.getText().toString());
            samplingDetail.setDescription(sample_mark.getText().toString());

            if (fsListPosition == -1) {
                if (mSample.getSamplingDetailResults() == null) {
                    mSample.setSamplingDetailResults(new ArrayList<SamplingDetail>());
                }
                List<SamplingDetail> samplingDetailResults = mSample.getSamplingDetailResults();
                samplingDetailResults.add(samplingDetail);
                mSample.setSamplingDetailResults(samplingDetailResults);
            }


            mSample.setIsFinish(isSamplingFinish());
            mSample.setStatusName(isSamplingFinish() ? "已完成" : "进行中");
            Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSample.getId())).unique();
            if (CheckUtil.isNull(sampling)) {
                DBHelper.get().getSamplingDao().insert(mSample);
            } else {
                DBHelper.get().getSamplingDao().update(mSample);
            }

            SamplingDetail samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(samplingDetail.getId())).unique();
            if (!CheckUtil.isNull(samplingDetails)) {
                DBHelper.get().getSamplingDetailDao().delete(samplingDetails);
            }
            DBHelper.get().getSamplingDetailDao().insert(samplingDetail);


            EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
            EventBus.getDefault().post(1, EventBusTags.TAG_WASTEWATER_COLLECTION);
            ArtUtils.makeText(getContext(), "保存成功");
        }
    }


    private boolean isSaveChecked() {
        if (TextUtils.isEmpty(sample_monitor_items.getText().toString())) {
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
    private void showMonitorItems(){
        Intent intent = new Intent(getContext(), MonItemActivity.class);
        intent.putExtra("tagId", mSample.getParentTagId());
        if (!CheckUtil.isEmpty(samplingDetail.getMonitemId())){
            intent.putExtra("selectItems", samplingDetail.getMonitemId());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!CheckUtil.isEmpty(data.getStringExtra("MonItemId")) && !CheckUtil.isEmpty(data.getStringExtra("MonItemName"))) {
                        samplingDetail.setMonitemName(data.getStringExtra("MonItemName"));
                        samplingDetail.setMonitemId(data.getStringExtra("MonItemId"));
                        sample_monitor_items.setText(samplingDetail.getMonitemName());
                        if (!CheckUtil.isEmpty(data.getStringExtra("MonItemId"))){
                            sample_monitor_items_title.setText("监测项目("+data.getStringExtra("MonItemId").split(",").length+")");
                        }
                    }
                }
            }
        });
    }

    /**
     * 选择现场监测项目
     */
    private void showAddressItems(){
        Intent intent = new Intent(getContext(), MonItemActivity.class);
        intent.putExtra("tagId", mSample.getParentTagId());
        if (!CheckUtil.isEmpty(samplingDetail.getAddresssId())){
            intent.putExtra("selectItems", samplingDetail.getAddresssId());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!CheckUtil.isEmpty(data.getStringExtra("MonItemId")) && !CheckUtil.isEmpty(data.getStringExtra("MonItemName"))) {
                        samplingDetail.setAddressName(data.getStringExtra("MonItemName"));
                        samplingDetail.setAddresssId(data.getStringExtra("MonItemId"));
                        sample_monitor.setText(samplingDetail.getAddressName());
                        if (!CheckUtil.isEmpty(data.getStringExtra("MonItemId"))){
                            sample_monitor_title.setText("现场监测("+data.getStringExtra("MonItemId").split(",").length+")");
                        }
                    }
                }
            }
        });
    }


}
