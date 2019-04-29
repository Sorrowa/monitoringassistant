package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PointItemAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.NavigationActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointActivity;
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
    @BindView(R.id.tv_edit_plan)
    TextView mTvEdit;

    @BindView(R.id.recyclerview_item)
    RecyclerView mRecyclerViewItem;

    private Context mContext;

    private PointItemAdapter mPointItemAdapter;

    private boolean isCanEdit;

    public PointHolder(Context context, View itemView, boolean isCanEdit) {
        super(itemView);
        this.mContext = context;
        this.isCanEdit = isCanEdit;
    }

    @Override
    public void setData(ProjectDetial data, int position) {
        if (!isCanEdit) {
            mTvEdit.setVisibility(View.GONE);
        } else {
            mTvEdit.setVisibility(View.VISIBLE);
        }

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
        List<EnvirPoint> envirPoints = new ArrayList<>();

        if (pointId.contains(",")) {
            envirPoints = DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.Properties.Id.in(pointId.split(","))).list();
        } else {
            envirPoints = DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.Properties.Id.eq(pointId)).list();
        }

        if (!CheckUtil.isEmpty(envirPoints)) {
            ArtUtils.configRecyclerView(mRecyclerViewItem, new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {//设置RecyclerView不可滑动
                    return false;
                }
            });
            mPointItemAdapter = new PointItemAdapter(envirPoints);
            mRecyclerViewItem.setAdapter(mPointItemAdapter);
            mPointItemAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int viewType, Object data, int position) {
                    EnvirPoint pointSelect = (EnvirPoint) data;
                    routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09LL, pointSelect);

                }
            });
        }

    }

    private void routeplanToNavi(final int coType, EnvirPoint pointSelect) {

        PointActivity pointActivity = (PointActivity) mContext;
        BDLocation bdLocation = pointActivity.bdLocation;
        if (bdLocation == null) {
            ArtUtils.makeText(mContext, "未定位到当前位置，请重试");
            return;
        }

        ArtUtils.makeText(mContext, bdLocation.getLongitude() + "，" + bdLocation.getLatitude());
        Log.i(TAG, "routeplanToNavi: +导航定位：\n经度"+bdLocation.getLongitude()+"\n纬度"+bdLocation.getLatitude());

        BNRoutePlanNode sNode = new BNRoutePlanNode(bdLocation.getLongitude(), bdLocation.getLatitude(), "", "", coType);
        BNRoutePlanNode eNode = new BNRoutePlanNode(pointSelect.getLongtitude(), pointSelect.getLatitude(), pointSelect.getName(), pointSelect.getName(), coType);

        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
        list.add(sNode);
        list.add(eNode);

        final BNRoutePlanNode mStartNode = sNode;

        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_TIME_FIRST,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(mContext, "算路开始", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(mContext, "算路成功", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(mContext, "算路失败", Toast.LENGTH_SHORT)
                                        .show();
                                break;
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(mContext, "算路成功准备进入导航", Toast.LENGTH_SHORT)
                                        .show();
                                Intent intent = new Intent(mContext,
                                        NavigationActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("endPoint", mStartNode);
                                intent.putExtras(bundle);
                                ArtUtils.startActivity(intent);
                                break;
                            default:
                                // nothing
                                break;
                        }
                    }
                });
    }
}
