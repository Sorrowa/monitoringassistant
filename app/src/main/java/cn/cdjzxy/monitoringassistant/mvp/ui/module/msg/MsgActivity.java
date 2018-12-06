package cn.cdjzxy.monitoringassistant.mvp.ui.module.msg;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.presenter.AppPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

public class MsgActivity extends BaseTitileActivity<AppPresenter> {

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.getLinearLayout(Gravity.LEFT).removeViewAt(1);
        titleBar.setTitleMainText("消息");
        titleBar.setTitleMainTextColor(Color.WHITE);
    }

    @Nullable
    @Override
    public AppPresenter obtainPresenter() {
        return new AppPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_msg;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);


    }


}
