package cn.cdjzxy.monitoringassistant.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.simple.eventbus.EventBus;

import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.utils.RxNetTool;


public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkChangeReceiver";
    private NetworkChangeReceiver receiver;

    /**
     * 注册监听网络状态的广播
     *
     * @param context
     * @return
     */
    public static NetworkChangeReceiver initRegisterReceiverNetWork(Context context) {
        // 注册监听网络状态的服务

        NetworkChangeReceiver mReceiverNetWork = new NetworkChangeReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(mReceiverNetWork, mFilter);
        return mReceiverNetWork;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        EventBus.getDefault().post(RxNetTool.getNetWorkType(context), EventBusTags.TAG_NETWORK_CHANGE);
    }



}
