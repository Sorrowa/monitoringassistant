package cn.cdjzxy.monitoringassistant.mvp.model.logic;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.wonders.health.lib.base.utils.DataHelper;

import org.apache.poi.ss.formula.functions.IfFunc;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.User;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

public class UserInfoHelper {

    private static final String KEY_USER_LOGIN = "user_login";
    private static final String KEY_USER_INFO = "user_info";
    private static final String KEY_USER_IS_OPEN_TRACE = "user_is_open_trace";//用户是否开启轨迹

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

    public void removeUserInfo() {
        DataHelper.remove(KEY_USER_INFO);
    }

    /**
     * 获取app权限字段
     *
     * @return{@UserInfo.getAppRightStr()}
     */
    public String getUserInfoAppRightStr() {
        UserInfo userInfo = getUserInfo();
        return userInfo.getAppRightStr();
    }

    /**
     * 检查是否拥有权限
     *
     * @param s
     * @return
     */
    public boolean isHavePermission(String s) {
        String myPermission = getUserInfoAppRightStr();
        if (CheckUtil.isEmpty(myPermission)) {
            return false;
        }

        return myPermission.contains(s);
    }

    /**
     * 保存用户是否开启轨迹
     *
     * @param isOpen
     * @return
     */
    public boolean saveUserTraceOpen(boolean isOpen) {
        return DataHelper.setBoolean(KEY_USER_IS_OPEN_TRACE, isOpen);
    }

    public boolean getUserTraceOpen() {
        return DataHelper.getBoolean(KEY_USER_IS_OPEN_TRACE);
    }
}
