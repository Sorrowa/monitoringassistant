package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

import java.util.List;

public class ProjectContent {


    /**
     * Id : 758e3c43-dfa7-43cf-ba94-2b15139c0002
     * AddressCount : 3
     * MonItemCount : 3
     * IsChecked : false
     * MonItemsName : 氟化物、丙烯醛、pH值
     * MonItems : [{"Id":"58dfc761-40f3-e14a-a0e8-aabee43c92f4","Name":"氟化物","MethodId":"4489e02e-77bc-47fe-a334-6840d381da30","MethodName":"大气降水中氟化物的测定 新氟试剂光度法(GB13580.10-1992)（无资质)","HaveCert":"无","TagId":"5e9a4bdc-a648-4f8c-9806-71e12449422c","TagName":"降水","IsOutsourcing":false,"IsOutsourcingtext":"否"},{"Id":"558c625a-e92c-7246-a3a4-1f78b8cd1125","Name":"丙烯醛","MethodId":"7d5a26b7-a9f8-4aa9-9d9a-dc7a2115d8b9","MethodName":"水质 乙醛、丙烯醛的测定 2.4-DNPH柱前衍生高效液相色谱法(MHJ/ZY-03(015)-2016（参考EPA 554 Method）)（有资质)","HaveCert":"有","TagId":"5e9a4bdc-a648-4f8c-9806-71e12449422c","TagName":"降水","IsOutsourcing":false,"IsOutsourcingtext":"否"},{"Id":"58a3e4d8-d235-8841-b61d-b3cdf2641114","Name":"pH值","MethodId":"3fe5b9bd-88ca-4433-831c-9bc2c47ddb49","MethodName":"大气降水 pH值的测定 电极法(GB13580.4-1992)（有资质)","HaveCert":"有","TagId":"5e9a4bdc-a648-4f8c-9806-71e12449422c","TagName":"降水","IsOutsourcing":false,"IsOutsourcingtext":"否"}]
     * TagId : 5e9a4bdc-a648-4f8c-9806-71e12449422c
     * TagName : 降水
     * Address : 22222、降水城区点位测试、降水点位23
     * AddressArr : [{"Id":"dac0ed09-8e98-4e5f-89e7-0cae6358aa39","Name":"22222","Type":0,"Istemp":false,"ESLimt":[],"Point":null,"Level":"1"},{"Id":"59abb48d-54b3-4180-adf7-2a49936989fb","Name":"降水城区点位测试","Type":0,"Istemp":false,"ESLimt":[],"Point":"","Level":"1"},{"Id":"e3a2e4ae-df82-4bd2-8e18-d6355a58bf96","Name":"降水点位23","Type":0,"Istemp":false,"ESLimt":[],"Point":"","Level":"3"}]
     * AddressIds : dac0ed09-8e98-4e5f-89e7-0cae6358aa39,59abb48d-54b3-4180-adf7-2a49936989fb,e3a2e4ae-df82-4bd2-8e18-d6355a58bf96
     * Days : 2
     * Period : 3
     * Comment : 123
     * PeriodShow : false
     * TagParentId : f5efa16d-3aae-4d5d-9241-8b80e121d249
     * TagParentName : 降水
     * ProjectDetials : [{"Id":"9d2235ac-f745-497e-800b-3a628402163a","ProjectId":"2c3a18d8-551c-47bb-8762-5dc708b28022","ProjectContentId":"758e3c43-dfa7-43cf-ba94-2b15139c0002","TagId":"5e9a4bdc-a648-4f8c-9806-71e12449422c","TagName":"降水","MonItemId":"58dfc761-40f3-e14a-a0e8-aabee43c92f4","MonItemName":"氟化物","MethodId":"4489e02e-77bc-47fe-a334-6840d381da30","MethodName":"大气降水中氟化物的测定 新氟试剂光度法(GB13580.10-1992)（无资质)","AddressId":"dac0ed09-8e98-4e5f-89e7-0cae6358aa39","Address":"22222","Flag":null,"Status":null,"Comment":null,"EvaluateValMin":null,"EvaluateUnitId":null,"EvaluateUnit":null,"EvaluateCondtion":0,"EvaluateCondtionId":null,"EvaluateValMax":1,"EvaluateName":"GB3838-2002"}]
     * guid : f4c7cb82-de65-222b-5bd4-02e3f589fbe8
     */

    private String                   Id;
    private int                      AddressCount;
    private int                      MonItemCount;
    private boolean                  IsChecked;
    private String                   MonItemsName;
    private String                   TagId;
    private String                   TagName;
    private String                   Address;
    private String                   AddressIds;
    private int                      Days;
    private int                      Period;
    private String                   Comment;
    private boolean                  PeriodShow;
    private String                   TagParentId;
    private String                   TagParentName;
    private String                   guid;
    private List<MonItemsBean>       MonItems;
    private List<AddressArrBean>     AddressArr;
    private List<ProjectDetialsBean> ProjectDetials;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public int getAddressCount() {
        return AddressCount;
    }

    public void setAddressCount(int AddressCount) {
        this.AddressCount = AddressCount;
    }

    public int getMonItemCount() {
        return MonItemCount;
    }

    public void setMonItemCount(int MonItemCount) {
        this.MonItemCount = MonItemCount;
    }

    public boolean isIsChecked() {
        return IsChecked;
    }

    public void setIsChecked(boolean IsChecked) {
        this.IsChecked = IsChecked;
    }

    public String getMonItemsName() {
        return MonItemsName;
    }

    public void setMonItemsName(String MonItemsName) {
        this.MonItemsName = MonItemsName;
    }

    public String getTagId() {
        return TagId;
    }

    public void setTagId(String TagId) {
        this.TagId = TagId;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String TagName) {
        this.TagName = TagName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getAddressIds() {
        return AddressIds;
    }

    public void setAddressIds(String AddressIds) {
        this.AddressIds = AddressIds;
    }

    public int getDays() {
        return Days;
    }

    public void setDays(int Days) {
        this.Days = Days;
    }

    public int getPeriod() {
        return Period;
    }

    public void setPeriod(int Period) {
        this.Period = Period;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public boolean isPeriodShow() {
        return PeriodShow;
    }

    public void setPeriodShow(boolean PeriodShow) {
        this.PeriodShow = PeriodShow;
    }

    public String getTagParentId() {
        return TagParentId;
    }

    public void setTagParentId(String TagParentId) {
        this.TagParentId = TagParentId;
    }

    public String getTagParentName() {
        return TagParentName;
    }

    public void setTagParentName(String TagParentName) {
        this.TagParentName = TagParentName;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public List<MonItemsBean> getMonItems() {
        return MonItems;
    }

    public void setMonItems(List<MonItemsBean> MonItems) {
        this.MonItems = MonItems;
    }

    public List<AddressArrBean> getAddressArr() {
        return AddressArr;
    }

    public void setAddressArr(List<AddressArrBean> AddressArr) {
        this.AddressArr = AddressArr;
    }

    public List<ProjectDetialsBean> getProjectDetials() {
        return ProjectDetials;
    }

    public void setProjectDetials(List<ProjectDetialsBean> ProjectDetials) {
        this.ProjectDetials = ProjectDetials;
    }

    public static class MonItemsBean {
        /**
         * Id : 58dfc761-40f3-e14a-a0e8-aabee43c92f4
         * Name : 氟化物
         * MethodId : 4489e02e-77bc-47fe-a334-6840d381da30
         * MethodName : 大气降水中氟化物的测定 新氟试剂光度法(GB13580.10-1992)（无资质)
         * HaveCert : 无
         * TagId : 5e9a4bdc-a648-4f8c-9806-71e12449422c
         * TagName : 降水
         * IsOutsourcing : false
         * IsOutsourcingtext : 否
         */

        private String  Id;
        private String  Name;
        private String  MethodId;
        private String  MethodName;
        private String  HaveCert;
        private String  TagId;
        private String  TagName;
        private boolean IsOutsourcing;
        private String  IsOutsourcingtext;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getMethodId() {
            return MethodId;
        }

        public void setMethodId(String MethodId) {
            this.MethodId = MethodId;
        }

        public String getMethodName() {
            return MethodName;
        }

        public void setMethodName(String MethodName) {
            this.MethodName = MethodName;
        }

        public String getHaveCert() {
            return HaveCert;
        }

        public void setHaveCert(String HaveCert) {
            this.HaveCert = HaveCert;
        }

        public String getTagId() {
            return TagId;
        }

        public void setTagId(String TagId) {
            this.TagId = TagId;
        }

        public String getTagName() {
            return TagName;
        }

        public void setTagName(String TagName) {
            this.TagName = TagName;
        }

        public boolean isIsOutsourcing() {
            return IsOutsourcing;
        }

        public void setIsOutsourcing(boolean IsOutsourcing) {
            this.IsOutsourcing = IsOutsourcing;
        }

        public String getIsOutsourcingtext() {
            return IsOutsourcingtext;
        }

        public void setIsOutsourcingtext(String IsOutsourcingtext) {
            this.IsOutsourcingtext = IsOutsourcingtext;
        }
    }

    public static class AddressArrBean {
        /**
         * Id : dac0ed09-8e98-4e5f-89e7-0cae6358aa39
         * Name : 22222
         * Type : 0
         * Istemp : false
         * ESLimt : []
         * Point : null
         * Level : 1
         */

        private String       Id;
        private String       Name;
        private int          Type;
        private boolean      Istemp;
        private String       Point;
        private String       Level;
        private List<String> ESLimt;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public boolean isIstemp() {
            return Istemp;
        }

        public void setIstemp(boolean Istemp) {
            this.Istemp = Istemp;
        }

        public String getPoint() {
            return Point;
        }

        public void setPoint(String Point) {
            this.Point = Point;
        }

        public String getLevel() {
            return Level;
        }

        public void setLevel(String Level) {
            this.Level = Level;
        }

        public List<String> getESLimt() {
            return ESLimt;
        }

        public void setESLimt(List<String> ESLimt) {
            this.ESLimt = ESLimt;
        }
    }

    public static class ProjectDetialsBean {
        /**
         * Id : 9d2235ac-f745-497e-800b-3a628402163a
         * ProjectId : 2c3a18d8-551c-47bb-8762-5dc708b28022
         * ProjectContentId : 758e3c43-dfa7-43cf-ba94-2b15139c0002
         * TagId : 5e9a4bdc-a648-4f8c-9806-71e12449422c
         * TagName : 降水
         * MonItemId : 58dfc761-40f3-e14a-a0e8-aabee43c92f4
         * MonItemName : 氟化物
         * MethodId : 4489e02e-77bc-47fe-a334-6840d381da30
         * MethodName : 大气降水中氟化物的测定 新氟试剂光度法(GB13580.10-1992)（无资质)
         * AddressId : dac0ed09-8e98-4e5f-89e7-0cae6358aa39
         * Address : 22222
         * Flag : null
         * Status : null
         * Comment : null
         * EvaluateValMin : null
         * EvaluateUnitId : null
         * EvaluateUnit : null
         * EvaluateCondtion : 0
         * EvaluateCondtionId : null
         * EvaluateValMax : 1
         * EvaluateName : GB3838-2002
         */

        private String Id;
        private String ProjectId;
        private String ProjectContentId;
        private String TagId;
        private String TagName;
        private String MonItemId;
        private String MonItemName;
        private String MethodId;
        private String MethodName;
        private String AddressId;
        private String Address;
        private Object Flag;
        private Object Status;
        private Object Comment;
        private Object EvaluateValMin;
        private Object EvaluateUnitId;
        private Object EvaluateUnit;
        private int    EvaluateCondtion;
        private Object EvaluateCondtionId;
        private int    EvaluateValMax;
        private String EvaluateName;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getProjectId() {
            return ProjectId;
        }

        public void setProjectId(String ProjectId) {
            this.ProjectId = ProjectId;
        }

        public String getProjectContentId() {
            return ProjectContentId;
        }

        public void setProjectContentId(String ProjectContentId) {
            this.ProjectContentId = ProjectContentId;
        }

        public String getTagId() {
            return TagId;
        }

        public void setTagId(String TagId) {
            this.TagId = TagId;
        }

        public String getTagName() {
            return TagName;
        }

        public void setTagName(String TagName) {
            this.TagName = TagName;
        }

        public String getMonItemId() {
            return MonItemId;
        }

        public void setMonItemId(String MonItemId) {
            this.MonItemId = MonItemId;
        }

        public String getMonItemName() {
            return MonItemName;
        }

        public void setMonItemName(String MonItemName) {
            this.MonItemName = MonItemName;
        }

        public String getMethodId() {
            return MethodId;
        }

        public void setMethodId(String MethodId) {
            this.MethodId = MethodId;
        }

        public String getMethodName() {
            return MethodName;
        }

        public void setMethodName(String MethodName) {
            this.MethodName = MethodName;
        }

        public String getAddressId() {
            return AddressId;
        }

        public void setAddressId(String AddressId) {
            this.AddressId = AddressId;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public Object getFlag() {
            return Flag;
        }

        public void setFlag(Object Flag) {
            this.Flag = Flag;
        }

        public Object getStatus() {
            return Status;
        }

        public void setStatus(Object Status) {
            this.Status = Status;
        }

        public Object getComment() {
            return Comment;
        }

        public void setComment(Object Comment) {
            this.Comment = Comment;
        }

        public Object getEvaluateValMin() {
            return EvaluateValMin;
        }

        public void setEvaluateValMin(Object EvaluateValMin) {
            this.EvaluateValMin = EvaluateValMin;
        }

        public Object getEvaluateUnitId() {
            return EvaluateUnitId;
        }

        public void setEvaluateUnitId(Object EvaluateUnitId) {
            this.EvaluateUnitId = EvaluateUnitId;
        }

        public Object getEvaluateUnit() {
            return EvaluateUnit;
        }

        public void setEvaluateUnit(Object EvaluateUnit) {
            this.EvaluateUnit = EvaluateUnit;
        }

        public int getEvaluateCondtion() {
            return EvaluateCondtion;
        }

        public void setEvaluateCondtion(int EvaluateCondtion) {
            this.EvaluateCondtion = EvaluateCondtion;
        }

        public Object getEvaluateCondtionId() {
            return EvaluateCondtionId;
        }

        public void setEvaluateCondtionId(Object EvaluateCondtionId) {
            this.EvaluateCondtionId = EvaluateCondtionId;
        }

        public int getEvaluateValMax() {
            return EvaluateValMax;
        }

        public void setEvaluateValMax(int EvaluateValMax) {
            this.EvaluateValMax = EvaluateValMax;
        }

        public String getEvaluateName() {
            return EvaluateName;
        }

        public void setEvaluateName(String EvaluateName) {
            this.EvaluateName = EvaluateName;
        }
    }
}
