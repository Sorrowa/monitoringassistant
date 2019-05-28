package cn.cdjzxy.monitoringassistant.mvp.model;


import com.wonders.health.lib.base.mvp.IModel;
import com.wonders.health.lib.base.mvp.IRepositoryManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cdjzxy.monitoringassistant.mvp.model.api.cache.ApiCache;
import cn.cdjzxy.monitoringassistant.mvp.model.api.service.ApiService;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.BaseResponse;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.UploadFileResponse;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Dic;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnterRelatePoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Enterprise;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MethodDevRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MethodTagRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemTagRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Rights;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.WanderSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.gps.Gps;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.qr.QrMoreInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.FileInfoData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectPlan;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.PartMap;

/**
 * App相关的数据操作
 */

public class ApiRepository implements IModel {

    private IRepositoryManager mManager;
    private ApiService mApiService;
    private ApiCache mApiCache;

    /**
     * 必须含有一个接收IRepositoryManager接口的构造函数,否则会报错
     *
     * @param manager
     */
    public ApiRepository(IRepositoryManager manager) {
        this.mManager = manager;
        this.mApiService = mManager.createRetrofitService(ApiService.class);
        this.mApiCache = mManager.createCacheService(ApiCache.class);
    }

    @Override
    public void onDestroy() {

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
                    public ObservableSource<BaseResponse<UserInfo>> apply(Observable<BaseResponse<UserInfo>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<UserInfo>, BaseResponse<UserInfo>>() {
                            @Override
                            public BaseResponse<UserInfo> apply(BaseResponse<UserInfo> userBaseResponse) {
                                return userBaseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 退出登录
     *
     * @return
     */
    public Observable<BaseResponse> logout() {
        return Observable.just(mApiService.logout())
                .flatMap(new Function<Observable<BaseResponse>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(Observable<BaseResponse> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse, BaseResponse>() {
                            @Override
                            public BaseResponse apply(BaseResponse baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 修改密码
     *
     * @return
     */
    public Observable<BaseResponse> modifyPwd(String oldPwd, String newPwd) {
        Map<String, String> params = new HashMap<>();
        params.put("oldpwd", oldPwd);
        params.put("newpwd", newPwd);
        return Observable.just(mApiService.modifyPwd(params))
                .flatMap(new Function<Observable<BaseResponse>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(Observable<BaseResponse> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse, BaseResponse>() {
                            @Override
                            public BaseResponse apply(BaseResponse baseResponse) {
                                return baseResponse;
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
                    public ObservableSource<BaseResponse<QrMoreInfo>> apply(Observable<BaseResponse<QrMoreInfo>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<QrMoreInfo>, BaseResponse<QrMoreInfo>>() {
                            @Override
                            public BaseResponse<QrMoreInfo> apply(BaseResponse<QrMoreInfo> qrMoreInfoBaseResponse) {
                                return qrMoreInfoBaseResponse;
                            }
                        });

                    }
                });
    }


    /**
     * 获取设备
     *
     * @return
     */
    public Observable<BaseResponse<List<Devices>>> getDevices() {
        return Observable.just(mApiService.getDevices())
                .flatMap(new Function<Observable<BaseResponse<List<Devices>>>, ObservableSource<BaseResponse<List<Devices>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Devices>>> apply(Observable<BaseResponse<List<Devices>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Devices>>, BaseResponse<List<Devices>>>() {
                            @Override
                            public BaseResponse<List<Devices>> apply(BaseResponse<List<Devices>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取方法
     *
     * @return
     */
    public Observable<BaseResponse<List<Methods>>> getMethods() {
        return Observable.just(mApiService.getMethods())
                .flatMap(new Function<Observable<BaseResponse<List<Methods>>>, ObservableSource<BaseResponse<List<Methods>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Methods>>> apply(Observable<BaseResponse<List<Methods>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Methods>>, BaseResponse<List<Methods>>>() {
                            @Override
                            public BaseResponse<List<Methods>> apply(BaseResponse<List<Methods>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取监测项目
     *
     * @return
     */
    public Observable<BaseResponse<List<MonItems>>> getMonItems() {
        return Observable.just(mApiService.getMonItems())
                .flatMap(new Function<Observable<BaseResponse<List<MonItems>>>, ObservableSource<BaseResponse<List<MonItems>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<MonItems>>> apply(Observable<BaseResponse<List<MonItems>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<MonItems>>, BaseResponse<List<MonItems>>>() {
                            @Override
                            public BaseResponse<List<MonItems>> apply(BaseResponse<List<MonItems>> baseResponse) {
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
                    public ObservableSource<BaseResponse<List<Tags>>> apply(Observable<BaseResponse<List<Tags>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Tags>>, BaseResponse<List<Tags>>>() {
                            @Override
                            public BaseResponse<List<Tags>> apply(BaseResponse<List<Tags>> baseResponse) {
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
    public Observable<BaseResponse<List<MonItemTagRelation>>> getMonItemTagRelation() {
        return Observable.just(mApiService.getMonItemTagRelation())
                .flatMap(new Function<Observable<BaseResponse<List<MonItemTagRelation>>>, ObservableSource<BaseResponse<List<MonItemTagRelation>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<MonItemTagRelation>>> apply(Observable<BaseResponse<List<MonItemTagRelation>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<MonItemTagRelation>>, BaseResponse<List<MonItemTagRelation>>>() {
                            @Override
                            public BaseResponse<List<MonItemTagRelation>> apply(BaseResponse<List<MonItemTagRelation>> baseResponse) {
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
    public Observable<BaseResponse<List<MethodTagRelation>>> getMethodTagRelation() {
        return Observable.just(mApiService.getMethodTagRelation())
                .flatMap(new Function<Observable<BaseResponse<List<MethodTagRelation>>>, ObservableSource<BaseResponse<List<MethodTagRelation>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<MethodTagRelation>>> apply(Observable<BaseResponse<List<MethodTagRelation>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<MethodTagRelation>>, BaseResponse<List<MethodTagRelation>>>() {
                            @Override
                            public BaseResponse<List<MethodTagRelation>> apply(BaseResponse<List<MethodTagRelation>> baseResponse) {
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
    public Observable<BaseResponse<List<MonItemMethodRelation>>> getMonItemMethodRelation() {
        return Observable.just(mApiService.getMonItemMethodRelation())
                .flatMap(new Function<Observable<BaseResponse<List<MonItemMethodRelation>>>, ObservableSource<BaseResponse<List<MonItemMethodRelation>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<MonItemMethodRelation>>> apply(Observable<BaseResponse<List<MonItemMethodRelation>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<MonItemMethodRelation>>, BaseResponse<List<MonItemMethodRelation>>>() {
                            @Override
                            public BaseResponse<List<MonItemMethodRelation>> apply(BaseResponse<List<MonItemMethodRelation>> baseResponse) {
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
    public Observable<BaseResponse<List<MethodDevRelation>>> getMethodDevRelation() {
        return Observable.just(mApiService.getMethodDevRelation())
                .flatMap(new Function<Observable<BaseResponse<List<MethodDevRelation>>>, ObservableSource<BaseResponse<List<MethodDevRelation>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<MethodDevRelation>>> apply(Observable<BaseResponse<List<MethodDevRelation>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<MethodDevRelation>>, BaseResponse<List<MethodDevRelation>>>() {
                            @Override
                            public BaseResponse<List<MethodDevRelation>> apply(BaseResponse<List<MethodDevRelation>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取权限
     *
     * @return
     */
    public Observable<BaseResponse<List<Rights>>> getRight() {
        return Observable.just(mApiService.getRight())
                .flatMap(new Function<Observable<BaseResponse<List<Rights>>>, ObservableSource<BaseResponse<List<Rights>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Rights>>> apply(Observable<BaseResponse<List<Rights>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Rights>>, BaseResponse<List<Rights>>>() {
                            @Override
                            public BaseResponse<List<Rights>> apply(BaseResponse<List<Rights>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取权限
     *
     * @return
     */
    public Observable<BaseResponse<List<EnvirPoint>>> getEnvirPoint() {
        return Observable.just(mApiService.getEnvirPoint())
                .flatMap(new Function<Observable<BaseResponse<List<EnvirPoint>>>, ObservableSource<BaseResponse<List<EnvirPoint>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<EnvirPoint>>> apply(Observable<BaseResponse<List<EnvirPoint>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<EnvirPoint>>, BaseResponse<List<EnvirPoint>>>() {
                            @Override
                            public BaseResponse<List<EnvirPoint>> apply(BaseResponse<List<EnvirPoint>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }


    /**
     * 获取权限
     *
     * @return
     */
    public Observable<BaseResponse<List<EnterRelatePoint>>> getEnterRelatePoint() {
        return Observable.just(mApiService.getEnterRelatePoint())
                .flatMap(new Function<Observable<BaseResponse<List<EnterRelatePoint>>>, ObservableSource<BaseResponse<List<EnterRelatePoint>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<EnterRelatePoint>>> apply(Observable<BaseResponse<List<EnterRelatePoint>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<EnterRelatePoint>>, BaseResponse<List<EnterRelatePoint>>>() {
                            @Override
                            public BaseResponse<List<EnterRelatePoint>> apply(BaseResponse<List<EnterRelatePoint>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取权限
     *
     * @return
     */
    public Observable<BaseResponse<List<Enterprise>>> getEnterprise() {
        return Observable.just(mApiService.getEnterprise())
                .flatMap(new Function<Observable<BaseResponse<List<Enterprise>>>, ObservableSource<BaseResponse<List<Enterprise>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Enterprise>>> apply(Observable<BaseResponse<List<Enterprise>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Enterprise>>, BaseResponse<List<Enterprise>>>() {
                            @Override
                            public BaseResponse<List<Enterprise>> apply(BaseResponse<List<Enterprise>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取权限
     *
     * @return
     */
    public Observable<BaseResponse<List<Dic>>> getDic(int type) {
        return Observable.just(mApiService.getDic(type))
                .flatMap(new Function<Observable<BaseResponse<List<Dic>>>, ObservableSource<BaseResponse<List<Dic>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Dic>>> apply(Observable<BaseResponse<List<Dic>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Dic>>, BaseResponse<List<Dic>>>() {
                            @Override
                            public BaseResponse<List<Dic>> apply(BaseResponse<List<Dic>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取天气
     *
     * @return
     */
    public Observable<BaseResponse<List<String>>> getWeather() {
        return Observable.just(mApiService.getWeather())
                .flatMap(new Function<Observable<BaseResponse<List<String>>>, ObservableSource<BaseResponse<List<String>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<String>>> apply(Observable<BaseResponse<List<String>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<String>>, BaseResponse<List<String>>>() {
                            @Override
                            public BaseResponse<List<String>> apply(BaseResponse<List<String>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取采样人员
     *
     * @return
     */
    public Observable<BaseResponse<List<User>>> getUser() {
        return Observable.just(mApiService.getUser())
                .flatMap(new Function<Observable<BaseResponse<List<User>>>, ObservableSource<BaseResponse<List<User>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<User>>> apply(Observable<BaseResponse<List<User>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<User>>, BaseResponse<List<User>>>() {
                            @Override
                            public BaseResponse<List<User>> apply(BaseResponse<List<User>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取结果单位
     *
     * @return
     */
    public Observable<BaseResponse<List<Unit>>> getUnit() {
        return Observable.just(mApiService.getUnit())
                .flatMap(new Function<Observable<BaseResponse<List<Unit>>>, ObservableSource<BaseResponse<List<Unit>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Unit>>> apply(Observable<BaseResponse<List<Unit>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Unit>>, BaseResponse<List<Unit>>>() {
                            @Override
                            public BaseResponse<List<Unit>> apply(BaseResponse<List<Unit>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }


    /**
     * 获取所有消息
     *
     * @return
     */
    public Observable<BaseResponse<List<Msg>>> getMsgs() {
        return Observable.just(mApiService.getMsgs())
                .flatMap(new Function<Observable<BaseResponse<List<Msg>>>, ObservableSource<BaseResponse<List<Msg>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Msg>>> apply(Observable<BaseResponse<List<Msg>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Msg>>, BaseResponse<List<Msg>>>() {
                            @Override
                            public BaseResponse<List<Msg>> apply(BaseResponse<List<Msg>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 批量阅读消息
     *
     * @return
     */
    public Observable<BaseResponse> putReadMsg(List<String> messageIds) {
        return Observable.just(mApiService.putReadMsg(messageIds))
                .flatMap(new Function<Observable<BaseResponse>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(Observable<BaseResponse> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse, BaseResponse>() {
                            @Override
                            public BaseResponse apply(BaseResponse baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }


    public Observable<BaseResponse<List<Project>>> getAllTasks() {
        return Observable.just(mApiService.getAllTasks())
                .flatMap(new Function<Observable<BaseResponse<List<Project>>>, ObservableSource<BaseResponse<List<Project>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Project>>> apply(Observable<BaseResponse<List<Project>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Project>>, BaseResponse<List<Project>>>() {
                            @Override
                            public BaseResponse<List<Project>> apply(BaseResponse<List<Project>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }


    /**
     * 获取我的任务
     *
     * @return
     */
    public Observable<BaseResponse<List<Project>>> getMyTasks() {
        return Observable.just(mApiService.getMyTasks())
                .flatMap(new Function<Observable<BaseResponse<List<Project>>>, ObservableSource<BaseResponse<List<Project>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Project>>> apply(Observable<BaseResponse<List<Project>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Project>>, BaseResponse<List<Project>>>() {
                            @Override
                            public BaseResponse<List<Project>> apply(BaseResponse<List<Project>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取我的任务
     *
     * @return
     */
    public Observable<BaseResponse<List<SamplingStantd>>> getSamplingStantd() {
        return Observable.just(mApiService.getSamplingStantd())
                .flatMap(new Function<Observable<BaseResponse<List<SamplingStantd>>>, ObservableSource<BaseResponse<List<SamplingStantd>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<SamplingStantd>>> apply(Observable<BaseResponse<List<SamplingStantd>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<SamplingStantd>>, BaseResponse<List<SamplingStantd>>>() {
                            @Override
                            public BaseResponse<List<SamplingStantd>> apply(BaseResponse<List<SamplingStantd>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }


    /**
     * 获取我的任务
     *
     * @return
     */
    public Observable<BaseResponse<List<Form>>> getFormSelect() {
        return Observable.just(mApiService.getFormSelect())
                .flatMap(new Function<Observable<BaseResponse<List<Form>>>, ObservableSource<BaseResponse<List<Form>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Form>>> apply(Observable<BaseResponse<List<Form>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Form>>, BaseResponse<List<Form>>>() {
                            @Override
                            public BaseResponse<List<Form>> apply(BaseResponse<List<Form>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取我的任务
     *
     * @return
     */
    public Observable<BaseResponse<List<Sampling>>> getSampling(List<String> projectIds) {
        return Observable.just(mApiService.getSampling(projectIds))
                .flatMap(new Function<Observable<BaseResponse<List<Sampling>>>, ObservableSource<BaseResponse<List<Sampling>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Sampling>>> apply(Observable<BaseResponse<List<Sampling>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Sampling>>, BaseResponse<List<Sampling>>>() {
                            @Override
                            public BaseResponse<List<Sampling>> apply(BaseResponse<List<Sampling>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 根据ID获取采样单信息
     *
     * @return
     */
    public Observable<BaseResponse<List<Sampling>>> getSamplingById(String samplingId) {
        return Observable.just(mApiService.getSamplingByID(samplingId))
                .flatMap(new Function<Observable<BaseResponse<List<Sampling>>>, ObservableSource<BaseResponse<List<Sampling>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<Sampling>>> apply(Observable<BaseResponse<List<Sampling>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<Sampling>>, BaseResponse<List<Sampling>>>() {
                            @Override
                            public BaseResponse<List<Sampling>> apply(BaseResponse<List<Sampling>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }


    /**
     * 创建
     *
     * @return
     */
    public Observable<BaseResponse> putSamplingFinish(Map<String, String> params) {
        return Observable.just(mApiService.putSamplingFinish(params))
                .flatMap(new Function<Observable<BaseResponse>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(Observable<BaseResponse> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse, BaseResponse>() {
                            @Override
                            public BaseResponse apply(BaseResponse baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 创建
     *
     * @return
     */
    public Observable<BaseResponse> putProjectContent(ProjectPlan projectPlan) {
        return Observable.just(mApiService.putProjectContent(projectPlan))
                .flatMap(new Function<Observable<BaseResponse>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(Observable<BaseResponse> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse, BaseResponse>() {
                            @Override
                            public BaseResponse apply(BaseResponse baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }


    /**
     * 创建
     *
     * @return
     */
    public Observable<BaseResponse> createTable(PreciptationSampForm preciptationSampForm) {
        return Observable.just(mApiService.createTable(preciptationSampForm))
                .flatMap(new Function<Observable<BaseResponse>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(Observable<BaseResponse> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse, BaseResponse>() {
                            @Override
                            public BaseResponse apply(BaseResponse baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 创建
     *
     * @return
     */
    public Observable<BaseResponse> updateTable(PreciptationSampForm preciptationSampForm) {
        return Observable.just(mApiService.updateTable(preciptationSampForm))
                .flatMap(new Function<Observable<BaseResponse>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(Observable<BaseResponse> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse, BaseResponse>() {
                            @Override
                            public BaseResponse apply(BaseResponse baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }


    /**
     * 批量阅读消息
     *
     * @return
     */
    public Observable<BaseResponse> deleteTable(String tableId) {
        return Observable.just(mApiService.deleteTable(tableId))
                .flatMap(new Function<Observable<BaseResponse>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(Observable<BaseResponse> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse, BaseResponse>() {
                            @Override
                            public BaseResponse apply(BaseResponse baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }


    /**
     * 上传文件
     *
     * @return
     */
    public Observable<Response<UploadFileResponse<List<FileInfoData>>>> uploadFile(List<MultipartBody.Part> parts, Map<String, RequestBody> params) {
        return Observable.just(mApiService.uploadFile(parts, params))
                .flatMap(new Function<Observable<Response<UploadFileResponse<List<FileInfoData>>>>, ObservableSource<Response<UploadFileResponse<List<FileInfoData>>>>>() {
                    @Override
                    public ObservableSource<Response<UploadFileResponse<List<FileInfoData>>>> apply(Observable<Response<UploadFileResponse<List<FileInfoData>>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<Response<UploadFileResponse<List<FileInfoData>>>, Response<UploadFileResponse<List<FileInfoData>>>>() {
                            @Override
                            public Response<UploadFileResponse<List<FileInfoData>>> apply(Response<UploadFileResponse<List<FileInfoData>>> baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

    /**
     * 获取流转任务列表
     *
     * @return
     */
    public Observable<BaseResponse<ProjectSampleStorage>> getSampleStorageProject(Map<String, String> map) {
        return Observable.just(mApiService.getSampleStorageProject(map))
                .flatMap(new Function<Observable<BaseResponse<ProjectSampleStorage>>, ObservableSource<BaseResponse<ProjectSampleStorage>>>() {
                    @Override
                    public ObservableSource<BaseResponse<ProjectSampleStorage>> apply(Observable<BaseResponse<ProjectSampleStorage>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<ProjectSampleStorage>, BaseResponse<ProjectSampleStorage>>() {
                            @Override
                            public BaseResponse<ProjectSampleStorage> apply(BaseResponse<ProjectSampleStorage> projectSampleStorageBaseResponse) {
                                return projectSampleStorageBaseResponse;
                            }
                        });
                    }
                });
    }

    /**
     * 获取流转任务列表
     *
     * @param map
     * @return
     */
    public Observable<BaseResponse<List<WanderSampleStorage>>> getSampleStorageList(Map<String, String> map) {
        return Observable.just(mApiService.getSampleStorageList(map))
                .flatMap(new Function<Observable<BaseResponse<List<WanderSampleStorage>>>, ObservableSource<BaseResponse<List<WanderSampleStorage>>>>() {
                    @Override
                    public ObservableSource<BaseResponse<List<WanderSampleStorage>>> apply(Observable<BaseResponse<List<WanderSampleStorage>>> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse<List<WanderSampleStorage>>, BaseResponse<List<WanderSampleStorage>>>() {
                            @Override
                            public BaseResponse<List<WanderSampleStorage>> apply(BaseResponse<List<WanderSampleStorage>> listBaseResponse) {
                                return listBaseResponse;
                            }
                        });
                    }
                });
    }

    /**
     * 上传点位
     *
     * @return
     */
    public Observable<BaseResponse> postGaoDeCoordInates(List<Gps> gpsList) {
        return Observable.just(mApiService.postGaoDeCoordinates(gpsList))
                .flatMap(new Function<Observable<BaseResponse>, ObservableSource<BaseResponse>>() {
                    @Override
                    public ObservableSource<BaseResponse> apply(Observable<BaseResponse> baseResponseObservable) {
                        return baseResponseObservable.map(new Function<BaseResponse, BaseResponse>() {
                            @Override
                            public BaseResponse apply(BaseResponse baseResponse) {
                                return baseResponse;
                            }
                        });

                    }
                });
    }

}