package cn.cdjzxy.monitoringassistant.mvp.model.entity.repository;

import java.util.List;

import lombok.Data;

/**
 * 知识库文件夹
 */
@Data
public class RepositoryFolder {
    private String                 Id;//文件编号id
    private String                 FatherId; //父文件夹id
    private String                 Path;//文件夹名称
    private List<RepositoryFolder> Childs;//子文件夹

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFatherId() {
        return FatherId;
    }

    public void setFatherId(String fatherId) {
        FatherId = fatherId;
    }

    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public List<RepositoryFolder> getChilds() {
        return Childs;
    }

    public void setChilds(List<RepositoryFolder> childs) {
        Childs = childs;
    }
}
