package cn.cdjzxy.monitoringassistant.mvp.ui.module.task;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MonItems;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.UserDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.MonItemAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.UserAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.widgets.GridItemDecoration;

/**
 * 采样人员
 */

public class UserActivity extends BaseTitileActivity<ApiPresenter> {


    @BindView(R.id.rv_project)
    RecyclerView rvProject;
    @BindView(R.id.rv_project_selected)
    RecyclerView rvProjectSelected;

    private UserAdapter mUserAdapter;

    private UserAdapter mUserSelectedAdapter;

    private String projectId;

    private List<User> mUsers         = new ArrayList<>();
    private List<User> mUsersSelected = new ArrayList<>();

    private StringBuilder UserName = new StringBuilder("");
    private StringBuilder UserId   = new StringBuilder("");

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("采样人员");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMonItemsData();
                Intent intent = new Intent();
                intent.putExtra("UserName", UserName.toString());
                intent.putExtra("UserId", UserId.toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_project;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initUsersView();
        initUsersSelectedView();

        projectId = getIntent().getStringExtra("projectId");

        Project project = DBHelper.get().getProjectDao().queryBuilder().where(ProjectDao.Properties.Id.eq(projectId)).unique();

        List<String> userIds = project.getSamplingUser();
        List<User> users = DBHelper.get().getUserDao().queryBuilder().where(UserDao.Properties.Id.in(userIds)).list();

        if (!CheckUtil.isEmpty(users)) {
            mUsers.clear();
            mUsers.addAll(users);
        }
        mUserAdapter.notifyDataSetChanged();

    }


    private void initUsersView() {
        ArtUtils.configRecyclerView(rvProject, new GridLayoutManager(this, 4));
        mUserAdapter = new UserAdapter(mUsers);
        mUserAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                upateMonItemState(position);
            }
        });
        rvProject.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProject.setAdapter(mUserAdapter);
    }


    private void initUsersSelectedView() {
        ArtUtils.configRecyclerView(rvProjectSelected, new GridLayoutManager(this, 4));
        mUserSelectedAdapter = new UserAdapter(mUsersSelected);
        mUserSelectedAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                upateMonItemSelectedState(position);
            }
        });
        rvProjectSelected.addItemDecoration(new GridItemDecoration(getResources().getDimensionPixelSize(R.dimen.dp_16), 4));
        rvProjectSelected.setAdapter(mUserSelectedAdapter);
    }

    @OnClick({R.id.iv_add_monitem, R.id.iv_delete_monitem})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_monitem:
                addMonItems();
                break;
            case R.id.iv_delete_monitem:
                deleteMonItems();
                break;
        }
    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void upateMonItemState(int position) {
        if (mUsers.get(position).isSelected()) {
            mUsers.get(position).setSelected(false);
        } else {
            mUsers.get(position).setSelected(true);
        }
        mUserAdapter.notifyDataSetChanged();
    }

    /**
     * 更新选中状态
     *
     * @param position
     */
    private void upateMonItemSelectedState(int position) {
        if (mUsersSelected.get(position).isSelected()) {
            mUsersSelected.get(position).setSelected(false);
        } else {
            mUsersSelected.get(position).setSelected(true);
        }
        mUserSelectedAdapter.notifyDataSetChanged();
    }


    private void addMonItems() {
        for (User user : mUsers) {
            if (user.isSelected()) {
                user.setSelected(false);
                mUsersSelected.add(user);
                mUsers.remove(user);
            }
        }
        mUserAdapter.notifyDataSetChanged();
        mUserSelectedAdapter.notifyDataSetChanged();
    }

    private void deleteMonItems() {
        for (User user : mUsersSelected) {
            if (user.isSelected()) {
                user.setSelected(false);
                mUsers.add(user);
                mUsersSelected.remove(user);
            }
        }
        mUserAdapter.notifyDataSetChanged();
        mUserSelectedAdapter.notifyDataSetChanged();
    }


    private void getMonItemsData() {

        if (CheckUtil.isEmpty(mUsersSelected)) {
            return;
        }

        for (User user : mUsersSelected) {
            UserName.append(user.getName() + ",");
            UserId.append(user.getId() + ",");
        }

        if (UserName.lastIndexOf(",") > 0) {
            UserName.deleteCharAt(UserName.lastIndexOf(","));
        }

        if (UserId.lastIndexOf(",") > 0) {
            UserId.deleteCharAt(UserId.lastIndexOf(","));
        }
    }


}
