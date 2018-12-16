package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import java.util.List;

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

    private String                             SamplingHeight;
    private String                             PollutionType;
    private String                             RainType;
    private String                             SampProperty;
    private String                             DeviceId;
    private String                             DeviceName;
    private String                             MethodId;
    private String                             MethodName;
    private String                             Weather;
    private String                             WindSpeed;
    private String                             Temprature;
    private String                             Pressure;
    private String                             CalibrationFactor;
    private String                             Transfer;
    private String                             SendSampTime;
    private String                             ReciveTime;
    private String                             PrivateData;
    private String                             CurUserId;
    private String                             CurUserName;
    private String                             Comment;
    private String                             MonitemName;
    private String                             FormFlows;
    private String                             Id;
    private String                             ProjectId;
    private String                             ProjectName;
    private String                             montype;
    private String                             SamplingNo;
    private String                             FormName;
    private String                             ParentTagId;
    private String                             TagId;
    private String                             TagName;
    private String                             AddressId;
    private String                             AddressName;
    private String                             SamplingUserId;
    private String                             SamplingUserName;
    private String                             SubmitId;
    private String                             SubmitName;
    private String                             SamplingTimeBegin;
    private String                             SamplingTimeEnd;
    private int                                Status;
    private String                             StatusName;
    private String                             UpdateTime;
    private List<SamplingFormStandResultsBean> SamplingFormStandResults;
    private List<SamplingDetailResultsBean>    SamplingDetailResults;
    private List<SamplingUserResultsBean>      SamplingUserResults;


    public static class SamplingFormStandResultsBean {
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

        private String Id;
        private String SamplingId;
        private int    StandNo;
        private String MonitemIds;
        private String MonitemName;
        private String SamplingAmount;
        private String AnalysisSite;
        private String SaveMehtod;
        private String Preservative;
        private int    Count;
        private String Container;
        private String SaveTimes;
        private int    Index;

    }

    public static class SamplingDetailResultsBean {
        /**
         * Id : 36d20d13-eb75-4a38-9e50-30b672a41ece
         * SamplingId : 29380d4a-4f27-421f-a750-3d015f12c42b
         * ProjectId : 601435b6-8d4d-45b1-ae79-0124dd0276cb
         * OrderIndex : 1
         * SampingCode : CD20181105-W111310-01
         * FrequecyNo : 1
         * SamplingTime :
         * SamplingType : 0
         * SampStandId : c7d18181-a337-90c7-5513-23e3ccae0de7
         * MonitemId : e113d898-1822-e549-8168-04fb822c6c9d
         * MonitemName : 铁
         * AddresssId : a05efff0-2526-40bb-98e0-ac6bf1d4ff42
         * AddressName : 林芝
         * SamplingCount : 2
         * Preservative : 否
         * SampleCollection :
         * SampleAcceptance :
         * IsSenceAnalysis : false
         * IsCompare : false
         * AnasysTime : null
         * Value1 : null
         * ValueUnit1 : null
         * valueUnit1Name : null
         * Value2 : null
         * valueUnit2 : null
         * valueUnit2Name : null
         * Value3 : null
         * ValueUnit3 : null
         * valueUnit3Name : null
         * Value : null
         * ValueUnit : null
         * ValueUnitNname : null
         * Value4 : null
         * Value5 : null
         * MethodName : null
         * MethodId : null
         * DeviceIdName : null
         * DeviceId : null
         * Description :
         * PrivateData : null
         */

        private String  Id;
        private String  SamplingId;
        private String  ProjectId;
        private int     OrderIndex;
        private String  SampingCode;
        private int     FrequecyNo;
        private String  SamplingTime;
        private int     SamplingType;
        private String  SampStandId;
        private String  MonitemId;
        private String  MonitemName;
        private String  AddresssId;
        private String  AddressName;
        private int     SamplingCount;
        private String  Preservative;
        private String  SampleCollection;
        private String  SampleAcceptance;
        private boolean IsSenceAnalysis;
        private boolean IsCompare;
        private String  AnasysTime;
        private String  Value1;
        private String  ValueUnit1;
        private String  valueUnit1Name;
        private String  Value2;
        private String  valueUnit2;
        private String  valueUnit2Name;
        private String  Value3;
        private String  ValueUnit3;
        private String  valueUnit3Name;
        private String  Value;
        private String  ValueUnit;
        private String  ValueUnitNname;
        private String  Value4;
        private String  Value5;
        private String  MethodName;
        private String  MethodId;
        private String  DeviceIdName;
        private String  DeviceId;
        private String  Description;
        private String  PrivateData;

    }

    public static class SamplingUserResultsBean {
        /**
         * UserId : 61bb48f0-5d8f-49a1-989d-7d6d7033fd91
         * UserName : Admin
         */
        private String UserId;
        private String UserName;

    }
}
