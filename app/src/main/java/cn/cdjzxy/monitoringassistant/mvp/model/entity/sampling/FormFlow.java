package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FormFlow {
    @Id(autoincrement = true)
    private long id;
    private long formSelect_Id;

    private String  FlowId;
    private String  FlowName;
    private int     NodeNumber;
    private int     CurrentStatus;
    private boolean IsJoinFlow;
    private String  NodeHandleCode;
    private String  AllFlowUsers;
    private String  FlowUserIds;
    private String  FlowUserNames;
    @Generated(hash = 1495533852)
    public FormFlow(long id, long formSelect_Id, String FlowId, String FlowName,
            int NodeNumber, int CurrentStatus, boolean IsJoinFlow,
            String NodeHandleCode, String AllFlowUsers, String FlowUserIds,
            String FlowUserNames) {
        this.id = id;
        this.formSelect_Id = formSelect_Id;
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
    @Generated(hash = 384426255)
    public FormFlow() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getFormSelect_Id() {
        return this.formSelect_Id;
    }
    public void setFormSelect_Id(long formSelect_Id) {
        this.formSelect_Id = formSelect_Id;
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
