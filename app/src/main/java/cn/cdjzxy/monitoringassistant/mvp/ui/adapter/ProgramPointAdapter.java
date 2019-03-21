package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.ProgramPointHolder;

/**
 * 方案点位adapter
 */
public class ProgramPointAdapter extends DefaultAdapter<EnvirPoint> {


    public ProgramPointAdapter(List<EnvirPoint> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_program_point;
    }

    @Override
    public BaseHolder<EnvirPoint> getHolder(View v, int viewType) {
        return new ProgramPointHolder(v);
    }
}
