package com.health.dependency;

import com.health.data.BodyFeeling;
import com.health.data.DaoSession;
import com.health.data.User;
import com.health.data.WeatherDaily;
import com.health.db.DB;
import com.health.db.UserDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by alex sh on 19.10.2015.
 */
public class HeartDependency extends Dependency {

    @Override
    public void buildDependincies() {
        DaoSession daoSession = DB.db().newSession();
        HashMap<Date, Integer> result = new HashMap<>();
        WeatherDaily prevWeatherDaily = null;
        WeatherDaily nextWeatherDaily = null;
        WeatherDaily weatherDaily = null;
        for(int i = 0; i < mWeatherDailyList.size(); i++){
            weatherDaily = mWeatherDailyList.get(i);
            if(i > 0){
                prevWeatherDaily = mWeatherDailyList.get(i-1);
            }
            if(i < (mWeatherDailyList.size() - 1)){
                nextWeatherDaily = mWeatherDailyList.get(i+1);
            }
            Date dt = weatherDaily.getInfoDate();
            if(weatherDaily != null && prevWeatherDaily != null && nextWeatherDaily != null){
                if(weatherDaily.getMaxTemperature() > prevWeatherDaily.getMaxTemperature() && weatherDaily.getMaxTemperature() > nextWeatherDaily.getMaxTemperature()){
                    result.put(dt, 1);
                }
            }
            if(weatherDaily.getPressure() < 1000){
                if(result.containsKey(dt)){
                    result.put(dt, result.get(dt) + 1);
                } else {
                    result.put(dt, 1);
                }
            }
            for(Date key : result.keySet()){
                if(result.get(key) > 1){
                    BodyFeeling bodyFeeling = new BodyFeeling();
                    bodyFeeling.setUserId(User.ANONIM_USER_ID);
                    bodyFeeling.setBodyRegionId(1L);
                    bodyFeeling.setFeelingTypeId(1L);
                    daoSession.getBodyFeelingDao().insert(bodyFeeling);
                }
            }
        }
    }


}
