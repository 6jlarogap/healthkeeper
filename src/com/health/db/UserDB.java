package com.health.db;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.health.data.City;
import com.health.data.DaoSession;
import com.health.data.User;
import com.health.data.UserDao;
import com.health.main.R;
import com.health.task.BaseTask;
import com.health.task.BaseTask.LoginInfo;

public class UserDB {
	
	public static final int DEFAULT_PERIOD_SYNC_DATA = 86400;
	
	public static List<User> getRealUsers(){
		DaoSession daoSession = DB.db().newSession();
		List<User> result = daoSession.getUserDao().queryBuilder().where(UserDao.Properties.Login.notEq(User.ANONIM_LOGIN)).orderAsc(UserDao.Properties.Login).list();
		return result;
	}
			
	public static User getCurrentUser(){
		User user = null;
		DaoSession daoSession = DB.db().newSession();
		List<User> users = daoSession.getUserDao().queryBuilder().where(UserDao.Properties.IsActive.eq(User.IS_ACTIVE)).list();
		if(users.size() == 1){
			user = users.get(0);
		}		
		if(user != null && user.getCity() != null){
			user.getCity().getCountry();
		}
		return user;
	}
	
	public static long getUserCount(){
		DaoSession daoSession = DB.db().newSession();
		long count = daoSession.getUserDao().count();
		return count;
	}
	
	public static Long getUnixSyncDateForUserData(){
		Long result = null;
		User user = getCurrentUser();
		if(user.getSyncDate() != null){
			result = (user.getSyncDate().getTime()/1000);
		}
		return result;
	}
	
	public static void updateSyncDateForUserData(Date syncDate){
		User user = getCurrentUser();
		if(user != null){
			user.setSyncDate(syncDate);
			DB.db().newSession().getUserDao().update(user);
		}
	}
	
	public static User changeUser(BaseTask.LoginInfo loginInfo, int isStorePassword){
		DaoSession daoSession = DB.db().newSession();
		User user = getCurrentUser();
		if(user != null){
			if(user.getLogin().equals(loginInfo.Login)){
				user.setId(new Long(loginInfo.Id));
				user.setLogin(loginInfo.Login);
				user.setPassword(loginInfo.Password);
				user.setFName(loginInfo.FName);
				user.setLName(loginInfo.LName);
				user.setMName(loginInfo.MName);
				user.setBirthDate(loginInfo.BirthDate);
				user.setCreateDate(loginInfo.CreateDate);
				user.setSex(loginInfo.Sex);
				user.setCityId(loginInfo.CityId);				
				user.setPeriodSyncData(DEFAULT_PERIOD_SYNC_DATA);
				
				user.setSocialStatusId(loginInfo.SocialStatusId);
				user.setMaritalStatusId(loginInfo.MaritalStatusId);
				user.setHeight(loginInfo.Height);
				user.setWeight(loginInfo.Weight);
				user.setPressureId(loginInfo.PressureId);
				user.setFootDistance(loginInfo.FootDistance);
				user.setSleepTime(loginInfo.SleepTime);
				if(loginInfo.Question1 != null){					
					user.setQuestion1(new Long(loginInfo.Question1));					
				}
				if(loginInfo.Question2 != null){
					user.setQuestion2(new Long(loginInfo.Question2));
				}
				user.setIsActive(User.IS_ACTIVE);
				user.setIsStorePassword(isStorePassword);
				daoSession.getUserDao().update(user);				
			} else {
				deletePersonalDataOnClient();
				if(user.getCity() == null || user.getCity().getId() != loginInfo.CityId){
					deleteCommonDataOnClient();
				}
				user.setIsActive(User.IS_PASSIVE);				
				daoSession.getUserDao().update(user);
				user = null;
			}
		}
		if(user == null){
			user = getUserByLogin(loginInfo.Login);
			if(user == null){
				user = new User();
			}
			user.setId(new Long(loginInfo.Id));
			user.setLogin(loginInfo.Login);
			user.setPassword(loginInfo.Password);
			user.setFName(loginInfo.FName);
			user.setLName(loginInfo.LName);
			user.setMName(loginInfo.MName);
			user.setBirthDate(loginInfo.BirthDate);
			user.setCreateDate(loginInfo.CreateDate);
			user.setSex(loginInfo.Sex);
			user.setCityId(loginInfo.CityId);			
			user.setIsAutoSync(1);
			user.setPeriodSyncData(DEFAULT_PERIOD_SYNC_DATA);
			
			user.setSocialStatusId(loginInfo.SocialStatusId);
			user.setMaritalStatusId(loginInfo.MaritalStatusId);
			user.setHeight(loginInfo.Height);
			user.setWeight(loginInfo.Weight);
			user.setPressureId(loginInfo.PressureId);
			user.setFootDistance(loginInfo.FootDistance);
			user.setSleepTime(loginInfo.SleepTime);
			if(loginInfo.Question1 != null){
				user.setQuestion1(new Long(loginInfo.Question1));				
			}
			if(loginInfo.Question2 != null){
				user.setQuestion2(new Long(loginInfo.Question2));
			}
			user.setIsActive(User.IS_ACTIVE);
			user.setIsStorePassword(isStorePassword);
			user.setSyncDate(null);
			daoSession.getUserDao().insertOrReplace(user);			
		}
		return user;
	}
	
	public static void savePersonalData(LoginInfo loginInfo){
		User currentUser = getCurrentUser();
		currentUser.setMaritalStatusId(loginInfo.MaritalStatusId);
		currentUser.setSocialStatusId(loginInfo.SocialStatusId);
		currentUser.setWeight(loginInfo.Weight);
		currentUser.setHeight(loginInfo.Height);
		currentUser.setFootDistance(loginInfo.FootDistance);
		currentUser.setSleepTime(loginInfo.SleepTime);
		currentUser.setPressureId(loginInfo.PressureId);
		DaoSession daoSession = DB.db().newSession();
		daoSession.getUserDao().update(currentUser);
	}
	
	public static boolean isAutoSyncData(){
	    boolean result = true;
	    User user = getCurrentUser();
	    if(user != null){
	        result = user.getIsAutoSync() == 1 ? true : false;
	    }
	    return result;
	}
	
	public static void saveAutoSyncData(boolean isAutoSync){
        User user = getCurrentUser();
        if(user != null){
            user.setIsAutoSync(isAutoSync ? 1 : 0);
            DaoSession daoSession = DB.db().newSession();
    		daoSession.getUserDao().update(user);
        }
    }
	
	public static void savePeriodSyncData(int period){
        User user = getCurrentUser();
        if(user != null){
            user.setPeriodSyncData(period);
            DaoSession daoSession = DB.db().newSession();
    		daoSession.getUserDao().update(user);
        }
    }
	
	public static User getAnonimUser(Context context){
		User prevUser = getCurrentUser();
		DB.db().getDatabase().execSQL("update tbluser set IsActive=0;");		
		User anonimUser = getUserByLogin(User.ANONIM_LOGIN);
		if(anonimUser == null){
			anonimUser = new User();
			anonimUser.setCityId(User.ANONIM_USER_CITY_ID);
		}
		anonimUser.setIsActive(User.IS_ACTIVE);
		anonimUser.setSyncDate(null);
		boolean isDeleteCommonData = true;
		if(prevUser != null && anonimUser.getCity().getId().equals(prevUser.getCity().getId())){
			isDeleteCommonData = false;
		}
		createOrUpdateAnonimUser(context, anonimUser, isDeleteCommonData);		
		return anonimUser;
	}
	
	private static User getUserByLogin(String login){
		User result = null;
		DaoSession daoSession = DB.db().newSession();
		List<User> users = daoSession.getUserDao().queryBuilder().where(UserDao.Properties.Login.eq(login)).list();
		if(users.size() > 0){
			result = users.get(0);
		}
		return result;
	}
	
	private static void createOrUpdateAnonimUser(Context context, User user, boolean isDeleteCommonData){
		DaoSession daoSession = DB.db().newSession();
		user.setId(User.ANONIM_USER_ID);
		user.setLogin(User.ANONIM_LOGIN);
		user.setPassword(User.ANONIM_PASSWORD);
		user.setFName(context.getString(R.string.login_anonim_first_name));
		user.setLName(context.getString(R.string.login_anonim_last_name));
		user.setMName(context.getString(R.string.login_anonim_middle_name));
		user.setCityId(User.ANONIM_USER_CITY_ID);
		GregorianCalendar gc = new GregorianCalendar();
		gc.add(Calendar.YEAR, -50);
		user.setBirthDate(gc.getTime());
		user.setCreateDate(new Date());
		user.setSex(User.ANONIM_SEX);
		user.setPeriodSyncData(DEFAULT_PERIOD_SYNC_DATA);
		daoSession.insertOrReplace(user);		
		deletePersonalDataOnClient();
		if(isDeleteCommonData){
			deleteCommonDataOnClient();			
		}
		DB.insertTestDataForAnonimUser(user);				
	}
	
	
	public static void deleteCommonDataOnClient(){
		SQLiteDatabase db = DB.db().getDatabase();
		db.execSQL("delete from tbldownloadperiod;");
		db.execSQL("delete from tblweatherhourly;");
		db.execSQL("delete from tblmoon;");
		db.execSQL("delete from tblsun;");
		db.execSQL("delete from tblheliophysicsdaily;");
		db.execSQL("delete from tblgeophysicshourly;");
		db.execSQL("delete from tblcomplaint;");
	}
	
	public static void deletePersonalDataOnClient(){
		SQLiteDatabase db = DB.db().getDatabase();
		db.execSQL("delete from tblbodyfeeling;");
		db.execSQL("delete from tblcommonfeeling;");
		db.execSQL("delete from tblfactor;");
		db.execSQL("delete from tbluserbodyfeelingtype;");
	    
		db.execSQL("delete from tblcustombodyfeelingtype;");
		db.execSQL("delete from tblcustomcommonfeelingtype;");
		db.execSQL("delete from tblcustomfactortype;");
	    
		db.execSQL("delete from tbloperationuserdata;");
	}

}
