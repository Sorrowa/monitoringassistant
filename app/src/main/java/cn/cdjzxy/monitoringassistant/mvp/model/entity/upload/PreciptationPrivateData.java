package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

/**
 * 降水数据保存实体
 */
@Data
public class PreciptationPrivateData {

    /**
     * ClientName : null
     * ClientAdd : null
     * SampHight : 采样高度
     * SampArea : 采样器接雨面积
     */
    @JSONField(name = "ClientName")
    private String ClientName;
    @JSONField(name = "ClientAdd")
    private String ClientAdd;
    @JSONField(name = "SampHight")
    private String SampHight;
    @JSONField(name = "SampArea")
    private String SampArea;

}
