package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;

import java.util.List;

import cn.cdjzxy.monitoringassistant.mvp.model.greendao.converter.StringConverter;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SamplingFormStand {

    /**
     * Id : d0828a83-016f-4b84-855c-2e5f44846678
     * SamplingId : 29380d4a-4f27-421f-a750-3d015f12c42b
     * StandNo : 1
     * MonitemIds : e113d898-1822-e549-8168-04fb822c6c9d
     * MonitemName : 铁
     * SamplingAmount :
     * AnalysisSite :
     * SaveMehtod :
     * Preservative :
     * Count : 1
     * Container :
     * SaveTimes :
     * Index : 1
     */

    private String       Id;
    private String       SamplingId;
    private int          StandNo;
    private String       MonitemIds;
    private String       MonitemName;
    private String       SamplingAmount;
    private String       AnalysisSite;
    private String       SaveMehtod;
    private String       Preservative;
    private int          Count;
    private String       Container;
    private String       SaveTimes;
    private int          Index;
    private String       UpdateTime;
    private String       SampingCode;//和SamplingId一样，唯一标识一条采样信息
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> MonItems;
    @Generated(hash = 705730091)
    public SamplingFormStand(String Id, String SamplingId, int StandNo,
            String MonitemIds, String MonitemName, String SamplingAmount,
            String AnalysisSite, String SaveMehtod, String Preservative, int Count,
            String Container, String SaveTimes, int Index, String UpdateTime,
            String SampingCode, List<String> MonItems) {
        this.Id = Id;
        this.SamplingId = SamplingId;
        this.StandNo = StandNo;
        this.MonitemIds = MonitemIds;
        this.MonitemName = MonitemName;
        this.SamplingAmount = SamplingAmount;
        this.AnalysisSite = AnalysisSite;
        this.SaveMehtod = SaveMehtod;
        this.Preservative = Preservative;
        this.Count = Count;
        this.Container = Container;
        this.SaveTimes = SaveTimes;
        this.Index = Index;
        this.UpdateTime = UpdateTime;
        this.SampingCode = SampingCode;
        this.MonItems = MonItems;
    }
    @Generated(hash = 922621764)
    public SamplingFormStand() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getSamplingId() {
        return this.SamplingId;
    }
    public void setSamplingId(String SamplingId) {
        this.SamplingId = SamplingId;
    }
    public int getStandNo() {
        return this.StandNo;
    }
    public void setStandNo(int StandNo) {
        this.StandNo = StandNo;
    }
    public String getMonitemIds() {
        return this.MonitemIds;
    }
    public void setMonitemIds(String MonitemIds) {
        this.MonitemIds = MonitemIds;
    }
    public String getMonitemName() {
        return this.MonitemName;
    }
    public void setMonitemName(String MonitemName) {
        this.MonitemName = MonitemName;
    }
    public String getSamplingAmount() {
        return this.SamplingAmount;
    }
    public void setSamplingAmount(String SamplingAmount) {
        this.SamplingAmount = SamplingAmount;
    }
    public String getAnalysisSite() {
        return this.AnalysisSite;
    }
    public void setAnalysisSite(String AnalysisSite) {
        this.AnalysisSite = AnalysisSite;
    }
    public String getSaveMehtod() {
        return this.SaveMehtod;
    }
    public void setSaveMehtod(String SaveMehtod) {
        this.SaveMehtod = SaveMehtod;
    }
    public String getPreservative() {
        return this.Preservative;
    }
    public void setPreservative(String Preservative) {
        this.Preservative = Preservative;
    }
    public int getCount() {
        return this.Count;
    }
    public void setCount(int Count) {
        this.Count = Count;
    }
    public String getContainer() {
        return this.Container;
    }
    public void setContainer(String Container) {
        this.Container = Container;
    }
    public String getSaveTimes() {
        return this.SaveTimes;
    }
    public void setSaveTimes(String SaveTimes) {
        this.SaveTimes = SaveTimes;
    }
    public int getIndex() {
        return this.Index;
    }
    public void setIndex(int Index) {
        this.Index = Index;
    }
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
    public List<String> getMonItems() {
        return this.MonItems;
    }
    public void setMonItems(List<String> MonItems) {
        this.MonItems = MonItems;
    }
    public String getSampingCode() {
        return this.SampingCode;
    }
    public void setSampingCode(String SampingCode) {
        this.SampingCode = SampingCode;
    }


}
