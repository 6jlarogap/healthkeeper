package com.health.main;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import android.content.Intent;
import android.os.Bundle;

import com.health.data.BodyFeeling;
import com.health.data.CommonFeeling;
import com.health.data.Factor;
import com.health.db.UserDB;
import com.health.repository.DBRepository;
import com.health.repository.IRepository;
import com.health.service.AsyncService;
import com.health.task.BaseTask;
import com.health.task.TaskResult;
import com.health.task.UploadUserDataTask;
import com.health.task.UploadUserDataTask.UploadDataType;
import com.health.util.Utils;

public class HistoryBodyFeelingActivity extends BaseSyncDataActivity implements OnChangeBodyFeelingListener {

	public static final String EXTRA_FEELING_ID = "id";
	
	public static final String EXTRA_FEELINGTYPE_ID = "typeid";
	
	public static final int BODYFEELING_TYPE_ID = 1;
	public static final int COMMONFEELING_TYPE_ID = 2;
	public static final int FACTOR_TYPE_ID = 3;
	
	private IRepository mRepository;
	
	private int mFeelingTypeId;
	
	private HashSet<Long> mDeletedDates = new HashSet<Long>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mFeelingTypeId = getIntent().getIntExtra(EXTRA_FEELINGTYPE_ID, BODYFEELING_TYPE_ID);
		this.mRepository = ((HealthApplication) getApplication()).getRepository();
		setContentView(R.layout.history_bodyfeeling_activity);
		switch(this.mFeelingTypeId){
		case BODYFEELING_TYPE_ID:
			setTitle(R.string.questionnaire_bodyfeeling_tab);
			break;
		case COMMONFEELING_TYPE_ID:
			setTitle(R.string.questionnaire_commonfeeling_tab);
			break;
		case FACTOR_TYPE_ID:
			setTitle(R.string.questionnaire_factor_tab);
			break;
		}
	}
	
	public void addDeletedDate(Date dt){
		Calendar calendar = Utils.toRoundDateAndIncrementDay(dt, 0);
		mDeletedDates.add(calendar.getTime().getTime());
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
    public void onAddBodyFeeling(BodyFeeling bodyFeeling) {
	    //not used in this Activity	    
    }

	@Override
    public void onDeleteBodyFeeling(BodyFeeling bodyFeeling) {
		addDeletedDate(bodyFeeling.getStartDate());
		this.mRepository.deleteBodyFeeling(bodyFeeling);
    }

	@Override
    public void onAddCommonFeeling(CommonFeeling commonFeeling) {
	    // TODO Auto-generated method stub
	    
    }

	@Override
    public void onDeleteCommonFeeling(CommonFeeling commonFeeling) {
		addDeletedDate(commonFeeling.getStartDate());
		this.mRepository.deleteCommonFeeling(commonFeeling);			    
    }

	
	@Override
    public void onAddFactor(Factor factor) {
	    // TODO Auto-generated method stub	    
    }	

	@Override
    public void onDeleteFactor(Factor factor) {
		this.mRepository.deleteFactor(factor);
			    
    }
	
	@Override
    public void onCompleteChangeUserData() {
		if(UserDB.isAutoSyncData()){
	        mTaskManager.startUploadUserData(this, this, true);
	    }	    
    }
	
	@Override
    public void onComplete(BaseTask task, TaskResult taskResult) {        
		if(task instanceof UploadUserDataTask){
			UploadUserDataTask uploadTask =(UploadUserDataTask) task;
			if(uploadTask.getTypeData() == UploadDataType.USER_DATA){
				for(Long dtFromTime : mDeletedDates){					
	    			if(dtFromTime > 0){
	    				long ms_in_day = 24 * 60 * 60 * 1000L;
	    				Date dtFrom = new Date((dtFromTime / ms_in_day) * ms_in_day - DBRepository.OFFSET_TO_UTC_IN_MS);
	    				Date dtTo = new Date(dtFrom.getTime() + ms_in_day - DBRepository.OFFSET_TO_UTC_IN_MS);
	    				asyncGetData(dtFrom, dtTo);
	    			}
				}
    			mDeletedDates.clear();
			}
		}
    }
	
	public void asyncGetData(Date dtFrom, Date dtTo){
		Intent intent = new Intent(this, AsyncService.class);
		intent.putExtra(AsyncService.EXTRA_OPERATION, AsyncService.OPERATION_GETDATA);
		intent.putExtra(AsyncService.EXTRA_DTFROM, dtFrom.getTime());
		intent.putExtra(AsyncService.EXTRA_DTTO, dtTo.getTime());
	    startService(intent);
	}

	

}
