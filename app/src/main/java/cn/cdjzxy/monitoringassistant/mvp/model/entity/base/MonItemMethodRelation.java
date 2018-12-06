package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class MonItemMethodRelation {


    /**
     * id : b98f5aaa-d7b0-4583-8984-000713326566
     * monItemId : a9ce8874-da97-42d6-94fd-1ed9b948cb9d
     * methodId : 254614a6-b086-40d7-aa8a-6c2dbf6b48be
     */
    @Id
    private String Id;
    private String MonItemId;
    private String MethodId;
    @Generated(hash = 772309768)
    public MonItemMethodRelation(String Id, String MonItemId, String MethodId) {
        this.Id = Id;
        this.MonItemId = MonItemId;
        this.MethodId = MethodId;
    }
    @Generated(hash = 502512815)
    public MonItemMethodRelation() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getMonItemId() {
        return this.MonItemId;
    }
    public void setMonItemId(String MonItemId) {
        this.MonItemId = MonItemId;
    }
    public String getMethodId() {
        return this.MethodId;
    }
    public void setMethodId(String MethodId) {
        this.MethodId = MethodId;
    }


}
