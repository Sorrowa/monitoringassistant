package com.wonders.health.lib.base;

import android.app.Application;

import com.wonders.health.lib.base.base.delegate.TitleBarViewControl;

public class AppManager {
    private static String TAG = "AppManager";
    private static volatile AppManager sInstance;


    private static Application mApplication;

    /**
     * 配置TitleBarView相关属性
     */
    private TitleBarViewControl mTitleBarViewControl;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("You've to call static method init(Application) first in Application");
        }
        return sInstance;
    }


    public Application getApplication() {
        return mApplication;
    }

    /**
     * @param application
     */
    public static AppManager init(Application application) {
        //保证只执行一次初始化属性
        if (mApplication == null && application != null) {
            mApplication = application;
            sInstance = new AppManager();

        }
        return getInstance();
    }


    public TitleBarViewControl getTitleBarViewControl() {
        return mTitleBarViewControl;
    }

    public AppManager setTitleBarViewControl(TitleBarViewControl control) {
        mTitleBarViewControl = control;
        return this;
    }
}
