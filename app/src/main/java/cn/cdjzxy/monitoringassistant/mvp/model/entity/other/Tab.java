package cn.cdjzxy.monitoringassistant.mvp.model.entity.other;

import lombok.Data;

@Data
public class Tab {
    private String  tabName;
    private int     resId;
    private int     selectedResId;
    private boolean isSelected;

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getSelectedResId() {
        return selectedResId;
    }

    public void setSelectedResId(int selectedResId) {
        this.selectedResId = selectedResId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
