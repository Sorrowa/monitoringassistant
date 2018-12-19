package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Transient;

import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DaoSession;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormSelectDao;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.FormDao;

@Entity
public class Form {

    private String           TagId;
    private String           TagName;

    @Transient
    private List<FormSelect> FormSelectList;

    @Generated(hash = 1230256564)
    public Form(String TagId, String TagName) {
        this.TagId = TagId;
        this.TagName = TagName;
    }

    @Generated(hash = 535210737)
    public Form() {
    }

    public String getTagId() {
        return this.TagId;
    }

    public void setTagId(String TagId) {
        this.TagId = TagId;
    }

    public String getTagName() {
        return this.TagName;
    }

    public void setTagName(String TagName) {
        this.TagName = TagName;
    }

    public List<FormSelect> getFormSelectList() {
        return FormSelectList;
    }

    public void setFormSelectList(List<FormSelect> formSelectList) {
        FormSelectList = formSelectList;
    }
}
