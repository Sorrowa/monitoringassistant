package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Unit {

    @Id
    private String Id;
    private String Name;
    @Generated(hash = 1123566035)
    public Unit(String Id, String Name) {
        this.Id = Id;
        this.Name = Name;
    }
    @Generated(hash = 1236212320)
    public Unit() {
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
}
