package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Tags {

    /**
     * group_Tag_Relation : []
     * monItem_Tag_Relation : []
     * id : 20366344-3f7b-4b57-879a-076ccfdf9334
     * name : 区域噪声
     * value :
     * parentId : 8a08d498-9936-4b2e-bbcb-cc79a061f73b
     * level : 1
     * type : 0
     * updateTime : 2018-04-03T20:50:43.55
     * ext1 : 0
     * ext2 :
     * orderIndex : 0
     * display : true
     * items : null
     */
    @Id
    private String  Id;
    private String  Name;
    private String  Value;
    private String  ParentId;
    private int     Level;
    private int     Type;
    private String  UpdateTime;
    private int     Ext1;
    private String  Ext2;
    private int     OrderIndex;
    private boolean Display;
    private String  Items;
    @Generated(hash = 417907230)
    public Tags(String Id, String Name, String Value, String ParentId, int Level,
            int Type, String UpdateTime, int Ext1, String Ext2, int OrderIndex,
            boolean Display, String Items) {
        this.Id = Id;
        this.Name = Name;
        this.Value = Value;
        this.ParentId = ParentId;
        this.Level = Level;
        this.Type = Type;
        this.UpdateTime = UpdateTime;
        this.Ext1 = Ext1;
        this.Ext2 = Ext2;
        this.OrderIndex = OrderIndex;
        this.Display = Display;
        this.Items = Items;
    }
    @Generated(hash = 1290390976)
    public Tags() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getName() {
        return this.Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getValue() {
        return this.Value;
    }
    public void setValue(String Value) {
        this.Value = Value;
    }
    public String getParentId() {
        return this.ParentId;
    }
    public void setParentId(String ParentId) {
        this.ParentId = ParentId;
    }
    public int getLevel() {
        return this.Level;
    }
    public void setLevel(int Level) {
        this.Level = Level;
    }
    public int getType() {
        return this.Type;
    }
    public void setType(int Type) {
        this.Type = Type;
    }
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
    public int getExt1() {
        return this.Ext1;
    }
    public void setExt1(int Ext1) {
        this.Ext1 = Ext1;
    }
    public String getExt2() {
        return this.Ext2;
    }
    public void setExt2(String Ext2) {
        this.Ext2 = Ext2;
    }
    public int getOrderIndex() {
        return this.OrderIndex;
    }
    public void setOrderIndex(int OrderIndex) {
        this.OrderIndex = OrderIndex;
    }
    public boolean getDisplay() {
        return this.Display;
    }
    public void setDisplay(boolean Display) {
        this.Display = Display;
    }
    public String getItems() {
        return this.Items;
    }
    public void setItems(String Items) {
        this.Items = Items;
    }


    //    private List<String> group_Tag_Relation;
    //    private List<String> monItem_Tag_Relation;


}
