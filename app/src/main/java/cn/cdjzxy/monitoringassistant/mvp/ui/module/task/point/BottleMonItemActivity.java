package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MonItemAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.HelpUtil;
import cn.cdjzxy.monitoringassistant.widgets.GridItemDecoration;

/**
 * 监测项目
 */

public class BottleMonItemActivity extends BaseTitileActivity<ApiPresenter> implements IView {


    @BindView(R.id.rv_project)
    RecyclerView rvProject;
    @BindView(R.id.rv_project_selected)
    RecyclerView rvProjectSelected;
    @BindView(R.id.linear_search)
    LinearLayout linearSearch;

    private MonItemAdapter mMonItemAdapter;

    private MonItemAdapter mMonItemSelectedAdapter;

    private String tagId;
    private String monItemId;
    private String samplingId;

    private List<MonItems> mMonItems = new ArrayList<>();
    private List<MonItems> mMonItemsSelected = new ArrayList<>();
    //    private List<MonItems> mMonItemsDelete = new ArrayList<>();
    private StringBuilder selectMonItemNameBuilder;//选中
    private StringBuilder selectMonItemIdBuilder;
    private StringBuilder monItemNameBuilder;
    private StringBuilder monItemIdBuilder;


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("监测项目");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMonItemsData();
                Intent intent = new Intent();
                intent.putExtra("selectMonItemName", selectMonItemNameBuilder.toString());
                intent.putExtra("selectMonItemId", selectMonItemIdBuilder.toString());
                intent.putExtra("MonItemName", monItemNameBuilder.toString());
                intent.putExtra("MonItemId", monItemIdBuilder.toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_project;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initMonItemsView();
        initMonItemsSelectedView();
        linearSearch.setVisibility(View.GONE);

        tagId = getIntent().getStringExtra("tagId");
        samplingId = getIntent().getStringExtra("samplingId");
        monItemId = getIntent().getStringExtra("monItemId");
        String selectItemsStr = getIntent().getStringExtra("selectItems");
//        if (!CheckUtil.isEmpty(selectItemsStr)) {
//          String[]  selectItems = selectItemsStr.split(",");
//        }
        List<String> monItemIdList = HelpUtil.getAllJcMonitIds(samplingId);

        //设置未选中的item
        Tags tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(tagId)).unique();
        List<MonItems> allMonItems = tags.getMMonItems();
        //List<MonItems> monItems = tags.getMMonItems();
        if (!CheckUtil.isEmpty(allMonItems)) {
            mMonItems.clear();
            for (MonItems monItems : allMonItems) {
                if (monItemIdList.contains(monItems.getId())) {
                    if (!CheckUtil.isEmpty(selectItemsStr) &&
                            !selectItemsStr.contains(monItems.getId())) {
                        monItems.setSelected(false);
                        mMonItems.add(monItems);
                    } else {
                        monItems.setSelected(true);
                        mMonItemsSelected.add(monItems);
                    }
                }
            }
        }
        mMonItemAdapter.notifyDataSetChanged();
        mMonItemSelectedAdapter.notifyDataSetChanged();

    }

    /**
     * 获取标题右边View
     *
     * @return
     */
    private View getSearchView() {
        View view = LayoutInflater.from(this).inflate(R.layout.view_search, null);
        return view;
    }


    private void initMonItemsView() {
        ArtUtils.configRecyclerView(rvProject, new GridLayoutManager(this, 4));
        mMonItemAdapter = new MonItemAdapter(mMonItems);
        mMonItemAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateMonItemState(position);
            }
        });
        rvProject.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProject.setAdapter(mMonItemAdapter);
    }


    private void initMonItemsSelectedView() {
        ArtUtils.configRecyclerView(rvProjectSelected, new GridLayoutManager(this, 4));
        mMonItemSelectedAdapter = new MonItemAdapter(mMonItemsSelected);
        mMonItemSelectedAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateMonItemSelectedState(position);
            }
        });
        rvProjectSelected.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProjectSelected.setAdapter(mMonItemSelectedAdapter);
    }

    @OnClick({R.id.iv_add_monitem, R.id.iv_delete_monitem})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_monitem:
                // addMonItems();
                break;
            case R.id.iv_delete_monitem:
                //  deleteMonItems();
                break;
        }
    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void updateMonItemState(int position) {
        MonItems monItems = mMonItems.get(position);
        monItems.setSelected(true);
        mMonItemsSelected.add(monItems);
        mMonItems.remove(position);
        mMonItemAdapter.notifyDataSetChanged();
        mMonItemSelectedAdapter.notifyDataSetChanged();
    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void updateMonItemSelectedState(int position) {
        if (mMonItemsSelected.size() == 1) {
            showMessage("至少保留一个监测项目");
            return;
        }

        MonItems monItems = mMonItemsSelected.get(position);
        monItems.setSelected(false);
        mMonItems.add(monItems);
        mMonItemsSelected.remove(position);
        mMonItemAdapter.notifyDataSetChanged();
        mMonItemSelectedAdapter.notifyDataSetChanged();
    }


//    private void addMonItems() {
//        mMonItemsDelete.clear();
//        for (MonItems monItem : mMonItems) {
//            if (monItem.isSelected()) {
//                monItem.setSelected(false);
//                mMonItemsSelected.add(monItem);
//                mMonItemsDelete.add(monItem);
//            }
//        }
//        mMonItems.removeAll(mMonItemsDelete);
//        mMonItemAdapter.notifyDataSetChanged();
//        mMonItemSelectedAdapter.notifyDataSetChanged();
//    }
//
//    private void deleteMonItems() {
//        mMonItemsDelete.clear();
//        for (MonItems monItems : mMonItemsSelected) {
//            if (monItems.isSelected()) {
//                monItems.setSelected(false);
//                mMonItems.add(monItems);
//                mMonItemsDelete.add(monItems);
//            }
//        }
//        mMonItemsSelected.removeAll(mMonItemsDelete);
//        mMonItemAdapter.notifyDataSetChanged();
//        mMonItemSelectedAdapter.notifyDataSetChanged();
//    }


    private void getMonItemsData() {
        selectMonItemNameBuilder = new StringBuilder("");
        selectMonItemIdBuilder = new StringBuilder("");
        monItemNameBuilder = new StringBuilder("");
        monItemIdBuilder = new StringBuilder("");
        if (CheckUtil.isEmpty(mMonItemsSelected)) {
            return;
        }

        for (MonItems monItems : mMonItemsSelected) {
            selectMonItemNameBuilder.append(monItems.getName() + ",");
            selectMonItemIdBuilder.append(monItems.getId() + ",");
        }

        if (selectMonItemNameBuilder.lastIndexOf(",") > 0) {
            selectMonItemNameBuilder.deleteCharAt(selectMonItemNameBuilder.lastIndexOf(","));
        }

        if (selectMonItemIdBuilder.lastIndexOf(",") > 0) {
            selectMonItemIdBuilder.deleteCharAt(selectMonItemIdBuilder.lastIndexOf(","));
        }

        for (MonItems items : mMonItems) {
            monItemNameBuilder.append(items.getName() + ",");
            monItemIdBuilder.append(items.getId() + ",");
        }
        if (monItemNameBuilder.lastIndexOf(",") > 0) {
            monItemNameBuilder.deleteCharAt(monItemNameBuilder.lastIndexOf(","));
        }

        if (monItemIdBuilder.lastIndexOf(",") > 0) {
            monItemIdBuilder.deleteCharAt(monItemIdBuilder.lastIndexOf(","));
        }
    }


    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
