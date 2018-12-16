package cn.cdjzxy.monitoringassistant.mvp.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;

import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.BasePresenter;
import com.wonders.health.lib.base.mvp.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cdjzxy.monitoringassistant.app.rx.RxObserver;
import cn.cdjzxy.monitoringassistant.app.rx.RxUtils;
import cn.cdjzxy.monitoringassistant.mvp.model.ApiRepository;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.BaseResponse;
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
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Task;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.qr.QrMoreInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Form;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.UserInfoDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;
import timber.log.Timber;

/**
 * App相关的数据操作Presenter
 */

public class ApiPresenter extends BasePresenter<ApiRepository> {

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
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
    private void loginOnline(final Message msg, String name, String pwd) {
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
                        DBHelper.get().getUserInfoDao().deleteAll();
                        DBHelper.get().getUserInfoDao().insert(userInfo);
                        UserInfoHelper.get().saveUserLoginStatee(true);
                        msg.what = Message.RESULT_OK;
                        msg.obj = response.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
        if (CheckUtil.isNull(userInfo)) {
            msg.getTarget().showMessage("用户信息不存在，请联网登录");
        } else if (name.equals(userInfo.getWorkNo())) {
            msg.getTarget().showMessage("用户错误");
        } else if (pwd.equals(userInfo.getPwd())) {
            msg.getTarget().showMessage("密码错误");
        } else {
            UserInfoHelper.get().saveUserLoginStatee(true);
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
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getAllTasks(final Message msg) {
        mModel.getAllTasks()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Task>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Task>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getTaskDao().deleteAll();
                            DBHelper.get().getTaskDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
                    }
                }));
    }


    /**
     * @param msg
     */
    public void getMyTasks(final Message msg) {
        mModel.getMyTasks()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Task>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Task>> baseResponse) {
                        if (!CheckUtil.isNull(baseResponse) && !CheckUtil.isEmpty(baseResponse.getData())) {
                            DBHelper.get().getTaskDao().deleteAll();
                            DBHelper.get().getTaskDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
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
                            DBHelper.get().getFormDao().deleteAll();
                            DBHelper.get().getFormDao().insertInTx(baseResponse.getData());
                        }
                        msg.what = Message.RESULT_OK;
                        msg.obj = baseResponse.getData();
                        msg.handleMessageToTarget();
                    }

                    @Override
                    public void onFailure(int Type, String message) {
                        msg.getTarget().showMessage(message);
                    }
                }));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
