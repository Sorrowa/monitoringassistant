package cn.cdjzxy.monitoringassistant.utils.repository;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadLargeFileListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.monitoringassistant.BuildConfig;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.repository.RepositoryFile;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

/**
 * 知识库文件下载帮助类
 */
public class RepositoryDownloadHelper {

    public static void downLoadFiles(List<RepositoryFile> files, String hashCode, String dirPath) {

        if (CheckUtil.isEmpty(files)) {
            return;
        }

        FileDownloadQueueSet queueSet = new FileDownloadQueueSet(new FileDownloadLargeFileListener() {
            @Override
            protected void pending(BaseDownloadTask task, long soFarBytes, long totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, long soFarBytes, long totalBytes) {

            }

            @Override
            protected void paused(BaseDownloadTask task, long soFarBytes, long totalBytes) {

            }

            @Override
            protected void completed(BaseDownloadTask task) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {

            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        });

        List<BaseDownloadTask> tasks = new ArrayList<>();
        for (RepositoryFile file : files) {
            tasks.add(FileDownloader.getImpl().create(getFileDownUrl(file.getFileKey(), hashCode)).setPath(getFileSvaePath(dirPath, file.getName())));
        }
        queueSet.disableCallbackProgressTimes(); // 由于是队列任务, 这里是我们假设了现在不需要每个任务都回调`FileDownloadListener#progress`, 我们只关系每个任务是否完成, 所以这里这样设置可以很有效的减少ipc.
        queueSet.setAutoRetryTimes(2);// 所有任务在下载失败的时候都自动重试二次
        queueSet.downloadSequentially(tasks);// 串行执行该任务队列
        queueSet.start();//启动下载
    }


    /**
     * 获取文件下载地址
     *
     * @param filekey  文件key
     * @param hashCode hash密码
     * @return
     */
    private static String getFileDownUrl(String filekey, String hashCode) {
        return BuildConfig.REPOSITORY_URL + "/ufInterface?opr=download&filekey=" + filekey + "&hash2=" + hashCode;
    }

    /**
     * 获取文件保存地址
     *
     * @param dirPath  目录地址
     * @param fileName 文件名
     * @return
     */
    private static String getFileSvaePath(String dirPath, String fileName) {
        return dirPath + "/" + fileName;
    }
}
