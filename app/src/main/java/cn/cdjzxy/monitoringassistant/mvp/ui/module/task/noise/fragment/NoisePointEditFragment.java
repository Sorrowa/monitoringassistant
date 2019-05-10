package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.onactivityresult.AvoidOnResult;

import org.simple.eventbus.EventBus;

import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.EnterRelatePointSelectActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.point.PointSelectActivity;
import cn.cdjzxy.monitoringassistant.widgets.MyDrawableLinearLayout;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_INT_POINT;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_POINT_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.isNeedSave;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mProject;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;

/**
 * 噪声——列表编辑页面
 */
public class NoisePointEditFragment extends BaseFragment implements IView {

    @BindView(R.id.btn_back)
    LinearLayout linearBack;
    @BindView(R.id.my_layout_point_id)
    MyDrawableLinearLayout edPointId;
    @BindView(R.id.my_layout_point_address)
    MyDrawableLinearLayout tvPointAddress;
    @BindView(R.id.my_layout_point_category)
    MyDrawableLinearLayout edPointCategory;
    @BindView(R.id.edit_point_comment)
    EditText edPointComment;
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_save)
    LinearLayout linearSave;
    private int position;
    private NoisePrivateData.MianNioseAddrBean addrBean;


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_point_edit, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        edPointId.setEditTextStr("");
        tvPointAddress.setEditTextStr("");
        edPointCategory.setEditTextStr("");
        edPointComment.setText("");

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void setViewData() {
        position = getActivity().getSharedPreferences(NOISE_FRAGMENT_SHARE, 0) == null ? -1 :
                getActivity().getSharedPreferences(NOISE_FRAGMENT_SHARE, 0).
                        getInt(NOISE_FRAGMENT_POINT_SHARE, -1);
        if (position != -1) {
            addrBean = mPrivateData.getMianNioseAddr().get(position);
        } else {
            addrBean = new NoisePrivateData.MianNioseAddrBean();
        }
        if (addrBean == null) {
            addrBean = new NoisePrivateData.MianNioseAddrBean();
        }
        edPointId.setEditTextStr(addrBean.getAddrCode());
        tvPointAddress.setRightTextStr(addrBean.getAddressName());
        edPointCategory.setEditTextStr(addrBean.getFuncType());
        edPointComment.setText(addrBean.getRemark());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            setViewData();
        }
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


    @OnClick({R.id.btn_back, R.id.linear_delete, R.id.linear_save, R.id.my_layout_point_address})
    public void onClick(View v) {
        hideSoftInput();
        if (v.getId() == R.id.btn_back) {
            EventBus.getDefault().post(NOISE_FRAGMENT_INT_POINT, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
            return;
        } else if (!mSample.getIsCanEdit()) {
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
                case R.id.my_layout_point_address:
                    choicePoint();
                    break;
            }
    }

    /**
     * 选择点位
     */
    private void choicePoint() {
        Intent intent = new Intent();

        if (mSample.getMontype() == 3) {//环境质量 在环境质量点位中找
//            intent.setClass(getContext(), PointSelectActivity.class);
//            intent.putExtra("tagId", tags.getId());
//            intent.putExtra("projectId", mSample.getProjectId());
            showMessage("您选择的表单暂无环境质量监测点位");
            return;
        } else {//污染源 在企业点位中找
            Tags tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Name.eq("厂界噪声")).unique();
            intent.setClass(getContext(), EnterRelatePointSelectActivity.class);
            intent.putExtra("projectId", mSample.getProjectId());
            intent.putExtra("tagId", tags.getId());
            intent.putExtra("rcvId", mProject.getRcvId());
        }
        new AvoidOnResult(getActivity()).startForResult(intent, new AvoidOnResult.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (resultCode == Activity.RESULT_OK) {
                    mSample.setAddressName(data.getStringExtra("Address"));
                    mSample.setAddressId(data.getStringExtra("AddressId"));
                    mSample.setAddressNo(data.getStringExtra("AddressNo"));
                    tvPointAddress.setRightTextStr(mSample.getAddressName());
                }
            }
        });
    }

    private void saveData() {
        addrBean.setAddressName(tvPointAddress.getRightTextViewStr());
        if (addrBean.getAddressName() == null || addrBean.getAddressName().equals("")) {
            showMessage("请选择测点位置");
            return;
        }
        isNeedSave = true;
        if (position == -1) {
            addrBean.setGuid(UUID.randomUUID().toString());
        }
        addrBean.setAddrCode(edPointId.getEditTextStr());

        addrBean.setFuncType(edPointCategory.getEditTextStr());
        addrBean.setRemark(edPointComment.getText().toString());

        if (mSample != null) {
            if (position == -1) {
                mPrivateData.getMianNioseAddr().add(addrBean);
            } else {
                mPrivateData.getMianNioseAddr().set(position, addrBean);
            }

            mSample.setPrivateData(new Gson().toJson(mPrivateData));
        }
        NoiseFactoryActivity.saveMySample();
        EventBus.getDefault().post("", NOISE_FRAGMENT_POINT_SHARE);
        EventBus.getDefault().post(NOISE_FRAGMENT_INT_POINT, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
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
                        for (int i = 0; i < mPrivateData.getMianNioseAddr().size(); i++) {
                            if (addrBean.getGuid().equals(mPrivateData.getMianNioseAddr().get(i).getGuid())) {
                                mPrivateData.getMianNioseAddr().remove(i);
                            }
                        }
                        hideLoading();
                        EventBus.getDefault().post(NOISE_FRAGMENT_INT_POINT, EventBusTags.TAG_NOISE_FRAGMENT_TYPE);
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
