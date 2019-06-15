package cn.cdjzxy.monitoringassistant.mvp.model.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 文件上传服务器返回标准数据格式结果
 */
@Data
public class UploadFileResponse<T> implements Serializable {

    private int Code;

    private String Message;

    private T Data;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        this.Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        this.Data = data;
    }
}

