package cn.cdjzxy.monitoringassistant.mvp.model.entity.project;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ProjectDetial {

    /**
     * Id : b1bf9d54-e497-47ca-9d38-0ea766c1dbaf
     * ProjectId : 601435b6-8d4d-45b1-ae79-0124dd0276cb
     * UpdateTime : 2018-11-09T13:43:50.657
     * TagId : ad7677ae-3bee-492d-bd2a-1ae830f9d9ba
     * TagName : 地下水
     * MonItemId : 58a3e4d8-d235-8841-b61d-b3cdf2641114
     * MethodId : 2bed9707-c1e0-5549-905b-71e9453518be
     * AddressId : a05efff0-2526-40bb-98e0-ac6bf1d4ff42
     * Address : 林芝
     * Comment : null
     * MonItemName : pH值
     * MethodName : 水质 pH值的测定 玻璃电极法(GB6920-1986)（无资质)
     * Days : 1
     * Period : 1
     * ProjectContentId : 0fea7814-efc1-4a40-98fe-00f0796e78f0
     * TagParentId : d877c7d5-6bb8-42d4-a79c-0f644e130a62
     * TagParentName : 水质
     */

    @Id
    private String Id;
    private String ProjectId;
    private String UpdateTime;
    private String TagId;
    private String TagName;
    private String MonItemId;
    private String MethodId;
    private String AddressId;
    private String Address;
    private String Comment;
    private String MonItemName;
    private String MethodName;
    private int    Days;
    private int    Period;
    private String ProjectContentId;
    private String TagParentId;
    private String TagParentName;
    @Generated(hash = 475900743)
    public ProjectDetial(String Id, String ProjectId, String UpdateTime,
            String TagId, String TagName, String MonItemId, String MethodId,
            String AddressId, String Address, String Comment, String MonItemName,
            String MethodName, int Days, int Period, String ProjectContentId,
            String TagParentId, String TagParentName) {
        this.Id = Id;
        this.ProjectId = ProjectId;
        this.UpdateTime = UpdateTime;
        this.TagId = TagId;
        this.TagName = TagName;
        this.MonItemId = MonItemId;
        this.MethodId = MethodId;
        this.AddressId = AddressId;
        this.Address = Address;
        this.Comment = Comment;
        this.MonItemName = MonItemName;
        this.MethodName = MethodName;
        this.Days = Days;
        this.Period = Period;
        this.ProjectContentId = ProjectContentId;
        this.TagParentId = TagParentId;
        this.TagParentName = TagParentName;
    }
    @Generated(hash = 1852370034)
    public ProjectDetial() {
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
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
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
    public String getMonItemId() {
        return this.MonItemId;
    }
    public void setMonItemId(String MonItemId) {
        this.MonItemId = MonItemId;
    }
    public String getMethodId() {
        return this.MethodId;
    }
    public void setMethodId(String MethodId) {
        this.MethodId = MethodId;
    }
    public String getAddressId() {
        return this.AddressId;
    }
    public void setAddressId(String AddressId) {
        this.AddressId = AddressId;
    }
    public String getAddress() {
        return this.Address;
    }
    public void setAddress(String Address) {
        this.Address = Address;
    }
    public String getComment() {
        return this.Comment;
    }
    public void setComment(String Comment) {
        this.Comment = Comment;
    }
    public String getMonItemName() {
        return this.MonItemName;
    }
    public void setMonItemName(String MonItemName) {
        this.MonItemName = MonItemName;
    }
    public String getMethodName() {
        return this.MethodName;
    }
    public void setMethodName(String MethodName) {
        this.MethodName = MethodName;
    }
    public int getDays() {
        return this.Days;
    }
    public void setDays(int Days) {
        this.Days = Days;
    }
    public int getPeriod() {
        return this.Period;
    }
    public void setPeriod(int Period) {
        this.Period = Period;
    }
    public String getProjectContentId() {
        return this.ProjectContentId;
    }
    public void setProjectContentId(String ProjectContentId) {
        this.ProjectContentId = ProjectContentId;
    }
    public String getTagParentId() {
        return this.TagParentId;
    }
    public void setTagParentId(String TagParentId) {
        this.TagParentId = TagParentId;
    }
    public String getTagParentName() {
        return this.TagParentName;
    }
    public void setTagParentName(String TagParentName) {
        this.TagParentName = TagParentName;
    }

}
