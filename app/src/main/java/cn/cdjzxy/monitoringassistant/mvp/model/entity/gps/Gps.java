package cn.cdjzxy.monitoringassistant.mvp.model.entity.gps;

/**
 * @author Vondear
 * @date 2017/6/19
 */

public class Gps {

    private double longitude;
    private double latitude;
    private String collectionTime;


    public Gps() {
    }

    public Gps(double longitude, double mLatitude) {
        setLatitude(mLatitude);
        setLongitude(longitude);
    }

    public Gps(double mLatitude, double mLongitude, String collectionTime) {
        this.latitude = mLatitude;
        this.longitude = mLongitude;
        this.collectionTime = collectionTime;
    }

    public String getTime() {
        return collectionTime;
    }

    public void setnTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return latitude + "," + longitude;
    }
}