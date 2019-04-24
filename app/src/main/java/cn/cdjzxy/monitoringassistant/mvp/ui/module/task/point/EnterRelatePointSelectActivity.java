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
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnterRelatePointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnterRelatePointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.EnterRelatePointSelectAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PointSelectAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.Constants;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

public class EnterRelatePointSelectActivity extends BaseTitileActivity implements IView {


    @BindView(R.id.recyclerView_point)
    RecyclerView recyclerViewPoint;
    @BindView(R.id.tabview)
    CustomTab tabview;
    private List<EnterRelatePoint> list;
    private EnterRelatePointSelectAdapter adapter;

    private String projectId;
    private String mRcvId;


    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_point_select;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        projectId = getIntent().getStringExtra("projectId");
        mRcvId = getIntent().getStringExtra("rcvId");
        list = new ArrayList<>();
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
        adapter = new EnterRelatePointSelectAdapter(list);
        adapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("AddressId", list.get(position).getId());
                intent.putExtra("Address", list.get(position).getName());
                intent.putExtra("AddressNo", list.get(position).getCode());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerViewPoint.setAdapter(adapter);
    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("点位选择");
    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    private void getPointData(boolean isRelationPoint) {
        List<String> pointIds = new ArrayList<>();
        //List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId),ProjectDetialDao.Properties.TagId.eq(tagId)).list();
        //获取立项点位
        // List<ProjectContent> projectContentList= DBHelper.get().getProjectContentDao().queryBuilder().build().list();
        List<ProjectContent> projectContentList = DBHelper.get().getProjectContentDao().queryBuilder().where(ProjectContentDao.Properties.ProjectId.eq(projectId)).list();
        if (!CheckUtil.isEmpty(projectContentList)) {
            for (ProjectContent content : projectContentList) {
                for (String s : content.getAddressIds().split(",")) {
                    pointIds.add(s);
                    Log.d(TAG, s);
                }

            }
        }

        //List<EnterRelatePoint> envirPoints = null;
        List<EnterRelatePoint> envirPoints = new ArrayList<>();
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
                        List<EnterRelatePoint> tempPoints = DBHelper
                                .get()
                                .getEnterRelatePointDao()
                                .queryBuilder()
                                .where(EnterRelatePointDao.Properties.EnterPriseId.eq(mRcvId), EnterRelatePointDao.Properties.Id.in(pointIdsTemp))
                                //.where(EnterRelatePointDao.Properties.Id.in(pointIds))
                                .list();
                        for (EnterRelatePoint EnterRelatePoint : tempPoints) {
                            if (!envirPoints.contains(EnterRelatePoint)) {
                                envirPoints.add(EnterRelatePoint);
                            }
                        }
                    }
                } else {
                    envirPoints = DBHelper
                            .get()
                            .getEnterRelatePointDao()
                            .queryBuilder()
                            .where(EnterRelatePointDao.Properties.EnterPriseId.eq(mRcvId), EnterRelatePointDao.Properties.Id.in(pointIds))
                            //.where(EnterRelatePointDao.Properties.Id.in(pointIds))
                            .list();
                }
            }

        } else {
            if (CheckUtil.isEmpty(pointIds)) {
                envirPoints = DBHelper
                        .get()
                        .getEnterRelatePointDao()
                        .queryBuilder()
                        .where(EnterRelatePointDao.Properties.EnterPriseId.eq(mRcvId))
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
                        List<EnterRelatePoint> tempPoints = DBHelper
                                .get()
                                .getEnterRelatePointDao()
                                .queryBuilder()
                                .where(EnterRelatePointDao.Properties.EnterPriseId.eq(mRcvId), EnterRelatePointDao.Properties.Id.notIn(pointIdsTemp))
                                //.where(EnterRelatePointDao.Properties.Id.in(pointIds))
                                .list();
                        for (EnterRelatePoint EnterRelatePoint : tempPoints) {
                            if (!envirPoints.contains(EnterRelatePoint)) {
                                envirPoints.add(EnterRelatePoint);
                            }
                        }
                    }
                } else {
                    envirPoints = DBHelper
                            .get()
                            .getEnterRelatePointDao()
                            .queryBuilder()
                            .where(EnterRelatePointDao.Properties.EnterPriseId.eq(mRcvId), EnterRelatePointDao.Properties.Id.notIn(pointIds))
                            .list();
                }
            }

        }

        list.clear();
        if (!CheckUtil.isEmpty(envirPoints)) {
            list.addAll(envirPoints);
        }

        adapter.notifyDataSetChanged();
    }
}
