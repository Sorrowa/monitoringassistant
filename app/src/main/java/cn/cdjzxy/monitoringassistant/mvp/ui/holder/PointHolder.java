package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PointItemAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;

/**
 * 主页tab
 */

public class PointHolder extends BaseHolder<Tab> {

    @BindView(R.id.recyclerview_item)
    RecyclerView mRecyclerViewItem;

    //    @BindView(R.id.tab_name)
    //    TextView mTabName;

    private Context mContext;

    private PointItemAdapter mPointItemAdapter;

    public PointHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
    }

    @Override
    public void setData(Tab data, int position) {
        //        mTabName.setText(data.getTabName());
        //        if (data.isSelected()) {
        //            itemView.setBackgroundColor(Color.parseColor("#c7e4ff"));
        //        } else {
        //            itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        //        }
        initPointItemData();
    }

    @Override
    protected void onRelease() {

        //        this.mTabName = null;

    }


    /**
     * 初始化Tab数据
     */
    private void initPointItemData() {
        ArtUtils.configRecyclerView(mRecyclerViewItem, new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return false;
            }
        });

        List<Tab> mTabs = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            mTabs.add(new Tab());
        }

        mPointItemAdapter = new PointItemAdapter(mTabs);
        mPointItemAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                ArtUtils.startActivity(TaskDetailActivity.class);
            }
        });
        mRecyclerViewItem.setAdapter(mPointItemAdapter);
    }
}
