package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseSamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
        if (user.getId().equals(submitId)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断表单是否提交
     *
     * @param status
     * @return
     */
    public static boolean samplingIsSubmit(int status) {
        if (status == 0 || status == 4 || status == 9) {
            return false;
        } else {
            return true;
        }
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
        sampling.setMonitemId(DbHelpUtils.getMonItems("工业企业厂界噪声").getId());//为了检测方法
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
        StringBuilder samplingNo = new StringBuilder("");
        String dateStr = DateUtils.getDate().replace("-", "").substring(2);
        samplingNo.append(dateStr);
        samplingNo.append(UserInfoHelper.get().getUser().getIntId());
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
        StringBuilder samplingNo = new StringBuilder("");
        String dateStr = data.replace("-", "").substring(2);
        samplingNo.append(dateStr).append("-");
        samplingNo.append(UserInfoHelper.get().getUser().getIntId()).append("-");
//        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%"), SamplingDao.Properties.ProjectId.eq(projectId)).orderAsc(SamplingDao.Properties.SamplingNo).list();
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.SamplingNo.like(dateStr + "%")).
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
        StringBuilder samplingNo = new StringBuilder("");
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
     * 判断当前表单能否编辑
     *
     * @param sampling
     * @return
     */
    public static boolean sampIsCanEdit(Sampling sampling) {
        return (sampling.getStatus() == 0 ||
                sampling.getStatus() == 4 || sampling.getStatus() == 9)
                && sampling.getSamplingUserId().
                contains(UserInfoHelper.get().getUserInfo().getId()) ? true : false;
    }


    /**
     * 判断噪声表是否采样完成
     *
     * @param sampling
     * @return
     */
    public static boolean isNoiseFinsh(Sampling sampling) {
        if (CheckUtil.isEmpty(sampling.getSamplingTimeBegin())) {//检测日期必填项
            return false;
        }

        if (CheckUtil.isEmpty(sampling.getPrivateData()))//采样检测信息
            return false;
        NoisePrivateData privateData = new Gson().fromJson(sampling.getPrivateData(),
                NoisePrivateData.class);
        if (CheckUtil.isEmpty(privateData.getMianNioseAddr())) {
            return false;
        }

        return true;

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
