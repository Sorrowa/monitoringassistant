package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

public class NoiseMonitorPrivateData {

    /**
     * TimeInterval : 哈哈哈
     * ZTestTime : 05:00
     * ZEndTestTime : 04:00
     * YTestTime : 03:00
     * YEndTestTime : 05:00
     * YBackgroundValue : 57687
     * YCorrectedValue : 6786
     */

    private String id;
    private String TimeInterval;
    private String ZTestTime;
    private String ZEndTestTime;
    private String YTestTime;
    private String YEndTestTime;
    private String YBackgroundValue;
    private String YCorrectedValue;
    private boolean isChecked;
    private String AddressName;
    private String value;
    private String addressId;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAddressName() {
        return AddressName;
    }

    public void setAddressName(String addressName) {
        AddressName = addressName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTimeInterval() {
        return TimeInterval;
    }

    public void setTimeInterval(String TimeInterval) {
        this.TimeInterval = TimeInterval;
    }

    public String getZTestTime() {
        if (ZTestTime == null) ZTestTime = "";
        return ZTestTime;
    }

    public void setZTestTime(String ZTestTime) {
        this.ZTestTime = ZTestTime;
    }

    public String getZEndTestTime() {
        return ZEndTestTime;
    }

    public void setZEndTestTime(String ZEndTestTime) {
        this.ZEndTestTime = ZEndTestTime;
    }

    public String getYTestTime() {
        return YTestTime;
    }

    public void setYTestTime(String YTestTime) {
        this.YTestTime = YTestTime;
    }

    public String getYEndTestTime() {
        return YEndTestTime;
    }

    public void setYEndTestTime(String YEndTestTime) {
        this.YEndTestTime = YEndTestTime;
    }

    public String getYBackgroundValue() {
        return YBackgroundValue;
    }

    public void setYBackgroundValue(String YBackgroundValue) {
        this.YBackgroundValue = YBackgroundValue;
    }

    public String getYCorrectedValue() {
        return YCorrectedValue;
    }

    public void setYCorrectedValue(String YCorrectedValue) {
        this.YCorrectedValue = YCorrectedValue;
    }
}
