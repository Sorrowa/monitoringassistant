package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WasteWaterSamplingAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WasteWaterMoniteAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment.TestRecordDetailFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

public class WasteWaterSamplingActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.recyclerView_monite)
    RecyclerView recyclerViewMonite;

    private String projectId;
    private String monitemId;
    private ArrayList<SamplingDetail> currSampling = new ArrayList<>();
    private WasteWaterSamplingAdapter mWasteWaterSamplingAdapter;

    private List<SamplingDetail> mSamplingDetails = new ArrayList<>();
    private List<SamplingDetail> mSelectDetails = new ArrayList<>();

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("样品选择");
        titleBar.setRightText("添加");
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addSelectSampling()) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_waste_water_sampling;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        projectId = InstrumentalActivity.mSampling.getProjectId();
        monitemId = InstrumentalActivity.mSampling.getMonitemId();
        currSampling.addAll(InstrumentalActivity.mSampling.getSamplingDetailYQFs());

        //初始化水和废水样品数据
        initSamplingDetailsData();
    }

    /**
     * 初始化Tab数据
     */
    private void initSamplingDetailsData() {
        ArtUtils.configRecyclerView(recyclerViewMonite, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        //获取项目中所有的样品，过滤已添加的样品
        getSampling(TaskDetailActivity.PATH_WASTEWATER);

        mWasteWaterSamplingAdapter = new WasteWaterSamplingAdapter(mSamplingDetails);
        mWasteWaterSamplingAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (position < 0 || position >= mSamplingDetails.size()) {
                    return;
                }

                SamplingDetail item = mSamplingDetails.get(position);
                if (item == null) {
                    return;
                }

                ImageView ivChoose = view.findViewById(R.id.ivChoose);

                if (mSelectDetails.contains(item)) {
                    mSelectDetails.remove(item);
                    ivChoose.setImageResource(R.mipmap.ic_cb_nor);
                } else {
                    mSelectDetails.add(item);
                    ivChoose.setImageResource(R.mipmap.ic_cb_checked);
                }
            }
        });
        recyclerViewMonite.setAdapter(mWasteWaterSamplingAdapter);
    }

    /**
     * 获取水和废水采样单
     *
     * @param path
     */
    private void getSampling(String path) {
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.ProjectId.eq(projectId), SamplingDao.Properties.FormPath.eq(path)).orderDesc(SamplingDao.Properties.SamplingNo).list();
        if (CheckUtil.isEmpty(samplings)) {
            return;
        }

        for (Sampling item : samplings) {
            //获取样品数据
            List<SamplingDetail> samplingDetails = item.getSamplingDetailResults();

            //如果为空则尝试从数据库获取
            if (CheckUtil.isEmpty(samplingDetails)) {
                samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(item.getId())).list();
            }

            for (SamplingDetail detail : samplingDetails) {
                if (!detail.getMonitemId().contains(monitemId)) {
                    continue;//过滤不同的项目名
                }

                //频次唯一
                if (isExists(detail.getFrequecyNo())) {
                    continue;//过滤已经存在的样品
                }

                //水和废水的样品，点位信息保存的现场检测信息，这里改为实际点位信息，用于显示
                detail.setAddresssId(item.getAddressId());
                detail.setAddressName(item.getAddressName());
                detail.setSamplingTime(item.getSamplingTimeBegin());

                mSamplingDetails.add(detail);
            }
        }
    }

    /**
     * 是否是已经存在的记录
     *
     * @param frequecyNo
     * @return
     */
    private boolean isExists(int frequecyNo) {
        for (SamplingDetail item : currSampling) {
            if (item.getFrequecyNo() == frequecyNo) {
                return true;
            }
        }

        return false;
    }

    /**
     * 添加选择的样品
     */
    private boolean addSelectSampling() {
        if (mSelectDetails.size() == 0) {
            ArtUtils.makeText(this, "请选择样品！");
            return false;
        }


        Sampling mSampling = InstrumentalActivity.mSampling;

        //先保存采样单
        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.Id.eq(mSampling.getId())).unique();
        if (CheckUtil.isNull(sampling)) {
            DBHelper.get().getSamplingDao().insert(mSampling);
        }

        for (SamplingDetail item : mSelectDetails) {
            //频次唯一
            if (isExists(item.getFrequecyNo())) {
                continue;//过滤已经存在的样品
            }

            SamplingDetail samplingDetail = new SamplingDetail();

            samplingDetail.setId("LC-" + UUID.randomUUID().toString());
            samplingDetail.setSamplingId(mSampling.getId());
            samplingDetail.setSampingCode(item.getSampingCode());
            samplingDetail.setSamplingTime(item.getSamplingTime());
            samplingDetail.setAddresssId(item.getAddresssId());
            samplingDetail.setAddressName(item.getAddressName());
            samplingDetail.setFrequecyNo(item.getFrequecyNo());
            samplingDetail.setPrivateDataBooleanValue("HasPX", false);
            samplingDetail.setPrivateDataStringValue("SamplingOnTime","");
            samplingDetail.setPrivateDataStringValue("CaleValue","");
            samplingDetail.setPrivateDataStringValue("RPDValue","");
            samplingDetail.setValue("");//均值

            //新加样品都可选择
            samplingDetail.setCanSelect(true);

            currSampling.add(samplingDetail);

            //保存到数据库
            DBHelper.get().getSamplingDetailDao().insert(samplingDetail);

            //添加到记录
            mSampling.getSamplingDetailYQFs().add(samplingDetail);
        }

        mSelectDetails.clear();

        return true;
    }
}
