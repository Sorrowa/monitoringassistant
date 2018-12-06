package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 监测项目
 */
@Entity
public class MonItems {


    /**
     * monItem_Tag_Relation : []
     * monMethodEdges : []
     * methodQualityControlDetails : []
     * id : feee5a0d-e4f5-44a0-82e3-0015146bdcbd
     * name : 双(2-氯乙基)醚
     * nameSpell :
     * code : s(2-lyj)m
     * index : 1
     * chemical : null
     * updateTime : 2018-07-05T14:49:31.293
     * unitId : null
     * ext1 : null
     * ext2 : null
     * ext3 : null
     * enName : null
     * enShortName : null
     * noParell : false
     * typeCode : null
     * casCode : null
     * typeName : null
     * parentId : 9355c61d-1520-449f-9136-3fc789ed664b
     * together : null
     * unitName : null
     * reportCode : null
     * methodId : null
     * standardCode : null
     * methodName : null
     * haveCert : null
     * tagId : null
     * complexConditionId : null
     * complexConditionName : null
     * valueType : null
     * valueMin : null
     * valueMax : null
     * value : null
     * evaluateName : null
     * isSubcontracting : null
     * clSampCount : 0
     * isOutsourcing : false
     * useCnt : 0
     */

    @Id
    private String  Id;
    private String  Name;
    private String  NameSpell;
    private String  Code;
    private int     Index;
    private String  Chemical;
    private String  UpdateTime;
    private String  UnitId;
    private String  Ext1;
    private String  Ext2;
    private String  Ext3;
    private String  EnName;
    private String  EnShortName;
    private boolean NoParell;
    private String  TypeCode;
    private String  CasCode;
    private String  TypeName;
    private String  ParentId;
    private String  Together;
    private String  UnitName;
    private String  ReportCode;
    private String  MethodId;
    private String  StandardCode;
    private String  MethodName;
    private String  HaveCert;
    private String  TagId;
    private String  ComplexConditionId;
    private String  ComplexConditionName;
    private String  ValueType;
    private String  ValueMin;
    private String  ValueMax;
    private String  Value;
    private String  EvaluateName;
    private String  IsSubcontracting;
    private int     ClSampCount;
    private boolean IsOutsourcing;
    private int     UseCnt;
    @Generated(hash = 263826643)
    public MonItems(String Id, String Name, String NameSpell, String Code,
            int Index, String Chemical, String UpdateTime, String UnitId,
            String Ext1, String Ext2, String Ext3, String EnName,
            String EnShortName, boolean NoParell, String TypeCode, String CasCode,
            String TypeName, String ParentId, String Together, String UnitName,
            String ReportCode, String MethodId, String StandardCode,
            String MethodName, String HaveCert, String TagId,
            String ComplexConditionId, String ComplexConditionName,
            String ValueType, String ValueMin, String ValueMax, String Value,
            String EvaluateName, String IsSubcontracting, int ClSampCount,
            boolean IsOutsourcing, int UseCnt) {
        this.Id = Id;
        this.Name = Name;
        this.NameSpell = NameSpell;
        this.Code = Code;
        this.Index = Index;
        this.Chemical = Chemical;
        this.UpdateTime = UpdateTime;
        this.UnitId = UnitId;
        this.Ext1 = Ext1;
        this.Ext2 = Ext2;
        this.Ext3 = Ext3;
        this.EnName = EnName;
        this.EnShortName = EnShortName;
        this.NoParell = NoParell;
        this.TypeCode = TypeCode;
        this.CasCode = CasCode;
        this.TypeName = TypeName;
        this.ParentId = ParentId;
        this.Together = Together;
        this.UnitName = UnitName;
        this.ReportCode = ReportCode;
        this.MethodId = MethodId;
        this.StandardCode = StandardCode;
        this.MethodName = MethodName;
        this.HaveCert = HaveCert;
        this.TagId = TagId;
        this.ComplexConditionId = ComplexConditionId;
        this.ComplexConditionName = ComplexConditionName;
        this.ValueType = ValueType;
        this.ValueMin = ValueMin;
        this.ValueMax = ValueMax;
        this.Value = Value;
        this.EvaluateName = EvaluateName;
        this.IsSubcontracting = IsSubcontracting;
        this.ClSampCount = ClSampCount;
        this.IsOutsourcing = IsOutsourcing;
        this.UseCnt = UseCnt;
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
    public String getName() {
        return this.Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getNameSpell() {
        return this.NameSpell;
    }
    public void setNameSpell(String NameSpell) {
        this.NameSpell = NameSpell;
    }
    public String getCode() {
        return this.Code;
    }
    public void setCode(String Code) {
        this.Code = Code;
    }
    public int getIndex() {
        return this.Index;
    }
    public void setIndex(int Index) {
        this.Index = Index;
    }
    public String getChemical() {
        return this.Chemical;
    }
    public void setChemical(String Chemical) {
        this.Chemical = Chemical;
    }
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
    public String getUnitId() {
        return this.UnitId;
    }
    public void setUnitId(String UnitId) {
        this.UnitId = UnitId;
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
    public String getEnName() {
        return this.EnName;
    }
    public void setEnName(String EnName) {
        this.EnName = EnName;
    }
    public String getEnShortName() {
        return this.EnShortName;
    }
    public void setEnShortName(String EnShortName) {
        this.EnShortName = EnShortName;
    }
    public boolean getNoParell() {
        return this.NoParell;
    }
    public void setNoParell(boolean NoParell) {
        this.NoParell = NoParell;
    }
    public String getTypeCode() {
        return this.TypeCode;
    }
    public void setTypeCode(String TypeCode) {
        this.TypeCode = TypeCode;
    }
    public String getCasCode() {
        return this.CasCode;
    }
    public void setCasCode(String CasCode) {
        this.CasCode = CasCode;
    }
    public String getTypeName() {
        return this.TypeName;
    }
    public void setTypeName(String TypeName) {
        this.TypeName = TypeName;
    }
    public String getParentId() {
        return this.ParentId;
    }
    public void setParentId(String ParentId) {
        this.ParentId = ParentId;
    }
    public String getTogether() {
        return this.Together;
    }
    public void setTogether(String Together) {
        this.Together = Together;
    }
    public String getUnitName() {
        return this.UnitName;
    }
    public void setUnitName(String UnitName) {
        this.UnitName = UnitName;
    }
    public String getReportCode() {
        return this.ReportCode;
    }
    public void setReportCode(String ReportCode) {
        this.ReportCode = ReportCode;
    }
    public String getMethodId() {
        return this.MethodId;
    }
    public void setMethodId(String MethodId) {
        this.MethodId = MethodId;
    }
    public String getStandardCode() {
        return this.StandardCode;
    }
    public void setStandardCode(String StandardCode) {
        this.StandardCode = StandardCode;
    }
    public String getMethodName() {
        return this.MethodName;
    }
    public void setMethodName(String MethodName) {
        this.MethodName = MethodName;
    }
    public String getHaveCert() {
        return this.HaveCert;
    }
    public void setHaveCert(String HaveCert) {
        this.HaveCert = HaveCert;
    }
    public String getTagId() {
        return this.TagId;
    }
    public void setTagId(String TagId) {
        this.TagId = TagId;
    }
    public String getComplexConditionId() {
        return this.ComplexConditionId;
    }
    public void setComplexConditionId(String ComplexConditionId) {
        this.ComplexConditionId = ComplexConditionId;
    }
    public String getComplexConditionName() {
        return this.ComplexConditionName;
    }
    public void setComplexConditionName(String ComplexConditionName) {
        this.ComplexConditionName = ComplexConditionName;
    }
    public String getValueType() {
        return this.ValueType;
    }
    public void setValueType(String ValueType) {
        this.ValueType = ValueType;
    }
    public String getValueMin() {
        return this.ValueMin;
    }
    public void setValueMin(String ValueMin) {
        this.ValueMin = ValueMin;
    }
    public String getValueMax() {
        return this.ValueMax;
    }
    public void setValueMax(String ValueMax) {
        this.ValueMax = ValueMax;
    }
    public String getValue() {
        return this.Value;
    }
    public void setValue(String Value) {
        this.Value = Value;
    }
    public String getEvaluateName() {
        return this.EvaluateName;
    }
    public void setEvaluateName(String EvaluateName) {
        this.EvaluateName = EvaluateName;
    }
    public String getIsSubcontracting() {
        return this.IsSubcontracting;
    }
    public void setIsSubcontracting(String IsSubcontracting) {
        this.IsSubcontracting = IsSubcontracting;
    }
    public int getClSampCount() {
        return this.ClSampCount;
    }
    public void setClSampCount(int ClSampCount) {
        this.ClSampCount = ClSampCount;
    }
    public boolean getIsOutsourcing() {
        return this.IsOutsourcing;
    }
    public void setIsOutsourcing(boolean IsOutsourcing) {
        this.IsOutsourcing = IsOutsourcing;
    }
    public int getUseCnt() {
        return this.UseCnt;
    }
    public void setUseCnt(int UseCnt) {
        this.UseCnt = UseCnt;
    }


    //    private List<String> monItem_Tag_Relation;
    //    private List<String> monMethodEdges;
    //    private List<String> methodQualityControlDetails;


}
