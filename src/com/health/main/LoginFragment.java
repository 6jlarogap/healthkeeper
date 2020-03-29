package com.health.main;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.health.data.User;
import com.health.dependency.DependencySet;
import com.health.dialog.AnonimUserListDialogFragment;
import com.health.dialog.DateTimeDialogFragment;
import com.health.dialog.PeriodDateDialogFragment;
import com.health.task.BaseTask.LoginInfo;

public class LoginFragment extends Fragment {
	private EditText etPassword, etFName, etLName, etMName;
	private DatePicker dpBirthDate;
	private CheckBox cbStorePassword;
	private Button btnLogin;
	private TextView tvLinkRegister;
	private TextView tvLinkAnonimLogin;
	private TextView tvLinkChangePassword;
	private String error_field_required;

	private View focusView;
	private View loginFormView;
	private View loginStatusView;
	private TextView tvLoginStatusMessage;



	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		LinearLayout wrapper = new LinearLayout(getActivity());
		View v = (View) inflater.inflate(R.layout.login_fragment, wrapper, true);
		this.loginFormView = v.findViewById(R.id.login_form);
		this.loginStatusView = v.findViewById(R.id.login_status);
		this.tvLoginStatusMessage = (TextView) v.findViewById(R.id.login_status_message);
		this.btnLogin = (Button) v.findViewById(R.id.btnLogin);
		this.tvLinkRegister = (TextView) v.findViewById(R.id.tvLinkRegister);
		this.etLName = (EditText) v.findViewById(R.id.etLName);
		this.etMName = (EditText) v.findViewById(R.id.etMName);
		this.etFName = (EditText) v.findViewById(R.id.etFName);
		this.dpBirthDate = (DatePicker) v.findViewById(R.id.dpBirthDate);
		this.etPassword = (EditText) v.findViewById(R.id.etPassword);
		this.tvLinkAnonimLogin = (TextView) v.findViewById(R.id.tvLinkAnonimLogin);
		this.tvLinkChangePassword = (TextView) v.findViewById(R.id.tvLinkChangePassword);
		this.cbStorePassword = (CheckBox) v.findViewById(R.id.cbStorePassword);
		String lname = getActivity().getIntent().getStringExtra(AuthorizeActivity.EXTRA_LNAME);
		String fname = getActivity().getIntent().getStringExtra(AuthorizeActivity.EXTRA_FNAME);
		String mname = getActivity().getIntent().getStringExtra(AuthorizeActivity.EXTRA_MNAME);
		String password = getActivity().getIntent().getStringExtra(AuthorizeActivity.EXTRA_PASSWORD);
		long birthDateLongMs = getActivity().getIntent().getLongExtra(AuthorizeActivity.EXTRA_BIRTHDATE, Long.MIN_VALUE);
		if (lname != null) {
			etLName.setText(lname);
		}
		if (fname != null) {
			etFName.setText(fname);
		}
		if (mname != null) {
			etMName.setText(mname);
		}
		if (password != null) {
			etPassword.setText(password);
		}
		if(birthDateLongMs != Long.MIN_VALUE){
			Calendar gc = new GregorianCalendar();
			gc.setTimeInMillis(birthDateLongMs);			
			dpBirthDate.init(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH), null);			
		}

		this.btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

		this.tvLinkRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				tvLinkRegister.setShadowLayer(1, 0, 0, Color.BLUE);
				hideScreenKeyboard();
				((AuthorizeActivity)getActivity()).openRegistrationFragment();
			}
		});
		
		this.tvLinkAnonimLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*tvLinkAnonimLogin.setShadowLayer(1, 0, 0, Color.BLUE);
				((AuthorizeActivity) getActivity()).loginAnonimUser();*/
				AnonimUserListDialogFragment newFragment = AnonimUserListDialogFragment.newInstance();
				newFragment.setTargetFragment(LoginFragment.this, AuthorizeActivity.ANONIM_LIST_DIALOG_FRAGMENT);
				newFragment.show(getFragmentManager(), "dialog");
			}
		});
		
		this.tvLinkChangePassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				AuthorizeActivity baseActivity = (AuthorizeActivity)getActivity();
				User user = getEnteredUserFromUI();
				LoginInfo loginInfo = new LoginInfo();
				loginInfo.FName = user.getFName();
				loginInfo.MName = user.getMName();
				loginInfo.LName = user.getLName();
				loginInfo.BirthDate = user.getBirthDate();
				loginInfo.Password = null;
				baseActivity.initializeExtraValues(loginInfo);
				tvLinkChangePassword.setShadowLayer(1, 0, 0, Color.BLUE);
				hideScreenKeyboard();
				baseActivity.openRecoveryPasswordFragment();
			}
		});
		
		this.error_field_required = getActivity().getResources().getString(R.string.error_field_required);		
		AuthorizeActivity baseActivity = (AuthorizeActivity)getActivity();
		if(baseActivity.isProgressSignIn()){
			baseActivity.showProgress(true, this.loginFormView, this.loginStatusView, this.tvLoginStatusMessage, R.string.login_progress_signing_in);
		}
		return v;
	}
	
	
	@Override
	public void onDetach() {
		super.onDetach();
	}

	public void hideScreenKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.loginFormView.getWindowToken(), 0); // скрытие экранной клавиатуры
	}
	
	private User getEnteredUserFromUI(){
		User user = new User();
		user.setLName(this.etLName.getText().toString());
		user.setFName(this.etFName.getText().toString());
		user.setMName(this.etMName.getText().toString());
		GregorianCalendar gc = new GregorianCalendar();
		gc.set(Calendar.YEAR, dpBirthDate.getYear());
		gc.set(Calendar.MONTH, dpBirthDate.getMonth());
		gc.set(Calendar.DAY_OF_MONTH, dpBirthDate.getDayOfMonth());
		user.setBirthDate(gc.getTime());
		user.setPassword(this.etPassword.getText().toString());
		return user;
	}

	public void attemptLogin() {
		User user = getEnteredUserFromUI();
		boolean isCancel = false;
		AuthorizeActivity baseActivity = (AuthorizeActivity) getActivity();
		if (TextUtils.isEmpty(user.getLName())) {
			baseActivity.showMessage(error_field_required);
			focusView = this.etLName;
			isCancel = true;
		}
		if (TextUtils.isEmpty(user.getFName())) {
			baseActivity.showMessage(error_field_required);
			focusView = this.etFName;
			isCancel = true;
		}
		if (TextUtils.isEmpty(user.getMName())) {
			baseActivity.showMessage(error_field_required);
			focusView = this.etMName;
			isCancel = true;
		}		
		if (TextUtils.isEmpty(user.getPassword())) {
			baseActivity.showMessage(error_field_required);
			focusView = this.etPassword;
			isCancel = true;
		}
		if(user.getBirthDate() == null){
			baseActivity.showMessage(error_field_required);
			focusView = this.dpBirthDate;
			isCancel = true;
		}
		if(cbStorePassword.isChecked()){
			user.setIsStorePassword(1);
		}
		if (isCancel) {
			focusView.requestFocus();
		} else {
			hideScreenKeyboard();
			baseActivity.showProgress(true, this.loginFormView, this.loginStatusView, this.tvLoginStatusMessage, R.string.login_progress_signing_in);					
			user.generateLogin();
			((AuthorizeActivity)getActivity()).signIn(user);

		}
	}
	
	public void showProgress(final boolean show, int statusMessageId) {
		AuthorizeActivity baseActivity = ((AuthorizeActivity)getActivity());
		baseActivity.showProgress(show, this.loginFormView, this.loginStatusView, this.tvLoginStatusMessage, statusMessageId);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		AuthorizeActivity baseActivity = ((AuthorizeActivity)getActivity());
		baseActivity.onActivityResult(requestCode, resultCode, data);
	}

}
