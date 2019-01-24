package cn.cdjzxy.monitoringassistant.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectPlan;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFileDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.InstrumentalActivity;

public class SubmitDataUtil {

    /**
     * 组装废水提交数据
     *
     * @param sampling
     * @return
     */
    public static PreciptationSampForm setUpFSData(Sampling sampling) {
        PreciptationSampForm preciptationSampForm = new PreciptationSampForm();
        //设置外面数据
        preciptationSampForm.setIsAdd(true);
        preciptationSampForm.setIsSubmit(true);
        preciptationSampForm.setDevceForm(true);
        preciptationSampForm.setCompelSubmit(false);
        preciptationSampForm.setAddTime(sampling.getAddTime());
        preciptationSampForm.setUpdateTime(sampling.getUpdateTime());
        //设置SampFormBean数据
        PreciptationSampForm.SampFormBean sampFormBean = setUpSampFormBean(sampling);
        //setSamplingDetails
        List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> detailsBeanList = setUpSamplingDetailDataList(sampling);
        if (!CheckUtil.isEmpty(detailsBeanList)) {
            sampFormBean.setSamplingDetails(detailsBeanList);
        }else {
            sampFormBean.setSamplingDetails(new ArrayList<>());
        }
        //setUpBottleSplitDataList
        List<PreciptationSampForm.SampFormBean.SamplingFormStandsBean> bottleSplitDataList = setUpBottleSplitDataList(sampling);
        if (!CheckUtil.isEmpty(bottleSplitDataList)) {
            sampFormBean.setSamplingFormStands(bottleSplitDataList);
        }else {
            sampFormBean.setSamplingFormStands(new ArrayList<>());
        }
        //文件信息组装
        List<PreciptationSampForm.SampFormBean.SamplingFileBean> fileBeanList = setUpSamplingFileDataList(sampling);
        if (!CheckUtil.isEmpty(fileBeanList)) {
            sampFormBean.setUploadFiles(fileBeanList);
        }else {
            sampFormBean.setUploadFiles(new ArrayList<>());
        }

        sampFormBean.setSamplingDetailYQFs(new ArrayList<>());
        preciptationSampForm.setSampForm(sampFormBean);
        return preciptationSampForm;
    }

    /**
     * set up SampFormBean
     *
     * @param sampling
     * @return
     */
    public static PreciptationSampForm.SampFormBean setUpSampFormBean(Sampling sampling) {
        PreciptationSampForm.SampFormBean sampFormBean = new PreciptationSampForm.SampFormBean();
        sampFormBean.setProjectId(sampling.getProjectId());
        sampFormBean.setFormPath(sampling.getFormPath());
        sampFormBean.setFormName(sampling.getFormName());
        sampFormBean.setProjectName(sampling.getProjectName());
        sampFormBean.setProjectNo(sampling.getProjectNo());
        sampFormBean.setMontype(sampling.getMontype());
        sampFormBean.setParentTagId(sampling.getParentTagId());
        sampFormBean.setFormType(sampling.getFormType());
        sampFormBean.setFormTypeName(sampling.getFormTypeName());
        sampFormBean.setPrivateData(sampling.getPrivateData());
        sampFormBean.setSendSampTime(sampling.getSendSampTime());
        sampFormBean.setSamplingNo(sampling.getSamplingNo());
        sampFormBean.setSamplingTimeBegin(sampling.getSamplingTimeBegin());
        sampFormBean.setTagName(sampling.getTagName());
        sampFormBean.setTagId(sampling.getTagId());
        sampFormBean.setAddressId(sampling.getAddressId());
        sampFormBean.setAddressName(sampling.getAddressName());
        sampFormBean.setAddressNo(sampling.getAddressNo());
        sampFormBean.setMonitemId(sampling.getMonitemId());
        sampFormBean.setMonitemName(sampling.getMonitemName());
        sampFormBean.setMethodName(sampling.getMethodName());
        sampFormBean.setMethodId(sampling.getMethodId());
        sampFormBean.setDeviceId(sampling.getDeviceId());
        sampFormBean.setDeviceName(sampling.getDeviceName());
        sampFormBean.setTransfer(sampling.getTransfer());
        sampFormBean.setReciveTime(sampling.getReciveTime());
        sampFormBean.setFile(sampling.getFile());
        sampFormBean.setSamplingUserId(sampling.getSamplingUserId());
        sampFormBean.setSamplingUserName(sampling.getSamplingUserName());
        sampFormBean.setSamplingTimeEnd(sampling.getSamplingTimeBegin());
        sampFormBean.setFormFlows(sampling.getFormFlows());
        sampFormBean.setLayTableCheckbox(sampling.getLayTableCheckbox());
        sampFormBean.setSubmitId(sampling.getSubmitId());
        sampFormBean.setSubmitName(sampling.getSubmitName());
        sampFormBean.setSubmitDate(sampling.getSubmitDate());
        sampFormBean.setMonitorPerson(sampling.getMonitorPerson());
        sampFormBean.setMonitorTime(sampling.getMonitorTime());
        sampFormBean.setStatus(sampling.getStatus());
        sampFormBean.setStatusName(sampling.getStatusName());
        sampFormBean.setTransStatus(sampling.getTransStatus());
        sampFormBean.setTransStatusName(sampling.getTransStatusName());
        sampFormBean.setCurUserId(sampling.getCurUserId());
        sampFormBean.setCurUserName(sampling.getCurUserName());
        sampFormBean.setComment(sampling.getComment());
        sampFormBean.setAddTime(sampling.getAddTime());
        sampFormBean.setUpdateTime(sampling.getUpdateTime());
        sampFormBean.setVersion(sampling.getVersion());
        sampFormBean.setTagId(sampling.getTagId());
        sampFormBean.setTagName(sampling.getTagName());
        //设置气象信息
        sampFormBean.setWeather(sampling.getWeather());
        sampFormBean.setTemprature(sampling.getTemprature());
        sampFormBean.setPressure(sampling.getPressure());
        return sampFormBean;
    }

    /**
     * 组装SamplingDetailDataList
     *
     * @param sampling
     * @return
     */
    private static List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> setUpSamplingDetailDataList(Sampling sampling) {
        List<SamplingDetail> samplingDetailsList = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(sampling.getId())).list();
        if (!CheckUtil.isEmpty(samplingDetailsList)) {
            List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> samplingDetailsBeansList = new ArrayList<>();
            int count = 1;
            for (SamplingDetail samplingDetail : samplingDetailsList) {
                if (CheckUtil.isEmpty(samplingDetail.getMonitemId())){
                   continue;
                }
                PreciptationSampForm.SampFormBean.SamplingDetailsBean samplingDetailsBean = new PreciptationSampForm.SampFormBean.SamplingDetailsBean();

                samplingDetailsBean.setSampingCode(samplingDetail.getSampingCode());
                samplingDetailsBean.setSamplingId(samplingDetail.getSamplingId());
                samplingDetailsBean.setProjectId(sampling.getProjectId());
                samplingDetailsBean.setIsSenceAnalysis(samplingDetail.getIsSenceAnalysis());
                samplingDetailsBean.setMonitemId(samplingDetail.getMonitemId());
                samplingDetailsBean.setMonitemName(samplingDetail.getMonitemName());
                samplingDetailsBean.setAddresssId(sampling.getAddressId());
                samplingDetailsBean.setAddressName(sampling.getAddressName());
                samplingDetailsBean.setOrderIndex(count + "");
                samplingDetailsBean.setFrequecyNo(samplingDetail.getFrequecyNo() + "");
                samplingDetailsBean.setSamplingTime(samplingDetail.getSamplingTime());
                samplingDetailsBean.setSamplingType(samplingDetail.getSamplingType() + "");
                samplingDetailsBean.setSamplingCount(samplingDetail.getSamplingCount() + "");
                samplingDetailsBean.setPreservative(samplingDetail.getPreservative());
                samplingDetailsBean.setIsCompare(samplingDetail.getIsCompare());
                samplingDetailsBean.setSampleCollection(samplingDetail.getSampleCollection());
                samplingDetailsBean.setSampleAcceptance(samplingDetail.getSampleAcceptance());
                samplingDetailsBean.setDescription(samplingDetail.getDescription());
                samplingDetailsBean.setPrivateData(samplingDetail.getPrivateData());
                samplingDetailsBean.setSamplingOnTime(samplingDetail.getSamplingOnTime());
                samplingDetailsBean.setValue(samplingDetail.getValue());
                //降水
                //samplingDetailsBean.setSampStandId("00000000-0000-0000-0000-000000000000");
                //samplingDetailsBean.setMonitemId("7253950a-9daa-9d4f-bd9a-a84789279c2a");
                //samplingDetailsBean.setMonitemName("降水量");
                samplingDetailsBean.setValue1(samplingDetail.getValue1());

                samplingDetailsBeansList.add(samplingDetailsBean);
                count++;
            }
            return samplingDetailsBeansList;
        }
        return null;
    }

    /**
     * set up BottleSplitDataList
     *
     * @param sampling
     * @return
     */
    private static List<PreciptationSampForm.SampFormBean.SamplingFormStandsBean> setUpBottleSplitDataList(Sampling sampling) {
        List<SamplingFormStand> samplingFormStandsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(sampling.getId())).list();
        if (!CheckUtil.isEmpty(samplingFormStandsList)) {
            List<PreciptationSampForm.SampFormBean.SamplingFormStandsBean> samplingFormStandsBeansList = new ArrayList<>();
            for (SamplingFormStand samplingFormStand : samplingFormStandsList) {
                PreciptationSampForm.SampFormBean.SamplingFormStandsBean samplingFormStandsBean = new PreciptationSampForm.SampFormBean.SamplingFormStandsBean();
                samplingFormStandsBean.setAnalysisSite(samplingFormStand.getAnalysisSite());
                samplingFormStandsBean.setContainer(samplingFormStand.getContainer());
                samplingFormStandsBean.setCount(samplingFormStand.getCount());
                samplingFormStandsBean.setId(samplingFormStand.getId());
                samplingFormStandsBean.setIndex(samplingFormStand.getIndex() + "");
                samplingFormStandsBean.setMonitemIds(samplingFormStand.getMonitemIds());
                samplingFormStandsBean.setMonitemName(samplingFormStand.getMonitemName());
                samplingFormStandsBean.setMonItems(samplingFormStand.getMonItems());
                samplingFormStandsBean.setPreservative(samplingFormStand.getPreservative());
                samplingFormStandsBean.setSamplingId(samplingFormStand.getSamplingId());
                samplingFormStandsBean.setSamplingAmount(samplingFormStand.getSamplingAmount());
                samplingFormStandsBean.setSaveMehtod(samplingFormStand.getSaveMehtod());
                samplingFormStandsBean.setSaveTimes(samplingFormStand.getSaveTimes());
                samplingFormStandsBean.setStandNo(samplingFormStand.getStandNo());
                samplingFormStandsBean.setUpdateTime(samplingFormStand.getUpdateTime());

                samplingFormStandsBeansList.add(samplingFormStandsBean);
            }
            return samplingFormStandsBeansList;
        }

        return null;

    }

    /**
     * 组装上传文件
     *
     * @param sampling
     * @return
     */
    public static List<PreciptationSampForm.SampFormBean.SamplingFileBean> setUpSamplingFileDataList(Sampling sampling) {
        List<PreciptationSampForm.SampFormBean.SamplingFileBean> result = new ArrayList<>();

        //从数据库加载数据
        List<SamplingFile> dataList = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(sampling.getId())).list();
        if (CheckUtil.isEmpty(dataList)) {
            return result;
        }

        for (SamplingFile item : dataList) {
            PreciptationSampForm.SampFormBean.SamplingFileBean bean = new PreciptationSampForm.SampFormBean.SamplingFileBean();
            bean.setId(item.getId());
            bean.setFileName(item.getFileName());
            bean.setUpdateTime(item.getUpdateTime());

            result.add(bean);
        }

        return result;
    }

    /**
     * 组装仪器法提交数据
     *
     * @param sampling
     * @return
     */
    public static PreciptationSampForm setUpYQFData(Sampling sampling) {

        UserInfo userInfo = UserInfoHelper.get().getUser();
        sampling.setSubmitId(userInfo.getId());
        sampling.setSubmitName(userInfo.getName());
        sampling.setSubmitDate(DateUtil.getDate());

        sampling.setMonitorTime(DateUtil.getDate());

        PreciptationSampForm preciptationSampForm = new PreciptationSampForm();
        //设置外面数据
        preciptationSampForm.setIsAdd(true);
        preciptationSampForm.setIsSubmit(true);
        preciptationSampForm.setDevceForm(true);
        preciptationSampForm.setCompelSubmit(false);
        preciptationSampForm.setAddTime(sampling.getAddTime());
        preciptationSampForm.setUpdateTime(sampling.getUpdateTime());
        preciptationSampForm.setUploadFiles(new ArrayList<>());
        preciptationSampForm.setDelFiles(new ArrayList<>());

        //设置SampFormBean数据
        PreciptationSampForm.SampFormBean sampFormBean = setUpSampFormBean(sampling);
        sampFormBean.setSamplingDetails(new ArrayList<>());
        sampFormBean.setSamplingFormStands(new ArrayList<>());

        preciptationSampForm.setSampForm(sampFormBean);

        //设置仪器法检测记录
        List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> detailsBeanList = setUpSamplingDetailDataList(sampling);
        if (!CheckUtil.isEmpty(detailsBeanList)) {
            sampFormBean.setSamplingDetailYQFs(detailsBeanList);
        }

        sampFormBean.setUploadFiles(new ArrayList<>());


        return preciptationSampForm;
    }

    /**
     * 设置提交的降水数据
     *
     * @param sampling
     * @return
     */
    public static PreciptationSampForm setUpJSData(Sampling sampling) {
        PreciptationSampForm preciptationSampForm = new PreciptationSampForm();
        //设置外面数据
        preciptationSampForm.setIsAdd(true);
        preciptationSampForm.setIsSubmit(true);
        preciptationSampForm.setDevceForm(true);
        preciptationSampForm.setCompelSubmit(false);
        preciptationSampForm.setAddTime(sampling.getAddTime());
        preciptationSampForm.setUpdateTime(sampling.getUpdateTime());
        //设置SampFormBean数据
        PreciptationSampForm.SampFormBean sampFormBean = setUpSampFormBean(sampling);
        //setSamplingDetails
        List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> detailsBeanList=setUpSamplingDetailDataList(sampling);
        if (!CheckUtil.isEmpty(detailsBeanList)){
            sampFormBean.setSamplingDetails(detailsBeanList);
        }
        //setUpBottleSplitDataList
        List<PreciptationSampForm.SampFormBean.SamplingFormStandsBean> bottleSplitDataList=setUpBottleSplitDataList(sampling);
        if (!CheckUtil.isEmpty(bottleSplitDataList)){
            sampFormBean.setSamplingFormStands(bottleSplitDataList);
        }
        preciptationSampForm.setSampForm(sampFormBean);
        //文件信息组装
        List<PreciptationSampForm.SampFormBean.SamplingFileBean> fileBeanList = setUpSamplingFileDataList(sampling);
        if (!CheckUtil.isEmpty(fileBeanList)) {
            sampFormBean.setUploadFiles(fileBeanList);
        }else {
            sampFormBean.setUploadFiles(new ArrayList<>());
        }
        sampFormBean.setSamplingDetailYQFs(new ArrayList<>());

        return preciptationSampForm;
    }

    /**
     * 设置采样方案数据
     * @return
     */
    public static ProjectPlan setUpProjectPlan(Project project){
        List<ProjectDetial> mProjectDetials = new ArrayList<>();
        Map<String, ProjectDetial> mStringProjectDetialMap = new HashMap<>();
        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(project.getId())).list();
        if (!CheckUtil.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials) {
                if (CheckUtil.isNull(mStringProjectDetialMap.get(projectDetial.getProjectContentId()))) {
                    mStringProjectDetialMap.put(projectDetial.getProjectContentId(), projectDetial);
                } else {
                    ProjectDetial projectDetialOld = mStringProjectDetialMap.get(projectDetial.getProjectContentId());

                    if (!projectDetialOld.getAddressId().contains(projectDetial.getAddressId())) {
                        projectDetialOld.setAddressId(projectDetialOld.getAddressId() + "," + projectDetial.getAddressId());
                        projectDetialOld.setAddress(projectDetialOld.getAddress() + "," + projectDetial.getAddress());
                    }

                    if (!projectDetialOld.getMonItemId().contains(projectDetial.getMonItemId())) {
                        projectDetialOld.setMonItemId(projectDetialOld.getMonItemId() + "," + projectDetial.getMonItemId());
                        projectDetialOld.setMonItemName(projectDetialOld.getMonItemName() + "," + projectDetial.getMonItemName());
                    }

                    mStringProjectDetialMap.put(projectDetialOld.getProjectContentId(), projectDetialOld);
                }
            }

            for (String key : mStringProjectDetialMap.keySet()) {
                mProjectDetials.add(mStringProjectDetialMap.get(key));
            }

        }

        List<ProjectContent> projectContents = new ArrayList<>();
        if (!CheckUtil.isEmpty(mProjectDetials)) {//开始组装数据
            for (ProjectDetial projectDetial : mProjectDetials) {
                ProjectContent projectContent = new ProjectContent();
                projectContent.setId(projectDetial.getProjectContentId());
                projectContent.setIsChecked(false);
                projectContent.setMonItemsName(projectDetial.getMonItemName());
                projectContent.setTagId(projectDetial.getTagId());
                projectContent.setTagName(projectDetial.getTagName());
                projectContent.setAddress(projectDetial.getAddress());
                projectContent.setAddressIds(projectDetial.getAddressId());
                projectContent.setDays(projectDetial.getDays());
                projectContent.setPeriod(projectDetial.getPeriod());
                projectContent.setComment(projectDetial.getComment());
                projectContent.setPeriodShow(false);
                projectContent.setTagParentId(projectDetial.getTagParentId());
                projectContent.setTagParentName(projectDetial.getTagParentName());
                projectContent.setGuid("");
                List<MonItems> monItemsList = new ArrayList<>();
                List<EnvirPoint> envirPoints = new ArrayList<>();
                if (!CheckUtil.isEmpty(projectDetial.getMethodId())) {
                    if (projectDetial.getMethodId().contains(",")) {
                        monItemsList = DBHelper.get().getMonItemsDao().queryBuilder().where(MonItemsDao.Properties.Id.in(projectDetial.getMonItemId().split(","))).list();
                    } else {
                        monItemsList = DBHelper.get().getMonItemsDao().queryBuilder().where(MonItemsDao.Properties.Id.eq(projectDetial.getMonItemId())).list();
                    }
                }

                if (!CheckUtil.isEmpty(projectDetial.getAddressId())) {
                    if (projectDetial.getAddressId().contains(",")) {
                        envirPoints = DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.Properties.Id.in(projectDetial.getAddressId().split(","))).list();
                    } else {
                        envirPoints = DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.Properties.Id.eq(projectDetial.getAddressId())).list();
                    }
                }

                List<ProjectContent.MonItemsBean> monItems = new ArrayList<>();
                List<ProjectContent.AddressArrBean> addressArrs = new ArrayList<>();

                if (!CheckUtil.isEmpty(monItemsList)) {
                    for (MonItems items : monItemsList) {
                        ProjectContent.MonItemsBean monItemsBean = new ProjectContent.MonItemsBean();
                        monItemsBean.setId(items.getId());
                        monItemsBean.setName(items.getName());
                        monItemsBean.setMethodId(projectDetial.getMethodId());
                        monItemsBean.setMethodName(projectDetial.getMethodName());
                        monItemsBean.setHaveCert("");
                        monItemsBean.setTagId(projectDetial.getTagId());
                        monItemsBean.setTagName(projectDetial.getTagName());
                        monItemsBean.setIsOutsourcing(false);
                        monItemsBean.setIsOutsourcingtext("");
                        monItems.add(monItemsBean);
                    }
                }

                if (!CheckUtil.isEmpty(envirPoints)) {
                    for (EnvirPoint envirPoint : envirPoints) {
                        ProjectContent.AddressArrBean addressArrBean = new ProjectContent.AddressArrBean();
                        addressArrBean.setId(envirPoint.getId());
                        addressArrBean.setName(envirPoint.getName());
                        addressArrBean.setType(0);
                        addressArrBean.setIstemp(false);
                        addressArrBean.setPoint("");
                        addressArrBean.setLevel("");
                        addressArrBean.setESLimt(new ArrayList<>());
                        addressArrs.add(addressArrBean);
                    }
                }

                projectContent.setMonItems(monItems);
                projectContent.setMonItemCount(monItems.size());
                projectContent.setAddressArr(addressArrs);
                projectContent.setAddressCount(addressArrs.size());
                projectContent.setProjectDetials(new ArrayList<>());
                projectContents.add(projectContent);
            }
        }


        ProjectPlan projectPlan = new ProjectPlan();
        projectPlan.setId(project.getId());
        //projectPlan.setIsCompelSubmit(true);
        projectPlan.setProjectContents(projectContents);
        return projectPlan;
    }
}
