package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
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
    private long   Longtitude;
    private long   Latitude;
    private String TagId;
    private String TagName;
    private String UpdateTime;
    @Generated(hash = 1043661983)
    public EnvirPoint(String Id, String Name, long Longtitude, long Latitude,
            String TagId, String TagName, String UpdateTime) {
        this.Id = Id;
        this.Name = Name;
        this.Longtitude = Longtitude;
        this.Latitude = Latitude;
        this.TagId = TagId;
        this.TagName = TagName;
        this.UpdateTime = UpdateTime;
    }
    @Generated(hash = 1000807744)
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
    public long getLongtitude() {
        return this.Longtitude;
    }
    public void setLongtitude(long Longtitude) {
        this.Longtitude = Longtitude;
    }
    public long getLatitude() {
        return this.Latitude;
    }
    public void setLatitude(long Latitude) {
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
