<?php
class DependencyGroup
{    
    private $id;
    private $name;
    private $hiddenName;

    function DependencyGroup($id, $name, $hiddenName) {
        $this->id = $id;
        $this->name = $name;
        $this->hiddenName = $hiddenName;
    }

    function getId()
    {
        return $this->id;
    }
    
    function getName()
    {
        return $this->name;
    }
    
    function getHiddenName()
    {
        return $this->hiddenName;
    }
}

abstract class Dependency {

    const SEC_IN_DAY = 86400;    

    const KPINDEX__ID_MEDIUM = 9;

    protected $TEMPERATURES_AVERAGE = array(-4.5, -4.4, -0.5, 7.2, 13.3, 16.4, 18.5, 17.5, 12.1, 6.6, 0.6, -3.4);

    protected $WINDSPEEDS_AVERAGE = array(2.8, 2.7, 2.6, 2.5, 2.2, 2.0, 1.9, 1.8, 2.0, 2.3, 2.7, 2.7); //м/с

    protected $HUMIDITY_AVERAGE = array(86, 83, 77, 67, 66, 70, 71, 72, 79, 82, 88, 88); // %

    protected $PRESSURE_AVERAGE = array(999.13, 1007.63, 1010.04, 1002.37, 1004.86, 1006.99, 1008.003, 1020.002, 1018.74, 1023.009, 1013.69, 1005.60);

    protected $mWeatherDailyList = NULL;

    protected $mParticleList = NULL;

    protected $mGeoPhysicsList = NULL;

    public function setParameterData($weatherDailyList, $particleList, $geoPhysicsList){
        $this->mWeatherDailyList = $weatherDailyList;
        $this->mParticleList = $particleList;
        $this->mGeoPhysicsList = $geoPhysicsList;        
    }
    
    public function getBodyFeeling($dt, $bodyregionid, $feelingtypeid){
        $bf = array("id" => null, "uid" => null, "rid" => null, "dt" => $dt, "ftid" => $feelingtypeid, "reg" => $bodyregionid, "cftid" => null, "x" => null, "y" => null, "op" => 1);
        return $bf;
    }
    
    public function getRoundUnixTime($dt){
        $unixtime = strtotime($dt."UTC");        
        return intval(floor($unixtime/self::SEC_IN_DAY) * self::SEC_IN_DAY);        
    }
    
    public function getMonth($unixTime){        
        $month = date('n', $unixTime) - 1;
        return $month;
    }

    abstract public function buildDependincies();

}

class HypertensionDependency1 extends Dependency {
    
    public function buildDependincies() {        
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['pressure'] > $this->PRESSURE_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 104, 7);
            $result[] = $this->getBodyFeeling($dt, 13, 23);
            $result[] = $this->getBodyFeeling($dt, 14, 23);
            $result[] = $this->getBodyFeeling($dt, 21, 32);
            
        };
        return $result;
    }
}

class HypertensionDependency2 extends Dependency {
    
    public function buildDependincies() {        
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['pressure'] < $this->PRESSURE_AVERAGE[$month] and $weatherDaily['humidity'] > $this->HUMIDITY_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 1, 10);
            $result[] = $this->getBodyFeeling($dt, 101, 10);
            $result[] = $this->getBodyFeeling($dt, 21, 2);
            $result[] = $this->getBodyFeeling($dt, 13, 23);
            $result[] = $this->getBodyFeeling($dt, 14, 23);
            $result[] = $this->getBodyFeeling($dt, 110, 23);
            $result[] = $this->getBodyFeeling($dt, 111, 23);
            
        };
        return $result;
    }
}

class HypertensionDependency3 extends Dependency {
    
    public function buildDependincies() {        
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['pressure'] < $this->PRESSURE_AVERAGE[$month] and $weatherDaily['humidity'] > $this->HUMIDITY_AVERAGE[$month] and $weatherDaily['windspeed'] > $this->WINDSPEEDS_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 3, 3);
            $result[] = $this->getBodyFeeling($dt, 4, 3);
            $result[] = $this->getBodyFeeling($dt, 102, 3);
            $result[] = $this->getBodyFeeling($dt, 103, 3);
            $result[] = $this->getBodyFeeling($dt, 24, 32);
            $result[] = $this->getBodyFeeling($dt, 21, 32);
            $result[] = $this->getBodyFeeling($dt, 1, 10);                       
        };
        return $result;
    }
}

class HypertensionDependency4 extends Dependency {
    
    public function buildDependincies() {        
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['pressure'] > $this->PRESSURE_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };
        $prevunixtime = -1;
        foreach($this->mGeoPhysicsList as $geo){            
            $dt = $geo['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($geo['kpid'] >= self::KPINDEX__ID_MEDIUM and $prevunixtime != $unixtime){
                $prevunixtime = $unixtime;                
                if(array_key_exists($unixtime, $dictionary)){                    
                    $dictionary[$unixtime] = array("dt"=>$dt, "count" => 2);
                }              
                
            };
        };
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $count = $value["count"];
            if($count == 2){
                $result[] = $this->getBodyFeeling($dt, 2, 1);
                $result[] = $this->getBodyFeeling($dt, 104, 7);
                $result[] = $this->getBodyFeeling($dt, 21, 32);            
            }
        };
        return $result;
    }
}

class Atherosclerosis1 extends Dependency {
    
    public function buildDependincies() {
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['pressure'] < $this->PRESSURE_AVERAGE[$month] and $weatherDaily['precipitation'] > 1 and $weatherDaily['humidity'] > $this->HUMIDITY_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };        
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 2, 1);
            $result[] = $this->getBodyFeeling($dt, 1, 10);
            $result[] = $this->getBodyFeeling($dt, 101, 10);            
            $result[] = $this->getBodyFeeling($dt, 13, 23);
            $result[] = $this->getBodyFeeling($dt, 14, 23);
            $result[] = $this->getBodyFeeling($dt, 110, 23);
            $result[] = $this->getBodyFeeling($dt, 111, 23);   
            
        };
        return $result;
    }
}

class Atherosclerosis2 extends Dependency {
    
    public function buildDependincies() {        
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['pressure'] < $this->PRESSURE_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };        
        $i = 0;
        foreach($this->mGeoPhysicsList as $geo){            
            $dt = $geo['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);
            $geonext = NULL;
            if(array_key_exists($i + 1, $this->mGeoPhysicsList)){
                $geonext = $this->mGeoPhysicsList[$i+1];
                $nextunixtime = $this->getRoundUnixTime($geonext['dt']);
                if($nextunixtime <> $unixtime){
                    $geonext = NULL;
                }
            }
            if($geo['kpid'] >= self::KPINDEX__ID_MEDIUM and $geo['kpid'] > $geonext['kpid']  ){                               
                if(array_key_exists($unixtime, $dictionary )){                    
                    $dictionary[$unixtime] = array("dt"=>$dt, "count" => 2);
                }
            };
            $i = $i + 1;
        };
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $count = $value["count"];
            if($count == 2){
                $result[] = $this->getBodyFeeling($dt, 2, 1);
                $result[] = $this->getBodyFeeling($dt, 13, 23);
                $result[] = $this->getBodyFeeling($dt, 14, 23);
                $result[] = $this->getBodyFeeling($dt, 110, 23);
                $result[] = $this->getBodyFeeling($dt, 111, 23);
            }
        };
        return $result;
    }
}

class Bronchit1 extends Dependency {
    
    public function buildDependincies() {
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['temp'] <= $this->TEMPERATURES_AVERAGE[$month] and $weatherDaily['windspeed'] >= $this->WINDSPEEDS_AVERAGE[$month] and $weatherDaily['humidity'] >= $this->HUMIDITY_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };        
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 20, 30);
            $result[] = $this->getBodyFeeling($dt, 21, 30);
            $result[] = $this->getBodyFeeling($dt, 24, 30);            
            $result[] = $this->getBodyFeeling($dt, 20, 18);
            $result[] = $this->getBodyFeeling($dt, 21, 18);
            $result[] = $this->getBodyFeeling($dt, 24, 18);            
        };
        return $result;
    }
}

class Bronchit2 extends Dependency {
    
    public function buildDependincies() {
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['temp'] <= $this->TEMPERATURES_AVERAGE[$month] and $weatherDaily['windspeed'] >= $this->WINDSPEEDS_AVERAGE[$month] and $weatherDaily['humidity'] >= $this->HUMIDITY_AVERAGE[$month] and $weatherDaily['pressure'] >= $this->PRESSURE_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };        
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 20, 30);
            $result[] = $this->getBodyFeeling($dt, 21, 30);
            $result[] = $this->getBodyFeeling($dt, 24, 30); 
            $result[] = $this->getBodyFeeling($dt, 20, 29);
            $result[] = $this->getBodyFeeling($dt, 21, 29);
            $result[] = $this->getBodyFeeling($dt, 24, 29);
            $result[] = $this->getBodyFeeling($dt, 1, 4);
            $result[] = $this->getBodyFeeling($dt, 101, 4);
            $result[] = $this->getBodyFeeling($dt, 2, 4);
            $result[] = $this->getBodyFeeling($dt, 3, 4); 
            $result[] = $this->getBodyFeeling($dt, 4, 4);
            $result[] = $this->getBodyFeeling($dt, 102, 4);
            $result[] = $this->getBodyFeeling($dt, 103, 4);
            $result[] = $this->getBodyFeeling($dt, 104, 4);
            $result[] = $this->getBodyFeeling($dt, 105, 4);                
            
        };
        return $result;
    }
}

class Bronchit3 extends Dependency {
    
    public function buildDependincies() {
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['pressure'] >= $this->PRESSURE_AVERAGE[$month] and $weatherDaily['temp'] >= $this->TEMPERATURES_AVERAGE[$month] and $weatherDaily['humidity'] <= $this->HUMIDITY_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };        
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 20, 29);
            $result[] = $this->getBodyFeeling($dt, 21, 29);
            $result[] = $this->getBodyFeeling($dt, 24, 29);            
            $result[] = $this->getBodyFeeling($dt, 20, 25);
            $result[] = $this->getBodyFeeling($dt, 21, 25);
            $result[] = $this->getBodyFeeling($dt, 24, 25);
            $result[] = $this->getBodyFeeling($dt, 2, 5);   
            
        };
        return $result;
    }
}

class Osteochondrosis1 extends Dependency {
    
    public function buildDependincies() {
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['pressure'] >= $this->PRESSURE_AVERAGE[$month] and $weatherDaily['temp'] <= $this->TEMPERATURES_AVERAGE[$month] and $weatherDaily['windspeed'] >= $this->WINDSPEEDS_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };        
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 122, 5);
            $result[] = $this->getBodyFeeling($dt, 123, 5);
            $result[] = $this->getBodyFeeling($dt, 129, 6);            
            $result[] = $this->getBodyFeeling($dt, 130, 6);
        };
        return $result;
    }
}

class Radiculitis1 extends Dependency {
    
    public function buildDependincies() {
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['windspeed'] >= $this->WINDSPEEDS_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };        
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 122, 5);
            $result[] = $this->getBodyFeeling($dt, 123, 5);
            $result[] = $this->getBodyFeeling($dt, 115, 5);
        };
        return $result;
    }
}

class Arthritis1 extends Dependency {
    
    public function buildDependincies() {
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['humidity'] >= $this->HUMIDITY_AVERAGE[$month] and $weatherDaily['temp'] <= $this->TEMPERATURES_AVERAGE[$month] and $weatherDaily['windspeed'] >= $this->WINDSPEEDS_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };        
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 34, 5);
            $result[] = $this->getBodyFeeling($dt, 35, 34);
            $result[] = $this->getBodyFeeling($dt, 40, 17);
            $result[] = $this->getBodyFeeling($dt, 41, 7);
        };
        return $result;
    }
}

class Arthritis2 extends Dependency {
    
    public function buildDependincies() {
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['humidity'] >= $this->HUMIDITY_AVERAGE[$month] and $weatherDaily['temp'] <= $this->TEMPERATURES_AVERAGE[$month] and $weatherDaily['windspeed'] >= $this->WINDSPEEDS_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };        
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 52, 5);
            $result[] = $this->getBodyFeeling($dt, 53, 34);
            $result[] = $this->getBodyFeeling($dt, 58, 17);
            $result[] = $this->getBodyFeeling($dt, 59, 7);
        };
        return $result;
    }
}

class Pyelonephritis1 extends Dependency {
    
    public function buildDependincies() {
        $dictionary = array();
        $result = array();        
        foreach($this->mWeatherDailyList as $weatherDaily){            
            $dt = $weatherDaily['dt'];            
            $unixtime = $this->getRoundUnixTime($dt);            
            $month = $this->getMonth($unixtime);            
            if($weatherDaily['pressure'] <= $this->PRESSURE_AVERAGE[$month] and $weatherDaily['temp'] <= $this->TEMPERATURES_AVERAGE[$month] and $weatherDaily['humidity'] >= $this->HUMIDITY_AVERAGE[$month]){
                $dictionary[$unixtime] = array("dt"=>$dt, "count" => 1);
            };
        };        
        
        foreach($dictionary as $key => $value){            
            $dt = $value["dt"];
            $result[] = $this->getBodyFeeling($dt, 2, 5);
            $result[] = $this->getBodyFeeling($dt, 122, 7);
            $result[] = $this->getBodyFeeling($dt, 123, 7);
            $result[] = $this->getBodyFeeling($dt, 42, 17);
            $result[] = $this->getBodyFeeling($dt, 43, 17);
        };
        return $result;
    }
}

class DependencySet {

    protected $mDependencyMap = array();
    
    public function DependencySet() {
        $dependencyGroup = new DependencyGroup(1, "гипертоническая болезнь 1", "Дневник1");
        $dependencyList = array(new HypertensionDependency1());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(2, "гипертоническая болезнь 2", "Дневник2");
        $dependencyList = array(new HypertensionDependency2());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(3, "гипертоническая болезнь 3", "Дневник3");
        $dependencyList = array(new HypertensionDependency3());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(4, "гипертоническая болезнь 4", "Дневник4");
        $dependencyList = array(new HypertensionDependency4());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(5, "атеросклероз сосудов головного мозга 1", "Дневник5");
        $dependencyList = array(new Atherosclerosis1());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(6, "атеросклероз сосудов головного мозга 2", "Дневник6");
        $dependencyList = array(new Atherosclerosis2());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(7, "бронхолёгочные болезни 1", "Дневник7");
        $dependencyList = array(new Bronchit1());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(8, "бронхолёгочные болезни 2", "Дневник8");
        $dependencyList = array(new Bronchit2());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(9, "бронхолёгочные болезни 3", "Дневник9");
        $dependencyList = array(new Bronchit3());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(10, "остеохондроз", "Дневник10");
        $dependencyList = array(new Osteochondrosis1());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;  
        
        $dependencyGroup = new DependencyGroup(11, "радикулит", "Дневник11");
        $dependencyList = array(new Radiculitis1());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(12, "артрит 1", "Дневник12");
        $dependencyList = array(new Arthritis1());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(13, "артрит 2", "Дневник13");
        $dependencyList = array(new Arthritis2());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
        $dependencyGroup = new DependencyGroup(14, "пиелонефрит", "Дневник14");
        $dependencyList = array(new Pyelonephritis1());        
        $this->mDependencyMap[$dependencyGroup->getId()] = $dependencyList;
        
    }

    

    public function generateDiaryFeeling($dependencyGroupId, $dtFrom, $dtTo, $weatherDailyList, $particleList, $geoPhysicsList){
        $result = array();        
        $dependencies = $this->mDependencyMap[$dependencyGroupId];
        if(!is_null($dependencies)){
            foreach ($dependencies as $d) {
                $d->setParameterData($weatherDailyList, $particleList, $geoPhysicsList);                
                $r = $d->buildDependincies();
                $result = array_merge($result, $r);                
            }
        }
        return $result;
    }
}

?>