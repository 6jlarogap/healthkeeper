<?php

header('Content-Type: application/json; charset=utf-8');
include('global.php'); 

define("PRESSURE_TYPE",1001,false);
define("TEMPERATURE_TYPE",1002,false);
define("HUMIDITY_TYPE",1003,false);
define("CLOUD_TYPE",1004,false);
define("PRECIPITATION_TYPE",1005,false);
define("WINDSPEED_TYPE",1006,false);

define("SUN_TYPE",2001,false);

define("MOON_VISIBILITY_TYPE",3001,false);
define("MOON_OLD_TYPE",3002,false);
define("MOON_TYPE",3003,false);

define("KP_TYPE",4001,false);
define("AP_TYPE",4002,false);

define("F10_7_TYPE",5001,false);
define("SUNSPOT_NUMBER_TYPE",5002,false);
define("SUNSPOT_AREA_TYPE",5003,false);
define("NEW_REGIONS_TYPE",5004,false);
define("FLARES_XRAY_TYPE",5005,false);
define("FLARES_OPTICAL_TYPE",5006,false);
define("XBKGD_TYPE",5007,false);

define("PROTON_1MEV_TYPE",6001,false);
define("PROTON_10MEV_TYPE",6002,false);
define("PROTON_100MEV_TYPE",6003,false);
define("ELECTRON_08MEV_TYPE",6004,false);
define("ELECTRON_2MEV_TYPE",6005,false);

$mm_Hg = 101325 / 760;
$hfs = 1;
$lat = 53.9;
$lng = 27.5667;
$cityid = 625144;
$id = PRESSURE_TYPE;
$minVal = null;
$maxVal = null;
if (isset($_GET['id'])) {
    $id = $_GET['id'];
}
if (isset($_GET['cityid'])) {
    $cityid = $_GET['cityid'];
}
if (isset($_GET['hfs'])) {
    $hfs = $_GET['hfs'];
}
if (isset($_GET['minVal'])) {
    $minVal = $_GET['minVal'];
}
if (isset($_GET['maxVal'])) {
    $maxVal = $_GET['maxVal'];
}
if (isset($_GET['lat'])) {
    $lat = $_GET['lat'];
}
if (isset($_GET['lng'])) {
    $lng = $_GET['lng'];
}
if (isset($_GET['datefrom'])) {
    $dtUnixFrom = $_GET['datefrom'];    
} else {
    $dtUnixFrom = time() - 30*24*60*60;    
}
if (isset($_GET['dateto'])) {
    $dtUnixTo = $_GET['dateto'];    
} else {
    $dtUnixTo = $dtUnixFrom + 60*24*60*60;
}
$dtFrom = getBeginDateFromUnixTime($dtUnixFrom);
$dtTo = getNextDateFromUnixTime($dtUnixTo);

$con=mysqli_connect($hostname,$username,$password,$dbName);
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$list = array();

switch ($id) {
    case PRESSURE_TYPE:
        $result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR((pressure*100/$mm_Hg)/$hfs)*$hfs as value FROM tblweatherdaily where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' and (pressure*100/$mm_Hg) >= ".$minVal." and (pressure*100/$mm_Hg) <= ".$maxVal." GROUP BY FLOOR((pressure*100/$mm_Hg)/$hfs)*$hfs ORDER BY FLOOR((pressure*100/$mm_Hg)/$hfs)*$hfs");
        break;
    case TEMPERATURE_TYPE:
        $result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(temp_day/$hfs)*$hfs as value FROM tblweatherdaily where not isNull(temp_day/$hfs)*$hfs and cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' and temp_day >= ".$minVal." and temp_day <= ".$maxVal." GROUP BY FLOOR(temp_day/$hfs)*$hfs ORDER BY FLOOR(temp_day/$hfs)*$hfs");
        break;
    case HUMIDITY_TYPE:
        $result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(humidity/$hfs)*$hfs as value FROM tblweatherdaily where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' and humidity >= ".$minVal." and humidity <= ".$maxVal." GROUP BY FLOOR(humidity/$hfs)*$hfs ORDER BY FLOOR(humidity/$hfs)*$hfs");
        break;
    case CLOUD_TYPE:
        $result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(clouds/$hfs)*$hfs as value FROM tblweatherdaily where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' and clouds >= ".$minVal." and clouds <= ".$maxVal." GROUP BY FLOOR(clouds/$hfs)*$hfs ORDER BY FLOOR(clouds/$hfs)*$hfs");
        break;
    case PRECIPITATION_TYPE:
        $result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR((IFNULL(snow,0) + IFNULL(rain,0))/$hfs)*$hfs as value FROM tblweatherdaily where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' and (IFNULL(snow,0) + IFNULL(rain,0)) >= ".$minVal." and (IFNULL(snow,0) + IFNULL(rain,0)) <= ".$maxVal." GROUP BY FLOOR((IFNULL(snow,0) + IFNULL(rain,0))/$hfs)*$hfs ORDER BY FLOOR((IFNULL(snow,0) + IFNULL(rain,0))/$hfs)*$hfs");
        break;
    case WINDSPEED_TYPE:
        $result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(windspeed/$hfs)*$hfs as value FROM tblweatherdaily where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' and windspeed >= ".$minVal." and windspeed <= ".$maxVal." GROUP BY FLOOR(windspeed/$hfs)*$hfs ORDER BY FLOOR(windspeed/$hfs)*$hfs");
        break;
	case KP_TYPE:
		$result = mysqli_query($con,"SELECT COUNT(*) AS count , kpid as value FROM tblgeophysicshourly where dt>='".$dtFrom."' and dt<='".$dtTo."' GROUP BY kpid ORDER BY kpid");
        break;
	case AP_TYPE:
		$result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(ap/$hfs)*$hfs as value FROM tblgeophysicsdaily where dt>='".$dtFrom."' and dt<='".$dtTo."' and ap >= ".$minVal." and ap <= ".$maxVal." GROUP BY FLOOR(ap/$hfs)*$hfs ORDER BY FLOOR(ap/$hfs)*$hfs");
        break;
	case F10_7_TYPE:
		$result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(f10_7/$hfs)*$hfs as value FROM tblheliophysicsdaily where dt>='".$dtFrom."' and dt<='".$dtTo."' and f10_7 >= ".$minVal." and f10_7 <= ".$maxVal." GROUP BY FLOOR(f10_7/$hfs)*$hfs ORDER BY FLOOR(f10_7/$hfs)*$hfs");
        break;
	case SUNSPOT_NUMBER_TYPE:
		$result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(sunspot_number/$hfs)*$hfs as value FROM tblheliophysicsdaily where not isNull(sunspot_number) and dt>='".$dtFrom."' and dt<='".$dtTo."' and sunspot_number >= ".$minVal." and sunspot_number <= ".$maxVal." GROUP BY FLOOR(sunspot_number/$hfs)*$hfs ORDER BY FLOOR(sunspot_number/$hfs)*$hfs");
        break;
	case SUNSPOT_AREA_TYPE:
		$result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(sunspot_area/$hfs)*$hfs as value FROM tblheliophysicsdaily where not isNull(sunspot_area) and dt>='".$dtFrom."' and dt<='".$dtTo."' and sunspot_area >= ".$minVal." and sunspot_area <= ".$maxVal." GROUP BY FLOOR(sunspot_area/$hfs)*$hfs ORDER BY FLOOR(sunspot_area/$hfs)*$hfs");
        break;
	case NEW_REGIONS_TYPE:
		$result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(new_regions/$hfs)*$hfs as value FROM tblheliophysicsdaily where not isNull(new_regions) and dt>='".$dtFrom."' and dt<='".$dtTo."' and new_regions >= ".$minVal." and new_regions <= ".$maxVal." GROUP BY FLOOR(new_regions/$hfs)*$hfs ORDER BY FLOOR(new_regions/$hfs)*$hfs");
        break;
    case PROTON_1MEV_TYPE:
		$result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(proton1mev/$hfs)*$hfs as value FROM tblparticledaily where dt>='".$dtFrom."' and dt<='".$dtTo."' and proton1mev >= ".$minVal." and proton1mev <= ".$maxVal." GROUP BY FLOOR(proton1mev/$hfs)*$hfs ORDER BY FLOOR(proton1mev/$hfs)*$hfs");
        break;
    case PROTON_10MEV_TYPE:
        $result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(proton10mev/$hfs)*$hfs as value FROM tblparticledaily where  dt>='".$dtFrom."' and dt<='".$dtTo."' and proton10mev >= ".$minVal." and proton10mev <= ".$maxVal." GROUP BY FLOOR(proton10mev/$hfs)*$hfs ORDER BY FLOOR(proton10mev/$hfs)*$hfs");
        break;
    case PROTON_100MEV_TYPE:
        $result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(proton100mev/$hfs)*$hfs as value FROM tblparticledaily where dt>='".$dtFrom."' and dt<='".$dtTo."' and proton100mev >= ".$minVal." and proton100mev <= ".$maxVal." GROUP BY FLOOR(proton100mev/$hfs)*$hfs ORDER BY FLOOR(proton100mev/$hfs)*$hfs");
        break;
    case ELECTRON_08MEV_TYPE:
        $result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(electron08mev/$hfs)*$hfs as value FROM tblparticledaily where dt>='".$dtFrom."' and dt<='".$dtTo."' and electron08mev >= ".$minVal." and electron08mev <= ".$maxVal." GROUP BY FLOOR(electron08mev/$hfs)*$hfs ORDER BY FLOOR(electron08mev/$hfs)*$hfs");
        break;
    case ELECTRON_2MEV_TYPE:
        $result = mysqli_query($con,"SELECT COUNT(*) AS count , FLOOR(electron2mev/$hfs)*$hfs as value FROM tblparticledaily where dt>='".$dtFrom."' and dt<='".$dtTo."' and electron2mev >= ".$minVal." and electron2mev <= ".$maxVal." GROUP BY FLOOR(electron2mev/$hfs)*$hfs ORDER BY FLOOR(electron2mev/$hfs)*$hfs");
        break;
}

if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("cnt" => $row['count'], "val" => $row['value']);
   $list[] = $partData;   
}
$json = json_encode($list);
echo $json;
?>