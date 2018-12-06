package cn.cdjzxy.monitoringassistant.mvp.model.api;

/**
 * API接口
 */

public interface Api {

    //*******************账户******************
    String LOGIN  = "api/Account/Login";//登录 POST
    String LOGOUT = "api/Account/Logout";//退出登录 POST

    //*******************知识库******************
    String REPOSITORY = "ufInterface";//知识库 GET

    //*******************扫码******************
    String QR_INFO = "Common/GetQrModelInfo";//二维码 GET

    //*******************基础数据******************
    String DEVICES                    = "Devices";//获取设备信息 GET
    String METHODS                    = "Methods";//获取方法信息 GET
    String MONITEMS                   = "MonItems";//获取监测项目 GET
    String GET_TAGS                   = "GetTags";//获取要素分类 GET
    String GET_MONITEMTAG_RELATION    = "GetMonItemTagRelation";// 获取项目要素关系 GET
    String GET_METHODTAG_RELATION     = "GetMethodTagRelation";//获取方法要素关系 GET
    String GET_MONITEMMETHOD_RELATION = "GetMonItemMethodRelation";//获取项目方法关系 GET
    String GET_METHODDEV_RELATION     = "GetMethodDevRelation";//获取方法设备关系 GET
    String GET_RIGHTS                 = "GetRight";//获取权限 GET

    //*******************文件******************
    String UPLOAD_FILE  = "api/File/Upload";//上传文件 POST
    String DELETE_FILE = "api/File/Delete";//删除文件 DELETE

    //*******************任务******************
    String GET_PROJECTS   = "api/Sampling/GetProjects";//获取所有立项信息 GET
    String GET_ENVIRPOINT = "api/Sampling/GetEnvirPoint";//获取环境质量点位 GET
    String GET_ENTERPOINT = "api/Sampling/EnterRelatePoint";//获取企业点位 GET
    String GET_ENTERPRISE = "api/Sampling/GetEnterprise";//获取企业 GET

    //*******************采样******************
    String GET_MYTASKS        = "api/Sampling/GetMyPendingTasks";//获取跟我相关待采样任务 GET
    String GET_ALLTASKS       = "api/Sampling/GetAllPendingTasks";//获取所有任务 GET
    String GET_TABLES         = "api/Sampling/GetTableList";//获取采样单清单 GET
    String GET_TABLE          = "api/Sampling/GetTable";//获取采样单信息 GET
    String CREATE_TABLE       = "api/Sampling/CreateTable";//创建采样单 POST
    String UPDATE_TABLE       = "api/Sampling/UpdateTable";//更新采样单 GET
    String DELETE_TABLE       = "api/Sampling/DeleteTable";//删除采样单 DELETE
    String GET_SAMPLINGSTANTD = "api/Sampling/GetSamplingStantd";//获取采样规范 GET
    String GET_FORMSELECT     = "api/Sampling/GetFormSelect";//获取表单分类 GET
    String GET_SAMPLING_USER  = "api/Sampling/GetsamplingUser";//获取采样人员 GET
    String SUBMIT_SAMPLING    = "api/Sampling/PutSubmitSampling";//批量提交采样单 PUT

}
