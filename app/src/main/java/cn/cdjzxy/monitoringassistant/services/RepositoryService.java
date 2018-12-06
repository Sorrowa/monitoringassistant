package cn.cdjzxy.monitoringassistant.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.wonders.health.lib.base.utils.ArtUtils;

import cn.cdjzxy.monitoringassistant.BuildConfig;
import cn.cdjzxy.monitoringassistant.app.Constant;
import cn.cdjzxy.monitoringassistant.mvp.presenter.AppPresenter;
import cn.cdjzxy.monitoringassistant.mvp.presenter.FilePresenter;
import cn.cdjzxy.monitoringassistant.utils.FileUtils;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;

/**
 * 知识库Service
 */
public class RepositoryService extends IntentService {

    private String name;
    private String pwd;
    private String dir;

    private FilePresenter filePresenter;

    public RepositoryService() {
        super("RepositoryService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtils.clearDir(Constant.REPOSITORY_DIR);
        filePresenter = new FilePresenter(ArtUtils.obtainAppComponentFromContext(getApplicationContext()));
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        name = intent.getStringExtra("name");
        pwd = intent.getStringExtra("pwd");
        dir = intent.getStringExtra("dir");
        filePresenter.getRepositoryHashCode(name, pwd, dir);
    }


}
