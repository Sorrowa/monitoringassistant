package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.io.File;
import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.RepositoryHolder;


/**
 * 知识库
 */

public class RepositoryAdapter extends DefaultAdapter<File> {


    public RepositoryAdapter(List<File> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_repository;
    }

    @Override
    public BaseHolder<File> getHolder(View v, int viewType) {
        return new RepositoryHolder(v);
    }
}
