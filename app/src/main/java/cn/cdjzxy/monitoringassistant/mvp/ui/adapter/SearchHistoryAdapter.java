package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.SearchHistoryHolder;


/**
 * 主页tab
 */

public class SearchHistoryAdapter extends DefaultAdapter<String> {

    private OnDeleteClickListener mOnDeleteClickListener;

    public SearchHistoryAdapter(List<String> infos,OnDeleteClickListener onDeleteClickListener) {
        super(infos);
        this.mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_search_history;
    }

    @Override
    public BaseHolder<String> getHolder(View v, int viewType) {
        return new SearchHistoryHolder(v, mOnDeleteClickListener);
    }


    public interface OnDeleteClickListener {
        void onDelete(int position);
    }
}
