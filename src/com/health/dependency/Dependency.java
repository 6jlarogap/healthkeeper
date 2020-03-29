package com.health.dependency;

import com.health.data.BodyFeeling;
import com.health.data.DaoSession;
import com.health.data.GeoPhysics;
import com.health.data.Particle;
import com.health.data.User;
import com.health.data.WeatherDaily;

import java.util.Date;
import java.util.List;

/**
 * Created by alex sh on 19.10.2015.
 */
public abstract class Dependency {

    public static final long MS_IN_DAY = 86400 * 1000L;

    /*public static final long PRESSURE_HIGH = 1026; //770 mm
    public static final long PRESSURE_LOW = 1000;

    public static final long HUMIDITY_HIGH = 70;
    public static final long HUMIDITY_LOW = 50;

    public static final long WINDSPEED_LOW = 2;
    public static final long WINDSPEED_HIGH = 5;*/

    public static final long KPINDEX__ID_MEDIUM = 9;

    public static final double[] TEMPERATURES_AVERAGE = {-4.5, -4.4, -0.5, 7.2, 13.3, 16.4, 18.5, 17.5, 12.1, 6.6, 0.6, -3.4};

    public static final double[] WINDSPEEDS_AVERAGE = {2.8, 2.7, 2.6, 2.5, 2.2, 2.0, 1.9, 1.8, 2.0, 2.3, 2.7, 2.7}; //м/с

    public static final double[] HUMIDITY_AVERAGE = {86, 83, 77, 67, 66, 70, 71, 72, 79, 82, 88, 88}; // %

    public static final double[] PRESSURE_AVERAGE ={999.13, 1007.63, 1010.04, 1002.37, 1004.86, 1006.99, 1008.003, 1020.002, 1018.74, 1023.009, 1013.69, 1005.60};

    protected List<WeatherDaily> mWeatherDailyList = null;

    protected List<Particle> mParticleList = null;

    protected List<GeoPhysics> mGeoPhysicsList = null;

    public void setParameterData(List<WeatherDaily> weatherDailyList, List<Particle> particleList, List<GeoPhysics> geoPhysicsList){
        this.mWeatherDailyList = weatherDailyList;
        this.mParticleList = particleList;
        this.mGeoPhysicsList = geoPhysicsList;
    }

    public abstract void buildDependincies();

    protected void insertBodyFeeling(DaoSession daoSession, Date dt, Long bodyRegionId, Long bodyFeelingId){
        BodyFeeling bodyFeeling = new BodyFeeling();
        bodyFeeling.setUserId(User.ANONIM_USER_ID);
        bodyFeeling.setStartDate(dt);
        bodyFeeling.setBodyRegionId(bodyRegionId);
        bodyFeeling.setFeelingTypeId(bodyFeelingId);
        daoSession.getBodyFeelingDao().insert(bodyFeeling);
    }


}
