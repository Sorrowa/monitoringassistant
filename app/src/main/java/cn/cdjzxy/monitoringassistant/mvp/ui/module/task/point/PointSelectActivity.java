package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.EnvirPointDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PointSelectAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.device.DeviceActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.CustomTab;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

public class PointSelectActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerView_point)
    RecyclerView recyclerViewPoint;
    @BindView(R.id.tabview)
    CustomTab    tabview;

    private List<EnvirPoint> mEnvirPoints = new ArrayList<>();
    private PointSelectAdapter mPointSelectAdapter;

    private String tagId;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("点位选择");
        titleBar.setRightTextDrawable(R.mipmap.ic_search_white);
        //        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                ArtUtils.makeText(getApplicationContext(), "搜索");
        //            }
        //        });

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
        initTabData();
        initPointData();

        tagId = getIntent().getStringExtra("tagId");

        getPointData(true);
    }


    /**
     * 初始化Tab数据
     */
    private void initTabData() {
        tabview.setTabs("方案点位", "其他点位");
        tabview.setOnTabSelectListener(new CustomTab.OnTabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int position) {
                getPointData(position == 0 ? true : false);
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initPointData() {
        ArtUtils.configRecyclerView(recyclerViewPoint, new LinearLayoutManager(this));
        mPointSelectAdapter = new PointSelectAdapter(mEnvirPoints);
        mPointSelectAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent();
                intent.putExtra("AddressId", mEnvirPoints.get(position).getId());
                intent.putExtra("Address", mEnvirPoints.get(position).getName());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        recyclerViewPoint.setAdapter(mPointSelectAdapter);
    }

    private void getPointData(boolean isRelationPoint) {
        List<EnvirPoint> envirPoints = null;
        if (isRelationPoint) {
            envirPoints = DBHelper
                    .get()
                    .getEnvirPointDao()
                    .queryBuilder()
                    .where(EnvirPointDao.Properties.TagId.eq(tagId))
                    .list();
        } else {
            envirPoints = DBHelper
                    .get()
                    .getEnvirPointDao()
                    .queryBuilder()
                    .where(EnvirPointDao.Properties.TagId.notEq(tagId))
                    .list();
        }

        mEnvirPoints.clear();
        if (!CheckUtil.isEmpty(envirPoints)) {
            mEnvirPoints.addAll(envirPoints);
        }

        mPointSelectAdapter.notifyDataSetChanged();
    }


}