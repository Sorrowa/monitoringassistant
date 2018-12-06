package cn.cdjzxy.monitoringassistant.mvp.ui.module.setting;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.utils.ArtUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.MainActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.launch.LoginActivity;

/**
 * 设置
 */

public class SettingFragment extends BaseFragment {

    Unbinder unbinder;

    public SettingFragment() {
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
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


    @OnClick({R.id.stv_clear, R.id.stv_modify_pwd, R.id.stv_about, R.id.stv_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stv_clear:
                showClearDialog();
                break;
            case R.id.stv_modify_pwd:
                ArtUtils.startActivity(PwdModifyActivity.class);
                break;
            case R.id.stv_about:
                ArtUtils.startActivity(AboutActivity.class);
                break;
            case R.id.stv_logout:
                showLogoutDialog();
                break;
        }
    }

    private void showClearDialog() {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                //                .setTitle("请做出选择")
                .setMessage("确定清除本地缓存数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showClearDialog1();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }


    private void showClearDialog1() {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                //                .setTitle("请做出选择")
                .setMessage("确定清除本地未提交任务数据？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoHelper.get().saveUserLoginStatee(false);
                        ArtUtils.startActivity(LoginActivity.class);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }


    private void showLogoutDialog() {
        final Dialog dialog = new AlertDialog.Builder(getContext())
                //                .setTitle("请做出选择")
                .setMessage("确定退出登录？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoHelper.get().saveUserLoginStatee(false);
                        ArtUtils.startActivity(LoginActivity.class);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
}
