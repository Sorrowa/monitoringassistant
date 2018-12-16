package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Rights {

    /**
     * EnumName : 委托查看
     * EnumValue : 10001
     */

    private String EnumName;
    private int    EnumValue;
    @Generated(hash = 1201011745)
    public Rights(String EnumName, int EnumValue) {
        this.EnumName = EnumName;
        this.EnumValue = EnumValue;
    }
    @Generated(hash = 40682534)
    public Rights() {
    }
    public String getEnumName() {
        return this.EnumName;
    }
    public void setEnumName(String EnumName) {
        this.EnumName = EnumName;
    }
    public int getEnumValue() {
        return this.EnumValue;
    }
    public void setEnumValue(int EnumValue) {
        this.EnumValue = EnumValue;
    }
}
