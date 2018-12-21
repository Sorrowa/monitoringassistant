package cn.cdjzxy.monitoringassistant.mvp.ui.module.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.webview.WebFragment;

public class AboutActivity extends BaseTitileActivity<ApiPresenter> {

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("关于我们");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_about;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.darkMode(this, false);
        Bundle mBundle = new Bundle();
        mBundle.putString(WebFragment.URL_KEY, "http://www.cdjzxy.cn/");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, WebFragment.getInstance(mBundle), WebFragment.class.getName());
        ft.commit();

    }


}
