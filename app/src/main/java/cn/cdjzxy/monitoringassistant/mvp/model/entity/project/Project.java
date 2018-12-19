package cn.cdjzxy.monitoringassistant.mvp.model.entity.project;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

import cn.cdjzxy.monitoringassistant.mvp.model.greendao.converter.StringConverter;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 采样任务
 */
@Entity
public class Project {


    /**
     * Id : 601435b6-8d4d-45b1-ae79-0124dd0276cb
     * UpdateTime : 2018-11-09T13:43:50.657
     * Name : 20181108测试
     * ProjectNo : CDHJ20180015
     * Urgency : 一般
     * ContractCode : null
     * Type : 环境质量监测
     * MonType : 地表水监测
     * ClientName : null
     * ClientId : 00000000-0000-0000-0000-000000000000
     * CreaterId : 61bb48f0-5d8f-49a1-989d-7d6d7033fd91
     * CreaterName : Admin
     * RcvId : 00000000-0000-0000-0000-000000000000
     * RcvName : null
     * StartDate : 2018-11-09T00:00:00
     * EndDate : null
     * CurrentNodeType : Transfer
     * Status : 已下达
     * AssignDate : 2018-11-09T13:43:50.643
     * CreateDate : 2018-11-09T00:00:00
     * FinishState : true
     * FinishDate : 2018-11-10T00:00:00
     * SamplingUser : ["61bb48f0-5d8f-49a1-989d-7d6d7033fd91","61bb48f0-5d8f-49a1-989d-7d6d7033fd91"]
     * ProjectDetial : [{"Id":"b1bf9d54-e497-47ca-9d38-0ea766c1dbaf","ProjectId":"601435b6-8d4d-45b1-ae79-0124dd0276cb","UpdateTime":"2018-11-09T13:43:50.657","TagId":"ad7677ae-3bee-492d-bd2a-1ae830f9d9ba","TagName":"地下水","MonItemId":"58a3e4d8-d235-8841-b61d-b3cdf2641114","MethodId":"2bed9707-c1e0-5549-905b-71e9453518be","AddressId":"a05efff0-2526-40bb-98e0-ac6bf1d4ff42","Address":"林芝","Comment":null,"MonItemName":"pH值","MethodName":"水质 pH值的测定 玻璃电极法(GB6920-1986)（无资质)","Days":1,"Period":1}]
     */

    @Id
    private String              Id;
    private String              UpdateTime;
    private String              Name;
    private String              ProjectNo;
    private String              Urgency;
    private String              ContractCode;
    private String              Type;
    private String              MonType;
    private String              ClientName;
    private String              ClientId;
    private String              CreaterId;
    private String              CreaterName;
    private String              RcvId;
    private String              RcvName;
    private String              StartDate;
    private String              EndDate;
    private String              CurrentNodeType;
    private String              Status;
    private String              AssignDate;
    private String              CreateDate;
    private boolean             FinishState;
    private String              FinishDate;
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String>        SamplingUser;
    @Transient
    private List<ProjectDetial> ProjectDetials;
    @Generated(hash = 2020280327)
    public Project(String Id, String UpdateTime, String Name, String ProjectNo, String Urgency, String ContractCode, String Type, String MonType, String ClientName, String ClientId, String CreaterId, String CreaterName, String RcvId, String RcvName, String StartDate, String EndDate, String CurrentNodeType, String Status, String AssignDate, String CreateDate, boolean FinishState, String FinishDate, List<String> SamplingUser) {
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
        this.SamplingUser = SamplingUser;
    }
    @Generated(hash = 1767516619)
    public Project() {
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
    public List<String> getSamplingUser() {
        return this.SamplingUser;
    }
    public void setSamplingUser(List<String> SamplingUser) {
        this.SamplingUser = SamplingUser;
    }


    public List<ProjectDetial> getProjectDetials() {
        return ProjectDetials;
    }

    public void setProjectDetials(List<ProjectDetial> projectDetials) {
        ProjectDetials = projectDetials;
    }
}
