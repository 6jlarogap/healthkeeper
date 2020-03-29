package com.health.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.health.data.BaseUserSyncDTO;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table BASE_USER_SYNC_DTO.
*/
public class BaseUserSyncDTODao extends AbstractDao<BaseUserSyncDTO, Long> {

    public static final String TABLENAME = "BASE_USER_SYNC_DTO";

    /**
     * Properties of entity BaseUserSyncDTO.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property ServerId = new Property(1, Long.class, "ServerId", false, "serverid");
        public final static Property RowId = new Property(2, String.class, "RowId", false, "rowid");
        public final static Property UserId = new Property(3, Long.class, "UserId", false, "userid");
    };


    public BaseUserSyncDTODao(DaoConfig config) {
        super(config);
    }
    
    public BaseUserSyncDTODao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BASE_USER_SYNC_DTO' (" + //
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'serverid' INTEGER UNIQUE ," + // 1: ServerId
                "'rowid' TEXT UNIQUE ," + // 2: RowId
                "'userid' INTEGER);"); // 3: UserId
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_BASE_USER_SYNC_DTO_serverid ON BASE_USER_SYNC_DTO" +
                " (serverid);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_BASE_USER_SYNC_DTO_userid ON BASE_USER_SYNC_DTO" +
                " (userid);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BASE_USER_SYNC_DTO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BaseUserSyncDTO entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long ServerId = entity.getServerId();
        if (ServerId != null) {
            stmt.bindLong(2, ServerId);
        }
 
        String RowId = entity.getRowId();
        if (RowId != null) {
            stmt.bindString(3, RowId);
        }
 
        Long UserId = entity.getUserId();
        if (UserId != null) {
            stmt.bindLong(4, UserId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public BaseUserSyncDTO readEntity(Cursor cursor, int offset) {
        BaseUserSyncDTO entity = new BaseUserSyncDTO( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // ServerId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // RowId
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // UserId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BaseUserSyncDTO entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setServerId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setRowId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(BaseUserSyncDTO entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(BaseUserSyncDTO entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
