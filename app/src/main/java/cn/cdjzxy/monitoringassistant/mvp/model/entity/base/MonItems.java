package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * 监测项目
 */
@Entity
public class MonItems {


    /**
     * Id : feee5a0d-e4f5-44a0-82e3-0015146bdcbd
     * Code : s(2-lyj)m
     * Name : 双(2-氯乙基)醚
     */
    @Id
    private String  Id;
    private String  Code;
    private String  Name;
    @Transient
    private boolean isSelected;
    @Transient
    private String addressId;
    @Transient
    private String addressName;
    @Transient
    private String tagId;
    @Transient
    private String tagName;
    @Transient
    private String allMonitemId;
    @Transient
    private String allMonitemName;


    @Generated(hash = 1403264955)
    public MonItems(String Id, String Code, String Name) {
        this.Id = Id;
        this.Code = Code;
        this.Name = Name;
    }

    @Generated(hash = 1505645660)
    public MonItems() {
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getAllMonitemId() {
        return allMonitemId;
    }

    public void setAllMonitemId(String allMonitemId) {
        this.allMonitemId = allMonitemId;
    }

    public String getAllMonitemName() {
        return allMonitemName;
    }

    public void setAllMonitemName(String allMonitemName) {
        this.allMonitemName = allMonitemName;
    }
}
