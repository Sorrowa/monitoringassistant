package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class MethodTagRelation {


    /**
     * id : f7822132-7a9c-4616-b98f-002934da5f0d
     * methodId : 2b9cdfef-b6c9-4a3f-92fd-0ec5ac48915a
     * tagId : 17ab6f44-cec6-4e45-94b1-b81f7158119e
     */
    @Id
    private String Id;
    private String MethodId;
    private String TagId;
    @Generated(hash = 1040507137)
    public MethodTagRelation(String Id, String MethodId, String TagId) {
        this.Id = Id;
        this.MethodId = MethodId;
        this.TagId = TagId;
    }
    @Generated(hash = 1149229255)
    public MethodTagRelation() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getMethodId() {
        return this.MethodId;
    }
    public void setMethodId(String MethodId) {
        this.MethodId = MethodId;
    }
    public String getTagId() {
        return this.TagId;
    }
    public void setTagId(String TagId) {
        this.TagId = TagId;
    }


}
