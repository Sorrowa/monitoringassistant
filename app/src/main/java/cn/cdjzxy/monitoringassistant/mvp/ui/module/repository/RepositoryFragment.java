package cn.cdjzxy.monitoringassistant.mvp.ui.module.repository;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.Constant;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.RepositoryAdapter;
import cn.cdjzxy.monitoringassistant.utils.FileUtils;
import cn.cdjzxy.monitoringassistant.utils.MediaFile;
import cn.cdjzxy.monitoringassistant.widgets.GridItemDecoration;

/**
 * 知识库
 */

public class RepositoryFragment extends BaseFragment {

    @BindView(R.id.btn_search)
    LinearLayout btnSearch;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    Unbinder unbinder;

    private List<File>        mFiles;
    private RepositoryAdapter mRepositoryAdapter;

    public RepositoryFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_repository, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRepositoryView();
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


    @OnClick(R.id.btn_search)
    public void onClick() {
        ArtUtils.startActivity(RepositorySearchActivity.class);
    }

    private void initRepositoryView() {
        mFiles = FileUtils.getFilesByDir(Constant.REPOSITORY_DIR);
        ArtUtils.configRecyclerView(recyclerview, new GridLayoutManager(this.getContext(), 3) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });
        recyclerview.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_12), 3));
        mRepositoryAdapter = new RepositoryAdapter(mFiles);
        mRepositoryAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                File file = mFiles.get(position);
                if (file.isDirectory()) {
                    startActivity(new Intent(getContext(), RepositoryActivity.class).putExtra("dir_path", file.getAbsolutePath()).putExtra("title", file.getName()));
                } else {
                    //                    startActivity(new Intent(mContext, FileDisplayActivity.class).putExtra("filepath", mFiles.get(pos).getAbsolutePath()).putExtra("title", mFiles.get(pos).getName()));
                    MediaFile.openFile(getContext(), file.getAbsolutePath());
                }
            }
        });
        recyclerview.setAdapter(mRepositoryAdapter);
    }
}
