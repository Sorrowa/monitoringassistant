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

    //监测方法
    @ToOne(joinProperty = "MethodId")
    private Methods mMethods;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1938661281)
    private transient MethodDevRelationDao myDao;

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

    @Generated(hash = 1144683162)
    private transient String mMethods__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1165453006)
    public Methods getMMethods() {
        String __key = this.MethodId;
        if (mMethods__resolvedKey == null || mMethods__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MethodsDao targetDao = daoSession.getMethodsDao();
            Methods mMethodsNew = targetDao.load(__key);
            synchronized (this) {
                mMethods = mMethodsNew;
                mMethods__resolvedKey = __key;
            }
        }
        return mMethods;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1497844221)
    public void setMMethods(Methods mMethods) {
        synchronized (this) {
            this.mMethods = mMethods;
            MethodId = mMethods == null ? null : mMethods.getId();
            mMethods__resolvedKey = MethodId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 429500839)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMethodDevRelationDao() : null;
    }


}
