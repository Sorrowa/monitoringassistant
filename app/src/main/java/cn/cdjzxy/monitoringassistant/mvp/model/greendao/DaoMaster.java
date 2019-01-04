package cn.cdjzxy.monitoringassistant.mvp.model.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.database.StandardDatabase;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;


// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * Master of DAO (schema version 1): knows all DAOs.
 */
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 1;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(Database db, boolean ifNotExists) {
        MsgDao.createTable(db, ifNotExists);
        FormDao.createTable(db, ifNotExists);
        SamplingFormStandDao.createTable(db, ifNotExists);
        SamplingDao.createTable(db, ifNotExists);
        TableDao.createTable(db, ifNotExists);
        SamplingFileDao.createTable(db, ifNotExists);
        FormFlowDao.createTable(db, ifNotExists);
        FormSelectDao.createTable(db, ifNotExists);
        SamplingUserDao.createTable(db, ifNotExists);
        SamplingStantdDao.createTable(db, ifNotExists);
        SamplingDetailDao.createTable(db, ifNotExists);
        UserInfoDao.createTable(db, ifNotExists);
        ProjectDetialDao.createTable(db, ifNotExists);
        ProjectDao.createTable(db, ifNotExists);
        MonItemMethodRelationDao.createTable(db, ifNotExists);
        TagsDao.createTable(db, ifNotExists);
        MonItemsDao.createTable(db, ifNotExists);
        EnvirPointDao.createTable(db, ifNotExists);
        DevicesDao.createTable(db, ifNotExists);
        RightsDao.createTable(db, ifNotExists);
        WeatherDao.createTable(db, ifNotExists);
        UserDao.createTable(db, ifNotExists);
        EnterRelatePointDao.createTable(db, ifNotExists);
        MethodsDao.createTable(db, ifNotExists);
        MethodTagRelationDao.createTable(db, ifNotExists);
        DicDao.createTable(db, ifNotExists);
        MethodDevRelationDao.createTable(db, ifNotExists);
        EnterpriseDao.createTable(db, ifNotExists);
        MonItemTagRelationDao.createTable(db, ifNotExists);
    }

    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(Database db, boolean ifExists) {
        MsgDao.dropTable(db, ifExists);
        FormDao.dropTable(db, ifExists);
        SamplingFormStandDao.dropTable(db, ifExists);
        SamplingDao.dropTable(db, ifExists);
        TableDao.dropTable(db, ifExists);
        SamplingFileDao.dropTable(db, ifExists);
        FormFlowDao.dropTable(db, ifExists);
        FormSelectDao.dropTable(db, ifExists);
        SamplingUserDao.dropTable(db, ifExists);
        SamplingStantdDao.dropTable(db, ifExists);
        SamplingDetailDao.dropTable(db, ifExists);
        UserInfoDao.dropTable(db, ifExists);
        ProjectDetialDao.dropTable(db, ifExists);
        ProjectDao.dropTable(db, ifExists);
        MonItemMethodRelationDao.dropTable(db, ifExists);
        TagsDao.dropTable(db, ifExists);
        MonItemsDao.dropTable(db, ifExists);
        EnvirPointDao.dropTable(db, ifExists);
        DevicesDao.dropTable(db, ifExists);
        RightsDao.dropTable(db, ifExists);
        WeatherDao.dropTable(db, ifExists);
        UserDao.dropTable(db, ifExists);
        EnterRelatePointDao.dropTable(db, ifExists);
        MethodsDao.dropTable(db, ifExists);
        MethodTagRelationDao.dropTable(db, ifExists);
        DicDao.dropTable(db, ifExists);
        MethodDevRelationDao.dropTable(db, ifExists);
        EnterpriseDao.dropTable(db, ifExists);
        MonItemTagRelationDao.dropTable(db, ifExists);
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     * Convenience method using a {@link DevOpenHelper}.
     */
    public static DaoSession newDevSession(Context context, String name) {
        Database db = new DevOpenHelper(context, name).getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        return daoMaster.newSession();
    }

    public DaoMaster(SQLiteDatabase db) {
        this(new StandardDatabase(db));
    }

    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(MsgDao.class);
        registerDaoClass(FormDao.class);
        registerDaoClass(SamplingFormStandDao.class);
        registerDaoClass(SamplingDao.class);
        registerDaoClass(TableDao.class);
        registerDaoClass(SamplingFileDao.class);
        registerDaoClass(FormFlowDao.class);
        registerDaoClass(FormSelectDao.class);
        registerDaoClass(SamplingUserDao.class);
        registerDaoClass(SamplingStantdDao.class);
        registerDaoClass(SamplingDetailDao.class);
        registerDaoClass(UserInfoDao.class);
        registerDaoClass(ProjectDetialDao.class);
        registerDaoClass(ProjectDao.class);
        registerDaoClass(MonItemMethodRelationDao.class);
        registerDaoClass(TagsDao.class);
        registerDaoClass(MonItemsDao.class);
        registerDaoClass(EnvirPointDao.class);
        registerDaoClass(DevicesDao.class);
        registerDaoClass(RightsDao.class);
        registerDaoClass(WeatherDao.class);
        registerDaoClass(UserDao.class);
        registerDaoClass(EnterRelatePointDao.class);
        registerDaoClass(MethodsDao.class);
        registerDaoClass(MethodTagRelationDao.class);
        registerDaoClass(DicDao.class);
        registerDaoClass(MethodDevRelationDao.class);
        registerDaoClass(EnterpriseDao.class);
        registerDaoClass(MonItemTagRelationDao.class);
    }

    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    /**
     * Calls {@link #createAllTables(Database, boolean)} in {@link #onCreate(Database)} -
     */
    public static abstract class OpenHelper extends DatabaseOpenHelper {
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

}
