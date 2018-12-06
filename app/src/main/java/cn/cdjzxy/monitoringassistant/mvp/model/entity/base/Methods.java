package cn.cdjzxy.monitoringassistant.mvp.model.entity.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 方法信息
 */
@Entity
public class Methods {


    /**
     * monMethodEdges : []
     * methodQualityControls : []
     * relationItems : null
     * tagId : null
     * esId : 00000000-0000-0000-0000-000000000000
     * groupIdExt : 00000000-0000-0000-0000-000000000000
     * id : 3a3865f7-f1ad-40d6-8e84-0036f2bcf565
     * groupId : c6aee2e8-b47e-42d6-847e-27172b9b31ea
     * index : null
     * fullName : 空气质量 苯胺类的测定 盐酸萘乙二胺分光光度法
     * fullNameSpell : null
     * shortName : null
     * shortNameSpell : null
     * standardCode : GB/T15502-1995
     * replaceSC : null
     * attachFileId : f4fa3dc4-7545-4113-b024-2480bcb673e4
     * attachFileName : GBT 15502-1995 空气质量 苯胺类的测定 盐酸萘乙二胺分光光度法.pdf
     * publishTime : 1995-03-15T00:00:00
     * executeTime : 1995-08-01T00:00:00
     * flag : null
     * isDefault : null
     * updateTime : 2017-08-08T18:01:23.557
     * formulaPath : null
     * ext1 : null
     * ext2 : null
     * ext3 : null
     * orgCode : null
     * yearLimit : 1995
     * methodType : null
     * standardType : 国标
     * mehodStatus : true
     * acceptRule : null
     * compareRule : null
     * roundRule : null
     * labelType : 0
     * formula : null
     */
    @Id
    private String  Id;
    private String  RelationItems;
    private String  TagId;
    private String  EsId;
    private String  GroupIdExt;
    private String  GroupId;
    private String  Index;
    private String  FullName;
    private String  FullNameSpell;
    private String  ShortName;
    private String  ShortNameSpell;
    private String  StandardCode;
    private String  ReplaceSC;
    private String  AttachFileId;
    private String  AttachFileName;
    private String  PublishTime;
    private String  ExecuteTime;
    private String  Flag;
    private String  IsDefault;
    private String  UpdateTime;
    private String  FormulaPath;
    private String  Ext1;
    private String  Ext2;
    private String  Ext3;
    private String  OrgCode;
    private String  YearLimit;
    private String  MethodType;
    private String  StandardType;
    private boolean MehodStatus;
    private String  AcceptRule;
    private String  CompareRule;
    private String  RoundRule;
    private int     LabelType;
    private String  Formula;
    //    private String  monMethodEdges;
    //    private String  methodQualityControls;
    @Generated(hash = 1351787229)
    public Methods(String Id, String RelationItems, String TagId, String EsId,
            String GroupIdExt, String GroupId, String Index, String FullName,
            String FullNameSpell, String ShortName, String ShortNameSpell,
            String StandardCode, String ReplaceSC, String AttachFileId,
            String AttachFileName, String PublishTime, String ExecuteTime,
            String Flag, String IsDefault, String UpdateTime, String FormulaPath,
            String Ext1, String Ext2, String Ext3, String OrgCode, String YearLimit,
            String MethodType, String StandardType, boolean MehodStatus,
            String AcceptRule, String CompareRule, String RoundRule, int LabelType,
            String Formula) {
        this.Id = Id;
        this.RelationItems = RelationItems;
        this.TagId = TagId;
        this.EsId = EsId;
        this.GroupIdExt = GroupIdExt;
        this.GroupId = GroupId;
        this.Index = Index;
        this.FullName = FullName;
        this.FullNameSpell = FullNameSpell;
        this.ShortName = ShortName;
        this.ShortNameSpell = ShortNameSpell;
        this.StandardCode = StandardCode;
        this.ReplaceSC = ReplaceSC;
        this.AttachFileId = AttachFileId;
        this.AttachFileName = AttachFileName;
        this.PublishTime = PublishTime;
        this.ExecuteTime = ExecuteTime;
        this.Flag = Flag;
        this.IsDefault = IsDefault;
        this.UpdateTime = UpdateTime;
        this.FormulaPath = FormulaPath;
        this.Ext1 = Ext1;
        this.Ext2 = Ext2;
        this.Ext3 = Ext3;
        this.OrgCode = OrgCode;
        this.YearLimit = YearLimit;
        this.MethodType = MethodType;
        this.StandardType = StandardType;
        this.MehodStatus = MehodStatus;
        this.AcceptRule = AcceptRule;
        this.CompareRule = CompareRule;
        this.RoundRule = RoundRule;
        this.LabelType = LabelType;
        this.Formula = Formula;
    }
    @Generated(hash = 632479964)
    public Methods() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getRelationItems() {
        return this.RelationItems;
    }
    public void setRelationItems(String RelationItems) {
        this.RelationItems = RelationItems;
    }
    public String getTagId() {
        return this.TagId;
    }
    public void setTagId(String TagId) {
        this.TagId = TagId;
    }
    public String getEsId() {
        return this.EsId;
    }
    public void setEsId(String EsId) {
        this.EsId = EsId;
    }
    public String getGroupIdExt() {
        return this.GroupIdExt;
    }
    public void setGroupIdExt(String GroupIdExt) {
        this.GroupIdExt = GroupIdExt;
    }
    public String getGroupId() {
        return this.GroupId;
    }
    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }
    public String getIndex() {
        return this.Index;
    }
    public void setIndex(String Index) {
        this.Index = Index;
    }
    public String getFullName() {
        return this.FullName;
    }
    public void setFullName(String FullName) {
        this.FullName = FullName;
    }
    public String getFullNameSpell() {
        return this.FullNameSpell;
    }
    public void setFullNameSpell(String FullNameSpell) {
        this.FullNameSpell = FullNameSpell;
    }
    public String getShortName() {
        return this.ShortName;
    }
    public void setShortName(String ShortName) {
        this.ShortName = ShortName;
    }
    public String getShortNameSpell() {
        return this.ShortNameSpell;
    }
    public void setShortNameSpell(String ShortNameSpell) {
        this.ShortNameSpell = ShortNameSpell;
    }
    public String getStandardCode() {
        return this.StandardCode;
    }
    public void setStandardCode(String StandardCode) {
        this.StandardCode = StandardCode;
    }
    public String getReplaceSC() {
        return this.ReplaceSC;
    }
    public void setReplaceSC(String ReplaceSC) {
        this.ReplaceSC = ReplaceSC;
    }
    public String getAttachFileId() {
        return this.AttachFileId;
    }
    public void setAttachFileId(String AttachFileId) {
        this.AttachFileId = AttachFileId;
    }
    public String getAttachFileName() {
        return this.AttachFileName;
    }
    public void setAttachFileName(String AttachFileName) {
        this.AttachFileName = AttachFileName;
    }
    public String getPublishTime() {
        return this.PublishTime;
    }
    public void setPublishTime(String PublishTime) {
        this.PublishTime = PublishTime;
    }
    public String getExecuteTime() {
        return this.ExecuteTime;
    }
    public void setExecuteTime(String ExecuteTime) {
        this.ExecuteTime = ExecuteTime;
    }
    public String getFlag() {
        return this.Flag;
    }
    public void setFlag(String Flag) {
        this.Flag = Flag;
    }
    public String getIsDefault() {
        return this.IsDefault;
    }
    public void setIsDefault(String IsDefault) {
        this.IsDefault = IsDefault;
    }
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
    public String getFormulaPath() {
        return this.FormulaPath;
    }
    public void setFormulaPath(String FormulaPath) {
        this.FormulaPath = FormulaPath;
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
    public String getOrgCode() {
        return this.OrgCode;
    }
    public void setOrgCode(String OrgCode) {
        this.OrgCode = OrgCode;
    }
    public String getYearLimit() {
        return this.YearLimit;
    }
    public void setYearLimit(String YearLimit) {
        this.YearLimit = YearLimit;
    }
    public String getMethodType() {
        return this.MethodType;
    }
    public void setMethodType(String MethodType) {
        this.MethodType = MethodType;
    }
    public String getStandardType() {
        return this.StandardType;
    }
    public void setStandardType(String StandardType) {
        this.StandardType = StandardType;
    }
    public boolean getMehodStatus() {
        return this.MehodStatus;
    }
    public void setMehodStatus(boolean MehodStatus) {
        this.MehodStatus = MehodStatus;
    }
    public String getAcceptRule() {
        return this.AcceptRule;
    }
    public void setAcceptRule(String AcceptRule) {
        this.AcceptRule = AcceptRule;
    }
    public String getCompareRule() {
        return this.CompareRule;
    }
    public void setCompareRule(String CompareRule) {
        this.CompareRule = CompareRule;
    }
    public String getRoundRule() {
        return this.RoundRule;
    }
    public void setRoundRule(String RoundRule) {
        this.RoundRule = RoundRule;
    }
    public int getLabelType() {
        return this.LabelType;
    }
    public void setLabelType(int LabelType) {
        this.LabelType = LabelType;
    }
    public String getFormula() {
        return this.Formula;
    }
    public void setFormula(String Formula) {
        this.Formula = Formula;
    }



}
