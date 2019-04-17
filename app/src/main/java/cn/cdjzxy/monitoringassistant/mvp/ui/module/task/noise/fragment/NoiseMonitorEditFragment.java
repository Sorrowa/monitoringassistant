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

import com.google.gson.Gson;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseMonitorPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;

import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment.NoiseMonitorListFragment.mPrivateDataList;

/**
 * 噪声监测数据——详情或者编辑页面
 */
public class NoiseMonitorEditFragment extends BaseFragment implements IView {
    @BindView(R.id.btn_back)
    LinearLayout linearBack;
    @BindView(R.id.text_monitor_address)
    TextView tvAddress;//测点位置
    @BindView(R.id.text_is_open)
    TextView tvIsOpen;
    @BindView(R.id.tv_monitor_date)
    TextView tvMonitorData;//监测时段
    @BindView(R.id.tv_monitor_time_first)
    TextView tvMonitorTimeFirst;//第一个监测时间
    @BindView(R.id.edit_monitor_data)
    EditText edMonitorData;//测量值
    @BindView(R.id.tv_monitor_time)
    TextView tvMonitorTime;//监测时间
    @BindView(R.id.edit_monitor_bg_data)
    EditText edMonitorBgData;//背景值
    @BindView(R.id.edit_monitor_edit_data)
    EditText edMonitorEditData;//修正值
    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_save)
    LinearLayout linearSave;
    private NoiseMonitorPrivateData privateData;

    private int position;


    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_monitor_edit, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        position = getActivity().getSharedPreferences("noise", 0).
                getInt(EventBusTags.TAG_NOISE_FRAGMENT_TYPE_POINT_EDIT, -1);

        if (position != -1) {
            privateData = mPrivateDataList != null ? mPrivateDataList.get(position) : null;
            tvAddress.setText(mSample != null ? mSample.getSamplingContentResults().get(position).getAddressName() : "");
            tvMonitorData.setText(privateData.getTimeInterval());
            tvMonitorTimeFirst.setText(privateData.getZTestTime() + "——" + privateData.getZEndTestTime());
            edMonitorData.setText(mSample != null ? mSample.getSamplingContentResults().get(position).getValue() : "");
            tvMonitorTime.setText(privateData.getYTestTime() + "——" + privateData.getYEndTestTime());
            edMonitorBgData.setText(privateData.getYBackgroundValue());
            edMonitorEditData.setText(privateData.getYCorrectedValue());
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
