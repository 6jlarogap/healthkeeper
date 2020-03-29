package com.health.repository;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;

import com.health.data.BodyFeeling;
import com.health.data.BodyFeelingType;
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

public interface IRepository {

    public enum DATA_AMOUNT_TYPE { ALL, HOURLY_3H, HOURLY_6H, DAILY, DAILY_2D, DAILY_4D, DAILY_6D }
		
	interface ILoadDataListener {
		void loadData(Date dtFrom, Date dtTo);
	}
	
	void setLoadDataListener(ILoadDataListener loadDataListener);
    
    User getCurrentUser();
    
    CustomBodyFeelingType addCustomBodyFeelingType(CustomBodyFeelingType customBodyFeelingType);
    CustomBodyFeelingType getCustomBodyFeelingType(String customBodyFeelingTypeName);
    
    void addBodyFeeling(BodyFeeling bodyFeeling);
    void updateBodyFeeling(BodyFeeling bodyFeeling);
    void deleteBodyFeeling(BodyFeeling bodyFeeling);
    
    CustomCommonFeelingType addCustomCommonFeelingType(CustomCommonFeelingType customCommonFeelingType);
    
    void addCommonFeeling(CommonFeeling commonFeeling);
    void updateCommonFeeling(CommonFeeling commonFeeling);
    void deleteCommonFeeling(CommonFeeling commonFeeling);
    
    CustomFactorType addCustomFactorType(CustomFactorType customFactorType);
    
    void addFactor(Factor factor);
    void updateFactor(Factor factor);
    void deleteFactor(Factor factor);
    
    BodyFeeling getBodyFeelingById(Context context, long id);
    CommonFeeling getCommonFeelingById(Context context, long id);
    Factor getFactorById(Context context, long id);
    FactorGroup getFactorGroupById(Context context, long id);
    CommonFeelingGroup getCommonFeelingGroupById(Context context, long id);
    int getNextOrdinalNumberForFactorGroup(Context context, long factorGroupId);
    int getNextOrdinalNumberForCommonFeelingGroup(Context context, long commonFeelingGroupId);


    List<Weather> getWeatherList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData);

    List<WeatherDaily> getWeatherDailyList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData);

    List<Sun> getSunList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData);

    List<SolarEclipse> getSolarEclipseList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData);

    List<Moon> getMoonList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData);

    List<MoonPhase> getMoonPhaseList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData);

    List<GeoPhysics> getGeoPhysicsList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData);

    List<HelioPhysics> getHelioPhysicsList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData);

    List<Particle> getParticleList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData);

    List<Planet> getPlanetList(Date dtFrom, Date dtTo, DATA_AMOUNT_TYPE amountData);

    List<Factor> getFactorList(Date dtFrom, Date dtTo);

    Weather getCurrentWeatherOnDate(Date dt);
    Sun getCurrentSunOnDate(Date dt);
    Moon getCurrentMoonOnDate(Date dt);
    GeoPhysics getCurrentGeoPhysicsOnDate(Date dt);
    HelioPhysics getCurrentHelioPhysicsOnDate(Date dt);
    Particle getCurrentParticleOnDate(Date dt);
    
    static final int DEFAULT_USER_BODY_FEELING_TYPE_COLOR = 0xFFFF0000;
    List<UserBodyFeelingType> getUserBodyFeelingTypes(String filterBodyFeelingTypeName);
    UserBodyFeelingType getUserBodyFeelingType(long bodyFeelingTypeId);
    UserBodyFeelingType getDefaultUserBodyFeelingType();
    void updateUserBodyFeelingType(UserBodyFeelingType userBodyFeelingType);
    int getUserColorForBodyFeelingType(long bodyFeelingTypeId);
    LinkedHashMap<IFeelingTypeInfo, List<IFeeling>> getFeelingGroups(Date dtFrom, Date dtTo);
    LinkedHashMap<IGridGroup, List<Complaint>> getBodyComplaints(Date dtFrom, Date dtTo);
    LinkedHashMap<IGridGroup, List<Complaint>> getUserBodyComplaints(Date dtFrom, Date dtTo);
    
    List<BodyFeelingType> getBodyFeelingTypes(long bodyRegionId);
    List<CustomBodyFeelingType> getCustomBodyFeelingTypes(long bodyRegionId);
    List<FactorType> getFactorTypes(long factorGroupId);
    List<CustomFactorType> getCustomFactorTypes(long factorGroupId);
    List<CommonFeelingType> getCommonFeelingTypes(long feelingGroupId);
    List<CustomCommonFeelingType> getCustomCommonFeelingTypes(long feelingGroupId);
    
    List<BodyFeeling> getBodyFeelings(Long bodyFeelingTypeId, Long customBodyFeelingTypeId, long bodyRegionId, Date dtFrom, Date dtTo);
    List<CommonFeeling> getCommonFeelings(Long commonFeelingTypeId, Long customCommonFeelingTypeId, Date dtFrom, Date dtTo);
    List<Factor> getFactors(Long factorTypeId, Long customFactorTypeId, Date dtFrom, Date dtTo);

    List<KpIndex> getKpIndicies();
    
    
    List<FactorGroup> getFactorGroups();
}
