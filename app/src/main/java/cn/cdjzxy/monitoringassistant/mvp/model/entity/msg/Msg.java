package cn.cdjzxy.monitoringassistant.mvp.model.entity.msg;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Msg {


    /**
     * Id : 0c427e30-85e7-4a2a-9332-003dbe142482
     * MsgContent : 您的项目<a  href='javascript:void(0)' class='messagereport'>[地表水监测]</a>编报被<a  href='javascript:void(0)' class='messageuser'>[Admin]</a><a  href='javascript:void(0)' class='messagepass'>提交</a>了，请注意查看。
     * MsgTitle : 编报提交
     * RecipientsId : 61bb48f0-5d8f-49a1-989d-7d6d7033fd91
     * Recipients : Admin
     * SendTime : 2018-12-06T23:16:35.093
     * ReceiveTime : 2018-12-06T23:16:35.093
     * MsgStatus : 0
     * MsgStatusName : 未读
     * ReceiptType : null
     * ReceiptTypeName :
     * MessageOutType : 4
     * MessageOutTypeName : 编报审核
     */

    @Id
    private String Id;
    private String MsgContent;
    private String MsgTitle;
    private String RecipientsId;
    private String Recipients;
    private String SendTime;
    private String ReceiveTime;
    private int    MsgStatus;
    private String MsgStatusName;
    private String ReceiptType;
    private String ReceiptTypeName;
    private int    MessageOutType;
    private String MessageOutTypeName;
    @Generated(hash = 1796981808)
    public Msg(String Id, String MsgContent, String MsgTitle, String RecipientsId, String Recipients, String SendTime, String ReceiveTime, int MsgStatus, String MsgStatusName, String ReceiptType, String ReceiptTypeName,
            int MessageOutType, String MessageOutTypeName) {
        this.Id = Id;
        this.MsgContent = MsgContent;
        this.MsgTitle = MsgTitle;
        this.RecipientsId = RecipientsId;
        this.Recipients = Recipients;
        this.SendTime = SendTime;
        this.ReceiveTime = ReceiveTime;
        this.MsgStatus = MsgStatus;
        this.MsgStatusName = MsgStatusName;
        this.ReceiptType = ReceiptType;
        this.ReceiptTypeName = ReceiptTypeName;
        this.MessageOutType = MessageOutType;
        this.MessageOutTypeName = MessageOutTypeName;
    }
    @Generated(hash = 23037457)
    public Msg() {
    }
    public String getId() {
        return this.Id;
    }
    public void setId(String Id) {
        this.Id = Id;
    }
    public String getMsgContent() {
        return this.MsgContent;
    }
    public void setMsgContent(String MsgContent) {
        this.MsgContent = MsgContent;
    }
    public String getMsgTitle() {
        return this.MsgTitle;
    }
    public void setMsgTitle(String MsgTitle) {
        this.MsgTitle = MsgTitle;
    }
    public String getRecipientsId() {
        return this.RecipientsId;
    }
    public void setRecipientsId(String RecipientsId) {
        this.RecipientsId = RecipientsId;
    }
    public String getRecipients() {
        return this.Recipients;
    }
    public void setRecipients(String Recipients) {
        this.Recipients = Recipients;
    }
    public String getSendTime() {
        return this.SendTime;
    }
    public void setSendTime(String SendTime) {
        this.SendTime = SendTime;
    }
    public String getReceiveTime() {
        return this.ReceiveTime;
    }
    public void setReceiveTime(String ReceiveTime) {
        this.ReceiveTime = ReceiveTime;
    }
    public int getMsgStatus() {
        return this.MsgStatus;
    }
    public void setMsgStatus(int MsgStatus) {
        this.MsgStatus = MsgStatus;
    }
    public String getMsgStatusName() {
        return this.MsgStatusName;
    }
    public void setMsgStatusName(String MsgStatusName) {
        this.MsgStatusName = MsgStatusName;
    }
    public String getReceiptType() {
        return this.ReceiptType;
    }
    public void setReceiptType(String ReceiptType) {
        this.ReceiptType = ReceiptType;
    }
    public String getReceiptTypeName() {
        return this.ReceiptTypeName;
    }
    public void setReceiptTypeName(String ReceiptTypeName) {
        this.ReceiptTypeName = ReceiptTypeName;
    }
    public int getMessageOutType() {
        return this.MessageOutType;
    }
    public void setMessageOutType(int MessageOutType) {
        this.MessageOutType = MessageOutType;
    }
    public String getMessageOutTypeName() {
        return this.MessageOutTypeName;
    }
    public void setMessageOutTypeName(String MessageOutTypeName) {
        this.MessageOutTypeName = MessageOutTypeName;
    }



}
