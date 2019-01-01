package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WasteWaterCollectAdapter;

/**
 * 样品收集
 */

public class CollectionFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.recyclerview)
    RecyclerView   recyclerview;
    @BindView(R.id.tv_add_parallel)
    TextView       tvAddParallel;
    @BindView(R.id.btn_add_parallel)
    RelativeLayout btnAddParallel;
    @BindView(R.id.tv_add_blank)
    TextView       tvAddBlank;
    @BindView(R.id.btn_add_blank)
    RelativeLayout btnAddBlank;
    @BindView(R.id.tv_print_label)
    TextView       tvPrintLabel;
    @BindView(R.id.btn_print_label)
    RelativeLayout btnPrintLabel;

    private WasteWaterCollectAdapter mWasteWaterCollectAdapter;

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


    @OnClick({R.id.btn_add_parallel, R.id.btn_add_blank, R.id.btn_print_label})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_parallel:
                ArtUtils.makeText(getContext(), "添加平行");
                break;
            case R.id.btn_add_blank:
                ArtUtils.makeText(getContext(), "添加空白");
                break;
            case R.id.btn_print_label:
                ArtUtils.makeText(getContext(), "打印标签");
                break;
        }
    }

    private void initRecyclerViewData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        List<Tab> mTabs = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Tab tab = new Tab();
            mTabs.add(tab);
        }

        mWasteWaterCollectAdapter = new WasteWaterCollectAdapter(mTabs);
        mWasteWaterCollectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                EventBus.getDefault().post(3, EventBusTags.TAG_WASTEWATER_COLLECTION);
            }
        });
        recyclerview.setAdapter(mWasteWaterCollectAdapter);
    }
}
