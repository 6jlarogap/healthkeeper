package com.health.task;

import java.util.Dictionary;
import java.util.Hashtable;

import android.content.Context;

import com.health.settings.Settings;

public class RecoveryPasswordTask extends BaseTask {
	public RecoveryPasswordTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb, Context context) {
		super(pl, cb, context);
		this.mTaskName = Settings.TASK_RECOVERY_PASSWORD_USER;
	}
	
    @Override
    public void init() {

    }

    @Override
    protected TaskResult doInBackground(String... params) {
    	TaskResult result = new TaskResult();
    	result.setTaskName(this.mTaskName);
    	String url = params[0];
    	String login = params[1];
    	String password = params[2];
    	String answer1 = params[3];
    	String answer2 = params[4];
		try {			
			Dictionary<String, String> dictPostData = new Hashtable<String, String>();
        	dictPostData.put(BaseTask.ARG_LOGIN, login);
        	dictPostData.put(BaseTask.ARG_PASSWORD, password);
        	dictPostData.put(BaseTask.ARG_RECOVERYPASSWORD_ANSWER1, answer1);
        	dictPostData.put(BaseTask.ARG_RECOVERYPASSWORD_ANSWER2, answer2);
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
