package cn.cdjzxy.monitoringassistant.mvp.model.greendao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.MethodDevRelation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "METHOD_DEV_RELATION".
*/
public class MethodDevRelationDao extends AbstractDao<MethodDevRelation, String> {

    public static final String TABLENAME = "METHOD_DEV_RELATION";

    /**
     * Properties of entity MethodDevRelation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "Id", true, "ID");
        public final static Property MethodId = new Property(1, String.class, "MethodId", false, "METHOD_ID");
        public final static Property DevId = new Property(2, String.class, "DevId", false, "DEV_ID");
    }

    private DaoSession daoSession;


    public MethodDevRelationDao(DaoConfig config) {
        super(config);
    }
    
    public MethodDevRelationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"METHOD_DEV_RELATION\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: Id
                "\"METHOD_ID\" TEXT," + // 1: MethodId
                "\"DEV_ID\" TEXT);"); // 2: DevId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"METHOD_DEV_RELATION\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MethodDevRelation entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String MethodId = entity.getMethodId();
        if (MethodId != null) {
            stmt.bindString(2, MethodId);
        }
 
        String DevId = entity.getDevId();
        if (DevId != null) {
            stmt.bindString(3, DevId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MethodDevRelation entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String MethodId = entity.getMethodId();
        if (MethodId != null) {
            stmt.bindString(2, MethodId);
        }
 
        String DevId = entity.getDevId();
        if (DevId != null) {
            stmt.bindString(3, DevId);
        }
    }

    @Override
    protected final void attachEntity(MethodDevRelation entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public MethodDevRelation readEntity(Cursor cursor, int offset) {
        MethodDevRelation entity = new MethodDevRelation( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // MethodId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // DevId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MethodDevRelation entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setMethodId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDevId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final String updateKeyAfterInsert(MethodDevRelation entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(MethodDevRelation entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MethodDevRelation entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getMethodsDao().getAllColumns());
            builder.append(" FROM METHOD_DEV_RELATION T");
            builder.append(" LEFT JOIN METHODS T0 ON T.\"METHOD_ID\"=T0.\"ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected MethodDevRelation loadCurrentDeep(Cursor cursor, boolean lock) {
        MethodDevRelation entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Methods mMethods = loadCurrentOther(daoSession.getMethodsDao(), cursor, offset);
        entity.setMMethods(mMethods);

        return entity;    
    }

    public MethodDevRelation loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<MethodDevRelation> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<MethodDevRelation> list = new ArrayList<MethodDevRelation>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<MethodDevRelation> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<MethodDevRelation> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
