package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class SamplingFile {

    @Id
    private String LocalId;
    private String Id;
    private String SamplingId;
    private String FileName;
    private String FilePath;
    private String UpdateTime;
    private boolean IsUploaded;
    @Keep()
    public SamplingFile(String LocalId, String Id, String SamplingId,
            String FileName, String FilePath, String UpdateTime,
            boolean IsUploaded) {
        this.LocalId = LocalId;
        this.Id = Id;
        this.SamplingId = SamplingId;
        this.FileName = FileName;
        this.FilePath = FilePath;
        this.UpdateTime = UpdateTime;
        this.IsUploaded = IsUploaded;
    }
    @Keep()
    public SamplingFile() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getSamplingId() {
        return this.SamplingId;
    }
    public void setSamplingId(String SamplingId) {
        this.SamplingId = SamplingId;
    }
    public String getFileName() {
        return this.FileName;
    }
    public void setFileName(String FileName) {
        this.FileName = FileName;
    }
    public String getFilePath() {
        return this.FilePath;
    }
    public void setFilePath(String FilePath) {
        this.FilePath = FilePath;
    }
    public String getLocalId() {
        return this.LocalId;
    }
    public void setLocalId(String LocalId) {
        this.LocalId = LocalId;
    }
    public String getUpdateTime() {
        return this.UpdateTime;
    }
    public void setUpdateTime(String UpdateTime) {
        this.UpdateTime = UpdateTime;
    }
    public boolean getIsUploaded() {
        return this.IsUploaded;
    }
    public void setIsUploaded(boolean IsUploaded) {
        this.IsUploaded = IsUploaded;
    }



}
