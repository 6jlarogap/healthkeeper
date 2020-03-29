package com.health.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.health.data.BodyFeeling;
import com.health.data.DaoSession;
import com.health.data.User;
import com.health.db.DB;
import com.health.db.UserDB;
import com.health.settings.Settings;
import com.health.task.AsyncTaskCompleteListener;
import com.health.task.BaseTask;
import com.health.task.GetDataTask;
import com.health.task.GetUserDataTask;
import com.health.task.TaskResult;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class AsyncService extends IntentService implements AsyncTaskCompleteListener<TaskResult> {
	
	public static final String TASK_COMPLETE = "TASK_COMPLETE";

    private static final String TAG = "AsyncService";
    
    public static final String EXTRA_OPERATION = "EXTRA_OPERATION";
    public static final String EXTRA_DTFROM = "EXTRA_DTFROM";
    public static final String EXTRA_DTTO = "EXTRA_DTTO";
	public static final String EXTRA_DIARY_ID = "EXTRA_DIARY_ID";
	public static final String EXTRA_RESULT = "EXTRA_RESULT";
    
    public static final int OPERATION_GETDATA = 1;
	public static final int OPERATION_GETDATA_HOURLY = 2;
	public static final int OPERATION_GETDATA_DAILY = 3;
	public static final int OPERATION_GET_TESTDIARY = 4;

	public static final int RESULT_OK = 0;
	public static final int RESULT_ERROR = 1;


   

    public AsyncService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
    	Bundle bundle =  intent.getExtras();
    	int operation = bundle.getInt(EXTRA_OPERATION);
    	long dtFromTime = bundle.getLong(EXTRA_DTFROM);
    	long dtToTime = bundle.getLong(EXTRA_DTTO);
    	Date dtFrom = new Date(dtFromTime);
    	Date dtTo = new Date(dtToTime);
		int diaryId = bundle.getInt(EXTRA_DIARY_ID, 1);
    	switch (operation) {
		case OPERATION_GETDATA:
			getData(dtFrom, dtTo, operation);
			break;
		case OPERATION_GETDATA_HOURLY:
			getData(dtFrom, dtTo, operation);
			break;
		case OPERATION_GETDATA_DAILY:
			getData(dtFrom, dtTo, operation);
			break;
		case OPERATION_GET_TESTDIARY:
			getTestDiary(diaryId, dtFrom, dtTo, User.ANONIM_USER_CITY_ID);
			break;
		default:
			break;
		}
    }
    
    private void getData(Date dtFrom, Date dtTo, int operation) {
    	TaskResult result = new TaskResult();    	
    	User user = UserDB.getCurrentUser();
    	String args = String.format("cityid=%d&lat=%s&lng=%s&datefrom=%d&dateto=%d&timezone=Etc/GMT%d&hourly=0", user.getCity().getId(), user.getCity().getLat(), user.getCity().getLng(), dtFrom.getTime() / 1000L, dtTo.getTime() / 1000L, dtFrom.getTimezoneOffset() / 60);
		switch (operation){
			case OPERATION_GETDATA_DAILY:
				args = String.format("%s&daily=1&hourly=0", args);
				break;
			case OPERATION_GETDATA_HOURLY:
				args = String.format("%s&daily=0&hourly=1", args);
				break;
		}
    	String url = Settings.getDataUrl(this, args);
    	Log.i(getClass().getName(), url);
    	GetDataTask getDataTask = new GetDataTask(null, this, this);
    	try {
	        String json = getDataTask.getJSON(url);
	        getDataTask.handleResponseGetDataJSON(json, dtFrom, dtTo);
        } catch (IOException ioException) {
	        result.setError(true);
        } catch (ParseException parseException) {
	        result.setError(true);
        }
    	Intent taskComplete = new Intent(TASK_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(taskComplete);
    }

	private void getTestDiary(int diary, Date dtFrom, Date dtTo, long cityId){
		TaskResult result = new TaskResult();
		User user = UserDB.getCurrentUser();
		String url = String.format("%s?diary=%d&datefrom=%d&dateto=%dcityid=%d", Settings.getTestDiaryUrl(getApplicationContext()), diary, (int) (dtFrom.getTime() / 1000L), (int) (dtTo.getTime() / 1000L), cityId);
		Log.i(getClass().getName(), url);
		GetUserDataTask task = new GetUserDataTask(null, this, this);
		try {
			String json = task.getJSON(url);
			BaseTask.UserData userData = task.parseTestDiaryData(json);
			DaoSession daoSession = DB.db().newSession();
			if(userData.BodyFeelingList != null){
				for(BodyFeeling bf : userData.BodyFeelingList){
					daoSession.getBodyFeelingDao().insert(bf);
				}
			}
		} catch (IOException ioException) {
			result.setError(true);
		}
		Intent taskComplete = new Intent(TASK_COMPLETE);
		taskComplete.putExtra(EXTRA_OPERATION, OPERATION_GET_TESTDIARY);
		taskComplete.putExtra(EXTRA_DIARY_ID, diary);
		if(result.isError()){
			taskComplete.putExtra(EXTRA_RESULT, RESULT_ERROR);
		} else {
			taskComplete.putExtra(EXTRA_RESULT, RESULT_OK);
		}
		LocalBroadcastManager.getInstance(this).sendBroadcast(taskComplete);
	}

	@Override
    public void onTaskComplete(BaseTask task, TaskResult result) {
	    //Do nothing	    
    }

}
