package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;


import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.PointHolder;


/**
 * 主页tab
 */

public class PointAdapter extends DefaultAdapter<ProjectContent> {

    private Context mContext;
    private ItemAdapterOnClickListener listener;

    public PointAdapter(Context context, List<ProjectContent> infos,
                        ItemAdapterOnClickListener listener) {
        super(infos);
        this.mContext = context;

        this.listener = listener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point;
    }

    @Override
    public BaseHolder<ProjectContent> getHolder(View v, int viewType) {
        return new PointHolder(mContext,v,listener);
    }

    public interface ItemAdapterOnClickListener {
        void onItemOnClick(EnvirPoint point);
    }
}
