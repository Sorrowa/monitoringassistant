package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

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
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MethodAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TagAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.precipitation.PrecipitationActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

public class TypeActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerView_point)
    RecyclerView recyclerViewPoint;
    @BindView(R.id.tabview)
    CustomTab    tabview;

    private List<Tags> mTags = new ArrayList<>();
    private TagAdapter mTagAdapter;

    private String tagId;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("降水类型选择");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_point_select;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        tabview.setVisibility(View.GONE);
        initMethodData();
        tagId = getIntent().getStringExtra("tagId");
        List<Tags> tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.ParentId.eq(tagId)).list();
        if (!CheckUtil.isEmpty(tags)) {
            for (Tags tag : tags) {
                if (tag.getLevel() == 1) {
                    mTags.add(tag);
                }
            }
        }

        mTagAdapter.notifyDataSetChanged();

    }

    /**
     * 初始化数据
     */
    private void initMethodData() {
        ArtUtils.configRecyclerView(recyclerViewPoint, new LinearLayoutManager(this));
        mTagAdapter = new TagAdapter(mTags);
        mTagAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("TagId", mTags.get(position).getId());
                intent.putExtra("TagName", mTags.get(position).getName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerViewPoint.setAdapter(mTagAdapter);
    }


}
