
package com.wonders.health.lib.base.base.delegate;

import android.app.Activity;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.wonders.health.lib.base.mvp.IPresenter;

import org.simple.eventbus.EventBus;



/**
 * ================================================
 * {@link ActivityDelegate} 默认实现类
 * <p>
 * Created by JessYan on 26/04/2017 20:23
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class ActivityDelegateImpl implements ActivityDelegate {
    private Activity mActivity;
    private IActivity iActivity;
    private IPresenter iPresenter;


    public ActivityDelegateImpl(@NonNull Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity.useEventBus()) {
            //注册到事件主线
            EventBus.getDefault().register(mActivity);
        }
        this.iPresenter = iActivity.obtainPresenter();
        iActivity.setPresenter(iPresenter);
        //将 LifecycleObserver 注册给 LifecycleOwner 后 @OnLifecycleEvent 才可以正常使用
        if (mActivity != null && mActivity instanceof LifecycleOwner
                && iPresenter != null && iPresenter instanceof LifecycleObserver){
            ((LifecycleOwner) mActivity).getLifecycle().addObserver((LifecycleObserver) iPresenter);
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

    }

    @Override
    public void onDestroy() {
        //如果要使用 EventBus 请将此方法返回 true
        if (iActivity != null && iActivity.useEventBus())
            EventBus.getDefault().unregister(mActivity);
        //释放资源
        if (iPresenter != null) iPresenter.onDestroy();
        this.iActivity = null;
        this.mActivity = null;
        this.iPresenter = null;
    }
}
