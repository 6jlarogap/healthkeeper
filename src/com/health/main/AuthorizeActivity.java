package com.health.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.health.data.BaseDTO;
import com.health.data.User;
import com.health.db.UserDB;
import com.health.dependency.DependencySet;
import com.health.dialog.AnonimUserListDialogFragment;
import com.health.repository.IRepository;
import com.health.settings.Settings;
import com.health.task.AsyncTaskCompleteListener;
import com.health.task.AsyncTaskProgressListener;
import com.health.task.BaseTask;
import com.health.task.LoginTask;
import com.health.task.RecoveryPasswordTask;
import com.health.task.TaskResult;
import com.health.task.BaseTask.LoginInfo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AuthorizeActivity extends Activity implements AsyncTaskProgressListener, AsyncTaskCompleteListener<TaskResult> {

	public static final int MAIN_ACTIVITY_REQUESTCODE = 1;
	public static final int ANONIM_LIST_DIALOG_FRAGMENT = 2;

	
	public static final String EXTRA_MAIN_SELECTED_TAB = "EXTRA_MAIN_SELECTED_TAB";
	public static final String EXTRA_OPERATION_TYPE = "EXTRA_OPERATION_TYPE";
	
	public static final int OPERATION_CHANGE_USER_TYPE = 1;
	public static final int OPERATION_REGISTRATION_TYPE = 2;
	public static final int OPERATION_LOGOUT_TYPE = 3;

	public static final String EXTRA_FNAME = "EXTRA_FNAME";
	public static final String EXTRA_LNAME = "EXTRA_LNAME";
	public static final String EXTRA_MNAME = "EXTRA_MNAME";
	public static final String EXTRA_BIRTHDATE = "EXTRA_BIRTHDATE";
	public static final String EXTRA_PASSWORD = "EXTRA_PASSWORD";	

	private Fragment mCurrentFragment;

	private LoginTask mLoginTask = null;
	private RecoveryPasswordTask mRecoveryPasswordTask = null;
	private IRepository mRepository;
	
	private int mIsStorePassword = 0;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mRepository = ((HealthApplication) getApplication()).getRepository();
		getActionBar().hide();
		setContentView(R.layout.activity_authorize);
		FragmentManager fragmentManager = getFragmentManager();

		this.mCurrentFragment = fragmentManager.findFragmentById(R.id.lAuthorize);
		if (this.mCurrentFragment == null) {
			openLoginListFragment();
		}
		int operationType = getIntent().getIntExtra(AuthorizeActivity.EXTRA_OPERATION_TYPE, BaseDTO.INT_NULL_VALUE);		
		if(operationType == BaseDTO.INT_NULL_VALUE){
			User user = mRepository.getCurrentUser();
			if(user == null || user.isAnonim()){
				if(UserDB.getUserCount() <= 1){
					loginAnonimUser(DependencySet.DEFAULT_DIARY_ID);
				}				
			} else {
				//signIn(user);
			}
		}
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(this.mCurrentFragment instanceof LoginListFragment){
			openLoginListFragment();
		}
	}

	public void openRegistrationFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		RegisterFragment registerFragment = new RegisterFragment();
		fragmentManager.beginTransaction().replace(R.id.lAuthorize, registerFragment).commit();
		this.mCurrentFragment = registerFragment;
	}
	
	public void openRecoveryPasswordFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		RecoveryPasswordFragment recoveryPasswordFragment = new RecoveryPasswordFragment();
		fragmentManager.beginTransaction().replace(R.id.lAuthorize, recoveryPasswordFragment).commit();
		this.mCurrentFragment = recoveryPasswordFragment;
	}

	public void openLoginFragment(User user) {
		if (user != null) {
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.FName = user.getFName();
			loginInfo.LName = user.getLName();
			loginInfo.MName = user.getMName();
			loginInfo.BirthDate = user.getBirthDate();
			loginInfo.Password = user.getPassword();
			loginInfo.Login = user.getLogin();
			initializeExtraValues(loginInfo);
		} else {
			removeExtraValues();
		}
		FragmentManager fragmentManager = getFragmentManager();
		LoginFragment loginFragment = new LoginFragment();
		fragmentManager.beginTransaction().replace(R.id.lAuthorize, loginFragment).commit();
		this.mCurrentFragment = loginFragment;
	}
	
	public void openLoginListFragment() {		
		FragmentManager fragmentManager = getFragmentManager();
		LoginListFragment loginListFragment = new LoginListFragment();
		fragmentManager.beginTransaction().replace(R.id.lAuthorize, loginListFragment).commit();
		this.mCurrentFragment = loginListFragment;
	}

	public void loginAnonimUser(Integer diaryId) {
		User anonimUser = UserDB.getAnonimUser(this);
		getIntent().putExtra(EXTRA_MAIN_SELECTED_TAB, 0);
		getIntent().removeExtra(EXTRA_OPERATION_TYPE);
		startMainActivity(diaryId);
	}
	
	public void startMainActivity(Integer diaryId){
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(EXTRA_MAIN_SELECTED_TAB, getIntent().getIntExtra(EXTRA_MAIN_SELECTED_TAB, 0));
		intent.putExtra(EXTRA_OPERATION_TYPE, getIntent().getIntExtra(EXTRA_OPERATION_TYPE, BaseDTO.INT_NULL_VALUE));
		if(diaryId != null){
			intent.putExtra(MainActivity.EXTRA_DIARY_ID, diaryId);
		}
		startActivityForResult(intent, MAIN_ACTIVITY_REQUESTCODE);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	public void onSuccessUserRegistration(LoginInfo loginInfo) {
		initializeExtraValues(loginInfo);
		User user = new User();
		user.setFName(loginInfo.FName);
		user.setLName(loginInfo.LName);
		user.setMName(loginInfo.MName);
		user.setBirthDate(loginInfo.BirthDate);
		user.setPassword(loginInfo.Password);
		user.generateLogin();		
		openLoginFragment(user);
	}

	public void initializeExtraValues(LoginInfo loginInfo) {
		removeExtraValues();
		getIntent().putExtra(EXTRA_FNAME, loginInfo.FName);
		getIntent().putExtra(EXTRA_MNAME, loginInfo.MName);
		getIntent().putExtra(EXTRA_LNAME, loginInfo.LName);
		getIntent().putExtra(EXTRA_PASSWORD, loginInfo.Password);
		getIntent().putExtra(EXTRA_BIRTHDATE, loginInfo.BirthDate.getTime());
	}
	
	public void removeExtraValues() {
		getIntent().removeExtra(EXTRA_BIRTHDATE);
		getIntent().removeExtra(EXTRA_LNAME);
		getIntent().removeExtra(EXTRA_MNAME);
		getIntent().removeExtra(EXTRA_FNAME);
		getIntent().removeExtra(EXTRA_PASSWORD);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		this.mCurrentFragment = getFragmentManager().findFragmentById(R.id.lAuthorize);
		if (this.mCurrentFragment instanceof RegisterFragment) {
			((RegisterFragment) this.mCurrentFragment).cleanRegisterStatus();
		}
		return super.dispatchTouchEvent(ev);
	}

	public void signIn(User user) {
		this.mIsStorePassword = user.getIsStorePassword() != null ?  user.getIsStorePassword() : 0;
		this.mLoginTask = new LoginTask(this, this, this);
		String url = Settings.getLoginURL(this);
		this.mLoginTask.execute(url, user.getLogin(), user.getPassword());
	}
	
	public void changePassword(User user) {
		this.mIsStorePassword = user.getIsStorePassword() != null ?  user.getIsStorePassword() : 0;
		this.mRecoveryPasswordTask = new RecoveryPasswordTask(this, this, this);
		String url = Settings.getRecoveryPasswordUrl(this);
		this.mRecoveryPasswordTask.execute(url, user.getLogin(), user.getPassword(), user.Answer1 , user.Answer2);
	}

	public boolean isProgressSignIn() {
		if (this.mLoginTask != null && this.mLoginTask.getStatus() == AsyncTask.Status.RUNNING) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isProgressChangePassword() {
		if (this.mRecoveryPasswordTask != null && this.mRecoveryPasswordTask.getStatus() == AsyncTask.Status.RUNNING) {
			return true;
		} else {
			return false;
		}
	}

	public void showMessage(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
		toast.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (MAIN_ACTIVITY_REQUESTCODE):
			if (resultCode == Activity.RESULT_OK) {
				int operationType = data.getIntExtra(EXTRA_OPERATION_TYPE, BaseDTO.INT_NULL_VALUE);
				switch (operationType) {
				case OPERATION_REGISTRATION_TYPE:
					if (!(this.mCurrentFragment instanceof RegisterFragment)) {
						openRegistrationFragment();
					}
					break;
				case OPERATION_CHANGE_USER_TYPE:
					if (!(this.mCurrentFragment instanceof LoginListFragment)) {
						openLoginListFragment();
					}
					break;
				case OPERATION_LOGOUT_TYPE:
					if(!(this.mCurrentFragment instanceof LoginListFragment)){
						openLoginListFragment();
					}
					break;
				default:
					break;
				}
				getIntent().putExtra(EXTRA_MAIN_SELECTED_TAB, data.getIntExtra(EXTRA_MAIN_SELECTED_TAB, 0));
				getIntent().putExtra(EXTRA_OPERATION_TYPE, data.getIntExtra(EXTRA_OPERATION_TYPE, BaseDTO.INT_NULL_VALUE));				
			}
			break;
			case ANONIM_LIST_DIALOG_FRAGMENT:
				if(resultCode == Activity.RESULT_OK){
					int diaryId = (Integer) data.getExtras().get(AnonimUserListDialogFragment.EXTRA_ID);
					loginAnonimUser(diaryId);
				}
				break;
		}
	}
	
	@Override
	public void onBackPressed()
	{
		if(mCurrentFragment != null) {
			if(mCurrentFragment instanceof RegisterFragment){
				openLoginFragment(null);
				return;
			}
			if(mCurrentFragment instanceof LoginFragment) {
				openLoginListFragment();
				return;
			}
			if(mCurrentFragment instanceof RecoveryPasswordFragment){
				openLoginFragment(null);
				return;
			}
		}
	    super.onBackPressed();
	}

	@Override
	public void onTaskComplete(BaseTask task, TaskResult result) {
		if(task instanceof LoginTask){
			this.mLoginTask = null;
			if (mCurrentFragment instanceof LoginFragment) {
				if (mCurrentFragment.isVisible()) {
					LoginFragment loginFragment = (LoginFragment) mCurrentFragment;
					loginFragment.showProgress(false, R.string.login_progress_signing_in);
					if (result.getStatus() == TaskResult.Status.LOGIN_SUCCESSED) {
						Settings.saveFirstStartApplication(this, false);
						UserDB.changeUser(result.getLoginInfo(), mIsStorePassword);
						startMainActivity(null);
					} else {
						showMessage(result.getErrorText());
					}
				}
			}
			if (mCurrentFragment instanceof LoginListFragment) {
				if (mCurrentFragment.isVisible()) {
					LoginListFragment loginListFragment = (LoginListFragment) mCurrentFragment;
					loginListFragment.showProgress(false, R.string.login_progress_signing_in);
					if (result.getStatus() == TaskResult.Status.LOGIN_SUCCESSED) {
						Settings.saveFirstStartApplication(this, false);
						UserDB.changeUser(result.getLoginInfo(), mIsStorePassword);
						startMainActivity(null);
					} else {
						showMessage(result.getErrorText());
					}
				}
			}
		}
		
		if(task instanceof RecoveryPasswordTask){
			this.mRecoveryPasswordTask = null;
			if(mCurrentFragment.isVisible()){
				if(mCurrentFragment instanceof RecoveryPasswordFragment){
					RecoveryPasswordFragment recoveryPasswordFragment = (RecoveryPasswordFragment) mCurrentFragment;
					recoveryPasswordFragment.showProgress(false, R.string.action_changing_password);
					if(result.getStatus() == TaskResult.Status.LOGIN_SUCCESSED){
						showMessage("Пароль успешно изменен.");
						openLoginFragment(null);
					} else {
						showMessage(result.getErrorText());
					}
				}
			}
			
		}
		
	}

	@Override
	public void onProgressUpdate(String... messages) {
		// do nothing
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show, final View loginFormView, final View loginStatusView, final TextView tvLoginStatusMessage, int statusMessageId) {
		tvLoginStatusMessage.setText(statusMessageId);
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
			loginStatusView.setVisibility(View.VISIBLE);
			loginStatusView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});

			loginFormView.setVisibility(View.GONE);
			loginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
				}
			});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			loginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			loginFormView.setVisibility(show ? View.VISIBLE : View.GONE);
		}
	}

}
