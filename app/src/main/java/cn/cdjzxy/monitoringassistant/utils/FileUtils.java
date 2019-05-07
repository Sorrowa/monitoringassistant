package cn.cdjzxy.monitoringassistant.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
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
        if (!imgName.contains(".png")) {
            imgName += ".png";
        }
        Log.e(TAG, "saveInOI: " + System.currentTimeMillis());

//        Bitmap newBM = sketchView.getResultBitmap();
//        switch (autographType) {
//            case check:
//                newBM = sketchViewCheck.getResultBitmap();
//                break;
//            case examine:
//                newBM = sketchViewExamine.getResultBitmap();
//                break;
//            case sampling:
//                newBM = sketchViewSampling.getResultBitmap();
//                break;
//            default:
//                newBM = sketchViewSampling.getResultBitmap();
//                break;
//        }

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
}
