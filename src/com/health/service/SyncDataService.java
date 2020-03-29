package com.health.service;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.health.data.User;
import com.health.db.UserDB;
import com.health.main.HealthApplication;
import com.health.task.BaseTask;
import com.health.task.TaskManager;
import com.health.task.TaskManager.OnStateTaskListener;
import com.health.task.TaskResult;

public class SyncDataService extends Service implements OnStateTaskListener {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.i("SyncDataService", "onStart");
		User user = UserDB.getCurrentUser();
		if(user != null && !user.isAnonim()){
			TaskManager mTaskManager = ((HealthApplication)getApplication()).getTaskManager();
			if(!mTaskManager.isRunning()){
				Date dtNow = new Date();
				Date dtFrom = new Date(dtNow.getTime() - 30 * 86400 * 1000);
				Date dtTo = new Date(dtNow.getTime() + 30 * 86400 * 1000);
				mTaskManager.startSyncData(this, this, false, dtFrom, dtTo);
			}
		}
	}

	@Override
	public void onDestroy() {		
		super.onDestroy();
	}

	@Override
    public void onComplete(BaseTask task, TaskResult taskResult) {
	    // TODO Auto-generated method stub	    
    }

	@Override
    public void onStartTask() {
	    // TODO Auto-generated method stub	    
    }

	@Override
    public void onStopTask() {
	    // TODO Auto-generated method stub	    
    }

}
