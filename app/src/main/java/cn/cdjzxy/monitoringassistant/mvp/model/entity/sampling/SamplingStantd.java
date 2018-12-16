package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SamplingStantd {


    /**
     * Id : 46c3dc6e-703c-4cdd-b767-2550961af7c7
     * Capacity : 500ml× 瓶
     * Contaner : G
     * SaveDescription :
     * IsSenceAnalysis : false
     * TagId : ad7677ae-3bee-492d-bd2a-1ae830f9d9ba
     * TagName : 地下水
     * MonItems : ["氨氮","总氮","总磷","高锰酸盐指数"]
     */

    @Id
    private String  Id;
    private String  Capacity;
    private String  Contaner;
    private String  SaveDescription;
    private boolean IsSenceAnalysis;
    private String  TagId;
    private String  TagName;
    private String  MonItems;
    @Generated(hash = 1569228593)
    public SamplingStantd(String Id, String Capacity, String Contaner,
            String SaveDescription, boolean IsSenceAnalysis, String TagId,
            String TagName, String MonItems) {
        this.Id = Id;
        this.Capacity = Capacity;
        this.Contaner = Contaner;
        this.SaveDescription = SaveDescription;
        this.IsSenceAnalysis = IsSenceAnalysis;
        this.TagId = TagId;
        this.TagName = TagName;
        this.MonItems = MonItems;
    }
    @Generated(hash = 212713864)
    public SamplingStantd() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getCapacity() {
        return this.Capacity;
    }
    public void setCapacity(String Capacity) {
        this.Capacity = Capacity;
    }
    public String getContaner() {
        return this.Contaner;
    }
    public void setContaner(String Contaner) {
        this.Contaner = Contaner;
    }
    public String getSaveDescription() {
        return this.SaveDescription;
    }
    public void setSaveDescription(String SaveDescription) {
        this.SaveDescription = SaveDescription;
    }
    public boolean getIsSenceAnalysis() {
        return this.IsSenceAnalysis;
    }
    public void setIsSenceAnalysis(boolean IsSenceAnalysis) {
        this.IsSenceAnalysis = IsSenceAnalysis;
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
    public String getMonItems() {
        return this.MonItems;
    }
    public void setMonItems(String MonItems) {
        this.MonItems = MonItems;
    }


}
