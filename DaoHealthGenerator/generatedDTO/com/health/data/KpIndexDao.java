package com.health.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.health.data.KpIndex;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table tblkpindex.
*/
public class KpIndexDao extends AbstractDao<KpIndex, Long> {

    public static final String TABLENAME = "tblkpindex";

    /**
     * Properties of entity KpIndex.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property IntValue = new Property(1, Integer.class, "IntValue", false, "intvalue");
        public final static Property StrValue = new Property(2, String.class, "StrValue", false, "strvalue");
    };


    public KpIndexDao(DaoConfig config) {
        super(config);
    }
    
    public KpIndexDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'tblkpindex' (" + //
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'intvalue' INTEGER," + // 1: IntValue
                "'strvalue' TEXT);"); // 2: StrValue
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'tblkpindex'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, KpIndex entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer IntValue = entity.getIntValue();
        if (IntValue != null) {
            stmt.bindLong(2, IntValue);
        }
 
        String StrValue = entity.getStrValue();
        if (StrValue != null) {
            stmt.bindString(3, StrValue);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public KpIndex readEntity(Cursor cursor, int offset) {
        KpIndex entity = new KpIndex( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // IntValue
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // StrValue
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, KpIndex entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIntValue(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setStrValue(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(KpIndex entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(KpIndex entity) {
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
