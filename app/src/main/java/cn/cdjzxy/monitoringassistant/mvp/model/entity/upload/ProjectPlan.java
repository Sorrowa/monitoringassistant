package cn.cdjzxy.monitoringassistant.mvp.model.entity.upload;

import java.util.List;

import lombok.Data;

@Data
public class ProjectPlan {
    private String               Id;
    private boolean              IsCompelSubmit;
    private List<ProjectContent> ProjectContents;

}
