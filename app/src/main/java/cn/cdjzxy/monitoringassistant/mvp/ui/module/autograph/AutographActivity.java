package cn.cdjzxy.monitoringassistant.mvp.ui.module.autograph;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.mvp.IPresenter;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

public class AutographActivity extends BaseTitileActivity {


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_autograph;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("签名");
    }
}
