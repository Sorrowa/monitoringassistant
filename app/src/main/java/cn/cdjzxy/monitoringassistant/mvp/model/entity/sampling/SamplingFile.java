package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SamplingFile {

    @Id
    private String Id;
    private String SamplingId;
    private String FileName;
    private String FilePath;
    @Generated(hash = 895474054)
    public SamplingFile(String Id, String SamplingId, String FileName,
            String FilePath) {
        this.Id = Id;
        this.SamplingId = SamplingId;
        this.FileName = FileName;
        this.FilePath = FilePath;
    }
    @Generated(hash = 863733679)
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



}
