package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WasteWaterBottleAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;

/**
 * 分瓶信息
 */

public class BottleSplitFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btn_add_parallel)
    RelativeLayout btnAddParallel;
    @BindView(R.id.tv_add_blank)
    TextView tvAddBlank;
    @BindView(R.id.btn_print_label)
    RelativeLayout btnPrintLabel;

    private WasteWaterBottleAdapter mWasteWaterBottleAdapter;
    private SharedPreferences collectListSettings;
    private SharedPreferences.Editor editor;

    public BottleSplitFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wastewater_bottle_split, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        btnAddParallel.setVisibility(View.GONE);
        btnPrintLabel.setVisibility(View.GONE);
        tvAddBlank.setText("添加");
        tvAddBlank.setVisibility(View.GONE);
        initRecyclerViewData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mWasteWaterBottleAdapter != null) {
                mWasteWaterBottleAdapter.refreshInfos(WastewaterActivity.mSample.getSamplingFormStandResults());
            } else {
                initRecyclerViewData();
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
    public void onPause() {
        super.onPause();
    }

    @OnClick({R.id.btn_add_blank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_blank:
                editor.putInt("bottleListPosition", -1);
                editor.commit();
                EventBus.getDefault().post(5, EventBusTags.TAG_WASTEWATER_COLLECTION);
                break;

        }
    }

    private void initRecyclerViewData() {
        if (WastewaterActivity.mSample.getSamplingFormStandResults() == null) {
            return;
        }
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });


        mWasteWaterBottleAdapter = new WasteWaterBottleAdapter(WastewaterActivity.mSample.getSamplingFormStandResults());
        mWasteWaterBottleAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                editor.putInt("bottleListPosition", position);
                editor.commit();
                EventBus.getDefault().post(5, EventBusTags.TAG_WASTEWATER_BOTTLE);
            }
        });
        recyclerview.setAdapter(mWasteWaterBottleAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
