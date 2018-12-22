package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.DaoException;

import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DaoSession;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.MethodsDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DevicesDao;

/**
 * 方法信息
 */
@Entity
public class Methods {


    /**
     * Id : 3a3865f7-f1ad-40d6-8e84-0036f2bcf565
     * Code : GB/T15502-1995
     * Name : 空气质量 苯胺类的测定 盐酸萘乙二胺分光光度法
     * Type : 国标
     */

    @Id
    private String Id;
    private String Code;
    private String Name;
    private String Type;

    @ToMany
    @JoinEntity(
            entity = MethodDevRelation.class,
            sourceProperty = "MethodId",
            targetProperty = "DevId"
    )
    List<Devices> mDevices;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1623936033)
    private transient MethodsDao myDao;

    @Generated(hash = 98449988)
    public Methods(String Id, String Code, String Name, String Type) {
        this.Id = Id;
        this.Code = Code;
        this.Name = Name;
        this.Type = Type;
    }

    @Generated(hash = 632479964)
    public Methods() {
    }

    public String getId() {
        return this.Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getCode() {
        return this.Code;
    }

    public void setCode(String Code) {
        this.Code = Code;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getType() {
        return this.Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 358935415)
    public List<Devices> getMDevices() {
        if (mDevices == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DevicesDao targetDao = daoSession.getDevicesDao();
            List<Devices> mDevicesNew = targetDao._queryMethods_MDevices(Id);
            synchronized (this) {
                if (mDevices == null) {
                    mDevices = mDevicesNew;
                }
            }
        }
        return mDevices;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1523619150)
    public synchronized void resetMDevices() {
        mDevices = null;
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
    @Generated(hash = 88362284)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMethodsDao() : null;
    }
    

}
