package com.health.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import com.health.data.FactorGroup;
import com.health.data.GeoPhysicsType;
import com.health.data.HelioPhysicsType;
import com.health.data.MoonType;
import com.health.data.ParameterType;
import com.health.data.ParticleType;
import com.health.data.PlanetType;
import com.health.data.SunType;
import com.health.data.WeatherType;
import com.health.main.R;
import com.health.plot.ApPlot;
import com.health.plot.CloudPlot;
import com.health.plot.Electron08MeVPlot;
import com.health.plot.Electron2MeVPlot;
import com.health.plot.F10_7Plot;
import com.health.plot.HumidityPlot;
import com.health.plot.IDataPlot;
import com.health.plot.JupiterPlot;
import com.health.plot.KpPlot;
import com.health.plot.MarsPlot;
import com.health.plot.MercuryPlot;
import com.health.plot.MoonOldPlot;
import com.health.plot.MoonPlot;
import com.health.plot.MoonVisibilityPlot;
import com.health.plot.NeptunePlot;
import com.health.plot.NewRegionsPlot;
import com.health.plot.OpticalFlares1Plot;
import com.health.plot.OpticalFlares2Plot;
import com.health.plot.OpticalFlares3Plot;
import com.health.plot.OpticalFlaresSPlot;
import com.health.plot.PlutoPlot;
import com.health.plot.PrecipitationPlot;
import com.health.plot.PressurePlot;
import com.health.plot.Proton100MeVPlot;
import com.health.plot.Proton10MeVPlot;
import com.health.plot.Proton1MeVPlot;
import com.health.plot.SaturnPlot;
import com.health.plot.SunPlot;
import com.health.plot.SunspotAreaPlot;
import com.health.plot.SunspotNumberPlot;
import com.health.plot.TemperaturePlot;
import com.health.plot.UranusPlot;
import com.health.plot.UserFactorPlot;
import com.health.plot.VenusPlot;
import com.health.plot.WindPlot;
import com.health.plot.XRayFlaresCPlot;
import com.health.plot.XRayFlaresMPlot;
import com.health.plot.XRayFlaresXPlot;
import com.health.plot.XbkgdPlot;
import com.health.repository.IRepository;
import com.health.settings.Settings;
import com.health.view.FactorPlotView.PLOT_TYPE;

public class FactorPlotsKeeper {
	private Activity activity;
	private IRepository mRepository;
	private FactorDataKeeper fdk;
	private Date mPlotDateFrom;
	private Date mPlotDateTo;
	private Date mMarkedDate;
	
	public PressurePlot pressurePlot;
	public CloudPlot cloudPlot;
	public HumidityPlot humidityPlot;
	public PrecipitationPlot precipitationPlot;
	public TemperaturePlot temperaturePlot;
	public WindPlot windPlot;
	public SunPlot sunPlot;
	public MoonVisibilityPlot moonVisibilityPlot;
	public MoonOldPlot moonOldPlot;
	public MoonPlot moonPlot;
	public KpPlot kpPlot;
	public ApPlot apPlot;
	public F10_7Plot f10_7Plot;
	public SunspotNumberPlot sunspotNumberPlot;
	public SunspotAreaPlot sunspotAreaPlot;
	public NewRegionsPlot newRegionsPlot;
	public XRayFlaresCPlot xRayFlaresCPlot;
	public XRayFlaresMPlot xRayFlaresMPlot;
	public XRayFlaresXPlot xRayFlaresXPlot;
	public OpticalFlares1Plot opticalFlares1Plot;
	public OpticalFlares2Plot opticalFlares2Plot;
	public OpticalFlares3Plot opticalFlares3Plot;
	public OpticalFlaresSPlot opticalFlaresSPlot;
	public XbkgdPlot xbkgdPlot;
	public Proton1MeVPlot proton1MeVPlot;
	public Proton10MeVPlot proton10MeVPlot;
	public Proton100MeVPlot proton100MeVPlot;
	public Electron08MeVPlot electron08MeVPlot;
	public Electron2MeVPlot electron2MeVPlot;
	public MercuryPlot mercuryPlot;
	public VenusPlot venusPlot;
	public MarsPlot marsPlot;
	public JupiterPlot jupiterPlot;
	public SaturnPlot saturnPlot;
	public UranusPlot uranusPlot;
	public NeptunePlot neptunePlot;
	public PlutoPlot plutoPlot;

	public List<IDataPlot> userPlots = new ArrayList<IDataPlot>();
	public List<IDataPlot> weatherPlots = new ArrayList<IDataPlot>();
	public List<IDataPlot> astronomyPlots = new ArrayList<IDataPlot>();
	public List<IDataPlot> geoHelioPhysicsPlots = new ArrayList<IDataPlot>();
	public List<IDataPlot> factorPlots = new ArrayList<IDataPlot>();
	public List<IDataPlot> juxtaposePlots = new ArrayList<IDataPlot>();
	
	public HashMap<IDataPlot, List<IDataPlot>> jp = new HashMap<IDataPlot, List<IDataPlot>>();
	
	public List<ParameterType> paramsUser;
	public List<ParameterType> paramsWeather;
	public List<ParameterType> paramsSun;
	public List<ParameterType> paramsMoon;
	public List<ParameterType> paramsGeoPhysics;
	public List<ParameterType> paramsHelioPhysics;
	public List<ParameterType> paramsParticle;
	public List<ParameterType> paramsPlanet;
	
	public FactorPlotsKeeper(Activity a, IRepository repository, FactorDataKeeper f, Date plotDateFrom, Date plotDateTo, Date markedDate){
		activity = a;
		mRepository = repository;
		fdk = f;
		mPlotDateFrom = plotDateFrom;
		mPlotDateTo = plotDateTo;
		mMarkedDate = markedDate;
	}
	
	public void updateParamsWeather() {
		paramsWeather = WeatherType.getOnlyVisibleWeatherTypeList(activity);
	}

	public void updateParamsSun() {
		paramsSun = SunType.getOnlyVisibleSunTypeList(activity);
	}

	public void updateParamsMoon() {
		paramsMoon = MoonType.getOnlyVisibleMoonTypeList(activity);
	}

	public void updateParamsGeoPhysics() {
		paramsGeoPhysics = GeoPhysicsType.getOnlyVisibleGeoPhysicsTypeList(activity);
	}

	public void updateParamsHelioPhysics() {
		paramsHelioPhysics = HelioPhysicsType.getOnlyVisibleHelioPhysicsTypeList(activity);
	}

	public void updateParamsParticle() {
		paramsParticle = ParticleType.getOnlyVisibleParticleTypeList(activity);
	}

	public void updateParamsPlanet() {
		paramsPlanet = PlanetType.getOnlyVisiblePlanetTypeList(activity);
	}

	public void updateParamsUser() {
		paramsUser = getOnlyVisibleUserTypeList(activity);
	}
	

	public static ArrayList<ParameterType> getOnlyVisibleUserTypeList(Context context) {
		ArrayList<ParameterType> result = new ArrayList<ParameterType>();
		ArrayList<ParameterType> types = new ArrayList<ParameterType>();

		types.addAll(WeatherType.getWeatherTypeList(context));
		types.addAll(SunType.getSunTypeList(context));
		types.addAll(MoonType.getMoonTypeList(context));
		types.addAll(PlanetType.getPlanetTypeList(context));
		types.addAll(GeoPhysicsType.getGeoPhysicsTypeList(context));
		types.addAll(HelioPhysicsType.getHelioPhysicsTypeList(context));
		types.addAll(ParticleType.getParticleTypeList(context));

		List<ParameterType> params = Settings.getGraphsVisibility(context, ParameterType.GROUP_USER_TYPE, types);
		for (ParameterType p : params) {
			if (p.isVisible()) {
				result.add(p);
			}
		}
		return result;
	}
	
	public void updateJuxtaposePlots(){
		for(IDataPlot dp : juxtaposePlots){
			if(dp.getClass() == PressurePlot.class ||
					dp.getClass() == CloudPlot.class ||
					dp.getClass() == HumidityPlot.class ||
					dp.getClass() == PrecipitationPlot.class ||
					dp.getClass() == TemperaturePlot.class ||
					dp.getClass() == WindPlot.class){
				dp.updateData(fdk.listWeatherHourly, fdk.listWeatherDaily);
			} else if(dp.getClass() == SunPlot.class){
				dp.updateData(fdk.listSun, fdk.listSolarEclipse);
			} else if(dp.getClass() == MoonVisibilityPlot.class ||
					dp.getClass() == MoonOldPlot.class ||
					dp.getClass() == MoonPlot.class){
				dp.updateData(fdk.listMoon, fdk.listMoonPhase);
			} else if(dp.getClass() == MercuryPlot.class ||
					dp.getClass() == VenusPlot.class ||
					dp.getClass() == MarsPlot.class ||
					dp.getClass() == JupiterPlot.class ||
					dp.getClass() == SaturnPlot.class ||
					dp.getClass() == UranusPlot.class ||
					dp.getClass() == NeptunePlot.class ||
					dp.getClass() == PlutoPlot.class){
				dp.updateData(fdk.listPlanet);
			} else if(dp.getClass() == KpPlot.class ||
					dp.getClass() == ApPlot.class){
				dp.updateData(fdk.listGeoPhysics);
			} else if(dp.getClass() == F10_7Plot.class ||
					dp.getClass() == SunspotNumberPlot.class ||
					dp.getClass() == SunspotAreaPlot.class ||
					dp.getClass() == NewRegionsPlot.class ||
					dp.getClass() == XRayFlaresCPlot.class ||
					dp.getClass() == XRayFlaresMPlot.class ||
					dp.getClass() == XRayFlaresXPlot.class ||
					dp.getClass() == OpticalFlares1Plot.class ||
					dp.getClass() == OpticalFlares2Plot.class ||
					dp.getClass() == OpticalFlares3Plot.class ||
					dp.getClass() == OpticalFlaresSPlot.class ||
					dp.getClass() == XbkgdPlot.class
					){
				dp.updateData(fdk.listHelioPhysics);
			} else if(dp.getClass() == Proton1MeVPlot.class ||
					dp.getClass() == Proton10MeVPlot.class ||
					dp.getClass() == Proton100MeVPlot.class ||
					dp.getClass() == Electron08MeVPlot.class ||
					dp.getClass() == Electron2MeVPlot.class){
				dp.updateData(fdk.listParticle);
			}
		}
	}
	
	public void updatePlots(PLOT_TYPE plotsType){
		List<IDataPlot> plots = null;
		List<ParameterType> params = null;
		switch(plotsType){
		case WEATHER: plots = weatherPlots; 
				updateParamsWeather();
				break;
		case ASTRONOMY: plots = astronomyPlots; 
        		updateParamsSun();
        		updateParamsMoon();
        		updateParamsPlanet();
        		break;
		case GEOHELIOPHYSICS: plots = geoHelioPhysicsPlots; 
        		updateParamsGeoPhysics();
        		updateParamsHelioPhysics();
        		updateParamsParticle();
        		break;
		case USER: plots = userPlots; 
				updateParamsUser();
				break;
		}
		
		plots.clear();
		
		if(plotsType == PLOT_TYPE.WEATHER || plotsType == PLOT_TYPE.USER){
			switch(plotsType){
			case WEATHER: params = paramsWeather; break;
			case USER: params = paramsUser; break;
			}
    		for (ParameterType pt : params) {
    			switch (pt.Id) {
    			case WeatherType.PRESSURE_TYPE:
    				if (pressurePlot == null) {
    					pressurePlot = new PressurePlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listWeatherHourly, fdk.listWeatherDaily);
    				} else {
    					pressurePlot.updateData(fdk.listWeatherHourly, fdk.listWeatherDaily);
    				}
    				plots.add(pressurePlot);
    				break;
    			case WeatherType.CLOUD_TYPE:
    				if (cloudPlot == null) {
    					cloudPlot = new CloudPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listWeatherHourly, fdk.listWeatherDaily);
    				} else {
    					cloudPlot.updateData(fdk.listWeatherHourly, fdk.listWeatherDaily);
    				}
    				plots.add(cloudPlot);
    				break;
    			case WeatherType.HUMIDITY_TYPE:
    				if (humidityPlot == null) {
    					humidityPlot = new HumidityPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listWeatherHourly, fdk.listWeatherDaily);
    				} else {
    					humidityPlot.updateData(fdk.listWeatherHourly, fdk.listWeatherDaily);
    				}
    				plots.add(humidityPlot);
    				break;
    			case WeatherType.PRECIPITATION_TYPE:
    				if (precipitationPlot == null) {
    					precipitationPlot = new PrecipitationPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listWeatherHourly, fdk.listWeatherDaily);
    				} else {
    					precipitationPlot.updateData(fdk.listWeatherHourly, fdk.listWeatherDaily);
    				}
    				plots.add(precipitationPlot);
    				break;
    			case WeatherType.TEMPERATURE_TYPE:
    				if (temperaturePlot == null) {
    					temperaturePlot = new TemperaturePlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listWeatherHourly, fdk.listWeatherDaily);
    				} else {
    					temperaturePlot.updateData(fdk.listWeatherHourly, fdk.listWeatherDaily);
    				}
    				plots.add(temperaturePlot);
    				break;
    			case WeatherType.WINDSPEED_TYPE:
    				if (windPlot == null) {
    					windPlot = new WindPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listWeatherHourly, fdk.listWeatherDaily);
    				} else {
    					windPlot.updateData(fdk.listWeatherHourly, fdk.listWeatherDaily);
    				}
    				plots.add(windPlot);
    				break;
    			}
    		}
		}
		
		if(plotsType == PLOT_TYPE.ASTRONOMY || plotsType == PLOT_TYPE.USER){
			switch(plotsType){
			case ASTRONOMY: params = paramsSun; break;
			case USER: params = paramsUser; break;
			}
    		for (ParameterType pt : paramsSun) {
    			switch (pt.Id) {
    			case SunType.SUN_TYPE:
    				if (sunPlot == null) {
    					sunPlot = new SunPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listSun, fdk.listSolarEclipse);
    				} else {
    					sunPlot.updateData(fdk.listSun, fdk.listSolarEclipse);
    				}
    				plots.add(sunPlot);
    				break;
    			}
    		}

			switch(plotsType){
			case ASTRONOMY: params = paramsMoon; break;
			case USER: params = paramsUser; break;
			}
    		for (ParameterType pt : paramsMoon) {
    			switch (pt.Id) {
    			case MoonType.MOON_VISIBILITY_TYPE:
    				if (moonVisibilityPlot == null) {
    					moonVisibilityPlot = new MoonVisibilityPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(),
    					        MoonType.MOON_VISIBILITY_TYPE_MIN_BORDER, MoonType.MOON_VISIBILITY_TYPE_MAX_BORDER, MoonType.MOON_VISIBILITY_TYPE_STEP, fdk.listMoon);
    				} else {
    					moonVisibilityPlot.updateData(fdk.listMoon);
    				}
    				plots.add(moonVisibilityPlot);
    				break;
    			case MoonType.MOON_OLD_TYPE:
    				if (moonOldPlot == null) {
    					moonOldPlot = new MoonOldPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(),
    					        MoonType.MOON_OLD_TYPE_MIN_BORDER, MoonType.MOON_OLD_TYPE_MAX_BORDER, MoonType.MOON_OLD_TYPE_STEP, fdk.listMoon, fdk.listMoonPhase);
    				} else {
    					moonOldPlot.updateData(fdk.listMoon, fdk.listMoonPhase);
    				}
    				plots.add(moonOldPlot);
    				break;
    			case MoonType.MOON_TYPE:
    				if (moonPlot == null) {
    					moonPlot = new MoonPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listMoon);
    				} else {
    					moonPlot.updateData(fdk.listMoon);
    				}
    				plots.add(moonPlot);
    				break;
    			}
    		}

			switch(plotsType){
			case ASTRONOMY: params = paramsPlanet; break;
			case USER: params = paramsUser; break;
			}
    		for (ParameterType pt : paramsPlanet) {
    			switch (pt.Id) {
    			case PlanetType.MERCURY_TYPE:
    				if (mercuryPlot == null) {
    					mercuryPlot = new MercuryPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listPlanet);
    				} else {
    					mercuryPlot.updateData(fdk.listPlanet);
    				}
    				plots.add(mercuryPlot);
    				break;
    			case PlanetType.VENUS_TYPE:
    				if (venusPlot == null) {
    					venusPlot = new VenusPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listPlanet);
    				} else {
    					venusPlot.updateData(fdk.listPlanet);
    				}
    				plots.add(venusPlot);
    				break;
    			case PlanetType.MARS_TYPE:
    				if (marsPlot == null) {
    					marsPlot = new MarsPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listPlanet);
    				} else {
    					marsPlot.updateData(fdk.listPlanet);
    				}
    				plots.add(marsPlot);
    				break;
    			case PlanetType.JUPITER_TYPE:
    				if (jupiterPlot == null) {
    					jupiterPlot = new JupiterPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listPlanet);
    				} else {
    					jupiterPlot.updateData(fdk.listPlanet);
    				}
    				plots.add(jupiterPlot);
    				break;
    			case PlanetType.SATURN_TYPE:
    				if (saturnPlot == null) {
    					saturnPlot = new SaturnPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listPlanet);
    				} else {
    					saturnPlot.updateData(fdk.listPlanet);
    				}
    				plots.add(saturnPlot);
    				break;
    			case PlanetType.URANUS_TYPE:
    				if (uranusPlot == null) {
    					uranusPlot = new UranusPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listPlanet);
    				} else {
    					uranusPlot.updateData(fdk.listPlanet);
    				}
    				plots.add(uranusPlot);
    				break;
    			case PlanetType.NEPTUNE_TYPE:
    				if (neptunePlot == null) {
    					neptunePlot = new NeptunePlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listPlanet);
    				} else {
    					neptunePlot.updateData(fdk.listPlanet);
    				}
    				plots.add(neptunePlot);
    				break;
    			case PlanetType.PLUTO_TYPE:
    				if (plutoPlot == null) {
    					plutoPlot = new PlutoPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listPlanet);
    				} else {
    					plutoPlot.updateData(fdk.listPlanet);
    				}
    				plots.add(plutoPlot);
    				break;
    			}
    		}
    	}

		if(plotsType == PLOT_TYPE.GEOHELIOPHYSICS || plotsType == PLOT_TYPE.USER){
			switch(plotsType){
			case GEOHELIOPHYSICS: params = paramsGeoPhysics; break;
			case USER: params = paramsUser; break;
			}
			for (ParameterType pt : paramsGeoPhysics) {
				switch (pt.Id) {
				case GeoPhysicsType.KP_TYPE:
					if (kpPlot == null) {
						kpPlot = new KpPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), GeoPhysicsType.KP_TYPE_MIN_BORDER,
						        GeoPhysicsType.KP_TYPE_MAX_BORDER, GeoPhysicsType.KP_TYPE_STEP, fdk.listGeoPhysics);
					} else {
						kpPlot.updateData(fdk.listGeoPhysics);
					}
					plots.add(kpPlot);
					break;
				case GeoPhysicsType.AP_TYPE:
					if (apPlot == null) {
						apPlot = new ApPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listGeoPhysics);
					} else {
						apPlot.updateData(fdk.listGeoPhysics);
					}
					plots.add(apPlot);
					break;
				}
			}

			switch(plotsType){
			case GEOHELIOPHYSICS: params = paramsHelioPhysics; break;
			case USER: params = paramsUser; break;
			}
			for (ParameterType pt : paramsHelioPhysics) {
				switch (pt.Id) {
				case HelioPhysicsType.F10_7_TYPE:
					if (f10_7Plot == null) {
						f10_7Plot = new F10_7Plot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						f10_7Plot.updateData(fdk.listHelioPhysics);
					}
					plots.add(f10_7Plot);
					break;
				case HelioPhysicsType.SUNSPOT_NUMBER_TYPE:
					if (sunspotNumberPlot == null) {
						sunspotNumberPlot = new SunspotNumberPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						sunspotNumberPlot.updateData(fdk.listHelioPhysics);
					}
					plots.add(sunspotNumberPlot);
					break;
				case HelioPhysicsType.SUNSPOT_AREA_TYPE:
					if (sunspotAreaPlot == null) {
						sunspotAreaPlot = new SunspotAreaPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						sunspotAreaPlot.updateData(fdk.listHelioPhysics);
					}
					plots.add(sunspotAreaPlot);
					break;
				case HelioPhysicsType.NEW_REGIONS_TYPE:
					if (newRegionsPlot == null) {
						newRegionsPlot = new NewRegionsPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						newRegionsPlot.updateData(fdk.listHelioPhysics);
					}
					plots.add(newRegionsPlot);
					break;
				case HelioPhysicsType.XRAY_FLARES_C_TYPE:
					if (xRayFlaresCPlot == null) {
						xRayFlaresCPlot = new XRayFlaresCPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						xRayFlaresCPlot.updateData(fdk.listHelioPhysics);
					}
					plots.add(xRayFlaresCPlot);
					break;
				case HelioPhysicsType.XRAY_FLARES_M_TYPE:
					if (xRayFlaresMPlot == null) {
						xRayFlaresMPlot = new XRayFlaresMPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						xRayFlaresMPlot.updateData(fdk.listHelioPhysics);
					}
					plots.add(xRayFlaresMPlot);
					break;
				case HelioPhysicsType.XRAY_FLARES_X_TYPE:
					if (xRayFlaresXPlot == null) {
						xRayFlaresXPlot = new XRayFlaresXPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						xRayFlaresXPlot.updateData(fdk.listHelioPhysics);
					}
					plots.add(xRayFlaresXPlot);
					break;
				case HelioPhysicsType.OPTICAL_FLARES_1_TYPE:
					if (opticalFlares1Plot == null) {
						opticalFlares1Plot = new OpticalFlares1Plot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						opticalFlares1Plot.updateData(fdk.listHelioPhysics);
					}
					plots.add(opticalFlares1Plot);
					break;
				case HelioPhysicsType.OPTICAL_FLARES_2_TYPE:
					if (opticalFlares2Plot == null) {
						opticalFlares2Plot = new OpticalFlares2Plot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						opticalFlares2Plot.updateData(fdk.listHelioPhysics);
					}
					plots.add(opticalFlares2Plot);
					break;
				case HelioPhysicsType.OPTICAL_FLARES_3_TYPE:
					if (opticalFlares3Plot == null) {
						opticalFlares3Plot = new OpticalFlares3Plot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						opticalFlares3Plot.updateData(fdk.listHelioPhysics);
					}
					plots.add(opticalFlares3Plot);
					break;
				case HelioPhysicsType.OPTICAL_FLARES_S_TYPE:
					if (opticalFlaresSPlot == null) {
						opticalFlaresSPlot = new OpticalFlaresSPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						opticalFlaresSPlot.updateData(fdk.listHelioPhysics);
					}
					plots.add(opticalFlaresSPlot);
					break;
				case HelioPhysicsType.XBKGD_TYPE:
					if (xbkgdPlot == null) {
						xbkgdPlot = new XbkgdPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listHelioPhysics);
					} else {
						xbkgdPlot.updateData(fdk.listHelioPhysics);
					}
					plots.add(xbkgdPlot);
					break;
				}
			}

			switch(plotsType){
			case GEOHELIOPHYSICS: params = paramsParticle; break;
			case USER: params = paramsUser; break;
			}
			for (ParameterType pt : paramsParticle) {
				switch (pt.Id) {
				case ParticleType.PROTON_1MEV_TYPE:
					if (proton1MeVPlot == null) {
						proton1MeVPlot = new Proton1MeVPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listParticle);
					} else {
						proton1MeVPlot.updateData(fdk.listParticle);
					}
					plots.add(proton1MeVPlot);
					break;
				case ParticleType.PROTON_10MEV_TYPE:
					if (proton10MeVPlot == null) {
						proton10MeVPlot = new Proton10MeVPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listParticle);
					} else {
						proton10MeVPlot.updateData(fdk.listParticle);
					}
					plots.add(proton10MeVPlot);
					break;
				case ParticleType.PROTON_100MEV_TYPE:
					if (proton100MeVPlot == null) {
						proton100MeVPlot = new Proton100MeVPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listParticle);
					} else {
						proton100MeVPlot.updateData(fdk.listParticle);
					}
					plots.add(proton100MeVPlot);
					break;
				case ParticleType.ELECTRON_08MEV_TYPE:
					if (electron08MeVPlot == null) {
						electron08MeVPlot = new Electron08MeVPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listParticle);
					} else {
						electron08MeVPlot.updateData(fdk.listParticle);
					}
					plots.add(electron08MeVPlot);
					break;
				case ParticleType.ELECTRON_2MEV_TYPE:
					if (electron2MeVPlot == null) {
						electron2MeVPlot = new Electron2MeVPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, pt.getName(), pt.getUnitDimension(), fdk.listParticle);
					} else {
						electron2MeVPlot.updateData(fdk.listParticle);
					}
					plots.add(electron2MeVPlot);
					break;
				}
			}
		}
	}
	
	public void updateFactorPlots() {
		factorPlots = new ArrayList<IDataPlot>();
		for(FactorGroup fg : fdk.listFactorGroup) {
			int layoutHelpDialogId = 0;
			if(fg.getName().equals("Сон")){
				layoutHelpDialogId = R.layout.dialog_plot_help_factor_dream;
			} else if(fg.getName().equals("Питание")){
				layoutHelpDialogId = R.layout.dialog_plot_help_factor_food;
			} else if(fg.getName().equals("Психоэмоциональная нагрузка")){
				layoutHelpDialogId = R.layout.dialog_plot_help_factor_psychoemotional;
			} else if(fg.getName().equals("Физическая нагрузка")){
				layoutHelpDialogId = R.layout.dialog_plot_help_factor_physical;
			} else if(fg.getName().equals("Умственная нагрузка")){
				layoutHelpDialogId = R.layout.dialog_plot_help_factor_mental;
			} else if(fg.getName().equals("Лекарства")){
				layoutHelpDialogId = R.layout.dialog_plot_help_factor_drug;
			} else if(fg.getName().equals("Вредные факторы")){
				layoutHelpDialogId = R.layout.dialog_plot_help_factor_harmful;
			} else if(fg.getName().equals("Полезные факторы")){
				layoutHelpDialogId = R.layout.dialog_plot_help_factor_helpful;
			}
    		UserFactorPlot plot = new UserFactorPlot(activity, mPlotDateFrom, mPlotDateTo, mMarkedDate, fg.getName(), "", 0.0, 1.0, 0.2, fdk.listFactor, mRepository.getFactorTypes(fg.getId()), mRepository.getCustomFactorTypes(fg.getId()), fg.getId(), layoutHelpDialogId);
    		factorPlots.add(plot);
		}
	}
}