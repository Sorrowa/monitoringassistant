package cn.cdjzxy.monitoringassistant.utils;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

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

}
