package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.bluetooth.BluetoothDevice;
import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.PrinterHolder;


/**
 * 主页tab
 */

public class PrinterAdapter extends DefaultAdapter<BluetoothDevice> {


    public PrinterAdapter(List<BluetoothDevice> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_form_print;
    }

    @Override
    public BaseHolder<BluetoothDevice> getHolder(View v, int viewType) {
        return new PrinterHolder(v);
    }
}
