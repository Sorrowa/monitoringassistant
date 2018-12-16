package cn.cdjzxy.monitoringassistant.mvp.model.entity.project;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 采样任务
 */
@Entity
public class Task {

    /**
     * Id : 303e5205-4ca6-41ec-b25d-01fdd6f5a969
     * UpdateTime : 2018-11-12T11:39:16.247
     * Name : 降水立项
     * ProjectNo : CDHJ20180011
     * Urgency : 一般
     * ContractCode : 321
     * Type : 环境质量监测
     * MonType : 降水监测
     * ClientName : null
     * ClientId : null
     * CreaterId : 6e2c449a-9ce1-412b-bac8-ff7c369814e0
     * CreaterName : Admin
     * RcvId : null
     * RcvName : null
     * StartDate : 2018-11-12T00:00:00
     * EndDate : null
     * CurrentNodeType : Sampling
     * Status : 已下达
     * AssignDate : 2018-11-12T11:39:16.24
     * CreateDate : 2018-11-12T00:00:00
     * FinishState : false
     * FinishDate : null
     */

    @Id
    private String  Id;
    private String  UpdateTime;
    private String  Name;
    private String  ProjectNo;
    private String  Urgency;
    private String  ContractCode;
    private String  Type;
    private String  MonType;
    private String  ClientName;
    private String  ClientId;
    private String  CreaterId;
    private String  CreaterName;
    private String  RcvId;
    private String  RcvName;
    private String  StartDate;
    private String  EndDate;
    private String  CurrentNodeType;
    private String  Status;
    private String  AssignDate;
    private String  CreateDate;
    private boolean FinishState;
    private String  FinishDate;

    @Generated(hash = 1623893222)
    public Task(String Id, String UpdateTime, String Name, String ProjectNo,
            String Urgency, String ContractCode, String Type, String MonType,
            String ClientName, String ClientId, String CreaterId,
            String CreaterName, String RcvId, String RcvName, String StartDate,
            String EndDate, String CurrentNodeType, String Status,
            String AssignDate, String CreateDate, boolean FinishState,
            String FinishDate) {
        this.Id = Id;
        this.UpdateTime = UpdateTime;
        this.Name = Name;
        this.ProjectNo = ProjectNo;
        this.Urgency = Urgency;
        this.ContractCode = ContractCode;
        this.Type = Type;
        this.MonType = MonType;
        this.ClientName = ClientName;
        this.ClientId = ClientId;
        this.CreaterId = CreaterId;
        this.CreaterName = CreaterName;
        this.RcvId = RcvId;
        this.RcvName = RcvName;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.CurrentNodeType = CurrentNodeType;
        this.Status = Status;
        this.AssignDate = AssignDate;
        this.CreateDate = CreateDate;
        this.FinishState = FinishState;
        this.FinishDate = FinishDate;
    }
    @Generated(hash = 733837707)
    public Task() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
    public String getName() {
        return this.Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getProjectNo() {
        return this.ProjectNo;
    }
    public void setProjectNo(String ProjectNo) {
        this.ProjectNo = ProjectNo;
    }
    public String getUrgency() {
        return this.Urgency;
    }
    public void setUrgency(String Urgency) {
        this.Urgency = Urgency;
    }
    public String getContractCode() {
        return this.ContractCode;
    }
    public void setContractCode(String ContractCode) {
        this.ContractCode = ContractCode;
    }
    public String getType() {
        return this.Type;
    }
    public void setType(String Type) {
        this.Type = Type;
    }
    public String getMonType() {
        return this.MonType;
    }
    public void setMonType(String MonType) {
        this.MonType = MonType;
    }
    public String getClientName() {
        return this.ClientName;
    }
    public void setClientName(String ClientName) {
        this.ClientName = ClientName;
    }
    public String getClientId() {
        return this.ClientId;
    }
    public void setClientId(String ClientId) {
        this.ClientId = ClientId;
    }
    public String getCreaterId() {
        return this.CreaterId;
    }
    public void setCreaterId(String CreaterId) {
        this.CreaterId = CreaterId;
    }
    public String getCreaterName() {
        return this.CreaterName;
    }
    public void setCreaterName(String CreaterName) {
        this.CreaterName = CreaterName;
    }
    public String getRcvId() {
        return this.RcvId;
    }
    public void setRcvId(String RcvId) {
        this.RcvId = RcvId;
    }
    public String getRcvName() {
        return this.RcvName;
    }
    public void setRcvName(String RcvName) {
        this.RcvName = RcvName;
    }
    public String getStartDate() {
        return this.StartDate;
    }
    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }
    public String getEndDate() {
        return this.EndDate;
    }
    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }
    public String getCurrentNodeType() {
        return this.CurrentNodeType;
    }
    public void setCurrentNodeType(String CurrentNodeType) {
        this.CurrentNodeType = CurrentNodeType;
    }
    public String getStatus() {
        return this.Status;
    }
    public void setStatus(String Status) {
        this.Status = Status;
    }
    public String getAssignDate() {
        return this.AssignDate;
    }
    public void setAssignDate(String AssignDate) {
        this.AssignDate = AssignDate;
    }
    public String getCreateDate() {
        return this.CreateDate;
    }
    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }
    public boolean getFinishState() {
        return this.FinishState;
    }
    public void setFinishState(boolean FinishState) {
        this.FinishState = FinishState;
    }
    public String getFinishDate() {
        return this.FinishDate;
    }
    public void setFinishDate(String FinishDate) {
        this.FinishDate = FinishDate;
    }



}
