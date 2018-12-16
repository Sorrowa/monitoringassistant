package cn.cdjzxy.monitoringassistant.mvp.model.logic;

import android.text.TextUtils;

import com.wonders.health.lib.base.utils.DataHelper;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;

public class UserInfoHelper {

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
        UserInfo userInfo = getUserInfo();
        return null == userInfo ? "" : userInfo.getWorkNo();
    }


    public UserInfo getUser() {
        UserInfo userInfo = getUserInfo();
        return null == userInfo ? new UserInfo() : userInfo;
    }


    public boolean isLogin() {
        return getUserLoginState() && !TextUtils.isEmpty(getUser().getId());
    }


    private UserInfo getUserInfo() {
        if (null != DBHelper.get()) {
            return DBHelper.get().getUserInfoDao().queryBuilder().unique();
        }
        return new UserInfo();
    }

}
