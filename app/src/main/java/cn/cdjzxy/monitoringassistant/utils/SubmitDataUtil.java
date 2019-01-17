package cn.cdjzxy.monitoringassistant.utils;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;

public class SubmitDataUtil {

    /**
     * 组装废水提交数据
     * @param sampling
     * @return
     */
    public static PreciptationSampForm setUpFSData(Sampling sampling){
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
        return preciptationSampForm;
    }

    /**
     * set up SampFormBean
     * @param sampling
     * @return
     */
    public static PreciptationSampForm.SampFormBean setUpSampFormBean(Sampling sampling){
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
        sampFormBean.setComment(sampling.getComment());
        sampFormBean.setFormFlows(sampling.getFormFlows());
        return sampFormBean;
    }

    /**
     * 组装SamplingDetailDataList
     * @param sampling
     * @return
     */
    private static List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> setUpSamplingDetailDataList(Sampling sampling){
        List<SamplingDetail> samplingDetailsList = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(sampling.getId())).list();
        if (!CheckUtil.isEmpty(samplingDetailsList)){
            List<PreciptationSampForm.SampFormBean.SamplingDetailsBean> samplingDetailsBeansList = new ArrayList<>();
            int count = 1;
            for (SamplingDetail samplingDetail:samplingDetailsList){
                PreciptationSampForm.SampFormBean.SamplingDetailsBean samplingDetailsBean = new PreciptationSampForm.SampFormBean.SamplingDetailsBean();

                samplingDetailsBean.setSampingCode(samplingDetail.getSampingCode());
                samplingDetailsBean.setSamplingId(samplingDetail.getSamplingId());
                samplingDetailsBean.setProjectId(sampling.getProjectId());
                samplingDetailsBean.setIsSenceAnalysis(samplingDetail.getIsSenceAnalysis());
                //samplingDetailsBean.setSampStandId("00000000-0000-0000-0000-000000000000");
                samplingDetailsBean.setMonitemId(samplingDetail.getMonitemId());
                samplingDetailsBean.setMonitemName(samplingDetail.getMonitemName());
                samplingDetailsBean.setAddresssId(sampling.getAddressId());
                samplingDetailsBean.setAddressName(sampling.getAddressName());
                samplingDetailsBean.setOrderIndex(count + "");
                samplingDetailsBean.setFrequecyNo(samplingDetail.getFrequecyNo() + "");
                samplingDetailsBean.setSamplingTime(samplingDetail.getSamplingTime());
                samplingDetailsBean.setSamplingType(samplingDetail.getSamplingType()+"");
                samplingDetailsBean.setSamplingCount(samplingDetail.getSamplingCount()+"");
                samplingDetailsBean.setPreservative(samplingDetail.getPreservative());
                samplingDetailsBean.setIsCompare(samplingDetail.getIsCompare());
                samplingDetailsBean.setSampleCollection(samplingDetail.getSampleCollection());
                samplingDetailsBean.setSampleAcceptance(samplingDetail.getSampleAcceptance());
                samplingDetailsBean.setDescription(samplingDetail.getDescription());
                samplingDetailsBean.setPrivateData(samplingDetail.getPrivateData());

                samplingDetailsBeansList.add(samplingDetailsBean);
                count++;
            }
            return samplingDetailsBeansList;
        }
        return null;
    }

    /**
     * set up BottleSplitDataList
     * @param sampling
     * @return
     */
    private static List<PreciptationSampForm.SampFormBean.SamplingFormStandsBean> setUpBottleSplitDataList(Sampling sampling){
        List<SamplingFormStand> samplingFormStandsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(sampling.getId())).list();
        if (!CheckUtil.isEmpty(samplingFormStandsList)) {
            List<PreciptationSampForm.SampFormBean.SamplingFormStandsBean> samplingFormStandsBeansList = new ArrayList<>();
            for (SamplingFormStand samplingFormStand:samplingFormStandsList){
                PreciptationSampForm.SampFormBean.SamplingFormStandsBean samplingFormStandsBean=new PreciptationSampForm.SampFormBean.SamplingFormStandsBean();
                samplingFormStandsBean.setAnalysisSite(samplingFormStand.getAnalysisSite());
                samplingFormStandsBean.setContainer(samplingFormStand.getContainer());
                samplingFormStandsBean.setCount(samplingFormStand.getCount());
                samplingFormStandsBean.setId(samplingFormStand.getId());
                samplingFormStandsBean.setIndex(samplingFormStand.getIndex()+"");
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
     * 设置提交的降水数据
     * @param sampling
     * @return
     */
    public static PreciptationSampForm setUpJSData(Sampling sampling){
        PreciptationSampForm preciptationSampForm = new PreciptationSampForm();
        //设置外面数据
        preciptationSampForm.setIsAdd(true);
        preciptationSampForm.setIsSubmit(true);
        preciptationSampForm.setDevceForm(true);
        preciptationSampForm.setCompelSubmit(false);
        preciptationSampForm.setAddTime(sampling.getAddTime());
        preciptationSampForm.setUpdateTime(sampling.getUpdateTime());
        /*
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
        */
        return preciptationSampForm;
    }



}
