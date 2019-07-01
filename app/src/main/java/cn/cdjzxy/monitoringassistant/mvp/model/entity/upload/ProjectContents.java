package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

import java.util.List;


import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.ProjectDetial;

/**
 * 上传表单数据模型
 */
public class ProjectContents {
    /**
     * "Id": "00000000-0000-0000-0000-000000000000",
     * "ProjectId": "00000000-0000-0000-0000-000000000000",
     * "UpdateTime": "2019-06-19T07:10:42.504Z",
     * "TagId": "00000000-0000-0000-0000-000000000000",
     * "TagName": "string",
     * "AddressIds": "string",
     * "Address": "string",
     * "Status": 0,
     * "GroupBy": "00000000-0000-0000-0000-000000000000",
     * "EvaluateId": "00000000-0000-0000-0000-000000000000",
     * "EvaluateName": "string",
     * "Days": 0,
     * "Period": 0,
     * "Comment": "string",
     * "Index": 0,
     * "TagParentId": "00000000-0000-0000-0000-000000000000",
     * "TagParentName": "string",
     */

    private String Id;
    private String ProjectId;
    private String UpdateTime;
    private String TagId;
    private String TagName;
    private String AddressIds;
    private String Address;
    private String AddressCount;
    private String  Status;
    private int Days;
    private int Period;
    private String TagParentId;
    private String TagParentName;
    private String MonItemCount;
    private String MonItemsName;

    private List<AddressArrBean>     AddressArr;
    private List<ProjectDetial> ProjectDetials;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public String getTagId() {
        return TagId;
    }

    public void setTagId(String tagId) {
        TagId = tagId;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String tagName) {
        TagName = tagName;
    }

    public String getAddressIds() {
        return AddressIds;
    }

    public void setAddressIds(String addressIds) {
        AddressIds = addressIds;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAddressCount() {
        return AddressCount;
    }

    public void setAddressCount(String addressCount) {
        AddressCount = addressCount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getDays() {
        return Days;
    }

    public void setDays(int days) {
        Days = days;
    }

    public int getPeriod() {
        return Period;
    }

    public void setPeriod(int period) {
        Period = period;
    }

    public String getTagParentId() {
        return TagParentId;
    }

    public void setTagParentId(String tagParentId) {
        TagParentId = tagParentId;
    }

    public String getTagParentName() {
        return TagParentName;
    }

    public void setTagParentName(String tagParentName) {
        TagParentName = tagParentName;
    }

    public String getMonItemCount() {
        return MonItemCount;
    }

    public void setMonItemCount(String monItemCount) {
        MonItemCount = monItemCount;
    }

    public String getMonItemsName() {
        return MonItemsName;
    }

    public void setMonItemsName(String monItemsName) {
        MonItemsName = monItemsName;
    }



    public List<AddressArrBean> getAddressArr() {
        return AddressArr;
    }

    public void setAddressArr(List<AddressArrBean> addressArr) {
        AddressArr = addressArr;
    }

    public List<ProjectDetial> getProjectDetials() {
        return ProjectDetials;
    }

    public void setProjectDetials(List<ProjectDetial> projectDetials) {
        ProjectDetials = projectDetials;
    }

    public static class AddressArrBean {
        /**
         * Id : dac0ed09-8e98-4e5f-89e7-0cae6358aa39
         * Name : 22222
         */

        private String       Id;
        private String       Name;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

    }

}
