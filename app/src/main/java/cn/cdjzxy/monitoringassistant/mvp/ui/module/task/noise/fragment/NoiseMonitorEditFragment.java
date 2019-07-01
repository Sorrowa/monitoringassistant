package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.navisdk.ui.routeguide.mapmode.subview.G;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseMonitorPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoisePointSelectActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_INT_MONITOR;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_MONITOR_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.isNeedSave;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.saveMySample;


/**
 * 噪声监测数据——详情或者编辑页面
 */
public class NoiseMonitorEditFragment extends BaseFragment implements IView {
    @BindView(R.id.btn_back)
    LinearLayout linearBack;
    @BindView(R.id.my_layout_monitor_address)
    MyDrawableLinearLayout tvAddress;//测点位置
    @BindView(R.id.btn_is_open)
    ImageButton btnIsOpen;
    @BindView(R.id.relate_layout)
    RelativeLayout relativeLayout;
    @BindView(R.id.linear_open)
    LinearLayout linearOpen;
    @BindView(R.id.my_layout_monitor_date)
    MyDrawableLinearLayout edMonitorDate;//监测时段
    @BindView(R.id.my_layout_monitor_time_first_start)
    MyDrawableLinearLayout tvMonitorTimeFirstStart;//第一个监测时间
    @BindView(R.id.my_layout_monitor_time_first_end)
    MyDrawableLinearLayout tvMonitorTimeFirstEnd;
    @BindView(R.id.my_layout_monitor_data)
    MyDrawableLinearLayout edMonitorData;//测量值
    @BindView(R.id.my_layout_monitor_time_start)
    MyDrawableLinearLayout tvMonitorTimeStart;//监测时间
    @BindView(R.id.my_layout_monitor_time_end)
    MyDrawableLinearLayout tvMonitorTimeEnd;
    @BindView(R.id.my_layout_bg_data)
    MyDrawableLinearLayout edMonitorBgData;//背景值
    @BindView(R.id.my_layout_monitor_edit_data)
    MyDrawableLinearLayout edMonitorEditData;//修正值
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_save)
    LinearLayout linearSave;
    private NoiseMonitorPrivateData privateData;
    private OptionsPickerView pickerView;
    private int position;


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_monitor_edit, null);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setViewData();
        }
    }

    private void setViewData() {
        position = getActivity().getSharedPreferences(NOISE_FRAGMENT_SHARE, 0) == null ? -1 :
                getActivity().getSharedPreferences(NOISE_FRAGMENT_SHARE, 0).getInt(NOISE_FRAGMENT_MONITOR_SHARE, -1);
        SamplingDetail detail;
        if (position != -1) {
            detail = mSample.getSamplingDetailResults().get(position);
            privateData = new Gson().fromJson(detail.getPrivateData(), NoiseMonitorPrivateData.class);
            privateData.setId(detail.getId());
            privateData.setChecked(detail.isSelected());
            privateData.setAddressName(detail.getAddressName());
            privateData.setValue(detail.getValue1());
            privateData.setAddressId(detail.getAddresssId());
        } else {
            privateData = new NoiseMonitorPrivateData();
            privateData.setChecked(true);
        }
        if (privateData == null) {
            privateData = new NoiseMonitorPrivateData();
            privateData.setChecked(true);
        }
        tvAddress.setRightTextStr(privateData.getAddressName());
        edMonitorDate.setEditTextStr(privateData.getTimeInterval());
        tvMonitorTimeFirstStart.setRightTextStr(privateData.getZTestTime());
        tvMonitorTimeFirstEnd.setEditTextStr(privateData.getZTestTime());
        edMonitorData.setEditTextStr(privateData.getValue());
        tvMonitorTimeStart.setRightTextStr(privateData.getYTestTime());
        tvMonitorTimeEnd.setRightTextStr(privateData.getYEndTestTime());
        edMonitorBgData.setEditTextStr(privateData.getYBackgroundValue());
        edMonitorEditData.setEditTextStr(privateData.getYCorrectedValue());
        linearOpen.setVisibility(privateData.isChecked() ? View.VISIBLE : View.GONE);
        setBtnIsOpenImg();
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    public void showLoading(String msg) {
        showLoadingDialog(msg);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
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

    @OnClick({R.id.btn_back, R.id.linear_delete, R.id.linear_save, R.id.relate_layout,
            R.id.my_layout_monitor_address, R.id.my_layout_monitor_time_first_start, R.id.my_layout_monitor_time_first_end,
            R.id.my_layout_monitor_time_start})
    public void onClick(View v) {
        hideSoftInput();
        if (v.getId() == R.id.btn_back) {
            EventBus.getDefault().post(NOISE_FRAGMENT_INT_MONITOR, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
            return;
        } else if (v.getId() == R.id.relate_layout) {
            linearOpen.setVisibility(linearOpen.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            setBtnIsOpenImg();
            return;
        } else if (!mSample.getIsCanEdit()) {
            showMessage("提示：当前采样单，不支持编辑");
        } else
            switch (v.getId()) {
                case R.id.linear_delete:
                    deleteData();
                    break;
                case R.id.linear_save:
                    saveData();
                    break;
                case R.id.my_layout_monitor_address:
                    choiceAddress();
                    break;
                case R.id.my_layout_monitor_time_first_start:
                    showDateSelectDialog(tvMonitorTimeFirstStart);
                    break;
//                case R.id.my_layout_monitor_time_first_end:
//                    showDateSelectDialog(tvMonitorTimeFirstEnd);
//                    break;
                case R.id.my_layout_monitor_time_start:
                    showDateSelectDialog(tvMonitorTimeStart);
                    break;
//                case R.id.my_layout_monitor_time_end:
//                    showDateSelectDialog(tvMonitorTimeEnd);
//                    break;
            }
    }

    /**
     * 选择点位
     */
    private void choiceAddress() {
        if (pickerView.isShowing()) {
            return;
        }
        List<String> stringList = new ArrayList<>();
        List<SamplingDetail> list = mSample.getSamplingDetailResults();
        if (mPrivateData != null && mPrivateData.getMianNioseAddr() != null) {
            for (NoisePrivateData.MianNioseAddrBean bean : mPrivateData.getMianNioseAddr()) {
                stringList.add(bean.getAddrCode());
            }
        }
        if (stringList.size() == 0) stringList.add("测试数据");
        pickerView = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                tvAddress.setRightTextStr(stringList.get(options1));
            }
        })
                .setTitleText("测点位置选择")
                .setContentTextSize(20)//设置滚轮文字大小
//                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setOutSideCancelable(true).build();
        pickerView.setPicker(stringList);
        pickerView.show();

    }

    /**
     * 时间选择器(监测日期——年月日)
     * data picker
     */
    private void showDateSelectDialog(MyDrawableLinearLayout dateTextView) {
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //mSample.setSamplingTimeBegin(DateUtils.getDate(date));
                dateTextView.setRightTextStr(DateUtils.getTimeNoMinute(date));
            }
        }).setType(new boolean[]{false, false, false, true, true, false}).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    private void saveData() {
        if (tvAddress.getRightTextViewStr() == null || tvAddress.getRightTextViewStr().equals("")) {
            showMessage("请填写测点位置");
            return;
        }
        isNeedSave = true;
        showLoading("正在保存");
        privateData.setTimeInterval(edMonitorDate.getEditTextStr());
        privateData.setZTestTime(tvMonitorTimeFirstStart.getRightTextViewStr());
        privateData.setZEndTestTime(tvMonitorTimeFirstEnd.getEditTextStr());
        privateData.setYTestTime(tvMonitorTimeStart.getRightTextViewStr());
        privateData.setYEndTestTime(tvMonitorTimeEnd.getEditTextStr());
        privateData.setYBackgroundValue(edMonitorBgData.getEditTextStr());
        privateData.setYCorrectedValue(edMonitorEditData.getEditTextStr());
        if (mSample != null) {
            SamplingDetail detail;
            if (position == -1) {
                detail = new SamplingDetail();
                detail.setAddresssId(privateData.getAddressId());
                detail.setAddressName(tvAddress.getRightTextViewStr());
                detail.setValue1(edMonitorData.getEditTextStr());
                detail.setId(UUID.randomUUID().toString());
                detail.setPrivateData(new Gson().toJson(privateData));
                detail.setDeviceId(mSample.getDeviceId());
                detail.setDeviceIdName(mSample.getDeviceName());
                detail.setMethodId(mSample.getMethodId());
                detail.setMonitemName(mSample.getMonitemName());
                mSample.getSamplingDetailResults().add(detail);
            } else {
                detail = mSample.getSamplingDetailResults().get(position);
                detail.setPrivateData(new Gson().toJson(privateData));
                mSample.getSamplingDetailResults().set(position, detail);
            }
        }
        hideLoading();
        EventBus.getDefault().post(NOISE_FRAGMENT_INT_MONITOR, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
        EventBus.getDefault().post("0", NOISE_FRAGMENT_MONITOR_SHARE);
    }

    public void setBtnIsOpenImg() {
        if (linearOpen.getVisibility() == View.VISIBLE) {
            btnIsOpen.setImageResource(R.mipmap.ic_has_open);
            privateData.setChecked(true);
        } else {
            btnIsOpen.setImageResource(R.mipmap.ic_has_next);
            privateData.setChecked(false);
        }
    }

    private void deleteData() {
        if (position == -1) {
            showMessage("您正在新建，无法删除！");
            return;
        }
        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("是否确定删除？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLoading("正在删除");
                        isNeedSave = true;
                        mSample.getSamplingDetailResults().remove(position);
                        hideLoading();
                        EventBus.getDefault().post("0", NOISE_FRAGMENT_MONITOR_SHARE);
                        EventBus.getDefault().post(NOISE_FRAGMENT_INT_MONITOR, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
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
