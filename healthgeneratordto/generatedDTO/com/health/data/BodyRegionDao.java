package com.health.data;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import com.health.data.BodyRegion;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table tblbodyregion.
*/
public class BodyRegionDao extends AbstractDao<BodyRegion, Long> {

    public static final String TABLENAME = "tblbodyregion";

    /**
     * Properties of entity BodyRegion.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property Name = new Property(1, String.class, "name", false, "name");
        public final static Property FullName = new Property(2, String.class, "FullName", false, "fullname");
        public final static Property BodyComplaintTypeId = new Property(3, Long.class, "BodyComplaintTypeId", false, "complaintid");
    };

    private DaoSession daoSession;


    public BodyRegionDao(DaoConfig config) {
        super(config);
    }
    
    public BodyRegionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'tblbodyregion' (" + //
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'name' TEXT," + // 1: name
                "'fullname' TEXT," + // 2: FullName
                "'complaintid' INTEGER);"); // 3: BodyComplaintTypeId
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_tblbodyregion_complaintid ON tblbodyregion" +
                " (complaintid);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'tblbodyregion'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BodyRegion entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String FullName = entity.getFullName();
        if (FullName != null) {
            stmt.bindString(3, FullName);
        }
 
        Long BodyComplaintTypeId = entity.getBodyComplaintTypeId();
        if (BodyComplaintTypeId != null) {
            stmt.bindLong(4, BodyComplaintTypeId);
        }
    }

    @Override
    protected void attachEntity(BodyRegion entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public BodyRegion readEntity(Cursor cursor, int offset) {
        BodyRegion entity = new BodyRegion( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // FullName
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3) // BodyComplaintTypeId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BodyRegion entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFullName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBodyComplaintTypeId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(BodyRegion entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(BodyRegion entity) {
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
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getBodyComplaintTypeDao().getAllColumns());
            builder.append(" FROM tblbodyregion T");
            builder.append(" LEFT JOIN tblbodycomplainttype T0 ON T.'complaintid'=T0.'id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected BodyRegion loadCurrentDeep(Cursor cursor, boolean lock) {
        BodyRegion entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        BodyComplaintType bodyComplaintType = loadCurrentOther(daoSession.getBodyComplaintTypeDao(), cursor, offset);
        entity.setBodyComplaintType(bodyComplaintType);

        return entity;    
    }

    public BodyRegion loadDeep(Long key) {
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
    public List<BodyRegion> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<BodyRegion> list = new ArrayList<BodyRegion>(count);
        
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
    
    protected List<BodyRegion> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<BodyRegion> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
