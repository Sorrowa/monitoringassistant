package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseMonitorPrivateData;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoiseMonitorAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoiseSourceAdapter;

public class NoiseMonitorHolder extends BaseHolder<NoiseMonitorPrivateData> {
    @BindView(R.id.iv_select)
    ImageView ivSelect;
    @BindView(R.id.tv_name)
    TextView tvName;
    private NoiseMonitorAdapter.ItemClickListener listener;

    public NoiseMonitorHolder(View v, NoiseMonitorAdapter.ItemClickListener listener) {
        super(v);
        this.listener = listener;
    }

    @Override
    public void setData(NoiseMonitorPrivateData data, int position) {
        if (data.isChecked()) {
            ivSelect.setImageResource(R.mipmap.ic_cb_checked);
        } else {
            ivSelect.setImageResource(R.mipmap.ic_cb_nor);
        }
        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelected(v, position);
                }
            }
        });
        tvName.setText(data.getAddressName());
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(v, position);
                }
            }
        });
    }
}
