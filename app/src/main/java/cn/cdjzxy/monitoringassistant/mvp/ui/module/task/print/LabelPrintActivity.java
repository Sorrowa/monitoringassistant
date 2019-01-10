package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aries.ui.view.title.TitleBarView;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.service.GpPrintService;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.LabelAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.UnitAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

/**
 * 标签打印
 */

public class LabelPrintActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.imageView)
    ImageView imageView;

    private TitleBarView titleBar;

    private List<Object> mdataList = new ArrayList<>();
    private LabelAdapter mLabelAdapter;
    public static GpService mGpService;

    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    private int mPrinterIndex = 0;
    private PrinterServiceConnection conn = null;
    private int mTotalCopies = 0;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        this.titleBar = titleBar;
        titleBar.setTitleMainText("标签打印");
        titleBar.setRightText("未连接");
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBluetoothDevice();
            }
        });
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    /**
     * 获取蓝牙设备
     */
    public void getBluetoothDevice() {
        // Get local Bluetooth adapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (bluetoothAdapter == null) {
            ArtUtils.makeText(this, "当前设备不支持蓝牙！");
        } else {
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult

            if (!bluetoothAdapter.isEnabled()) {
                //蓝牙未启用，请求打开蓝牙
//                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                //请求打开蓝牙
                bluetoothAdapter.enable();
            }

            if (bluetoothAdapter.isEnabled()) {
                //跳转到蓝牙设备扫描、连接界面
                Intent intent = new Intent(LabelPrintActivity.this, LabelPrintDeviceActivity.class);
//                startActivityForResult(intent, INTENT_PORT_SETTINGS);
                ArtUtils.startActivity(intent);
            } else {
                ArtUtils.makeText(this, "请打开蓝牙！");
            }
        }
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

        initUnitData();

        mLabelAdapter.notifyDataSetChanged();

        connection();

        // 注册实时状态查询广播
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
        /**
         * 标签模式下，可注册该广播，在需要打印内容的最后加入addQueryPrinterStatus(RESPONSE_MODE mode)
         * ，在打印完成后会接收到，action为GpCom.ACTION_LABEL_RESPONSE的广播，特别用于连续打印，
         * 可参照该sample中的sendLabelWithResponse方法与广播中的处理
         **/
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_LABEL_RESPONSE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        queryPrinterStatus();
    }

    @Override
    public void onStart() {
        super.onStart();

        queryPrinterStatus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            unbindService(conn); // unBindService
        }
        unregisterReceiver(mBroadcastReceiver);
    }

    public void queryPrinterStatus() {
        if (mGpService == null) {
            return;
        }
        try {
            mGpService.queryPrinterStatus(mPrinterIndex, 500, MAIN_QUERY_PRINTER_STATUS);
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * 初始化数据
     */
    private void initUnitData() {
        ArtUtils.configRecyclerView(recyclerview, new GridLayoutManager(this, 2));

        mdataList.add(new Object());
        mLabelAdapter = new LabelAdapter(mdataList);
        mLabelAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                View content = view.findViewById(R.id.ll_content);
                printViewImage(content);
            }
        });

        recyclerview.setAdapter(mLabelAdapter);
    }

    /**
     * 打印视图图片
     *
     * @param view
     */
    void printViewImage(View view) {
        if (view == null) {
            return;
        }

        Bitmap b = shotView(view);
        if (b == null) {
            return;
        }

        b = scaleImage(b,550,366);

        imageView.setImageBitmap(b);
        Log.e(TAG, "w:" + b.getWidth() + " h:" + b.getHeight());

        LabelCommand tsc = new LabelCommand();
        tsc.addSize(75, 50); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 20);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        // 绘制图片
//        tsc.addBitmap(10, 10, LabelCommand.BITMAP_MODE.OVERWRITE, b.getWidth(), b);
        tsc.addBitmap(10,10,550,b);

        tsc.addPrint(1, 1); // 打印标签
//        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);

        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public Bitmap shotView(View view) {

//        View view = ctx.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap bp = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, view.getMeasuredWidth(),
                view.getMeasuredHeight());

        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();

        return bp;
    }

    /**
     * 缩放图片
     *
     * @param bm        要缩放图片
     * @param newWidth  宽度
     * @param newHeight 高度
     * @return处理后的图片
     */
    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        if (bm != null & !bm.isRecycled()) {
            bm.recycle();//销毁原图片
            bm = null;
        }

        return newbm;
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转角度
     * @param bitmap 要处理的Bitmap
     * @return 处理后的Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        if (resizedBitmap != bitmap && bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        return resizedBitmap;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // GpCom.ACTION_DEVICE_REAL_STATUS 为广播的IntentFilter
            if (action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)) {

                // 业务逻辑的请求码，对应哪里查询做什么操作
                int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);
                // 判断请求码，是则进行业务操作
                if (requestCode == MAIN_QUERY_PRINTER_STATUS) {

                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    String str;
                    String rightText = "未连接";
                    if (status == GpCom.STATE_NO_ERR) {
                        str = "打印机连接正常";
                        rightText = "已连接";
                    } else {
                        str = "打印机: ";
                        if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                            str += "未连接";
                        }
                        if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                            str += "缺纸";
                        }
                        if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                            str += "打印机开盖";
                        }
                        if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                            str += "打印机出错";
                        }
                        if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                            str += "查询超时";
                        }
                    }

                    LabelPrintActivity.this.titleBar.setRightText(rightText);

                    ArtUtils.makeText(context, "打印机：" + mPrinterIndex + " 状态：" + str);

                } else if (action.equals(GpCom.ACTION_LABEL_RESPONSE)) {
                    byte[] data = intent.getByteArrayExtra(GpCom.EXTRA_PRINTER_LABEL_RESPONSE);
                    int cnt = intent.getIntExtra(GpCom.EXTRA_PRINTER_LABEL_RESPONSE_CNT, 1);
                    String d = new String(data, 0, cnt);
                    /**
                     * 这里的d的内容根据RESPONSE_MODE去判断返回的内容去判断是否成功，具体可以查看标签编程手册SET
                     * RESPONSE指令
                     * 该sample中实现的是发一张就返回一次,这里返回的是{00,00001}。这里的对应{Status,######,ID}
                     * 所以我们需要取出STATUS
                     */
                    Log.d("LABEL RESPONSE", d);
                    if (--mTotalCopies > 0 && d.charAt(1) == 0x00) {
                        //TODO:继续打印
                    }
                }
            }
        }
    };

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.i(TAG, "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }
}
