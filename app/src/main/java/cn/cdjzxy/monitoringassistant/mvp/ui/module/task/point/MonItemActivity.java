package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.aries.ui.view.title.TitleBarView;
import com.github.promeg.pinyinhelper.Pinyin;
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
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskSearchActivity;
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
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.img_search)
    ImageView imgSearch;


    private MonItemAdapter mMonItemAdapter;

    private MonItemAdapter mMonItemSelectedAdapter;

    private String tagId;
    private String monItemId;
    private String selectItemsStr;

    private List<MonItems> mMonItems = new ArrayList<>();
    private List<MonItems> searchMonItem = new ArrayList<>();
    private List<MonItems> mMonItemsSelected = new ArrayList<>();
    //    private List<MonItems> mMonItemsDelete = new ArrayList<>();

    private StringBuilder MonItemName = new StringBuilder();
    private StringBuilder MonItemId = new StringBuilder();
    private String[] selectItems;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("监测项目");
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
        monItemId = getIntent().getStringExtra("monItemId");
        selectItemsStr = getIntent().getStringExtra("selectItems");
        if (!CheckUtil.isEmpty(selectItemsStr)) {
            selectItems = selectItemsStr.split(",");
        }

        initListData();
        //搜索
//        etSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                search(etSearch.getText().toString());
//            }
//        });
    }

    /**
     * 初始化list的原始数据
     */
    private void initListData() {
        //设置未选中的item
        Tags tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(tagId)).unique();
        List<MonItems> monItems = tags.getMMonItems();
        if (!CheckUtil.isEmpty(monItems)) {
            mMonItems.clear();
            for (MonItems monItem : monItems) {
                if (CheckUtil.isEmpty(selectItemsStr) || !selectItemsStr.contains(monItem.getId())) {
                    monItem.setSelected(false);
                    mMonItems.add(monItem);
                } else {
                    monItem.setSelected(true);
                    mMonItems.add(monItem);
                    //设置选中的items
                    mMonItemsSelected.add(monItem);
                }
            }
        }
        searchMonItem.clear();
        searchMonItem.addAll(mMonItems);
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
        mMonItemAdapter = new MonItemAdapter(searchMonItem);
        mMonItemAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                updateMonItem(position);
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
                updateMonItemS(position);
            }
        });
        rvProjectSelected.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProjectSelected.setAdapter(mMonItemSelectedAdapter);
    }

    @OnClick({R.id.iv_add_monitem, R.id.iv_delete_monitem, R.id.img_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_monitem:
                // addMonItems();
                break;
            case R.id.iv_delete_monitem:
                //deleteMonItems();
                break;
            case R.id.img_search:
                hideSoftInput();
                searchMonItems();
                break;
        }
    }

    /**
     * 搜索条目
     */
    private void searchMonItems() {
        if (!CheckUtil.isEmpty(etSearch.getText().toString())) {
            search(etSearch.getText().toString());
        }
    }

    /**
     * 搜索.
     *
     * @param key 搜索键
     */
    public void search(String key) {
        if (key == null || key.equals("")) {
            searchMonItem.clear();
            searchMonItem.addAll(mMonItems);
            mMonItemAdapter.refreshInfos(searchMonItem);
            mMonItemSelectedAdapter.refreshInfos(mMonItemsSelected);
            return;
        }
        List<MonItems> monItems = new ArrayList<>();
        List<MonItems> monItemsSelected = new ArrayList<>();
        char[] chars = key.toCharArray();
        for (MonItems item : searchMonItem) {
            // 已经包含这个字段了, 直接放入.
            if (item.getName().contains(key)) {
                monItems.add(item);
                continue;
            }
            String pinyin = Pinyin.toPinyin(item.getName(), "").toLowerCase();
            boolean isSame = true;
            int currentIndex = 0;
            for (char c : chars) {
                if ((currentIndex = pinyin.indexOf(c, currentIndex)) == -1) {
                    isSame = false;
                    break;
                }
            }
            if (isSame) {
                monItems.add(item);
            }
        }
        searchMonItem.clear();
        searchMonItem.addAll(monItems);
        mMonItemAdapter.refreshInfos(searchMonItem);
//        for (MonItems item : mMonItemsSelected) {
//            // 已经包含这个字段了, 直接放入.
//            if (item.getName().contains(key)) {
//                //monItemsSelected.add(item);
//                continue;
//            }
//            String pinyin = Pinyin.toPinyin(item.getName(), "").toLowerCase();
//            boolean isSame = true;
//            int currentIndex = 0;
//            for (char c : chars) {
//                if ((currentIndex = pinyin.indexOf(c, currentIndex)) == -1) {
//                    isSame = false;
//                    break;
//                }
//            }
//            if (isSame) {
//                monItemsSelected.add(item);
//            }
//        }
//        mMonItemsSelected.addAll(monItemsSelected);
//        mMonItemSelectedAdapter.refreshInfos(mMonItemsSelected);
    }

    /**
     * 这里的需求更改   改成点击添加   添加后   点击添加后的列表删除添加
     * 更新选中状态
     *
     * @param position
     */
    private void updateMonItem(int position) {
        MonItems item = searchMonItem.get(position);
        item.setSelected(true);
        if (mMonItemsSelected.contains(item)) {
            searchMonItem.set(position, item);
        } else {
            searchMonItem.get(position).setSelected(true);
            mMonItemsSelected.add(item);
//            if (mMonItems.get(position).isSelected()) {
//                mMonItems.get(position).setSelected(false);
//            } else {
//                mMonItems.get(position).setSelected(true);
//            }
        }
        mMonItemAdapter.notifyDataSetChanged();
        mMonItemSelectedAdapter.notifyDataSetChanged();
    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void updateMonItemS(int position) {
        MonItems item = mMonItemsSelected.get(position);
        item.setSelected(false);
        mMonItemsSelected.remove(item);
        for (int i = 0; i < mMonItems.size(); i++) {
            if (mMonItems.get(i).getId().equals(item.getId())) {
                mMonItems.set(i, item);
                break;
            }
        }
//        if (mMonItemsSelected.get(position).isSelected()) {
//            mMonItemsSelected.get(position).setSelected(false);
//        } else {
//            mMonItemsSelected.get(position).setSelected(true);
//        }
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
