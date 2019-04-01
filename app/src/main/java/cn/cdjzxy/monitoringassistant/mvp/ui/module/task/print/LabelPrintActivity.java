package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.ui.view.title.TitleBarView;
import com.google.gson.reflect.TypeToken;
import com.micheal.print.manager.MyPrinterPlusConnManager;
import com.micheal.print.manager.MyPrinterPlusManager;
import com.micheal.print.thread.ThreadFactoryBuilder;
import com.micheal.print.thread.ThreadPool;
import com.micheal.print.utils.Utils;
import com.tools.command.EscCommand;
import com.tools.command.GpUtils;
import com.tools.command.LabelCommand;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.LabelInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SealInfo;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.LabelAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.PrintUtil;

import com.google.gson.Gson;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 标签打印
 */

public class LabelPrintActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.btnPrintSeal)
    Button btnPrintSeal;
    @BindView(R.id.btnPrintLabel)
    Button btnPrintLabel;
    @BindView(R.id.ivSelectAll)
    ImageView ivSelectAll;
    @BindView(R.id.tvSelectAll)
    TextView tvSelectAll;

    private TitleBarView titleBar;

    private List<LabelInfo> mDataList = new ArrayList<>();
    private LabelAdapter mLabelAdapter;
    private List<LabelInfo> mPrintList = new ArrayList<>();
    private int mPrintIndex = 0;
    private boolean isSelectAll = false;
    private SealInfo sealInfo = null;

    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    private static final int UPDATE_STATUS = 1;
    public static final String LABEL_JSON_DATA = "label_json_data";
    public static final String SEAL_JSON_DATA = "seal_json_data";
    public static int PrinterIndex = 0;
    public static boolean isConnect = false;
    public static final int REQUEST_CODE = 10010;
    private String deviceName;
    private MyPrinterPlusManager manager;

    @Override

    public void setTitleBar(TitleBarView titleBar) {
        this.titleBar = titleBar;
        titleBar.setTitleMainText("标签打印");
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LabelPrintActivity.this,
                        LabelPrintDeviceActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        updateConnectText();
    }

    private void updateConnectText() {
        titleBar.setRightText(isConnect ? "已连接" : "未连接");
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_label_print;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initLabelData();
        manager = MyPrinterPlusManager.getInstance(this);
        mLabelAdapter.notifyDataSetChanged();
        initBroadcast();

        //  initPermission();//获取定位权限
    }

    /**
     * 初始化广播
     */
    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙打开状态广播
        filter.addAction(MyPrinterPlusManager.BLUE_TOOTH_DEVICE_NO_SUPPORTED);//设备不支持蓝牙
        filter.addAction(MyPrinterPlusConnManager.ACTION_QUERY_PRINTER_STATE);
        registerReceiver(mBroadcastReceiver, filter);
    }

    /**
     * 初始化数据
     */
    private void initLabelData() {
        Gson gson = new Gson();
        //读取传过来的标签数据
        String labelStr = getIntent().getStringExtra(LABEL_JSON_DATA);
        if (!TextUtils.isEmpty(labelStr)) {
            //反序列化
            ArrayList<LabelInfo> labelList = gson.fromJson(labelStr, new TypeToken<ArrayList<LabelInfo>>() {
            }.getType());
            mDataList.addAll(labelList);
        }

        //读取传过来的封条数据
        String sealStr = getIntent().getStringExtra(SEAL_JSON_DATA);
        if (!TextUtils.isEmpty(sealStr)) {
            //反序列化
            sealInfo = gson.fromJson(sealStr, new TypeToken<SealInfo>() {
            }.getType());
        }
//        //增加测试数据
//        addTestData();

        ArtUtils.configRecyclerView(recyclerview, new GridLayoutManager(this, 2));
        mLabelAdapter = new LabelAdapter(mDataList);
        mLabelAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                //打印视图内容
//                View content = view.findViewById(R.id.ll_content);
//                printViewImage(content);
                //打印标签内容
                if (position >= 0 && position < mDataList.size()) {
                    LabelInfo item = mDataList.get(position);
                    item.setChoose(!item.isChoose());
                    mLabelAdapter.notifyDataSetChanged();
//                    printLabelInfo(mDataList.get(position));
                }
            }
        });

        recyclerview.setAdapter(mLabelAdapter);
    }

    private void addTestData() {
        //测试数据
        LabelInfo info1 = new LabelInfo();
        info1.setTaskName("任务名称1");
        info1.setNumber("采样单流水号");
        info1.setFrequecyNo("频次：" + 123);
        info1.setType("废水");
        info1.setSampingCode("FSL12344-123LLK-1");
        info1.setMonitemName("甲萘威,炔雌醇甲醚,2,3,3',4,4',5'-六氯联苯");
        info1.setRemark("容器、采样量、保存方法123");
        info1.setQrCode("12345");
        info1.setCb1("交接");
        info1.setCb2("分析");
        mDataList.add(info1);

        //测试数据
        LabelInfo info2 = new LabelInfo();
        info2.setTaskName("任务名称2");
        info2.setNumber("采样单流水号");
        info2.setFrequecyNo("频次：" + 123);
        info2.setType("废水");
        info2.setSampingCode("FSL12344-123LLK-1");
        info2.setMonitemName("监测项目123456789123456712312321");
        info2.setRemark("容器、采样量、保存方法123");
        info2.setQrCode("12345");
        info2.setCb1("交接");
        info2.setCb2("分析");
        mDataList.add(info2);
        sealInfo = new SealInfo("新都区环境监测站", "测试任务名称", "成都郊区", "地下水", "2018-10-11 15:20:21");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    //todo:一个记号
    @OnClick({R.id.btnPrintSeal, R.id.btnPrintLabel, R.id.ivSelectAll, R.id.tvSelectAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPrintSeal://密封条
                printSeal();
                break;
            case R.id.btnPrintLabel://标签
                printAllLabel();
                break;
            case R.id.ivSelectAll://全选
            case R.id.tvSelectAll:
                isSelectAll = !isSelectAll;
                selectAllLabel();
                updateSelectStatus();
                break;
        }
    }

    /**
     * 选中/取消选中所有标签
     */
    private void selectAllLabel() {
        boolean isSelect = isSelectAll;
        for (LabelInfo item : mDataList) {
            item.setChoose(isSelect);
        }
        mLabelAdapter.notifyDataSetChanged();
    }

    private void updateSelectStatus() {
        if (isSelectAll) {
            ivSelectAll.setImageResource(R.mipmap.ic_cb_checked);
        } else {
            ivSelectAll.setImageResource(R.mipmap.ic_cb_nor);
        }
    }

    /**
     * 打印所有标签
     */
    private void printAllLabel() {
        if (!isConnect) {
            ArtUtils.makeText(this, "请先连接到打印机！");
            return;
        }
        //清理
        mPrintList.clear();
        mPrintIndex = 0;
        //获取选中的标签
        for (LabelInfo item : mDataList) {
            if (!item.isChoose()) {
                continue;
            }
            mPrintList.add(item);
        }
        if (mPrintList.size() > 0) {
            Log.e(TAG, "printAllLabel: 标签数量" + mPrintList.size());
            sendContinuityPrint();
        } else {
            ArtUtils.makeText(this, "请选择要打印的标签！");
        }
    }

    /**
     * 发生连续打印数据
     */
    private void sendContinuityPrint() {
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder("MainActivity_sendContinuity_Timer");
                ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, threadFactoryBuilder);
                scheduledExecutorService.schedule(threadFactoryBuilder.newThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mPrintList != null && mPrintList.size() > 0) {
                            PrintUtil.printLabelInfo(mPrintList.get(0), mContext);
                        }
                    }
                }), 1000, TimeUnit.MILLISECONDS);
            }

        });

    }


    /**
     * 打印密封条
     */
    private void printSeal() {
        if (!isConnect) {
            ArtUtils.makeText(this, "请先连接到打印机！");
            return;
        }

        if (sealInfo == null) {
            ArtUtils.makeText(this, "未获取到封条信息！");
            return;
        }
        PrintUtil.printSealInfo(sealInfo, this);
    }


    /**
     * 查询打印状态
     */
    public void queryPrinterStatus() {
        MyPrinterPlusConnManager.getInstance(this).queryPrinterState();
    }

    /**
     * 打印测试页
     */
    public void printTestPage() {

    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "BroadcastReceiver->" + action);
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED://蓝牙状态广播
                    int openState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.STATE_OFF);
                    switch (openState) {
                        case BluetoothAdapter.STATE_OFF:              //蓝牙关闭
                        case BluetoothAdapter.STATE_TURNING_ON:       //蓝牙正在打开
                            titleBar.setRightText("未连接");
                            isConnect = false;
                            break;
                    }
                    break;
                case MyPrinterPlusManager.BLUE_TOOTH_DEVICE_NO_SUPPORTED:
                    titleBar.setRightText("当前设备不支持蓝牙打印");
                    isConnect = false;
                    break;
                case MyPrinterPlusConnManager.ACTION_QUERY_PRINTER_STATE:
                    if (mPrintList != null && mPrintList.size() > 0) {
                        mPrintIndex++;
                        Utils.toast(mContext, "连续打印" + " " + mPrintIndex);
                        mPrintList.remove(0);
                        Log.e(TAG, "onReceive: 已经连续打印" + mPrintIndex);
                        if (mPrintList.size() > 0) {
                            sendContinuityPrint();
                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            isConnect = data.getBooleanExtra("is_connect", false);
            deviceName = data.getStringExtra("device_name");
            if (isConnect) {
                titleBar.setRightText(deviceName == null || deviceName.equals("") ? "打印机以连接" : deviceName + " 已连接");
            } else {
                titleBar.setRightText(deviceName == null || deviceName.equals("") ? "打印机未连接" : deviceName + " 未连接");
            }
        }
    }
}
