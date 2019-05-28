package cn.cdjzxy.monitoringassistant.utils;

import android.text.TextUtils;

import java.util.List;

public class StringUtil {

    /**
     * 不够位数的在前面补0，保留num的长度位数字
     *
     * @param number
     * @return
     */
    public static String autoGenericCode(int number, int num) {
        String result = "";
        // 保留num的位数
        // 0 代表前面补充0
        // num 代表长度为4
        // d 代表参数为正数型
        if (number>100){
            result=number+"";
        }else {
            result = String.format("%0" + num + "d", number);
        }
        return result;
    }


    public static String join(String seperator, List<String> dataList){
        if (!CheckUtil.isEmpty(dataList)){
            StringBuilder stringBuilder = new StringBuilder();
            for (String item:dataList){
                stringBuilder.append(item+seperator);
            }
            if (stringBuilder.lastIndexOf(seperator) > 0) {
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(seperator));
            }
            return stringBuilder.toString();
        }
        return "";
    }

    /**
     * 去除开始和末尾的字符串
     * @param targetStr
     * @param c
     * @return
     */
    public static String trimStr(String targetStr, String c) {
        if (TextUtils.isEmpty(targetStr) || TextUtils.isEmpty(c)) {
            return targetStr;
        }

        //aaabcdef->a
        while (targetStr.length() > 0 && targetStr.startsWith(c)) {
            targetStr = targetStr.substring(c.length());
        }

        //abcdee->e
        while (targetStr.length() > 0 && targetStr.endsWith(c)) {
            targetStr = targetStr.substring(0, targetStr.length() - c.length());
        }

        return targetStr;
    }
}
