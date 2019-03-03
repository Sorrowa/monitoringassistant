package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.LabelInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SealInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WasteWaterCollectAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.LabelPrintActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.HelpUtil;

/**
 * 样品收集
 */

public class CollectionFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_add_parallel)
    TextView tvAddParallel;
    @BindView(R.id.btn_add_parallel)
    RelativeLayout btnAddParallel;
    @BindView(R.id.tv_add_blank)
    TextView tvAddBlank;
    @BindView(R.id.btn_add_blank)
    RelativeLayout btnAddBlank;
    @BindView(R.id.tv_print_label)
    TextView tvPrintLabel;
    @BindView(R.id.btn_print_label)
    RelativeLayout btnPrintLabel;
    @BindView(R.id.btn_add_new)
    RelativeLayout btn_add_new;
    @BindView(R.id.operate_layout)
    View operate_layout;


    private WasteWaterCollectAdapter mWasteWaterCollectAdapter;
    private SharedPreferences collectListSettings;
    private SharedPreferences.Editor editor;
    private SamplingContent selectSamplingDetail;

    public CollectionFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_collect, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        if (!CheckUtil.isNull(WastewaterActivity.mSample) && !WastewaterActivity.mSample.getIsCanEdit()){
            //operate_layout.setVisibility(View.INVISIBLE);
            btnAddParallel.setEnabled(false);
            btnAddBlank.setEnabled(false);
            btn_add_new.setEnabled(false);
            btnPrintLabel.setEnabled(true);
            btnAddParallel.setAlpha(0.5f);
            btnAddBlank.setAlpha(0.5f);
            btn_add_new.setAlpha(0.5f);
        }else {
            //operate_layout.setVisibility(View.VISIBLE);
            btnAddParallel.setEnabled(true);
            btnAddBlank.setEnabled(true);
            btn_add_new.setEnabled(true);
            btnPrintLabel.setEnabled(true);
            btnAddParallel.setAlpha(1f);
            btnAddBlank.setAlpha(1f);
            btn_add_new.setAlpha(1f);
        }
        initRecyclerViewData();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        collectListSettings = getActivity().getSharedPreferences("setting", 0);
        editor = collectListSettings.edit();
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
            if (mWasteWaterCollectAdapter != null) {
                mWasteWaterCollectAdapter.refreshInfos(WastewaterActivity.mSample.getSamplingContentResults());
            } else {
                initRecyclerViewData();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.btn_add_parallel, R.id.btn_add_blank, R.id.btn_print_label, R.id.btn_add_new})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_parallel://添加平行(需要选择普通样)
                if (CheckUtil.isNull(selectSamplingDetail)) {
                    ArtUtils.makeText(getContext(), "请先选择一项样品");
                    return;
                }
                if (selectSamplingDetail.getSamplingType() != 0) {
                    ArtUtils.makeText(getContext(), "请选择普通样品");
                    return;
                }
                if (isPxSampleAdded(selectSamplingDetail)) {
                    ArtUtils.makeText(getContext(), "该普通样下面已经添加平行样，请另外选择");
                    return;
                }
                addTheSameSample();
                break;
            case R.id.btn_add_blank://添加空白(不用选择普通样品)
                addBlankSample();
                break;
            case R.id.btn_print_label:
                Gson gson = new Gson();
                //构建标签数据
                String labelStr = gson.toJson(buildPrintLabelList(WastewaterActivity.mSample));
                //构建封条数据
                String sealStr = gson.toJson(buildSealInfo(WastewaterActivity.mProject));

                Intent intent = new Intent(getContext(), LabelPrintActivity.class);
                intent.putExtra(LabelPrintActivity.LABEL_JSON_DATA, labelStr);
                intent.putExtra(LabelPrintActivity.SEAL_JSON_DATA, sealStr);
                ArtUtils.startActivity(intent);
                break;
            case R.id.btn_add_new://添加样品
                if (TextUtils.isEmpty(WastewaterActivity.mSample.getAddressId())) {
                    ArtUtils.makeText(getContext(), "请先选择采样点位");
                    return;
                }
                editor.putInt("fsListPosition", -1);
                editor.commit();
                EventBus.getDefault().post(3, EventBusTags.TAG_WASTEWATER_COLLECTION);
                break;
            default:
                break;
        }
    }

    private void initRecyclerViewData() {
        if (WastewaterActivity.mSample.getSamplingContentResults() == null) {
            return;
        }
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mWasteWaterCollectAdapter = new WasteWaterCollectAdapter(WastewaterActivity.mSample.getSamplingContentResults(), new WasteWaterCollectAdapter.OnWasteWaterCollectListener() {
            @Override
            public void onSelected(View view, int position, boolean isSelected) {
                changSelectState(position, isSelected);
            }
        });
        mWasteWaterCollectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                EventBus.getDefault().post(3, EventBusTags.TAG_WASTEWATER_COLLECTION);
                editor.putInt("fsListPosition", position);
                editor.commit();
            }
        });
        recyclerview.setAdapter(mWasteWaterCollectAdapter);
    }

    /**
     * 构建打印的标签信息列表
     *
     * @return
     */
    private ArrayList<LabelInfo> buildPrintLabelList(Sampling sampling) {
        ArrayList<LabelInfo> result = new ArrayList<>();

        //组装标签信息\
        for (SamplingContent item : sampling.getSamplingContentResults()) {
            LabelInfo info = new LabelInfo();
            info.setTaskName(sampling.getProjectName());
            info.setNumber(sampling.getSamplingNo());
            info.setFrequecyNo("频次：" + item.getFrequecyNo());
            info.setType(WastewaterActivity.mSample.getTagName());//项目类型取样品性质
            info.setMonitemName(item.getMonitemName());//监测项目
            info.setSampingCode(item.getSampingCode());//样品编码
            info.setCb1("交接");
            info.setCb2("分析");
            info.setQrCode(item.getSampingCode());//二维码为样品编码

            //根据样品的监测项目获取对应的分瓶信息
            SamplingFormStand samplingFormStand = getSamplingFormStand(sampling, item.getMonitemName());
            if (samplingFormStand != null) {
                //保存方法
                info.setRemark(samplingFormStand.getPreservative());
            }

            result.add(info);
        }

        return result;
    }

    /**
     * 获取监测项目对应的分瓶信息
     *
     * @param sampling
     * @param monitemName
     * @return
     */
    private SamplingFormStand getSamplingFormStand(Sampling sampling, String monitemName) {
        if (TextUtils.isEmpty(monitemName)) {
            return null;
        }

        for (SamplingFormStand item : sampling.getSamplingFormStandResults()) {
            if (monitemName.equals(item.getMonitemName())) {
                return item;
            }
        }
        return null;
    }

    /**
     * 构造封条信息
     *
     * @return
     */
    private SealInfo buildSealInfo(Project project) {

        StringBuilder points = new StringBuilder("");

        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(project.getId())).list();
        if (!CheckUtil.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials) {
                if (!points.toString().contains(projectDetial.getAddress())) {
                    if (points.length() > 0) {
                        points.append(",");
                    }
                    points.append(projectDetial.getAddress());
                }
            }
        }

        SealInfo result = new SealInfo();
        result.setTitle("新都区环境监测站");
        result.setTaskName(project.getName());
        result.setSampingAddr(points.toString());
        result.setType(project.getMonType());//样品性质
        result.setTime(DateUtils.getTime(new Date().getTime()));

        return result;
    }

    /**
     * 设置选中状态
     *
     * @param position
     * @param isSelected
     */
    private void changSelectState(int position, boolean isSelected) {
        List<SamplingContent> detailList = WastewaterActivity.mSample.getSamplingContentResults();
        if (!CheckUtil.isEmpty(detailList)) {
            for (SamplingContent detail : detailList) {
                detail.setSelected(false);
            }
            detailList.get(position).setSelected(isSelected);
            mWasteWaterCollectAdapter.notifyDataSetChanged();

            if (isSelected) {
                selectSamplingDetail = detailList.get(position);
            } else {
                selectSamplingDetail = null;
            }
        }
    }

    /**
     * 添加平行样品:频次相同，但是SamplingCode不同
     */
    private void addTheSameSample() {
        SamplingContent samplingContent=new SamplingContent();
        samplingContent.setId(UUID.randomUUID().toString());
        samplingContent.setProjectId(selectSamplingDetail.getProjectId());
        samplingContent.setSamplingId(selectSamplingDetail.getSamplingId());
        samplingContent.setSampingCode(HelpUtil.createSamplingCode(WastewaterActivity.mSample));
        samplingContent.setFrequecyNo(selectSamplingDetail.getFrequecyNo());
        samplingContent.setDescription(selectSamplingDetail.getDescription());
        samplingContent.setSamplingType(1);
        samplingContent.setIsCompare(selectSamplingDetail.getIsCompare());
        samplingContent.setIsAddPreserve(selectSamplingDetail.getIsAddPreserve());
        samplingContent.setMonitemName(selectSamplingDetail.getMonitemName());
        samplingContent.setMonitemId(selectSamplingDetail.getMonitemId());
        samplingContent.setAddressName(selectSamplingDetail.getAddressName());
        samplingContent.setAddresssId(selectSamplingDetail.getAddresssId());
        samplingContent.setSamplingTime(selectSamplingDetail.getSamplingTime());
        samplingContent.setOrderIndex(HelpUtil.createOrderIndex(WastewaterActivity.mSample));
        samplingContent.setSampleCollection(selectSamplingDetail.getSampleCollection());
        samplingContent.setSampleAcceptance(selectSamplingDetail.getSampleAcceptance());
        samplingContent.setPreservative(selectSamplingDetail.getPreservative());

//        //金蓉说平行数据不带现场监测项目
//        samplingContent.setSenceMonitemName(selectSamplingDetail.getSenceMonitemName());
//        samplingContent.setSenceMonitemId(selectSamplingDetail.getSenceMonitemId());

        //包括监测项目和现场监测项目
        int count=0;
        String[] monitemIds=selectSamplingDetail.getMonitemId().split(",");
        if (!CheckUtil.isEmpty(monitemIds)){
            count+=monitemIds.length;
            for (String itemId:monitemIds){
                SamplingDetail detail = new SamplingDetail();
                detail.setProjectId(samplingContent.getProjectId());
                detail.setId(UUID.randomUUID().toString());
                detail.setSamplingId(samplingContent.getSamplingId());
                detail.setSampingCode(samplingContent.getSampingCode());
                detail.setFrequecyNo(samplingContent.getFrequecyNo());
                detail.setDescription(samplingContent.getDescription());
                detail.setSamplingType(1);
                detail.setIsCompare(samplingContent.getIsCompare());
                detail.setIsAddPreserve(samplingContent.getIsAddPreserve());
                detail.setMonitemName(getMonItemNameById(itemId));
                detail.setMonitemId(itemId);
                detail.setAddressName(samplingContent.getAddressName());
                detail.setAddresssId(samplingContent.getAddresssId());
                detail.setIsSenceAnalysis(false);
                detail.setSamplingTime(selectSamplingDetail.getSamplingTime());
                detail.setOrderIndex(samplingContent.getOrderIndex());
                detail.setSampleCollection(samplingContent.getSampleCollection());
                detail.setSampleAcceptance(samplingContent.getSampleAcceptance());
                detail.setPreservative(samplingContent.getPreservative());
                DBHelper.get().getSamplingDetailDao().insert(detail);
                WastewaterActivity.mSample.getSamplingDetailResults().add(detail);
            }
        }

        /*
        String[] sendMonitemIds=selectSamplingDetail.getSenceMonitemId().split(",");
        if (!CheckUtil.isEmpty(sendMonitemIds)){
            count+=sendMonitemIds.length;
            for (String itemId:sendMonitemIds){
                SamplingDetail detail = new SamplingDetail();
                detail.setProjectId(samplingContent.getProjectId());
                detail.setId(UUID.randomUUID().toString());
                detail.setSamplingId(samplingContent.getSamplingId());
                detail.setSampingCode(samplingContent.getSampingCode());
                detail.setFrequecyNo(samplingContent.getFrequecyNo());
                detail.setDescription(samplingContent.getDescription());
                detail.setSamplingType(1);
                detail.setIsCompare(samplingContent.getIsCompare());
                detail.setIsAddPreserve(samplingContent.getIsAddPreserve());
                detail.setMonitemName(getMonItemNameById(itemId));
                detail.setMonitemId(itemId);
                detail.setAddressName(samplingContent.getAddressName());
                detail.setAddresssId(samplingContent.getAddresssId());
                detail.setIsSenceAnalysis(true);
                detail.setSamplingTime(selectSamplingDetail.getSamplingTime());
                detail.setOrderIndex(samplingContent.getOrderIndex());
                detail.setSampleCollection(samplingContent.getSampleCollection());
                detail.setSampleAcceptance(samplingContent.getSampleAcceptance());
                detail.setPreservative(samplingContent.getPreservative());
                DBHelper.get().getSamplingDetailDao().insert(detail);
                WastewaterActivity.mSample.getSamplingDetailResults().add(detail);
            }
        }
        */

        //添加平行的样品数量和被选择的是一样的
        samplingContent.setSamplingCount(selectSamplingDetail.getSamplingCount());
        DBHelper.get().getSamplingContentDao().insert(samplingContent);
        WastewaterActivity.mSample.getSamplingContentResults().add(samplingContent);
        mWasteWaterCollectAdapter.notifyDataSetChanged();
    }


    /**
     * 添加空白样
     */
    private void addBlankSample() {
        //创建SamplingContent
        SamplingContent samplingContent=new SamplingContent();
        samplingContent.setId(UUID.randomUUID().toString());
        samplingContent.setProjectId(WastewaterActivity.mSample.getProjectId());
        samplingContent.setSamplingType(2);
        samplingContent.setSamplingId(WastewaterActivity.mSample.getId());
        //添加空白默认频次为0
        samplingContent.setSampingCode(HelpUtil.createSamplingCode(WastewaterActivity.mSample));
        samplingContent.setFrequecyNo(0);
        samplingContent.setOrderIndex(HelpUtil.createOrderIndex(WastewaterActivity.mSample));
        samplingContent.setMonitemId("");
        samplingContent.setMonitemName("");
        samplingContent.setSenceMonitemId("");
        samplingContent.setSenceMonitemName("");

        //创建SamplingDetail
        SamplingDetail detail = new SamplingDetail();
        detail.setProjectId(samplingContent.getProjectId());
        detail.setSamplingType(samplingContent.getSamplingType());
        detail.setId(UUID.randomUUID().toString());
        detail.setSamplingId(WastewaterActivity.mSample.getId());
        //添加空白默认频次为0
        detail.setSampingCode(samplingContent.getSampingCode());
        detail.setFrequecyNo(0);
        detail.setOrderIndex(samplingContent.getOrderIndex());
        DBHelper.get().getSamplingDetailDao().insert(detail);
        detail.setMonitemId("");
        detail.setMonitemName("");
        detail.setIsAddPreserve(false);
        detail.setPreservative("否");
        detail.setIsCompare(false);
        WastewaterActivity.mSample.getSamplingDetailResults().add(detail);

        //数据处理
        if (CheckUtil.isEmpty(WastewaterActivity.mSample.getSamplingContentResults())){
            WastewaterActivity.mSample.setSamplingContentResults(new ArrayList<>());
        }
        WastewaterActivity.mSample.getSamplingContentResults().add(samplingContent);
        DBHelper.get().getSamplingContentDao().insert(samplingContent);
        //刷新界面
        if (mWasteWaterCollectAdapter!=null){
            mWasteWaterCollectAdapter.refreshInfos(WastewaterActivity.mSample.getSamplingContentResults());
            //mWasteWaterCollectAdapter.notifyDataSetChanged();
        }else {
            initRecyclerViewData();
        }

    }

    /**
     * 判断某个普通样是否添加平行样:频次相同， code不同
     *
     * @param selectSamplingDetail
     * @return
     */
    private boolean isPxSampleAdded(SamplingContent selectSamplingDetail) {
        boolean flag = false;
        List<SamplingContent> samplingList = WastewaterActivity.mSample.getSamplingContentResults();
        if (!CheckUtil.isEmpty(samplingList)) {
            for (SamplingContent detail : samplingList) {
                if (selectSamplingDetail.getFrequecyNo()==detail.getFrequecyNo() && detail.getSamplingType() == 1) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }


    /**
     * 根据itemId获取name
     * @param itemId
     * @return
     */
    private String getMonItemNameById(String itemId){
        Tags tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(WastewaterActivity.mSample.getParentTagId())).unique();
        List<MonItems> monItems = tags.getMMonItems();
        if (!CheckUtil.isEmpty(monItems)){
            for (MonItems monItem:monItems){
                if (!CheckUtil.isEmpty(monItem.getId()) && !CheckUtil.isEmpty(itemId) && monItem.getId().equals(itemId)){
                    return monItem.getName();
                }
            }
        }
        return "";
    }

}
