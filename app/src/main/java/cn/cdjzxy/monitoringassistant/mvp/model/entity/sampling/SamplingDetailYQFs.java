package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONException;
import org.json.JSONObject;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SamplingDetailYQFs {


    /**
     * Id : f3c0fcd7-cc03-4e73-a178-ff0e4f3425c0
     * SamplingId : 507c8d97-3936-4d38-9cb7-6b8ebdb4d762
     * SampingCode : 1905311203
     * FrequecyNo : 3
     * OrderIndex : 1
     * MonitemId : 237bf063-5ee4-ed4c-afec-02e23f5f459b
     * SamplingType : 0
     * ProjectId : f3ff5fe3-25cb-4909-81a5-ee4873a15f2c
     * UpdateTime : 2019-06-03 10:44:37
     * Value :
     * AddressID : cbdbb26b-a725-4117-a303-730fe9c93838
     * AddressName : 长江舒巷子-右
     * SamplingOnTime : 2019-05-31
     * PrivateData : {"SamplingOnTime":"","CaleValue":"","ValueUnit":"","ValueUnitName":"","RPDValue":"","HasPX":false}
     */
    @Id
    private String Id;
    private String SamplingId;
    private String SampingCode;
    private int FrequecyNo;
    private int OrderIndex;
    private String MonitemId;
    private int SamplingType;
    private String ProjectId;
    private String UpdateTime;
    private String Value;
    private String AddressID;
    private String AddressName;
    private String SamplingOnTime;
    private String PrivateData;
    @Transient
    private boolean isCanSelect;
    @Transient
    private boolean isSelected;
    @Transient
    private PrivateJsonData jsonData;

    @Keep()
    public SamplingDetailYQFs(String Id, String SamplingId, String SampingCode, int FrequecyNo, int OrderIndex,
                              String MonitemId, int SamplingType, String ProjectId, String UpdateTime, String Value, String AddressID,
                              String AddressName, String SamplingOnTime, String PrivateData) {
        this.Id = Id;
        this.SamplingId = SamplingId;
        this.SampingCode = SampingCode;
        this.FrequecyNo = FrequecyNo;
        this.OrderIndex = OrderIndex;
        this.MonitemId = MonitemId;
        this.SamplingType = SamplingType;
        this.ProjectId = ProjectId;
        this.UpdateTime = UpdateTime;
        this.Value = Value;
        this.AddressID = AddressID;
        this.AddressName = AddressName;
        this.SamplingOnTime = SamplingOnTime;
        this.PrivateData = PrivateData;
    }

    @Keep()
    public SamplingDetailYQFs() {
    }

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

    public String getSampingCode() {
        return SampingCode;
    }

    public void setSampingCode(String SampingCode) {
        this.SampingCode = SampingCode;
    }

    public int getFrequecyNo() {
        return FrequecyNo;
    }

    public void setFrequecyNo(int FrequecyNo) {
        this.FrequecyNo = FrequecyNo;
    }

    public int getOrderIndex() {
        return OrderIndex;
    }

    public void setOrderIndex(int OrderIndex) {
        this.OrderIndex = OrderIndex;
    }

    public String getMonitemId() {
        return MonitemId;
    }

    public void setMonitemId(String MonitemId) {
        this.MonitemId = MonitemId;
    }

    public int getSamplingType() {
        return SamplingType;
    }

    public void setSamplingType(int SamplingType) {
        this.SamplingType = SamplingType;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String ProjectId) {
        this.ProjectId = ProjectId;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String getAddressID() {
        return AddressID;
    }

    public void setAddressID(String AddressID) {
        this.AddressID = AddressID;
    }

    public String getAddressName() {
        return AddressName;
    }

    public void setAddressName(String AddressName) {
        this.AddressName = AddressName;
    }

    public String getSamplingOnTime() {
        return SamplingOnTime;
    }

    public void setSamplingOnTime(String SamplingOnTime) {
        this.SamplingOnTime = SamplingOnTime;
    }

    public String getPrivateData() {
        return PrivateData;
    }

    public void setPrivateData(String PrivateData) {
        this.PrivateData = PrivateData;
    }

    public boolean isCanSelect() {
        return isCanSelect;
    }

    public void setCanSelect(boolean canSelect) {
        isCanSelect = canSelect;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public PrivateJsonData getJsonPrivateData() {
        jsonData = new Gson().fromJson(PrivateData, PrivateJsonData.class);
        return jsonData;
    }

    public void setJsonData(PrivateJsonData data) {
        this.jsonData = data;
        this.PrivateData = new Gson().toJson(jsonData);
    }



    public static class PrivateJsonData {

        /**
         * SamplingOnTime : 00:00
         * CaleValue :
         * ValueUnit :
         * ValueUnitName :
         * RPDValue :
         * HasPX : false
         */

        private String SamplingOnTime;
        private String CaleValue;
        private String ValueUnit;
        private String ValueUnitName;
        private String RPDValue;
        private boolean HasPX;

        public String getSamplingOnTime() {
            return SamplingOnTime;
        }

        public void setSamplingOnTime(String SamplingOnTime) {
            this.SamplingOnTime = SamplingOnTime;
        }

        public String getCaleValue() {
            return CaleValue;
        }

        public void setCaleValue(String CaleValue) {
            this.CaleValue = CaleValue;
        }

        public String getValueUnit() {
            return ValueUnit;
        }

        public void setValueUnit(String ValueUnit) {
            this.ValueUnit = ValueUnit;
        }

        public String getValueUnitName() {
            return ValueUnitName;
        }

        public void setValueUnitName(String ValueUnitName) {
            this.ValueUnitName = ValueUnitName;
        }

        public String getRPDValue() {
            return RPDValue;
        }

        public void setRPDValue(String RPDValue) {
            this.RPDValue = RPDValue;
        }

        public boolean isHasPX() {
            return HasPX;
        }

        public void setHasPX(boolean HasPX) {
            this.HasPX = HasPX;
        }
    }
}
