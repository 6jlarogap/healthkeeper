package com.health.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Pair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.settings.Settings;
import com.health.util.HttpUtils;

public class GetRangeCountTask extends AsyncTask<String, String, List<Pair<Double, Integer>>> {

    private String mTaskName = null;
    private Date mDateFrom = null;
	private Date mDateTo = null;

	public GetRangeCountTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb, Context context) {
		this.mTaskName = Settings.TASK_GETSTAT;
	}
	
	public void setDateRange(Date dtFrom, Date dtTo){		
		this.mDateFrom = dtFrom;
		this.mDateTo = dtTo;
	}
	
	@Override
    protected List<Pair<Double, Integer>> doInBackground(String... params) {
    	long startTime = System.currentTimeMillis();
    	if(mDateFrom == null || mDateTo == null){
    		throw new RuntimeException("Period of downloaded data don't initialized");
    	}
    	List<Pair<Double, Integer>> result = new ArrayList<Pair<Double, Integer>>();
    	String resultJSON = null;
    	String url = params[0];
    	if(mDateTo.after(mDateFrom)){
    		String finalUrl = String.format("%s&datefrom=%d&dateto=%d", url, mDateFrom.getTime()/1000L, mDateTo.getTime()/1000L);
			try {
				resultJSON = HttpUtils.getJSON(finalUrl);
            }
            catch (Exception e) {
            }
			if(resultJSON != null){
                try{
                	result = getResult(resultJSON);
                } catch (Exception e) {
                }
            }
    	}
    	
	    return result;
    }
	
	@Override
    protected void onPostExecute(List<Pair<Double, Integer>> result) {
		super.onPostExecute(result);
    }
	
	private List<Pair<Double, Integer>> getResult(String resultJSON) throws JsonProcessingException, IOException{
		List<Pair<Double, Integer>> result = new ArrayList<Pair<Double, Integer>>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(resultJSON);
		Iterator<JsonNode> elements = rootNode.elements();
		while(elements.hasNext()){
		    JsonNode item = elements.next();
		    result.add(new Pair<Double, Integer>(getDouble(item,"val"), getInt(item,"cnt")));
		}
		
		return result;
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

}
