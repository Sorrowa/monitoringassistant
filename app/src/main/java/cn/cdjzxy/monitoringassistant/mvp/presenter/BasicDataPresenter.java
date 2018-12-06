package cn.cdjzxy.monitoringassistant.mvp.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.BasePresenter;
import com.wonders.health.lib.base.mvp.Message;

import java.util.List;

import cn.cdjzxy.monitoringassistant.app.rx.RxObserver;
import cn.cdjzxy.monitoringassistant.app.rx.RxUtils;
import cn.cdjzxy.monitoringassistant.mvp.model.BasicDataRepository;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.BaseResponse;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MethodDevRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MethodTagRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemMethodRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItemTagRelation;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Rights;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import timber.log.Timber;

/**
 * 基础数据相关操作Presenter
 */

public class BasicDataPresenter extends BasePresenter<BasicDataRepository> {

    public BasicDataPresenter(AppComponent appComponent) {
        super(appComponent.repositoryManager().createRepository(BasicDataRepository.class));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        Timber.d("onCreate");
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
    public void GetMonItemTagRelation(final Message msg) {
        mModel.GetMonItemTagRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItemTagRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItemTagRelation>> baseResponse) {
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
    public void GetMethodTagRelation(final Message msg) {
        mModel.GetMethodTagRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MethodTagRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MethodTagRelation>> baseResponse) {
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
    public void GetMonItemMethodRelation(final Message msg) {
        mModel.GetMonItemMethodRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MonItemMethodRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MonItemMethodRelation>> baseResponse) {
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
    public void GetMethodDevRelation(final Message msg) {
        mModel.GetMethodDevRelation()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<MethodDevRelation>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<MethodDevRelation>> baseResponse) {
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
    public void GetRight(final Message msg) {
        mModel.GetRight()
                .compose(RxUtils.applySchedulers(this, msg.getTarget()))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<BaseResponse<List<Rights>>>() {
                    @Override
                    public void onSuccess(BaseResponse<List<Rights>> baseResponse) {
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
