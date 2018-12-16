package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DaoSession;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormFlowDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;

@Entity
public class FormSelect {

    @Id(autoincrement = true)
    private long   id;
    private long   form_Id;
    private String FormId;
    private String FormCode;
    private String FormName;
    private String TagParentId;
    private String TagId;
    private String Path;
    private String Flows;

    @ToMany(joinProperties = {
            @JoinProperty(name = "id", referencedName = "formSelect_Id")
    })
    private List<FormFlow> FormFlows;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1624776321)
    private transient FormSelectDao myDao;

    @Generated(hash = 1576383961)
    public FormSelect(long id, long form_Id, String FormId, String FormCode,
            String FormName, String TagParentId, String TagId, String Path,
            String Flows) {
        this.id = id;
        this.form_Id = form_Id;
        this.FormId = FormId;
        this.FormCode = FormCode;
        this.FormName = FormName;
        this.TagParentId = TagParentId;
        this.TagId = TagId;
        this.Path = Path;
        this.Flows = Flows;
    }

    @Generated(hash = 343193175)
    public FormSelect() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getForm_Id() {
        return this.form_Id;
    }

    public void setForm_Id(long form_Id) {
        this.form_Id = form_Id;
    }

    public String getFormId() {
        return this.FormId;
    }

    public void setFormId(String FormId) {
        this.FormId = FormId;
    }

    public String getFormCode() {
        return this.FormCode;
    }

    public void setFormCode(String FormCode) {
        this.FormCode = FormCode;
    }

    public String getFormName() {
        return this.FormName;
    }

    public void setFormName(String FormName) {
        this.FormName = FormName;
    }

    public String getTagParentId() {
        return this.TagParentId;
    }

    public void setTagParentId(String TagParentId) {
        this.TagParentId = TagParentId;
    }

    public String getTagId() {
        return this.TagId;
    }

    public void setTagId(String TagId) {
        this.TagId = TagId;
    }

    public String getPath() {
        return this.Path;
    }

    public void setPath(String Path) {
        this.Path = Path;
    }

    public String getFlows() {
        return this.Flows;
    }

    public void setFlows(String Flows) {
        this.Flows = Flows;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1780581099)
    public List<FormFlow> getFormFlows() {
        if (FormFlows == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FormFlowDao targetDao = daoSession.getFormFlowDao();
            List<FormFlow> FormFlowsNew = targetDao._queryFormSelect_FormFlows(id);
            synchronized (this) {
                if (FormFlows == null) {
                    FormFlows = FormFlowsNew;
                }
            }
        }
        return FormFlows;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1206424218)
    public synchronized void resetFormFlows() {
        FormFlows = null;
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
    @Generated(hash = 77184979)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getFormSelectDao() : null;
    }

}
