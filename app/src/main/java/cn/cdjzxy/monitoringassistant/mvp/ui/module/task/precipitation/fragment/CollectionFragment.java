package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.fragment;


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
import com.google.gson.reflect.TypeToken;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.LabelInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SealInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PrecipitationCollectAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.LabelPrintActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity.mSampling;

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

    private PrecipitationCollectAdapter mPrecipitationCollectAdapter;
    private SharedPreferences collectListSettings;
    private SharedPreferences.Editor editor;
    private List<SamplingDetail> list;

    public CollectionFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_precipitation_collect, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        btnAddParallel.setVisibility(View.GONE);
        btn_add_new.setVisibility(View.GONE);
        tvAddBlank.setText("添加");
        getData();
        initRecyclerViewData();
    }

    private void getData() {
        if (list == null) list = new ArrayList<>();
        else list.clear();
        for (SamplingDetail detail : mSampling.getSamplingDetailResults()) {
            if (detail != null && detail.getMethodName() != null &&
                    detail.getMonitemName().equals("降水量")) {//毛杨说的这个表单  这个只有降水量
                list.add(detail);
            }
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getData();
            if (mPrecipitationCollectAdapter != null) {
                mPrecipitationCollectAdapter.notifyDataSetChanged();
            } else {
                initRecyclerViewData();
            }
        }

    }

    @OnClick({R.id.btn_add_parallel, R.id.btn_add_blank, R.id.btn_print_label})
    public void onClick(View view) {
        hideSoftInput();
        switch (view.getId()) {
            case R.id.btn_add_parallel:

                break;
            case R.id.btn_add_blank:
                //添加空白
                if (TextUtils.isEmpty(mSampling.getAddressName())) {
                    ArtUtils.makeText(getContext(), "请先选择采样点位或者采样编号");
                    return;
                }
                editor.putInt("listPosition", -1);
                editor.commit();
                EventBus.getDefault().post(2, EventBusTags.TAG_PRECIPITATION_COLLECTION);
                break;
            case R.id.btn_print_label:
                Gson gson = new Gson();
                //构建标签数据
                String labelStr = gson.toJson(buildPrintLabelList(mSampling));
                //构建封条数据
                String sealStr = gson.toJson(buildSealInfo(PrecipitationActivity.mProject));

                Intent intent = new Intent(getContext(), LabelPrintActivity.class);
                intent.putExtra(LabelPrintActivity.LABEL_JSON_DATA, labelStr);
                intent.putExtra(LabelPrintActivity.SEAL_JSON_DATA, sealStr);
                ArtUtils.startActivity(intent);
                break;
        }
    }

    private void initRecyclerViewData() {
        list = new ArrayList<>();
        if (!mSampling.getIsCanEdit()) {
            btnAddParallel.setVisibility(View.GONE);
            //btnAddBlank.setVisibility(View.GONE);
            //btnPrintLabel.setVisibility(View.GONE);
            btnAddBlank.setEnabled(false);
            btnPrintLabel.setEnabled(true);
            btnAddBlank.setAlpha(0.5f);
        } else {
            btnAddBlank.setEnabled(true);
            btnPrintLabel.setEnabled(true);
            btnAddBlank.setAlpha(1f);
        }

        if (mSampling.getSamplingDetailResults() == null) {
            return;
        }

        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mPrecipitationCollectAdapter = new PrecipitationCollectAdapter(list);
        mPrecipitationCollectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                EventBus.getDefault().post(2, EventBusTags.TAG_PRECIPITATION_COLLECTION);
                editor.putInt("listPosition", position);
                editor.commit();
            }
        });

        recyclerview.setAdapter(mPrecipitationCollectAdapter);
    }

    /**
     * 构建打印的标签信息列表
     *
     * @return
     */
    private ArrayList<LabelInfo> buildPrintLabelList(Sampling sampling) {
        ArrayList<LabelInfo> result = new ArrayList<>();

        //组装标签信息
        for (SamplingDetail item : sampling.getSamplingDetailResults()) {
            LabelInfo info = new LabelInfo();
            info.setTaskName(sampling.getProjectName());
            info.setNumber(sampling.getSamplingNo());
            info.setFrequecyNo("频次：" + item.getFrequecyNo());
            info.setType("降水");//项目类型固定“降水”
            info.setMonitemName(item.getMonitemName());//监测项目固定“降水量”
            info.setSampingCode(item.getSampingCode());
            info.setRemark("");//保存方法
            info.setCb1("交接");
            info.setCb2("分析");
            info.setQrCode(item.getSampingCode());//二维码为样品编码

            result.add(info);
        }

        return result;
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
}
