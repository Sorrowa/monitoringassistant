package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;

/**
 *仪器法监测结果
 */

public class InstrumentalTestRecordHolder extends BaseHolder<Object> {

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

    @BindView(R.id.tv_ave_value)
    TextView tvAveValue;

    @BindView(R.id.tv_rela_offset)
    TextView tvRelaOffset;

    public InstrumentalTestRecordHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Object data, int position) {
//        tvFrequency.setText("频率：" + data.getFrequecyNo());
//        try {
//            JSONObject privateData = new JSONObject(data.getPrivateData());
//            tvTime.setText(privateData.getString("SDataTime") + "--" +privateData.getString("EDataTime"));
//            tvRainwaterVolume.setText("雨水体积(ml)：" + privateData.getString("RainVol"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        tvPrecipitation.setText("降雨量(mm)：" + data.getValue());
//        tvRemark.setText("备注：" + data.getDescription());
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
