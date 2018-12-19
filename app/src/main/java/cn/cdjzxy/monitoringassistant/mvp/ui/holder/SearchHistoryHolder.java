package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.db.annotation.Check;
import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SearchHistoryAdapter;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

/**
 * 主页tab
 */

public class SearchHistoryHolder extends BaseHolder<String> {

    @BindView(R.id.tv_name)
    TextView  mTvName;
    @BindView(R.id.iv_delete)
    ImageView mIvDelete;

    private SearchHistoryAdapter.OnDeleteClickListener mOnDeleteClickListener;


    public SearchHistoryHolder(View itemView, SearchHistoryAdapter.OnDeleteClickListener onDeleteClickListener) {
        super(itemView);
        this.mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public void setData(String data, final int position) {
        mTvName.setText(data);
        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CheckUtil.isNull(mOnDeleteClickListener)) {
                    mOnDeleteClickListener.onDelete(position);
                }
            }
        });
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
        this.mIvDelete = null;
    }
}
