package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;


/**
 * 数据库工具类
 * 2019年5月7日 嘉泽向昆杰   优化代码——进行中
 */
public class DbHelpUtils {
    public static void initDb(Context context) {
        DBHelper.init(context, UserInfoHelper.get().getUserName());//初始化创建数据库
    }


    public static Project getDbProject(String id) {
        return DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(id)).unique();
    }

    public static FormSelect getDbFormSelect(String id) {
        return DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.FormId.eq(id)).unique();
    }

    public static Sampling getDbSampling(String id) {
        return DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.Id.eq(id)).unique();
    }

    public static MonItems getMonItems(String name) {
        return DBHelper.get().getMonItemsDao().queryBuilder().
                where(MonItemsDao.Properties.Name.eq(name)).unique();
    }

    /**
     * 获取Project 数据库集合
     *
     * @return
     */
    public static List<Project> getProjectList() {
        return DBHelper.get().getProjectDao().loadAll();
    }

    /**
     * 按照Project 的{@ProjectDao.PlanEndTime}降序排序
     *
     * @return
     */
    public static List<Project> getProjectListDesc() {
        return DBHelper.get().getProjectDao().queryBuilder().orderDesc(ProjectDao.Properties.PlanEndTime).list();
    }

    /**
     * 获取Project的数量
     *
     * @return
     */
    public static long getProjectiSize() {
        return DBHelper.get().getProjectDao().count();
    }

    /**
     * 根据i采样d获取分瓶集合
     *
     * @param id
     * @return
     */
    public static List<SamplingFormStand> getSamplingFormStandListForSampId(String id) {
        List<SamplingFormStand> list = DBHelper.get().getSamplingFormStandDao().queryBuilder().
                where(SamplingFormStandDao.
                        Properties.SamplingId.eq(id)).list();
        if (list == null) return new ArrayList<>();
        else return list;
    }

    /**
     * ProjectContent集合
     *
     * @param id
     * @return
     */
    public static List<ProjectContent> getProjectContentList(String id) {
        List<ProjectContent> projectContentList = DBHelper.get().getProjectContentDao().
                queryBuilder().where(ProjectContentDao.Properties.ProjectId.eq(id)).list();
        if (projectContentList == null) {
            return new ArrayList<>();
        } else {
            return projectContentList;
        }
    }

    /**
     * 获取ProjectDetial 集合
     *
     * @param projectId
     * @param addressId
     * @param tagId
     * @return
     */
    public static List<ProjectDetial> getProjectDetialList(String projectId, String addressId, String tagId) {
        if (projectId == null || projectId.equals("")) return new ArrayList<>();
        List<ProjectDetial> projectDetialList = DBHelper.get().getProjectDetialDao().queryBuilder()
                .where(ProjectDetialDao.Properties.ProjectId.eq(projectId),
                        ProjectDetialDao.Properties.AddressId.eq(addressId),
                        ProjectDetialDao.Properties.TagId.eq(tagId)).list();
        if (projectDetialList == null) {
            return new ArrayList<>();
        } else {
            return projectDetialList;
        }


    }

    public static List<ProjectDetial> getProjectDetialList(String projectId, String addressId) {
        List<ProjectDetial> projectDetialList = DBHelper.get().getProjectDetialDao().queryBuilder()
                .where(ProjectDetialDao.Properties.ProjectId.eq(projectId),
                        ProjectDetialDao.Properties.AddressId.eq(addressId)
                ).list();
        if (projectDetialList == null) {
            return new ArrayList<>();
        } else {
            return projectDetialList;
        }
    }

    public static List<ProjectDetial> getProjectDetialList(String projectId) {
        List<ProjectDetial> projectDetialList = DBHelper.get().getProjectDetialDao().queryBuilder()
                .where(ProjectDetialDao.Properties.ProjectId.eq(projectId)
                ).list();
        if (projectDetialList == null) {
            return new ArrayList<>();
        } else {
            return projectDetialList;
        }
    }
}
