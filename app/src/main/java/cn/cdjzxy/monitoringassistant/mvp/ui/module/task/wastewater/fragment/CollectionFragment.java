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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.LabelInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SealInfo;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WasteWaterCollectAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.LabelPrintActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

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

    private WasteWaterCollectAdapter mWasteWaterCollectAdapter;
    private SharedPreferences collectListSettings;
    private SharedPreferences.Editor editor;

    public CollectionFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_collect, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
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
                mWasteWaterCollectAdapter.notifyDataSetChanged();
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


    @OnClick({R.id.btn_add_parallel, R.id.btn_add_blank, R.id.btn_print_label})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_parallel:
                ArtUtils.makeText(getContext(), "添加平行");
                break;
            case R.id.btn_add_blank:
                //添加空白
                if (TextUtils.isEmpty(WastewaterActivity.mSample.getAddressNo())) {
                    ArtUtils.makeText(getContext(), "请先选择采样点位");
                    return;
                }
                editor.putInt("fsListPosition", -1);
                editor.commit();
                EventBus.getDefault().post(3, EventBusTags.TAG_WASTEWATER_COLLECTION);
                break;
            case R.id.btn_print_label:
                Gson gson = new Gson();
                //构建标签数据
                String labelStr = gson.toJson(buildPrintLabelList(WastewaterActivity.mSample));
                //构建封条数据
                String sealStr = gson.toJson(buildSealInfo(WastewaterActivity.mSample));

                Intent intent = new Intent(getContext(), LabelPrintActivity.class);
                intent.putExtra(LabelPrintActivity.LABEL_JSON_DATA, labelStr);
                intent.putExtra(LabelPrintActivity.SEAL_JSON_DATA, sealStr);
                ArtUtils.startActivity(intent);
                break;
        }
    }

    private void initRecyclerViewData() {
        if (WastewaterActivity.mSample.getSamplingDetailResults() == null) {
            return;
        }
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mWasteWaterCollectAdapter = new WasteWaterCollectAdapter(WastewaterActivity.mSample.getSamplingDetailResults());
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
        for (SamplingDetail item : sampling.getSamplingDetailResults()) {
            LabelInfo info = new LabelInfo();
            info.setTaskName(sampling.getProjectName());
            info.setNumber(sampling.getSamplingNo());
            info.setFrequecyNo("频次：" + item.getFrequecyNo());
            info.setType("废水");//项目类型固定废水
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
    private SealInfo buildSealInfo(Sampling sampling) {
        SealInfo result = new SealInfo();
        result.setTitle("新都区环境监测站");
        result.setTaskName(sampling.getProjectName());
        result.setSampingAddr(sampling.getAddressName());
        result.setType(sampling.getSampProperty());//样品性质
        result.setTime(DateUtils.getTime(new Date().getTime()));

        return result;
    }
}
