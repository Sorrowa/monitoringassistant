package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MethodDevRelation {


    /**
     * Id : 052bd271-e3c6-4597-97c9-001da198d033
     * MethodId : 377c5034-11fe-49ff-bec8-4bff392f1c3e
     * DevId : 9fe4141f-158e-40d1-bfba-b50664c55a6b
     */

    @Id
    private String Id;
    private String MethodId;
    private String DevId;
    @Keep()
    public MethodDevRelation(String Id, String MethodId, String DevId) {
        this.Id = Id;
        this.MethodId = MethodId;
        this.DevId = DevId;
    }
    @Keep()
    public MethodDevRelation() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getMethodId() {
        return this.MethodId;
    }
    public void setMethodId(String MethodId) {
        this.MethodId = MethodId;
    }
    public String getDevId() {
        return this.DevId;
    }
    public void setDevId(String DevId) {
        this.DevId = DevId;
    }





}
