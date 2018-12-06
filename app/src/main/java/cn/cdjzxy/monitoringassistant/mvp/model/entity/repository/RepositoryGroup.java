package cn.cdjzxy.monitoringassistant.mvp.model.entity.repository;

import java.util.List;

import lombok.Data;

/**
 * 知识库组
 */
@Data
public class RepositoryGroup {

    /**
     * Id : 0
     * FatherId :
     * Name : 成都嘉泽兴业科技有限责任公司
     * Members : {"Item":{"LasterLoginTm":"1899-12-30 00:00:00","Email":"","Id":"1000001","CoreId":"","Name":"admin"},"Count":"1"}
     * Childs : {"GroupItem":{"Id":"103","FatherId":"0","Members":{"Item":[{"LasterLoginTm":"1899-12-30 00:00:00","Email":"","Id":"1000004","CoreId":"","Name":"renfang(renfang)"},{"LasterLoginTm":"1899-12-30 00:00:00","Email":"","Id":"1000002","CoreId":"","Name":"wenxiaoqin(wenxiaoqin)"},{"LasterLoginTm":"1899-12-30 00:00:00","Email":"","Id":"1000003","CoreId":"","Name":"yifei(yifei)"}],"Count":"3"},"Name":"售前"},"Count":"1"}
     */

    private String                Id;//组id
    private String                FatherId; //父级id
    private String                Name;//组名称
    private List<Member>          Members;//组成员列表
    private List<RepositoryGroup> Childs;//子级组

    /**
     * 成员
     */
    @Data
    public static class Member {
        /**
         * LasterLoginTm : 1899-12-30 00:00:00
         * Email :
         * Id : 1000002
         * CoreId :
         * Name : wenxiaoqin(wenxiaoqin)
         */

        private String LasterLoginTm;
        private String Email;
        private String Id;
        private String CoreId;
        private String Name;

    }
}
