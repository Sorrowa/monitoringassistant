package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.content.Context;
import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.PointHolder;


/**
 * 主页tab
 */

public class PointAdapter extends DefaultAdapter<ProjectDetial> {

    private Context mContext;

    public PointAdapter(Context context, List<ProjectDetial> infos) {
        super(infos);
        this.mContext = context;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_point;
    }

    @Override
    public BaseHolder<ProjectDetial> getHolder(View v, int viewType) {
        return new PointHolder(mContext, v);
    }
}
