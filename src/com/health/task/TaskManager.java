package com.health.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import com.health.data.BaseDTO;
import com.health.data.User;
import com.health.db.UserDB;
import com.health.main.R;
import com.health.settings.Settings;
import com.health.task.UploadUserDataTask.UploadDataType;

public class TaskManager implements AsyncTaskCompleteListener<TaskResult>, AsyncTaskProgressListener {

	public interface OnStateTaskListener {
		public void onComplete(BaseTask task, TaskResult taskResult);
		public void onStartTask();
		public void onStopTask();
	}

	public enum OperationType {
		GET_COMMON_DATA, GET_ONLY_USER_DATA, GET_ALL_DATA, UPLOAD_DATA, SYNC_DATA, UPDATE_PERSONAL_DATA
	};

	private Context mContext;
	private Activity mCurrentActivity = null;
	private ProgressDialog mProgressDialog;
	private OnStateTaskListener mOnTaskCompleteListener;	
	private String mProgressDialogTitle, mPreogressDialogMessage;
	private int mExecutingTaskCount = 0;
	private boolean mIsShowDialogProgress = false;
	private OperationType mOperationType;
	private BaseTask mCurrentExecutedTask = null;

	public TaskManager(Context context, OnStateTaskListener onTaskCompleteListener) {	
		init(context, onTaskCompleteListener);
	}
	
	public boolean isRunning(){
		return this.mExecutingTaskCount > 0;
	}
	
	public void cancelTasks(){
		if(isRunning()){
			if(mCurrentExecutedTask != null){
				if(mCurrentExecutedTask.getStatus() != AsyncTask.Status.FINISHED){
					mCurrentExecutedTask.cancel(true);
				}
			}
		}
	}

	public void checkExecutingTask(Context context, OnStateTaskListener onTaskCompleteListener) {
		init(context, onTaskCompleteListener);
		if (this.mExecutingTaskCount > 0) {
			if (mIsShowDialogProgress) {
				showProgressDialogGetData(this.mProgressDialogTitle, this.mPreogressDialogMessage);
			}
		}
	}

	public TaskManager retainExecutingTask() {
		if (this.mProgressDialog.isShowing()) {
			this.mProgressDialog.dismiss();
		}
		return this;
	}

	private void init(Context context, OnStateTaskListener onTaskCompleteListener) {
		this.mContext = context;
		this.mOnTaskCompleteListener = onTaskCompleteListener;		
		this.mProgressDialog = new ProgressDialog(this.mContext);
		this.mProgressDialog.setIndeterminate(true);
		this.mProgressDialog.setCancelable(false);
	}

	public void startGetAllData(Activity activity, OnStateTaskListener onTaskCompleteListener, boolean isShowDialogProgress, Date dtFrom, Date dtTo) {
		if(!isRunning()){
    		Log.i("startGetAllData", "startGetAllData");
    		if(mOnTaskCompleteListener != null){
    			mOnTaskCompleteListener.onStartTask();
    		}
    		init(activity, onTaskCompleteListener);
    		this.mCurrentActivity = activity;
    		this.mIsShowDialogProgress = isShowDialogProgress;
    		this.mExecutingTaskCount = 2;
    		this.mOperationType = OperationType.GET_ALL_DATA;
    		startGetData(dtFrom, dtTo);
		}

	}
	
	public void startGetCommonData(Activity activity, OnStateTaskListener onTaskCompleteListener, boolean isShowDialogProgress, Date dtFrom, Date dtTo) {
		if(!isRunning()){
    		Log.i("startGetCommonData", "startGetCommonData");
    		if(mOnTaskCompleteListener != null){
    			mOnTaskCompleteListener.onStartTask();
    		}
    		init(activity, onTaskCompleteListener);
    		this.mCurrentActivity = activity;
    		this.mIsShowDialogProgress = isShowDialogProgress;
    		this.mExecutingTaskCount = 1;
    		this.mOperationType = OperationType.GET_COMMON_DATA;
    		startGetData(dtFrom, dtTo);
		}
	}
	
	public void startGetOnlyUserData(Activity activity, OnStateTaskListener onTaskCompleteListener, boolean isShowDialogProgress) {
		if(!isRunning()){
    		Log.i("startGetOnlyUserData", "startGetOnlyUserData");
    		if(mOnTaskCompleteListener != null){
    			mOnTaskCompleteListener.onStartTask();
    		}
    		init(activity, onTaskCompleteListener);
    		this.mCurrentActivity = activity;
    		this.mIsShowDialogProgress = isShowDialogProgress;
    		this.mExecutingTaskCount = 1;
    		this.mOperationType = OperationType.GET_ONLY_USER_DATA;
    		startGetUserData();
		}
	}

	public void startUploadUserData(Activity activity, OnStateTaskListener onTaskCompleteListener, boolean isShowDialogProgress) {
		if(!isRunning()){
    		Log.i("startUploadUserData", "startUploadUserData");		
    		if(mOnTaskCompleteListener != null){
    			mOnTaskCompleteListener.onStartTask();
    		}
    		init(activity, onTaskCompleteListener);
    		this.mCurrentActivity = activity;
    		this.mIsShowDialogProgress = isShowDialogProgress;
    		this.mExecutingTaskCount = 2;
    		this.mOperationType = OperationType.UPLOAD_DATA;
    		startUploadUserData(UploadDataType.DICTIONARY_USER_DATA);
		}
	}

	public void startSyncData(Context context, OnStateTaskListener onTaskCompleteListener, boolean isShowDialogProgress, Date dtFrom, Date dtTo) {
		if(!isRunning()){
    		Log.i("startSyncData", "startSyncData");
    		if(mOnTaskCompleteListener != null){
    			mOnTaskCompleteListener.onStartTask();
    		}
    		init(context, onTaskCompleteListener);
    		this.mIsShowDialogProgress = isShowDialogProgress;
    		this.mExecutingTaskCount = 3;
    		this.mOperationType = OperationType.SYNC_DATA;
    		startGetData(dtFrom, dtTo);
		}
	}
	
	public void startUpdatePersonalData(Activity activity, BaseTask.LoginInfo loginInfo, OnStateTaskListener onTaskCompleteListener) {
		if(!isRunning()){
    		Log.i("startUpdatePersonalData", "startUpdatePersonalData");
    		if(mOnTaskCompleteListener != null){
    			mOnTaskCompleteListener.onStartTask();
    		}
    		init(activity, onTaskCompleteListener);
    		this.mIsShowDialogProgress = true;
    		this.mExecutingTaskCount = 1;
    		this.mOperationType = OperationType.UPDATE_PERSONAL_DATA;
    		this.mCurrentExecutedTask = new UpdatePersonalDataTask(this, this, this.mContext);
    		SimpleDateFormat sdf = new SimpleDateFormat(BaseDTO.DATE_FORMAT);
    		String birthDateString = sdf.format(loginInfo.BirthDate);		
    		mCurrentExecutedTask.execute(Settings.getRegisterUserURL(this.mContext), loginInfo.Login, loginInfo.Password, loginInfo.FName, loginInfo.LName, loginInfo.MName, birthDateString, Integer.toString(loginInfo.Sex), Long.toString(loginInfo.CityId),
    				loginInfo.MaritalStatusId != null ? Integer.toString(loginInfo.MaritalStatusId) : "",
    			    loginInfo.SocialStatusId != null ? Integer.toString(loginInfo.SocialStatusId) : "", 
    			    loginInfo.Height != null ? Integer.toString(loginInfo.Height) : "",
    				loginInfo.Weight != null ? Integer.toString(loginInfo.Weight) : "",
    				loginInfo.PressureId != null ? Integer.toString(loginInfo.PressureId) : "",
    				loginInfo.FootDistance != null ? Integer.toString(loginInfo.FootDistance) : "",
    				loginInfo.SleepTime != null ? Integer.toString(loginInfo.SleepTime) : "");
		}
	}
	
	private void startUploadUserData(UploadDataType uploadDataType){
		User user = UserDB.getCurrentUser();
		this.mCurrentExecutedTask = new UploadUserDataTask(this, this, this.mContext, uploadDataType);
		mCurrentExecutedTask.execute(Settings.getUploadDataUrl(this.mContext), user.getLogin(), user.getPassword());		
	}

	private void startGetData(Date dtFrom, Date dtTo) {
		User user = UserDB.getCurrentUser();
		String args = String.format("cityid=%d&lat=%s&lng=%s", user.getCity().getId(), user.getCity().getLat(), user.getCity().getLng());		
		this.mCurrentExecutedTask = new GetDataTask(this, this, this.mContext);
		mCurrentExecutedTask.setDateRange(dtFrom, dtTo);
		mCurrentExecutedTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, Settings.getDataUrl(this.mContext, args));
	}

	private void startGetUserData() {
		this.mCurrentExecutedTask = new GetUserDataTask(this, this, this.mContext);
		mCurrentExecutedTask.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, Settings.getUserDataUrl(this.mContext) + getURLArgs());
	}

	private String getURLArgs() {
		User user = UserDB.getCurrentUser();
		String getArgs = "";
		if (getArgs.length() == 0) {
			getArgs += String.format("?" + BaseTask.ARG_LOGIN + "=%s", user.getLogin());
		} else {
			getArgs += String.format("&" + BaseTask.ARG_LOGIN + "=%s", user.getLogin());
		}
		if (getArgs.length() == 0) {
			getArgs += String.format("?" + BaseTask.ARG_PASSWORD + "=%s", user.getPassword());
		} else {
			getArgs += String.format("&" + BaseTask.ARG_PASSWORD + "=%s", user.getPassword());
		}
		if (user.getSyncDate() != null) {
			long unixSyncTime = user.getSyncDate().getTime() / 1000L;
			if (getArgs.length() == 0) {
				getArgs += String.format("?" + BaseTask.ARG_SYNC_DATE + "=%d", unixSyncTime);
			} else {
				getArgs += String.format("&" + BaseTask.ARG_SYNC_DATE + "=%d", unixSyncTime);
			}
		}
		return getArgs;
	}

	public void showProgressDialogGetData(String title, String message) {
		this.mProgressDialog.setTitle(title);
		this.mProgressDialog.setMessage(message);
		if (!this.mProgressDialog.isShowing()) {
			this.mProgressDialog.show();
		}
	}

	@Override
	public void onProgressUpdate(String... messages) {
		if (messages.length == 2) {
			this.mProgressDialogTitle = messages[0];
			this.mPreogressDialogMessage = messages[1];
			if (mIsShowDialogProgress) {
				showProgressDialogGetData(messages[0], messages[1]);
			}
		}
	}

	@Override
	public void onTaskComplete(BaseTask task, TaskResult result) {
		if (mOnTaskCompleteListener != null) {
			mOnTaskCompleteListener.onComplete(task, result);
		}
		this.mExecutingTaskCount--;
		if (result.isError()) {
			this.mExecutingTaskCount = 0;
		}
		if (this.mExecutingTaskCount > 0) {
			switch (mOperationType) {
			case GET_ALL_DATA:				
				startGetUserData();				
				break;
			case SYNC_DATA:
				switch (mExecutingTaskCount) {
				case 1:
					startUploadUserData(mCurrentActivity, this.mOnTaskCompleteListener, this.mIsShowDialogProgress);
					break;
				case 2:
					startGetUserData();
					break;
				}
				break;
			case UPLOAD_DATA:
				startUploadUserData(UploadDataType.USER_DATA);
				break;
			}
		}
		if (this.mExecutingTaskCount <= 0) {			
			mProgressDialog.dismiss();
			if(mOnTaskCompleteListener != null){
				mOnTaskCompleteListener.onStopTask();
			}
		}		
		if(result.isError() && result.getStatus() == TaskResult.Status.SERVER_UNAVALAIBLE){
			showNetworkErrorInfo();
		}
		if(this.mExecutingTaskCount <= 0){
			this.mCurrentActivity = null;
		}
	}
	
	private void showNetworkErrorInfo(){
		if(mCurrentActivity != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(mCurrentActivity);
            builder.setMessage(R.string.network_error);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    
                }
            });        
            AlertDialog dialog = builder.create();
            dialog.show();
		}
    }

}
