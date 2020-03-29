package com.health.task;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.data.BaseDTO;
import com.health.data.Complaint;
import com.health.data.ComplaintDao;
import com.health.data.DaoSession;
import com.health.data.DownloadPeriod;
import com.health.data.GeoPhysics;
import com.health.data.HelioPhysics;
import com.health.data.Moon;
import com.health.data.MoonPhase;
import com.health.data.Particle;
import com.health.data.Planet;
import com.health.data.SolarEclipse;
import com.health.data.Sun;
import com.health.data.Weather;
import com.health.data.WeatherDaily;
import com.health.db.DB;
import com.health.db.DownloadPeriodDB;
import com.health.repository.DBRepository;
import com.health.settings.Settings;

public class GetDataTask extends BaseTask {

    public GetDataTask(AsyncTaskProgressListener pl, AsyncTaskCompleteListener<TaskResult> cb, Context context) {
		super(pl, cb, context);
		this.mTaskName = Settings.TASK_GETDATA;
	}

    @Override
    public void init() {
        this.mProgressDialogTitle = "Загрузка данных...";
        this.mProgressDialogMessage = "Подождите";
    }

    @Override
    protected TaskResult doInBackground(String... params) {
    	long startTime = System.currentTimeMillis();
    	if(mDateFrom == null || mDateTo == null){
    		throw new RuntimeException("Period of downloaded data don't initialized");
    	}
    	TaskResult result = new TaskResult();
    	result.setTaskName(this.mTaskName);    	
    	String resultJSON = null;
    	String url = params[0];
    	if(mDateTo.after(mDateFrom)){
    		List<DownloadPeriod> periods = DownloadPeriodDB.getDownloadPeriods(mDateFrom, mDateTo);
    		Date nowDate = new Date();
    		for(DownloadPeriod period : periods){
    			if(period.getSyncDate() != null){
    				long deltaTime = nowDate.getTime() - period.getSyncDate().getTime();
        			if(Math.abs(deltaTime) < 30 * 60 * 1000L){
        				//пропускаем загрузка т.к. недавно мы загружали эти данные
        				continue;
        			}
    			}
    			String finalUrl = String.format("%s&datefrom=%d&dateto=%d&timezone=Etc/GMT%d", url, period.getDateFrom().getTime()/1000L, period.getDateTo().getTime()/1000L, period.getDateFrom().getTimezoneOffset()/60);
    			try {
    				long startTime1 = System.currentTimeMillis();
    				Log.i(getClass().getName(), String.format("Start download data from %s", finalUrl));
                	resultJSON = getJSON(finalUrl);
                	Log.i(getClass().getName(), String.format("End download data from %s", finalUrl));
                	long endTime1 = System.currentTimeMillis();
                	Log.i("GetDataTask_http_query_time_ms", Long.toString(endTime1-startTime1));
                }
                catch (Exception e) {      
                    result.setError(true);
                    result.setStatus(TaskResult.Status.SERVER_UNAVALAIBLE);
                    break;
                }        
                if(resultJSON != null){
                    try{
                    	long startTime2 = System.currentTimeMillis();
                    	handleResponseGetDataJSON(resultJSON, period.getDateFrom(), period.getDateTo());
                    	long endTime2 = System.currentTimeMillis();
                    	Log.i("GetDataTask_JsonParseAndSave", Long.toString(endTime2-startTime2));
                    }catch (Exception e) {
                    	Log.e(getClass().getName(), e.getMessage(), e);
                        result.setError(true);
                        result.setStatus(TaskResult.Status.HANDLE_ERROR);
                        break;
                    }
                }
                period.setSyncDate(nowDate);
                DownloadPeriodDB.saveDownloadPeriod(period);
                if(isCancelled()){
                	break;
                }
    		}            
    	}
    	long endTime = System.currentTimeMillis();
    	Log.i("GetDataTask_time_ms", Long.toString(endTime-startTime));
        return result;
    }
    
    
	public void handleResponseGetDataJSON(final String resultJSON, Date dtFrom, Date dtTo) throws JsonProcessingException, IOException, ParseException {
		DaoSession daoSession = DB.db().newSession();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(resultJSON);
		JsonNode weatherNode = rootNode.path("w");

		SimpleDateFormat sdfDateTime = new SimpleDateFormat(BaseDTO.DATETIME_FORMAT);
		SimpleDateFormat sdfTime = new SimpleDateFormat(BaseDTO.TIME_FORMAT);
		SimpleDateFormat sdfDate = new SimpleDateFormat(BaseDTO.DATE_FORMAT);

		sdfDateTime.setTimeZone(UTC_TIMEZONE);
		sdfTime.setTimeZone(UTC_TIMEZONE);
		sdfDate.setTimeZone(UTC_TIMEZONE);				
		
		Iterator<JsonNode> weatherElements = weatherNode.elements();
		ArrayList<Weather> weatherList = new ArrayList<Weather>();
		while(weatherElements.hasNext()){
			Weather w = new Weather();
		    JsonNode item = weatherElements.next();
		    w.setServerId(getLong(item, "id"));
		    w.setInfoDate(getDate(item, "dt", sdfDateTime));
		    w.setTemperature(getDouble(item, "t")); 
		    w.setMinTemperature(getDouble(item, "tmin"));
		    w.setMaxTemperature(getDouble(item, "tmax"));
		    w.setPressure(getDouble(item, "p"));
		    w.setHumidity(getDouble(item, "h"));
		    w.setWindSpeed(getDouble(item, "ws"));
		    w.setWindDeg(getDouble(item, "wd"));
		    w.setClouds(getDouble(item, "c"));
		    w.setRain(getDouble(item, "r"));
		    w.setSnow(getDouble(item, "sn"));
		    w.setId(w.getServerId());
		    weatherList.add(w);
		}
		daoSession.getWeatherDao().insertOrReplaceInTx(weatherList);

		JsonNode weatherDailyNode = rootNode.path("wd");
		Iterator<JsonNode> weatherDailyElements = weatherDailyNode.elements();
		ArrayList<WeatherDaily> weatherDailyList = new ArrayList<WeatherDaily>();
		while(weatherDailyElements.hasNext()){
			WeatherDaily wd = new WeatherDaily();
		    JsonNode item = weatherDailyElements.next();
		    wd.setServerId(getLong(item, "id"));
		    wd.setInfoDate(getDate(item, "dt", sdfDateTime));
		    wd.setMinTemperature(getDouble(item, "tmin"));
		    wd.setMaxTemperature(getDouble(item, "tmax"));
		    wd.setPressure(getDouble(item, "p"));
		    wd.setHumidity(getDouble(item, "h"));
		    wd.setWindSpeed(getDouble(item, "ws"));
		    wd.setWindDeg(getDouble(item, "wd"));
		    wd.setClouds(getDouble(item, "c"));
		    wd.setRain(getDouble(item, "r"));
		    wd.setSnow(getDouble(item, "sn"));
		    wd.setId(wd.getServerId());
		    weatherDailyList.add(wd);
		}
		daoSession.getWeatherDailyDao().insertOrReplaceInTx(weatherDailyList);
		
		JsonNode sunNode = rootNode.path("s");
		Iterator<JsonNode> sunElements = sunNode.elements();
		ArrayList<Sun> sunList = new ArrayList<Sun>();
		while(sunElements.hasNext()){
			Sun s = new Sun();
		    JsonNode item = sunElements.next();
		    s.setServerId(getLong(item, "id"));
		    s.setInfoDate(getDate(item, "dt", sdfDateTime));
		    s.setSunRise(getDate(item, "r", sdfDateTime));
		    s.setSunSet(getDate(item, "s", sdfDateTime));
		    s.setDayLength(getString(item, "l"));
		    s.setId(s.getServerId());
		    sunList.add(s);
		}
		daoSession.getSunDao().insertOrReplaceInTx(sunList);

		JsonNode solarEclipseNode = rootNode.path("se");		
		Iterator<JsonNode> solarEclipseElements = solarEclipseNode.elements();
		ArrayList<SolarEclipse> solarEclipseList = new ArrayList<SolarEclipse>();
		while(solarEclipseElements.hasNext()){
			SolarEclipse s = new SolarEclipse();
		    JsonNode item = solarEclipseElements.next();		    
		    s.setInfoDate(getDate(item, "max", sdfDateTime)); 
		    s.setServerId(s.getInfoDate().getTime());
		    s.setPhase(getInt(item, "p"));
		    s.setMagnitude(getDouble(item, "m"));
		    s.setContact1(getDate(item, "c1", sdfDateTime));
		    s.setContact2(getDate(item, "c2", sdfDateTime));
		    s.setContact3(getDate(item, "c3", sdfDateTime));
		    s.setContact4(getDate(item, "c4", sdfDateTime));
		    s.setId(s.getServerId());
		    solarEclipseList.add(s);
		}
		daoSession.getSolarEclipseDao().insertOrReplaceInTx(solarEclipseList);

		JsonNode moonNode = rootNode.path("m");
		Iterator<JsonNode> moonElements = moonNode.elements();
		ArrayList<Moon> moonList = new ArrayList<Moon>();
		while(moonElements.hasNext()){
			Moon m = new Moon();
		    JsonNode item = moonElements.next();
		    m.setServerId(getLong(item, "id"));
		    m.setInfoDate(getDate(item, "dt", sdfDateTime));
		    m.setMoonVisibility(getDouble(item, "v"));
		    m.setMoonOld(getDate(item, "o", sdfDateTime));
		    m.setMoonRise(getDate(item, "r", sdfDateTime));
		    m.setMoonSet(getDate(item, "s", sdfDateTime));
		    m.setMoonPhaseId(getInt(item, "pid"));		    
		    m.setId(m.getServerId());
		    moonList.add(m);
		}
		daoSession.getMoonDao().insertOrReplaceInTx(moonList);

		JsonNode moonPhaseNode = rootNode.path("mp");
		Iterator<JsonNode> moonPhaseElements = moonPhaseNode.elements();
		ArrayList<MoonPhase> moonPhaseList = new ArrayList<MoonPhase>();
		while(moonPhaseElements.hasNext()){
			MoonPhase m = new MoonPhase();
		    JsonNode item = moonPhaseElements.next();
		    m.setServerId(getLong(item, "id"));
		    m.setInfoDate(getDate(item, "dt", sdfDateTime));
		    m.setMoonPhase(getString(item, "p")); 
		    m.setMoonPhaseId(getLong(item, "pid"));
		    m.setId(m.getServerId());
		    moonPhaseList.add(m);
		}
		daoSession.getMoonPhaseDao().insertOrReplaceInTx(moonPhaseList);

		JsonNode geoPhysicsNode = rootNode.path("gh");
		Iterator<JsonNode> geoPhysicsElements = geoPhysicsNode.elements();
		ArrayList<GeoPhysics> geoPhysicsList = new ArrayList<GeoPhysics>();
		while(geoPhysicsElements.hasNext()){
			GeoPhysics g = new GeoPhysics();
		    JsonNode item = geoPhysicsElements.next();
		    g.setServerId(getLong(item, "id"));
		    g.setInfoDate(getDate(item, "dt", sdfDateTime));
		    g.setKpId(getInt(item, "kpid"));
		    g.setAp(getInt(item, "ap"));		    
		    g.setId(g.getServerId());
		    geoPhysicsList.add(g);
		}
		daoSession.getGeoPhysicsDao().insertOrReplaceInTx(geoPhysicsList);

		geoPhysicsNode = rootNode.path("gd");
		geoPhysicsElements = geoPhysicsNode.elements();
		geoPhysicsList = new ArrayList<GeoPhysics>();
		while(geoPhysicsElements.hasNext()){
			GeoPhysics g = new GeoPhysics();
		    JsonNode item = geoPhysicsElements.next();
		    g.setServerId(getLong(item, "id"));
		    g.setInfoDate(getDate(item, "dt", sdfDateTime));
		    g.setKpId(getInt(item, "kpid"));
		    g.setAp(getInt(item, "ap"));		    
		    g.setId(g.getServerId());
		    geoPhysicsList.add(g);
		}
		daoSession.getGeoPhysicsDao().insertOrReplaceInTx(geoPhysicsList);

		JsonNode helioPhysicsNode = rootNode.path("hd");
		Iterator<JsonNode> helioPhysicsElements = helioPhysicsNode.elements();
		ArrayList<HelioPhysics> helioPhysicsList = new ArrayList<HelioPhysics>();
		while(helioPhysicsElements.hasNext()){
			HelioPhysics h = new HelioPhysics();
		    JsonNode item = helioPhysicsElements.next();
		    h.setServerId(getLong(item, "id"));
		    h.setInfoDate(getDate(item, "dt", sdfDateTime));
		    h.setF10_7(getInt(item, "f10_7"));
		    h.setSunspotNumber(getInt(item, "sn"));
		    h.setSunspotArea(getInt(item, "sa"));
		    h.setNewRegions(getInt(item, "nr"));
		    h.setXbkgd(getString(item, "x"));
		    h.setFlares1(getInt(item, "f1"));
		    h.setFlares2(getInt(item, "f2"));
		    h.setFlares3(getInt(item, "f3"));
		    h.setFlaresC(getInt(item, "fc"));
		    h.setFlaresM(getInt(item, "fm"));
		    h.setFlaresS(getInt(item, "fs"));
		    h.setFlaresX(getInt(item, "fx"));		    
		    h.setId(h.getServerId());
		    helioPhysicsList.add(h);
		}
		daoSession.getHelioPhysicsDao().insertOrReplaceInTx(helioPhysicsList);

		JsonNode particleNode = rootNode.path("pd");
		Iterator<JsonNode> particleElements = particleNode.elements();
		ArrayList<Particle> particleList = new ArrayList<Particle>();
		while(particleElements.hasNext()){
			Particle p = new Particle();
		    JsonNode item = particleElements.next();
		    p.setServerId(getLong(item, "id"));
		    p.setInfoDate(getDate(item, "dt", sdfDateTime));
		    p.setProton1MeV(getLong(item, "p1"));
		    p.setProton10MeV(getLong(item, "p2"));
		    p.setProton100MeV(getLong(item, "p3"));
		    p.setElectron08MeV(getLong(item, "e8"));
		    p.setElectron2MeV(getLong(item, "e2"));
		    p.setId(p.getServerId());
		    particleList.add(p);
		}
		daoSession.getParticleDao().insertOrReplaceInTx(particleList);

		JsonNode planetNode = rootNode.path("p");
		Iterator<JsonNode> planetElements = planetNode.elements();
		ArrayList<Planet> planetList = new ArrayList<Planet>();
		while(planetElements.hasNext()){
			Planet p = new Planet();
		    JsonNode item = planetElements.next();
		    p.setServerId(getLong(item, "id"));
		    p.setInfoDate(getDate(item, "dt", sdfDateTime));
		    p.setMercuryRise(getDate(item, "me1", sdfDateTime));
		    p.setMercurySet(getDate(item, "me2", sdfDateTime));
		    p.setVenusRise(getDate(item, "v1", sdfDateTime));
		    p.setVenusSet(getDate(item, "v2", sdfDateTime));
		    p.setMarsRise(getDate(item, "m1", sdfDateTime));
		    p.setMarsSet(getDate(item, "m2", sdfDateTime));
		    p.setJupiterRise(getDate(item, "j1", sdfDateTime));
		    p.setJupiterSet(getDate(item, "j2", sdfDateTime));
		    p.setSaturnRise(getDate(item, "s1", sdfDateTime));
		    p.setSaturnSet(getDate(item, "s2", sdfDateTime));
		    p.setUranusRise(getDate(item, "u1", sdfDateTime));
		    p.setUranusSet(getDate(item, "u2", sdfDateTime));
		    p.setNeptuneRise(getDate(item, "n1", sdfDateTime));
		    p.setNeptuneSet(getDate(item, "n2", sdfDateTime));
		    p.setPlutoRise(getDate(item, "p1", sdfDateTime));
		    p.setPlutoSet(getDate(item, "p2", sdfDateTime));		    
		    p.setId(p.getServerId());
		    planetList.add(p);
		}
		daoSession.getPlanetDao().insertOrReplaceInTx(planetList);
		
		Calendar cal = Calendar.getInstance();
		JsonNode complaintNode = rootNode.path("complaint");
		Iterator<JsonNode> complaintElements = complaintNode.elements();
		ArrayList<Complaint> complaintList = new ArrayList<Complaint>();
		while(complaintElements.hasNext()){
			Complaint complaint = new Complaint();
		    JsonNode item = complaintElements.next();
		    complaint.setServerId(getLong(item, "id"));
		    complaint.setBodyComplaintTypeId(getLong(item, "cid"));
		    complaint.setCommonFeelingTypeId(getLong(item, "fid"));
		    complaint.setStartDate(getDate(item, "dt", sdfDate));
		    complaint.setCount(getInt(item, "cnt"));		    
		    complaint.setId(complaint.getServerId());		    
            cal.setTime(complaint.getStartDate());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            complaint.setStartDate(cal.getTime());
		    complaintList.add(complaint);
		}
		daoSession.getComplaintDao().queryBuilder().where(ComplaintDao.Properties.StartDate.ge(dtFrom), ComplaintDao.Properties.StartDate.lt(dtTo)).buildDelete().executeDeleteWithoutDetachingEntities();
        daoSession.getComplaintDao().insertOrReplaceInTx(complaintList);

	}
	
	
    
    
    
}