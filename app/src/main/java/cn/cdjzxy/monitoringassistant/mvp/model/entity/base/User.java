package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class User {

    /**
     * Id : 61bb48f0-5d8f-49a1-989d-7d6d7033fd91
     * Code : liuyang
     * Name : Admin
     */

    @org.greenrobot.greendao.annotation.Id
    private String Id;
    private String Code;
    private String Name;
    @Generated(hash = 1259622395)
    public User(String Id, String Code, String Name) {
        this.Id = Id;
        this.Code = Code;
        this.Name = Name;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getCode() {
        return this.Code;
    }
    public void setCode(String Code) {
        this.Code = Code;
    }
    public String getName() {
        return this.Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
}
