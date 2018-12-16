package cn.cdjzxy.monitoringassistant.mvp.ui.module.repository;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.Constant;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.RepositoryAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.MediaFile;
import cn.cdjzxy.monitoringassistant.widgets.ClearEditText;
import cn.cdjzxy.monitoringassistant.widgets.GridItemDecoration;

public class RepositorySearchActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.et_keyword)
    ClearEditText etKeyword;
    @BindView(R.id.recyclerview)
    RecyclerView  recyclerview;

    private List<File> mFiles    = new ArrayList<>();
    private List<File> mAllFiles = new ArrayList<>();
    private RepositoryAdapter mRepositoryAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("搜索");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_repository_search;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    search(s.toString());
                } else {
                    mFiles.clear();
                    mRepositoryAdapter.notifyDataSetChanged();
                }
            }
        });

        getAllFiles();
        initRepositoryView();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @OnClick(R.id.iv_search)
    public void onClick() {
        search(etKeyword.getText().toString());
    }

    private void search(String keyword) {
        mFiles.clear();
        for (File file : mAllFiles) {
            if (file.getName().contains(keyword)) {
                mFiles.add(file);
            }
        }
        mRepositoryAdapter.notifyDataSetChanged();
    }

    private void getAllFiles() {
        File rootFile = new File(Constant.REPOSITORY_DIR);
        getFilesAll(rootFile, mAllFiles);
    }

    public void getFilesAll(File rootFile, List<File> allFiles) {
        File[] files = rootFile.listFiles();
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            allFiles.add(files[i]);
            if (file.isDirectory()) {
                getFilesAll(file, allFiles);
            }
        }
    }

    private void initRepositoryView() {
        ArtUtils.configRecyclerView(recyclerview, new GridLayoutManager(this, 3) {
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
                    startActivity(new Intent(RepositorySearchActivity.this, RepositoryActivity.class).putExtra("dir_path", file.getAbsolutePath()).putExtra("title", file.getName()));
                } else {
                    MediaFile.openFile(RepositorySearchActivity.this, file.getAbsolutePath());
                }
            }
        });
        recyclerview.setAdapter(mRepositoryAdapter);
    }
}
