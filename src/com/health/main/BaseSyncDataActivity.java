package com.health.main;

import com.health.task.BaseTask;
import com.health.task.TaskManager;
import com.health.task.TaskResult;

import android.app.Activity;
import android.os.Bundle;

public class BaseSyncDataActivity extends Activity implements TaskManager.OnStateTaskListener {

    protected TaskManager mTaskManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Object instance = getLastNonConfigurationInstance();
        if(instance != null && instance instanceof TaskManager){
            this.mTaskManager = (TaskManager) instance;            
        } else {
            this.mTaskManager = ((HealthApplication) getApplication()).getTaskManager();
        }
        this.mTaskManager.checkExecutingTask(this, this);
    }
    
    @Override
    public Object onRetainNonConfigurationInstance() {        
        return mTaskManager.retainExecutingTask();
    }

    @Override
    public void onComplete(BaseTask task, TaskResult taskResult) {        
        
    }

	@Override
    public void onStartTask() {	    
	    
    }

	@Override
    public void onStopTask() {
	    
    }
    
}
