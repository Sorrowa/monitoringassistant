package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 仪器法详细信息私有数据
 */
public class InstrumentalDetailPrivateData {

    /**
     * {
     * "SamplingOnTime":"10:34"//分析时间,
     * "CaleValue":"14"//分析结果,
     * "RPDValue":"+3.7"//相对偏差,
     * "HasPX":true //做了平行传true 没做平行传false
     * }
     */

    private String SamplingOnTime;
    private String CaleValue;
    private String RPDValue;
    private Boolean HasPX;

    @JSONField(name = "SamplingOnTime")
    public String getSamplingOnTime() {
        return SamplingOnTime;
    }

    @JSONField(name = "SamplingOnTime")
    public void setSamplingOnTime(String samplingOnTime) {
        SamplingOnTime = samplingOnTime;
    }

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

    @JSONField(name = "HasPX")
    public Boolean getHasPX() {
        return HasPX;
    }

    @JSONField(name = "HasPX")
    public void setHasPX(Boolean hasPX) {
        HasPX = hasPX;
    }
}
