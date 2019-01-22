package cn.cdjzxy.monitoringassistant.utils;

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
}
