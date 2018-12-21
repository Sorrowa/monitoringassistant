package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;

/**
 * 主页tab
 */

public class TaskDetailHolder extends BaseHolder<Sampling> {

    @BindView(R.id.iv_cb)
    ImageView mIvCb;
    @BindView(R.id.iv_type)
    ImageView mIvType;
    @BindView(R.id.iv_upload)
    ImageView mIvUpload;
    @BindView(R.id.tv_num)
    TextView  mTvNum;
    @BindView(R.id.tv_status)
    TextView  mTvStatus;
    @BindView(R.id.tv_name)
    TextView  mTvName;
    @BindView(R.id.tv_submit_time)
    TextView  mSubmitTime;
    @BindView(R.id.tv_review_time)
    TextView  mTvReviewTime;
    @BindView(R.id.tv_point)
    TextView  mPoint;

    public TaskDetailHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Sampling data, int position) {
        if (data.isSelected()) {
            mIvCb.setImageResource(R.mipmap.ic_cb_checked);
        } else {
            mIvCb.setImageResource(R.mipmap.ic_cb_nor);
        }

        mTvNum.setText(data.getSamplingNo());
        mTvStatus.setText(data.getStatusName());
        mTvName.setText(data.getFormName());
        mPoint.setText(data.getAddressName());

        if (data.getStatus() == 0) {
            mIvUpload.setImageResource(R.mipmap.ic_upload);
            mSubmitTime.setVisibility(View.GONE);
            mTvReviewTime.setVisibility(View.GONE);
        } else {
            mIvUpload.setImageResource(R.mipmap.ic_finish);
            mSubmitTime.setVisibility(View.VISIBLE);
            mTvReviewTime.setVisibility(View.VISIBLE);
            mSubmitTime.setText("提交：" + data.getSendSampTime());
            mTvReviewTime.setText("审核：" + data.getReciveTime());
        }
    }

    @Override
    protected void onRelease() {
        this.mIvCb = null;
        this.mIvType = null;
        this.mIvUpload = null;
        this.mTvNum = null;
        this.mTvStatus = null;
        this.mTvName = null;
        this.mSubmitTime = null;
        this.mTvReviewTime = null;
        this.mPoint = null;
    }
}
