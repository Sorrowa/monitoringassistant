package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.wonders.health.lib.base.base.BaseHolder;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Tags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.TagsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.TaskDetailAdapter;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import io.reactivex.functions.Consumer;
import kotlin.Unit;

/**
 * 主页tab
 */

public class TaskDetailHolder extends BaseHolder<Sampling> {

    @BindView(R.id.ivChoose)
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

        RxView.clicks(mIvUpload)
                .throttleFirst(3, TimeUnit.SECONDS)//在一秒内只取第一次点击
                .subscribe(new Consumer<Unit>() {
                    @Override
                    public void accept(Unit unit) {
                        onSamplingListener.onUpload(mIvUpload, position);
                    }
                });


        //        mIvUpload.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                onSamplingListener.onUpload(v, position);
        //            }
        //        });

        mLayoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSamplingListener.onClick(v, position);
            }
        });

        mLayoutContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onSamplingListener.onLongClick(v, position);
                return false;
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

        if (data.getSamplingUserId().contains(UserInfoHelper.get().getUserInfo().getId())) {
            mIvType.setBackgroundResource(R.drawable.shape_task_detail_blue);
        } else {
            mIvType.setBackgroundResource(R.drawable.shape_task_detail_yellow);
        }

        if (data.getStatus() == 0 || data.getStatus() == 4 || data.getStatus() == 9) {
            mIvUpload.setImageResource(R.mipmap.ic_upload);
            mSubmitTime.setVisibility(View.GONE);
            mTvReviewTime.setVisibility(View.GONE);
            mIvCb.setEnabled(true);
            mIvUpload.setEnabled(true);
            mLayoutContainer.setEnabled(true);
        } else {
            mIvCb.setEnabled(false);
            mIvUpload.setEnabled(false);
            mIvUpload.setImageResource(R.mipmap.ic_finish);

            if (CheckUtil.isEmpty(data.getSubmitDate())) {
                mSubmitTime.setVisibility(View.INVISIBLE);
            } else {
                mSubmitTime.setVisibility(View.VISIBLE);
                mSubmitTime.setText("提交：" + data.getSubmitDate().replace("T", ""));
            }

            if (CheckUtil.isEmpty(data.getAuditDate())) {
                mTvReviewTime.setVisibility(View.INVISIBLE);
            } else {
                mTvReviewTime.setVisibility(View.VISIBLE);
                mTvReviewTime.setText("审核：" + data.getAuditDate().replace("T", ""));
            }

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
