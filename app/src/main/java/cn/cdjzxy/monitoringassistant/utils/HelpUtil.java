package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.Sampling;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingDetail;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.SamplingDetailDao;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.DBHelper;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;


public class HelpUtil {
    private static final String TAG = "HelpUtil";

    private HelpUtil() {
    }

    /**
     * to get the status bar height
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            statusHeight = 80;
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * get width
     *
     * @param context
     * @return
     */
    public static int getWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * get height
     *
     * @param context
     * @return
     */
    public static int getHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 生成废水采样单code
     * @param sampling
     * @return
     */
    public static String createSamplingCode(Sampling sampling) {
        String samplingNo;
        String snDate = DateUtils.getDate().replace("-", "").substring(2);
        String snSampling = "";
        if (!CheckUtil.isEmpty(sampling.getSamplingNo())) {
            snSampling = sampling.getSamplingNo().substring(sampling.getSamplingNo().length()-2);
        }
        String snUserId = UserInfoHelper.get().getUser().getIntId() + "";

        int snFrequency = createFrequency(sampling);
        samplingNo = getYpxzCode(sampling.getTagName()) + snDate + "-" + snSampling + snUserId + "-" + StringUtil.autoGenericCode(snFrequency, 2);
        return samplingNo;
    }

    /**
     * 创建频次
     * @param sampling
     * @return
     */
    public static int createFrequency(Sampling sampling){
        int snFrequency = 1;
        List<SamplingDetail> samplingDetailResults = sampling.getSamplingDetailResults();
        if (samplingDetailResults != null && samplingDetailResults.size() > 0) {
            List<Integer> tempList=new ArrayList<>();
            for (SamplingDetail detail:samplingDetailResults){
                if (detail.getSamplingType()==0 || detail.getSamplingType()==2){
                    tempList.add(detail.getFrequecyNo());
                }
            }
            if (!CheckUtil.isEmpty(tempList)){
                snFrequency=Collections.max(tempList)+1;
            }
        }
        return snFrequency;
    }

    /**
     * 获取样品性质code
     * @return
     */
    public static String getYpxzCode(String name){
        if (CheckUtil.isEmpty(name)){
            return "";
        }
        String nameStr="";
        if (name.equals("地下水")){
            nameStr="DXS";
        }else if (name.equals("海水")){
            nameStr="HS";
        }else if (name.equals("废水")){
            nameStr="FS";
        }else if (name.equals("地表水")){
            nameStr="DBS";
        }
        return nameStr;
    }

}
