package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;

import java.util.List;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Form {

    private String           TagId;
    private String           TagName;

    @Transient
    private List<FormSelect> FormSelectList;

    @Keep()
    public Form(String TagId, String TagName) {
        this.TagId = TagId;
        this.TagName = TagName;
    }

    @Keep()
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
