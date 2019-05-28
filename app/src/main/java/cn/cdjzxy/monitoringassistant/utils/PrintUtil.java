package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.micheal.print.manager.MyPrinterPlusConnManager;
import com.micheal.print.thread.ThreadFactoryBuilder;
import com.micheal.print.thread.ThreadPool;
import com.tools.command.EscCommand;
import com.tools.command.GpUtils;
import com.tools.command.LabelCommand;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.LabelInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SealInfo;

/**
 * 蓝牙打印机——打印类
 * 在此封装一些方法：打印标签 表单打印  密封条打印等
 */
public class PrintUtil {
    private static final String TAG = "PrintUtil";
    //打印的勾选框大小
    public static final int PrintCheckBoxSize = 20;
    public static final int PrintCheckBoxOffset = 5;


    /**
     * 打印标签信息
     * 打印纸尺寸(宽*高)：75mm * 50mm
     * 1mm约等于2.8像素=>210px * 140px
     */
    public static void printLabelInfo(LabelInfo item, Context context) {
        Log.e(TAG, "printLabelInfo: 标签" + item.getTaskName());
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(75, 50); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(2); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
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
        int maxY = 380;
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
        ey += normalHeight * 2;
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
        // 打印
        MyPrinterPlusConnManager.getInstance(context).sendDataImmediately(datas);
        Log.d("zzh", "打印结束位置");
    }

    /**
     * 打印视图图片
     *
     * @param view
     */
    public static void printViewImage(View view) {
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

    }

    /**
     * 打印封条信息
     * 打印纸尺寸(宽*高)：75mm * 50mm
     * 1mm约等于2.8像素=>210px * 140px
     */
    public static void printSealInfo(SealInfo sealInfo, Context context) {
        Log.e(TAG, "printSealInfo: 打印密封条" + sealInfo.getTaskName());
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
        int ey = sy + (int) (normalHeight * 3.8);
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
        MyPrinterPlusConnManager.getInstance(context).sendDataImmediately(datas);//打印
    }

    private static Bitmap shotView(View view) {

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
    private static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight) {
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
    private static void addTextToLabel(LabelCommand tsc, int sx, int sy, int ex, int ey, String text, boolean addCheckBox, int thickness) {
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
    private static void addTextToLabel(LabelCommand tsc, int sx, int sy, int ex, int ey, String text, boolean addCheckBox, int thickness, LabelCommand.FONTMUL fontmul) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        int[] size = getTextSize(text, fontmul.getValue());
        int width = size[0];
        int height = size[1];

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
            size = getTextSize(text, fontmul.getValue());
            width = size[0];
            height = size[1];
        }

        if (addCheckBox) {
            //绘制-勾选框
            addBoxToLabel(tsc, sx, sy, ex - width - PrintCheckBoxOffset, ey, PrintCheckBoxSize, thickness);
            //更新宽度
            ex += PrintCheckBoxSize + PrintCheckBoxOffset;
        }

        int x = sx + Math.max((ex - sx) / 2 - width / 2, 0);
        int y = sy + Math.max((ey - sy) / 2 - height / 2, 0);

        tsc.addText(x, y, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, fontmul, fontmul, text);
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
    private static void addBoxToLabel(LabelCommand tsc, int sx, int sy, int ex, int ey, int size, int thickness) {
        int width = size;
        int height = size;

        int x = sx + (ex - sx) / 2 - width / 2;
        int y = sy + (ey - sy) / 2 - height / 2;

        tsc.addBox(x, y, x + size, y + size, thickness);
    }

    /**
     * 获取字符高宽
     *
     * @param text
     * @return
     */
    private static int[] getTextSize(String text, int mul) {
        if (TextUtils.isEmpty(text)) {
            return new int[]{0, 0};
        }

        Rect rect = new Rect();
        new Paint().getTextBounds(text, 0, text.length(), rect);

        int width = rect.left + rect.width();
        int height = rect.height();

        if (text.length() >= 40) {
            width = (int) (width * 2.6);
            height = (int) (height * 1.7);
        } else if (text.length() >= 20) {
            width = (int) (width * 2.4);
            height = (int) (height * 1.7);
        } else {
            width = (int) (width * 1.9);
            height = (int) (height * 1.7);
        }

        if (mul > 1) {
            width *= mul * 1.13;
            height *= mul * 1.13;
        }

        return new int[]{width, height};
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
    private static void addQRCodeToLabel(LabelCommand tsc, int sx, int sy, int ex, int ey, String text, int[] size) {
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


}
