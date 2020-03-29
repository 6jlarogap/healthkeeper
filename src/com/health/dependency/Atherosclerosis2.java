package com.health.dependency;

import com.health.data.DaoSession;
import com.health.data.GeoPhysics;
import com.health.data.WeatherDaily;
import com.health.db.DB;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by alex sh on 19.10.2015.
 */
public class Atherosclerosis2 extends Dependency {

    @Override
    public void buildDependincies() {
        DaoSession daoSession = DB.db().newSession();
        HashMap<Long, Date> result = new HashMap<>();
        WeatherDaily weatherDaily = null;
        Calendar calendar = Calendar.getInstance();
        long unixDay = -1;
        for(int i = 0; i < mWeatherDailyList.size(); i++){
            weatherDaily = mWeatherDailyList.get(i);
            Date dt = weatherDaily.getInfoDate();
            calendar.setTime(dt);
            int month = calendar.get(Calendar.MONTH);
            unixDay = dt.getTime() / MS_IN_DAY;
            if(weatherDaily.getPressure().longValue() < PRESSURE_AVERAGE[month]){
                result.put(unixDay, dt);
            }
        }
        long prevKpId = -1;
        for(GeoPhysics geoPhysics : mGeoPhysicsList){
            Date dt = geoPhysics.getInfoDate();
            unixDay = dt.getTime() / MS_IN_DAY;
            if(geoPhysics.getKpId() != null && geoPhysics.getKpId() >= KPINDEX__ID_MEDIUM ){
                if(result.containsKey(unixDay)){
                    if(geoPhysics.getKpId() > prevKpId || result.get(unixDay) == null){
                        result.put(unixDay, dt);
                    }
                }
                prevKpId = geoPhysics.getKpId().longValue();
            }
        }
        for(Long key : result.keySet()){
            if(result.get(key) != null){
                Date dt = result.get(key);
                insertBodyFeeling(daoSession, dt, 2L, 1L);
                insertBodyFeeling(daoSession, dt, 13L, 23L);
                insertBodyFeeling(daoSession, dt, 14L, 23L);
                insertBodyFeeling(daoSession, dt, 110L, 23L);
                insertBodyFeeling(daoSession, dt, 111L, 23L);
            }

        }
    }


}
