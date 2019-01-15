package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.aries.ui.view.title.TitleBarView;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRouteGuideManager;
import com.wonders.health.lib.base.utils.ArtUtils;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

public class NavigationActivity extends BaseTitileActivity<ApiPresenter> {
    @BindView(R.id.map_content_view)
    LinearLayout map_content_view;

    private BNRoutePlanNode mBNRoutePlanNode = null;

    private IBNRouteGuideManager mRouteGuideManager;

    private Handler hd = null;

    private static final int MSG_RESET_NODE = 3;


    @Override
    public void setTitleBar(TitleBarView titleBar) {
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            titleBar.setTitleMainText(title);
            return;
        }
        titleBar.setTitleMainText("点位导航");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_navigation;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        createHandler();
        mRouteGuideManager = BaiduNaviManagerFactory.getRouteGuideManager();
        View view = mRouteGuideManager.onCreate(this, mOnNavigationListener);

        if (view != null) {
            map_content_view.addView(view);
            //setContentView(view);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mBNRoutePlanNode = (BNRoutePlanNode)
                        bundle.getSerializable("endPoint");
            }
        }
        routeGuideEvent();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createHandler();
        mRouteGuideManager = BaiduNaviManagerFactory.getRouteGuideManager();
        View view = mRouteGuideManager.onCreate(this, mOnNavigationListener);

        if (view != null) {
            setContentView(view);
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                mBNRoutePlanNode = (BNRoutePlanNode)
                        bundle.getSerializable("guobing");
            }
        }
        routeGuideEvent();


    }

    private void createHandler() {
        if (hd == null) {
            hd = new Handler(getMainLooper()) {
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == MSG_RESET_NODE) {
                        mRouteGuideManager.resetEndNodeInNavi(
                                new BNRoutePlanNode(116.21142, 40.85087, "百度大厦11",
                                        null, CoordinateType.GCJ02));
                    }
                }
            };
        }
    }

    private IBNRouteGuideManager.OnNavigationListener mOnNavigationListener =
            new IBNRouteGuideManager.OnNavigationListener() {

                @Override
                public void onNaviGuideEnd() {
                    finish();
                }

                @Override
                public void notifyOtherAction(int actionType, int arg1, int arg2, Object obj) {
                    if (actionType == 0) {
                        mRouteGuideManager.forceQuitNaviWithoutDialog();
                    }
                }
            };

    // 导航过程事件监听
    private void routeGuideEvent() {
        BaiduNaviManagerFactory.getRouteGuideManager().setRouteGuideEventListener(
                new IBNRouteGuideManager.IRouteGuideEventListener() {
                    @Override
                    public void onCommonEventCall(int what, int arg1, int arg2, Bundle bundle) {

                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRouteGuideManager.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRouteGuideManager.onResume();
    }

    protected void onPause() {
        super.onPause();
        mRouteGuideManager.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mRouteGuideManager.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRouteGuideManager.onDestroy(false);
    }

    @Override
    public void onBackPressed() {
        mRouteGuideManager.onBackPressed(false, true);
    }

    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mRouteGuideManager.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (!mRouteGuideManager.onKeyDown(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;

    }

}
