package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PendingTasks {


    /**
     * id : 00000000-0000-0000-0000-000000000000
     * updateTime : 2018-12-05T01:14:59.867Z
     * name : string
     * projectNo : string
     * urgency : string
     * contractCode : string
     * type : string
     * monType : string
     * clientName : string
     * clientId : 00000000-0000-0000-0000-000000000000
     * createrId : 00000000-0000-0000-0000-000000000000
     * createrName : string
     * rcvId : 00000000-0000-0000-0000-000000000000
     * rcvName : string
     * startDate : 2018-12-05T01:14:59.867Z
     * endDate : 2018-12-05T01:14:59.867Z
     * currentNodeType : string
     * status : string
     * assignDate : 2018-12-05T01:14:59.867Z
     * createDate : 2018-12-05T01:14:59.867Z
     * finishState : true
     * finishDate : 2018-12-05T01:14:59.867Z
     */

    private String  id;
    private String  updateTime;
    private String  name;
    private String  projectNo;
    private String  urgency;
    private String  contractCode;
    private String  type;
    private String  monType;
    private String  clientName;
    private String  clientId;
    private String  createrId;
    private String  createrName;
    private String  rcvId;
    private String  rcvName;
    private String  startDate;
    private String  endDate;
    private String  currentNodeType;
    private String  status;
    private String  assignDate;
    private String  createDate;
    private boolean finishState;
    private String  finishDate;
    @Generated(hash = 914994481)
    public PendingTasks(String id, String updateTime, String name, String projectNo,
            String urgency, String contractCode, String type, String monType,
            String clientName, String clientId, String createrId,
            String createrName, String rcvId, String rcvName, String startDate,
            String endDate, String currentNodeType, String status,
            String assignDate, String createDate, boolean finishState,
            String finishDate) {
        this.id = id;
        this.updateTime = updateTime;
        this.name = name;
        this.projectNo = projectNo;
        this.urgency = urgency;
        this.contractCode = contractCode;
        this.type = type;
        this.monType = monType;
        this.clientName = clientName;
        this.clientId = clientId;
        this.createrId = createrId;
        this.createrName = createrName;
        this.rcvId = rcvId;
        this.rcvName = rcvName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentNodeType = currentNodeType;
        this.status = status;
        this.assignDate = assignDate;
        this.createDate = createDate;
        this.finishState = finishState;
        this.finishDate = finishDate;
    }
    @Generated(hash = 604985946)
    public PendingTasks() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getProjectNo() {
        return this.projectNo;
    }
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }
    public String getUrgency() {
        return this.urgency;
    }
    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }
    public String getContractCode() {
        return this.contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getMonType() {
        return this.monType;
    }
    public void setMonType(String monType) {
        this.monType = monType;
    }
    public String getClientName() {
        return this.clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public String getClientId() {
        return this.clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getCreaterId() {
        return this.createrId;
    }
    public void setCreaterId(String createrId) {
        this.createrId = createrId;
    }
    public String getCreaterName() {
        return this.createrName;
    }
    public void setCreaterName(String createrName) {
        this.createrName = createrName;
    }
    public String getRcvId() {
        return this.rcvId;
    }
    public void setRcvId(String rcvId) {
        this.rcvId = rcvId;
    }
    public String getRcvName() {
        return this.rcvName;
    }
    public void setRcvName(String rcvName) {
        this.rcvName = rcvName;
    }
    public String getStartDate() {
        return this.startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return this.endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getCurrentNodeType() {
        return this.currentNodeType;
    }
    public void setCurrentNodeType(String currentNodeType) {
        this.currentNodeType = currentNodeType;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getAssignDate() {
        return this.assignDate;
    }
    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }
    public String getCreateDate() {
        return this.createDate;
    }
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public boolean getFinishState() {
        return this.finishState;
    }
    public void setFinishState(boolean finishState) {
        this.finishState = finishState;
    }
    public String getFinishDate() {
        return this.finishDate;
    }
    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }


}
