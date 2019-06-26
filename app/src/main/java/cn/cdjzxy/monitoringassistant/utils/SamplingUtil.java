package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;
import android.os.Message;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetailYQFs;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity.mProject;

/**
 * 2019年5月7日 嘉泽向昆杰   优化代码——进行中
 */
public class SamplingUtil {

    /**
     * 降水表单路径/路径
     */
    public static final String PATH_PRECIPITATION = "/FormTemplate/FILL_JS_GAS_XD";
    public static final String NAME_PRECIPITATION = "降水采样及样品交接记录（新都）";

    /**
     * 废水表单路径/路径
     */
    public static final String PATH_WASTE_WATER = "/FormTemplate/FILL_WATER_NEW_XD";
    public static final String NAME_WASTE_WATER = "水和废水样品采集与交接记录（新都）";

    /**
     * 仪器法表单路径/路径
     */
    public static final String PATH_INSTRUMENTAL = "/FormTemplate/FILL_YQF_WATER";
    public static final String NAME_INSTRUMENTAL = "现场监测仪器法";

    /**
     * 工业企业厂界噪声监测记录
     */
    public static final String PATH_NOISE_FACTORY = "/FormTemplate/FILL_GYZS_VOICE_XD";
    public static final String NAME_NOISE_FACTORY = "工业企业厂界噪声监测记录";

    public static final int SAMPLING_MESSAGE = 1001;//采样点消息

    /**
     * 判断当前表单是否是本人的
     *
     * @return@true本人表单，@false他人表单
     */
    public static boolean isMySampling(String submitId) {
        UserInfo user = UserInfoHelper.get().getUserInfo();
        return user.getId().equals(submitId);
    }

    /**
     * 判断表单是否提交
     *
     * @param status
     * @return
     */
    public static boolean samplingIsSubmit(int status) {
        return status != 0 && status != 4 && status != 9;
    }

    /**
     * 创建采样单:水和废水
     *
     * @return
     */
    public static Sampling createWaterSample(String projectId, String formSelectId) {
        Project project = DbHelpUtils.getDbProject(projectId);
        FormSelect formSelect = DbHelpUtils.getDbFormSelect(formSelectId);
        Sampling sampling = new Sampling();
        sampling.setId(UUID.randomUUID().toString());//唯一标志
        sampling.setSamplingNo(createSamplingNo());
        sampling.setProjectId(project.getId());
        sampling.setProjectName(project.getName());
        sampling.setProjectNo(project.getProjectNo());
        //sampling.setTagId(formSelect.getTagId());
        //sampling.setMontype(project.getMonType() + "");
        sampling.setMontype(project.getTypeCode());
        //sampling.setTagName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagId())).unique().getName());
        sampling.setFormType(formSelect.getTagParentId());
//        sampling.setFormTypeName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagParentId())).unique().getName());
        sampling.setFormTypeName("水");//Tip:毛阳说写死
        sampling.setFormName(formSelect.getFormName());
        sampling.setFormPath(formSelect.getPath());
        //        sampling.setFormFlows(formSelect.getFormFlows().toString());
        sampling.setParentTagId(formSelect.getTagParentId());
        sampling.setStatusName("进行中");
        sampling.setStatus(0);
        sampling.setSamplingUserId(UserInfoHelper.get().getUser().getId());
        sampling.setSamplingUserName(UserInfoHelper.get().getUser().getName());
        sampling.setSamplingTimeBegin(DateUtils.getDate());
        sampling.setSamplingDetailResults(new ArrayList<>());
        sampling.setSamplingContentResults(new ArrayList<>());
        sampling.setIsLocal(true);
        sampling.setIsUpload(false);
        sampling.setIsCanEdit(true);
        return sampling;
    }

    public static Sampling createPrecipitationSample(Project project, String formSelectId) {
        FormSelect formSelect = DbHelpUtils.getDbFormSelect(formSelectId);
        Sampling sampling = new Sampling();
        sampling.setId("LC-" + UUID.randomUUID().toString());
        sampling.setSamplingNo(createSamplingNo());
        sampling.setProjectId(project.getId());
        sampling.setProjectName(project.getName());
        sampling.setProjectNo(project.getProjectNo());
        sampling.setTagId(formSelect.getTagId());
        sampling.setMontype(project.getTypeCode());
        sampling.setTagName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagId())).unique().getName());
        sampling.setFormType(formSelect.getTagParentId());
        sampling.setFormTypeName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagParentId())).unique().getName());
        sampling.setFormName(formSelect.getFormName());
        sampling.setFormPath(formSelect.getPath());
        //        sampling.setFormFlows(formSelect.getFormFlows().toString());
        sampling.setParentTagId(formSelect.getTagParentId());
        sampling.setStatusName("进行中");
        sampling.setStatus(0);
        sampling.setSamplingUserId(UserInfoHelper.get().getUser().getId());
        sampling.setSamplingUserName(UserInfoHelper.get().getUser().getName());
        sampling.setSamplingTimeBegin(DateUtils.getDate());
        sampling.setSamplingDetailResults(new ArrayList<>());
        sampling.setSamplingFiless(new ArrayList<>());
        sampling.setIsLocal(true);
        sampling.setIsUpload(false);
        sampling.setIsCanEdit(true);
        return sampling;
    }

    /**
     * 创建仪器法单子
     *
     * @param projectId
     * @param formSelectId
     * @return
     */
    public static Sampling createInstrumentalSampling(String projectId, String formSelectId) {
        Project project = DbHelpUtils.getDbProject(projectId);
//        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(projectId)).list();

        FormSelect formSelect = DbHelpUtils.getDbFormSelect(formSelectId);

        Sampling sampling = new Sampling();
        sampling.setId("LC-" + UUID.randomUUID().toString());
        sampling.setSamplingNo(createSamplingNo());
        sampling.setProjectId(project.getId());
        sampling.setProjectName(project.getName());
        sampling.setProjectNo(project.getProjectNo());
        sampling.setTagId(formSelect.getTagId());
        sampling.setMontype(project.getTypeCode());
        sampling.setTagId(project.getMonType());
        sampling.setTagName(project.getMonType());
        sampling.setAddressNo("");
        sampling.setFormType(formSelect.getTagParentId());
        sampling.setFormTypeName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagParentId())).unique().getName());
        sampling.setFormName(formSelect.getFormName());
        sampling.setFormPath(formSelect.getPath());
        sampling.setParentTagId(formSelect.getTagParentId());
        sampling.setStatus(0);
        sampling.setSamplingUserId(UserInfoHelper.get().getUser().getId());
        sampling.setSamplingUserName(UserInfoHelper.get().getUser().getName());
        sampling.setMonitorPerson(UserInfoHelper.get().getUser().getName());
        sampling.setSamplingTimeBegin(DateUtils.getDate());
        sampling.setSamplingTimeEnd(DateUtils.getDate());
        sampling.setSamplingDetailResults(new ArrayList());
        sampling.setSamplingFiless(new ArrayList());
        sampling.setSamplingDetailYQFs(new ArrayList());
        sampling.setSamplingFormStandResults(new ArrayList<>());
        sampling.setIsLocal(true);
        sampling.setIsUpload(false);
        sampling.setIsCanEdit(true);
        sampling.setStatusName("等待提交");
        sampling.setSendSampTime("");
        sampling.setLayTableCheckbox("on");
        sampling.setTransStatus(3);
        sampling.setTransStatusName("等待提交");
        sampling.setCurUserId(UserInfoHelper.get().getUser().getId());
        sampling.setCurUserName(UserInfoHelper.get().getUser().getName());
        sampling.setAddTime(DateUtils.getTime(new Date().getTime()));
        sampling.setUpdateTime(DateUtils.getTime(new Date().getTime()));
        sampling.setVersion(0);
        sampling.setFormFlows("");
        sampling.setTagId("");
        sampling.setTagName("");
        sampling.setAddressId("");
        sampling.setAddressName("");
        sampling.setComment("");

//        if (!CheckUtil.isEmpty(projectDetials)) {
//            for (ProjectDetial projectDetial : projectDetials) {
//                if (!sampling.getTagId().contains(projectDetial.getMonItemId())) {
//                    if (!TextUtils.isEmpty(sampling.getTagId())) {
//                        sampling.setTagId(sampling.getTagId() + ",");
//                    }
//                    sampling.setTagId(sampling.getTagId() + projectDetial.getMonItemId());
//                }
//
//                if (!sampling.getTagName().contains(projectDetial.getMonItemName())) {
//                    if (!TextUtils.isEmpty(sampling.getTagName())) {
//                        sampling.setTagName(sampling.getTagName() + ",");
//                    }
//                    sampling.setTagName(sampling.getTagName() + projectDetial.getMonItemName());
//                }
//                if (!sampling.getAddressId().contains(projectDetial.getAddressId())) {
//                    if (!TextUtils.isEmpty(sampling.getAddressId())) {
//                        sampling.setAddressId(sampling.getAddressId() + ",");
//                    }
//                    sampling.setAddressId(sampling.getAddressId() + projectDetial.getAddressId());
//                }
//
//                if (!sampling.getAddressName().contains(projectDetial.getAddress())) {
//                    if (!TextUtils.isEmpty(sampling.getAddressName())) {
//                        sampling.setAddressName(sampling.getAddressName() + ",");
//                    }
//                    sampling.setAddressName(sampling.getAddressName() + projectDetial.getAddress());
//                }
//            }
//        }

        HashMap<String, String> privateData = new HashMap<>();
        privateData.put("CaleValue", "");
        privateData.put("RPDValue", "");
        privateData.put("SamplingOnTime", "");
        privateData.put("HasPX", "false");
        privateData.put("FormTypeName", "");
        privateData.put("SourceWay", "");
        privateData.put("SourceDate", "");
        privateData.put("DeviceText", "");
        sampling.setPrivateData(com.alibaba.fastjson.JSONObject.toJSONString(privateData));

        return sampling;
    }

    /**
     * 创建采样单：噪声
     *
     * @param projectId
     * @param formSelectId
     * @return
     */
    public static Sampling createNoiseSample(String projectId, String formSelectId) {
        Project project = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
        FormSelect formSelect = DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.FormId.eq(formSelectId)).unique();
        Sampling sampling = new Sampling();
        sampling.setId(UUID.randomUUID().toString());//唯一标志
        sampling.setSamplingNo(createSamplingNo());
        sampling.setProjectId(project.getId());
        sampling.setProjectName(project.getName());
        sampling.setProjectNo(project.getProjectNo());
        sampling.setTagId(formSelect.getTagId());
        sampling.setParentTagId(formSelect.getTagParentId());
        sampling.setMonitemName("工业企业厂界噪声");
        MonItems monItems = DbHelpUtils.getMonItems("工业企业厂界噪声");
        sampling.setMonitemId(CheckUtil.isNull(monItems) ? "" : monItems.getId());//为了检测方法
//        sampling.setMontype(project.getMonType() + "");
        sampling.setMontype(project.getTypeCode());
        sampling.setTagName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagId())).unique().getName());
        sampling.setFormType(formSelect.getTagParentId());
        sampling.setFormTypeName(DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(formSelect.getTagParentId())).unique().getName());
        sampling.setFormTypeName("水");//Tip:毛阳说写死
        sampling.setFormName(formSelect.getFormName());
        sampling.setFormPath(formSelect.getPath());
//        sampling.setFormFlows(formSelect.getFormFlows()());
        sampling.setParentTagId(formSelect.getTagParentId());
        sampling.setStatusName("进行中");
        sampling.setStatus(0);
        sampling.setSamplingUserId(UserInfoHelper.get().getUser().getId());
        sampling.setSamplingUserName(UserInfoHelper.get().getUser().getName());
        sampling.setSamplingTimeBegin(DateUtils.getDate());
        sampling.setSamplingDetailResults(new ArrayList<>());
        sampling.setSamplingContentResults(new ArrayList<>());
        sampling.setIsLocal(true);
        sampling.setIsUpload(false);
        sampling.setIsCanEdit(true);
        return sampling;

    }

    /**
     * 创建采样单编号:年月日+账号+流水号
     *
     * @return
     */
    public static String createSamplingNo() {
        StringBuilder samplingNo = new StringBuilder();
        String dateStr = DateUtils.getDate().replace("-", "").substring(2);
        samplingNo.append(dateStr).append("-");
        samplingNo.append(UserInfoHelper.get().getUser().getIntId()).append("-");
//        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%"), SamplingDao.Properties.ProjectId.eq(projectId)).orderAsc(SamplingDao.Properties.SamplingNo).list();
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%")).
                orderAsc(SamplingDao.Properties.SamplingNo).list();

        if (CheckUtil.isEmpty(samplings)) {
            samplingNo.append(StringUtil.autoGenericCode(1, 2));
        } else {
            String lastSamlingNo = samplings.get(samplings.size() - 1).getSamplingNo();
            if (!CheckUtil.isEmpty(lastSamlingNo)) {
                int serialNumber = Integer.parseInt(lastSamlingNo.substring(lastSamlingNo.length() - 2)) + 1;
                samplingNo.append(StringUtil.autoGenericCode(serialNumber, 2));
            } else {
                samplingNo.append(StringUtil.autoGenericCode(1, 2));
            }
        }
        return samplingNo.toString();
    }

    /**
     * 创建采样单编号:年月日-账号-流水号
     *
     * @return
     */
    public static String createSamplingNo(String data) {
        StringBuilder samplingNo = new StringBuilder();
        String dateStr = data.replace("-", "").substring(2);
        samplingNo.append(dateStr).append("-");
        samplingNo.append(UserInfoHelper.get().getUser().getIntId());
//        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%"), SamplingDao.Properties.ProjectId.eq(projectId)).orderAsc(SamplingDao.Properties.SamplingNo).list();
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%")).
                orderAsc(SamplingDao.Properties.SamplingNo).list();

        if (CheckUtil.isEmpty(samplings)) {
            samplingNo.append("-").append(StringUtil.autoGenericCode(1, 2));
        } else {
            String lastSamlingNo = samplings.get(samplings.size() - 1).getSamplingNo();
            if (!CheckUtil.isEmpty(lastSamlingNo)) {
                int serialNumber = Integer.parseInt(lastSamlingNo.substring(lastSamlingNo.length() - 2)) + 1;
                samplingNo.append("-").append(StringUtil.autoGenericCode(serialNumber, 2));
            } else {
                samplingNo.append("-").append(StringUtil.autoGenericCode(1, 2));
            }
        }
        return samplingNo.toString();
    }

    /**
     * 样品编码规则 样品性质年月日——采样单流水号账号——采样号
     * 样品性质：
     * 1.水质
     * 地下水、海水、废水、地表水     ===》  DXS/HS/FS/DBS
     * 2.空气
     * 无组织废气、环境空气、室内空气  ===》  WF/HK/SK
     * 3.废气
     * 有组织废气                  ===》  YF
     * 4.降水
     * 降水                       ===》  JS
     * <p>
     * <p>
     * 注这里只返回：年月日——采样单流水号账号
     *
     * @return 年月日——采样单流水号账号
     */
    public static String createSamplingFrequecyNo() {
        StringBuilder samplingNo = new StringBuilder();
        String dateStr = DateUtils.getDate().replace("-", "").substring(2);
        samplingNo.append(dateStr).append("-");
//        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%"), SamplingDao.Properties.ProjectId.eq(projectId)).orderAsc(SamplingDao.Properties.SamplingNo).list();
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%")).
                orderAsc(SamplingDao.Properties.SamplingNo).list();
        if (CheckUtil.isEmpty(samplings)) {
            samplingNo.append(StringUtil.autoGenericCode(1, 2));
        } else {
            String lastSamlingNo = samplings.get(samplings.size() - 1).getSamplingNo();
            if (!CheckUtil.isEmpty(lastSamlingNo)) {
                int serialNumber = Integer.parseInt(lastSamlingNo.substring(lastSamlingNo.length() - 2)) + 1;
                samplingNo.append(StringUtil.autoGenericCode(serialNumber, 2));
            } else {
                samplingNo.append(StringUtil.autoGenericCode(1, 2));
            }
        }
        samplingNo.append(UserInfoHelper.get().getUser().getIntId());
        return samplingNo.toString();
    }

    /**
     * 样品编码规则 样品性质年月日——采样单流水号账号——采样号
     * 样品性质：
     * 1.水质
     * 地下水、海水、废水、地表水     ===》  DXS/HS/FS/DBS
     * 2.空气
     * 无组织废气、环境空气、室内空气  ===》  WF/HK/SK
     * 3.废气
     * 有组织废气                  ===》  YF
     * 4.降水
     * 降水                       ===》  JS
     * <p>
     * <p>
     * 注这里只返回：年月日——采样单流水号账号
     *
     * @return 年月日——采样单流水号账号
     */
    public static String createSamplingFrequecyNo(String data) {
        StringBuilder samplingNo = new StringBuilder();
        String dateStr = data.replace("-", "").substring(2);
        samplingNo.append(dateStr);
//        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%"), SamplingDao.Properties.ProjectId.eq(projectId)).orderAsc(SamplingDao.Properties.SamplingNo).list();
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%")).
                orderAsc(SamplingDao.Properties.SamplingNo).list();
        if (CheckUtil.isEmpty(samplings)) {
            samplingNo.append("-").append(StringUtil.autoGenericCode(1, 2));
        } else {
            String lastSamlingNo = samplings.get(samplings.size() - 1).getSamplingNo();
            if (!CheckUtil.isEmpty(lastSamlingNo)) {
                int serialNumber = Integer.parseInt(lastSamlingNo.substring(lastSamlingNo.length() - 2));
                samplingNo.append("-").append(StringUtil.autoGenericCode(serialNumber, 2));
            } else {
                samplingNo.append("-").append(StringUtil.autoGenericCode(1, 2));
            }
        }
        samplingNo.append(UserInfoHelper.get().getUser().getIntId());
        return samplingNo.toString();
    }

    /**
     * 判断当前表单能否编辑
     * 通过采样单状态
     * {@sampling.getStatus:0等待提交，1等待收样审核，2等待室主任审核，
     * 3等待质控审核，4打回重新提交，5作废，6通过审核，7等待检验审核，8协助人员审核，9撤回待提交，10app上传服务器保存
     * }
     * 和采样是否有本人有关
     *
     * @param sampling
     * @return
     */
    public static boolean setSampIsCanEdit(Sampling sampling) {
        return (sampling.getStatus() == 0 ||
                sampling.getStatus() == 4 ||
                sampling.getStatus() == 9
        )
                && sampling.getSamplingUserId().
                contains(UserInfoHelper.get().getUserInfo().getId());
    }

    /**
     * 判断采样单是否完成
     *
     * @param sampling
     * @return
     */
    public static boolean isSamplingFinsh(Sampling sampling) {
        switch (sampling.getFormPath()) {
            case PATH_PRECIPITATION://降水
                return isPrecipitationSamplingFinish(sampling);
            case PATH_WASTE_WATER://水和废水
                return isWasterSamplingFinish(sampling);
            case PATH_INSTRUMENTAL://仪器法
                return isInstrumentSamplingFinish(sampling) == "";
            case PATH_NOISE_FACTORY://噪声
                return isNoiseSamplingFinsh(sampling);
            default://还没有开发
                return false;

        }
    }

    /**
     * 判断噪声表是否采样完成
     *
     * @param sampling
     * @return
     */
    public static boolean isNoiseSamplingFinsh(Sampling sampling) {
        if (CheckUtil.isEmpty(sampling.getSamplingTimeBegin())) {//检测日期必填项
            return false;
        }
        if (CheckUtil.isEmpty(sampling.getPrivateData()))//采样检测信息
            return false;
        NoisePrivateData privateData = new Gson().fromJson(sampling.getPrivateData(),
                NoisePrivateData.class);
        return !CheckUtil.isEmpty(privateData.getMianNioseAddr());
    }

    /**
     * 降水采样单采样是否完成
     *
     * @return
     */
    public static boolean isPrecipitationSamplingFinish(Sampling mSampling) {
        if (CheckUtil.isEmpty(mSampling.getSamplingDetailResults())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getSamplingUserName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getTagName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getAddressName())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getPrivateData())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSampling.getMethodName())) {
            return false;
        }
        return !CheckUtil.isEmpty(mSampling.getDeviceName());
    }

    /**
     * 采样是否完成
     * 仪器法表
     *
     * @return
     */
    public static String isInstrumentSamplingFinish(Sampling sampling) {
        if (CheckUtil.isEmpty(sampling.getSamplingUserName())) {
            return "请选择“监测人”";
        }

        if (CheckUtil.isEmpty(sampling.getSamplingTimeBegin())) {
            return "请选择“分析开始日期”";
        }
        if (CheckUtil.isEmpty(sampling.getSamplingTimeEnd())) {
            return "请选择“分析结束日期”";
        }

        if (CheckUtil.isEmpty(sampling.getMonitemId())) {
            return "请选择“监测项目”";
        }

        if (CheckUtil.isEmpty(sampling.getMethodName())) {
            return "请选择“监测方法”";
        }

        if (CheckUtil.isEmpty(sampling.getDeviceName())) {
            return "请选择“监测设备”";
        }

        if (CheckUtil.isEmpty(sampling.getSamplingDetailYQFs())) {
            return "请添加监测结果";
        }

        for (SamplingDetailYQFs detail : sampling.getSamplingDetailYQFs()) {
            SamplingDetailYQFs.PrivateJsonData privateData =detail.getJsonPrivateData();
            if (CheckUtil.isEmpty(privateData.getSamplingOnTime())) {
                return String.format("监测结果[%s]需选择分析时间", detail.getSampingCode());//没有填写分析时间
            }
            if (CheckUtil.isEmpty(privateData.getCaleValue())) {
                return String.format("监测结果[%s]需填写分析结果", detail.getSampingCode());//没有填写分析结果
            }
            if (CheckUtil.isEmpty(privateData.getValueUnit())) {
                return String.format("监测结果[%s]需填写结果单位", detail.getSampingCode());//没有填写结果单位
            }
        }

        return "";
    }

    /**
     * 判断水和废水采样单是否完成
     *
     * @param mSample
     * @return
     */
    public static boolean isWasterSamplingFinish(Sampling mSample) {
        if (CheckUtil.isEmpty(mSample.getSamplingDetailResults())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getSamplingUserId())) {
            return false;
        }
        if (CheckUtil.isEmpty(mSample.getTagId())) {
            return false;
        }
        return !CheckUtil.isEmpty(mSample.getAddressId());
    }

    /**
     * 设置采样单的MonitemName
     *
     * @param mSampling
     * @return
     */
    public static String setPrecipiationMonitemName(Sampling mSampling) {
        if (mSampling.getAddressId() == null || mSampling.getAddressId().equals("")) return "";
        if (mSampling.getTagId() == null || mSampling.getTagId().equals("")) return "";
        List<ProjectDetial> projectDetialList = DbHelpUtils.getProjectDetialList(mProject.getId()
                , mSampling.getAddressId(), mSampling.getTagId());
        if (CheckUtil.isEmpty(projectDetialList)) {
            return "";
        }
        StringBuilder stringBuilderName = new StringBuilder();


        for (ProjectDetial content : projectDetialList) {
            stringBuilderName.append(content.getMonItemName()).append(",");
        }
        if (stringBuilderName.lastIndexOf(",") > 0) {
            stringBuilderName.deleteCharAt(stringBuilderName.lastIndexOf(","));
        }
        return stringBuilderName.toString();
    }

    /**
     * 设置采样单的MonitemId
     *
     * @param mSampling
     * @return
     */
    public static String setPrecipiationMonitemId(Sampling mSampling) {
        if (mSampling.getAddressId() == null || mSampling.getAddressId().equals("")) return "";
        if (mSampling.getTagId() == null || mSampling.getTagId().equals("")) return "";
        List<ProjectDetial> projectDetialList = DbHelpUtils.getProjectDetialList(mProject.getId()
                , mSampling.getAddressId(), mSampling.getTagId());
        if (CheckUtil.isEmpty(projectDetialList)) {
            return "";
        }
        StringBuilder stringBuilderId = new StringBuilder();

        for (ProjectDetial content : projectDetialList) {
            stringBuilderId.append(content.getMonItemId()).append(",");
        }
        if (stringBuilderId.lastIndexOf(",") > 0)
            stringBuilderId.deleteCharAt(stringBuilderId.lastIndexOf(","));
        return stringBuilderId.toString();
    }

    /**
     * 保存任务
     *
     * @param project 任务
     */
    public static void saveProjectData(Project project) {
        if (RxDataTool.isEmpty(DbHelpUtils.getDbProject(project.getId()))) {
            //数据库没有就直接插入
            DBHelper.get().getProjectDao().insert(project);
        } else {
            //有得话就直接更新
            DBHelper.get().getProjectDao().update(project);
        }

    }

    /**
     * 批量保存任务详情数据 ProjectDetial ProjectContent
     *
     * @param projects 任务集合
     */
    public static void saveProjectDetailList(List<Project> projects) {
        new Thread() {
            @Override
            public void run() {
                for (Project project : projects)
                    synchronized (project) {
                        saveProjectDetail(project);
                    }
            }
        }.start();

    }

    /**
     * 保存任务详情ProjectDetial ProjectContent
     *
     * @param project 任务
     */
    public static void saveProjectDetail(Project project) {
        Project dbProject = DbHelpUtils.getDbProject(project.getId());
        if (!RxDataTool.isNull(dbProject) &&
                dbProject.getCanSamplingEidt() && //第一步判断是否有权限修改
                dbProject.getIsEditProjectContent()) {//并且判断本地是否有采样数据没有提交
            //如果是的话，就不用保存，以本地为准
            return;
        }
        List<ProjectContent> contentList = project.getProjectContents();
        List<ProjectDetial> detailList = project.getProjectDetials();
        List<ProjectDetial> dbDetailList = DbHelpUtils.getProjectDetialList(project.getId());
        List<ProjectContent> dbContentList = DbHelpUtils.getProjectContentList(project.getId());

        if (!RxDataTool.isEmpty(dbContentList)) {
            DBHelper.get().getProjectContentDao().deleteInTx(dbContentList);
        }
        if (!RxDataTool.isEmpty(contentList)) {
            DBHelper.get().getProjectContentDao().insertInTx(contentList);
        }
        if (!RxDataTool.isEmpty(dbDetailList)) {
            DBHelper.get().getProjectDetialDao().deleteInTx(detailList);
        }
        if (!RxDataTool.isEmpty(detailList)) {
            DBHelper.get().getProjectDetialDao().insertInTx(detailList);
        }
    }
/**********************************************************************************************/
    /**
     * 上传采样单
     *
     * @param sampling       采样单
     * @param isCompelSubmit 是否强制提交
     */
    public static void uploadSamplingData(Sampling sampling, boolean isCompelSubmit,
                                          Context context, Message message) {
        uploadSamplingFile(sampling, context, message);
    }

    /**
     * 上传采样单文件
     * 上传文件这里需要改一下：因为噪声表里面有测定示意图的图片 没有保存到文件集合，所以这里需要区别对待
     *
     * @param sampling 采样单
     * @param context  @context
     * @param message  回调
     */
    public static void uploadSamplingFile(Sampling sampling, Context context, Message message) {
        if (sampling.getFormPath().equals(PATH_NOISE_FACTORY)) {
            uploadSamplingNoiseFile(sampling, context, message);
            return;
        } else {

        }

    }

    /**
     * 上传噪声采样表文件：
     * 第一步：先上传测点示意图的图片，第二步上传文件集合图片
     *
     * @param sampling
     * @param message
     */
    private static void uploadSamplingNoiseFile(Sampling sampling, Context context,
                                                Message message) {
        if (sampling.getPrivateData() != null) {
            NoisePrivateData privateData = new Gson().fromJson(sampling.getPrivateData(), NoisePrivateData.class);
            if (privateData.getImageSYT() != null && !privateData.getImageSYT().equals("")
                    && !privateData.getImageSYT().startsWith("/Upload")) {
                File file = new File(privateData.getImageSYT());
                List<File> list = new ArrayList<>();
                list.add(file);
                FileUtils.compressPic(list, context, message, new FileUtils.PictureCompressCallBack() {
                    @Override
                    public void onSuccess(List<File> list) {

                    }

                    @Override
                    public void onFailed(String message) {

                    }
                });
            }
        }

    }


    public interface FileUploadHandler {
        void onSuccess();

        void onFailed(String message);
    }
}
