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

import com.health.data.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table tbluser.
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "tbluser";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "id");
        public final static Property IsActive = new Property(1, Integer.class, "IsActive", false, "IsActive");
        public final static Property IsStorePassword = new Property(2, Integer.class, "IsStorePassword", false, "IsStorePassword");
        public final static Property Login = new Property(3, String.class, "Login", false, "Login");
        public final static Property Password = new Property(4, String.class, "Password", false, "Password");
        public final static Property FName = new Property(5, String.class, "FName", false, "FName");
        public final static Property LName = new Property(6, String.class, "LName", false, "LName");
        public final static Property MName = new Property(7, String.class, "MName", false, "MName");
        public final static Property BirthDate = new Property(8, java.util.Date.class, "BirthDate", false, "BirthDate");
        public final static Property CreateDate = new Property(9, java.util.Date.class, "CreateDate", false, "CreateDate");
        public final static Property Sex = new Property(10, Integer.class, "Sex", false, "Sex");
        public final static Property IsAutoSync = new Property(11, Integer.class, "IsAutoSync", false, "IsAutoSync");
        public final static Property PeriodSyncData = new Property(12, Integer.class, "PeriodSyncData", false, "PeriodSyncData");
        public final static Property SyncDate = new Property(13, java.util.Date.class, "SyncDate", false, "SyncDate");
        public final static Property CityId = new Property(14, Long.class, "CityId", false, "cityid");
        public final static Property Question1 = new Property(15, Long.class, "Question1", false, "question1");
        public final static Property Question2 = new Property(16, Long.class, "Question2", false, "question2");
        public final static Property MaritalStatusId = new Property(17, Integer.class, "MaritalStatusId", false, "MaritalStatusId");
        public final static Property SocialStatusId = new Property(18, Integer.class, "SocialStatusId", false, "SocialStatusId");
        public final static Property Height = new Property(19, Integer.class, "Height", false, "Height");
        public final static Property Weight = new Property(20, Integer.class, "Weight", false, "Weight");
        public final static Property PressureId = new Property(21, Integer.class, "PressureId", false, "PressureId");
        public final static Property FootDistance = new Property(22, Integer.class, "FootDistance", false, "FootDistance");
        public final static Property SleepTime = new Property(23, Integer.class, "SleepTime", false, "SleepTime");
    };

    private DaoSession daoSession;


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'tbluser' (" + //
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'IsActive' INTEGER," + // 1: IsActive
                "'IsStorePassword' INTEGER," + // 2: IsStorePassword
                "'Login' TEXT," + // 3: Login
                "'Password' TEXT," + // 4: Password
                "'FName' TEXT," + // 5: FName
                "'LName' TEXT," + // 6: LName
                "'MName' TEXT," + // 7: MName
                "'BirthDate' INTEGER," + // 8: BirthDate
                "'CreateDate' INTEGER," + // 9: CreateDate
                "'Sex' INTEGER," + // 10: Sex
                "'IsAutoSync' INTEGER," + // 11: IsAutoSync
                "'PeriodSyncData' INTEGER," + // 12: PeriodSyncData
                "'SyncDate' INTEGER," + // 13: SyncDate
                "'cityid' INTEGER," + // 14: CityId
                "'question1' INTEGER," + // 15: Question1
                "'question2' INTEGER," + // 16: Question2
                "'MaritalStatusId' INTEGER," + // 17: MaritalStatusId
                "'SocialStatusId' INTEGER," + // 18: SocialStatusId
                "'Height' INTEGER," + // 19: Height
                "'Weight' INTEGER," + // 20: Weight
                "'PressureId' INTEGER," + // 21: PressureId
                "'FootDistance' INTEGER," + // 22: FootDistance
                "'SleepTime' INTEGER);"); // 23: SleepTime
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_tbluser_cityid ON tbluser" +
                " (cityid);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_tbluser_question1 ON tbluser" +
                " (question1);");
        db.execSQL("CREATE INDEX " + constraint + "IDX_tbluser_question2 ON tbluser" +
                " (question2);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'tbluser'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer IsActive = entity.getIsActive();
        if (IsActive != null) {
            stmt.bindLong(2, IsActive);
        }
 
        Integer IsStorePassword = entity.getIsStorePassword();
        if (IsStorePassword != null) {
            stmt.bindLong(3, IsStorePassword);
        }
 
        String Login = entity.getLogin();
        if (Login != null) {
            stmt.bindString(4, Login);
        }
 
        String Password = entity.getPassword();
        if (Password != null) {
            stmt.bindString(5, Password);
        }
 
        String FName = entity.getFName();
        if (FName != null) {
            stmt.bindString(6, FName);
        }
 
        String LName = entity.getLName();
        if (LName != null) {
            stmt.bindString(7, LName);
        }
 
        String MName = entity.getMName();
        if (MName != null) {
            stmt.bindString(8, MName);
        }
 
        java.util.Date BirthDate = entity.getBirthDate();
        if (BirthDate != null) {
            stmt.bindLong(9, BirthDate.getTime());
        }
 
        java.util.Date CreateDate = entity.getCreateDate();
        if (CreateDate != null) {
            stmt.bindLong(10, CreateDate.getTime());
        }
 
        Integer Sex = entity.getSex();
        if (Sex != null) {
            stmt.bindLong(11, Sex);
        }
 
        Integer IsAutoSync = entity.getIsAutoSync();
        if (IsAutoSync != null) {
            stmt.bindLong(12, IsAutoSync);
        }
 
        Integer PeriodSyncData = entity.getPeriodSyncData();
        if (PeriodSyncData != null) {
            stmt.bindLong(13, PeriodSyncData);
        }
 
        java.util.Date SyncDate = entity.getSyncDate();
        if (SyncDate != null) {
            stmt.bindLong(14, SyncDate.getTime());
        }
 
        Long CityId = entity.getCityId();
        if (CityId != null) {
            stmt.bindLong(15, CityId);
        }
 
        Long Question1 = entity.getQuestion1();
        if (Question1 != null) {
            stmt.bindLong(16, Question1);
        }
 
        Long Question2 = entity.getQuestion2();
        if (Question2 != null) {
            stmt.bindLong(17, Question2);
        }
 
        Integer MaritalStatusId = entity.getMaritalStatusId();
        if (MaritalStatusId != null) {
            stmt.bindLong(18, MaritalStatusId);
        }
 
        Integer SocialStatusId = entity.getSocialStatusId();
        if (SocialStatusId != null) {
            stmt.bindLong(19, SocialStatusId);
        }
 
        Integer Height = entity.getHeight();
        if (Height != null) {
            stmt.bindLong(20, Height);
        }
 
        Integer Weight = entity.getWeight();
        if (Weight != null) {
            stmt.bindLong(21, Weight);
        }
 
        Integer PressureId = entity.getPressureId();
        if (PressureId != null) {
            stmt.bindLong(22, PressureId);
        }
 
        Integer FootDistance = entity.getFootDistance();
        if (FootDistance != null) {
            stmt.bindLong(23, FootDistance);
        }
 
        Integer SleepTime = entity.getSleepTime();
        if (SleepTime != null) {
            stmt.bindLong(24, SleepTime);
        }
    }

    @Override
    protected void attachEntity(User entity) {
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
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // IsActive
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // IsStorePassword
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Login
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Password
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // FName
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // LName
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // MName
            cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)), // BirthDate
            cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)), // CreateDate
            cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10), // Sex
            cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11), // IsAutoSync
            cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12), // PeriodSyncData
            cursor.isNull(offset + 13) ? null : new java.util.Date(cursor.getLong(offset + 13)), // SyncDate
            cursor.isNull(offset + 14) ? null : cursor.getLong(offset + 14), // CityId
            cursor.isNull(offset + 15) ? null : cursor.getLong(offset + 15), // Question1
            cursor.isNull(offset + 16) ? null : cursor.getLong(offset + 16), // Question2
            cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17), // MaritalStatusId
            cursor.isNull(offset + 18) ? null : cursor.getInt(offset + 18), // SocialStatusId
            cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19), // Height
            cursor.isNull(offset + 20) ? null : cursor.getInt(offset + 20), // Weight
            cursor.isNull(offset + 21) ? null : cursor.getInt(offset + 21), // PressureId
            cursor.isNull(offset + 22) ? null : cursor.getInt(offset + 22), // FootDistance
            cursor.isNull(offset + 23) ? null : cursor.getInt(offset + 23) // SleepTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIsActive(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setIsStorePassword(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setLogin(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setPassword(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFName(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setLName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMName(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setBirthDate(cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)));
        entity.setCreateDate(cursor.isNull(offset + 9) ? null : new java.util.Date(cursor.getLong(offset + 9)));
        entity.setSex(cursor.isNull(offset + 10) ? null : cursor.getInt(offset + 10));
        entity.setIsAutoSync(cursor.isNull(offset + 11) ? null : cursor.getInt(offset + 11));
        entity.setPeriodSyncData(cursor.isNull(offset + 12) ? null : cursor.getInt(offset + 12));
        entity.setSyncDate(cursor.isNull(offset + 13) ? null : new java.util.Date(cursor.getLong(offset + 13)));
        entity.setCityId(cursor.isNull(offset + 14) ? null : cursor.getLong(offset + 14));
        entity.setQuestion1(cursor.isNull(offset + 15) ? null : cursor.getLong(offset + 15));
        entity.setQuestion2(cursor.isNull(offset + 16) ? null : cursor.getLong(offset + 16));
        entity.setMaritalStatusId(cursor.isNull(offset + 17) ? null : cursor.getInt(offset + 17));
        entity.setSocialStatusId(cursor.isNull(offset + 18) ? null : cursor.getInt(offset + 18));
        entity.setHeight(cursor.isNull(offset + 19) ? null : cursor.getInt(offset + 19));
        entity.setWeight(cursor.isNull(offset + 20) ? null : cursor.getInt(offset + 20));
        entity.setPressureId(cursor.isNull(offset + 21) ? null : cursor.getInt(offset + 21));
        entity.setFootDistance(cursor.isNull(offset + 22) ? null : cursor.getInt(offset + 22));
        entity.setSleepTime(cursor.isNull(offset + 23) ? null : cursor.getInt(offset + 23));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
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
            SqlUtils.appendColumns(builder, "T0", daoSession.getCityDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getRecoveryAccountQuestionDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getRecoveryAccountQuestionDao().getAllColumns());
            builder.append(" FROM tbluser T");
            builder.append(" LEFT JOIN tblcity T0 ON T.'cityid'=T0.'id'");
            builder.append(" LEFT JOIN tblrecoveryaccount_question T1 ON T.'question1'=T1.'id'");
            builder.append(" LEFT JOIN tblrecoveryaccount_question T2 ON T.'question2'=T2.'id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected User loadCurrentDeep(Cursor cursor, boolean lock) {
        User entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        City city = loadCurrentOther(daoSession.getCityDao(), cursor, offset);
        entity.setCity(city);
        offset += daoSession.getCityDao().getAllColumns().length;

        RecoveryAccountQuestion ReqoveryQuestion1 = loadCurrentOther(daoSession.getRecoveryAccountQuestionDao(), cursor, offset);
        entity.setReqoveryQuestion1(ReqoveryQuestion1);
        offset += daoSession.getRecoveryAccountQuestionDao().getAllColumns().length;

        RecoveryAccountQuestion ReqoveryQuestion2 = loadCurrentOther(daoSession.getRecoveryAccountQuestionDao(), cursor, offset);
        entity.setReqoveryQuestion2(ReqoveryQuestion2);

        return entity;    
    }

    public User loadDeep(Long key) {
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
    public List<User> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<User> list = new ArrayList<User>(count);
        
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
    
    protected List<User> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<User> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}