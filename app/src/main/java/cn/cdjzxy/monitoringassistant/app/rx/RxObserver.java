package cn.cdjzxy.monitoringassistant.app.rx;

import android.net.ParseException;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import org.json.JSONException;
import org.simple.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.BaseResponse;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * 提前处理结果和错误信息
 */

public class RxObserver<T> implements Observer<T> {

    public static final int TYPE_TIME_OUT      = 0;//请求超时
    public static final int TYPE_NO_NETWORK    = 1;//网络错误
    public static final int TYPE_TOKEN_EXPIRED = 2;//Token错误
    public static final int TYPE_ERROR         = 3;//错误
    public static final int TYPE_EMPTY         = 4;//数据为空


    private RxCallBack<T> mRxCallBack;

    public RxObserver(RxCallBack<T> rxCallBack) {
        this.mRxCallBack = rxCallBack;
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if (t instanceof BaseResponse) {
            BaseResponse response = ((BaseResponse) t);
            if (response.getCode() > 0) {
                mRxCallBack.onSuccess(t);
            } else {
                mRxCallBack.onFailure(TYPE_ERROR, response.getMessage());
            }
        } else {
            mRxCallBack.onSuccess(t);
        }
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable t) {
        Timber.tag("Catch-Error").w(t.getMessage());
        String msg = "未知错误";
        int type = TYPE_ERROR;
        if (t instanceof UnknownHostException || t instanceof ConnectException) {
            msg = "网络连接异常，请稍后再试";
            type = TYPE_NO_NETWORK;
        } else if (t instanceof SocketTimeoutException) {
            msg = "请求网络超时";
            type = TYPE_TIME_OUT;
        } else if (t instanceof CompositeException) {
            CompositeException compositeException = (CompositeException) t;
            msg = convertCompositeExceptionStatusCode(compositeException);
        } else if (t instanceof HttpException) {
            HttpException httpException = (HttpException) t;
            msg = convertHttpExceptionStatusCode(httpException);
        } else if (t instanceof JsonParseException || t instanceof ParseException || t instanceof JSONException || t instanceof JsonIOException) {
            msg = "数据解析错误";
        }
        mRxCallBack.onFailure(type, msg);
    }

    private String convertCompositeExceptionStatusCode(CompositeException compositeException) {
        String msg = "未知错误";
        List<Throwable> throwables = compositeException.getExceptions();
        for (Throwable throwable : throwables) {
            if (throwable instanceof ConnectException) {
                msg = "网络连接异常，请稍后再试";
                break;
            }
        }
        return msg;
    }


    private String convertHttpExceptionStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() == 500) {
            msg = "服务器发生错误";
        } else if (httpException.code() == 401) {
            msg = "Token授权失效，请重新登录，在执行操作";
            EventBus.getDefault().post(true, EventBusTags.TAG_TOKEN_EXPIRE);
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在";
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝";
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面";
        } else {
            msg = httpException.message();
        }
        return msg;
    }

    public interface RxCallBack<T> {

        void onSuccess(T t);

        void onFailure(int Type, String message);

    }

}
