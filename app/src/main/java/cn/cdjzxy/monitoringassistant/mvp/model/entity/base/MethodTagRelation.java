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
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MethodTagRelationDao;

@Entity
public class MethodTagRelation {

    /**
     * Id : f7822132-7a9c-4616-b98f-002934da5f0d
     * MethodId : 2b9cdfef-b6c9-4a3f-92fd-0ec5ac48915a
     * TagId : 17ab6f44-cec6-4e45-94b1-b81f7158119e
     */
    @Id
    private String Id;
    private String MethodId;
    private String TagId;

    //监测方法
    @ToOne(joinProperty = "MethodId")
    private Methods mMethods;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1635583082)
    private transient MethodTagRelationDao myDao;

    @Generated(hash = 1040507137)
    public MethodTagRelation(String Id, String MethodId, String TagId) {
        this.Id = Id;
        this.MethodId = MethodId;
        this.TagId = TagId;
    }

    @Generated(hash = 1149229255)
    public MethodTagRelation() {
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

    public String getTagId() {
        return this.TagId;
    }

    public void setTagId(String TagId) {
        this.TagId = TagId;
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
    @Generated(hash = 2061009363)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMethodTagRelationDao() : null;
    }



}
