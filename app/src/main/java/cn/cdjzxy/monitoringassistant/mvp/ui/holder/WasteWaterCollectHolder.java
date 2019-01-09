package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

public class WasteWaterCollectHolder extends BaseHolder<SamplingDetail> {

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

    public WasteWaterCollectHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SamplingDetail detail, int position) {
        collect_frequency.setText("频次：" + detail.getFrequecyNo());
        collect_date.setText(detail.getSamplingTime());
        collect_sample_count.setText("样品数量：" + detail.getSamplingCount());
        String monitorIdsStr=detail.getMonitemId();
        if (!CheckUtil.isEmpty(monitorIdsStr) && monitorIdsStr.length()>0){
            collect_monitor_project.setText("监测项目（"+ monitorIdsStr.split(",").length+"）：" + detail.getMonitemName());
        }
        String monitorStr=detail.getAddresssId();
        if (!CheckUtil.isEmpty(monitorStr) && monitorStr.length()>0){
            collect_live_measure.setText("现场监测（"+monitorStr.split(",").length+"）：" + detail.getAddressName());
        }
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
