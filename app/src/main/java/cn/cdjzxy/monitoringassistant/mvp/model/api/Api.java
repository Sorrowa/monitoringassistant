package cn.cdjzxy.monitoringassistant.mvp.model.api;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;

/**
 * API接口
 */

public interface Api {

    //*******************账户******************
    String LOGIN      = "api/Account/Login";//登录 POST
    String LOGOUT     = "api/Account/Logout";//退出登录 POST
    String PWD_MODIFY = "api/Account/PutUserPwd";//修改密码

    //*******************知识库******************
    String REPOSITORY = "ufInterface";//知识库 GET

    //*******************扫码******************
    String QR_INFO = "Common/GetQrModelInfo";//二维码 GET

    //*******************基础数据******************
    String GET_DEVICES                = "api/BaseData/Devices";//获取设备信息 GET
    String GET_METHODS                = "api/BaseData/Methods";//获取方法信息 GET
    String GET_MONITEMS               = "api/BaseData/MonItems";//获取监测项目 GET
    String GET_TAGS                   = "api/BaseData/GetTags";//获取要素分类 GET
    String GET_MONITEMTAG_RELATION    = "api/BaseData/GetMonItemTagRelation";// 获取项目要素关系 GET
    String GET_METHODTAG_RELATION     = "api/BaseData/GetMethodTagRelation";//获取方法要素关系 GET
    String GET_MONITEMMETHOD_RELATION = "api/BaseData/GetMonItemMethodRelation";//获取项目方法关系 GET
    String GET_METHODDEV_RELATION     = "api/BaseData/GetMethodDevRelation";//获取方法设备关系 GET
    String GET_RIGHTS                 = "api/BaseData/GetRight";//获取权限 GET
    String GET_ENVIRPOINT             = "api/BaseData/GetEnvirPoint";//获取环境质量点位 GET
    String GET_ENTERPOINT             = "api/BaseData/GetEnterRelatePoint";//获取企业点位 GET
    String GET_ENTERPRISE             = "api/BaseData/GetEnterprise";//获取企业 GET
    String GET_DIC                    = "api/BaseData/GetDic";//获取字典  GET
    String GET_WEATHER                = "api/BaseData/GetWeather";//获取天气  GET
    String GET_USER                   = "api/BaseData/GetUser";//获取采样用户  GET

    //*******************文件******************
    String UPLOAD_FILE = "api/File/Upload";//上传文件 POST
    String DELETE_FILE = "api/File/Delete";//删除文件 DELETE

    //*******************任务******************
    String GET_MY_TASKS        = "api/Project/GetMyPendingTasks";//获取跟我相关待采样任务 GET
    String GET_ALL_TASKS       = "api/Project/GetAllPendingTasks";///获取所有任务 GET
    String PUT_SAMPLING_FINISH = "api/Project/PutSamplingFinish";//采样任务完结操作 PUT
    String GET_SAMPLE_STORAGE  = "api/Project/GetSampleStorageProject";//获取流转任务 GET
    String PUT_PROJECT_CONTENT = "api/Project/PutProjectContent";// 修改任务方案 PUT


    //*******************流转******************
    String GET_SAMPLE_STORAGE_LIST = "api/SampleStorage/GetSampleStorageList";//获取流转单清单
    String PUT_SAMPLE_STORAGE      = "api/SampleStorage/PutVerifySampleStorage";//批量审核流转单


    //*******************采样******************
    String GET_TABLES         = "api/Sampling/GetTableList";//获取采样单清单 GET
    String GET_SAMPLING       = "api/Sampling/GetSampling";///获取所有采样单信息(支持批量)
    String CREATE_TABLE       = "api/Sampling/PostTable";//创建采样单 POST
    String UPDATE_TABLE       = "api/Sampling/PutTable";//更新采样单 Put
    String DELETE_TABLE       = "api/Sampling/DeleteTable";//删除采样单 DELETE
    String GET_SAMPLINGSTANTD = "api/Sampling/GetSamplingStantd";//获取采样规范 GET
    String GET_FORMSELECT     = "api/Sampling/GetFormSelect";//获取表单分类 GET
    String GET_SAMPLING_USER  = "api/Sampling/GetsamplingUser";//获取采样人员 GET
    String SUBMIT_SAMPLING    = "api/Sampling/PutSubmitSampling";//批量提交采样单 PUT
    String VERIFY_SAMPLING    = "api/Sampling/PutVerifySampling";//批量审核采样单 PUT


    //*******************消息******************
    String GET_MSG      = "api/Massage/GetMessage";//获取全部消息
    String PUT_READ_MSG = "api/Massage/PutReadMessage";//批量阅读消息

    String TEMPORARY_SERVER_IP = "TEMPORARY_SERVER_IP";//临时IP
    String LOGIN_RESP_WEBURL = "LOGIN_RESP_WEBURL";//登录返回的webUrl
}


