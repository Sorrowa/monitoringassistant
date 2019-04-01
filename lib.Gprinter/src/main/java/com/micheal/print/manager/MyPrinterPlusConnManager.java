package com.micheal.print.manager;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.micheal.print.PrinterCommand;
import com.micheal.print.thread.ThreadFactoryBuilder;
import com.micheal.print.thread.ThreadPool;
import com.micheal.print.utils.Utils;
import com.tools.io.BluetoothPort;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.micheal.print.manager.MyPrinterPlusManager.DEVICE_STATE;

/**
 * 2019/2/29：嘉泽@android向  重构：打印机蓝牙扫描——连接——打印逻辑   连接模块
 */
public class MyPrinterPlusConnManager {
    private static final String TAG = "MyPrinterPlusConnManage";
    private static MyPrinterPlusConnManager manager;
    private Context mContext;

    private MyPrinterPlusConnManager(Context context) {
        mContext = context;
    }

    public static MyPrinterPlusConnManager getInstance(Context context) {
        if (manager == null) {
            synchronized (MyPrinterPlusConnManager.class) {
                if (manager == null) {
                    manager = new MyPrinterPlusConnManager(context);
                }
            }
        }
        return manager;
    }

    private BluetoothPort port;
    private boolean isOpenPort;
    public static final String ACTION_CONN_STATE = "action_connect_state";//设备连接状态
    public static final String ACTION_QUERY_PRINTER_STATE = "action_query_printer_state";//查询打印机状态
    public static final int CONN_STATE_DISCONNECT = 0x90;//未连接
    public static final int CONN_STATE_CONNECTING = CONN_STATE_DISCONNECT << 1;//连接中
    public static final int CONN_STATE_FAILED = CONN_STATE_DISCONNECT << 2;//连接失败
    public static final int CONN_STATE_CONNECTED = CONN_STATE_DISCONNECT << 3;//成功连接
    public static final int CONN_STATE_NO_INVALID_PRINTER = CONN_STATE_DISCONNECT << 4;//连接失败——未知打印机设备
    public static final int CONN_STATE_PAIRED = CONN_STATE_DISCONNECT << 5;//设备状态——已配对（未连接）
    public PrinterReader reader;


    public static final byte FLAG = 0x10;
    private static final String READ_DATA_CNT = "read_data_cnt";
    private static final String READ_BUFFER_ARRAY = "read_buffer_array";
    /**
     * CPCL指令查询打印机实时状态 打印机缺纸状态
     */
    public static final int CPCL_STATE_PAPER_ERR = 0x02;
    /**
     * CPCL指令查询打印机实时状态 打印机开盖状态
     */
    public static final int CPCL_STATE_COVER_OPEN = 0x04;
    private byte[] sendCommand;
    /**
     * ESC查询打印机实时状态指令
     */
    private byte[] esc = {0x10, 0x04, 0x02};
    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    private PrinterCommand currentPrinterCommand;
    /**
     * 查询当前连接打印机所使用打印机指令（ESC（EscCommand.java）、TSC（LabelCommand.java））
     */
    private static final int READ_DATA = 10000;

    /**
     * ESC查询打印机实时状态 缺纸状态
     */
    private static final int ESC_STATE_PAPER_ERR = 0x20;
    /**
     * ESC指令查询打印机实时状态 打印机开盖状态
     */
    private static final int ESC_STATE_COVER_OPEN = 0x04;
    /**
     * ESC指令查询打印机实时状态 打印机报错状态
     */
    private static final int ESC_STATE_ERR_OCCURS = 0x40;

    /**
     * TSC查询打印机状态指令
     */
    private byte[] tsc = {0x1b, '!', '?'};

    /**
     * TSC指令查询打印机实时状态 打印机缺纸状态
     */
    private static final int TSC_STATE_PAPER_ERR = 0x04;

    /**
     * TSC指令查询打印机实时状态 打印机开盖状态
     */
    private static final int TSC_STATE_COVER_OPEN = 0x01;

    /**
     * TSC指令查询打印机实时状态 打印机出错状态
     */
    private static final int TSC_STATE_ERR_OCCURS = 0x80;

    /**
     * CPCL查询打印机状态指令
     */
    public static byte[] cpcl = new byte[]{0x1b, 0x68};

    /**
     * 获取端口打开状态（true 打开，false 未打开）
     *
     * @return
     */
    public boolean getConnState() {
        return isOpenPort;
    }

    /**
     * 连接设备
     *
     * @param device
     */
    public void openPort(BluetoothDevice device) {
        isOpenPort = false;
        port = new BluetoothPort(device.getAddress());
        isOpenPort = port.openPort();
        if (isOpenPort) {
            queryCommand();
        } else {
            if (this.port != null) {
                this.port = null;
            }
            sendBroadcast(CONN_STATE_FAILED, ACTION_CONN_STATE);
        }
    }

    private void queryCommand() {
        //开启读取打印机返回数据线程
        reader = new PrinterReader();
        reader.start(); //读取数据线程
        //查询打印机所使用指令
        queryPrinterCommand(); //
    }

    /**
     * 查询打印机当前状态
     */
    public void queryPrinterState() {
        if (isOpenPort) {
            ThreadPool.getInstantiation().addTask(new Runnable() {
                @Override
                public void run() {
                    Vector<Byte> data = new Vector<>(esc.length);
                    switch (currentPrinterCommand) {
                        case ESC:
                            for (int i = 0; i < esc.length; i++) {
                                data.add(esc[i]);
                            }
                            sendDataImmediately(data);
                            break;
                        case TSC:
                            for (int i = 0; i < tsc.length; i++) {
                                data.add(tsc[i]);
                            }
                            sendDataImmediately(data);
                            break;
                        case CPCL:
                            for (int i = 0; i < cpcl.length; i++) {
                                data.add(cpcl[i]);
                            }
                            sendDataImmediately(data);
                    }
                }
            });
        }
    }


    /**
     * 查询打印机当前使用的指令（TSC、ESC）
     */
    private void queryPrinterCommand() {
        //线程池添加任务
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                //发送ESC查询打印机状态指令
                sendCommand = esc;
                Vector<Byte> data = new Vector<>(esc.length);
                for (int i = 0; i < esc.length; i++) {
                    data.add(esc[i]);
                }
                sendDataImmediately(data); //发送esc数据
                //开启计时器，隔2000毫秒没有没返回值时发送TSC查询打印机状态指令
                final ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder("Timer");
                final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, threadFactoryBuilder);
                scheduledExecutorService.schedule(threadFactoryBuilder.newThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPrinterCommand == null || currentPrinterCommand != PrinterCommand.ESC) {
                            Log.e(TAG, Thread.currentThread().getName());
                            //发送TSC查询打印机状态指令
                            sendCommand = tsc;
                            Vector<Byte> data = new Vector<>(tsc.length);
                            for (int i = 0; i < tsc.length; i++) {
                                data.add(tsc[i]);
                            }
                            sendDataImmediately(data);
                            //开启计时器，隔2000毫秒没有没返回值时发送CPCL查询打印机状态指令
                            scheduledExecutorService.schedule(threadFactoryBuilder.newThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (currentPrinterCommand == null || (currentPrinterCommand != PrinterCommand.ESC && currentPrinterCommand != PrinterCommand.TSC)) {
                                        Log.e(TAG, Thread.currentThread().getName());
                                        //发送CPCL查询打印机状态指令
                                        sendCommand = cpcl;
                                        Vector<Byte> data = new Vector<Byte>(cpcl.length);
                                        for (int i = 0; i < cpcl.length; i++) {
                                            data.add(cpcl[i]);
                                        }
                                        sendDataImmediately(data);
                                        //开启计时器，隔2000毫秒打印机没有响应者停止读取打印机数据线程并且关闭端口
                                        scheduledExecutorService.schedule(threadFactoryBuilder.newThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (currentPrinterCommand == null) {
                                                    if (reader != null) {
                                                        reader.cancel();
                                                        port.closePort();
                                                        isOpenPort = false;
                                                        port = null;
                                                        sendBroadcast(CONN_STATE_FAILED, ACTION_CONN_STATE);
                                                    }
                                                }
                                            }
                                        }), 2000, TimeUnit.MILLISECONDS);
                                    }
                                }
                            }), 2000, TimeUnit.MILLISECONDS);
                        }
                    }
                }), 2000, TimeUnit.MILLISECONDS);
            }
        });
    }

    class PrinterReader extends Thread {
        private boolean isRun = false;

        private byte[] buffer = new byte[100];

        public PrinterReader() {
            isRun = true;
        }

        @Override
        public void run() {
            try {
                while (isRun) {
                    //读取打印机返回信息
                    int len = readDataImmediately(buffer);
                    if (len > 0) {
                        Message message = Message.obtain();
                        message.what = READ_DATA;
                        Bundle bundle = new Bundle();
                        bundle.putInt(READ_DATA_CNT, len); //数据长度
                        bundle.putByteArray(READ_BUFFER_ARRAY, buffer); //数据
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }
                }
            } catch (Exception e) {
                if (manager != null) {
                    closePort();
                }
            }
        }

        public void cancel() {
            isRun = false;
        }
    }

    public void sendDataImmediately(final Vector<Byte> data) {
        if (this.port == null) {
            return;
        }
        try {
            //  Log.e(TAG, "data -> " + new String(com.gprinter.command.GpUtils.convertVectorByteTobytes(data), "gb2312"));
            this.port.writeDataImmediately(data, 0, data.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int readDataImmediately(byte[] buffer) throws IOException {
        return this.port.readData(buffer);
    }

    /**
     * 关闭端口
     */
    public void closePort() {
        if (this.port != null) {
            if (reader!=null){
                reader.cancel();
            }
            boolean b = this.port.closePort();
            if (b) {
                this.port = null;
                isOpenPort = false;
                currentPrinterCommand = null;
            }
        }
        sendBroadcast(CONN_STATE_DISCONNECT, ACTION_CONN_STATE);
    }

    /**
     * 判断是实时状态（10 04 02）还是查询状态（1D 72 01）
     */
    private int judgeResponseType(byte r) {
        return (byte) ((r & FLAG) >> 4);
    }

    /**
     * 发生广播
     *
     * @param state  状态
     * @param action 广播类型
     */
    private void sendBroadcast(int state, String action) {
        Intent intent = new Intent(action);
        intent.putExtra(DEVICE_STATE, state);
        if (mContext != null) {
            mContext.sendBroadcast(intent);
        }
        if (state == CONN_STATE_CONNECTED) {//连接成功后：查询打印机的状态
            queryPrinterState();
        }
    }

    /**
     * 发送广播
     *
     * @param state  状态
     * @param action 广播类型
     */
    private void sendBroadcast(String state, String action) {
        Intent intent = new Intent(action);
        intent.putExtra(DEVICE_STATE, state);
        if (mContext != null) {
            mContext.sendBroadcast(intent);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case READ_DATA:
                    int cnt = msg.getData().getInt(READ_DATA_CNT); //数据长度 >0;
                    byte[] buffer = msg.getData().getByteArray(READ_BUFFER_ARRAY);  //数据
                    //这里只对查询状态返回值做处理，其它返回值可参考编程手册来解析
                    if (buffer == null) {
                        return;
                    }
                    int result = judgeResponseType(buffer[0]);
                    String status = "打印机连接正常";
                    if (sendCommand == esc) {
                        //设置当前打印机模式为ESC模式
                        if (currentPrinterCommand == null) {
                            currentPrinterCommand = PrinterCommand.ESC;
                            sendBroadcast(CONN_STATE_CONNECTED, ACTION_CONN_STATE);
                        } else {//查询打印机状态
                            if (result == 0) {//打印机状态查询
                                Intent intent = new Intent(ACTION_QUERY_PRINTER_STATE);
                                mContext.sendBroadcast(intent);
                            } else if (result == 1) {//查询打印机实时状态
                                if ((buffer[0] & ESC_STATE_PAPER_ERR) > 0) {
                                    status += " " + "打印机缺纸";
                                }
                                if ((buffer[0] & ESC_STATE_COVER_OPEN) > 0) {
                                    status += " " + "打印机开盖";
                                }
                                if ((buffer[0] & ESC_STATE_ERR_OCCURS) > 0) {
                                    status += " " + "打印机出错";
                                }
                                Log.i("Circle+打印机状态：", status);
                                Utils.toast(mContext, "打印模式:ESC" + " " + status);
                            }
                        }
                    } else if (sendCommand == tsc) {
                        //设置当前打印机模式为TSC模式
                        if (currentPrinterCommand == null) {
                            currentPrinterCommand = PrinterCommand.TSC;
                            sendBroadcast(CONN_STATE_CONNECTED, ACTION_CONN_STATE);
                        } else {
                            if (cnt == 1) {//查询打印机实时状态
                                if ((buffer[0] & TSC_STATE_PAPER_ERR) > 0) {//缺纸
                                    status += " " + "打印机缺纸";
                                }
                                if ((buffer[0] & TSC_STATE_COVER_OPEN) > 0) {//开盖
                                    status += " " + "打印机开盖";
                                }
                                if ((buffer[0] & TSC_STATE_ERR_OCCURS) > 0) {//打印机报错
                                    status += " " + "打印机出错";
                                }
                                Log.i("Circle+打印机状态:", status);

                                Utils.toast(mContext, "打印模式:TSC" + " " + status);
                            } else {//打印机状态查询
                                Intent intent = new Intent(ACTION_QUERY_PRINTER_STATE);
                                mContext.sendBroadcast(intent);
                            }
                        }
                    } else if (sendCommand == cpcl) {
                        if (currentPrinterCommand == null) {
                            currentPrinterCommand = PrinterCommand.CPCL;
                            sendBroadcast(CONN_STATE_CONNECTED, ACTION_CONN_STATE);
                        } else {
                            if (cnt == 1) {
                                if ((buffer[0] == CPCL_STATE_PAPER_ERR)) {//缺纸
                                    status += " " + "打印机缺纸";
                                }
                                if ((buffer[0] == CPCL_STATE_COVER_OPEN)) {//开盖
                                    status += " " + "打印机开盖";
                                }
                                Log.i("Circle+打印机状态:", status);
                                Utils.toast(mContext, "打印模式:CPCL" + " " + status);
                            } else {//打印机状态查询
                                Intent intent = new Intent(ACTION_QUERY_PRINTER_STATE);
                                mContext.sendBroadcast(intent);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
