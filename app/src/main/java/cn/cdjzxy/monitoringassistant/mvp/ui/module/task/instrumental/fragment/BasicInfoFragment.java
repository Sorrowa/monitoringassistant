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

import org.simple.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.MethodActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.MonItemMethodActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.UserActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.WasteWaterMoniteActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity.mSampling;

public class BasicInfoFragment extends BaseFragment {

    @BindView(R.id.my_layout_project)
    MyDrawableLinearLayout tvChooseProject;

    @BindView(R.id.my_layout_sampling_no)
    MyDrawableLinearLayout tvSamplingNo;

    @BindView(R.id.my_layout_project_name)
    MyDrawableLinearLayout tvProjectName;

    @BindView(R.id.my_layout_monitem_name)
    MyDrawableLinearLayout tvMonitemName;

    @BindView(R.id.my_layout_sampling_property)
    MyDrawableLinearLayout tvSamplingProperty;

    @BindView(R.id.my_layout_test_user)
    MyDrawableLinearLayout tvTestUser;

    @BindView(R.id.my_layout_start_date)
    MyDrawableLinearLayout tvTestStartDate;

    @BindView(R.id.my_layout_end_date)
    MyDrawableLinearLayout tvTestEndDate;

    @BindView(R.id.my_layout_method)
    MyDrawableLinearLayout tvTestMethod;

    @BindView(R.id.my_layout_device)
    MyDrawableLinearLayout tvTestDevice;

    @BindView(R.id.tv_comment)
    EditText tvComment;

    Unbinder unbinder;

    public BasicInfoFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instrumental_basic_info, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!CheckUtil.isNull(mSampling)) {
//            tvChooseProject.setText(InstrumentalActivity.mSampling.getMonitemName());
            tvSamplingNo.setRightTextStr(mSampling.getSamplingNo());
            tvProjectName.setRightTextStr(mSampling.getProjectName());
            tvMonitemName.setRightTextStr(mSampling.getMonitemName());
//            tvSamplingProperty.setText(InstrumentalActivity.mSampling.getTagName());
            tvSamplingProperty.setRightTextStr(mSampling.getFormTypeName());
            tvTestUser.setRightTextStr(mSampling.getSamplingUserName());
            tvTestStartDate.setRightTextStr(DateUtils.strGetDate(mSampling.getSamplingTimeBegin()));
            tvTestEndDate.setRightTextStr(DateUtils.strGetDate(mSampling.getSamplingTimeEnd()));
            tvTestMethod.setRightTextStr(mSampling.getMethodName());
            tvTestDevice.setRightTextStr(mSampling.getPrivateDataStringValue("DeviceText"));
            tvComment.setText(mSampling.getComment());

            tvChooseProject.setEnabled(mSampling.getIsCanEdit());
            tvTestUser.setEnabled(mSampling.getIsCanEdit());
            tvTestStartDate.setEnabled(mSampling.getIsCanEdit());
            tvTestEndDate.setEnabled(mSampling.getIsCanEdit());
            tvTestMethod.setEnabled(mSampling.getIsCanEdit());
            tvTestDevice.setEnabled(mSampling.getIsCanEdit());
            tvComment.setEnabled(mSampling.getIsCanEdit());
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
                mSampling.setComment(CheckUtil.isEmpty(s.toString()) ? "" : s.toString());
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

    @OnClick({R.id.my_layout_test_user, R.id.my_layout_project, R.id.my_layout_start_date,
            R.id.my_layout_end_date, R.id.my_layout_method, R.id.my_layout_device})
    public void onClick(View view) {
        hideSoftInput();
        if (!InstrumentalActivity.isNewCreate &&
                !UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
            showNoPermissionDialog("才能进行表单编辑。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
            return;
        }
        switch (view.getId()) {
            case R.id.my_layout_test_user:
                Intent intent1 = new Intent(getContext(), UserActivity.class);
                intent1.putExtra("projectId", mSampling.getProjectId());
                intent1.putExtra("selectUserIds", mSampling.getSamplingUserId());
                new AvoidOnResult(getActivity()).startForResult(intent1, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (!CheckUtil.isEmpty(data.getStringExtra("UserId")) && !CheckUtil.isEmpty(data.getStringExtra("UserName"))) {
                                mSampling.setSamplingUserId(data.getStringExtra("UserId"));
                                mSampling.setSamplingUserName(data.getStringExtra("UserName"));

                                mSampling.setMonitorPerson(mSampling.getSamplingUserName());

                                tvTestUser.setRightTextStr(mSampling.getSamplingUserName());
                            }
                        }
                    }
                });
                break;

            case R.id.my_layout_project:
                intent1 = new Intent(getContext(), WasteWaterMoniteActivity.class);
                intent1.putExtra("projectId", mSampling.getProjectId());
                new AvoidOnResult(getActivity()).startForResult(intent1, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode != Activity.RESULT_OK) {
                            return;
                        }

                        if (CheckUtil.isEmpty(data.getStringExtra("MonitemId")) || CheckUtil.isEmpty(data.getStringExtra("MonitemName"))) {
                            return;
                        }

                        String monitemId = data.getStringExtra("MonitemId");
                        String monitemName = data.getStringExtra("MonitemName");
                        tvChooseProject.setRightTextStr(monitemName);
                        if (monitemId.equals(mSampling.getMonitemId()) && monitemName.equals(mSampling.getMonitemName())) {
                            return;//跟之前选择的一样
                        }

                        mSampling.setMonitemId(monitemId);
                        mSampling.setMonitemName(monitemName);

                        //改为在添加样品时记录点位信息
//                        InstrumentalActivity.mSampling.setAddressId(data.getStringExtra("AddressId"));
//                        InstrumentalActivity.mSampling.setAddressName(data.getStringExtra("AddressName"));

                        mSampling.setTagId(data.getStringExtra("TagId"));
                        mSampling.setTagName(data.getStringExtra("TagName"));
                        mSampling.setFormType(data.getStringExtra("TagId"));
                        mSampling.setFormTypeName(data.getStringExtra("TagName"));

                        mSampling.setPrivateDataStringValue("FormTypeName", mSampling.getFormTypeName());

//                        InstrumentalActivity.mSampling.setAllMonitemId(data.getStringExtra("AllMonitemId"));
//                        InstrumentalActivity.mSampling.setAllMonitemName(data.getStringExtra("AllMonitemName"));

//                        tvChooseProject.setText(InstrumentalActivity.mSampling.getMonitemName());
//                        tvSamplingProperty.setText(InstrumentalActivity.mSampling.getTagName());
                        tvSamplingProperty.setRightTextStr(mSampling.getFormTypeName());
                        tvMonitemName.setRightTextStr(mSampling.getMonitemName());

                        //重置监测方法
                        mSampling.setMethodId("");
                        mSampling.setMethodName("");
                        tvTestMethod.setRightTextStr(mSampling.getMethodName());

                        //重置监测仪器
                        mSampling.setDeviceName("");
                        mSampling.setDeviceId("");
                        tvTestDevice.setRightTextStr(mSampling.getDeviceName());

                        //重置检测结果,先清理数据库中的数据
                        List<SamplingDetail> samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(mSampling.getId())).list();

                        //遍历数据删除
                        for (SamplingDetail detail : samplingDetails) {
                            if (CheckUtil.isNull(detail)) {
                                continue;
                            }

                            //从数据库中删除
                            DBHelper.get().getSamplingDetailDao().delete(detail);
                        }

                        //清理内存中的数据
                        mSampling.getSamplingDetailYQFs().clear();
                    }
                });
                break;

            case R.id.my_layout_start_date:
                showDateSelectDialog(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = DateUtils.getDate(date);
                        tvTestStartDate.setRightTextStr(time);
                        mSampling.setSamplingTimeBegin(time);
                        mSampling.setSamplingNo(SamplingUtil.createSamplingNo(time));
                        tvSamplingNo.setRightTextStr(mSampling.getSamplingNo());
                    }
                });
                break;

            case R.id.my_layout_end_date:
                showDateSelectDialog(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        String time = DateUtils.getDate(date);
                        tvTestEndDate.setRightTextStr(time);
                        mSampling.setSamplingTimeEnd(time);
                    }
                });
                break;

            case R.id.my_layout_method:
                if (CheckUtil.isEmpty(mSampling.getMonitemId())) {
                    ArtUtils.makeText(getContext(), "请选择项目！");
                    return;
                }

                Intent intent3 = new Intent(getContext(), MonItemMethodActivity.class);
                intent3.putExtra("MonItemId", mSampling.getMonitemId());
                new AvoidOnResult(getActivity()).startForResult(intent3, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            mSampling.setMethodId(data.getStringExtra("MethodId"));
                            mSampling.setMethodName(data.getStringExtra("MethodName"));
                            tvTestMethod.setRightTextStr(mSampling.getMethodName());

                            //重置监测仪器
                            mSampling.setDeviceName("");
                            mSampling.setDeviceId("");
                            tvTestDevice.setRightTextStr(mSampling.getDeviceName());
                        }
                    }
                });
                break;

            case R.id.my_layout_device:
                if (CheckUtil.isEmpty(mSampling.getMethodId())) {
                    ArtUtils.makeText(getContext(), "请先选择方法");
                    return;
                }
                Intent intent4 = new Intent(getContext(), DeviceActivity.class);
                intent4.putExtra("methodId", mSampling.getMethodId());
                new AvoidOnResult(getActivity()).startForResult(intent4, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            String deviceId = data.getStringExtra("DeviceId");
                            String deviceName = data.getStringExtra("DeviceName");
                            String deviceCode = data.getStringExtra("DeviceCode");
                            String sourceWay = data.getStringExtra("SourceWay");
                            String expireDate = data.getStringExtra("ExpireDate");
                            String deviceText;
                            //修改之后要显示到时分秒
                            if (expireDate != null && !expireDate.equals("")) {
                                String[] s = expireDate.split(" ");
//                                deviceText = String.format("%s(%s)(%s %s)", deviceName, deviceCode, sourceWay, s[0]);
                                deviceText = String.format("%s(%s)(%s %s)", deviceName, deviceCode, sourceWay, expireDate);
                            } else {
                                deviceText = String.format("%s(%s)(%s %s)", deviceName, deviceCode, sourceWay, expireDate);
                            }


                            mSampling.setDeviceId(deviceId);
                            mSampling.setDeviceName(deviceText);
                            mSampling.setPrivateDataStringValue("SourceWay", sourceWay);
                            mSampling.setPrivateDataStringValue("SourceDate", expireDate);
                            //设备信息格式：仪器名称(仪器编号)(仪器溯源方式 仪器溯源有效期)
                            mSampling.setPrivateDataStringValue("DeviceText", deviceText);

                            tvTestDevice.setRightTextStr(deviceText);
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