package cn.cdjzxy.monitoringassistant.mvp.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.BasePresenter;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.cdjzxy.monitoringassistant.app.rx.RxObserver;
import cn.cdjzxy.monitoringassistant.app.rx.RxUtils;
import cn.cdjzxy.monitoringassistant.mvp.model.FileRepository;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.repository.RepositoryFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.repository.RepositoryFolder;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.repository.RepositoryGroup;
import cn.cdjzxy.monitoringassistant.utils.FileUtils;
import cn.cdjzxy.monitoringassistant.utils.repository.RepositoryDownloadHelper;
import cn.cdjzxy.monitoringassistant.utils.repository.RepositoryFileHelper;
import cn.cdjzxy.monitoringassistant.utils.repository.RepositoryFolderHelper;
import cn.cdjzxy.monitoringassistant.utils.repository.RepositoryGroupHelper;
import okhttp3.ResponseBody;
import timber.log.Timber;

/**
 * 知识库相关的数据操作Presenter
 */
public class FilePresenter extends BasePresenter<FileRepository> {
    private String dir;

    public FilePresenter(AppComponent appComponent) {
        super(appComponent.repositoryManager().createRepository(FileRepository.class));

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        Timber.d("onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 获取知识库 hash密码
     *
     * @param username
     * @param pwd
     */
    public void getRepositoryHashCode(String username, String pwd, String dir) {
        this.dir = dir;
        try {
            Map<String, String> params = new HashMap<>();
            params.put("opr", "getHash");
            params.put("name", URLEncoder.encode(username, "utf-8"));
            params.put("password", pwd);
            mModel.getRepositoryHashCode(params)
                    .compose(RxUtils.applySchedulers(this))
                    .subscribe(new RxObserver<>(new RxObserver.RxCallBack<ResponseBody>() {
                        @Override
                        public void onSuccess(ResponseBody responseBody) {
                            try {
                                String result = responseBody.string();
                                if (handleResult(result)) {
                                    Log.e("RepositoryHashCode : ", result);
                                    getRepositoryGroups(result);
                                }
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(int Type, String message, int responseCode) {

                        }
                    }));
        } catch (Exception e) {

        }
    }


    /**
     * 获取知识库 小组
     *
     * @param hashCode 参数
     * @return
     */
    public void getRepositoryGroups(final String hashCode) {
        Map<String, String> params = new HashMap<>();
        params.put("opr", "org");
        params.put("hash2", hashCode);
        mModel.getRepositoryGroups(params)
                .compose(RxUtils.applySchedulers(this))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            if (handleResult(result)) {
                                Log.e("RepositoryGroups : ", result);
                                getRepositoryFoders(RepositoryGroupHelper.getRepositoryGroups(result).get(0).getChilds(), hashCode);
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {
                    }
                }));
    }


    /**
     * 获取知识库 目录
     *
     * @param ownerid
     * @param hashCode
     */
    public void getRepositoryFolder(String ownerid, String hashCode) {
        Map<String, String> params = new HashMap<>();
        params.put("opr", "folderfiles");
        params.put("ownerid", ownerid);
        params.put("folderid", "0");
        params.put("hash2", hashCode);
        mModel.getRepositoryFolder(params)
                .compose(RxUtils.applySchedulers(this))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            if (handleResult(result)) {
                                Log.e("RepositoryFolder : ", result);
                                getRepositoryFiles(RepositoryFolderHelper.getRepositoryFolders(result), dir, hashCode, ownerid);
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {

                    }
                }));
    }

    /**
     * 获取知识库 文件
     *
     * @param ownerid
     * @param folderid
     * @param hashCode
     * @param dirPath
     */
    public void getRepositoryFile(String ownerid, String folderid, String hashCode, String dirPath) {
        Map<String, String> params = new HashMap<>();
        params.put("opr", "folderfiles");
        params.put("ownerid", ownerid);
        params.put("folderid", folderid);
        params.put("hash2", hashCode);
        mModel.getRepositoryFile(params)
                .compose(RxUtils.applySchedulers(this))
                .subscribe(new RxObserver<>(new RxObserver.RxCallBack<ResponseBody>() {
                    @Override
                    public void onSuccess(ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            if (handleResult(result)) {
                                Log.e("RepositoryFile : ", result);
                                downloadRepositoryFiles(RepositoryFileHelper.getRepositoryFiles(result), hashCode, dirPath);
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(int Type, String message, int responseCode) {

                    }
                }));
    }


    /**
     * 下载知识库文件
     *
     * @param files
     * @param hashCode
     * @param dirPath
     */
    public void downloadRepositoryFiles(List<RepositoryFile> files, String hashCode, String dirPath) {
        RepositoryDownloadHelper.downLoadFiles(files, hashCode, dirPath);
    }


    /**
     * 处理结果
     *
     * @param result
     */
    private boolean handleResult(String result) {
        if (result.startsWith("X:")) {//请求失败返回的错误信息
            Log.e("RepositoryError : ", result);
            return false;
        }
        return true;
    }


    /**
     * 获取 知识库 文件目录
     *
     * @param groups
     * @param hashCode
     */
    private void getRepositoryFoders(List<RepositoryGroup> groups, String hashCode) {
        for (RepositoryGroup group : groups) {
            getRepositoryFolder(group.getId(), hashCode);
            if (!group.getChilds().isEmpty()) {
                getRepositoryFoders(group.getChilds(), hashCode);
            }
        }
    }

    /**
     * 获取 知识库 文件
     *
     * @param folderes
     * @param path
     * @param hashCode
     * @param groupId
     */
    private void getRepositoryFiles(List<RepositoryFolder> folderes, String path, String hashCode, String groupId) {
        for (RepositoryFolder folder : folderes) {
            String dirPath = path + "/" + folder.getPath();
            FileUtils.makeDir(dirPath);
            getRepositoryFile(groupId, folder.getId(), hashCode, dirPath);
            if (!folder.getChilds().isEmpty()) {
                getRepositoryFiles(folder.getChilds(), dirPath, hashCode, groupId);
            }
        }

    }


}
