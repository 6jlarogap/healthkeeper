package com.health.main;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.data.BaseDTO;
import com.health.data.City;
import com.health.data.CityDao;
import com.health.data.Country;
import com.health.data.RecoveryAccountQuestion;
import com.health.data.User;
import com.health.db.DB;
import com.health.settings.Settings;
import com.health.task.AsyncTaskCompleteListener;
import com.health.task.AsyncTaskProgressListener;
import com.health.task.BaseTask;
import com.health.task.RegisterUserTask;
import com.health.task.TaskResult;
import com.health.util.HttpUtils;

public class RegisterFragment extends Fragment implements AsyncTaskProgressListener, AsyncTaskCompleteListener<TaskResult> {
	
	private View mainView;

	private EditText etPassword, etPasswordAgain, etLName, etMName, etFName, etCaptcha, etAnswer1, etAnswer2;
	
	private Spinner spinnerQuestion1, spinnerQuestion2;
	
	private ImageView ivCaptcha;
	private ProgressBar pbCaptcha;

	private AutoCompleteTextView autoCompleteTVCountry;
	private AutoCompleteTextView autoCompleteTVCity;

	private TextView tvRegisterStatus;

	private DatePicker dpBirthDate;

	private RadioButton rbMen, rbWomen;

	private RegisterUserTask mRegisterUserTask = null;

	private Button btnRegistration;
	private Button btnCaptchaRefresh;

	private ProgressDialog pdRegistration;

	public String mRegisterStatus = "";
	public int mRegisterStatusColor = R.color.red;
	private TaskResult mLastTaskResult;
	private Timer mTimer;

	private Locale mCurrentLocale;
	
	private Location mCurrentLocation;
	
	private GetCityTask mGetCityTask;
	
	private GetCaptchaTask mGetCaptchaTask;
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.mCurrentLocale = getResources().getConfiguration().locale;
		if(this.mainView == null){
			LinearLayout wrapper = new LinearLayout(getActivity());
			this.mainView = inflater.inflate(R.layout.register_fragment, wrapper, true);		
			this.tvRegisterStatus = (TextView) mainView.findViewById(R.id.tvRegisterStatus);
			this.etPassword = (EditText) mainView.findViewById(R.id.etPassword);
			this.etPasswordAgain = (EditText) mainView.findViewById(R.id.etPasswordAgain);
			this.etLName = (EditText) mainView.findViewById(R.id.etLName);
			this.etFName = (EditText) mainView.findViewById(R.id.etFName);
			this.etMName = (EditText) mainView.findViewById(R.id.etMName);
			this.dpBirthDate = (DatePicker) mainView.findViewById(R.id.dpBirthDate);
			this.rbMen = (RadioButton) mainView.findViewById(R.id.rbMan);
			this.rbWomen = (RadioButton) mainView.findViewById(R.id.rbWoman);
			this.btnRegistration = (Button) mainView.findViewById(R.id.btnRegistration);
			this.autoCompleteTVCity = (AutoCompleteTextView) mainView.findViewById(R.id.auto_tv_city);
			this.autoCompleteTVCountry = (AutoCompleteTextView) mainView.findViewById(R.id.auto_tv_country);
			this.ivCaptcha = (ImageView) mainView.findViewById(R.id.ivCaptcha);
			this.pbCaptcha = (ProgressBar) mainView.findViewById(R.id.pbCaptcha);
			this.etCaptcha = (EditText) mainView.findViewById(R.id.etCaptcha);
			this.btnCaptchaRefresh = (Button) mainView.findViewById(R.id.btnCaptchaRefresh);
			this.etAnswer1 = (EditText) mainView.findViewById(R.id.etAnswer1);
			this.etAnswer2 = (EditText) mainView.findViewById(R.id.etAnswer2);
			this.spinnerQuestion1 = (Spinner) mainView.findViewById(R.id.spinnerQuestion1);
			this.spinnerQuestion2 = (Spinner) mainView.findViewById(R.id.spinnerQuestion2);
						
			Calendar calendar = GregorianCalendar.getInstance();			
			this.dpBirthDate.updateDate(calendar.get(Calendar.YEAR) - 40, 1, 1);
			initializeCaptcha();
			if(this.spinnerQuestion1.getAdapter() == null){
				DB.db().newSession().getRecoveryAccountQuestionDao().loadAll();
				List<RecoveryAccountQuestion> questionList = DB.db().newSession().getRecoveryAccountQuestionDao().loadAll();				
				ArrayAdapter<RecoveryAccountQuestion> questionAdapter = new ArrayAdapter<RecoveryAccountQuestion>(getActivity(), android.R.layout.simple_spinner_item, questionList);
				questionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				this.spinnerQuestion1.setAdapter(questionAdapter);
				this.spinnerQuestion1.setSelection(0);				
				this.spinnerQuestion2.setAdapter(questionAdapter);
				this.spinnerQuestion2.setSelection(1);
			}
		}
		
		this.btnRegistration.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Settings.saveFirstStartApplication(getActivity(), false);
				attemptRegister();
			}
		});
		
		this.btnCaptchaRefresh.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initializeCaptcha();				
			}
		});

		if (this.mRegisterUserTask != null && this.mRegisterUserTask.getStatus() == AsyncTask.Status.RUNNING) {
			showProgress(true);
		}
		fillAutoCompleteCountry();
		showRegistrationStatus();		
		return this.mainView;

	}
	
	private void initializeCaptcha(){
		this.mGetCaptchaTask = new GetCaptchaTask(this, this);
		this.mGetCaptchaTask.execute(Settings.getRegisterUserURL(getActivity()));
	}
	

	private void fillAutoCompleteCountry() {
		List<Country> countries = DB.db().newSession().getCountryDao().loadAll();
		final CountryAdapter countryAdapter = new CountryAdapter(getActivity(), R.layout.country_item, countries);
		this.autoCompleteTVCountry.setAdapter(countryAdapter);
		this.autoCompleteTVCountry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					updateSelectedCountry();
				}
			}
		});
		this.autoCompleteTVCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {				
				countryAdapter.updateFilter(autoCompleteTVCountry.getText().toString());
			}
		});
		if(this.autoCompleteTVCountry.getTag() != null){			
			Country selectedCountry = (Country) this.autoCompleteTVCountry.getTag();
			fillAutoCompleteCity(selectedCountry);
		}
		
	}

	private void fillAutoCompleteCity(Country country) {
		List<City> cities = new ArrayList<City>();
		cities = DB.db().newSession().getCityDao().queryBuilder().where(CityDao.Properties.CountryId.eq(country.getId())).list();				
		final CityAdapter cityAdapter = new CityAdapter(getActivity(), android.R.layout.simple_list_item_1, cities);
		this.autoCompleteTVCity.setAdapter(cityAdapter);
		
		this.autoCompleteTVCity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					updateSelectedCity();
				}
			}
		});
		this.autoCompleteTVCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {				
				cityAdapter.updateFilter(autoCompleteTVCity.getText().toString());
			}
		});
	}
	
	private void updateSelectedCountry(){
		if (!TextUtils.isEmpty(autoCompleteTVCountry.getText())) {
			CountryAdapter countryAdapter = (CountryAdapter) autoCompleteTVCountry.getAdapter();
			String enteredCountryName = autoCompleteTVCountry.getText().toString();
			if (!countryAdapter.isRightCountryName(enteredCountryName)) {
				autoCompleteTVCountry.setError(getResources().getString(R.string.registration_country_invalid));
			} else {
				autoCompleteTVCountry.setError(null);
				Country selectedCountry = countryAdapter.getSelectedCountry();
				if (selectedCountry != null) {								
					Object tag = autoCompleteTVCountry.getTag();								
					autoCompleteTVCountry.setTag(selectedCountry);
					if(tag == null || ((Country) tag).getId() != selectedCountry.getId()){
						autoCompleteTVCity.setText(null);
						autoCompleteTVCity.setTag(null);
						fillAutoCompleteCity(selectedCountry);
					}					
				}
			}

		} else {
			autoCompleteTVCountry.setError(getResources().getString(R.string.registration_required_field));
		}
	}
	
	private void updateSelectedCity(){
		if (!TextUtils.isEmpty(autoCompleteTVCity.getText())) {
			CityAdapter cityAdapter = (CityAdapter) autoCompleteTVCity.getAdapter();
			if(cityAdapter != null){
    			String enteredCityName = autoCompleteTVCity.getText().toString();
    			if (!cityAdapter.isRightCityName(enteredCityName)) {
    				autoCompleteTVCity.setError(getResources().getString(R.string.registration_city_invalid));
    			} else {
    				autoCompleteTVCity.setError(null);
    				City selectedCity = cityAdapter.getSelectedCity();
    				if (selectedCity != null) {
    					autoCompleteTVCity.setTag(selectedCity);								
    				}
    			}
			}

		} else {
			autoCompleteTVCity.setError(getResources().getString(R.string.registration_required_field));
		}
	}

	public class CountryAdapter extends ArrayAdapter<Country> {		
		private List<Country> mItems;
		private List<Country> mItemsAll;
		private List<Country> mSuggestions;
		private int mViewResourceId;

		public CountryAdapter(Context context, int viewResourceId, List<Country> items) {
			super(context, viewResourceId, items);
			this.mItems = items;
			this.mItemsAll = new ArrayList<Country>();
			this.mItemsAll.addAll(items);
			this.mSuggestions = new ArrayList<Country>();
			this.mViewResourceId = viewResourceId;

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(mViewResourceId, null);
			}
			Country country = mItems.get(position);
			if (country != null) {
				TextView tvCountry = (TextView) v.findViewById(R.id.textView1);
				ImageView ivFlag = (ImageView) v.findViewById(R.id.imageView1);
				tvCountry.setText(country.getName(mCurrentLocale));
				String pathFlag = String.format("flags/%s.png", country.getCode().toLowerCase());
				try {
					InputStream inputStreamFlag = getActivity().getAssets().open(pathFlag);
					Drawable flagDrawable = Drawable.createFromStream(inputStreamFlag, null);
					ivFlag.setImageDrawable(flagDrawable);
				} catch (IOException exc) {
					// do nothing
				}

			}
			return v;
		}

		public boolean isRightCountryName(String countryName) {
			boolean result = false;
			if (this.mSuggestions.size() == 1) {
				Country country = this.mSuggestions.get(0);
				if (countryName.equalsIgnoreCase(country.getName()) || (country.getName_ru() != null && countryName.equalsIgnoreCase(country.getName_ru()))) {
					result = true;
				}
			}
			return result;
		}

		public Country getSelectedCountry() {
			if (this.mSuggestions != null && this.mSuggestions.size() == 1) {
				return this.mSuggestions.get(0);
			}
			return null;
		}

		public void updateFilter(String countryName) {
			nameFilter.filter(countryName);
		}

		@Override
		public Filter getFilter() {
			return nameFilter;
		}

		Filter nameFilter = new Filter() {

			@Override
			public String convertResultToString(Object resultValue) {
				String str = ((Country) (resultValue)).getName(getResources().getConfiguration().locale);
				return str;
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				if (constraint != null) {
					mSuggestions.clear();
					String constraintString = constraint.toString().toLowerCase();
					for (Country country : mItemsAll) {
						if (country.getName().toLowerCase().startsWith(constraintString) || (country.getName_ru() != null && country.getName_ru().toLowerCase().startsWith(constraintString))) {
							mSuggestions.add(country);
						}
					}
					FilterResults filterResults = new FilterResults();
					filterResults.values = mSuggestions;
					filterResults.count = mSuggestions.size();
					return filterResults;
				} else {
					return new FilterResults();
				}
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				ArrayList<Country> filteredList = (ArrayList<Country>) results.values;
				if (results != null && results.count > 0) {
					clear();
					for (Country c : filteredList) {
						add(c);
					}
					notifyDataSetChanged();
				}
			}
		};

	}
	
	public class CityAdapter extends ArrayAdapter<City> {		
		private List<City> mItems;
		private List<City> mItemsAll;
		private List<City> mSuggestions;
		private int mViewResourceId;

		public CityAdapter(Context context, int viewResourceId, List<City> items) {
			super(context, viewResourceId, items);
			this.mItems = items;
			this.mItemsAll = new ArrayList<City>();
			this.mItemsAll.addAll(items);
			this.mSuggestions = new ArrayList<City>();
			this.mViewResourceId = viewResourceId;

		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(mViewResourceId, null);
			}
			City city = mItems.get(position);
			if (city != null) {
				TextView tvCity = (TextView) v.findViewById(android.R.id.text1);				
				tvCity.setText(city.getName(mCurrentLocale));
			}
			return v;
		}

		public boolean isRightCityName(String cityName) {
			boolean result = false;
			if (this.mSuggestions.size() == 1) {
				City city = this.mSuggestions.get(0);
				if (cityName.equalsIgnoreCase(city.getName()) || (city.getName_ru() != null && cityName.equalsIgnoreCase(city.getName_ru()))) {
					result = true;
				}
			}
			return result;
		}

		public City getSelectedCity() {
			if (this.mSuggestions != null && this.mSuggestions.size() == 1) {
				return this.mSuggestions.get(0);
			}
			return null;
		}

		public void updateFilter(String countryName) {
			nameFilter.filter(countryName);
		}

		@Override
		public Filter getFilter() {
			return nameFilter;
		}

		Filter nameFilter = new Filter() {

			@Override
			public String convertResultToString(Object resultValue) {
				String str = ((City) (resultValue)).getName(mCurrentLocale);
				return str;
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				if (constraint != null) {
					mSuggestions.clear();
					String constraintString = constraint.toString().toLowerCase();
					for (City city : mItemsAll) {
						if (city.getName().toLowerCase().startsWith(constraintString) || (city.getName_ru() != null && city.getName_ru().toLowerCase().startsWith(constraintString))) {
							mSuggestions.add(city);
						}
					}
					FilterResults filterResults = new FilterResults();
					filterResults.values = mSuggestions;
					filterResults.count = mSuggestions.size();
					return filterResults;
				} else {
					return new FilterResults();
				}
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				ArrayList<City> filteredList = (ArrayList<City>) results.values;
				if (results != null && results.count > 0) {
					clear();
					for (City c : filteredList) {
						add(c);
					}
					notifyDataSetChanged();
				}
			}
		};

	}

	private boolean checkRegisterData() {
		boolean result = true;		
		if (TextUtils.isEmpty(this.etPassword.getText())) {
			this.etPassword.setError(getResources().getString(R.string.registration_required_field));
			result = false;
		}
		if (TextUtils.isEmpty(this.etPasswordAgain.getText())) {
			this.etPasswordAgain.setError(getResources().getString(R.string.registration_required_field));
			result = false;
		}
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
		if (TextUtils.isEmpty(this.autoCompleteTVCountry.getText())) {
			this.autoCompleteTVCountry.setError(getResources().getString(R.string.registration_required_field));
			result = false;
		}
		if (TextUtils.isEmpty(this.autoCompleteTVCity.getText())) {
			this.autoCompleteTVCity.setError(getResources().getString(R.string.registration_required_field));
			result = false;
		}
		if(this.autoCompleteTVCountry.getTag() == null){
			this.autoCompleteTVCountry.setError(getResources().getString(R.string.registration_country_invalid));
			result = false;
		}
		if(this.autoCompleteTVCity.getTag() == null){
			this.autoCompleteTVCity.setError(getResources().getString(R.string.registration_city_invalid));
			result = false;
		}
		if(TextUtils.isEmpty(this.etCaptcha.getText().toString())){
			this.etCaptcha.setText(null);
			this.etCaptcha.setError(getResources().getString(R.string.registration_captcha_invalid));
			result = false;
		}		
		if (TextUtils.isEmpty(this.etAnswer1.getText()) && !TextUtils.isEmpty(this.etAnswer2.getText())) {
			this.etFName.setError(getResources().getString(R.string.registration_required_all_answer));
			result = false;
		}
		if (TextUtils.isEmpty(this.etAnswer2.getText()) && !TextUtils.isEmpty(this.etAnswer1.getText())) {
			this.etFName.setError(getResources().getString(R.string.registration_required_all_answer));
			result = false;
		}
		if(this.spinnerQuestion1.getSelectedItemId() == this.spinnerQuestion2.getSelectedItemId()){
			this.etAnswer2.setError(getResources().getString(R.string.registration_required_different_question));
			result = false;
		}

		if (result) {
			if (!TextUtils.equals(etPassword.getText(), etPasswordAgain.getText())) {
				this.etPassword.setError(getActivity().getResources().getString(R.string.registration_password_notmatch));
				this.etPassword.setText(null);
				this.etPasswordAgain.setText(null);
				result = false;
			}
		}

		return result;
	}
	
	public void attemptRegister() {
		updateSelectedCountry();
		updateSelectedCity();		
		if (!checkRegisterData()) {
			return;
		}
		showProgress(true);
		User user = new User();
		String url = Settings.getRegisterUserURL(getActivity());		
		user.setPassword(etPassword.getText().toString());
		user.setFName(etFName.getText().toString());
		user.setMName(etMName.getText().toString());
		user.setLName(etLName.getText().toString());		
		if (rbMen.isChecked()) {
			user.setSex(User.MAN_SEX);
		}
		if (rbWomen.isChecked()) {
			user.setSex(User.WOMAN_SEX);
		}
		Date birthDate = new Date(dpBirthDate.getYear() - 1900, dpBirthDate.getMonth(), dpBirthDate.getDayOfMonth());
		SimpleDateFormat sdf = new SimpleDateFormat(BaseDTO.DATE_FORMAT);
		String birthDateString = sdf.format(birthDate);
		user.setBirthDate(birthDate);
		long cityId = ((City)autoCompleteTVCity.getTag()).getId();
		this.mRegisterUserTask = new RegisterUserTask(this, this, getActivity());
		user.generateLogin();
		user.Answer1 = this.etAnswer1.getText().toString();
		user.Answer2 = this.etAnswer2.getText().toString();
		user.setQuestion1(((RecoveryAccountQuestion) spinnerQuestion1.getSelectedItem()).getId());
		user.setQuestion2(((RecoveryAccountQuestion) spinnerQuestion2.getSelectedItem()).getId());		
		this.mRegisterUserTask.execute(url, user.getLogin(), user.getPassword(), user.getFName(), user.getLName(), user.getMName(), birthDateString, Integer.toString(user.getSex()), Long.toString(cityId), 
				Long.toString(user.getQuestion1()), user.Answer1, Long.toString(user.getQuestion2()), user.Answer2,
				etCaptcha.getText().toString(), ivCaptcha.getTag().toString());
	}
	
	

	private void showProgress(boolean show) {
		if (show) {
			this.pdRegistration = new ProgressDialog(getActivity());
			this.pdRegistration.setTitle(R.string.action_register);
			this.pdRegistration.setMessage(getActivity().getString(R.string.action_register));
			this.pdRegistration.setProgressDrawable(getActivity().getResources().getDrawable(android.R.drawable.progress_horizontal));
			this.pdRegistration.setCancelable(false);
			this.pdRegistration.show();
		} else {
			if (this.pdRegistration != null) {
				this.pdRegistration.cancel();
				this.pdRegistration = null;
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);		
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		        mCurrentLocation = location;		        
		        setCurrentCity(location);
		    }

		    public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {}
		};		
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
		locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, locationListener);
	}
	
	public void setCurrentCity(Location location){
		if(location != null){
			if(this.mGetCityTask == null){
				double lat = location.getLatitude();				
		    	double lng = location.getLongitude();
		    	String url = String.format("%s?lat=%s&lng=%s", Settings.getCityDataUrl(getActivity()), Double.toString(lat), Double.toString(lng));
		    	this.mGetCityTask = new GetCityTask(null, null);
		    	this.mGetCityTask.execute(url);
			}
		}
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
	public void onTaskComplete(BaseTask task, TaskResult result) {
		if(task instanceof RegisterUserTask){
			this.mRegisterUserTask = null;
			this.mLastTaskResult = result;
			showProgress(false);
			if (result.getStatus() == TaskResult.Status.LOGIN_SUCCESSED) {
				this.mRegisterStatusColor = getActivity().getResources().getColor(R.color.green);
				this.mRegisterStatus = getActivity().getResources().getString(R.string.registration_user_success);
				this.mTimer = new Timer();
				this.mTimer.schedule(new OpenLoginFragmentTask(), 2 * 1000);
			} else {
				initializeCaptcha();
				this.mRegisterStatusColor = getActivity().getResources().getColor(R.color.red);
				this.mRegisterStatus = result.getErrorText();
			}
			showRegistrationStatus();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void showRegistrationStatus() {
		boolean isShow = !TextUtils.isEmpty(this.mRegisterStatus);
		this.tvRegisterStatus.setVisibility(isShow ? View.VISIBLE : View.GONE);
		this.tvRegisterStatus.setText(this.mRegisterStatus);
		this.tvRegisterStatus.setTextColor(this.mRegisterStatusColor);
	}

	public void cleanRegisterStatus() {
		this.mRegisterStatus = "";
		showRegistrationStatus();
	}

	@Override
	public void onProgressUpdate(String... messages) {
		// TODO Auto-generated method stub
	}

	class OpenLoginFragmentTask extends TimerTask {
		public void run() {
			if (getActivity() != null && mLastTaskResult != null) {
				((AuthorizeActivity) getActivity()).onSuccessUserRegistration(mLastTaskResult.getLoginInfo());
			}
		}
	}
	
	class GetCaptchaTask extends BaseTask {
		
		private Bitmap mBmpCaptha;
		
		private String mCookiesValue;
		
        public GetCaptchaTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb) {
	        super(pl, cb);	        
        }
        
        @Override
        protected void init() {	        
	        
        }
        
        @Override
	    protected void onPreExecute() {
        	super.onPreExecute();
        	btnCaptchaRefresh.setEnabled(false);
        	ivCaptcha.setImageBitmap(null);
        	pbCaptcha.setVisibility(View.VISIBLE);
        }

		@Override
        protected TaskResult doInBackground(String... params) {
			TaskResult result = new TaskResult();
	        try {
	        	String url = params[0];
	        	Connection.Response response = Jsoup.connect(url).timeout(10 * 1000).method(Method.GET).execute();
	        	Document doc = response.parse();	        	
	        	StringBuffer sb = new StringBuffer();	        	
	        	for (String key : response.cookies().keySet())
	        	{
	        	    sb.append(String.format("%s=%s;", key, response.cookie(key)));
	        	}
	        	this.mCookiesValue = sb.toString();	        	
	            Element image = doc.select("img").first();
	            String imageUrlString = image.absUrl("src");	            
	            Connection.Response resultImageResponse = Jsoup.connect(imageUrlString).cookies(response.cookies()).ignoreContentType(true).execute();
                this.mBmpCaptha = BitmapFactory.decodeByteArray(resultImageResponse.bodyAsBytes(), 0, resultImageResponse.bodyAsBytes().length);	             
            } catch (Exception e) {
	            result.setError(true);	            
            }
	        return result;
        }
		
		@Override
	    protected void onPostExecute(TaskResult result) {
	        super.onPostExecute(result);
	        btnCaptchaRefresh.setEnabled(true);
	        pbCaptcha.setVisibility(View.GONE);
	        if(!result.isError()){
    	        ivCaptcha.setImageBitmap(mBmpCaptha);
    	        ivCaptcha.setTag(mCookiesValue);
    	    } else {
    	    	ivCaptcha.setImageResource(R.drawable.ic_error_black_24dp);
    	        ivCaptcha.setTag(null);
    	    }
	    }		
	}
	
	class GetCityTask extends BaseTask{
		
		private City mResultCity;
		
        public GetCityTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb) {
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
	            this.mResultCity = getCityFromJSON(json);	            
            } catch (Exception e) {
	            result.setError(false);
            }
	        return result;
        }
		
		@Override
	    protected void onPostExecute(TaskResult result) {
	        super.onPostExecute(result);
	        if(this.mResultCity != null && isVisible()){
	        	autoCompleteTVCountry.setText(this.mResultCity.getCountry().getName(mCurrentLocale));
	        	autoCompleteTVCountry.setTag(this.mResultCity.getCountry());
	        	autoCompleteTVCity.setText(this.mResultCity.getName(mCurrentLocale));
	        	autoCompleteTVCity.setTag(this.mResultCity);
	        	Toast.makeText(getActivity(), "Ваше текущее местоположение определено", Toast.LENGTH_LONG).show();
	        }
	    }		
	}
	
	private City getCityFromJSON(String resultJSON){
		City result = null;
    	ObjectMapper mapper = new ObjectMapper();   	    	
		try {
			JsonNode rootNode = mapper.readTree(resultJSON);			
			JsonNode citiesNode = rootNode.path("cities");
			String citiesString = citiesNode.toString();					
			City[] cities = mapper.readValue(citiesString, City[].class);
			if(cities.length > 0){
				result = cities[0];
				result.setCountryId(result.getCountryId());
			}
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();			
		} catch (IOException e) {			
			e.printStackTrace();
		}
		return result;
    }

}
