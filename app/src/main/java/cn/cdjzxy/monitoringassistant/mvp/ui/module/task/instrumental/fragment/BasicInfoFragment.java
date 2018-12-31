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
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SamplingFileAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.Glide4Engine;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.MethodActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TypeActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.UserActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointSelectActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
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
        return inflater.inflate(R.layout.fragment_preciptation_basic_info, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                PrecipitationActivity.mSampling.setComment(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
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
//                Intent intent1 = new Intent(getContext(), UserActivity.class);
//                intent1.putExtra("projectId", PrecipitationActivity.mSampling.getProjectId());
//                new AvoidOnResult(getActivity()).startForResult(intent1, new AvoidOnResult.Callback() {
//                    @Override
//                    public void onActivityResult(int resultCode, Intent data) {
//                        if (resultCode == Activity.RESULT_OK) {
//                            if (!CheckUtil.isEmpty(data.getStringExtra("UserId")) && !CheckUtil.isEmpty(data.getStringExtra("UserName"))) {
//                                PrecipitationActivity.mSampling.setSamplingUserId(data.getStringExtra("UserId"));
//                                PrecipitationActivity.mSampling.setSamplingUserName(data.getStringExtra("UserName"));
//                                tvTestUser.setText(PrecipitationActivity.mSampling.getSamplingUserName());
//                            }
//                        }
//                    }
//                });
                break;

            case R.id.tv_choose_project:
//                Intent intent1 = new Intent(getContext(), UserActivity.class);
//                intent1.putExtra("projectId", PrecipitationActivity.mSampling.getProjectId());
//                new AvoidOnResult(getActivity()).startForResult(intent1, new AvoidOnResult.Callback() {
//                    @Override
//                    public void onActivityResult(int resultCode, Intent data) {
//                        if (resultCode == Activity.RESULT_OK) {
//                            if (!CheckUtil.isEmpty(data.getStringExtra("UserId")) && !CheckUtil.isEmpty(data.getStringExtra("UserName"))) {
//                                PrecipitationActivity.mSampling.setSamplingUserId(data.getStringExtra("UserId"));
//                                PrecipitationActivity.mSampling.setSamplingUserName(data.getStringExtra("UserName"));
//                                tvTestUser.setText(PrecipitationActivity.mSampling.getSamplingUserName());
//                            }
//                        }
//                    }
//                });
                break;

            case R.id.tv_test_start_date:
                showDateSelectDialog(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = DateUtils.getDate(date);
                        tvTestStartDate.setText(time);
                    }
                });
                break;

            case R.id.tv_test_end_date:
                showDateSelectDialog(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = DateUtils.getDate(date);
                        tvTestEndDate.setText(time);
                    }
                });
                break;

            case R.id.tv_test_method:
//                if (CheckUtil.isEmpty(PrecipitationActivity.mSampling.getParentTagId())) {
//                    ArtUtils.makeText(getContext(), "请先选择降水类型");
//                    return;
//                }
//                Intent intent3 = new Intent(getContext(), MethodActivity.class);
//                intent3.putExtra("tagId", PrecipitationActivity.mSampling.getParentTagId());
//                new AvoidOnResult(getActivity()).startForResult(intent3, new AvoidOnResult.Callback() {
//                    @Override
//                    public void onActivityResult(int resultCode, Intent data) {
//                        if (resultCode == Activity.RESULT_OK) {
//                            PrecipitationActivity.mSampling.setMethodName(data.getStringExtra("MethodName"));
//                            PrecipitationActivity.mSampling.setMethodId(data.getStringExtra("MethodId"));
//                            tvSamplingMethod.setText(PrecipitationActivity.mSampling.getMethodName());
//                        }
//                    }
//                });
                break;

            case R.id.tv_test_device:
//                if (CheckUtil.isEmpty(PrecipitationActivity.mSampling.getMethodId())) {
//                    ArtUtils.makeText(getContext(), "请先选择方法");
//                    return;
//                }
//                Intent intent4 = new Intent(getContext(), DeviceActivity.class);
//                intent4.putExtra("methodId", PrecipitationActivity.mSampling.getMethodId());
//                new AvoidOnResult(getActivity()).startForResult(intent4, new AvoidOnResult.Callback() {
//                    @Override
//                    public void onActivityResult(int resultCode, Intent data) {
//                        if (resultCode == Activity.RESULT_OK) {
//                            PrecipitationActivity.mSampling.setDeviceName(data.getStringExtra("DeviceName"));
//                            PrecipitationActivity.mSampling.setDeviceId(data.getStringExtra("DeviceId"));
//                            tvTestDevice.setText(PrecipitationActivity.mSampling.getDeviceName());
//                        }
//                    }
//                });
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