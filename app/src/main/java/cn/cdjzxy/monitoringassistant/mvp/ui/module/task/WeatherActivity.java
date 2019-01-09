package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Weather;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WeatherAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

public class WeatherActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tabview)
    CustomTab tabview;

    private List<String> weatherList = new ArrayList<>();
    private WeatherAdapter weatherAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("天气选择");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_weather;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tabview.setVisibility(View.GONE);
        initMethodData();
        //tagId = getIntent().getStringExtra("tagId");
        List<Weather> weathers = DBHelper.get().getWeatherDao().queryBuilder().list();
        if (!CheckUtil.isEmpty(weathers)) {
            for (Weather weather : weathers) {
                weatherList.addAll(weather.getWeathers());
            }
        }

        weatherAdapter.notifyDataSetChanged();

    }

    /**
     * 初始化数据
     */
    private void initMethodData() {
        ArtUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this));
        weatherAdapter = new WeatherAdapter(weatherList);
        weatherAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("weather", weatherList.get(position));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerView.setAdapter(weatherAdapter);
    }


}
