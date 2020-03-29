package com.health.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.health.data.BodyFeeling;
import com.health.data.BodyFeelingType;
import com.health.data.BodyRegion;
import com.health.data.CommonFeeling;
import com.health.data.CommonFeelingGroup;
import com.health.data.CommonFeelingType;
import com.health.data.Complaint;
import com.health.data.CustomBodyFeelingType;
import com.health.data.CustomCommonFeelingType;
import com.health.data.CustomFactorType;
import com.health.data.Factor;
import com.health.data.FactorGroup;
import com.health.data.FactorType;
import com.health.data.GeoPhysics;
import com.health.data.HelioPhysics;
import com.health.data.IFeeling;
import com.health.data.IFeelingTypeInfo;
import com.health.data.IGridGroup;
import com.health.data.KpIndex;
import com.health.data.Moon;
import com.health.data.MoonPhase;
import com.health.data.Particle;
import com.health.data.Planet;
import com.health.data.SolarEclipse;
import com.health.data.Sun;
import com.health.data.User;
import com.health.data.UserBodyFeelingType;
import com.health.data.Weather;
import com.health.data.WeatherDaily;
import com.health.generator.Generator;
import com.health.viewmodel.BodyFeelingTypeInfo;

public class Repository implements Serializable, IRepository {

	private static ArrayList<Weather> mWeatherList;

	private static ArrayList<Sun> mSunList;

	private static ArrayList<SolarEclipse> mSolarEclipseList;

	private static ArrayList<Moon> mMoonList;

	private static ArrayList<MoonPhase> mMoonPhaseList;

	private static ArrayList<GeoPhysics> mGeoPhysicsList;

	private static ArrayList<HelioPhysics> mHelioPhysicsList;

	private static ArrayList<Particle> mParticleList;

	private static ArrayList<BodyFeelingType> mBodyFeelingTypeList;

	private static ArrayList<UserBodyFeelingType> mUserBodyFeelingTypeList;

	static {
		mWeatherList = new ArrayList<Weather>();
		GregorianCalendar gc = new GregorianCalendar(2013, 11, 1);
		long delta = 3 * 60 * 60 * 1000;
		int i = 0;
		Date nowDate = new Date();
		Date endDate = new Date(nowDate.getTime() + 15 * 24 * 60 * 60 * 1000);
		Date startDate = gc.getTime();
		Date d = gc.getTime();
		while (d.getTime() < endDate.getTime()) {
			Weather w = new Weather();
			d = new Date(startDate.getTime() + i * delta);
			GregorianCalendar gcTemp = new GregorianCalendar();
			gcTemp.setTime(d);
			w.setId(new Long(i));
			Double temp = (double) gcTemp.get(Calendar.DAY_OF_MONTH);
			w.setClouds((i % 10 + 1) * 10d);

			w.setTemperature(temp + 0d);
			w.setHumidity((i % 10 + 1) * 10d);
			w.setWindDeg((i % 10) * 18d);
			w.setWindSpeed((i % 10) + 0d);
			switch (i % 3) {
			case 0:
				w.setPressure(980d);
				break;
			case 1:
				w.setPressure(990d);
				break;
			case 2:
				w.setPressure(1000d);
				break;
			default:
				break;
			}
			w.setMaxTemperature(w.getTemperature());
			w.setMinTemperature(w.getTemperature());
			w.setInfoDate(d);
			w.setSnow(null);
			w.setRain( (i % 10) == 0 ? 2d : 0d);
			i++;
			mWeatherList.add(w);
		}

		mBodyFeelingTypeList = new ArrayList<BodyFeelingType>();
		mUserBodyFeelingTypeList = new ArrayList<UserBodyFeelingType>();
		for (i = 1; i < 20; i++) {
			BodyFeelingType bodyFeelingType = new BodyFeelingType();
			bodyFeelingType.setId(new Long(i));
			bodyFeelingType.setName(String.format("Ощущение %d", i));
			mBodyFeelingTypeList.add(bodyFeelingType);
			UserBodyFeelingType userBodyFeelingType = new UserBodyFeelingType();
			userBodyFeelingType.setBodyFeelingTypeId(new Long(i));
			userBodyFeelingType.setId(new Long(i));
			userBodyFeelingType.setColor( i * 100);
			mUserBodyFeelingTypeList.add(userBodyFeelingType);
		}

	}

	private ArrayList<BodyFeeling> mBodyFeelings = new ArrayList<BodyFeeling>();

	private Context context;

	public Repository(Context context) {
		this.mBodyFeelings = Generator.generateBodyFeelings(context);
		this.context = context;
	}

	@Override
	public void addBodyFeeling(BodyFeeling bodyFeeling) {
		int id = mBodyFeelings.size();
		bodyFeeling.setId(new Long(id));
		mBodyFeelings.add(bodyFeeling);
	}

	@Override
	public void updateBodyFeeling(BodyFeeling bodyFeeling) {
		long index = bodyFeeling.getId();
		if (index >= 0 && index < mBodyFeelings.size()) {
			mBodyFeelings.remove(index);
			mBodyFeelings.add((int) index, bodyFeeling);
		}

	}

	@Override
	public void deleteBodyFeeling(BodyFeeling bodyFeeling) {
		long index = bodyFeeling.getId();
		if (index >= 0 && index < mBodyFeelings.size()) {
			mBodyFeelings.remove(index);
		}

	}

	@Override
	public BodyFeeling getBodyFeelingById(Context context, long id) {
		return mBodyFeelings.get((int) id);
	}


	@Override
	public User getCurrentUser() {
		User user = new User();
		user.setFName("Имя");
		user.setLName("Фамилия");
		user.setMName("Отчество");
		user.setBirthDate(new Date());
		user.setCreateDate(new Date());
		user.setLogin("login");
		user.setPassword("password");
		user.setSex(1);
		return user;
	}

	@Override
	public List<Weather> getWeatherList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		ArrayList<Weather> result = new ArrayList<Weather>();
		for (Weather w : mWeatherList) {
			if (w.getInfoDate().getTime() >= dtFrom.getTime() && w.getInfoDate().getTime() <= dtTo.getTime()) {
				result.add(w);
			}
		}
		return result;
	}

	@Override
	public List<Sun> getSunList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		ArrayList<Sun> result = new ArrayList<Sun>();
		for (Sun s : mSunList) {
			if (s.getInfoDate().getTime() >= dtFrom.getTime() && s.getInfoDate().getTime() <= dtTo.getTime()) {
				result.add(s);
			}
		}
		return result;
	}


	@Override
	public List<SolarEclipse> getSolarEclipseList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		ArrayList<SolarEclipse> result = new ArrayList<SolarEclipse>();
		for (SolarEclipse se : mSolarEclipseList) {
			if (se.getInfoDate().getTime() >= dtFrom.getTime() && se.getInfoDate().getTime() <= dtTo.getTime()) {
				result.add(se);
			}
		}
		return result;
	}
	
	@Override
	public List<Moon> getMoonList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		ArrayList<Moon> result = new ArrayList<Moon>();
		for (Moon m : mMoonList) {
			if (m.getInfoDate().getTime() >= dtFrom.getTime() && m.getInfoDate().getTime() <= dtTo.getTime()) {
				result.add(m);
			}
		}
		return result;
	}

	@Override
	public List<MoonPhase> getMoonPhaseList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		ArrayList<MoonPhase> result = new ArrayList<MoonPhase>();
		for (MoonPhase mp : mMoonPhaseList) {
			if (mp.getInfoDate().getTime() >= dtFrom.getTime() && mp.getInfoDate().getTime() <= dtTo.getTime()) {
				result.add(mp);
			}
		}
		return result;
	}

	@Override
	public List<GeoPhysics> getGeoPhysicsList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		ArrayList<GeoPhysics> result = new ArrayList<GeoPhysics>();
		for (GeoPhysics gp : mGeoPhysicsList) {
			if (gp.getInfoDate().getTime() >= dtFrom.getTime() && gp.getInfoDate().getTime() <= dtTo.getTime()) {
				result.add(gp);
			}
		}
		return result;
	}

	@Override
	public List<HelioPhysics> getHelioPhysicsList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		ArrayList<HelioPhysics> result = new ArrayList<HelioPhysics>();
		for (HelioPhysics hp : mHelioPhysicsList) {
			if (hp.getInfoDate().getTime() >= dtFrom.getTime() && hp.getInfoDate().getTime() <= dtTo.getTime()) {
				result.add(hp);
			}
		}
		return result;
	}

	@Override
	public List<Particle> getParticleList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		ArrayList<Particle> result = new ArrayList<Particle>();
		for (Particle p : mParticleList) {
			if (p.getInfoDate().getTime() >= dtFrom.getTime() && p.getInfoDate().getTime() <= dtTo.getTime()) {
				result.add(p);
			}
		}
		return result;
	}

	@Override
	public List<UserBodyFeelingType> getUserBodyFeelingTypes(String filterBodyFeelingTypeName) {
		if (TextUtils.isEmpty(filterBodyFeelingTypeName)) {
			return mUserBodyFeelingTypeList;
		} else {
			List<UserBodyFeelingType> result = new ArrayList<UserBodyFeelingType>();
			for (UserBodyFeelingType u : mUserBodyFeelingTypeList) {
				if (u.getBodyFeelingType().toString().contains(filterBodyFeelingTypeName)) {
					result.add(u);
				}
			}
			return result;
		}
	}

	@Override
	public UserBodyFeelingType getUserBodyFeelingType(long bodyFeelingTypeId) {
		for (UserBodyFeelingType u : mUserBodyFeelingTypeList) {
			if (u.getBodyFeelingTypeId() == bodyFeelingTypeId) {
				return u;
			}
		}
		return null;
	}

	@Override
	public UserBodyFeelingType getDefaultUserBodyFeelingType() {
		UserBodyFeelingType userBodyFeelingType = new UserBodyFeelingType();
		userBodyFeelingType.setBodyFeelingType(null);
		userBodyFeelingType.setColor(IRepository.DEFAULT_USER_BODY_FEELING_TYPE_COLOR);
		userBodyFeelingType.setId(new Long(1));
		return userBodyFeelingType;
	}

	@Override
	public void updateUserBodyFeelingType(UserBodyFeelingType userBodyFeelingType) {
		for (UserBodyFeelingType u : mUserBodyFeelingTypeList) {
			if (u.getId() == userBodyFeelingType.getId()) {
				u.setBodyFeelingType(userBodyFeelingType.getBodyFeelingType());
				u.setColor(userBodyFeelingType.getColor());
			}
		}
	}

	@Override
	public int getUserColorForBodyFeelingType(long bodyFeelingTypeId) {
		int color = getDefaultUserBodyFeelingType().getColor();
		for (UserBodyFeelingType u : mUserBodyFeelingTypeList) {
			if (u.getId() == bodyFeelingTypeId) {
				color = u.getColor();
			}
		}
		return color;
	}

	@Override
	public LinkedHashMap<IFeelingTypeInfo, List<IFeeling>> getFeelingGroups(Date dtFrom, Date dtTo) {
		LinkedHashMap<IFeelingTypeInfo, List<IFeeling>> result = new LinkedHashMap<IFeelingTypeInfo, List<IFeeling>>();
		for (int i = 0; i < 20; i++) {
			ArrayList<IFeeling> value = new ArrayList<IFeeling>();
			BodyFeelingType bodyFeelingType = new BodyFeelingType();
			BodyRegion bodyRegion = new BodyRegion();
			UserBodyFeelingType userBodyfeelingType = new UserBodyFeelingType();
			userBodyfeelingType.setColor(Color.RED);
			userBodyfeelingType.setBodyFeelingTypeId(bodyFeelingType.getId());
			bodyFeelingType.setName("Боль " + Integer.toString(i));
			bodyRegion.setName("Голова");
			bodyRegion.setId(new Long(1));
			IFeelingTypeInfo key = new BodyFeelingTypeInfo(bodyFeelingType, null, bodyRegion, userBodyfeelingType);
			Random random = new Random();
			for (int j = 0; j < 10; j++) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.add(Calendar.DAY_OF_YEAR, -random.nextInt(30));
				BodyFeeling bodyFeeling = new BodyFeeling();
				bodyFeeling.setStartDate(gc.getTime());
				value.add((IFeeling)bodyFeeling);
			}
			result.put(key, value);
		}
		return result;
	}

	@Override
	public List<BodyFeelingType> getBodyFeelingTypes(long bodyRegionId) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void addCommonFeeling(CommonFeeling commonFeeling) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateCommonFeeling(CommonFeeling commonFeeling) {
		// TODO Auto-generated method stub
	}

	@Override
	public void deleteCommonFeeling(CommonFeeling commonFeeling) {
		// TODO Auto-generated method stub
	}

	@Override
	public CommonFeeling getCommonFeelingById(Context context, long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomBodyFeelingType addCustomBodyFeelingType(CustomBodyFeelingType customBodyFeelingType) {
		return customBodyFeelingType;
	}
	
	@Override
    public CustomBodyFeelingType getCustomBodyFeelingType(String customBodyFeelingTypeName) {
	    // TODO Auto-generated method stub
	    return null;
    }


	@Override
	public List<BodyFeeling> getBodyFeelings(Long bodyFeelingTypeId, Long customBodyFeelingTypeId, long bodyRegionId, Date dtFrom, Date dtTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CustomBodyFeelingType> getCustomBodyFeelingTypes(long bodyRegionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<KpIndex> getKpIndicies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addFactor(Factor commonFeeling) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateFactor(Factor factor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteFactor(Factor factor) {
		// TODO Auto-generated method stub

	}

	@Override
	public Factor getFactorById(Context context, long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public CustomFactorType addCustomFactorType(CustomFactorType customFactorType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FactorType> getFactorTypes(long factorGroupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CustomFactorType> getCustomFactorTypes(long factorGroupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Factor> getFactors(Long factorTypeId, Long customFactorTypeId, Date dtFrom, Date dtTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Factor> getFactorList(Date dtFrom, Date dtTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Planet> getPlanetList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
		return null;
	}

	@Override
	public CustomCommonFeelingType addCustomCommonFeelingType(CustomCommonFeelingType customCommonFeelingType) {
		return null;
	}

	@Override
	public List<CommonFeeling> getCommonFeelings(Long commonFeelingTypeId, Long customCommonFeelingTypeId, Date dtFrom, Date dtTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CustomCommonFeelingType> getCustomCommonFeelingTypes(long feelingGroupId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FactorGroup getFactorGroupById(Context context, long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonFeelingGroup getCommonFeelingGroupById(Context context, long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNextOrdinalNumberForFactorGroup(Context context, long factorGroupId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNextOrdinalNumberForCommonFeelingGroup(Context context, long commonFeelingGroupId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<CommonFeelingType> getCommonFeelingTypes(long feelingGroupId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<FactorGroup> getFactorGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public void setLoadDataListener(ILoadDataListener loadDataListener) {
		// TODO Auto-generated method stub	    
    }

	@Override
    public LinkedHashMap<IGridGroup, List<Complaint>> getBodyComplaints(Date dtFrom, Date dtTo) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public LinkedHashMap<IGridGroup, List<Complaint>> getUserBodyComplaints(Date dtFrom, Date dtTo) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Weather getCurrentWeatherOnDate(Date dt) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Sun getCurrentSunOnDate(Date dt) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Moon getCurrentMoonOnDate(Date dt) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public GeoPhysics getCurrentGeoPhysicsOnDate(Date dt) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public HelioPhysics getCurrentHelioPhysicsOnDate(Date dt) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public Particle getCurrentParticleOnDate(Date dt) {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public List<WeatherDaily> getWeatherDailyList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData) {
	    // TODO Auto-generated method stub
	    return null;
    }
	
}