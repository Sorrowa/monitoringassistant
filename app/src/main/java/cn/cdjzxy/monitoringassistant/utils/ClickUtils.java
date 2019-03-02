package cn.cdjzxy.monitoringassistant.utils;

public class ClickUtils {
    private static long lastTime;
    private static final long INTERVAL_TIME_MS=3000;

    public static boolean canClick() {
        long interval=System.currentTimeMillis() - lastTime;
        if ( interval>= INTERVAL_TIME_MS) {
            lastTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
