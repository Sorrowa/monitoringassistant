package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MonItemTagRelation {


    /**
     * Id : ae31de4a-00a2-496b-8ca8-00245db842bb
     * MonItemId : c4a2e9ea-a436-4634-bdd5-aac353212f89
     * TagId : 17ab6f44-cec6-4e45-94b1-b81f7158119e
     */
    @Id
    private String Id;
    private String MonItemId;
    private String TagId;
    @Generated(hash = 478583243)
    public MonItemTagRelation(String Id, String MonItemId, String TagId) {
        this.Id = Id;
        this.MonItemId = MonItemId;
        this.TagId = TagId;
    }
    @Generated(hash = 2041616331)
    public MonItemTagRelation() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getMonItemId() {
        return this.MonItemId;
    }
    public void setMonItemId(String MonItemId) {
        this.MonItemId = MonItemId;
    }
    public String getTagId() {
        return this.TagId;
    }
    public void setTagId(String TagId) {
        this.TagId = TagId;
    }


}
