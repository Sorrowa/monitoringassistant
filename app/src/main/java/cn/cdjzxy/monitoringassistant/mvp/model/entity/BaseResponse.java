package cn.cdjzxy.monitoringassistant.mvp.model.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 服务器返回标准数据格式结果
 * Created by zhang on 2018/7/26.
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int Code;

    private String Message;

    private T Data;

}

