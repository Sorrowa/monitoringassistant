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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.InstrumentalTestRecordAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.UnitActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.WasteWaterSamplingActivity;

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

    private InstrumentalTestRecordAdapter mInstrumentalTestRecordAdapter;
    private SharedPreferences collectListSettings;
    private SharedPreferences.Editor editor;

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

                break;
            case R.id.btn_add_blank:
                if (TextUtils.isEmpty(InstrumentalActivity.mSampling.getMonitemId())) {
                    ArtUtils.makeText(getContext(), "请选择项目！");
                    return;
                }

                //跳转到添加样品
                Intent intent4 = new Intent(getContext(), WasteWaterSamplingActivity.class);
                new AvoidOnResult(getActivity()).startForResult(intent4, new AvoidOnResult.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        if (resultCode == Activity.RESULT_OK) {
                            mInstrumentalTestRecordAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
        }
    }

    private void initRecyclerViewData() {
        if (!InstrumentalActivity.mSampling.getIsCanEdit()) {
            btnAddParallel.setVisibility(View.GONE);
            btnAddBlank.setVisibility(View.GONE);
        }

        if (InstrumentalActivity.mSampling.getSamplingDetailYQFs() == null) {
            InstrumentalActivity.mSampling.setSamplingDetailYQFs(new ArrayList<SamplingDetail>());
        }

        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mInstrumentalTestRecordAdapter = new InstrumentalTestRecordAdapter(InstrumentalActivity.mSampling.getSamplingDetailYQFs());
        mInstrumentalTestRecordAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                EventBus.getDefault().post(2, EventBusTags.TAG_INSTRUMENTAL_RECORD);
                editor.putInt("listPosition", position);
                editor.commit();
            }
        });

        recyclerview.setAdapter(mInstrumentalTestRecordAdapter);
    }

    /**
     * 计算仪器法数据
     */
    private void calcYQFData(List<SamplingDetail> details) {
        if (details == null || details.size() == 0) {
            return;
        }

        for (SamplingDetail detail : details) {
            SamplingDetail pxItem = findPXItem(details, detail);
            if (pxItem == null) {
                continue;
            }
            //计算均值和相对偏差
        }
    }

    /**
     * 查找原始记录的平行数据
     *
     * @param details
     * @param sourceItem
     * @return
     */
    private SamplingDetail findPXItem(List<SamplingDetail> details, SamplingDetail sourceItem) {
        try {
            //原数据是平行数据，无须再查找
            if (sourceItem.getPrivateJsonData().getBoolean("HasPX")) {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (SamplingDetail item : details) {
            if (item == sourceItem) {
                continue;//过滤原数据
            }

            //平行数据，频次相等
            if (item.getFrequecyNo() != sourceItem.getFrequecyNo()) {
                continue;
            }

            //解析私有数据
            try {
                if (item.getPrivateJsonData().getBoolean("HasPX")) {
                    return item;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
