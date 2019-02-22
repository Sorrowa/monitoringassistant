package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

import java.util.List;

public class PreciptationSampForm {

    /**
     * IsAdd : true
     * IsSubmit : true
     * SampForm : {"ProjectId":"7f2f030e-c690-45d1-a0c7-2f8c35ec6df6","FormPath":"/FormTemplate/FILL_JS_GAS_XD","FormName":"降水采样及交接记录","ProjectName":"测试待办","ProjectNo":"CDHJ20180020","Montype":"3","ParentTagId":"f5efa16d-3aae-4d5d-9241-8b80e121d249","FormType":"f5efa16d-3aae-4d5d-9241-8b80e121d249","FormTypeName":"降水","PrivateData":"{\"ClientName\":null,\"ClientAdd\":null,\"SampHight\":\"采样高度\",\"SampArea\":\"采样器接雨面积\"}","SamplingDetails":[{"SamplingId":"00000000-0000-0000-0000-000000000000","ProjectId":"7f2f030e-c690-45d1-a0c7-2f8c35ec6df6","IsSenceAnalysis":true,"SampStandId":"00000000-0000-0000-0000-000000000000","MonitemId":"7253950a-9daa-9d4f-bd9a-a84789279c2a","MonitemName":"降水量","AddresssId":"fe39b598-29ec-47c9-9a7f-3be6e17b37e7","AddressName":"合江县环监站","PrivateData":"{\"SDataTime\":\"2018-11-30 14:33\",\"EDataTime\":\"2018-11-30 14:33\",\"RainVol\":\"222222\"}","Value":"11111","OrderIndex":"1","FrequecyNo":"1","Value1":"11111","Description":"备注"}],"SendSampTime":"2018-11-30 14:33","SamplingNo":"","SamplingTimeBegin":"2018-11-30","TagName":"降水","TagId":"5e9a4bdc-a648-4f8c-9806-71e12449422c","AddressId":"fe39b598-29ec-47c9-9a7f-3be6e17b37e7","AddressName":"合江县环监站","AddressNo":"点位编号","MonitemName":"","MethodName":"酸沉降监测技术规范(HJ/T165-2004)","MethodId":"1ec725ef-c8cb-4a25-9370-2b16bd1a94c4","DeviceId":"a4619ba6-6259-4a69-a4ff-1718a9c0717f,6801ed9f-736e-4ba7-895b-714969d75962,4e5e1ee2-9d17-4360-a829-001017df8bb3","DeviceName":"便携式低温冷藏装置(W35DC)07000687(校准),智能降水监测仪(崂应5021)V02015740(校准),空调（1.5P）(KFR-35GW/K(35556)D1-N2)939920024221(校准)","Transfer":"运输方式","ReciveTime":"","file":"","layTableCheckbox":"on","SamplingUserId":"6e2c449a-9ce1-412b-bac8-ff7c369814e0,a1f2a31b-3543-4166-939d-f0040b50102c","SamplingUserName":"Admin,胡丽梅","SamplingTimeEnd":"2018-11-30","Comment":"<p>魏奇奇翁无群<\/p>","FormFlows":"[]","SamplingFormStands":[{"Id":"904b99e9-b5e4-c98c-e0fe-83b7f7ced00f","SamplingId":"00000000-0000-0000-0000-000000000000","MonitemIds":"7253950a-9daa-9d4f-bd9a-a84789279c2a","MonitemName":"降水量"}]}
     * UploadFiles : []
     * DelFiles : []
     */

    private boolean IsAdd;
    private boolean IsSubmit;

    public boolean isDevceForm() {
        return isDevceForm;
    }

    public void setDevceForm(boolean devceForm) {
        isDevceForm = devceForm;
    }

    private boolean isDevceForm;
    private SampFormBean SampForm;
    private List<PreciptationSampForm.SampFormBean.SamplingFileBean> UploadFiles;
    private List<String> DelFiles;

    private boolean isCompelSubmit;//是否强制提交
    private String addTime;
    private String updateTime;

    public boolean isIsAdd() {
        return IsAdd;
    }

    public void setIsAdd(boolean IsAdd) {
        this.IsAdd = IsAdd;
    }

    public boolean isIsSubmit() {
        return IsSubmit;
    }

    public void setIsSubmit(boolean IsSubmit) {
        this.IsSubmit = IsSubmit;
    }

    public SampFormBean getSampForm() {
        return SampForm;
    }

    public void setSampForm(SampFormBean SampForm) {
        this.SampForm = SampForm;
    }

    public List<PreciptationSampForm.SampFormBean.SamplingFileBean> getUploadFiles() {
        return UploadFiles;
    }

    public void setUploadFiles(List<PreciptationSampForm.SampFormBean.SamplingFileBean> uploadFiles) {
        UploadFiles = uploadFiles;
    }

    public void setDelFiles(List<String> DelFiles) {
        this.DelFiles = DelFiles;
    }

    public boolean isCompelSubmit() {
        return isCompelSubmit;
    }

    public void setCompelSubmit(boolean compelSubmit) {
        isCompelSubmit = compelSubmit;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public static class SampFormBean {
        /**
         * ProjectId : 7f2f030e-c690-45d1-a0c7-2f8c35ec6df6
         * FormPath : /FormTemplate/FILL_JS_GAS_XD
         * FormName : 降水采样及交接记录
         * ProjectName : 测试待办
         * ProjectNo : CDHJ20180020
         * Montype : 3
         * ParentTagId : f5efa16d-3aae-4d5d-9241-8b80e121d249
         * FormType : f5efa16d-3aae-4d5d-9241-8b80e121d249
         * FormTypeName : 降水
         * PrivateData : {"ClientName":null,"ClientAdd":null,"SampHight":"采样高度","SampArea":"采样器接雨面积"}
         * SamplingDetails : [{"SamplingId":"00000000-0000-0000-0000-000000000000","ProjectId":"7f2f030e-c690-45d1-a0c7-2f8c35ec6df6","IsSenceAnalysis":true,"SampStandId":"00000000-0000-0000-0000-000000000000","MonitemId":"7253950a-9daa-9d4f-bd9a-a84789279c2a","MonitemName":"降水量","AddresssId":"fe39b598-29ec-47c9-9a7f-3be6e17b37e7","AddressName":"合江县环监站","PrivateData":"{\"SDataTime\":\"2018-11-30 14:33\",\"EDataTime\":\"2018-11-30 14:33\",\"RainVol\":\"222222\"}","Value":"11111","OrderIndex":"1","FrequecyNo":"1","Value1":"11111","Description":"备注"}]
         * SendSampTime : 2018-11-30 14:33
         * SamplingNo :
         * SamplingTimeBegin : 2018-11-30
         * TagName : 降水
         * TagId : 5e9a4bdc-a648-4f8c-9806-71e12449422c
         * AddressId : fe39b598-29ec-47c9-9a7f-3be6e17b37e7
         * AddressName : 合江县环监站
         * AddressNo : 点位编号
         * MonitemName :
         * MethodName : 酸沉降监测技术规范(HJ/T165-2004)
         * MethodId : 1ec725ef-c8cb-4a25-9370-2b16bd1a94c4
         * DeviceId : a4619ba6-6259-4a69-a4ff-1718a9c0717f,6801ed9f-736e-4ba7-895b-714969d75962,4e5e1ee2-9d17-4360-a829-001017df8bb3
         * DeviceName : 便携式低温冷藏装置(W35DC)07000687(校准),智能降水监测仪(崂应5021)V02015740(校准),空调（1.5P）(KFR-35GW/K(35556)D1-N2)939920024221(校准)
         * Transfer : 运输方式
         * ReciveTime :
         * file :
         * layTableCheckbox : on
         * SamplingUserId : 6e2c449a-9ce1-412b-bac8-ff7c369814e0,a1f2a31b-3543-4166-939d-f0040b50102c
         * SamplingUserName : Admin,胡丽梅
         * SamplingTimeEnd : 2018-11-30
         * Comment : <p>魏奇奇翁无群</p>
         * FormFlows : []
         * SamplingFormStands : [{"Id":"904b99e9-b5e4-c98c-e0fe-83b7f7ced00f","SamplingId":"00000000-0000-0000-0000-000000000000","MonitemIds":"7253950a-9daa-9d4f-bd9a-a84789279c2a","MonitemName":"降水量"}]
         */

        private String ProjectId;
        private String FormPath;
        private String FormName;
        private String ProjectName;
        private String ProjectNo;
        private int Montype;
        private String ParentTagId;
        private String FormType;
        private String FormTypeName;
        private String PrivateData;
        private String SendSampTime;
        private String SamplingNo;
        private String SamplingTimeBegin;
        private String TagName;
        private String TagId;
        private String AddressId;
        private String AddressName;
        private String AddressNo;
        private String MonitemId;
        private String MonitemName;
        private String MethodName;
        private String MethodId;
        private String DeviceId;
        private String DeviceName;
        private String Transfer;
        private String ReciveTime;
        private String file;
        private String layTableCheckbox;
        private String SamplingUserId;
        private String SamplingUserName;
        private String SamplingTimeEnd;
        private String Comment;
        private String FormFlows;
        private String SubmitId;
        private String SubmitName;
        private String SubmitDate;
        private String MonitorPerson;
        private String MonitorTime;
        private int Status;
        private String StatusName;
        private int TransStatus;
        private String TransStatusName;
        private String CurUserId;
        private String CurUserName;
        private String AddTime;
        private String UpdateTime;
        private int Version;
        private List<SamplingDetailsBean> SamplingDetails;
        private List<SamplingFormStandsBean> SamplingFormStands;
        private List<SamplingDetailsBean> SamplingDetailYQFs;
        //气象信息
        private String Weather;
        private String Temprature;
        private String Pressure;

        public String getProjectId() {
            return ProjectId;
        }

        public void setProjectId(String ProjectId) {
            this.ProjectId = ProjectId;
        }

        public String getFormPath() {
            return FormPath;
        }

        public void setFormPath(String FormPath) {
            this.FormPath = FormPath;
        }

        public String getFormName() {
            return FormName;
        }

        public void setFormName(String FormName) {
            this.FormName = FormName;
        }

        public String getProjectName() {
            return ProjectName;
        }

        public void setProjectName(String ProjectName) {
            this.ProjectName = ProjectName;
        }

        public String getProjectNo() {
            return ProjectNo;
        }

        public void setProjectNo(String ProjectNo) {
            this.ProjectNo = ProjectNo;
        }

        public int getMontype() {
            return Montype;
        }

        public void setMontype(int Montype) {
            this.Montype = Montype;
        }

        public String getParentTagId() {
            return ParentTagId;
        }

        public void setParentTagId(String ParentTagId) {
            this.ParentTagId = ParentTagId;
        }

        public String getFormType() {
            return FormType;
        }

        public void setFormType(String FormType) {
            this.FormType = FormType;
        }

        public String getFormTypeName() {
            return FormTypeName;
        }

        public void setFormTypeName(String FormTypeName) {
            this.FormTypeName = FormTypeName;
        }

        public String getPrivateData() {
            return PrivateData;
        }

        public void setPrivateData(String PrivateData) {
            this.PrivateData = PrivateData;
        }

        public String getSendSampTime() {
            return SendSampTime;
        }

        public void setSendSampTime(String SendSampTime) {
            this.SendSampTime = SendSampTime;
        }

        public String getSamplingNo() {
            return SamplingNo;
        }

        public void setSamplingNo(String SamplingNo) {
            this.SamplingNo = SamplingNo;
        }

        public String getSamplingTimeBegin() {
            return SamplingTimeBegin;
        }

        public void setSamplingTimeBegin(String SamplingTimeBegin) {
            this.SamplingTimeBegin = SamplingTimeBegin;
        }

        public String getTagName() {
            return TagName;
        }

        public void setTagName(String TagName) {
            this.TagName = TagName;
        }

        public String getTagId() {
            return TagId;
        }

        public void setTagId(String TagId) {
            this.TagId = TagId;
        }

        public String getAddressId() {
            return AddressId;
        }

        public void setAddressId(String AddressId) {
            this.AddressId = AddressId;
        }

        public String getAddressName() {
            return AddressName;
        }

        public void setAddressName(String AddressName) {
            this.AddressName = AddressName;
        }

        public String getAddressNo() {
            return AddressNo;
        }

        public void setAddressNo(String AddressNo) {
            this.AddressNo = AddressNo;
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

        public void setMonitemName(String MonitemName) {
            this.MonitemName = MonitemName;
        }

        public String getMethodName() {
            return MethodName;
        }

        public void setMethodName(String MethodName) {
            this.MethodName = MethodName;
        }

        public String getMethodId() {
            return MethodId;
        }

        public void setMethodId(String MethodId) {
            this.MethodId = MethodId;
        }

        public String getDeviceId() {
            return DeviceId;
        }

        public void setDeviceId(String DeviceId) {
            this.DeviceId = DeviceId;
        }

        public String getDeviceName() {
            return DeviceName;
        }

        public void setDeviceName(String DeviceName) {
            this.DeviceName = DeviceName;
        }

        public String getTransfer() {
            return Transfer;
        }

        public void setTransfer(String Transfer) {
            this.Transfer = Transfer;
        }

        public String getReciveTime() {
            return ReciveTime;
        }

        public void setReciveTime(String ReciveTime) {
            this.ReciveTime = ReciveTime;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getLayTableCheckbox() {
            return layTableCheckbox;
        }

        public void setLayTableCheckbox(String layTableCheckbox) {
            this.layTableCheckbox = layTableCheckbox;
        }

        public String getSamplingUserId() {
            return SamplingUserId;
        }

        public void setSamplingUserId(String SamplingUserId) {
            this.SamplingUserId = SamplingUserId;
        }

        public String getSamplingUserName() {
            return SamplingUserName;
        }

        public void setSamplingUserName(String SamplingUserName) {
            this.SamplingUserName = SamplingUserName;
        }

        public String getSamplingTimeEnd() {
            return SamplingTimeEnd;
        }

        public void setSamplingTimeEnd(String SamplingTimeEnd) {
            this.SamplingTimeEnd = SamplingTimeEnd;
        }

        public String getComment() {
            return Comment;
        }

        public void setComment(String Comment) {
            this.Comment = Comment;
        }

        public String getFormFlows() {
            return FormFlows;
        }

        public void setFormFlows(String FormFlows) {
            this.FormFlows = FormFlows;
        }

        public List<SamplingDetailsBean> getSamplingDetails() {
            return SamplingDetails;
        }

        public void setSamplingDetails(List<SamplingDetailsBean> SamplingDetails) {
            this.SamplingDetails = SamplingDetails;
        }

        public List<SamplingFormStandsBean> getSamplingFormStands() {
            return SamplingFormStands;
        }

        public void setSamplingFormStands(List<SamplingFormStandsBean> SamplingFormStands) {
            this.SamplingFormStands = SamplingFormStands;
        }

        public List<SamplingDetailsBean> getSamplingDetailYQFs() {
            return SamplingDetailYQFs;
        }

        public void setSamplingDetailYQFs(List<SamplingDetailsBean> samplingDetailYQFs) {
            SamplingDetailYQFs = samplingDetailYQFs;
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

        public String getWeather() {
            return Weather;
        }

        public void setWeather(String weather) {
            Weather = weather;
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

        public static class SamplingDetailsBean {
            public String getSampingCode() {
                return SampingCode;
            }

            public void setSampingCode(String sampingCode) {
                SampingCode = sampingCode;
            }

            /**
             * SamplingId : 00000000-0000-0000-0000-000000000000
             * ProjectId : 7f2f030e-c690-45d1-a0c7-2f8c35ec6df6
             * IsSenceAnalysis : true
             * SampStandId : 00000000-0000-0000-0000-000000000000
             * MonitemId : 7253950a-9daa-9d4f-bd9a-a84789279c2a
             * MonitemName : 降水量
             * AddresssId : fe39b598-29ec-47c9-9a7f-3be6e17b37e7
             * AddressName : 合江县环监站
             * PrivateData : {"SDataTime":"2018-11-30 14:33","EDataTime":"2018-11-30 14:33","RainVol":"222222"}
             * Value : 11111
             * OrderIndex : 1
             * FrequecyNo : 1
             * Value1 : 11111
             * Description : 备注
             */

            private String SampingCode;
            private String SamplingId;
            private String ProjectId;
            private boolean IsSenceAnalysis;
            private String SampStandId;
            private String MonitemId;
            private String MonitemName;
            private String AddresssId;
            private String AddressName;
            private String PrivateData;
            private String Value;
            private String OrderIndex;
            private String FrequecyNo;
            private String Value1;
            private String Description;
            //new
            private String SamplingTime;
            private String SamplingType;
            private String SamplingCount;
            private String Preservative;
            private boolean IsCompare;
            private String SampleCollection;
            private String SampleAcceptance;
            private String SamplingOnTime;

            public String getSamplingId() {
                return SamplingId;
            }

            public void setSamplingId(String SamplingId) {
                this.SamplingId = SamplingId;
            }

            public String getProjectId() {
                return ProjectId;
            }

            public void setProjectId(String ProjectId) {
                this.ProjectId = ProjectId;
            }

            public boolean isIsSenceAnalysis() {
                return IsSenceAnalysis;
            }

            public void setIsSenceAnalysis(boolean IsSenceAnalysis) {
                this.IsSenceAnalysis = IsSenceAnalysis;
            }

            public String getSampStandId() {
                return SampStandId;
            }

            public void setSampStandId(String SampStandId) {
                this.SampStandId = SampStandId;
            }

            public String getMonitemId() {
                return MonitemId;
            }

            public void setMonitemId(String MonitemId) {
                this.MonitemId = MonitemId;
            }

            public String getMonitemName() {
                return MonitemName;
            }

            public void setMonitemName(String MonitemName) {
                this.MonitemName = MonitemName;
            }

            public String getAddresssId() {
                return AddresssId;
            }

            public void setAddresssId(String AddresssId) {
                this.AddresssId = AddresssId;
            }

            public String getAddressName() {
                return AddressName;
            }

            public void setAddressName(String AddressName) {
                this.AddressName = AddressName;
            }

            public String getPrivateData() {
                return PrivateData;
            }

            public void setPrivateData(String PrivateData) {
                this.PrivateData = PrivateData;
            }

            public String getValue() {
                return Value;
            }

            public void setValue(String Value) {
                this.Value = Value;
            }

            public String getOrderIndex() {
                return OrderIndex;
            }

            public void setOrderIndex(String OrderIndex) {
                this.OrderIndex = OrderIndex;
            }

            public String getFrequecyNo() {
                return FrequecyNo;
            }

            public void setFrequecyNo(String FrequecyNo) {
                this.FrequecyNo = FrequecyNo;
            }

            public String getValue1() {
                return Value1;
            }

            public void setValue1(String Value1) {
                this.Value1 = Value1;
            }

            public String getDescription() {
                return Description;
            }

            public void setDescription(String Description) {
                this.Description = Description;
            }

            public boolean isSenceAnalysis() {
                return IsSenceAnalysis;
            }

            public void setSenceAnalysis(boolean senceAnalysis) {
                IsSenceAnalysis = senceAnalysis;
            }

            public String getSamplingTime() {
                return SamplingTime;
            }

            public void setSamplingTime(String samplingTime) {
                SamplingTime = samplingTime;
            }

            public String getSamplingType() {
                return SamplingType;
            }

            public void setSamplingType(String samplingType) {
                SamplingType = samplingType;
            }

            public String getSamplingCount() {
                return SamplingCount;
            }

            public void setSamplingCount(String samplingCount) {
                SamplingCount = samplingCount;
            }

            public String getPreservative() {
                return Preservative;
            }

            public void setPreservative(String preservative) {
                Preservative = preservative;
            }

            public boolean getIsCompare() {
                return IsCompare;
            }

            public void setIsCompare(boolean isCompare) {
                IsCompare = isCompare;
            }

            public String getSampleCollection() {
                return SampleCollection;
            }

            public void setSampleCollection(String sampleCollection) {
                SampleCollection = sampleCollection;
            }

            public String getSampleAcceptance() {
                return SampleAcceptance;
            }

            public void setSampleAcceptance(String sampleAcceptance) {
                SampleAcceptance = sampleAcceptance;
            }

            public String getSamplingOnTime() {
                return SamplingOnTime;
            }

            public void setSamplingOnTime(String samplingOnTime) {
                SamplingOnTime = samplingOnTime;
            }
        }

        public static class SamplingFormStandsBean {
            /**
             * Id : 904b99e9-b5e4-c98c-e0fe-83b7f7ced00f
             * SamplingId : 00000000-0000-0000-0000-000000000000
             * MonitemIds : 7253950a-9daa-9d4f-bd9a-a84789279c2a
             * MonitemName : 降水量
             */

            private String Id;
            private String SamplingId;
            private String MonitemIds;
            private String MonitemName;
            private int StandNo;
            private int Count;
            private String SamplingAmount;
            private String AnalysisSite;
            private String SaveMehtod;
            private String Preservative;
            private String Container;
            private String SaveTimes;
            private String Index;
            private String UpdateTime;
            private List<String> MonItems;


            public String getId() {
                return Id;
            }

            public void setId(String Id) {
                this.Id = Id;
            }

            public String getSamplingId() {
                return SamplingId;
            }

            public void setSamplingId(String SamplingId) {
                this.SamplingId = SamplingId;
            }

            public String getMonitemIds() {
                return MonitemIds;
            }

            public void setMonitemIds(String MonitemIds) {
                this.MonitemIds = MonitemIds;
            }

            public String getMonitemName() {
                return MonitemName;
            }

            public void setMonitemName(String MonitemName) {
                this.MonitemName = MonitemName;
            }

            public int getStandNo() {
                return StandNo;
            }

            public void setStandNo(int standNo) {
                StandNo = standNo;
            }

            public int getCount() {
                return Count;
            }

            public void setCount(int count) {
                Count = count;
            }

            public String getSamplingAmount() {
                return SamplingAmount;
            }

            public void setSamplingAmount(String samplingAmount) {
                SamplingAmount = samplingAmount;
            }

            public String getAnalysisSite() {
                return AnalysisSite;
            }

            public void setAnalysisSite(String analysisSite) {
                AnalysisSite = analysisSite;
            }

            public String getSaveMehtod() {
                return SaveMehtod;
            }

            public void setSaveMehtod(String saveMehtod) {
                SaveMehtod = saveMehtod;
            }

            public String getPreservative() {
                return Preservative;
            }

            public void setPreservative(String preservative) {
                Preservative = preservative;
            }

            public String getContainer() {
                return Container;
            }

            public void setContainer(String container) {
                Container = container;
            }

            public String getSaveTimes() {
                return SaveTimes;
            }

            public void setSaveTimes(String saveTimes) {
                SaveTimes = saveTimes;
            }

            public String getIndex() {
                return Index;
            }

            public void setIndex(String index) {
                Index = index;
            }

            public String getUpdateTime() {
                return UpdateTime;
            }

            public void setUpdateTime(String updateTime) {
                UpdateTime = updateTime;
            }

            public List<String> getMonItems() {
                return MonItems;
            }

            public void setMonItems(List<String> monItems) {
                MonItems = monItems;
            }
        }

        public static class SamplingFileBean {
            private String Id;
            private String FileName;
            private String UpdateTime;

            public String getId() {
                return Id;
            }

            public void setId(String id) {
                Id = id;
            }

            public String getFileName() {
                return FileName;
            }

            public void setFileName(String fileName) {
                FileName = fileName;
            }

            public String getUpdateTime() {
                return UpdateTime;
            }

            public void setUpdateTime(String updateTime) {
                UpdateTime = updateTime;
            }
        }
    }
}
