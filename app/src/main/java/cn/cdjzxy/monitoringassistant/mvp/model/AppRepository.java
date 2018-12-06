package cn.cdjzxy.monitoringassistant.mvp.model;


import com.wonders.health.lib.base.mvp.IModel;
import com.wonders.health.lib.base.mvp.IRepositoryManager;

import java.util.List;
import java.util.Map;

import cn.cdjzxy.monitoringassistant.mvp.model.api.cache.ApiCache;
import cn.cdjzxy.monitoringassistant.mvp.model.api.service.ApiService;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.BaseResponse;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.QrMoreInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MethodDevRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MethodTagRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemTagRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Rights;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * App相关的数据操作
 * Created by zhang on 2018/7/27.
 */

public class AppRepository implements IModel {

    private IRepositoryManager mManager;
    private ApiService         mApiService;
    private ApiCache           mApiCache;

    /**
     * 必须含有一个接收IRepositoryManager接口的构造函数,否则会报错
     *
     * @param manager
     */
    public AppRepository(IRepositoryManager manager) {
        this.mManager = manager;
        this.mApiService = mManager.createRetrofitService(ApiService.class);
        this.mApiCache = mManager.createCacheService(ApiCache.class);

    }

    /**
     * 登录
     *
     * @param params 参数
     * @return
     */
    public Observable<BaseResponse<UserInfo>> login(Map<String, String> params) {
        return Observable.just(mApiService.login(params))
                .flatMap(new Function<Observable<BaseResponse<UserInfo>>, ObservableSource<BaseResponse<UserInfo>>>() {
                    @Override
                    public ObservableSource<BaseResponse<UserInfo>> apply(Observable<BaseResponse<UserInfo>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<UserInfo>, BaseResponse<UserInfo>>() {
                            @Override
                            public BaseResponse<UserInfo> apply(BaseResponse<UserInfo> userBaseResponse) throws Exception {
                                return userBaseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取扫码结果
     *
     * @param qrCode
     * @return
     */
    public Observable<BaseResponse<QrMoreInfo>> getQrInfo(String qrCode) {
        return Observable.just(mApiService.getQrModelInfo(qrCode))
                .flatMap(new Function<Observable<BaseResponse<QrMoreInfo>>, ObservableSource<BaseResponse<QrMoreInfo>>>() {
                    @Override
                    public ObservableSource<BaseResponse<QrMoreInfo>> apply(Observable<BaseResponse<QrMoreInfo>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<QrMoreInfo>, BaseResponse<QrMoreInfo>>() {
                            @Override
                            public BaseResponse<QrMoreInfo> apply(BaseResponse<QrMoreInfo> qrMoreInfoBaseResponse) throws Exception {
                                return qrMoreInfoBaseResponse;
                            }
                        });

                    }
                });
    }



    @Override
    public void onDestroy() {
        //        this.mManager = null;
        //        this.mApiService = null;
        //        this.mApiCache = null;
    }
}
