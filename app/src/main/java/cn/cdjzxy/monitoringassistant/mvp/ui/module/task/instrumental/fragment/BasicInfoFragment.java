package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.MethodActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.UserActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.WasteWaterMoniteActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

public class BasicInfoFragment  extends BaseFragment {

    @BindView(R.id.tv_test_user)
    TextView tvTestUser;

    @BindView(R.id.tv_choose_project)
    TextView tvChooseProject;

    @BindView(R.id.tv_test_start_date)
    TextView       tvTestStartDate;

    @BindView(R.id.tv_test_end_date)
    TextView       tvTestEndDate;

    @BindView(R.id.tv_test_method)
    TextView       tvTestMethod;

    @BindView(R.id.tv_test_device)
    TextView tvTestDevice;

    @BindView(R.id.tv_comment)
    EditText       tvComment;

    Unbinder unbinder;

    public BasicInfoFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instrumental_basic_info, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!CheckUtil.isNull(InstrumentalActivity.mSampling)) {
            tvTestUser.setText(InstrumentalActivity.mSampling.getSamplingUserName());
            tvChooseProject.setText(InstrumentalActivity.mSampling.getMonitemName());
            tvTestStartDate.setText(InstrumentalActivity.mSampling.getSamplingTimeBegin());
            tvTestEndDate.setText(InstrumentalActivity.mSampling.getSamplingTimeEnd());
            tvTestMethod.setText(InstrumentalActivity.mSampling.getMethodName());
            tvTestDevice.setText(InstrumentalActivity.mSampling.getDeviceName());
            tvComment.setText(InstrumentalActivity.mSampling.getComment());

            tvTestUser.setEnabled(InstrumentalActivity.mSampling.getIsCanEdit());
            tvChooseProject.setEnabled(InstrumentalActivity.mSampling.getIsCanEdit());
            tvTestStartDate.setEnabled(InstrumentalActivity.mSampling.getIsCanEdit());
            tvTestEndDate.setEnabled(InstrumentalActivity.mSampling.getIsCanEdit());
            tvTestMethod.setEnabled(InstrumentalActivity.mSampling.getIsCanEdit());
            tvTestDevice.setEnabled(InstrumentalActivity.mSampling.getIsCanEdit());
            tvComment.setEnabled(InstrumentalActivity.mSampling.getIsCanEdit());
        }

        tvComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                InstrumentalActivity.mSampling.setComment(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.tv_test_user,R.id.tv_choose_project,R.id.tv_test_start_date,R.id.tv_test_end_date,R.id.tv_test_method, R.id.tv_test_device})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_test_user:
                Intent intent1 = new Intent(getContext(), UserActivity.class);
                intent1.putExtra("projectId", InstrumentalActivity.mSampling.getProjectId());
                new AvoidOnResult(getActivity()).startForResult(intent1, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (!CheckUtil.isEmpty(data.getStringExtra("UserId")) && !CheckUtil.isEmpty(data.getStringExtra("UserName"))) {
                                InstrumentalActivity.mSampling.setSamplingUserId(data.getStringExtra("UserId"));
                                InstrumentalActivity.mSampling.setSamplingUserName(data.getStringExtra("UserName"));

                                InstrumentalActivity.mSampling.setMonitorPerson(InstrumentalActivity.mSampling.getSamplingUserName());

                                tvTestUser.setText(InstrumentalActivity.mSampling.getSamplingUserName());
                            }
                        }
                    }
                });
                break;

            case R.id.tv_choose_project:
                intent1 = new Intent(getContext(), WasteWaterMoniteActivity.class);
                intent1.putExtra("projectId", InstrumentalActivity.mSampling.getProjectId());
                new AvoidOnResult(getActivity()).startForResult(intent1, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (!CheckUtil.isEmpty(data.getStringExtra("MonitemId")) && !CheckUtil.isEmpty(data.getStringExtra("MonitemName"))) {
                                InstrumentalActivity.mSampling.setMonitemId(data.getStringExtra("MonitemId"));
                                InstrumentalActivity.mSampling.setMonitemName(data.getStringExtra("MonitemName"));
                                InstrumentalActivity.mSampling.setAddressId(data.getStringExtra("AddressId"));
                                InstrumentalActivity.mSampling.setAddressName(data.getStringExtra("AddressName"));
                                tvChooseProject.setText(InstrumentalActivity.mSampling.getMonitemName());
                            }
                        }
                    }
                });
                break;

            case R.id.tv_test_start_date:
                showDateSelectDialog(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = DateUtils.getDate(date);
                        tvTestStartDate.setText(time);
                        InstrumentalActivity.mSampling.setSamplingTimeBegin(time);
                    }
                });
                break;

            case R.id.tv_test_end_date:
                showDateSelectDialog(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = DateUtils.getDate(date);
                        tvTestEndDate.setText(time);
                        InstrumentalActivity.mSampling.setSamplingTimeEnd(time);
                    }
                });
                break;

            case R.id.tv_test_method:
                if (CheckUtil.isEmpty(InstrumentalActivity.mSampling.getParentTagId())) {
                    ArtUtils.makeText(getContext(), "需要父要素ID");
                    return;
                }
                Intent intent3 = new Intent(getContext(), MethodActivity.class);
                intent3.putExtra("tagId", InstrumentalActivity.mSampling.getParentTagId());
                new AvoidOnResult(getActivity()).startForResult(intent3, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            InstrumentalActivity.mSampling.setMethodName(data.getStringExtra("MethodName"));
                            InstrumentalActivity.mSampling.setMethodId(data.getStringExtra("MethodId"));
                            tvTestMethod.setText(InstrumentalActivity.mSampling.getMethodName());
                        }
                    }
                });
                break;

            case R.id.tv_test_device:
                if (CheckUtil.isEmpty(InstrumentalActivity.mSampling.getMethodId())) {
                    ArtUtils.makeText(getContext(), "请先选择方法");
                    return;
                }
                Intent intent4 = new Intent(getContext(), DeviceActivity.class);
                intent4.putExtra("methodId", InstrumentalActivity.mSampling.getMethodId());
                new AvoidOnResult(getActivity()).startForResult(intent4, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            InstrumentalActivity.mSampling.setDeviceName(data.getStringExtra("DeviceName"));
                            InstrumentalActivity.mSampling.setDeviceId(data.getStringExtra("DeviceId"));
                            tvTestDevice.setText(InstrumentalActivity.mSampling.getDeviceName());
                        }
                    }
                });
                break;
        }
    }

    private void showDateSelectDialog(OnTimeSelectListener listener) {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(getContext(), listener).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }
}