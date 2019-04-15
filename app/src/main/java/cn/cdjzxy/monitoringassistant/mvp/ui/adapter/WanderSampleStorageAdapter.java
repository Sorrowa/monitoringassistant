package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.WanderSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.WanderDetailHolder;

public class WanderSampleStorageAdapter extends DefaultAdapter<WanderSampleStorage> {
    public WanderSampleStorageAdapter(List<WanderSampleStorage> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<WanderSampleStorage> getHolder(View v, int viewType) {
        return new WanderDetailHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_wander_task_detail;
    }
}
