package com.health.dependency;

import com.health.data.BodyFeeling;
import com.health.data.DaoSession;
import com.health.data.GeoPhysics;
import com.health.data.User;
import com.health.data.WeatherDaily;
import com.health.db.DB;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by alex sh on 19.10.2015.
 */
public class HypertensionDependency4 extends Dependency {

    @Override
    public void buildDependincies() {
        DaoSession daoSession = DB.db().newSession();
        HashMap<Long, Date> result = new HashMap<>();
        WeatherDaily weatherDaily = null;
        long unixDay = -1;
        Calendar calendar = Calendar.getInstance();
        for(int i = 0; i < mWeatherDailyList.size(); i++){
            weatherDaily = mWeatherDailyList.get(i);
            Date dt = weatherDaily.getInfoDate();
            calendar.setTime(dt);
            int month = calendar.get(Calendar.MONTH);
            unixDay = dt.getTime() / MS_IN_DAY;
            if(weatherDaily.getPressure().longValue() > PRESSURE_AVERAGE[month]){
                result.put(unixDay, null);
            }
        }
        long prevUnixDay = -1;
        for(GeoPhysics geoPhysics : mGeoPhysicsList){
            Date dt = geoPhysics.getInfoDate();
            unixDay = dt.getTime() / MS_IN_DAY;
            if(geoPhysics.getKpId() != null && geoPhysics.getKpId() >= KPINDEX__ID_MEDIUM && prevUnixDay != unixDay){
                prevUnixDay = unixDay;
                if(result.containsKey(unixDay)){
                    result.put(unixDay, dt);
                }
            }
        }
        for(long key : result.keySet()){
            if(result.get(key) != null){
                Date dt = result.get(key);
                insertBodyFeeling(daoSession, dt, 2L, 1L);
                insertBodyFeeling(daoSession, dt, 104L, 7L);
                insertBodyFeeling(daoSession, dt, 21L, 32L);
            }

        }
    }


}
