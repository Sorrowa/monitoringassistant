package cn.cdjzxy.monitoringassistant.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * Created by LM on 2018/1/18.
 */

public class DateUtils {
    private static final String TAG = "DateUtils";

    public static Calendar getLastDateInMonth(Calendar source, Calendar target) {
        Calendar resultCalendar = new GregorianCalendar(target.get(Calendar.YEAR), target.get(Calendar.MONTH), 1);
        resultCalendar.add(Calendar.MONTH, 1);
        resultCalendar.add(Calendar.DATE, -1);

        if (resultCalendar.get(Calendar.DAY_OF_MONTH) <= source.get(Calendar.DAY_OF_MONTH)) {
            return resultCalendar;
        }
        return source;
    }

    //判断结束时间大于起始时间
    public static Boolean compareTime(String startTime, String endTime) throws ParseException {
        long longstr1 = Long.valueOf(startTime.replaceAll("[-\\s:]", ""));
        long longstr2 = Long.valueOf(endTime.replaceAll("[-\\s:]", ""));
        if (longstr1 - longstr2 < 0) {
            return true;
        } else {
            return false;
        }
    }

    //获取剩余时间
    public static int getLastDays(String startTime, String endTime) {
        int time1 = Integer.valueOf(startTime.replaceAll("[-\\s:]", ""));
        int time2 = Integer.valueOf(endTime.replaceAll("[-\\s:]", ""));
        return time2 - time1;

    }


    public static String getLastMouthDay() {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        //        start_time.setText(dateFormater.format(cal.getTime()) + "");

        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormater.format(cal.getTime());
    }


    public static String getLastMouth() {
        SimpleDateFormat dateFormater = new SimpleDateFormat(
                "yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();
        //        start_time.setText(dateFormater.format(cal.getTime()) + "");

        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormater.format(cal.getTime());
    }


    /**
     * 将时间戳改为日期加时间
     */
    public static String stampToDateAndTime(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳改为日期
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * calendar 转换为yyyy-MM-dd
     */
    public static String calendarToDate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String stampToDate(long time) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳改为月份
     */
    public static String stampToMonth(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 将时间戳改当天的时分
     */
    public static String stampToTime(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    public static long getTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return sdf.parse(time).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getTimeShort(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date date = new Date();
            date.setTime(time);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTimeInPoint(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            date.setTime(time);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTime(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            date.setTime(time);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getHour(long time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            date.setTime(time);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTimeOfEnglish(long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy HH:mm", Locale.ENGLISH);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    public static String getTimeOfEnglishWhthoutHour(long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    public static String getTimeDay(long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("d", Locale.ENGLISH);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    public static String getTimeMonth(long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM", Locale.ENGLISH);
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    public static String getTimeHour(long millSec) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date date = new Date();
            date.setTime(millSec);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatTime(long s) {

        long minute = s / 60;
        long second = s % 60;
        if (minute >= 60) {
            long hour = minute / 60;
            minute = minute % 60;
            return formatString((int) hour) + ":" + formatString((int) minute) + ":" + formatString((int) second);
        } else {
            return formatString((int) minute) + ":" + formatString((int) second);
        }
    }

    public static String formatRecordsTime(long s) {
        if (s < 60) {
            return "less 1 min";
        }
        long minute = s / 60;
        long second = s % 60;
        if (minute >= 60) {
            long hour = minute / 60;
            minute = minute % 60;
            return formatString((int) hour) + " hour " + formatString((int) minute) + " min ";
        } else {
            return formatString((int) minute) + " min";
        }
    }

    /**
     * 讲不足两位的前面补0
     *
     * @param source
     * @return
     */
    public static String formatString(int source) {
        return String.format("%02d", source);
    }

    public static Bitmap.CompressFormat getCompressFormat(String path) {
        if (path.endsWith("img_xiala.png")) {
            return Bitmap.CompressFormat.PNG;
        } else {
            return Bitmap.CompressFormat.JPEG;
        }
    }

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        String regExp = "^[0-9]*$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String balance(String rmb) {
        return String.format("¥ %.2f", rmb);
    }

    public static Map<String, String> parseData(Object data, Type typeOfSrc) {
        Gson gson = new Gson();
        String json = gson.toJson(data, typeOfSrc);
        Log.i(TAG, "parseData: " + json);
        Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {

        }.getType());
        for (int i = 0; i < map.size(); i++) {
            Log.i(TAG, "parseData: 键   值 : " + map.keySet() + "  " + map.values());
        }
        return map;
    }

    public static Map<String, String> parseDataNoZero(Object data, Type typeOfSrc) {
        Gson gson = new Gson();
        String json = gson.toJson(data, typeOfSrc);

        Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());

        Iterator<String> iter = map.keySet().iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            if ("0".equals(map.get(key))) {
                //   map.remove(key);  // java.util.ConcurrentModificationException
                iter.remove();
            }
        }
        Log.i(TAG, "parseData: " + json);
        for (int i = 0; i < map.size(); i++) {
            Log.i(TAG, "parseData: 键   值 : " + map.keySet() + "  " + map.values());
        }
        return map;
    }

    public static int getAge(Date dateOfBirth) {
        int age = 0;
        Calendar born = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        if (dateOfBirth != null) {
            now.setTime(new Date());
            born.setTime(dateOfBirth);
            if (born.after(now)) {
                throw new IllegalArgumentException("年龄不能超过当前日期");
            }
            age = now.get(Calendar.YEAR) - born.get(Calendar.YEAR);
            int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
            int bornDayOfYear = born.get(Calendar.DAY_OF_YEAR);
            if (nowDayOfYear < bornDayOfYear) {
                age -= 1;
            }
        }
        return age;
    }


    /**
     * 获取当前日期是星期几<br>
     *
     * @return 当前日期是星期几
     */
    public static String getWeek() {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    /**
     * 获取当前年月日
     *
     * @return 当前年月日
     */
    public static String getDate(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
    }

    public static String getYears(Date date) {
        return new SimpleDateFormat("yyyy-MM").format(date).toString();
    }

    /**
     * 获取当前年月日
     *
     * @return 当前年月日
     */
    public static String getWholeDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()).toString();
    }

    /**
     * 返回时间
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    /**
     * 返回时间
     *
     * @param date
     * @return
     */
    public static String getTimeNoMinute(Date date) {
        return new SimpleDateFormat("HH:mm").format(date);
    }

    /**
     * 获取当前年月日
     *
     * @return 当前年月日
     */
    public static String getDate() {
        return getDate(new Date());
    }


    /**
     * @param interval 间隔
     * @return
     */
    public static String getDate(int interval) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        //        rightNow.add(Calendar.YEAR, -1);//日期减1年
        //        rightNow.add(Calendar.MONTH, 3);//日期加3个月
        rightNow.add(Calendar.DAY_OF_YEAR, interval);//日期加10天
        Date dt1 = rightNow.getTime();
        String reStr = sdf.format(dt1);
        return reStr;
    }

    public static String getWholeDateStr() {
        return new SimpleDateFormat("yyyyMMddHHMMss").format(new Date()).toString();
    }

}
