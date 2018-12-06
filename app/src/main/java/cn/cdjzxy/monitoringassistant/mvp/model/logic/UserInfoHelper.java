package cn.cdjzxy.monitoringassistant.mvp.model.logic;

import com.wonders.health.lib.base.utils.DataHelper;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.UserInfo;

public class UserInfoHelper {

    private static final String KEY_USER_INFO = "user_info";

    private static final String KEY_USER_NAME = "user_name";

    private static final String KEY_USER_LOGIN = "user_login";

    private static UserInfoHelper sUserInfoHelper;

    private UserInfoHelper() {
    }

    public static UserInfoHelper get() {
        if (null == sUserInfoHelper) {
            synchronized (UserInfoHelper.class) {
                if (null == sUserInfoHelper) {
                    sUserInfoHelper = new UserInfoHelper();
                }
            }
        }
        return sUserInfoHelper;
    }

    public boolean getUserLoginState() {
        return DataHelper.getBoolean(KEY_USER_LOGIN);
    }

    public boolean saveUserLoginStatee(boolean isLogined) {
        return DataHelper.setBoolean(KEY_USER_LOGIN, isLogined);
    }


    public String getUserName() {
        return DataHelper.getString(KEY_USER_NAME);
    }

    public boolean saveUserName(String userName) {
        return DataHelper.setString(KEY_USER_NAME, userName);
    }

    public UserInfo getUser() {
        UserInfo userInfo = DataHelper.getData(KEY_USER_INFO);
        return null == userInfo ? new UserInfo() : userInfo;
    }

    public boolean saveUser(UserInfo userInfo) {
        return DataHelper.saveData(KEY_USER_INFO, userInfo);
    }

    public boolean isLogin() {
        return getUserLoginState();
    }

}
