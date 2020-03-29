package com.health.task;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.data.BaseDTO;
import com.health.data.BodyFeelingDao;
import com.health.data.CommonFeelingDao;
import com.health.data.Complaint;
import com.health.data.BaseSyncDTO;
import com.health.data.BaseUserSyncDTO;
import com.health.data.BodyComplaintType;
import com.health.data.BodyFeeling;
import com.health.data.BodyFeelingType;
import com.health.data.BodyRegion;
import com.health.data.CommonFeeling;
import com.health.data.CommonFeelingGroup;
import com.health.data.CommonFeelingType;
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
import com.health.data.FactorType;
import com.health.data.OperationUserData;
import com.health.data.UnitDimension;
import com.health.data.UserBodyFeelingType;
import com.health.data.UserBodyFeelingTypeDao;
import com.health.data.Weather;
import com.health.db.DB;
import com.health.db.UserDB;
import com.health.settings.Settings;

public class GetUserDataTask extends BaseTask {
	
	public GetUserDataTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb, Context context) {
		super(pl, cb, context);
		this.mTaskName = Settings.TASK_GETUSERDATA;
	}

    @Override
    public void init() {
        this.mProgressDialogTitle = "Загрузка пользовательских данных...";
        this.mProgressDialogMessage = "Подождите";
    }

    @Override
    protected TaskResult doInBackground(String... params) {
    	TaskResult result = new TaskResult();
    	result.setTaskName(this.mTaskName);
    	String resultJSON = null;
    	String url = params[0];    	
        try {
        	Log.i(getClass().getName(), String.format("Start download data from %s", url));
        	resultJSON = getJSON(url);
        	Log.i(getClass().getName(), String.format("End download data from %s", url));
        }
        catch (Exception e) {      
            result.setError(true);
            result.setStatus(TaskResult.Status.SERVER_UNAVALAIBLE);
        }        
        if(resultJSON != null){
            try{
            	long time1 = System.currentTimeMillis();
            	handleResponseGetUserDataJSON(resultJSON);
            	long time2 = System.currentTimeMillis();
            	Log.i("GetUserData_handleResponseGetUserDataJSON", Long.toString(time2 - time1));
            }catch (Exception e) {
            	Log.e("GetUserDataTask", e.getMessage(), e);
                result.setError(true);
                result.setStatus(TaskResult.Status.HANDLE_ERROR);             
            }
        }
        return result;
    }
    
    
    
    private void handleResponseGetUserDataJSON(final String resultJSON) throws JsonProcessingException, IOException{
    	UserData userData = parseUserData(resultJSON);
    	DaoSession daoSession = DB.db().newSession();

		if(userData.CustomBodyFeelingTypeList != null) {
            for(CustomBodyFeelingType userSyncObj : userData.CustomBodyFeelingTypeList){
                if(!OperationUserData.isChangeOnClient(OperationUserData.getTableId(CustomBodyFeelingType.class), userSyncObj.getServerId())){
                	List<CustomBodyFeelingType> findedUserSyncObj = daoSession.getCustomBodyFeelingTypeDao().queryBuilder().where(CustomBodyFeelingTypeDao.Properties.ServerId.eq(userSyncObj.getServerId())).list();                    
                    if(findedUserSyncObj.size() > 0){
                    	CustomBodyFeelingType dbObj = findedUserSyncObj.get(0);
                        userSyncObj.setId(dbObj.getId());
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getCustomBodyFeelingTypeDao().update(userSyncObj);                            
                        } else {
                        	daoSession.getCustomBodyFeelingTypeDao().delete(userSyncObj);                            
                        }
                    } else {
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getCustomBodyFeelingTypeDao().insert(userSyncObj);                            
                        }
                    }                   
                }               
            }
        }
		
		if(userData.CustomFactorTypeList != null) {
			for(CustomFactorType userSyncObj : userData.CustomFactorTypeList){
                if(!OperationUserData.isChangeOnClient(OperationUserData.getTableId(CustomFactorType.class), userSyncObj.getServerId())){
                	List<CustomFactorType> findedUserSyncObj = daoSession.getCustomFactorTypeDao().queryBuilder().where(CustomFactorTypeDao.Properties.ServerId.eq(userSyncObj.getServerId())).list();                    
                    if(findedUserSyncObj.size() > 0){
                    	CustomFactorType dbObj = findedUserSyncObj.get(0);
                        userSyncObj.setId(dbObj.getId());
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getCustomFactorTypeDao().update(userSyncObj);                            
                        } else {
                        	daoSession.getCustomFactorTypeDao().delete(userSyncObj);                            
                        }
                    } else {
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getCustomFactorTypeDao().insert(userSyncObj);                            
                        }
                    }                   
                }               
            }
        }
		
		if(userData.CustomCommonFeelingTypeList != null) {
			for(CustomCommonFeelingType userSyncObj : userData.CustomCommonFeelingTypeList){
                if(!OperationUserData.isChangeOnClient(OperationUserData.getTableId(CustomCommonFeelingType.class), userSyncObj.getServerId())){
                	List<CustomCommonFeelingType> findedUserSyncObj = daoSession.getCustomCommonFeelingTypeDao().queryBuilder().where(CustomCommonFeelingTypeDao.Properties.ServerId.eq(userSyncObj.getServerId())).list();                    
                    if(findedUserSyncObj.size() > 0){
                    	CustomCommonFeelingType dbObj = findedUserSyncObj.get(0);
                        userSyncObj.setId(dbObj.getId());
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getCustomCommonFeelingTypeDao().update(userSyncObj);                            
                        } else {
                        	daoSession.getCustomCommonFeelingTypeDao().delete(userSyncObj);                            
                        }
                    } else {
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getCustomCommonFeelingTypeDao().insert(userSyncObj);                            
                        }
                    }                   
                }               
            }
        }
		
		if(userData.BodyFeelingList != null){
			for(BodyFeeling bodyFeeling : userData.BodyFeelingList){
				if(bodyFeeling.CustomBodyFeelingTypeServerId != null){
				    bodyFeeling.setCustomBodyFeelingType(daoSession.getCustomBodyFeelingTypeDao().queryBuilder().where(CustomBodyFeelingTypeDao.Properties.ServerId.eq(bodyFeeling.CustomBodyFeelingTypeServerId)).list().get(0));
				}
			}
			for(BodyFeeling userSyncObj : userData.BodyFeelingList){
                if(!OperationUserData.isChangeOnClient(OperationUserData.getTableId(BodyFeeling.class), userSyncObj.getServerId())){
                	List<BodyFeeling> findedUserSyncObj = daoSession.getBodyFeelingDao().queryBuilder().where(BodyFeelingDao.Properties.ServerId.eq(userSyncObj.getServerId())).list();                    
                    if(findedUserSyncObj.size() > 0){
                    	BodyFeeling dbObj = findedUserSyncObj.get(0);
                        userSyncObj.setId(dbObj.getId());
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getBodyFeelingDao().update(userSyncObj);                            
                        } else {
                        	daoSession.getBodyFeelingDao().delete(userSyncObj);                            
                        }
                    } else {
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getBodyFeelingDao().insert(userSyncObj);                            
                        }
                    }                   
                }               
            }
		}
		
		if(userData.UserBodyFeelingTypeList != null){
			for(UserBodyFeelingType userSyncObj : userData.UserBodyFeelingTypeList){
                if(!OperationUserData.isChangeOnClient(OperationUserData.getTableId(UserBodyFeelingType.class), userSyncObj.getServerId())){
                	List<UserBodyFeelingType> findedUserSyncObj = daoSession.getUserBodyFeelingTypeDao().queryBuilder().where(UserBodyFeelingTypeDao.Properties.ServerId.eq(userSyncObj.getServerId())).list();                    
                    if(findedUserSyncObj.size() > 0){
                    	UserBodyFeelingType dbObj = findedUserSyncObj.get(0);
                        userSyncObj.setId(dbObj.getId());
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getUserBodyFeelingTypeDao().update(userSyncObj);                            
                        } else {
                        	daoSession.getUserBodyFeelingTypeDao().delete(userSyncObj);                            
                        }
                    } else {
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getUserBodyFeelingTypeDao().insert(userSyncObj);                            
                        }
                    }                   
                }               
            }
        }
		
		if(userData.CommonFeelingList != null){
			for(CommonFeeling commonFeeling : userData.CommonFeelingList){
                if(commonFeeling.CustomCommonFeelingTypeServerId != null){
                	commonFeeling.setCustomCommonFeelingType(daoSession.getCustomCommonFeelingTypeDao().queryBuilder().where(CustomCommonFeelingTypeDao.Properties.ServerId.eq(commonFeeling.CustomCommonFeelingTypeServerId)).list().get(0));
                }
            }
            for(CommonFeeling userSyncObj : userData.CommonFeelingList){
                if(!OperationUserData.isChangeOnClient(OperationUserData.getTableId(CommonFeeling.class), userSyncObj.getServerId())){
                	List<CommonFeeling> findedUserSyncObj = daoSession.getCommonFeelingDao().queryBuilder().where(CommonFeelingDao.Properties.ServerId.eq(userSyncObj.getServerId())).list();                    
                    if(findedUserSyncObj.size() > 0){
                    	CommonFeeling dbObj = findedUserSyncObj.get(0);
                        userSyncObj.setId(dbObj.getId());
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getCommonFeelingDao().update(userSyncObj);                            
                        } else {
                        	daoSession.getCommonFeelingDao().delete(userSyncObj);                            
                        }
                    } else {
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getCommonFeelingDao().insert(userSyncObj);                            
                        }
                    }                   
                }               
            }
        }
		
		if(userData.FactorList != null){
			for(Factor factor : userData.FactorList){
            	if(factor.CustomFactorTypeServerId != null){
				    factor.setCustomFactorType(daoSession.getCustomFactorTypeDao().queryBuilder().where(CustomFactorTypeDao.Properties.ServerId.eq(factor.CustomFactorTypeServerId)).list().get(0));
				}            
            }
            for(Factor userSyncObj : userData.FactorList){
                if(!OperationUserData.isChangeOnClient(OperationUserData.getTableId(Factor.class), userSyncObj.getServerId())){
                	List<Factor> findedUserSyncObj = daoSession.getFactorDao().queryBuilder().where(FactorDao.Properties.ServerId.eq(userSyncObj.getServerId())).list();                    
                    if(findedUserSyncObj.size() > 0){
                    	Factor dbObj = findedUserSyncObj.get(0);
                        userSyncObj.setId(dbObj.getId());
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getFactorDao().update(userSyncObj);                            
                        } else {
                        	daoSession.getFactorDao().delete(userSyncObj);                            
                        }
                    } else {
                        if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
                        	daoSession.getFactorDao().insert(userSyncObj);                            
                        }
                    }                   
                }               
            }
        }
		
		Date userSyncDate = new Date(userData.UnixSyncDate * 1000);
        UserDB.updateSyncDateForUserData(userSyncDate);
								
				
    }
    

    
}
