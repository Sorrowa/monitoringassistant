package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print.LabelPrintDeviceActivity;

public class BluetoothDeviceHolder extends BaseHolder<LabelPrintDeviceActivity.DeviceInfo> {

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_connect)
    TextView mTvConnect;

    public BluetoothDeviceHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(LabelPrintDeviceActivity.DeviceInfo data, int position) {
        mTvName.setText(data.getName());
        mTvConnect.setText(data.getStatusName());
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
        this.mTvConnect = null;
    }
}
