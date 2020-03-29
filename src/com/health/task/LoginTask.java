package com.health.task;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import android.content.Context;

import com.health.settings.Settings;

public class LoginTask extends BaseTask {
	public LoginTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb, Context context) {
		super(pl, cb, context);
		this.mTaskName = Settings.TASK_LOGIN_USER;
	}
	
    @Override
    public void init() {

    }

    @Override
    protected TaskResult doInBackground(String... params) {
    	TaskResult result = new TaskResult();
    	result.setTaskName(this.mTaskName);    	
    	if(Settings.IS_SQLLITE_DB == 0){
    		result.setStatus(TaskResult.Status.LOGIN_SUCCESSED);
    		LoginInfo testLoginInfo = new LoginInfo();
    		testLoginInfo.FName = "Имя";
    		testLoginInfo.LName = "Фамилия";
    		testLoginInfo.MName = "Отчество";
    		testLoginInfo.BirthDate = new Date();
    		testLoginInfo.Login = "login";
    		testLoginInfo.Password = "password";
    		testLoginInfo.Sex = 1;
    		result.setLoginInfo(testLoginInfo);
    		return result;
    	}
    	String url = params[0];
    	String login = params[1];
    	String password = params[2];    	    	
		try {			
			Dictionary<String, String> dictPostData = new Hashtable<String, String>();
        	dictPostData.put(BaseTask.ARG_LOGIN, login);
        	dictPostData.put(BaseTask.ARG_PASSWORD, password);      	
        	String responseString = postData(url, dictPostData, null);        	
    		LoginInfo loginInfo = deserializeLoginInfoJSON(responseString);
    		if(loginInfo != null && loginInfo.IsSuccess()){    			
    			result.setStatus(TaskResult.Status.LOGIN_SUCCESSED);
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

}
