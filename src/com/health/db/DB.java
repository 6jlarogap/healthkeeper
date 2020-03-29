package com.health.db;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.health.data.BaseDTO;
import com.health.data.BodyFeeling;
import com.health.data.CommonFeeling;
import com.health.data.DaoMaster;
import com.health.data.DaoSession;
import com.health.data.User;
import com.health.data.UserBodyFeelingType;
import com.health.util.Utils;

public class DB {
	
    //public static final String DATABASE_NAME = "/mnt/sdcard/health.db";
    public static final String DATABASE_NAME = "health.db";
    
    private static Context mContext;
    
    private static DaoMaster mDaoMaster;
    
    public static DaoMaster db(){
    	if(mDaoMaster == null){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, DATABASE_NAME, null);
            //db = SQLiteDatabase.openOrCreateDatabase(path, null);
            //helper.onCreate(db);
            SQLiteDatabase db = helper.getWritableDatabase();
            mDaoMaster = new DaoMaster(db);
    	}
        return mDaoMaster;
    }

    public static void setContext(Context context){
        mContext = context;
    }    
    public static void release(){
        
    }   
    
    public static void insertDictionaryTable(SQLiteDatabase db){    	
    	executeSqlScriptFromAssets(db);
    }
    
    private static ArrayList<String> getArraySqlQuery(String assetName){
    	ArrayList<String> result = new ArrayList<String>();
    	try {
			InputStream is =  mContext.getAssets().open(assetName);
			java.util.Scanner scanner = new java.util.Scanner(is).useDelimiter(";");
			while (scanner.hasNext()) {
				String statement = scanner.next().trim();
				if (!TextUtils.isEmpty(statement)) {
					result.add(statement);
				}
			}

		} catch (IOException e) {
			Log.e("assetError", String.format("%s не найден в assets", assetName));
			throw new RuntimeException(e);
		}
    	return result;
    }
    
    private static void executeSqlStatements(SQLiteDatabase db, List<String> queryList){
    	for(String query : queryList){
    		db.execSQL(query);
    	}
    }
    
    private static void executeSqlScriptFromAssets(SQLiteDatabase db){
    	executeSqlStatements(db, getArraySqlQuery("tblrecoveryaccount_question.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblunitdimension.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblcloudytype.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblwinddirections.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblzodiaks.sql"));    	
    	executeSqlStatements(db, getArraySqlQuery("tblbodycomplainttype.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblbodyregion.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblbodyfeelingtype.sql"));    	
    	executeSqlStatements(db, getArraySqlQuery("tblbodyregion_feelingtype.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblcommonfeelinggroup.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblcommonfeelingtype.sql"));
      	executeSqlStatements(db, getArraySqlQuery("tblcountry.sql"));    	
    	executeSqlStatements(db, getArraySqlQuery("tblcity.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblkpindex.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblfactorgroup.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tblfactortype.sql"));
    }
    
    public static void insertTestDataForAnonimUser(User user){    	
    	SQLiteDatabase db = DB.db().getDatabase();
    	executeSqlStatements(db, getArraySqlQuery("tblcommonfeeling.sql"));
    	executeSqlStatements(db, getArraySqlQuery("tbluserbodyfeelingtype.sql"));
    	Date nowDate = new Date();
    	Date maxDate = new Date(114, 11, 18); // 2014-12-18
    	int deltaDays = Utils.getDaysBetween(maxDate, nowDate);
    	DaoSession daoSession = DB.db().newSession();    	
    	/*List<BodyFeeling> bodyFeelingList =  daoSession.getBodyFeelingDao().queryBuilder().list();
    	for(BodyFeeling bf : bodyFeelingList){
    		bf.setUserId(user.getId());    		
    		GregorianCalendar gc = new GregorianCalendar();
    		gc.setTime(bf.getStartDate());
    		gc.add(Calendar.DAY_OF_YEAR, deltaDays);
    		bf.setStartDate(gc.getTime());    		
    	}
    	daoSession.getBodyFeelingDao().updateInTx(bodyFeelingList);*/
    	
    	/*List<CommonFeeling> commonFeelingList =  daoSession.getCommonFeelingDao().queryBuilder().list();
    	for(CommonFeeling cf : commonFeelingList){
    		cf.setUserId(user.getId());
    		GregorianCalendar gc = new GregorianCalendar();
    		gc.setTime(cf.getStartDate());
    		gc.add(Calendar.DAY_OF_YEAR, deltaDays);
    		cf.setStartDate(gc.getTime());    		
    	}
    	daoSession.getCommonFeelingDao().updateInTx(commonFeelingList);*/
    	
    	List<UserBodyFeelingType> list =  daoSession.getUserBodyFeelingTypeDao().queryBuilder().list();
    	for(UserBodyFeelingType dto : list){
    		dto.setUserId(user.getId());
    	}
    	daoSession.getUserBodyFeelingTypeDao().updateInTx(list);
    }
}