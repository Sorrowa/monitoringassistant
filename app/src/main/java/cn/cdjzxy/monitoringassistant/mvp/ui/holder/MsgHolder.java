package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.msg.Msg;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;

/**
 * 主页tab
 */

public class MsgHolder extends BaseHolder<Msg> {

    @BindView(R.id.tv_title)
    TextView  mTvTitle;
    @BindView(R.id.tv_time)
    TextView  mTvTime;
    @BindView(R.id.tv_content)
    TextView  mTvContent;
    @BindView(R.id.iv_status)
    ImageView mIvStatus;

    public MsgHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Msg data, int position) {
        mTvTitle.setText(data.getMsgTitle());
        mTvTime.setText(data.getSendTime().replace("T", " "));
        mTvContent.setText(Html.fromHtml(data.getMsgContent()));
        if (data.getMsgStatus() == 0) {
            mIvStatus.setVisibility(View.VISIBLE);
        } else {
            mIvStatus.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRelease() {
        this.mTvTitle = null;
        this.mTvTime = null;
        this.mTvContent = null;

    }
}
