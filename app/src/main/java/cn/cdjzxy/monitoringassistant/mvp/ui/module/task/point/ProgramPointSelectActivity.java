package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.ProgramPointAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;
import cn.cdjzxy.monitoringassistant.utils.RxDataTool;
import cn.cdjzxy.monitoringassistant.widgets.GridItemDecoration;


/**
 * 修改方案点位选择
 */

public class ProgramPointSelectActivity extends BaseTitileActivity {


    @BindView(R.id.program_recyclerView)
    RecyclerView programRecyclerView;
    @BindView(R.id.other_recyclerView)
    RecyclerView otherRecyclerView;

    private ProgramPointAdapter programPointAdapter;
    private ProgramPointAdapter otherPointAdapter;

    private String projectId;
    private String tagId;
    private String selectedAddressIds = "";
    private String selectedAddress = "";
    private String rcvId;

    private List<EnvirPoint> programPointList;
    private List<EnvirPoint> otherPointList;
    private boolean isRcv;//true污染源 false环境质量

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        programPointList = new ArrayList<>();
        otherPointList = new ArrayList<>();
        titleBar.setTitleMainText("点位选择");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleBar.setRightTextBackgroundColor(R.color.white);
        titleBar.addRightAction(titleBar.new TextAction("确定选择", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sureSelect();
            }
        }));
    }

    /**
     * 确定选择内容
     */
    private void sureSelect() {
        setSelectedPoint();
        Intent intent = new Intent();
        intent.putExtra("Address", selectedAddress);
        intent.putExtra("AddressId", selectedAddressIds);
        setResult(Activity.RESULT_OK, intent);
        finish();
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
        selectedAddressIds = getIntent().getStringExtra("addressId");
        selectedAddress = getIntent().getStringExtra("addressName");
        isRcv = getIntent().getBooleanExtra("isRcv", false);
        if (isRcv) {
            rcvId = getIntent().getStringExtra("rcvId");
        }
        //设置view
        initProgramView();
        initOtherView();
        //获取数据
        if (isRcv) {//企业点位
            getProgramRelatePoint();
            getOtherRelatePointData();
        } else {//环境质量点位
            getProgramPointData();
            getOtherPointData();
        }
    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    private void initProgramView() {
        ArtUtils.configRecyclerView(programRecyclerView, new GridLayoutManager(this, 8));
        programPointAdapter = new ProgramPointAdapter(programPointList);
        programPointAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateProgramPointState(position);
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

    private void getProgramRelatePoint() {
        List<String> pointIds = new ArrayList<>();
        List<ProjectContent> projectContentList = DbHelpUtils.getProjectContentList(projectId);
        if (!RxDataTool.isEmpty(projectContentList)) {
            for (ProjectContent content : projectContentList) {
                if (content.getTagId().equals(tagId)) {
                    for (String s : content.getAddressIds().split(",")) {
                        pointIds.add(s);
                        Log.d(TAG, s);
                    }
                    break;
                }
            }
        }
        List<EnvirPoint> envirPoints = new ArrayList<>();
        if (!RxDataTool.isEmpty(pointIds)) {
            if (pointIds.size() > RxDataTool.DATA_PAGE_SIZE) {
                int pages = pointIds.size() % RxDataTool.DATA_PAGE_SIZE > 0 ? pointIds.size() /
                        RxDataTool.DATA_PAGE_SIZE + 1 :
                        pointIds.size() / RxDataTool.DATA_PAGE_SIZE;
                for (int i = 0; i < pages; i++) {
                    int pageSize = RxDataTool.DATA_PAGE_SIZE;
                    if ((i + 1) == pages) {
                        pageSize = pointIds.size() % RxDataTool.DATA_PAGE_SIZE;
                    }
                    List<String> pointIdsTemp = pointIds.subList(i * RxDataTool.DATA_PAGE_SIZE, i * RxDataTool.DATA_PAGE_SIZE + pageSize);
                    List<EnterRelatePoint> tempPoints = DbHelpUtils.getEnterRelatePointList(rcvId, pointIdsTemp);
                    for (EnterRelatePoint enterRelatePoint : tempPoints) {
                        EnvirPoint envirPoint = new EnvirPoint();
                        envirPoint.setId(enterRelatePoint.getId());
                        envirPoint.setTagId(enterRelatePoint.getTagId());
                        envirPoint.setTagName(enterRelatePoint.getTagName());
                        envirPoint.setName(enterRelatePoint.getName());
                        envirPoint.setLongtitude(enterRelatePoint.getLongtitude());
                        envirPoint.setLatitude(enterRelatePoint.getLatitude());
                        envirPoint.setUpdateTime(enterRelatePoint.getUpdateTime());
                        envirPoint.setCode(enterRelatePoint.getCode());
                        if (!envirPoints.contains(envirPoint)) {
                            envirPoints.add(envirPoint);
                        }
                    }
                }
            } else {
                List<EnterRelatePoint> tempPoints = DbHelpUtils.getEnterRelatePointList(rcvId, pointIds);

                for (EnterRelatePoint enterRelatePoint : tempPoints) {
                    EnvirPoint envirPoint = new EnvirPoint();
                    envirPoint.setId(enterRelatePoint.getId());
                    envirPoint.setTagId(enterRelatePoint.getTagId());
                    envirPoint.setTagName(enterRelatePoint.getTagName());
                    envirPoint.setName(enterRelatePoint.getName());
                    envirPoint.setLongtitude(enterRelatePoint.getLongtitude());
                    envirPoint.setLatitude(enterRelatePoint.getLatitude());
                    envirPoint.setUpdateTime(enterRelatePoint.getUpdateTime());
                    envirPoint.setCode(enterRelatePoint.getCode());

                    if (!envirPoints.contains(envirPoint)) {
                        envirPoints.add(envirPoint);
                    }
                }
            }
        }
        programPointList.clear();
        if (!RxDataTool.isEmpty(envirPoints)) {
            setInitPointState(envirPoints);
            programPointList.addAll(envirPoints);
        }
        programPointAdapter.notifyDataSetChanged();
    }

    /**
     * 获取方案点位
     */
    private void getProgramPointData() {
        List<String> pointIds = new ArrayList<>();
        List<ProjectContent> projectContentList = DbHelpUtils.getProjectContentList(projectId);
        if (!RxDataTool.isEmpty(projectContentList)) {
            for (ProjectContent content : projectContentList) {
                for (String s : content.getAddressIds().split(",")) {
                    pointIds.add(s);
                    Log.d(TAG, s);
                }

            }
        }
        List<EnvirPoint> envirPoints = new ArrayList<>();
        if (!RxDataTool.isEmpty(pointIds)) {
            if (pointIds.size() > RxDataTool.DATA_PAGE_SIZE) {
                int pages = pointIds.size() % RxDataTool.DATA_PAGE_SIZE > 0 ? pointIds.size() /
                        RxDataTool.DATA_PAGE_SIZE + 1 : pointIds.size() / RxDataTool.DATA_PAGE_SIZE;
                for (int i = 0; i < pages; i++) {
                    int pageSize = RxDataTool.DATA_PAGE_SIZE;
                    if ((i + 1) == pages) {
                        pageSize = pointIds.size() % RxDataTool.DATA_PAGE_SIZE;
                    }
                    List<String> pointIdsTemp = pointIds.subList(i * RxDataTool.DATA_PAGE_SIZE,
                            i * RxDataTool.DATA_PAGE_SIZE + pageSize);
                    List<EnvirPoint> tempPoints = DbHelpUtils.getEnvirPointList(tagId, pointIdsTemp);
                    for (EnvirPoint envirPoint : tempPoints) {
                        if (!envirPoints.contains(envirPoint)) {
                            envirPoints.add(envirPoint);
                        }
                    }
                }
            } else {
                envirPoints = DbHelpUtils.getEnvirPointList(tagId, pointIds);

            }
        }
        programPointList.clear();
        if (!RxDataTool.isEmpty(envirPoints)) {
            setInitPointState(envirPoints);
            programPointList.addAll(envirPoints);
        }
        programPointAdapter.notifyDataSetChanged();
    }

    /**
     * 获取其他点位
     */
    private void getOtherPointData() {
        List<String> pointIds = new ArrayList<>();
        List<ProjectDetial> projectDetials = DbHelpUtils.getProjectDetialList(projectId);
        if (!RxDataTool.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials) {
                pointIds.add(projectDetial.getAddressId());
            }
        }
        List<EnvirPoint> envirPoints = new ArrayList<>();
        if (RxDataTool.isEmpty(pointIds)) {
            envirPoints = DbHelpUtils.getEnvirPointListForTagId(tagId);

        } else {
            if (pointIds.size() > RxDataTool.DATA_PAGE_SIZE) {
                int pages = pointIds.size() % RxDataTool.DATA_PAGE_SIZE > 0 ? pointIds.size() /
                        RxDataTool.DATA_PAGE_SIZE + 1 : pointIds.size() / RxDataTool.DATA_PAGE_SIZE;
                for (int i = 0; i < pages; i++) {
                    int pageSize = RxDataTool.DATA_PAGE_SIZE;
                    if ((i + 1) == pages) {
                        pageSize = pointIds.size() % RxDataTool.DATA_PAGE_SIZE;
                    }
                    List<String> pointIdsTemp = pointIds.subList(i * RxDataTool.DATA_PAGE_SIZE,
                            i * RxDataTool.DATA_PAGE_SIZE + pageSize);
                    List<EnvirPoint> tempPoints = DbHelpUtils.getEnvirPointListNotInIds(tagId, pointIdsTemp);
                    for (EnvirPoint envirPoint : tempPoints) {
                        if (!envirPoints.contains(envirPoint)) {
                            envirPoints.add(envirPoint);
                        }
                    }
                }
            } else {
                envirPoints = DbHelpUtils.getEnvirPointListNotInIds(tagId, pointIds);
            }
        }

        otherPointList.clear();
        if (!RxDataTool.isEmpty(envirPoints)) {
            setInitPointState(envirPoints);
            otherPointList.addAll(envirPoints);
        }
        otherPointAdapter.notifyDataSetChanged();
    }


    private void getOtherRelatePointData() {
        List<String> pointIds = new ArrayList<>();
        List<ProjectContent> projectContentList = DbHelpUtils.getProjectContentList(projectId);
        if (!RxDataTool.isEmpty(projectContentList)) {
            for (ProjectContent content : projectContentList) {
                if (content.getTagId().equals(tagId)) {
                    for (String s : content.getAddressIds().split(",")) {
                        pointIds.add(s);
                        Log.d(TAG, s);
                    }
                    break;
                }
            }
        }
        List<EnvirPoint> envirPoints = new ArrayList<>();
        if (!RxDataTool.isEmpty(pointIds)) {
            if (pointIds.size() > RxDataTool.DATA_PAGE_SIZE) {
                int pages = pointIds.size() % RxDataTool.DATA_PAGE_SIZE > 0 ? pointIds.size() /
                        RxDataTool.DATA_PAGE_SIZE + 1 :
                        pointIds.size() / RxDataTool.DATA_PAGE_SIZE;
                for (int i = 0; i < pages; i++) {
                    int pageSize = RxDataTool.DATA_PAGE_SIZE;
                    if ((i + 1) == pages) {
                        pageSize = pointIds.size() % RxDataTool.DATA_PAGE_SIZE;
                    }
                    List<String> pointIdsTemp = pointIds.subList(i * RxDataTool.DATA_PAGE_SIZE,
                            i * RxDataTool.DATA_PAGE_SIZE + pageSize);
                    List<EnterRelatePoint> tempPoints = DbHelpUtils.
                            getEnterRelatePointListNotIds(rcvId, pointIdsTemp);
                    for (EnterRelatePoint enterRelatePoint : tempPoints) {
                        EnvirPoint envirPoint = new EnvirPoint();
                        envirPoint.setId(enterRelatePoint.getId());
                        envirPoint.setTagId(enterRelatePoint.getTagId());
                        envirPoint.setTagName(enterRelatePoint.getTagName());
                        envirPoint.setName(enterRelatePoint.getName());
                        envirPoint.setLongtitude(enterRelatePoint.getLongtitude());
                        envirPoint.setLatitude(enterRelatePoint.getLatitude());
                        envirPoint.setUpdateTime(enterRelatePoint.getUpdateTime());
                        envirPoint.setCode(enterRelatePoint.getCode());
                        if (!envirPoints.contains(envirPoint)) {
                            envirPoints.add(envirPoint);
                        }
                    }
                }
            } else {
                List<EnterRelatePoint> tempPoints = DbHelpUtils.getEnterRelatePointListNotIds(rcvId, pointIds);

                for (EnterRelatePoint enterRelatePoint : tempPoints) {
                    EnvirPoint envirPoint = new EnvirPoint();
                    envirPoint.setId(enterRelatePoint.getId());
                    envirPoint.setTagId(enterRelatePoint.getTagId());
                    envirPoint.setTagName(enterRelatePoint.getTagName());
                    envirPoint.setName(enterRelatePoint.getName());
                    envirPoint.setLongtitude(enterRelatePoint.getLongtitude());
                    envirPoint.setLatitude(enterRelatePoint.getLatitude());
                    envirPoint.setUpdateTime(enterRelatePoint.getUpdateTime());
                    envirPoint.setCode(enterRelatePoint.getCode());

                    if (!envirPoints.contains(envirPoint)) {
                        envirPoints.add(envirPoint);
                    }
                }
            }
        }
        otherPointList.clear();
        if (!RxDataTool.isEmpty(envirPoints)) {
            setInitPointState(envirPoints);
            otherPointList.addAll(envirPoints);
        }
        otherPointAdapter.notifyDataSetChanged();
    }

    /**
     * 更新方案点位选中状态
     *
     * @param position
     */
    private void updateProgramPointState(int position) {
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

    /**
     * 设置初始化点位选中状态
     *
     * @param envirPoints
     */
    private void setInitPointState(List<EnvirPoint> envirPoints) {
        if (!RxDataTool.isEmpty(envirPoints)) {
            for (EnvirPoint envirPoint : envirPoints) {
                if (!RxDataTool.isEmpty(selectedAddressIds) && selectedAddressIds.contains(envirPoint.getId())) {
                    envirPoint.setSelected(true);
                } else {
                    envirPoint.setSelected(false);
                }
            }
        }

    }


    /**
     * 设置选中点位的address和id
     */
    private void setSelectedPoint() {
        List<String> addressIdList = new ArrayList<>();
        List<String> addressNameList = new ArrayList<>();
        if (!RxDataTool.isEmpty(programPointList)) {
            for (EnvirPoint envirPoint : programPointList) {
                if (envirPoint.isSelected()) {
                    addressIdList.add(envirPoint.getId());
                    addressNameList.add(envirPoint.getName());
                }
            }
        }

        if (!RxDataTool.isEmpty(otherPointList)) {
            for (EnvirPoint envirPoint : otherPointList) {
                if (envirPoint.isSelected()) {
                    addressIdList.add(envirPoint.getId());
                    addressNameList.add(envirPoint.getName());
                }
            }
        }

        selectedAddressIds = RxDataTool.listToStr(addressIdList);
        selectedAddress = RxDataTool.listToStr(addressNameList);
    }


}
