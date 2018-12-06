package cn.cdjzxy.monitoringassistant.mvp.model;


import com.wonders.health.lib.base.mvp.IModel;
import com.wonders.health.lib.base.mvp.IRepositoryManager;

import java.util.Map;

import cn.cdjzxy.monitoringassistant.mvp.model.api.service.ApiService;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * 知识库相关的数据操作
 */

public class FileRepository implements IModel {

    private IRepositoryManager mManager;
    private ApiService         mApiService;
    /**
     * 必须含有一个接收IRepositoryManager接口的构造函数,否则会报错
     *
     * @param manager
     */
    public FileRepository(IRepositoryManager manager) {
        this.mManager = manager;
        this.mApiService = mManager.createRetrofitService(ApiService.class);
    }

    /**
     * 获取知识库 hash密码
     *
     * @param params 参数
     * @return
     */
    public Observable<ResponseBody> getRepositoryHashCode(Map<String, String> params) {
        return Observable.just(mApiService.getRepositoryHashCode(params))
                .flatMap(new Function<Observable<ResponseBody>, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(Observable<ResponseBody> responseBodyObservable) throws Exception {
                        return responseBodyObservable.map(new Function<ResponseBody, ResponseBody>() {
                            @Override
                            public ResponseBody apply(ResponseBody responseBody) throws Exception {
                                return responseBody;
                            }
                        });

                    }
                });
    }

    /**
     * 获取知识库 小组
     *
     * @param params 参数
     * @return
     */
    public Observable<ResponseBody> getRepositoryGroups(Map<String, String> params) {
        return Observable.just(mApiService.getRepositoryGroups(params))
                .flatMap(new Function<Observable<ResponseBody>, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(Observable<ResponseBody> responseBodyObservable) throws Exception {
                        return responseBodyObservable.map(new Function<ResponseBody, ResponseBody>() {
                            @Override
                            public ResponseBody apply(ResponseBody responseBody) throws Exception {
                                return responseBody;
                            }
                        });

                    }
                });
    }

    /**
     * 获取知识库 目录
     *
     * @param params 参数
     * @return
     */
    public Observable<ResponseBody> getRepositoryFolder(Map<String, String> params) {
        return Observable.just(mApiService.getRepositoryFolder(params))
                .flatMap(new Function<Observable<ResponseBody>, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(Observable<ResponseBody> responseBodyObservable) throws Exception {
                        return responseBodyObservable.map(new Function<ResponseBody, ResponseBody>() {
                            @Override
                            public ResponseBody apply(ResponseBody responseBody) throws Exception {
                                return responseBody;
                            }
                        });

                    }
                });
    }

    /**
     * 获取知识库 文件
     *
     * @param params 参数
     * @return
     */
    public Observable<ResponseBody> getRepositoryFile(Map<String, String> params) {
        return Observable.just(mApiService.getRepositoryFile(params))
                .flatMap(new Function<Observable<ResponseBody>, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(Observable<ResponseBody> responseBodyObservable) throws Exception {
                        return responseBodyObservable.map(new Function<ResponseBody, ResponseBody>() {
                            @Override
                            public ResponseBody apply(ResponseBody responseBody) throws Exception {
                                return responseBody;
                            }
                        });

                    }
                });
    }





    @Override
    public void onDestroy() {

    }
}
