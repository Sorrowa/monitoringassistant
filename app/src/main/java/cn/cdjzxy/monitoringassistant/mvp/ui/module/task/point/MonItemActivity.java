package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemTagRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemTagRelationDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MonItemAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.GridItemDecoration;

/**
 * 监测项目
 */

public class MonItemActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.rv_project)
    RecyclerView rvProject;
    @BindView(R.id.rv_project_selected)
    RecyclerView rvProjectSelected;

    private MonItemAdapter mMonItemAdapter;

    private MonItemAdapter mMonItemSelectedAdapter;

    private String tagId;
    private String monItemId;

    private List<MonItems> mMonItems         = new ArrayList<>();
    private List<MonItems> mMonItemsSelected = new ArrayList<>();
    private List<MonItems> mMonItemsDelete   = new ArrayList<>();
    private StringBuilder  MonItemName       = new StringBuilder("");
    private StringBuilder  MonItemId         = new StringBuilder("");

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("监测项目");
        //        titleBar.addCenterAction(titleBar.new ViewAction(getSearchView()));
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMonItemsData();
                Intent intent = new Intent();
                intent.putExtra("MonItemName", MonItemName.toString());
                intent.putExtra("MonItemId", MonItemId.toString());
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

        tagId = getIntent().getStringExtra("tagId");

        Tags tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(tagId)).unique();
        List<MonItems> monItems = tags.getMMonItems();
        if (!CheckUtil.isEmpty(monItems)) {
            mMonItems.clear();
            mMonItems.addAll(monItems);
        }

        mMonItemAdapter.notifyDataSetChanged();

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
                upateMonItemState(position);
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
                upateMonItemSelectedState(position);
            }
        });
        rvProjectSelected.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProjectSelected.setAdapter(mMonItemSelectedAdapter);
    }

    @OnClick({R.id.iv_add_monitem, R.id.iv_delete_monitem})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_monitem:
                addMonItems();
                break;
            case R.id.iv_delete_monitem:
                deleteMonItems();
                break;
        }
    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void upateMonItemState(int position) {
        if (mMonItems.get(position).isSelected()) {
            mMonItems.get(position).setSelected(false);
        } else {
            mMonItems.get(position).setSelected(true);
        }
        mMonItemAdapter.notifyDataSetChanged();
    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void upateMonItemSelectedState(int position) {
        if (mMonItemsSelected.get(position).isSelected()) {
            mMonItemsSelected.get(position).setSelected(false);
        } else {
            mMonItemsSelected.get(position).setSelected(true);
        }
        mMonItemSelectedAdapter.notifyDataSetChanged();
    }


    private void addMonItems() {
        mMonItemsDelete.clear();
        for (MonItems monItem : mMonItems) {
            if (monItem.isSelected()) {
                monItem.setSelected(false);
                mMonItemsSelected.add(monItem);
                mMonItemsDelete.add(monItem);
            }
        }
        mMonItems.removeAll(mMonItemsDelete);
        mMonItemAdapter.notifyDataSetChanged();
        mMonItemSelectedAdapter.notifyDataSetChanged();
    }

    private void deleteMonItems() {
        mMonItemsDelete.clear();
        for (MonItems monItems : mMonItemsSelected) {
            if (monItems.isSelected()) {
                monItems.setSelected(false);
                mMonItems.add(monItems);
                mMonItemsDelete.add(monItems);
            }
        }
        mMonItemsSelected.removeAll(mMonItemsDelete);
        mMonItemAdapter.notifyDataSetChanged();
        mMonItemSelectedAdapter.notifyDataSetChanged();
    }


    private void getMonItemsData() {

        if (CheckUtil.isEmpty(mMonItemsSelected)) {
            return;
        }

        for (MonItems monItems : mMonItemsSelected) {
            MonItemName.append(monItems.getName() + ",");
            MonItemId.append(monItems.getId() + ",");
        }

        if (MonItemName.lastIndexOf(",") > 0) {
            MonItemName.deleteCharAt(MonItemName.lastIndexOf(","));
        }

        if (MonItemId.lastIndexOf(",") > 0) {
            MonItemId.deleteCharAt(MonItemId.lastIndexOf(","));
        }
    }


}
