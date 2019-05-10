package cn.cdjzxy.monitoringassistant.utils;


import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;

public class ProjectUtil {
    /**
     * 获取当前项目剩余时间
     * 此方法用于获取项目的剩余时间和当前时间进行比较
     *
     * @param project
     * @return
     */
    public int getSampleLastDay(Project project) {
        String nowTime = DateUtils.getDate();
        String proJectTime = project.getPlanEndTime();
        if (nowTime == null
                || nowTime.equals("")
                || proJectTime == null
                || proJectTime.equals("")
                ) {
            return -1;
        }
        return 0;
    }
}
