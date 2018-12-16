package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Dic {


    /**
     * Id : fb729456-279f-402f-8739-bce7576b7151
     * Code : 1
     * Name : 国控
     * Type : 7
     */

    @Id
    private String Id;
    private String Code;
    private String Name;
    private int    Type;
    @Generated(hash = 400093017)
    public Dic(String Id, String Code, String Name, int Type) {
        this.Id = Id;
        this.Code = Code;
        this.Name = Name;
        this.Type = Type;
    }
    @Generated(hash = 114904903)
    public Dic() {
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
    public int getType() {
        return this.Type;
    }
    public void setType(int Type) {
        this.Type = Type;
    }

}
