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

    //监测项目
    @ToOne(joinProperty = "MonItemId")
    private MonItems mMonItems;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1140878212)
    private transient MonItemMethodRelationDao myDao;

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

    @Generated(hash = 394226115)
    private transient String mMonItems__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1390776638)
    public MonItems getMMonItems() {
        String __key = this.MonItemId;
        if (mMonItems__resolvedKey == null || mMonItems__resolvedKey != __key) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MonItemsDao targetDao = daoSession.getMonItemsDao();
            MonItems mMonItemsNew = targetDao.load(__key);
            synchronized (this) {
                mMonItems = mMonItemsNew;
                mMonItems__resolvedKey = __key;
            }
        }
        return mMonItems;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2123018103)
    public void setMMonItems(MonItems mMonItems) {
        synchronized (this) {
            this.mMonItems = mMonItems;
            MonItemId = mMonItems == null ? null : mMonItems.getId();
            mMonItems__resolvedKey = MonItemId;
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
    @Generated(hash = 1289332257)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMonItemMethodRelationDao()
                : null;
    }


}
