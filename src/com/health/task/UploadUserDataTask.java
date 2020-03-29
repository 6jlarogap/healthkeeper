package com.health.task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.os.IInterface;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.data.BaseDTO;
import com.health.data.BaseUserSyncDTO;
import com.health.data.BodyFeeling;
import com.health.data.BodyFeelingDao;
import com.health.data.CommonFeeling;
import com.health.data.CommonFeelingDao;
import com.health.data.CustomBodyFeelingType;
import com.health.data.CustomBodyFeelingTypeDao;
import com.health.data.CustomCommonFeelingType;
import com.health.data.CustomCommonFeelingTypeDao;
import com.health.data.CustomFactorType;
import com.health.data.CustomFactorTypeDao;
import com.health.data.DaoSession;
import com.health.data.Factor;
import com.health.data.FactorDao;
import com.health.data.IBaseUserSyncDTO;
import com.health.data.OperationUserData;
import com.health.data.OperationUserDataDao;
import com.health.data.User;
import com.health.data.UserBodyFeelingType;
import com.health.data.UserBodyFeelingTypeDao;
import com.health.db.DB;
import com.health.db.UserDB;
import com.health.settings.Settings;


public class UploadUserDataTask extends BaseTask {
	public enum UploadDataType { DICTIONARY_USER_DATA, USER_DATA};
	
	private UploadDataType mUploadDataType; 
	
	public UploadUserDataTask(AsyncTaskProgressListener pl,	AsyncTaskCompleteListener<TaskResult> cb, Context context, UploadDataType type) {
		super(pl, cb, context);
		this.mTaskName = Settings.TASK_UPLOAD_USERDATA;
		this.mUploadDataType = type;
	}
	
    @Override
    public void init() {
        this.mProgressDialogTitle = "Отправка пользовательских данных...";
        this.mProgressDialogMessage = "Подождите";
    }
    
    public UploadDataType getTypeData(){
    	return mUploadDataType;    	
    }

    @Override
    protected TaskResult doInBackground(String... params) {
    	TaskResult result = new TaskResult();
    	result.setTaskName(this.mTaskName);
    	String url = params[0];
    	String login = params[1];
    	String password = params[2];
    	String json = null;
    	User currentUser = UserDB.getCurrentUser();
    	switch (mUploadDataType) {
		case DICTIONARY_USER_DATA:
			json = createJSONDictionaryRequest(currentUser);
			break;
		case USER_DATA:
			json = createJSONRequest(currentUser); 
			break;
		default:
			break;
		}
    	   	
    	if(json != null){
			try {
				Dictionary<String, String> dictPostData = new Hashtable<String, String>();
	        	dictPostData.put(BaseTask.ARG_LOGIN, login);
	        	dictPostData.put(BaseTask.ARG_PASSWORD, password);
	        	dictPostData.put(BaseTask.ARG_DATA, json);
	        	String responseString = postData(url, dictPostData, null);
	        	if(responseString != null){
	        		handleResponseUploadUserDataJSON(responseString);           		
	        	} else {
	        		result.setError(true);
	        		result.setStatus(TaskResult.Status.HANDLE_ERROR);
	        	}            	
	        }
	        catch (Exception e) {
				Log.e("UploadUserDataTask", e.getMessage(), e);
				result.setError(true);
	            result.setStatus(TaskResult.Status.SERVER_UNAVALAIBLE);
	        }
    	}      
        return result;
    }
        
    private String createJSON(UserData userData){
    	String NULL_STRING = "null";
    	StringBuilder sb = new StringBuilder();
    	SimpleDateFormat sdfDateTime = new SimpleDateFormat(BaseDTO.DATETIME_FORMAT);
    	sdfDateTime.setTimeZone(UTC_TIMEZONE);
    	sb.append("{\"udate\":null,\"bodytype\":");
    	if(userData.CustomBodyFeelingTypeList != null){
    		sb.append("[");
    		int i = 0;
    		for(CustomBodyFeelingType item : userData.CustomBodyFeelingTypeList){
    			if(i !=0 ){
    				sb.append(",");
    			}
    			String jsonObj = String.format("{\"id\":%s,\"rid\":\"%s\",\"uid\":\"%d\",\"name\":\"%s\",\"op\":\"%d\"}", item.getServerId() != null ? Long.toString(item.getServerId()) : NULL_STRING,
    					item.getRowId(), item.getUserId(), item.getName(), item.OperationTypeId);
    			sb.append(jsonObj);
    			i++;
    		}
    		sb.append("]");
    	} else {
    		sb.append(NULL_STRING);
    	}
    	
    	sb.append(",\"commontype\":");
    	if(userData.CustomCommonFeelingTypeList != null){
    		sb.append("[");
    		int i = 0;
    		for(CustomCommonFeelingType item : userData.CustomCommonFeelingTypeList){
    			if(i !=0 ){
    				sb.append(",");
    			}
    			String jsonObj = String.format("{\"id\":%s,\"rid\":\"%s\",\"uid\":%d,\"fgid\":%d,\"num\":\"%d\",\"name\":\"%s\",\"status\":%d,\"unitid\":%d,\"op\":\"%d\"}", item.getServerId() != null ? Long.toString(item.getServerId()) : NULL_STRING,
    					item.getRowId(), item.getUserId(), item.getCommonFeelingGroupId(), item.getOrdinalNumber(), item.getName(), item.getStatus(), item.getUnitId(), item.OperationTypeId);
    			sb.append(jsonObj);
    			i++;
    		}
    		sb.append("]");
    	} else {
    		sb.append(NULL_STRING);
    	}
    	
    	sb.append(",\"factortype\":");
    	if(userData.CustomFactorTypeList != null){
    		sb.append("[");
    		int i = 0;
    		for(CustomFactorType item : userData.CustomFactorTypeList){
    			if(i !=0 ){
    				sb.append(",");
    			}
    			String jsonObj = String.format("{\"id\":%s,\"rid\":\"%s\",\"uid\":%d,\"fgid\":%d,\"name\":\"%s\",\"num\":%d,\"status\":%d,\"unitid\":%d,\"op\":\"%d\"}", item.getServerId() != null ? Long.toString(item.getServerId()) : NULL_STRING,
    					item.getRowId(), item.getUserId(), item.getFactorGroupId(), item.getName(), item.getOrdinalNumber(), item.getStatus(), item.getUnitId(), item.OperationTypeId);
    			sb.append(jsonObj);
    			i++;
    		}
    		sb.append("]");
    	} else {
    		sb.append(NULL_STRING);
    	}
    	
    	sb.append(",\"bf\":");
    	if(userData.BodyFeelingList != null){
    		sb.append("[");
    		int i = 0;
    		for(BodyFeeling item : userData.BodyFeelingList){
    			if(i !=0 ){
    				sb.append(",");
    			}    			
    			String jsonObj = String.format("{\"id\":%s,\"uid\":%d,\"rid\":\"%s\",\"dt\":\"%s\",\"ftid\":%s,\"reg\":%d,\"cftid\":%s,\"x\":%s,\"y\":%s,\"op\":%d}", item.getServerId() != null ? Long.toString(item.getServerId()) : NULL_STRING,
    					item.getUserId(), item.getRowId(), item.getStartDate() != null ? sdfDateTime.format(item.getStartDate()) : NULL_STRING,
    					item.getFeelingTypeId() != null ? Long.toString(item.getFeelingTypeId()) : NULL_STRING, item.getBodyRegionId(),
    					item.getCustomFeelingTypeId() != null ? Long.toString(item.getCustomBodyFeelingType().getServerId()) : NULL_STRING,
    	                item.getX() != null ? Integer.toString(item.getX()) : NULL_STRING, item.getY() != null ? Integer.toString(item.getY()) : NULL_STRING, item.OperationTypeId);
    			sb.append(jsonObj);
    			i++;
    		}
    		sb.append("]");
    	} else {
    		sb.append(NULL_STRING);
    	}
    	
    	sb.append(",\"c\":");
    	if(userData.CommonFeelingList != null){
    		sb.append("[");
    		int i = 0;
    		for(CommonFeeling item : userData.CommonFeelingList){
    			if(i !=0 ){
    				sb.append(",");
    			}
    			String jsonObj = String.format("{\"id\":%s,\"uid\":%d,\"rid\":\"%s\",\"dt\":\"%s\",\"ftid\":%s,\"cftid\":%s,\"v1\":%s,\"v2\":%s,\"v3\":%s,\"op\":\"%d\"}", item.getServerId() != null ? Long.toString(item.getServerId()) : NULL_STRING,
    					item.getUserId(), item.getRowId(), item.getStartDate() != null ? sdfDateTime.format(item.getStartDate()) : NULL_STRING,
    					item.getCommonFeelingTypeId() != null ? Long.toString(item.getCommonFeelingTypeId()) : NULL_STRING,
    					item.getCustomCommonFeelingTypeId() != null ? Long.toString(item.getCustomCommonFeelingType().getServerId()) : NULL_STRING,
    					item.getValue1() != null ? Double.toString(item.getValue1()) : NULL_STRING,
    					item.getValue2() != null ? Double.toString(item.getValue2()) : NULL_STRING,
    					item.getValue3() != null ? Double.toString(item.getValue3()) : NULL_STRING,
    	                item.OperationTypeId);
    			sb.append(jsonObj);
    			i++;
    		}
    		sb.append("]");
    	} else {
    		sb.append(NULL_STRING);
    	}
    	
    	sb.append(",\"f\":");
    	if(userData.FactorList != null){
    		sb.append("[");
    		int i = 0;
    		for(Factor item : userData.FactorList){
    			if(i !=0 ){
    				sb.append(",");
    			}
    			String jsonObj = String.format("{\"id\":%s,\"uid\":%d,\"rid\":\"%s\",\"dt\":\"%s\",\"ftid\":%s,\"cfid\":%s,\"v1\":%s,\"v2\":%s,\"v3\":%s,\"op\":\"%d\"}", item.getServerId() != null ? Long.toString(item.getServerId()) : NULL_STRING,
    					item.getUserId(), item.getRowId(), item.getStartDate() != null ? sdfDateTime.format(item.getStartDate()) : NULL_STRING,
    					item.getFactorTypeId() != null ? Long.toString(item.getFactorTypeId()) : NULL_STRING,
    					item.getCustomFactorTypeId() != null ? Long.toString(item.getCustomFactorType().getServerId()) : NULL_STRING,
    					item.getValue1() != null ? Double.toString(item.getValue1()) : NULL_STRING,
    					item.getValue2() != null ? Double.toString(item.getValue2()) : NULL_STRING,
    					item.getValue3() != null ? Double.toString(item.getValue3()) : NULL_STRING,
    	                item.OperationTypeId);
    			sb.append(jsonObj);
    			i++;
    		}
    		sb.append("]");
    	} else {
    		sb.append(NULL_STRING);
    	}
    	
    	sb.append(",\"ubft\":");
    	if(userData.UserBodyFeelingTypeList != null){
    		sb.append("[");
    		int i = 0;
    		for(UserBodyFeelingType item : userData.UserBodyFeelingTypeList){
    			if(i !=0 ){
    				sb.append(",");
    			}
    			String jsonObj = String.format("{\"id\":%s,\"uid\":\"%d\",\"rid\":\"%s\",\"ftid\":%s,\"color\":\"%d\",\"op\":\"%d\"}", item.getServerId() != null ? Long.toString(item.getServerId()) : NULL_STRING,
    					item.getUserId(), item.getRowId(),
    					item.getBodyFeelingTypeId() != null ? Long.toString(item.getBodyFeelingTypeId()) : NULL_STRING,
    					item.getColor(), item.OperationTypeId);
    			sb.append(jsonObj);
    			i++;
    		}
    		sb.append("]");
    	} else {
    		sb.append(NULL_STRING);
    	}
    	
    	
    	sb.append("}");
    	return sb.toString();
    }
    
    private String createJSONRequest(User user){
    	String result = null;
    	ObjectMapper mapper = new ObjectMapper();
    	UserData userData = new UserData();
	
	    //BodyFeeling
		DaoSession daoSession = DB.db().newSession();
		List<OperationUserData> operationUserDataList = daoSession.getOperationUserDataDao().queryBuilder().where(OperationUserDataDao.Properties.TableId.eq(OperationUserData.BODYFEELING_TABLEID)).list();
		List<BodyFeeling> bodyFeelingList = new ArrayList<BodyFeeling>();
		for(OperationUserData op : operationUserDataList){
		    BodyFeeling bodyFeeling = null;
		    if(op.getUserId() == user.getId()){
				if(op.getOperationType() == OperationUserData.INSERT_OPERATION_TYPE || op.getOperationType() == OperationUserData.UPDATE_OPERATION_TYPE){
					bodyFeeling = daoSession.getBodyFeelingDao().load(op.getClientId());    					
					if(bodyFeeling.getCustomBodyFeelingType() != null){    						
						bodyFeeling.CustomBodyFeelingTypeServerId =  bodyFeeling.getCustomBodyFeelingType().getServerId();
					}
					if(bodyFeeling.getBodyFeelingType() != null){
						bodyFeeling.setFeelingTypeId(bodyFeeling.getBodyFeelingType().getId());                            
                    }
					if(bodyFeeling.getBodyRegion() != null){
						bodyFeeling.setBodyRegionId(bodyFeeling.getBodyRegion().getId());
                    }
				}
				if(op.getOperationType() == OperationUserData.DELETE_OPERATION_TYPE){
					bodyFeeling = new BodyFeeling();
					bodyFeeling.setId(op.getClientId());
					bodyFeeling.setServerId(op.getServerId());
					bodyFeeling.setRowId(op.getRowId());
				}
				bodyFeeling.OperationTypeId = op.getOperationType();
				bodyFeelingList.add(bodyFeeling);
		    }
		}
		userData.BodyFeelingList = bodyFeelingList;
		
		//UserBodyFeelingType
		operationUserDataList = daoSession.getOperationUserDataDao().queryBuilder().where(OperationUserDataDao.Properties.TableId.eq(OperationUserData.USERBODYFEELINGTYPE_TABLEID)).list();			
        List<UserBodyFeelingType> userBodyFeelingTypeList = new ArrayList<UserBodyFeelingType>();
        for(OperationUserData op : operationUserDataList){
            UserBodyFeelingType userBodyFeelingType = null;
            if(op.getUserId() == user.getId()){
                if(op.getOperationType() == OperationUserData.INSERT_OPERATION_TYPE || op.getOperationType() == OperationUserData.UPDATE_OPERATION_TYPE){
                    userBodyFeelingType = daoSession.getUserBodyFeelingTypeDao().load(op.getClientId());
                }
                if(op.getOperationType() == OperationUserData.DELETE_OPERATION_TYPE){
                    userBodyFeelingType = new UserBodyFeelingType();
                    userBodyFeelingType.setId(op.getClientId());
                    userBodyFeelingType.setServerId(op.getServerId());
                    userBodyFeelingType.setRowId(op.getRowId());
                }
                userBodyFeelingType.OperationTypeId = op.getOperationType();
                userBodyFeelingTypeList.add(userBodyFeelingType);
            }
        }
        userData.UserBodyFeelingTypeList = userBodyFeelingTypeList;
        
        //CommonFeeling
        operationUserDataList = daoSession.getOperationUserDataDao().queryBuilder().where(OperationUserDataDao.Properties.TableId.eq(OperationUserData.COMMONFEELING_TABLEID)).list();			
		List<CommonFeeling> commonFeelingList = new ArrayList<CommonFeeling>();
		for(OperationUserData op : operationUserDataList){
		    CommonFeeling commonFeeling = null;
		    if(op.getUserId() == user.getId()){
				if(op.getOperationType() == OperationUserData.INSERT_OPERATION_TYPE || op.getOperationType() == OperationUserData.UPDATE_OPERATION_TYPE){
					commonFeeling = daoSession.getCommonFeelingDao().load(op.getClientId());
					if(commonFeeling.getCommonFeelingType() != null){
						commonFeeling.setCommonFeelingTypeId(commonFeeling.getCommonFeelingType().getId());
					}
					if(commonFeeling.getCustomCommonFeelingType() != null){
						commonFeeling.CustomCommonFeelingTypeServerId = commonFeeling.getCustomCommonFeelingType().getServerId();
					}
				}
				if(op.getOperationType() == OperationUserData.DELETE_OPERATION_TYPE){
					commonFeeling = new CommonFeeling();
					commonFeeling.setId(op.getClientId());
					commonFeeling.setServerId(op.getServerId());
					commonFeeling.setRowId(op.getRowId());
				}
				commonFeeling.OperationTypeId = op.getOperationType();
				commonFeelingList.add(commonFeeling);
		    }
		}
		userData.CommonFeelingList = commonFeelingList;

        //Factor
		operationUserDataList = daoSession.getOperationUserDataDao().queryBuilder().where(OperationUserDataDao.Properties.TableId.eq(OperationUserData.FACTOR_TABLEID)).list();			
		List<Factor> factorList = new ArrayList<Factor>();
		for(OperationUserData op : operationUserDataList){
			Factor factor = null;
			if(op.getUserId() == user.getId()){
				if(op.getOperationType() == OperationUserData.INSERT_OPERATION_TYPE || op.getOperationType() == OperationUserData.UPDATE_OPERATION_TYPE){
					factor = daoSession.getFactorDao().load(op.getClientId());
					if(factor.getFactorType() != null){
						factor.setFactorTypeId(factor.getFactorTypeId());
					}
					if(factor.getCustomFactorType() != null){    						
						factor.CustomFactorTypeServerId =  factor.getCustomFactorType().getServerId();
					}
				}
				if(op.getOperationType() == OperationUserData.DELETE_OPERATION_TYPE){
					factor = new Factor();
					factor.setId(op.getClientId());
					factor.setServerId(op.getServerId());
					factor.setRowId(op.getRowId());
				}
				factor.OperationTypeId = op.getOperationType();
				factorList.add(factor);
			}
		}
		userData.FactorList = factorList;
		
		if(userData.IsExistDataForSending()){			
			//result = mapper.writeValueAsString(userData);
			result = createJSON(userData);
		}		
		
    	return result;
    }
    
    private String createJSONDictionaryRequest(User user){
    	String result = null;
    	ObjectMapper mapper = new ObjectMapper();
    	UserData userData = new UserData();
	
		DaoSession daoSession = DB.db().newSession();
		
        //CustomBodyFeelingType
		List<OperationUserData> operationUserDataList = daoSession.getOperationUserDataDao().queryBuilder().where(OperationUserDataDao.Properties.TableId.eq(OperationUserData.CUSTOMBODYFEELINGTYPE_TABLEID)).list();            
		List<CustomBodyFeelingType> items = new ArrayList<CustomBodyFeelingType>();
		for(OperationUserData op : operationUserDataList){
		    CustomBodyFeelingType item = null;
		    if(op.getUserId() == user.getId()){
				if(op.getOperationType() == OperationUserData.INSERT_OPERATION_TYPE || op.getOperationType() == OperationUserData.UPDATE_OPERATION_TYPE){
					item = daoSession.getCustomBodyFeelingTypeDao().load(op.getClientId());										
				}
				if(op.getOperationType() == OperationUserData.DELETE_OPERATION_TYPE){
					item = new CustomBodyFeelingType();
					item.setId(op.getClientId());
					item.setServerId(op.getServerId());
					item.setRowId(op.getRowId());
				}
				if(item != null){
    				item.OperationTypeId = op.getOperationType();
    				items.add(item);
				}
		    }
		}
		userData.CustomBodyFeelingTypeList = items;
		
		//CustomFactorType
		operationUserDataList = daoSession.getOperationUserDataDao().queryBuilder().where(OperationUserDataDao.Properties.TableId.eq(OperationUserData.CUSTOMFACTORTYPE_TABLEID)).list();
        List<CustomFactorType> customFactorTypes = new ArrayList<CustomFactorType>();
		for(OperationUserData op : operationUserDataList){
		    CustomFactorType customFactorType = null;
		    if(op.getUserId() == user.getId()){
				if(op.getOperationType() == OperationUserData.INSERT_OPERATION_TYPE || op.getOperationType() == OperationUserData.UPDATE_OPERATION_TYPE){
					customFactorType = daoSession.getCustomFactorTypeDao().load(op.getClientId());
					if(customFactorType.getFactorGroup() != null){
						customFactorType.setFactorGroupId(customFactorType.getFactorGroup().getId());
                    }
					if(customFactorType.getUnitDimension() != null){
						customFactorType.setUnitDimensionId(customFactorType.getUnitDimension().getId());
					}
				}
				if(op.getOperationType() == OperationUserData.DELETE_OPERATION_TYPE){
					customFactorType = new CustomFactorType();
					customFactorType.setId(op.getClientId());
					customFactorType.setServerId(op.getServerId());
					customFactorType.setRowId(op.getRowId());
				}
				customFactorType.OperationTypeId = op.getOperationType();
				customFactorTypes.add(customFactorType);
		    }
		}
		userData.CustomFactorTypeList = customFactorTypes;
		
		//CustomCommonFeelingType
		operationUserDataList = daoSession.getOperationUserDataDao().queryBuilder().where(OperationUserDataDao.Properties.TableId.eq(OperationUserData.CUSTOMCOMMONFEELINGTYPE_TABLEID)).list();
        List<CustomCommonFeelingType> customCommonFeelingTypes = new ArrayList<CustomCommonFeelingType>();
		for(OperationUserData op : operationUserDataList){
			CustomCommonFeelingType customCommonFeelingType = null;
			if(op.getUserId() == user.getId()){
				if(op.getOperationType() == OperationUserData.INSERT_OPERATION_TYPE || op.getOperationType() == OperationUserData.UPDATE_OPERATION_TYPE){
					customCommonFeelingType = daoSession.getCustomCommonFeelingTypeDao().load(op.getClientId());
					if(customCommonFeelingType.getCommonFeelingGroup() != null){
						customCommonFeelingType.setCommonFeelingGroupId(customCommonFeelingType.getCommonFeelingGroup().getId());
                    }
					if(customCommonFeelingType.getUnitDimension() != null){
						customCommonFeelingType.setUnitDimensionId(customCommonFeelingType.getUnitDimension().getId());
					}
				}
				if(op.getOperationType() == OperationUserData.DELETE_OPERATION_TYPE){
					customCommonFeelingType = new CustomCommonFeelingType();
					customCommonFeelingType.setId(op.getClientId());
					customCommonFeelingType.setServerId(op.getServerId());
					customCommonFeelingType.setRowId(op.getRowId());
				}
				customCommonFeelingType.OperationTypeId = op.getOperationType();
				customCommonFeelingTypes.add(customCommonFeelingType);
			}
		}
		userData.CustomCommonFeelingTypeList = customCommonFeelingTypes;
		
		if(userData.IsExistDataForSending()){			
			//result = mapper.writeValueAsString(userData);
			result = createJSON(userData);
		}			
		
    	return result;
    }
    
    private void handleResponseUploadUserDataJSON(String resultJSON) throws JsonProcessingException, IOException{
    	UserData userData = parseUserData(resultJSON);
    	if(userData != null){
    		DaoSession daoSession = DB.db().newSession();
    		if(userData.CustomBodyFeelingTypeList != null){
    			for(CustomBodyFeelingType userSyncObj : userData.CustomBodyFeelingTypeList){
    				CustomBodyFeelingType dbUserSyncObj = null;
    	            if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){
    	            	List<CustomBodyFeelingType> dbList = daoSession.getCustomBodyFeelingTypeDao().queryBuilder().where(CustomBodyFeelingTypeDao.Properties.RowId.eq(userSyncObj.getRowId())).list();
    	            	if(dbList.size() > 0){
    	            		dbUserSyncObj = dbList.get(0);
        	                dbUserSyncObj.setServerId(userSyncObj.getServerId());
        	                daoSession.getCustomBodyFeelingTypeDao().update(dbUserSyncObj);
        	                OperationUserData.deleteOperationUserData(OperationUserData.getTableId(CustomBodyFeelingType.class), dbUserSyncObj.getId());
    	            	}    	            	  	                
    	            } else {
    	            	OperationUserData.deleteOperationUserData(userSyncObj.getRowId());
    	            }
    	        }
    		}
    		if(userData.CustomFactorTypeList != null){
    			for(CustomFactorType userSyncObj : userData.CustomFactorTypeList){
    	            if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){    	            	
    	            	CustomFactorType dbUserSyncObj = daoSession.getCustomFactorTypeDao().queryBuilder().where(CustomFactorTypeDao.Properties.RowId.eq(userSyncObj.getRowId())).list().get(0);
    	                dbUserSyncObj.setServerId(userSyncObj.getServerId());
    	                daoSession.getCustomFactorTypeDao().update(dbUserSyncObj);
    	                OperationUserData.deleteOperationUserData(OperationUserData.getTableId(CustomFactorType.class), dbUserSyncObj.getId());
    	            } else {
    	            	OperationUserData.deleteOperationUserData(userSyncObj.getRowId());
    	            }    	            
    	        }
    		}
    		if(userData.CustomCommonFeelingTypeList != null){
    			for(CustomCommonFeelingType userSyncObj : userData.CustomCommonFeelingTypeList){
    	            if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){    	            	
    	            	CustomCommonFeelingType dbUserSyncObj = daoSession.getCustomCommonFeelingTypeDao().queryBuilder().where(CustomCommonFeelingTypeDao.Properties.RowId.eq(userSyncObj.getRowId())).list().get(0);
    	                dbUserSyncObj.setServerId(userSyncObj.getServerId());
    	                daoSession.getCustomCommonFeelingTypeDao().update(dbUserSyncObj);
    	                OperationUserData.deleteOperationUserData(OperationUserData.getTableId(CustomCommonFeelingType.class), dbUserSyncObj.getId());
    	            } else {
    	            	OperationUserData.deleteOperationUserData(userSyncObj.getRowId());
    	            }    	            
    	        }
    		}
    		if(userData.BodyFeelingList != null){
    			for(BodyFeeling userSyncObj : userData.BodyFeelingList){
    	            if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){    	            	
    	            	BodyFeeling dbUserSyncObj = daoSession.getBodyFeelingDao().queryBuilder().where(BodyFeelingDao.Properties.RowId.eq(userSyncObj.getRowId())).list().get(0);
    	                dbUserSyncObj.setServerId(userSyncObj.getServerId());
    	                daoSession.getBodyFeelingDao().update(dbUserSyncObj);
    	                OperationUserData.deleteOperationUserData(OperationUserData.getTableId(BodyFeeling.class), dbUserSyncObj.getId());
    	            } else {
    	            	OperationUserData.deleteOperationUserData(userSyncObj.getRowId());
    	            }    	            
    	        }
    		}
    		if(userData.UserBodyFeelingTypeList != null){
    			for(UserBodyFeelingType userSyncObj : userData.UserBodyFeelingTypeList){
    	            if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){    	            	
    	            	UserBodyFeelingType dbUserSyncObj = daoSession.getUserBodyFeelingTypeDao().queryBuilder().where(UserBodyFeelingTypeDao.Properties.RowId.eq(userSyncObj.getRowId())).list().get(0);
    	                dbUserSyncObj.setServerId(userSyncObj.getServerId());
    	                daoSession.getUserBodyFeelingTypeDao().update(dbUserSyncObj);
    	                OperationUserData.deleteOperationUserData(OperationUserData.getTableId(UserBodyFeelingType.class), dbUserSyncObj.getId());
    	            } else {
    	            	OperationUserData.deleteOperationUserData(userSyncObj.getRowId());
    	            }
    	        }
            }
    		if(userData.CommonFeelingList != null){
    			for(CommonFeeling userSyncObj : userData.CommonFeelingList){
    	            if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){    	            	
    	            	CommonFeeling dbUserSyncObj = daoSession.getCommonFeelingDao().queryBuilder().where(CommonFeelingDao.Properties.RowId.eq(userSyncObj.getRowId())).list().get(0);
    	                dbUserSyncObj.setServerId(userSyncObj.getServerId());
    	                daoSession.getCommonFeelingDao().update(dbUserSyncObj);
    	                OperationUserData.deleteOperationUserData(OperationUserData.getTableId(CommonFeeling.class), dbUserSyncObj.getId());
    	            } else {
    	            	OperationUserData.deleteOperationUserData(userSyncObj.getRowId());
    	            }    	            
    	        }
    		}
    		if(userData.FactorList != null){
    			for(Factor userSyncObj : userData.FactorList){
    	            if(userSyncObj.OperationTypeId != OperationUserData.DELETE_OPERATION_TYPE){    	            	
    	            	Factor dbUserSyncObj = daoSession.getFactorDao().queryBuilder().where(FactorDao.Properties.RowId.eq(userSyncObj.getRowId())).list().get(0);
    	                dbUserSyncObj.setServerId(userSyncObj.getServerId());
    	                daoSession.getFactorDao().update(dbUserSyncObj);
    	                OperationUserData.deleteOperationUserData(OperationUserData.getTableId(Factor.class), dbUserSyncObj.getId());
    	            } else {
    	            	OperationUserData.deleteOperationUserData(userSyncObj.getRowId());
    	            }
    	        }
    		}
    	}
    }
        
}
