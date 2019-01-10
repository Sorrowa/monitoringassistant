package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.LabelPrintDeviceActivity;

public class LabelHolder extends BaseHolder<Object> {

//    @BindView(R.id.tv_name)
//    TextView mTvName;

    public LabelHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Object data, int position) {
//        mTvName.setText(data.getName());
    }

    @Override
    protected void onRelease() {
//        this.mTvName = null;
    }
}
