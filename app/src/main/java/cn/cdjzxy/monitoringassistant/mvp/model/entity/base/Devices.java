package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 设备信息
 */
@Entity
public class Devices {

    /**
     * deviceUsedRecords : []
     * id : 182b9db6-4a1b-48c4-865c-31b2b044f2fe
     * name : 碎纸机
     * specification : 黑金刚
     * systemCode : null
     * devCode :
     * departmentId : d63f7a90-d4da-40c2-af70-76ec0bdd4c9a
     * company : 科密
     * purchasingDate : 2017-07-31T00:00:00
     * rePrice : 900
     * storeLoaction : 409
     * expireDate : null
     * manager : 肖开煌
     * state : 1
     * groupId : 3ad66b96-0e82-4a5d-a20e-1bdbceef738f
     * isForceChecked : false
     * certCode : null
     * updateTime : 2018-01-30T09:46:04.547
     * ext1 : null
     * ext2 : null
     * ext3 : null
     * stateComment : null
     * supplier : null
     * supplierContact : null
     * supplierPhone : null
     * factoryContact : null
     * factoryPhone : null
     * properties : null
     * agencies : null
     * sourceWay : 校准
     * sourceDate : 2018-04-14T00:00:00
     * isLend : false
     * departmentName : null
     * hasUploadFile : false
     * statusStr : null
     */
    @Id
    private String  Id;
    private String  Name;
    private String  Specification;
    private String  SystemCode;
    private String  DevCode;
    private String  DepartmentId;
    private String  Company;
    private String  PurchasingDate;
    private int     RePrice;
    private String  StoreLoaction;
    private String  ExpireDate;
    private String  Manager;
    private int     State;
    private String  GroupId;
    private boolean IsForceChecked;
    private String  CertCode;
    private String  UpdateTime;
    private String  Ext1;
    private String  Ext2;
    private String  Ext3;
    private String  StateComment;
    private String  Supplier;
    private String  SupplierContact;
    private String  SupplierPhone;
    private String  SactoryContact;
    private String  SactoryPhone;
    private String  Sroperties;
    private String  Sgencies;
    private String  SourceWay;
    private String  SourceDate;
    private boolean SsLend;
    private String  SepartmentName;
    private boolean HasUploadFile;
    private String  StatusStr;
    //    private String  deviceUsedRecords;
    @Generated(hash = 1030168342)
    public Devices(String Id, String Name, String Specification, String SystemCode,
            String DevCode, String DepartmentId, String Company,
            String PurchasingDate, int RePrice, String StoreLoaction,
            String ExpireDate, String Manager, int State, String GroupId,
            boolean IsForceChecked, String CertCode, String UpdateTime, String Ext1,
            String Ext2, String Ext3, String StateComment, String Supplier,
            String SupplierContact, String SupplierPhone, String SactoryContact,
            String SactoryPhone, String Sroperties, String Sgencies,
            String SourceWay, String SourceDate, boolean SsLend,
            String SepartmentName, boolean HasUploadFile, String StatusStr) {
        this.Id = Id;
        this.Name = Name;
        this.Specification = Specification;
        this.SystemCode = SystemCode;
        this.DevCode = DevCode;
        this.DepartmentId = DepartmentId;
        this.Company = Company;
        this.PurchasingDate = PurchasingDate;
        this.RePrice = RePrice;
        this.StoreLoaction = StoreLoaction;
        this.ExpireDate = ExpireDate;
        this.Manager = Manager;
        this.State = State;
        this.GroupId = GroupId;
        this.IsForceChecked = IsForceChecked;
        this.CertCode = CertCode;
        this.UpdateTime = UpdateTime;
        this.Ext1 = Ext1;
        this.Ext2 = Ext2;
        this.Ext3 = Ext3;
        this.StateComment = StateComment;
        this.Supplier = Supplier;
        this.SupplierContact = SupplierContact;
        this.SupplierPhone = SupplierPhone;
        this.SactoryContact = SactoryContact;
        this.SactoryPhone = SactoryPhone;
        this.Sroperties = Sroperties;
        this.Sgencies = Sgencies;
        this.SourceWay = SourceWay;
        this.SourceDate = SourceDate;
        this.SsLend = SsLend;
        this.SepartmentName = SepartmentName;
        this.HasUploadFile = HasUploadFile;
        this.StatusStr = StatusStr;
    }
    @Generated(hash = 597445211)
    public Devices() {
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
    public String getSpecification() {
        return this.Specification;
    }
    public void setSpecification(String Specification) {
        this.Specification = Specification;
    }
    public String getSystemCode() {
        return this.SystemCode;
    }
    public void setSystemCode(String SystemCode) {
        this.SystemCode = SystemCode;
    }
    public String getDevCode() {
        return this.DevCode;
    }
    public void setDevCode(String DevCode) {
        this.DevCode = DevCode;
    }
    public String getDepartmentId() {
        return this.DepartmentId;
    }
    public void setDepartmentId(String DepartmentId) {
        this.DepartmentId = DepartmentId;
    }
    public String getCompany() {
        return this.Company;
    }
    public void setCompany(String Company) {
        this.Company = Company;
    }
    public String getPurchasingDate() {
        return this.PurchasingDate;
    }
    public void setPurchasingDate(String PurchasingDate) {
        this.PurchasingDate = PurchasingDate;
    }
    public int getRePrice() {
        return this.RePrice;
    }
    public void setRePrice(int RePrice) {
        this.RePrice = RePrice;
    }
    public String getStoreLoaction() {
        return this.StoreLoaction;
    }
    public void setStoreLoaction(String StoreLoaction) {
        this.StoreLoaction = StoreLoaction;
    }
    public String getExpireDate() {
        return this.ExpireDate;
    }
    public void setExpireDate(String ExpireDate) {
        this.ExpireDate = ExpireDate;
    }
    public String getManager() {
        return this.Manager;
    }
    public void setManager(String Manager) {
        this.Manager = Manager;
    }
    public int getState() {
        return this.State;
    }
    public void setState(int State) {
        this.State = State;
    }
    public String getGroupId() {
        return this.GroupId;
    }
    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }
    public boolean getIsForceChecked() {
        return this.IsForceChecked;
    }
    public void setIsForceChecked(boolean IsForceChecked) {
        this.IsForceChecked = IsForceChecked;
    }
    public String getCertCode() {
        return this.CertCode;
    }
    public void setCertCode(String CertCode) {
        this.CertCode = CertCode;
    }
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
    public String getExt1() {
        return this.Ext1;
    }
    public void setExt1(String Ext1) {
        this.Ext1 = Ext1;
    }
    public String getExt2() {
        return this.Ext2;
    }
    public void setExt2(String Ext2) {
        this.Ext2 = Ext2;
    }
    public String getExt3() {
        return this.Ext3;
    }
    public void setExt3(String Ext3) {
        this.Ext3 = Ext3;
    }
    public String getStateComment() {
        return this.StateComment;
    }
    public void setStateComment(String StateComment) {
        this.StateComment = StateComment;
    }
    public String getSupplier() {
        return this.Supplier;
    }
    public void setSupplier(String Supplier) {
        this.Supplier = Supplier;
    }
    public String getSupplierContact() {
        return this.SupplierContact;
    }
    public void setSupplierContact(String SupplierContact) {
        this.SupplierContact = SupplierContact;
    }
    public String getSupplierPhone() {
        return this.SupplierPhone;
    }
    public void setSupplierPhone(String SupplierPhone) {
        this.SupplierPhone = SupplierPhone;
    }
    public String getSactoryContact() {
        return this.SactoryContact;
    }
    public void setSactoryContact(String SactoryContact) {
        this.SactoryContact = SactoryContact;
    }
    public String getSactoryPhone() {
        return this.SactoryPhone;
    }
    public void setSactoryPhone(String SactoryPhone) {
        this.SactoryPhone = SactoryPhone;
    }
    public String getSroperties() {
        return this.Sroperties;
    }
    public void setSroperties(String Sroperties) {
        this.Sroperties = Sroperties;
    }
    public String getSgencies() {
        return this.Sgencies;
    }
    public void setSgencies(String Sgencies) {
        this.Sgencies = Sgencies;
    }
    public String getSourceWay() {
        return this.SourceWay;
    }
    public void setSourceWay(String SourceWay) {
        this.SourceWay = SourceWay;
    }
    public String getSourceDate() {
        return this.SourceDate;
    }
    public void setSourceDate(String SourceDate) {
        this.SourceDate = SourceDate;
    }
    public boolean getSsLend() {
        return this.SsLend;
    }
    public void setSsLend(boolean SsLend) {
        this.SsLend = SsLend;
    }
    public String getSepartmentName() {
        return this.SepartmentName;
    }
    public void setSepartmentName(String SepartmentName) {
        this.SepartmentName = SepartmentName;
    }
    public boolean getHasUploadFile() {
        return this.HasUploadFile;
    }
    public void setHasUploadFile(boolean HasUploadFile) {
        this.HasUploadFile = HasUploadFile;
    }
    public String getStatusStr() {
        return this.StatusStr;
    }
    public void setStatusStr(String StatusStr) {
        this.StatusStr = StatusStr;
    }


}
