package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;

/**
 * 主页tab
 */

public class MonItemHolder extends BaseHolder<MonItems> {


    @BindView(R.id.tv_name)
    TextView mTvName;

    public MonItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(MonItems data, int position) {
        mTvName.setText(data.getName());
    }

    @Override
    protected void onRelease() {

        this.mTvName = null;

    }
}
