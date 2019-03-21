package cn.cdjzxy.monitoringassistant.mvp.model.entity.repository;

import lombok.Data;

/**
 * 知识库文件
 */
@Data
public class RepositoryFile {
    // Name="2013--环保信息化远程维护与支撑系统.jpg"
    // FileKey="477199592665648"
    // Size="441749"
    // ModifyDatetime="2017-07-19 14:07:06"
    // UploadDatetime="2017-07-19 14:08:55"
    // Uploader="wenxiaoqin(wenxiaoqin)"
    // IsAudited="true"
    // Encrypted="0"
    // InRecycle="false"
    // DiskIndex="0"
    // Path="103\f_1\"
    // SC="0"
    // CheckoutUserId=""
    // IsMapping="0"

    private String Name;
    private String FileKey;
    private String Size;
    private String ModifyDatetime;
    private String UploadDatetime;
    private String Uploader;
    private String IsAudited;
    private String Encrypted;
    private String InRecycle;
    private String DiskIndex;
    private String Path;
    private String SC;
    private String CheckoutUserId;
    private String IsMapping;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFileKey() {
        return FileKey;
    }

    public void setFileKey(String fileKey) {
        FileKey = fileKey;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getModifyDatetime() {
        return ModifyDatetime;
    }

    public void setModifyDatetime(String modifyDatetime) {
        ModifyDatetime = modifyDatetime;
    }

    public String getUploadDatetime() {
        return UploadDatetime;
    }

    public void setUploadDatetime(String uploadDatetime) {
        UploadDatetime = uploadDatetime;
    }

    public String getUploader() {
        return Uploader;
    }

    public void setUploader(String uploader) {
        Uploader = uploader;
    }

    public String getIsAudited() {
        return IsAudited;
    }

    public void setIsAudited(String isAudited) {
        IsAudited = isAudited;
    }

    public String getEncrypted() {
        return Encrypted;
    }

    public void setEncrypted(String encrypted) {
        Encrypted = encrypted;
    }

    public String getInRecycle() {
        return InRecycle;
    }

    public void setInRecycle(String inRecycle) {
        InRecycle = inRecycle;
    }

    public String getDiskIndex() {
        return DiskIndex;
    }

    public void setDiskIndex(String diskIndex) {
        DiskIndex = diskIndex;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public String getSC() {
        return SC;
    }

    public void setSC(String SC) {
        this.SC = SC;
    }

    public String getCheckoutUserId() {
        return CheckoutUserId;
    }

    public void setCheckoutUserId(String checkoutUserId) {
        CheckoutUserId = checkoutUserId;
    }

    public String getIsMapping() {
        return IsMapping;
    }

    public void setIsMapping(String isMapping) {
        IsMapping = isMapping;
    }
}
