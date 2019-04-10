package cn.cdjzxy.monitoringassistant.mvp.model.logic;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wonders.health.lib.base.utils.DataHelper;

import org.apache.poi.ss.formula.functions.IfFunc;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

public class UserInfoHelper {

    private static final String KEY_USER_LOGIN = "user_login";
    private static final String KEY_USER_INFO = "user_info";

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

    public String getPwd() {
        UserInfo userInfo = getUserInfo();
         return null == userInfo ? "" : userInfo.getPwd();
    }


    public UserInfo getUser() {
        UserInfo userInfo = getUserInfo();
        return null == userInfo ? new UserInfo() : userInfo;
    }


    public boolean isLogin() {
        return getUserLoginState();
    }


    public UserInfo getUserInfo() {
        UserInfo userInfo = JSONObject.parseObject(DataHelper.getString(KEY_USER_INFO), UserInfo.class);
        return CheckUtil.isNull(userInfo) ? new UserInfo() : userInfo;
    }

    public boolean saveUserInfo(UserInfo userInfo) {
        return DataHelper.setString(KEY_USER_INFO, JSONObject.toJSONString(userInfo));

    }

}
