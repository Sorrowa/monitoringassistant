package cn.cdjzxy.monitoringassistant.mvp.model.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.cdjzxy.monitoringassistant.mvp.model.greendao.converter.StringConverter;
import java.util.List;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.project.Project;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PROJECT".
*/
public class ProjectDao extends AbstractDao<Project, String> {

    public static final String TABLENAME = "PROJECT";

    /**
     * Properties of entity Project.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "Id", true, "ID");
        public final static Property UpdateTime = new Property(1, String.class, "UpdateTime", false, "UPDATE_TIME");
        public final static Property Name = new Property(2, String.class, "Name", false, "NAME");
        public final static Property ProjectNo = new Property(3, String.class, "ProjectNo", false, "PROJECT_NO");
        public final static Property Urgency = new Property(4, String.class, "Urgency", false, "URGENCY");
        public final static Property ContractCode = new Property(5, String.class, "ContractCode", false, "CONTRACT_CODE");
        public final static Property Type = new Property(6, String.class, "Type", false, "TYPE");
        public final static Property TypeCode = new Property(7, int.class, "TypeCode", false, "TYPE_CODE");
        public final static Property MonType = new Property(8, String.class, "MonType", false, "MON_TYPE");
        public final static Property ClientName = new Property(9, String.class, "ClientName", false, "CLIENT_NAME");
        public final static Property ClientId = new Property(10, String.class, "ClientId", false, "CLIENT_ID");
        public final static Property CreaterId = new Property(11, String.class, "CreaterId", false, "CREATER_ID");
        public final static Property CreaterName = new Property(12, String.class, "CreaterName", false, "CREATER_NAME");
        public final static Property RcvId = new Property(13, String.class, "RcvId", false, "RCV_ID");
        public final static Property RcvName = new Property(14, String.class, "RcvName", false, "RCV_NAME");
        public final static Property StartDate = new Property(15, String.class, "StartDate", false, "START_DATE");
        public final static Property EndDate = new Property(16, String.class, "EndDate", false, "END_DATE");
        public final static Property CurrentNodeType = new Property(17, String.class, "CurrentNodeType", false, "CURRENT_NODE_TYPE");
        public final static Property Status = new Property(18, String.class, "Status", false, "STATUS");
        public final static Property AssignDate = new Property(19, String.class, "AssignDate", false, "ASSIGN_DATE");
        public final static Property CreateDate = new Property(20, String.class, "CreateDate", false, "CREATE_DATE");
        public final static Property FinishState = new Property(21, boolean.class, "FinishState", false, "FINISH_STATE");
        public final static Property FinishDate = new Property(22, String.class, "FinishDate", false, "FINISH_DATE");
        public final static Property PlanBeginTime = new Property(23, String.class, "PlanBeginTime", false, "PLAN_BEGIN_TIME");
        public final static Property PlanEndTime = new Property(24, String.class, "PlanEndTime", false, "PLAN_END_TIME");
        public final static Property CanSamplingEidt = new Property(25, boolean.class, "CanSamplingEidt", false, "CAN_SAMPLING_EIDT");
        public final static Property IsSamplingEidt = new Property(26, boolean.class, "isSamplingEidt", false, "IS_SAMPLING_EIDT");
        public final static Property SamplingUser = new Property(27, String.class, "SamplingUser", false, "SAMPLING_USER");
    }

    private final StringConverter SamplingUserConverter = new StringConverter();

    public ProjectDao(DaoConfig config) {
        super(config);
    }
    
    public ProjectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PROJECT\" (" + //
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: Id
                "\"UPDATE_TIME\" TEXT," + // 1: UpdateTime
                "\"NAME\" TEXT," + // 2: Name
                "\"PROJECT_NO\" TEXT," + // 3: ProjectNo
                "\"URGENCY\" TEXT," + // 4: Urgency
                "\"CONTRACT_CODE\" TEXT," + // 5: ContractCode
                "\"TYPE\" TEXT," + // 6: Type
                "\"TYPE_CODE\" INTEGER NOT NULL ," + // 7: TypeCode
                "\"MON_TYPE\" TEXT," + // 8: MonType
                "\"CLIENT_NAME\" TEXT," + // 9: ClientName
                "\"CLIENT_ID\" TEXT," + // 10: ClientId
                "\"CREATER_ID\" TEXT," + // 11: CreaterId
                "\"CREATER_NAME\" TEXT," + // 12: CreaterName
                "\"RCV_ID\" TEXT," + // 13: RcvId
                "\"RCV_NAME\" TEXT," + // 14: RcvName
                "\"START_DATE\" TEXT," + // 15: StartDate
                "\"END_DATE\" TEXT," + // 16: EndDate
                "\"CURRENT_NODE_TYPE\" TEXT," + // 17: CurrentNodeType
                "\"STATUS\" TEXT," + // 18: Status
                "\"ASSIGN_DATE\" TEXT," + // 19: AssignDate
                "\"CREATE_DATE\" TEXT," + // 20: CreateDate
                "\"FINISH_STATE\" INTEGER NOT NULL ," + // 21: FinishState
                "\"FINISH_DATE\" TEXT," + // 22: FinishDate
                "\"PLAN_BEGIN_TIME\" TEXT," + // 23: PlanBeginTime
                "\"PLAN_END_TIME\" TEXT," + // 24: PlanEndTime
                "\"CAN_SAMPLING_EIDT\" INTEGER NOT NULL ," + // 25: CanSamplingEidt
                "\"IS_SAMPLING_EIDT\" INTEGER NOT NULL ," + // 26: isSamplingEidt
                "\"SAMPLING_USER\" TEXT);"); // 27: SamplingUser
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PROJECT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Project entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String UpdateTime = entity.getUpdateTime();
        if (UpdateTime != null) {
            stmt.bindString(2, UpdateTime);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(3, Name);
        }
 
        String ProjectNo = entity.getProjectNo();
        if (ProjectNo != null) {
            stmt.bindString(4, ProjectNo);
        }
 
        String Urgency = entity.getUrgency();
        if (Urgency != null) {
            stmt.bindString(5, Urgency);
        }
 
        String ContractCode = entity.getContractCode();
        if (ContractCode != null) {
            stmt.bindString(6, ContractCode);
        }
 
        String Type = entity.getType();
        if (Type != null) {
            stmt.bindString(7, Type);
        }
        stmt.bindLong(8, entity.getTypeCode());
 
        String MonType = entity.getMonType();
        if (MonType != null) {
            stmt.bindString(9, MonType);
        }
 
        String ClientName = entity.getClientName();
        if (ClientName != null) {
            stmt.bindString(10, ClientName);
        }
 
        String ClientId = entity.getClientId();
        if (ClientId != null) {
            stmt.bindString(11, ClientId);
        }
 
        String CreaterId = entity.getCreaterId();
        if (CreaterId != null) {
            stmt.bindString(12, CreaterId);
        }
 
        String CreaterName = entity.getCreaterName();
        if (CreaterName != null) {
            stmt.bindString(13, CreaterName);
        }
 
        String RcvId = entity.getRcvId();
        if (RcvId != null) {
            stmt.bindString(14, RcvId);
        }
 
        String RcvName = entity.getRcvName();
        if (RcvName != null) {
            stmt.bindString(15, RcvName);
        }
 
        String StartDate = entity.getStartDate();
        if (StartDate != null) {
            stmt.bindString(16, StartDate);
        }
 
        String EndDate = entity.getEndDate();
        if (EndDate != null) {
            stmt.bindString(17, EndDate);
        }
 
        String CurrentNodeType = entity.getCurrentNodeType();
        if (CurrentNodeType != null) {
            stmt.bindString(18, CurrentNodeType);
        }
 
        String Status = entity.getStatus();
        if (Status != null) {
            stmt.bindString(19, Status);
        }
 
        String AssignDate = entity.getAssignDate();
        if (AssignDate != null) {
            stmt.bindString(20, AssignDate);
        }
 
        String CreateDate = entity.getCreateDate();
        if (CreateDate != null) {
            stmt.bindString(21, CreateDate);
        }
        stmt.bindLong(22, entity.getFinishState() ? 1L: 0L);
 
        String FinishDate = entity.getFinishDate();
        if (FinishDate != null) {
            stmt.bindString(23, FinishDate);
        }
 
        String PlanBeginTime = entity.getPlanBeginTime();
        if (PlanBeginTime != null) {
            stmt.bindString(24, PlanBeginTime);
        }
 
        String PlanEndTime = entity.getPlanEndTime();
        if (PlanEndTime != null) {
            stmt.bindString(25, PlanEndTime);
        }
        stmt.bindLong(26, entity.getCanSamplingEidt() ? 1L: 0L);
        stmt.bindLong(27, entity.getIsSamplingEidt() ? 1L: 0L);
 
        List SamplingUser = entity.getSamplingUser();
        if (SamplingUser != null) {
            stmt.bindString(28, SamplingUserConverter.convertToDatabaseValue(SamplingUser));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Project entity) {
        stmt.clearBindings();
 
        String Id = entity.getId();
        if (Id != null) {
            stmt.bindString(1, Id);
        }
 
        String UpdateTime = entity.getUpdateTime();
        if (UpdateTime != null) {
            stmt.bindString(2, UpdateTime);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(3, Name);
        }
 
        String ProjectNo = entity.getProjectNo();
        if (ProjectNo != null) {
            stmt.bindString(4, ProjectNo);
        }
 
        String Urgency = entity.getUrgency();
        if (Urgency != null) {
            stmt.bindString(5, Urgency);
        }
 
        String ContractCode = entity.getContractCode();
        if (ContractCode != null) {
            stmt.bindString(6, ContractCode);
        }
 
        String Type = entity.getType();
        if (Type != null) {
            stmt.bindString(7, Type);
        }
        stmt.bindLong(8, entity.getTypeCode());
 
        String MonType = entity.getMonType();
        if (MonType != null) {
            stmt.bindString(9, MonType);
        }
 
        String ClientName = entity.getClientName();
        if (ClientName != null) {
            stmt.bindString(10, ClientName);
        }
 
        String ClientId = entity.getClientId();
        if (ClientId != null) {
            stmt.bindString(11, ClientId);
        }
 
        String CreaterId = entity.getCreaterId();
        if (CreaterId != null) {
            stmt.bindString(12, CreaterId);
        }
 
        String CreaterName = entity.getCreaterName();
        if (CreaterName != null) {
            stmt.bindString(13, CreaterName);
        }
 
        String RcvId = entity.getRcvId();
        if (RcvId != null) {
            stmt.bindString(14, RcvId);
        }
 
        String RcvName = entity.getRcvName();
        if (RcvName != null) {
            stmt.bindString(15, RcvName);
        }
 
        String StartDate = entity.getStartDate();
        if (StartDate != null) {
            stmt.bindString(16, StartDate);
        }
 
        String EndDate = entity.getEndDate();
        if (EndDate != null) {
            stmt.bindString(17, EndDate);
        }
 
        String CurrentNodeType = entity.getCurrentNodeType();
        if (CurrentNodeType != null) {
            stmt.bindString(18, CurrentNodeType);
        }
 
        String Status = entity.getStatus();
        if (Status != null) {
            stmt.bindString(19, Status);
        }
 
        String AssignDate = entity.getAssignDate();
        if (AssignDate != null) {
            stmt.bindString(20, AssignDate);
        }
 
        String CreateDate = entity.getCreateDate();
        if (CreateDate != null) {
            stmt.bindString(21, CreateDate);
        }
        stmt.bindLong(22, entity.getFinishState() ? 1L: 0L);
 
        String FinishDate = entity.getFinishDate();
        if (FinishDate != null) {
            stmt.bindString(23, FinishDate);
        }
 
        String PlanBeginTime = entity.getPlanBeginTime();
        if (PlanBeginTime != null) {
            stmt.bindString(24, PlanBeginTime);
        }
 
        String PlanEndTime = entity.getPlanEndTime();
        if (PlanEndTime != null) {
            stmt.bindString(25, PlanEndTime);
        }
        stmt.bindLong(26, entity.getCanSamplingEidt() ? 1L: 0L);
        stmt.bindLong(27, entity.getIsSamplingEidt() ? 1L: 0L);
 
        List SamplingUser = entity.getSamplingUser();
        if (SamplingUser != null) {
            stmt.bindString(28, SamplingUserConverter.convertToDatabaseValue(SamplingUser));
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public Project readEntity(Cursor cursor, int offset) {
        Project entity = new Project( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // Id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // UpdateTime
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ProjectNo
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Urgency
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // ContractCode
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // Type
            cursor.getInt(offset + 7), // TypeCode
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // MonType
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // ClientName
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // ClientId
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // CreaterId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // CreaterName
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // RcvId
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // RcvName
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // StartDate
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // EndDate
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // CurrentNodeType
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // Status
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // AssignDate
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // CreateDate
            cursor.getShort(offset + 21) != 0, // FinishState
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // FinishDate
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // PlanBeginTime
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // PlanEndTime
            cursor.getShort(offset + 25) != 0, // CanSamplingEidt
            cursor.getShort(offset + 26) != 0, // isSamplingEidt
            cursor.isNull(offset + 27) ? null : SamplingUserConverter.convertToEntityProperty(cursor.getString(offset + 27)) // SamplingUser
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Project entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUpdateTime(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setProjectNo(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUrgency(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setContractCode(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setType(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setTypeCode(cursor.getInt(offset + 7));
        entity.setMonType(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setClientName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setClientId(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCreaterId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setCreaterName(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setRcvId(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setRcvName(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setStartDate(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setEndDate(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setCurrentNodeType(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setStatus(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setAssignDate(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setCreateDate(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setFinishState(cursor.getShort(offset + 21) != 0);
        entity.setFinishDate(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setPlanBeginTime(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setPlanEndTime(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setCanSamplingEidt(cursor.getShort(offset + 25) != 0);
        entity.setIsSamplingEidt(cursor.getShort(offset + 26) != 0);
        entity.setSamplingUser(cursor.isNull(offset + 27) ? null : SamplingUserConverter.convertToEntityProperty(cursor.getString(offset + 27)));
     }
    
    @Override
    protected final String updateKeyAfterInsert(Project entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public String getKey(Project entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Project entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}