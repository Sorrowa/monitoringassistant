package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

import cn.cdjzxy.monitoringassistant.mvp.model.greendao.converter.StringConverter;

@Entity
public class Sampling {

    /**
     * SamplingHeight : null
     * PollutionType : null
     * RainType : null
     * SampProperty : null
     * DeviceId : null
     * DeviceName : null
     * MethodId :
     * MethodName :
     * Weather : 晴天
     * WindSpeed : null
     * Temprature :
     * Pressure :
     * CalibrationFactor : null
     * Transfer :
     * SendSampTime : null
     * ReciveTime : 2018-11-09T13:49:00
     * PrivateData : {"ClientName":"","ClientAdd":"","HandleDevice":"","Receiving":"","waterWD":"","waterLS":"","waterLL":"","waterSW":""}
     * CurUserId : 61bb48f0-5d8f-49a1-989d-7d6d7033fd91
     * CurUserName : Admin
     * Comment :
     * MonitemName : null
     * FormFlows : [{"FlowId":"38233438-bcbc-44a0-801c-ac185d2861f5","FlowName":"送样","NodeNumber":1,"CurrentStatus":0,"IsJoinFlow":false,"NodeHandleCode":"0","AllFlowUsers":null,"FlowUserIds":"","FlowUserNames":""},{"FlowId":"3f87c4f2-bbdc-47f1-9ae8-c73fe86aadc5","FlowName":"校核审核","NodeNumber":2,"CurrentStatus":7,"IsJoinFlow":true,"NodeHandleCode":"40004","AllFlowUsers":null,"FlowUserIds":"61bb48f0-5d8f-49a1-989d-7d6d7033fd91","FlowUserNames":"Admin"},{"FlowId":"da66e2d6-4869-47a3-8fee-3718305949ef","FlowName":"审核","NodeNumber":3,"CurrentStatus":2,"IsJoinFlow":true,"NodeHandleCode":"40005","AllFlowUsers":null,"FlowUserIds":"61bb48f0-5d8f-49a1-989d-7d6d7033fd91","FlowUserNames":"Admin"}]
     * SamplingFormStandResults : [{"Id":"d0828a83-016f-4b84-855c-2e5f44846678","SamplingId":"29380d4a-4f27-421f-a750-3d015f12c42b","StandNo":1,"MonitemIds":"e113d898-1822-e549-8168-04fb822c6c9d","MonitemName":"铁","SamplingAmount":"","AnalysisSite":"","SaveMehtod":"","Preservative":"","Count":1,"Container":"","SaveTimes":"","Index":1},{"Id":"f100fb12-7208-4d48-9648-b92cc11efa37","SamplingId":"29380d4a-4f27-421f-a750-3d015f12c42b","StandNo":2,"MonitemIds":"58a3e4d8-d235-8841-b61d-b3cdf2641114","MonitemName":"pH值","SamplingAmount":"","AnalysisSite":"","SaveMehtod":"","Preservative":"","Count":1,"Container":"","SaveTimes":"","Index":2}]
     * SamplingDetailResults : [{"Id":"36d20d13-eb75-4a38-9e50-30b672a41ece","SamplingId":"29380d4a-4f27-421f-a750-3d015f12c42b","ProjectId":"601435b6-8d4d-45b1-ae79-0124dd0276cb","OrderIndex":1,"SampingCode":"CD20181105-W111310-01","FrequecyNo":1,"SamplingTime":"","SamplingType":0,"SampStandId":"c7d18181-a337-90c7-5513-23e3ccae0de7","MonitemId":"e113d898-1822-e549-8168-04fb822c6c9d","MonitemName":"铁","AddresssId":"a05efff0-2526-40bb-98e0-ac6bf1d4ff42","AddressName":"林芝","SamplingCount":2,"Preservative":"否","SampleCollection":"","SampleAcceptance":"","IsSenceAnalysis":false,"IsCompare":false,"AnasysTime":null,"Value1":null,"ValueUnit1":null,"valueUnit1Name":null,"Value2":null,"valueUnit2":null,"valueUnit2Name":null,"Value3":null,"ValueUnit3":null,"valueUnit3Name":null,"Value":null,"ValueUnit":null,"ValueUnitNname":null,"Value4":null,"Value5":null,"MethodName":null,"MethodId":null,"DeviceIdName":null,"DeviceId":null,"Description":"","PrivateData":null},{"Id":"27edfc0a-691f-4d5a-81a8-d7853570e0f3","SamplingId":"29380d4a-4f27-421f-a750-3d015f12c42b","ProjectId":"601435b6-8d4d-45b1-ae79-0124dd0276cb","OrderIndex":1,"SampingCode":"CD20181105-W111310-01","FrequecyNo":1,"SamplingTime":"","SamplingType":0,"SampStandId":"c415b225-1356-86f4-f5d1-426720b82634","MonitemId":"58a3e4d8-d235-8841-b61d-b3cdf2641114","MonitemName":"pH值","AddresssId":"a05efff0-2526-40bb-98e0-ac6bf1d4ff42","AddressName":"林芝","SamplingCount":2,"Preservative":"否","SampleCollection":"","SampleAcceptance":"","IsSenceAnalysis":false,"IsCompare":false,"AnasysTime":null,"Value1":null,"ValueUnit1":null,"valueUnit1Name":null,"Value2":null,"valueUnit2":null,"valueUnit2Name":null,"Value3":null,"ValueUnit3":null,"valueUnit3Name":null,"Value":null,"ValueUnit":null,"ValueUnitNname":null,"Value4":null,"Value5":null,"MethodName":null,"MethodId":null,"DeviceIdName":null,"DeviceId":null,"Description":"","PrivateData":null}]
     * SamplingUserResults : [{"UserId":"61bb48f0-5d8f-49a1-989d-7d6d7033fd91","UserName":"Admin"},{"UserId":"61bb48f0-5d8f-49a1-989d-7d6d7033fd91","UserName":"Admin"}]
     * Id : 29380d4a-4f27-421f-a750-3d015f12c42b
     * ProjectId : 601435b6-8d4d-45b1-ae79-0124dd0276cb
     * ProjectName : 20181108测试
     * montype : 环境质量监测
     * SamplingNo : CD20181105-W111310
     * FormName : 水和废水采样及交接记录
     * ParentTagId : d877c7d5-6bb8-42d4-a79c-0f644e130a62
     * TagId : ad7677ae-3bee-492d-bd2a-1ae830f9d9ba
     * TagName : 地下水
     * AddressId : a05efff0-2526-40bb-98e0-ac6bf1d4ff42
     * AddressName : 林芝
     * SamplingUserId : 61bb48f0-5d8f-49a1-989d-7d6d7033fd91
     * SamplingUserName : Admin
     * SubmitId : 61bb48f0-5d8f-49a1-989d-7d6d7033fd91
     * SubmitName : Admin
     * SamplingTimeBegin : 2018-11-05T00:00:00
     * SamplingTimeEnd : 2018-11-05T00:00:00
     * Status : 7
     * StatusName : 等待校核审核
     * UpdateTime : 2018-11-09T13:49:00.5
     */

    private String                  Id;
    private String                  ProjectId;
    private String                  SamplingNo;
    private String                  FormPath;
    private String                  FormName;
    private String                  ProjectName;
    private String                  Montype;
    private String                  SamplingTimeBegin;
    private String                  SamplingTimeEnd;
    private String                  ParentTagId;
    private String                  TagId;
    private String                  TagName;
    private String                  AddressId;
    private String                  AddressName;
    private String                  AddressNo;
    private String                  SamplingHeight;
    private String                  PollutionType;
    private String                  RainType;
    private String                  SampProperty;
    private String                  FormType;
    private String                  FormTypeName;
    private String                  DeviceId;
    private String                  DeviceName;
    private String                  MethodId;
    private String                  MethodName;
    private String                  Weather;
    private String                  WindSpeed;
    private String                  Temprature;
    private String                  Pressure;
    private String                  CalibrationFactor;
    private String                  Transfer;
    private String                  SendSampTime;
    private String                  ReciveTime;
    private String                  PrivateData;
    private String                  SamplingUserId;
    private String                  SamplingUserName;
    private String                  SubmitId;
    private String                  SubmitName;
    private String                  SubmitDate;
    private String                  MonitorPerson;
    private String                  MonitorTime;
    private int                     Status;
    private String                  StatusName;
    private int                     TransStatus;
    private String                  TransStatusName;
    private String                  CurUserId;
    private String                  CurUserName;
    private String                  FormFlows;
    private String                  Comment;
    private String                  AddTime;
    private String                  UpdateTime;
    private int                     Version;
    private String                  MonitemId;
    private String                  MonitemName;
    private String                  Recoding;
    private String                  ProjectNo;
    private String                  file;
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String>            SamplingUserResults;
    @Transient
    private List<SamplingFormStand> SamplingFormStandResults;
    @Transient
    private List<SamplingDetail>    SamplingDetailResults;
    @Transient
    private boolean        isSelected;
    @Generated(hash = 13489724)
    public Sampling(String Id, String ProjectId, String SamplingNo, String FormPath, String FormName, String ProjectName, String Montype, String SamplingTimeBegin, String SamplingTimeEnd, String ParentTagId, String TagId, String TagName, String AddressId, String AddressName, String AddressNo, String SamplingHeight, String PollutionType, String RainType, String SampProperty, String FormType, String FormTypeName, String DeviceId, String DeviceName, String MethodId, String MethodName, String Weather, String WindSpeed, String Temprature, String Pressure, String CalibrationFactor, String Transfer, String SendSampTime, String ReciveTime, String PrivateData, String SamplingUserId, String SamplingUserName, String SubmitId, String SubmitName, String SubmitDate, String MonitorPerson, String MonitorTime, int Status, String StatusName, int TransStatus, String TransStatusName, String CurUserId, String CurUserName, String FormFlows, String Comment, String AddTime, String UpdateTime, int Version, String MonitemId, String MonitemName, String Recoding, String ProjectNo, String file, List<String> SamplingUserResults) {
        this.Id = Id;
        this.ProjectId = ProjectId;
        this.SamplingNo = SamplingNo;
        this.FormPath = FormPath;
        this.FormName = FormName;
        this.ProjectName = ProjectName;
        this.Montype = Montype;
        this.SamplingTimeBegin = SamplingTimeBegin;
        this.SamplingTimeEnd = SamplingTimeEnd;
        this.ParentTagId = ParentTagId;
        this.TagId = TagId;
        this.TagName = TagName;
        this.AddressId = AddressId;
        this.AddressName = AddressName;
        this.AddressNo = AddressNo;
        this.SamplingHeight = SamplingHeight;
        this.PollutionType = PollutionType;
        this.RainType = RainType;
        this.SampProperty = SampProperty;
        this.FormType = FormType;
        this.FormTypeName = FormTypeName;
        this.DeviceId = DeviceId;
        this.DeviceName = DeviceName;
        this.MethodId = MethodId;
        this.MethodName = MethodName;
        this.Weather = Weather;
        this.WindSpeed = WindSpeed;
        this.Temprature = Temprature;
        this.Pressure = Pressure;
        this.CalibrationFactor = CalibrationFactor;
        this.Transfer = Transfer;
        this.SendSampTime = SendSampTime;
        this.ReciveTime = ReciveTime;
        this.PrivateData = PrivateData;
        this.SamplingUserId = SamplingUserId;
        this.SamplingUserName = SamplingUserName;
        this.SubmitId = SubmitId;
        this.SubmitName = SubmitName;
        this.SubmitDate = SubmitDate;
        this.MonitorPerson = MonitorPerson;
        this.MonitorTime = MonitorTime;
        this.Status = Status;
        this.StatusName = StatusName;
        this.TransStatus = TransStatus;
        this.TransStatusName = TransStatusName;
        this.CurUserId = CurUserId;
        this.CurUserName = CurUserName;
        this.FormFlows = FormFlows;
        this.Comment = Comment;
        this.AddTime = AddTime;
        this.UpdateTime = UpdateTime;
        this.Version = Version;
        this.MonitemId = MonitemId;
        this.MonitemName = MonitemName;
        this.Recoding = Recoding;
        this.ProjectNo = ProjectNo;
        this.file = file;
        this.SamplingUserResults = SamplingUserResults;
    }
    @Generated(hash = 1062560110)
    public Sampling() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getProjectId() {
        return this.ProjectId;
    }
    public void setProjectId(String ProjectId) {
        this.ProjectId = ProjectId;
    }
    public String getSamplingNo() {
        return this.SamplingNo;
    }
    public void setSamplingNo(String SamplingNo) {
        this.SamplingNo = SamplingNo;
    }
    public String getFormPath() {
        return this.FormPath;
    }
    public void setFormPath(String FormPath) {
        this.FormPath = FormPath;
    }
    public String getFormName() {
        return this.FormName;
    }
    public void setFormName(String FormName) {
        this.FormName = FormName;
    }
    public String getProjectName() {
        return this.ProjectName;
    }
    public void setProjectName(String ProjectName) {
        this.ProjectName = ProjectName;
    }
    public String getMontype() {
        return this.Montype;
    }
    public void setMontype(String Montype) {
        this.Montype = Montype;
    }
    public String getSamplingTimeBegin() {
        return this.SamplingTimeBegin;
    }
    public void setSamplingTimeBegin(String SamplingTimeBegin) {
        this.SamplingTimeBegin = SamplingTimeBegin;
    }
    public String getSamplingTimeEnd() {
        return this.SamplingTimeEnd;
    }
    public void setSamplingTimeEnd(String SamplingTimeEnd) {
        this.SamplingTimeEnd = SamplingTimeEnd;
    }
    public String getParentTagId() {
        return this.ParentTagId;
    }
    public void setParentTagId(String ParentTagId) {
        this.ParentTagId = ParentTagId;
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
    public String getAddressId() {
        return this.AddressId;
    }
    public void setAddressId(String AddressId) {
        this.AddressId = AddressId;
    }
    public String getAddressName() {
        return this.AddressName;
    }
    public void setAddressName(String AddressName) {
        this.AddressName = AddressName;
    }
    public String getAddressNo() {
        return this.AddressNo;
    }
    public void setAddressNo(String AddressNo) {
        this.AddressNo = AddressNo;
    }
    public String getSamplingHeight() {
        return this.SamplingHeight;
    }
    public void setSamplingHeight(String SamplingHeight) {
        this.SamplingHeight = SamplingHeight;
    }
    public String getPollutionType() {
        return this.PollutionType;
    }
    public void setPollutionType(String PollutionType) {
        this.PollutionType = PollutionType;
    }
    public String getRainType() {
        return this.RainType;
    }
    public void setRainType(String RainType) {
        this.RainType = RainType;
    }
    public String getSampProperty() {
        return this.SampProperty;
    }
    public void setSampProperty(String SampProperty) {
        this.SampProperty = SampProperty;
    }
    public String getFormType() {
        return this.FormType;
    }
    public void setFormType(String FormType) {
        this.FormType = FormType;
    }
    public String getFormTypeName() {
        return this.FormTypeName;
    }
    public void setFormTypeName(String FormTypeName) {
        this.FormTypeName = FormTypeName;
    }
    public String getDeviceId() {
        return this.DeviceId;
    }
    public void setDeviceId(String DeviceId) {
        this.DeviceId = DeviceId;
    }
    public String getDeviceName() {
        return this.DeviceName;
    }
    public void setDeviceName(String DeviceName) {
        this.DeviceName = DeviceName;
    }
    public String getMethodId() {
        return this.MethodId;
    }
    public void setMethodId(String MethodId) {
        this.MethodId = MethodId;
    }
    public String getMethodName() {
        return this.MethodName;
    }
    public void setMethodName(String MethodName) {
        this.MethodName = MethodName;
    }
    public String getWeather() {
        return this.Weather;
    }
    public void setWeather(String Weather) {
        this.Weather = Weather;
    }
    public String getWindSpeed() {
        return this.WindSpeed;
    }
    public void setWindSpeed(String WindSpeed) {
        this.WindSpeed = WindSpeed;
    }
    public String getTemprature() {
        return this.Temprature;
    }
    public void setTemprature(String Temprature) {
        this.Temprature = Temprature;
    }
    public String getPressure() {
        return this.Pressure;
    }
    public void setPressure(String Pressure) {
        this.Pressure = Pressure;
    }
    public String getCalibrationFactor() {
        return this.CalibrationFactor;
    }
    public void setCalibrationFactor(String CalibrationFactor) {
        this.CalibrationFactor = CalibrationFactor;
    }
    public String getTransfer() {
        return this.Transfer;
    }
    public void setTransfer(String Transfer) {
        this.Transfer = Transfer;
    }
    public String getSendSampTime() {
        return this.SendSampTime;
    }
    public void setSendSampTime(String SendSampTime) {
        this.SendSampTime = SendSampTime;
    }
    public String getReciveTime() {
        return this.ReciveTime;
    }
    public void setReciveTime(String ReciveTime) {
        this.ReciveTime = ReciveTime;
    }
    public String getPrivateData() {
        return this.PrivateData;
    }
    public void setPrivateData(String PrivateData) {
        this.PrivateData = PrivateData;
    }
    public String getSamplingUserId() {
        return this.SamplingUserId;
    }
    public void setSamplingUserId(String SamplingUserId) {
        this.SamplingUserId = SamplingUserId;
    }
    public String getSamplingUserName() {
        return this.SamplingUserName;
    }
    public void setSamplingUserName(String SamplingUserName) {
        this.SamplingUserName = SamplingUserName;
    }
    public String getSubmitId() {
        return this.SubmitId;
    }
    public void setSubmitId(String SubmitId) {
        this.SubmitId = SubmitId;
    }
    public String getSubmitName() {
        return this.SubmitName;
    }
    public void setSubmitName(String SubmitName) {
        this.SubmitName = SubmitName;
    }
    public String getSubmitDate() {
        return this.SubmitDate;
    }
    public void setSubmitDate(String SubmitDate) {
        this.SubmitDate = SubmitDate;
    }
    public String getMonitorPerson() {
        return this.MonitorPerson;
    }
    public void setMonitorPerson(String MonitorPerson) {
        this.MonitorPerson = MonitorPerson;
    }
    public String getMonitorTime() {
        return this.MonitorTime;
    }
    public void setMonitorTime(String MonitorTime) {
        this.MonitorTime = MonitorTime;
    }
    public int getStatus() {
        return this.Status;
    }
    public void setStatus(int Status) {
        this.Status = Status;
    }
    public String getStatusName() {
        return this.StatusName;
    }
    public void setStatusName(String StatusName) {
        this.StatusName = StatusName;
    }
    public int getTransStatus() {
        return this.TransStatus;
    }
    public void setTransStatus(int TransStatus) {
        this.TransStatus = TransStatus;
    }
    public String getTransStatusName() {
        return this.TransStatusName;
    }
    public void setTransStatusName(String TransStatusName) {
        this.TransStatusName = TransStatusName;
    }
    public String getCurUserId() {
        return this.CurUserId;
    }
    public void setCurUserId(String CurUserId) {
        this.CurUserId = CurUserId;
    }
    public String getCurUserName() {
        return this.CurUserName;
    }
    public void setCurUserName(String CurUserName) {
        this.CurUserName = CurUserName;
    }
    public String getFormFlows() {
        return this.FormFlows;
    }
    public void setFormFlows(String FormFlows) {
        this.FormFlows = FormFlows;
    }
    public String getComment() {
        return this.Comment;
    }
    public void setComment(String Comment) {
        this.Comment = Comment;
    }
    public String getAddTime() {
        return this.AddTime;
    }
    public void setAddTime(String AddTime) {
        this.AddTime = AddTime;
    }
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
    public int getVersion() {
        return this.Version;
    }
    public void setVersion(int Version) {
        this.Version = Version;
    }
    public String getMonitemId() {
        return this.MonitemId;
    }
    public void setMonitemId(String MonitemId) {
        this.MonitemId = MonitemId;
    }
    public String getMonitemName() {
        return this.MonitemName;
    }
    public void setMonitemName(String MonitemName) {
        this.MonitemName = MonitemName;
    }
    public String getRecoding() {
        return this.Recoding;
    }
    public void setRecoding(String Recoding) {
        this.Recoding = Recoding;
    }
    public String getProjectNo() {
        return this.ProjectNo;
    }
    public void setProjectNo(String ProjectNo) {
        this.ProjectNo = ProjectNo;
    }
    public String getFile() {
        return this.file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public List<String> getSamplingUserResults() {
        return this.SamplingUserResults;
    }
    public void setSamplingUserResults(List<String> SamplingUserResults) {
        this.SamplingUserResults = SamplingUserResults;
    }


    public List<SamplingFormStand> getSamplingFormStandResults() {
        return SamplingFormStandResults;
    }

    public void setSamplingFormStandResults(List<SamplingFormStand> samplingFormStandResults) {
        SamplingFormStandResults = samplingFormStandResults;
    }

    public List<SamplingDetail> getSamplingDetailResults() {
        return SamplingDetailResults;
    }

    public void setSamplingDetailResults(List<SamplingDetail> samplingDetailResults) {
        SamplingDetailResults = samplingDetailResults;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}