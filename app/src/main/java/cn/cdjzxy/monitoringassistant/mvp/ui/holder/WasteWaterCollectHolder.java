package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingContent;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.WasteWaterCollectAdapter;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.Constants;

public class WasteWaterCollectHolder extends BaseHolder<SamplingContent> {

    @BindView(R.id.collect_frequency)
    TextView collect_frequency;
    @BindView(R.id.collect_date)
    TextView collect_date;
    @BindView(R.id.collect_operate)
    TextView collect_operate;
    @BindView(R.id.collect_sample_count)
    TextView collect_sample_count;
    @BindView(R.id.collect_monitor_project)
    TextView collect_monitor_project;
    @BindView(R.id.collect_live_measure)
    TextView collect_live_measure;
    @BindView(R.id.choose_img)
    ImageView choose_img;

    private WasteWaterCollectAdapter.OnWasteWaterCollectListener collectListener;

    public WasteWaterCollectHolder(View itemView, WasteWaterCollectAdapter.OnWasteWaterCollectListener collectListener) {
        super(itemView);
        this.collectListener = collectListener;
    }

    @Override
    public void setData(SamplingContent detail, int position) {
        collect_frequency.setText("频次：" + detail.getFrequecyNo());
        collect_date.setText(CheckUtil.isEmpty(detail.getSamplingTime())?"-":detail.getSamplingTime());
        collect_sample_count.setText("样品数量：" + detail.getSamplingCount());
        String monitorIdsStr = detail.getMonitemId();
        if (!CheckUtil.isEmpty(monitorIdsStr) && monitorIdsStr.length() > 0) {
            collect_monitor_project.setText("监测项目（" + monitorIdsStr.split(",").length + "）：" + detail.getMonitemName());
        }
        String monitorStr = detail.getSenceMonitemId();
        if (!CheckUtil.isEmpty(monitorStr) && monitorStr.length() > 0) {
            collect_live_measure.setText("现场监测（" + monitorStr.split(",").length + "）：" + detail.getSenceMonitemName());
        }

        if (detail.getSamplingType() == 0) {
            collect_operate.setText("");
        } else if (detail.getSamplingType() == 1) {
            collect_operate.setText(Constants.SAMPLING_TYPE_PX);
        } else if (detail.getSamplingType() == 2) {
            collect_operate.setText(Constants.SAMPLING_TYPE_KB);
            //String frequency=detail.getFrequecyNo()==0?"--":detail.getFrequecyNo()+"";
            String frequency="-";
            collect_frequency.setText("频次：" + frequency);
        }

        if (detail.isSelected()) {
            choose_img.setImageResource(R.mipmap.radio_select);
        } else {
            choose_img.setImageResource(R.mipmap.radio);
        }
        choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectListener != null) {
                    collectListener.onSelected(v, position, !detail.isSelected());
                }
            }
        });

    }

    @Override
    protected void onRelease() {
        this.collect_frequency = null;
        this.collect_date = null;
        this.collect_operate = null;
        this.collect_sample_count = null;
        this.collect_monitor_project = null;
        this.collect_live_measure = null;

    }
}
