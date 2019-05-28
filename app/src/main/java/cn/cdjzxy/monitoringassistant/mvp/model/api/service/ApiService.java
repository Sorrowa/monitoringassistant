
package cn.cdjzxy.monitoringassistant.mvp.model.api.service;


import com.baidu.navisdk.ui.routeguide.mapmode.subview.M;

import java.util.List;
import java.util.Map;

import cn.cdjzxy.monitoringassistant.mvp.model.api.Api;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.BaseResponse;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.UploadFileResponse;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Unit;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.WanderSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.gps.Gps;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectSampleStorage;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.qr.QrMoreInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingUser;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Table;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.FileInfoData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectPlan;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
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
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

import static me.jessyan.retrofiturlmanager.RetrofitUrlManager.DOMAIN_NAME_HEADER;

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
     * @return
     */
    @POST(Api.LOGOUT)
    Observable<BaseResponse> logout();

    /**
     * 修改密码
     *
     * @param params
     * @return
     */
    @PUT(Api.PWD_MODIFY)
    Observable<BaseResponse> modifyPwd(@QueryMap Map<String, String> params);

    @Headers({DOMAIN_NAME_HEADER + Api.LOGIN_RESP_WEBURL})
    @GET(Api.QR_INFO)
    Observable<BaseResponse<QrMoreInfo>> getQrModelInfo(@Query("qrCode") String qrCode);


    //*******************基础数据******************
    @GET(Api.GET_DEVICES)
    Observable<BaseResponse<List<Devices>>> getDevices();

    @GET(Api.GET_METHODS)
    Observable<BaseResponse<List<Methods>>> getMethods();

    @GET(Api.GET_MONITEMS)
    Observable<BaseResponse<List<MonItems>>> getMonItems();

    @GET(Api.GET_TAGS)
    Observable<BaseResponse<List<Tags>>> getTags();

    @GET(Api.GET_MONITEMTAG_RELATION)
    Observable<BaseResponse<List<MonItemTagRelation>>> getMonItemTagRelation();

    @GET(Api.GET_METHODTAG_RELATION)
    Observable<BaseResponse<List<MethodTagRelation>>> getMethodTagRelation();

    @GET(Api.GET_MONITEMMETHOD_RELATION)
    Observable<BaseResponse<List<MonItemMethodRelation>>> getMonItemMethodRelation();

    @GET(Api.GET_METHODDEV_RELATION)
    Observable<BaseResponse<List<MethodDevRelation>>> getMethodDevRelation();

    @GET(Api.GET_RIGHTS)
    Observable<BaseResponse<List<Rights>>> getRight();

    @GET(Api.GET_ENVIRPOINT)
    Observable<BaseResponse<List<EnvirPoint>>> getEnvirPoint();

    @GET(Api.GET_ENTERPOINT)
    Observable<BaseResponse<List<EnterRelatePoint>>> getEnterRelatePoint();

    @GET(Api.GET_ENTERPRISE)
    Observable<BaseResponse<List<Enterprise>>> getEnterprise();

    @GET(Api.GET_DIC)
    Observable<BaseResponse<List<Dic>>> getDic(@Query("Type") int type);

    @GET(Api.GET_WEATHER)
    Observable<BaseResponse<List<String>>> getWeather();

    @GET(Api.GET_USER)
    Observable<BaseResponse<List<User>>> getUser();

    @GET(Api.GET_Unit)
    Observable<BaseResponse<List<Unit>>> getUnit();


    //*******************任务******************
    @GET(Api.GET_MY_TASKS)
    Observable<BaseResponse<List<Project>>> getMyTasks();

    @GET(Api.GET_ALL_TASKS)
    Observable<BaseResponse<List<Project>>> getAllTasks();

    @PUT(Api.PUT_SAMPLING_FINISH)
    Observable<BaseResponse> putSamplingFinish(@Body Map<String, String> params);

    @PUT(Api.PUT_PROJECT_CONTENT)
    Observable<BaseResponse> putProjectContent(@Body ProjectPlan projectPlan);

    @GET(Api.GET_SAMPLE_STORAGE)
    Observable<BaseResponse<ProjectSampleStorage>> getSampleStorageProject(@QueryMap Map<String, String> map);

    //*******************流转******************
    @GET(Api.GET_SAMPLE_STORAGE_LIST)
    Observable<BaseResponse<List<WanderSampleStorage>>> getSampleStorageList(@QueryMap Map<String, String> map);

    @GET(Api.GET_SAMPLE_STORAGE_ALL_LIST)
    Observable<BaseResponse<List<Project>>> getSampleStorageAllList();

    @PUT(Api.PUT_SAMPLE_STORAGE)
    Observable<BaseResponse> putVerifySampleStorage();


    //*******************采样******************
    @GET(Api.GET_TABLES)
    Observable<BaseResponse<List<Table>>> getTableList();

    @POST(Api.CREATE_TABLE)
    Observable<BaseResponse> createTable(@Body PreciptationSampForm preciptationSampForm);

    @GET(Api.UPDATE_TABLE)
    Observable<BaseResponse> updateTable(@Body PreciptationSampForm preciptationSampForm);

    @GET(Api.DELETE_TABLE)
    Observable<BaseResponse> deleteTable(@Query("tableId") String tableId);

    @GET(Api.GET_SAMPLINGSTANTD)
    Observable<BaseResponse<List<SamplingStantd>>> getSamplingStantd();

    @GET(Api.GET_FORMSELECT)
    Observable<BaseResponse<List<Form>>> getFormSelect();

    @GET(Api.GET_SAMPLING_USER)
    Observable<BaseResponse<List<SamplingUser>>> getsamplingUser();

    @PUT(Api.SUBMIT_SAMPLING)
    Observable<BaseResponse> putSubmitSampling();

    @GET(Api.GET_SAMPLING)
    Observable<BaseResponse<List<Sampling>>> getSampling(@Query("ProjectId") List<String> projectIds);

    @GET(Api.GET_SAMPLING_BY_ID)
    Observable<BaseResponse<List<Sampling>>> getSamplingByID(@Query("SamplingID") String samplingID);

    //*******************文件******************
    @Multipart
    @POST(Api.UPLOAD_FILE)
    Observable<Response<UploadFileResponse<List<FileInfoData>>>> uploadFile(@Part() List<MultipartBody.Part> parts, @PartMap Map<String, RequestBody> params);

    @DELETE(Api.DELETE_FILE)
    Observable<BaseResponse> deleteFile();


    //*******************知识库******************

    /**
     * 获取hash密码
     *
     * @param params 请求参数map
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + Api.TEMPORARY_SERVER_IP})
    @GET(Api.REPOSITORY)
    Observable<ResponseBody> getRepositoryHashCode(@QueryMap Map<String, String> params);

    /**
     * 获取组结构
     *
     * @param params 请求参数map
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + Api.TEMPORARY_SERVER_IP})
    @GET(Api.REPOSITORY)
    Observable<ResponseBody> getRepositoryGroups(@QueryMap Map<String, String> params);

    /**
     * 获取文件目录
     *
     * @param params 请求参数map
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + Api.TEMPORARY_SERVER_IP})
    @GET(Api.REPOSITORY)
    Observable<ResponseBody> getRepositoryFolder(@QueryMap Map<String, String> params);

    /**
     * 获取文件
     *
     * @param params 请求参数map
     * @return
     */
    @Headers({DOMAIN_NAME_HEADER + Api.TEMPORARY_SERVER_IP})
    @GET(Api.REPOSITORY)
    Observable<ResponseBody> getRepositoryFile(@QueryMap Map<String, String> params);

    /**
     * 下载文件
     *
     * @param params 请求参数map
     * @return
     */
    @GET(Api.REPOSITORY)
    Observable<ResponseBody> downloadRepositoryFile(@QueryMap Map<String, String> params);


    //*******************消息******************

    /**
     * 获取消息
     *
     * @return
     */
    @GET(Api.GET_MSG)
    Observable<BaseResponse<List<Msg>>> getMsgs();

    /**
     * 批量阅读消息
     *
     * @return
     */
    @Headers({"Content-Type: application/json"})
    @PUT(Api.PUT_READ_MSG)
    Observable<BaseResponse> putReadMsg(@Body List<String> messageIds);

    /**
     * 上传点位
     *
     * @param gpsList
     * @return
     */
    @POST(Api.GAO_DE_COORDINATES)
    Observable<BaseResponse> postGaoDeCoordinates(@Body List<Gps> gpsList);

}
