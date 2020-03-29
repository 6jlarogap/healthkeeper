package com.health.main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.health.data.BaseDTO;
import com.health.data.BodyFeeling;
import com.health.data.BodyFeelingType;
import com.health.data.BodyRegion;
import com.health.data.CommonFeeling;
import com.health.data.CommonFeelingType;
import com.health.data.CustomBodyFeelingType;
import com.health.data.CustomCommonFeelingType;
import com.health.data.DaoSession;
import com.health.data.Factor;
import com.health.data.IGridItem;
import com.health.data.IGridItemValue;
import com.health.data.OperationUserData;
import com.health.data.User;
import com.health.data.WeatherDailyDao;
import com.health.db.DB;
import com.health.db.UserDB;
import com.health.dependency.DependencySet;
import com.health.repository.IRepository;
import com.health.service.AsyncService;
import com.health.service.LocationService;
import com.health.service.SyncDataServiceReceiver;
import com.health.settings.Settings;
import com.health.task.BaseTask;
import com.health.task.GetDataTask;
import com.health.task.GetUserDataTask;
import com.health.task.TaskResult;
import com.health.task.UploadUserDataTask;
import com.health.task.UploadUserDataTask.UploadDataType;

public class MainActivity extends BaseSyncDataActivity implements OnChangeBodyFeelingListener, IRepository.ILoadDataListener {
	
	public static final String EXTRA_DATE_UPLOAD = "EXTRA_DATE_UPLOAD";
	public static final String EXTRA_DIARY_ID = "EXTRA_DIARY_ID";

	public static final String MAIN_FRAGMENT_TAG = "main";
	public static final String DIARY_FRAGMENT_TAG = "diary";
	public static final String QUESTIONNAIRE_FRAGMENT_TAG = "questionnaire";

	public static final int SETTINGS_ACTIVITY_REQUEST_CODE = 135;

	private User mUser;

	private User mUserBeforeSettings = null;

	private IRepository mRepository;

	private int mSelectedTabIndex = 0;

	private MenuItem mGetDataMenuItem = null;

	private boolean mIsVisibleActivity = false;

	Messenger mService = null;

	boolean mBound;

	/**
	 * Class for interacting with the main interface of the service.
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = new Messenger(service);
			mBound = true;
		}

		public void onServiceDisconnected(ComponentName className) {
			mService = null;
			mBound = false;
		}
	};

	public void getCurrentGPS() {
		if (!mBound){
			return;
		}
		Message msg = Message.obtain(null, LocationService.MSG_GPS_GET, 0, 0, null);		
		try {
			mService.send(msg);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private BroadcastReceiver mServiceMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int status = intent.getIntExtra(LocationService.EXTRA_STATUS, -1);
			if(status == LocationService.STATUS_OK){
				double lat = intent.getDoubleExtra(LocationService.EXTRA_LAT, 0);
				double lng = intent.getDoubleExtra(LocationService.EXTRA_LNG, 0);
				int distance = intent.getIntExtra(LocationService.EXTRA_DISTANCE, 0);				
				MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
				if(mainFragment != null){
					mainFragment.updateGPSLocation(lat, lng, distance, mUser.getCity());
				}
			}			
		}
	};
	
	private BroadcastReceiver mAJAXBroadcastReceiver = new BroadcastReceiver() {
		
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("AJAX_Complete", "onReceive");
			if(mUser != null && mUser.isAnonim() && intent.getIntExtra(AsyncService.EXTRA_OPERATION, -1) == AsyncService.OPERATION_GET_TESTDIARY){
				if(intent.getIntExtra(AsyncService.EXTRA_RESULT, -1) == AsyncService.RESULT_ERROR){
					int diary = intent.getIntExtra(AsyncService.EXTRA_DIARY_ID, 1);
					getIntent().putExtra(EXTRA_DIARY_ID, diary);
				}
				DiaryFragment diaryFragment = ((DiaryFragment) getFragmentManager().findFragmentByTag(DIARY_FRAGMENT_TAG));
				if (diaryFragment != null) {
					diaryFragment.updateFeelingInfo();
					diaryFragment.updateFactorInfo();
				}
			}
        }
    };;

	/*private void generateDemonstrateDiary(){
		int diaryId = getIntent().getIntExtra(EXTRA_DIARY_ID, -1);
		getIntent().removeExtra(EXTRA_DIARY_ID);
		DependencySet dependencySet = new DependencySet();
		Date[] period = getDateRangeForDemonstrationDiary();
		dependencySet.generateDiaryFeeling(diaryId, period[0], period[1]);
	}*/
	
	public void asyncGetData(Date dtFrom, Date dtTo){
		Intent intent = new Intent(this, AsyncService.class);
		intent.putExtra(AsyncService.EXTRA_OPERATION, AsyncService.OPERATION_GETDATA);
		intent.putExtra(AsyncService.EXTRA_DTFROM, dtFrom.getTime());
		intent.putExtra(AsyncService.EXTRA_DTTO, dtTo.getTime());
	    startService(intent);
	}

	public void asyncGetDailyData(Date dtFrom, Date dtTo){
		Intent intent = new Intent(this, AsyncService.class);
		intent.putExtra(AsyncService.EXTRA_OPERATION, AsyncService.OPERATION_GETDATA_DAILY);
		intent.putExtra(AsyncService.EXTRA_DTFROM, dtFrom.getTime());
		intent.putExtra(AsyncService.EXTRA_DTTO, dtTo.getTime());
		startService(intent);
	}

	public void asyncGetTestDiary(){
		int diaryId = getIntent().getIntExtra(EXTRA_DIARY_ID, -1);
		getIntent().removeExtra(EXTRA_DIARY_ID);
		Date dtTo = new Date();
		Date dtFrom = new Date(dtTo.getTime() - 90 * 86400 * 1000L);
		Intent intent = new Intent(this, AsyncService.class);
		intent.putExtra(AsyncService.EXTRA_OPERATION, AsyncService.OPERATION_GET_TESTDIARY);
		intent.putExtra(AsyncService.EXTRA_DTFROM, dtFrom.getTime());
		intent.putExtra(AsyncService.EXTRA_DTTO, dtTo.getTime());
		intent.putExtra(AsyncService.EXTRA_DIARY_ID, diaryId);
		startService(intent);
	}
	

	public User getCurrentUser() {
		return mUser;
	}

	@Override
	public void onAddBodyFeeling(BodyFeeling bodyFeeling) {
		this.mRepository.addBodyFeeling(bodyFeeling);
		if (UserDB.isAutoSyncData()) {
			mTaskManager.startUploadUserData(this, this, false);
		}
	}


	public void uploadUserData() {
		if (UserDB.isAutoSyncData()) {
			mTaskManager.startUploadUserData(this, this, false);
		}
	}

	@Override
	public void onDeleteBodyFeeling(BodyFeeling bodyFeeling) {
		// not used in this Activity
	}

	@Override
	public void onAddCommonFeeling(CommonFeeling commonFeeling) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeleteCommonFeeling(CommonFeeling commonFeeling) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAddFactor(Factor factor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeleteFactor(Factor factor) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCompleteChangeUserData() {
		if (UserDB.isAutoSyncData()) {
			mTaskManager.startUploadUserData(this, this, false);
		}
	}

	private void showMessage(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title).setMessage(message).setIcon(R.drawable.heart).setCancelable(true).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public boolean checkRegistration() {
		if (mUser.getSex() == User.ANONIM_SEX) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.request_registration_title).setMessage(R.string.request_registration_message).setIcon(R.drawable.heart).setCancelable(true)
					.setPositiveButton(R.string.request_registration_ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent resultIntent = new Intent();
							resultIntent.putExtra(AuthorizeActivity.EXTRA_MAIN_SELECTED_TAB, getActionBar().getSelectedNavigationIndex());
							resultIntent.putExtra(AuthorizeActivity.EXTRA_OPERATION_TYPE, AuthorizeActivity.OPERATION_REGISTRATION_TYPE);
							setResult(Activity.RESULT_OK, resultIntent);
							finish();
						}
					}).setNegativeButton(R.string.request_registration_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			return false;
		}
		return true;
	}

	public boolean checkSaveBodyFeeling(Date selectedDate, BodyFeelingType bodyFeelingType, BodyRegion bodyRegion, String customBodyFeelingTypeName) {
		boolean result = true;
		Date nowDate = new Date();
		if (selectedDate.before(mUser.getBirthDate())) {
			result = false;
			showMessage(null, getString(R.string.incorect_value_feeling_date_before));
		}
		if (selectedDate.after(nowDate)) {
			result = false;
			showMessage(null, getString(R.string.incorect_value_feeling_date_after));
		}
		if (result) {
			GregorianCalendar gcFrom = new GregorianCalendar();
			GregorianCalendar gcTo = new GregorianCalendar();
			gcFrom.setTime(selectedDate);
			gcTo.setTime(selectedDate);
			gcFrom.add(Calendar.HOUR_OF_DAY, -1);
			gcTo.add(Calendar.HOUR_OF_DAY, 1);
			if (bodyFeelingType != null) {
				List<BodyFeeling> list = mRepository.getBodyFeelings(bodyFeelingType.getId(), null, bodyRegion.getId(), gcFrom.getTime(), gcTo.getTime());
				if (list.size() > 0) {
					result = false;
					showMessage(null, getString(R.string.incorect_value_feeling_date));
				}
			} else {
				if(customBodyFeelingTypeName != null){
					CustomBodyFeelingType customBodyFeelingType = mRepository.getCustomBodyFeelingType(customBodyFeelingTypeName);
					if(customBodyFeelingType != null){
						List<BodyFeeling> list = mRepository.getBodyFeelings(null, customBodyFeelingType.getId(), bodyRegion.getId(), gcFrom.getTime(), gcTo.getTime());
						if (list.size() > 0) {
							result = false;
							showMessage(null, getString(R.string.incorect_value_feeling_date));
						}
					}
				}
			}
		}
		return result;
	}

	public boolean checkSaveCommonFeeling(Date selectedDate, CommonFeelingType commonFeelingType, CustomCommonFeelingType customCommonFeelingType) {
		boolean result = true;
		Date nowDate = new Date();
		if (selectedDate.before(mUser.getBirthDate())) {
			result = false;
			showMessage(null, getString(R.string.incorect_value_feeling_date_before));
		}
		if (selectedDate.after(nowDate)) {
			result = false;
			showMessage(null, getString(R.string.incorect_value_feeling_date_after));
		}
		if (result) {
			GregorianCalendar gcFrom = new GregorianCalendar();
			GregorianCalendar gcTo = new GregorianCalendar();
			gcFrom.setTime(selectedDate);
			gcTo.setTime(selectedDate);
			gcFrom.add(Calendar.HOUR_OF_DAY, -1);
			gcTo.add(Calendar.HOUR_OF_DAY, 1);
			List<CommonFeeling> list = null;
			if (commonFeelingType != null) {
				list = mRepository.getCommonFeelings(commonFeelingType.getId(), null, gcFrom.getTime(), gcTo.getTime());
			}
			if (customCommonFeelingType != null) {
				list = mRepository.getCommonFeelings(null, customCommonFeelingType.getId(), gcFrom.getTime(), gcTo.getTime());
			}
			if (list.size() > 0) {
				result = false;
				showMessage(null, getString(R.string.incorect_value_feeling_date));
			}
		}
		return result;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SETTINGS_ACTIVITY_REQUEST_CODE:
			if (mUserBeforeSettings != null) {
				User user = UserDB.getCurrentUser();
				if (user.getPeriodSyncData() != mUserBeforeSettings.getPeriodSyncData()) {
					startSyncDataService();
				}
			}
			((MainFragment) getFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG)).updateTotalInfo();
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mServiceMessageReceiver, new IntentFilter(LocationService.CALLBACK_INTENT));
		LocalBroadcastManager.getInstance(this).registerReceiver(mAJAXBroadcastReceiver, new IntentFilter(AsyncService.TASK_COMPLETE));
	}

	@Override
	protected void onPause() {		
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mServiceMessageReceiver);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mAJAXBroadcastReceiver);
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.mIsVisibleActivity = true;
		bindService(new Intent(this, LocationService.class), mConnection, Context.BIND_AUTO_CREATE);
		if(mUser.isAnonim() && getIntent().getIntExtra(EXTRA_DIARY_ID, -1) > 0){
			asyncGetTestDiary();
		}
	}

	public Date[] getDateRangeForDemonstrationDiary(){
		Calendar calendar = GregorianCalendar.getInstance();
		Date dtFrom, dtTo;
		dtTo = calendar.getTime();
		calendar.add(Calendar.MONTH, -1);
		dtFrom = calendar.getTime();
		Date[] result = new Date[2];
		result[0] = dtFrom;
		result[1] = dtTo;
		return result;
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.mIsVisibleActivity = false;
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mRepository = ((HealthApplication) getApplication()).getRepository();
		this.mRepository.setLoadDataListener(this);
		this.mUser = UserDB.getCurrentUser();
		if (savedInstanceState == null) {
			int operationType = getIntent().getIntExtra(AuthorizeActivity.EXTRA_OPERATION_TYPE, BaseDTO.INT_NULL_VALUE);
			switch (operationType) {
			case AuthorizeActivity.OPERATION_CHANGE_USER_TYPE:
				this.mTaskManager.startGetOnlyUserData(this, this, false);
				break;
			default:
				Date nowDate = new Date();
				Date dtFrom = new Date(nowDate.getTime() - 3 * 86400 * 1000L);
				Date dtTo = new Date(nowDate.getTime() + 3 * 86400 * 1000L);
				if (!mUser.isAnonim()) {
					this.mTaskManager.startGetAllData(this, this, false, dtFrom, dtTo);
				} else {
					this.mTaskManager.startGetCommonData(this, this, false, dtFrom, dtTo);
				}
				break;
			}
			startSyncDataService();
			startLocationService();
		}
		mSelectedTabIndex = getIntent().getIntExtra(AuthorizeActivity.EXTRA_MAIN_SELECTED_TAB, 0);
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			actionBar.setTitle(R.string.app_name);
			actionBar.setHomeButtonEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			ActionBar.Tab tab = actionBar.newTab().setText("Обобщенные данные").setTabListener(new TabListener<MainFragment>(this, MAIN_FRAGMENT_TAG, MainFragment.class));
			actionBar.addTab(tab);

			tab = actionBar.newTab().setText("Дневник самочувствия").setTabListener(new TabListener<DiaryFragment>(this, DIARY_FRAGMENT_TAG, DiaryFragment.class));
			actionBar.addTab(tab);

			tab = actionBar.newTab().setText("Опросный лист").setTabListener(new TabListener<QuestionnaireFragment>(this, QUESTIONNAIRE_FRAGMENT_TAG, QuestionnaireFragment.class));
			actionBar.addTab(tab);
			if (savedInstanceState != null) {
				mSelectedTabIndex = savedInstanceState.getInt("selectedTabIndex", 0);
			}
			actionBar.selectTab(actionBar.getTabAt(mSelectedTabIndex));
		}
	}

	public void startLocationService() {
		Intent serviceIntent = new Intent(this, LocationService.class);
		startService(serviceIntent);
	}

	public void startSyncDataService() {
		User user = UserDB.getCurrentUser();
		Intent serviceIntent = new Intent(getApplicationContext(), SyncDataServiceReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, serviceIntent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int interval = user.getPeriodSyncData() * 1000;
		alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis() + interval / 10, interval, pendingIntent);
	}

	public void stopSyncDataService() {
		Intent serviceIntent = new Intent(getApplicationContext(), SyncDataServiceReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, serviceIntent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		ActionBar actionBar = getActionBar();
		if (actionBar != null) {
			outState.putInt("selectedTabIndex", actionBar.getSelectedNavigationIndex());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onPrepareOptionsMenu(Menu menu) {
		this.mGetDataMenuItem = menu.findItem(R.id.menu_item_get_data);
		if (mTaskManager.isRunning()) {
			setProgressBarInMenuItem(true);
		} else {
			setProgressBarInMenuItem(false);
		}
		MenuItem uploadUserDataMenu = menu.findItem(R.id.menu_item_upload_user_data);
		MenuItem personalDataMenu = menu.findItem(R.id.menu_item_personaldata);
		MenuItem registerUserMenu = menu.findItem(R.id.menu_item_registration);
		MenuItem changeUserMenu = menu.findItem(R.id.menu_item_changeuser);
		MenuItem settingsMenu = menu.findItem(R.id.menu_item_settings);
		User currentUser = UserDB.getCurrentUser();
		if (currentUser.isAnonim()) {
			uploadUserDataMenu.setVisible(false);
			registerUserMenu.setVisible(true);
			changeUserMenu.setVisible(false);
			settingsMenu.setVisible(false);
		} else {
			personalDataMenu.setVisible(true);
			changeUserMenu.setVisible(true);
			settingsMenu.setVisible(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_item_get_data:
			MainFragment mainFragment = (MainFragment) getFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
			Date dtFrom = mainFragment.getPlotDateFrom();
			Date dtTo = mainFragment.getPlotDateTo();
			if (!mUser.isAnonim()) {
				this.mTaskManager.startGetAllData(this, this, false, dtFrom, dtTo);
			} else {
				this.mTaskManager.startGetCommonData(this, this, false, dtFrom, dtTo);
			}
			break;
		case R.id.menu_item_logout:
			if (!isExistUnsavedData(item)) {
				this.mTaskManager.cancelTasks();
				logOut();
			}
			break;
		case R.id.menu_item_upload_user_data:
			startUploadUserData();
			break;
		case R.id.menu_item_settings:
			Intent intent = new Intent(this, com.health.settings.SettingsActivity.class);
			this.mUserBeforeSettings = UserDB.getCurrentUser();
			startActivityForResult(intent, SETTINGS_ACTIVITY_REQUEST_CODE);
			break;
		case R.id.menu_item_registration:
			if (!isExistUnsavedData(item)) {
				this.mTaskManager.cancelTasks();
				registration();
			}
			break;
		case R.id.menu_item_changeuser:
			if (!isExistUnsavedData(item)) {
				this.mTaskManager.cancelTasks();
				changeUser();
			}
			break;
		case R.id.menu_item_personaldata:
			this.mTaskManager.cancelTasks();
			Intent personalDataIntent = new Intent(this, PersonalDataActivity.class);
			startActivity(personalDataIntent);
			break;
		}
		return true;
	}

	private void logOut() {
		stopSyncDataService();
		Settings.saveFirstStartApplication(this, false);
		Intent resultIntent = new Intent();
		resultIntent.putExtra(AuthorizeActivity.EXTRA_MAIN_SELECTED_TAB, getActionBar().getSelectedNavigationIndex());
		resultIntent.putExtra(AuthorizeActivity.EXTRA_OPERATION_TYPE, AuthorizeActivity.OPERATION_LOGOUT_TYPE);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

	private void changeUser() {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(AuthorizeActivity.EXTRA_MAIN_SELECTED_TAB, getActionBar().getSelectedNavigationIndex());
		resultIntent.putExtra(AuthorizeActivity.EXTRA_OPERATION_TYPE, AuthorizeActivity.OPERATION_CHANGE_USER_TYPE);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

	private void registration() {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(AuthorizeActivity.EXTRA_MAIN_SELECTED_TAB, getActionBar().getSelectedNavigationIndex());
		resultIntent.putExtra(AuthorizeActivity.EXTRA_OPERATION_TYPE, AuthorizeActivity.OPERATION_REGISTRATION_TYPE);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}

	private boolean isExistUnsavedData(final MenuItem menuItem) {
		boolean result = true;
		result = (DB.db().newSession().getOperationUserDataDao().count() != 0);
		if (result) {
			AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle(R.string.attention).setMessage(R.string.info_unsaved_data).setIcon(R.drawable.heart).setCancelable(false)
			        .setPositiveButton(R.string.menu_item_upload_user_data, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int id) {
					        dialog.cancel();
					        startUploadUserData();
				        }
			        }).setNegativeButton(menuItem.getTitle(), new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int id) {
					        dialog.cancel();
					        switch (menuItem.getItemId()) {
							case R.id.menu_item_logout:
								logOut();
								break;
							case R.id.menu_item_registration:
								registration();
								break;
							case R.id.menu_item_changeuser:
								changeUser();
								break;
							}
						}
			        });
			AlertDialog alert = builder.create();
			alert.show();
		}
		return result;
	}

	@Override
	public void onBackPressed() {
		return;
	}

	public void startUploadUserData() {
		mTaskManager.startUploadUserData(this, this, false);
	}

	@Override
	public void onComplete(BaseTask task, TaskResult taskResult) {
		if (mIsVisibleActivity) {
			if (!taskResult.isError()) {
				if (task instanceof GetDataTask) {
					((MainFragment) getFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG)).updateFactorTotalInfo();
					((MainFragment) getFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG)).updateFeelingTotalInfo();
					DiaryFragment diaryFragment = ((DiaryFragment) getFragmentManager().findFragmentByTag(DIARY_FRAGMENT_TAG));
					if (diaryFragment != null) {
						diaryFragment.updateFactorInfo();
					}
				}
				if (task instanceof GetUserDataTask) {
					QuestionnaireFragment questionnaireFragment = ((QuestionnaireFragment) getFragmentManager().findFragmentByTag(QUESTIONNAIRE_FRAGMENT_TAG));
					if (questionnaireFragment != null) {
						if (questionnaireFragment.isVisible()) {
							questionnaireFragment.resetLoader();
						}
					}
					DiaryFragment diaryFragment = ((DiaryFragment) getFragmentManager().findFragmentByTag(DIARY_FRAGMENT_TAG));
					if (diaryFragment != null) {
						diaryFragment.updateFeelingInfo();
					}
					setProgressBarInMenuItem(false);
				}

			} else {
				setProgressBarInMenuItem(false);
			}
		}
		if(task instanceof UploadUserDataTask){
			UploadUserDataTask uploadTask =(UploadUserDataTask) task;
			if(uploadTask.getTypeData() == UploadDataType.USER_DATA){
    			long dtFromTime = getIntent().getLongExtra(EXTRA_DATE_UPLOAD, 0L);
    			if(dtFromTime > 0){
    				long ms_in_day = 24 * 60 * 60 * 1000L;
    				Date dtFrom = new Date((dtFromTime / ms_in_day) * ms_in_day );
    				Date dtTo = new Date(dtFrom.getTime() + ms_in_day);
    				asyncGetData(dtFrom, dtTo);
    			}
			}
		}
	}

	private void setProgressBarInMenuItem(boolean progress) {
		if (mGetDataMenuItem != null) {
			if (progress) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View progressBar = inflater.inflate(R.layout.progress_wheel, null);
				mGetDataMenuItem.setActionView(progressBar);
			} else {
				mGetDataMenuItem.setActionView(null);
			}
		}
	}

	@Override
	public void onStartTask() {
		setProgressBarInMenuItem(true);
	}

	@Override
	public void onStopTask() {
		setProgressBarInMenuItem(false);
	}

	private static class TabListener<T extends Fragment> implements ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
			mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);

		}

		/* The following are each of the ActionBar.TabListener callbacks */

		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				// mFragment.setArguments(outState);
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}

		}

		public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}

	@Override
	public void loadData(final Date dtFrom, final Date dtTo) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (!mTaskManager.isRunning()) {
					if (!mUser.isAnonim()) {
						mTaskManager.startGetCommonData(MainActivity.this, MainActivity.this, false, dtFrom, dtTo);
					} else {
						mTaskManager.startGetCommonData(MainActivity.this, MainActivity.this, false, dtFrom, dtTo);
					}
				}

			}
		});

	}

}
