package cn.cdjzxy.monitoringassistant.mvp.model.entity.project;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;


@Entity
public class ProjectContent {

    /**
     * Id : a8fea199-d4ba-40ab-8ba3-edff82df4221
     * ProjectId : 1e9c4eb8-612c-4404-8e74-3bc22aef4f8d
     * UpdateTime : 2019-04-19 09:46:02
     * TagId : 7e5c590d-d594-4861-87b1-d0a822f60e46
     * TagName : 地表水
     * AddressIds : e8c99ba6-1025-423d-a0d4-663f54a57f05,e410f5e6-5464-4758-8f98-626aeff4da63,87fe77fc-6075-444f-9a91-1da58653a212,fe1fb5d6-7f46-4bb3-b092-3d1c166fb745
     * Address : 拦河堰（新都）、马家提灌站、南四支三斗（工业东区入口处）、南四支三斗（新都出境）
     * TagParentId : d877c7d5-6bb8-42d4-a79c-0f644e130a62
     * TagParentName : 水质
     * Days : 1
     * Period : 1
     */

    @Id
    private String Id;
    private String ProjectId;
    private String UpdateTime;
    private String TagId;
    private String TagName;
    private String AddressIds;
    private String Address;
    private String TagParentId;
    private String TagParentName;
    private int Days;
    private int Period;

    @Transient
    private String MonItemNames;
    private String MonItemIds;
    @Keep()
    public ProjectContent(String id, String projectId, String updateTime, String tagId, String tagName, String addressIds, String address, String tagParentId, String tagParentName, int days, int period) {
        Id = id;
        ProjectId = projectId;
        UpdateTime = updateTime;
        TagId = tagId;
        TagName = tagName;
        AddressIds = addressIds;
        Address = address;
        TagParentId = tagParentId;
        TagParentName = tagParentName;
        Days = days;
        Period = period;
    }
    @Keep()
    public ProjectContent() {
    }
    @Generated(hash = 1983004678)
    public ProjectContent(String Id, String ProjectId, String UpdateTime, String TagId, String TagName, String AddressIds, String Address, String TagParentId, String TagParentName, int Days, int Period,
            String MonItemIds) {
        this.Id = Id;
        this.ProjectId = ProjectId;
        this.UpdateTime = UpdateTime;
        this.TagId = TagId;
        this.TagName = TagName;
        this.AddressIds = AddressIds;
        this.Address = Address;
        this.TagParentId = TagParentId;
        this.TagParentName = TagParentName;
        this.Days = Days;
        this.Period = Period;
        this.MonItemIds = MonItemIds;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String ProjectId) {
        this.ProjectId = ProjectId;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public String getTagId() {
        return TagId;
    }

    public void setTagId(String TagId) {
        this.TagId = TagId;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String TagName) {
        this.TagName = TagName;
    }

    public String getAddressIds() {
        return AddressIds;
    }

    public void setAddressIds(String AddressIds) {
        this.AddressIds = AddressIds;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getTagParentId() {
        return TagParentId;
    }

    public void setTagParentId(String TagParentId) {
        this.TagParentId = TagParentId;
    }

    public String getTagParentName() {
        return TagParentName;
    }

    public void setTagParentName(String TagParentName) {
        this.TagParentName = TagParentName;
    }

    public int getDays() {
        return Days;
    }

    public void setDays(int Days) {
        this.Days = Days;
    }

    public int getPeriod() {
        return Period;
    }

    public void setPeriod(int Period) {
        this.Period = Period;
    }

    public String getMonItemIds() {
        return this.MonItemIds;
    }
    public void setMonItemIds(String MonItemIds) {
        this.MonItemIds = MonItemIds;
    }

    public String getMonItemNames() {
        return MonItemNames;
    }

    public void setMonItemNames(String monItemNames) {
        MonItemNames = monItemNames;
    }
}
