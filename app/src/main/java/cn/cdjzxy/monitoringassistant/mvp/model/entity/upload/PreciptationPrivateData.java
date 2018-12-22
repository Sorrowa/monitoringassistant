package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

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

    private String ClientName;
    private String ClientAdd;
    private String SampHight;
    private String SampArea;

}
