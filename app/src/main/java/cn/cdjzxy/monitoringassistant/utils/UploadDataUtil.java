package cn.cdjzxy.monitoringassistant.utils;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectContents;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectPlan;

/**
 * 上传数据的辅助类
 */
public class UploadDataUtil {
    /**
     * 设置采样计划数据 采样方案
     *
     * @param projectId
     * @return
     */
    public static ProjectPlan setUploadProjectContextData(String projectId) {
        Project project = DbHelpUtils.getDbProject(projectId);
        if (RxDataTool.isNull(project)) return null;
        ProjectPlan projectPlan = new ProjectPlan();
        projectPlan.setId(projectId);
        projectPlan.setIsCompelSubmit(true);
        List<ProjectContent> contentList = DbHelpUtils.getProjectContentList(projectId);
        if (RxDataTool.isEmpty(contentList)) return projectPlan;
        List<ProjectContents> upContentList = new ArrayList<>();
        for (ProjectContent content : contentList) {
            List<ProjectContents.AddressArrBean> arrBeanList = new ArrayList<>();
            ProjectContents upContent = new ProjectContents();
            upContent.setId(content.getId());
            upContent.setProjectId(projectId);
            upContent.setUpdateTime(content.getUpdateTime());
            upContent.setAddressIds(content.getAddressIds());
            upContent.setAddress(content.getAddress());
            List<String> addressIdList = RxDataTool.strToList(content.getAddressIds());
            List<String> addressNameList = RxDataTool.strToList(content.getAddress(), "、");
            upContent.setAddressCount(addressIdList.size() + "");
            for (int i = 0; i < addressIdList.size(); i++) {
                ProjectContents.AddressArrBean bean = new ProjectContents.AddressArrBean();
                bean.setId(addressIdList.get(i));
                if (i < addressNameList.size()) {
                    bean.setName(addressNameList.get(i));
                } else {
                    bean.setName("");
                }
                arrBeanList.add(bean);
            }
            upContent.setAddressArr(arrBeanList);
            upContent.setDays(content.getDays());
            upContent.setPeriod(content.getPeriod());
            upContent.setTagId(content.getTagId());
            upContent.setTagName(content.getTagName());
            upContent.setTagParentId(content.getTagParentId());
            upContent.setTagParentName(content.getTagParentName());
            upContent.setMonItemsName(content.getMonItemNames());
            upContent.setMonItemCount(RxDataTool.strToList(content.getMonItemIds()).size() + "");
            upContent.setProjectDetials(DbHelpUtils.getProjectDetailList(content.getProjectId(), content.getId()));
            upContentList.add(upContent);
        }
        projectPlan.setProjectContents(upContentList);
        return projectPlan;
    }

//    /**
//     * 设置采样单上传的文件数据
//     * @param sampling 采样单
//     * @return 文件集合
//     */
//    public static List<File> setUploadSampleFileList(Sampling sampling) {
//        List<SamplingFile> fileList =sampling.getHasFile();
//        List<File> uploadFileList = new ArrayList<>();
//        if (RxDataTool.isEmpty(fileList)) {//采样单数据没有 尝试从数据库获取
//            fileList = DbHelpUtils.getSamplingFileList(sampling.getId());
//        }
//        if (RxDataTool.isEmpty(fileList)) {
//            return uploadFileList;
//        } else {
//            for (SamplingFile samplingFile : fileList) {
//                if (RxDataTool.isEmpty(samplingFile.getId())) {
//                    File file = new File(samplingFile.getFilePath());
//                    if (RxFileTool.isFileExists(file)) {
//                        if (!uploadFileList.contains(file))
//                            uploadFileList.add(file);
//                    }
//                }
//            }
//            return uploadFileList;
//        }
//    }
}