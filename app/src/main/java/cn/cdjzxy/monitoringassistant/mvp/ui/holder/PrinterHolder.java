package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;

/**
 * 主页tab
 */

public class PrinterHolder extends BaseHolder<BluetoothDevice> {

    @BindView(R.id.tv_printer_name)
    TextView tv_printer_name;
    @BindView(R.id.tv_connect)
    TextView tv_connect;
    @BindView(R.id.iv_connect_status)
    ImageView iv_connect_status;

    public PrinterHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(BluetoothDevice device, int position) {
        tv_printer_name.setText(TextUtils.isEmpty(device.getName()) ? "未知设备" : device.getName());

        if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
            tv_connect.setVisibility(View.GONE);
            iv_connect_status.setVisibility(View.VISIBLE);
        } else {
            tv_connect.setVisibility(View.VISIBLE);
            iv_connect_status.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRelease() {
        tv_printer_name = null;
        tv_connect = null;
        iv_connect_status = null;

    }
}
