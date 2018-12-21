package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;

/**
 * 标签打印
 */

public class LabelPrintActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private TaskAdapter mTaskAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("采样任务");
        titleBar.setRightTextDrawable(R.mipmap.ic_form_print);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        return R.layout.activity_task;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initTaskData();
    }

    /**
     * 初始化Tab数据
     */
    private void initTaskData() {
//        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
//            @Override
//            public boolean canScrollVertically() {//设置RecyclerView不可滑动
//                return true;
//            }
//        });
//
//        List<Tab> mTabs = new ArrayList<>();
//
//        for (int i = 0; i < 10; i++) {
//            mTabs.add(new Tab());
//        }
//
//        mTaskAdapter = new TaskAdapter(mTabs);
//        mTaskAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClick(View view, int viewType, Object data, int position) {
//                ArtUtils.startActivity(TaskDetailActivity.class);
//            }
//        });
//        recyclerview.setAdapter(mTaskAdapter);
    }

}
