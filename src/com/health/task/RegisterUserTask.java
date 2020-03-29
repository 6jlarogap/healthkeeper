package com.health.task;

import java.util.Dictionary;
import java.util.Hashtable;

import android.content.Context;

import com.health.settings.Settings;


public class RegisterUserTask extends BaseTask{
	public RegisterUserTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb, Context context) {
		super(pl, cb, context);
		this.mTaskName = Settings.TASK_REGISTER_USER;
	}
	
    @Override
    public void init() {

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
    	String question1 = params[9];
    	String answer1 = params[10];
    	String question2 = params[11];
    	String answer2 = params[12];
    	String captcha = params[13];
    	String cookiesValue = params[14];
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
        	dictPostData.put("question1", question1);
        	dictPostData.put("answer1", answer1);
        	dictPostData.put("question2", question2);
        	dictPostData.put("answer2", answer2);
        	dictPostData.put("keystring", captcha);
        	
        	
        	String responseString = postData(url, dictPostData, cookiesValue);        	
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
