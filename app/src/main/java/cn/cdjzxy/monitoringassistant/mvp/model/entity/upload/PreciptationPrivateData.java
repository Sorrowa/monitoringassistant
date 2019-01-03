package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 降水数据保存实体
 */

public class PreciptationPrivateData {

    /**
     * ClientName : null
     * ClientAdd : null
     * SampHight : 采样高度
     * SampArea : 采样器接雨面积
     */

    private String ClientName;
    private String ClientAdd;
    private String SampHight;
    private String SampArea;

    @JSONField(name = "ClientName")
    public String getClientName() {
        return ClientName;
    }
    @JSONField(name = "ClientName")
    public void setClientName(String clientName) {
        ClientName = clientName;
    }
    @JSONField(name = "ClientAdd")
    public String getClientAdd() {
        return ClientAdd;
    }
    @JSONField(name = "ClientAdd")
    public void setClientAdd(String clientAdd) {
        ClientAdd = clientAdd;
    }
    @JSONField(name = "SampHight")
    public String getSampHight() {
        return SampHight;
    }
    @JSONField(name = "SampHight")
    public void setSampHight(String sampHight) {
        SampHight = sampHight;
    }
    @JSONField(name = "SampArea")
    public String getSampArea() {
        return SampArea;
    }
    @JSONField(name = "SampArea")
    public void setSampArea(String sampArea) {
        SampArea = sampArea;
    }
}
