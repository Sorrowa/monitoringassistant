package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

/**
 * 密封条
 */
public class SealInfo {
    /**
     * 标题
     */
    private String title;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 采样点位
     */
    private String sampingAddr;

    /**
     * 样品性质
     */
    private String type;

    /**
     * 密封时间
     */
    private String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSampingAddr() {
        return sampingAddr;
    }

    public void setSampingAddr(String sampingAddr) {
        this.sampingAddr = sampingAddr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SealInfo() {
    }

    public SealInfo(String title, String taskName, String sampingAddr, String type, String time) {
        this.title = title;
        this.taskName = taskName;
        this.sampingAddr = sampingAddr;
        this.type = type;
        this.time = time;
    }
}
