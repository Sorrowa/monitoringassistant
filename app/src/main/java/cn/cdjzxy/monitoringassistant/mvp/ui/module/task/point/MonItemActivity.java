package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

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

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("监测项目");
        //        titleBar.addCenterAction(titleBar.new ViewAction(getSearchView()));
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
        monItemId = getIntent().getStringExtra("monItemId");

        Tags tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(tagId)).unique();
        tags.getMMonItems();

        List<MonItemTagRelation> monItemTagRelations = DBHelper.get().getMonItemTagRelationDao().queryBuilder().where(MonItemTagRelationDao.Properties.TagId.eq(tagId)).list();

        List<MonItems> monItems = DBHelper.get().getMonItemsDao().queryBuilder().where(MonItemsDao.Properties.Id.eq(monItemId)).list();
        mMonItemsSelected.addAll(monItems);
        mMonItemSelectedAdapter.notifyDataSetChanged();

        //        List<MonItemTagRelation> monItemTagRelations = DBHelper.get().getMonItemTagRelationDao().queryBuilder().where(MonItemTagRelationDao.Properties.TagId.notEq(tagId)).list();
        //
        //        if (!CheckUtil.isEmpty(monItemTagRelations)) {
        //            mMonItems.clear();
        //            mMonItemsSelected.clear();
        //            for (MonItemTagRelation monItemTagRelation : monItemTagRelations) {
        //                MonItems monItems = monItemTagRelation.getMMonItems();
        //                if (!CheckUtil.isNull(monItems)) {
        //                    if (monItems.getId().equals(monItemId)) {
        //                        mMonItemsSelected.add(monItems);
        //                    } else {
        //                        mMonItems.add(monItems);
        //                    }
        //
        //                }
        //            }
        //            mMonItemAdapter.notifyDataSetChanged();
        //            mMonItemSelectedAdapter.notifyDataSetChanged();
        //        }

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

            }
        });
        rvProjectSelected.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProjectSelected.setAdapter(mMonItemSelectedAdapter);
    }

    @OnClick({R.id.iv_add_monitem, R.id.iv_delete_monitem})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_monitem:

                break;
            case R.id.iv_delete_monitem:

                break;
        }
    }
}
