package cn.cdjzxy.monitoringassistant.mvp.ui.module.setting;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.NetworkUtil;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 修改密码
 */

public class PwdModifyFragment extends BaseFragment<ApiPresenter> implements IView {

    Unbinder unbinder;
    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_sure_pwd)
    EditText etSurePwd;

    public PwdModifyFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pwd_modify, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(getContext()));
    }

    @Override
    public void showLoading() {
        showLoadingDialog("提交中...");
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(getContext(), message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        hideLoading();
        checkNotNull(message);
        switch (message.what) {
            case Message.RESULT_FAILURE:

                break;
            case Message.RESULT_OK:
                EventBus.getDefault().post(5, EventBusTags.TAG_MODIFY_PWD);
                showMessage("修改密码成功");
                break;

        }
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_modify_back, R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_modify_back:
                EventBus.getDefault().post(5, EventBusTags.TAG_MODIFY_PWD);
                break;
            case R.id.btn_submit:
                String oldPwd = etOldPwd.getText().toString();
                String newPwd = etNewPwd.getText().toString();
                String surePwd = etSurePwd.getText().toString();
                if (CheckUtil.isEmpty(oldPwd)) {
                    showMessage("请输入旧密码");
                    return;
                }

                if (!oldPwd.equals(UserInfoHelper.get().getUser().getPwd())) {
                    showMessage("密码输入错误，请输入正确的原始密码");
                    return;
                }

                if (CheckUtil.isEmpty(newPwd)) {
                    showMessage("请输入新密码");
                    return;
                }

                if (CheckUtil.isEmpty(surePwd)) {
                    showMessage("请输入确认密码");
                    return;
                }
                if (!newPwd.equals(surePwd)) {
                    showMessage("两次密码输入不一致");
                    return;
                }

                if (NetworkUtil.isNetworkAvailable(getContext())) {
                    showLoading();
                    mPresenter.modifyPwd(Message.obtain(this, new Object()), oldPwd, newPwd);
                } else {
                    showMessage("网络未连接，请联网后操作");
                }
                break;
        }
    }
}


