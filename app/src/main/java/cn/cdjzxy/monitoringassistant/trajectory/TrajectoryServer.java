package cn.cdjzxy.monitoringassistant.trajectory;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.track.ClearCacheTrackRequest;
import com.baidu.trace.api.track.ClearCacheTrackResponse;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnCustomAttributeListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.Constant;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.MainActivity;
import cn.cdjzxy.monitoringassistant.trajectory.receiver.TrackReceiver;
import cn.cdjzxy.monitoringassistant.utils.DateUtil;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;


/**
 * gps轨迹上传服务
 */
public class TrajectoryServer extends Service {
    private static final String TAG = "TrajectoryServer+鹰眼轨迹";
    private PowerManager powerManager;

    private boolean isRealTimeRunning = true;//是否开启定位服务

    private PowerManager.WakeLock wakeLock;

    private TrackReceiver trackReceiver;
    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener;
    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private OnTrackListener trackListener;
    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener;
    /**
     * 实时定位任务
     */
    private RealTimeHandler realTimeHandler = new RealTimeHandler();

    private RealTimeLocRunnable realTimeLocRunnable;

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    // 打包回传周期(单位:秒) 频率可设置范围为：2~300秒
    private static final int packInterval = 60;
    // 定位周期(单位:秒) 频率可设置范围为：2~300秒
    private static final int gatherInterval = 60;

    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    private LocRequest locRequest = null;

    private Notification notification = null;

    public SharedPreferences trackConf = null;

    /**
     * 轨迹客户端
     */
    public LBSTraceClient mClient = null;

    /**
     * 轨迹服务
     */
    public Trace mTrace = null;

    /**
     * 轨迹服务ID
     */
    public long serviceId = 211193;

    /**
     * 轨迹服务名称
     */
    public String name;


    public boolean isRegisterReceiver = false;

    /**
     * 服务是否开启标识
     */
    public boolean isTraceStarted = false;

    /**
     * 采集是否开启标识
     */
    public boolean isGatherStarted = false;

    public static int screenWidth = 0;

    public static int screenHeight = 0;

    public boolean trajectoryServerIsStart = false;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        // 初始化轨迹服务
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mClient = new LBSTraceClient(getApplicationContext());
        UserInfo userInfo = UserInfoHelper.get().getUserInfo();
        name = userInfo.getName() + "-" + userInfo.getWorkNo();
        mTrace = new Trace(serviceId, name);
        mTrace.setNotification(notification);
        trackConf = getSharedPreferences("track_conf", MODE_PRIVATE);
        locRequest = new LocRequest(serviceId);
        mClient.setOnCustomAttributeListener(new OnCustomAttributeListener() {
            @Override
            public Map<String, String> onTrackAttributeCallback() {
                Map<String, String> map = new HashMap<>();
                map.put("key1", "value1");
                map.put("key2", "value2");
                return map;
            }

            @Override
            public Map<String, String> onTrackAttributeCallback(long locTime) {
                System.out.println("onTrackAttributeCallback, locTime : " + locTime);
                Map<String, String> map = new HashMap<>();
                map.put("key1", "value1");
                map.put("key2", "value2");
                return map;
            }
        });
    }


    private void initListener() {
        /**
         * 轨迹监听器(用于接收纠偏后实时位置回调)
         */
        trackListener = new OnTrackListener() {

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                Log.i(TAG, "onLatestPointCallback: " + response.toString());
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    return;
                }
                Log.i(TAG, "onLatestPointCallback: " + response.toString());
            }
        };
        /**
         * Entity监听器(用于接收实时定位回调)
         */
        entityListener = new OnEntityListener() {

            @Override
            public void onReceiveLocation(TraceLocation location) {
                Log.i(TAG, "onReceiveLocation: " + location.toString());
            }

        };

        /**
         * 轨迹服务监听器
         */
        traceListener = new OnTraceListener() {

            /**
             * 绑定服务回调接口
             * @param errorNo  状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>1：失败</pre>
             */
            @Override
            public void onBindServiceCallback(int errorNo, String message) {
                Log.i(TAG, "onBindServiceCallback: " +
                        String.format("onBindServiceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 开启服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>10000：请求发送失败</pre>
             *                <pre>10001：服务开启失败</pre>
             *                <pre>10002：参数错误</pre>
             *                <pre>10003：网络连接失败</pre>
             *                <pre>10004：网络未开启</pre>
             *                <pre>10005：服务正在开启</pre>
             *                <pre>10006：服务已开启</pre>
             */
            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    isTraceStarted = true;
                    registerReceiver();
                    mClient.startGather(traceListener);//开启采集服务
                }
                Log.e(TAG, "onStartTraceCallback: " +
                        String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
                Log.i(TAG, "onStartTraceCallback: " +
                        String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 停止服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>11000：请求发送失败</pre>
             *                <pre>11001：服务停止失败</pre>
             *                <pre>11002：服务未开启</pre>
             *                <pre>11003：服务正在停止</pre>
             */
            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    isTraceStarted = false;
                    isGatherStarted = false;
                    // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                    unregisterPowerReceiver();
                    mClient.stopGather(traceListener);
                }
                Log.i(TAG, "onStopTraceCallback: " +
                        String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message));
                Log.e(TAG, "onStopTraceCallback: " +
                        String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 开启采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>12000：请求发送失败</pre>
             *                <pre>12001：采集开启失败</pre>
             *                <pre>12002：服务未开启</pre>
             */
            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    isGatherStarted = true;
                }
                Log.i(TAG, "onStartGatherCallback: " +
                        String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 停止采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>13000：请求发送失败</pre>
             *                <pre>13001：采集停止失败</pre>
             *                <pre>13002：服务未开启</pre>
             */
            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    isGatherStarted = false;

                }
                Log.i(TAG, "onStopGatherCallback: " +
                        String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message));

            }

            /**
             * 推送消息回调接口
             *
             * @param messageType 状态码
             * @param pushMessage 消息
             *                  <p>
             *                  <pre>0x01：配置下发</pre>
             *                  <pre>0x02：语音消息</pre>
             *                  <pre>0x03：服务端围栏报警消息</pre>
             *                  <pre>0x04：本地围栏报警消息</pre>
             *                  <pre>0x05~0x40：系统预留</pre>
             *                  <pre>0x41~0xFF：开发者自定义</pre>
             */
            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {
                if (messageType < 0x03 || messageType > 0x04) {
                    Log.i(TAG, "onPushCallback: " + pushMessage.getMessage());
                    return;
                }
                FenceAlarmPushInfo alarmPushInfo = pushMessage.getFenceAlarmPushInfo();
                if (null == alarmPushInfo) {
                    Log.i(TAG, "onPushCallback: " +
                            String.format("onPushCallback, messageType:%d, messageContent:%s ", messageType,
                                    pushMessage));

                    return;
                }

                StringBuffer alarmInfo = new StringBuffer();
                alarmInfo.append("您于")
                        .append(DateUtil.getTime())
                        .append(alarmPushInfo.getMonitoredAction() == MonitoredAction.enter ? "进入" : "离开")
                        .append(messageType == 0x03 ? "云端" : "本地")
                        .append("围栏：").append(alarmPushInfo.getFenceName());
                Log.i(TAG, "onPushCallback: " + alarmInfo.toString());
                Log.e(TAG, "onPushCallback: " + alarmInfo.toString());

            }

            @Override
            public void onInitBOSCallback(int errorNo, String message) {
                Log.i(TAG, "onInitBOSCallback: " +
                        String.format("onInitBOSCallback, errorNo:%d, message:%s ", errorNo, message));
                Log.e(TAG, "onInitBOSCallback: " +
                        String.format("onInitBOSCallback, errorNo:%d, message:%s ", errorNo, message));
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initTrack();
        trajectoryServerIsStart = true;
        Log.e(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     *
     */
    private void initTrack() {
        if (mClient == null || !isTraceStarted) {
            mClient.setLocationMode(LocationMode.High_Accuracy);//设置定位模式
            initListener();
            startRealTimeLoc(packInterval);
            powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            initNotification();
            mClient.setInterval(gatherInterval, packInterval);//设置采集和打包位置数据的时间间隔
            mClient.startTrace(mTrace, traceListener);//启动鹰眼服务
            Log.e(TAG, "initTrackinitTrack: id：" + mTrace.getServiceId() + "\n name:" + mTrace.getEntityName());
            Log.i(TAG, "onCreate: id：" + mTrace.getServiceId() + "\n name:" + mTrace.getEntityName());
        }
    }

    /**
     * 开启定位任务
     *
     * @param interval
     */
    public void startRealTimeLoc(int interval) {
        isRealTimeRunning = true;
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
    }

    public void stopRealTimeLoc() {
        isRealTimeRunning = false;
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
        }
    }

    /**
     * 实时定位任务
     *
     * @author baidu
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            if (isRealTimeRunning) {
                getCurrentLocation(entityListener, trackListener);
                realTimeHandler.postDelayed(this, interval * 1000);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
        mClient.stopTrace(mTrace, traceListener);//停止轨迹服务  同时停止采集
        stopRealTimeLoc();
        trajectoryServerIsStart = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 注册广播（电源锁、GPS状态）
     */
    private void registerReceiver() {
        if (isRegisterReceiver) {
            return;
        }

        if (null == wakeLock) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");
        }
        if (null == trackReceiver) {
            trackReceiver = new TrackReceiver(wakeLock);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(StatusCodes.GPS_STATUS_ACTION);
        registerReceiver(trackReceiver, filter);
        isRegisterReceiver = true;
    }

    private void unregisterPowerReceiver() {
        if (!isRegisterReceiver) {
            return;
        }
        if (null != trackReceiver) {
            unregisterReceiver(trackReceiver);
        }
        isRegisterReceiver = false;
    }

    public void getCurrentLocation(OnEntityListener entityListener, OnTrackListener trackListener) {
        // 网络连接正常，开启服务及采集，则查询纠偏后实时位置；否则进行实时定位
        if (NetworkUtil.isNetworkAvailable(this)
                && isTraceStarted
                && isGatherStarted) {
            LatestPointRequest request = new LatestPointRequest(getTag(),
                    serviceId, name);
            ProcessOption processOption = new ProcessOption();
            processOption.setNeedDenoise(true);
            processOption.setRadiusThreshold(100);
            request.setProcessOption(processOption);
            mClient.queryLatestPoint(request, trackListener);
        } else {
            Log.e(TAG, "getCurrentLocation: 定位" + locRequest.toString());
            mClient.queryRealTimeLoc(locRequest, entityListener);
        }
    }

    @TargetApi(16)
    private void initNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        Intent notificationIntent = new Intent(this, MainActivity.class);

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.mipmap.ic_launcher);

        // 设置PendingIntent
        builder.setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, 0))
                .setLargeIcon(icon)  // 设置下拉列表中的图标(大图标)
                .setContentTitle("嘉泽小助手") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("正在运行...") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
    }

    /**
     * 获取屏幕尺寸
     */
    private void getScreenSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
    }

    /**
     * 清除Trace状态：初始化app时，判断上次是正常停止服务还是强制杀死进程，根据trackConf中是否有is_trace_started字段进行判断。
     * <p>
     * 停止服务成功后，会将该字段清除；若未清除，表明为非正常停止服务。
     */
    private void clearTraceStatus() {
        if (trackConf.contains("is_trace_started") || trackConf.contains("is_gather_started")) {
            SharedPreferences.Editor editor = trackConf.edit();
            editor.remove("is_trace_started");
            editor.remove("is_gather_started");
            editor.apply();
        }
    }

    /**
     * 初始化请求公共参数
     *
     * @param request
     */
    public void initRequest(BaseRequest request) {
        request.setTag(getTag());
        request.setServiceId(serviceId);
    }

    /**
     * 获取请求标识
     *
     * @return
     */
    public int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }


    public void onClearCacheTrack() {
        ClearCacheTrackRequest request = new ClearCacheTrackRequest(getTag(),
                serviceId);
        mClient.clearCacheTrack(request, new OnTrackListener() {
            @Override
            public void onClearCacheTrackCallback(ClearCacheTrackResponse clearCacheTrackResponse) {
                Log.e(TAG, "onClearCacheTrackCallback: " + clearCacheTrackResponse.toString());
                super.onClearCacheTrackCallback(clearCacheTrackResponse);
            }
        });
    }
}
