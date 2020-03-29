package com.health.task;

import java.util.Dictionary;
import java.util.Hashtable;

import android.content.Context;

import com.health.db.UserDB;
import com.health.settings.Settings;


public class UpdatePersonalDataTask extends BaseTask{
	public UpdatePersonalDataTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb, Context context) {
		super(pl, cb, context);
		this.mTaskName = Settings.TASK_UPDATEPERSONALDATA_USER;
	}
	
    @Override
    public void init() {
    	this.mProgressDialogTitle = "Сохранение личных данных...";
        this.mProgressDialogMessage = "Подождите";
    }
    
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected TaskResult doInBackground(String... params) {
    	TaskResult result = new TaskResult();
    	result.setTaskName(this.mTaskName);
    	String url = params[0];
    	String login = params[1];
    	String password = params[2];
    	String firstName = params[3];
    	String lastName = params[4];
    	String middleName = params[5];
    	String birthDateString = params[6];
    	String sex = params[7];
    	String cityId = params[8];
    	String isEdit = "1";
    	String maritalStatusId = params[9];
    	String socilaStatusId = params[10];
    	String height = params[11];
    	String weight = params[12];
    	String pressureid = params[13];
    	String footdistance = params[14];
    	String sleeptime = params[15];
		try {			
			Dictionary<String, String> dictPostData = new Hashtable<String, String>();
        	dictPostData.put(BaseTask.ARG_LOGIN, login);
        	dictPostData.put(BaseTask.ARG_PASSWORD, password);
        	dictPostData.put("fname", firstName);
        	dictPostData.put("lname", lastName);
        	dictPostData.put("mname", middleName);
        	dictPostData.put("birthdate", birthDateString);
        	dictPostData.put("sex", sex);
        	dictPostData.put("cityid", cityId);
        	dictPostData.put("isedit", isEdit);
        	
        	dictPostData.put("marital_status_id", maritalStatusId);
        	dictPostData.put("social_status_id", socilaStatusId);
        	dictPostData.put("height", height);
        	dictPostData.put("weight", weight);
        	dictPostData.put("pressure_id", pressureid);
        	dictPostData.put("foot_distance", footdistance);
        	dictPostData.put("sleep_time", sleeptime);        	
        	String responseString = postData(url, dictPostData, null);        	
    		LoginInfo loginInfo = deserializeLoginInfoJSON(responseString);
    		if(loginInfo != null && loginInfo.IsSuccess()){    			
    			result.setStatus(TaskResult.Status.LOGIN_SUCCESSED);
    			UserDB.savePersonalData(loginInfo);
    		} else {
    			result.setError(true);
    			result.setErrorText((loginInfo != null) ? loginInfo.toString() : null);
    			result.setStatus(TaskResult.Status.LOGIN_FAILED);
    		}
    		result.setLoginInfo(loginInfo);
        	            	
        }
        catch (Exception e) {                
            result.setError(true);
            result.setErrorText(this.networkError);
            result.setStatus(TaskResult.Status.SERVER_UNAVALAIBLE);
        }
    	     
        return result;
    }
    
    @Override
    protected void onProgressUpdate(String... messages) {
        super.onProgressUpdate(messages);
    }

    @Override
    protected void onCancelled(TaskResult result) {        
        super.onCancelled(result);
    }
    
    @Override
    protected void onPostExecute(TaskResult result) {        
        super.onPostExecute(result);
    }

}
