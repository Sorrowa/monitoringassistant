package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Table {


    /**
     * Id : 29380d4a-4f27-421f-a750-3d015f12c42b
     * ProjectId : 601435b6-8d4d-45b1-ae79-0124dd0276cb
     * ProjectName : 20181108测试
     * montype : 环境质量监测
     * SamplingNo : CD20181105-W111310
     * FormName : 水和废水采样及交接记录
     * ParentTagId : d877c7d5-6bb8-42d4-a79c-0f644e130a62
     * TagId : ad7677ae-3bee-492d-bd2a-1ae830f9d9ba
     * TagName : 地下水
     * AddressId : a05efff0-2526-40bb-98e0-ac6bf1d4ff42
     * AddressName : 林芝
     * SamplingUserId : 61bb48f0-5d8f-49a1-989d-7d6d7033fd91
     * SamplingUserName : Admin
     * SubmitId : 61bb48f0-5d8f-49a1-989d-7d6d7033fd91
     * SubmitName : Admin
     * SamplingTimeBegin : 2018-11-05T00:00:00
     * SamplingTimeEnd : 2018-11-05T00:00:00
     * Status : 7
     * StatusName : 等待校核审核
     * UpdateTime : 2018-11-09T13:49:00.5
     */

    @Id
    private String Id;
    private String ProjectId;
    private String ProjectName;
    private String montype;
    private String SamplingNo;
    private String FormName;
    private String ParentTagId;
    private String TagId;
    private String TagName;
    private String AddressId;
    private String AddressName;
    private String SamplingUserId;
    private String SamplingUserName;
    private String SubmitId;
    private String SubmitName;
    private String SamplingTimeBegin;
    private String SamplingTimeEnd;
    private int    Status;
    private String StatusName;
    private String UpdateTime;
    @Generated(hash = 426210253)
    public Table(String Id, String ProjectId, String ProjectName, String montype,
            String SamplingNo, String FormName, String ParentTagId, String TagId,
            String TagName, String AddressId, String AddressName,
            String SamplingUserId, String SamplingUserName, String SubmitId,
            String SubmitName, String SamplingTimeBegin, String SamplingTimeEnd,
            int Status, String StatusName, String UpdateTime) {
        this.Id = Id;
        this.ProjectId = ProjectId;
        this.ProjectName = ProjectName;
        this.montype = montype;
        this.SamplingNo = SamplingNo;
        this.FormName = FormName;
        this.ParentTagId = ParentTagId;
        this.TagId = TagId;
        this.TagName = TagName;
        this.AddressId = AddressId;
        this.AddressName = AddressName;
        this.SamplingUserId = SamplingUserId;
        this.SamplingUserName = SamplingUserName;
        this.SubmitId = SubmitId;
        this.SubmitName = SubmitName;
        this.SamplingTimeBegin = SamplingTimeBegin;
        this.SamplingTimeEnd = SamplingTimeEnd;
        this.Status = Status;
        this.StatusName = StatusName;
        this.UpdateTime = UpdateTime;
    }
    @Generated(hash = 752389689)
    public Table() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getProjectId() {
        return this.ProjectId;
    }
    public void setProjectId(String ProjectId) {
        this.ProjectId = ProjectId;
    }
    public String getProjectName() {
        return this.ProjectName;
    }
    public void setProjectName(String ProjectName) {
        this.ProjectName = ProjectName;
    }
    public String getMontype() {
        return this.montype;
    }
    public void setMontype(String montype) {
        this.montype = montype;
    }
    public String getSamplingNo() {
        return this.SamplingNo;
    }
    public void setSamplingNo(String SamplingNo) {
        this.SamplingNo = SamplingNo;
    }
    public String getFormName() {
        return this.FormName;
    }
    public void setFormName(String FormName) {
        this.FormName = FormName;
    }
    public String getParentTagId() {
        return this.ParentTagId;
    }
    public void setParentTagId(String ParentTagId) {
        this.ParentTagId = ParentTagId;
    }
    public String getTagId() {
        return this.TagId;
    }
    public void setTagId(String TagId) {
        this.TagId = TagId;
    }
    public String getTagName() {
        return this.TagName;
    }
    public void setTagName(String TagName) {
        this.TagName = TagName;
    }
    public String getAddressId() {
        return this.AddressId;
    }
    public void setAddressId(String AddressId) {
        this.AddressId = AddressId;
    }
    public String getAddressName() {
        return this.AddressName;
    }
    public void setAddressName(String AddressName) {
        this.AddressName = AddressName;
    }
    public String getSamplingUserId() {
        return this.SamplingUserId;
    }
    public void setSamplingUserId(String SamplingUserId) {
        this.SamplingUserId = SamplingUserId;
    }
    public String getSamplingUserName() {
        return this.SamplingUserName;
    }
    public void setSamplingUserName(String SamplingUserName) {
        this.SamplingUserName = SamplingUserName;
    }
    public String getSubmitId() {
        return this.SubmitId;
    }
    public void setSubmitId(String SubmitId) {
        this.SubmitId = SubmitId;
    }
    public String getSubmitName() {
        return this.SubmitName;
    }
    public void setSubmitName(String SubmitName) {
        this.SubmitName = SubmitName;
    }
    public String getSamplingTimeBegin() {
        return this.SamplingTimeBegin;
    }
    public void setSamplingTimeBegin(String SamplingTimeBegin) {
        this.SamplingTimeBegin = SamplingTimeBegin;
    }
    public String getSamplingTimeEnd() {
        return this.SamplingTimeEnd;
    }
    public void setSamplingTimeEnd(String SamplingTimeEnd) {
        this.SamplingTimeEnd = SamplingTimeEnd;
    }
    public int getStatus() {
        return this.Status;
    }
    public void setStatus(int Status) {
        this.Status = Status;
    }
    public String getStatusName() {
        return this.StatusName;
    }
    public void setStatusName(String StatusName) {
        this.StatusName = StatusName;
    }
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }


}
