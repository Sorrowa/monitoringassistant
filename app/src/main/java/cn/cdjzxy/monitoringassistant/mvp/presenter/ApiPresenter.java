package cn.cdjzxy.monitoringassistant.mvp.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.util.Log;

import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.BasePresenter;
import com.wonders.health.lib.base.mvp.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import cn.cdjzxy.monitoringassistant.app.rx.RxObserver;
import cn.cdjzxy.monitoringassistant.app.rx.RxUtils;
import cn.cdjzxy.monitoringassistant.mvp.model.ApiRepository;
import cn.cdjzxy.monitoringassistant.mvp.model.api.Api;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Weather;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.qr.QrMoreInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormFlow;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.FileInfoData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.ProjectPlan;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.MainActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.Constants;
import cn.cdjzxy.monitoringassistant.utils.HawkUtil;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

/**
 * App相关的数据操作Presenter
 */
public class ApiPresenter extends BasePresenter<ApiRepository> {

    public static final int PROGRESS = 100 / 20;

    public ApiPresenter(AppComponent appComponent) {
        super(appComponent.repositoryManager().createRepository(ApiRepository.class));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        Timber.d("onCreate");
    }


    /**
     * 登录
     *
     * @param msg
     * @param name
     * @param pwd
     */
    public void login(final Message msg, String name, String pwd, Context context) {
        if (NetworkUtil.isNetworkAvailable(context)) {
            loginOnline(msg, name, pwd);
        } else {
            loginLocal(msg, name, pwd);
        }
    }

    /**
     * 退出登录
     *
     * @param msg
     */
    public void logout(final Message msg) {
        mModel.logout()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        UserInfoHelper.get().saveUserLoginStatee(false);
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * 修改密码
     *
     * @param msg
     */
    public void modifyPwd(final Message msg, String oldPwd, String newPwd) {
        mModel.modifyPwd(oldPwd, newPwd)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * 在线登录
     *
     * @param msg
     * @param name
     * @param pwd
     */
    private void loginOnline(final Message msg, String name, final String pwd) {
        Map<String, String> params = new HashMap<>();
        params.put("code", "0");
        params.put("loginId", name);
        params.put("pwd", pwd);
        mModel.login(params)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<UserInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<UserInfo> response) {
                        UserInfo userInfo = response.getData();
                        userInfo.setPwd(pwd);
                        DBHelper.get().getUserInfoDao().deleteAll();
                        DBHelper.get().getUserInfoDao().insert(userInfo);
                        UserInfoHelper.get().saveUserLoginStatee(true);
                        UserInfoHelper.get().saveUserInfo(userInfo);
                        HawkUtil.putBoolean("isUpdated", false);

                        //重新设置weburl
                        resetWebUrl(userInfo);

                        msg.what = Message.RESULT_OK;
                        msg.obj = response.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * 本地登录
     *
     * @param msg
     * @param name
     * @param pwd
     */
    private void loginLocal(final Message msg, String name, String pwd) {
        UserInfo userInfo = UserInfoHelper.get().getUser();
        if (CheckUtil.isNull(userInfo) || CheckUtil.isEmpty(userInfo.getWorkNo())) {
            msg.getTarget().showMessage("用户信息不存在，请先联网登录更新数据");
            msg.what = Message.RESULT_FAILURE;
            msg.handleMessageToTarget();
        } else if (!name.equals(userInfo.getWorkNo())) {
            msg.getTarget().showMessage("输入的用户名错误,请重新输入");
            msg.what = Message.RESULT_FAILURE;
            msg.handleMessageToTarget();
        } else if (!pwd.equals(userInfo.getPwd())) {
            msg.getTarget().showMessage("输入的密码错误,请重新输入");
            msg.what = Message.RESULT_FAILURE;
            msg.handleMessageToTarget();
        } else {
            UserInfoHelper.get().saveUserLoginStatee(true);

            //重新设置weburl
            resetWebUrl(userInfo);

            msg.what = Message.RESULT_OK;
            msg.obj = userInfo;
            msg.handleMessageToTarget();
        }
    }

    /**
     * 获取二维码结果
     *
     * @param msg
     * @param qrCode
     */
    public void getQrInfo(final Message msg, String qrCode) {
        mModel.getQrInfo(qrCode)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<QrMoreInfo>>() {
                    @Override
                    public void onSuccess(BaseResponse<QrMoreInfo> qrMoreInfoBaseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = qrMoreInfoBaseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getDevices(final Message msg) {
        mModel.getDevices()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Devices>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Devices>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getDevicesDao().deleteAll();
                            DBHelper.get().getDevicesDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMethods(final Message msg) {
        mModel.getMethods()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Methods>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Methods>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMethodsDao().deleteAll();
                            DBHelper.get().getMethodsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMonItems(final Message msg) {
        mModel.getMonItems()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItems>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItems>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMonItemsDao().deleteAll();
                            DBHelper.get().getMonItemsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getTags(final Message msg) {
        mModel.getTags()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Tags>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Tags>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getTagsDao().deleteAll();
                            DBHelper.get().getTagsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMonItemTagRelation(final Message msg) {
        mModel.getMonItemTagRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItemTagRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItemTagRelation>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMonItemTagRelationDao().deleteAll();
                            DBHelper.get().getMonItemTagRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMethodTagRelation(final Message msg) {
        mModel.getMethodTagRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MethodTagRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MethodTagRelation>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMethodTagRelationDao().deleteAll();
                            DBHelper.get().getMethodTagRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMonItemMethodRelation(final Message msg) {
        mModel.getMonItemMethodRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItemMethodRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItemMethodRelation>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMonItemMethodRelationDao().deleteAll();
                            DBHelper.get().getMonItemMethodRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getMethodDevRelation(final Message msg) {
        mModel.getMethodDevRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MethodDevRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MethodDevRelation>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMethodDevRelationDao().deleteAll();
                            DBHelper.get().getMethodDevRelationDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getRight(final Message msg) {
        mModel.getRight()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Rights>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Rights>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getRightsDao().deleteAll();
                            DBHelper.get().getRightsDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getEnvirPoint(final Message msg) {
        mModel.getEnvirPoint()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<EnvirPoint>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<EnvirPoint>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getEnvirPointDao().deleteAll();
                            DBHelper.get().getEnvirPointDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getEnterRelatePoint(final Message msg) {
        mModel.getEnterRelatePoint()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<EnterRelatePoint>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<EnterRelatePoint>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getEnterRelatePointDao().deleteAll();
                            DBHelper.get().getEnterRelatePointDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getEnterprise(final Message msg) {
        mModel.getEnterprise()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Enterprise>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Enterprise>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getEnterpriseDao().deleteAll();
                            DBHelper.get().getEnterpriseDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getDic(final Message msg, int type) {
        mModel.getDic(type)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Dic>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Dic>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getDicDao().deleteAll();
                            DBHelper.get().getDicDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getWeather(final Message msg) {
        mModel.getWeather()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<String>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<String>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            Weather weather = new Weather();
                            weather.setWeathers(baseResponse.getData());
                            DBHelper.get().getWeatherDao().deleteAll();
                            DBHelper.get().getWeatherDao().insert(weather);

                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getUser(final Message msg) {
        mModel.getUser()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<User>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<User>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getUserDao().deleteAll();
                            DBHelper.get().getUserDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getUnit(final Message msg) {
        mModel.getUnit()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Unit>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Unit>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getUnitDao().deleteAll();
                            DBHelper.get().getUnitDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getMsgs(final Message msg) {
        mModel.getMsgs()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Msg>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Msg>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getMsgDao().deleteAll();
                            DBHelper.get().getMsgDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void putReadMsg(final Message msg, List<String> messageIds) {
        mModel.putReadMsg(messageIds)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getAllTasks(final Message msg) {
        mModel.getAllTasks()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Project>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Project>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getProjectDao().deleteAll();
                            DBHelper.get().getProjectDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getMyTasks(final Message msg) {
        mModel.getMyTasks()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Project>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Project>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            List<Project> projects = baseResponse.getData();
                            DBHelper.get().getProjectDetialDao().deleteAll();
                            DBHelper.get().getProjectDao().deleteAll();
                            for (Project project : projects) {
                                List<ProjectDetial> projectDetials = project.getProjectDetials();
                                if (!CheckUtil.isEmpty(projectDetials)) {
                                    DBHelper.get().getProjectDetialDao().insertInTx(projectDetials);
                                }
                            }
                            DBHelper.get().getProjectDao().insertInTx(baseResponse.getData());
                        }

                        List<Project> projects = baseResponse.getData();
                        List<String> taskIds = new ArrayList<>();

                        for (Project project : projects) {
                            taskIds.add(project.getId());
                        }

                        msg.what = MainActivity.TYPE_TASK;
                        msg.obj = taskIds;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getSamplingStantd(final Message msg) {
        mModel.getSamplingStantd()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<SamplingStantd>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<SamplingStantd>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getSamplingStantdDao().deleteAll();
                            DBHelper.get().getSamplingStantdDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    /**
     * @param msg
     */
    public void getFormSelect(final Message msg) {
        mModel.getFormSelect()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Form>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Form>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            List<Form> forms = baseResponse.getData();
                            DBHelper.get().getFormFlowDao().deleteAll();
                            DBHelper.get().getFormSelectDao().deleteAll();
                            DBHelper.get().getFormDao().deleteAll();
                            for (Form form : forms) {
                                List<FormSelect> formSelects = form.getFormSelectList();
                                if (!CheckUtil.isEmpty(formSelects)) {
                                    for (FormSelect formSelect : formSelects) {
                                        List<FormFlow> formFlows = formSelect.getFormFlows();
                                        if (!CheckUtil.isEmpty(formFlows)) {
                                            DBHelper.get().getFormFlowDao().insertInTx(formFlows);
                                        }
                                    }
                                    DBHelper.get().getFormSelectDao().insertInTx(formSelects);
                                }
                            }
                            DBHelper.get().getFormDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getSampling(final Message msg, List<String> projectIds) {
        mModel.getSampling(projectIds)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Sampling>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Sampling>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            List<Sampling> samplings = baseResponse.getData();
                            if (!CheckUtil.isEmpty(samplings)) {

                                DBHelper.get().getSamplingFormStandDao().deleteAll();

                                for (Sampling sampling : samplings) {

                                    DBHelper.get().getSamplingDao().delete(sampling);

                                    List<SamplingFormStand> samplingFormStands = sampling.getSamplingFormStandResults();
                                    if (!CheckUtil.isEmpty(samplingFormStands)) {
                                        DBHelper.get().getSamplingFormStandDao().insertInTx(samplingFormStands);
                                    }

                                    List<SamplingDetail> samplingDetails = sampling.getSamplingDetailResults();

                                    if (!CheckUtil.isEmpty(samplingDetails)) {
                                        for (SamplingDetail samplingDetail : samplingDetails) {
                                            DBHelper.get().getSamplingDetailDao().delete(samplingDetail);
                                        }
                                        DBHelper.get().getSamplingDetailDao().insertInTx(samplingDetails);
                                    }

                                    List<SamplingContent> samplingContents = sampling.getSamplingContentResults();
                                    if (!CheckUtil.isEmpty(samplingContents)) {
                                        for (SamplingContent samplingContent : samplingContents) {
                                            samplingContent.setId(UUID.randomUUID().toString());
                                            DBHelper.get().getSamplingContentDao().delete(samplingContent);
                                        }
                                        DBHelper.get().getSamplingContentDao().insertInTx(samplingContents);
                                    }

                                    sampling.setIsCanEdit((sampling.getStatus() == 0 || sampling.getStatus() == 4 || sampling.getStatus() == 9)
                                            && sampling.getSamplingUserId().contains(UserInfoHelper.get().getUserInfo().getId()) ? true : false);
                                    sampling.setIsLocal(false);
                                    DBHelper.get().getSamplingDao().insert(sampling);
                                }
                            }
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = PROGRESS;
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }


    public void putSamplingFinish(final Message msg, String projectId, String comment) {
        Map<String, String> params = new HashMap<>();
        params.put("projectID", projectId);
        params.put("finishComment", comment);
        mModel.putSamplingFinish(params)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void putProjectContent(final Message msg, ProjectPlan projectPlan) {
        mModel.putProjectContent(projectPlan)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void createTable(final Message msg, PreciptationSampForm preciptationSampForm) {
        mModel.createTable(preciptationSampForm)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Constants.NET_RESPONSE_CODE_259;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        if (responseCode == Constants.NET_RESPONSE_SAMPLING_DIFFER) {
                            msg.what = Constants.NET_RESPONSE_SAMPLING_DIFFER;
                        } else {
                            msg.what = Message.RESULT_FAILURE;
                        }

                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void updateTable(final Message msg, PreciptationSampForm preciptationSampForm) {
        mModel.updateTable(preciptationSampForm)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void deleteTable(final Message msg, String tableId) {
        mModel.deleteTable(tableId)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse>() {
                    @Override
                    public void onSuccess(BaseResponse baseResponse) {
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getMessage();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    public void uploadFile(final Message msg, List<MultipartBody.Part> parts, Map<String, RequestBody> params) {
        mModel.uploadFile(parts, params)
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<UploadFileResponse<List<FileInfoData>>>() {
                    @Override
                    public void onSuccess(UploadFileResponse<List<FileInfoData>> baseResponse) {
                        msg.what = Message.RESULT_OK;
//                        msg.obj = baseResponse.getMessage();
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message, int code) {
                        msg.getTarget().showMessage(message);
                        msg.what = Message.RESULT_FAILURE;
                        msg.handleMessageToTarget();
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 重新设置WebUrl
     *
     * @param userInfo
     */
    public void resetWebUrl(UserInfo userInfo) {
        if (CheckUtil.isNull((userInfo))) {
            userInfo = UserInfoHelper.get().getUserInfo();
        }

        //动态设置weburl
        if (!CheckUtil.isEmpty(userInfo.getWebUrl())) {
            RetrofitUrlManager.getInstance().putDomain(Api.LOGIN_RESP_WEBURL, userInfo.getWebUrl());
        }
    }

}
