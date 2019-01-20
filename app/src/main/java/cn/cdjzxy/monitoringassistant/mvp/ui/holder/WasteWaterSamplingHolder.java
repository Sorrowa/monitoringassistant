package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;


public class WasteWaterSamplingHolder extends BaseHolder<SamplingDetail> {

    @BindView(R.id.tvSamplingCode)
    TextView tvSamplingCode;
    @BindView(R.id.tvFrequency)
    TextView tvFrequency;
    @BindView(R.id.tvPoint)
    TextView tvPoint;

    public WasteWaterSamplingHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SamplingDetail data, int position) {
        tvSamplingCode.setText(data.getSampingCode());
        tvFrequency.setText(data.getFrequecyNo() + "");
        tvPoint.setText(data.getTempValue3());
    }

    @Override
    protected void onRelease() {
        this.tvSamplingCode = null;
        this.tvFrequency = null;
        this.tvPoint = null;
    }
}
