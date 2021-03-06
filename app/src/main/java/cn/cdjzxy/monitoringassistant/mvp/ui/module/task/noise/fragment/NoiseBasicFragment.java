package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;

import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;

import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.MonItemMethodActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.WeatherActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;

import cn.cdjzxy.monitoringassistant.utils.DateUtils;

import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;


import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mProject;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;

public class NoiseBasicFragment extends BaseFragment implements IView {

    @BindView(R.id.my_layout_number)
    MyDrawableLinearLayout tvSampleNo;//采样点编号
    @BindView(R.id.my_layout_name)
    MyDrawableLinearLayout tvSampleName;//项目名称
    @BindView(R.id.my_layout_nature)
    MyDrawableLinearLayout tvNature;//监测性质
    @BindView(R.id.my_layout_date)
    MyDrawableLinearLayout tvDate;//监测日期
    @BindView(R.id.my_layout_factory_name)
    MyDrawableLinearLayout edFactoryName;//企业名称
    //    @BindView(R.id.text_view_factory_name)
//    TextView tvFactoryName;//企业名称 用于展示（不可编辑）
    @BindView(R.id.my_layout_factory_address)
    MyDrawableLinearLayout edFactoryAddress;//企业地址
    //    @BindView(R.id.text_view_factory_address)
//    TextView tvFactoryAddress;//企业地址 用于展示（不可编辑）
    @BindView(R.id.my_layout_factory_info)
    MyDrawableLinearLayout edFactoryInfo;//企业生产工况
    @BindView(R.id.my_layout_weather_state)
    MyDrawableLinearLayout tvWeatherState;//天气情况
    @BindView(R.id.my_layout_wind_speed)
    MyDrawableLinearLayout edWingSpeed;//风速
    @BindView(R.id.my_layout_calibration_front)
    MyDrawableLinearLayout edCalibrationFront;//测前校准值
    @BindView(R.id.my_layout_calibration_after)
    MyDrawableLinearLayout edCalibrationAfter;//测后校准值
    @BindView(R.id.my_layout_device_number)
    MyDrawableLinearLayout tvDeviceNumber;//风速仪型号及编号
    @BindView(R.id.my_layout_monitor_from)
    MyDrawableLinearLayout tvMonitorForm;//监测方法及来源
    @BindView(R.id.my_layout_device_name)
    MyDrawableLinearLayout tvMonitorDeviceName;//监测仪器名称
    @BindView(R.id.my_layout_calibration_method)
    MyDrawableLinearLayout edCalibrationMethod;//校准方法
    @BindView(R.id.my_layout_calibration_number)
    MyDrawableLinearLayout tvCalibrationNumber;//校准仪器名称
    @BindView(R.id.edit_remarks)
    EditText edeRemarks;//备注
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_save)
    LinearLayout linearSave;


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_basic, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSample != null) {
            Gson gson = new Gson();
            mPrivateData = gson.fromJson(mSample.getPrivateData(), NoisePrivateData.class);
            if (mPrivateData == null) {
                mPrivateData = new NoisePrivateData();
            }
            setViewData();
        }

    }

    private void setViewData() {
        tvSampleNo.setRightTextStr(mSample.getSamplingNo());
        tvSampleName.setRightTextStr(mSample.getProjectName());
        tvNature.setRightTextStr(mProject.getType());
        tvDate.setRightTextStr(DateUtils.strGetDate(mSample.getSamplingTimeBegin()));
        tvWeatherState.setRightTextStr(mSample.getWeather());
        edWingSpeed.setEditTextStr(mSample.getWindSpeed());
        tvMonitorForm.setRightTextStr(mSample.getMethodName());
        StringBuffer deviceName = new StringBuffer();
        if (mSample.getDeviceName() != null) {
            if (mPrivateData == null) {
                deviceName.append(mSample.getDeviceName());
            } else if (mPrivateData.getSourceWay() != null) {
                deviceName.append(mSample.getDeviceName()).append("(").append(mPrivateData.getSourceWay());
                if (mPrivateData.getSourceDate() != null) {
                    deviceName.append(mPrivateData.getSourceDate());
                }
                deviceName.append(")");
            }
        }
        tvMonitorDeviceName.setRightTextStr(deviceName.toString());
        edeRemarks.setText(mSample.getComment());
        if (mPrivateData != null && mPrivateData.getClientName() != null) {
            edFactoryName.setEditTextStr(mPrivateData.getClientName());
        } else {
            edFactoryName.setEditTextStr(mProject.getRcvName());
            // edFactoryAddress.setText(mProject.getc());
        }
        edFactoryAddress.setEditTextStr(mPrivateData.getClientAddr());
        edFactoryInfo.setEditTextStr(mPrivateData.getProductionCondition());

        edCalibrationFront.setEditTextStr(mPrivateData.getCalibrationBefore());
        edCalibrationAfter.setEditTextStr(mPrivateData.getCalibrationAfter());
        tvDeviceNumber.setRightTextStr(mPrivateData.getWindDevName());

        edCalibrationMethod.setEditTextStr(mPrivateData.getCalibrationMethodName());
        tvCalibrationNumber.setRightTextStr(mPrivateData.getCalibrationDeviceName());
    }


    @OnClick({R.id.my_layout_date, R.id.my_layout_weather_state, R.id.my_layout_device_number,
            R.id.my_layout_monitor_from, R.id.my_layout_device_name,
            R.id.my_layout_calibration_number, R.id.linear_delete, R.id.linear_save
    })
    public void onClick(View v) {
        hideSoftInput();
        if (!mSample.getIsCanEdit()) {
            showMessage("提示：当前采样单，不支持编辑");
            return;
        }
        switch (v.getId()) {
            case R.id.my_layout_date://日期
                showDateSelectDialog(tvDate.getRightTextView());
                break;
            case R.id.my_layout_weather_state://天气
                showWeatherChoose();
                break;
            case R.id.my_layout_device_number://风速仪型号及编号
                getDevice(tvDeviceNumber, 1);
                break;
            case R.id.my_layout_monitor_from://监测方法及来源
                showSamplingMethods(tvMonitorForm);
                break;
            case R.id.my_layout_device_name://监测仪器名称,型号及编号
                getDevice(tvMonitorDeviceName, 2);
                break;
            case R.id.my_layout_calibration_number://校准仪器名称、型号及编号(溯源信息)
                getDevice(tvCalibrationNumber, 3);
                break;
            case R.id.linear_delete:
            case R.id.linear_save:
                break;
        }
    }

    /**
     * 获取设备名称及编号
     *
     * @param myDrawableLinearLayout 显示的tv
     * @param type                   {@int1风速仪型号及编号
     * @int2监测仪器名称、型号及编号(溯源信息)
     * @int3校准仪器名称、型号及编号(溯源信息) }
     */
    private void getDevice(MyDrawableLinearLayout myDrawableLinearLayout, int type) {
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
                    String specification = data.getStringExtra("Specification");
                    //修改之后要显示到时分秒
                    String deviceText;
                    //修改之后要显示到时分秒
                    if (expireDate != null && !expireDate.equals("")) {
                        String[] s = expireDate.split(" ");
                        deviceText = String.format("%s%s(%s)(%s %s)", deviceName, specification, deviceCode, sourceWay, s[0]);
                        if (type==2){
                            mSample.setPrivateDataStringValue("SourceDate", s[0]);
                        }
                    } else {
                        deviceText = String.format("%s%s(%s)(%s %s)", deviceName, specification, deviceCode, sourceWay, expireDate == null ? "" : expireDate);
                        if (type==2){
                            mSample.setPrivateDataStringValue("SourceDate", expireDate);
                        }
                    }
                    switch (type) {
                        case 1:
                            mSample.setPrivateDataStringValue("WindDevName", String.format("%s%s", deviceName, specification));
                            break;
                        case 2:
                            mSample.setDeviceId(deviceId);
                            mSample.setDeviceName(String.format("%s%s(%s)", deviceName, specification, deviceCode));
                            mSample.setPrivateDataStringValue("SourceWay", sourceWay);
                            break;
                        case 3:
                            mSample.setPrivateDataStringValue("CalibrationDeviceName", deviceText);
                            mSample.setPrivateDataStringValue("CalibrationDeviceId", deviceId);
                            break;
                    }


                }
            }
        });
    }

    /**
     * 选择采样方法
     */
    private void showSamplingMethods(MyDrawableLinearLayout myDrawableLinearLayout) {

        Intent intent = new Intent(getContext(), MonItemMethodActivity.class);
        intent.putExtra("MonItemId", mSample.getMonitemId());
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSample.setMethodName(data.getStringExtra("MethodName"));
                    mSample.setMethodId(data.getStringExtra("MethodId"));
                    myDrawableLinearLayout.setRightTextStr(mSample.getMethodName());
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
                String strDate = DateUtils.getDate(date);
                mSample.setSamplingTimeBegin(strDate);
                dateTextView.setText(strDate);
                mSample.setSamplingNo(SamplingUtil.createSamplingNo(strDate));
                tvSampleNo.setRightTextStr(mSample.getSamplingNo());
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
                    tvWeatherState.setRightTextStr(mSample.getWeather());
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

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(getContext(), message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    @Override
    public void onStop() {
        super.onStop();
        if (!mSample.getIsCanEdit()) return;
        if (mPrivateData != null && mSample != null) {
            saveData();
            Log.e(TAG, "onStop: " + "保存临时数据");
        }
    }

    private void saveData() {
        mSample.setWindSpeed(edWingSpeed.getEditTextStr());
        mSample.setComment(edeRemarks.getText().toString());
        mPrivateData.setClientName(edFactoryName.getEditTextStr());
        mPrivateData.setClientAddr(edFactoryAddress.getEditTextStr());
        mPrivateData.setProductionCondition(edFactoryInfo.getEditTextStr());
        mPrivateData.setCalibrationBefore(edCalibrationFront.getEditTextStr());
        mPrivateData.setCalibrationAfter(edCalibrationAfter.getEditTextStr());
        mPrivateData.setWindDevName(tvDeviceNumber.getRightTextViewStr());
        mPrivateData.setCalibrationMethodName(edCalibrationMethod.getEditTextStr());
        mPrivateData.setCalibrationDeviceName(tvCalibrationNumber.getRightTextViewStr());
        String jsonStr = new Gson().toJson(mPrivateData);
        mSample.setPrivateData(jsonStr);
        mProject.setClientName(edFactoryName.getEditTextStr());
    }
}
