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

import org.simple.eventbus.EventBus;

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

        TestRecordDetailFragment.this.unitId = "";

        if (isVisibleToUser) {
            tvAnalyseTime.setText("");
            tvTestUnit.setText("");
            etAnalyseResult.setText("");
            creatSampleDetailNo();
        }
    }

    private void creatSampleDetailNo() {
        mSampling = InstrumentalActivity.mSampling;

        List<SamplingDetail> samplingDetailResults = mSampling.getSamplingDetailYQFs();

        SharedPreferences collectListSettings = getActivity().getSharedPreferences("setting", 0);
        listPosition = collectListSettings.getInt("listPosition", -1);

        if (listPosition == -1) {
            //添加 生成编码

            //JS(要素)181029(日期)-01(点位)01(账号)-01(频次)
            String samplingNo;

            String snDate = DateUtils.getDate().replace("-", "").substring(2);
            String snPointPosition = "采样点位编号未填写";
            if (!CheckUtil.isEmpty(mSampling.getAddressNo())) {
                snPointPosition = mSampling.getAddressNo();
            }
            String snUserId = UserInfoHelper.get().getUser().getIntId() + "";
            int snFrequency = 1;
            if (samplingDetailResults != null
                    && samplingDetailResults.size() > 0) {
                snFrequency = samplingDetailResults.get(samplingDetailResults.size() - 1).getFrequecyNo() + 1;
            }

            samplingNo = "JS" + snDate + "-" + snPointPosition + snUserId + "-" + StringUtil.autoGenericCode(snFrequency, 2);

            tvSampleCode.setText(samplingNo);
            tvFrequency.setText(snFrequency + "");
        } else {
            btnDelete.setVisibility(View.VISIBLE);

            SamplingDetail samplingDetail = samplingDetailResults.get(listPosition);
            tvSampleCode.setText(samplingDetail.getSampingCode());
            tvFrequency.setText(samplingDetail.getFrequecyNo());
//          TODO:tvPoint.setText();
            tvTestTime.setText(samplingDetail.getSamplingTime());
            try {
                JSONObject jsonObject = new JSONObject(samplingDetail.getPrivateData());
                tvControl.setText(jsonObject.getBoolean("HasPX") ? "平行" : "样品");
                tvAnalyseTime.setText(jsonObject.getString("SamplingOnTime"));
                etAnalyseResult.setText(jsonObject.getString("CaleValue"));
//              TODO:tvTestUnit.setText(jsonObject.getString("CaleValue"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                if (saveCheck()) {
                    SamplingDetail samplingDetail;
                    if (listPosition == -1) {
                        samplingDetail = new SamplingDetail();
                    } else {
                        samplingDetail = mSampling.getSamplingDetailYQFs().get(listPosition);
                    }

                    samplingDetail.setId("LC-" + UUID.randomUUID().toString());
                    samplingDetail.setSamplingId(InstrumentalActivity.mSampling.getId());
                    samplingDetail.setSampingCode(tvSampleCode.getText().toString());
                    samplingDetail.setFrequecyNo(Integer.parseInt(tvFrequency.getText().toString()));

                    HashMap<String, String> map = new HashMap<>();
                    map.put("SamplingOnTime", tvAnalyseTime.getText().toString());
                    map.put("CaleValue", etAnalyseResult.getText().toString());
                    map.put("ValueUnit", TestRecordDetailFragment.this.unitId);
                    map.put("ValueUnitName", tvTestUnit.getText().toString());
                    Boolean hasPx = "平行".equals(tvControl.getText().toString());
                    map.put("HasPX", hasPx.toString());

                    samplingDetail.setPrivateData(new JSONObject(map).toString());

                    if (listPosition == -1) {
                        if (mSampling.getSamplingDetailYQFs() == null) {
                            mSampling.setSamplingDetailYQFs(new ArrayList<SamplingDetail>());
                        }
                        List<SamplingDetail> samplingDetailResults = mSampling.getSamplingDetailYQFs();
                        samplingDetailResults.add(samplingDetail);
                        mSampling.setSamplingDetailYQFs(samplingDetailResults);
                    }

                    mSampling.setIsFinish(isSamplingFinish());
                    mSampling.setStatusName(isSamplingFinish() ? "已完成" : "进行中");
                    Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSampling.getId())).unique();
                    if (CheckUtil.isNull(sampling)) {
                        DBHelper.get().getSamplingDao().insert(mSampling);
                    } else {
                        DBHelper.get().getSamplingDao().update(mSampling);
                    }

                    SamplingDetail samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(samplingDetail.getId())).unique();
                    if (!CheckUtil.isNull(samplingDetails)) {
                        DBHelper.get().getSamplingDetailDao().delete(samplingDetails);
                    }
                    DBHelper.get().getSamplingDetailDao().insert(samplingDetail);

                    EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                    EventBus.getDefault().post(1, EventBusTags.TAG_PRECIPITATION_COLLECTION);
                    ArtUtils.makeText(getContext(), "保存成功");
                }

                break;
        }
    }

    private boolean saveCheck() {
        if (TextUtils.isEmpty(tvAnalyseTime.getText().toString())) {
            ArtUtils.makeText(getContext(), "请选择分析时间");
            return false;
        }

        if (TextUtils.isEmpty(etAnalyseResult.getText().toString())) {
            ArtUtils.makeText(getContext(), "请输入分析结果");
            return false;
        }

        if (TextUtils.isEmpty(tvTestUnit.getText().toString())) {
            ArtUtils.makeText(getContext(), "请选择结果单位");
            return false;
        }

        return true;
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

                        SamplingDetail samplingDetails1 = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(samplingDetail1.getSamplingId())).unique();
                        if (!CheckUtil.isNull(samplingDetails1)) {
                            DBHelper.get().getSamplingDetailDao().delete(samplingDetails1);
                        }

                        mSampling.getSamplingDetailYQFs().remove(listPosition);

                        mSampling.setIsFinish(isSamplingFinish());
                        mSampling.setStatusName(isSamplingFinish() ? "已完成" : "进行中");
                        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSampling.getId())).unique();
                        if (CheckUtil.isNull(sampling)) {
                            DBHelper.get().getSamplingDao().insert(mSampling);
                        } else {
                            DBHelper.get().getSamplingDao().update(mSampling);
                        }

                        ArtUtils.makeText(getContext(), "删除成功");
                        EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                        EventBus.getDefault().post(1, EventBusTags.TAG_PRECIPITATION_COLLECTION);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    /**
     * 采样是否完成
     *
     * @return
     */
    private boolean isSamplingFinish() {
        if (CheckUtil.isEmpty(mSampling.getSamplingDetailYQFs())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getSamplingUserName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getTagName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getAddressName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getPrivateData())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getMethodName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getDeviceName())) {
            return false;
        }
        return true;

    }
}
