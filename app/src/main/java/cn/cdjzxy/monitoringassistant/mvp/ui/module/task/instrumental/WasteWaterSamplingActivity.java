package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WasteWaterSamplingAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.StringUtil;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity.mSampling;

public class WasteWaterSamplingActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.recyclerView_monite)
    RecyclerView recyclerViewMonite;

    private String projectId;
    private String monitemId;
    private ArrayList<SamplingDetail> currSampling = new ArrayList<>();
    private WasteWaterSamplingAdapter mWasteWaterSamplingAdapter;

    private List<SamplingContent> mSamplingDetails = new ArrayList<>();
    private List<SamplingContent> mSelectDetails = new ArrayList<>();

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
        projectId = mSampling.getProjectId();
        monitemId = mSampling.getMonitemId();


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

        initCurrSamplings();
        //获取项目中所有的样品，过滤已添加的样品
        getSampling(TaskDetailActivity.PATH_WASTEWATER);

        mWasteWaterSamplingAdapter = new WasteWaterSamplingAdapter(mSamplingDetails);
        mWasteWaterSamplingAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (position < 0 || position >= mSamplingDetails.size()) {
                    return;
                }

                SamplingContent item = mSamplingDetails.get(position);
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
                if (addSelectSampling()) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });
        recyclerViewMonite.setAdapter(mWasteWaterSamplingAdapter);
    }

    private void initCurrSamplings() {
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.ProjectId.eq(projectId),
                        SamplingDao.Properties.FormPath.eq(TaskDetailActivity.PATH_INSTRUMENTAL)).list();
        if (CheckUtil.isEmpty(samplings)) {
            return;
        }
        for (Sampling item : samplings) {
            if (item.getSamplingDetailYQFs() != null)
                currSampling.addAll(item.getSamplingDetailYQFs());
        }
    }

    /**
     * 获取水和废水采样单
     *
     * @param path
     */
    private void getSampling(String path) {
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.ProjectId.eq(projectId),
                        SamplingDao.Properties.FormPath.eq(path)).orderDesc(SamplingDao.Properties.SamplingNo).list();
        if (CheckUtil.isEmpty(samplings)) {
            return;
        }

        for (Sampling item : samplings) {
            //获取样品数据
            List<SamplingContent> contentList = item.getSamplingContentResults();

            //如果为空则尝试从数据库获取
            if (CheckUtil.isEmpty(contentList)) {
                contentList = DBHelper.get().getSamplingContentDao().queryBuilder().where(SamplingContentDao.Properties.SamplingId.eq(item.getId())).list();
            }

            for (SamplingContent content : contentList) {
                if (TextUtils.isEmpty(content.getSenceMonitemId())) {
                    continue;
                }

                //水和废水中，现场监测项目
                if (!content.getSenceMonitemId().contains(monitemId)) {
                    continue;//过滤不同的项目名
                }

                //频次唯一,不选平行数据
                if (isExists(content) || content.getSamplingType() == 1) {
                    continue;//过滤已经存在的样品 或 平行数据
                }

                //水和废水的样品，点位信息保存的现场检测信息，这里改为实际点位信息，用于显示
                content.setTempValue1(item.getSamplingTimeBegin());
                content.setTempValue2(item.getAddressId());
                content.setTempValue3(item.getAddressName());

                mSamplingDetails.add(content);
            }
        }
    }

    /**
     * 是否是已经存在的记录
     *
     * @param detail
     * @return
     */
    private boolean isExists(SamplingContent detail) {
        for (SamplingDetail item : currSampling) {
            //重复项：样品类型一致（样品、平行），样品编码一致
            if (item.getSamplingType() == detail.getSamplingType() && item.getSampingCode().
                    equals(detail.getSampingCode())) {
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


        //先保存采样单
        Sampling sampling = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.
                Properties.Id.eq(mSampling.getId())).unique();
        if (CheckUtil.isNull(sampling)) {
            DBHelper.get().getSamplingDao().insert(mSampling);
        }

        for (SamplingContent item : mSelectDetails) {
            //频次唯一,不选平行数据
            if (isExists(item) || item.getSamplingType() == 1) {
                continue;//过滤已经存在的样品 或 平行数据
            }

            SamplingDetail samplingDetail = new SamplingDetail();

            samplingDetail.setId("YQF-" + UUID.randomUUID().toString());
            samplingDetail.setProjectId(mSampling.getProjectId());
            samplingDetail.setMonitemId(mSampling.getMonitemId());
            samplingDetail.setMonitemName(mSampling.getMonitemName());
            samplingDetail.setSamplingId(mSampling.getId());//对应到当前采样单
            samplingDetail.setSampingCode(item.getSampingCode());//样品编码
            samplingDetail.setSamplingType(0);////样品0  平行1
            samplingDetail.setSamplingOnTime(item.getTempValue1());//监测日期
            samplingDetail.setAddresssId(item.getTempValue2());
            samplingDetail.setAddressName(item.getTempValue3());
            samplingDetail.setFrequecyNo(item.getFrequecyNo());//复制样品的频次
            samplingDetail.setOrderIndex(mSampling.getSamplingDetailYQFs()
                    == null ? 0 : mSampling.getSamplingDetailYQFs().size());
            samplingDetail.setPrivateDataBooleanValue("HasPX", false);
            samplingDetail.setPrivateDataStringValue("SamplingOnTime", "");
            samplingDetail.setPrivateDataStringValue("CaleValue", "");
            samplingDetail.setPrivateDataStringValue("RPDValue", "");
            samplingDetail.setPrivateDataStringValue("ValueUnit", "");
            samplingDetail.setPrivateDataStringValue("ValueUnitName", "");
            samplingDetail.setValue("");//均值

            //新加样品都可选择
            samplingDetail.setCanSelect(true);

            currSampling.add(samplingDetail);

            //保存到数据库
            DBHelper.get().getSamplingDetailDao().insert(samplingDetail);

            //添加到记录
            mSampling.getSamplingDetailYQFs().add(samplingDetail);

            //记录点位ID和点位名称
            if (!TextUtils.isEmpty(item.getTempValue2()) && !mSampling.getAddressId().contains(item.getTempValue2())) {
                mSampling.setAddressId(StringUtil.trimStr(mSampling.getAddressId() + "," + item.getTempValue2(), ","));
            }
            if (!TextUtils.isEmpty(item.getTempValue3()) && !mSampling.getAddressName().contains(item.getTempValue3())) {
                mSampling.setAddressName(StringUtil.trimStr(mSampling.getAddressName() + "," + item.getTempValue3(), ","));
            }
        }

        mSelectDetails.clear();

        //保存到数据库
        DBHelper.get().getSamplingDao().update(mSampling);

        //更新采样单列表的显示
        EventBus.getDefault().post(true, EventBusTags.TAG_SAMPLING_UPDATE);

        return true;
    }
}
