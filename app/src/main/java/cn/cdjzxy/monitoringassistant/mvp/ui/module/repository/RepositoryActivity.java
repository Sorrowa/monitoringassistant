package cn.cdjzxy.monitoringassistant.mvp.ui.module.repository;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.RepositoryAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import cn.cdjzxy.monitoringassistant.utils.FileUtils;
import cn.cdjzxy.monitoringassistant.utils.MediaFile;
import cn.cdjzxy.monitoringassistant.widgets.GridItemDecoration;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class RepositoryActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.tv_desc)
    TextView     tvDesc;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private String            dir;
    private List<File>        mFiles;
    private RepositoryAdapter mRepositoryAdapter;

    private TitleBarView titleBar;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        this.titleBar = titleBar;
        titleBar.setTitleMainText("知识库");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_repository;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);
        dir = getIntent().getStringExtra("dir_path");
        titleBar.setTitleMainText(getIntent().getStringExtra("title"));
        tvDesc.setText("更新时间：" + DateUtils.getDate() + "\n" + "暂无文件目录说明");
        initRepositoryView();

    }

    private void initRepositoryView() {
        mFiles = FileUtils.getFilesByDir(dir);
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
                    startActivity(new Intent(RepositoryActivity.this, RepositoryActivity.class).putExtra("dir_path", file.getAbsolutePath()).putExtra("title", file.getName()));
                } else {
                    //                    startActivity(new Intent(mContext, FileDisplayActivity.class).putExtra("filepath", mFiles.get(pos).getAbsolutePath()).putExtra("title", mFiles.get(pos).getName()));
                    MediaFile.openFile(RepositoryActivity.this, file.getAbsolutePath());
                }
            }
        });
        recyclerview.setAdapter(mRepositoryAdapter);
    }


}
