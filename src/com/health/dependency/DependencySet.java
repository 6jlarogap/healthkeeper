package com.health.dependency;

import com.health.data.DaoSession;
import com.health.data.GeoPhysics;
import com.health.data.GeoPhysicsDao;
import com.health.data.Particle;
import com.health.data.ParticleDao;
import com.health.data.WeatherDaily;
import com.health.data.WeatherDailyDao;
import com.health.db.DB;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by alex sh on 19.10.2015.
 */
public class DependencySet {

    public static final int DEFAULT_DIARY_ID = 4;

    private static LinkedHashMap<DependencyGroup, ArrayList<Dependency>> dependencyMap = new LinkedHashMap<>();

    static {
        DependencyGroup dependencyGroup = new DependencyGroup(1, "гипертоническая болезнь 1", "Дневник1");
        ArrayList<Dependency> dependencyList = new ArrayList<>();
        Dependency dependency = new HypertensionDependency1();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);


        dependencyGroup = new DependencyGroup(2, "гипертоническая болезнь 2", "Дневник2");
        dependencyList = new ArrayList<>();
        dependency = new HypertensionDependency2();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(3, "гипертоническая болезнь 3", "Дневник3");
        dependencyList = new ArrayList<>();
        dependency = new HypertensionDependency3();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(4, "гипертоническая болезнь 4", "Дневник4");
        dependencyList = new ArrayList<>();
        dependency = new HypertensionDependency4();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(5, "атеросклероз сосудов головного мозга 1", "Дневник5");
        dependencyList = new ArrayList<>();
        dependency = new Atherosclerosis1();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(6, "атеросклероз сосудов головного мозга 2", "Дневник6");
        dependencyList = new ArrayList<>();
        dependency = new Atherosclerosis2();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(7, "бронхолёгочные болезни 1", "Дневник7");
        dependencyList = new ArrayList<>();
        dependency = new Bronchit1();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(8, "бронхолёгочные болезни 2", "Дневник8");
        dependencyList = new ArrayList<>();
        dependency = new Bronchit2();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(9, "бронхолёгочные болезни 3", "Дневник9");
        dependencyList = new ArrayList<>();
        dependency = new Bronchit3();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(10, "остеохондроз", "Дневник10");
        dependencyList = new ArrayList<>();
        dependency = new Osteochondrosis1();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(11, "радикулит", "Дневник11");
        dependencyList = new ArrayList<>();
        dependency = new Radiculitis1();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(12, "артрит 1", "Дневник12");
        dependencyList = new ArrayList<>();
        dependency = new Arthritis1();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(13, "артрит 2", "Дневник13");
        dependencyList = new ArrayList<>();
        dependency = new Arthritis2();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);

        dependencyGroup = new DependencyGroup(14, "пиелонефрит", "Дневник14");
        dependencyList = new ArrayList<>();
        dependency = new Pyelonephritis1();
        dependencyList.add(dependency);
        dependencyMap.put(dependencyGroup, dependencyList);
    }

    public static ArrayList<String> getDiaryTitles(){
        ArrayList<String> result = new ArrayList<>();
        for(DependencyGroup group : dependencyMap.keySet()){
            result.add(group.getHiddenName());
        }
        return result;
    }

    public static DependencyGroup getDependencyGroup(int dependencyGroupId){
        DependencyGroup result = null;
        for(DependencyGroup dependencyGroup: dependencyMap.keySet()){
            if(dependencyGroup.getId() == dependencyGroupId){
                result = dependencyGroup;
                break;
            }
        }
        return result;
    }

    public void generateDiaryFeeling(int dependencyGroupId, Date dtFrom, Date dtTo){
        DaoSession daoSession = DB.db().newSession();
        daoSession.getBodyFeelingDao().deleteAll();
        DependencyGroup key = null;
        for(DependencyGroup dependencyGroup: dependencyMap.keySet()){
            if(dependencyGroup.getId() == dependencyGroupId){
                key = dependencyGroup;
                break;
            }
        }
        if(key != null){
            List<WeatherDaily> weatherDailyList = daoSession.getWeatherDailyDao().queryBuilder().where(WeatherDailyDao.Properties.InfoDate.ge(dtFrom), WeatherDailyDao.Properties.InfoDate.le(dtTo)).orderAsc(WeatherDailyDao.Properties.InfoDate).list();
            List<Particle> particleList = daoSession.getParticleDao().queryBuilder().where(ParticleDao.Properties.InfoDate.ge(dtFrom), ParticleDao.Properties.InfoDate.le(dtTo)).orderAsc(ParticleDao.Properties.InfoDate).list();
            List<GeoPhysics> geoPhysicsList = daoSession.getGeoPhysicsDao().queryBuilder().where(GeoPhysicsDao.Properties.InfoDate.ge(dtFrom), GeoPhysicsDao.Properties.InfoDate.le(dtTo)).orderAsc(GeoPhysicsDao.Properties.InfoDate).list();
            for(Dependency dependency : dependencyMap.get(key)){
                dependency.setParameterData(weatherDailyList, particleList, geoPhysicsList);
                dependency.buildDependincies();
            }
        }
    }
}
