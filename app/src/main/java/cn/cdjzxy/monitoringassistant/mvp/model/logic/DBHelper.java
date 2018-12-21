package cn.cdjzxy.monitoringassistant.mvp.model.logic;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DaoMaster;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.DaoSession;
import cn.cdjzxy.monitoringassistant.mvp.model.greendao.wrapper.GreenDaoContext;

public class DBHelper {

    private static DaoSession sDaoSession;
    private static DaoMaster  sDaoMaster;
    private static DBHelper   sDBHelper;

    private DBHelper() {

    }

    /**
     * 初始化
     *
     * @param context
     * @param dbName
     */
    public static void init(Context context, String dbName) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(new GreenDaoContext(context), dbName, null);
        Database db = helper.getWritableDb();
        sDaoMaster = new DaoMaster(db);
        sDaoSession = sDaoMaster.newSession();
    }

    public static DBHelper getInstance() {
        return sDBHelper;
    }

    public static DaoSession get() {
        if (null == sDBHelper) {
            synchronized (UserInfoHelper.class) {
                if (null == sDBHelper) {
                    sDBHelper = new DBHelper();
                }
            }
        }
        return sDaoSession;
    }

    /**
     * 清空数据库
     */
    public void dropAllTables() {
        DaoMaster.dropAllTables(sDaoMaster.getDatabase(), true);
    }
}
