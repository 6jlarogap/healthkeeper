<?php
include('../global.php'); 
include('../libhttp.php');
if (isset($_GET['id'])) {
    $cityid = $_GET['id'];
}
$isHist = false;
if (isset($_GET['datefrom'])) {
    $dtUnixFrom = $_GET['datefrom'];
    $isHist = true;
} 
if (isset($_GET['dateto'])) {
    $dtUnixTo = $_GET['dateto'];    
} else {
    $dtUnixTo = time();
}

global $dbName, $hostname, $username, $password;
$deltaKelvinCelsius = 273.15;
set_time_limit(60 * 20); 

function saveWeatherData($connection, $cityid, $dt, $temp, $humidity, $pressure, $windspeed, $winddeg, $clouds, $rain, $snow, $temp_min, $temp_max, $isdaily, $temp_morning, $temp_day, $temp_evening, $temp_night)
{
    //echo "_saveWeatherData:cityid=".$cityid."temp=".$temp."dt=".$dt;
    //проверка на корректность данных
    if($isdaily !== 1){
        if($temp < -100){
            return;
        }
    }
    if($isdaily == 1){
        $insertStatement = $connection->prepare("insert into tblweatherdaily1 (cityid, dt, humidity, pressure, windspeed, winddeg, clouds, rain, snow, temp_min, temp_max, temp_morning, temp_day, temp_evening, temp_night) values(:cityid, :dt, :humidity, :pressure, :windspeed, :winddeg, :clouds, :rain, :snow, :temp_min, :temp_max, :temp_morning, :temp_day, :temp_evening, :temp_night);");
        $updateStatement = $connection->prepare("update tblweatherdaily1 set cityid = :cityid, dt = :dt, humidity = :humidity, pressure = :pressure, windspeed = :windspeed, winddeg = :winddeg, clouds = :clouds, rain = :rain, snow = :snow, temp_min = :temp_min, temp_max = :temp_max, temp_morning = :temp_morning, temp_day = :temp_day, temp_evening = :temp_evening, temp_night = :temp_night where id = :id;");
                
        $sqlSelectWeather = "SELECT max(id) FROM tblweatherdaily1 where cityid=".$cityid." and dt='".$dt."';"; 
    } else {
        $insertStatement = $connection->prepare("insert into tblweatherhourly1 (cityid, dt, temp, humidity, pressure, windspeed, winddeg, clouds, rain, snow, temp_min, temp_max) values(:cityid, :dt, :temp, :humidity, :pressure, :windspeed, :winddeg, :clouds, :rain, :snow, :temp_min, :temp_max);");
        $updateStatement = $connection->prepare("update tblweatherhourly1 set cityid = :cityid, dt = :dt, temp = :temp, humidity = :humidity, pressure = :pressure, windspeed = :windspeed, winddeg = :winddeg, clouds = :clouds, rain = :rain, snow = :snow, temp_min = :temp_min, temp_max = :temp_max where id = :id;");
                
        $sqlSelectWeather = "SELECT max(id) FROM tblweatherhourly1 where cityid=".$cityid." and dt='".$dt."';"; 
    }
	       
    $query = $connection->query($sqlSelectWeather);
    $row = $query->fetch();
    $id =  $row[0];
    
    if(!isset($id)){
        $insertStatement->bindParam(':cityid', $cityid, PDO::PARAM_INT);
        $insertStatement->bindParam(':dt', $dt, PDO::PARAM_STR);
        $insertStatement->bindParam(':humidity', $humidity, PDO::PARAM_INT);
        $insertStatement->bindParam(':pressure', $pressure, PDO::PARAM_INT);
        $insertStatement->bindParam(':windspeed', $windspeed, PDO::PARAM_INT);
        $insertStatement->bindParam(':winddeg', $winddeg, PDO::PARAM_INT);
        $insertStatement->bindParam(':clouds', $clouds, PDO::PARAM_INT);
        $insertStatement->bindParam(':rain', $rain, PDO::PARAM_INT);
        $insertStatement->bindParam(':snow', $snow, PDO::PARAM_INT);
        $insertStatement->bindParam(':temp_min', $temp_min, PDO::PARAM_INT);
        $insertStatement->bindParam(':temp_max', $temp_max, PDO::PARAM_INT);
        
        if($isdaily == 1){
            $insertStatement->bindParam(':temp_morning', $temp_morning, PDO::PARAM_INT);
            $insertStatement->bindParam(':temp_day', $temp_day, PDO::PARAM_INT);
            $insertStatement->bindParam(':temp_evening', $temp_evening, PDO::PARAM_INT);
            $insertStatement->bindParam(':temp_night', $temp_night, PDO::PARAM_INT);
        } else {
            $insertStatement->bindParam(':temp', $temp, PDO::PARAM_INT);
        }
        
        $result = $insertStatement->execute();
        $weatherid = $connection->lastInsertId();
    } else {
        $weatherid = $id;
        $updateStatement->bindParam(':id', $id, PDO::PARAM_INT);
        $updateStatement->bindParam(':cityid', $cityid, PDO::PARAM_INT);
        $updateStatement->bindParam(':dt', $dt, PDO::PARAM_STR);        
        $updateStatement->bindParam(':humidity', $humidity, PDO::PARAM_INT);
        $updateStatement->bindParam(':pressure', $pressure, PDO::PARAM_INT);
        $updateStatement->bindParam(':windspeed', $windspeed, PDO::PARAM_INT);
        $updateStatement->bindParam(':winddeg', $winddeg, PDO::PARAM_INT);
        $updateStatement->bindParam(':clouds', $clouds, PDO::PARAM_INT);
        $updateStatement->bindParam(':rain', $rain, PDO::PARAM_INT);
        $updateStatement->bindParam(':snow', $snow, PDO::PARAM_INT);
        $updateStatement->bindParam(':temp_min', $temp_min, PDO::PARAM_INT);
        $updateStatement->bindParam(':temp_max', $temp_max, PDO::PARAM_INT);
        
        if($isdaily == 1){
            $updateStatement->bindParam(':temp_morning', $temp_morning, PDO::PARAM_INT);
            $updateStatement->bindParam(':temp_day', $temp_day, PDO::PARAM_INT);
            $updateStatement->bindParam(':temp_evening', $temp_evening, PDO::PARAM_INT);
            $updateStatement->bindParam(':temp_night', $temp_night, PDO::PARAM_INT);
        } else {
            $updateStatement->bindParam(':temp', $temp, PDO::PARAM_INT);
        }
        $result = $updateStatement->execute();        
    }
}

$arrayCity = array();
if(!isset($cityid)){
    $connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
    $sql = 'SELECT id, name, name_ru from tblcity c where c.name_ru is not null order by id';    
    foreach ($connection->query($sql) as $row) {    
        $arrayCity[] = array('id' => $row['id'], 'name' => $row['name'], 'name_ru' => $row['name_ru']);
    }
} else {
    $arrayCity[] = array('id' => $cityid, 'name' => 'name', 'name_ru' => 'name_ru');
}

foreach ($arrayCity as $city) {
    $cityid = $city['id'];
    $cityname = $city['name'];
    try {
        
        $connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
        $connection->exec("set names utf8");
        $connection->exec("set time_zone='+00:00'"); 
        $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);        
        $connection->beginTransaction();
        #погода на определенное время
        sleep(2);
        if($isHist === false){
            $json = getHtmlPage('http://api.openweathermap.org/data/2.5/forecast?id='.$cityid, $prox);
        } else {
            $json = getHtmlPage('http://api.openweathermap.org/data/2.5/history/city?id='.$cityid.'&type=hour&start='.$dtUnixFrom.'&end='.$dtUnixTo, $prox);            
        }
        $arrayData = json_decode($json, True);
        //echo $json;
        $arrayWeather = $arrayData['list'];
        foreach ($arrayWeather as $w) {
            $main_w = $w['main'];
            $weather_w = $w['weather'];
            $clouds_w = $w['clouds'];
            $wind_w = $w['wind'];        
            $unixTime = $w['dt'];            
            if($isHist === true){
                $unixTime = floor($unixTime/(3* 60 * 60)) * (3* 60 * 60);
                $txtTime = gmdate("Y-m-d H:i:s", $unixTime);                
            } else {
                $txtTime = $w['dt_txt'];
            }
            $isdaily = 0;        
            $rain = NULL;
            $snow = NULL; 
            if (isset($w['rain'])){
                $rain_w = $w['rain'];
                $rain = $rain_w["3h"];
            }
            if(isset($w['snow'])){
                $snow_w = $w['snow'];
                $snow = $snow_w["3h"]; 
            }        
            $humidity = $main_w['humidity'];
            $pressure = $main_w['pressure'];
            $windspeed = $wind_w['speed'];
            $winddeg = $wind_w['deg'];
            $clouds = $clouds_w['all'];        
            $temp = $main_w['temp'] - $deltaKelvinCelsius;
            $temp_min = $main_w['temp_min'] - $deltaKelvinCelsius;
            $temp_max = $main_w['temp_max'] - $deltaKelvinCelsius;
            $isdaily = 0;
            $temp_morning = NULL;
            $temp_day = NULL;
            $temp_evening = NULL;
            $temp_night = NULL;
            saveWeatherData($connection, $cityid, $txtTime, $temp, $humidity, $pressure, $windspeed, $winddeg, $clouds, $rain, $snow, $temp_min, $temp_max, $isdaily, $temp_morning, $temp_day, $temp_evening, $temp_night);
        }
        $connection->commit();
        echo "Save weather success(cityid=".$cityid.", cityname=".$cityname.")<br/>";
        
        if($isHist === false){
            #погода на весь день 
            sleep(2);
            $jsonDaily = getHtmlPage("http://api.openweathermap.org/data/2.5/forecast/daily?id=".$cityid."&mode=json&cnt=14", $prox);
            $arrayDataDaily = json_decode($jsonDaily, True);
            //echo $jsonDaily;
            $connection->beginTransaction();        
            $arrayWeather = $arrayDataDaily['list'];
            foreach ($arrayWeather as $w) {
                $temp_w = $w['temp'];
                $weather_w = $w['weather'];              
                $unixTime = $w['dt'];
                $txtTime = getDateFromUnixTime($unixTime);       
                $rain = NULL;
                $snow = NULL; 
                if (isset($w['rain'])){
                    $rain = $w['rain'];            
                }
                if(isset($w['snow'])){
                    $snow = $w['snow'];            
                }        
                $humidity = $w['humidity'];
                $pressure = $w['pressure'];
                $windspeed = $w['speed'];
                $winddeg = $w['deg'];
                $clouds = $w['clouds'];     
                $temp = NULL;
                $temp_min = $temp_w['min'] - $deltaKelvinCelsius;
                $temp_max = $temp_w['max'] - $deltaKelvinCelsius;
                $isdaily = 1;
                $temp_morning = $temp_w['morn'] - $deltaKelvinCelsius;
                $temp_day = $temp_w['day'] - $deltaKelvinCelsius;
                $temp_evening = $temp_w['eve'] - $deltaKelvinCelsius;
                $temp_night = $temp_w['night'] - $deltaKelvinCelsius;
                saveWeatherData($connection, $cityid, $txtTime, $temp, $humidity, $pressure, $windspeed, $winddeg, $clouds, $rain, $snow, $temp_min, $temp_max, $isdaily, $temp_morning, $temp_day, $temp_evening, $temp_night);
            }
            $connection->commit();
            echo "Save daily weather success(cityid=".$cityid.", cityname=".$cityname.")<br/>";
        }   
        
    } catch (Exception $e) {
        echo "Error:cityid=".$cityid.",name=".$cityname;
        echo "#######";
        echo $json;
        echo "#######";
        echo $jsonDaily;
        echo "=getCode=" . $e->getCode();
        echo "Exception" . $e->getMessage();
        $connection->rollback();
    }
}
echo "End";

?>