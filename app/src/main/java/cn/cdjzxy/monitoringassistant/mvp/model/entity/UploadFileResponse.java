package cn.cdjzxy.monitoringassistant.mvp.model.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 文件上传服务器返回标准数据格式结果
 */
@Data
public class UploadFileResponse<T> implements Serializable {

    private int code;

    private String message;

    private T data;
}

