package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static cn.cdjzxy.monitoringassistant.utils.SamplingUtil.SAMPLING_MESSAGE;

public class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * 获取目录下的文件及文件夹
     *
     * @param dir
     * @return
     */
    public static List<File> getFilesByDir(String dir) {
        List<File> mFiles = new ArrayList<>();
        File[] files = new File(dir).listFiles();
        for (int i = 0; i < files.length; i++) {
            mFiles.add(files[i]);
        }
        return mFiles;
    }


    /**
     * 创建目录
     *
     * @param dirPath
     * @return
     */
    public static boolean makeDir(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        File folder = new File(dirPath);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }


    /**
     * 清空指定目录（不删除目录）
     *
     * @param dirPath 目录路径
     */
    public static boolean clearDir(String dirPath) {

        if (TextUtils.isEmpty(dirPath) || getDirAllFile(dirPath) == null) {
            return false;
        }

        List<File> files = getDirAllFile(dirPath);
        if (files.isEmpty()) {
            return true;
        }
        for (File f : files) {
            f.delete();
        }

        return true;
    }


    /**
     * 获取文件夹下的所有文件(包含子目录)
     *
     * @param dirPath 文件夹路径
     * @return
     */
    public static List<File> getDirAllFile(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return null;
        }
        List<File> allFile = new ArrayList<>();
        SecurityManager checker = new SecurityManager();
        File path = new File(dirPath);
        checker.checkRead(dirPath);
        File[] files = path.listFiles();
        for (File f : files) {
            if (f.isFile())
                allFile.add(f);
            else
                getDirAllFile(f.getAbsolutePath());
        }
        return allFile;
    }

    /**
     * show 保存图片到本地文件，耗时操作
     *
     * @param bitmap   需要保存的图片
     * @param filePath 文件保存路径
     * @param imgName  文件名
     * @param compress 压缩百分比1-100
     * @return 返回保存的图片文件
     * @author TangentLu
     * create at 16/6/17 上午11:18
     */
    public static File saveInOI(Bitmap bitmap, String filePath, String imgName, int compress) {
//        Log.e(TAG, "saveInOI: " + String.format(" filePath:%d, imgName:%s ", filePath, imgName));

        Log.e(TAG, "saveInOI: " + System.currentTimeMillis());

        Log.e(TAG, "saveInOI: " + System.currentTimeMillis());
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(filePath, imgName);
            if (!f.exists()) {
                f.createNewFile();
            } else {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            Log.e(TAG, "saveInOI: " + System.currentTimeMillis());

            if (compress >= 1 && compress <= 100)
                bitmap.compress(Bitmap.CompressFormat.PNG, compress, out);
            else {
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
            }
            Log.e(TAG, "saveInOI: " + System.currentTimeMillis());

            out.close();
            bitmap.recycle();
            bitmap = null;
            return f;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName 包含文件路径和文件名称
     * @return @true存在 @false不存在
     */
    public static boolean fileIsExists(String fileName) {
        try {
            File f = new File(fileName);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * 压缩图片
     *
     * @param fileList 原图片集合
     * @param context  context
     * @param message  压缩状态
     * @param callBack 压缩回调
     */
    public static void compressPic(List<File> fileList, Context context,
                                   Message message, PictureCompressCallBack callBack) {
        List<File> compressFiles = new ArrayList<>();
        for (File source : fileList) {
            //异步压缩图片
            new Compressor(context)
                    .setMaxWidth(640)
                    .setMaxHeight(480)
                    .setQuality(30)
//                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .compressToFileAsFlowable(source)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            // 压缩成功后调用，返回压缩后的图片
                            compressFiles.add(file);

                            message.what = SAMPLING_MESSAGE;
                            message.obj = String.format("正在压缩采样单图片[%d/%d]", compressFiles.size(), fileList.size());
                            //全部压缩完成，上传图片
                            if (compressFiles.size() >= fileList.size()) {
                                if (callBack != null) {
                                    callBack.onSuccess(compressFiles);
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
//                            throwable.printStackTrace();
                            //当压缩过程出现问题时调用
                            if (message != null) {
                                message.what = SAMPLING_MESSAGE;
                                message.obj = "图片压缩失败";

                            }
                            if (callBack != null) {
                                callBack.onFailed("图片压缩失败");
                            }

                        }
                    });
        }
    }

    public interface PictureCompressCallBack {
        void onSuccess(List<File> list);

        void onFailed(String message);
    }



}
