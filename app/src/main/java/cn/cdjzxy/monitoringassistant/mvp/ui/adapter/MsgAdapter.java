package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.MsgHolder;


/**
 * 主页tab
 */

public class MsgAdapter extends DefaultAdapter<Msg> {


    public MsgAdapter(List<Msg> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_msg;
    }

    @Override
    public BaseHolder<Msg> getHolder(View v, int viewType) {
        return new MsgHolder(v);
    }
}
