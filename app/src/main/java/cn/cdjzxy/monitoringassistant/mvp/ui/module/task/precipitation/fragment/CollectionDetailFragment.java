package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment;


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

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.utils.DateUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.MyInputFilter;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;

/**
 * 采集样品详情
 */

public class CollectionDetailFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.tv_sample_code)
    TextView tvSampleCode;
    @BindView(R.id.tv_frequency)
    TextView tvFrequency;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.et_precipitation)
    EditText etPrecipitation;
    @BindView(R.id.et_rainwater_volume)
    EditText etRainwaterVolume;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;
    @BindView(R.id.btn_save)
    RelativeLayout btnSave;
    private Sampling mSampling;
    private boolean isStartTime;


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

            creatSampleDetailNo();

            tvStartTime.setText("");
            tvEndTime.setText("");
            etPrecipitation.setText("");
            etRainwaterVolume.setText("");
            etRemark.setText("");
        }
    }

    private void creatSampleDetailNo() {
        mSampling = PrecipitationActivity.mSampling;

        // TODO: 2018/12/22 mSamplingDetail
        if (TextUtils.isEmpty(mSampling.getSamplingNo())) {
            //无样品编码

            //JS(要素)181029(日期)-01(点位)01(账号)-01(频次)
            String samplingNo;

            String snDate = DateUtil.getDate().replace("-", "").substring(2);
            String snPointPosition = mSampling.getAddressNo();
            String snUserId = UserInfoHelper.get().getUser().getWorkNo();
            // TODO: 2018/12/22 频次
            int snFrequency = 1;

            samplingNo = "JS" + snDate + "-" + snPointPosition + snUserId + "-" + StringUtil.autoGenericCode(snFrequency,2);

            tvSampleCode.setText(samplingNo);
            tvFrequency.setText(snFrequency);
        }else {
            //已有样品编码
            // TODO: 2018/12/23
            tvSampleCode.setText(mSampling.getSamplingNo());
//            tvFrequency.setText(mSampling.getSamplingNo().substring(12,14));
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
                ArtUtils.makeText(getContext(),"请选择开始时间");
                EventBus.getDefault().post(1, EventBusTags.TAG_PRECIPITATION_COLLECTION);
                break;
            case R.id.btn_save:
                if (saveCheck()) {
                    SamplingDetail samplingDetail = new SamplingDetail();
                    samplingDetail.setSampingCode(tvSampleCode.getText().toString());
//                    samplingDetail.setFrequecyNo(Integer.parseInt(tvFrequency.getText().toString()));

                    HashMap<String, String> map = new HashMap<>();
                    map.put("SDataTime",tvStartTime.getText().toString());
                    map.put("EDataTime",tvEndTime.getText().toString());
                    map.put("RainVol",etRainwaterVolume.getText().toString());
                    samplingDetail.setPrivateData(new JSONObject(map).toString());

                    samplingDetail.setValue(etPrecipitation.getText().toString());
                    samplingDetail.setDescription(etRemark.getText().toString());

                    if (mSampling.getSamplingDetailResults() == null) {
                        mSampling.setSamplingDetailResults(new ArrayList<SamplingDetail>());
                    }
                    mSampling.getSamplingDetailResults().add(samplingDetail);

                    EventBus.getDefault().post(1, EventBusTags.TAG_PRECIPITATION_COLLECTION);
                    ArtUtils.makeText(getContext(),"请选择开始时间");
                }

                break;
        }
    }

    private boolean saveCheck() {
        if (TextUtils.isEmpty(tvStartTime.getText().toString())) {
            ArtUtils.makeText(getContext(),"请选择开始时间");
            return false;
        }
        if (TextUtils.isEmpty(tvEndTime.getText().toString())) {
            ArtUtils.makeText(getContext(),"请选择结束时间");
            return false;
        }
        if (TextUtils.isEmpty(etPrecipitation.getText().toString())) {
            ArtUtils.makeText(getContext(),"请输入降水量");
            return false;
        }
        if (TextUtils.isEmpty(etRainwaterVolume.getText().toString())) {
            ArtUtils.makeText(getContext(),"请输入雨水体积");
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
                        if (!TextUtils.isEmpty(tvEndTime.getText().toString()) && DateUtils.compareTime(selectTime,tvEndTime.getText().toString())){
                            ArtUtils.makeText(getContext(),"结束时间必须大于开始时间");
                            return;
                        }
                        tvStartTime.setText(selectTime);
                    }else {
                        if (!TextUtils.isEmpty(tvStartTime.getText().toString()) && DateUtils.compareTime(selectTime,tvStartTime.getText().toString())){
                            ArtUtils.makeText(getContext(),"结束时间必须大于开始时间");
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
}
