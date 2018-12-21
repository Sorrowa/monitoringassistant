package cn.cdjzxy.monitoringassistant.app;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.gson.GsonBuilder;
import com.wonders.health.lib.base.base.delegate.AppLifecycles;
import com.wonders.health.lib.base.di.module.AppModule;
import com.wonders.health.lib.base.di.module.ClientModule;
import com.wonders.health.lib.base.di.module.GlobalConfigModule;
import com.wonders.health.lib.base.http.log.RequestInterceptor;
import com.wonders.health.lib.base.integration.ConfigModule;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.cdjzxy.monitoringassistant.BuildConfig;
import io.rx_cache2.internal.RxCache;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * APP全局配置
 */

public class GlobalConfiguration implements ConfigModule {

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        if (!BuildConfig.LOG_DEBUG) { //Release 时,让框架不再打印 Http 请求和响应的信息
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
        }

        builder.baseurl(BuildConfig.SERVER_IP)
                .globalHttpHandler(new GlobalHttpHandlerImpl(context))
                .responseErrorListener(new ResponseErrorListenerImpl())
                .gsonConfiguration(new AppModule.GsonConfiguration() {//配置Gson
                    @Override
                    public void configGson(Context context, GsonBuilder gsonBuilder) {
                        gsonBuilder.serializeNulls()//支持序列化null的参数
                                .enableComplexMapKeySerialization();//支持将序列化key为object的map,默认只能序列化key为string的map
                    }
                })
                .retrofitConfiguration(new ClientModule.RetrofitConfiguration() {//配置retrofit
                    @Override
                    public void configRetrofit(Context context, Retrofit.Builder retrofitBuilder) {
                        //                        retrofitBuilder.addConverterFactory(FastJsonConverterFactory.create());//比如使用fastjson替代gson
                    }
                })
                .okhttpConfiguration(new ClientModule.OkhttpConfiguration() {//配置Okhttp
                    @Override
                    public void configOkhttp(Context context, OkHttpClient.Builder okHttpBuilder) {
                        okHttpBuilder.connectTimeout(6, TimeUnit.SECONDS);
                        RetrofitUrlManager.getInstance().with(okHttpBuilder);
                    }
                })
                .rxCacheConfiguration(new ClientModule.RxCacheConfiguration() {//配置RxCache
                    @Override
                    public RxCache configRxCache(Context context, RxCache.Builder rxCacheBuilder) {
                        rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                        return null;
                    }
                });
    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        lifecycles.add(new AppLifecyclesImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new ActivityLifecyclesImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                super.onFragmentCreated(fm, f, savedInstanceState);
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                super.onFragmentDestroyed(fm, f);
            }
        });
    }
}
