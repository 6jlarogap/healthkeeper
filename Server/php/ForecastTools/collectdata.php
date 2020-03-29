<?php

require_once 'lib/Forecast.php';
$api_key = '57f81dbdba988ee0a0526bca25475740';

$latitude  = 53.9;
$longitude = 27.5667;
include('../global.php'); 
include('../libhttp.php');
if (isset($_GET['id'])) {
    $cityid = $_GET['id'];
}

if (isset($_GET['datefrom'])) {
    $dtUnixFrom = $_GET['datefrom'];    
} 
if (isset($_GET['dateto'])) {
    $dtUnixTo = $_GET['dateto'];    
} else {
    $dtUnixTo = time();
}

global $dbName, $hostname, $username, $password;
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
        $insertStatement = $connection->prepare("insert into tblweatherdaily (cityid, dt, humidity, pressure, windspeed, winddeg, clouds, rain, snow, temp_min, temp_max, temp_morning, temp_day, temp_evening, temp_night) values(:cityid, :dt, :humidity, :pressure, :windspeed, :winddeg, :clouds, :rain, :snow, :temp_min, :temp_max, :temp_morning, :temp_day, :temp_evening, :temp_night);");
        $updateStatement = $connection->prepare("update tblweatherdaily set cityid = :cityid, dt = :dt, humidity = :humidity, pressure = :pressure, windspeed = :windspeed, winddeg = :winddeg, clouds = :clouds, rain = :rain, snow = :snow, temp_min = :temp_min, temp_max = :temp_max, temp_morning = :temp_morning, temp_day = :temp_day, temp_evening = :temp_evening, temp_night = :temp_night where id = :id;");
        $insertDetailWeatherStatement = $connection->prepare("insert into tbldetailweatherdaily (weatherid, typeid) values(:weatherid, :typeid);");
        $deleteDetailWeatherStatement = $connection->prepare("delete from tbldetailweatherdaily where weatherid = :weatherid;");
        
        $sqlSelectWeather = "SELECT max(id) FROM tblweatherdaily where cityid=".$cityid." and dt='".$dt."';"; 
    } else {
        $insertStatement = $connection->prepare("insert into tblweatherhourly (cityid, dt, temp, humidity, pressure, windspeed, winddeg, clouds, rain, snow, temp_min, temp_max) values(:cityid, :dt, :temp, :humidity, :pressure, :windspeed, :winddeg, :clouds, :rain, :snow, :temp_min, :temp_max);");
        $updateStatement = $connection->prepare("update tblweatherhourly set cityid = :cityid, dt = :dt, temp = :temp, humidity = :humidity, pressure = :pressure, windspeed = :windspeed, winddeg = :winddeg, clouds = :clouds, rain = :rain, snow = :snow, temp_min = :temp_min, temp_max = :temp_max where id = :id;");
        $insertDetailWeatherStatement = $connection->prepare("insert into tbldetailweatherhourly (weatherid, typeid) values(:weatherid, :typeid);");
        $deleteDetailWeatherStatement = $connection->prepare("delete from tbldetailweatherhourly where weatherid = :weatherid;");
        
        $sqlSelectWeather = "SELECT max(id) FROM tblweatherhourly where cityid=".$cityid." and dt='".$dt."';"; 
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
    $sql = 'SELECT id, name, name_ru, lat, lng from tblcity c where c.name_ru is not null order by id';    
    foreach ($connection->query($sql) as $row) {    
        $arrayCity[] = array('id' => $row['id'], 'lat' => $row['lat'], 'lng' => $row['lng'], 'name' => $row['name'], 'name_ru' => $row['name_ru']);
    }
} else {
    $connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
    $sql = 'SELECT id, name, name_ru, lat, lng from tblcity c where c.id ='.$cityid.' order by id';    
    foreach ($connection->query($sql) as $row) {    
        $arrayCity[] = array('id' => $row['id'], 'lat' => $row['lat'], 'lng' => $row['lng'], 'name' => $row['name'], 'name_ru' => $row['name_ru']);
    }
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
        // Make request to the API for the current forecast
        $forecast  = new Forecast($api_key);
        $response = $forecast->getData($city['lat'], $city['lng']);
        //$currently = $response->getCurrently();
        $hourly = $response->getHourly();
        if($response->getCount('hourly') > 0)
        {            
            for ($i = 0; $i < count($hourly); $i++) 
            {
                $data = $hourly[$i];
                $unixTime = $data->getTime();
                $precip_type = $data->getPrecipType();
                $precip_intensity = $data->getPrecipIntensity(); // inches per hour                
                $precip = $precip_intensity * 25.4; // mm per hour                
                $txtTime = gmdate("Y-m-d H:i:s", $data->getTime());
                $temp = $data->getTemperature(); //Fahrenheit
                $temp = ($temp - 32) * 5 / 9;  //Celsius
                $temp = number_format($temp, 2);
                $windspeed = $data->getWindSpeed(); //miles per hour
                $windspeed = number_format($windspeed * 0.44704, 2); //meters per second        
                $windbearing = $data->getWindBearing();
                $cloud = $data->getCloudCover();
                $cloud = $cloud * 100; //%
                $humidity = $data->getHumidity();
                $humidity = $humidity * 100; //%
                $pressure = $data->getPressure();
                $json="{time=".$txtTime.", temp=".$temp.", precip_type=".$precip_type.", precip=".$precip.", windspeed=".$windspeed.", windbearing=".$windbearing.", cloud=".$cloud.", humidity=".$humidity.", pressure=".$pressure."}";
                $isdaily = 0;        
                $rain = NULL;
                $snow = NULL;
                if($precip_type)
                {
                    if($precip == 0){
                        $precip == NULL;
                    } 
                    else 
                    {
                        $precip = number_format($precip, 2);
                    }
                    if($precip_type == "rain"){
                        $rain = $precip;
                    } 
                    else {
                        $snow = $precip;
                    }
                }                
                $temp_min = NULL;
                $temp_max = NULL;
                $temp_morning = NULL;
                $temp_day = NULL;
                $temp_evening = NULL;
                $temp_night = NULL;
                saveWeatherData($connection, $cityid, $txtTime, $temp, $humidity, $pressure, $windspeed, $windbearing, $cloud, $rain, $snow, $temp_min, $temp_max, $isdaily, $temp_morning, $temp_day, $temp_evening, $temp_night);                
                echo $json;
                echo "</br>";                
            }
            $connection->commit();
            echo "Save hourly weather success(cityid=".$cityid.", cityname=".$cityname.")<br/>";
        } 
        else 
        {
            echo "Hourly data not find";
        }
        
        $connection->beginTransaction();
        $daily = $response->getDaily();
        if($response->getCount('daily') > 0)
        {            
            for ($i = 0; $i < count($daily); $i++) 
            {
                $data = $daily[$i];
                $unixTime = $data->getTime();
                $precip_type = $data->getPrecipType();
                $precip_intensity = $data->getPrecipIntensity(); // inches per hour     
                $precip = 24 * $precip_intensity * 25.4; // mm per day                
                $txtTime = gmdate("Y-m-d H:i:s", $data->getTime());
                $temp = $data->getTemperature(); //Fahrenheit
                $temp = ($temp - 32) * 5 / 9;  //Celsius
                $temp = number_format($temp, 2);
                $tempMax = $data->getTemperatureMax(); //Fahrenheit
                $tempMax = ($tempMax - 32) * 5 / 9;  //Celsius
                $tempMax = number_format($tempMax, 2);
                $tempMin = $data->getTemperatureMin(); //Fahrenheit
                $tempMin = ($tempMin - 32) * 5 / 9;  //Celsius
                $tempMin = number_format($tempMin, 2);
                $windspeed = $data->getWindSpeed(); //miles per hour
                $windspeed = number_format($windspeed * 0.44704, 2); //meters per second        
                $windbearing = $data->getWindBearing();
                $cloud = $data->getCloudCover();
                $cloud = $cloud * 100; //%
                $humidity = $data->getHumidity();
                $humidity = $humidity * 100; //%
                $pressure = $data->getPressure();
                $json="{time=".$txtTime.", temp=".$temp." tempMin = ".$tempMin." tempMax =".$tempMax." , precip_type=".$precip_type.", windspeed=".$windspeed.", windbearing=".$windbearing.", cloud=".$cloud.", humidity=".$humidity.", pressure=".$pressure."}";
                $isdaily = 1;        
                $rain = NULL;
                $snow = NULL;
                if($precip)
                {                    
                    if($precip_type == "rain"){
                        $rain = $precip;
                    } 
                    else {
                        $snow = $precip;
                    }
                }
                $temp_morning = NULL;
                $temp_day = NULL;
                $temp_evening = NULL;
                $temp_night = NULL;
                saveWeatherData($connection, $cityid, $txtTime, $temp, $humidity, $pressure, $windspeed, $windbearing, $cloud, $rain, $snow, $tempMin, $tempMax, $isdaily, $temp_morning, $temp_day, $temp_evening, $temp_night);                
                echo $json;
                echo "</br>";                
            }
            $connection->commit();
            echo "Save daily weather success(cityid=".$cityid.", cityname=".$cityname.")<br/>";
        } 
        else 
        {
            echo "Daily data not find";
        }
        
    } catch (Exception $e) {
        echo "Error:cityid=".$cityid.",name=".$cityname;             
        echo "=getCode=" . $e->getCode();
        echo "Exception" . $e->getMessage();
        $connection->rollback();
    }
}


/*
$forecast  = new Forecast($api_key);
$response = $forecast->getData($latitude, $longitude);
$currently = $response->getCurrently();
$hourly = $response->getHourly();

if($response->getCount('hourly') > 0)
{
    for ($i = 0; $i < count($hourly); $i++) 
    {
        $data = $hourly[$i];
        $time = date("Y-m-d H:i:s", $data->getTime());
        $temp = $data->getTemperature(); //Fahrenheit
        $temp = ($temp - 32) * 5 / 9;  //Celsius
        $precip_type = $data->getPrecipType();
        $wind_speed = $data->getWindSpeed(); //miles per hour
        $wind_speed = $wind_speed * 0.44704; //meters per second        
        $wind_bearing = $data->getWindBearing();
        $cloud = $data->getCloudCover();
        $cloud = $cloud * 100; //%
        $humidity = $data->getHumidity();
        $humidity = $humidity * 100; //%
        $pressure = $data->getPressure();
        $json="{time=".$time.", temp=".$temp.", precip_type=".$precip_type.", wind_speed=".$wind_speed.", wind_bearing=".$wind_bearing.", cloud=".$cloud.", humidity=".$humidity.", pressure=".$pressure."}";
        echo $json;
        echo "</br>";
    }
} 
else 
{
    echo "Hourly data not find";
}
*/



?>