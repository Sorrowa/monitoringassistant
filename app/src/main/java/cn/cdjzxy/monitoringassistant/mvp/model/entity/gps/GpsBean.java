package cn.cdjzxy.monitoringassistant.mvp.model.entity.gps;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class GpsBean {
    @Id
    String id;
    String coordType; //坐标类型
    double longitude;
    double latitude;
    String locType;//定位类型：gps 网络  无效定位
    String collectionTime;//时间
    @Generated(hash = 1164164028)
    public GpsBean(String id, String coordType, double longitude, double latitude,
            String locType, String collectionTime) {
        this.id = id;
        this.coordType = coordType;
        this.longitude = longitude;
        this.latitude = latitude;
        this.locType = locType;
        this.collectionTime = collectionTime;
    }
    @Generated(hash = 1084355342)
    public GpsBean() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCoordType() {
        return this.coordType;
    }
    public void setCoordType(String coordType) {
        this.coordType = coordType;
    }
    public double getLongitude() {
        return this.longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getLatitude() {
        return this.latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public String getLocType() {
        return this.locType;
    }
    public void setLocType(String locType) {
        this.locType = locType;
    }
    public String getCollectionTime() {
        return this.collectionTime;
    }
    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }
    

}
