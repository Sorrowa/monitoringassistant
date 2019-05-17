package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FsExtends;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingFormStandDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingStantdDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;


public class HelpUtil {
    private static final String TAG = "HelpUtil";

    private HelpUtil() {
    }

    /**
     * to get the status bar height
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            statusHeight = 80;
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * get width
     *
     * @param context
     * @return
     */
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * get height
     *
     * @param context
     * @return
     */
    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 生成废水采样单code
     *
     * @param sampling
     * @return
     */
    public static String createSamplingCode(Sampling sampling) {
        String samplingNo;
        String snDate = DateUtils.getDate().replace("-", "").substring(2);
        String snSampling = "";
        if (!CheckUtil.isEmpty(sampling.getSamplingNo())) {
            snSampling = sampling.getSamplingNo().substring(sampling.getSamplingNo().length() - 2);
        }
        String snUserId = UserInfoHelper.get().getUser().getIntId() + "";

        int snIndex = HelpUtil.createOrderIndex(sampling);
        samplingNo = getYpxzCode(sampling.getTagName()) + snDate + "-" + snSampling + snUserId + "-" + StringUtil.autoGenericCode(snIndex, 2);
        return samplingNo;
    }

    /**
     * 创建orderIndex
     *
     * @param sampling
     * @return
     */
    public static int createOrderIndex(Sampling sampling) {
        int snIndex = 1;
        List<SamplingContent> samplingDetailResults = sampling.getSamplingContentResults();
        if (!CheckUtil.isEmpty(samplingDetailResults)) {
            SamplingContent lastContent = samplingDetailResults.get(samplingDetailResults.size() - 1);
            snIndex = lastContent.getOrderIndex() + 1;//OrderOndex:表示前面的序号
        }
        return snIndex;
    }

    /**
     * 创建频次(普通样)
     *
     * @param sampling
     * @return
     */
    public static int createFrequency(Sampling sampling) {
        int snFrequency = 1;
        List<SamplingContent> samplingDetailResults = sampling.getSamplingContentResults();
        if (samplingDetailResults != null && samplingDetailResults.size() > 0) {
            List<Integer> tempList = new ArrayList<>();
            for (SamplingContent detail : samplingDetailResults) {
                if (detail.getSamplingType() == 0) {
                    tempList.add(detail.getFrequecyNo());
                }
            }
            if (!CheckUtil.isEmpty(tempList)) {
                snFrequency = Collections.max(tempList) + 1;
            }
        }
        return snFrequency;
    }

    /**
     * 获取样品性质code
     *
     * @return
     */
    public static String getYpxzCode(String name) {
        if (CheckUtil.isEmpty(name)) {
            return "";
        }
        String nameStr = "";
        if (name.equals("地下水")) {
            nameStr = "DXS";
        } else if (name.equals("海水")) {
            nameStr = "HS";
        } else if (name.equals("废水")) {
            nameStr = "FS";
        } else if (name.equals("地表水")) {
            nameStr = "DBS";
        }
        return nameStr;
    }


    /**
     * 产生新的分瓶信息index
     *
     * @return
     */
    public static int generateNewSplitBottleIndex(Sampling mSample) {
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).orderAsc(SamplingFormStandDao.Properties.Index).list();
        if (!CheckUtil.isEmpty(formStantdsList)) {
            return formStantdsList.get(formStantdsList.size() - 1).getIndex() + 1;
        }
        return 1;
    }

    /**
     * 根据itemId获取对应的name
     *
     * @param itemId
     * @param mSample
     * @return
     */
    public static String getMonItemNameById(String itemId, Sampling mSample) {
        Tags tags = DBHelper.get().getTagsDao().queryBuilder().
                where(TagsDao.Properties.Id.eq(mSample.getParentTagId())).unique();
        List<MonItems> monItems = tags.getMMonItems();
        if (!CheckUtil.isEmpty(monItems)) {
            for (MonItems monItem : monItems) {
                if (!CheckUtil.isEmpty(monItem.getId()) && !CheckUtil.isEmpty(itemId) && monItem.getId().equals(itemId)) {
                    return monItem.getName();
                }
            }
        }
        //return null;
        return "";
    }

    /**
     * 通过projectId samplingId samplingCode samplingType查询对应的SamplingContent
     *
     * @param projectId
     * @param samplingId
     * @param samplingCode
     * @param samplingType
     * @return
     */
    public static List<SamplingContent> getSamplingContent(String projectId, String samplingId, String samplingCode, int samplingType) {
        List<SamplingContent> contentList = DBHelper.get().getSamplingContentDao().queryBuilder().where(SamplingContentDao.Properties.ProjectId.eq(projectId), SamplingContentDao.Properties.SamplingId.eq(samplingId), SamplingContentDao.Properties.SampingCode.eq(samplingCode), SamplingContentDao.Properties.SamplingType.eq(samplingType)).list();
        return contentList;
    }

    /**
     * 使用,连接数据
     *
     * @param strList
     * @return
     */
    public static String joinStringList(List<String> strList) {
        StringBuilder builderItems = new StringBuilder("");
        if (!CheckUtil.isEmpty(strList)) {
            for (String itemStr : strList) {
                builderItems.append(itemStr + ",");
            }
        }
        if (builderItems.lastIndexOf(",") > 0) {
            builderItems.deleteCharAt(builderItems.lastIndexOf(","));
        }
        return builderItems.toString();
    }

    /**
     * 判断采样单下面是否有分瓶信息
     *
     * @param samplingId
     * @return
     */
    public static boolean isSamplingHasBottle(String samplingId) {
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().
                where(SamplingFormStandDao.Properties.SamplingId.eq(samplingId)).list();
        if (!CheckUtil.isEmpty(formStantdsList)) {
            return true;
        }
        return false;
    }

    /**
     * monitemName 对应的 SamplingStantd
     *
     * @param monitemName
     * @param tagId
     * @return
     */
    public static SamplingStantd getSamplingStantdByMonItem(String monitemName, String tagId) {
        List<SamplingStantd> stantdsList = DBHelper.get().getSamplingStantdDao().queryBuilder().where(SamplingStantdDao.Properties.TagId.eq(tagId)).list();
        if (!CheckUtil.isEmpty(stantdsList)) {
            for (SamplingStantd samplingStantd : stantdsList) {
                List<String> monItemsList = samplingStantd.getMonItems();
                if (!CheckUtil.isEmpty(monitemName) && !CheckUtil.isEmpty(monItemsList) && monItemsList.contains(monitemName)) {
                    return samplingStantd;
                }
            }
        }
        return null;
    }

    /**
     * 此方法主要是分瓶监测项目更改了  对应的样品采集数量也应该更改
     *
     * @param sample
     * @return
     */
    public static List<SamplingContent> setSamplingCountList(Sampling sample) {
        List<SamplingContent> list = new ArrayList<>();
        if (sample == null && sample.getSamplingContentResults() == null) {
            return list;
        }
        if (sample.getSamplingFormStandResults() == null && sample.getSamplingFormStandResults().size() == 0)
            return list;
        for (SamplingContent content : sample.getSamplingContentResults()) {
            List<SamplingFormStand> stanTdList = new ArrayList<>();
            int i = 0;
            if (content.getMonitemId() != null && !content.getMonitemId().equals("")) {
                String[] monIds = content.getMonitemId().split(",");
                for (String id : monIds) {
                    List<SamplingFormStand> stanTds = DbHelpUtils.getSamplingStanTdList(sample.getId(), id);
                    if (!CheckUtil.isNull(stanTds)) {
                        if (!stanTdList.contains(stanTds)) {
                            stanTdList.addAll(stanTds);
                        }
                    } else {
                        Log.e(TAG, "countSamplingCount: " + "异常分瓶信息里面没有找到监测项目");
                        i++;
                    }
                }
            }

            i += stanTdList.size();
            content.setSamplingCount(i);
            list.add(content);
            generateSamplingDetails(content);
        }
        return list;
    }

    /**
     * 判断采样单下包含itemId的分瓶信息是否存在
     *
     * @param itemId
     * @param samplingId
     * @return
     */
    public static SamplingFormStand isBottleExists(String itemId, String samplingId) {
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(samplingId)).list();
        if (!CheckUtil.isEmpty(formStantdsList)) {
            for (SamplingFormStand formStand : formStantdsList) {
                if (formStand.getMonitemIds().contains(itemId)) {
                    return formStand;
                }
            }
        }
        return null;
    }

    /**
     * 获取采样单里面的所有样品的MonitIds取并集并去重
     *
     * @param samplingId
     * @return
     */
    public static List<String> getAllJcMonitIds(String samplingId) {
        List<String> ids = new ArrayList<>();
        List<SamplingContent> samplingContentList = DBHelper.get().getSamplingContentDao().queryBuilder().where(SamplingContentDao.Properties.SamplingId.eq(samplingId)).orderAsc(SamplingContentDao.Properties.OrderIndex).list();
        if (!CheckUtil.isEmpty(samplingContentList)) {
            for (SamplingContent samplingContent : samplingContentList) {
                String idsStr = samplingContent.getMonitemId();
                if (!CheckUtil.isEmpty(idsStr)) {
                    String[] idsArray = idsStr.split(",");
                    if (!CheckUtil.isEmpty(idsArray)) {
                        for (String idStr : idsArray) {
                            if (!ids.contains(idStr)) {
                                ids.add(idStr);
                            }
                        }
                    }
                }
            }
        }
        return ids;
    }

    /**
     * 删除包含itemId的分瓶信息
     * 19年4月26日 向昆杰更改  添加itemIdSize字段 itemIdSize=0的话 是当前样品采集的itemId 如果一共只出现了一次 就直接删除
     * 如果大于1次 证明其他样品采集也有 可不用删除
     *
     * @param itemId
     */
    public static void deleteBottleByItemId(String itemId, Sampling mSample) {
        if (itemId == null || itemId.equals("")) return;
        int itemIdSize = 0;//记录当前itemId出现的次数，如果只有一次就删除包含删除包含itemId的分瓶信息
        List<SamplingContent> samplingContents = mSample.getSamplingContentResults();
        if (!CheckUtil.isEmpty(samplingContents)) {
            for (SamplingContent content : samplingContents) {
                String monItemIds = content.getMonitemId();
                if (!CheckUtil.isEmpty(monItemIds) && !CheckUtil.isEmpty(itemId) && monItemIds.contains(itemId)) {
                    itemIdSize++;
                    if (itemIdSize > 1) break;
                }
            }
        }
        //只出现了一次
        if (itemIdSize <= 1) {
            SamplingFormStand samplingFormStand = HelpUtil.isBottleExists(itemId, mSample.getId());
            if (!CheckUtil.isNull(samplingFormStand)) {
                String[] ids = samplingFormStand.getMonitemIds().split(",");
                List<String> monItemIdList = new ArrayList<>();
                List<String> monItemNameList = new ArrayList<>();
                for (String id : ids) {
                    if (!id.equals(itemId)) {
                        monItemIdList.add(id);
                        String itemName = HelpUtil.getMonItemNameById(id, mSample);
                        if (CheckUtil.isEmpty(itemName)) {
                            monItemNameList.add(" ");
                        } else {
                            monItemNameList.add(itemName);
                        }
                    }
                }
                samplingFormStand.setMonitemIds(HelpUtil.joinStringList(monItemIdList));
                samplingFormStand.setMonitemName(HelpUtil.joinStringList(monItemNameList));
                if (monItemIdList == null || monItemIdList.size() == 0)
                    DBHelper.get().getSamplingFormStandDao().delete(samplingFormStand);
                else
                    DBHelper.get().getSamplingFormStandDao().update(samplingFormStand);
            }
        }
    }

    /**
     * 获取采样单下面是否和监测项目的itemId一样的标准的分瓶信息
     *
     * @param itemId
     * @param mSample
     * @return
     */
    public static SamplingFormStand getTheSameStandBottleByItemId(String itemId, Sampling mSample) {
        String itemName = HelpUtil.getMonItemNameById(itemId, mSample);
        SamplingStantd samplingStantd = HelpUtil.getSamplingStantdByMonItem(itemName, mSample.getTagId());
        //查询所有bottle
        List<SamplingFormStand> formStantdsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(mSample.getId())).list();
        if (!CheckUtil.isEmpty(formStantdsList)) {
            for (SamplingFormStand formStand : formStantdsList) {
                String idsStr = formStand.getMonitemIds();
                if (!CheckUtil.isEmpty(idsStr)) {
                    String[] idsArry = idsStr.split(",");
                    if (!CheckUtil.isEmpty(idsArry)) {
                        String firstId = idsArry[0];
                        String monItemName = HelpUtil.getMonItemNameById(firstId, mSample);
                        if (!CheckUtil.isEmpty(monItemName) && !CheckUtil.isNull(samplingStantd) && !CheckUtil.isEmpty(samplingStantd.getMonItems()) && samplingStantd.getMonItems().contains(monItemName)) {
                            return formStand;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 产生新的分瓶信息的index
     *
     * @return
     */
    public static int generateNewBottleIndex(String samplingId) {
        List<SamplingFormStand> formStandsList = DBHelper.get().getSamplingFormStandDao().queryBuilder().where(SamplingFormStandDao.Properties.SamplingId.eq(samplingId)).orderAsc(SamplingFormStandDao.Properties.Index).list();
        if (!CheckUtil.isEmpty(formStandsList)) {
            return formStandsList.get(formStandsList.size() - 1).getIndex() + 1;
        }
        return 1;
    }

    /**
     * 根据itemId创建或者更新分瓶信息
     *
     * @param itemId
     */
    public static void createAndUpdateBottle(String itemId, Sampling mSample) {
        //获取存在包含该itemId的分瓶信息
        SamplingFormStand samplingFormStand = isBottleExists(itemId, mSample.getId());

        //如果存在包含该itemId的分瓶信息则不用管
        if (CheckUtil.isNull(samplingFormStand)) {
            //获取与itemId同一个分瓶信息
            SamplingFormStand theSameStandBottle = getTheSameStandBottleByItemId(itemId, mSample);
            // 据itemId获取对应的name
            String itemName = getMonItemNameById(itemId, mSample);
            //如果存在标准的分瓶信息  则跟新否则新增
            if (CheckUtil.isNull(theSameStandBottle)) {
                SamplingFormStand bottleSplit = new SamplingFormStand();
                bottleSplit.setAnalysisSite("");
                bottleSplit.setSaveTimes("");
                bottleSplit.setMonitemIds(itemId);
                bottleSplit.setId(UUID.randomUUID().toString());
                bottleSplit.setMonitemName(itemName);
                List<String> items = new ArrayList<>();
                items.add(itemId);
                bottleSplit.setMonItems(items);
                bottleSplit.setStandNo(generateNewBottleIndex(mSample.getId()));
                bottleSplit.setIndex(generateNewBottleIndex(mSample.getId()));
                bottleSplit.setSamplingId(mSample.getId());
                bottleSplit.setUpdateTime(DateUtils.getWholeDate());
                bottleSplit.setCount(1);

                //获取包含itemId的标准，如果有则使用标准的信息新增，如果没有则用默认信息新增
                SamplingStantd samplingStantd = HelpUtil.getSamplingStantdByMonItem(itemName, mSample.getTagId());
                if (!CheckUtil.isNull(samplingStantd)) {
                    bottleSplit.setContainer(samplingStantd.getContaner());
                    bottleSplit.setSamplingAmount(samplingStantd.getCapacity());
                    bottleSplit.setSaveMehtod(samplingStantd.getSaveDescription());
                    // oldMonitemIds.add(bottleSplit);
                } else {
                    bottleSplit.setContainer("");
                    bottleSplit.setSamplingAmount("");
                    bottleSplit.setSaveMehtod("");
                    // oldMonitemIds.add(bottleSplit);
                }

                DBHelper.get().getSamplingFormStandDao().insertInTx(bottleSplit);
            } else {//存在则更新
                theSameStandBottle.setMonitemIds(theSameStandBottle.getMonitemIds() + "," + itemId);
                theSameStandBottle.setMonitemName(theSameStandBottle.getMonitemName() + "," + itemName);
                DBHelper.get().getSamplingFormStandDao().updateInTx(theSameStandBottle);
            }
        }

    }

    /**
     * 统计样品数量
     *
     * @return
     */
    public static int countSamplingCount(SamplingContent samplingContent, Sampling sample) {
        int count = 0;
        String monitemIdStr = samplingContent.getMonitemId();
        List<SamplingStantd> groupStandList = new ArrayList<>();
        if (!CheckUtil.isEmpty(monitemIdStr)) {
            String[] idsArray = monitemIdStr.split(",");
            if (!CheckUtil.isEmpty(idsArray)) {
                for (String itemId : idsArray) {
                    String itemName = HelpUtil.getMonItemNameById(itemId, sample);
                    SamplingStantd samplingStantd = HelpUtil.getSamplingStantdByMonItem(itemName, sample.getTagId());
                    if (!CheckUtil.isNull(samplingStantd)) {
                        if (!groupStandList.contains(samplingStantd)) {
                            groupStandList.add(samplingStantd);
                        }
                    } else {
                        count += 1;
                    }
                }

            }
        }
        count += groupStandList.size();
        return count;
    }


    /**
     * 统计样品数量
     * 安装样品
     *
     * @return
     */
    public static int setSamplingCount(SamplingContent samplingContent, Sampling sample) {
        List<SamplingFormStand> stanTdList = new ArrayList<>();
        int i = 0;
        if (samplingContent.getMonitemId() != null && !samplingContent.getMonitemId().equals("")) {
            String[] monIds = samplingContent.getMonitemId().split(",");
            for (String id : monIds) {
                List<SamplingFormStand> stanTds = DbHelpUtils.getSamplingStanTdList(sample.getId(), id);
                if (!CheckUtil.isNull(stanTds)) {
                    if (!stanTdList.contains(stanTds)) {
                        stanTdList.addAll(stanTds);
                    }
                } else {
                    Log.e(TAG, "countSamplingCount: " + "异常分瓶信息里面没有找到监测项目");
                    i++;
                }
            }
        }
        i += stanTdList.size();
        return i;
    }


    /**
     * 更改样品采集下面监测项目的样品数量
     *
     * @param content
     * @return
     */
    public static void generateSamplingDetails(SamplingContent content) {
        if (CheckUtil.isNull(content)) return;
        List<SamplingDetail> samplingDetailsList = DBHelper.get().getSamplingDetailDao().
                queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(content.getSamplingId()),
                SamplingDetailDao.Properties.ProjectId.eq(content.getProjectId()),
                SamplingDetailDao.Properties.SampingCode.eq(content.getSampingCode()),
                SamplingDetailDao.Properties.FrequecyNo.eq(content.getFrequecyNo()),
                SamplingDetailDao.Properties.SamplingType.eq(content.getSamplingType())).list();
        if (!CheckUtil.isEmpty(samplingDetailsList)) {
            for (SamplingDetail detail : samplingDetailsList) {
                detail.setSamplingCount(content.getSamplingCount());
            }
            DBHelper.get().getSamplingDetailDao().updateInTx(samplingDetailsList);
        }
    }
}
