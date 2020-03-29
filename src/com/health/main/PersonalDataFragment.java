package com.health.main;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.health.data.City;
import com.health.data.Country;
import com.health.data.User;
import com.health.db.DB;
import com.health.db.UserDB;
import com.health.repository.IRepository;
import com.health.task.BaseTask;
import com.health.task.BaseTask.LoginInfo;
import com.health.task.TaskManager;
import com.health.task.TaskManager.OnStateTaskListener;
import com.health.task.TaskResult;

public class PersonalDataFragment extends Fragment implements OnStateTaskListener {
	
	private EditText etLName, etMName, etFName;
	
	private AutoCompleteTextView autoCompleteTVCountry;
	private AutoCompleteTextView autoCompleteTVCity;
	
	private Spinner spSocialStatus, spMaritalStatus, spPressure;
	
	private EditText etHeight, etWeight, etSleepTime;
	
	private SeekBar sbFootDistance;
	private TextView tvFootDistanceValue;

	private DatePicker dpBirthDate;

	private RadioButton rbMen, rbWomen;

	private Button btnPersonalDataSave;
	
	private Locale mCurrentLocale;
	
	private TaskManager mTaskManager;
	
	private User mCurrentUser;
	
	private IRepository mRepository;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		this.mTaskManager = ((HealthApplication) getActivity().getApplication()).getTaskManager();
		this.mRepository = ((HealthApplication) getActivity().getApplication()).getRepository();
		this.mCurrentUser = this.mRepository.getCurrentUser();
	}
	
	@Override
	public void onStart() {
		super.onStart();		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.mCurrentLocale = getResources().getConfiguration().locale;
		LinearLayout wrapper = new LinearLayout(getActivity());
		View mainView = inflater.inflate(R.layout.personal_data_fragment, wrapper, true);
		this.etLName = (EditText) mainView.findViewById(R.id.etLName);
		this.etFName = (EditText) mainView.findViewById(R.id.etFName);
		this.etMName = (EditText) mainView.findViewById(R.id.etMName);
		this.dpBirthDate = (DatePicker) mainView.findViewById(R.id.dpBirthDate);
		this.rbMen = (RadioButton) mainView.findViewById(R.id.rbMan);
		this.rbWomen = (RadioButton) mainView.findViewById(R.id.rbWoman);
		this.btnPersonalDataSave = (Button) mainView.findViewById(R.id.btnSave);
		this.autoCompleteTVCity = (AutoCompleteTextView) mainView.findViewById(R.id.auto_tv_city);
		this.autoCompleteTVCountry = (AutoCompleteTextView) mainView.findViewById(R.id.auto_tv_country);
		this.etLName.setText(mCurrentUser.getLName());
		this.etFName.setText(mCurrentUser.getFName());
		this.etMName.setText(mCurrentUser.getMName());
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(mCurrentUser.getBirthDate());
		this.dpBirthDate.init(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH), null);
		switch (mCurrentUser.getSex()) {
		case User.MAN_SEX:
			rbMen.setChecked(true);
			break;
		case User.WOMAN_SEX:
			rbWomen.setChecked(true);
			break;
		default:
			break;
		}		
		this.autoCompleteTVCity.setText(mCurrentUser.getCity().getName_ru());
		this.autoCompleteTVCountry.setText(mCurrentUser.getCity().getCountry().getName_ru());		
		this.etLName.setEnabled(false);
		this.etFName.setEnabled(false);
		this.etMName.setEnabled(false);
		this.dpBirthDate.setEnabled(false); 
		this.rbMen.setEnabled(false);
		this.rbWomen.setEnabled(false);
		this.autoCompleteTVCity.setEnabled(false);
		this.autoCompleteTVCountry.setEnabled(false);
		
		this.spSocialStatus = (Spinner) mainView.findViewById(R.id.spSocialStatus);
		this.spMaritalStatus = (Spinner) mainView.findViewById(R.id.spMaritalStatus);
		this.spPressure = (Spinner) mainView.findViewById(R.id.spPressure);
		this.etHeight = (EditText) mainView.findViewById(R.id.etHeight);
		this.etWeight = (EditText) mainView.findViewById(R.id.etWeight);
		this.etSleepTime = (EditText) mainView.findViewById(R.id.etSleepTime);
		this.tvFootDistanceValue = (TextView) mainView.findViewById(R.id.tvOnFootDistanceValue);
		this.sbFootDistance = (SeekBar) mainView.findViewById(R.id.sbOnFootDistance);
		initializePersonalData();
		if(mCurrentUser.getSocialStatusId() != null){
			this.spSocialStatus.setSelection(mCurrentUser.getSocialStatusId() - 1);
		}
		if(mCurrentUser.getMaritalStatusId() != null){
			this.spMaritalStatus.setSelection(mCurrentUser.getMaritalStatusId() - 1);
		}
		if(mCurrentUser.getPressureId() != null){
			this.spPressure.setSelection(mCurrentUser.getPressureId() - 1);
		}
		if(mCurrentUser.getHeight() != null){
			this.etHeight.setText(Integer.toString(mCurrentUser.getHeight()));
		}
		if(mCurrentUser.getWeight() != null){
			this.etWeight.setText(Integer.toString(mCurrentUser.getWeight()));
		}
		if(mCurrentUser.getFootDistance() != null){
			this.sbFootDistance.setProgress(mCurrentUser.getFootDistance());
		}
		if(mCurrentUser.getSleepTime() != null){
			this.etSleepTime.setText(Integer.toString(mCurrentUser.getSleepTime()));
		}
		
		this.btnPersonalDataSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {				
				attemptPersonalDataSave();
			}
		});		
		return mainView;
	}
	
	private void initializePersonalData(){		
		ArrayAdapter<String> socialStatusAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.registration_social_status_array));
		socialStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spSocialStatus.setAdapter(socialStatusAdapter);
		
		ArrayAdapter<String> maritalStatusAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.registration_marital_status_array));
		maritalStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spMaritalStatus.setAdapter(maritalStatusAdapter);
		
		ArrayAdapter<String> pressureAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.registration_pressure_array));
		pressureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spPressure.setAdapter(pressureAdapter);						
		
		this.sbFootDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvFootDistanceValue.setText(String.format("%d км", progress));
				tvFootDistanceValue.setTag(progress);
				
			}
		});
		this.sbFootDistance.setProgress(3);
	}

	
	private boolean checkPersonalData() {
		boolean result = true;		
		if (TextUtils.isEmpty(this.etFName.getText())) {
			this.etFName.setError(getResources().getString(R.string.registration_required_field));
			result = false;
		}
		if (TextUtils.isEmpty(this.etLName.getText())) {
			this.etLName.setError(getResources().getString(R.string.registration_required_field));
			result = false;
		}
		if (TextUtils.isEmpty(this.etMName.getText())) {
			this.etMName.setError(getResources().getString(R.string.registration_required_field));
			result = false;
		}
		return result;
	}

	public void attemptPersonalDataSave() {		
		if (!checkPersonalData()) {
			return;
		}
		User currentUser = UserDB.getCurrentUser();
		Integer sex = null;
		if (rbMen.isChecked()) {
			sex = User.MAN_SEX;
		}
		if (rbWomen.isChecked()) {
			sex = User.WOMAN_SEX;
		}	
		LoginInfo loginInfo = new LoginInfo();
		loginInfo.Login = currentUser.getLogin();
		loginInfo.Password = currentUser.getPassword();
		loginInfo.LName = currentUser.getLName();
		loginInfo.MName = currentUser.getMName();
		loginInfo.FName = currentUser.getFName();
		loginInfo.BirthDate = currentUser.getBirthDate();
		loginInfo.Sex = sex;
		loginInfo.CityId = currentUser.getCityId();
		loginInfo.SocialStatusId = spSocialStatus.getSelectedItemPosition() + 1;
		loginInfo.MaritalStatusId = spMaritalStatus.getSelectedItemPosition() + 1;
		if(!TextUtils.isEmpty(etHeight.getText()) && TextUtils.isDigitsOnly(etHeight.getText())){
			loginInfo.Height = Integer.parseInt(etHeight.getText().toString());
		}
		if(!TextUtils.isEmpty(etWeight.getText()) && TextUtils.isDigitsOnly(etWeight.getText())){
			loginInfo.Weight = Integer.parseInt(etWeight.getText().toString());
		}
		loginInfo.PressureId = spPressure.getSelectedItemPosition() + 1;
		if(tvFootDistanceValue.getTag() != null){
			loginInfo.FootDistance = (Integer)tvFootDistanceValue.getTag();
		}
		if(!TextUtils.isEmpty(etSleepTime.getText()) && TextUtils.isDigitsOnly(etSleepTime.getText())){
			loginInfo.SleepTime = Integer.parseInt(etSleepTime.getText().toString());
		}		
		this.mTaskManager.startUpdatePersonalData(getActivity(), loginInfo, this);
	}
	
	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
    public void onComplete(BaseTask task, TaskResult taskResult) {
	    if(!taskResult.isError()){
	    	
	    }	    
    }

	@Override
    public void onStartTask() {
	    
    }

	@Override
    public void onStopTask() {
	    	    
    }

	

	
	
	

}
