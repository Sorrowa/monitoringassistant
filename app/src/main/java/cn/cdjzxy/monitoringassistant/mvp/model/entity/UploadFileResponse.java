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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

