package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.ProgramPointAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.Constants;
import cn.cdjzxy.monitoringassistant.widgets.GridItemDecoration;

/**
 * 修改方案点位选择
 */

public class ProgramPointSelectActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.program_recyclerView)
    RecyclerView programRecyclerView;
    @BindView(R.id.other_recyclerView)
    RecyclerView otherRecyclerView;

    private ProgramPointAdapter programPointAdapter;
    private ProgramPointAdapter otherPointAdapter;

    private String projectId;
    private String tagId;

    private List<EnvirPoint> programPointList = new ArrayList<>();
    private List<EnvirPoint> otherPointList = new ArrayList<>();
    private StringBuilder MonItemName = new StringBuilder("");
    private StringBuilder MonItemId = new StringBuilder("");
    private String[] selectItems;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("点位选择");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getMonItemsData();
                Intent intent = new Intent();
                intent.putExtra("MonItemName", MonItemName.toString());
                intent.putExtra("MonItemId", MonItemId.toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_program_point_select;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        //获取数据
        projectId = getIntent().getStringExtra("projectId");
        tagId = getIntent().getStringExtra("tagId");
        //设置view
        initProgramView();
        initOtherView();
        //获取数据
        getProgramPointData();
        getOtherPointData();


        String selectItemsStr = getIntent().getStringExtra("selectItems");
        if (!CheckUtil.isEmpty(selectItemsStr)) {
            selectItems = selectItemsStr.split(",");
        }

    }


    private void initProgramView() {
        ArtUtils.configRecyclerView(programRecyclerView, new GridLayoutManager(this, 8));
        programPointAdapter = new ProgramPointAdapter(programPointList);
        programPointAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                upateProgramPointState(position);
            }
        });
        programRecyclerView.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 8));
        programRecyclerView.setAdapter(programPointAdapter);
    }


    private void initOtherView() {
        ArtUtils.configRecyclerView(otherRecyclerView, new GridLayoutManager(this, 8));
        otherPointAdapter = new ProgramPointAdapter(otherPointList);
        otherPointAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                upateOtherPointState(position);
            }
        });
        otherRecyclerView.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 8));
        otherRecyclerView.setAdapter(otherPointAdapter);
    }

    /**
     * 获取方案点位
     */
    private void getProgramPointData() {
        List<String> pointIds = new ArrayList<>();
        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId)).list();
        if (!CheckUtil.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials) {
                pointIds.add(projectDetial.getAddressId());
            }
        }
        List<EnvirPoint> envirPoints = new ArrayList<>();
        if (!CheckUtil.isEmpty(pointIds)) {
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
                        .list();
            }
        }
        programPointList.clear();
        if (!CheckUtil.isEmpty(envirPoints)) {
            programPointList.addAll(envirPoints);
        }
        programPointAdapter.notifyDataSetChanged();
    }

    /**
     * 获取其他点位
     */
    private void getOtherPointData() {
        List<String> pointIds = new ArrayList<>();
        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId)).list();
        if (!CheckUtil.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials) {
                pointIds.add(projectDetial.getAddressId());
            }
        }
        List<EnvirPoint> envirPoints = new ArrayList<>();
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

        otherPointList.clear();
        if (!CheckUtil.isEmpty(envirPoints)) {
            otherPointList.addAll(envirPoints);
        }
        otherPointAdapter.notifyDataSetChanged();
    }


    /**
     * 更新方案点位选中状态
     *
     * @param position
     */
    private void upateProgramPointState(int position) {
        if (programPointList.get(position).isSelected()) {
            programPointList.get(position).setSelected(false);
        } else {
            programPointList.get(position).setSelected(true);
        }
        programPointAdapter.notifyDataSetChanged();
    }

    /**
     * 更新其他点位选中状态
     *
     * @param position
     */
    private void upateOtherPointState(int position) {
        if (otherPointList.get(position).isSelected()) {
            otherPointList.get(position).setSelected(false);
        } else {
            otherPointList.get(position).setSelected(true);
        }
        otherPointAdapter.notifyDataSetChanged();
    }


//    private void getMonItemsData() {
//
//        if (CheckUtil.isEmpty(mMonItemsSelected)) {
//            return;
//        }
//
//        for (MonItems monItems : mMonItemsSelected) {
//            MonItemName.append(monItems.getName() + ",");
//            MonItemId.append(monItems.getId() + ",");
//        }
//
//        if (MonItemName.lastIndexOf(",") > 0) {
//            MonItemName.deleteCharAt(MonItemName.lastIndexOf(","));
//        }
//
//        if (MonItemId.lastIndexOf(",") > 0) {
//            MonItemId.deleteCharAt(MonItemId.lastIndexOf(","));
//        }
//    }


}
