package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class MethodDevRelation {

    /**
     * id : 33185324-77c1-4ed9-8283-000edb5a17e4
     * methodId : 8622c204-8b71-4c73-888e-dc1416e88242
     * devId : f223a06d-7511-4983-8cef-068ba332263e
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
