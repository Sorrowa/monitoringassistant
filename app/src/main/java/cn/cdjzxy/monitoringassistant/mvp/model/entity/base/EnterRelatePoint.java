package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class EnterRelatePoint {


    /**
     * Id : d19ff323-0e3e-49d7-87e9-00d640684927
     * EnterPriseId : 564d29e6-e890-4737-a195-b024f353d249
     * Name : 其它点位
     * Longtitude : null
     * Latitude : null
     * TagId : bdefb077-fe60-466b-a28c-d3f17c7fed6f
     * TagName : null
     * UpdateTime : 2018-12-12T14:59:26.41
     */

    @Id
    private String Id;
    private String EnterPriseId;
    private String Name;
    private String Code;
    private String Longtitude;
    private String Latitude;
    private String TagId;
    private String TagName;
    private String UpdateTime;

    @Generated(hash = 882419821)
    public EnterRelatePoint(String Id, String EnterPriseId, String Name,
                            String Code, String Longtitude, String Latitude, String TagId,
                            String TagName, String UpdateTime) {
        this.Id = Id;
        this.EnterPriseId = EnterPriseId;
        this.Name = Name;
        this.Code = Code;
        this.Longtitude = Longtitude;
        this.Latitude = Latitude;
        this.TagId = TagId;
        this.TagName = TagName;
        this.UpdateTime = UpdateTime;
    }

    @Generated(hash = 2128730093)
    public EnterRelatePoint() {
    }

    public String getId() {
        return this.Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getEnterPriseId() {
        return this.EnterPriseId;
    }

    public void setEnterPriseId(String EnterPriseId) {
        this.EnterPriseId = EnterPriseId;
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

    public String getLongtitude() {
        return this.Longtitude;
    }

    public void setLongtitude(String Longtitude) {
        this.Longtitude = Longtitude;
    }

    public String getLatitude() {
        return this.Latitude;
    }

    public void setLatitude(String Latitude) {
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


}
