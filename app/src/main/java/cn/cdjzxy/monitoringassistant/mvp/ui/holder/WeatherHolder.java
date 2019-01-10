package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;

public class WeatherHolder extends BaseHolder<String> {


    @BindView(R.id.tv_name)
    TextView mTvName;

    public WeatherHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(String data, int position) {
        mTvName.setText(data);
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
    }
}
