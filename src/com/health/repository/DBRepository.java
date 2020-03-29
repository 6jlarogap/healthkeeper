package com.health.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.health.data.BaseDTO;
import com.health.data.BodyComplaintType;
import com.health.data.BodyFeeling;
import com.health.data.BodyFeelingDao;
import com.health.data.BodyFeelingType;
import com.health.data.BodyFeelingTypeDao;
import com.health.data.BodyRegion;
import com.health.data.BodyRegion_BodyFeelingTypeDao;
import com.health.data.CommonFeeling;
import com.health.data.CommonFeelingDao;
import com.health.data.CommonFeelingGroup;
import com.health.data.CommonFeelingType;
import com.health.data.CommonFeelingTypeDao;
import com.health.data.Complaint;
import com.health.data.ComplaintDao;
import com.health.data.CustomBodyFeelingType;
import com.health.data.CustomBodyFeelingTypeDao;
import com.health.data.CustomCommonFeelingType;
import com.health.data.CustomCommonFeelingTypeDao;
import com.health.data.CustomFactorType;
import com.health.data.CustomFactorTypeDao;
import com.health.data.DaoSession;
import com.health.data.Factor;
import com.health.data.FactorDao;
import com.health.data.FactorGroup;
import com.health.data.FactorGroupDao;
import com.health.data.FactorType;
import com.health.data.FactorTypeDao;
import com.health.data.GeoPhysics;
import com.health.data.GeoPhysicsDao;
import com.health.data.HelioPhysics;
import com.health.data.HelioPhysicsDao;
import com.health.data.IFeeling;
import com.health.data.IFeelingTypeInfo;
import com.health.data.IGridGroup;
import com.health.data.KpIndex;
import com.health.data.KpIndexDao;
import com.health.data.Moon;
import com.health.data.MoonDao;
import com.health.data.MoonPhase;
import com.health.data.MoonPhaseDao;
import com.health.data.OperationUserData;
import com.health.data.Particle;
import com.health.data.ParticleDao;
import com.health.data.Planet;
import com.health.data.PlanetDao;
import com.health.data.SolarEclipse;
import com.health.data.SolarEclipseDao;
import com.health.data.Sun;
import com.health.data.SunDao;
import com.health.data.User;
import com.health.data.UserBodyFeelingType;
import com.health.data.UserBodyFeelingTypeDao;
import com.health.data.Weather;
import com.health.data.WeatherDaily;
import com.health.data.WeatherDailyDao;
import com.health.data.WeatherDao;
import com.health.db.DB;
import com.health.db.UserDB;
import com.health.viewmodel.BodyFeelingTypeInfo;
import com.health.viewmodel.CommonFeelingTypeInfo;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;

public class DBRepository implements IRepository {

	public static final long PERIOD_3H_IN_MS = 3 * 60 * 60 * 1000L;
	public static final long PERIOD_6H_IN_MS = 6 * 60 * 60 * 1000L;
	public static final long PERIOD_1D_IN_MS = 1 * 24 * 60 * 60 * 1000L;
	public static final long PERIOD_2D_IN_MS = 2 * 24 * 60 * 60 * 1000L;
	public static final long PERIOD_4D_IN_MS = 4 * 24 * 60 * 60 * 1000L;
	public static final long PERIOD_6D_IN_MS = 6 * 24 * 60 * 60 * 1000L;

	public static final int OFFSET_TO_UTC_IN_MS = TimeZone.getDefault().getOffset(new Date().getTime());

	public static final String RAW_LIMIT_SQL = "(%s + " + Integer.toString(OFFSET_TO_UTC_IN_MS) + ") %% %d = 0";

	@Override
	public User getCurrentUser() {
		return UserDB.getCurrentUser();
	}
	
	private static final Pattern CLEAR_SPACE_PATTERN = Pattern.compile("[\\s]+");
	
	@Override
    public CustomBodyFeelingType addCustomBodyFeelingType(CustomBodyFeelingType customBodyFeelingType) {
		User user = getCurrentUser();
		customBodyFeelingType.setUserId(user.getId());
		customBodyFeelingType.setName(CLEAR_SPACE_PATTERN.matcher(customBodyFeelingType.getName()).replaceAll(" ").trim());
		customBodyFeelingType.setRowId(UUID.randomUUID().toString());
		CustomBodyFeelingType dbCustomBodyFeelingType = null;
		DaoSession daoSession = DB.db().newSession();
		List<CustomBodyFeelingType> dbList = daoSession.getCustomBodyFeelingTypeDao().queryBuilder().where(CustomBodyFeelingTypeDao.Properties.UserId.eq(customBodyFeelingType.getUserId()), CustomBodyFeelingTypeDao.Properties.Name.eq(customBodyFeelingType.getName())).limit(1).list();
		if(dbList.size() > 0){
			dbCustomBodyFeelingType = dbList.get(0);
		}
		if(dbCustomBodyFeelingType != null){
			return dbCustomBodyFeelingType;
		} else {			
			daoSession.getCustomBodyFeelingTypeDao().insert(customBodyFeelingType);			
			OperationUserData.updateOperationUserData(OperationUserData.CUSTOMBODYFEELINGTYPE_TABLEID, customBodyFeelingType.getId(), customBodyFeelingType.getServerId(), UUID.fromString(customBodyFeelingType.getRowId()), OperationUserData.INSERT_OPERATION_TYPE);
			return customBodyFeelingType;
		}
	    
    }
	
	@Override
	public CustomBodyFeelingType getCustomBodyFeelingType(String customBodyFeelingTypeName) {
		DaoSession daoSession = DB.db().newSession();
		User user = getCurrentUser();		
		customBodyFeelingTypeName = CLEAR_SPACE_PATTERN.matcher(customBodyFeelingTypeName).replaceAll(" ").trim();
		CustomBodyFeelingType dbCustomBodyFeelingType = null;
		List<CustomBodyFeelingType> dbList = daoSession.getCustomBodyFeelingTypeDao().queryBuilder().where(CustomBodyFeelingTypeDao.Properties.UserId.eq(user.getId()), CustomBodyFeelingTypeDao.Properties.Name.eq(customBodyFeelingTypeName)).list();
		if(dbList.size() > 0){
			dbCustomBodyFeelingType = dbList.get(0);
		}
		return dbCustomBodyFeelingType;
    }

	@Override
	public void addBodyFeeling(BodyFeeling bodyFeeling) {
		DaoSession daoSession = DB.db().newSession();
		User user = getCurrentUser();
		bodyFeeling.setUserId(user.getId());
		bodyFeeling.setRowId(UUID.randomUUID().toString());
		daoSession.getBodyFeelingDao().insert(bodyFeeling);		
		OperationUserData.updateOperationUserData(OperationUserData.BODYFEELING_TABLEID, bodyFeeling.getId(), bodyFeeling.getServerId(), UUID.fromString(bodyFeeling.getRowId()), OperationUserData.INSERT_OPERATION_TYPE);
	}

	@Override
	public void updateBodyFeeling(BodyFeeling bodyFeeling) {
		DaoSession daoSession = DB.db().newSession();
		BodyFeeling dbBodyFeeling = daoSession.getBodyFeelingDao().load(bodyFeeling.getId());
		bodyFeeling.setId(dbBodyFeeling.getId());
		bodyFeeling.setServerId(dbBodyFeeling.getServerId());
		bodyFeeling.setRowId(dbBodyFeeling.getRowId());
		bodyFeeling.setUserId(dbBodyFeeling.getUserId());
		daoSession.getBodyFeelingDao().update(bodyFeeling);
		OperationUserData.updateOperationUserData(OperationUserData.BODYFEELING_TABLEID, bodyFeeling.getId(), bodyFeeling.getServerId(), UUID.fromString(bodyFeeling.getRowId()), OperationUserData.UPDATE_OPERATION_TYPE);
	}

	@Override
	public void deleteBodyFeeling(BodyFeeling bodyFeeling) {
		DaoSession daoSession = DB.db().newSession();		
		BodyFeeling dbBodyFeeling = daoSession.getBodyFeelingDao().load(bodyFeeling.getId());
		OperationUserData.updateOperationUserData(OperationUserData.BODYFEELING_TABLEID, dbBodyFeeling.getId(), dbBodyFeeling.getServerId(), UUID.fromString(dbBodyFeeling.getRowId()), OperationUserData.DELETE_OPERATION_TYPE);
		daoSession.getBodyFeelingDao().delete(dbBodyFeeling);
	}

	@Override
	public BodyFeeling getBodyFeelingById(Context context, long id) {
		DaoSession daoSession = DB.db().newSession();
		BodyFeeling bodyFeeling = daoSession.getBodyFeelingDao().load(id);
		if (bodyFeeling != null) {
			bodyFeeling.getBodyFeelingType();
			bodyFeeling.getBodyRegion();
			bodyFeeling.getCustomBodyFeelingType();			
		}
		return bodyFeeling;
	}
	
	@Override
    public void addCommonFeeling(CommonFeeling commonFeeling) {
		DaoSession daoSession = DB.db().newSession();
		User user = getCurrentUser();
		commonFeeling.setUserId(user.getId());
		commonFeeling.setRowId(UUID.randomUUID().toString());
		daoSession.getCommonFeelingDao().insert(commonFeeling);
		OperationUserData.updateOperationUserData(OperationUserData.COMMONFEELING_TABLEID, commonFeeling.getId(), commonFeeling.getServerId(), UUID.fromString(commonFeeling.getRowId()), OperationUserData.INSERT_OPERATION_TYPE);
	    
    }

	@Override
    public void updateCommonFeeling(CommonFeeling commonFeeling) {
		DaoSession daoSession = DB.db().newSession();
		CommonFeeling dbCommonFeeling = daoSession.getCommonFeelingDao().load(commonFeeling.getId());
		commonFeeling.setId(dbCommonFeeling.getId());
		commonFeeling.setServerId(dbCommonFeeling.getServerId());
		commonFeeling.setRowId(dbCommonFeeling.getRowId());
		commonFeeling.setUserId(dbCommonFeeling.getUserId());
		daoSession.getCommonFeelingDao().update(commonFeeling);
		OperationUserData.updateOperationUserData(OperationUserData.COMMONFEELING_TABLEID, commonFeeling.getId(), commonFeeling.getServerId(), UUID.fromString(commonFeeling.getRowId()), OperationUserData.UPDATE_OPERATION_TYPE);	    
    }

	@Override
    public void deleteCommonFeeling(CommonFeeling commonFeeling) {
		DaoSession daoSession = DB.db().newSession();
		CommonFeeling dbCommonFeeling = daoSession.getCommonFeelingDao().load(commonFeeling.getId());
		OperationUserData.updateOperationUserData(OperationUserData.COMMONFEELING_TABLEID, dbCommonFeeling.getId(), dbCommonFeeling.getServerId(), UUID.fromString(dbCommonFeeling.getRowId()), OperationUserData.DELETE_OPERATION_TYPE);
		daoSession.getCommonFeelingDao().delete(dbCommonFeeling);	    
    }

	@Override
    public CommonFeeling getCommonFeelingById(Context context, long id) {
		DaoSession daoSession = DB.db().newSession();
		CommonFeeling commonFeeling = daoSession.getCommonFeelingDao().load(id);		
		if (commonFeeling != null) {
			commonFeeling.getCommonFeelingType();		
		}
		return commonFeeling;
    }

	private long getPeriodHourlyDataInMS(DATA_AMOUNT_TYPE dataAmountType){
		long result = 1;
		switch (dataAmountType){
			case ALL:
				break;
			case HOURLY_3H:
				result = PERIOD_3H_IN_MS;
				break;
			case HOURLY_6H:
				result = PERIOD_6H_IN_MS;
				break;
			case DAILY:
				result = PERIOD_1D_IN_MS;
				break;
			case DAILY_2D:
				result = PERIOD_2D_IN_MS;
				break;
			case DAILY_4D:
				result = PERIOD_4D_IN_MS;
				break;
			case DAILY_6D:
				result = PERIOD_6D_IN_MS;
				break;
		}
		return result;
	}

	private long getPeriodDailyDataInMS(DATA_AMOUNT_TYPE dataAmountType){
		long result = 1;
		switch (dataAmountType){
			case ALL:
				break;
			case HOURLY_3H:
				break;
			case HOURLY_6H:
				break;
			case DAILY:
				result = PERIOD_1D_IN_MS;
				break;
			case DAILY_2D:
				result = PERIOD_1D_IN_MS;
				break;
			case DAILY_4D:
				result = PERIOD_2D_IN_MS;
				break;
			case DAILY_6D:
				result = PERIOD_2D_IN_MS;
				break;
		}
		return result;
	}

	@Override
	public List<Weather> getWeatherList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		DaoSession daoSession = DB.db().newSession();
		String periodLimitSql = String.format(RAW_LIMIT_SQL, WeatherDao.Properties.InfoDate.columnName, getPeriodHourlyDataInMS(amountData));
		List<Weather> result = daoSession.getWeatherDao().queryBuilder().where(WeatherDao.Properties.InfoDate.ge(dtFrom), WeatherDao.Properties.InfoDate.le(dtTo), new WhereCondition.StringCondition(periodLimitSql)).orderAsc(WeatherDao.Properties.InfoDate).list();
		if(result.size() == 0){
			onLoadData(dtFrom, dtTo);
		} else {
			long period  = getPeriodHourlyDataInMS(amountData);
			if(Math.abs(result.get(0).getInfoDate().getTime() - dtFrom.getTime()) > period){
				onLoadData(dtFrom, dtTo);
			}
		}
		return result;
	}
		

	
	@Override
	public List<WeatherDaily> getWeatherDailyList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		DaoSession daoSession = DB.db().newSession();
		String periodLimitSql = String.format(RAW_LIMIT_SQL, WeatherDailyDao.Properties.InfoDate.columnName, getPeriodDailyDataInMS(amountData));
		List<WeatherDaily> result = daoSession.getWeatherDailyDao().queryBuilder().where(WeatherDailyDao.Properties.InfoDate.ge(dtFrom), WeatherDailyDao.Properties.InfoDate.le(dtTo), new WhereCondition.StringCondition(periodLimitSql)).orderAsc(WeatherDailyDao.Properties.InfoDate).list();
		if(result.size() == 0){
			onLoadData(dtFrom, dtTo);
		} else {
			long period  = getPeriodDailyDataInMS(amountData);
			if(Math.abs(result.get(0).getInfoDate().getTime() - dtFrom.getTime()) > period){
				onLoadData(dtFrom, dtTo);
			}
		}
		return result;
	}
		

		
	@Override
	public List<Sun> getSunList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		DaoSession daoSession = DB.db().newSession();
		String periodLimitSql = String.format(RAW_LIMIT_SQL, SunDao.Properties.InfoDate.columnName, getPeriodDailyDataInMS(amountData));
		List<Sun> result = daoSession.getSunDao().queryBuilder().where(SunDao.Properties.InfoDate.ge(dtFrom), SunDao.Properties.InfoDate.le(dtTo), new WhereCondition.StringCondition(periodLimitSql)).orderAsc(SunDao.Properties.InfoDate).list();
		return result;
	}
	


	@Override
	public List<SolarEclipse> getSolarEclipseList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		DaoSession daoSession = DB.db().newSession();
		String periodLimitSql = String.format(RAW_LIMIT_SQL, SolarEclipseDao.Properties.InfoDate.columnName, getPeriodDailyDataInMS(amountData));
		List<SolarEclipse> result = daoSession.getSolarEclipseDao().queryBuilder().where(SolarEclipseDao.Properties.InfoDate.ge(dtFrom), SolarEclipseDao.Properties.InfoDate.le(dtTo), new WhereCondition.StringCondition(periodLimitSql)).orderAsc(SolarEclipseDao.Properties.InfoDate).list();
		return result;
	}
	
	@Override
	public List<Moon> getMoonList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		DaoSession daoSession = DB.db().newSession();
		String periodLimitSql = String.format(RAW_LIMIT_SQL, MoonDao.Properties.InfoDate.columnName, getPeriodDailyDataInMS(amountData));
		List<Moon> result = daoSession.getMoonDao().queryBuilder().where(MoonDao.Properties.InfoDate.ge(dtFrom), MoonDao.Properties.InfoDate.le(dtTo), new WhereCondition.StringCondition(periodLimitSql)).orderAsc(MoonDao.Properties.InfoDate).list();
		return result;
	}
	


	@Override
	public List<MoonPhase> getMoonPhaseList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		DaoSession daoSession = DB.db().newSession();
		String periodLimitSql = String.format(RAW_LIMIT_SQL, MoonPhaseDao.Properties.InfoDate.columnName, getPeriodDailyDataInMS(amountData));
		List<MoonPhase> result = daoSession.getMoonPhaseDao().queryBuilder().where(MoonPhaseDao.Properties.InfoDate.ge(dtFrom), MoonPhaseDao.Properties.InfoDate.le(dtTo), new WhereCondition.StringCondition(periodLimitSql)).orderAsc(MoonPhaseDao.Properties.InfoDate).list();
		return result;
	}

	@Override
	public List<GeoPhysics> getGeoPhysicsList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		DaoSession daoSession = DB.db().newSession();
		String periodLimitSql = String.format(RAW_LIMIT_SQL, GeoPhysicsDao.Properties.InfoDate.columnName, getPeriodDailyDataInMS(amountData));
		List<GeoPhysics> result = daoSession.getGeoPhysicsDao().queryBuilder().where(GeoPhysicsDao.Properties.InfoDate.ge(dtFrom), GeoPhysicsDao.Properties.InfoDate.le(dtTo), new WhereCondition.StringCondition(periodLimitSql)).orderAsc(GeoPhysicsDao.Properties.InfoDate).list();
		return result;
	}

	@Override
	public List<HelioPhysics> getHelioPhysicsList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		DaoSession daoSession = DB.db().newSession();
		String periodLimitSql = String.format(RAW_LIMIT_SQL, HelioPhysicsDao.Properties.InfoDate.columnName, getPeriodDailyDataInMS(amountData));
		List<HelioPhysics> result = daoSession.getHelioPhysicsDao().queryBuilder().where(HelioPhysicsDao.Properties.InfoDate.ge(dtFrom), HelioPhysicsDao.Properties.InfoDate.le(dtTo), new WhereCondition.StringCondition(periodLimitSql)).orderAsc(HelioPhysicsDao.Properties.InfoDate).list();
		return result;
	}

	@Override
	public List<Particle> getParticleList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		DaoSession daoSession = DB.db().newSession();
		String periodLimitSql = String.format(RAW_LIMIT_SQL, ParticleDao.Properties.InfoDate.columnName, getPeriodDailyDataInMS(amountData));
		List<Particle> result = daoSession.getParticleDao().queryBuilder().where(ParticleDao.Properties.InfoDate.ge(dtFrom), ParticleDao.Properties.InfoDate.le(dtTo), new WhereCondition.StringCondition(periodLimitSql)).orderAsc(ParticleDao.Properties.InfoDate).list();
		return result;
	}
	

	@Override
	public List<Planet> getPlanetList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		DaoSession daoSession = DB.db().newSession();
		String periodLimitSql = String.format(RAW_LIMIT_SQL, PlanetDao.Properties.InfoDate.columnName, getPeriodDailyDataInMS(amountData));
		List<Planet> result = daoSession.getPlanetDao().queryBuilder().where(PlanetDao.Properties.InfoDate.ge(dtFrom), PlanetDao.Properties.InfoDate.le(dtTo), new WhereCondition.StringCondition(periodLimitSql)).orderAsc(PlanetDao.Properties.InfoDate).list();
		return result;
	}

	@Override
	public List<UserBodyFeelingType> getUserBodyFeelingTypes(String filterBodyFeelingTypeName) {		
		String selectString = String.format("SELECT t.Id, t.name, u.BodyFeelingType_id, u.Color, userid, RowId, ServerId, u.Id  FROM tblBodyFeelingType t left join tblUserBodyFeelingType u on t.Id =  u.BodyFeelingType_id where name like '%%%s%%' order by t.name;",
		                filterBodyFeelingTypeName != null ? filterBodyFeelingTypeName : "");
		Cursor cursor = DB.db().getDatabase().rawQuery(selectString, null);
		List<UserBodyFeelingType> result = new ArrayList<UserBodyFeelingType>();
		while (cursor.moveToNext()) {
			UserBodyFeelingType u = new UserBodyFeelingType();
			BodyFeelingType bodyFeelingType = new BodyFeelingType();
			bodyFeelingType.setId(cursor.getLong(0));
			bodyFeelingType.setName(cursor.getString(1));
			u.setBodyFeelingTypeId(bodyFeelingType.getId());			
			if (!TextUtils.isEmpty(cursor.getString(2))) {
				u.setColor(cursor.getInt(3));
				u.setUserId(cursor.getLong(4));
				u.setRowId(cursor.getString(5));
				u.setServerId(cursor.getLong(6));
				u.setId(cursor.getLong(7));
			} else {
				u.setColor(null);
				u.setUserId(null);
				u.setServerId(null);
				u.setId(null);
			}
			result.add(u);
		}		
		return result;
	}

	@Override
	public UserBodyFeelingType getUserBodyFeelingType(long bodyFeelingTypeId) {
		DaoSession daoSession = DB.db().newSession();		
		UserBodyFeelingType result = null;
		List<UserBodyFeelingType> findedResults = null;
		if (bodyFeelingTypeId != BaseDTO.INT_NULL_VALUE) {
			findedResults = daoSession.getUserBodyFeelingTypeDao().queryBuilder().where(UserBodyFeelingTypeDao.Properties.BodyFeelingTypeId.eq(bodyFeelingTypeId)).list();
		} else {
			findedResults = daoSession.getUserBodyFeelingTypeDao().queryBuilder().whereOr(UserBodyFeelingTypeDao.Properties.BodyFeelingTypeId.isNull(), UserBodyFeelingTypeDao.Properties.BodyFeelingTypeId.eq(BaseDTO.INT_NULL_VALUE)).list();
		}
		if (findedResults.size() > 0) {
			result = findedResults.get(0);
		}
		return result;
	}

	@Override
	public UserBodyFeelingType getDefaultUserBodyFeelingType() {
		UserBodyFeelingType userBodyFeelingType = getUserBodyFeelingType(BaseDTO.INT_NULL_VALUE);
		if (userBodyFeelingType == null) {
			userBodyFeelingType = new UserBodyFeelingType();
			userBodyFeelingType.setRowId(UUID.randomUUID().toString());
			userBodyFeelingType.setBodyFeelingTypeId(new Long(BaseDTO.INT_NULL_VALUE));
			userBodyFeelingType.setColor(IRepository.DEFAULT_USER_BODY_FEELING_TYPE_COLOR);
		}
		return userBodyFeelingType;
	}

	@Override
	public void updateUserBodyFeelingType(UserBodyFeelingType userBodyFeelingType) {
		DaoSession daoSession = DB.db().newSession();
		if (userBodyFeelingType.getId() != null) {
			daoSession.getUserBodyFeelingTypeDao().update(userBodyFeelingType);
			OperationUserData.updateOperationUserData(OperationUserData.USERBODYFEELINGTYPE_TABLEID, userBodyFeelingType.getId(), userBodyFeelingType.getServerId(), UUID.fromString(userBodyFeelingType.getRowId()), OperationUserData.UPDATE_OPERATION_TYPE);
		} else {
			daoSession.getUserBodyFeelingTypeDao().insert(userBodyFeelingType);
			OperationUserData.updateOperationUserData(OperationUserData.USERBODYFEELINGTYPE_TABLEID, userBodyFeelingType.getId(), userBodyFeelingType.getServerId(), UUID.fromString(userBodyFeelingType.getRowId()), OperationUserData.INSERT_OPERATION_TYPE);
		}
	}

	@Override
	public int getUserColorForBodyFeelingType(long bodyFeelingTypeId) {
		int color;
		UserBodyFeelingType userBodyFeelingType = getUserBodyFeelingType(bodyFeelingTypeId);
		if (userBodyFeelingType != null) {
			color = userBodyFeelingType.getColor();
		} else {
			userBodyFeelingType = getDefaultUserBodyFeelingType();
			color = userBodyFeelingType.getColor();
		}
		return color;
	}

	@Override
    public LinkedHashMap<IFeelingTypeInfo, List<IFeeling>> getFeelingGroups(Date dtFrom, Date dtTo) {
		DaoSession daoSession = DB.db().newSession();
		LinkedHashMap<IFeelingTypeInfo, List<IFeeling>> result = new LinkedHashMap<IFeelingTypeInfo, List<IFeeling>>();
		UserBodyFeelingType defaultUserBodyFeelingType = null;
		List<UserBodyFeelingType> defaultUserBodyFeelingTypes = daoSession.getUserBodyFeelingTypeDao().queryBuilder().where(UserBodyFeelingTypeDao.Properties.BodyFeelingTypeId.isNull()).list();
		if(defaultUserBodyFeelingTypes.size() > 0){
			defaultUserBodyFeelingType = defaultUserBodyFeelingTypes.get(0);
		} else {
			defaultUserBodyFeelingType = new UserBodyFeelingType();
			defaultUserBodyFeelingType.setBodyFeelingType(null);
			defaultUserBodyFeelingType.setColor(IRepository.DEFAULT_USER_BODY_FEELING_TYPE_COLOR);
		}	
    
        
        //BodyFeeling    
        List<BodyFeeling> bodyFeelingList = daoSession.getBodyFeelingDao().queryBuilder().where(BodyFeelingDao.Properties.StartDate.ge(dtFrom), BodyFeelingDao.Properties.StartDate.le(dtTo)).orderAsc(BodyFeelingDao.Properties.FeelingTypeId).orderAsc(BodyFeelingDao.Properties.CustomFeelingTypeId).orderAsc(BodyFeelingDao.Properties.BodyRegionId).orderAsc(BodyFeelingDao.Properties.StartDate).list();
        BodyFeelingTypeInfo key = null;
        for(BodyFeeling bf: bodyFeelingList){
        	if(key == null 
        			|| key.getBodyRegion().getId() != bf.getBodyRegion().getId()
        			|| ((key.getBodyFeelingType() != null) ? key.getBodyFeelingType().getId() : BaseDTO.INT_NULL_VALUE) != ((bf.getBodyFeelingType() != null) ? bf.getBodyFeelingType().getId() : BaseDTO.INT_NULL_VALUE)
        			|| ((key.getCustomBodyFeelingType() != null) ? key.getCustomBodyFeelingType().getId() : BaseDTO.INT_NULL_VALUE) != ((bf.getCustomBodyFeelingType() != null) ? bf.getCustomBodyFeelingType().getId() : BaseDTO.INT_NULL_VALUE) ){
        		BodyFeelingType bodyFeelingType = bf.getBodyFeelingType();
        		CustomBodyFeelingType customBodyFeelingType = bf.getCustomBodyFeelingType();
        		BodyRegion bodyRegion = bf.getBodyRegion();
        		UserBodyFeelingType userBodyFeelingType = null;
        		if(bodyFeelingType != null){	        			
        			List<UserBodyFeelingType> userBodyFeelingTypes = daoSession.getUserBodyFeelingTypeDao().queryBuilder().where(UserBodyFeelingTypeDao.Properties.BodyFeelingTypeId.eq(bf.getBodyFeelingType().getId())).list();
	        		if(userBodyFeelingTypes.size() > 0){
	        			userBodyFeelingType = userBodyFeelingTypes.get(0);
	        		} else {
	        			userBodyFeelingType = defaultUserBodyFeelingType;
	        		}
        		} else {
        			userBodyFeelingType = defaultUserBodyFeelingType;
        		}	        		
        		key = new BodyFeelingTypeInfo(bodyFeelingType, customBodyFeelingType, bodyRegion, userBodyFeelingType);
        		result.put(key, new ArrayList<IFeeling>());	        		
        	}
        	result.get(key).add((IFeeling)bf);
        }    
	    
	    //CommonFeeling
    	List<CommonFeeling> commonFeelingList = daoSession.getCommonFeelingDao().queryBuilder().where(CommonFeelingDao.Properties.StartDate.ge(dtFrom), CommonFeelingDao.Properties.StartDate.le(dtTo)).
    			orderAsc(CommonFeelingDao.Properties.CommonFeelingTypeId).orderAsc(CommonFeelingDao.Properties.CustomCommonFeelingTypeId).orderAsc(CommonFeelingDao.Properties.StartDate).list();	        
        CommonFeelingTypeInfo key2 = null;
        for(CommonFeeling feeling: commonFeelingList){
        	if(key2 == null  
        			|| ((key2.getCommonFeelingType() != null) ? key2.getCommonFeelingType().getId() : BaseDTO.INT_NULL_VALUE) != ((feeling.getCommonFeelingType() != null) ? feeling.getCommonFeelingType().getId() : BaseDTO.INT_NULL_VALUE)
        			|| ((key2.getCustomCommonFeelingType() != null) ? key2.getCustomCommonFeelingType().getId() : BaseDTO.INT_NULL_VALUE) != ((feeling.getCustomCommonFeelingType() != null) ? feeling.getCustomCommonFeelingType().getId() : BaseDTO.INT_NULL_VALUE)){
        		CommonFeelingType commonFeelingType = feeling.getCommonFeelingType();
        		CustomCommonFeelingType customCommonFeelingType = feeling.getCustomCommonFeelingType();	        		
        		key2 = new CommonFeelingTypeInfo(commonFeelingType, customCommonFeelingType);
        		result.put(key2, new ArrayList<IFeeling>());
        	}
        	result.get(key2).add((IFeeling)feeling);
        }	    
	    return result;
    }

	

	@Override
    public List<BodyFeelingType> getBodyFeelingTypes(long bodyRegionId) {
		DaoSession daoSession = DB.db().newSession();
		String rawWhere = String.format("SELECT DISTINCT %s FROM %s where %s=%d", BodyRegion_BodyFeelingTypeDao.Properties.BodyFeelingTypeId.columnName, BodyRegion_BodyFeelingTypeDao.TABLENAME, BodyRegion_BodyFeelingTypeDao.Properties.BodyRegionId.columnName, bodyRegionId);		
		List<BodyFeelingType> result = daoSession.getBodyFeelingTypeDao().queryRawCreate(String.format(" WHERE %s in(%s)", BodyFeelingTypeDao.Properties.Id.columnName, rawWhere)).list();
		return result;		
    }
	

	@Override
	public LinkedHashMap<IGridGroup, List<Complaint>> getBodyComplaints(Date dtFrom, Date dtTo) {
		DaoSession daoSession = DB.db().newSession();		
		LinkedHashMap<IGridGroup, List<Complaint>> result = new LinkedHashMap<IGridGroup, List<Complaint>>();        
        List<Complaint> complaints = daoSession.getComplaintDao().queryBuilder().where(ComplaintDao.Properties.StartDate.ge(dtFrom), ComplaintDao.Properties.StartDate.le(dtTo)).
        		orderAsc(ComplaintDao.Properties.CommonFeelingTypeId).orderAsc(ComplaintDao.Properties.BodyComplaintTypeId).orderAsc(ComplaintDao.Properties.StartDate).list();
        BodyComplaintType key1 = null;
        CommonFeelingType key2 = null;
        for(Complaint item: complaints){
        	if(item.getBodyComplaintType() != null){
        		if(key1 == null || key1.getId() != item.getBodyComplaintType().getId()){	        		     		
	        		key1 = item.getBodyComplaintType();
	        		result.put(key1, new ArrayList<Complaint>());	        		
	        	}
	        	result.get(key1).add(item);
        	}
        	if(item.getCommonFeelingType() != null){
        		if(key2 == null || key2.getId() != item.getCommonFeelingType().getId() ){	        		 		
	        		key2 = item.getCommonFeelingType();
	        		result.put(key2, new ArrayList<Complaint>());	        		
	        	}
	        	result.get(key2).add(item);
        	}
        }
    
	    return result;
	}
	
	@Override
	public LinkedHashMap<IGridGroup, List<Complaint>> getUserBodyComplaints(Date dtFrom, Date dtTo) {
		LinkedHashMap<IGridGroup, List<Complaint>> result = new LinkedHashMap<IGridGroup, List<Complaint>>();
		User currentUser = UserDB.getCurrentUser();
		DaoSession daoSession = DB.db().newSession();		
		String rawWhere1 = String.format("SELECT DISTINCT complaintid from tblbodyregion reg, tblbodyfeeling f where reg.id = f.bodyregionid and f.userid=%d and complaintid is not null", currentUser.getId());
		String rawWhere2 = String.format("SELECT DISTINCT %s FROM %s where %s=%d", CommonFeelingDao.Properties.CommonFeelingTypeId.columnName, CommonFeelingDao.TABLENAME, CommonFeelingDao.Properties.UserId.columnName, currentUser.getId());
		
        List<Complaint> complaints1 = daoSession.getComplaintDao().queryBuilder().where(ComplaintDao.Properties.StartDate.ge(dtFrom), ComplaintDao.Properties.StartDate.le(dtTo), new WhereCondition.StringCondition(String.format(" %s in (%s) ", ComplaintDao.Properties.BodyComplaintTypeId.columnName, rawWhere1))).
        		orderAsc(ComplaintDao.Properties.CommonFeelingTypeId).orderAsc(ComplaintDao.Properties.BodyComplaintTypeId).orderAsc(ComplaintDao.Properties.StartDate).list();
        List<Complaint> complaints2 = daoSession.getComplaintDao().queryBuilder().where(ComplaintDao.Properties.StartDate.ge(dtFrom), ComplaintDao.Properties.StartDate.le(dtTo), new WhereCondition.StringCondition(String.format(" %s in (%s) ", ComplaintDao.Properties.CommonFeelingTypeId.columnName, rawWhere2))).
        		orderAsc(ComplaintDao.Properties.CommonFeelingTypeId).orderAsc(ComplaintDao.Properties.BodyComplaintTypeId).orderAsc(ComplaintDao.Properties.StartDate).list();
        
        BodyComplaintType key1 = null;
        CommonFeelingType key2 = null;
        for(Complaint item: complaints1){
        	if(item.getBodyComplaintType() != null){
        		if(key1 == null || key1.getId() != item.getBodyComplaintType().getId()){	        		     		
	        		key1 = item.getBodyComplaintType();
	        		result.put(key1, new ArrayList<Complaint>());	        		
	        	}
	        	result.get(key1).add(item);
        	}        	
        }
        for(Complaint item: complaints2){        	
        	if(item.getCommonFeelingType() != null){
        		if(key2 == null || key2.getId() != item.getCommonFeelingType().getId() ){	        		 		
	        		key2 = item.getCommonFeelingType();
	        		result.put(key2, new ArrayList<Complaint>());	        		
	        	}
	        	result.get(key2).add(item);
        	}
        }		
	    return result;
	}

	@Override
    public List<BodyFeeling> getBodyFeelings(Long bodyFeelingTypeId, Long customBodyFeelingTypeId, long bodyRegionId, Date dtFrom, Date dtTo) {
		DaoSession daoSession = DB.db().newSession();
		QueryBuilder<BodyFeeling> qb = daoSession.getBodyFeelingDao().queryBuilder().where(BodyFeelingDao.Properties.BodyRegionId.eq(bodyRegionId), BodyFeelingDao.Properties.StartDate.ge(dtFrom), BodyFeelingDao.Properties.StartDate.le(dtTo));
		if(bodyFeelingTypeId != null){
			qb = qb.where(BodyFeelingDao.Properties.FeelingTypeId.eq(bodyFeelingTypeId));			
		} else {
			qb = qb.where(BodyFeelingDao.Properties.CustomFeelingTypeId.eq(customBodyFeelingTypeId));			
		}
		List<BodyFeeling> result = qb.orderAsc(BodyFeelingDao.Properties.FeelingTypeId).orderAsc(BodyFeelingDao.Properties.BodyRegionId).orderAsc(BodyFeelingDao.Properties.CustomFeelingTypeId).orderAsc(BodyFeelingDao.Properties.StartDate).list();		
		return result;
    }

	@Override
    public List<CommonFeeling> getCommonFeelings(Long commonFeelingTypeId, Long customCommonFeelingTypeId, Date dtFrom, Date dtTo) {
		DaoSession daoSession = DB.db().newSession();
		QueryBuilder<CommonFeeling> qb = null;
		if(commonFeelingTypeId != null) {
			qb = daoSession.getCommonFeelingDao().queryBuilder().where(CommonFeelingDao.Properties.CommonFeelingTypeId.eq(commonFeelingTypeId), CommonFeelingDao.Properties.StartDate.ge(dtFrom), CommonFeelingDao.Properties.StartDate.le(dtTo));
		} else {
			qb = daoSession.getCommonFeelingDao().queryBuilder().where(CommonFeelingDao.Properties.CustomCommonFeelingTypeId.eq(customCommonFeelingTypeId), CommonFeelingDao.Properties.StartDate.ge(dtFrom), CommonFeelingDao.Properties.StartDate.le(dtTo));
		}
		List<CommonFeeling> result = qb.orderAsc(CommonFeelingDao.Properties.CommonFeelingTypeId).orderAsc(CommonFeelingDao.Properties.CustomCommonFeelingTypeId).orderAsc(CommonFeelingDao.Properties.StartDate).list();
		return result;
    }

	@Override
    public List<CustomBodyFeelingType> getCustomBodyFeelingTypes(long bodyRegionId) {
		User user = getCurrentUser();
	    DaoSession daoSession = DB.db().newSession();
		String rawWhere = String.format("SELECT DISTINCT %s FROM %s where %s=%d and %s is not null and %s=%d", 
				BodyFeelingDao.Properties.CustomFeelingTypeId.columnName, BodyFeelingDao.TABLENAME, BodyFeelingDao.Properties.UserId.columnName, user.getId(),
				BodyFeelingDao.Properties.CustomFeelingTypeId.columnName, BodyFeelingDao.Properties.BodyRegionId.columnName, bodyRegionId);		
		List<CustomBodyFeelingType> result = daoSession.getCustomBodyFeelingTypeDao().queryRawCreate(String.format(" WHERE %s in(%s)", CustomBodyFeelingTypeDao.Properties.Id.columnName, rawWhere)).list();
		return result;		
    }

	@Override
    public List<KpIndex> getKpIndicies() {
		DaoSession daoSession = DB.db().newSession();
		List<KpIndex> list = daoSession.getKpIndexDao().queryBuilder().orderAsc(KpIndexDao.Properties.Id).list();
		return list;        
    }
	
	@Override
    public CustomFactorType addCustomFactorType(CustomFactorType customFactorType) {
		DaoSession daoSession = DB.db().newSession();
		User user = getCurrentUser();
		customFactorType.setUserId(user.getId());
		customFactorType.setName(CLEAR_SPACE_PATTERN.matcher(customFactorType.getName()).replaceAll(" ").trim());
		customFactorType.setRowId(UUID.randomUUID().toString());
		CustomFactorType dbCustomFactorType = null;		
		List<CustomFactorType> dbList = daoSession.getCustomFactorTypeDao().queryBuilder().where(CustomFactorTypeDao.Properties.UserId.eq(user.getId()), CustomFactorTypeDao.Properties.Name.eq(customFactorType.getName()), CustomFactorTypeDao.Properties.FactorGroupId.eq(customFactorType.getFactorGroupId())).list();
		if(dbList.size() > 0){
			dbCustomFactorType = dbList.get(0);
		}
		if(dbCustomFactorType != null){
			return dbCustomFactorType;
		} else {
			daoSession.getCustomFactorTypeDao().insert(customFactorType);
			OperationUserData.updateOperationUserData(OperationUserData.CUSTOMFACTORTYPE_TABLEID, customFactorType.getId(), customFactorType.getServerId(), UUID.fromString(customFactorType.getRowId()), OperationUserData.INSERT_OPERATION_TYPE);
			return customFactorType;
		}
    }
	
	@Override
    public CustomCommonFeelingType addCustomCommonFeelingType(CustomCommonFeelingType customCommonFeelingType) {
		DaoSession daoSession = DB.db().newSession();
		User user = getCurrentUser();
		customCommonFeelingType.setUserId(user.getId());
		customCommonFeelingType.setName(CLEAR_SPACE_PATTERN.matcher(customCommonFeelingType.getName()).replaceAll(" ").trim());
		customCommonFeelingType.setRowId(UUID.randomUUID().toString());
		CustomCommonFeelingType dbCustomCommonFeelingType = null;
		List<CustomCommonFeelingType> dbList = daoSession.getCustomCommonFeelingTypeDao().queryBuilder().where(CustomCommonFeelingTypeDao.Properties.UserId.eq(user.getId()), CustomCommonFeelingTypeDao.Properties.Name.eq(customCommonFeelingType.getName()), CustomCommonFeelingTypeDao.Properties.CommonFeelingGroupId.eq(customCommonFeelingType.getCommonFeelingGroupId())).list();
		if(dbList.size() > 0){
			dbCustomCommonFeelingType = dbList.get(0);
		}
		if(dbCustomCommonFeelingType != null){
			return dbCustomCommonFeelingType;
		} else {
			daoSession.getCustomCommonFeelingTypeDao().insert(customCommonFeelingType);
			OperationUserData.updateOperationUserData(OperationUserData.CUSTOMCOMMONFEELINGTYPE_TABLEID, customCommonFeelingType.getId(), customCommonFeelingType.getServerId(), UUID.fromString(customCommonFeelingType.getRowId()), OperationUserData.INSERT_OPERATION_TYPE);
			return customCommonFeelingType;
		}
    }

	@Override
    public List<Factor> getFactors(Long factorTypeId, Long customFactorTypeId, Date dtFrom, Date dtTo) {
		DaoSession daoSession = DB.db().newSession();
		List<Factor> result = null;
		if(factorTypeId != null){	        
	        result = daoSession.getFactorDao().queryBuilder().
	        		where(FactorDao.Properties.FactorTypeId.eq(factorTypeId), FactorDao.Properties.StartDate.ge(dtFrom), FactorDao.Properties.StartDate.le(dtTo)).
	        		orderAsc(FactorDao.Properties.FactorTypeId).orderAsc(FactorDao.Properties.StartDate).list();
		}
		if(customFactorTypeId != null){
			result = daoSession.getFactorDao().queryBuilder().
	        		where(FactorDao.Properties.CustomFactorTypeId.eq(customFactorTypeId), FactorDao.Properties.StartDate.ge(dtFrom), FactorDao.Properties.StartDate.le(dtTo)).
	        		orderAsc(FactorDao.Properties.CustomFactorTypeId).orderAsc(FactorDao.Properties.StartDate).list();
		}		
		return result;
    }

	@Override
    public void addFactor(Factor factor) {
		DaoSession daoSession = DB.db().newSession();
		User user = getCurrentUser();
		factor.setUserId(user.getId());
		factor.setRowId(UUID.randomUUID().toString());
		daoSession.getFactorDao().insert(factor);
		OperationUserData.updateOperationUserData(OperationUserData.FACTOR_TABLEID, factor.getId(), factor.getServerId(), UUID.fromString(factor.getRowId()), OperationUserData.INSERT_OPERATION_TYPE);	    
    }

	@Override
    public void updateFactor(Factor factor) {
		DaoSession daoSession = DB.db().newSession();
		Factor dbFactor = daoSession.getFactorDao().load(factor.getId());
		factor.setId(dbFactor.getId());
		factor.setServerId(dbFactor.getServerId());
		factor.setRowId(dbFactor.getRowId());
		factor.setUserId(dbFactor.getUserId());		
		daoSession.getFactorDao().update(factor);
		OperationUserData.updateOperationUserData(OperationUserData.FACTOR_TABLEID, factor.getId(), factor.getServerId(), UUID.fromString(factor.getRowId()), OperationUserData.UPDATE_OPERATION_TYPE);
    }

	@Override
    public void deleteFactor(Factor factor) {
		DaoSession daoSession = DB.db().newSession();
		Factor dbFactor = daoSession.getFactorDao().load(factor.getId());
		OperationUserData.updateOperationUserData(OperationUserData.FACTOR_TABLEID, dbFactor.getId(), dbFactor.getServerId(), UUID.fromString(dbFactor.getRowId()), OperationUserData.DELETE_OPERATION_TYPE);
		daoSession.getFactorDao().delete(dbFactor);
    }

	@Override
    public Factor getFactorById(Context context, long id) {
		DaoSession daoSession = DB.db().newSession();
		Factor factor = daoSession.getFactorDao().load(id);
		if (factor != null) {
			factor.getFactorType();
			factor.getCustomFactorType();			
		}
		return factor;
    }	
	
	@Override
    public List<Factor> getFactorList(Date dtFrom, Date dtTo) {
		DaoSession daoSession = DB.db().newSession();
		List<Factor> result = daoSession.getFactorDao().queryBuilder().where(FactorDao.Properties.StartDate.ge(dtFrom), FactorDao.Properties.StartDate.le(dtTo)).orderAsc(FactorDao.Properties.StartDate).orderAsc(FactorDao.Properties.Id).list();		
		return result;
    }
	
	@Override
    public List<FactorType> getFactorTypes(long factorGroupId) {
		DaoSession daoSession = DB.db().newSession();		
		List<FactorType> result = daoSession.getFactorTypeDao().queryBuilder().where(FactorTypeDao.Properties.FactorGroupId.eq(factorGroupId)).orderAsc(FactorTypeDao.Properties.OrdinalNumber).list();		
	    return result;
    }

	@Override
    public List<CustomFactorType> getCustomFactorTypes(long factorGroupId) {
		User user = getCurrentUser();
		DaoSession daoSession = DB.db().newSession();		
		List<CustomFactorType> result = daoSession.getCustomFactorTypeDao().queryBuilder().where(CustomFactorTypeDao.Properties.UserId.eq(user.getId()), CustomFactorTypeDao.Properties.FactorGroupId.eq(factorGroupId)).orderAsc(CustomFactorTypeDao.Properties.OrdinalNumber).list();
	    return result;
    }

	@Override
    public List<CommonFeelingType> getCommonFeelingTypes(long feelingGroupId) {
		DaoSession daoSession = DB.db().newSession();		
		List<CommonFeelingType> result = daoSession.getCommonFeelingTypeDao().queryBuilder().where(CommonFeelingTypeDao.Properties.CommonFeelingGroupId.eq(feelingGroupId)).orderAsc(CommonFeelingTypeDao.Properties.OrdinalNumber).list();		
	    return result;
    }

	@Override
    public List<CustomCommonFeelingType> getCustomCommonFeelingTypes(long feelingGroupId) {
		User user = getCurrentUser();
		DaoSession daoSession = DB.db().newSession();		
		List<CustomCommonFeelingType> result = daoSession.getCustomCommonFeelingTypeDao().queryBuilder().where(CustomCommonFeelingTypeDao.Properties.UserId.eq(user.getId()), CustomCommonFeelingTypeDao.Properties.CommonFeelingGroupId.eq(feelingGroupId)).orderAsc(CustomCommonFeelingTypeDao.Properties.OrdinalNumber).list();		
	    return result;		
    }

	@Override
    public FactorGroup getFactorGroupById(Context context, long id) {
		DaoSession daoSession = DB.db().newSession();
	    return daoSession.getFactorGroupDao().load(id);
    }

	@Override
    public CommonFeelingGroup getCommonFeelingGroupById(Context context, long id) {
		DaoSession daoSession = DB.db().newSession();
	    return daoSession.getCommonFeelingGroupDao().load(id);
    }

	@Override
	public int getNextOrdinalNumberForFactorGroup(Context context, long factorGroupId) {
		int maxOrdinalNumber = 0;
		int ordinalNumber = 0;
		String selectQuery1 = String.format("SELECT coalesce(max(ordinalnumber), 0) from tblfactortype where factorgroupid=%d;", factorGroupId);
		String selectQuery2 = String.format("SELECT coalesce(max(ordinalnumber), 0) from tblcustomfactortype where factorgroupid=%d;", factorGroupId);        
    	Cursor cursor1 = DB.db().getDatabase().rawQuery(selectQuery1, null);
    	if(cursor1.moveToFirst()){
    		ordinalNumber = cursor1.getInt(0);
    		if(ordinalNumber > maxOrdinalNumber){
        		maxOrdinalNumber = ordinalNumber;
        	}
    	}
    	Cursor cursor2 = DB.db().getDatabase().rawQuery(selectQuery2, null);
        if(cursor2.moveToFirst()){
        	ordinalNumber = cursor2.getInt(0);
        	if(ordinalNumber > maxOrdinalNumber){
        		maxOrdinalNumber = ordinalNumber;
        	}
        }           
		return maxOrdinalNumber + 1;
	}

	@Override
    public int getNextOrdinalNumberForCommonFeelingGroup(Context context, long commonFeelingGroupId) {
		int maxOrdinalNumber = 0;
		int ordinalNumber = 0;
		String selectQuery1 = String.format("SELECT coalesce(max(ordinalnumber), 0) from tblcommonfeelingtype where feelinggroupid=%d;", commonFeelingGroupId);
		String selectQuery2 = String.format("SELECT coalesce(max(ordinalnumber), 0) from tblcustomcommonfeelingtype where feelinggroupid=%d;", commonFeelingGroupId);
		Cursor cursor1 = DB.db().getDatabase().rawQuery(selectQuery1, null);
    	if(cursor1.moveToFirst()){
    		ordinalNumber = cursor1.getInt(0);
    		if(ordinalNumber > maxOrdinalNumber){
        		maxOrdinalNumber = ordinalNumber;
        	}
    	}
        Cursor cursor2 = DB.db().getDatabase().rawQuery(selectQuery2, null);
        if(cursor2.moveToFirst()){
        	ordinalNumber = cursor2.getInt(0);
        	if(ordinalNumber > maxOrdinalNumber){
        		maxOrdinalNumber = ordinalNumber;
        	}
        }       
		return maxOrdinalNumber + 1;
    }

    public List<FactorGroup> getFactorGroups() {
    	DaoSession daoSession = DB.db().newSession();
	    return daoSession.getFactorGroupDao().queryBuilder().orderAsc(FactorGroupDao.Properties.Id).list();		
    }
    
    private ILoadDataListener mLoadDataListener;
    
	@Override
    public void setLoadDataListener(ILoadDataListener loadDataListener) {
	    this.mLoadDataListener = loadDataListener;	    
    }
	
	private Date mLastCallLoadData = null;
	
	public void onLoadData(Date dtFrom, Date dtTo) {
		Date nowDate = new Date();
		if (this.mLoadDataListener != null) {
			if (mLastCallLoadData == null || Math.abs(mLastCallLoadData.getTime() - nowDate.getTime()) > 5000) {
				mLastCallLoadData = nowDate;
				this.mLoadDataListener.loadData(dtFrom, dtTo);
			}
		}
	}

	@Override
    public Weather getCurrentWeatherOnDate(Date dt) {
		DaoSession daoSession = DB.db().newSession();
		List<Weather> result = daoSession.getWeatherDao().queryBuilder().where(WeatherDao.Properties.InfoDate.le(dt)).orderDesc(WeatherDao.Properties.InfoDate).limit(1).list();		
		if(result.size() == 0){
			return new Weather();
		} else {
			return result.get(0);
		}
    }

	@Override
    public Sun getCurrentSunOnDate(Date dt) {
		DaoSession daoSession = DB.db().newSession();
		List<Sun> result = daoSession.getSunDao().queryBuilder().where(SunDao.Properties.InfoDate.le(dt)).orderDesc(SunDao.Properties.InfoDate).limit(1).list();
		if(result.size() == 0){
			return new Sun();
		} else {
			return result.get(0);
		}
    }

	@Override
    public Moon getCurrentMoonOnDate(Date dt) {
		DaoSession daoSession = DB.db().newSession();
		List<Moon> result = daoSession.getMoonDao().queryBuilder().where(MoonDao.Properties.InfoDate.le(dt)).orderDesc(MoonDao.Properties.InfoDate).limit(1).list();		
		if(result.size() == 0){
			return new Moon();
		} else {
			return result.get(0);
		}
    }

	@Override
    public GeoPhysics getCurrentGeoPhysicsOnDate(Date dt) {
		DaoSession daoSession = DB.db().newSession();
		List<GeoPhysics> resultList = daoSession.getGeoPhysicsDao().queryBuilder().where(GeoPhysicsDao.Properties.InfoDate.le(dt)).orderAsc(GeoPhysicsDao.Properties.InfoDate).list();
		GeoPhysics result = new GeoPhysics();
		if(resultList.size() != 0){
			int i = resultList.size() - 1;
			while(i != 0 && 
					(result.getKpId() == null ||
					 result.getAp() == null)){
				if(result.getKpId() == null) {
					result.setKpId(resultList.get(i).getKpId());
				}
				if(result.getAp() == null){
					result.setAp(resultList.get(i).getAp());
				}
				i--;
			}
		}
		return result;
    }

	@Override
    public HelioPhysics getCurrentHelioPhysicsOnDate(Date dt) {
		DaoSession daoSession = DB.db().newSession();
		List<HelioPhysics> resultList = daoSession.getHelioPhysicsDao().queryBuilder().where(HelioPhysicsDao.Properties.InfoDate.le(dt)).orderAsc(HelioPhysicsDao.Properties.InfoDate).list();
		HelioPhysics result = new HelioPhysics();		
		if(resultList.size() != 0){
			int i = resultList.size() - 1;
			while(i != 0 && 
					(result.getF10_7() == null ||
					 result.getSunspotNumber() == null ||
					 result.getSunspotArea() == null ||
					 result.getNewRegions() == null ||
					 result.getFlares1()== null ||
					 result.getFlares2() == null ||
					 result.getFlares3() == null ||
					 result.getFlaresS() == null ||
					 result.getFlaresC() == null ||
					 result.getFlaresM() == null ||
					 result.getFlaresX() == null ||
					 result.getXbkgd() == null)){
				if(result.getF10_7() == null) {
					result.setF10_7(resultList.get(i).getF10_7());
				}
				if(result.getSunspotNumber() == null){
					result.setSunspotNumber(resultList.get(i).getSunspotNumber());
				}
				if(result.getSunspotArea() == null){
					result.setSunspotArea(resultList.get(i).getSunspotArea());
				}
				if(result.getNewRegions() == null){
					result.setNewRegions(resultList.get(i).getNewRegions());
				}
				if(result.getFlares1() == null){
					result.setFlares1(resultList.get(i).getFlares1());
				}
				if(result.getFlares2() == null){
					result.setFlares2(resultList.get(i).getFlares2());
				}
				if(result.getFlares3() == null){
					result.setFlares3(resultList.get(i).getFlares3());
				}
				if(result.getFlaresS() == null){
					result.setFlaresS(resultList.get(i).getFlaresS());
				}
				if(result.getFlaresC() == null){
					result.setFlaresC(resultList.get(i).getFlaresC());
				}
				if(result.getFlaresM() == null){
					result.setFlaresM(resultList.get(i).getFlaresM());
				}
				if(result.getFlaresX() == null){
					result.setFlaresX(resultList.get(i).getFlaresX());
				}
				if(result.getXbkgd() == null){
					result.setXbkgd(resultList.get(i).getXbkgd());
				}
				i--;
			}
		}
		return result;
    }

	@Override
    public Particle getCurrentParticleOnDate(Date dt) {
		DaoSession daoSession = DB.db().newSession();
		List<Particle> result = daoSession.getParticleDao().queryBuilder().where(ParticleDao.Properties.InfoDate.le(dt)).orderDesc(ParticleDao.Properties.InfoDate).limit(1).list();		
		if(result.size() == 0){
			return new Particle();
		} else {
			return result.get(result.size() - 1);
		}
    }

	
}
