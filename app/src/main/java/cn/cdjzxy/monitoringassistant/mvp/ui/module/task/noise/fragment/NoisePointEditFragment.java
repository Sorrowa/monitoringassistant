package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;

/**
 * 噪声——列表编辑页面
 */
public class NoisePointEditFragment extends BaseFragment implements IView {

    @BindView(R.id.btn_back)
    LinearLayout linearBack;
    @BindView(R.id.edit_point_id)
    EditText edPointId;
    @BindView(R.id.edit_point_address)
    EditText edPointAddress;
    @BindView(R.id.edit_point_category)
    EditText edPointCategory;
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
        position = getActivity().getSharedPreferences("noise", 0).
                getInt(EventBusTags.TAG_NOISE_FRAGMENT_TYPE_POINT_EDIT, -1);
        if (position != -1) {
            addrBean = mPrivateData != null ? mPrivateData.getMianNioseAddr().get(position) : null;
            edPointId.setText(addrBean.getAddrCode());
            edPointAddress.setText(addrBean.getAddressName());
            edPointCategory.setText(addrBean.getFuncType());
            edPointComment.setText(addrBean.getRemark());
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

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }
}
