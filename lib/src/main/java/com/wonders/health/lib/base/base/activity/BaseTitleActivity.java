/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wonders.health.lib.base.base.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.delegate.IActivity;
import com.wonders.health.lib.base.base.delegate.ITitleView;
import com.wonders.health.lib.base.base.delegate.TitleDelegate;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.integration.cache.Cache;
import com.wonders.health.lib.base.integration.cache.CacheType;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.widget.state.loader.StateRepository;
import com.wonders.health.lib.base.widget.state.manager.StateChanger;
import com.wonders.health.lib.base.widget.state.manager.StateEventListener;
import com.wonders.health.lib.base.widget.state.manager.StateManager;
import com.wonders.health.lib.base.widget.state.state.StateProperty;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;


/**
 * 可使用标题Activity
 */
public abstract class BaseTitleActivity<P extends IPresenter> extends AppCompatActivity implements IActivity<P>, ITitleView, StateChanger {
    protected final String TAG = this.getClass().getSimpleName();
    private Cache<String, Object> mCache;
    private Unbinder mUnbinder;
    protected P mPresenter;

    protected View mContentView;
    protected TitleDelegate mTitleDelegate;
    protected TitleBarView mTitleBar;

    private StateManager stateManager;

    @NonNull
    @Override
    public synchronized Cache<String, Object> provideCache() {
        if (mCache == null) {
            mCache = ArtUtils.obtainAppComponentFromContext(this).cacheFactory().build(CacheType.ACTIVITY_CACHE);
        }
        return mCache;
    }

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stateManager = StateManager.newInstance(this, new StateRepository(this));
        try {
            int layoutResID = initView(savedInstanceState);
            //如果 initView 返回 0, 框架则不会调用 setContentView(), 当然也不会 Bind ButterKnife
            if (layoutResID != 0) {
                mContentView = View.inflate(this, layoutResID, null);
                setContentView(mContentView);
                //绑定到butterknife
                mUnbinder = ButterKnife.bind(this);
                mTitleDelegate = new TitleDelegate(mContentView, this, this.getClass());
                mTitleBar = mTitleDelegate.mTitleBar;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    @Override
    public void setPresenter(@Nullable P presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mPresenter == null)
            mPresenter = obtainPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mPresenter = null;
        this.mUnbinder = null;
        stateManager.onDestoryView();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));//设置字体
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(stateManager.setContentView(layoutResID));
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(stateManager.setContentView(view), params);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(stateManager.setContentView(view));
    }

    @Override
    public void setStateEventListener(StateEventListener listener) {
        stateManager.setStateEventListener(listener);
    }


    @Override
    public boolean showState(String state) {
        return stateManager.showState(state);
    }

    @Override
    public boolean showState(StateProperty state) {
        return stateManager.showState(state);
    }

    @Override
    public String getState() {
        return stateManager.getState();
    }

    /**
     * 获取全局View
     */
    public View getOverallView() {
        return stateManager.getOverallView();
    }

    public StateManager getStateManager() {
        return stateManager;
    }


    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    @Override
    public boolean useFragment() {
        return true;
    }

}
