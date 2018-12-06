package cn.cdjzxy.monitoringassistant.app;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.wonders.health.lib.base.utils.StackUtil;


/**
 * Activity生命周期：
 */

public class ActivityLifecyclesImpl implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        StackUtil.getInstance().push(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        StackUtil.getInstance().pop(activity);
    }
}
