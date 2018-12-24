package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PointItemAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

/**
 * 主页tab
 */

public class PointHolder extends BaseHolder<ProjectDetial> {

    @BindView(R.id.tv_project_name)
    TextView mTvName;
    @BindView(R.id.tv_project_time_range)
    TextView mTvTime;
    @BindView(R.id.tv_project_members)
    TextView mTvMember;

    @BindView(R.id.recyclerview_item)
    RecyclerView mRecyclerViewItem;

    private Context mContext;

    private PointItemAdapter mPointItemAdapter;

    public PointHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
    }

    @Override
    public void setData(ProjectDetial data, int position) {
        mTvName.setText(data.getTagName());
        mTvTime.setText(data.getDays() + "天" + data.getPeriod() + "次");
        mTvMember.setText(data.getMonItemName());
        initPointItemData(data.getAddressId());
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
        this.mTvTime = null;
        this.mTvMember = null;
        this.mRecyclerViewItem = null;
    }


    /**
     * 初始化Tab数据
     */
    private void initPointItemData(String pointId) {

        List<EnvirPoint> envirPoints = DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.Properties.Id.eq(pointId)).list();

        if (!CheckUtil.isEmpty(envirPoints)) {
            ArtUtils.configRecyclerView(mRecyclerViewItem, new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {//设置RecyclerView不可滑动
                    return false;
                }
            });
            mPointItemAdapter = new PointItemAdapter(envirPoints);
            mRecyclerViewItem.setAdapter(mPointItemAdapter);
        }

    }
}
