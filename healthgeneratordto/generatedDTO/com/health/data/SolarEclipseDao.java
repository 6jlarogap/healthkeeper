package com.health.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.health.data.SolarEclipse;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table tblsolareclipse.
*/
public class SolarEclipseDao extends AbstractDao<SolarEclipse, Long> {

    public static final String TABLENAME = "tblsolareclipse";

    /**
     * Properties of entity SolarEclipse.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property ServerId = new Property(1, Long.class, "ServerId", false, "serverid");
        public final static Property RowId = new Property(2, String.class, "RowId", false, "rowid");
        public final static Property InfoDate = new Property(3, java.util.Date.class, "InfoDate", false, "maximum");
        public final static Property Phase = new Property(4, Integer.class, "Phase", false, "PHASE");
        public final static Property Magnitude = new Property(5, Double.class, "Magnitude", false, "MAGNITUDE");
        public final static Property Contact1 = new Property(6, java.util.Date.class, "Contact1", false, "CONTACT1");
        public final static Property Contact2 = new Property(7, java.util.Date.class, "Contact2", false, "CONTACT2");
        public final static Property Contact3 = new Property(8, java.util.Date.class, "Contact3", false, "CONTACT3");
        public final static Property Contact4 = new Property(9, java.util.Date.class, "Contact4", false, "CONTACT4");
    };


    public SolarEclipseDao(DaoConfig config) {
        super(config);
    }
    
    public SolarEclipseDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'tblsolareclipse' (" + //
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'serverid' INTEGER UNIQUE ," + // 1: ServerId
                "'rowid' TEXT UNIQUE ," + // 2: RowId
                "'maximum' INTEGER," + // 3: InfoDate
                "'PHASE' INTEGER," + // 4: Phase
                "'MAGNITUDE' REAL," + // 5: Magnitude
                "'CONTACT1' INTEGER," + // 6: Contact1
                "'CONTACT2' INTEGER," + // 7: Contact2
                "'CONTACT3' INTEGER," + // 8: Contact3
                "'CONTACT4' INTEGER);"); // 9: Contact4
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_tblsolareclipse_serverid ON tblsolareclipse" +
                " (serverid);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'tblsolareclipse'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SolarEclipse entity) {
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
 
        java.util.Date InfoDate = entity.getInfoDate();
        if (InfoDate != null) {
            stmt.bindLong(4, InfoDate.getTime());
        }
 
        Integer Phase = entity.getPhase();
        if (Phase != null) {
            stmt.bindLong(5, Phase);
        }
 
        Double Magnitude = entity.getMagnitude();
        if (Magnitude != null) {
            stmt.bindDouble(6, Magnitude);
        }
 
        java.util.Date Contact1 = entity.getContact1();
        if (Contact1 != null) {
            stmt.bindLong(7, Contact1.getTime());
        }
 
        java.util.Date Contact2 = entity.getContact2();
        if (Contact2 != null) {
            stmt.bindLong(8, Contact2.getTime());
        }
 
        java.util.Date Contact3 = entity.getContact3();
        if (Contact3 != null) {
            stmt.bindLong(9, Contact3.getTime());
        }
 
        java.util.Date Contact4 = entity.getContact4();
        if (Contact4 != null) {
            stmt.bindLong(10, Contact4.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SolarEclipse readEntity(Cursor cursor, int offset) {
        SolarEclipse entity = new SolarEclipse( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // ServerId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // RowId
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // InfoDate
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // Phase
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // Magnitude
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)), // Contact1
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)), // Contact2
            cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)), // Contact3
            cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)) // Contact4
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SolarEclipse entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setServerId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setRowId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setInfoDate(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setPhase(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setMagnitude(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setContact1(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
        entity.setContact2(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
        entity.setContact3(cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)));
        entity.setContact4(cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SolarEclipse entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SolarEclipse entity) {
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
