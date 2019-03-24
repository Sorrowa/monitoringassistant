package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
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
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import org.apache.poi.hssf.util.HSSFColor;
import org.simple.eventbus.EventBus;

import java.text.NumberFormat;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.UnitActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.NumberUtil;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class TestRecordDetailFragment extends BaseFragment {

    Unbinder unbinder;

    /**
     * 样品编码
     */
    @BindView(R.id.tv_sample_code)
    TextView tvSampleCode;

    /**
     * 频次
     */
    @BindView(R.id.tv_frequency)
    TextView tvFrequency;

    /**
     * 点位
     */
    @BindView(R.id.tv_point)
    TextView tvPoint;

    /**
     * 内控，平行/样品
     */
    @BindView(R.id.tv_control)
    TextView tvControl;

    /**
     * 检测日期
     */
    @BindView(R.id.tv_test_time)
    TextView tvTestTime;

    /**
     * 分析时间
     */
    @BindView(R.id.tv_analyse_time)
    TextView tvAnalyseTime;

    /**
     * 分析结果
     */
    @BindView(R.id.et_analyse_result)
    EditText etAnalyseResult;

    /**
     * 结果单位
     */
    @BindView(R.id.tv_test_unit)
    TextView tvTestUnit;

    /**
     * 删除按钮
     */
    @BindView(R.id.btn_delete)
    RelativeLayout btnDelete;

    /**
     * 保存按钮
     */
    @BindView(R.id.btn_save)
    RelativeLayout btnSave;

    /**
     * 实体
     */
    private Sampling mSampling;
    private boolean isStartTime;
    private int listPosition;
    private String unitId;


    public TestRecordDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instrumental_test_record_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

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

        mSampling = InstrumentalActivity.mSampling;

        if (isVisibleToUser) {
            tvAnalyseTime.setText("");
            tvTestUnit.setText("");
            etAnalyseResult.setText("");
            TestRecordDetailFragment.this.unitId = "";
            createSampleDetailNo();
        }
    }

    private void createSampleDetailNo() {
        List<SamplingDetail> samplingDetailResults = mSampling.getSamplingDetailYQFs();

        SharedPreferences collectListSettings = getActivity().getSharedPreferences("setting", 0);
        listPosition = collectListSettings.getInt("listPosition", -1);

        btnDelete.setVisibility(View.VISIBLE);

        SamplingDetail samplingDetail = samplingDetailResults.get(listPosition);
        tvSampleCode.setText(samplingDetail.getSampingCode());
        tvFrequency.setText(samplingDetail.getFrequecyNo() + "");
        tvPoint.setText(samplingDetail.getAddressName());
        tvTestTime.setText(samplingDetail.getSamplingOnTime());//检测日期
        tvControl.setText(samplingDetail.getSamplingType() == 1 ? "平行" : "");
        tvAnalyseTime.setText(samplingDetail.getPrivateDataStringValue("SamplingOnTime"));//分析实际
        etAnalyseResult.setText(samplingDetail.getPrivateDataStringValue("CaleValue"));//分析结果

        //记录结果单位ID
        TestRecordDetailFragment.this.unitId = samplingDetail.getPrivateDataStringValue("ValueUnit");
        if (samplingDetail.getPrivateDataStringValue("ValueUnitName") == null ||
                samplingDetail.getPrivateDataStringValue("ValueUnitName").equals("")) {
            List<Unit> units = DBHelper.get().getUnitDao().loadAll();
            if (units != null && units.size() > 0) {
                tvTestUnit.setText(units.get(0).getName());
                TestRecordDetailFragment.this.unitId = units.get(0).getId();
            }

        } else {
            tvTestUnit.setText(samplingDetail.getPrivateDataStringValue("ValueUnitName"));//结果单位
        }
        if (!mSampling.getIsCanEdit()) {
            btnDelete.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);

            tvTestTime.setEnabled(mSampling.getIsCanEdit());
            tvAnalyseTime.setEnabled(mSampling.getIsCanEdit());
            etAnalyseResult.setEnabled(mSampling.getIsCanEdit());
            tvTestUnit.setEnabled(mSampling.getIsCanEdit());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btn_back)
    public void onClick() {
        EventBus.getDefault().post(1, EventBusTags.TAG_INSTRUMENTAL_RECORD);
    }

    @OnClick({R.id.tv_analyse_time, R.id.tv_test_unit, R.id.btn_delete, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_analyse_time:
                initTimePickerView(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvAnalyseTime.setText(DateUtils.getTime(date.getTime()));
                    }
                });

                break;

            case R.id.tv_test_unit:
                Intent intent4 = new Intent(getContext(), UnitActivity.class);
                new AvoidOnResult(getActivity()).startForResult(intent4, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            //记录结果单位
                            TestRecordDetailFragment.this.unitId = data.getStringExtra("UnitId");
                            tvTestUnit.setText(data.getStringExtra("UnitName"));
                        }
                    }
                });
                break;

            case R.id.btn_delete:
                if (CheckUtil.isEmpty(mSampling.getSamplingDetailYQFs()) || listPosition == -1) {
                    ArtUtils.makeText(getContext(), "请先添加采样数据");
                    return;
                }
                showDelDialog();
                break;

            case R.id.btn_save:
                if (!saveCheck()) {
                    return;
                }

                //获取数据
                SamplingDetail samplingDetail = mSampling.getSamplingDetailYQFs().get(listPosition);

                //更新数据
                samplingDetail.setPrivateDataStringValue("SamplingOnTime", tvAnalyseTime.getText().toString());
                samplingDetail.setPrivateDataStringValue("CaleValue", etAnalyseResult.getText().toString());
                samplingDetail.setPrivateDataStringValue("ValueUnit", TestRecordDetailFragment.this.unitId);
                samplingDetail.setPrivateDataStringValue("ValueUnitName", tvTestUnit.getText().toString());
//                    samplingDetail.setPrivateDataBooleanValue("HasPX", "平行".equals(tvControl.getText().toString()));

                //计算均值和偏差值
                calcPXData(samplingDetail, false);

                //未完成状态改变为已完成，则更新到数据库
                if (!mSampling.getIsFinish()) {
                    //是否完成
                    mSampling.setIsFinish(InstrumentalActivity.IsSamplingFinish());
                    mSampling.setStatusName(mSampling.getIsFinish() ? "已完成" : "进行中");
                    if (mSampling.getIsFinish()) {
                        DBHelper.get().getSamplingDao().update(mSampling);
                    }
                }

                DBHelper.get().getSamplingDetailDao().update(samplingDetail);

                EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                EventBus.getDefault().post(1, EventBusTags.TAG_INSTRUMENTAL_RECORD);
                ArtUtils.makeText(getContext(), "保存成功");

                break;
        }
    }

    private boolean saveCheck() {
        if (TextUtils.isEmpty(tvAnalyseTime.getText().toString())) {
            ArtUtils.makeText(getContext(), "请选择分析时间！");
            return false;
        }

        String result = etAnalyseResult.getText().toString();
        if (TextUtils.isEmpty(result)) {
            ArtUtils.makeText(getContext(), "请输入分析结果！");
            return false;
        }

        try {
            Double.parseDouble(result);
        } catch (Exception e) {
            ArtUtils.makeText(getContext(), "分析结果不是合法数字！");
            return false;
        }

        if (TextUtils.isEmpty(TestRecordDetailFragment.this.unitId)) {
            ArtUtils.makeText(getContext(), "结果单位ID为空！");
            return false;
        }

        if (TextUtils.isEmpty(tvTestUnit.getText().toString())) {
            ArtUtils.makeText(getContext(), "请选择结果单位！");
            return false;
        }

        return true;
    }

    /**
     * 计算平行均值、相对偏差数据
     */
    private void calcPXData(SamplingDetail detail, boolean isDelete) {
        if (detail == null) {
            return;
        }

        SamplingDetail targetItem = TestRecordFragment.findPXItem(mSampling.getSamplingDetailYQFs(), detail);
        try {
            if (targetItem == null || TextUtils.isEmpty(targetItem.getPrivateDataStringValue("CaleValue"))) {
                //找不到对应数据，则删除计算的数据
                detail.setPrivateDataStringValue("RPDValue", "");
                detail.setValue("");
                return;
            } else if (detail == null || TextUtils.isEmpty(detail.getPrivateDataStringValue("CaleValue"))) {
                //找不到对应数据，则删除计算的数据
                targetItem.setPrivateDataStringValue("RPDValue", "");
                targetItem.setValue("");
                return;
            }

            //分别计算原样和平行样
            calcRecordValue(detail, targetItem);
            calcRecordValue(targetItem, detail);
        } finally {
            //保存一次对应数据
            if (targetItem != null) {
                DBHelper.get().getSamplingDetailDao().update(targetItem);

                //如果是删除平行数据，则原样可以选择
                if (isDelete && targetItem.getSamplingType() == 0) {
                    targetItem.setCanSelect(true);
                }
            }
        }
    }

    /**
     * 计算记录的值
     *
     * @param detail
     * @param targetDetail
     */
    private void calcRecordValue(SamplingDetail detail, SamplingDetail targetDetail) {
        String caleValue = detail.getPrivateDataStringValue("CaleValue");
        String targetCaleValue = targetDetail.getPrivateDataStringValue("CaleValue");

        if (TextUtils.isEmpty(caleValue) || TextUtils.isEmpty(targetCaleValue)) {
            return;
        }

        try {
            double value = Double.parseDouble(caleValue);
            double targetValue = Double.parseDouble(targetCaleValue);

            //四舍六入，奇进偶退
            //(样品含量-平行含量)/(样品含量+平行含量)
            double rpdValue = NumberUtil.roundingNumber((value - targetValue) / (value + targetValue) * 100);
            detail.setPrivateDataStringValue("RPDValue", rpdValue + "");

            if (detail.getSamplingType() == 0) {
                //均值计算公式：（样品含量+平行样含量）/2
                detail.setValue(calcAvg(value, targetValue) + "");
                //原样数据，标记做了平行
                detail.setPrivateDataBooleanValue("HasPX", true);
            }

        } catch (Exception e) {

        }
    }

    /**
     * 计算均值
     *
     * @param value1
     * @param value2
     * @return
     */
    private double calcAvg(double value1, double value2) {
        //计算小数位数
        int value1NumOfBits = calcNumberNumOfBits(value1);
        int value2NumOfBits = calcNumberNumOfBits(value2);

        //保留位数：取小数位数最大的
        return NumberUtil.fourHomesSixEntries((value1 + value2) / 2, value1NumOfBits > value2NumOfBits ? value1NumOfBits : value2NumOfBits);
    }

    /**
     * 计算小数位数
     *
     * @param value
     * @return
     */
    private int calcNumberNumOfBits(double value) {
        //转换成字符串
        String valueStr = value + "";

        //获取小数点的位置
        int bitPos = valueStr.indexOf(".");
        if (bitPos == -1) {
            return 0;//没有小数点
        }

        //往后移一位
        bitPos += 1;

        //小数点后面的数值转换成整数
        int bitNum = Integer.parseInt(valueStr.substring(bitPos));
        if (bitNum == 0) {
            return 0;//小红点后面是填充的0
        }

        //字符串总长度减去小数点位置就是小数位数
        return valueStr.length() - bitPos;
    }


    /**
     * 获取分析结果
     *
     * @param detail
     * @return
     */
    private double getCaleValue(SamplingDetail detail) {
        return Double.parseDouble(detail.getPrivateDataStringValue("CaleValue"));
    }

    private void initTimePickerView(OnTimeSelectListener listener) {
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(getActivity(), listener).setType(new boolean[]{true, true, true, true, true, true})
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
                        SamplingDetail samplingDetail1 = mSampling.getSamplingDetailYQFs().get(listPosition);

                        SamplingDetail samplingDetails1 = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.Id.eq(samplingDetail1.getId())).unique();
                        if (!CheckUtil.isNull(samplingDetails1)) {
                            DBHelper.get().getSamplingDetailDao().delete(samplingDetails1);
                        }

                        mSampling.getSamplingDetailYQFs().remove(samplingDetail1);

                        if (samplingDetail1.getSamplingType() == 1) {
                            //删除平行数据，重新计算样品计算均值和偏差值
                            calcPXData(samplingDetail1, true);
                        } else {
                            //删除样品数据时删除平行数据
                            SamplingDetail pxItem = TestRecordFragment.findPXItem(mSampling.getSamplingDetailYQFs(), samplingDetail1);
                            if (pxItem != null) {
                                DBHelper.get().getSamplingDetailDao().delete(pxItem);
                                mSampling.getSamplingDetailYQFs().remove(pxItem);
                            }

                            //删除点位ID和点位名称
                            if (!TextUtils.isEmpty(samplingDetail1.getAddresssId()) && mSampling.getAddressId().contains(samplingDetail1.getAddresssId())) {
                                mSampling.setAddressId(StringUtil.trimStr(mSampling.getAddressId().replace(samplingDetail1.getAddresssId(), ""), ","));
                            }
                            if (!TextUtils.isEmpty(samplingDetail1.getAddressName()) && mSampling.getAddressName().contains(samplingDetail1.getAddressName())) {
                                mSampling.setAddressName(StringUtil.trimStr(mSampling.getAddressName().replace(samplingDetail1.getAddressName(), ""), ","));
                            }

                            //保存到数据库
                            DBHelper.get().getSamplingDao().update(mSampling);

                            //更新采样单列表的显示
                            EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                        }

                        ArtUtils.makeText(getContext(), "删除成功");
                        EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                        EventBus.getDefault().post(1, EventBusTags.TAG_INSTRUMENTAL_RECORD);
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
