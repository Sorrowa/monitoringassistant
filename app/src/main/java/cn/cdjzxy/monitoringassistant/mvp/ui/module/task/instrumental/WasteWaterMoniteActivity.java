package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetailYQFs;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingContentDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WasteWaterMoniteAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DbHelpUtils;

public class WasteWaterMoniteActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.recyclerView_monite)
    RecyclerView recyclerViewMonite;

    private String projectId, sampleId;
    private WasteWaterMoniteAdapter mWasteWaterMoniteAdapter;

    private List<MonItems> mMontes;
    private String inStrList;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("项目选择");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_waste_water_monite;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        projectId = getIntent().getStringExtra("projectId");
        sampleId = getIntent().getStringExtra("sampleId");

        mMontes = new ArrayList<>();
        inStrList = "";
        getInstruSamplings(TaskDetailActivity.PATH_INSTRUMENTAL);
        //获取水和废水采样单
        getSamplings(TaskDetailActivity.PATH_WASTEWATER);

        //初始化水和废水样品数据
        initSamplingDetailsData();
    }

    /**
     * 获取仪器法的数据
     *
     * @param path
     */
    private void getInstruSamplings(String path) {
        List<Sampling> samplingList = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.ProjectId.eq(projectId),
                        SamplingDao.Properties.FormPath.eq(path)).
                orderDesc(SamplingDao.Properties.SamplingNo).list();
        if (CheckUtil.isEmpty(samplingList))
            return;
        List<SamplingDetailYQFs> yqFsList = new ArrayList<>();
        for (Sampling sampling : samplingList) {
            yqFsList.addAll(DbHelpUtils.getSamplingDetailYQFsList(sampling.getId()));
        }
        if (CheckUtil.isEmpty(yqFsList)) return;
        StringBuilder builder = new StringBuilder();
        for (SamplingDetailYQFs yqFs : yqFsList) {
            builder.append(yqFs.getSampingCode()).append(",");
        }
        if (builder.lastIndexOf(",") > 0) {
            builder.deleteCharAt(builder.lastIndexOf(","));
        }
        inStrList = builder.toString();
    }

    /**
     * 获取水和废水采样单的所有样品检测项目
     *
     * @param path
     */
    private void getSamplings(String path) {
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().
                where(SamplingDao.Properties.ProjectId.eq(projectId),
                        SamplingDao.Properties.FormPath.eq(path)).
                orderDesc(SamplingDao.Properties.SamplingNo).list();
        if (CheckUtil.isEmpty(samplings)) {
            return;
        }

        HashMap<String, HashMap<String, MonItems>> monItemsMap = new HashMap<String, HashMap<String, MonItems>>();

        HashMap<String, MonItems> monites = new HashMap<>();
        for (Sampling item : samplings) {
            if (TextUtils.isEmpty(item.getParentTagId())) {
                continue;
            }

            //从数据库加载项目，避免项目名称显示错误
            HashMap<String, MonItems> monItemMap = null;
            if (!monItemsMap.containsKey(item.getParentTagId())) {
                monItemMap = new HashMap<String, MonItems>();
                monItemsMap.put(item.getParentTagId(), monItemMap);

                Tags tags = DBHelper.get().getTagsDao().queryBuilder().
                        where(TagsDao.Properties.Id.eq(item.getParentTagId())).unique();
                List<MonItems> monItems = tags.getMMonItems();
                if (!CheckUtil.isEmpty(monItems)) {
                    for (MonItems monItem : monItems) {
                        monItemMap.put(monItem.getId(), monItem);
                    }
                }
            } else {
                monItemMap = monItemsMap.get(item.getParentTagId());
            }

            //获取样品数据
            List<SamplingContent> contentList = item.getSamplingContentResults();

            //如果为空则尝试从数据库获取
            if (CheckUtil.isEmpty(contentList)) {
                contentList = DBHelper.get().getSamplingContentDao().queryBuilder().
                        where(SamplingContentDao.Properties.SamplingId.eq(item.getId())).list();
            }

            for (SamplingContent content : contentList) {
                //水和废水中，现场监测项目
                if (TextUtils.isEmpty(content.getSenceMonitemId())) {
                    continue;
                }
                //当前样品已在仪器法中进行处理
                if (!CheckUtil.isEmpty(inStrList)
                        && inStrList.contains(content.getSampingCode())
                        && !sampleId.equals(item.getId())) {
                    continue;
                }

                String[] moniteIds = content.getSenceMonitemId().split(",");

                for (int i = 0; i < moniteIds.length; i++) {
                    String id = moniteIds[i];
                    String name = monItemMap.get(id).getName();

                    MonItems monItem = null;

                    //过滤重复项
                    if (!monites.containsKey(id)) {
                        monItem = new MonItems(id, "", name);
                        monItem.setAddressId(item.getAddressId());//点位ID
                        monItem.setAddressName(item.getAddressName());//点位名称
                        monItem.setTagId(item.getTagId());//样品性质ID
                        monItem.setTagName(item.getTagName());//样品性质

                        monites.put(id, monItem);

                        mMontes.add(monItem);
                    } else {
                        monItem = monites.get(id);
                        //记录所有采样单点位
                        if (!monItem.getAddressId().contains(item.getAddressId())) {
                            monItem.setAddressId(monItem.getAddressId() + "," + item.getAddressId());
                            monItem.setAddressName(monItem.getAddressName() + "," + item.getAddressName());
                        }

                        //记录所有样品性质
                        if (!monItem.getTagId().contains(item.getTagId())) {
                            monItem.setTagId(monItem.getTagId() + "," + item.getTagId());
                            monItem.setTagName(monItem.getTagName() + "," + item.getTagName());
                        }
                    }

//                    //组装所有现场监测项目，去重
//                    String allMonitemId = monItem.getAllMonitemId();
//                    String allMonitemName = monItem.getAllMonitemName();
//                    if (TextUtils.isEmpty(allMonitemId)) {
//                        allMonitemId = "";
//                    }
//                    if (TextUtils.isEmpty(allMonitemName)) {
//                        allMonitemName = "";
//                    }
//                    for (String mid : moniteIds) {
//                        if (allMonitemId.contains(mid)) {
//                            continue;
//                        }
//
//                        if (allMonitemId.length() > 0) {
//                            allMonitemId += ",";
//                            allMonitemName += ",";
//                        }
//                        allMonitemId += mid;
//                        allMonitemName += monItemMap.get(mid).getName();
//                    }
//
//                    monItem.setAllMonitemId(allMonitemId);
//                    monItem.setAllMonitemName(allMonitemName);
                }
            }
        }
    }

    /**
     * 初始化Tab数据
     */
    private void initSamplingDetailsData() {
        ArtUtils.configRecyclerView(recyclerViewMonite, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {//设置RecyclerView不可滑动
                return true;
            }
        });

        mWasteWaterMoniteAdapter = new WasteWaterMoniteAdapter(mMontes);
        mWasteWaterMoniteAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                if (position < 0 || position >= mMontes.size()) {
                    return;
                }

                MonItems item = mMontes.get(position);
                if (item == null) {
                    return;
                }

                Intent intent = new Intent();

                //现场监测项目信息
                intent.putExtra("MonitemId", item.getId());
                intent.putExtra("MonitemName", item.getName());

                //所有点位集合
                intent.putExtra("AddressId", item.getAddressId());
                intent.putExtra("AddressName", item.getAddressName());

                //所有样品性质集合
                intent.putExtra("TagId", item.getTagId());
                intent.putExtra("TagName", item.getTagName());

//                //所有现场监测项目信息
//                intent.putExtra("AllMonitemId", item.getAllMonitemId());
//                intent.putExtra("AllMonitemName", item.getAllMonitemName());

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerViewMonite.setAdapter(mWasteWaterMoniteAdapter);
    }
}