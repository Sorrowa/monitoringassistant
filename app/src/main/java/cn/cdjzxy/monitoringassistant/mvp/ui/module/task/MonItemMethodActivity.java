package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

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
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MethodsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemMethodRelationDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MethodAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

public class MonItemMethodActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerView_point)
    RecyclerView recyclerViewPoint;
    @BindView(R.id.tabview)
    CustomTab tabview;

    private List<Methods> mMethods = new ArrayList<>();
    private MethodAdapter mMethodAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("方法");
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

        //获取监测项目ID
        String monItemId = getIntent().getStringExtra("MonItemId");

        if (!TextUtils.isEmpty((monItemId))) {
            //获取监测项目和监测方法的关系列表
            List<MonItemMethodRelation> methodRelaList = DBHelper.get().getMonItemMethodRelationDao().queryBuilder().where(MonItemMethodRelationDao.Properties.MonItemId.eq(monItemId)).list();

            //获取方法ID列表
            List<String> methodIdList = new ArrayList<>();
            for (MonItemMethodRelation rela : methodRelaList) {
                methodIdList.add(rela.getMethodId());
            }

            //查询方法
            List<Methods> tempList = DBHelper.get().getMethodsDao().queryBuilder().where(MethodsDao.Properties.Id.in(methodIdList)).list();
            if (!CheckUtil.isEmpty(tempList)) {
                mMethods.addAll(tempList);
            }
        }

        //初始化方法列表
        initMethodData();
    }

    /**
     * 初始化数据
     */
    private void initMethodData() {
        ArtUtils.configRecyclerView(recyclerViewPoint, new LinearLayoutManager(this));
        mMethodAdapter = new MethodAdapter(mMethods);
        mMethodAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("MethodId", mMethods.get(position).getId());
                intent.putExtra("MethodName", mMethods.get(position).getName() + "(" + mMethods.get(position).getCode() + ")");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerViewPoint.setAdapter(mMethodAdapter);
    }
}
