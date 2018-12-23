package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

import java.util.List;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFormStand;

public class SampForm {

    /**
     * IsAdd : false
     * IsSubmit : false
     * SampForm : {"SamplingDetails":[{"SamplingId":"f5accc0e-846f-4062-8803-f3ee4a7b3485","ProjectId":"7f2f030e-c690-45d1-a0c7-2f8c35ec6df6","IsSenceAnalysis":false,"SampStandId":"8ed9a41c-767e-fb5a-b401-6f9b64b29ed1","MonitemId":"1f46483a-02fb-d04e-ae42-2105d40d503f","MonitemName":"氨氮","SampingCode":"DXS181130051206","AddresssId":"2b73336c-74c1-4c36-8966-202f03b034f2","AddressName":"地表水","OrderIndex":"1","FrequecyNo":"12","SamplingTime":"","SamplingType":0,"SamplingCount":"1","Preservative":"否","IsCompare":false,"SampleCollection":"","SampleAcceptance":"","Description":""}],"SamplingFormStands":[{"Id":"09e2b476-0083-48c5-bfa5-c563ac2c658b","SamplingId":"f5accc0e-846f-4062-8803-f3ee4a7b3485","StandNo":1,"MonitemIds":"1f46483a-02fb-d04e-ae42-2105d40d503f","MonitemName":"氨氮","SamplingAmount":"500ml× 瓶  ","AnalysisSite":"","SaveMehtod":"","Preservative":"","Count":"1","Container":"G","SaveTimes":"","Index":"1","UpdateTime":"2018-11-30 11:24:51","MonItems":["1f46483a-02fb-d04e-ae42-2105d40d503f"]}],"SamplingDetailYQFs":[],"Id":"f5accc0e-846f-4062-8803-f3ee4a7b3485","ProjectId":"7f2f030e-c690-45d1-a0c7-2f8c35ec6df6","SamplingNo":"18113005","FormPath":"/FormTemplate/FILL_WATER_NEW_XD","FormName":"水和废水采样及交接记录","ProjectName":"测试待办","Montype":"3","SamplingTimeBegin":"2018-11-30","SamplingTimeEnd":"2018-11-30","ParentTagId":"d877c7d5-6bb8-42d4-a79c-0f644e130a62","TagId":"ad7677ae-3bee-492d-bd2a-1ae830f9d9ba","TagName":"地下水","AddressId":"2b73336c-74c1-4c36-8966-202f03b034f2","AddressName":"地表水","AddressNo":null,"SamplingHeight":null,"PollutionType":null,"RainType":null,"SampProperty":null,"FormType":"d877c7d5-6bb8-42d4-a79c-0f644e130a62","FormTypeName":"水","DeviceId":null,"DeviceName":null,"MethodId":"c0ad626d-c96b-4144-8c2b-2bea8c9e69d1","MethodName":"水污染物排放总量监测技术规范 (流量 容器法)(HJ/T92-2002)","Weather":"多云","WindSpeed":null,"Temprature":"25","Pressure":"155","CalibrationFactor":null,"Transfer":"运输方式1","SendSampTime":"2018-11-30 10:52","ReciveTime":"","PrivateData":"{\"ClientName\":\"企业名称1\",\"ClientAdd\":\"企业地址1\",\"HandleDevice\":\"处理设施1\",\"Receiving\":\"受纳水体1\",\"FrequencyNo\":\"12\",\"SewageDisposal\":\"是\",\"TransportConditions\":\"好\",\"TempFrequency\":12,\"waterWD\":\"21\",\"waterLS\":\"12\",\"waterLL\":\"215\"}","SamplingUserId":"6e2c449a-9ce1-412b-bac8-ff7c369814e0,a1f2a31b-3543-4166-939d-f0040b50102c","SamplingUserName":"Admin,胡丽梅","SubmitId":"6e2c449a-9ce1-412b-bac8-ff7c369814e0","SubmitName":"Admin","SubmitDate":null,"MonitorPerson":"Admin","MonitorTime":"2018-11-30 11:13:14","Status":0,"StatusName":"等待提交","TransStatus":3,"TransStatusName":"等待提交","CurUserId":"6e2c449a-9ce1-412b-bac8-ff7c369814e0","CurUserName":"Admin","FormFlows":"[{\"FlowId\":\"3f87c4f2-bbdc-47f1-9ae8-c73fe86aadc5\",\"FlowName\":\"校核审核\",\"NodeNumber\":2,\"CurrentStatus\":7,\"IsJoinFlow\":true,\"NodeHandleCode\":\"40004\",\"AllFlowUsers\":null,\"FlowUserIds\":\"26083573-ce5f-4593-a798-5d011ff09a8e,6e2c449a-9ce1-412b-bac8-ff7c369814e0,a1f2a31b-3543-4166-939d-f0040b50144c,a1f2a31b-3543-4166-939d-f0040b50148c,a1f2a31b-3543-4166-939d-f0040b50152c,95b1b5b3-6655-435f-9669-8ad10a12d160,0db1c4b0-e735-41d5-9cad-e62cef25fbdf,52a76a42-5ed5-4f13-8ed0-2998db5f9520,ccd10d7e-ffa1-4d70-807e-c289a4e0f2bb,d8c74e73-e570-4a30-89d8-c9def4337332,c628129e-47df-43ba-a96a-f3df3df06463,4d76b345-bc76-42d4-9486-9afbc8b06251\",\"FlowUserNames\":\"李竞,Admin,石玉科,张雪锋,闫海全,钱志,周静静,黄强,Mr.WU,李竞,易飞,邓宇杰\"},{\"FlowId\":\"da66e2d6-4869-47a3-8fee-3718305949ef\",\"FlowName\":\"审核\",\"NodeNumber\":3,\"CurrentStatus\":2,\"IsJoinFlow\":true,\"NodeHandleCode\":\"40005\",\"AllFlowUsers\":null,\"FlowUserIds\":\"26083573-ce5f-4593-a798-5d011ff09a8e,6e2c449a-9ce1-412b-bac8-ff7c369814e0,a1f2a31b-3543-4166-939d-f0040b50148c,a1f2a31b-3543-4166-939d-f0040b50152c,95b1b5b3-6655-435f-9669-8ad10a12d160,0db1c4b0-e735-41d5-9cad-e62cef25fbdf,52a76a42-5ed5-4f13-8ed0-2998db5f9520,ccd10d7e-ffa1-4d70-807e-c289a4e0f2bb,d8c74e73-e570-4a30-89d8-c9def4337332,c628129e-47df-43ba-a96a-f3df3df06463,4d76b345-bc76-42d4-9486-9afbc8b06251\",\"FlowUserNames\":\"李竞,Admin,张雪锋,闫海全,钱志,周静静,黄强,Mr.WU,李竞,易飞,邓宇杰\"}]","Comment":"<p>问我群二群翁无群二<\/p>","AddTime":"2018-11-30 11:13:14","UpdateTime":"2018-11-30 13:54:53.757","Version":0,"MonitemId":null,"MonitemName":null,"Recoding":null,"ProjectNo":"CDHJ20180020","file":""}
     * UploadFiles : []
     * DelFiles : []
     */

    private boolean      IsAdd;

    public boolean isAdd() {
        return IsAdd;
    }

    public void setAdd(boolean add) {
        IsAdd = add;
    }

    public boolean isSubmit() {
        return IsSubmit;
    }

    public void setSubmit(boolean submit) {
        IsSubmit = submit;
    }

    public SampFormBean getSampForm() {
        return SampForm;
    }

    public void setSampForm(SampFormBean sampForm) {
        SampForm = sampForm;
    }

    public List<String> getUploadFiles() {
        return UploadFiles;
    }

    public void setUploadFiles(List<String> uploadFiles) {
        UploadFiles = uploadFiles;
    }

    public List<String> getDelFiles() {
        return DelFiles;
    }

    public void setDelFiles(List<String> delFiles) {
        DelFiles = delFiles;
    }

    private boolean      IsSubmit;
    private SampFormBean SampForm;
    private List<String> UploadFiles;
    private List<String> DelFiles;

    public static class SampFormBean {
        private String                  Id;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getProjectId() {
            return ProjectId;
        }

        public void setProjectId(String projectId) {
            ProjectId = projectId;
        }

        public String getSamplingNo() {
            return SamplingNo;
        }

        public void setSamplingNo(String samplingNo) {
            SamplingNo = samplingNo;
        }

        public String getFormPath() {
            return FormPath;
        }

        public void setFormPath(String formPath) {
            FormPath = formPath;
        }

        public String getFormName() {
            return FormName;
        }

        public void setFormName(String formName) {
            FormName = formName;
        }

        public String getProjectName() {
            return ProjectName;
        }

        public void setProjectName(String projectName) {
            ProjectName = projectName;
        }

        public String getMontype() {
            return Montype;
        }

        public void setMontype(String montype) {
            Montype = montype;
        }

        public String getSamplingTimeBegin() {
            return SamplingTimeBegin;
        }

        public void setSamplingTimeBegin(String samplingTimeBegin) {
            SamplingTimeBegin = samplingTimeBegin;
        }

        public String getSamplingTimeEnd() {
            return SamplingTimeEnd;
        }

        public void setSamplingTimeEnd(String samplingTimeEnd) {
            SamplingTimeEnd = samplingTimeEnd;
        }

        public String getParentTagId() {
            return ParentTagId;
        }

        public void setParentTagId(String parentTagId) {
            ParentTagId = parentTagId;
        }

        public String getTagId() {
            return TagId;
        }

        public void setTagId(String tagId) {
            TagId = tagId;
        }

        public String getTagName() {
            return TagName;
        }

        public void setTagName(String tagName) {
            TagName = tagName;
        }

        public String getAddressId() {
            return AddressId;
        }

        public void setAddressId(String addressId) {
            AddressId = addressId;
        }

        public String getAddressName() {
            return AddressName;
        }

        public void setAddressName(String addressName) {
            AddressName = addressName;
        }

        public String getAddressNo() {
            return AddressNo;
        }

        public void setAddressNo(String addressNo) {
            AddressNo = addressNo;
        }

        public String getSamplingHeight() {
            return SamplingHeight;
        }

        public void setSamplingHeight(String samplingHeight) {
            SamplingHeight = samplingHeight;
        }

        public String getPollutionType() {
            return PollutionType;
        }

        public void setPollutionType(String pollutionType) {
            PollutionType = pollutionType;
        }

        public String getRainType() {
            return RainType;
        }

        public void setRainType(String rainType) {
            RainType = rainType;
        }

        public String getSampProperty() {
            return SampProperty;
        }

        public void setSampProperty(String sampProperty) {
            SampProperty = sampProperty;
        }

        public String getFormType() {
            return FormType;
        }

        public void setFormType(String formType) {
            FormType = formType;
        }

        public String getFormTypeName() {
            return FormTypeName;
        }

        public void setFormTypeName(String formTypeName) {
            FormTypeName = formTypeName;
        }

        public String getDeviceId() {
            return DeviceId;
        }

        public void setDeviceId(String deviceId) {
            DeviceId = deviceId;
        }

        public String getDeviceName() {
            return DeviceName;
        }

        public void setDeviceName(String deviceName) {
            DeviceName = deviceName;
        }

        public String getMethodId() {
            return MethodId;
        }

        public void setMethodId(String methodId) {
            MethodId = methodId;
        }

        public String getMethodName() {
            return MethodName;
        }

        public void setMethodName(String methodName) {
            MethodName = methodName;
        }

        public String getWeather() {
            return Weather;
        }

        public void setWeather(String weather) {
            Weather = weather;
        }

        public String getWindSpeed() {
            return WindSpeed;
        }

        public void setWindSpeed(String windSpeed) {
            WindSpeed = windSpeed;
        }

        public String getTemprature() {
            return Temprature;
        }

        public void setTemprature(String temprature) {
            Temprature = temprature;
        }

        public String getPressure() {
            return Pressure;
        }

        public void setPressure(String pressure) {
            Pressure = pressure;
        }

        public String getCalibrationFactor() {
            return CalibrationFactor;
        }

        public void setCalibrationFactor(String calibrationFactor) {
            CalibrationFactor = calibrationFactor;
        }

        public String getTransfer() {
            return Transfer;
        }

        public void setTransfer(String transfer) {
            Transfer = transfer;
        }

        public String getSendSampTime() {
            return SendSampTime;
        }

        public void setSendSampTime(String sendSampTime) {
            SendSampTime = sendSampTime;
        }

        public String getReciveTime() {
            return ReciveTime;
        }

        public void setReciveTime(String reciveTime) {
            ReciveTime = reciveTime;
        }

        public String getPrivateData() {
            return PrivateData;
        }

        public void setPrivateData(String privateData) {
            PrivateData = privateData;
        }

        public String getSamplingUserId() {
            return SamplingUserId;
        }

        public void setSamplingUserId(String samplingUserId) {
            SamplingUserId = samplingUserId;
        }

        public String getSamplingUserName() {
            return SamplingUserName;
        }

        public void setSamplingUserName(String samplingUserName) {
            SamplingUserName = samplingUserName;
        }

        public String getSubmitId() {
            return SubmitId;
        }

        public void setSubmitId(String submitId) {
            SubmitId = submitId;
        }

        public String getSubmitName() {
            return SubmitName;
        }

        public void setSubmitName(String submitName) {
            SubmitName = submitName;
        }

        public String getSubmitDate() {
            return SubmitDate;
        }

        public void setSubmitDate(String submitDate) {
            SubmitDate = submitDate;
        }

        public String getMonitorPerson() {
            return MonitorPerson;
        }

        public void setMonitorPerson(String monitorPerson) {
            MonitorPerson = monitorPerson;
        }

        public String getMonitorTime() {
            return MonitorTime;
        }

        public void setMonitorTime(String monitorTime) {
            MonitorTime = monitorTime;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

        public String getStatusName() {
            return StatusName;
        }

        public void setStatusName(String statusName) {
            StatusName = statusName;
        }

        public int getTransStatus() {
            return TransStatus;
        }

        public void setTransStatus(int transStatus) {
            TransStatus = transStatus;
        }

        public String getTransStatusName() {
            return TransStatusName;
        }

        public void setTransStatusName(String transStatusName) {
            TransStatusName = transStatusName;
        }

        public String getCurUserId() {
            return CurUserId;
        }

        public void setCurUserId(String curUserId) {
            CurUserId = curUserId;
        }

        public String getCurUserName() {
            return CurUserName;
        }

        public void setCurUserName(String curUserName) {
            CurUserName = curUserName;
        }

        public String getFormFlows() {
            return FormFlows;
        }

        public void setFormFlows(String formFlows) {
            FormFlows = formFlows;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String comment) {
            Comment = comment;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String addTime) {
            AddTime = addTime;
        }

        public String getUpdateTime() {
            return UpdateTime;
        }

        public void setUpdateTime(String updateTime) {
            UpdateTime = updateTime;
        }

        public int getVersion() {
            return Version;
        }

        public void setVersion(int version) {
            Version = version;
        }

        public String getMonitemId() {
            return MonitemId;
        }

        public void setMonitemId(String monitemId) {
            MonitemId = monitemId;
        }

        public String getMonitemName() {
            return MonitemName;
        }

        public void setMonitemName(String monitemName) {
            MonitemName = monitemName;
        }

        public String getRecoding() {
            return Recoding;
        }

        public void setRecoding(String recoding) {
            Recoding = recoding;
        }

        public String getProjectNo() {
            return ProjectNo;
        }

        public void setProjectNo(String projectNo) {
            ProjectNo = projectNo;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public List<SamplingDetail> getSamplingDetails() {
            return SamplingDetails;
        }

        public void setSamplingDetails(List<SamplingDetail> samplingDetails) {
            SamplingDetails = samplingDetails;
        }

        public List<SamplingFormStand> getSamplingFormStands() {
            return SamplingFormStands;
        }

        public void setSamplingFormStands(List<SamplingFormStand> samplingFormStands) {
            SamplingFormStands = samplingFormStands;
        }

        public List<String> getSamplingDetailYQFs() {
            return SamplingDetailYQFs;
        }

        public void setSamplingDetailYQFs(List<String> samplingDetailYQFs) {
            SamplingDetailYQFs = samplingDetailYQFs;
        }

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
        private List<SamplingDetail>    SamplingDetails;
        private List<SamplingFormStand> SamplingFormStands;
        private List<String>            SamplingDetailYQFs;

    }
}
