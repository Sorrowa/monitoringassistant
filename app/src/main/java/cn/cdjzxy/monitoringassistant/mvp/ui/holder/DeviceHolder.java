package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Devices;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

/**
 * 主页tab
 */

public class DeviceHolder extends BaseHolder<Devices> {


    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_state)
    TextView mTvState;
    @BindView(R.id.tv_model)
    TextView mTvModel;
    @BindView(R.id.tv_numbering)
    TextView mTvNumbering;
    @BindView(R.id.tv_validity_period)
    TextView mTvValidityPeriod;

    public DeviceHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Devices data, int position) {
        mTvName.setText(data.getName().trim());
        mTvState.setText(data.getSourceWay().trim());
        mTvModel.setText(data.getSpecification().trim());
        if (CheckUtil.isEmpty(data.getDevCode())) {
            mTvNumbering.setVisibility(View.INVISIBLE);
        } else {
            mTvNumbering.setVisibility(View.VISIBLE);
            mTvNumbering.setText(data.getDevCode().trim());
        }
        if (CheckUtil.isEmpty(data.getExpireDate())) {
            mTvValidityPeriod.setVisibility(View.INVISIBLE);
        } else {
            mTvValidityPeriod.setVisibility(View.VISIBLE);
            mTvValidityPeriod.setText("有效期：" +DateUtils.strGetDate(data.getExpireDate()) );
        }
    }

    @Override
    protected void onRelease() {
        this.mTvName = null;
        this.mTvState = null;
        this.mTvModel = null;
        this.mTvNumbering = null;
        this.mTvValidityPeriod = null;
    }


}
