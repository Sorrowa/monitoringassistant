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
import com.wonders.health.lib.base.mvp.Message;
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
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.setting.SettingFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.MyInputFilter;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;

/**
 * 采集样品详情
 */

public class CollectionDetailFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.tv_sample_code)
    TextView       tvSampleCode;
    @BindView(R.id.tv_frequency)
    TextView       tvFrequency;
    @BindView(R.id.tv_start_time)
    TextView       tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView       tvEndTime;
    @BindView(R.id.et_precipitation)
    EditText       etPrecipitation;
    @BindView(R.id.et_rainwater_volume)
    EditText       etRainwaterVolume;
    @BindView(R.id.et_remark)
    EditText       etRemark;
    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @BindView(R.id.btn_save)
    RelativeLayout btnSave;
    private Sampling mSampling;
    private boolean  isStartTime;
    private int listPosition;


    public CollectionDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_precipitation_collect_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        etPrecipitation.setFilters(new InputFilter[]{new MyInputFilter(2)});
        etRainwaterVolume.setFilters(new InputFilter[]{new MyInputFilter(2)});
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
            tvStartTime.setText("");
            tvEndTime.setText("");
            etPrecipitation.setText("");
            etRainwaterVolume.setText("");
            etRemark.setText("");

            creatSampleDetailNo();
        }
    }

    private void creatSampleDetailNo() {
        mSampling = PrecipitationActivity.mSampling;

        List<SamplingDetail> samplingDetailResults = mSampling.getSamplingDetailResults();

        SharedPreferences collectListSettings= getActivity().getSharedPreferences("setting", 0);
        listPosition = collectListSettings.getInt("listPosition",-1);

        if (listPosition == -1) {
            //添加 生成编码

            //JS(要素)181029(日期)-01(点位)01(账号)-01(频次)
            String samplingNo;

            String snDate = DateUtils.getDate().replace("-", "").substring(2);
            String snPointPosition = mSampling.getAddressNo();
            String snUserId = UserInfoHelper.get().getUser().getIntId() + "";
            int snFrequency = 1;
            if (samplingDetailResults != null
                    && samplingDetailResults.size() > 0) {
                snFrequency = samplingDetailResults.get(samplingDetailResults.size() - 1).getFrequecyNo() + 1;
            }

            samplingNo = "JS" + snDate + "-" + snPointPosition + snUserId + "-" + StringUtil.autoGenericCode(snFrequency, 2);

            tvSampleCode.setText(samplingNo);
            tvFrequency.setText(snFrequency + "");
        }else {
            btnDelete.setVisibility(View.VISIBLE);

            SamplingDetail samplingDetail = samplingDetailResults.get(listPosition);
            tvSampleCode.setText(samplingDetail.getSampingCode());
            tvFrequency.setText(samplingDetail.getFrequecyNo()+"");

            String privateData = samplingDetail.getPrivateData();
            try {
                JSONObject jsonObject = new JSONObject(privateData);
                tvStartTime.setText(jsonObject.getString("SDataTime"));
                tvEndTime.setText(jsonObject.getString("EDataTime"));
                etRainwaterVolume.setText(jsonObject.getString("RainVol"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            etPrecipitation.setText(samplingDetail.getValue1());
            etRemark.setText(samplingDetail.getDescription());
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

    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.btn_delete, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                isStartTime = true;
                initTimePickerView();
                break;
            case R.id.tv_end_time:
                isStartTime = false;
                initTimePickerView();
                break;
            case R.id.btn_delete:
                showDelDialog();
                break;
            case R.id.btn_save:
                if (saveCheck()) {
                    SamplingDetail samplingDetail;
                    if (listPosition ==-1) {
                        samplingDetail = new SamplingDetail();
                    }else {
                        samplingDetail = mSampling.getSamplingDetailResults().get(listPosition);
                    }

                    samplingDetail.setId("LC-" + UUID.randomUUID().toString());
                    samplingDetail.setSamplingId(PrecipitationActivity.mSampling.getId());
                    samplingDetail.setSampingCode(tvSampleCode.getText().toString());
                    samplingDetail.setFrequecyNo(Integer.parseInt(tvFrequency.getText().toString()));

                    HashMap<String, String> map = new HashMap<>();
                    map.put("SDataTime", tvStartTime.getText().toString());
                    map.put("EDataTime", tvEndTime.getText().toString());
                    map.put("RainVol", etRainwaterVolume.getText().toString());
                    samplingDetail.setPrivateData(new JSONObject(map).toString());

                    samplingDetail.setValue(etPrecipitation.getText().toString());
                    samplingDetail.setValue1(etPrecipitation.getText().toString());
                    samplingDetail.setDescription(etRemark.getText().toString());

                    if (listPosition == -1) {
                        if (mSampling.getSamplingDetailResults() == null) {
                            mSampling.setSamplingDetailResults(new ArrayList<SamplingDetail>());
                        }
                        List<SamplingDetail> samplingDetailResults = mSampling.getSamplingDetailResults();
                        samplingDetailResults.add(samplingDetail);
                        mSampling.setSamplingDetailResults(samplingDetailResults);
                    }

                    EventBus.getDefault().post(1, EventBusTags.TAG_PRECIPITATION_COLLECTION);
                    ArtUtils.makeText(getContext(), "保存成功");
                }

                break;
        }
    }

    private boolean saveCheck() {
        if (TextUtils.isEmpty(tvStartTime.getText().toString())) {
            ArtUtils.makeText(getContext(), "请选择开始时间");
            return false;
        }
        if (TextUtils.isEmpty(tvEndTime.getText().toString())) {
            ArtUtils.makeText(getContext(), "请选择结束时间");
            return false;
        }
        if (TextUtils.isEmpty(etPrecipitation.getText().toString())) {
            ArtUtils.makeText(getContext(), "请输入降水量");
            return false;
        }
        if (TextUtils.isEmpty(etRainwaterVolume.getText().toString())) {
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
                String selectTime = DateUtils.getTime(date.getTime());
                try {
                    if (isStartTime) {
                        if (!TextUtils.isEmpty(tvEndTime.getText().toString()) && !DateUtils.compareTime(selectTime, tvEndTime.getText().toString())) {
                            ArtUtils.makeText(getContext(), "开始时间必须小于结束时间");
                            return;
                        }
                        tvStartTime.setText(selectTime);
                    } else {
                        if (!TextUtils.isEmpty(tvStartTime.getText().toString()) && DateUtils.compareTime(selectTime, tvStartTime.getText().toString())) {
                            ArtUtils.makeText(getContext(), "结束时间必须大于开始时间");
                            return;
                        }
                        tvEndTime.setText(selectTime);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }).setType(new boolean[]{true, true, true, true, true, true})
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
                        mSampling.getSamplingDetailResults().remove(listPosition);
                        ArtUtils.makeText(getContext(), "删除成功");
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
}
