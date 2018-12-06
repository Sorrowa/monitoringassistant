package cn.cdjzxy.monitoringassistant.mvp.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;

import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.BasePresenter;
import com.wonders.health.lib.base.mvp.Message;

import java.util.HashMap;
import java.util.Map;

import cn.cdjzxy.monitoringassistant.app.rx.RxObserver;
import cn.cdjzxy.monitoringassistant.app.rx.RxUtils;
import cn.cdjzxy.monitoringassistant.mvp.model.AppRepository;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.BaseResponse;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.QrMoreInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.UserInfoDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;
import timber.log.Timber;

/**
 * App相关的数据操作Presenter
 */

public class AppPresenter extends BasePresenter<AppRepository> {

    public AppPresenter(AppComponent appComponent) {
        super(appComponent.repositoryManager().createRepository(AppRepository.class));
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
                        UserInfoHelper.get().saveUserName(userInfo.getWorkNo());
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
        UserInfo userInfo = DBHelper.get().getUserInfoDao().queryBuilder().where(UserInfoDao.Properties.WorkNo.eq(name)).unique();
        if (CheckUtil.isNull(userInfo)) {
            msg.getTarget().showMessage("用户不存在");
        } else {
            UserInfoHelper.get().saveUserName(userInfo.getWorkNo());
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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
