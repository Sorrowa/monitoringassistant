package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

/**
 * 水和废水扩展信息
 */
public class FsExtends {
    private String ClientName;//企业名称
    private String ClientAdd;//企业地址
    private String HandleDevice;//处理设施
    private String Receiving;//受纳水体
    private String FrequencyNo;//采样频次
    private String SewageDisposal;//是否进行污水处理
    private String TransportConditions;
    private String TempFrequency;
    private String waterWD;//水温
    private String waterLS;//流速
    private String waterLL;//流量
    private String BuildTime;//建设时间
    private String AccessPipeNetwork;//是否进入管网

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientAdd() {
        return ClientAdd;
    }

    public void setClientAdd(String clientAdd) {
        ClientAdd = clientAdd;
    }

    public String getHandleDevice() {
        return HandleDevice;
    }

    public void setHandleDevice(String handleDevice) {
        HandleDevice = handleDevice;
    }

    public String getReceiving() {
        return Receiving;
    }

    public void setReceiving(String receiving) {
        Receiving = receiving;
    }

    public String getFrequencyNo() {
        return FrequencyNo;
    }

    public void setFrequencyNo(String frequencyNo) {
        FrequencyNo = frequencyNo;
    }

    public String getSewageDisposal() {
        return SewageDisposal;
    }

    public void setSewageDisposal(String sewageDisposal) {
        SewageDisposal = sewageDisposal;
    }

    public String getTransportConditions() {
        return TransportConditions;
    }

    public void setTransportConditions(String transportConditions) {
        TransportConditions = transportConditions;
    }

    public String getTempFrequency() {
        return TempFrequency;
    }

    public void setTempFrequency(String tempFrequency) {
        TempFrequency = tempFrequency;
    }

    public String getWaterWD() {
        return waterWD;
    }

    public void setWaterWD(String waterWD) {
        this.waterWD = waterWD;
    }

    public String getWaterLS() {
        return waterLS;
    }

    public void setWaterLS(String waterLS) {
        this.waterLS = waterLS;
    }

    public String getWaterLL() {
        return waterLL;
    }

    public void setWaterLL(String waterLL) {
        this.waterLL = waterLL;
    }

    public String getBuildTime() {
        return BuildTime;
    }

    public void setBuildTime(String buildTime) {
        BuildTime = buildTime;
    }

    public String getAccessPipeNetwork() {
        return AccessPipeNetwork;
    }

    public void setAccessPipeNetwork(String accessPipeNetwork) {
        AccessPipeNetwork = accessPipeNetwork;
    }
}
