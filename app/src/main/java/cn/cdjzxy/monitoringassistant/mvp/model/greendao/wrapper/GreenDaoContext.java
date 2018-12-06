package cn.cdjzxy.monitoringassistant.mvp.model.greendao.wrapper;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.IOException;

import cn.cdjzxy.monitoringassistant.app.Constant;

/**
 * 自定义GreenDao数据库储存路径
 */
public class GreenDaoContext extends ContextWrapper {

    public GreenDaoContext(Context context) {
        super(context);
    }

    @Override
    public File getDatabasePath(String name) {
        boolean isFileCreateSuccess = false;// 数据库文件是否创建成功
        File dbFile = new File(Constant.DATABASE_DIR + "/" + name);
        if (!dbFile.exists()) {
            try {
                isFileCreateSuccess = dbFile.createNewFile();// 创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            isFileCreateSuccess = true;
        }

        if (isFileCreateSuccess) {// 返回数据库文件对象
            return dbFile;
        }
        return super.getDatabasePath(name);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
    }
}
