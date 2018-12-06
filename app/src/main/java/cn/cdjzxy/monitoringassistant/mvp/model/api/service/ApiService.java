
package cn.cdjzxy.monitoringassistant.mvp.model.api.service;


import java.util.List;
import java.util.Map;

import cn.cdjzxy.monitoringassistant.mvp.model.api.Api;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.EnterRelatePoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Enterprise;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.EnvirPoint;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Projects;
import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * APP Service
 */
public interface ApiService {
    //*******************账户******************

    /**
     * 登录
     *
     * @param params
     * @return
     */
    @POST(Api.LOGIN)
    Observable<BaseResponse<UserInfo>> login(@QueryMap Map<String, String> params);

    /**
     * 退出登录
     *
     * @param token
     * @return
     */
    @POST(Api.LOGOUT)
    Observable<UserInfo> logout(@Query("token") String token);


    @GET(Api.QR_INFO)
    Observable<BaseResponse<QrMoreInfo>> getQrModelInfo(@Query("qrCode") String qrCode);


    //*******************基础数据******************
    @GET(Api.DEVICES)
    Observable<BaseResponse<List<Devices>>> getDevices();

    @GET(Api.METHODS)
    Observable<BaseResponse<List<Methods>>> getMethods();

    @GET(Api.MONITEMS)
    Observable<BaseResponse<List<MonItems>>> getMonItems();

    @GET(Api.GET_TAGS)
    Observable<BaseResponse<List<Tags>>> getTags();

    @GET(Api.GET_MONITEMTAG_RELATION)
    Observable<BaseResponse<List<MonItemTagRelation>>> GetMonItemTagRelation();

    @GET(Api.GET_METHODTAG_RELATION)
    Observable<BaseResponse<List<MethodTagRelation>>> GetMethodTagRelation();

    @GET(Api.GET_MONITEMMETHOD_RELATION)
    Observable<BaseResponse<List<MonItemMethodRelation>>> GetMonItemMethodRelation();

    @GET(Api.GET_METHODDEV_RELATION)
    Observable<BaseResponse<List<MethodDevRelation>>> GetMethodDevRelation();

    @GET(Api.GET_RIGHTS)
    Observable<BaseResponse<List<Rights>>> GetRight();


    //*******************任务******************
    @GET(Api.GET_PROJECTS)
    Observable<BaseResponse<List<Projects>>> getProjects();

    @GET(Api.GET_ENVIRPOINT)
    Observable<BaseResponse<List<EnvirPoint>>> getEnvirPoint();

    @GET(Api.GET_ENTERPOINT)
    Observable<BaseResponse<List<EnterRelatePoint>>> getEnterRelatePoint();

    @GET(Api.GET_ENTERPRISE)
    Observable<BaseResponse<List<Enterprise>>> getEnterprise();


    //*******************采样******************
    @GET(Api.GET_PROJECTS)
    Observable<BaseResponse<List<Projects>>> GetMyPendingTasks();

    @GET(Api.GET_ENVIRPOINT)
    Observable<BaseResponse<List<EnvirPoint>>> GetAllPendingTasks();

    @GET(Api.GET_ENTERPOINT)
    Observable<BaseResponse<List<EnterRelatePoint>>> GetTableList();

    @GET(Api.GET_ENTERPRISE)
    Observable<BaseResponse<List<Enterprise>>> GetTable();

    @GET(Api.GET_PROJECTS)
    Observable<BaseResponse<List<Projects>>> CreateTable();

    @GET(Api.GET_ENVIRPOINT)
    Observable<BaseResponse<List<EnvirPoint>>> UpdateTable();

    @GET(Api.GET_ENTERPOINT)
    Observable<BaseResponse<List<EnterRelatePoint>>> DeleteTable();

    @GET(Api.GET_ENTERPRISE)
    Observable<BaseResponse<List<Enterprise>>> GetSamplingStantd();

    @GET(Api.GET_PROJECTS)
    Observable<BaseResponse<List<Projects>>> GetFormSelect();

    @GET(Api.GET_ENVIRPOINT)
    Observable<BaseResponse<List<EnvirPoint>>> GetsamplingUser();

    @GET(Api.GET_ENTERPOINT)
    Observable<BaseResponse<List<EnterRelatePoint>>> PutSubmitSampling();


    //*******************文件******************
    @POST(Api.UPLOAD_FILE)
    Observable<BaseResponse> uploadFile();

    @DELETE(Api.DELETE_FILE)
    Observable<BaseResponse> deleteFile();


    //*******************知识库******************
    /**
     * 获取hash密码
     *
     * @param params 请求参数map
     * @return
     */
    @Headers({"Domain-Name: repository_server"})
    @GET(Api.REPOSITORY)
    Observable<ResponseBody> getRepositoryHashCode(@QueryMap Map<String, String> params);

    /**
     * 获取组结构
     *
     * @param params 请求参数map
     * @return
     */
    @Headers({"Domain-Name: repository_server"})
    @GET(Api.REPOSITORY)
    Observable<ResponseBody> getRepositoryGroups(@QueryMap Map<String, String> params);

    /**
     * 获取文件目录
     *
     * @param params 请求参数map
     * @return
     */
    @Headers({"Domain-Name: repository_server"})
    @GET(Api.REPOSITORY)
    Observable<ResponseBody> getRepositoryFolder(@QueryMap Map<String, String> params);

    /**
     * 获取文件
     *
     * @param params 请求参数map
     * @return
     */
    @Headers({"Domain-Name: repository_server"})
    @GET(Api.REPOSITORY)
    Observable<ResponseBody> getRepositoryFile(@QueryMap Map<String, String> params);

    /**
     * 下载文件
     *
     * @param params 请求参数map
     * @return
     */
    @Headers({"Domain-Name: repository_server"})
    @GET(Api.REPOSITORY)
    Observable<ResponseBody> downloadRepositoryFile(@QueryMap Map<String, String> params);
}
