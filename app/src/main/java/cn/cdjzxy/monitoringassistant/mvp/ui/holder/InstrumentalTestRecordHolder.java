package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;

/**
 * 仪器法监测结果
 */

public class InstrumentalTestRecordHolder extends BaseHolder<SamplingDetail> {

    @BindView(R.id.tv_frequency)
    TextView tvFrequency;

    @BindView(R.id.tv_analyse_time)
    TextView tvAnalyseTime;

    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.tv_analyse_result)
    TextView tvAnalyseResult;

    @BindView(R.id.tv_point)
    TextView tvPoint;

    @BindView(R.id.tv_has_PX)
    TextView tvHasPX;

    @BindView(R.id.tv_ave_value)
    TextView tvAveValue;

    @BindView(R.id.tv_rela_offset)
    TextView tvRelaOffset;

    public InstrumentalTestRecordHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(SamplingDetail data, int position) {
        tvFrequency.setText("频次：" + data.getFrequecyNo());
        tvTime.setText(data.getSamplingTime());//TODO:SamplingOnTime
        tvAveValue.setText(data.getValue());//均值
//        tvPoint.setText("点位：");TODO

        tvAnalyseTime.setText("分析时间：" + data.getPrivateDataStringValue("SamplingOnTime"));
        tvAnalyseResult.setText("分析结果：" + data.getPrivateDataStringValue("CaleValue"));
        tvHasPX.setText(data.getPrivateDataBooleanValue("HasPX") ? "平行" : "样品");
        tvRelaOffset.setText("相对偏差" + data.getPrivateDataStringValue("RPDValue"));//相对偏差
    }

    @Override
    protected void onRelease() {
        this.tvFrequency = null;
        this.tvAnalyseTime = null;
        this.tvTime = null;
        this.tvAnalyseResult = null;
        this.tvPoint = null;
        this.tvAveValue = null;
        this.tvRelaOffset = null;
    }
}