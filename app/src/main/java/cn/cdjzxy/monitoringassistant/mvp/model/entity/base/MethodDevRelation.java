package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DaoSession;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MethodsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MethodDevRelationDao;

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
    @Generated(hash = 374344931)
    public MethodDevRelation(String Id, String MethodId, String DevId) {
        this.Id = Id;
        this.MethodId = MethodId;
        this.DevId = DevId;
    }
    @Generated(hash = 1472157057)
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
