package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wonders.health.lib.base.integration.cache.Cache;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_INT_SOURCE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_SOURCE_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.isNeedSave;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mProject;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.saveMySample;


/**
 * 主要噪声源——详情或者编辑界面
 */
public class NoiseSourceEditFragment extends BaseFragment implements IView {
    @BindView(R.id.btn_back)
    LinearLayout linearBack;//返回上级的title
    @BindView(R.id.my_layout_source)
    MyDrawableLinearLayout edSource;//主要声源
    @BindView(R.id.my_layout_source_name)
    MyDrawableLinearLayout edSourceName;//声源名称
    @BindView(R.id.my_layout_source_num)
    MyDrawableLinearLayout edSourceNum;//数量
    @BindView(R.id.my_layout_source_date)
    MyDrawableLinearLayout edSourceDate;//运行时间和状况
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_save)
    LinearLayout linearSave;
    private NoisePrivateData.MianNioseSourceBean sourceBean;
    private int position;
    private boolean isStop = false;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_source_edit, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        isStop = false;
        Log.e(TAG, "initData: ");
        edSource.setEditTextStr("");
        edSourceName.setEditTextStr("");
        edSourceNum.setEditTextStr("");
        edSourceDate.setEditTextStr("");

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isStop) {
            setDataShow();
        }
    }

    private void setDataShow() {
        position = getActivity().getSharedPreferences(NOISE_FRAGMENT_SHARE, 0) == null ? -1 :
                getActivity().getSharedPreferences(NOISE_FRAGMENT_SHARE, 0).
                        getInt(NOISE_FRAGMENT_SOURCE_SHARE, -1);
        if (position != -1) {
            sourceBean = mPrivateData.getMianNioseSource().get(position);
        } else {
            sourceBean = new NoisePrivateData.MianNioseSourceBean();
        }
        if (sourceBean == null) {
            sourceBean = new NoisePrivateData.MianNioseSourceBean();
        }
        edSource.setEditTextStr(sourceBean.getModel());
        edSourceName.setEditTextStr(sourceBean.getName());
        edSourceNum.setEditTextStr(sourceBean.getNum());
        edSourceDate.setEditTextStr(sourceBean.getRunTime());
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
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(getContext(), message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    public void showLoading(String msg) {
        showLoadingDialog(msg);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }


    @OnClick({R.id.btn_back, R.id.linear_delete, R.id.linear_save})
    public void onClick(View v) {
        hideSoftInput();
        if (v.getId() == R.id.btn_back) {
            EventBus.getDefault().post(NOISE_FRAGMENT_INT_SOURCE, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
            return;
        }
        if (!mSample.getIsCanEdit()) {
            showMessage("提示：当前采样单，不支持编辑");
            return;
        } else
            switch (v.getId()) {
                case R.id.linear_delete:
                    deleteData();
                    break;
                case R.id.linear_save:
                    saveData();
                    break;
            }
    }

    private void saveData() {
        if (position == -1) {
            if (edSourceName.getEditTextStr() == null || edSource.getEditTextStr() == null) {
                showMessage("请填写声源信息");
                return;
            }
            sourceBean.setGuid(UUID.randomUUID().toString());
        }
        isNeedSave = true;
        sourceBean.setName(edSourceName.getEditTextStr());
        sourceBean.setModel(edSource.getEditTextStr());
        sourceBean.setNum(edSourceNum.getEditTextStr());
        sourceBean.setRunTime(edSourceDate.getEditTextStr());

        if (mSample != null) {
//            NoisePrivateData privateData = mPrivateData;
//            privateData.getMianNioseSource().add(sourceBean);
            if (position == -1) {
                mPrivateData.getMianNioseSource().add(sourceBean);
            } else {
                mPrivateData.getMianNioseSource().set(position, sourceBean);
            }

            mSample.setPrivateData(new Gson().toJson(mPrivateData));
        }
        EventBus.getDefault().post("1", NOISE_FRAGMENT_SOURCE_SHARE);
        EventBus.getDefault().post(NOISE_FRAGMENT_INT_SOURCE, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);

    }

    private void deleteData() {
        if (position == -1) {
            showMessage("您正在新建，无法删除！");
            return;
        }
        final Dialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("是否确定删除？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {// 积极
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLoading("正在删除");
                        isNeedSave = true;
                        mPrivateData.getMianNioseSource().remove(position);
                        mSample.setPrivateData(new Gson().toJson(mPrivateData));
                        hideLoading();
                        EventBus.getDefault().post("0", NOISE_FRAGMENT_SOURCE_SHARE);
                        EventBus.getDefault().post(NOISE_FRAGMENT_INT_SOURCE, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 消极

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
        isStop = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}
