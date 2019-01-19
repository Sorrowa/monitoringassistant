package cn.cdjzxy.monitoringassistant.utils;

/**
 * 数字处理工具
 */
public class NumberUtil {

    /**
     * 数字舍入
     * 修约规则：小于0.5，保留1位有效数，大于0.5，保留整数；四舍六入，奇进偶退
     *
     * @param value
     * @return
     */
    public static double roundingNumber(double value) {
        if (value <= 0.5) {
            return fourHomesSixEntries(value, 1);
        } else {
            return fourHomesSixEntries(value, 0);
        }
    }

    /**
     * 四舍六入，奇进偶退
     *
     * @param value   原值
     * @param keepNum 保留小数位数
     * @return
     */
    public static double fourHomesSixEntries(double value, int keepNum) {
        int pow = 1;

        if (keepNum > 0) {
            //10的平方，保留数取整
            pow = (int) Math.pow(10, keepNum);
        }

        //10的平方，保留数+尾数取整
        double calcPow = Math.pow(10, keepNum + 1);
        //保留数取整
        int powValue = (int) Math.floor(value * pow);
        //保留数+尾数
        double calcPowDoubleValue = value * calcPow;
        //保留数+尾数取整
        int calcPowIntValue = (int) Math.floor(calcPowDoubleValue);

        //取到尾数，保留0位时，直接取尾数
        int endNum = calcPowIntValue;
        if (keepNum > 0) {
            //0.543 => 543 % (54 * 10) = 3
            endNum = calcPowIntValue % (powValue * 10);
        }

        if (endNum <= 4) {
            //尾数小于等于4时直接将尾数舍去
            return powValue / (double) pow;
        } else if (endNum >= 6) {
            //尾数大于等于6时尾数舍去并向前进1位
            return (powValue + 1) / (double) pow;
        }

        //当尾数为5而尾数后面还有任何不是0的数字时，都应向前进一位
        if (calcPowDoubleValue > calcPowIntValue) {
            return (powValue + 1) / (double) pow;
        }

        //当尾数为5而尾数后面的数字均为0时，应看前一位
        int lessValue = powValue % 10;
        if (lessValue % 2 != 0) {
            //若前一位数字为奇数则向前进一位，
            return (powValue + 1) / (double) pow;
        }

        //若前一位数字为偶数则将尾数舍去
        return powValue / (double) pow;
    }
}
