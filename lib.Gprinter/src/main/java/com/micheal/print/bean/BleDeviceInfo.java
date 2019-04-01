package com.micheal.print.bean;

import android.text.TextUtils;

import com.micheal.print.manager.MyPrinterPlusConnManager;

public class BleDeviceInfo {
        private String name;
        private String address;
        private int status;

        public String getName() {
            if (TextUtils.isEmpty(name)) {
                return "";
            }
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public BleDeviceInfo() {
        }

        public BleDeviceInfo(String name, String address, int status) {
            this.name = name;
            this.address = address;
            this.status = status;
        }

        public String getStatusName() {
            switch (this.status) {
                case MyPrinterPlusConnManager.CONN_STATE_DISCONNECT:
                    return "未连接";
                case MyPrinterPlusConnManager.CONN_STATE_CONNECTING:
                    return "连接中...";
                case MyPrinterPlusConnManager.CONN_STATE_CONNECTED:
                    return "连接成功";
                case MyPrinterPlusConnManager.CONN_STATE_NO_INVALID_PRINTER:
                    return "设备不支持，请重新连接打印机";
            }
            return "";
        }
    }