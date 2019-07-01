package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.widget.dialogplus.DialogPlus;
import com.wonders.health.lib.base.widget.dialogplus.DialogPlusBuilder;
import com.wonders.health.lib.base.widget.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.FormSelectAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TagAdapter;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;


public class TagsUtils {
    private static int tagParent = 0;

    /**
     * 把tag数据更新到数据库  因为是基础数据  所以直接删除后插入
     *
     * @param tagsList
     */
    public static void insertDb(List<Tags> tagsList) {
        if (!RxDataTool.isEmpty(tagsList)) {
            DBHelper.get().getTagsDao().deleteAll();
            DBHelper.get().getTagsDao().insertInTx(tagsList);
        }
    }

    /**
     * 获取数据库中所有的tags表
     *
     * @return
     */
    public static List<Tags> getAllDbTags() {
//        return DbHelpUtils.getTags();
        return DBHelper.get().getTagsDao().loadAll();
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
     * 根据tagId获取tag的名称
     *
     * @param tagId tagId
     * @return tagId对应的名称用","隔开
     */
    public static String getTagsName(List<String> tagId) {
        List<Tags> tagsList = DbHelpUtils.getTags(tagId);
        StringBuffer stringBufferName = new StringBuffer();
        for (Tags tags : tagsList) {
            stringBufferName.append(tags.getName() + ",");
        }
        if (stringBufferName.lastIndexOf(",") > 0) {
            stringBufferName.deleteCharAt(stringBufferName.lastIndexOf(","));
        }
        return stringBufferName.toString();
    }


    /**
     * 根据itemId获取监测项目对应的name
     *
     * @param itemId
     * @param mSample
     * @return
     */
    public static String getMonItemNameById(String itemId, Sampling mSample) {
        Tags tags = DbHelpUtils.getTags(mSample.getParentTagId());
        List<MonItems> monItems = tags.getMMonItems();
        if (!RxDataTool.isEmpty(monItems)) {
            for (MonItems monItem : monItems) {
                if (!RxDataTool.isEmpty(monItem.getId())
                        && !RxDataTool.isEmpty(itemId)
                        && monItem.getId().equals(itemId)) {
                    return monItem.getName();
                }
            }
        }
        //return null;
        return "";
    }

    /**
     * 获取
     *
     * @return
     */
    public static List<Tags> getTagsLevel0() {
        return DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Level.eq(0)).list();
    }


    public static void showTagDialog(Context context, TagSelectAdapterOnItemClick onItemClick) {
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_tag, null);
        dialogPlusBuilder.setContentHolder(new ViewHolder(view));
        dialogPlusBuilder.setGravity(Gravity.CENTER);
        dialogPlusBuilder.setContentWidth(700);
        dialogPlusBuilder.setContentHeight(800);
        DialogPlus mDialogPlus = dialogPlusBuilder.create();
        initTagDialog(view, context, mDialogPlus, onItemClick);
        mDialogPlus.show();
    }

    /**
     * 初始化tag 选择dialog 提示框
     *
     * @param view
     * @param context
     * @param mDialogPlus
     */
    private static void initTagDialog(View view, Context context, DialogPlus mDialogPlus,
                                      TagSelectAdapterOnItemClick onItemClick) {
        tagParent = 0;
        CustomTab mCustomTab = view.findViewById(R.id.tabview);
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        List<Tab> tabList = new ArrayList<>();
        List<Form> formList = DbHelpUtils.getFormList();
        if (!RxDataTool.isEmpty(formList)) {
            for (Form form : formList) {
                Tab tab = new Tab();
                tab.setTabName(form.getTagName());
                tab.setSelected(false);
                tabList.add(tab);
            }
        }
        if (!RxDataTool.isEmpty(tabList)) {
            tabList.get(0).setSelected(true);
            mCustomTab.setTabs(tabList);
        }

        TagAdapter tagAdapter = new
                TagAdapter(updateTags(formList.get(0).getTagId()));
        mCustomTab.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                tagParent = position;
                tagAdapter.refreshInfos(updateTags(formList.get(position).getTagId()));
            }
        });

        ArtUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(context));
        tagAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (onItemClick != null) {
                    onItemClick.onItemClick(tagAdapter.getItem(position), formList.get(tagParent));
                }
                mDialogPlus.dismiss();
            }
        });
        mRecyclerView.setAdapter(tagAdapter);
    }

    public static List<Tags> updateTags(String tagParentId) {
        List<Tags> tagsList = DbHelpUtils.getTagListForParentId(tagParentId, 1);
        if (RxDataTool.isNull(tagsList))
            return new ArrayList<>();
        else return tagsList;
    }

    /**
     * 显示要素选择框
     */
    public static void showAddDialog(Context context, FormSelectAdapterOnItemClick onItemClick) {
        DialogPlusBuilder dialogPlusBuilder = DialogPlus.newDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_dialog_tag, null);
        dialogPlusBuilder.setContentHolder(new ViewHolder(view));
        dialogPlusBuilder.setGravity(Gravity.CENTER);
        dialogPlusBuilder.setContentWidth(700);
        dialogPlusBuilder.setContentHeight(800);
        DialogPlus mDialogPlus = dialogPlusBuilder.create();
        initDialogView(view, context, mDialogPlus, onItemClick);
        mDialogPlus.show();

    }

    private static void initDialogView(View view, Context context,
                                       DialogPlus dialogPlus, FormSelectAdapterOnItemClick onItemClick) {
        CustomTab mCustomTab = view.findViewById(R.id.tabview);
        RecyclerView mRecyclerView = view.findViewById(R.id.recyclerView);
        List<Tab> tabList = new ArrayList<>();
        List<Form> formList = DbHelpUtils.getFormList();
        if (!RxDataTool.isEmpty(formList)) {
            for (Form form : formList) {
                Tab tab = new Tab();
                tab.setTabName(form.getTagName());
                tab.setSelected(false);
                tabList.add(tab);
            }
        }
        if (!RxDataTool.isEmpty(tabList)) {
            tabList.get(0).setSelected(true);
            mCustomTab.setTabs(tabList);
        }
        FormSelectAdapter mFormSelectAdapter = new
                FormSelectAdapter(getFormSelectList(formList.get(0).getTagId()));
        mCustomTab.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                mFormSelectAdapter.refreshInfos(getFormSelectList(formList.get(position).getTagId()));
            }
        });

        ArtUtils.configRecyclerView(mRecyclerView, new LinearLayoutManager(context));
        mFormSelectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (onItemClick != null) {
                    onItemClick.onItemClick(mFormSelectAdapter.getItem(position));
                }
                dialogPlus.dismiss();
            }
        });
        mRecyclerView.setAdapter(mFormSelectAdapter);
    }

    /**
     * 根据tagParentId 获取 List<FormSelect>
     *
     * @param tagParentId 表单分类的tagId
     * @return
     */
    public static List<FormSelect> getFormSelectList(String tagParentId) {
        return DbHelpUtils.getDbFormSelectForTagParentId(tagParentId);
    }

    //表单分类
    public static interface FormSelectAdapterOnItemClick {
        void onItemClick(FormSelect formSelect);
    }

    //tag选择
    public static interface TagSelectAdapterOnItemClick {
        void onItemClick(Tags tags, Form form);
    }


}
