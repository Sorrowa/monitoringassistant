package cn.cdjzxy.monitoringassistant.mvp.model.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SAMPLING_FILE".
*/
public class SamplingFileDao extends AbstractDao<SamplingFile, String> {

    public static final String TABLENAME = "SAMPLING_FILE";

    /**
     * Properties of entity SamplingFile.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property LocalId = new Property(0, String.class, "LocalId", true, "LOCAL_ID");
        public final static Property Id = new Property(1, String.class, "Id", false, "ID");
        public final static Property SamplingId = new Property(2, String.class, "SamplingId", false, "SAMPLING_ID");
        public final static Property FileName = new Property(3, String.class, "FileName", false, "FILE_NAME");
        public final static Property FilePath = new Property(4, String.class, "FilePath", false, "FILE_PATH");
        public final static Property UpdateTime = new Property(5, String.class, "UpdateTime", false, "UPDATE_TIME");
        public final static Property IsUploaded = new Property(6, boolean.class, "IsUploaded", false, "IS_UPLOADED");
    }


    public SamplingFileDao(DaoConfig config) {
        super(config);
    }
    
    public SamplingFileDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SAMPLING_FILE\" (" + //
                "\"LOCAL_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: LocalId
                "\"ID\" TEXT," + // 1: Id
                "\"SAMPLING_ID\" TEXT," + // 2: SamplingId
                "\"FILE_NAME\" TEXT," + // 3: FileName
                "\"FILE_PATH\" TEXT," + // 4: FilePath
                "\"UPDATE_TIME\" TEXT," + // 5: UpdateTime
                "\"IS_UPLOADED\" INTEGER NOT NULL );"); // 6: IsUploaded
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SAMPLING_FILE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SamplingFile entity) {
        stmt.clearBindings();
 
        String LocalId = entity.getLocalId();
        if (LocalId != null) {
            stmt.bindString(1, LocalId);
        }
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(2, Id);
        }
 
        String SamplingId = entity.getSamplingId();
        if (SamplingId != null) {
            stmt.bindString(3, SamplingId);
        }
 
        String FileName = entity.getFileName();
        if (FileName != null) {
            stmt.bindString(4, FileName);
        }
 
        String FilePath = entity.getFilePath();
        if (FilePath != null) {
            stmt.bindString(5, FilePath);
        }
 
        String UpdateTime = entity.getUpdateTime();
        if (UpdateTime != null) {
            stmt.bindString(6, UpdateTime);
        }
        stmt.bindLong(7, entity.getIsUploaded() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SamplingFile entity) {
        stmt.clearBindings();
 
        String LocalId = entity.getLocalId();
        if (LocalId != null) {
            stmt.bindString(1, LocalId);
        }
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(2, Id);
        }
 
        String SamplingId = entity.getSamplingId();
        if (SamplingId != null) {
            stmt.bindString(3, SamplingId);
        }
 
        String FileName = entity.getFileName();
        if (FileName != null) {
            stmt.bindString(4, FileName);
        }
 
        String FilePath = entity.getFilePath();
        if (FilePath != null) {
            stmt.bindString(5, FilePath);
        }
 
        String UpdateTime = entity.getUpdateTime();
        if (UpdateTime != null) {
            stmt.bindString(6, UpdateTime);
        }
        stmt.bindLong(7, entity.getIsUploaded() ? 1L: 0L);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public SamplingFile readEntity(Cursor cursor, int offset) {
        SamplingFile entity = new SamplingFile( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // LocalId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // SamplingId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // FileName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // FilePath
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // UpdateTime
            cursor.getShort(offset + 6) != 0 // IsUploaded
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SamplingFile entity, int offset) {
        entity.setLocalId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSamplingId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFileName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFilePath(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setUpdateTime(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIsUploaded(cursor.getShort(offset + 6) != 0);
     }
    
    @Override
    protected final String updateKeyAfterInsert(SamplingFile entity, long rowId) {
        return entity.getLocalId();
    }
    
    @Override
    public String getKey(SamplingFile entity) {
        if(entity != null) {
            return entity.getLocalId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SamplingFile entity) {
        return entity.getLocalId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
