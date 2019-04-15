package cn.cdjzxy.monitoringassistant.mvp.ui.module.wander;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.activity.BaseTitleActivity;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;

/**
 * 流转任务详情
 */
public class WanderTaskDetailActivity extends BaseTitleActivity<ApiPresenter> implements IView {


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wander_task_detail;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
