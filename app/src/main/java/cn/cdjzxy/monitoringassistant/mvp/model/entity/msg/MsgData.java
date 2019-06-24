package cn.cdjzxy.monitoringassistant.mvp.model.entity.msg;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.PagerInfo.PagerInfoBean;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;


public class MsgData {
    private List<Msg> Data;
    private PagerInfoBean PagerInfo;

    public List<Msg> getData() {
        if (CheckUtil.isNull(Data)) {
            return new ArrayList<>();
        }
        return Data;
    }

    public void setData(List<Msg> data) {
        Data = data;
    }

    public PagerInfoBean getPagerInfo() {
        return PagerInfo;
    }

    public void setPagerInfo(PagerInfoBean pagerInfo) {
        PagerInfo = pagerInfo;
    }
}
