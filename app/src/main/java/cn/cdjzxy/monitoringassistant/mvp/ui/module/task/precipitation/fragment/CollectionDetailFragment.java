package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.MyInputFilter;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;

/**
 * 采集样品详情
 */

public class CollectionDetailFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.my_layout_sample_code)
    MyDrawableLinearLayout tvSampleCode;
    @BindView(R.id.my_layout_frequency)
    MyDrawableLinearLayout tvFrequency;
    @BindView(R.id.my_layout_start_time)
    MyDrawableLinearLayout tvStartTime;
    @BindView(R.id.my_layout_end_time)
    MyDrawableLinearLayout tvEndTime;
    @BindView(R.id.my_layout_precipitation)
    MyDrawableLinearLayout etPrecipitation;
    @BindView(R.id.my_layout_rainwater_volume)
    MyDrawableLinearLayout etRainwaterVolume;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @BindView(R.id.btn_save)
    RelativeLayout btnSave;
    private Sampling mSampling;
    private boolean isStartTime;
    private int listPosition;


    public CollectionDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_precipitation_collect_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        etPrecipitation.getEditText().setFilters(new InputFilter[]{new MyInputFilter(2)});
        etRainwaterVolume.getEditText().setFilters(new InputFilter[]{new MyInputFilter(2)});
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
            tvStartTime.setRightTextStr("");
            tvEndTime.setRightTextStr("");
            etPrecipitation.setEditTextStr("");
            etRainwaterVolume.setEditTextStr("");
            etRemark.setText("");
            createSampleDetailNo();
        }
    }

    private void createSampleDetailNo() {
        mSampling = PrecipitationActivity.mSampling;
        //拿到只有降水量的数据
        List<SamplingDetail> samplingDetailResults = mSampling.getSamplingDetailResults();
//        if (!CheckUtil.isEmpty(mSampling.getSamplingDetailResults())) {
//            for (SamplingDetail detail : mSampling.getSamplingDetailResults()) {
//                if (detail != null && detail.getMonitemName() != null && detail.getMonitemName().equals("降水量")) {
//                    samplingDetailResults.add(detail);
//                }
//            }
//        }

        SharedPreferences collectListSettings = getActivity().getSharedPreferences("setting", 0);
        listPosition = collectListSettings.getInt("listPosition", -1);

        if (listPosition == -1) {
            int snFrequency = 1;
            if (samplingDetailResults != null
                    && samplingDetailResults.size() > 0) {
                snFrequency = samplingDetailResults.size() + 1;
            }
            if (CheckUtil.isEmpty(mSampling.getSamplingNo())) {
                mSampling.setSamplingNo(SamplingUtil.createSamplingNo(mSampling.getSamplingTimeBegin()));
            }
//            新的样品编码规范——————2019年5月10更改
//            JS(样品性质——降水)——年月日——采样单流水号(时间相同就加一)采样人员系统编号——采样号
            String mSamplingNo = mSampling.getSamplingNo();//采样单编号
            String dateStr = DateUtils.strGetDate(mSampling.getSamplingTimeBegin()).replace("-", "").substring(2);//年月日
            String waterNo = mSamplingNo.substring(mSamplingNo.lastIndexOf("-") + 1, mSamplingNo.length());//采样单流水号
            String userId = UserInfoHelper.get().getUser().getIntId() + "-";//用户系统编号
            String samplingNo = "JS" + dateStr + "-" + waterNo + userId + StringUtil.autoGenericCode(snFrequency, 2);//采样号
            tvSampleCode.setRightTextStr(samplingNo);
            tvFrequency.setRightTextStr(snFrequency + "");
        } else {
            btnDelete.setVisibility(View.VISIBLE);

            SamplingDetail samplingDetail = samplingDetailResults.get(listPosition);
            tvSampleCode.setRightTextStr(samplingDetail.getSampingCode());
            tvFrequency.setRightTextStr(samplingDetail.getFrequecyNo() + "");

            String privateData = samplingDetail.getPrivateData();
            try {
                JSONObject jsonObject = new JSONObject(privateData);
                tvStartTime.setRightTextStr(jsonObject.getString("SDataTime"));
                tvEndTime.setRightTextStr(jsonObject.getString("EDataTime"));
                etRainwaterVolume.setEditTextStr(jsonObject.getString("RainVol"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            etPrecipitation.setEditTextStr(samplingDetail.getValue1());
            etRemark.setText(samplingDetail.getDescription());
        }

        if (!mSampling.getIsCanEdit()) {
            btnDelete.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);
            tvStartTime.setEnabled(mSampling.getIsCanEdit());
            tvEndTime.setEnabled(mSampling.getIsCanEdit());
            etPrecipitation.setEnabled(mSampling.getIsCanEdit());
            etRainwaterVolume.setEnabled(mSampling.getIsCanEdit());
            etRemark.setEnabled(mSampling.getIsCanEdit());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_back)
    public void onClick() {
        EventBus.getDefault().post(1, EventBusTags.TAG_PRECIPITATION_COLLECTION);
    }

    @OnClick({R.id.my_layout_start_time, R.id.my_layout_end_time, R.id.btn_delete, R.id.btn_save})
    public void onViewClicked(View view) {
        hideSoftInput();
        if (!PrecipitationActivity.isNewCreate &&
                !UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
            showNoPermissionDialog("才能进行表单编辑。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
            return;
        }
        switch (view.getId()) {
            case R.id.my_layout_start_time:
                isStartTime = true;
                initTimePickerView();
                break;
            case R.id.my_layout_end_time:
                isStartTime = false;
                initTimePickerView();
                break;
            case R.id.btn_delete:
                if (CheckUtil.isEmpty(mSampling.getSamplingDetailResults()) || listPosition == -1) {
                    ArtUtils.makeText(getContext(), "请先添加采样数据");
                    return;
                }
                showDelDialog();
                break;
            case R.id.btn_save:
                if (saveCheck()) {
                    SamplingDetail samplingDetail;
                    if (listPosition == -1) {
                        samplingDetail = new SamplingDetail();
                        samplingDetail.setMonitemName("降水量");
                        samplingDetail.setId("LC-" + UUID.randomUUID().toString());
                    } else {
                        samplingDetail = mSampling.getSamplingDetailResults().get(listPosition);
                    }

                    samplingDetail.setSamplingId(mSampling.getId());
                    samplingDetail.setSampingCode(tvSampleCode.getRightTextViewStr());
                    samplingDetail.setFrequecyNo(Integer.parseInt(tvFrequency.getRightTextViewStr()));
                    samplingDetail.setAddresssId(mSampling.getAddressId());
                    samplingDetail.setAddressName(mSampling.getAddressName());
                    samplingDetail.setDeviceIdName(mSampling.getDeviceName());
                    samplingDetail.setDeviceId(mSampling.getDeviceId());
                    samplingDetail.setMethodName(mSampling.getMonitemName());
                    samplingDetail.setMethodId(mSampling.getMonitemId());
                    samplingDetail.setIsSenceAnalysis(true);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("SDataTime", tvStartTime.getRightTextViewStr());
                    map.put("EDataTime", tvEndTime.getRightTextViewStr());
                    map.put("RainVol", etRainwaterVolume.getEditTextStr());
                    samplingDetail.setPrivateData(new JSONObject(map).toString());

                    samplingDetail.setValue(etPrecipitation.getEditTextStr());
                    samplingDetail.setValue1(etPrecipitation.getEditTextStr());
                    samplingDetail.setDescription(etRemark.getText().toString());


                    if (listPosition == -1) {
                        if (mSampling.getSamplingDetailResults() == null) {
                            mSampling.setSamplingDetailResults(new ArrayList<SamplingDetail>());
                        }
                        List<SamplingDetail> samplingDetailResults = mSampling.getSamplingDetailResults();

                        samplingDetailResults.add(samplingDetail);
                        mSampling.setSamplingDetailResults(samplingDetailResults);
                    }

                    mSampling.setIsFinish(isSamplingFinish());
                    mSampling.setStatusName(isSamplingFinish() ? "已完成" : "进行中");
                    Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSampling.getId())).unique();
                    if (CheckUtil.isNull(sampling)) {
                        DBHelper.get().getSamplingDao().insert(mSampling);
                    } else {
                        DBHelper.get().getSamplingDao().update(mSampling);
                    }

                    SamplingDetail samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.Id.eq(samplingDetail.getId())).unique();
                    if (CheckUtil.isNull(samplingDetails)) {
                        DBHelper.get().getSamplingDetailDao().insert(samplingDetail);
                    } else {
                        DBHelper.get().getSamplingDetailDao().update(samplingDetails);
                    }


                    EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                    EventBus.getDefault().post(1, EventBusTags.TAG_PRECIPITATION_COLLECTION);
                    ArtUtils.makeText(getContext(), "保存成功");
                }
                break;
        }

    }

    private boolean saveCheck() {
        if (TextUtils.isEmpty(tvStartTime.getRightTextViewStr())) {
            ArtUtils.makeText(getContext(), "请选择开始时间");
            return false;
        }
        if (TextUtils.isEmpty(tvEndTime.getRightTextViewStr())) {
            ArtUtils.makeText(getContext(), "请选择结束时间");
            return false;
        }
        if (TextUtils.isEmpty(etPrecipitation.getEditTextStr())) {
            ArtUtils.makeText(getContext(), "请输入降水量");
            return false;
        }
        if (TextUtils.isEmpty(etRainwaterVolume.getEditTextStr())) {
            ArtUtils.makeText(getContext(), "请输入雨水体积");
            return false;
        }
        return true;
    }

    private void initTimePickerView() {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String selectTime = DateUtils.getTimeShort(date.getTime());
                try {
                    if (isStartTime) {
                        if (!TextUtils.isEmpty(tvEndTime.getRightTextViewStr()) &&
                                !DateUtils.compareTime(selectTime, tvEndTime.getRightTextViewStr())) {
                            ArtUtils.makeText(getContext(), "开始时间必须小于结束时间");
                            return;
                        }
                        tvStartTime.setRightTextStr(selectTime);
                    } else {
                        if (!TextUtils.isEmpty(tvStartTime.getRightTextViewStr())
                                && DateUtils.compareTime(selectTime, tvStartTime.getRightTextViewStr())) {
                            ArtUtils.makeText(getContext(), "结束时间必须大于开始时间");
                            return;
                        }
                        tvEndTime.setRightTextStr(selectTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).setType(new boolean[]{true, true, true, true, true, false})
                .isCyclic(true)
                .build();
        pvTime.show();
    }


    private void showDelDialog() {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("确定删除数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SamplingDetail samplingDetail1 = mSampling.getSamplingDetailResults().get(listPosition);

                        SamplingDetail samplingDetails1 = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.Id.eq(samplingDetail1.getId())).unique();
                        if (!CheckUtil.isNull(samplingDetails1)) {
                            DBHelper.get().getSamplingDetailDao().delete(samplingDetails1);
                        }

                        mSampling.getSamplingDetailResults().remove(listPosition);

                        mSampling.setIsFinish(isSamplingFinish());
                        mSampling.setStatusName(isSamplingFinish() ? "已完成" : "进行中");
                        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSampling.getId())).unique();
                        if (CheckUtil.isNull(sampling)) {
                            DBHelper.get().getSamplingDao().insert(mSampling);
                        } else {
                            DBHelper.get().getSamplingDao().update(mSampling);
                        }

                        ArtUtils.makeText(getContext(), "删除成功");
                        EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                        EventBus.getDefault().post(1, EventBusTags.TAG_PRECIPITATION_COLLECTION);
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
        if (CheckUtil.isEmpty(mSampling.getSamplingDetailResults())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getSamplingUserName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getTagName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getAddressName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getPrivateData())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getMethodName())) {
            return false;
        }
        return !CheckUtil.isEmpty(mSampling.getDeviceName());
    }

}
