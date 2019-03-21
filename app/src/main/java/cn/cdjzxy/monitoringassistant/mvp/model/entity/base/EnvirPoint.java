package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class EnvirPoint {


    /**
     * Id : d2f6dd2f-c021-41a6-aee9-078930bda7f7
     * Name : 胡市大桥
     * Longtitude : 105.3589
     * Latitude : 28.9533
     * TagId : 7e5c590d-d594-4861-87b1-d0a822f60e46
     * TagName : 地表水
     * UpdateTime : 2018-11-09T17:06:26.043
     */

    @Id
    private String Id;
    private String Name;
    private String Code;
    private double Longtitude;
    private double Latitude;
    private String TagId;
    private String TagName;
    private String UpdateTime;
    @Transient
    private boolean isSelected;

    @Keep()
    public EnvirPoint(String Id, String Name, String Code, double Longtitude,
                      double Latitude, String TagId, String TagName, String UpdateTime) {
        this.Id = Id;
        this.Name = Name;
        this.Code = Code;
        this.Longtitude = Longtitude;
        this.Latitude = Latitude;
        this.TagId = TagId;
        this.TagName = TagName;
        this.UpdateTime = UpdateTime;
    }

    @Keep()
    public EnvirPoint() {
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

    public String getCode() {
        return this.Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public double getLongtitude() {
        return this.Longtitude;
    }

    public void setLongtitude(double Longtitude) {
        this.Longtitude = Longtitude;
    }

    public double getLatitude() {
        return this.Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public String getTagId() {
        return this.TagId;
    }

    public void setTagId(String TagId) {
        this.TagId = TagId;
    }

    public String getTagName() {
        return this.TagName;
    }

    public void setTagName(String TagName) {
        this.TagName = TagName;
    }

    public String getUpdateTime() {
        return this.UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
