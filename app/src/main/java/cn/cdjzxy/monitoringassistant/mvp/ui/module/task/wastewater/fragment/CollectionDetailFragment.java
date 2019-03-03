package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import org.greenrobot.greendao.query.WhereCondition;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FsExtends;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingStantdDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.MonItemActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.ClickUtils;
import cn.cdjzxy.monitoringassistant.utils.Constants;
import cn.cdjzxy.monitoringassistant.utils.DateUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.HelpUtil;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;
import cn.cdjzxy.monitoringassistant.widgets.ViewClickProxy;

/**
 * 采集样品详情
 */

public class CollectionDetailFragment extends BaseFragment {
    @BindView(R.id.sample_code)
    TextView sample_code;
    @BindView(R.id.sample_frequency)
    TextView sample_frequency;
    @BindView(R.id.sample_quality)
    TextView sample_quality;
    @BindView(R.id.sample_monitor_items)
    TextView sample_monitor_items;
    @BindView(R.id.sample_monitor)
    TextView sample_monitor;
    @BindView(R.id.sample_add_preserve)
    CheckedTextView sample_add_preserve;
    @BindView(R.id.sample_compare_monitor)
    CheckedTextView sample_compare_monitor;
    @BindView(R.id.sample_mark)
    TextView sample_mark;
    @BindView(R.id.sample_monitor_items_title)
    TextView sample_monitor_items_title;
    @BindView(R.id.sample_monitor_title)
    TextView sample_monitor_title;
    @BindView(R.id.operate_layout)
    View operate_layout;

    Unbinder unbinder;

    private int fsListPosition;
    private Sampling mSample;
    SamplingContent samplingDetail;


    public CollectionDetailFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_collect_detail, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!CheckUtil.isNull(WastewaterActivity.mSample) && !WastewaterActivity.mSample.getIsCanEdit()){
            operate_layout.setVisibility(View.INVISIBLE);

        }else {
            operate_layout.setVisibility(View.VISIBLE);
        }

        sample_add_preserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_add_preserve.setChecked(!sample_add_preserve.isChecked());
                samplingDetail.setIsAddPreserve(sample_add_preserve.isChecked());
                if (samplingDetail.isAddPreserve()){
                    samplingDetail.setPreservative("是");
                }else {
                    samplingDetail.setPreservative("否");
                }

            }
        });

        sample_compare_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_compare_monitor.setChecked(!sample_compare_monitor.isChecked());
                samplingDetail.setIsCompare(sample_compare_monitor.isChecked());
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
            sample_code.setText("");
            sample_frequency.setText("");
            sample_quality.setText("");
            sample_monitor_items.setText("");
            sample_monitor.setText("");
            sample_mark.setText("");
            sample_monitor_items_title.setText("监测项目（0）");
            sample_monitor_title.setText("现场监测（0）");

            setSamplingDetail();
        }

    }

    private void setSamplingDetail() {
        mSample = WastewaterActivity.mSample;
        List<SamplingContent> samplingDetailResults = mSample.getSamplingContentResults();
        SharedPreferences collectListSettings = getActivity().getSharedPreferences("setting", 0);
        fsListPosition = collectListSettings.getInt("fsListPosition", -1);
        if (fsListPosition == -1) {
            samplingDetail = new SamplingContent();
            samplingDetail.setId(UUID.randomUUID().toString());
            sample_code.setText(HelpUtil.createSamplingCode(mSample));
            sample_frequency.setText(HelpUtil.createFrequency(mSample)+"");
            sample_quality.setText(Constants.SAMPLING_TYPE_PT);
            samplingDetail.setSamplingType(0);
            samplingDetail.setOrderIndex(HelpUtil.createOrderIndex(mSample));
            samplingDetail.setFrequecyNo(HelpUtil.createFrequency(mSample));
            //设置是否添加保存剂，是否对比监测
            samplingDetail.setPreservative("否");
            samplingDetail.setIsAddPreserve(false);
            samplingDetail.setIsCompare(false);
            //新增时要将监测项目和现场监测项目带过来
            setDefaultMonitor();
        } else {
            samplingDetail = samplingDetailResults.get(fsListPosition);
            sample_code.setText(samplingDetail.getSampingCode());
            sample_frequency.setText(samplingDetail.getFrequecyNo() + "");

            sample_monitor_items.setText(samplingDetail.getMonitemName());
            sample_monitor.setText(samplingDetail.getSenceMonitemName());

            //金蓉：平行数据或空白数据不能编辑现场监测
            if(samplingDetail.getSamplingType()==2 || samplingDetail.getSamplingType()==1) {
                sample_monitor.setEnabled(false);
            }

            if (!CheckUtil.isNull(samplingDetail.getPreservative()) && samplingDetail.getPreservative().equals("是")){
                sample_add_preserve.setChecked(true);
                samplingDetail.setIsAddPreserve(true);
                samplingDetail.setPreservative("是");
            }else {
                sample_add_preserve.setChecked(false);
                samplingDetail.setIsAddPreserve(false);
                samplingDetail.setPreservative("否");
            }

            sample_compare_monitor.setChecked(samplingDetail.getIsCompare());
            sample_mark.setText(samplingDetail.getDescription());

            if (!CheckUtil.isEmpty(samplingDetail.getMonitemId())){
                sample_monitor_items_title.setText("监测项目（"+samplingDetail.getMonitemId().split(",").length+"）");
            }

            if (!CheckUtil.isEmpty(samplingDetail.getSenceMonitemId())){
                sample_monitor_title.setText("现场监测（"+samplingDetail.getSenceMonitemId().split(",").length+"）");
            }

            if (samplingDetail.getSamplingType()==0){
                sample_quality.setText(Constants.SAMPLING_TYPE_PT);
            }else if (samplingDetail.getSamplingType()==1){
                sample_quality.setText(Constants.SAMPLING_TYPE_PX);
            }else if (samplingDetail.getSamplingType()==2){
                sample_quality.setText(Constants.SAMPLING_TYPE_KB);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_back, R.id.sample_monitor_items, R.id.sample_monitor, R.id.btn_delete, R.id.btn_save})
    public void onClick(View view) {
        if (!ClickUtils.canClick()){
            return;
        }
        switch (view.getId()) {
            case R.id.btn_back:
                EventBus.getDefault().post(1, EventBusTags.TAG_WASTEWATER_COLLECTION);
                break;
            case R.id.sample_monitor_items:
                showMonitorItems();
                break;
            case R.id.sample_monitor:
                showAddressItems();
                break;
            case R.id.btn_delete:
                operateDelete();
                break;
            case R.id.btn_save:
                operateSave();
                break;
            default:
                break;
        }
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
                        //删除SamplingContent
                        SamplingContent currentDetail = mSample.getSamplingContentResults().get(fsListPosition);
                        SamplingContent samplingDetail = DBHelper.get().getSamplingContentDao().queryBuilder().where(SamplingContentDao.Properties.Id.eq(currentDetail.getId())).unique();
                        if (!CheckUtil.isNull(samplingDetail)) {
                            DBHelper.get().getSamplingContentDao().delete(samplingDetail);
                        }
                        mSample.getSamplingContentResults().remove(fsListPosition);
                        //删除对应的SamplingDetail
                        List<SamplingDetail> samplingDetailList = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(currentDetail.getSamplingId()),SamplingDetailDao.Properties.SampingCode.eq(currentDetail.getSampingCode()),SamplingDetailDao.Properties.SamplingType.eq(currentDetail.getSamplingType())).build().list();
                        if (!CheckUtil.isEmpty(samplingDetailList)){
                            DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetailList);
                            WastewaterActivity.mSample.setSamplingDetailResults(new ArrayList<>());
                        }

                        //删除分瓶信息
                        deleteRelateBottle();
                        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).list();
                        if (!CheckUtil.isEmpty(formStantdsList)){
                            WastewaterActivity.mSample.setSamplingFormStandResults(formStantdsList);
                        }else {
                            WastewaterActivity.mSample.setSamplingFormStandResults(new ArrayList<>());
                        }

                        mSample.setIsFinish(HelpUtil.isSamplingFinish(mSample));
                        mSample.setStatusName(HelpUtil.isSamplingFinish(mSample) ? "已完成" : "进行中");
                        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSample.getId())).unique();
                        if (CheckUtil.isNull(sampling)) {
                            DBHelper.get().getSamplingDao().insert(mSample);
                        } else {
                            DBHelper.get().getSamplingDao().update(mSample);
                        }

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
     */
    private void operateSave() {
        if (isSaveChecked()) {
            samplingDetail.setProjectId(mSample.getProjectId());
            samplingDetail.setSamplingId(mSample.getId());
            samplingDetail.setSampingCode(sample_code.getText().toString());
            samplingDetail.setFrequecyNo(Integer.parseInt(sample_frequency.getText().toString()));
            samplingDetail.setDescription(sample_mark.getText().toString());
            samplingDetail.setSamplingTime(DateUtils.getWholeDate());
            //设置样品采集和样品验收
            samplingDetail.setSampleCollection("");
            samplingDetail.setSampleAcceptance("");

            //新增样品采集到采样单中
            if (fsListPosition == -1) {
                if (mSample.getSamplingContentResults() == null) {
                    mSample.setSamplingContentResults(new ArrayList<SamplingContent>());
                }
                List<SamplingContent> samplingDetailResults = mSample.getSamplingContentResults();
                samplingDetailResults.add(samplingDetail);
                mSample.setSamplingContentResults(samplingDetailResults);
            }

            //设置采样单完结状态以及更新采样单
            mSample.setIsFinish(HelpUtil.isSamplingFinish(mSample));
            mSample.setStatusName(HelpUtil.isSamplingFinish(mSample) ? "已完成" : "进行中");
            Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSample.getId())).unique();
            if (CheckUtil.isNull(sampling)) {
                //mSample.setId( UUID.randomUUID().toString());
                DBHelper.get().getSamplingDao().insert(mSample);
            } else {
                DBHelper.get().getSamplingDao().update(mSample);
            }

            //生成分瓶信息
            generateBottleSplit();

            //删除之前生成的SamplingDetail
            List<SamplingDetail> samplingDetailList = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(samplingDetail.getSamplingId()),SamplingDetailDao.Properties.SampingCode.eq(samplingDetail.getSampingCode()),SamplingDetailDao.Properties.SamplingType.eq(samplingDetail.getSamplingType())).build().list();
            if (!CheckUtil.isEmpty(samplingDetailList)){
                DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetailList);
            }
            //生成新的SamplingDetail
            generateSamplingDetails();
            if (fsListPosition == -1) {
                //samplingDetail.setId(UUID.randomUUID().toString());
                //samplingDetail.setSamplingType(0);
                //DBHelper.get().getSamplingContentDao().insert(samplingDetail);
                DBHelper.get().getSamplingContentDao().insertOrReplace(samplingDetail);
            }else {
                DBHelper.get().getSamplingContentDao().update(samplingDetail);
            }

            //设置信息
            setBottleAndContent();

            EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);
            EventBus.getDefault().post(1, EventBusTags.TAG_WASTEWATER_COLLECTION);
            ArtUtils.makeText(getContext(), "保存成功");
        }
    }


    /**
     * 保存必须校验
     * @return
     */
    private boolean isSaveChecked() {
        if (TextUtils.isEmpty(sample_monitor_items.getText().toString())) {
            ArtUtils.makeText(getContext(), "请选择监测项目");
            return false;
        }
        return true;
    }

    /**
     * 选择监测项目
     */
    private void showMonitorItems(){
        Intent intent = new Intent(getContext(), MonItemActivity.class);
        intent.putExtra("tagId", mSample.getParentTagId());
        if (!CheckUtil.isEmpty(samplingDetail.getMonitemId())){
            intent.putExtra("selectItems", samplingDetail.getMonitemId());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!CheckUtil.isEmpty(data.getStringExtra("MonItemId")) && !CheckUtil.isEmpty(data.getStringExtra("MonItemName"))) {
                        samplingDetail.setMonitemName(data.getStringExtra("MonItemName"));
                        samplingDetail.setMonitemId(data.getStringExtra("MonItemId"));
                        sample_monitor_items.setText(samplingDetail.getMonitemName());
                        if (!CheckUtil.isEmpty(data.getStringExtra("MonItemId"))){
                            sample_monitor_items_title.setText("监测项目("+data.getStringExtra("MonItemId").split(",").length+")");
                        }
                        //adjustMonitorItems(false);
                    }
                }
            }
        });
    }

    /**
     * 选择现场监测项目
     */
    private void showAddressItems(){
        Intent intent = new Intent(getContext(), MonItemActivity.class);
        intent.putExtra("tagId", mSample.getParentTagId());
        if (!CheckUtil.isEmpty(samplingDetail.getSenceMonitemId())){
            intent.putExtra("selectItems", samplingDetail.getSenceMonitemId());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    if (!CheckUtil.isEmpty(data.getStringExtra("MonItemId")) && !CheckUtil.isEmpty(data.getStringExtra("MonItemName"))) {
                        samplingDetail.setSenceMonitemName(data.getStringExtra("MonItemName"));
                        samplingDetail.setSenceMonitemId(data.getStringExtra("MonItemId"));
                        sample_monitor.setText(samplingDetail.getSenceMonitemName());
                        if (!CheckUtil.isEmpty(data.getStringExtra("MonItemId"))){
                            sample_monitor_title.setText("现场监测("+data.getStringExtra("MonItemId").split(",").length+")");
                        }
                    }
                    //adjustMonitorItems(true);
                }
            }
        });
    }

    /**
     * 生成分瓶信息:只有监测项目才生成分瓶信息
     * @return
     */
    private void generateBottleSplit(){
        String currentMonitemIds=samplingDetail.getMonitemId();
        String[] monitemIds=currentMonitemIds.split(",");
        if (monitemIds!=null && monitemIds.length>0){
            for (String itemId:monitemIds){
                SamplingFormStand samplingFormStand=bottleIsExists(itemId);
                //如果已经存在，则不操作
                if (CheckUtil.isNull(samplingFormStand)){
                    String itemName=HelpUtil.getMonItemNameById(itemId,mSample);
                    //如果itemName为空则不操作
                    if (!CheckUtil.isEmpty(itemName)){
                        //获取规则
                        SamplingStantd samplingStantd=getRelateSamplingStantd(itemId);
                        if (!CheckUtil.isNull(samplingStantd)){
                            SamplingFormStand bottleSplit = new SamplingFormStand();
                            bottleSplit.setContainer(samplingStantd.getContaner());
                            bottleSplit.setAnalysisSite("");
                            bottleSplit.setSaveTimes("");
                            bottleSplit.setMonitemIds(itemId);
                            bottleSplit.setId(UUID.randomUUID().toString());
                            bottleSplit.setMonitemName(itemName);
                            List<String> items=new ArrayList<>();
                            items.add(itemId);
                            bottleSplit.setMonItems(items);
                            bottleSplit.setSamplingAmount(samplingStantd.getCapacity());
                            bottleSplit.setStandNo(generateNewIndex());
                            bottleSplit.setIndex(generateNewIndex());
                            bottleSplit.setSamplingId(mSample.getId());
                            bottleSplit.setUpdateTime(DateUtils.getWholeDate());
                            bottleSplit.setSaveMehtod(samplingStantd.getSaveDescription());
                            DBHelper.get().getSamplingFormStandDao().insertInTx(bottleSplit);

                        }else {
                            SamplingFormStand bottleSplit = new SamplingFormStand();
                            bottleSplit.setContainer("");
                            bottleSplit.setAnalysisSite("");
                            bottleSplit.setSaveTimes("");
                            bottleSplit.setMonitemIds(itemId);
                            bottleSplit.setId(UUID.randomUUID().toString());
                            bottleSplit.setMonitemName(itemName);
                            List<String> items=new ArrayList<>();
                            items.add(itemId);
                            bottleSplit.setMonItems(items);
                            bottleSplit.setSamplingAmount("");
                            bottleSplit.setStandNo(generateNewIndex());
                            bottleSplit.setIndex(generateNewIndex());
                            bottleSplit.setSamplingId(mSample.getId());
                            bottleSplit.setUpdateTime(DateUtils.getWholeDate());
                            bottleSplit.setSaveMehtod("");
                            DBHelper.get().getSamplingFormStandDao().insertInTx(bottleSplit);
                        }
                    }
                }
            }

        }
    }

    /**
     * 获去monItem 对应的 SamplingStantd
     * @param monItem
     * @return
     */
    private SamplingStantd getRelateSamplingStantd(String monItem){
        String tagId=mSample.getTagId();
        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().queryBuilder().where(SamplingStantdDao.Properties.TagId.eq(mSample.getTagId())).build().list();
        if (!CheckUtil.isEmpty(stantdsList)){
            for (SamplingStantd samplingStantd:stantdsList){
                List<String> monItems=samplingStantd.getMonItems();
                if (!CheckUtil.isEmpty(tagId) && !CheckUtil.isEmpty(samplingStantd.getTagId()) && tagId.equals(samplingStantd.getTagId()) && !CheckUtil.isEmpty(monItem ) && !CheckUtil.isEmpty(monItems) && monItems.contains(monItem)){
                    return samplingStantd;
                }
            }
        }
        return null;
    }


    /**
     * 判断分瓶信息是否存在
     * @param itemId
     * @return
     */
    private SamplingFormStand bottleIsExists(String itemId){
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).list();
        if (!CheckUtil.isEmpty(formStantdsList)){
            for (SamplingFormStand formStand:formStantdsList){
                if (formStand.getMonitemIds().contains(itemId)){
                    return formStand;
                }
            }
        }
        return null;
    }

    /**
     * 产生新的index
     * @return
     */
    private int generateNewIndex(){
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).orderAsc(SamplingFormStandDao.Properties.Index).list();
        if (!CheckUtil.isEmpty(formStantdsList)){
            return formStantdsList.get(formStantdsList.size()-1).getIndex()+1;
        }
        return 1;
    }

    private void setBottleAndContent(){
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).list();
        if (!CheckUtil.isEmpty(formStantdsList)){
            WastewaterActivity.mSample.setSamplingFormStandResults(formStantdsList);
        }else {
            WastewaterActivity.mSample.setSamplingFormStandResults(new ArrayList<>());
        }

        List<SamplingContent> samplingDetails = DBHelper.get().getSamplingContentDao().queryBuilder().where(SamplingContentDao.Properties.SamplingId.eq(mSample.getId())).list();
        if (!CheckUtil.isEmpty(samplingDetails)) {
            WastewaterActivity.mSample.setSamplingContentResults(samplingDetails);
        }else {
            WastewaterActivity.mSample.setSamplingContentResults(new ArrayList<>());
        }

    }

    /**
     * 删除分瓶信息
     */
    private void deleteRelateBottle(){
        String currentMonitemIds=samplingDetail.getMonitemId();
        String[] monitemIds=currentMonitemIds.split(",");
        if (monitemIds!=null && monitemIds.length>0){
            for (String itemId:monitemIds){
                List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).orderAsc(SamplingFormStandDao.Properties.Index).list();
                if (!CheckUtil.isEmpty(formStantdsList)){
                    for (SamplingFormStand formStand:formStantdsList){
                        if (!CheckUtil.isEmpty(formStand.getMonitemIds()) && !CheckUtil.isEmpty(itemId)){
                            String[] ids=formStand.getMonitemIds().split(",");
                            if (!CheckUtil.isEmpty(ids) && ids.length>0 && formStand.getMonitemIds().contains(itemId)){
                                if (ids.length==1){
                                    DBHelper.get().getSamplingFormStandDao().deleteInTx(formStand);
                                    break;
                                }else {
                                    StringBuilder  MonItemName = new StringBuilder("");
                                    StringBuilder  MonItemId = new StringBuilder("");
                                    List<String> MonItemNameList=formStand.getMonItems();
                                    if (CheckUtil.isEmpty(MonItemNameList)){
                                        MonItemNameList=new ArrayList<>();
                                    }else {
                                        MonItemNameList=new ArrayList<>(MonItemNameList);
                                    }
                                    for (String id:ids){
                                        if (!id.equals(itemId)){
                                            MonItemId.append(id + ",");
                                            String itemName=HelpUtil.getMonItemNameById(id,mSample);;
                                            if (CheckUtil.isEmpty(itemName)){
                                                MonItemId.append(" " + ",");
                                            }else {
                                                MonItemId.append(itemName + ",");
                                            }
                                        }else {
                                            MonItemNameList.remove(id);
                                        }

                                    }

                                    if (MonItemName.lastIndexOf(",") > 0) {
                                        MonItemName.deleteCharAt(MonItemName.lastIndexOf(","));
                                    }

                                    if (MonItemId.lastIndexOf(",") > 0) {
                                        MonItemId.deleteCharAt(MonItemId.lastIndexOf(","));
                                    }
                                    formStand.setMonItems(MonItemNameList);
                                    formStand.setMonitemName(MonItemName.toString());
                                    formStand.setMonitemIds(MonItemId.toString());
                                    DBHelper.get().getSamplingFormStandDao().updateInTx(formStand);
                                    break;
                                }
                            }
                        }
                    }

                }

            }

        }
    }

    /**
     * 设置默认监测项目
     */
    private void setDefaultMonitor(){
        //List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(new WhereCondition.StringCondition("1 GROUP BY "+ProjectDetialDao.Properties.MonItemName)).list();
        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(mSample.getProjectId()),ProjectDetialDao.Properties.TagId.eq(mSample.getTagId())).list();
        List<String> itemIdList=new ArrayList<>();
        List<String> itemNameList=new ArrayList<>();
        if (!CheckUtil.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials){
                if (!itemIdList.contains(projectDetial.getMonItemId())){
                    itemIdList.add(projectDetial.getMonItemId());
                    itemNameList.add(projectDetial.getMonItemName());
                }
            }
        }

        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().queryBuilder().where(SamplingStantdDao.Properties.TagId.eq(mSample.getTagId())).list();
        List<String> xcNameList=new ArrayList<>();
        List<String> nameList=new ArrayList<>();

        List<String> xcIdList=new ArrayList<>();
        List<String> idList=new ArrayList<>();


        for (int i=0;i<itemNameList.size();i++){
            boolean isAdd=false;
            String itemName=itemNameList.get(i);
            for (SamplingStantd stantd:stantdsList){
                if (stantd.getMonItems().contains(itemName) && stantd.getIsSenceAnalysis()){
                    xcNameList.add(itemName);
                    xcIdList.add(itemIdList.get(i));
                    isAdd=true;
                    break;
                }else if (stantd.getMonItems().contains(itemName) && !stantd.getIsSenceAnalysis()){
                    nameList.add(itemName);
                    idList.add(itemIdList.get(i));
                    isAdd=true;
                    break;
                }
            }

            if (!isAdd){
                nameList.add(itemName);
                idList.add(itemIdList.get(i));
            }
        }

        samplingDetail.setMonitemName(HelpUtil.joinStringList(nameList));
        samplingDetail.setMonitemId(HelpUtil.joinStringList(idList));
        samplingDetail.setSenceMonitemName(HelpUtil.joinStringList(xcNameList));
        samplingDetail.setSenceMonitemId(HelpUtil.joinStringList(xcIdList));

        sample_monitor_items.setText(samplingDetail.getMonitemName());
        sample_monitor.setText(samplingDetail.getSenceMonitemName());

        samplingDetail.setSamplingTime(DateUtils.getWholeDate());
        int count=0;
        if (!CheckUtil.isEmpty(samplingDetail.getMonitemId())){
            count+=samplingDetail.getMonitemId().split(",").length;
            sample_monitor_items_title.setText("监测项目（"+count+"）");
        }

        if (!CheckUtil.isEmpty(samplingDetail.getSenceMonitemId())){
            count+=samplingDetail.getSenceMonitemId().split(",").length;
            sample_monitor_title.setText("现场监测（"+samplingDetail.getSenceMonitemId().split(",").length+"）");
        }

        samplingDetail.setSamplingCount(countSamplingCount());

        //adjustMonitorItems(true);

    }

    /**
     * 获取标准的采样规范
     * @param isSenceAnalysis
     * @return
     */
    private String getStandMonitors(boolean isSenceAnalysis){
        StringBuilder monItemsBuilder = new StringBuilder("");
        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().queryBuilder().build().list();
        if (!CheckUtil.isEmpty(stantdsList)){
            for (SamplingStantd stantd:stantdsList){
                if (stantd.getIsSenceAnalysis()==isSenceAnalysis){
                    List<String> monItems=stantd.getMonItems();
                    if (!CheckUtil.isEmpty(monItems)){
                        for (String item:monItems){
                            monItemsBuilder.append(item+",");
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
    private void generateSamplingDetails(){
        //包括监测项目和现场监测项目
        //非现场监测的样品数量
        int fxcCount=countSamplingCount();
        if (!CheckUtil.isEmpty(samplingDetail.getMonitemId())){
            String[] monitemIds=samplingDetail.getMonitemId().split(",");
            if (!CheckUtil.isEmpty(monitemIds)){
                for (String itemId:monitemIds){
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
                    detail.setMonitemName(HelpUtil.getMonItemNameById(itemId,mSample));
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
                    WastewaterActivity.mSample.getSamplingDetailResults().add(detail);
                }
            }
        }


        if (!CheckUtil.isEmpty(samplingDetail.getSenceMonitemId())){
            String[] sendMonitemIds=samplingDetail.getSenceMonitemId().split(",");
            if (!CheckUtil.isEmpty(sendMonitemIds)){
                for (String itemId:sendMonitemIds){
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
                    detail.setMonitemName(HelpUtil.getMonItemNameById(itemId,mSample));
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
                    WastewaterActivity.mSample.getSamplingDetailResults().add(detail);
                }
            }
        }

        samplingDetail.setSamplingCount(fxcCount);
    }


    /**
     * 调整监测项目和现场监测
     * @param isSenceAnalysis
     */
    private void adjustMonitorItems(boolean isSenceAnalysis){
        String monitemIdStr=samplingDetail.getMonitemId();
        String senceMonitemIdStr=samplingDetail.getSenceMonitemId();
        List<String> monitemIdList=new ArrayList<>();
        List<String> senceMonitemIdList=new ArrayList<>();
        if (!CheckUtil.isEmpty(monitemIdStr)){
            String[] monitemIds=monitemIdStr.split(",");
            monitemIdList=new ArrayList<>(Arrays.asList(monitemIds));
        }
        if (!CheckUtil.isEmpty(senceMonitemIdStr)){
            String[] sendMonitemIds=senceMonitemIdStr.split(",");
            senceMonitemIdList=new ArrayList<>(Arrays.asList(sendMonitemIds));
        }

        if (!isSenceAnalysis){
            if (!CheckUtil.isEmpty(monitemIdList)){
                if (!CheckUtil.isEmpty(senceMonitemIdList)){
                    for (String monitemId:monitemIdList){
                        if (senceMonitemIdList.contains(monitemId)){
                            senceMonitemIdList.remove(monitemId);
                        }
                    }
                }
                if (!CheckUtil.isEmpty(senceMonitemIdList)){
                    List<String> senceMonitemNameList=new ArrayList<>();
                    for (String senceMonitemId:senceMonitemIdList){
                        senceMonitemNameList.add(HelpUtil.getMonItemNameById(senceMonitemId,mSample));
                    }
                    samplingDetail.setSenceMonitemName(StringUtil.join(",",senceMonitemNameList));
                    samplingDetail.setSenceMonitemId(StringUtil.join(",",senceMonitemIdList));
                    sample_monitor.setText(samplingDetail.getSenceMonitemName());
                    sample_monitor_title.setText("现场监测("+senceMonitemIdList.size()+")");
                }else {
                    samplingDetail.setSenceMonitemName("");
                    samplingDetail.setSenceMonitemId("");
                    sample_monitor.setText(samplingDetail.getSenceMonitemName());
                    sample_monitor_title.setText("现场监测(0)");
                }
            }

        }else {
            if (!CheckUtil.isEmpty(senceMonitemIdList)){
                if (!CheckUtil.isEmpty(monitemIdList)){
                    for (String monitemId:senceMonitemIdList){
                        if (monitemIdList.contains(monitemId)){
                            monitemIdList.remove(monitemId);
                        }
                    }
                }
                if (!CheckUtil.isEmpty(monitemIdList)){
                    List<String> monitemNameList=new ArrayList<>();
                    for (String monitemId:monitemIdList){
                        monitemNameList.add(HelpUtil.getMonItemNameById(monitemId,mSample));
                    }
                    samplingDetail.setMonitemName(StringUtil.join(",",monitemNameList));
                    samplingDetail.setMonitemId(StringUtil.join(",",monitemIdList));
                    sample_monitor_items.setText(samplingDetail.getMonitemName());
                    sample_monitor_items_title.setText("监测项目("+monitemIdList.size()+")");
                }else {
                    samplingDetail.setMonitemName("");
                    samplingDetail.setMonitemId("");
                    sample_monitor_items.setText(samplingDetail.getMonitemName());
                    sample_monitor_items_title.setText("监测项目(0)");
                }
            }

        }

    }

    /**
     * 获取分瓶信息的瓶数
     * @return
     */
    private int getBottleNumber(){
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).list();
        if (!CheckUtil.isEmpty(formStantdsList)){
            return formStantdsList.size();
        }else {
            return 0;
        }
    }

    /*
    private SamplingStantd getSamplingStantdSense(String monItemsName){
        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().queryBuilder().where(SamplingStantdDao.Properties.TagId.eq(mSample.getTagId())).list();
        SamplingStantd samplingStantd=null;
        for (SamplingStantd stantd:stantdsList){
            if (stantd.getMonItems().contains(monItemsName) && stantd.getIsSenceAnalysis()){
                samplingStantd=stantd;
                break;
            }
        }
        return samplingStantd;
    }

    private SamplingStantd getSamplingStantdNotSense(String monItemsName){
        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().queryBuilder().where(SamplingStantdDao.Properties.TagId.eq(mSample.getTagId())).list();
        SamplingStantd samplingStantd=null;
        for (SamplingStantd stantd:stantdsList){
            if (stantd.getMonItems().contains(monItemsName) && !stantd.getIsSenceAnalysis()){
                samplingStantd=stantd;
                break;
            }
        }
        return samplingStantd;
    }

    private SamplingStantd getSamplingStantdNotExistSense(String monItemsName){
        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().queryBuilder().where(SamplingStantdDao.Properties.TagId.eq(mSample.getTagId())).list();
        SamplingStantd samplingStantd=null;
        for (SamplingStantd stantd:stantdsList){
            if (!stantd.getMonItems().contains(monItemsName)){
                samplingStantd=stantd;
                break;
            }
        }
        return samplingStantd;
    }
    */


    /**
     * 统计样品数量
     * @return
     */
    private int countSamplingCount(){
        int count=0;
        String monitemIdStr=samplingDetail.getMonitemId();
        List<SamplingStantd> groupStandList=new ArrayList<>();
        if (!CheckUtil.isEmpty(monitemIdStr)){
            String[] idsArray=monitemIdStr.split(",");
            if (!CheckUtil.isEmpty(idsArray)){
                for (String itemId:idsArray){
                    String itemName=HelpUtil.getMonItemNameById(itemId,mSample);
                    SamplingStantd samplingStantd=getSamplingStantdByMonItem(itemName);
                    if (!CheckUtil.isNull(samplingStantd)){
                        if (!groupStandList.contains(samplingStantd)){
                            groupStandList.add(samplingStantd);
                        }
                    }else {
                        count +=1;
                    }
                }

            }
        }
        count +=groupStandList.size();
        return count;
    }

    /**
     * monitemName 对应的 SamplingStantd
     * @param monitemName
     * @return
     */
    private SamplingStantd getSamplingStantdByMonItem(String monitemName){
        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().queryBuilder().where(SamplingStantdDao.Properties.TagId.eq(mSample.getTagId())).list();
        if (!CheckUtil.isEmpty(stantdsList)){
            for (SamplingStantd samplingStantd:stantdsList){
                List<String> monItemsList=samplingStantd.getMonItems();
                if (!CheckUtil.isEmpty(monitemName ) && !CheckUtil.isEmpty(monItemsList) && monItemsList.contains(monitemName)){
                    return samplingStantd;
                }
            }
        }
        return null;
    }

}
