package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.micheal.print.bean.BleDeviceInfo;
import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;

public class BluetoothDeviceHolder extends BaseHolder<BleDeviceInfo> {

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_connect)
    TextView mTvConnect;

    public BluetoothDeviceHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(BleDeviceInfo data, int position) {
        mTvName.setText(data.getName()+String.format(" [%s]",data.getAddress()));
        mTvConnect.setText(data.getStatusName());
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
        this.mTvConnect = null;
    }
}
