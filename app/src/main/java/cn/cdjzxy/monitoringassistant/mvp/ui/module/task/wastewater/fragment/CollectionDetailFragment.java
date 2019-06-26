package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingStantdDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.MonItemActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.ClickUtils;
import cn.cdjzxy.monitoringassistant.utils.Constants;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.HelpUtil;
import cn.cdjzxy.monitoringassistant.utils.SamplingUtil;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity.mSample;

/**
 * 采集样品详情
 */

public class CollectionDetailFragment extends BaseFragment {
    @BindView(R.id.my_layout_sample_code)
    MyDrawableLinearLayout sample_code;//样品编码
    @BindView(R.id.my_layout_sample_time)
    MyDrawableLinearLayout sample_Time;//采样时间
    @BindView(R.id.my_layout_sample_frequency)
    MyDrawableLinearLayout sample_frequency;//频次
    @BindView(R.id.my_layout_sample_quality)
    MyDrawableLinearLayout sample_quality;//质控
    @BindView(R.id.my_layout_sample_monitor_items)
    MyDrawableLinearLayout sample_monitor_items;//检测项目
    @BindView(R.id.my_layout_sample_monitor)
    MyDrawableLinearLayout sample_monitor;//现场检测
    @BindView(R.id.text_view_sample_add_preserve)
    TextView text_view_sample_add_preserve;//是否添加保存剂
    @BindView(R.id.sample_add_preserve)
    CheckedTextView sample_add_preserve;
    @BindView(R.id.text_view_sample_compare_monitor)
    TextView text_view_sample_compare_monitor;//是否对比监测
    @BindView(R.id.sample_compare_monitor)
    CheckedTextView sample_compare_monitor;
    @BindView(R.id.sample_mark)
    TextView sample_mark;
    @BindView(R.id.operate_layout)
    View operate_layout;

    Unbinder unbinder;

    private int fsListPosition;
    SamplingContent samplingDetail;

    //用于显示加载框
    private Dialog dialog;
    private TextView dialogTextView;

    // private static ArrayList<SamplingFormStand> oldMonitemIds = new ArrayList<>();
    private String oldItemId;

    public CollectionDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_collect_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!CheckUtil.isNull(mSample) && !mSample.getIsCanEdit()) {
            operate_layout.setVisibility(View.INVISIBLE);
        } else {
            operate_layout.setVisibility(View.VISIBLE);
        }

        sample_add_preserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_add_preserve.setChecked(!sample_add_preserve.isChecked());
                samplingDetail.setIsAddPreserve(sample_add_preserve.isChecked());
                setViewStyleDrawable(samplingDetail.isAddPreserve(), text_view_sample_add_preserve);
                if (samplingDetail.isAddPreserve()) {
                    samplingDetail.setPreservative("是");
                } else {
                    samplingDetail.setPreservative("否");
                }

            }
        });

        sample_compare_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_compare_monitor.setChecked(!sample_compare_monitor.isChecked());
                samplingDetail.setIsCompare(sample_compare_monitor.isChecked());
                setViewStyleDrawable(samplingDetail.getIsCompare(), text_view_sample_compare_monitor);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            sample_code.setRightTextStr("");
            sample_Time.setRightTextStr("");
            sample_frequency.setRightTextStr("");
            sample_quality.setRightTextStr("");
            sample_monitor_items.setRightTextStr("");
            sample_monitor.setRightTextStr("");
            sample_mark.setText("");
            setSamplingDetail();
        }

    }

    private void setSamplingDetail() {
        List<SamplingContent> samplingDetailResults = mSample.getSamplingContentResults();
        SharedPreferences collectListSettings = getActivity().getSharedPreferences("setting", 0);
        fsListPosition = collectListSettings.getInt("fsListPosition", -1);
        if (fsListPosition == -1) {
            samplingDetail = new SamplingContent();
            samplingDetail.setId(UUID.randomUUID().toString());
            sample_code.setRightTextStr(HelpUtil.createSamplingCode(mSample));
            sample_frequency.setRightTextStr(HelpUtil.createFrequency(mSample) + "");
            sample_quality.setRightTextStr(Constants.SAMPLING_TYPE_PT);
            samplingDetail.setSamplingType(0);
            samplingDetail.setOrderIndex(HelpUtil.createOrderIndex(mSample));
            samplingDetail.setFrequecyNo(HelpUtil.createFrequency(mSample));
            //设置是否添加保存剂，是否对比监测
            samplingDetail.setPreservative("否");
            samplingDetail.setIsAddPreserve(false);
            samplingDetail.setIsCompare(false);
            setViewStyleDrawable(false, text_view_sample_add_preserve);
            setViewStyleDrawable(false, text_view_sample_compare_monitor);
            samplingDetail.setSamplingTime("");
            //新增时要将监测项目和现场监测项目带过来
            setDefaultMonitor();
        } else {
            samplingDetail = samplingDetailResults.get(fsListPosition);
            sample_code.setRightTextStr(samplingDetail.getSampingCode());
            sample_frequency.setRightTextStr(samplingDetail.getFrequecyNo() + "");
            /**设置检测项目简介**/
            //这里把MonitemName内容去除重复
            String res = MakeDeferenceGone(samplingDetail.getMonitemName());
            samplingDetail.setMonitemName(res);
            sample_monitor_items.setRightTextStr(res);
            /**设置现场项目简介**/
            //在过滤重复项
//            String res = getDifference(samplingDetail.getSenceMonitemName()
//                    , samplingDetail.getMonitemName());
//            Log.d("zzh", "res=" + res);
//            Log.d("zzh", "template=" + samplingDetail.getSenceMonitemName());

            sample_monitor.setRightTextStr(samplingDetail.getSenceMonitemName());
            sample_Time.setRightTextStr(samplingDetail.getSamplingTime());
            if (!CheckUtil.isNull(samplingDetail.getPreservative()) && samplingDetail.getPreservative().equals("是")) {
                sample_add_preserve.setChecked(true);
                samplingDetail.setIsAddPreserve(true);
                samplingDetail.setPreservative("是");
                setViewStyleDrawable(true, text_view_sample_add_preserve);
            } else {
                sample_add_preserve.setChecked(false);
                samplingDetail.setIsAddPreserve(false);
                samplingDetail.setPreservative("否");
                setViewStyleDrawable(false, text_view_sample_add_preserve);
            }
            setViewStyleDrawable(samplingDetail.getIsCompare(), text_view_sample_compare_monitor);
            sample_compare_monitor.setChecked(samplingDetail.getIsCompare());
            sample_mark.setText(samplingDetail.getDescription());

            //记录不同项目的长度

            if (!CheckUtil.isEmpty(samplingDetail.getMonitemId())) {
                sample_monitor_items.setLeftTextViewStr("监测项目（"
                        + samplingDetail.getMonitemId().split(",").length
                        + "）");
            } else {
                sample_monitor_items.setLeftTextViewStr("监测项目（0）");
            }

            if (!CheckUtil.isEmpty(samplingDetail.getSenceMonitemId())) {
                /**判断有无相似项目**/
                int resone = getThelenth(samplingDetail.getSenceMonitemId().split(",")
                        , samplingDetail.getMonitemId().split(","));
                sample_monitor.setLeftTextViewStr("现场监测（" + resone + "）");
            } else {
                sample_monitor.setLeftTextViewStr("现场监测（0）");
            }

            if (samplingDetail.getSamplingType() == 0) {
                sample_quality.setRightTextStr(Constants.SAMPLING_TYPE_PT);
            } else if (samplingDetail.getSamplingType() == 1) {
                sample_quality.setRightTextStr(Constants.SAMPLING_TYPE_PX);
            } else if (samplingDetail.getSamplingType() == 2) {
                sample_quality.setRightTextStr(Constants.SAMPLING_TYPE_KB);
            }
        }

        //金蓉：平行数据或空白数据不能编辑现场监测
        if (samplingDetail.getSamplingType() == 2 || samplingDetail.getSamplingType() == 1) {
            sample_monitor.setEnabled(false);
        } else {
            sample_monitor.setEnabled(true);
        }
    }

    /**
     * 给view设置选中样式
     *
     * @param isSelect 选中还是未选中
     * @param view     view
     */
    private void setViewStyleDrawable(boolean isSelect, TextView view) {
        Drawable drawable;
        if (isSelect) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getDrawable(R.mipmap.icon_yes_data);
            } else {
                drawable = getContext().getResources().getDrawable(R.mipmap.icon_yes_data);
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = getContext().getDrawable(R.mipmap.icon_no_data);
            } else {
                drawable = getContext().getResources().getDrawable(R.mipmap.icon_no_data);
            }
        }
        view.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
    }

    /**
     * 将monitemName中的重复项去除
     *
     * @param monitemName 需要去重的字符串
     * @return 去重结果
     */
    private String MakeDeferenceGone(String monitemName) {
        if (monitemName == null || monitemName.equals("")) {
            return monitemName;
        }

        List<String> aimList = new ArrayList<>();
        String[] itemList = monitemName.split(",");

        for (String item : itemList) {
            if (!aimList.contains(item)) {
                aimList.add(item);
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String item : aimList) {
            builder.append(item + ",");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        return builder.toString();
    }

    /**
     * 过滤重复项 如果str1中有包含str2的内容，则把str1重复的内容去掉，
     *
     * @param str1 过滤的本体
     * @param str2 过滤材料源
     * @return 由","区分的结果 返回str1中剩下的部分
     */
    private String getDifference(String str1, String str2) {
        if (str1 == null || str1.equals("")) {
            return str1;
        }
        if (str2 == null || str2.equals(""))
            return str1;
        String[] strList = str1.split(",");
        List<String> itemList = Arrays.asList(str2.split(","));
        StringBuilder res = new StringBuilder();
        for (String item : strList) {
            if (!itemList.contains(item)) {
                res.append(item + ",");
            }
        }
        if (res.lastIndexOf(",") > 0) {
            res.deleteCharAt(res.lastIndexOf(","));
        }
        return res.toString();
    }

    /**
     * 判断有无相似项
     *
     * @param split 返回不同项的长度
     */
    private int getThelenth(String[] split, String[] other) {
        int count = 0;
        int len = split.length;
        List<String> one = Arrays.asList(other);
        for (int i = 0; i < len; i++) {
            if (!one.contains(split[i])) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.my_layout_sample_monitor_items, R.id.my_layout_sample_monitor,
            R.id.btn_delete, R.id.btn_save, R.id.my_layout_sample_time})
    public void onClick(View view) {
        hideSoftInput();
        if (view.getId() == R.id.btn_back) {
            EventBus.getDefault().post(1, EventBusTags.TAG_WASTEWATER_COLLECTION);
            return;
        }
        if (!WastewaterActivity.isNewCreate && !UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Sampling_Modify_Num)) {
            showNoPermissionDialog("才能进行表单编辑。", UserInfoAppRight.APP_Permission_Sampling_Modify_Name);
            return;
        }
        if (!ClickUtils.canClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn_back:
                EventBus.getDefault().post(1, EventBusTags.TAG_WASTEWATER_COLLECTION);
                break;
            case R.id.my_layout_sample_monitor_items:
                showMonitorItems();
                break;
            case R.id.my_layout_sample_monitor:
                showAddressItems();
                break;
            case R.id.btn_delete:
                operateDelete();
                break;
            case R.id.btn_save:
                operateSave();
                break;
            case R.id.my_layout_sample_time:
                showDateSelectDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 时间选择器(采样日期)
     * data picker
     */
    private void showDateSelectDialog() {
        TimePickerView pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                samplingDetail.setSamplingTime(DateUtils.getTimeNoMinute(date));
                sample_Time.setRightTextStr(DateUtils.getTimeNoMinute(date));
            }
        }).setType(new boolean[]{false, false, false, true, true, false}).build();
        pvTime.setDate(Calendar.getInstance());
        pvTime.show();
    }

    /**
     * delete operate
     */
    private void operateDelete() {
        if (CheckUtil.isEmpty(mSample.getSamplingContentResults()) || fsListPosition == -1) {
            ArtUtils.makeText(getContext(), "请先添加采样数据");
            return;
        }
        showDeleteDialog();
    }


    /**
     * 弹出删除对话框
     */
    private void showDeleteDialog() {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("确定删除数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLoadingDialog("请等待...", true);
                        //删除SamplingContent
                        SamplingContent currentDetail = mSample.getSamplingContentResults().get(fsListPosition);
                        SamplingContent samplingDetail = DBHelper.get().getSamplingContentDao().queryBuilder().where(SamplingContentDao.Properties.Id.eq(currentDetail.getId())).unique();
                        if (!CheckUtil.isNull(samplingDetail)) {
                            DBHelper.get().getSamplingContentDao().delete(samplingDetail);
                        }
                        //删除平行样
                        if (!CheckUtil.isNull(samplingDetail) && samplingDetail.getSamplingType() == 0) {//删除普通样对应的平行样
                            SamplingContent parallelSamContent = DBHelper.get().getSamplingContentDao().
                                    queryBuilder().where(SamplingContentDao.Properties.ProjectId.
                                    eq(samplingDetail.getProjectId()), SamplingContentDao.Properties.SamplingId.
                                    eq(samplingDetail.getSamplingId()), SamplingContentDao.Properties.FrequecyNo.
                                    eq(samplingDetail.getFrequecyNo())).unique();
                            if (!CheckUtil.isNull(parallelSamContent)) {
                                DBHelper.get().getSamplingContentDao().delete(parallelSamContent);
                                mSample.getSamplingContentResults().remove(parallelSamContent);
                            }
                        }
                        //删除分瓶信息
                        deleteRelateBottle();

                        if ((fsListPosition >= 0) && (fsListPosition < mSample.getSamplingContentResults().size())) {
                            mSample.getSamplingContentResults().remove(fsListPosition);
                        }

                        //删除对应的SamplingDetail
                        List<SamplingDetail> samplingDetailList = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(currentDetail.getSamplingId()), SamplingDetailDao.Properties.SampingCode.eq(currentDetail.getSampingCode()), SamplingDetailDao.Properties.SamplingType.eq(currentDetail.getSamplingType())).build().list();
                        if (!CheckUtil.isEmpty(samplingDetailList)) {
                            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetailList);
                            //WastewaterActivity.mSample.setSamplingDetailResults(new ArrayList<>());
                            mSample.getSamplingDetailResults().removeAll(samplingDetailList);
                        }


                        //下面这个地方有问题，没清除数据库之前的数据
                        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().
                                queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).list();
                        if (!CheckUtil.isEmpty(formStantdsList)) {
                            mSample.setSamplingFormStandResults(formStantdsList);
                        } else {
                            mSample.setSamplingFormStandResults(new ArrayList<>());
                        }

                        mSample.setIsFinish(SamplingUtil.isSamplingFinsh(mSample));
                        mSample.setStatusName(mSample.getIsFinish() ? "已完成" : "进行中");
                        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSample.getId())).unique();
                        if (CheckUtil.isNull(sampling)) {
                            DBHelper.get().getSamplingDao().insert(mSample);
                        } else {
                            DBHelper.get().getSamplingDao().update(mSample);
                        }

                        closeLoadingDialog();

                        ArtUtils.makeText(getContext(), "删除成功");
                        EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                        EventBus.getDefault().post(1, EventBusTags.TAG_WASTEWATER_COLLECTION);
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
     * save
     * todo:修改下面这个地方！！
     */
    private void operateSave() {
        if (isSaveChecked()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    showLoadingDialog("请等待...", true);
                    samplingDetail.setProjectId(mSample.getProjectId());
                    samplingDetail.setSamplingId(mSample.getId());
                    samplingDetail.setSampingCode(sample_code.getRightTextViewStr());
                    samplingDetail.setFrequecyNo(Integer.parseInt(sample_frequency.getRightTextViewStr()));
                    samplingDetail.setDescription(sample_mark.getText().toString());
                    // samplingDetail.setSamplingTime(DateUtils.getWholeDate());
                    //设置样品采集和样品验收
                    samplingDetail.setSampleCollection("");
                    samplingDetail.setSampleAcceptance("");
                    //生成分瓶信息
                    generateBottleSplit();
                    //根据分瓶等到的样品数量
                    int fxcCount = HelpUtil.setSamplingCount(samplingDetail, mSample);
                    samplingDetail.setSamplingCount(fxcCount);
                    //新增样品采集到采样单中
                    if (fsListPosition == -1) {
                        if (mSample.getSamplingContentResults() == null) {
                            mSample.setSamplingContentResults(new ArrayList<SamplingContent>());
                        }
                        List<SamplingContent> samplingDetailResults = mSample.getSamplingContentResults();
                        if (!samplingDetailResults.contains(samplingDetail)) {
                            samplingDetailResults.add(samplingDetail);
                        }
                        mSample.setSamplingContentResults(samplingDetailResults);
                    }

                    //设置采样单完结状态以及更新采样单
                    mSample.setIsFinish(SamplingUtil.isSamplingFinsh(mSample));
                    mSample.setStatusName(mSample.getIsFinish() ? "已完成" : "进行中");
                    Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSample.getId())).unique();
                    if (CheckUtil.isNull(sampling)) {
                        //mSample.setId( UUID.randomUUID().toString());
                        DBHelper.get().getSamplingDao().insert(mSample);
                    } else {
                        DBHelper.get().getSamplingDao().update(mSample);
                    }


                    //删除之前生成的SamplingDetail
                    List<SamplingDetail> samplingDetailList = DBHelper.get().getSamplingDetailDao().
                            queryBuilder().where(SamplingDetailDao.Properties.SamplingId.
                                    eq(samplingDetail.getSamplingId()),
                            SamplingDetailDao.Properties.SampingCode.eq(samplingDetail.getSampingCode()),
                            SamplingDetailDao.Properties.SamplingType.eq(samplingDetail.getSamplingType()))
                            .build().list();
                    if (!CheckUtil.isEmpty(samplingDetailList)) {
                        DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetailList);
                        mSample.getSamplingDetailResults().removeAll(samplingDetailList);
                    }
                    //生成新的SamplingDetail
                    generateSamplingDetails(fxcCount);

                    if (fsListPosition == -1) {
                        //samplingDetail.setId(UUID.randomUUID().toString());
                        //samplingDetail.setSamplingType(0);
                        //DBHelper.get().getSamplingContentDao().insert(samplingDetail);
                        DBHelper.get().getSamplingContentDao().insertOrReplace(samplingDetail);
                    } else {
                        DBHelper.get().getSamplingContentDao().update(samplingDetail);
                    }

                    //设置信息
                    setBottleAndContent();
                    closeLoadingDialog();

                    EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
                    EventBus.getDefault().post(1, EventBusTags.TAG_WASTEWATER_COLLECTION);
                }
            }).start();

        }
    }


    /**
     * 保存必须校验
     *
     * @return
     */
    private boolean isSaveChecked() {
        if (TextUtils.isEmpty(sample_monitor_items.getRightTextViewStr())) {
            ArtUtils.makeText(getContext(), "请选择监测项目");
            return false;
        }
        return true;
    }

    /**
     * 选择监测项目
     */
    private void showMonitorItems() {
        if (fsListPosition != -1) {
            oldItemId = samplingDetail.getMonitemId();
        }
        Intent intent = new Intent(getContext(), MonItemActivity.class);
        intent.putExtra("tagId", mSample.getParentTagId());
        if (!CheckUtil.isEmpty(samplingDetail.getMonitemId())) {
            intent.putExtra("selectItems", samplingDetail.getMonitemId());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    saveAndShowItem(data, false);
                }
            }
        });
    }

    /**
     * 选择现场监测项目
     */
    private void showAddressItems() {
        Intent intent = new Intent(getContext(), MonItemActivity.class);
        intent.putExtra("tagId", mSample.getParentTagId());
        if (!CheckUtil.isEmpty(samplingDetail.getSenceMonitemId())) {
            intent.putExtra("selectItems", samplingDetail.getSenceMonitemId());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    saveAndShowItem(data, true);
                }
            }
        });
    }

    /**
     * 根据@isScenel来区分保存和显示intent传递的项目值是“现场检测”还是“检测项目”
     *
     * @param data                          传递的项目名称和id
     * @param isScene@true现场检测”@false“检测项目”
     */
    public void saveAndShowItem(Intent data, boolean isScene) {
        String id = data.getStringExtra("MonItemId");
        String name = data.getStringExtra("MonItemName");
        String str = "";
        if (isScene) {//现场检测
            samplingDetail.setSenceMonitemName(name);
            samplingDetail.setSenceMonitemId(id);
            sample_monitor.setRightTextStr(name);
            //name去重
            str = getDifference(samplingDetail.getMonitemName(), samplingDetail.getSenceMonitemName());
            sample_monitor_items.setRightTextStr(str);
            samplingDetail.setMonitemName(str);
            //id去重
            samplingDetail.setMonitemId(getDifference(samplingDetail.getMonitemId(),
                    samplingDetail.getSenceMonitemId()));
        } else {//检测项目
            samplingDetail.setMonitemName(name);
            samplingDetail.setMonitemId(id);
            sample_monitor_items.setRightTextStr(name);
            //name去重
            str = getDifference(samplingDetail.getSenceMonitemName(), samplingDetail.getMonitemName());
            sample_monitor.setRightTextStr(str);
            samplingDetail.setSenceMonitemName(str);
            //id去重
            samplingDetail.setSenceMonitemId(getDifference(samplingDetail.getSenceMonitemId(),
                    samplingDetail.getMonitemId()));
        }
        //名称项数量
        if (samplingDetail.getMonitemId() == null || samplingDetail.getMonitemId().equals("")) {
            sample_monitor_items.setLeftTextViewStr("监测项目(" + 0 + ")");
        } else {
            sample_monitor_items.setLeftTextViewStr("监测项目(" + samplingDetail.
                    getMonitemId().split(",").length + ")");
        }
        if (samplingDetail.getSenceMonitemId() == null || samplingDetail.getSenceMonitemId().equals("")) {
            sample_monitor.setLeftTextViewStr("现场监测(" + 0 + ")");
        } else {
            sample_monitor.setLeftTextViewStr("现场监测(" + samplingDetail.getSenceMonitemId().
                    split(",").length + ")");
        }
    }

    /**
     * 生成分瓶信息:只有监测项目才生成分瓶信息
     *
     * @return
     */
    private void generateBottleSplit() {
        String currentMonItemIds = samplingDetail.getMonitemId();
        if (oldItemId == null) oldItemId = "";
        //更新数据库所存的分瓶信息
//        if (oldMonitemIds != null && oldMonitemIds.size() != 0) {
//            SamplingFormStandDao dao = DBHelper.get().getSamplingFormStandDao();
//            for (SamplingFormStand item : oldMonitemIds) {
//                dao.deleteByKey(item.getId());
//            }
//        }
////        DBHelper.get().getSamplingFormStandDao().deleteAll();
        //相同 不用生成分瓶信息  不同则需要
        if (!oldItemId.equals(currentMonItemIds)) {
            List<String> monItemIds = Arrays.asList(currentMonItemIds.split(","));
            List<String> oldItemIdList = Arrays.asList(oldItemId.split(","));
            if (oldItemIdList != null && oldItemIdList.size() > 0) {
                for (String oldId : oldItemIdList) {
                    HelpUtil.deleteBottleByItemId(oldId, mSample);
                }
            }

            if (monItemIds != null && monItemIds.size() > 0) {
                for (String id : monItemIds)
                    HelpUtil.createAndUpdateBottle(id, mSample);
            }
        }


//            if (HelpUtil.isSamplingHasBottle(mSample.getId())) {
//                oldMonitemIds.clear();
//
//            } else {//如果不存在表示第一次增加
//                for (String itemId : monitemIds) {
//                    createAndUpdateBottle(itemId);
//                }
//            }

    }


    /**
     * 获去monItem 对应的 SamplingStantd
     *
     * @param monItem
     * @return
     */
    private SamplingStantd getRelateSamplingStantd(String monItem) {
        String tagId = mSample.getTagId();
        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().queryBuilder().where(SamplingStantdDao.Properties.TagId.eq(mSample.getTagId())).build().list();
        if (!CheckUtil.isEmpty(stantdsList)) {
            for (SamplingStantd samplingStantd : stantdsList) {
                List<String> monItems = samplingStantd.getMonItems();
                if (!CheckUtil.isEmpty(tagId) && !CheckUtil.isEmpty(samplingStantd.getTagId()) && tagId.equals(samplingStantd.getTagId()) && !CheckUtil.isEmpty(monItem) && !CheckUtil.isEmpty(monItems) && monItems.contains(monItem)) {
                    return samplingStantd;
                }
            }
        }
        return null;
    }


    /**
     * 判断分瓶信息是否存在
     *
     * @param itemId
     * @return
     */
    private SamplingFormStand bottleIsExists(String itemId) {
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).list();
        if (!CheckUtil.isEmpty(formStantdsList)) {
            for (SamplingFormStand formStand : formStantdsList) {
                if (formStand.getMonitemIds().contains(itemId)) {
                    return formStand;
                }
            }
        }
        return null;
    }


    private void setBottleAndContent() {
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).list();
        if (!CheckUtil.isEmpty(formStantdsList)) {
            mSample.setSamplingFormStandResults(formStantdsList);
        } else {
            mSample.setSamplingFormStandResults(new ArrayList<>());
        }

        List<SamplingContent> samplingDetails = DBHelper.get().getSamplingContentDao().queryBuilder().where(SamplingContentDao.Properties.SamplingId.eq(mSample.getId())).list();
        if (!CheckUtil.isEmpty(samplingDetails)) {
            mSample.setSamplingContentResults(samplingDetails);
        } else {
            mSample.setSamplingContentResults(new ArrayList<>());
        }

    }

    /**
     * 删除分瓶信息
     */
    private void deleteRelateBottle() {
        String currentMonitemIds = samplingDetail.getMonitemId();
        String[] monItemIds = currentMonitemIds.split(",");
        if (monItemIds.length > 0) {
            for (String itemId : monItemIds) {
                HelpUtil.deleteBottleByItemId(itemId, mSample);
            }
        }
    }


    /**
     * 设置默认监测项目
     */
    private void setDefaultMonitor() {
        //List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(new WhereCondition.StringCondition("1 GROUP BY "+ProjectDetialDao.Properties.MonItemName)).list();
        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().
                queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(mSample.getProjectId()),
                ProjectDetialDao.Properties.TagId.eq(mSample.getTagId())).list();
        List<String> itemIdList = new ArrayList<>();
        List<String> itemNameList = new ArrayList<>();
        if (!CheckUtil.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials) {
                if (!itemIdList.contains(projectDetial.getMonItemId())) {
                    itemIdList.add(projectDetial.getMonItemId());
                    itemNameList.add(projectDetial.getMonItemName());
                }
            }
        }

        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().
                queryBuilder().where(SamplingStantdDao.Properties.TagId.eq(mSample.getTagId())).list();
        List<String> xcNameList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();

        List<String> xcIdList = new ArrayList<>();
        List<String> idList = new ArrayList<>();


        for (int i = 0; i < itemNameList.size(); i++) {
            boolean isAdd = false;
            String itemName = itemNameList.get(i);
            for (SamplingStantd stantd : stantdsList) {
                if (stantd.getMonItems().contains(itemName) && stantd.getIsSenceAnalysis()) {
                    xcNameList.add(itemName);
                    xcIdList.add(itemIdList.get(i));
                    isAdd = true;
                    break;
                } else if (stantd.getMonItems().contains(itemName) && !stantd.getIsSenceAnalysis()) {
                    nameList.add(itemName);
                    idList.add(itemIdList.get(i));
                    isAdd = true;
                    break;
                }
            }

            if (!isAdd) {
                nameList.add(itemName);
                idList.add(itemIdList.get(i));
            }
        }

        samplingDetail.setMonitemName(HelpUtil.joinStringList(nameList));
        samplingDetail.setMonitemId(HelpUtil.joinStringList(idList));
        samplingDetail.setSenceMonitemName(HelpUtil.joinStringList(xcNameList));
        samplingDetail.setSenceMonitemId(HelpUtil.joinStringList(xcIdList));

        sample_monitor_items.setRightTextStr(samplingDetail.getMonitemName());
        sample_monitor.setRightTextStr(samplingDetail.getSenceMonitemName());

        int count = 0;
        if (!CheckUtil.isEmpty(samplingDetail.getMonitemId())) {
            count += samplingDetail.getMonitemId().split(",").length;
        }
        sample_monitor_items.setLeftTextViewStr("监测项目（" + count + "）");
        int resone = 0;
        if (!CheckUtil.isEmpty(samplingDetail.getSenceMonitemId())) {
            resone = getThelenth(samplingDetail.getSenceMonitemId().split(",")
                    , samplingDetail.getMonitemId().split(","));
            sample_monitor.setLeftTextViewStr("现场监测（" + resone + "）");
        }
        sample_monitor.setLeftTextViewStr("现场监测（" + resone + "）");

        // samplingDetail.setSamplingCount(HelpUtil.setSamplingCount(samplingDetail, mSample));

    }

    /**
     * 获取标准的采样规范
     *
     * @param isSenceAnalysis
     * @return
     */
    private String getStandMonitors(boolean isSenceAnalysis) {
        StringBuilder monItemsBuilder = new StringBuilder();
        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().queryBuilder().build().list();
        if (!CheckUtil.isEmpty(stantdsList)) {
            for (SamplingStantd stantd : stantdsList) {
                if (stantd.getIsSenceAnalysis() == isSenceAnalysis) {
                    List<String> monItems = stantd.getMonItems();
                    if (!CheckUtil.isEmpty(monItems)) {
                        for (String item : monItems) {
                            monItemsBuilder.append(item + ",");
                        }
                    }
                }

            }
        }
        if (monItemsBuilder.lastIndexOf(",") > 0) {
            monItemsBuilder.deleteCharAt(monItemsBuilder.lastIndexOf(","));
        }
        return monItemsBuilder.toString();
    }

    /**
     * 产生SamplingDetails
     */
    private void generateSamplingDetails(int fxcCount) {
        samplingDetail.setSamplingCount(fxcCount);
        //包括监测项目和现场监测项目
        //非现场监测的样品数量
        if (!CheckUtil.isEmpty(samplingDetail.getMonitemId())) {
            String[] monitemIds = samplingDetail.getMonitemId().split(",");
            if (!CheckUtil.isEmpty(monitemIds)) {
                for (String itemId : monitemIds) {
                    SamplingDetail detail = new SamplingDetail();
                    detail.setProjectId(samplingDetail.getProjectId());
                    detail.setId(UUID.randomUUID().toString());
                    detail.setSamplingId(samplingDetail.getSamplingId());
                    detail.setSampingCode(samplingDetail.getSampingCode());
                    detail.setFrequecyNo(samplingDetail.getFrequecyNo());
                    detail.setDescription(samplingDetail.getDescription());

                    detail.setSamplingType(samplingDetail.getSamplingType());
                    detail.setIsCompare(samplingDetail.getIsCompare());
                    detail.setIsAddPreserve(samplingDetail.getIsAddPreserve());
                    detail.setMonitemName(HelpUtil.getMonItemNameById(itemId, mSample));
                    detail.setMonitemId(itemId);
                    detail.setAddressName(samplingDetail.getAddressName());
                    detail.setAddresssId(samplingDetail.getAddresssId());
                    detail.setIsSenceAnalysis(false);
                    detail.setSamplingTime(samplingDetail.getSamplingTime());
                    detail.setOrderIndex(samplingDetail.getOrderIndex());
                    //设置剩余信息
                    detail.setSampleCollection(samplingDetail.getSampleCollection());
                    detail.setSampleAcceptance(samplingDetail.getSampleAcceptance());
                    detail.setPreservative(samplingDetail.getPreservative());
                    //计算SamplingCount为非现场监测的样品数量
                    detail.setSamplingCount(fxcCount);
                    DBHelper.get().getSamplingDetailDao().insert(detail);
                    mSample.getSamplingDetailResults().add(detail);
                }
            }
        }
        if (!CheckUtil.isEmpty(samplingDetail.getSenceMonitemId())) {
            String[] sendMonitemIds = samplingDetail.getSenceMonitemId().split(",");
            if (!CheckUtil.isEmpty(sendMonitemIds)) {
                for (String itemId : sendMonitemIds) {
                    SamplingDetail detail = new SamplingDetail();
                    detail.setProjectId(samplingDetail.getProjectId());
                    detail.setId(UUID.randomUUID().toString());
                    detail.setSamplingId(samplingDetail.getSamplingId());
                    detail.setSampingCode(samplingDetail.getSampingCode());
                    detail.setFrequecyNo(samplingDetail.getFrequecyNo());
                    detail.setDescription(samplingDetail.getDescription());
                    detail.setSamplingType(samplingDetail.getSamplingType());
                    detail.setIsCompare(samplingDetail.getIsCompare());
                    detail.setIsAddPreserve(samplingDetail.getIsAddPreserve());
                    detail.setMonitemName(HelpUtil.getMonItemNameById(itemId, mSample));
                    detail.setMonitemId(itemId);
                    detail.setAddressName(samplingDetail.getAddressName());
                    detail.setAddresssId(samplingDetail.getAddresssId());
                    detail.setIsSenceAnalysis(true);
                    detail.setSamplingTime(samplingDetail.getSamplingTime());
                    detail.setOrderIndex(samplingDetail.getOrderIndex());
                    //设置剩余信息
                    detail.setSampleCollection(samplingDetail.getSampleCollection());
                    detail.setSampleAcceptance(samplingDetail.getSampleAcceptance());
                    detail.setPreservative(samplingDetail.getPreservative());
                    //现场监测样品数量为0
                    detail.setSamplingCount(0);

                    DBHelper.get().getSamplingDetailDao().insert(detail);
                    mSample.getSamplingDetailResults().add(detail);
                }
            }
        }


    }


    /**
     * 调整监测项目和现场监测
     *
     * @param isSenceAnalysis
     */
    private void adjustMonitorItems(boolean isSenceAnalysis) {
        String monitemIdStr = samplingDetail.getMonitemId();
        String senceMonitemIdStr = samplingDetail.getSenceMonitemId();
        List<String> monitemIdList = new ArrayList<>();
        List<String> senceMonitemIdList = new ArrayList<>();
        if (!CheckUtil.isEmpty(monitemIdStr)) {
            String[] monitemIds = monitemIdStr.split(",");
            monitemIdList = new ArrayList<>(Arrays.asList(monitemIds));
        }
        if (!CheckUtil.isEmpty(senceMonitemIdStr)) {
            String[] sendMonitemIds = senceMonitemIdStr.split(",");
            senceMonitemIdList = new ArrayList<>(Arrays.asList(sendMonitemIds));
        }

        if (!isSenceAnalysis) {
            if (!CheckUtil.isEmpty(monitemIdList)) {
                if (!CheckUtil.isEmpty(senceMonitemIdList)) {
                    for (String monitemId : monitemIdList) {
                        senceMonitemIdList.remove(monitemId);
                    }
                }
                if (!CheckUtil.isEmpty(senceMonitemIdList)) {
                    List<String> senceMonitemNameList = new ArrayList<>();
                    for (String senceMonitemId : senceMonitemIdList) {
                        senceMonitemNameList.add(HelpUtil.getMonItemNameById(senceMonitemId, mSample));
                    }
                    samplingDetail.setSenceMonitemName(StringUtil.join(",", senceMonitemNameList));
                    samplingDetail.setSenceMonitemId(StringUtil.join(",", senceMonitemIdList));
                    String res = getDifference(samplingDetail.getSenceMonitemName()
                            , samplingDetail.getMonitemName());
                    sample_monitor.setRightTextStr(res);

                    int resone = getLenth(senceMonitemIdList
                            , samplingDetail.getMonitemId().split(","));
                    sample_monitor.setLeftTextViewStr("现场监测（" + resone + "）");

//                    sample_monitor_title.setText("现场监测("+senceMonitemIdList.size()+")");
                } else {
                    samplingDetail.setSenceMonitemName("");
                    samplingDetail.setSenceMonitemId("");
                    //空的，就不管了
                    sample_monitor.setRightTextStr(samplingDetail.getSenceMonitemName());
                    sample_monitor.setLeftTextViewStr("现场监测(0)");
                }
            }

        } else {
            if (!CheckUtil.isEmpty(senceMonitemIdList)) {
                if (!CheckUtil.isEmpty(monitemIdList)) {
                    for (String monitemId : senceMonitemIdList) {
                        monitemIdList.remove(monitemId);
                    }
                }
                if (!CheckUtil.isEmpty(monitemIdList)) {
                    List<String> monitemNameList = new ArrayList<>();
                    for (String monitemId : monitemIdList) {
                        monitemNameList.add(HelpUtil.getMonItemNameById(monitemId, mSample));
                    }
                    samplingDetail.setMonitemName(StringUtil.join(",", monitemNameList));
                    samplingDetail.setMonitemId(StringUtil.join(",", monitemIdList));
                    sample_monitor_items.setRightTextStr(samplingDetail.getMonitemName());
                    sample_monitor_items.setLeftTextViewStr("监测项目(" + monitemIdList.size() + ")");
                } else {
                    samplingDetail.setMonitemName("");
                    samplingDetail.setMonitemId("");
                    sample_monitor_items.setRightTextStr(samplingDetail.getMonitemName());
                    sample_monitor_items.setLeftTextViewStr("监测项目(0)");
                }
            }

        }

    }

    /**
     * 返回相似长度
     *
     * @param senceMonitemIdList
     * @param split
     * @return
     */
    private int getLenth(List<String> senceMonitemIdList, String[] split) {
        int count = 0;
        int len = Math.min(senceMonitemIdList.size(), split.length);
        List<String> one = Arrays.asList(split);
        for (int i = 0; i < len; i++) {
            if (!one.contains(senceMonitemIdList.get(i))) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取分瓶信息的瓶数
     *
     * @return
     */
    private int getBottleNumber() {
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).list();
        if (!CheckUtil.isEmpty(formStantdsList)) {
            return formStantdsList.size();
        } else {
            return 0;
        }
    }


    /**
     * 删除对应的SamplingDetailList
     */
    private void deleteSamplingDetailList() {
        //删除对应的SamplingDetail
        List<SamplingDetail> samplingDetailList = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(samplingDetail.getSamplingId()), SamplingDetailDao.Properties.SampingCode.eq(samplingDetail.getSampingCode()), SamplingDetailDao.Properties.SamplingType.eq(samplingDetail.getSamplingType())).build().list();
        if (!CheckUtil.isEmpty(samplingDetailList)) {
            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetailList);
            //WastewaterActivity.mSample.setSamplingDetailResults(new ArrayList<>());
            mSample.getSamplingDetailResults().removeAll(samplingDetailList);
        }
    }


    public void showLoadingDialog(String str, boolean isCanCanceled) {

        new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (dialog != null && dialog.isShowing()) {
                    setLoadingDialogText(str);
                    return;
                }

                View layout = getLayoutInflater().inflate(R.layout.dialog_loading, null);
                dialogTextView = layout.findViewById(R.id.tv_content);
                RelativeLayout rlDialog = layout.findViewById(R.id.rl_dialog);
                if (CheckUtil.isEmpty(str)) {
                    dialogTextView.setVisibility(View.GONE);
                } else {
                    rlDialog.setBackgroundResource(R.drawable.loading_bg);
                    dialogTextView.setVisibility(View.VISIBLE);
                    dialogTextView.setText(str);
                }
                if (dialog == null) {
                    dialog = new Dialog(getContext(), R.style.loadingDialog);
                }
                dialog.setContentView(layout);
                dialog.setCancelable(isCanCanceled);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                dialog.show();
            }
        }.sendEmptyMessage(0);

    }

    private void setLoadingDialogText(String str) {
        if (dialogTextView != null) {
            dialogTextView.setText(str);
        }
    }

    public void closeLoadingDialog() {
        new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (null != dialog && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
        }.sendEmptyMessage(0);

    }

    private void closeLoadingDialog_gai() {
        dialog.dismiss();
        dialog = null;
    }

}
