package cn.cdjzxy.monitoringassistant.mvp.model.entity;

import java.util.List;

public class test {


    /**
     * code : 0
     * data : {"hospitalId":166,"hospitalCode":"451208767","receiveThumb":"http://218.89.178.119:8089/webServiceOrderRegCDS/hosImage/1212174","hospitalPhoto":"http://218.89.178.119:8089/webServiceOrderRegCDS/hosImage/1212174","hospitalName":"四川绵阳四0四医院","hospitalAddress":"绵阳市跃进路56号","hospitalDesc":"院始建于1964年，医院占地70余亩，业务用建筑面积7万余平方米，除跃进路本部外，建有四0四医院丰谷病区（绵阳市传染病医院）。开放床位1500张，在岗职工1400余人","hospitalTel":"0816-2333342","hospitalGrade":"三级甲等","receiveCount":"46","evaluateCount":7,"concern":1,"evaluList":[{"id":46,"uid":"98b9d7caa7f740d59c24298e2b75d6e9","nickName":"黄*","content":"带个好哈回答的","createTime":"2016-11-18"},{"id":45,"uid":"85de2861378845fcab8a601574765ebc","nickName":"测**","content":"啦啦啦啦啊","createTime":"2016-11-18"},{"id":44,"uid":"28cc60e1774e45e09000e1211a2fcacd","nickName":"微健康用户28cc","content":"带个好哈回答的","createTime":"2016-11-18"},{"id":43,"uid":"28cc60e1774e45e09000e1211a2fcacd","nickName":"微健康用户28cc","content":"发个广告发个广告发个广告发个广告发个广告","createTime":"2016-11-18"},{"id":31,"uid":"1fa2775f90014c0ab5aae8dd89231094","nickName":"李*","content":"头大了头大了头大了头大了头大了头大了头大了头大了头大了大了头大了大了头","createTime":"2016-11-17"}],"payChannel":"{\"online\":true,\"underline\":false}","payMethod":"{\"alipay\":true,\"wepay\":true}","jzModules":"{\"yygh\":true,\"mzjf\":false,\"jyjc\":false,\"dzcf\":false,\"zyjyj\":false,\"ksgy\":true}"}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * hospitalId : 166
         * hospitalCode : 451208767
         * receiveThumb : http://218.89.178.119:8089/webServiceOrderRegCDS/hosImage/1212174
         * hospitalPhoto : http://218.89.178.119:8089/webServiceOrderRegCDS/hosImage/1212174
         * hospitalName : 四川绵阳四0四医院
         * hospitalAddress : 绵阳市跃进路56号
         * hospitalDesc : 院始建于1964年，医院占地70余亩，业务用建筑面积7万余平方米，除跃进路本部外，建有四0四医院丰谷病区（绵阳市传染病医院）。开放床位1500张，在岗职工1400余人
         * hospitalTel : 0816-2333342
         * hospitalGrade : 三级甲等
         * receiveCount : 46
         * evaluateCount : 7
         * concern : 1
         * evaluList : [{"id":46,"uid":"98b9d7caa7f740d59c24298e2b75d6e9","nickName":"黄*","content":"带个好哈回答的","createTime":"2016-11-18"},{"id":45,"uid":"85de2861378845fcab8a601574765ebc","nickName":"测**","content":"啦啦啦啦啊","createTime":"2016-11-18"},{"id":44,"uid":"28cc60e1774e45e09000e1211a2fcacd","nickName":"微健康用户28cc","content":"带个好哈回答的","createTime":"2016-11-18"},{"id":43,"uid":"28cc60e1774e45e09000e1211a2fcacd","nickName":"微健康用户28cc","content":"发个广告发个广告发个广告发个广告发个广告","createTime":"2016-11-18"},{"id":31,"uid":"1fa2775f90014c0ab5aae8dd89231094","nickName":"李*","content":"头大了头大了头大了头大了头大了头大了头大了头大了头大了大了头大了大了头","createTime":"2016-11-17"}]
         * payChannel : {"online":true,"underline":false}
         * payMethod : {"alipay":true,"wepay":true}
         * jzModules : {"yygh":true,"mzjf":false,"jyjc":false,"dzcf":false,"zyjyj":false,"ksgy":true}
         */

        private int hospitalId;
        private String hospitalCode;
        private String receiveThumb;
        private String hospitalPhoto;
        private String hospitalName;
        private String hospitalAddress;
        private String hospitalDesc;
        private String hospitalTel;
        private String hospitalGrade;
        private String receiveCount;
        private int evaluateCount;
        private int concern;
        private String payChannel;
        private String payMethod;
        private String jzModules;
        private List<EvaluListBean> evaluList;

        public int getHospitalId() {
            return hospitalId;
        }

        public void setHospitalId(int hospitalId) {
            this.hospitalId = hospitalId;
        }

        public String getHospitalCode() {
            return hospitalCode;
        }

        public void setHospitalCode(String hospitalCode) {
            this.hospitalCode = hospitalCode;
        }

        public String getReceiveThumb() {
            return receiveThumb;
        }

        public void setReceiveThumb(String receiveThumb) {
            this.receiveThumb = receiveThumb;
        }

        public String getHospitalPhoto() {
            return hospitalPhoto;
        }

        public void setHospitalPhoto(String hospitalPhoto) {
            this.hospitalPhoto = hospitalPhoto;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public String getHospitalAddress() {
            return hospitalAddress;
        }

        public void setHospitalAddress(String hospitalAddress) {
            this.hospitalAddress = hospitalAddress;
        }

        public String getHospitalDesc() {
            return hospitalDesc;
        }

        public void setHospitalDesc(String hospitalDesc) {
            this.hospitalDesc = hospitalDesc;
        }

        public String getHospitalTel() {
            return hospitalTel;
        }

        public void setHospitalTel(String hospitalTel) {
            this.hospitalTel = hospitalTel;
        }

        public String getHospitalGrade() {
            return hospitalGrade;
        }

        public void setHospitalGrade(String hospitalGrade) {
            this.hospitalGrade = hospitalGrade;
        }

        public String getReceiveCount() {
            return receiveCount;
        }

        public void setReceiveCount(String receiveCount) {
            this.receiveCount = receiveCount;
        }

        public int getEvaluateCount() {
            return evaluateCount;
        }

        public void setEvaluateCount(int evaluateCount) {
            this.evaluateCount = evaluateCount;
        }

        public int getConcern() {
            return concern;
        }

        public void setConcern(int concern) {
            this.concern = concern;
        }

        public String getPayChannel() {
            return payChannel;
        }

        public void setPayChannel(String payChannel) {
            this.payChannel = payChannel;
        }

        public String getPayMethod() {
            return payMethod;
        }

        public void setPayMethod(String payMethod) {
            this.payMethod = payMethod;
        }

        public String getJzModules() {
            return jzModules;
        }

        public void setJzModules(String jzModules) {
            this.jzModules = jzModules;
        }

        public List<EvaluListBean> getEvaluList() {
            return evaluList;
        }

        public void setEvaluList(List<EvaluListBean> evaluList) {
            this.evaluList = evaluList;
        }

        public static class EvaluListBean {
            /**
             * id : 46
             * uid : 98b9d7caa7f740d59c24298e2b75d6e9
             * nickName : 黄*
             * content : 带个好哈回答的
             * createTime : 2016-11-18
             */

            private int id;
            private String uid;
            private String nickName;
            private String content;
            private String createTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
