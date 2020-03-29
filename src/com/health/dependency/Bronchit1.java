package com.health.dependency;

import com.health.data.DaoSession;
import com.health.data.WeatherDaily;
import com.health.db.DB;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by alex sh on 19.10.2015.
 */
public class Bronchit1 extends Dependency {

    @Override
    public void buildDependincies() {
        DaoSession daoSession = DB.db().newSession();
        HashMap<Date, Date> result = new HashMap<>();
        WeatherDaily weatherDaily = null;
        Calendar calendar = Calendar.getInstance();

        for(int i = 0; i < mWeatherDailyList.size(); i++){
            weatherDaily = mWeatherDailyList.get(i);
            Date dt = weatherDaily.getInfoDate();
            calendar.setTime(dt);
            int month = calendar.get(Calendar.MONTH);
            if((weatherDaily.getMinTemperature() + weatherDaily.getMaxTemperature())/2 <= TEMPERATURES_AVERAGE[month] && weatherDaily.getWindSpeed() >= WINDSPEEDS_AVERAGE[month]  && weatherDaily.getHumidity() >= HUMIDITY_AVERAGE[month]){
                result.put(dt, dt);
            }
        }

        for(Date dt : result.keySet()){
            if(result.get(dt) != null){
                insertBodyFeeling(daoSession, dt, 20L, 30L);
                insertBodyFeeling(daoSession, dt, 21L, 30L);
                insertBodyFeeling(daoSession, dt, 24L, 30L);
                insertBodyFeeling(daoSession, dt, 20L, 18L);
                insertBodyFeeling(daoSession, dt, 21L, 18L);
                insertBodyFeeling(daoSession, dt, 24L, 18L);
            }

        }
    }


}
