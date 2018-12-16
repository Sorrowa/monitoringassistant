package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 方法信息
 */
@Entity
public class Methods {


    /**
     * Id : 3a3865f7-f1ad-40d6-8e84-0036f2bcf565
     * Code : GB/T15502-1995
     * Name : 空气质量 苯胺类的测定 盐酸萘乙二胺分光光度法
     * Type : 国标
     */

    @Id
    private String Id;
    private String Code;
    private String Name;
    private String Type;
    @Generated(hash = 98449988)
    public Methods(String Id, String Code, String Name, String Type) {
        this.Id = Id;
        this.Code = Code;
        this.Name = Name;
        this.Type = Type;
    }
    @Generated(hash = 632479964)
    public Methods() {
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
    public String getType() {
        return this.Type;
    }
    public void setType(String Type) {
        this.Type = Type;
    }


}
