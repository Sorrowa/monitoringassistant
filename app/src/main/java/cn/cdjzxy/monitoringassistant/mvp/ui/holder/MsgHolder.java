package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;

/**
 * 主页tab
 */

public class MsgHolder extends BaseHolder<Tab> {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_content)
    TextView mTvContent;

    public MsgHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Tab data, int position) {

    }

    @Override
    protected void onRelease() {
        this.mTvTitle = null;
        this.mTvTime = null;
        this.mTvContent = null;

    }
}
