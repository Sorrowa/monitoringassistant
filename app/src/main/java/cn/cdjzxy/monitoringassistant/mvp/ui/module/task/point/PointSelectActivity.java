package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PointSelectAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.Constants;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 点位选择 如果采样的监测性质是环境质量{@montype=3} 则在环境质量点位里面查
 */
public class PointSelectActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerView_point)
    RecyclerView recyclerViewPoint;
    @BindView(R.id.tabview)
    CustomTab tabview;

    private List<EnvirPoint> mEnvirPoints = new ArrayList<>();
    private PointSelectAdapter mPointSelectAdapter;

    private String projectId;
    private String tagId;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("点位选择");
        //        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        //        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                ArtUtils.makeText(getApplicationContext(), "搜索");
        //            }
        //        });

    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_point_select;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        projectId = getIntent().getStringExtra("projectId");
        tagId = getIntent().getStringExtra("tagId");
        initTabData();
        initPointData();
        getPointData(true);
    }


    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        tabview.setTabs("方案点位", "其他点位");
        tabview.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                getPointData(position == 0 ? true : false);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initPointData() {
        ArtUtils.configRecyclerView(recyclerViewPoint, new LinearLayoutManager(this));
        mPointSelectAdapter = new PointSelectAdapter(mEnvirPoints);
        mPointSelectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("AddressId", mEnvirPoints.get(position).getId());
                intent.putExtra("Address", mEnvirPoints.get(position).getName());
                intent.putExtra("AddressNo", mEnvirPoints.get(position).getCode());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerViewPoint.setAdapter(mPointSelectAdapter);
    }

    private void getPointData(boolean isRelationPoint) {
        List<String> pointIds = new ArrayList<>();
        //List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId),ProjectDetialDao.Properties.TagId.eq(tagId)).list();
//        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId)).list();
//        if (!CheckUtil.isEmpty(projectDetials)) {
//            for (ProjectDetial projectDetial : projectDetials) {
//                pointIds.add(projectDetial.getAddressId());
//                Log.d(TAG,projectDetial.getAddressId()+":"+projectDetial.getAddress());
//            }
//        }
        //List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId),ProjectDetialDao.Properties.TagId.eq(tagId)).list();
        //获取立项点位
        List<ProjectContent> projectContentList = DbHelpUtils.getProjectContentList(projectId);
        if (!CheckUtil.isEmpty(projectContentList)) {
            for (ProjectContent content : projectContentList) {
                for (String s : content.getAddressIds().split(",")) {
                    pointIds.add(s);
                    Log.d(TAG, s);
                }

            }
        }

        //List<EnvirPoint> envirPoints = null;
        List<EnvirPoint> envirPoints = new ArrayList<>();
        if (isRelationPoint) {
            if (CheckUtil.isEmpty(pointIds)) {
                envirPoints = new ArrayList<>();
            } else {
                if (pointIds.size() > Constants.DATA_PAGE_SIZE) {
                    int pages = pointIds.size() % Constants.DATA_PAGE_SIZE > 0 ? pointIds.size() / Constants.DATA_PAGE_SIZE + 1 : pointIds.size() / Constants.DATA_PAGE_SIZE;
                    for (int i = 0; i < pages; i++) {
                        int pageSize = Constants.DATA_PAGE_SIZE;
                        if ((i + 1) == pages) {
                            pageSize = pointIds.size() % Constants.DATA_PAGE_SIZE;
                        }
                        List<String> pointIdsTemp = pointIds.subList(i * Constants.DATA_PAGE_SIZE, i * Constants.DATA_PAGE_SIZE + pageSize);
                        List<EnvirPoint> tempPoints = DBHelper
                                .get()
                                .getEnvirPointDao()
                                .queryBuilder()
                                .where(EnvirPointDao.Properties.TagId.eq(tagId), EnvirPointDao.Properties.Id.in(pointIdsTemp))
                                //.where(EnvirPointDao.Properties.Id.in(pointIds))
                                .list();
                        for (EnvirPoint envirPoint : tempPoints) {
                            if (!envirPoints.contains(envirPoint)) {
                                envirPoints.add(envirPoint);
                            }
                        }
                    }
                } else {
                    envirPoints = DBHelper
                            .get()
                            .getEnvirPointDao()
                            .queryBuilder()
                            .where(EnvirPointDao.Properties.TagId.eq(tagId), EnvirPointDao.Properties.Id.in(pointIds))
                            //.where(EnvirPointDao.Properties.Id.in(pointIds))
                            .list();
                }
            }

        } else {
            if (CheckUtil.isEmpty(pointIds)) {
                envirPoints = DBHelper
                        .get()
                        .getEnvirPointDao()
                        .queryBuilder()
                        .where(EnvirPointDao.Properties.TagId.eq(tagId))
                        .list();
            } else {
                if (pointIds.size() > Constants.DATA_PAGE_SIZE) {
                    int pages = pointIds.size() % Constants.DATA_PAGE_SIZE > 0 ? pointIds.size() / Constants.DATA_PAGE_SIZE + 1 : pointIds.size() / Constants.DATA_PAGE_SIZE;
                    for (int i = 0; i < pages; i++) {
                        int pageSize = Constants.DATA_PAGE_SIZE;
                        if ((i + 1) == pages) {
                            pageSize = pointIds.size() % Constants.DATA_PAGE_SIZE;
                        }
                        List<String> pointIdsTemp = pointIds.subList(i * Constants.DATA_PAGE_SIZE, i * Constants.DATA_PAGE_SIZE + pageSize);
                        List<EnvirPoint> tempPoints = DBHelper
                                .get()
                                .getEnvirPointDao()
                                .queryBuilder()
                                .where(EnvirPointDao.Properties.TagId.eq(tagId), EnvirPointDao.Properties.Id.notIn(pointIdsTemp))
                                //.where(EnvirPointDao.Properties.Id.in(pointIds))
                                .list();
                        for (EnvirPoint envirPoint : tempPoints) {
                            if (!envirPoints.contains(envirPoint)) {
                                envirPoints.add(envirPoint);
                            }
                        }
                    }
                } else {
                    envirPoints = DBHelper
                            .get()
                            .getEnvirPointDao()
                            .queryBuilder()
                            .where(EnvirPointDao.Properties.TagId.eq(tagId), EnvirPointDao.Properties.Id.notIn(pointIds))
                            .list();
                }
            }

        }

        mEnvirPoints.clear();
        if (!CheckUtil.isEmpty(envirPoints)) {
            mEnvirPoints.addAll(envirPoints);
        }

        mPointSelectAdapter.notifyDataSetChanged();
    }


}
