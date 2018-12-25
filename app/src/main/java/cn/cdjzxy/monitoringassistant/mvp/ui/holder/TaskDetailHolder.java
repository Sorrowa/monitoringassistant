package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.FormSelect;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskDetailAdapter;

/**
 * 主页tab
 */

public class TaskDetailHolder extends BaseHolder<Sampling> {

    @BindView(R.id.iv_cb)
    ImageView    mIvCb;
    @BindView(R.id.iv_type)
    ImageView    mIvType;
    @BindView(R.id.iv_upload)
    ImageView    mIvUpload;
    @BindView(R.id.layout_container)
    LinearLayout mLayoutContainer;
    @BindView(R.id.tv_num)
    TextView     mTvNum;
    @BindView(R.id.tv_status)
    TextView     mTvStatus;
    @BindView(R.id.tv_name)
    TextView     mTvName;
    @BindView(R.id.tv_submit_time)
    TextView     mSubmitTime;
    @BindView(R.id.tv_review_time)
    TextView     mTvReviewTime;
    @BindView(R.id.tv_point)
    TextView     mPoint;

    private TaskDetailAdapter.OnSamplingListener onSamplingListener;

    public TaskDetailHolder(View itemView, TaskDetailAdapter.OnSamplingListener onSamplingListener) {
        super(itemView);
        this.onSamplingListener = onSamplingListener;
    }

    @Override
    public void setData(Sampling data, final int position) {
        if (data.isSelected()) {
            mIvCb.setImageResource(R.mipmap.ic_cb_checked);
        } else {
            mIvCb.setImageResource(R.mipmap.ic_cb_nor);
        }

        mIvCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSamplingListener.onSelected(v, position);
            }
        });

        mIvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSamplingListener.onUpload(v, position);
            }
        });

        mLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSamplingListener.onClick(v, position);
            }
        });

        Tags tags = DBHelper.get().getTagsDao().queryBuilder().where(TagsDao.Properties.Id.eq(data.getParentTagId())).unique();

        if ("水质".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_water);
        } else if ("煤质".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_coal);
        } else if ("振动".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_shock);
        } else if ("土壤".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_soil);
        } else if ("降水".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_precipitation);
        } else if ("固废".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_solid);
        } else if ("辐射".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_radiation);
        } else if ("废气".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_gas);
        } else if ("噪声".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_noise);
        } else if ("空气".equals(tags.getName())) {
            mIvType.setImageResource(R.mipmap.ic_air);
        }

        mTvNum.setText(data.getSamplingNo());
        mTvStatus.setText(data.getStatusName());
        mTvName.setText(data.getFormName());
        mPoint.setText(data.getAddressName());

        if (data.getStatus() == 0) {
            mIvUpload.setImageResource(R.mipmap.ic_upload);
            mSubmitTime.setVisibility(View.GONE);
            mTvReviewTime.setVisibility(View.GONE);
            mIvCb.setEnabled(true);
            mIvUpload.setEnabled(true);
            mLayoutContainer.setEnabled(true);
        } else {
            mIvCb.setEnabled(false);
            mIvUpload.setEnabled(false);
            mLayoutContainer.setEnabled(false);
            mIvUpload.setImageResource(R.mipmap.ic_finish);
            mSubmitTime.setVisibility(View.VISIBLE);
            mTvReviewTime.setVisibility(View.VISIBLE);
            mSubmitTime.setText("提交：" + data.getSubmitDate());
            mTvReviewTime.setText("审核：" + data.getAuditDate());
        }
    }

    @Override
    protected void onRelease() {
        this.mIvCb = null;
        this.mIvType = null;
        this.mIvUpload = null;
        this.mLayoutContainer = null;
        this.mTvNum = null;
        this.mTvStatus = null;
        this.mTvName = null;
        this.mSubmitTime = null;
        this.mTvReviewTime = null;
        this.mPoint = null;
    }
}
