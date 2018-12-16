package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.other.Tab;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Task;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

/**
 * 主页tab
 */

public class TaskHolder extends BaseHolder<Task> {

    @BindView(R.id.iv_task_type)
    ImageView mIvTaskType;
    @BindView(R.id.tv_task_name)
    TextView  mTvTaskName;
    @BindView(R.id.tv_task_time_range)
    TextView  mTvTaskTimeRange;
    @BindView(R.id.tv_task_num)
    TextView  mTvTaskNum;
    @BindView(R.id.tv_task_point)
    TextView  mTvTaskPoint;
    @BindView(R.id.tv_task_project_num)
    TextView  mTvTaskProjectNum;
    @BindView(R.id.tv_task_type)
    TextView  mTvTaskType;
    @BindView(R.id.tv_task_person)
    TextView  mTvTaskPerson;
    @BindView(R.id.tv_task_start_time)
    TextView  mTvTaskStartTime;

    public TaskHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(Task data, int position) {
        mIvTaskType.setVisibility(View.VISIBLE);
        mTvTaskName.setText(data.getName());
        String currentTime = DateUtils.getDate();
        String endTime = data.getAssignDate();
        int lastDays = DateUtils.getLastDays(currentTime, endTime.split("T")[0]);
        if (lastDays <= 1) {
            mTvTaskTimeRange.setTextColor(Color.parseColor("#ff0000"));
        } else if (lastDays <= 3) {
            mTvTaskTimeRange.setTextColor(Color.parseColor("#ffbe00"));
        } else {
            mTvTaskTimeRange.setTextColor(Color.parseColor("#333333"));
        }
        mTvTaskTimeRange.setText(data.getCreateDate().split("T")[0].replace("-","/") + "~" + data.getAssignDate().split("T")[0].replace("-","/"));
        mTvTaskNum.setText("任务编号:" + data.getProjectNo());
        mTvTaskPoint.setText("点位:" + data.getContractCode());
        mTvTaskProjectNum.setText("项目:" + data.getContractCode());
        mTvTaskType.setText("样品性质:" + data.getMonType());
        mTvTaskPerson.setText("无");
        mTvTaskStartTime.setText("下达:" + data.getAssignDate().split("T")[0].replace("-","/"));
    }

    @Override
    protected void onRelease() {
        this.mIvTaskType = null;
        this.mTvTaskName = null;
        this.mTvTaskTimeRange = null;
        this.mTvTaskNum = null;
        this.mTvTaskPoint = null;
        this.mTvTaskProjectNum = null;
        this.mIvTaskType = null;
        this.mTvTaskPerson = null;
        this.mTvTaskStartTime = null;

    }
}
