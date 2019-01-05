package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 仪器法私有数据
 */
public class InstrumentalPrivateData {

    /**
     * {
     * "CaleValue"://传空字符串,
     * "RPDValue"://传空字符串,
     * "SamplingOnTime"://传空字符串,
     * "HasPX"://传false,
     * "FormTypeName":"地下水"//样品性质 ,
     * "SourceWay":"检定"//仪器溯源方式,
     * "SourceDate":"2028-02-28"//仪器溯源有效期,
     * "DeviceText":"css1(1201012)(检定2028-02-28)"//仪器名称(仪器编号)(仪器溯源方式 仪器溯源有效期)
     * }
     */
    private String CaleValue;
    private String RPDValue;
    private String SamplingOnTime;
    private Boolean HasPX;
    private String FormTypeName;
    private String SourceWay;
    private String SourceDate;
    private String DeviceText;

    @JSONField(name = "CaleValue")
    public String getCaleValue() {
        return CaleValue;
    }

    @JSONField(name = "CaleValue")
    public void setCaleValue(String caleValue) {
        CaleValue = caleValue;
    }

    @JSONField(name = "RPDValue")
    public String getRPDValue() {
        return RPDValue;
    }

    @JSONField(name = "RPDValue")
    public void setRPDValue(String RPDValue) {
        this.RPDValue = RPDValue;
    }

    @JSONField(name = "SamplingOnTime")
    public String getSamplingOnTime() {
        return SamplingOnTime;
    }

    @JSONField(name = "SamplingOnTime")
    public void setSamplingOnTime(String samplingOnTime) {
        SamplingOnTime = samplingOnTime;
    }

    @JSONField(name = "HasPX")
    public Boolean getHasPX() {
        return HasPX;
    }

    @JSONField(name = "HasPX")
    public void setHasPX(Boolean hasPX) {
        HasPX = hasPX;
    }

    @JSONField(name = "FormTypeName")
    public String getFormTypeName() {
        return FormTypeName;
    }

    @JSONField(name = "FormTypeName")
    public void setFormTypeName(String formTypeName) {
        FormTypeName = formTypeName;
    }

    @JSONField(name = "SourceWay")
    public String getSourceWay() {
        return SourceWay;
    }

    @JSONField(name = "SourceWay")
    public void setSourceWay(String sourceWay) {
        SourceWay = sourceWay;
    }

    @JSONField(name = "SourceDate")
    public String getSourceDate() {
        return SourceDate;
    }

    @JSONField(name = "SourceDate")
    public void setSourceDate(String sourceDate) {
        SourceDate = sourceDate;
    }

    @JSONField(name = "DeviceText")
    public String getDeviceText() {
        return DeviceText;
    }

    @JSONField(name = "DeviceText")
    public void setDeviceText(String deviceText) {
        DeviceText = deviceText;
    }
}
