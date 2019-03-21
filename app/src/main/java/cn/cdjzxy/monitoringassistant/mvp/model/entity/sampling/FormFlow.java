package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FormFlow {
    private String  FlowId;
    private String  FlowName;
    private int     NodeNumber;
    private int     CurrentStatus;
    private boolean IsJoinFlow;
    private String  NodeHandleCode;
    private String  AllFlowUsers;
    private String  FlowUserIds;
    private String  FlowUserNames;
    @Keep()
    public FormFlow(String FlowId, String FlowName, int NodeNumber,
            int CurrentStatus, boolean IsJoinFlow, String NodeHandleCode,
            String AllFlowUsers, String FlowUserIds, String FlowUserNames) {
        this.FlowId = FlowId;
        this.FlowName = FlowName;
        this.NodeNumber = NodeNumber;
        this.CurrentStatus = CurrentStatus;
        this.IsJoinFlow = IsJoinFlow;
        this.NodeHandleCode = NodeHandleCode;
        this.AllFlowUsers = AllFlowUsers;
        this.FlowUserIds = FlowUserIds;
        this.FlowUserNames = FlowUserNames;
    }
    @Keep()
    public FormFlow() {
    }
    public String getFlowId() {
        return this.FlowId;
    }
    public void setFlowId(String FlowId) {
        this.FlowId = FlowId;
    }
    public String getFlowName() {
        return this.FlowName;
    }
    public void setFlowName(String FlowName) {
        this.FlowName = FlowName;
    }
    public int getNodeNumber() {
        return this.NodeNumber;
    }
    public void setNodeNumber(int NodeNumber) {
        this.NodeNumber = NodeNumber;
    }
    public int getCurrentStatus() {
        return this.CurrentStatus;
    }
    public void setCurrentStatus(int CurrentStatus) {
        this.CurrentStatus = CurrentStatus;
    }
    public boolean getIsJoinFlow() {
        return this.IsJoinFlow;
    }
    public void setIsJoinFlow(boolean IsJoinFlow) {
        this.IsJoinFlow = IsJoinFlow;
    }
    public String getNodeHandleCode() {
        return this.NodeHandleCode;
    }
    public void setNodeHandleCode(String NodeHandleCode) {
        this.NodeHandleCode = NodeHandleCode;
    }
    public String getAllFlowUsers() {
        return this.AllFlowUsers;
    }
    public void setAllFlowUsers(String AllFlowUsers) {
        this.AllFlowUsers = AllFlowUsers;
    }
    public String getFlowUserIds() {
        return this.FlowUserIds;
    }
    public void setFlowUserIds(String FlowUserIds) {
        this.FlowUserIds = FlowUserIds;
    }
    public String getFlowUserNames() {
        return this.FlowUserNames;
    }
    public void setFlowUserNames(String FlowUserNames) {
        this.FlowUserNames = FlowUserNames;
    }

}
