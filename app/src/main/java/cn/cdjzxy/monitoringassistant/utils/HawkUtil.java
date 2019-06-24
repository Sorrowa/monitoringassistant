package cn.cdjzxy.monitoringassistant.utils;

import com.alibaba.fastjson.JSON;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存和获取应用设置文件
 */
public class HawkUtil {
    public static final String IS_FIRST_LOGIN = "isFirstLogin";
    public static String getString(String key) {
        return getString(key, "");
    }


    public static String getString(String key, String defaultValue) {
        return get(key, defaultValue);
    }


    public static int getInt(String key) {
        return get(key, -1);
    }

    public static int getInt(String key, int defaultValue) {
        return get(key, defaultValue);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, true);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return get(key, defaultValue);
    }

    public static boolean putString(String key, String value) {
        return put(key, value);
    }

    public static boolean putInt(String key, int value) {
        return put(key, value);
    }

    public static boolean putBoolean(String key, boolean value) {
        return put(key, value);
    }


    public static boolean remove(String key) {
        return Hawk.delete(key);
    }

    public static boolean removeAll() {
        return Hawk.deleteAll();
    }


    /**
     * 把对象转化为json并存储
     * putJsonObject
     */
    public static boolean putJsonObject(String key, Object object) {
        if (CheckUtil.isNull(object)) {
            return false;
        }
        return putString(key, JSON.toJSONString(object));
    }

    /**
     * 获取存储内容并转为对象
     * getJsonObject
     */
    public static <T> T getJsonObject(String key, Class<T> clazz) {
        String str = getString(key);
        if (!CheckUtil.isEmpty(str)) {
            return JSON.parseObject(str, clazz);
        }
        return null;
    }

    /**
     * 把对象转化为json并存储
     * putJsonArray
     */
    public static boolean putJsonArray(String key, Object object) {
        if (CheckUtil.isNull(object)) {
            return false;
        }

        return putString(key, JSON.toJSONString(object));
    }

    /**
     * 获取存储内容并转为列表对象
     * getJsonArray
     */
    public static <T> List<T> getJsonArray(String key, Class<T> clazz) {
        String str = getString(key);
        if (!CheckUtil.isEmpty(str)) {
            return JSON.parseArray(str, clazz);
        }
        return new ArrayList<>();
    }

    private static <T> boolean put(String key, T value) {
        return Hawk.put(key, value);
    }

    private static <T> T get(String key, T defaultValue) {
        return Hawk.get(key, defaultValue);
    }

}
