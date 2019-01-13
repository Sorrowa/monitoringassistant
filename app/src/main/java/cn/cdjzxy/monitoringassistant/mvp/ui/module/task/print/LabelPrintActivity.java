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
import android.support.annotation.NonNull;
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
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.LabelInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SealInfo;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.LabelAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

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
    public static GpService GpService;
    public static int PrinterIndex = 0;
    public static boolean IsConnet = false;
    private static PrinterServiceConnection conn = null;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        this.titleBar = titleBar;
        titleBar.setTitleMainText("标签打印");
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBluetoothDevice();
            }
        });

        updateConnectText();
    }

    private void updateConnectText() {
        titleBar.setRightText(IsConnet ? "已连接" : "未连接");
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

        mLabelAdapter.notifyDataSetChanged();

        initPrintService();
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
        info1.setMonitemName("监测项目123456789123456789123456789123456789123456789");
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
//        if (conn != null) {
//            unbindService(conn); // unBindService
//        }
//        unregisterReceiver(mBroadcastReceiver);
    }

    @OnClick({R.id.btnPrintSeal, R.id.btnPrintLabel, R.id.ivSelectAll, R.id.tvSelectAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPrintSeal:
                printSeal();
                break;

            case R.id.btnPrintLabel:
                printAllLabel();
                break;

            case R.id.ivSelectAll:
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
        //清理
        mPrintList.clear();
        mPrintIndex = 0;

        //获取选中的标签
        for (LabelInfo item : mDataList) {
            if (!item.isChoose()) {
                continue;
            }

//            mPrintList.add(item);
            printLabelInfo(item);
        }

        //如果有选中，则开始打印
        if (mPrintList.size() > 0) {
//            printNextLabel();
        } else {
//            ArtUtils.makeText(this, "请选择要打印的标签！");
        }
    }

    /**
     * 打印下一个标签
     */
    private void printNextLabel() {
        if (mPrintIndex >= mPrintList.size() || mPrintIndex < 0) {
            return;
        }

        //打印标签
        printLabelInfo(mPrintList.get(mPrintIndex++));
    }

    /**
     * 打印密封条
     */
    private void printSeal() {
        printSealInfo();
    }

    /**
     * 初始化打印服务
     */
    private void initPrintService() {
        if (conn != null && GpService != null) {
            return;
        }

        if (conn != null) {
            unbindService(conn); // unBindService
            unregisterReceiver(mBroadcastReceiver);
        }

        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService

        // 注册实时状态查询广播
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
        //注册连接状态广播
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_CONNECT_STATUS));
        /**
         * 标签模式下，可注册该广播，在需要打印内容的最后加入addQueryPrinterStatus(RESPONSE_MODE mode)
         * ，在打印完成后会接收到，action为GpCom.ACTION_LABEL_RESPONSE的广播，特别用于连续打印，
         * 可参照该sample中的sendLabelWithResponse方法与广播中的处理
         **/
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_LABEL_RESPONSE));
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
                if (GpService == null) {
                    ArtUtils.makeText(this, "打印服务初始化失败！");
                    return;
                }

                //跳转到蓝牙设备扫描、连接界面
                Intent intent = new Intent(LabelPrintActivity.this, LabelPrintDeviceActivity.class);
                ArtUtils.startActivity(intent);
            } else {
                ArtUtils.makeText(this, "请打开蓝牙！");
            }
        }
    }

    /**
     * 查询打印状态
     */
    public void queryPrinterStatus() {
        if (GpService == null) {
            return;
        }

        try {
            GpService.queryPrinterStatus(PrinterIndex, 500, MAIN_QUERY_PRINTER_STATUS);
        } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    /**
     * 打印测试页
     */
    public void printTestPage() {
        try {
            int rel = GpService.printeTestPage(PrinterIndex);
            Log.i("ServiceConnection", "rel " + rel);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
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

        b = scaleImage(b, 550, 366);

//        imageView.setImageBitmap(b);
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
        tsc.addBitmap(10, 10, 550, b);

        tsc.addPrint(1, 1); // 打印标签
//        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);

        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = GpService.sendLabelCommand(PrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印标签信息
     * 打印纸尺寸(宽*高)：75mm * 50mm
     * 1mm约等于2.8像素=>210px * 140px
     */
    public void printLabelInfo(LabelInfo item) {
        if (!IsConnet) {
            ArtUtils.makeText(this, "请先连接到打印机！");
            return;
        }

        if (item == null) {
            return;
        }

        LabelCommand tsc = new LabelCommand();
        tsc.addSize(75, 50); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(2); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        // 先绘制表格
//        tsc.addBox(10,20,60,50,1);

        /**
         * SX轴0开始，SY轴30开始，留有一定边距，Y轴设置为0打印不全
         * 最大宽度550-sx，最大高度360-SY，留有一定边距
         */

        //线条厚度
        int thickness = 2;
        //最小X坐标
        int minX = 0;
        //最小Y坐标
        int minY = 30;
        //最大X坐标
        int maxX = 560;
        //最大Y坐标
        int maxY = 370;
        //普通高度
        int normalHeight = 50;
        //开始X坐标
        int sx = minX;
        //开始Y坐标
        int sy = minY;
        //结束X坐标
        int ex = maxX;
        //结束Y坐标
        int ey = maxY;

        // 绘制最大边框
        tsc.addBox(sx, sy, ex, ey, thickness);

        // 绘制-任务名称
        addTextToLabel(tsc, sx, sy, ex, sy + normalHeight, item.getTaskName(), false, thickness);

        //边框-采样单流水号，宽度5/8
        sy += normalHeight;
        ex = (int) (maxX * 0.625);
        ey = sy + normalHeight;
        tsc.addBox(sx, sy, ex, ey, thickness);

        // 绘制-采样单流水号
        addTextToLabel(tsc, sx, sy, ex, ey, item.getNumber(), false, thickness);

        //边框-频次，3/8
        tsc.addBox(ex, sy, maxX, ey, thickness);

        // 绘制-频次
        addTextToLabel(tsc, ex, sy, maxX, ey, item.getFrequecyNo(), false, thickness);

        //边框-废水，宽度3/8
        sy = ey;
        ex = (int) (maxX * 0.375);
        ey += normalHeight;
        tsc.addBox(minX, sy, ex, ey, thickness);

        // 绘制-类型
        addTextToLabel(tsc, minX, sy, ex, ey, item.getType(), false, thickness);

        //边框-编码，宽度8/10
        tsc.addBox(ex, sy, maxX, ey, thickness);

        // 绘制-编码
        addTextToLabel(tsc, ex, sy, maxX, ey, item.getSampingCode(), false, thickness);

        //边框-监测项目
        sy = ey;
        ey += (int) (normalHeight * 1.8);
        tsc.addBox(minX, sy, maxX, ey, thickness);

        // 绘制-监测项目
        addTextToLabel(tsc, minX, sy, maxX, ey, item.getMonitemName(), false, thickness);

        //边框-二维码
        sy = ey;
        ex = (int) (maxX * 0.2);
        ey += normalHeight * 2;
        tsc.addBox(minX, sy, ex, maxY, thickness);

        // 绘制-二维码
        addQRCodeToLabel(tsc, minX, sy, ex, maxY, item.getQrCode(), item.getQrCodeSize());

        //边框-容器、采样量、保存方法
        ey = sy + normalHeight;
        tsc.addBox(ex, sy, maxX, ey, thickness);

        // 绘制-容器、采样量、保存方法
        addTextToLabel(tsc, ex, sy, maxX, ey, item.getRemark(), false, thickness);

        //边框-交接，宽度3/6
        sx = ex;
        ex = (int) ((maxX - sx) * 0.5) + sx;
        sy = ey;
        ey = sy + normalHeight;
        tsc.addBox(sx, sy, ex, maxY, thickness);

        // 绘制-交接和勾选框
        addTextToLabel(tsc, sx, sy, ex, maxY, item.getCb1(), true, thickness);
        //绘制-勾选框
//        addBoxToLabel(tsc, sx - 40, sy, ex - 40, maxY, 20, 1);

        //边框-分析，宽度3/6
        tsc.addBox(ex, sy, maxX, maxY, thickness);
        // 绘制-分析和勾选框
        addTextToLabel(tsc, ex, sy, maxX, maxY, item.getCb2(), true, thickness);

        tsc.addPrint(1, 1); // 打印标签
//        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
//        //开启带Response的打印，用于连续打印
//        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);

        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = GpService.sendLabelCommand(PrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印封条信息
     * 打印纸尺寸(宽*高)：75mm * 50mm
     * 1mm约等于2.8像素=>210px * 140px
     */
    public void printSealInfo() {
        if (!IsConnet) {
            ArtUtils.makeText(this, "请先连接到打印机！");
            return;
        }

        if (sealInfo == null) {
            ArtUtils.makeText(this, "未获取到封条信息！");
            return;
        }

        LabelCommand tsc = new LabelCommand();
        tsc.addSize(75, 50); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(2); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区

        /**
         * SX轴0开始，SY轴30开始，留有一定边距，Y轴设置为0打印不全
         * 最大宽度550-sx，最大高度360-SY，留有一定边距
         */

        //线条厚度
        int thickness = 1;
        //最小X坐标
        int minX = 25;
        //最小Y坐标
        int minY = 20;
        //最大X坐标
        int maxX = 540;
        //最大Y坐标
        int maxY = 370;
        //普通高度
        int normalHeight = 30;
        //开始X坐标
        int sx = minX;
        //开始Y坐标
        int sy = minY;
        //结束X坐标
        int ex = maxX;
        //结束Y坐标
        int ey = sy + normalHeight * 3;
        int offsetX = 1;
        int offsetY = 5;

//        // 绘制边框
//        tsc.addBox(sx, sy, ex, ey, thickness);

        // 绘制-标题
        addTextToLabel(tsc, sx, sy, ex, ey, sealInfo.getTitle(), false, thickness, LabelCommand.FONTMUL.MUL_2);


        sy = ey;
        ex = (int) (maxX * 0.3);
        ey = sy + normalHeight * 2;
//        //边框-任务名称
//        tsc.addBox(sx, sy, ex, ey, thickness);

        // 绘制-任务名称
        addTextToLabel(tsc, sx, sy, ex + offsetX, ey, "任务名称：", false, thickness);

        // 绘制-任务名称
        sx = ex;
        ex += (int) (maxX * 0.65);
        addTextToLabel(tsc, sx - offsetX, sy, ex, ey, sealInfo.getTaskName(), false, thickness);

        //绘制底线
        ey -= offsetY;
        sy = ey;
        tsc.addBox(sx - offsetX, sy, ex, ey, thickness);


        sx = minX;
        sy = ey;
        ex = (int) (maxX * 0.3);
        ey = sy + normalHeight * 2;
//        //边框-采样点位
//        tsc.addBox(sx, sy, ex, ey, thickness);

        // 绘制-采样点位
        addTextToLabel(tsc, sx, sy, ex + offsetX, ey, "采样点位：", false, thickness);

        // 绘制-采样点位
        sx = ex;
        ex += (int) (maxX * 0.65);
        addTextToLabel(tsc, sx - offsetX, sy, ex, ey, sealInfo.getSampingAddr(), false, thickness);

        //绘制底线
        ey -= offsetY;
        sy = ey;
        tsc.addBox(sx - offsetX, sy, ex, ey, thickness);


        sx = minX;
        sy = ey;
        ex = (int) (maxX * 0.3);
        ey = sy + normalHeight * 2;
//        //边框-样品性质
//        tsc.addBox(sx, sy, ex, ey, thickness);

        // 绘制-样品性质
        addTextToLabel(tsc, sx, sy, ex + offsetX, ey, "样品性质：", false, thickness);

        // 绘制-样品性质
        sx = ex;
        ex += (int) (maxX * 0.65);
        addTextToLabel(tsc, sx - offsetX, sy, ex, ey, sealInfo.getType(), false, thickness);

        //绘制底线
        ey -= offsetY;
        sy = ey;
        tsc.addBox(sx - offsetX, sy, ex, ey, thickness);


        sx = minX;
        sy = ey;
        ex = (int) (maxX * 0.3);
        ey = sy + normalHeight * 2;
//        //边框-密封时间
//        tsc.addBox(sx, sy, ex, ey, thickness);

        // 绘制-密封时间
        addTextToLabel(tsc, sx, sy, ex + offsetX, ey, "密封时间：", false, thickness);

        // 绘制-密封时间
        sx = ex;
        ex += (int) (maxX * 0.65);
        addTextToLabel(tsc, sx - offsetX, sy, ex, ey, sealInfo.getTime(), false, thickness);

        //绘制底线
        ey -= offsetY;
        sy = ey;
        tsc.addBox(sx - offsetX, sy, ex, ey, thickness);

        tsc.addPrint(1, 1); // 打印标签
//        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = GpService.sendLabelCommand(PrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    //打印的勾选框大小
    final int PrintCheckBoxSize = 20;
    final int PrintCheckBoxOffset = 5;

    /**
     * 添加文字，自动居中
     *
     * @param tsc
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param text
     */
    private void addTextToLabel(LabelCommand tsc, int sx, int sy, int ex, int ey, String text, boolean addCheckBox, int thickness) {
        addTextToLabel(tsc, sx, sy, ex, ey, text, addCheckBox, thickness, LabelCommand.FONTMUL.MUL_1);
    }

    /**
     * 添加文字，自动居中
     *
     * @param tsc
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param text
     */
    private void addTextToLabel(LabelCommand tsc, int sx, int sy, int ex, int ey, String text, boolean addCheckBox, int thickness, LabelCommand.FONTMUL fontmul) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        int[] size = getTextSize(text);
        int width = (int) (size[0] * 1.8);
        int height = (int) (size[1] * 1.7);

        if (fontmul.getValue() > 1) {
            width *= fontmul.getValue() * 1.13;
            height *= fontmul.getValue() * 1.13;
        }

        //如果超框，则换行
        int maxWidth = ex - sx - 10;
        if (maxWidth > 0 && width >= maxWidth) {
            //截取一半
            String newText = text.substring(0, text.length() / 2);
            //更新字符
            text = text.substring(newText.length());

            //打印前部分
            addTextToLabel(tsc, sx, sy, ex, ey - height - 3, newText, addCheckBox, thickness, fontmul);

            //勾选框在第一行
            addCheckBox = false;

            //本次打印下移一部分
            ey += height + 3;

            //重新计算字符尺寸
            size = getTextSize(text);
            width = (int) (size[0] * 1.8);
            height = (int) (size[1] * 1.7);
        }

        if (addCheckBox) {
            //绘制-勾选框
            addBoxToLabel(tsc, sx, sy, ex - width - PrintCheckBoxOffset, ey, PrintCheckBoxSize, thickness);
            //更新宽度
            ex += PrintCheckBoxSize + PrintCheckBoxOffset;
        }

        int x = sx + (ex - sx) / 2 - width / 2;
        int y = sy + (ey - sy) / 2 - height / 2;

        tsc.addText(x, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, fontmul, fontmul, text);
        Log.e(TAG, text + " -> " + x + "," + y);
    }

    /**
     * 添加二维码，自动居中
     *
     * @param tsc
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param text
     */
    private void addQRCodeToLabel(LabelCommand tsc, int sx, int sy, int ex, int ey, String text, int[] size) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        int width = size[0];
        int height = size[1];

        int x = sx + (ex - sx) / 2 - width / 2;
        int y = sy + (ey - sy) / 2 - height / 2;

        tsc.addQRCode(x, y, LabelCommand.EEC.LEVEL_L, 4, LabelCommand.ROTATION.ROTATION_0, text);
        Log.e(TAG, text + " qr-> " + x + "," + y);
    }

    /**
     * 添加一个框，自动居中
     *
     * @param tsc
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param size
     * @param thickness
     */
    private void addBoxToLabel(LabelCommand tsc, int sx, int sy, int ex, int ey, int size, int thickness) {
        int width = size;
        int height = size;

        int x = sx + (ex - sx) / 2 - width / 2;
        int y = sy + (ey - sy) / 2 - height / 2;

        tsc.addBox(x, y, x + size, y + size, thickness);
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
     * 获取字符高宽
     *
     * @param text
     * @return
     */
    public static int[] getTextSize(String text) {
        Rect rect = new Rect();
        new Paint().getTextBounds(text, 0, text.length(), rect);
        return new int[]{rect.width(), rect.height()};
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
            Log.d(TAG, "BroadcastReceiver->" + action);
            if (GpCom.ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                Log.d(TAG, "connect status " + type);
                switch (type) {
                    case GpDevice.STATE_NONE:
                        IsConnet = false;
                        break;

                    case GpDevice.STATE_VALID_PRINTER:
                        IsConnet = true;
                        break;
                }

                //更新连接字符串
                handler.sendEmptyMessage(UPDATE_STATUS);
            }

            // GpCom.ACTION_DEVICE_REAL_STATUS 为广播的IntentFilter
            if (action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)) {
                // 业务逻辑的请求码，对应哪里查询做什么操作
                int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);
                // 判断请求码，是则进行业务操作
                if (requestCode == MAIN_QUERY_PRINTER_STATUS) {
                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);

                    String str = "";
                    if (status == GpCom.STATE_NO_ERR) {
//                        str = "打印机连接正常";
                        IsConnet = true;
                    } else {
                        IsConnet = false;
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

                    //更新连接字符串
                    handler.sendEmptyMessage(UPDATE_STATUS);

                    if (!TextUtils.isEmpty(str)) {
                        ArtUtils.makeText(context, "打印机：" + PrinterIndex + " 状态：" + str);
                    }
                }
//                else if (action.equals(GpCom.ACTION_LABEL_RESPONSE)) {
//                    byte[] data = intent.getByteArrayExtra(GpCom.EXTRA_PRINTER_LABEL_RESPONSE);
//                    int cnt = intent.getIntExtra(GpCom.EXTRA_PRINTER_LABEL_RESPONSE_CNT, 1);
//                    String d = new String(data, 0, cnt);
//                    /**
//                     * 这里的d的内容根据RESPONSE_MODE去判断返回的内容去判断是否成功，具体可以查看标签编程手册SET
//                     * RESPONSE指令
//                     * 该sample中实现的是发一张就返回一次,这里返回的是{00,00001}。这里的对应{Status,######,ID}
//                     * 所以我们需要取出STATUS
//                     */
//                    Log.d("LABEL RESPONSE", d);
//                    if (d.charAt(1) == 0x00) {
//                        printNextLabel();
//                    }
//                }
            }
        }
    };

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected() called");
            GpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected() called");
            GpService = com.gprinter.aidl.GpService.Stub.asInterface(service);
        }
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message message) {
            checkNotNull(message);
            switch (message.what) {
                case UPDATE_STATUS:
                    updateConnectText();
                    break;
            }
        }
    };
}
