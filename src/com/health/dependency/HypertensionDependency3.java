package com.health.dependency;

import com.health.data.BodyFeeling;
import com.health.data.DaoSession;
import com.health.data.User;
import com.health.data.WeatherDaily;
import com.health.db.DB;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by alex sh on 19.10.2015.
 */
public class HypertensionDependency3 extends Dependency {

    @Override
    public void buildDependincies() {
        DaoSession daoSession = DB.db().newSession();
        HashMap<Date, Integer> result = new HashMap<>();
        WeatherDaily weatherDaily = null;
        Calendar calendar = Calendar.getInstance();
        for(int i = 0; i < mWeatherDailyList.size(); i++){
            weatherDaily = mWeatherDailyList.get(i);
            Date dt = weatherDaily.getInfoDate();
            calendar.setTime(dt);
            int month = calendar.get(Calendar.MONTH);
            if(weatherDaily.getPressure() < PRESSURE_AVERAGE[month]){
                if(weatherDaily.getHumidity() > HUMIDITY_AVERAGE[month]){
                    if(weatherDaily.getWindSpeed() > WINDSPEEDS_AVERAGE[month]){
                        result.put(dt, 1);
                    }
                }
            }

        }
        for(Date key : result.keySet()){
            insertBodyFeeling(daoSession, key, 3L, 3L);
            insertBodyFeeling(daoSession, key, 4L, 3L);
            insertBodyFeeling(daoSession, key, 102L, 3L);
            insertBodyFeeling(daoSession, key, 103L, 3L);
            insertBodyFeeling(daoSession, key, 24L, 32L);
            insertBodyFeeling(daoSession, key, 21L, 32L);
            insertBodyFeeling(daoSession, key, 1L, 10L);
        }
    }


}
