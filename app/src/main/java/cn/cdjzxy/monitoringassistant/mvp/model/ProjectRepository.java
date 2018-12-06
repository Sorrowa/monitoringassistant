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
 * 任务相关的数据操作
 * Created by zhang on 2018/7/27.
 */

public class ProjectRepository implements IModel {

    private IRepositoryManager mManager;
    private ApiService         mApiService;
    private ApiCache           mApiCache;

    /**
     * 必须含有一个接收IRepositoryManager接口的构造函数,否则会报错
     *
     * @param manager
     */
    public ProjectRepository(IRepositoryManager manager) {
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


    /**
     * 获取扫码结果
     *
     * @return
     */
    public Observable<BaseResponse<List<Devices>>> getDevices() {
        return Observable.just(mApiService.getDevices())
                .flatMap(new Function<Observable<BaseResponse<List<Devices>>>, ObservableSource<BaseResponse<List<Devices>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Devices>>> apply(Observable<BaseResponse<List<Devices>>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Devices>>, BaseResponse<List<Devices>>>() {
                            @Override
                            public BaseResponse<List<Devices>> apply(BaseResponse<List<Devices>> baseResponse) throws Exception {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取扫码结果
     *
     * @return
     */
    public Observable<BaseResponse<List<Methods>>> getMethods() {
        return Observable.just(mApiService.getMethods())
                .flatMap(new Function<Observable<BaseResponse<List<Methods>>>, ObservableSource<BaseResponse<List<Methods>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Methods>>> apply(Observable<BaseResponse<List<Methods>>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Methods>>, BaseResponse<List<Methods>>>() {
                            @Override
                            public BaseResponse<List<Methods>> apply(BaseResponse<List<Methods>> baseResponse) throws Exception {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取扫码结果
     *
     * @return
     */
    public Observable<BaseResponse<List<MonItems>>> getMonItems() {
        return Observable.just(mApiService.getMonItems())
                .flatMap(new Function<Observable<BaseResponse<List<MonItems>>>, ObservableSource<BaseResponse<List<MonItems>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<MonItems>>> apply(Observable<BaseResponse<List<MonItems>>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<List<MonItems>>, BaseResponse<List<MonItems>>>() {
                            @Override
                            public BaseResponse<List<MonItems>> apply(BaseResponse<List<MonItems>> baseResponse) throws Exception {
                                return baseResponse;
                            }
                        });

                    }
                });
    }


    /**
     * 获取扫码结果
     *
     * @return
     */
    public Observable<BaseResponse<List<Tags>>> getTags() {
        return Observable.just(mApiService.getTags())
                .flatMap(new Function<Observable<BaseResponse<List<Tags>>>, ObservableSource<BaseResponse<List<Tags>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Tags>>> apply(Observable<BaseResponse<List<Tags>>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Tags>>, BaseResponse<List<Tags>>>() {
                            @Override
                            public BaseResponse<List<Tags>> apply(BaseResponse<List<Tags>> baseResponse) throws Exception {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取扫码结果
     *
     * @return
     */
    public Observable<BaseResponse<List<MonItemTagRelation>>> GetMonItemTagRelation() {
        return Observable.just(mApiService.GetMonItemTagRelation())
                .flatMap(new Function<Observable<BaseResponse<List<MonItemTagRelation>>>, ObservableSource<BaseResponse<List<MonItemTagRelation>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<MonItemTagRelation>>> apply(Observable<BaseResponse<List<MonItemTagRelation>>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<List<MonItemTagRelation>>, BaseResponse<List<MonItemTagRelation>>>() {
                            @Override
                            public BaseResponse<List<MonItemTagRelation>> apply(BaseResponse<List<MonItemTagRelation>> baseResponse) throws Exception {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取扫码结果
     *
     * @return
     */
    public Observable<BaseResponse<List<MethodTagRelation>>> GetMethodTagRelation() {
        return Observable.just(mApiService.GetMethodTagRelation())
                .flatMap(new Function<Observable<BaseResponse<List<MethodTagRelation>>>, ObservableSource<BaseResponse<List<MethodTagRelation>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<MethodTagRelation>>> apply(Observable<BaseResponse<List<MethodTagRelation>>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<List<MethodTagRelation>>, BaseResponse<List<MethodTagRelation>>>() {
                            @Override
                            public BaseResponse<List<MethodTagRelation>> apply(BaseResponse<List<MethodTagRelation>> baseResponse) throws Exception {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取扫码结果
     *
     * @return
     */
    public Observable<BaseResponse<List<MonItemMethodRelation>>> GetMonItemMethodRelation() {
        return Observable.just(mApiService.GetMonItemMethodRelation())
                .flatMap(new Function<Observable<BaseResponse<List<MonItemMethodRelation>>>, ObservableSource<BaseResponse<List<MonItemMethodRelation>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<MonItemMethodRelation>>> apply(Observable<BaseResponse<List<MonItemMethodRelation>>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<List<MonItemMethodRelation>>, BaseResponse<List<MonItemMethodRelation>>>() {
                            @Override
                            public BaseResponse<List<MonItemMethodRelation>> apply(BaseResponse<List<MonItemMethodRelation>> baseResponse) throws Exception {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取扫码结果
     *
     * @return
     */
    public Observable<BaseResponse<List<MethodDevRelation>>> GetMethodDevRelation() {
        return Observable.just(mApiService.GetMethodDevRelation())
                .flatMap(new Function<Observable<BaseResponse<List<MethodDevRelation>>>, ObservableSource<BaseResponse<List<MethodDevRelation>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<MethodDevRelation>>> apply(Observable<BaseResponse<List<MethodDevRelation>>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<List<MethodDevRelation>>, BaseResponse<List<MethodDevRelation>>>() {
                            @Override
                            public BaseResponse<List<MethodDevRelation>> apply(BaseResponse<List<MethodDevRelation>> baseResponse) throws Exception {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取扫码结果
     *
     * @return
     */
    public Observable<BaseResponse<List<Rights>>> GetRight() {
        return Observable.just(mApiService.GetRight())
                .flatMap(new Function<Observable<BaseResponse<List<Rights>>>, ObservableSource<BaseResponse<List<Rights>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Rights>>> apply(Observable<BaseResponse<List<Rights>>> baseResponseObservable) throws Exception {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Rights>>, BaseResponse<List<Rights>>>() {
                            @Override
                            public BaseResponse<List<Rights>> apply(BaseResponse<List<Rights>> baseResponse) throws Exception {
                                return baseResponse;
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
