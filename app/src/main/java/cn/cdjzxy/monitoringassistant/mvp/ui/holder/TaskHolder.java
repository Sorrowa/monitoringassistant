package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.ProjectDetialDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.UserDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;

/**
 * 主页tab
 */

public class TaskHolder extends BaseHolder<Project> {

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
    public void setData(Project data, int position) {
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
        StringBuilder users = new StringBuilder("");
        List<String> userIds = data.getSamplingUser();
        if (!CheckUtil.isEmpty(userIds)) {
            for (String userId : userIds) {
                User user = DBHelper.get().getUserDao().queryBuilder().where(UserDao.Properties.Id.eq(userId)).unique();
                if (!CheckUtil.isNull(user)) {
                    users.append(user.getName() + ",");
                }
            }
        }

        StringBuilder monItems = new StringBuilder("");
        StringBuilder points = new StringBuilder("");

        List<ProjectDetial> projectDetials = DBHelper.get().getProjectDetialDao().queryBuilder().where(ProjectDetialDao.Properties.ProjectId.eq(data.getId())).list();
        if (!CheckUtil.isEmpty(projectDetials)) {
            for (ProjectDetial projectDetial : projectDetials) {
                monItems.append(projectDetial.getMonItemName() + ",");
                points.append(projectDetial.getAddress() + ",");
            }
        }

       if (users.lastIndexOf(",") > 0){
           users.deleteCharAt(users.lastIndexOf(","));
       }

        if (monItems.lastIndexOf(",") > 0){
            monItems.deleteCharAt(monItems.lastIndexOf(","));
        }

        if (points.lastIndexOf(",") > 0){
            points.deleteCharAt(points.lastIndexOf(","));
        }


        mTvTaskTimeRange.setText(data.getCreateDate().split("T")[0].replace("-", "/") + "~" + data.getAssignDate().split("T")[0].replace("-", "/"));
        mTvTaskNum.setText("任务编号:" + data.getProjectNo());
        mTvTaskPoint.setText("点位:" + points.toString());
        mTvTaskProjectNum.setText("项目:" + monItems.toString());
        mTvTaskType.setText("样品性质:" + data.getMonType());
        mTvTaskPerson.setText("人员：" + users.toString());
        mTvTaskStartTime.setText("下达:" + data.getAssignDate().split("T")[0].replace("-", "/"));
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
