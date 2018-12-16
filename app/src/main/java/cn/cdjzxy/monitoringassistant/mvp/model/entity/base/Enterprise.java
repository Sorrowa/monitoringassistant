package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Enterprise {


    /**
     * Id : e663618a-b2e1-489a-b2e7-06cbaac44212
     * Name : 四川中科玻璃有限公司
     * Division : null
     * DetailAddress : 江阳区黄舣镇酒业集中发展园区南区
     * LegalName : null
     * PhoneNum : null
     * Longitude : null
     * Latitude : null
     * UpdateTime : 2018-11-09T14:09:34.433
     * RawMaterial : null
     * Product : null
     * ProductiveTechnology : null
     * Profile : null
     * MainAuxiliary : null
     * PollutionSolveTech : null
     * enterprisePollutionResult : null
     */
    @Id
    private String Id;
    private String Name;
    private String Division;
    private String DetailAddress;
    private String LegalName;
    private String PhoneNum;
    private String Longitude;
    private String Latitude;
    private String UpdateTime;
    private String RawMaterial;
    private String Product;
    private String ProductiveTechnology;
    private String Profile;
    private String MainAuxiliary;
    private String PollutionSolveTech;
    private String enterprisePollutionResult;
    @Generated(hash = 610080650)
    public Enterprise(String Id, String Name, String Division, String DetailAddress,
            String LegalName, String PhoneNum, String Longitude, String Latitude,
            String UpdateTime, String RawMaterial, String Product,
            String ProductiveTechnology, String Profile, String MainAuxiliary,
            String PollutionSolveTech, String enterprisePollutionResult) {
        this.Id = Id;
        this.Name = Name;
        this.Division = Division;
        this.DetailAddress = DetailAddress;
        this.LegalName = LegalName;
        this.PhoneNum = PhoneNum;
        this.Longitude = Longitude;
        this.Latitude = Latitude;
        this.UpdateTime = UpdateTime;
        this.RawMaterial = RawMaterial;
        this.Product = Product;
        this.ProductiveTechnology = ProductiveTechnology;
        this.Profile = Profile;
        this.MainAuxiliary = MainAuxiliary;
        this.PollutionSolveTech = PollutionSolveTech;
        this.enterprisePollutionResult = enterprisePollutionResult;
    }
    @Generated(hash = 790140480)
    public Enterprise() {
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
    public String getDivision() {
        return this.Division;
    }
    public void setDivision(String Division) {
        this.Division = Division;
    }
    public String getDetailAddress() {
        return this.DetailAddress;
    }
    public void setDetailAddress(String DetailAddress) {
        this.DetailAddress = DetailAddress;
    }
    public String getLegalName() {
        return this.LegalName;
    }
    public void setLegalName(String LegalName) {
        this.LegalName = LegalName;
    }
    public String getPhoneNum() {
        return this.PhoneNum;
    }
    public void setPhoneNum(String PhoneNum) {
        this.PhoneNum = PhoneNum;
    }
    public String getLongitude() {
        return this.Longitude;
    }
    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }
    public String getLatitude() {
        return this.Latitude;
    }
    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
    public String getRawMaterial() {
        return this.RawMaterial;
    }
    public void setRawMaterial(String RawMaterial) {
        this.RawMaterial = RawMaterial;
    }
    public String getProduct() {
        return this.Product;
    }
    public void setProduct(String Product) {
        this.Product = Product;
    }
    public String getProductiveTechnology() {
        return this.ProductiveTechnology;
    }
    public void setProductiveTechnology(String ProductiveTechnology) {
        this.ProductiveTechnology = ProductiveTechnology;
    }
    public String getProfile() {
        return this.Profile;
    }
    public void setProfile(String Profile) {
        this.Profile = Profile;
    }
    public String getMainAuxiliary() {
        return this.MainAuxiliary;
    }
    public void setMainAuxiliary(String MainAuxiliary) {
        this.MainAuxiliary = MainAuxiliary;
    }
    public String getPollutionSolveTech() {
        return this.PollutionSolveTech;
    }
    public void setPollutionSolveTech(String PollutionSolveTech) {
        this.PollutionSolveTech = PollutionSolveTech;
    }
    public String getEnterprisePollutionResult() {
        return this.enterprisePollutionResult;
    }
    public void setEnterprisePollutionResult(String enterprisePollutionResult) {
        this.enterprisePollutionResult = enterprisePollutionResult;
    }


}
