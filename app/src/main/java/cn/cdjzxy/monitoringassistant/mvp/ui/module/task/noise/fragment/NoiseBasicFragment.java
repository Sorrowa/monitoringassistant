package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.MethodActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.WeatherActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mProject;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;

public class NoiseBasicFragment extends BaseFragment {

    @BindView(R.id.base_sample_no)
    TextView tvSampleNo;//采样点编号
    @BindView(R.id.text_view_name)
    TextView tvSampleName;//项目名称
    @BindView(R.id.text_view_nature)
    TextView tvNature;//监测性质
    @BindView(R.id.text_view_date)
    TextView tvDate;//监测日期
    @BindView(R.id.edit_factory_name)
    EditText edFactoryName;//企业名称
    @BindView(R.id.text_view_factory_name)
    TextView tvFactoryName;//企业名称 用于展示（不可编辑）
    @BindView(R.id.edit_factory_address)
    EditText edFactoryAddress;//企业地址
    @BindView(R.id.text_view_factory_address)
    TextView tvFactoryAddress;//企业地址 用于展示（不可编辑）
    @BindView(R.id.edit_factory_info)
    EditText edFactoryInfo;//企业生产工况
    @BindView(R.id.text_weather_state)
    TextView tvWeatherState;//天气情况
    @BindView(R.id.edit_wind_speed)
    EditText edWingSpeed;//风速
    @BindView(R.id.edit_calibration_front)
    EditText edCalibrationFront;//测前校准值
    @BindView(R.id.edit_calibration_after)
    EditText edCalibrationAfter;//测后校准值
    @BindView(R.id.text_view_device_number)
    TextView tvDeviceNumber;//风速仪型号及编号
    @BindView(R.id.text_view_monitor_from)
    TextView tvMonitorForm;//监测方法及来源
    @BindView(R.id.text_view_monitor_device_name)
    TextView tvMonitorDeviceName;//监测仪器名称
    @BindView(R.id.edit_calibration_method)
    EditText edCalibrationMethod;//校准方法
    @BindView(R.id.text_view_calibration_number)
    TextView tvCalibrationNumber;//校准仪器名称
    @BindView(R.id.edit_remarks)
    EditText edeRemarks;//备注


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_basic, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!CheckUtil.isNull(mSample)) {
            tvSampleNo.setText(mSample.getSamplingNo());
            tvSampleName.setText(mSample.getProjectName());
            tvNature.setText(mProject.getMonType());
            tvDate.setText(mSample.getSamplingTimeBegin());
            edFactoryName.setText(mPrivateData.getClientName());
            edFactoryAddress.setText(mPrivateData.getClientAddr());
            edFactoryInfo.setText(mPrivateData.getProductionCondition());
            tvWeatherState.setText(mSample.getWeather());
            edWingSpeed.setText(mSample.getWindSpeed());
            edCalibrationFront.setText(mPrivateData.getCalibrationBefore());
            edCalibrationAfter.setText(mPrivateData.getCalibrationAfter());
            tvDeviceNumber.setText(mPrivateData.getWindDevName());
            tvMonitorForm.setText(mSample.getMethodName());
            tvMonitorDeviceName.setText(mSample.getDeviceName());
            edCalibrationMethod.setText(mPrivateData.getCalibrationMethodName());
            tvCalibrationNumber.setText(mPrivateData.getCalibrationDeviceName());
            edeRemarks.setText(mSample.getComment());
        }
    }

    /**
     * 保存信息
     *
     * @return
     */
    public void saveSampling() {
        mSample.setWindSpeed(edWingSpeed.getText().toString());
        if (mPrivateData != null && mSample != null) {
            mPrivateData.setClientName(tvFactoryName.getText().toString());
            mPrivateData.setClientAddr(tvFactoryAddress.getText().toString());
            mPrivateData.setProductionCondition(edFactoryInfo.getText().toString());
            mPrivateData.setCalibrationBefore(edCalibrationFront.getText().toString());
            mPrivateData.setCalibrationAfter(edCalibrationAfter.getText().toString());
            mPrivateData.setWindDevName(tvDeviceNumber.getText().toString());
            mPrivateData.setCalibrationMethodName(edCalibrationMethod.getText().toString());
            mPrivateData.setCalibrationDeviceName(tvCalibrationNumber.getText().toString());

            Gson gson = new Gson();
            String jsonStr = gson.toJson(mPrivateData);
            mSample.setPrivateData(jsonStr);
        }
    }

    @OnClick({R.id.text_view_date, R.id.text_weather_state, R.id.text_view_device_number,
            R.id.text_view_monitor_from, R.id.text_view_monitor_device_name,
            R.id.text_view_calibration_number})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_view_date://日期
                showDateSelectDialog(tvDate);
                break;
            case R.id.text_weather_state://天气
                showWeatherChoose();
                break;
            case R.id.text_view_device_number://风速仪型号及编号
                getDevice(tvDeviceNumber, 1);
                break;
            case R.id.text_view_monitor_from://监测方法及来源
                showSamplingMethods(tvMonitorForm);
                break;
            case R.id.text_view_monitor_device_name://监测仪器名称,型号及编号
                getDevice(tvMonitorDeviceName, 2);
                break;
            case R.id.text_view_calibration_number://校准仪器名称、型号及编号(溯源信息)
                getDevice(tvCalibrationNumber, 3);
                break;
        }
    }

    /**
     * 获取设备名称及编号
     *
     * @param tvDeviceNumber 显示的tv
     * @param type           {@int1风速仪型号及编号
     * @int2监测仪器名称、型号及编号(溯源信息)
     * @int3校准仪器名称、型号及编号(溯源信息) }
     */
    private void getDevice(TextView tvDeviceNumber, int type) {
        Intent intent = new Intent();
        intent.setClass(getContext(), DeviceActivity.class);
        intent.putExtra("methodId", mSample.getMethodId() == null ? "" : mSample.getMethodId());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    String deviceId = data.getStringExtra("DeviceId");
                    String deviceName = data.getStringExtra("DeviceName");
                    String deviceCode = data.getStringExtra("DeviceCode");
                    String sourceWay = data.getStringExtra("SourceWay");
                    String expireDate = data.getStringExtra("ExpireDate");
                    String deviceText = String.format("%s(%s)(%s %s)", deviceName, deviceCode, sourceWay, expireDate);

                    switch (type) {
                        case 1:
                            mSample.setPrivateDataStringValue("WindDevName", deviceName);
                            break;
                        case 2:
                            mSample.setDeviceId(deviceId);
                            mSample.setDeviceName(deviceName);
                            break;
                        case 3:
                            mSample.setPrivateDataStringValue("CalibrationDeviceName", deviceName);
                            mSample.setPrivateDataStringValue("CalibrationDeviceId", deviceId);
                            break;
                    }

                    tvDeviceNumber.setText(deviceText);
                }
            }
        });
    }

    /**
     * 选择采样方法
     */
    private void showSamplingMethods(TextView tv) {
        if (CheckUtil.isEmpty(mSample.getFormType())) {
            ArtUtils.makeText(getContext(), "请先设置表单类型");
            return;
        }
        Intent intent = new Intent(getContext(), MethodActivity.class);
        intent.putExtra("tagId", mSample.getParentTagId());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSample.setMethodName(data.getStringExtra("MethodName"));
                    mSample.setMethodId(data.getStringExtra("MethodId"));
                    tv.setText(mSample.getMethodName());
                }
            }
        });
    }

    /**
     * 时间选择器(监测日期——年月日)
     * data picker
     */
    private void showDateSelectDialog(TextView dateTextView) {
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                mSample.setSamplingTimeBegin(DateUtils.getDate(date));
                dateTextView.setText(DateUtils.getDate(date));
            }
        }).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    /**
     * 选择天气
     */
    private void showWeatherChoose() {
        Intent intent = new Intent(getContext(), WeatherActivity.class);
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSample.setWeather(data.getStringExtra("weather"));
                    tvWeatherState.setText(mSample.getWeather());
                }
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

}
