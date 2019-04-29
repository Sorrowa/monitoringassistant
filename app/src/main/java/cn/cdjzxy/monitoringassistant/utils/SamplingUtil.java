package cn.cdjzxy.monitoringassistant.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;

public class SamplingUtil {

    /**
     * 获取当前年月日
     *
     * @return 当前年月日
     */
    public static String getNumberForSampling() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date()).toString();
    }

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
     * 创建采样单
     *
     * @return
     */
    public static Sampling createSample(String projectId, String formSelectId) {
        Project project = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();
        FormSelect formSelect = DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.FormId.eq(formSelectId)).unique();
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
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.SamplingNo.like(samplingNo.toString() + "%")).orderAsc(SamplingDao.Properties.SamplingNo).list();

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

}
