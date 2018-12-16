package cn.cdjzxy.monitoringassistant.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;

import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.alpha.AlphaTextView;
import com.aries.ui.view.title.TitleBarView;

import com.liulishuo.filedownloader.FileDownloader;
import com.wonders.health.lib.base.AppManager;
import com.wonders.health.lib.base.base.delegate.AppLifecycles;
import com.wonders.health.lib.base.base.delegate.TitleBarViewControl;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.StackUtil;
import com.wonders.health.lib.base.widget.selector.SelectorDrawable;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import cn.cdjzxy.monitoringassistant.BuildConfig;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.MainActivity;
import io.reactivex.annotations.NonNull;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.external.ExternalAdaptInfo;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.LogUtils;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import timber.log.Timber;

/**
 * Application生命周期：用于初始化
 * Created by zhang on 2018/7/26.
 */

public class AppLifecyclesImpl implements AppLifecycles, TitleBarViewControl {

    private Context mContext;

    @Override
    public void attachBaseContext(@NonNull Context base) {
        mContext = base;
        MultiDex.install(base);
    }

    @Override
    public void onCreate(@NonNull Application application) {
        if (BuildConfig.LOG_DEBUG) {//Timber初始化
            Timber.plant(new Timber.DebugTree());
            ButterKnife.setDebug(true);
        }
        AppManager.init(application);
        AppManager.getInstance().setTitleBarViewControl(this);
        Thread.setDefaultUncaughtExceptionHandler(new ExHandler(Thread.getDefaultUncaughtExceptionHandler()));
        //        RetrofitUrlManager.getInstance().putDomain("repository_server", BuildConfig.REPOSITORY_URL);
        FileDownloader.setupOnApplicationOnCreate(application);
    }

    @Override
    public void onTerminate(@NonNull Application application) {

    }


    @Override
    public boolean createTitleBarViewControl(TitleBarView titleBar, Class<?> cls) {
        ColorStateList csl = ContextCompat.getColorStateList(mContext, R.color.btn_back_text_color);
        //是否支持状态栏白色
        boolean isSupport = StatusBarUtil.isSupportStatusBarFontChange();
        boolean isActivity = Activity.class.isAssignableFrom(cls);
        //设置TitleBarView 所有TextView颜色
        titleBar.setStatusBarLightMode(isSupport)
                //不支持黑字的设置白透明
                .setStatusAlpha(isSupport ? 0 : 102)
                //                .setLeftText(mContext.getResources().getString(R.string.back_str))
                .setLeftText("  ")
                .setLeftTextColor(csl)
                .setTitleMainTextSize(16)
                .setRightTextSize(16)
                .setTitleMainTextColor(Color.WHITE)
                .setRightTextColor(Color.WHITE)
                .setLeftTextColor(Color.WHITE)
                .setStatusBarLightMode(false)
                .setActionPadding(24)
                .setBgColor(ContextCompat.getColor(mContext, R.color.bg_color_2b99ff))
                .setLeftTextDrawable(isActivity ? SelectorDrawable.createBgDrawable(mContext, R.mipmap.ic_back) : null)
                .setDividerHeight(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? TitleBarView.dip2px(0.5f) : 0)
                .setOnLeftTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StackUtil.getInstance().getActivity(cls).onBackPressed();
                    }
                })
                .addLeftAction(titleBar.new TextAction(mContext.getResources().getString(R.string.close_str), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArtUtils.startActivity(MainActivity.class);
                    }
                }));
        ((AlphaTextView) titleBar.getLinearLayout(Gravity.LEFT).getChildAt(1)).setTextColor(csl);
        titleBar.getLinearLayout(Gravity.LEFT).removeViewAt(1);
        ViewCompat.setElevation(titleBar, mContext.getResources().getDimension(R.dimen.dp_2));
        return false;
    }


    // 捕获程序崩溃的异常,记录log(可以考虑将异常信息发回服务器)
    private class ExHandler implements Thread.UncaughtExceptionHandler {
        private Thread.UncaughtExceptionHandler internal;

        private ExHandler(Thread.UncaughtExceptionHandler eh) {
            internal = eh;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            File file = new File(Constant.LOG_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String fname = sdf.format(new Date());
            try {
                PrintStream ps = new PrintStream(file.getAbsolutePath() + "/" + fname);
                ps.println(ex.getMessage());
                ex.printStackTrace(ps);
                ps.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            internal.uncaughtException(thread, ex);
        }
    }

    // 判断是否主进程启动App
    private boolean shouldInit() {
        android.app.ActivityManager am = ((android.app.ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = mContext.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


}
