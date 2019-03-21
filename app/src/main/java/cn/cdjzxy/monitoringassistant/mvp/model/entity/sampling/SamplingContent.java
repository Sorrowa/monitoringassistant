package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Transient;
import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class SamplingContent {
    @Id
    private String id;
    private String SamplingId;
    private String ProjectId;
    private int OrderIndex;
    private String SampingCode;
    private int FrequecyNo;
    private String SamplingTime;
    private int SamplingType;
    private String SampStandId;
    private String MonitemId;
    private String MonitemName;
    private String AddresssId;
    private String AddressName;
    private int SamplingCount;
    private String Preservative;
    private String SampleCollection;
    private String SampleAcceptance;
    private boolean IsSenceAnalysis;
    private boolean IsCompare;
    private String AnasysTime;
    private String Value1;
    private String ValueUnit1;
    private String valueUnit1Name;
    private String Value2;
    private String valueUnit2;
    private String valueUnit2Name;
    private String Value3;
    private String ValueUnit3;
    private String valueUnit3Name;
    private String Value;
    private String ValueUnit;
    private String ValueUnitNname;
    private String Value4;
    private String Value5;
    private String MethodName;
    private String MethodId;
    private String DeviceIdName;
    private String DeviceId;
    private String Description;
    private String PrivateData;
    private boolean isAddPreserve;
    private String SamplingOnTime;
    private String SenceMonitemId;//现场监测id
    private String SenceMonitemName;//现场监测name
    @Transient
    private JSONObject PrivateJsonData;
    @Transient
    private boolean isCanSelect;
    @Transient
    private boolean isSelected;
    @Transient
    private String TempValue1;
    @Transient
    private String TempValue2;
    @Transient
    private String TempValue3;

    @Keep()
    public SamplingContent(String id, String SamplingId, String ProjectId, int OrderIndex,
            String SampingCode, int FrequecyNo, String SamplingTime, int SamplingType,
            String SampStandId, String MonitemId, String MonitemName, String AddresssId,
            String AddressName, int SamplingCount, String Preservative,
            String SampleCollection, String SampleAcceptance, boolean IsSenceAnalysis,
            boolean IsCompare, String AnasysTime, String Value1, String ValueUnit1,
            String valueUnit1Name, String Value2, String valueUnit2, String valueUnit2Name,
            String Value3, String ValueUnit3, String valueUnit3Name, String Value,
            String ValueUnit, String ValueUnitNname, String Value4, String Value5,
            String MethodName, String MethodId, String DeviceIdName, String DeviceId,
            String Description, String PrivateData, boolean isAddPreserve,
            String SamplingOnTime, String SenceMonitemId, String SenceMonitemName) {
        this.id = id;
        this.SamplingId = SamplingId;
        this.ProjectId = ProjectId;
        this.OrderIndex = OrderIndex;
        this.SampingCode = SampingCode;
        this.FrequecyNo = FrequecyNo;
        this.SamplingTime = SamplingTime;
        this.SamplingType = SamplingType;
        this.SampStandId = SampStandId;
        this.MonitemId = MonitemId;
        this.MonitemName = MonitemName;
        this.AddresssId = AddresssId;
        this.AddressName = AddressName;
        this.SamplingCount = SamplingCount;
        this.Preservative = Preservative;
        this.SampleCollection = SampleCollection;
        this.SampleAcceptance = SampleAcceptance;
        this.IsSenceAnalysis = IsSenceAnalysis;
        this.IsCompare = IsCompare;
        this.AnasysTime = AnasysTime;
        this.Value1 = Value1;
        this.ValueUnit1 = ValueUnit1;
        this.valueUnit1Name = valueUnit1Name;
        this.Value2 = Value2;
        this.valueUnit2 = valueUnit2;
        this.valueUnit2Name = valueUnit2Name;
        this.Value3 = Value3;
        this.ValueUnit3 = ValueUnit3;
        this.valueUnit3Name = valueUnit3Name;
        this.Value = Value;
        this.ValueUnit = ValueUnit;
        this.ValueUnitNname = ValueUnitNname;
        this.Value4 = Value4;
        this.Value5 = Value5;
        this.MethodName = MethodName;
        this.MethodId = MethodId;
        this.DeviceIdName = DeviceIdName;
        this.DeviceId = DeviceId;
        this.Description = Description;
        this.PrivateData = PrivateData;
        this.isAddPreserve = isAddPreserve;
        this.SamplingOnTime = SamplingOnTime;
        this.SenceMonitemId = SenceMonitemId;
        this.SenceMonitemName = SenceMonitemName;
    }

    @Keep()
    public SamplingContent() {
    }

    public boolean isSenceAnalysis() {
        return IsSenceAnalysis;
    }

    public void setSenceAnalysis(boolean senceAnalysis) {
        IsSenceAnalysis = senceAnalysis;
    }

    public boolean isCompare() {
        return IsCompare;
    }

    public void setCompare(boolean compare) {
        IsCompare = compare;
    }

    public boolean isAddPreserve() {
        return isAddPreserve;
    }

    public void setAddPreserve(boolean addPreserve) {
        isAddPreserve = addPreserve;
    }

    public JSONObject getPrivateJsonData() {
        return PrivateJsonData;
    }

    public void setPrivateJsonData(JSONObject privateJsonData) {
        PrivateJsonData = privateJsonData;
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

    public String getTempValue1() {
        return TempValue1;
    }

    public void setTempValue1(String tempValue1) {
        TempValue1 = tempValue1;
    }

    public String getTempValue2() {
        return TempValue2;
    }

    public void setTempValue2(String tempValue2) {
        TempValue2 = tempValue2;
    }

    public String getTempValue3() {
        return TempValue3;
    }

    public void setTempValue3(String tempValue3) {
        TempValue3 = tempValue3;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSamplingId() {
        return this.SamplingId;
    }

    public void setSamplingId(String SamplingId) {
        this.SamplingId = SamplingId;
    }

    public String getProjectId() {
        return this.ProjectId;
    }

    public void setProjectId(String ProjectId) {
        this.ProjectId = ProjectId;
    }

    public int getOrderIndex() {
        return this.OrderIndex;
    }

    public void setOrderIndex(int OrderIndex) {
        this.OrderIndex = OrderIndex;
    }

    public String getSampingCode() {
        return this.SampingCode;
    }

    public void setSampingCode(String SampingCode) {
        this.SampingCode = SampingCode;
    }

    public int getFrequecyNo() {
        return this.FrequecyNo;
    }

    public void setFrequecyNo(int FrequecyNo) {
        this.FrequecyNo = FrequecyNo;
    }

    public String getSamplingTime() {
        return this.SamplingTime;
    }

    public void setSamplingTime(String SamplingTime) {
        this.SamplingTime = SamplingTime;
    }

    public int getSamplingType() {
        return this.SamplingType;
    }

    public void setSamplingType(int SamplingType) {
        this.SamplingType = SamplingType;
    }

    public String getSampStandId() {
        return this.SampStandId;
    }

    public void setSampStandId(String SampStandId) {
        this.SampStandId = SampStandId;
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

    public String getAddresssId() {
        return this.AddresssId;
    }

    public void setAddresssId(String AddresssId) {
        this.AddresssId = AddresssId;
    }

    public String getAddressName() {
        return this.AddressName;
    }

    public void setAddressName(String AddressName) {
        this.AddressName = AddressName;
    }

    public int getSamplingCount() {
        return this.SamplingCount;
    }

    public void setSamplingCount(int SamplingCount) {
        this.SamplingCount = SamplingCount;
    }

    public String getPreservative() {
        return this.Preservative;
    }

    public void setPreservative(String Preservative) {
        this.Preservative = Preservative;
    }

    public String getSampleCollection() {
        return this.SampleCollection;
    }

    public void setSampleCollection(String SampleCollection) {
        this.SampleCollection = SampleCollection;
    }

    public String getSampleAcceptance() {
        return this.SampleAcceptance;
    }

    public void setSampleAcceptance(String SampleAcceptance) {
        this.SampleAcceptance = SampleAcceptance;
    }

    public boolean getIsSenceAnalysis() {
        return this.IsSenceAnalysis;
    }

    public void setIsSenceAnalysis(boolean IsSenceAnalysis) {
        this.IsSenceAnalysis = IsSenceAnalysis;
    }

    public boolean getIsCompare() {
        return this.IsCompare;
    }

    public void setIsCompare(boolean IsCompare) {
        this.IsCompare = IsCompare;
    }

    public String getAnasysTime() {
        return this.AnasysTime;
    }

    public void setAnasysTime(String AnasysTime) {
        this.AnasysTime = AnasysTime;
    }

    public String getValue1() {
        return this.Value1;
    }

    public void setValue1(String Value1) {
        this.Value1 = Value1;
    }

    public String getValueUnit1() {
        return this.ValueUnit1;
    }

    public void setValueUnit1(String ValueUnit1) {
        this.ValueUnit1 = ValueUnit1;
    }

    public String getValueUnit1Name() {
        return this.valueUnit1Name;
    }

    public void setValueUnit1Name(String valueUnit1Name) {
        this.valueUnit1Name = valueUnit1Name;
    }

    public String getValue2() {
        return this.Value2;
    }

    public void setValue2(String Value2) {
        this.Value2 = Value2;
    }

    public String getValueUnit2() {
        return this.valueUnit2;
    }

    public void setValueUnit2(String valueUnit2) {
        this.valueUnit2 = valueUnit2;
    }

    public String getValueUnit2Name() {
        return this.valueUnit2Name;
    }

    public void setValueUnit2Name(String valueUnit2Name) {
        this.valueUnit2Name = valueUnit2Name;
    }

    public String getValue3() {
        return this.Value3;
    }

    public void setValue3(String Value3) {
        this.Value3 = Value3;
    }

    public String getValueUnit3() {
        return this.ValueUnit3;
    }

    public void setValueUnit3(String ValueUnit3) {
        this.ValueUnit3 = ValueUnit3;
    }

    public String getValueUnit3Name() {
        return this.valueUnit3Name;
    }

    public void setValueUnit3Name(String valueUnit3Name) {
        this.valueUnit3Name = valueUnit3Name;
    }

    public String getValue() {
        return this.Value;
    }

    public void setValue(String Value) {
        this.Value = Value;
    }

    public String getValueUnit() {
        return this.ValueUnit;
    }

    public void setValueUnit(String ValueUnit) {
        this.ValueUnit = ValueUnit;
    }

    public String getValueUnitNname() {
        return this.ValueUnitNname;
    }

    public void setValueUnitNname(String ValueUnitNname) {
        this.ValueUnitNname = ValueUnitNname;
    }

    public String getValue4() {
        return this.Value4;
    }

    public void setValue4(String Value4) {
        this.Value4 = Value4;
    }

    public String getValue5() {
        return this.Value5;
    }

    public void setValue5(String Value5) {
        this.Value5 = Value5;
    }

    public String getMethodName() {
        return this.MethodName;
    }

    public void setMethodName(String MethodName) {
        this.MethodName = MethodName;
    }

    public String getMethodId() {
        return this.MethodId;
    }

    public void setMethodId(String MethodId) {
        this.MethodId = MethodId;
    }

    public String getDeviceIdName() {
        return this.DeviceIdName;
    }

    public void setDeviceIdName(String DeviceIdName) {
        this.DeviceIdName = DeviceIdName;
    }

    public String getDeviceId() {
        return this.DeviceId;
    }

    public void setDeviceId(String DeviceId) {
        this.DeviceId = DeviceId;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getPrivateData() {
        return this.PrivateData;
    }

    public void setPrivateData(String PrivateData) {
        this.PrivateData = PrivateData;
    }

    public boolean getIsAddPreserve() {
        return this.isAddPreserve;
    }

    public void setIsAddPreserve(boolean isAddPreserve) {
        this.isAddPreserve = isAddPreserve;
    }

    public String getSamplingOnTime() {
        return this.SamplingOnTime;
    }

    public void setSamplingOnTime(String SamplingOnTime) {
        this.SamplingOnTime = SamplingOnTime;
    }

    public String getSenceMonitemId() {
        return this.SenceMonitemId;
    }

    public void setSenceMonitemId(String senceMonitemId) {
        this.SenceMonitemId = senceMonitemId;
    }

    public String getSenceMonitemName() {
        return this.SenceMonitemName;
    }

    public void setSenceMonitemName(String senceMonitemName) {
        this.SenceMonitemName = senceMonitemName;
    }
}
