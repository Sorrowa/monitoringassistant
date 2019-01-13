package cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Size;

import cn.cdjzxy.monitoringassistant.utils.QRCodeUtil;

public class LabelInfo {

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 流水号
     */
    private String number;

    /**
     * 频次
     */
    private String frequecyNo;

    /**
     * 样品类型
     */
    private String type;

    /**
     * 样品编号
     */
    private String sampingCode;

    /**
     * 检测项目
     */
    private String monitemName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 二维码信息
     */
    private String qrCode;

    /**
     * 勾选框1
     */
    private String cb1;

    /**
     * 勾选框2
     */
    private String cb2;

    /**
     * 二维码图片
     */
    private Bitmap qrCodeImg;

    /**
     * 二维码大小
     */
    private int[] qrCodeSize = new int[]{84, 84};

    /**
     * 是否选择该标签
     */
    private boolean isChoose;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFrequecyNo() {
        return frequecyNo;
    }

    public void setFrequecyNo(String frequecyNo) {
        this.frequecyNo = frequecyNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSampingCode() {
        return sampingCode;
    }

    public void setSampingCode(String sampingCode) {
        this.sampingCode = sampingCode;
    }

    public String getMonitemName() {
        return monitemName;
    }

    public void setMonitemName(String monitemName) {
        this.monitemName = monitemName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
        this.qrCodeImg = null;
    }

    public int[] getQrCodeSize() {
        return qrCodeSize;
    }

    public void setQrCodeSize(int[] qrCodeSize) {
        this.qrCodeSize = qrCodeSize;
    }

    public String getCb1() {
        return cb1;
    }

    public void setCb1(String cb1) {
        this.cb1 = cb1;
    }

    public String getCb2() {
        return cb2;
    }

    public void setCb2(String cb2) {
        this.cb2 = cb2;
    }

    public Bitmap getQrCodeImg() {
        if (!TextUtils.isEmpty(this.qrCode) && this.qrCodeImg == null) {
            //创建二维码
            this.qrCodeImg = QRCodeUtil.createQRCodeBitmap(qrCode, this.qrCodeSize[0], this.qrCodeSize[1]);
        }

        return this.qrCodeImg;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public LabelInfo() {
    }

    public LabelInfo(String taskName, String number, String frequecyNo, String type, String sampingCode, String monitemName, String remark, String qrCode, String cb1, String cb2) {
        this.taskName = taskName;
        this.number = number;
        this.frequecyNo = frequecyNo;
        this.type = type;
        this.sampingCode = sampingCode;
        this.monitemName = monitemName;
        this.remark = remark;
        this.qrCode = qrCode;
        this.cb1 = cb1;
        this.cb2 = cb2;
    }

    /**
     * 释放资源
     */
    public void dispose() {
        if (this.qrCodeImg != null) {
            this.qrCodeImg.recycle();
            this.qrCodeImg = null;
        }
    }
}
