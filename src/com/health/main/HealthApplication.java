package com.health.main;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;

import com.health.db.DB;
import com.health.repository.DBRepository;
import com.health.repository.IRepository;
import com.health.repository.Repository;
import com.health.settings.Settings;
import com.health.task.TaskManager;

@ReportsCrashes(formKey = "", mailTo = "health.developer.by@gmail.com", 
    mode = ReportingInteractionMode.DIALOG,
    resToastText = R.string.crash_toast_text,
    resDialogText = R.string.crash_dialog_text,
    resDialogIcon = android.R.drawable.ic_dialog_info,
    resDialogTitle = R.string.crash_dialog_title,
    resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
    resDialogOkToast = R.string.crash_dialog_ok_toast,
    customReportContent = { ReportField.USER_COMMENT, ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.DEVICE_ID, ReportField.DEVICE_FEATURES,
        ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT, 
        ReportField.APPLICATION_LOG, ReportField.SHARED_PREFERENCES})
public final class HealthApplication extends Application {

    private IRepository mRepository;
    
    private TaskManager mTaskManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);        
        ACRA.getErrorReporter().checkReportsOnApplicationStart();        
        DB.setContext(getApplicationContext());
        if (Settings.IS_SQLLITE_DB == 0) {            
            this.mRepository = new Repository(this);
        } else {
            this.mRepository = new DBRepository();
        }
        this.mTaskManager = new TaskManager(this, null);
    }

    @Override
    public void onTerminate() {
        DB.release();
        super.onTerminate();
    }

    public IRepository getRepository() {
        return mRepository;
    }
    
    public TaskManager getTaskManager(){
    	return mTaskManager;
    }

    

}
