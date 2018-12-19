package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.PrinterHolder;


/**
 * 主页tab
 */

public class PrinterAdapter extends DefaultAdapter<Tab> {


    public PrinterAdapter(List<Tab> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_form_print;
    }

    @Override
    public BaseHolder<Tab> getHolder(View v, int viewType) {
        return new PrinterHolder(v);
    }
}