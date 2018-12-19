package cn.cdjzxy.monitoringassistant.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SamplingUtil {

    /**
     * 获取当前年月日
     *
     * @return 当前年月日
     */
    public static String getNumberForSampling() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date()).toString();
    }
}
