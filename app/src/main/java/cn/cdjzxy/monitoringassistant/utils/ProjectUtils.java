package cn.cdjzxy.monitoringassistant.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;


public class ProjectUtils {
    /**
     * 生成新的ProjectDetail数据
     */
    public static void generateProjectDetails(String contentId, String projectId) {
        Project project = DbHelpUtils.getDbProject(projectId);
        if (RxDataTool.isNull(project)) return;
        ProjectContent mProjectContent = DbHelpUtils.getProjectContent(contentId);
        List<String> addressList = RxDataTool.strToList(mProjectContent.getAddressIds());
        if (RxDataTool.isEmpty(addressList)) return;
        if (RxDataTool.isNull(mProjectContent.getMonItemIds())) return;
        List<String> monItemIdList = RxDataTool.strToList(mProjectContent.getMonItemIds());
        if (RxDataTool.isEmpty(monItemIdList)) return;
        List<ProjectDetial> detialList = new ArrayList<>();
        List<MonItemMethodRelation> relationList = DbHelpUtils.getMonItemMethodRelationAll();
        MonItemMethodRelation relation = null;
        if (!RxDataTool.isEmpty(relationList))
            relation = relationList.get(0);
        for (String addressId : addressList) {
            for (String id : monItemIdList) {
                ProjectDetial detail = new ProjectDetial();
                detail.setId(UUID.randomUUID().toString());
                detail.setProjectId(mProjectContent.getProjectId());
                detail.setProjectContentId(mProjectContent.getId());
                detail.setUpdateTime(DateUtils.getDate());
                detail.setTagId(mProjectContent.getTagId());
                detail.setTagName(mProjectContent.getTagName());
                detail.setTagParentId(mProjectContent.getTagParentId());
                detail.setTagParentName(mProjectContent.getTagParentName());
                detail.setMonItemId(id);
                detail.setMonItemName(DbHelpUtils.getMonItemForId(id).getName());
                detail.setAddressId(addressId);
                if (project.getTypeCode() == 3) {//环境质量
                    detail.setAddress(DbHelpUtils.getEnvirPoint(addressId).getName());
                } else {//污染源
                    detail.setAddress(DbHelpUtils.getEnterRelatePoint(addressId).getName());
                }

                detail.setMonItemId(RxDataTool.isNull(relation) ? "" : relation.getMonItemId());
//                detail.setMethodName(RxDataTool.isNull(relation)?"":relation());
                detail.setDays(mProjectContent.getDays());
                detail.setPeriod(mProjectContent.getPeriod());
                detialList.add(detail);
            }
        }
        if (!RxDataTool.isEmpty(detialList)) {
            DBHelper.get().getProjectDetialDao().insertInTx(detialList);
        }
    }

    /**
     * 生成新的ProjectDetail数据
     */
    public static void generateProjectDetails(Project project, ProjectContent mProjectContent) {
        if (RxDataTool.isNull(project) || RxDataTool.isNull(mProjectContent)) return;
        List<String> addressList = RxDataTool.strToList(mProjectContent.getAddressIds());
        if (RxDataTool.isEmpty(addressList)) return;
        if (RxDataTool.isNull(mProjectContent.getMonItemIds())) return;
        List<String> monItemIdList = RxDataTool.strToList(mProjectContent.getMonItemIds());
        if (RxDataTool.isEmpty(monItemIdList)) return;
        List<ProjectDetial> detialList = new ArrayList<>();
        for (String addressId : addressList) {
            for (String id : monItemIdList) {
                List<MonItemMethodRelation> relationList = DbHelpUtils.getMonItemMethodRelationListForMonItemId(id);
                Methods methods = null;
                if (!RxDataTool.isEmpty(relationList) && !RxDataTool.isNull(relationList.get(0))) {
                    methods = DbHelpUtils.getMethod(relationList.get(0).getMethodId());
                }
                ProjectDetial detail = new ProjectDetial();
                detail.setId(UUID.randomUUID().toString());
                detail.setProjectId(mProjectContent.getProjectId());
                detail.setProjectContentId(mProjectContent.getId());
                detail.setUpdateTime(DateUtils.getDate());
                detail.setTagId(mProjectContent.getTagId());
                detail.setTagName(mProjectContent.getTagName());
                detail.setTagParentId(mProjectContent.getTagParentId());
                detail.setTagParentName(mProjectContent.getTagParentName());
                detail.setMonItemId(id);
                detail.setMonItemName(DbHelpUtils.getMonItemForId(id).getName());
                detail.setAddressId(addressId);
                if (project.getTypeCode() == 3) {//环境质量
                    detail.setAddress(DbHelpUtils.getEnvirPoint(addressId).getName());
                } else {//污染源
                    detail.setAddress(DbHelpUtils.getEnterRelatePoint(addressId).getName());
                }

                detail.setMethodId(RxDataTool.isNull(methods) ? "" : methods.getId());
                detail.setMethodName(RxDataTool.isNull(methods) ? "" : methods.getName());
                detail.setDays(mProjectContent.getDays());
                detail.setPeriod(mProjectContent.getPeriod());
                detialList.add(detail);
            }
        }
        if (!RxDataTool.isEmpty(detialList)) {
            DBHelper.get().getProjectDetialDao().insertInTx(detialList);
        }
    }


    /**
     * 设置context的监测项目
     *
     * @param content
     * @return
     */
    public static ProjectContent setProjectContentMonItemsData(ProjectContent content) {
        List<ProjectDetial> detailList = DbHelpUtils.getProjectDetailList(content.getProjectId(),
                content.getId());
        if (RxDataTool.isEmpty(detailList)) return content;
        List<String> monItemNameList = new ArrayList<>();
        List<String> monItemIdList = new ArrayList<>();

        for (ProjectDetial detail : detailList) {
            if (!monItemIdList.contains(detail.getMonItemId())) {
                monItemIdList.add(detail.getMonItemId());
                monItemNameList.add(detail.getMonItemName());
            }
        }
        content.setMonItemNames(RxDataTool.listToStr(monItemNameList));
        content.setMonItemIds(RxDataTool.listToStr(monItemIdList));
        return content;
    }
}
