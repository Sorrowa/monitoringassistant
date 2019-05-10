package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.micheal.print.thread.ThreadPool;
import com.wonders.health.lib.base.integration.cache.Cache;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;

import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoiseSourceAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_INT_SOURCE_EDIT;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_SOURCE_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;


/**
 * 噪声——主要噪声源数据列表
 */
public class NoiseSourceListFragment extends BaseFragment implements IView {
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_add)
    LinearLayout linearAdd;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private NoiseSourceAdapter adapter;
    private List<NoisePrivateData.MianNioseSourceBean> list;
    public List<String> selectList;//选择的集合


    @Override

    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_source_list, null);
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

    @Subscriber(tag = NOISE_FRAGMENT_SOURCE_SHARE)
    private void initListData(String i) {
        list = new ArrayList<>();
        selectList = new ArrayList<>();
        if (!CheckUtil.isNull(mSample)) {
            if (mPrivateData.getMianNioseSource() != null && mPrivateData.getMianNioseSource().size() > 0) {
                list.addAll(mPrivateData.getMianNioseSource());
            }
        }
        for (NoisePrivateData.MianNioseSourceBean sourceBean : list) {
            if (sourceBean.isIsChecked()) {
                selectList.add(sourceBean.getGuid());
            }
        }
        if (adapter != null) adapter.refreshInfos(list);
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NoiseSourceAdapter(list, new NoiseSourceAdapter.ItemClickListener() {
            @Override
            public void onSelected(View view, int position) {
                upListSelectState(position);
            }

            @Override
            public void onClick(View view, int position) {
                getActivity().getSharedPreferences(NOISE_FRAGMENT_SHARE, 0).edit()
                        .putInt(NOISE_FRAGMENT_SOURCE_SHARE, position).commit();
                EventBus.getDefault().post(NOISE_FRAGMENT_INT_SOURCE_EDIT, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
            }
        });
        recyclerView.setAdapter(adapter);
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            initListData();
//            initRecyclerView();
////            if (adapter == null) {
////
////            } else {
////                initListData();
////                adapter.notifyDataSetChanged();
////            }
//        }
//    }

    /**
     * 更新选择状态
     *
     * @param position
     */
    private void upListSelectState(int position) {
        if (list.get(position) != null) {
            if (list.get(position).isIsChecked()) {
                list.get(position).setIsChecked(false);
                if (selectList != null && selectList.size() > 0) {
                    if (selectList.contains(list.get(position).getGuid())) {
                        selectList.remove(list.get(position).getGuid());
                    }
                }
            } else {
                list.get(position).setIsChecked(true);
                if (selectList == null) {
                    selectList = new ArrayList<>();
                }
                if (!selectList.contains(list.get(position).getGuid())) {
                    selectList.add(list.get(position).getGuid());
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(getContext(), message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }


    public void showLoading(String msg) {
        showLoadingDialog(msg);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }


    @OnClick({R.id.linear_delete, R.id.linear_add})
    public void onClick(View v) {
        hideSoftInput();
        if (!mSample.getIsCanEdit()){
            showMessage("提示：当前采样单，不支持编辑");
            return;
        }
        switch (v.getId()) {
            case R.id.linear_delete:
                deleteSelect();
                break;
            case R.id.linear_add:
                getActivity().getSharedPreferences(NOISE_FRAGMENT_SHARE, 0).edit()
                        .putInt(NOISE_FRAGMENT_SOURCE_SHARE, -1).commit();
                EventBus.getDefault().post(NOISE_FRAGMENT_INT_SOURCE_EDIT, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
                break;
        }
    }

    private void deleteSelect() {
        if (list == null || list.size() == 0) {
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
                if (list != null) {
                    for (String guId : selectList) {
                        for (int i = 0; i < list.size(); i++) {
                            if (guId.equals(list.get(i).getGuid())) {
                                list.remove(i);
                                mPrivateData.getMianNioseSource().remove(i);
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
