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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.UnitAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

/**
 * 单位选择页面
 */
public class UnitActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerView_point)
    RecyclerView recyclerViewPoint;
    @BindView(R.id.tabview)
    CustomTab tabview;

    private List<Unit> mUnits = new ArrayList<>();
    private UnitAdapter mUnitAdapter;


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("单位选择");
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

        initUnitData();

        List<Unit> units = DBHelper.get().getUnitDao().loadAll();
        if (!CheckUtil.isEmpty(units) || units.size() != mUnits.size()) {
            mUnits.clear();
            mUnits.addAll(units);
        }

        mUnitAdapter.notifyDataSetChanged();
    }

    /**
     * 初始化数据
     */
    private void initUnitData() {
        ArtUtils.configRecyclerView(recyclerViewPoint, new LinearLayoutManager(this));

        mUnitAdapter = new UnitAdapter(mUnits);
        mUnitAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("UnitId", mUnits.get(position).getId());
                intent.putExtra("UnitName", mUnits.get(position).getName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        recyclerViewPoint.setAdapter(mUnitAdapter);
    }
}
