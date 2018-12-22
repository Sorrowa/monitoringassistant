package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;

import android.view.View;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.base.DefaultAdapter;

import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.MonItemHolder;
import cn.cdjzxy.monitoringassistant.mvp.ui.holder.UserHolder;


/**
 * 主页tab
 */

public class UserAdapter extends DefaultAdapter<User> {


    public UserAdapter(List<User> infos) {
        super(infos);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_project;
    }

    @Override
    public BaseHolder<User> getHolder(View v, int viewType) {
        return new UserHolder(v);
    }
}
