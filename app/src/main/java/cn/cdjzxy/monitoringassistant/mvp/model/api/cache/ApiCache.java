
package cn.cdjzxy.monitoringassistant.mvp.model.api.cache;


import io.rx_cache2.EncryptKey;

/**
 * APP 缓存
 */
@EncryptKey("123")
public interface ApiCache {

    //    @Encrypt
    //    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    //    Observable<Reply<BaseResponse<AppConfig>>> getAppConfig(Observable<BaseResponse<AppConfig>> appConfigObservable);

}
