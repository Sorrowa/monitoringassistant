package cn.cdjzxy.monitoringassistant.mvp.model.entity.user;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * 用户信息
 */
@Entity
public class UserInfo {

    /**
     * id : 6e2c449a-9ce1-412b-bac8-ff7c369814e0
     * loginName : null
     * workNo : liuyang
     * name : Admin
     * sex : 男
     * brithdate : 1992-06-11T00:00:00
     * identityCode : null
     * mobile : 15680579757
     * departmentName : 系统人员
     * position : 系统测试维护人员
     * reportSite :
     * nation : 满
     * province : 吉林
     * city : null
     * country : null
     * dgree : 本科
     * major : 电子信息工程
     * school : 电子科技大学
     * politicsStatus : 无
     * jobType : 专技
     * professionalTitle : 技术员
     * token : 46cf3706e3f749d5b18213d8d58e2021
     */

    @Id
    private String Id;
    private String LoginName;
    private String WorkNo;
    private String Name;
    private String Sex;
    private String Brithdate;
    private String IdentityCode;
    private String Mobile;
    private String DepartmentName;
    private String Position;
    private String ReportSite;
    private String Nation;
    private String Province;
    private String City;
    private String Country;
    private String Dgree;
    private String Major;
    private String School;
    private String PoliticsStatus;
    private String JobType;
    private String ProfessionalTitle;
    private String Token;
    private String WebUrl;
    private String pwd;
    private int    intId;
    @Generated(hash = 1596207180)
    public UserInfo(String Id, String LoginName, String WorkNo, String Name,
            String Sex, String Brithdate, String IdentityCode, String Mobile,
            String DepartmentName, String Position, String ReportSite,
            String Nation, String Province, String City, String Country,
            String Dgree, String Major, String School, String PoliticsStatus,
            String JobType, String ProfessionalTitle, String Token, String WebUrl,
            String pwd, int intId) {
        this.Id = Id;
        this.LoginName = LoginName;
        this.WorkNo = WorkNo;
        this.Name = Name;
        this.Sex = Sex;
        this.Brithdate = Brithdate;
        this.IdentityCode = IdentityCode;
        this.Mobile = Mobile;
        this.DepartmentName = DepartmentName;
        this.Position = Position;
        this.ReportSite = ReportSite;
        this.Nation = Nation;
        this.Province = Province;
        this.City = City;
        this.Country = Country;
        this.Dgree = Dgree;
        this.Major = Major;
        this.School = School;
        this.PoliticsStatus = PoliticsStatus;
        this.JobType = JobType;
        this.ProfessionalTitle = ProfessionalTitle;
        this.Token = Token;
        this.WebUrl = WebUrl;
        this.pwd = pwd;
        this.intId = intId;
    }
    @Generated(hash = 1279772520)
    public UserInfo() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getLoginName() {
        return this.LoginName;
    }
    public void setLoginName(String LoginName) {
        this.LoginName = LoginName;
    }
    public String getWorkNo() {
        return this.WorkNo;
    }
    public void setWorkNo(String WorkNo) {
        this.WorkNo = WorkNo;
    }
    public String getName() {
        return this.Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getSex() {
        return this.Sex;
    }
    public void setSex(String Sex) {
        this.Sex = Sex;
    }
    public String getBrithdate() {
        return this.Brithdate;
    }
    public void setBrithdate(String Brithdate) {
        this.Brithdate = Brithdate;
    }
    public String getIdentityCode() {
        return this.IdentityCode;
    }
    public void setIdentityCode(String IdentityCode) {
        this.IdentityCode = IdentityCode;
    }
    public String getMobile() {
        return this.Mobile;
    }
    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }
    public String getDepartmentName() {
        return this.DepartmentName;
    }
    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
    }
    public String getPosition() {
        return this.Position;
    }
    public void setPosition(String Position) {
        this.Position = Position;
    }
    public String getReportSite() {
        return this.ReportSite;
    }
    public void setReportSite(String ReportSite) {
        this.ReportSite = ReportSite;
    }
    public String getNation() {
        return this.Nation;
    }
    public void setNation(String Nation) {
        this.Nation = Nation;
    }
    public String getProvince() {
        return this.Province;
    }
    public void setProvince(String Province) {
        this.Province = Province;
    }
    public String getCity() {
        return this.City;
    }
    public void setCity(String City) {
        this.City = City;
    }
    public String getCountry() {
        return this.Country;
    }
    public void setCountry(String Country) {
        this.Country = Country;
    }
    public String getDgree() {
        return this.Dgree;
    }
    public void setDgree(String Dgree) {
        this.Dgree = Dgree;
    }
    public String getMajor() {
        return this.Major;
    }
    public void setMajor(String Major) {
        this.Major = Major;
    }
    public String getSchool() {
        return this.School;
    }
    public void setSchool(String School) {
        this.School = School;
    }
    public String getPoliticsStatus() {
        return this.PoliticsStatus;
    }
    public void setPoliticsStatus(String PoliticsStatus) {
        this.PoliticsStatus = PoliticsStatus;
    }
    public String getJobType() {
        return this.JobType;
    }
    public void setJobType(String JobType) {
        this.JobType = JobType;
    }
    public String getProfessionalTitle() {
        return this.ProfessionalTitle;
    }
    public void setProfessionalTitle(String ProfessionalTitle) {
        this.ProfessionalTitle = ProfessionalTitle;
    }
    public String getToken() {
        return this.Token;
    }
    public void setToken(String Token) {
        this.Token = Token;
    }
    public String getWebUrl() {
        return this.WebUrl;
    }
    public void setWebUrl(String WebUrl) {
        this.WebUrl = WebUrl;
    }
    public String getPwd() {
        return this.pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public int getIntId() {
        return this.intId;
    }
    public void setIntId(int intId) {
        this.intId = intId;
    }

}
