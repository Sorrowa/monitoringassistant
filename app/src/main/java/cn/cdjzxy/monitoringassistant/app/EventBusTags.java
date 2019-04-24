
package cn.cdjzxy.monitoringassistant.app;

/**
 * 放置EventBus tag
 */
public interface EventBusTags {

    String TAG_MODIFY_PWD = "MODIFY_PWD";//修改密码
    String TAG_PRECIPITATION_COLLECTION = "PRECIPITATION_COLLECTION";//降水样品收集
    String TAG_WASTEWATER_COLLECTION = "WATEWATER_COLLECTION";//废水样品收集
    String TAG_INSTRUMENTAL_RECORD = "INSTRUMENTAL_RECORD";//仪器法检测结果
    String TAG_WASTEWATER_BOTTLE = "WASTEWATER_BOTTLE";//废水分瓶
    String TAG_PROGRAM_MODIFY = "PROGRAM_MODIFY  ";//方案修改
    String TAG_TOKEN_EXPIRE = "TOKEN_EXPIRE  ";//token过期
    String TAG_SAMPLING_UPDATE = "SAMPLING_UPDATE  ";//表单数据更新
    String TAG_PROJECT_FINISH = "TAG_PROJECT_FINISH  ";//采样完结操作

    String TAG_NOISE_FRAGMENT_TYPE = "TAG_NOISE_FRAGMENT_TYPE";//噪声操作

}
