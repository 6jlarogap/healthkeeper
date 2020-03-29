package com.health.view;

import java.util.Date;
import java.util.List;

import android.util.Log;

import com.health.data.Factor;
import com.health.data.FactorGroup;
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
import com.health.repository.IRepository;
import com.health.repository.IRepository.DATA_AMOUNT_TYPE;

public class FactorDataKeeper {

	private IRepository mRepository;

	public List<Weather> listWeatherHourly;
	public List<WeatherDaily> listWeatherDaily;
	public List<Sun> listSun;
	public List<SolarEclipse> listSolarEclipse;
	public List<Moon> listMoon;
	public List<MoonPhase> listMoonPhase;
	public List<GeoPhysics> listGeoPhysics;
	public List<HelioPhysics> listHelioPhysics;
	public List<Particle> listParticle;
	public List<Planet> listPlanet;
	public List<Factor> listFactor;
	public List<FactorGroup> listFactorGroup;

	public Weather currentWeather;
	public Sun currentSun;
	public Moon currentMoon;
	public GeoPhysics currentGeoPhysics;
	public HelioPhysics currentHelioPhysics;
	public Particle currentParticle;
	
	public FactorDataKeeper(IRepository r){
		mRepository = r;
	}

	public void updateListWeather(Date mDbDateFrom, Date mDbDateTo, DATA_AMOUNT_TYPE mDataAmountType) {
		listWeatherHourly = mRepository.getWeatherList(mDbDateFrom, mDbDateTo, mDataAmountType);
		listWeatherDaily = mRepository.getWeatherDailyList(mDbDateFrom, mDbDateTo, mDataAmountType);
	}

	public void updateListSun(Date mDbDateFrom, Date mDbDateTo, DATA_AMOUNT_TYPE mDataAmountType) {
		listSun = mRepository.getSunList(mDbDateFrom, mDbDateTo, mDataAmountType);
	}

	public void updateListSolarEclipse(Date mDbDateFrom, Date mDbDateTo) {		
		listSolarEclipse = mRepository.getSolarEclipseList(mDbDateFrom, mDbDateTo, IRepository.DATA_AMOUNT_TYPE.ALL);
	}

	public void updateListMoon(Date mDbDateFrom, Date mDbDateTo, DATA_AMOUNT_TYPE mDataAmountType) {
		listMoon = mRepository.getMoonList(mDbDateFrom, mDbDateTo, mDataAmountType);
	}

	public void updateListMoonPhase(Date mDbDateFrom, Date mDbDateTo) {
		listMoonPhase = mRepository.getMoonPhaseList(mDbDateFrom, mDbDateTo, IRepository.DATA_AMOUNT_TYPE.ALL);
	}

	public void updateListGeoPhysics(Date mDbDateFrom, Date mDbDateTo) {
		listGeoPhysics = mRepository.getGeoPhysicsList(mDbDateFrom, mDbDateTo, IRepository.DATA_AMOUNT_TYPE.ALL);
	}

	public void updateListHelioPhysics(Date mDbDateFrom, Date mDbDateTo) {
		listHelioPhysics = mRepository.getHelioPhysicsList(mDbDateFrom, mDbDateTo, IRepository.DATA_AMOUNT_TYPE.ALL);
	}

	public void updateListParticle(Date mDbDateFrom, Date mDbDateTo) {
		listParticle = mRepository.getParticleList(mDbDateFrom, mDbDateTo, IRepository.DATA_AMOUNT_TYPE.ALL);
	}

	public void updateListPlanet(Date mDbDateFrom, Date mDbDateTo, DATA_AMOUNT_TYPE mDataAmountType) {
		listPlanet = mRepository.getPlanetList(mDbDateFrom, mDbDateTo, mDataAmountType);
	}

	public void updateListFactor(Date mDbDateFrom, Date mDbDateTo) {
		listFactor = mRepository.getFactorList(mDbDateFrom, mDbDateTo);
		listFactorGroup = mRepository.getFactorGroups();
	}

	public void updateCurrentData(Date mMarkedDate){
		currentWeather = mRepository.getCurrentWeatherOnDate(mMarkedDate);
		currentSun = mRepository.getCurrentSunOnDate(mMarkedDate);
		currentMoon = mRepository.getCurrentMoonOnDate(mMarkedDate);
		currentGeoPhysics = mRepository.getCurrentGeoPhysicsOnDate(mMarkedDate);
		currentHelioPhysics = mRepository.getCurrentHelioPhysicsOnDate(mMarkedDate);
		currentParticle = mRepository.getCurrentParticleOnDate(mMarkedDate);
	}
}
