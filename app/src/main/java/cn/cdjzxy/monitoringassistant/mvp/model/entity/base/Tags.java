package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Tags {

    /**
     * Id : 20366344-3f7b-4b57-879a-076ccfdf9334
     * Name : 区域噪声
     * ParentId : 8a08d498-9936-4b2e-bbcb-cc79a061f73b
     * level : 1
     */
    @Id
    private String Id;
    private String Name;
    private String ParentId;
    private int    level;
    @Generated(hash = 519331555)
    public Tags(String Id, String Name, String ParentId, int level) {
        this.Id = Id;
        this.Name = Name;
        this.ParentId = ParentId;
        this.level = level;
    }
    @Generated(hash = 1290390976)
    public Tags() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getName() {
        return this.Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getParentId() {
        return this.ParentId;
    }
    public void setParentId(String ParentId) {
        this.ParentId = ParentId;
    }
    public int getLevel() {
        return this.level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

}
