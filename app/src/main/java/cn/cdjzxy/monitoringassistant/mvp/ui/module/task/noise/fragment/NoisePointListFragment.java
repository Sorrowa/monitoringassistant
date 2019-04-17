package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.content.Context;
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
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoisePointAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoiseSourceAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;
/**
 * 噪声——监测点位列表
 */
public class NoisePointListFragment extends BaseFragment implements IView {
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_add)
    LinearLayout linearAdd;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private List<NoisePrivateData.MianNioseAddrBean> list;
    private NoisePointAdapter adapter;


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_source_list, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        list = new ArrayList<>();

        if (!CheckUtil.isNull(mSample)) {
            Gson gson = new Gson();

            if (mPrivateData.getMianNioseSource() != null && mPrivateData.getMianNioseSource().size() > 0) {
                list.addAll(mPrivateData.getMianNioseAddr());
            }
        }
        initRecyclerView();
    }
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new NoisePointAdapter(list, new NoisePointAdapter.ItemClickListener() {
            @Override
            public void onSelected(View view, int position) {
                showMessage(position+"");
            }

            @Override
            public void onClick(View view, int position) {
                EventBus.getDefault().post(6, EventBusTags.TAG_NOISE_FRAGMENT_TYPE_POINT_EDIT);
                getActivity().getSharedPreferences("noise", 0).edit()
                        .putInt(EventBusTags.TAG_NOISE_FRAGMENT_TYPE_POINT_EDIT, position).commit();
            }
        });
        recyclerView.setAdapter(adapter);
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

    @OnClick({R.id.linear_delete, R.id.linear_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_delete:
                showMessage("待开发");
                break;
            case R.id.linear_add:
                showMessage("待开发");
                break;
        }
    }
}
