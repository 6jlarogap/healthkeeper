package com.health.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.data.BaseSyncDTO;
import com.health.data.BaseUserSyncDTO;
import com.health.data.Complaint;
import com.health.data.BaseDTO;
import com.health.data.BodyFeeling;
import com.health.data.CommonFeeling;
import com.health.data.CustomBodyFeelingType;
import com.health.data.CustomCommonFeelingType;
import com.health.data.CustomFactorType;
import com.health.data.DownloadPeriod;
import com.health.data.Factor;
import com.health.data.OperationUserData;
import com.health.data.User;
import com.health.data.UserBodyFeelingType;
import com.health.db.DB;
import com.health.main.R;
import com.health.settings.Settings;
import com.health.util.HttpUtils;

public abstract class BaseTask extends AsyncTask<String, String, TaskResult> {

    AsyncTaskProgressListener progressListener;
    AsyncTaskCompleteListener<TaskResult> callback;
    Context mainContext;
    TaskResult mTaskResult;
    protected String mTaskName = null;
    

    public static final String ARG_LOGIN = "name";
    public static final String ARG_PASSWORD = "password";
    public static final String ARG_SYNC_DATE = "syncdate";
    public static final String ARG_DATA = "data";
    public static final String ARG_RECOVERYPASSWORD_ANSWER1 = "answer1";
    public static final String ARG_RECOVERYPASSWORD_ANSWER2 = "answer2";
    
    public static final TimeZone UTC_TIMEZONE = TimeZone.getTimeZone("UTC");

    protected String networkError = "";
    protected String mProgressDialogTitle = "";
    protected String mProgressDialogMessage = "";
    
    protected Date mDateFrom = null;
	protected Date mDateTo = null;
	
	public void setDateRange(Date dtFrom, Date dtTo){		
		if(DownloadPeriod.MIN_DATE.after(dtFrom)){
			this.mDateFrom = DownloadPeriod.MIN_DATE;
		} else {
			this.mDateFrom = dtFrom;
		}
		if(DownloadPeriod.MAX_DATE.before(dtTo)){
			this.mDateTo = DownloadPeriod.MAX_DATE;
		} else {
			this.mDateTo = dtTo;
		}		
	}

    public BaseTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb, Context context) {
        this.callback = cb;
        this.progressListener = pl;
        this.mainContext = context;
        init();
        this.networkError = mainContext.getString(R.string.network_error);
    }

    public BaseTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb) {
        this.callback = cb;
        this.progressListener = pl;
        init();
    }

    public TaskResult getTaskResult() {
        return mTaskResult;
    }

    public void setTaskResult(TaskResult result) {
        this.mTaskResult = result;
    }

    public String getTaskName() {
        return this.mTaskName;
    }

    /**
     * Переопределяемый метод для инициализации задачи. Вызывается из
     * конструктора после основной инициалиции.
     */
    protected abstract void init();

    /**
     * Получить контекст.
     * 
     * @return Контекст переданный в конструктор.
     */
    public Context getContext() {
        return mainContext;
    }
    
    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        publishProgress(this.mProgressDialogTitle, this.mProgressDialogMessage);
    }

    @Override
    protected void onProgressUpdate(String... messages) {
        if (progressListener != null)
            progressListener.onProgressUpdate(messages);
    }

    @Override
    protected void onPostExecute(TaskResult result) {
        this.mTaskResult = result;
        if (callback != null) {
            callback.onTaskComplete(this, result);
        }        
    }

    @Override
    protected void onCancelled() {
        TaskResult result = new TaskResult();
        result.setError(true);
        result.setStatus(TaskResult.Status.CANCEL_TASK);
        this.mTaskResult = result;
        if (callback != null){
            callback.onTaskComplete(this, result);
        }
    }
    
    public String getJSON(String url) throws IOException {        
        return HttpUtils.getJSON(url);        
    }

    public String uploadJSON(String url, String json, String filePostName) throws IOException {
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        File tempFile = null;
        tempFile = File.createTempFile("json", null);
        tempFile.deleteOnExit();
        BufferedWriter out = new BufferedWriter(new FileWriter(tempFile));
        out.write(json);
        out.close();
        FileBody fileBody = new FileBody(tempFile);
        multipartEntity.addPart(filePostName, fileBody);
        return postHTTPRequest(url, multipartEntity, null);
    }

    public String postData(String url, Dictionary<String, String> dictPostData, String cookiesValue) throws IOException {
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (Enumeration e = dictPostData.keys(); e.hasMoreElements();) {
            Object key = e.nextElement();
            String value = dictPostData.get(key);
            try {
                multipartEntity.addPart(key.toString(), new StringBody(value, Charset.forName(Settings.DEFAULT_ENCODING)));
            } catch (UnsupportedEncodingException exc) {
                exc.printStackTrace();
                return null;
            }
        }
        String responseString = postHTTPRequest(url, multipartEntity, cookiesValue);
        return responseString;
    }

    private String postHTTPRequest(String url, MultipartEntity multipartEntity, String cookiesValue) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();        
        HttpPost httpPost = new HttpPost(url);
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("http.protocol.handle-redirects", false);
        if(cookiesValue != null){
        	httpPost.setHeader("Cookie", cookiesValue);
        }
        httpPost.setParams(httpParams);
        httpPost.setEntity(multipartEntity);
        HttpResponse httpResponse = null;
        httpResponse = httpClient.execute(httpPost);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String result = null;
        if (statusCode == HttpStatus.SC_OK) {
            result = EntityUtils.toString(httpResponse.getEntity());
        } else {
            result = null;
        }
        return result;
    }
    
    protected UserData parseUserData(String resultJSON) throws JsonProcessingException, IOException{
    	ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(resultJSON);		
		
		SimpleDateFormat sdfDateTime = new SimpleDateFormat(BaseDTO.DATETIME_FORMAT);
		SimpleDateFormat sdfTime = new SimpleDateFormat(BaseDTO.TIME_FORMAT);
		SimpleDateFormat sdfDate = new SimpleDateFormat(BaseDTO.DATE_FORMAT);
		sdfDateTime.setTimeZone(UTC_TIMEZONE);
		sdfTime.setTimeZone(UTC_TIMEZONE);
		sdfDate.setTimeZone(UTC_TIMEZONE);
		UserData userData = new UserData();
		
		userData.UnixSyncDate = rootNode.get("udate").asLong();
				
		JsonNode customBodyFeelingTypeNode = rootNode.path("bodytype");
		Iterator<JsonNode> customBodyFeelingTypeElements = customBodyFeelingTypeNode.elements();
		ArrayList<CustomBodyFeelingType> customBodyFeelingTypeList = new ArrayList<CustomBodyFeelingType>();
		while(customBodyFeelingTypeElements.hasNext()){
			CustomBodyFeelingType c = new CustomBodyFeelingType();
		    JsonNode item = customBodyFeelingTypeElements.next();
		    c.setServerId(getLong(item, "id"));
		    c.setRowId(getString(item, "rid"));
		    c.setUserId(getLong(item, "uid"));
		    c.OperationTypeId = getInt(item, "op");
		    c.setName(getString(item, "name"));
		    customBodyFeelingTypeList.add(c);
		}
		userData.CustomBodyFeelingTypeList = customBodyFeelingTypeList;
		
		
		JsonNode customFactorTypeNode = rootNode.path("factortype");
		Iterator<JsonNode> customFactorTypeElements = customFactorTypeNode.elements();
		ArrayList<CustomFactorType> customFactorTypes = new ArrayList<CustomFactorType>();
		while(customFactorTypeElements.hasNext()){
			CustomFactorType c = new CustomFactorType();
		    JsonNode item = customFactorTypeElements.next();
		    c.setServerId(getLong(item, "id"));
		    c.setRowId(getString(item, "rid"));
		    c.setUserId(getLong(item, "uid"));
		    c.OperationTypeId = getInt(item, "op");
		    c.setName(getString(item, "name"));
		    c.setUnitDimensionId(getLong(item, "unitid"));
		    c.setStatus(getInt(item, "status"));
		    c.setOrdinalNumber(getInt(item, "num"));
		    c.setFactorGroupId(getLong(item, "fgid"));
		    customFactorTypes.add(c);
		}
		userData.CustomFactorTypeList = customFactorTypes;
		
		
		JsonNode customCommonFeelingTypeNode = rootNode.path("commontype");
		Iterator<JsonNode> customCommonFeelingTypeElements = customCommonFeelingTypeNode.elements();
		ArrayList<CustomCommonFeelingType> customCommonFeelingTypes = new ArrayList<CustomCommonFeelingType>();
		while(customCommonFeelingTypeElements.hasNext()){
			CustomCommonFeelingType c = new CustomCommonFeelingType();
		    JsonNode item = customCommonFeelingTypeElements.next();
		    c.setServerId(getLong(item, "id"));
		    c.setRowId(getString(item, "rid"));
		    c.setUserId(getLong(item, "uid"));
		    c.OperationTypeId = getInt(item, "op");
		    c.setName(getString(item, "name"));		    
		    c.setUnitDimensionId(getLong(item, "unitid"));
		    c.setStatus(getInt(item, "status"));
		    c.setOrdinalNumber(getInt(item, "num"));
		    c.setCommonFeelingGroupId(getLong(item, "fgid"));		    
		    customCommonFeelingTypes.add(c);
		}
		userData.CustomCommonFeelingTypeList = customCommonFeelingTypes;
		
		
		JsonNode bodyFeelingNode = rootNode.path("bf");
		Iterator<JsonNode> bodyFeelingElements = bodyFeelingNode.elements();
		ArrayList<BodyFeeling> bodyFeelingTypes = new ArrayList<BodyFeeling>();
		while(bodyFeelingElements.hasNext()){
			BodyFeeling c = new BodyFeeling();
		    JsonNode item = bodyFeelingElements.next();
		    c.setServerId(getLong(item, "id"));
		    c.setRowId(getString(item, "rid"));
		    c.setUserId(getLong(item, "uid"));
		    c.OperationTypeId = getInt(item, "op");
		    c.setX(getInt(item, "x"));
		    c.setY(getInt(item, "y"));
		    c.setBodyRegionId(getLong(item, "reg"));
		    c.setFeelingTypeId(getLong(item, "ftid"));
		    c.BodyFeelingTypeServerId = getLong(item, "ftid");
		    c.CustomBodyFeelingTypeServerId = getLong(item, "cftid");		    
		    c.setStartDate(getDate(item, "dt", sdfDateTime));
		    bodyFeelingTypes.add(c);
		}
		userData.BodyFeelingList = bodyFeelingTypes;
		
		
		
		JsonNode commonFeelingNode = rootNode.path("c");
		Iterator<JsonNode> commonFeelingElements = commonFeelingNode.elements();
		ArrayList<CommonFeeling> commonFeelingTypes = new ArrayList<CommonFeeling>();
		while(commonFeelingElements.hasNext()){
			CommonFeeling c = new CommonFeeling();
		    JsonNode item = commonFeelingElements.next();
		    c.setServerId(getLong(item, "id"));
		    c.setRowId(getString(item, "rid"));
		    c.setUserId(getLong(item, "uid"));
		    c.OperationTypeId = getInt(item, "op");
		    c.setStartDate(getDate(item, "dt", sdfDateTime));
		    c.setValue1(getDouble(item, "v1"));
		    c.setValue2(getDouble(item, "v2"));
		    c.setValue3(getDouble(item, "v3"));
		    c.setCommonFeelingTypeId(getLong(item, "ftid"));
		    c.CustomCommonFeelingTypeServerId = getLong(item, "cftid");		    
		    commonFeelingTypes.add(c);
		}
		userData.CommonFeelingList = commonFeelingTypes;
		
		
		JsonNode factorNode = rootNode.path("f");
		Iterator<JsonNode> factorElements = factorNode.elements();
		ArrayList<Factor> factorTypes = new ArrayList<Factor>();
		while(factorElements.hasNext()){
			Factor c = new Factor();
		    JsonNode item = factorElements.next();
		    c.setServerId(getLong(item, "id"));
		    c.setRowId(getString(item, "rid"));
		    c.setUserId(getLong(item, "uid"));
		    c.OperationTypeId = getInt(item, "op");
		    c.setStartDate(getDate(item, "dt", sdfDateTime));
		    c.setValue1(getDouble(item, "v1"));
		    c.setValue2(getDouble(item, "v2"));
		    c.setValue3(getDouble(item, "v3"));
		    c.setFactorTypeId(getLong(item, "ftid"));		    
		    c.CustomFactorTypeServerId = getLong(item, "cfid");
		    factorTypes.add(c);
		}
		userData.FactorList = factorTypes;
		
		
		JsonNode userBodyFeelingTypeNode = rootNode.path("ubft");
		Iterator<JsonNode> userBodyFeelingTypeElements = userBodyFeelingTypeNode.elements();
		ArrayList<UserBodyFeelingType> userBodyFeelingTypes = new ArrayList<UserBodyFeelingType>();
		while(userBodyFeelingTypeElements.hasNext()){
			UserBodyFeelingType c = new UserBodyFeelingType();
		    JsonNode item = userBodyFeelingTypeElements.next();
		    c.setServerId(getLong(item, "id"));
		    c.setRowId(getString(item, "rid"));
		    c.setUserId(getLong(item, "uid"));
		    c.OperationTypeId = getInt(item, "op");
		    c.setColor(getInt(item, "color"));
		    c.setBodyFeelingTypeId(getLong(item, "ftid"));
		    userBodyFeelingTypes.add(c);
		}
		userData.UserBodyFeelingTypeList = userBodyFeelingTypes;
		
		return userData;
    }

	public UserData parseTestDiaryData(String resultJSON) throws JsonProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(resultJSON);

		SimpleDateFormat sdfDateTime = new SimpleDateFormat(BaseDTO.DATETIME_FORMAT);
		SimpleDateFormat sdfTime = new SimpleDateFormat(BaseDTO.TIME_FORMAT);
		SimpleDateFormat sdfDate = new SimpleDateFormat(BaseDTO.DATE_FORMAT);
		sdfDateTime.setTimeZone(UTC_TIMEZONE);
		sdfTime.setTimeZone(UTC_TIMEZONE);
		sdfDate.setTimeZone(UTC_TIMEZONE);
		UserData userData = new UserData();


		Iterator<JsonNode> bodyFeelingElements = rootNode.elements();
		ArrayList<BodyFeeling> bodyFeelingTypes = new ArrayList<BodyFeeling>();
		while(bodyFeelingElements.hasNext()){
			BodyFeeling c = new BodyFeeling();
			JsonNode item = bodyFeelingElements.next();
			c.setServerId(null);
			c.setRowId(UUID.randomUUID().toString());
			c.setUserId(User.ANONIM_USER_ID);
			c.OperationTypeId = getInt(item, "op");
			c.setX(null);
			c.setY(null);
			c.setBodyRegionId(getLong(item, "reg"));
			c.setFeelingTypeId(getLong(item, "ftid"));
			c.BodyFeelingTypeServerId = getLong(item, "ftid");
			c.CustomBodyFeelingTypeServerId = null;
			c.setStartDate(getDate(item, "dt", sdfDateTime));
			bodyFeelingTypes.add(c);
		}
		userData.BodyFeelingList = bodyFeelingTypes;

		return userData;
	}

    
    public LoginInfo deserializeLoginInfoJSON(String loginInfoJSON) {
        ObjectMapper mapper = new ObjectMapper();
        LoginInfo loginInfo = null;
        try {
            loginInfo = mapper.readValue(loginInfoJSON, LoginInfo.class);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginInfo;
    }
    
    protected Integer getInt(JsonNode jsonNode, String property){
		try{
			Integer value = jsonNode.get(property).asInt();
			if(value == 0){
				String text = jsonNode.get(property).asText();
				try {  
			        value = Integer.parseInt(text);
			    } catch (NumberFormatException e) {  
			    	value = null; 
			    }
			}
			return value;
		} catch(Exception ex){
			return null;
		}		
	}
	
	protected Long getLong(JsonNode jsonNode, String property){
		try{
			Long value = jsonNode.get(property).asLong();
			if(value == 0){
				String text = jsonNode.get(property).asText();
				try {  
			        value = Long.parseLong(text);
			    } catch (NumberFormatException e) {  
			    	value = null; 
			    }				
			}
			return value;
		} catch(Exception ex){
			return null;
		}
	}
	
	protected Double getDouble(JsonNode jsonNode, String property){
		try{
			Double value = jsonNode.get(property).asDouble();
			if(value == 0){
				String text = jsonNode.get(property).asText();
				try {  
			        value = Double.parseDouble(text);
			    } catch (NumberFormatException e) {  
			    	value = null; 
			    }
			}
			return value;
		} catch(Exception ex){
			return null;
		}
	}
	
	protected String getString(JsonNode jsonNode, String property){
		try{
			String value = jsonNode.get(property).asText();
			if(value != null && value.equals("null")){
				value = null;
			}
			return value;
		} catch(Exception ex){
			return null;
		}
	}
	
	protected Date getDate(JsonNode jsonNode, String property, SimpleDateFormat sdf){
		try{
			return sdf.parse(jsonNode.get(property).textValue());
		} catch(Exception ex){
			return null;
		}
	}
        
    public static class UserData {

        @JsonProperty(value = "udate")
        public Long UnixSyncDate;
        
        @JsonProperty(value = "bodytype")
        public List<CustomBodyFeelingType> CustomBodyFeelingTypeList;
        
        @JsonProperty(value = "commontype")
        public List<CustomCommonFeelingType> CustomCommonFeelingTypeList;
        
        @JsonProperty(value = "factortype")
        public List<CustomFactorType> CustomFactorTypeList;

        @JsonProperty(value = "bf")
        public List<BodyFeeling> BodyFeelingList;
        
        @JsonProperty(value = "c")
        public List<CommonFeeling> CommonFeelingList;
        
        @JsonProperty(value = "f")
        public List<Factor> FactorList;
               
        @JsonProperty(value = "ubft")
        public List<UserBodyFeelingType> UserBodyFeelingTypeList;        
        
        public boolean IsExistDataForSending() {
            boolean result = false;
            if (this.CustomBodyFeelingTypeList != null && this.CustomBodyFeelingTypeList.size() > 0) {
                result = true;
            }
            if (this.CustomFactorTypeList != null && this.CustomFactorTypeList.size() > 0) {
                result = true;
            }
            if (this.CustomCommonFeelingTypeList != null && this.CustomCommonFeelingTypeList.size() > 0) {
                result = true;
            }
            if (this.BodyFeelingList != null && this.BodyFeelingList.size() > 0) {
                result = true;
            }
            if(this.UserBodyFeelingTypeList != null && this.UserBodyFeelingTypeList.size() > 0){
                result = true;
            }
            if (this.CommonFeelingList != null && this.CommonFeelingList.size() > 0) {
                result = true;
            }
            if (this.FactorList != null && this.FactorList.size() > 0) {
                result = true;
            }
            return result;
        }
    }

    public static class LoginInfo {

        @JsonProperty(value = "result")
        public String Result;

        @JsonProperty(value = "error")
        public List<String> Errors;
        
        @JsonProperty(value = "id")
        public int Id;

        @JsonProperty(value = "name")
        public String Login;

        @JsonProperty(value = "password")
        public String Password;

        @JsonProperty(value = "fname")
        public String FName;

        @JsonProperty(value = "lname")
        public String LName;

        @JsonProperty(value = "mname")
        public String MName;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BaseDTO.DATE_FORMAT, timezone = "GMT")
        @JsonProperty(value = "birthdate")
        public Date BirthDate;
        
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = BaseDTO.DATETIME_FORMAT, timezone = "GMT")
        @JsonProperty(value = "createdate")
        public Date CreateDate;

        @JsonProperty(value = "sex")
        public int Sex;
        
        @JsonProperty(value = "cityid")
        public long CityId;
        
        @JsonProperty(value = "marital_status_id")
        public Integer MaritalStatusId;
        
        @JsonProperty(value = "social_status_id")
        public Integer SocialStatusId;
        
        @JsonProperty(value = "height")
        public Integer Height;
        
        @JsonProperty(value = "weight")
        public Integer Weight;
        
        @JsonProperty(value = "pressure_id")
        public Integer PressureId;
        
        @JsonProperty(value = "foot_distance")
        public Integer FootDistance;
        
        @JsonProperty(value = "sleep_time")
        public Integer SleepTime;
        
        @JsonProperty(value = "question1")
        public Integer Question1;
        
        @JsonProperty(value = "question2")
        public Integer Question2;

        public boolean IsSuccess() {
            return (this.Result != null) ? this.Result.equalsIgnoreCase("ok") : false;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.Errors != null) {
                for (String err : this.Errors) {
                    sb.append(err);
                    sb.append(System.getProperty("line.separator"));
                }
            }
            return sb.toString();
        }
    }

}