package cn.cdjzxy.monitoringassistant.mvp.model.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 服务器返回标准数据格式结果
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int Code;

    private String Message;

    private T Data;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }
}

