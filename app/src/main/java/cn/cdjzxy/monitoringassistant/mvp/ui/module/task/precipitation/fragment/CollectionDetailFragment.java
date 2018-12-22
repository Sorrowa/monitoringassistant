package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

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


    public CollectionDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_precipitation_collect_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mSampling = PrecipitationActivity.mSampling;

//        // TODO: 2018/12/22 删除
//        mSampling.setSamplingNo("");

//        if (TextUtils.isEmpty(mSampling.getSamplingNo())) {
//            //无样品编码
//
//            //JS(要素)181029(日期)-01(点位)01(账号)-01(频次)
//            String samplingNo;
//
//            String snDate = DateUtil.getDate().replace("-", "").substring(2);
//            String snPointPosition = mSampling.getAddressNo();
//            String snUserId = UserInfoHelper.get().getUser().getWorkNo();
//            // TODO: 2018/12/22 频次
//            String snFrequency = "01";
//
//            samplingNo = "JS" + snDate + "-" + snPointPosition + snUserId + "-" + snFrequency;
//
//            tvSampleCode.setText(samplingNo);
//            tvFrequency.setText(snFrequency);
//        }else {
//            //已有样品编码
//            tvSampleCode.setText(mSampling.getSamplingNo());
//            tvFrequency.setText(mSampling.getSamplingNo().substring(12,14));
//        }




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

    @OnClick(R.id.btn_back)
    public void onClick() {
        EventBus.getDefault().post(1, EventBusTags.TAG_PRECIPITATION_COLLECTION);
    }

    @OnClick({R.id.tv_start_time, R.id.tv_end_time, R.id.btn_delete, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_start_time:
                // TODO: 2018/12/22
                tvStartTime.setText(DateUtil.getDate() + " " + DateUtil.getTime());
                break;
            case R.id.tv_end_time:
                // TODO: 2018/12/22
                tvEndTime.setText(DateUtil.getDate() + " " + DateUtil.getTime());
                break;
            case R.id.btn_delete:
                break;
            case R.id.btn_save:
                // TODO: 2018/12/22 提交检查
                SamplingDetail samplingDetail = new SamplingDetail();
                samplingDetail.setSampingCode(tvSampleCode.getText().toString());
                samplingDetail.setFrequecyNo(Integer.parseInt(tvFrequency.getText().toString()));
                JSONObject jsonObject = new JSONObject();

                samplingDetail.setSampingCode(tvSampleCode.getText().toString());
                samplingDetail.setSampingCode(tvSampleCode.getText().toString());
                samplingDetail.setSampingCode(tvSampleCode.getText().toString());
                samplingDetail.setSampingCode(tvSampleCode.getText().toString());

                break;
        }
    }
}
