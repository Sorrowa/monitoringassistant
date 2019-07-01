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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.EnterRelatePointSelectAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PointAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PointItemAdapter;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.ProjectUtils;
import cn.cdjzxy.monitoringassistant.utils.RxDataTool;


/**
 * 主页tab
 */

public class PointHolder extends BaseHolder<ProjectContent> {

    @BindView(R.id.tv_project_name)
    TextView mTvName;
    @BindView(R.id.tv_project_time_range)
    TextView mTvTime;
    @BindView(R.id.tv_project_members)
    TextView mTvMember;
    @BindView(R.id.tv_edit_plan)
    TextView mTvEdit;

    @BindView(R.id.recyclerview_item)
    RecyclerView mRecyclerViewItem;

    private Context mContext;
    Project project;

    private PointAdapter.ItemAdapterOnClickListener listener;


    public PointHolder(Context context, View itemView,
                       PointAdapter.ItemAdapterOnClickListener listener) {
        super(itemView);
        this.mContext = context;
        this.listener = listener;
    }

    @Override
    public void setData(ProjectContent data, int position) {
        project = DbHelpUtils.getDbProject(data.getProjectId());
        if (UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Plan_Modify_Num)
                && project.getCanSamplingEidt()) {
            mTvEdit.setText("修改方案");
        } else {
            mTvEdit.setText("查看方案");
        }

        mTvName.setText(data.getTagName());
        mTvTime.setText(data.getDays() + "天" + data.getPeriod() + "次");
        initMonItemData(data);
        initPointItemData(data.getAddressIds());
    }

    /**
     * 初始化监测项目
     *
     * @param data ProjectContent
     */
    private void initMonItemData(ProjectContent data) {
        data = ProjectUtils.setProjectContentMonItemsData(data);
        mTvMember.setText(RxDataTool.isEmpty(data.getMonItemNames()) ? "" : data.getMonItemNames());
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
        ArtUtils.configRecyclerView(mRecyclerViewItem, new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return false;
            }
        });
        if (RxDataTool.isEmpty(pointId)) return;

        if (RxDataTool.isNull(project) || RxDataTool.isNull(project.getTypeCode())) return;
        if (project.getTypeCode() == 3) {//环境质量
            setEnvirPointData(pointId);
        } else {//污染源
            setEnterRelatePointData(pointId);
        }
    }

    /**
     * 设置污染源的点位
     *
     * @param pointId
     */
    private void setEnterRelatePointData(String pointId) {
        List<EnterRelatePoint> pointList = new ArrayList<>();
        if (pointId.contains(",")) {
            pointList = DbHelpUtils.getEnterRelatePointList(RxDataTool.strToList(pointId));
        }
        if (!RxDataTool.isNull(pointList)) {
            EnterRelatePointSelectAdapter adapter = new EnterRelatePointSelectAdapter(pointList);
            mRecyclerViewItem.setAdapter(adapter);
            adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int viewType, Object data, int position) {
                    EnterRelatePoint point = (EnterRelatePoint) data;
                        if (listener != null) {
                            EnvirPoint envirPoint = new EnvirPoint();
                            envirPoint.setId(point.getId());
                            envirPoint.setCode(point.getCode());
                            envirPoint.setLatitude(point.getLatitude());
                            envirPoint.setLongtitude(point.getLongtitude());
                            envirPoint.setName(point.getName());
                            envirPoint.setTagId(point.getTagId());
                            envirPoint.setTagName(point.getTagName());
                            envirPoint.setUpdateTime(point.getUpdateTime());
                            listener.onItemOnClick(envirPoint);
                        }
                }
            });
        }
    }

    /**
     * 设置环境质量点位
     *
     * @param pointId
     */
    private void setEnvirPointData(String pointId) {
        List<EnvirPoint> envirPoints = new ArrayList<>();
        envirPoints = DbHelpUtils.getEnvirPointList(RxDataTool.strToList(pointId));
        if (!RxDataTool.isNull(envirPoints)) {
            PointItemAdapter mPointItemAdapter = new PointItemAdapter(envirPoints);
            mRecyclerViewItem.setAdapter(mPointItemAdapter);
            mPointItemAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int viewType, Object data, int position) {
                    EnvirPoint pointSelect = (EnvirPoint) data;
                    if (listener != null) {
                        listener.onItemOnClick(pointSelect);
                    }
                }
            });
        }
    }


}
