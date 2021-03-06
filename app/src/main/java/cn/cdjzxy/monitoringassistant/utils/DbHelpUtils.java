package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetailYQFs;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnterRelatePointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MethodsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemMethodRelationDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailYQFsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;

import static android.content.ContentValues.TAG;


/**
 * 数据库工具类
 * 2019年5月7日 嘉泽向昆杰   优化代码——进行中
 */
public class DbHelpUtils {
    public static void initDb(Context context) {
        DBHelper.init(context, UserInfoHelper.get().getUserName());//初始化创建数据库
    }


    public static Project getDbProject(String id) {
        if (CheckUtil.isEmpty(id)) return new Project();
        return DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(id)).unique();
    }

    /**
     * 通过主键id 查找ProjectContent
     *
     * @param id 主键id
     * @return ProjectContent
     */
    public static ProjectContent getProjectContent(String id) {
        if (RxDataTool.isEmpty(id)) return null;
        return DBHelper.get().getProjectContentDao().
                queryBuilder().where(ProjectContentDao.Properties.Id.eq(id)).unique();

    }

    public static FormSelect getDbFormSelect(String id) {
        if (CheckUtil.isEmpty(id)) return new FormSelect();
        return DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.FormId.eq(id)).unique();
    }

    public static Sampling getDbSampling(String id) {
        if (CheckUtil.isEmpty(id)) return new Sampling();
        return DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.Id.eq(id)).unique();
    }

    public static MonItems getMonItems(String name) {
        if (CheckUtil.isEmpty(name)) return new MonItems();
        return DBHelper.get().getMonItemsDao().queryBuilder().
                where(MonItemsDao.Properties.Name.eq(name)).unique();
    }

    /**
     * 分瓶表
     *
     * @param id
     * @return
     */
    public static SamplingFormStand getSamplingFormStandDaoForId(String id) {
        if (id == null || id.equals("")) return null;
        return DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.
                Properties.Id.eq(id)).
                unique();
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
    public static long getProJectSize() {
        return DBHelper.get().getProjectDao().count();
    }

    /**
     * 根据i采样d获取分瓶集合
     *
     * @param id
     * @return
     */
    public static List<SamplingFormStand> getSamplingFormStandListForSampId(String id) {
        if (CheckUtil.isEmpty(id)) return new ArrayList<>();
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

    /**
     * 根据采样id和监测项目id 查找分瓶
     *
     * @param samplingId
     * @param itemId
     * @return
     */
    public static List<SamplingFormStand> getSamplingStanTdList(String samplingId, String itemId) {
        List<SamplingFormStand>
                stanTdList = DBHelper.get().getSamplingFormStandDao().queryBuilder()
                .where(SamplingFormStandDao.Properties.SamplingId.eq(samplingId),
                        SamplingFormStandDao.Properties.MonitemIds.like(itemId + "%")).list();
        if (stanTdList == null) return new ArrayList<>();
        else return stanTdList;
    }


    /**
     * 获取样品采集列表
     *
     * @param samplingId
     * @param projectId
     * @return
     */
    public static List<SamplingContent> getSamplingContentList(String samplingId, String projectId) {
        return DBHelper.get().getSamplingContentDao().queryBuilder().
                where(SamplingContentDao.Properties.SamplingId.eq(samplingId),
                        SamplingContentDao.Properties.ProjectId.eq(projectId)).list();
    }

    /**
     * 获取样品采集列表
     *
     * @param samplingId
     * @return
     */
    public static List<SamplingContent> getSamplingContentList(String samplingId) {
        return DBHelper.get().getSamplingContentDao().queryBuilder().
                where(SamplingContentDao.Properties.SamplingId.eq(samplingId)).list();
    }

    public static List<SamplingDetail> getSamplingDetaiList(String id) {
        if (CheckUtil.isEmpty(id)) return new ArrayList<>();
        return DBHelper.get().getSamplingDetailDao().queryBuilder().
                where(SamplingDetailDao.Properties.SamplingId.eq(id)).list();
    }

    /**
     * 获取数据库中非ids列表中的数据
     *
     * @param ids id集合
     * @return List<Project>
     */
    public static List<Project> getProjectListNoInIds(List<String> ids) {
        return DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.notIn(ids)).list();
    }

    /**
     * 通过{@SamplingDao.Properties.projectId}查找Sampling表
     * Sampling 采样表
     *
     * @param projectId 任务id
     * @return Sampling 采样表
     */
    public static List<Sampling> getDbSampleForProjectId(String projectId) {
        if (CheckUtil.isEmpty(projectId)) {
            Log.e(TAG, "getDbSampling:数据库查找采样单id为空");
            return new ArrayList<>();
        }
        return DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.ProjectId.eq(projectId)).list();
    }

    /**
     * 通过{@SamplingContentDao{SamplingId }}查找SamplingContent表
     * SamplingContent 采样单数据表
     *
     * @param projectId 任务id
     * @return SamplingContent 采样单数据表
     */
    public static List<SamplingContent> getSampleContentListForProjectId(String projectId) {
        List<SamplingContent> contentList = DBHelper.get().
                getSamplingContentDao().queryBuilder().
                where(SamplingContentDao.Properties.ProjectId.eq(projectId)).list();
        return contentList;
    }

    /**
     * 通过{@SamplingContentDao{@ProjectId,SamplingId }}查找数据库所有的SamplingDetailDao表
     * SamplingDetail 采样单数据表
     *
     * @param projectId 任务id
     * @return SamplingDetail 采样单数据表
     */
    public static List<SamplingDetail> getSampleDetailListForProjectId(String projectId) {
        if (CheckUtil.isEmpty(projectId)) return new ArrayList<>();
        return DBHelper.get().getSamplingDetailDao().queryBuilder().
                where(SamplingDetailDao.Properties.ProjectId.eq(projectId)).list();
    }

    /**
     * 按照{@SamplingFormStandDao.Properties.SamplingId}查找数据库所有的SamplingFormStandDao表
     * SamplingFormStand 分瓶信息表
     *
     * @param SamplingId 主键id
     * @return List<SamplingFormStand>：查询所有的分瓶信息表
     */
    public static List<SamplingFormStand> getSamplingFormStandList(String SamplingId) {
        if (CheckUtil.isEmpty(SamplingId)) return new ArrayList<>();
        List<SamplingFormStand> list = DBHelper.get().getSamplingFormStandDao().queryBuilder().
                where(SamplingFormStandDao.
                        Properties.SamplingId.eq(SamplingId)).list();
        if (list == null) return new ArrayList<>();
        else return list;
    }

    /**
     * 查找数据库中非projectIds的数据并删除
     *
     * @param projectIds 任务的id集合
     */
    public static void deleteOldDataForProject(List<String> projectIds) {
        List<Project> dbOldProjectList = getProjectListNoInIds(projectIds);
        if (CheckUtil.isEmpty(dbOldProjectList)) return;
        for (Project project : dbOldProjectList) {
            String projectId = project.getId();
            List<ProjectContent> contentList = getProjectContentList(projectId);
            List<ProjectDetial> detailList = getProjectDetialList(projectId);
            List<Sampling> samplingList = getDbSampleForProjectId(projectId);
            List<SamplingContent> samplingContentList = getSampleContentListForProjectId(projectId);
            List<SamplingDetail> samplingDetailList = getSampleDetailListForProjectId(projectId);
            if (!CheckUtil.isEmpty(contentList))
                DBHelper.get().getProjectContentDao().deleteInTx(contentList);
            if (!CheckUtil.isEmpty(detailList))
                DBHelper.get().getProjectDetialDao().deleteInTx(detailList);
            if (!CheckUtil.isEmpty(samplingList)) {
                for (Sampling sampling : samplingList) {
                    List<SamplingFormStand> stantdList = getSamplingFormStandList(sampling.getId());
                    if (!CheckUtil.isEmpty(stantdList)) {
                        DBHelper.get().getSamplingFormStandDao().deleteInTx(stantdList);
                    }
                }
            }
            if (!CheckUtil.isEmpty(samplingContentList))
                DBHelper.get().getSamplingContentDao().deleteInTx(samplingContentList);
            if (!CheckUtil.isEmpty(samplingDetailList))
                DBHelper.get().getSamplingDetailDao().deleteInTx(samplingDetailList);
        }
    }

    /**
     * 获取环境点位
     *
     * @param idList 点位ID
     * @return 环境质量点位信息
     */
    public static List<EnterRelatePoint> getEnterRelatePointList(List<String> idList) {
        return DBHelper.get().getEnterRelatePointDao().queryBuilder().where(
                EnterRelatePointDao.Properties.Id.in(idList))
                .list();
    }

    /**
     * 根据采样单id获取SamplingDetailYQFs数据
     *
     * @param sampleId 采样单id
     * @return List<SamplingDetailYQFs>
     */
    public static List<SamplingDetailYQFs> getSamplingDetailYQFsList(String sampleId) {
        if (RxDataTool.isNull(sampleId)) return new ArrayList<>();
        else return DBHelper.get().getSamplingDetailYQFsDao().queryBuilder().
                where(SamplingDetailYQFsDao.Properties.SamplingId.eq(sampleId)).list();
    }

    /**
     * 根据采样单id获取SamplingDetailYQFs数据
     *
     * @param id 采样单id
     * @return List<SamplingDetailYQFs>
     */
    public static SamplingDetailYQFs getSamplingDetailYQFs(String id) {
        if (RxDataTool.isNull(id)) return null;
        else return DBHelper.get().getSamplingDetailYQFsDao().queryBuilder().
                where(SamplingDetailYQFsDao.Properties.Id.eq(id)).unique();
    }


    /**
     * 根据所有项目获方法
     *
     * @return 监测方法list
     */
    public static List<MonItemMethodRelation> getMonItemMethodRelationAll() {
        return DBHelper.get().getMonItemMethodRelationDao().queryBuilder().list();
    }

    /**
     * 根据id查找MonItems
     *
     * @param id 主键id
     * @return MonItems
     */
    public static MonItems getMonItemForId(String id) {
        if (RxDataTool.isEmpty(id)) return null;
        return DBHelper.get().getMonItemsDao().queryBuilder().where(MonItemsDao.Properties
                .Id.eq(id)).unique();
    }


    /**
     * 获取环境点位
     *
     * @param id 点位ID
     * @return 环境质量点位信息
     */
    public static EnvirPoint getEnvirPoint(String id) {
        if (RxDataTool.isEmpty(id)) return null;
        return DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.Properties.
                Id.eq(id)).unique();
    }


    /**
     * 获取环境点位
     *
     * @param tagIds tagId
     * @return 环境质量点位信息
     */
    public static List<EnvirPoint> getEnvirPointListForTagId(List<String> tagIds) {
        return DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.
                Properties.TagId.in(tagIds)).list();
    }
    public static List<EnvirPoint> getEnvirPointListForTagId(String tagId) {
        return DBHelper.get().getEnvirPointDao().queryBuilder().where(EnvirPointDao.
                Properties.TagId.in(tagId)).list();
    }
    /**
     * 获取污染源点位
     *
     * @param mRcvId 监测单位ID
     * @param ids    点位集合
     * @return List<EnterRelatePoint>
     */
    public static List<EnterRelatePoint> getEnterRelatePointList(String mRcvId, List<String> ids) {
        return DBHelper
                .get()
                .getEnterRelatePointDao()
                .queryBuilder()
                .where(EnterRelatePointDao.Properties.EnterPriseId.eq(mRcvId), EnterRelatePointDao.
                        Properties.Id.in(ids))
                //.where(EnterRelatePointDao.Properties.Id.in(pointIds))
                .list();
    }

    /**
     * 获取污染源点位
     *
     * @param id 主键ID
     * @return List<EnterRelatePoint>
     */
    public static EnterRelatePoint getEnterRelatePoint(String id) {
        return DBHelper
                .get()
                .getEnterRelatePointDao()
                .queryBuilder()
                .where(EnterRelatePointDao.Properties.Id.eq(id))
                //.where(EnterRelatePointDao.Properties.Id.in(pointIds))
                .unique();
    }
    /**
     * 通过项目id  查找项目监测方法
     *
     * @param monItemId
     * @return
     */
    public static List<MonItemMethodRelation> getMonItemMethodRelationListForMonItemId(String monItemId) {
        return DBHelper.get().getMonItemMethodRelationDao().queryBuilder().where(
                MonItemMethodRelationDao.Properties.MonItemId.eq(monItemId)
        ).list();
    }
    /**
     * 根据方法id获取方法详情
     *
     * @param methodId 方法id集合
     * @return 方法详情集合
     */
    public static Methods getMethod(String methodId) {
        if (RxDataTool.isEmpty(methodId)) return new Methods();
        return DBHelper.get().getMethodsDao().
                queryBuilder().where(MethodsDao.Properties.Id.
                eq(methodId)).unique();
    }

    /**
     * 查找 ProjectDetial 数据
     *
     * @param projectId        任务id
     * @param projectContentId projectContentId
     * @return List<ProjectDetial>
     */
    public static List<ProjectDetial> getProjectDetailList(String projectId, String projectContentId) {
        if (projectId == null || projectId.equals("")) return new ArrayList<>();
        return DBHelper.get().getProjectDetialDao().queryBuilder()
                .where(ProjectDetialDao.Properties.ProjectId.eq(projectId),
                        ProjectDetialDao.Properties.ProjectContentId.eq(projectContentId)).list();

    }

    /**
     * 获取环境点位
     *
     * @param tagId 点位ID
     * @return 环境质量点位信息
     */
    public static List<EnvirPoint> getEnvirPointList(String tagId, List<String> pointIds) {
        return DBHelper
                .get()
                .getEnvirPointDao()
                .queryBuilder()
                .where(EnvirPointDao.Properties.TagId.eq(tagId), EnvirPointDao.Properties.Id.in(pointIds))
                //.where(EnvirPointDao.Properties.Id.in(pointIds))
                .list();
    }
    /**
     * 获取环境点位
     *
     * @param idList 点位ID
     * @return 环境质量点位信息
     */
    public static List<EnvirPoint> getEnvirPointList(List<String> idList) {
        return DBHelper.get().getEnvirPointDao().queryBuilder().where(
                EnvirPointDao.Properties.Id.in(idList))
                .list();
    }
    /**
     * 通过{TagsDao.Properties.Id}查找数据库所有的TagsDao表
     * TagsDao 要素分类表
     *
     * @param tagId 要素分类表
     * @return Tags 要素
     */
    public static Tags getTags(String tagId) {
        return DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(tagId)).unique();
    }
    /**
     * 通过{TagsDao.Properties.Id}查找数据库所有的TagsDao表
     * TagsDao 要素分类表
     *
     * @param stringList 要素分类表
     * @return Tags 要素
     */
    public static List<Tags> getTags(List<String> stringList) {
        return DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.in(stringList)).list();
    }

    /**
     * 查找数据库表单分类
     *
     * @return 表单分类集合
     */
    public static List<Form> getFormList() {
        return DBHelper.get().getFormDao().loadAll();
    }


    public static List<Tags> getTagListForParentId(String parentId, int level) {
        return DBHelper.get().getTagsDao().queryBuilder().
                where(TagsDao.Properties.ParentId.eq(parentId), TagsDao.Properties.Level.eq(level)).list();
    }
    /**
     * 通过{@FormSelectDao.Properties.tagParentId}查找FormSelect表
     * FormSelect 表单分类表（采样单分类）
     *
     * @param tagParentId tagParentId
     * @return List<FormSelect>
     */
    public static List<FormSelect> getDbFormSelectForTagParentId(String tagParentId) {
        if (RxDataTool.isEmpty(tagParentId)) return new ArrayList<>();
        return DBHelper.get().getFormSelectDao().queryBuilder().where(FormSelectDao.Properties.
                TagParentId.eq(tagParentId)).list();
    }

    /**
     * 获取环境点位
     *
     * @param tagId 点位ID
     * @return 环境质量点位信息
     */
    public static List<EnvirPoint> getEnvirPointListNotInIds(String tagId, List<String> ids) {
        return DBHelper
                .get()
                .getEnvirPointDao()
                .queryBuilder()
                .where(EnvirPointDao.Properties.TagId.eq(tagId), EnvirPointDao.Properties.Id.notIn(ids))
                //.where(EnvirPointDao.Properties.Id.in(pointIds))
                .list();
    }

    /**
     * 获取污染源点位
     *
     * @param mRcvId 监测单位ID
     * @param ids    点位集合
     * @return List<EnterRelatePoint>
     */
    public static List<EnterRelatePoint> getEnterRelatePointListNotIds(String mRcvId, List<String> ids) {
        return DBHelper
                .get()
                .getEnterRelatePointDao()
                .queryBuilder()
                .where(EnterRelatePointDao.Properties.EnterPriseId.eq(mRcvId), EnterRelatePointDao.
                        Properties.Id.notIn(ids))
                //.where(EnterRelatePointDao.Properties.Id.in(pointIds))
                .list();
    }

}
