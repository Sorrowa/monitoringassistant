package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

public class NoiseSamplingFile extends SamplingFile{
    private boolean isSelect;

    public boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean select) {
        isSelect = select;
    }
}
