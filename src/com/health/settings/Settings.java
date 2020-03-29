package com.health.settings;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.health.data.GeoPhysicsType;
import com.health.data.HelioPhysicsType;
import com.health.data.MoonType;
import com.health.data.ParameterType;
import com.health.data.ParticleType;
import com.health.data.PlanetType;
import com.health.data.SunType;
import com.health.data.WeatherType;
import com.health.main.HealthApplication;
import com.health.repository.IRepository;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Settings {
	private static Set<String> DefaultGraphWeatherVisibility;
	private static Set<String> DefaultGraphAstronomyVisibility;
	private static Set<String> DefaultGraphGeoHelioPhysicsVisibility;
	private static Set<String> DefaultGraphUserVisibility;
	
	private static GregorianCalendar BF_SHOW_DATE_TO = new GregorianCalendar();
	private static GregorianCalendar BF_SHOW_DATE_FROM = new GregorianCalendar();
	
	static {
		BF_SHOW_DATE_FROM.add(Calendar.DAY_OF_YEAR, -20);
		DefaultGraphWeatherVisibility = new HashSet<String>();		
		DefaultGraphWeatherVisibility.add(Integer.toString(WeatherType.PRESSURE_TYPE));
		DefaultGraphWeatherVisibility.add(Integer.toString(WeatherType.TEMPERATURE_TYPE));
		DefaultGraphWeatherVisibility.add(Integer.toString(WeatherType.HUMIDITY_TYPE));
		DefaultGraphWeatherVisibility.add(Integer.toString(WeatherType.CLOUD_TYPE));
		DefaultGraphWeatherVisibility.add(Integer.toString(WeatherType.PRECIPITATION_TYPE));
		DefaultGraphWeatherVisibility.add(Integer.toString(WeatherType.WINDSPEED_TYPE));
		
		DefaultGraphAstronomyVisibility = new HashSet<String>();
		DefaultGraphAstronomyVisibility.add(Integer.toString(SunType.SUN_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(MoonType.MOON_VISIBILITY_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(MoonType.MOON_OLD_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(MoonType.MOON_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(PlanetType.MERCURY_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(PlanetType.VENUS_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(PlanetType.MARS_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(PlanetType.JUPITER_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(PlanetType.SATURN_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(PlanetType.URANUS_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(PlanetType.NEPTUNE_TYPE));
		DefaultGraphAstronomyVisibility.add(Integer.toString(PlanetType.PLUTO_TYPE));
		
		DefaultGraphGeoHelioPhysicsVisibility = new HashSet<String>();
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(GeoPhysicsType.KP_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(GeoPhysicsType.AP_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.F10_7_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.SUNSPOT_NUMBER_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.SUNSPOT_AREA_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.NEW_REGIONS_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.OPTICAL_FLARES_1_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.OPTICAL_FLARES_2_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.OPTICAL_FLARES_3_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.OPTICAL_FLARES_S_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.XRAY_FLARES_C_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.XRAY_FLARES_M_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.XRAY_FLARES_X_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(HelioPhysicsType.XBKGD_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(ParticleType.PROTON_1MEV_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(ParticleType.PROTON_10MEV_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(ParticleType.PROTON_100MEV_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(ParticleType.ELECTRON_08MEV_TYPE));
		DefaultGraphGeoHelioPhysicsVisibility.add(Integer.toString(ParticleType.ELECTRON_2MEV_TYPE));

		DefaultGraphUserVisibility = new HashSet<String>();
		DefaultGraphUserVisibility.add(Integer.toString(WeatherType.PRESSURE_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(WeatherType.TEMPERATURE_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(WeatherType.HUMIDITY_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(WeatherType.CLOUD_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(WeatherType.PRECIPITATION_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(WeatherType.WINDSPEED_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(SunType.SUN_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(MoonType.MOON_VISIBILITY_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(MoonType.MOON_OLD_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(MoonType.MOON_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(PlanetType.MERCURY_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(PlanetType.VENUS_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(PlanetType.MARS_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(PlanetType.JUPITER_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(PlanetType.SATURN_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(PlanetType.URANUS_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(PlanetType.NEPTUNE_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(PlanetType.PLUTO_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(GeoPhysicsType.KP_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(GeoPhysicsType.AP_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.F10_7_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.SUNSPOT_NUMBER_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.SUNSPOT_AREA_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.NEW_REGIONS_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.OPTICAL_FLARES_1_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.OPTICAL_FLARES_2_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.OPTICAL_FLARES_3_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.OPTICAL_FLARES_S_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.XRAY_FLARES_C_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.XRAY_FLARES_M_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.XRAY_FLARES_X_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(HelioPhysicsType.XBKGD_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(ParticleType.PROTON_1MEV_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(ParticleType.PROTON_10MEV_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(ParticleType.PROTON_100MEV_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(ParticleType.ELECTRON_08MEV_TYPE));
		DefaultGraphUserVisibility.add(Integer.toString(ParticleType.ELECTRON_2MEV_TYPE));
				
	}
    
    public static final int IS_SQLLITE_DB = 1;
	
	public static class SettingsData {
		
		public String Login;
		
		public String Password;
	}
	    
    final static String TYPE_PHOTO = "image/jpg";
    public final static int httpTimeOut = 100000; 
    public static final String DEFAULT_ENCODING ="utf-8";
    public static final String DATETIME_FORMAT_UI ="dd.MM.yyyy HH:mm";
    public static final String EXTRA_ID = "id";
    public static final String PREFS_NAME = "HealthPreferencesName";
    public static final String PREFS_LOGIN_KEY = "Login";
    public static final String PREFS_PASSWORD_KEY = "Password";
    public static final String PREFS_GRAPH_WEATHER_VISIBLE_KEY = "GraphWeatherVisible";
    public static final String PREFS_GRAPH_ASTRONOMY_VISIBLE_KEY = "GraphAstronomyVisible";
    public static final String PREFS_GRAPH_GEOHELIOPHYSICS_VISIBLE_KEY = "GraphGeoHelioPhysicsVisible";
    public static final String PREFS_GRAPH_USER_VISIBLE_KEY = "GraphUserVisible";
    public static final String PREFS_IS_FIRST_KEY = "IsFirstStart";
    public static final String TASK_GETDATA = "getdata";
    public static final String TASK_GETSTAT = "getstat";
    public static final String TASK_GETUSERDATA = "getuserdata";
    public static final String TASK_UPLOAD_USERDATA = "uploaduserdata";    
    public static final String TASK_REGISTER_USER = "registeruser";
    public static final String TASK_LOGIN_USER = "loginuser";
    public static final String TASK_RECOVERY_PASSWORD_USER = "recoverypassworduser";
    public static final String TASK_UPDATEPERSONALDATA_USER = "updatepersonaldata";
    //private static String DefaultServerAddress = "http://192.168.53.181/health";
    //private static String DefaultServerAddress = "http://health.pomnimvas.by";
    private static String DefaultServerAddress = "http://health.sesystem.org";
    
    private static String RelativeGetDataUrl = "/getdata_rar.php";
    private static String RelativeGetUserDataUrl = "/getuserdata_rar.php";
    private static String RelativeGetStatUrl = "/getstat.php";
    private static String RelativeGetCityDataUrl = "/getcity.php";
    private static String RelativeGetRecoveryPasswordQuestionUrl = "/get_recovery_password_question.php";
    private static String RelativeUploadUserDataUrl = "/uploaduserdata_rar.php";
    private static String RelativeLoginUrl = "/login.php";
    private static String RelativeRegisterUserUrl = "/register.php";
    private static String RelativeRecoveryPasswordUrl = "/recovery_password.php";
	private static String RelativeGetTestDiaryUrl = "/anonim/get_diary.php";

    public static String getDataUrl(Context context, String args){
    	return DefaultServerAddress + RelativeGetDataUrl + "?" + args;
    }
    
    public static String getStatUrl(Context context, String args){
    	return DefaultServerAddress + RelativeGetStatUrl + "?" + args;
    }
    
    public static String getUploadDataUrl(Context context){
    	return DefaultServerAddress + RelativeUploadUserDataUrl;
    }
    
    public static String getUserDataUrl(Context context){
    	return DefaultServerAddress + RelativeGetUserDataUrl;
    }
    
    public static String getCityDataUrl(Context context){
    	return DefaultServerAddress + RelativeGetCityDataUrl;
    }
    
    public static String getRecoveryPasswordQuestionUrl(Context context){
    	return DefaultServerAddress + RelativeGetRecoveryPasswordQuestionUrl;
    }
    
    public static String getLoginURL(Context context){
    	return DefaultServerAddress + RelativeLoginUrl;
    }
    
    public static String getRegisterUserURL(Context context){
    	return DefaultServerAddress + RelativeRegisterUserUrl;
    }
    
    public static String getRecoveryPasswordUrl(Context context){
    	return DefaultServerAddress + RelativeRecoveryPasswordUrl;
    }

	public static String getTestDiaryUrl(Context context){
		return DefaultServerAddress + RelativeGetTestDiaryUrl;
	}
    
    public static boolean isFirstStartApplication(Context context){
    	IRepository mRepository = ((HealthApplication) ((Activity) context).getApplication()).getRepository();;
    	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME + String.valueOf(mRepository.getCurrentUser().getId()), android.content.Context.MODE_PRIVATE);
    	boolean result = settings.getBoolean(PREFS_IS_FIRST_KEY, true);
        return result;
    }
    
    public static void saveFirstStartApplication(Context context, boolean isFirstStart){
    	IRepository mRepository = ((HealthApplication) ((Activity) context).getApplication()).getRepository();;
    	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME + String.valueOf(mRepository.getCurrentUser().getId()), android.content.Context.MODE_PRIVATE);    	
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(PREFS_IS_FIRST_KEY, isFirstStart);
        editor.commit();
    }
        
    public static SettingsData getSettingData(Context context){
    	IRepository mRepository = ((HealthApplication) ((Activity) context).getApplication()).getRepository();;
    	SettingsData data = new SettingsData();
    	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME + String.valueOf(mRepository.getCurrentUser().getId()), android.content.Context.MODE_PRIVATE);
    	data.Login = settings.getString(PREFS_LOGIN_KEY, null);
        data.Password = settings.getString(PREFS_PASSWORD_KEY, null);        
        return data;
    }
    
    public static void saveSettingsData(Context context, SettingsData settingData){
    	IRepository mRepository = ((HealthApplication) ((Activity) context).getApplication()).getRepository();;
    	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME + String.valueOf(mRepository.getCurrentUser().getId()), android.content.Context.MODE_PRIVATE);    	
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(PREFS_LOGIN_KEY, settingData.Login);
        editor.putString(PREFS_PASSWORD_KEY, settingData.Password);        
        editor.commit();
    }
    
    public static void saveGraphsVisibility(Context context, int parameterGroupId, List<ParameterType> params){
    	IRepository mRepository = ((HealthApplication) ((Activity) context).getApplication()).getRepository();;
    	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME + String.valueOf(mRepository.getCurrentUser().getId()), android.content.Context.MODE_PRIVATE);    	
        SharedPreferences.Editor editor = settings.edit();
        Set<String> values = new HashSet<String>();
        for (ParameterType type : params) {
	        if(type.isVisible()){
	        	values.add(Integer.toString(type.Id));
	        }
        }
        switch (parameterGroupId) {
		case ParameterType.GROUP_WEATHER_TYPE:
			editor.putStringSet(PREFS_GRAPH_WEATHER_VISIBLE_KEY, values);
			break;
		case ParameterType.GROUP_ASTRONOMY_TYPE:
			editor.putStringSet(PREFS_GRAPH_ASTRONOMY_VISIBLE_KEY, values);
			break;
		case ParameterType.GROUP_GEOHELIOPHYSICS_TYPE:
			editor.putStringSet(PREFS_GRAPH_GEOHELIOPHYSICS_VISIBLE_KEY, values);
			break;
		case ParameterType.GROUP_USER_TYPE:
			editor.putStringSet(PREFS_GRAPH_USER_VISIBLE_KEY, values);
			break;
		default:
			break;
		}                       
        editor.commit();
    }
    
    public static List<ParameterType> getGraphsVisibility(Context context, int parameterGroupId, List<ParameterType> params){
    	IRepository mRepository = ((HealthApplication) ((Activity) context).getApplication()).getRepository();;
    	SharedPreferences settings = context.getSharedPreferences(PREFS_NAME + String.valueOf(mRepository.getCurrentUser().getId()), android.content.Context.MODE_PRIVATE);
    	Set<String> values = null;
    	switch (parameterGroupId) {
		case ParameterType.GROUP_WEATHER_TYPE:
			values = settings.getStringSet(PREFS_GRAPH_WEATHER_VISIBLE_KEY, DefaultGraphWeatherVisibility);
			break;
		case ParameterType.GROUP_ASTRONOMY_TYPE:
			values = settings.getStringSet(PREFS_GRAPH_ASTRONOMY_VISIBLE_KEY, DefaultGraphAstronomyVisibility);
			break;
		case ParameterType.GROUP_GEOHELIOPHYSICS_TYPE:
			values = settings.getStringSet(PREFS_GRAPH_GEOHELIOPHYSICS_VISIBLE_KEY, DefaultGraphGeoHelioPhysicsVisibility);
			break;
		case ParameterType.GROUP_USER_TYPE:
			values = settings.getStringSet(PREFS_GRAPH_USER_VISIBLE_KEY, DefaultGraphUserVisibility);
			break;
		default:
			break;
		}
        HashSet<Integer> intValues= new HashSet<Integer>();
        for(String value : values){
        	intValues.add(Integer.parseInt(value));
        }
        for(ParameterType param : params){
        	if(intValues.contains(param.Id)){
        		param.setVisible(true);
        	} else {
        		param.setVisible(false);
        	}
        }
        return params;
    }
        
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isWiFi(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
    }

    public static String getCurrentVersion(Context context) {
        String version = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            version = pInfo.versionName;            
        } catch (PackageManager.NameNotFoundException e) {
            
        }
        return version;
    }
    
    public static void setBFShowPeriod(GregorianCalendar bfDateFrom, GregorianCalendar bfDateTo){    	
    	BF_SHOW_DATE_FROM = bfDateFrom;
    	BF_SHOW_DATE_TO = bfDateTo;
    }
    
    public static GregorianCalendar getBFShowPeriodFrom(){
    	return BF_SHOW_DATE_FROM;
    }
    
    public static GregorianCalendar getBFShowPeriodTo(){
    	return BF_SHOW_DATE_TO;
    }
}
