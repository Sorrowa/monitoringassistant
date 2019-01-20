package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.instrumental;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WasteWaterMoniteAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

public class WasteWaterMoniteActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.recyclerView_monite)
    RecyclerView recyclerViewMonite;

    private String projectId;
    private WasteWaterMoniteAdapter mWasteWaterMoniteAdapter;

    private List<MonItems> mMontes = new ArrayList<>();

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

        //获取水和废水采样单
        getSamplings(TaskDetailActivity.PATH_WASTEWATER);

        //初始化水和废水样品数据
        initSamplingDetailsData();
    }

    /**
     * 获取水和废水采样单的所有样品检测项目
     *
     * @param path
     */
    private void getSamplings(String path) {
        List<Sampling> samplings = DBHelper.get().getSamplingDao().queryBuilder().where(SamplingDao.Properties.ProjectId.eq(projectId), SamplingDao.Properties.FormPath.eq(path)).orderDesc(SamplingDao.Properties.SamplingNo).list();
        if (CheckUtil.isEmpty(samplings)) {
            return;
        }

        HashMap<String, HashMap<String, MonItems>> monItemsMap = new HashMap<String, HashMap<String, MonItems>>();

        HashMap<String, MonItems> monites = new HashMap<>();
        for (Sampling item : samplings) {
            //从数据库加载项目，避免项目名称显示错误
            HashMap<String, MonItems> monItemMap = null;
            if (!monItemsMap.containsKey(item.getParentTagId())) {
                monItemMap = new HashMap<String, MonItems>();
                monItemsMap.put(item.getParentTagId(), monItemMap);

                Tags tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(item.getParentTagId())).unique();
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
            List<SamplingDetail> samplingDetails = item.getSamplingDetailResults();

            //如果为空则尝试从数据库获取
            if (CheckUtil.isEmpty(samplingDetails)) {
                samplingDetails = DBHelper.get().getSamplingDetailDao().queryBuilder().where(SamplingDetailDao.Properties.SamplingId.eq(item.getId())).list();
            }

            for (SamplingDetail detail : samplingDetails) {
                //水和废水中，现场监测项目，存到AddressId和AddressName中的
                String[] moniteIds = detail.getAddresssId().split(",");
//                String[] moniteIds = detail.getMonitemId().split(",");
//                String[] monitemNames = detail.getMonitemName().split(",");//名称可能包含“,”

                for (int i = 0; i < moniteIds.length; i++) {
                    String id = moniteIds[i];

                    //过滤重复项
                    if (!monites.containsKey(id)) {
                        MonItems monItem = new MonItems(id, "", monItemMap.get(id).getName());
                        monItem.setAddressId(item.getAddressId());
                        monItem.setAddressName(item.getAddressName());
                        monites.put(id, monItem);
                        mMontes.add(monItem);
                    } else {
                        MonItems monItem = monites.get(id);
                        if (!monItem.getAddressId().contains(item.getAddressId())) {
                            monItem.setAddressId("," + item.getAddressId());
                            monItem.setAddressName("," + item.getAddressName());
                        }
                    }
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
                intent.putExtra("MonitemId", item.getId());
                intent.putExtra("MonitemName", item.getName());
                intent.putExtra("AddressId", item.getAddressId());
                intent.putExtra("AddressName", item.getAddressName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerViewMonite.setAdapter(mWasteWaterMoniteAdapter);
    }
}