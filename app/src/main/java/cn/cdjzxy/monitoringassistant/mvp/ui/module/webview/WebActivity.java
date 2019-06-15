package cn.cdjzxy.monitoringassistant.mvp.ui.module.webview;


import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.mvp.IPresenter;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;


/**
 * 通用显示H5页面
 */
public class WebActivity extends BaseTitileActivity {

    private WebFragment mWebFragment;
    public static final String KEY_TITLE = "keyTitle";



    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_web;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        openFragment();
    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    private void openFragment() {
        Bundle mBundle = new Bundle();
        mBundle.putString(WebFragment.URL_KEY, getUrl());
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, mWebFragment = WebFragment.getInstance(mBundle), WebFragment.class.getName());
        ft.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //传递按键到web层
        boolean result = mWebFragment.onFragmentKeyDown(keyCode, event);

        //web层未处理返回操作，已返回到首页，再次返回则退出当前activity
        if (!result && keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }

        return result;
    }

    public String getUrl() {
        if (getIntent() != null) {
            return getIntent().getStringExtra(WebFragment.URL_KEY);
        } else {
            return "";
        }

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (getIntent() != null)
            titleBar.setTitleMainText(getIntent().getStringExtra(KEY_TITLE));
        else
            titleBar.setTitleMainText("嘉泽云监测");
    }



}
