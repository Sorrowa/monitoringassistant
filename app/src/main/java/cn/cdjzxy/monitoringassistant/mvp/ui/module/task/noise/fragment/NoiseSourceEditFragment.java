package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wonders.health.lib.base.integration.cache.Cache;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;


/**
 * 主要噪声源——详情或者编辑界面
 */
public class NoiseSourceEditFragment extends BaseFragment implements IView {
    @BindView(R.id.btn_back)
    LinearLayout linearBack;//返回上级的title
    @BindView(R.id.edit_source)
    EditText edSource;//主要声源
    @BindView(R.id.edit_source_name)
    EditText edSourceName;//声源名称
    @BindView(R.id.edit_source_num)
    EditText edSourceNum;//数量
    @BindView(R.id.tv_source_date)
    TextView tvSourceDate;//运行时间和状况
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_save)
    LinearLayout linearSave;
    private NoisePrivateData.MianNioseSourceBean sourceBean;
    private int position;


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_source_edit, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        position= getActivity().getSharedPreferences("noise", 0).
                getInt(EventBusTags.TAG_NOISE_FRAGMENT_TYPE_SOURCE_EDIT,-1);
        if (position!=-1){
            sourceBean = mPrivateData != null ? mPrivateData.getMianNioseSource().get(position) : null;
            edSource.setText(sourceBean.getName());
            edSourceName.setText(sourceBean.getModel());
            edSourceNum.setText(sourceBean.getNum());
            tvSourceDate.setText(sourceBean.getRunTime());
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
