package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoisePrivateData;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoisePointAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoiseSourceAdapter;

public class NoisePointHolder extends BaseHolder<NoisePrivateData.MianNioseAddrBean> {
    @BindView(R.id.iv_select)
    ImageView ivSelect;
    @BindView(R.id.tv_name)
    TextView tvName;
    private NoisePointAdapter.ItemClickListener listener;

    public NoisePointHolder(View v, NoisePointAdapter.ItemClickListener listener) {
        super(v);
        this.listener = listener;
    }

    @Override
    public void setData(NoisePrivateData.MianNioseAddrBean data, int position) {
        if (data.isIsChecked()) {
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
