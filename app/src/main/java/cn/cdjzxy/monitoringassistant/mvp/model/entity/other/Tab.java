package cn.cdjzxy.monitoringassistant.mvp.model.entity.other;

import lombok.Data;

@Data
public class Tab {
    private String  tabName;
    private int     resId;
    private int     selectedResId;
    private boolean isSelected;
}
