package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;

/**
 * 仪器法监测结果
 */

public class InstrumentalTestRecordHolder extends BaseHolder<SamplingDetail> {

    @BindView(R.id.ivChoose)
    ImageView ivChoose;

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

    @BindView(R.id.ivDetails)
    ImageView ivDetails;

    int position = 0;

    public InstrumentalTestRecordHolder(View itemView) {
        super(itemView);

        ivDetails.setOnClickListener(clickListener);
    }

    @Override
    public void setData(SamplingDetail data, int position) {
        this.position = position;

        if (data.isCanSelect()) {
            ivChoose.setVisibility(View.VISIBLE);

            if (data.isSelect()) {
                ivChoose.setImageResource(R.mipmap.ic_cb_checked);
            } else {
                ivChoose.setImageResource(R.mipmap.ic_cb_nor);
            }
        } else {
            ivChoose.setVisibility(View.INVISIBLE);
        }

        tvFrequency.setText("频次：" + data.getFrequecyNo());
        tvTime.setText(data.getSamplingTime());
        tvPoint.setText(data.getAddressName());
        tvHasPX.setText(data.getPrivateDataBooleanValue("HasPX") ? "平行" : "样品");

        tvAnalyseTime.setText("分析时间：" + data.getPrivateDataStringValue("SamplingOnTime"));
        tvAnalyseResult.setText("分析结果：" + data.getPrivateDataStringValue("CaleValue"));
        tvAveValue.setText("均值：" + data.getValue());//均值
        String rpdValue = data.getPrivateDataStringValue("RPDValue");
        tvRelaOffset.setText("相对偏差：" + (TextUtils.isEmpty(rpdValue) ? "" : rpdValue + "%"));//相对偏差
    }

    @Override
    protected void onRelease() {
        this.ivDetails.setOnClickListener(null);
        this.ivDetails = null;
        this.clickListener = null;
        this.tvFrequency = null;
        this.tvAnalyseTime = null;
        this.tvTime = null;
        this.tvAnalyseResult = null;
        this.tvPoint = null;
        this.tvAveValue = null;
        this.tvRelaOffset = null;
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(2, EventBusTags.TAG_INSTRUMENTAL_RECORD);

            SharedPreferences collectListSettings = v.getContext().getSharedPreferences("setting", 0);
            SharedPreferences.Editor editor = collectListSettings.edit();
            editor.putInt("listPosition", position);
            editor.commit();
        }
    };
}