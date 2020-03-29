package com.health.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.health.data.Weather;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table tblweatherhourly.
*/
public class WeatherDao extends AbstractDao<Weather, Long> {

    public static final String TABLENAME = "tblweatherhourly";

    /**
     * Properties of entity Weather.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property ServerId = new Property(1, Long.class, "ServerId", false, "serverid");
        public final static Property RowId = new Property(2, String.class, "RowId", false, "rowid");
        public final static Property InfoDate = new Property(3, java.util.Date.class, "InfoDate", false, "dt");
        public final static Property Temperature = new Property(4, Double.class, "Temperature", false, "TEMPERATURE");
        public final static Property MinTemperature = new Property(5, Double.class, "MinTemperature", false, "MIN_TEMPERATURE");
        public final static Property MaxTemperature = new Property(6, Double.class, "MaxTemperature", false, "MAX_TEMPERATURE");
        public final static Property Pressure = new Property(7, Double.class, "Pressure", false, "PRESSURE");
        public final static Property Humidity = new Property(8, Double.class, "Humidity", false, "HUMIDITY");
        public final static Property WindSpeed = new Property(9, Double.class, "WindSpeed", false, "WIND_SPEED");
        public final static Property WindDeg = new Property(10, Double.class, "WindDeg", false, "WIND_DEG");
        public final static Property Clouds = new Property(11, Double.class, "Clouds", false, "CLOUDS");
        public final static Property Rain = new Property(12, Double.class, "Rain", false, "RAIN");
        public final static Property Snow = new Property(13, Double.class, "Snow", false, "SNOW");
    };


    public WeatherDao(DaoConfig config) {
        super(config);
    }
    
    public WeatherDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'tblweatherhourly' (" + //
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'serverid' INTEGER UNIQUE ," + // 1: ServerId
                "'rowid' TEXT UNIQUE ," + // 2: RowId
                "'dt' INTEGER," + // 3: InfoDate
                "'TEMPERATURE' REAL," + // 4: Temperature
                "'MIN_TEMPERATURE' REAL," + // 5: MinTemperature
                "'MAX_TEMPERATURE' REAL," + // 6: MaxTemperature
                "'PRESSURE' REAL," + // 7: Pressure
                "'HUMIDITY' REAL," + // 8: Humidity
                "'WIND_SPEED' REAL," + // 9: WindSpeed
                "'WIND_DEG' REAL," + // 10: WindDeg
                "'CLOUDS' REAL," + // 11: Clouds
                "'RAIN' REAL," + // 12: Rain
                "'SNOW' REAL);"); // 13: Snow
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_tblweatherhourly_serverid ON tblweatherhourly" +
                " (serverid);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'tblweatherhourly'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Weather entity) {
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
 
        Double Temperature = entity.getTemperature();
        if (Temperature != null) {
            stmt.bindDouble(5, Temperature);
        }
 
        Double MinTemperature = entity.getMinTemperature();
        if (MinTemperature != null) {
            stmt.bindDouble(6, MinTemperature);
        }
 
        Double MaxTemperature = entity.getMaxTemperature();
        if (MaxTemperature != null) {
            stmt.bindDouble(7, MaxTemperature);
        }
 
        Double Pressure = entity.getPressure();
        if (Pressure != null) {
            stmt.bindDouble(8, Pressure);
        }
 
        Double Humidity = entity.getHumidity();
        if (Humidity != null) {
            stmt.bindDouble(9, Humidity);
        }
 
        Double WindSpeed = entity.getWindSpeed();
        if (WindSpeed != null) {
            stmt.bindDouble(10, WindSpeed);
        }
 
        Double WindDeg = entity.getWindDeg();
        if (WindDeg != null) {
            stmt.bindDouble(11, WindDeg);
        }
 
        Double Clouds = entity.getClouds();
        if (Clouds != null) {
            stmt.bindDouble(12, Clouds);
        }
 
        Double Rain = entity.getRain();
        if (Rain != null) {
            stmt.bindDouble(13, Rain);
        }
 
        Double Snow = entity.getSnow();
        if (Snow != null) {
            stmt.bindDouble(14, Snow);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Weather readEntity(Cursor cursor, int offset) {
        Weather entity = new Weather( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // ServerId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // RowId
            cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)), // InfoDate
            cursor.isNull(offset + 4) ? null : cursor.getDouble(offset + 4), // Temperature
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // MinTemperature
            cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6), // MaxTemperature
            cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7), // Pressure
            cursor.isNull(offset + 8) ? null : cursor.getDouble(offset + 8), // Humidity
            cursor.isNull(offset + 9) ? null : cursor.getDouble(offset + 9), // WindSpeed
            cursor.isNull(offset + 10) ? null : cursor.getDouble(offset + 10), // WindDeg
            cursor.isNull(offset + 11) ? null : cursor.getDouble(offset + 11), // Clouds
            cursor.isNull(offset + 12) ? null : cursor.getDouble(offset + 12), // Rain
            cursor.isNull(offset + 13) ? null : cursor.getDouble(offset + 13) // Snow
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Weather entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setServerId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setRowId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setInfoDate(cursor.isNull(offset + 3) ? null : new java.util.Date(cursor.getLong(offset + 3)));
        entity.setTemperature(cursor.isNull(offset + 4) ? null : cursor.getDouble(offset + 4));
        entity.setMinTemperature(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setMaxTemperature(cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6));
        entity.setPressure(cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7));
        entity.setHumidity(cursor.isNull(offset + 8) ? null : cursor.getDouble(offset + 8));
        entity.setWindSpeed(cursor.isNull(offset + 9) ? null : cursor.getDouble(offset + 9));
        entity.setWindDeg(cursor.isNull(offset + 10) ? null : cursor.getDouble(offset + 10));
        entity.setClouds(cursor.isNull(offset + 11) ? null : cursor.getDouble(offset + 11));
        entity.setRain(cursor.isNull(offset + 12) ? null : cursor.getDouble(offset + 12));
        entity.setSnow(cursor.isNull(offset + 13) ? null : cursor.getDouble(offset + 13));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Weather entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Weather entity) {
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
