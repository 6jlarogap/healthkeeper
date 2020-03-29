package com.health.main;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.data.RecoveryAccountQuestion;
import com.health.data.User;
import com.health.settings.Settings;
import com.health.task.AsyncTaskCompleteListener;
import com.health.task.AsyncTaskProgressListener;
import com.health.task.BaseTask;
import com.health.task.TaskResult;
import com.health.util.HttpUtils;

public class RecoveryPasswordFragment extends Fragment {
	private EditText etPassword1, etPassword2, etFName, etLName, etMName;
	private DatePicker dpBirthDate;
	private Button btnChangePassword;
	private TextView tvQuestion1, tvQuestion2;
	private EditText etAnswer1, etAnswer2;
	
	private String error_field_required;

	private View focusView;
	private View formView;
	private View statusView;
	private TextView tvStatusMessage;
	private String mLastLoginForGetRecoveryQuestion = null;
	
	private GetRecoveryPasswordQuestionTask mGetRecoveryPasswordQuestionTask;
	
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
		View v = (View) inflater.inflate(R.layout.recovery_password_fragment, wrapper, true);
		this.formView = v.findViewById(R.id.recovery_password_form);
		this.statusView = v.findViewById(R.id.login_status);
		this.tvStatusMessage = (TextView) v.findViewById(R.id.login_status_message);
		this.btnChangePassword = (Button) v.findViewById(R.id.btnChangePassword);		
		this.etLName = (EditText) v.findViewById(R.id.etLName);
		this.etMName = (EditText) v.findViewById(R.id.etMName);
		this.etFName = (EditText) v.findViewById(R.id.etFName);
		this.dpBirthDate = (DatePicker) v.findViewById(R.id.dpBirthDate);
		this.etPassword1 = (EditText) v.findViewById(R.id.etPassword1);
		this.etPassword2 = (EditText) v.findViewById(R.id.etPassword2);
		this.etAnswer1 = (EditText) v.findViewById(R.id.etAnswer1);
		this.etAnswer2 = (EditText) v.findViewById(R.id.etAnswer2);
		this.tvQuestion1 = (TextView) v.findViewById(R.id.tvQuestion1);
		this.tvQuestion2 = (TextView) v.findViewById(R.id.tvQuestion2);
		String lname = getActivity().getIntent().getStringExtra(AuthorizeActivity.EXTRA_LNAME);
		String fname = getActivity().getIntent().getStringExtra(AuthorizeActivity.EXTRA_FNAME);
		String mname = getActivity().getIntent().getStringExtra(AuthorizeActivity.EXTRA_MNAME);
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
		if(birthDateLongMs != Long.MIN_VALUE){
			Calendar gc = new GregorianCalendar();
			gc.setTimeInMillis(birthDateLongMs);			
			dpBirthDate.init(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH), null);			
		}

		this.btnChangePassword.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptChangePassword();
			}
		});		
		this.error_field_required = getActivity().getResources().getString(R.string.error_field_required);
		AuthorizeActivity baseActivity = (AuthorizeActivity)getActivity();
		if(baseActivity.isProgressChangePassword()){
			baseActivity.showProgress(true, this.formView, this.statusView, this.tvStatusMessage, R.string.action_changing_password);
		}
		User user = getEnteredUserFromUI();
		user.generateLogin();
		getRecoveryQuestionForUser(user.getLogin());
		View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus == false){
					User user = getEnteredUserFromUI();
					user.generateLogin();
					getRecoveryQuestionForUser(user.getLogin());
				}
				
			}
		};
		this.etFName.setOnFocusChangeListener(onFocusChangeListener);
		this.etLName.setOnFocusChangeListener(onFocusChangeListener);
		this.etMName.setOnFocusChangeListener(onFocusChangeListener);
		
		View.OnFocusChangeListener onFocusChangeListener2 = new View.OnFocusChangeListener() {			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus == true){
					User user = getEnteredUserFromUI();
					user.generateLogin();
					getRecoveryQuestionForUser(user.getLogin());
				}
				
			}
		};
		this.etPassword1.setOnFocusChangeListener(onFocusChangeListener2);
		this.etPassword2.setOnFocusChangeListener(onFocusChangeListener2);
		this.etAnswer1.setOnFocusChangeListener(onFocusChangeListener2);
		this.etAnswer2.setOnFocusChangeListener(onFocusChangeListener2);
		return v;
	}
	
	
	@Override
	public void onDetach() {
		super.onDetach();
	}

	public void hideScreenKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.formView.getWindowToken(), 0); // скрытие экранной клавиатуры
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
		user.setPassword(this.etPassword1.getText().toString());
		user.Answer1 = this.etAnswer1.getText().toString();
		user.Answer2 = this.etAnswer2.getText().toString();
		return user;
	}

	public void attemptChangePassword() {
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
			focusView = this.etPassword1;
			isCancel = true;
		}
		if(!etPassword1.getText().toString().equals(etPassword2.getText().toString())){
			baseActivity.showMessage(getString(R.string.registration_password_notmatch));
			focusView = this.etPassword1;
			isCancel = true;
		}
		if(user.getBirthDate() == null){
			baseActivity.showMessage(error_field_required);
			focusView = this.dpBirthDate;
			isCancel = true;
		}
		if (TextUtils.isEmpty(user.Answer1)) {
			baseActivity.showMessage(error_field_required);
			focusView = this.etAnswer1;
			isCancel = true;
		}
		if (TextUtils.isEmpty(user.Answer2)) {
			baseActivity.showMessage(error_field_required);
			focusView = this.etAnswer2;
			isCancel = true;
		}
		if (isCancel) {
			focusView.requestFocus();
		} else {
			hideScreenKeyboard();
			baseActivity.showProgress(true, this.formView, this.statusView, this.tvStatusMessage, R.string.action_changing_password);					
			user.generateLogin();
			((AuthorizeActivity)getActivity()).changePassword(user);
		}
	}
	
	public void getRecoveryQuestionForUser(String loginName){		
		if((this.mGetRecoveryPasswordQuestionTask == null || this.mGetRecoveryPasswordQuestionTask.getStatus() == AsyncTask.Status.FINISHED) && !loginName.equals(this.mLastLoginForGetRecoveryQuestion)){
			this.mLastLoginForGetRecoveryQuestion = loginName;
			String url = String.format("%s?name=%s", Settings.getRecoveryPasswordQuestionUrl(getActivity()), loginName);
	    	this.mGetRecoveryPasswordQuestionTask = new GetRecoveryPasswordQuestionTask(null, null);
	    	this.mGetRecoveryPasswordQuestionTask.execute(url);
		}		
	}
	
	class GetRecoveryPasswordQuestionTask extends BaseTask {
		
		private RecoveryAccountQuestion[] Result;
						
        public GetRecoveryPasswordQuestionTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb) {
	        super(pl, cb);	        
        }
        
        @Override
        protected void init() {	        
	        
        }		

		@Override
        protected TaskResult doInBackground(String... params) {
			TaskResult result = new TaskResult();
	        try {
	        	String url = params[0];
	            String json = HttpUtils.getJSON(url);
	            this.Result = getQuestionFromJSON(json);	            
            } catch (Exception e) {
	            result.setError(false);
	            mLastLoginForGetRecoveryQuestion = null;
            }
	        return result;
        }
		
		@Override
	    protected void onPostExecute(TaskResult result) {
	        super.onPostExecute(result);
	        if(this.Result != null && isVisible()){
	        	if(this.Result[0] != null){
	        		tvQuestion1.setText(String.format("Ответьте на вопрос №1: %s", this.Result[0].getQuestion() != null ? this.Result[0].getQuestion() : ""));
	        	}
	        	if(this.Result[1] != null){
	        		tvQuestion2.setText(String.format("Ответьте на вопрос №2: %s", this.Result[1].getQuestion() != null ? this.Result[1].getQuestion() : ""));
	        	}
	        }
	    }
		
	}
	
	private RecoveryAccountQuestion[] getQuestionFromJSON(String resultJSON){
		ObjectMapper mapper = new ObjectMapper();
		RecoveryAccountQuestion[] result = new RecoveryAccountQuestion[2];
		try {
			JsonNode rootNode = mapper.readTree(resultJSON);
			result[0] = new RecoveryAccountQuestion();
			result[0].setId(new Long(rootNode.get("question1").intValue()));
			result[0].setQuestion(rootNode.get("questiontext1").textValue());
			result[1] = new RecoveryAccountQuestion();
			result[1].setId(new Long(rootNode.get("question2").intValue()));
			result[1].setQuestion(rootNode.get("questiontext2").textValue());
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();			
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return result;
    }
	
	public void showProgress(final boolean show, int statusMessageId) {
		AuthorizeActivity baseActivity = ((AuthorizeActivity)getActivity());
		baseActivity.showProgress(show, this.formView, this.statusView, this.tvStatusMessage, statusMessageId);
	}

}
