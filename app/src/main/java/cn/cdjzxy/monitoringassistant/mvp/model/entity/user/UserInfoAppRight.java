package cn.cdjzxy.monitoringassistant.mvp.model.entity.user;


/**
 * app权限字段
 */
public class UserInfoAppRight {
    //任务权限
    public static final String APP_Permission_Project_Administration_Name = "APP任务管理";
    public static final String APP_Permission_Project_Administration_Num = "99999100";
    public static final String APP_Permission_Project_See_Name = "app任务查看权限";
    public static final String APP_Permission_Project_See_Num = "99999101";
    //表单权限
    public static final String APP_Permission_Sampling_Administration_Name = "APP表单管理";
    public static final String APP_Permission_Sampling_Administration_Num = "99999200";
    public static final String APP_Permission_Sampling_See_Name = "App表单查看";
    public static final String APP_Permission_Sampling_See_Num = "99999201";
    public static final String APP_Permission_Sampling_Add_Name = "App表单添加";
    public static final String APP_Permission_Sampling_Add_Num = "99999202";
    public static final String APP_Permission_Sampling_Modify_Name = "App表单修改";
    public static final String APP_Permission_Sampling_Modify_Num = "99999203";
//    public static final String APP_Permission_Sampling_Edit_Name = "App表单编辑";
//    public static final String APP_Permission_Sampling_Edit_Num = "99999204";
    public static final String APP_Permission_Sampling_Upload_Name = "App表单上传";
    public static final String APP_Permission_Sampling_Upload_Num = "99999204";
    public static final String APP_Permission_Sampling_Submit_Name = "App表单提交";
    public static final String APP_Permission_Sampling_Submit_Num = "99999205";
    public static final String APP_Permission_Sampling_Collection_Name = "App表单收样";
    public static final String APP_Permission_Sampling_Collection_Num = "99999206";
    public static final String APP_Permission_Sampling_Examine_Name = "App表单审核";
    public static final String APP_Permission_Sampling_Examine_Num = "99999207";
    //方案权限
    public static final String APP_Permission_Plan_Administration_Name = "App方案管理";
    public static final String APP_Permission_Plan_Administration_Num = "99999300";
    public static final String APP_Permission_Plan_See_Name = "App方案查看";
    public static final String APP_Permission_Plan_See_Num = "99999301";
    public static final String APP_Permission_Plan_Add_Name = "App方案添加";
    public static final String APP_Permission_Plan_Add_Num = "99999302";
    public static final String APP_Permission_Plan_Modify_Name = "App方案修改";
    public static final String APP_Permission_Plan_Modify_Num = "99999303";
//    public static final String APP_Permission_Plan_Edit_Name = "App方案编辑";
//    public static final String APP_Permission_Plan_Edit = "99999304";
    public static final String APP_Permission_Plan_Upload_Name = "App方案上传";
    public static final String APP_Permission_Plan_Upload_Num = "99999304";
    public static final String APP_Permission_Plan_Submit_Name = "App方案提交";
    public static final String APP_Permission_Plan_Submit_Num = "99999305";
    //仪器权限
    public static final String APP_Permission_Instrument_Administration_Name = "App仪器管理";
    public static final String APP_Permission_Instrument_Administration_Num = "99999400";
    public static final String APP_Permission_Instrument_Lend_Name = "App仪器借出";
    public static final String APP_Permission_Instrument_Lend_Num = "99999401";


    public class AppPermission {
        String name;
        String num;

        public AppPermission(String name, String num) {
            this.name = name;
            this.num = num;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
