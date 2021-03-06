package cn.cdjzxy.monitoringassistant.utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetailYQFs;
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
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental.fragment.TestRecordFragment;

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
        } else {
            sampFormBean.setSamplingDetails(new ArrayList<>());
        }
        //setUpBottleSplitDataList
        List<PreciptationSampForm.SampFormBean.SamplingFormStandsBean> bottleSplitDataList = setUpBottleSplitDataList(sampling);
        if (!CheckUtil.isEmpty(bottleSplitDataList)) {
            sampFormBean.setSamplingFormStands(bottleSplitDataList);
        } else {
            sampFormBean.setSamplingFormStands(new ArrayList<>());
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
        sampFormBean.setId(sampling.getId());
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
        sampFormBean.setSamplingTimeBegin(sampling.getSamplingTimeBegin() == null ? DateUtils.getDate(new Date()) : sampling.getSamplingTimeBegin());
        sampFormBean.setSamplingTimeEnd(CheckUtil.isEmpty(sampling.getSamplingTimeEnd()) ? sampFormBean.getSamplingTimeBegin() : sampling.getSamplingTimeEnd());
        sampFormBean.setTagName(sampling.getTagName());
        sampFormBean.setTagId(sampling.getTagId());
        if (sampling.getAddressId() == null || sampling.getAddressId().equals("")) {
            sampFormBean.setAddressId("00000000-0000-0000-0000-000000000000");
        } else {
            if (sampling.getAddressId().length() > 50) {
                sampFormBean.setAddressId(sampling.getAddressId().split(",")[0]);
            } else {
                sampFormBean.setAddressId(sampling.getAddressId());
            }
        }
        if (sampling.getAddressName() == null) {
            sampling.setAddressName("  ");
        }
        sampFormBean.setAddressName(sampling.getAddressName().length() > 50 ?
                sampling.getAddressName().substring(0, 50) : sampling.getAddressName());
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
     * set up SampFormBean
     *
     * @param sampling
     * @return
     */
    public static PreciptationSampForm.SampFormBean setYqfUpSampFormBean(Sampling sampling) {
        PreciptationSampForm.SampFormBean sampFormBean = new PreciptationSampForm.SampFormBean();
        sampFormBean.setProjectId(sampling.getProjectId());
        sampFormBean.setId(sampling.getId());
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
        sampFormBean.setSamplingTimeBegin(sampling.getSamplingTimeBegin() == null ? DateUtils.getDate(new Date()) : sampling.getSamplingTimeBegin());
        sampFormBean.setSamplingTimeEnd(CheckUtil.isEmpty(sampling.getSamplingTimeEnd()) ? sampFormBean.getSamplingTimeBegin() : sampling.getSamplingTimeEnd());
        sampFormBean.setTagName(sampling.getTagName());
        sampFormBean.setTagId(sampling.getTagId());
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
                if (CheckUtil.isEmpty(samplingDetail.getMonitemId())) {
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
                if (samplingDetail.getOrderIndex() == 0) {
                    samplingDetailsBean.setOrderIndex(count + "");
                } else {
                    samplingDetailsBean.setOrderIndex(samplingDetail.getOrderIndex() + "");
                }
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

    private static List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> setYqfUpSamplingDetailDataList(Sampling sampling) {
        List<SamplingDetailYQFs> samplingDetailsList =DbHelpUtils.getSamplingDetailYQFsList(sampling.getId());
        if (!CheckUtil.isEmpty(samplingDetailsList)) {
            int count = 1;
            Collections.sort(samplingDetailsList, new TestRecordFragment.DetailComparator());
            List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> samplingDetailsBeansList = new ArrayList<>();
            for (SamplingDetailYQFs samplingDetail : samplingDetailsList) {
                if (CheckUtil.isEmpty(samplingDetail.getMonitemId())) {
                    continue;
                }
                PreciptationSampForm.SampFormBean.SamplingDetailsBean samplingDetailsBean =
                        new PreciptationSampForm.SampFormBean.SamplingDetailsBean();
                samplingDetailsBean.setProjectId(sampling.getProjectId());
                samplingDetailsBean.setSampingCode(samplingDetail.getSampingCode());
                samplingDetailsBean.setFrequecyNo(samplingDetail.getFrequecyNo() + "");
                samplingDetailsBean.setOrderIndex(count + "");
                samplingDetailsBean.setMonitemId(samplingDetail.getMonitemId());
                samplingDetailsBean.setSamplingId(samplingDetail.getSamplingId());
                samplingDetailsBean.setSamplingType(samplingDetail.getSamplingType() + "");
                samplingDetailsBean.setSamplingOnTime(samplingDetail.getSamplingOnTime());
                samplingDetailsBean.setValue(samplingDetail.getValue());
                samplingDetailsBean.setPrivateData(samplingDetail.getPrivateData());
                samplingDetailsBeansList.add(samplingDetailsBean);
                count++;
            }
            return samplingDetailsBeansList;
        }
        return null;
    }

    /**
     * 降水SamplingDetail设置
     *
     * @param sampling
     * @return
     */
    private static List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> setUpJsSamplingDetailDataList(Sampling sampling) {
        List<SamplingDetail> samplingDetailsList = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(sampling.getId())).list();
        if (!CheckUtil.isEmpty(samplingDetailsList)) {
            List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> samplingDetailsBeansList = new ArrayList<>();
            int count = 1;
            for (SamplingDetail samplingDetail : samplingDetailsList) {
                if (!samplingDetail.getMonitemName().equals("降水量")) {
                    continue;
                }
                PreciptationSampForm.SampFormBean.SamplingDetailsBean samplingDetailsBean = new PreciptationSampForm.SampFormBean.SamplingDetailsBean();
                samplingDetailsBean.setSampingCode(samplingDetail.getSampingCode());
                samplingDetailsBean.setSamplingId(samplingDetail.getSamplingId());
                samplingDetailsBean.setProjectId(sampling.getProjectId());
                samplingDetailsBean.setIsSenceAnalysis(true);
                samplingDetailsBean.setId(samplingDetail.getId());
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
                samplingDetailsBean.setValue1(samplingDetail.getValue1());
                if (sampling.getMonitemId() == null || sampling.getMethodId().equals("")) {
                    sampling.setMonitemId(SamplingUtil.setPrecipiationMonitemId(sampling));
                }
                if (!CheckUtil.isEmpty(sampling.getMonitemId())) {
                    String[] monItemId = sampling.getMonitemId().split(",");
                    for (String s : monItemId) {
                        if (!CheckUtil.isEmpty(s)) {
                            PreciptationSampForm.SampFormBean.SamplingDetailsBean detail =
                                    new PreciptationSampForm.SampFormBean.SamplingDetailsBean();
                            detail.setAddresssId(sampling.getAddressId());
                            detail.setAddressName(sampling.getAddressName());
                            detail.setSampingCode(samplingDetail.getSampingCode());
                            detail.setSamplingId(samplingDetail.getSamplingId());
                            detail.setProjectId(sampling.getProjectId());
                            detail.setFrequecyNo(samplingDetail.getFrequecyNo() + "");
                            detail.setMonitemId(s);
                            detail.setOrderIndex(count + "");
                            detail.setMonitemName(HelpUtil.getMonItemNameById(s, sampling));
                            detail.setIsSenceAnalysis(false);
                            detail.setValue(samplingDetail.getValue());
                            detail.setPrivateData(samplingDetail.getPrivateData());
                            detail.setValue1(samplingDetail.getValue1());
                            detail.setDescription(samplingDetail.getDescription());
                            samplingDetailsBeansList.add(detail);
                        }
                    }
                }
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
                String saveMethod = samplingFormStand.getSaveMehtod();
                saveMethod = saveMethod.replaceAll("%(?![0-9a-fA-F]{2})", "百分之");
                samplingFormStandsBean.setSaveMehtod(saveMethod);
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
        List<SamplingFile> dataList = DBHelper.get().getSamplingFileDao().queryBuilder().where(SamplingFileDao.Properties.SamplingId.eq(sampling.getId()), SamplingFileDao.Properties.Id.notEq(""), SamplingFileDao.Properties.IsUploaded.eq(false)).list();
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
        PreciptationSampForm.SampFormBean sampFormBean = setYqfUpSampFormBean(sampling);
        sampFormBean.setSamplingDetails(new ArrayList<>());
        sampFormBean.setSamplingFormStands(new ArrayList<>());

        preciptationSampForm.setSampForm(sampFormBean);

        //设置仪器法检测记录
        List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> detailsBeanList = setYqfUpSamplingDetailDataList(sampling);
        if (!CheckUtil.isEmpty(detailsBeanList)) {
            sampFormBean.setSamplingDetailYQFs(detailsBeanList);
        }


        return preciptationSampForm;
    }

    /**
     * 组织噪声 工业企业界提交数据
     *
     * @return
     */
    public static PreciptationSampForm setNoiseIndustralData(Sampling sampling) {
        PreciptationSampForm preciptationSampForm = new PreciptationSampForm();
        preciptationSampForm.setIsAdd(true);
        preciptationSampForm.setIsSubmit(true);
        PreciptationSampForm.SampFormBean sampFormBean = setUpSampFormBean(sampling);
        List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> detailsBeanList = setUpJsSamplingDetailDataList(sampling);
        if (!CheckUtil.isEmpty(detailsBeanList)) {
            sampFormBean.setSamplingDetails(detailsBeanList);
        } else {
            sampFormBean.setSamplingDetails(new ArrayList<>());
        }
        List<PreciptationSampForm.SampFormBean.SamplingFormStandsBean> bottleSplitDataList = setUpBottleSplitDataList(sampling);
        if (!CheckUtil.isEmpty(bottleSplitDataList)) {
            sampFormBean.setSamplingFormStands(bottleSplitDataList);
        } else {
            sampFormBean.setSamplingFormStands(new ArrayList<>());
        }
        preciptationSampForm.setSampForm(sampFormBean);
        sampFormBean.setSamplingDetailYQFs(new ArrayList<>());
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
        List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> detailsBeanList = setUpJsSamplingDetailDataList(sampling);
        if (!CheckUtil.isEmpty(detailsBeanList)) {
            sampFormBean.setSamplingDetails(detailsBeanList);
        } else {
            sampFormBean.setSamplingDetails(new ArrayList<>());
        }
        //setUpBottleSplitDataList
        List<PreciptationSampForm.SampFormBean.SamplingFormStandsBean> bottleSplitDataList = setUpBottleSplitDataList(sampling);
        if (!CheckUtil.isEmpty(bottleSplitDataList)) {
            sampFormBean.setSamplingFormStands(bottleSplitDataList);
        } else {
            sampFormBean.setSamplingFormStands(new ArrayList<>());
        }
        preciptationSampForm.setSampForm(sampFormBean);
        sampFormBean.setSamplingDetailYQFs(new ArrayList<>());

        return preciptationSampForm;
    }




    /**
     * 修改采样方案数据组装
     *
     * @return
     */
    private static List<ProjectDetial> generateProjectDetials(String monitorIds, Project project) {
        List<ProjectDetial> dataList = new ArrayList<>();
        if (!CheckUtil.isEmpty(monitorIds)) {
            String[] monitorIdArray = monitorIds.split(",");
            if (!CheckUtil.isEmpty(monitorIdArray)) {
                for (String monitorId : monitorIdArray) {
                    ProjectDetial projectDetial = getProjectDetials(monitorId, project);
                    if (!CheckUtil.isNull(projectDetial)) {
                        dataList.add(projectDetial);
                    }
                }
            }
        }
        return dataList;
    }

    private static ProjectDetial getProjectDetials(String monitorId, Project project) {
        List<ProjectDetial> projectDetialsList = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(project.getId())).list();
        ProjectDetial projectDetial = null;
        if (!CheckUtil.isEmpty(projectDetialsList)) {
            for (ProjectDetial projectDetial1 : projectDetialsList) {
                String monitorIdStr = projectDetial1.getMonItemId();
                if (!CheckUtil.isEmpty(monitorIdStr) && !CheckUtil.isEmpty(monitorId) && monitorIdStr.equals(monitorId)) {
                    projectDetial = projectDetial1;
                    break;
                }
            }
        }
        return projectDetial;
    }


}
