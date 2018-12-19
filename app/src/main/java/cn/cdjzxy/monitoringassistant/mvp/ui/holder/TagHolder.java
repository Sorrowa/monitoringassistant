package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;

/**
 * 主页tab
 */

public class TagHolder extends BaseHolder<Tags> {


    @BindView(R.id.tv_name)
    TextView mTvName;

    public TagHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Tags data, int position) {
        mTvName.setText(data.getName());
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
    }
}
