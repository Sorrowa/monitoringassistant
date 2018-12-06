package cn.cdjzxy.monitoringassistant.mvp.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.BasePresenter;

import cn.cdjzxy.monitoringassistant.mvp.model.BasicDataRepository;
import cn.cdjzxy.monitoringassistant.mvp.model.ProjectRepository;
import timber.log.Timber;

/**
 * 任务相关的数据操作Presenter
 */

public class ProjectPresenter extends BasePresenter<ProjectRepository> {

    public ProjectPresenter(AppComponent appComponent) {
        super(appComponent.repositoryManager().createRepository(ProjectRepository.class));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        Timber.d("onCreate");
    }






    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
