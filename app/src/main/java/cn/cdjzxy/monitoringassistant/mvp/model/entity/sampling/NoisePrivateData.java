package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import java.util.ArrayList;
import java.util.List;

/**
 * 噪声——扩展信息
 */
public class NoisePrivateData {

    /**
     * CalibrationBefore : 40
     * CalibrationAfter : 50
     * CalibrationMethodName : 方法
     * SourceWay : 检定
     * SourceDate : 2019-03-15
     * WindDevName : 电导率仪DDS-307A(610612030024)
     * SampFormType : JZSG
     * CalibrationDeviceName : 自动烟尘（气）测试仪3012H(A08237799X)(检定 2019-03-21)
     * CalibrationDeviceId : 5768fd84-f3ae-499e-8a7d-467f21156663
     * ClientName : 不知道
     * ClientAddr : 地址
     * Category : 打算
     * ProductionCondition : 海口可以
     * ProductionEquipment : 哈哈
     * MianNioseSource : [{"IsChecked":true,"oIndex":1,"Name":" 车","Model":"骑车","Num":"1000","RunTime":"8-9","guid":"8a082b2e-f9a8-9f7b-98fb-c3aadadaae1b"}]
     * MianNioseAddr : [{"IsChecked":false,"oIndex":1,"AddresssId":"ff4dc1c7-4f36-4245-a39f-45014c9d64fa","AddressName":"香城湿地人工湖","AddrCode":"123455655","FuncType":"大","Remark":"大大大","guid":"2277f1af-bfdf-60df-1550-24c309ba7e61"}]
     */

    private String CalibrationBefore;
    private String CalibrationAfter;
    private String CalibrationMethodName;
    private String SourceWay;
    private String SourceDate;
    private String WindDevName;
    private String SampFormType;
    private String CalibrationDeviceName;
    private String CalibrationDeviceId;
    private String ClientName;
    private String ClientAddr;
    private String Category;
    private String ProductionCondition;
    private String ProductionEquipment;
    private List<MianNioseSourceBean> MianNioseSource;
    private List<MianNioseAddrBean> MianNioseAddr;
    private String ImageSYT;

    public String getCalibrationBefore() {
        return CalibrationBefore;
    }

    public void setCalibrationBefore(String CalibrationBefore) {
        this.CalibrationBefore = CalibrationBefore;
    }

    public String getCalibrationAfter() {
        return CalibrationAfter;
    }

    public void setCalibrationAfter(String CalibrationAfter) {
        this.CalibrationAfter = CalibrationAfter;
    }

    public String getCalibrationMethodName() {
        return CalibrationMethodName;
    }

    public void setCalibrationMethodName(String CalibrationMethodName) {
        this.CalibrationMethodName = CalibrationMethodName;
    }

    public String getSourceWay() {
        return SourceWay;
    }

    public void setSourceWay(String SourceWay) {
        this.SourceWay = SourceWay;
    }

    public String getSourceDate() {
        return SourceDate;
    }

    public void setSourceDate(String SourceDate) {
        this.SourceDate = SourceDate;
    }

    public String getWindDevName() {
        return WindDevName;
    }

    public void setWindDevName(String WindDevName) {
        this.WindDevName = WindDevName;
    }

    public String getSampFormType() {
        return SampFormType;
    }

    public void setSampFormType(String SampFormType) {
        this.SampFormType = SampFormType;
    }

    public String getCalibrationDeviceName() {
        return CalibrationDeviceName;
    }

    public void setCalibrationDeviceName(String CalibrationDeviceName) {
        this.CalibrationDeviceName = CalibrationDeviceName;
    }

    public String getCalibrationDeviceId() {
        return CalibrationDeviceId;
    }

    public void setCalibrationDeviceId(String CalibrationDeviceId) {
        this.CalibrationDeviceId = CalibrationDeviceId;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String ClientName) {
        this.ClientName = ClientName;
    }

    public String getClientAddr() {
        return ClientAddr;
    }

    public void setClientAddr(String ClientAddr) {
        this.ClientAddr = ClientAddr;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getProductionCondition() {
        return ProductionCondition;
    }

    public void setProductionCondition(String ProductionCondition) {
        this.ProductionCondition = ProductionCondition;
    }

    public String getProductionEquipment() {
        return ProductionEquipment;
    }

    public void setProductionEquipment(String ProductionEquipment) {
        this.ProductionEquipment = ProductionEquipment;
    }

    public List<MianNioseSourceBean> getMianNioseSource() {
        if (MianNioseSource == null) MianNioseSource = new ArrayList<>();
        return MianNioseSource;
    }

    public void setMianNioseSource(List<MianNioseSourceBean> MianNioseSource) {
        this.MianNioseSource = MianNioseSource;
    }

    public List<MianNioseAddrBean> getMianNioseAddr() {
        if (MianNioseAddr == null) MianNioseAddr = new ArrayList<>();
        return MianNioseAddr;
    }

    public void setMianNioseAddr(List<MianNioseAddrBean> MianNioseAddr) {
        this.MianNioseAddr = MianNioseAddr;
    }

    public String getImageSYT() {
        return ImageSYT;
    }

    public void setImageSYT(String imageSYT) {
        ImageSYT = imageSYT;
    }

    public static class MianNioseSourceBean {
        /**
         * IsChecked : true
         * oIndex : 1
         * Name :  车
         * Model : 骑车
         * Num : 1000
         * RunTime : 8-9
         * guid : 8a082b2e-f9a8-9f7b-98fb-c3aadadaae1b
         */

        private boolean IsChecked;
        private int oIndex;
        private String Name;
        private String Model;
        private String Num;
        private String RunTime;
        private String guid;

        public boolean isIsChecked() {
            return IsChecked;
        }

        public void setIsChecked(boolean IsChecked) {
            this.IsChecked = IsChecked;
        }

        public int getOIndex() {
            return oIndex;
        }

        public void setOIndex(int oIndex) {
            this.oIndex = oIndex;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getModel() {
            return Model;
        }

        public void setModel(String Model) {
            this.Model = Model;
        }

        public String getNum() {
            return Num;
        }

        public void setNum(String Num) {
            this.Num = Num;
        }

        public String getRunTime() {
            return RunTime;
        }

        public void setRunTime(String RunTime) {
            this.RunTime = RunTime;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }


    }

    public static class MianNioseAddrBean {
        /**
         * IsChecked : false
         * oIndex : 1
         * AddresssId : ff4dc1c7-4f36-4245-a39f-45014c9d64fa
         * AddressName : 香城湿地人工湖
         * AddrCode : 123455655
         * FuncType : 大
         * Remark : 大大大
         * guid : 2277f1af-bfdf-60df-1550-24c309ba7e61
         */

        private boolean IsChecked;
        private int oIndex;
        private String AddresssId;
        private String AddressName;
        private String AddrCode;
        private String FuncType;
        private String Remark;
        private String guid;

        public boolean isIsChecked() {
            return IsChecked;
        }

        public void setIsChecked(boolean IsChecked) {
            this.IsChecked = IsChecked;
        }

        public int getOIndex() {
            return oIndex;
        }

        public void setOIndex(int oIndex) {
            this.oIndex = oIndex;
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

        public String getAddrCode() {
            return AddrCode;
        }

        public void setAddrCode(String AddrCode) {
            this.AddrCode = AddrCode;
        }

        public String getFuncType() {
            return FuncType;
        }

        public void setFuncType(String FuncType) {
            this.FuncType = FuncType;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getGuid() {
            return guid;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }
    }


}

