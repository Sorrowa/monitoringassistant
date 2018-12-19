package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 设备信息
 */
@Entity
public class Devices {


    /**
     * Id : 3d56fda8-58db-46af-8795-9d256b519e5c
     * Name : css
     * Specification : 1
     * SystemCode : 1201011
     * DevCode : 1201012
     * DepartmentId : 3df59ffd-beb0-43f0-b652-40fcd3a52d25
     * DepartmentName : null
     * Company : 制造
     * PurchasingDate : 2018-11-27T00:00:00
     * RePrice : 0
     * StoreLoaction : null
     * ExpireDate : null
     * Manager : Admin
     * State : 1
     * IsForceChecked : false
     * CertCode : null
     * SourceWay : 检定
     */
    @Id
    private String  Id;
    private String  Name;
    private String  Specification;
    private String  SystemCode;
    private String  DevCode;
    private String  DepartmentId;
    private String  DepartmentName;
    private String  Company;
    private String  PurchasingDate;
    private float     RePrice;
    private String  StoreLoaction;
    private String  ExpireDate;
    private String  Manager;
    private int     State;
    private boolean IsForceChecked;
    private String  CertCode;
    private String  SourceWay;
    @Generated(hash = 765118164)
    public Devices(String Id, String Name, String Specification, String SystemCode,
            String DevCode, String DepartmentId, String DepartmentName,
            String Company, String PurchasingDate, float RePrice,
            String StoreLoaction, String ExpireDate, String Manager, int State,
            boolean IsForceChecked, String CertCode, String SourceWay) {
        this.Id = Id;
        this.Name = Name;
        this.Specification = Specification;
        this.SystemCode = SystemCode;
        this.DevCode = DevCode;
        this.DepartmentId = DepartmentId;
        this.DepartmentName = DepartmentName;
        this.Company = Company;
        this.PurchasingDate = PurchasingDate;
        this.RePrice = RePrice;
        this.StoreLoaction = StoreLoaction;
        this.ExpireDate = ExpireDate;
        this.Manager = Manager;
        this.State = State;
        this.IsForceChecked = IsForceChecked;
        this.CertCode = CertCode;
        this.SourceWay = SourceWay;
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
    public String getDepartmentName() {
        return this.DepartmentName;
    }
    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
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
    public float getRePrice() {
        return this.RePrice;
    }
    public void setRePrice(float RePrice) {
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
    public String getSourceWay() {
        return this.SourceWay;
    }
    public void setSourceWay(String SourceWay) {
        this.SourceWay = SourceWay;
    }
  

}
