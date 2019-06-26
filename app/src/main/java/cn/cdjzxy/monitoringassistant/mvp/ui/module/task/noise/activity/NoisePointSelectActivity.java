package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.baidu.navisdk.ui.routeguide.mapmode.subview.G;
import com.wonders.health.lib.base.mvp.IPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WeatherAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

public class NoisePointSelectActivity extends BaseTitileActivity {
    @BindView(R.id.tabview)
    CustomTab customTab;
    @BindView(R.id.recyclerView_point)
    RecyclerView recyclerView;

    private List<String> weatherList;
    private WeatherAdapter weatherAdapter;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_device;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        customTab.setVisibility(View.GONE);
        weatherList = new ArrayList<>();
        initListData();
    }

    private void initListData() {

    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("测点位置");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
    }

    private void onBack() {
    }
}
