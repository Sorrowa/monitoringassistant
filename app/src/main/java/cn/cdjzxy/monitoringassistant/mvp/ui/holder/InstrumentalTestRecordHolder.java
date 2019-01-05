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
//        tvPoint.setText("点位：");

        try {
            JSONObject privateData = new JSONObject(data.getPrivateData());
            tvAnalyseTime.setText("分析时间：" + privateData.getString("SamplingOnTime"));
            tvAnalyseResult.setText("分析结果：" + privateData.getString("CaleValue"));
            tvHasPX.setText(privateData.getBoolean("HasPX")?"平行":"样品");
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        tvAveValue//TODO:均值
//        tvRelaOffset//TODO:相对偏差
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