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
import com.google.gson.Gson;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import org.simple.eventbus.EventBus;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetailYQFs;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.UnitActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.NumberUtil;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity.mSampling;

public class TestRecordDetailFragment extends BaseFragment {

    Unbinder unbinder;

    /**
     * 样品编码
     */
    @BindView(R.id.my_layout_sample_code)
    MyDrawableLinearLayout tvSampleCode;

    /**
     * 频次
     */
    @BindView(R.id.my_layout_frequency)
    MyDrawableLinearLayout tvFrequency;

    /**
     * 点位
     */
    @BindView(R.id.my_layout_point)
    MyDrawableLinearLayout tvPoint;

    /**
     * 内控，平行/样品
     */
    @BindView(R.id.my_layout_control)
    MyDrawableLinearLayout tvControl;

    /**
     * 检测日期
     */
    @BindView(R.id.my_layout_time)
    MyDrawableLinearLayout tvTestTime;

    /**
     * 分析时间
     */
    @BindView(R.id.my_layout_analyse_time)
    MyDrawableLinearLayout tvAnalyseTime;

    /**
     * 分析结果
     */
    @BindView(R.id.my_layout_analyse_result)
    MyDrawableLinearLayout etAnalyseResult;

    /**
     * 结果单位
     */
    @BindView(R.id.my_layout_unit)
    MyDrawableLinearLayout tvTestUnit;

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
    private SamplingDetailYQFs samplingDetail;


    public TestRecordDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instrumental_test_record_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tvTestUnit.getEditText().setFocusable(false);
        tvTestUnit.getEditText().setClickable(false);
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
            tvAnalyseTime.setRightTextStr("");
            tvTestUnit.setRightTextStr("");
            etAnalyseResult.setEditTextStr("");
            TestRecordDetailFragment.this.unitId = "";
            createSampleDetailNo();
        }
    }

    private void createSampleDetailNo() {
        List<SamplingDetailYQFs> samplingDetailResults = mSampling.getSamplingDetailYQFs();

        SharedPreferences collectListSettings = getActivity().getSharedPreferences("setting", 0);
        listPosition = collectListSettings.getInt("listPosition", -1);

        btnDelete.setVisibility(View.VISIBLE);

        samplingDetail = samplingDetailResults.get(listPosition);
        SamplingDetailYQFs.PrivateJsonData privateData = samplingDetail.getJsonPrivateData();
        if (CheckUtil.isNull(privateData)) privateData = new SamplingDetailYQFs.PrivateJsonData();
        tvSampleCode.setRightTextStr(samplingDetail.getSampingCode());
        tvFrequency.setRightTextStr(samplingDetail.getFrequecyNo() + "");
        tvPoint.setRightTextStr(samplingDetail.getAddressName());
        tvTestTime.setRightTextStr(DateUtils.strGetDate(samplingDetail.getSamplingOnTime()));//监测日期
        tvControl.setRightTextStr(samplingDetail.getSamplingType() == 1 ? "平行" : "");
        tvAnalyseTime.setRightTextStr(CheckUtil.isNull(privateData.getSamplingOnTime()) ? "" :
                privateData.getSamplingOnTime());//分析实际
        etAnalyseResult.setEditTextStr(CheckUtil.isNull(privateData.getCaleValue()) ? "" :
                privateData.getCaleValue());//分析结果

        //记录结果单位ID
        unitId = CheckUtil.isNull(privateData.getValueUnit()) ? "" : privateData.getValueUnit();
        if (CheckUtil.isEmpty(privateData.getValueUnitName())) {
            List<Unit> units = DBHelper.get().getUnitDao().loadAll();
            if (units != null && units.size() > 0) {
                tvTestUnit.setRightTextStr(units.get(0).getName());
                unitId = units.get(0).getId();
            }
        } else {
            tvTestUnit.setRightTextStr(privateData.getValueUnitName());//结果单位
        }
        if (!mSampling.getIsCanEdit()) {
            btnDelete.setVisibility(View.GONE);
            btnSave.setVisibility(View.GONE);

//            tvTestTime.setEnabled(mSampling.getIsCanEdit());
//            tvAnalyseTime.setEnabled(mSampling.getIsCanEdit());
//            etAnalyseResult.setEnabled(mSampling.getIsCanEdit());
//            tvTestUnit.setEnabled(mSampling.getIsCanEdit());
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

    @OnClick({R.id.my_layout_analyse_time, R.id.my_layout_unit, R.id.btn_delete, R.id.btn_save})
    public void onViewClicked(View view) {
        hideSoftInput();
        if (!InstrumentalActivity.isNewCreate &&
                !UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
            showNoPermissionDialog("才能进行表单编辑。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
            return;
        }
        switch (view.getId()) {
            case R.id.my_layout_analyse_time:
                initTimePickerView(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvAnalyseTime.setRightTextStr(DateUtils.getTimeHour(date.getTime()));
                    }
                });

                break;

            case R.id.my_layout_unit:
                Intent intent4 = new Intent(getContext(), UnitActivity.class);
                new AvoidOnResult(getActivity()).startForResult(intent4, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            //记录结果单位
                            TestRecordDetailFragment.this.unitId = data.getStringExtra("UnitId");
                            tvTestUnit.setRightTextStr(data.getStringExtra("UnitName"));
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
                SamplingDetailYQFs.PrivateJsonData privateData = samplingDetail.getJsonPrivateData();
                //更新数据
                privateData.setSamplingOnTime(tvAnalyseTime.getRightTextViewStr());
                privateData.setCaleValue(etAnalyseResult.getEditTextStr());
                privateData.setValueUnit(unitId);
                privateData.setValueUnitName(tvTestUnit.getRightTextViewStr());
                samplingDetail.setJsonData(privateData);
//                    samplingDetail.setPrivateDataBooleanValue("HasPX", "平行".equals(tvControl.getText().toString()));

                //计算均值和偏差值
                calcPXData(samplingDetail, false);
                mSampling.getSamplingDetailYQFs().set(listPosition, samplingDetail);

                EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                EventBus.getDefault().post(1, EventBusTags.TAG_INSTRUMENTAL_RECORD);
                ArtUtils.makeText(getContext(), "保存成功");

                break;
        }
    }

    private boolean saveCheck() {
        if (TextUtils.isEmpty(tvAnalyseTime.getRightTextViewStr())) {
            ArtUtils.makeText(getContext(), "请选择分析时间！");
            return false;
        }

        String result = etAnalyseResult.getEditTextStr();
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

        if (TextUtils.isEmpty(unitId)) {
            ArtUtils.makeText(getContext(), "结果单位ID为空！");
            return false;
        }

        if (TextUtils.isEmpty(tvTestUnit.getRightTextViewStr())) {
            ArtUtils.makeText(getContext(), "请选择结果单位！");
            return false;
        }

        return true;
    }

    /**
     * 计算平行均值、相对偏差数据
     */
    private void calcPXData(SamplingDetailYQFs detail, boolean isDelete) {
        if (detail == null
                || CheckUtil.isNull(detail.getJsonPrivateData())
                || CheckUtil.isEmpty(detail.getJsonPrivateData().getCaleValue())) {
            return;
        }

        SamplingDetailYQFs targetItem = TestRecordFragment.findPXItem(mSampling.getSamplingDetailYQFs(), detail);
        if (targetItem == null
                || CheckUtil.isNull(targetItem.getJsonPrivateData())
                || TextUtils.isEmpty(targetItem.getJsonPrivateData().getCaleValue())) {//找不到对应的平行样或者普样的数据
            return;
        }
        SamplingDetailYQFs.PrivateJsonData privateDataItem = targetItem.getJsonPrivateData();
        SamplingDetailYQFs.PrivateJsonData privateData = detail.getJsonPrivateData();
        boolean detailIsPX = false;
        if (detail.getSamplingType() == 1
                && targetItem.getSamplingType() == 0) { //detail 是平行数据
            detailIsPX = true;
        } else if (targetItem.getSamplingType() == 1
                && detail.getSamplingType() == 0) {//targetItem 是平行样
            detailIsPX = false;
        }
        if (detailIsPX) { //detail 是平行数据
            if (isDelete) {
                privateDataItem.setRPDValue("");
                targetItem.setValue("");
            } else {
                targetItem.setValue(getValue(privateData.getCaleValue(), privateDataItem.getCaleValue()));//均值
                privateDataItem.setRPDValue(getRpdValue(privateDataItem.getCaleValue(), privateData.getCaleValue()));
                privateData.setRPDValue(getRpdValue(privateData.getCaleValue(), privateDataItem.getCaleValue()));
                privateData.setHasPX(true);           //原样数据，标记做了平行
            }
        } else { //detail 不是平行数据
            if (isDelete) {
                privateData.setRPDValue("");
                detail.setValue("");
            } else {
                detail.setValue(getValue(privateData.getCaleValue(), privateDataItem.getCaleValue()));//均值
                privateDataItem.setRPDValue(getRpdValue(privateDataItem.getCaleValue(), privateData.getCaleValue()));
                privateData.setRPDValue(getRpdValue(privateData.getCaleValue(), privateDataItem.getCaleValue()));
                privateData.setHasPX(true);           //原样数据，标记做了平行
            }
        }
        detail.setJsonData(privateData);
        targetItem.setJsonData(privateDataItem);
        Collections.sort(mSampling.getSamplingDetailYQFs(), new TestRecordFragment.DetailComparator());
        for (int i = 0; i < mSampling.getSamplingDetailYQFs().size(); i++) {
            SamplingDetailYQFs yqFs = mSampling.getSamplingDetailYQFs().get(i);
            if (yqFs == targetItem) {
                mSampling.getSamplingDetailYQFs().set(i, targetItem);
                break;
            }
        }
        samplingDetail = detail;
    }

    /**
     * 获取相对偏差
     *
     * @param caleValue  原样值分析结果
     * @param caleValue1 平行样分析结果
     * @return
     */
    private String getRpdValue(String caleValue, String caleValue1) {
        try {
            double value = Double.parseDouble(caleValue);
            double targetValue = Double.parseDouble(caleValue1);
            //四舍六入，奇进偶退
            //(样品含量-平行含量)/(样品含量+平行含量)
            double rpdValue = NumberUtil.roundingNumber((value - targetValue) / (value + targetValue) * 100);
            return rpdValue + "";
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取均值
     *
     * @param caleValue   原样值分析结果
     * @param caleValuePx 平行样分析结果
     * @return
     */
    public String getValue(String caleValue, String caleValuePx) {
        if (TextUtils.isEmpty(caleValue) || TextUtils.isEmpty(caleValuePx)) {
            return "";
        }
        try {
            //均值计算公式：（样品含量+平行样含量）/2
            double valueDouble = calcAvg(caleValue, caleValuePx);
            double value = Double.parseDouble(caleValue);
            double targetValue = Double.parseDouble(caleValuePx);
            int size = 0;
            if (value > targetValue) {
                size = NumberUtil.calcNumberNumOfBits(caleValue);
            } else if (targetValue > value) {
                size = NumberUtil.calcNumberNumOfBits(caleValuePx);
            }
            return size > 0 ? valueDouble + "" : (int) valueDouble + "";

        } catch (Exception e) {
            return "";
        }

    }

    /**
     * 计算记录的值
     *
     * @param detail
     * @param targetDetail
     */
    private void calcRecordValue(SamplingDetailYQFs detail, SamplingDetailYQFs targetDetail) {
        SamplingDetailYQFs.PrivateJsonData privateData = detail.getJsonPrivateData();
        SamplingDetailYQFs.PrivateJsonData privateDataPx = targetDetail.getJsonPrivateData();
        String caleValue = privateData.getCaleValue();
        String targetCaleValue = privateDataPx.getCaleValue();

        if (TextUtils.isEmpty(caleValue) || TextUtils.isEmpty(targetCaleValue)) {
            return;
        }

        try {
            double value = Double.parseDouble(caleValue);
            double targetValue = Double.parseDouble(targetCaleValue);

            //四舍六入，奇进偶退
            //(样品含量-平行含量)/(样品含量+平行含量)
            double rpdValue = NumberUtil.roundingNumber((value - targetValue) / (value + targetValue) * 100);
            privateData.setRPDValue(rpdValue + "");//相对偏差

            if (detail.getSamplingType() == 0) {
                //均值计算公式：（样品含量+平行样含量）/2
                double valueDouble = calcAvg(caleValue, targetCaleValue);
                int size = 0;
                if (value > targetValue) {
                    size = NumberUtil.calcNumberNumOfBits(caleValue);
                } else if (targetValue > value) {
                    size = NumberUtil.calcNumberNumOfBits(targetCaleValue);
                }
                detail.setValue(size > 0 ? valueDouble + "" : (int) valueDouble + "");
                //原样数据，标记做了平行
                privateData.setHasPX(true);
            }

            detail.setJsonData(privateData);
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
    private double calcAvg(String value1, String value2) {
        //计算小数位数
        int value1NumOfBits = NumberUtil.calcNumberNumOfBits(value1);
        int value2NumOfBits = NumberUtil.calcNumberNumOfBits(value2);
        double va1 = Double.parseDouble(value1);
        double va2 = Double.parseDouble(value2);
        //保留位数：取小数位数最大的
        return NumberUtil.fourHomesSixEntries((va1 + va2) / 2, va1 > va2 ? value1NumOfBits : value2NumOfBits);
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
        TimePickerView pvTime = new TimePickerBuilder(getActivity(), listener).setType(new boolean[]{false, false, false, true, true, false})
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

                        SamplingDetail samplingDetails1 = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.Id.eq(samplingDetail.getId())).unique();
                        if (!CheckUtil.isNull(samplingDetails1)) {
                            DBHelper.get().getSamplingDetailDao().delete(samplingDetails1);
                        }

                        mSampling.getSamplingDetailYQFs().remove(samplingDetail);

                        if (samplingDetail.getSamplingType() == 1) {
                            //删除平行数据，重新计算样品计算均值和偏差值
                            calcPXData(samplingDetail, true);
                        } else {
                            //删除样品数据时删除平行数据
                            SamplingDetailYQFs pxItem = TestRecordFragment.findPXItem(mSampling.getSamplingDetailYQFs(), samplingDetail);
                            if (pxItem != null) {
                                mSampling.getSamplingDetailYQFs().remove(pxItem);
                            }

                            //保存到数据库
//                            DBHelper.get().getSamplingDao().update(mSampling);

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
