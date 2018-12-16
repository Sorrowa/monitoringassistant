package cn.cdjzxy.monitoringassistant.mvp.model.entity.qr;

import lombok.Data;

/**
 * 二维扫描结果
 */
@Data
public class QrMoreInfo {
    private String Content; //结果值
    private int    Type;//类型  2 相对路径 3 绝对路径
}
