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

import com.health.data.CommonFeeling;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table tblcommonfeeling.
*/
public class CommonFeelingDao extends AbstractDao<CommonFeeling, Long> {

    public static final String TABLENAME = "tblcommonfeeling";

    /**
     * Properties of entity CommonFeeling.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property ServerId = new Property(1, Long.class, "ServerId", false, "serverid");
        public final static Property RowId = new Property(2, String.class, "RowId", false, "rowid");
        public final static Property UserId = new Property(3, Long.class, "UserId", false, "userid");
        public final static Property StartDate = new Property(4, java.util.Date.class, "StartDate", false, "dt");
        public final static Property Value1 = new Property(5, Double.class, "Value1", false, "value1");
        public final static Property Value2 = new Property(6, Double.class, "Value2", false, "value2");
        public final static Property Value3 = new Property(7, Double.class, "Value3", false, "value3");
        public final static Property CommonFeelingTypeId = new Property(8, Long.class, "CommonFeelingTypeId", false, "feelingtypeid");
        public final static Property CustomCommonFeelingTypeId = new Property(9, Long.class, "CustomCommonFeelingTypeId", false, "customfeelingtypeid");
    };

    private DaoSession daoSession;


    public CommonFeelingDao(DaoConfig config) {
        super(config);
    }
    
    public CommonFeelingDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'tblcommonfeeling' (" + //
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'serverid' INTEGER UNIQUE ," + // 1: ServerId
                "'rowid' TEXT UNIQUE ," + // 2: RowId
                "'userid' INTEGER," + // 3: UserId
                "'dt' INTEGER," + // 4: StartDate
                "'value1' REAL," + // 5: Value1
                "'value2' REAL," + // 6: Value2
                "'value3' REAL," + // 7: Value3
                "'feelingtypeid' INTEGER," + // 8: CommonFeelingTypeId
                "'customfeelingtypeid' INTEGER);"); // 9: CustomCommonFeelingTypeId
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_tblcommonfeeling_serverid ON tblcommonfeeling" +
                " (serverid);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_tblcommonfeeling_userid ON tblcommonfeeling" +
                " (userid);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_tblcommonfeeling_feelingtypeid ON tblcommonfeeling" +
                " (feelingtypeid);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_tblcommonfeeling_customfeelingtypeid ON tblcommonfeeling" +
                " (customfeelingtypeid);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'tblcommonfeeling'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CommonFeeling entity) {
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
 
        java.util.Date StartDate = entity.getStartDate();
        if (StartDate != null) {
            stmt.bindLong(5, StartDate.getTime());
        }
 
        Double Value1 = entity.getValue1();
        if (Value1 != null) {
            stmt.bindDouble(6, Value1);
        }
 
        Double Value2 = entity.getValue2();
        if (Value2 != null) {
            stmt.bindDouble(7, Value2);
        }
 
        Double Value3 = entity.getValue3();
        if (Value3 != null) {
            stmt.bindDouble(8, Value3);
        }
 
        Long CommonFeelingTypeId = entity.getCommonFeelingTypeId();
        if (CommonFeelingTypeId != null) {
            stmt.bindLong(9, CommonFeelingTypeId);
        }
 
        Long CustomCommonFeelingTypeId = entity.getCustomCommonFeelingTypeId();
        if (CustomCommonFeelingTypeId != null) {
            stmt.bindLong(10, CustomCommonFeelingTypeId);
        }
    }

    @Override
    protected void attachEntity(CommonFeeling entity) {
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
    public CommonFeeling readEntity(Cursor cursor, int offset) {
        CommonFeeling entity = new CommonFeeling( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // ServerId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // RowId
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // UserId
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // StartDate
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // Value1
            cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6), // Value2
            cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7), // Value3
            cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8), // CommonFeelingTypeId
            cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9) // CustomCommonFeelingTypeId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CommonFeeling entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setServerId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setRowId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserId(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setStartDate(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setValue1(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setValue2(cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6));
        entity.setValue3(cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7));
        entity.setCommonFeelingTypeId(cursor.isNull(offset + 8) ? null : cursor.getLong(offset + 8));
        entity.setCustomCommonFeelingTypeId(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CommonFeeling entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CommonFeeling entity) {
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
            SqlUtils.appendColumns(builder, "T0", daoSession.getCommonFeelingTypeDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getCustomCommonFeelingTypeDao().getAllColumns());
            builder.append(" FROM tblcommonfeeling T");
            builder.append(" LEFT JOIN tblcommonfeelingtype T0 ON T.'feelingtypeid'=T0.'id'");
            builder.append(" LEFT JOIN tblcustomcommonfeelingtype T1 ON T.'customfeelingtypeid'=T1.'id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected CommonFeeling loadCurrentDeep(Cursor cursor, boolean lock) {
        CommonFeeling entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        CommonFeelingType commonFeelingType = loadCurrentOther(daoSession.getCommonFeelingTypeDao(), cursor, offset);
        entity.setCommonFeelingType(commonFeelingType);
        offset += daoSession.getCommonFeelingTypeDao().getAllColumns().length;

        CustomCommonFeelingType customCommonFeelingType = loadCurrentOther(daoSession.getCustomCommonFeelingTypeDao(), cursor, offset);
        entity.setCustomCommonFeelingType(customCommonFeelingType);

        return entity;    
    }

    public CommonFeeling loadDeep(Long key) {
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
    public List<CommonFeeling> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<CommonFeeling> list = new ArrayList<CommonFeeling>(count);
        
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
    
    protected List<CommonFeeling> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<CommonFeeling> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
