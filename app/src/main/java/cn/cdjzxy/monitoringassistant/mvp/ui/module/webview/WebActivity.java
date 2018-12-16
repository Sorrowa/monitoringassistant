package cn.cdjzxy.monitoringassistant.mvp.ui.module.webview;


import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.wonders.health.lib.base.mvp.IPresenter;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseActivity;


/**
 * 通用显示H5页面
 */
public class WebActivity extends BaseActivity {

    private WebFragment mWebFragment;

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_web;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        openFragment();
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
        return mWebFragment.onFragmentKeyDown(keyCode, event);
    }

    public String getUrl() {
        return getIntent().getStringExtra(WebFragment.URL_KEY);
    }

}
