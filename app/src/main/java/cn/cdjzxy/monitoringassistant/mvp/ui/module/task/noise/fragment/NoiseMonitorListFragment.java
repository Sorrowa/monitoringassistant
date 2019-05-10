package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.micheal.print.thread.ThreadPool;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseMonitorPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoiseMonitorAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoisePointAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_INT_MONITOR_EDIT;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_MONITOR_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;


/**
 * 噪声监测数据列表
 */
public class NoiseMonitorListFragment extends BaseFragment implements IView {
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_add)
    LinearLayout linearAdd;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<NoiseMonitorPrivateData> mPrivateDataList;
    private NoiseMonitorAdapter adapter;
    public List<String> selectList;//选中的集合

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_source_list, null);
    }

    public void showLoading(String msg) {
        showLoadingDialog(msg);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }


    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }


    @Override
    public void onResume() {
        super.onResume();
        initListData("");
        initRecyclerView();
    }

    @Subscriber(tag = NOISE_FRAGMENT_MONITOR_SHARE)
    private void initListData(String s) {
        mPrivateDataList = new ArrayList<>();
        selectList = new ArrayList<>();
        if (mSample != null) {
            if (mSample.getSamplingDetailResults() != null && mSample.getSamplingDetailResults().size() > 0) {
                Gson gson = new Gson();
                for (SamplingDetail content : mSample.getSamplingDetailResults()) {
                    NoiseMonitorPrivateData privateData = gson.fromJson(content.getPrivateData(),
                            NoiseMonitorPrivateData.class);
                    privateData.setId(content.getId());
                    privateData.setAddressName(content.getAddressName());
                    privateData.setValue(content.getValue());
                    privateData.setChecked(content.isSelected());
                    mPrivateDataList.add(privateData);
                }
            }
        }
        for (NoiseMonitorPrivateData privateData : mPrivateDataList) {
            if (privateData.isChecked()) {
                selectList.add(privateData.getId());
            }
        }
        if (adapter != null) adapter.refreshInfos(mPrivateDataList);

    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NoiseMonitorAdapter(mPrivateDataList, new NoiseMonitorAdapter.ItemClickListener() {
            @Override
            public void onSelected(View view, int position) {
                upListSelectState(position);
            }

            @Override
            public void onClick(View view, int position) {
                getActivity().getSharedPreferences(NOISE_FRAGMENT_SHARE, 0).edit()
                        .putInt(NOISE_FRAGMENT_MONITOR_SHARE, position).commit();
                EventBus.getDefault().post(NOISE_FRAGMENT_INT_MONITOR_EDIT, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
            }
        });
        recyclerView.setAdapter(adapter);
    }


    /**
     * 更新选择状态
     *
     * @param position
     */
    private void upListSelectState(int position) {
        if (mPrivateDataList.get(position) != null) {
            if (mPrivateDataList.get(position).isChecked()) {
                mPrivateDataList.get(position).setChecked(false);
                if (selectList != null && selectList.size() > 0) {
                    if (selectList.contains(mPrivateDataList.get(position).getId())) {
                        selectList.remove(mPrivateDataList.get(position).getId());
                    }
                }
            } else {
                mPrivateDataList.get(position).setChecked(true);
                if (selectList == null) {
                    selectList = new ArrayList<>();
                }
                if (!selectList.contains(mPrivateDataList.get(position).getId())) {
                    selectList.add(mPrivateDataList.get(position).getId());
                }

            }
        }
        adapter.notifyDataSetChanged();
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
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(getContext(), message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    @OnClick({R.id.linear_delete, R.id.linear_add})
    public void onClick(View v) {
        hideSoftInput();
        if (!mSample.getIsCanEdit()) {
            showMessage("提示：当前采样单，不支持编辑");
            return;
        }
        switch (v.getId()) {
            case R.id.linear_delete:
                deleteSelect();
                break;
            case R.id.linear_add:
                if (mPrivateData == null || mPrivateData.getMianNioseAddr().size() == 0) {
                    showMessage("请先选择监测点位信息");
                } else {
                    getActivity().getSharedPreferences(NOISE_FRAGMENT_SHARE, 0).edit()
                            .putInt(NOISE_FRAGMENT_MONITOR_SHARE, -1).commit();
                    EventBus.getDefault().post(NOISE_FRAGMENT_INT_MONITOR_EDIT, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
                }
                break;
        }
    }

    private void deleteSelect() {
        if (mPrivateDataList == null || mPrivateDataList.size() == 0) {
            showMessage("暂无可删除列表");
            return;
        }
        if (selectList.size() == 0) {
            showMessage("请选择删除选项");
            return;
        }
        showLoading("正在删除");
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                for (String id : selectList) {
                    for (int i = 0; i < mPrivateDataList.size(); i++) {
                        if (id.equals(mPrivateDataList.get(i).getId())) {
                            mPrivateDataList.remove(i);
                            for (int j = 0; j < mSample.getSamplingDetailResults().size(); j++) {
                                if (id.equals(mSample.getSamplingDetailResults().get(j).getId())) {
                                    mSample.getSamplingDetailResults().remove(j);
                                }
                            }
                        }
                    }
                }
                selectList.clear();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        hideLoading();
                    }
                });
            }
        });
    }


}
