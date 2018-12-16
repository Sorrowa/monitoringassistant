package cn.cdjzxy.monitoringassistant.mvp.model.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingStantd;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SAMPLING_STANTD".
*/
public class SamplingStantdDao extends AbstractDao<SamplingStantd, String> {

    public static final String TABLENAME = "SAMPLING_STANTD";

    /**
     * Properties of entity SamplingStantd.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "Id", true, "ID");
        public final static Property Capacity = new Property(1, String.class, "Capacity", false, "CAPACITY");
        public final static Property Contaner = new Property(2, String.class, "Contaner", false, "CONTANER");
        public final static Property SaveDescription = new Property(3, String.class, "SaveDescription", false, "SAVE_DESCRIPTION");
        public final static Property IsSenceAnalysis = new Property(4, boolean.class, "IsSenceAnalysis", false, "IS_SENCE_ANALYSIS");
        public final static Property TagId = new Property(5, String.class, "TagId", false, "TAG_ID");
        public final static Property TagName = new Property(6, String.class, "TagName", false, "TAG_NAME");
        public final static Property MonItems = new Property(7, String.class, "MonItems", false, "MON_ITEMS");
    }


    public SamplingStantdDao(DaoConfig config) {
        super(config);
    }
    
    public SamplingStantdDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SAMPLING_STANTD\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: Id
                "\"CAPACITY\" TEXT," + // 1: Capacity
                "\"CONTANER\" TEXT," + // 2: Contaner
                "\"SAVE_DESCRIPTION\" TEXT," + // 3: SaveDescription
                "\"IS_SENCE_ANALYSIS\" INTEGER NOT NULL ," + // 4: IsSenceAnalysis
                "\"TAG_ID\" TEXT," + // 5: TagId
                "\"TAG_NAME\" TEXT," + // 6: TagName
                "\"MON_ITEMS\" TEXT);"); // 7: MonItems
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SAMPLING_STANTD\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SamplingStantd entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String Capacity = entity.getCapacity();
        if (Capacity != null) {
            stmt.bindString(2, Capacity);
        }
 
        String Contaner = entity.getContaner();
        if (Contaner != null) {
            stmt.bindString(3, Contaner);
        }
 
        String SaveDescription = entity.getSaveDescription();
        if (SaveDescription != null) {
            stmt.bindString(4, SaveDescription);
        }
        stmt.bindLong(5, entity.getIsSenceAnalysis() ? 1L: 0L);
 
        String TagId = entity.getTagId();
        if (TagId != null) {
            stmt.bindString(6, TagId);
        }
 
        String TagName = entity.getTagName();
        if (TagName != null) {
            stmt.bindString(7, TagName);
        }
 
        String MonItems = entity.getMonItems();
        if (MonItems != null) {
            stmt.bindString(8, MonItems);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SamplingStantd entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String Capacity = entity.getCapacity();
        if (Capacity != null) {
            stmt.bindString(2, Capacity);
        }
 
        String Contaner = entity.getContaner();
        if (Contaner != null) {
            stmt.bindString(3, Contaner);
        }
 
        String SaveDescription = entity.getSaveDescription();
        if (SaveDescription != null) {
            stmt.bindString(4, SaveDescription);
        }
        stmt.bindLong(5, entity.getIsSenceAnalysis() ? 1L: 0L);
 
        String TagId = entity.getTagId();
        if (TagId != null) {
            stmt.bindString(6, TagId);
        }
 
        String TagName = entity.getTagName();
        if (TagName != null) {
            stmt.bindString(7, TagName);
        }
 
        String MonItems = entity.getMonItems();
        if (MonItems != null) {
            stmt.bindString(8, MonItems);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public SamplingStantd readEntity(Cursor cursor, int offset) {
        SamplingStantd entity = new SamplingStantd( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Capacity
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Contaner
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // SaveDescription
            cursor.getShort(offset + 4) != 0, // IsSenceAnalysis
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // TagId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // TagName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // MonItems
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SamplingStantd entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setCapacity(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setContaner(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSaveDescription(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIsSenceAnalysis(cursor.getShort(offset + 4) != 0);
        entity.setTagId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setTagName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMonItems(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    @Override
    protected final String updateKeyAfterInsert(SamplingStantd entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(SamplingStantd entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SamplingStantd entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
