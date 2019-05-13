package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.InstrumentalTestRecordAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.WasteWaterSamplingActivity;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity.mSampling;

public class TestRecordFragment extends BaseFragment {

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

    private InstrumentalTestRecordAdapter mInstrumentalTestRecordAdapter;
//    private SharedPreferences collectListSettings;
//    private SharedPreferences.Editor editor;

    SamplingDetail currSelectDetails = null;

    public TestRecordFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instrumental_test_record, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        btnPrintLabel.setVisibility(View.GONE);
        tvAddBlank.setText("添加样品");
        btn_add_new.setVisibility(View.GONE);
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
//        collectListSettings = getActivity().getSharedPreferences("setting", 0);
//        editor = collectListSettings.edit();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            mInstrumentalTestRecordAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mInstrumentalTestRecordAdapter != null) {
                //排序
                Collections.sort(mSampling.getSamplingDetailYQFs(), new DetailComparator());
                mInstrumentalTestRecordAdapter.notifyDataSetChanged();
            } else {
                initRecyclerViewData();
            }
        }

    }

    @OnClick({R.id.btn_add_parallel, R.id.btn_add_blank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_parallel:
                //添加选中样品的平行数据
                addPxItem();
                break;
            case R.id.btn_add_blank:
                if (TextUtils.isEmpty(mSampling.getMonitemId())) {
                    ArtUtils.makeText(getContext(), "请选择项目！");
                    return;
                }

                //跳转到添加样品
                Intent intent4 = new Intent(getContext(), WasteWaterSamplingActivity.class);
                new AvoidOnResult(getActivity()).startForResult(intent4, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            if (mInstrumentalTestRecordAdapter != null) {
                                //排序
                                Collections.sort(mSampling.getSamplingDetailYQFs(), new DetailComparator());
                                mInstrumentalTestRecordAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
                break;
        }
    }

    private void initRecyclerViewData() {
        if (!mSampling.getIsCanEdit()) {
            btnAddParallel.setVisibility(View.GONE);
            btnAddBlank.setVisibility(View.GONE);
        }

        if (mSampling.getSamplingDetailYQFs() == null) {
            mSampling.setSamplingDetailYQFs(new ArrayList<SamplingDetail>());
        }

        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        //检查平行数据，决定是否可选中
        checkPxData(mSampling.getSamplingDetailYQFs());

        //排序
        Collections.sort(mSampling.getSamplingDetailYQFs(), new DetailComparator());


        mInstrumentalTestRecordAdapter = new InstrumentalTestRecordAdapter(mSampling.getSamplingDetailYQFs());
        mInstrumentalTestRecordAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (position < 0 || position >= mSampling.getSamplingDetailYQFs().size()) {
                    return;
                }

                SamplingDetail item = mSampling.getSamplingDetailYQFs().get(position);
                if (item == null) {
                    return;
                }
                if (currSelectDetails == item) {
                    currSelectDetails.setSelected(false);
                    currSelectDetails = null;
                } else if (item.isCanSelect()) {
                    if (currSelectDetails != null) {
                        currSelectDetails.setSelected(false);
                    }
                    //记录选中项
                    currSelectDetails = item;
                    currSelectDetails.setSelected(true);
                }
                //更新列表
                mInstrumentalTestRecordAdapter.notifyDataSetChanged();
            }
        });
        recyclerview.setAdapter(mInstrumentalTestRecordAdapter);
    }

    /**
     * 验证平行数据
     */
    private void checkPxData(List<SamplingDetail> details) {
        for (SamplingDetail item : details) {
            if (item.getSamplingType() == 1) {
                //平行数据，不能被选中
                item.setCanSelect(false);
                continue;
            } else {
                //样品数据，已添加平行数据时不能被选中
                SamplingDetail pxItem = findPXItem(details, item);
                if (pxItem != null) {
                    item.setCanSelect(false);
                    continue;
                }
            }

            item.setCanSelect(true);

            //记录选中项
            if (item.isSelected()) {
                if (currSelectDetails != null) {
                    currSelectDetails.setSelected(false);
                }
                currSelectDetails = item;
            }
        }
    }

    /**
     * 添加平行数据
     */
    private void addPxItem() {
        if (currSelectDetails == null) {
            ArtUtils.makeText(getContext(), "请选择一个样品！");
            return;
        }

        //复制数据
        SamplingDetail samplingDetail = new SamplingDetail();

        samplingDetail.setId("LC-" + UUID.randomUUID().toString());
        samplingDetail.setProjectId(currSelectDetails.getProjectId());
        samplingDetail.setMonitemId(currSelectDetails.getMonitemId());
        samplingDetail.setMonitemName(currSelectDetails.getMonitemName());
        samplingDetail.setSamplingId(currSelectDetails.getSamplingId());
        samplingDetail.setSampingCode(currSelectDetails.getSampingCode());
        samplingDetail.setSamplingType(1);//样品0  平行1
        samplingDetail.setSamplingOnTime(currSelectDetails.getSamplingOnTime());
        samplingDetail.setAddresssId(currSelectDetails.getAddresssId());
        samplingDetail.setAddressName(currSelectDetails.getAddressName());
        samplingDetail.setFrequecyNo(currSelectDetails.getFrequecyNo());
        samplingDetail.setOrderIndex(currSelectDetails.getOrderIndex() + 1);
        samplingDetail.setPrivateDataBooleanValue("HasPX", false);
        samplingDetail.setPrivateDataStringValue("SamplingOnTime", "");
        samplingDetail.setPrivateDataStringValue("CaleValue", "");
        samplingDetail.setPrivateDataStringValue("RPDValue", "");
        samplingDetail.setPrivateDataStringValue("ValueUnit", "");
        samplingDetail.setPrivateDataStringValue("ValueUnitName", "");
        samplingDetail.setValue("");//均值

        //保存到数据库
        DBHelper.get().getSamplingDetailDao().insert(samplingDetail);

        //添加到样品记录的下一行
        mSampling.getSamplingDetailYQFs().add(samplingDetail);

        currSelectDetails.setCanSelect(false);
        currSelectDetails.setSelected(false);
        currSelectDetails = null;

        //排序
        Collections.sort(mSampling.getSamplingDetailYQFs(), new DetailComparator());

        //更新列表
        mInstrumentalTestRecordAdapter.notifyDataSetChanged();
    }

    /**
     * 查找原始记录的平行数据
     *
     * @param details
     * @param sourceItem
     * @return
     */
    public static SamplingDetail findPXItem(List<SamplingDetail> details, SamplingDetail sourceItem) {
        for (SamplingDetail item : details) {
            if (item == sourceItem) {
                continue;//过滤原数据
            }

            //平行数据，样品类型（样品、平行）不一样，样品编码相等
            if (item.getSamplingType() == sourceItem.getSamplingType() || !item.getSampingCode().equals(sourceItem.getSampingCode())) {
                continue;
            }

            return item;
        }

        return null;
    }

    public static class DetailComparator implements Comparator<SamplingDetail> {

        @Override
        public int compare(SamplingDetail o1, SamplingDetail o2) {
            if (!o1.getSampingCode().equals(o2.getSampingCode())) {
                return o1.getSampingCode().compareTo(o2.getSampingCode());
            }

            if (o1.getFrequecyNo() < o2.getFrequecyNo()) {
                return -1;
            } else if (o1.getFrequecyNo() > o2.getFrequecyNo()) {
                return 1;
            } else {
                if (o1.getSamplingType() == 0) {
                    return -1;
                }

                return 0;
            }

        }
    }
}
