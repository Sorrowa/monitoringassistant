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
}
