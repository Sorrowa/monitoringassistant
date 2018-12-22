package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DaoSession;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MonItemMethodRelationDao;

@Entity
public class MonItemMethodRelation {


    /**
     * Id : b98f5aaa-d7b0-4583-8984-000713326566
     * MonItemId : a9ce8874-da97-42d6-94fd-1ed9b948cb9d
     * MethodId : 254614a6-b086-40d7-aa8a-6c2dbf6b48be
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
