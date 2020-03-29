<?php
header('Content-Type: application/json; charset=utf-8');
include('../global.php');
include('dependency.php');  

function get_timezone_offset($remote_tz, $origin_tz = null) {
    if($origin_tz === null) {
        if(!is_string($origin_tz = 'Europe/London')) {
            return false; // A UTC timestamp was returned -- bail out!
        }
    }
    $origin_dtz = new DateTimeZone($origin_tz);
    $remote_dtz = new DateTimeZone($remote_tz);
    $origin_dt = new DateTime("now", $origin_dtz);
    $remote_dt = new DateTime("now", $remote_dtz);
    $offset = $origin_dtz->getOffset($origin_dt) - $remote_dtz->getOffset($remote_dt);
    return $offset;
}

$lat = 53.9;
$lng = 27.5667;
$cityid = 625144;
$diary = 1;
if (isset($_GET['diary'])) {
    $diary = $_GET['diary'];
}
if (isset($_GET['cityid'])) {
    $cityid = $_GET['cityid'];
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
    $dtUnixFrom = time() - 90*24*60*60;    
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
$allData = array();

$allWeatherDailyData = array();
$result = mysqli_query($con,"SELECT * from tblweatherdaily where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "cityid" => $row['cityid'], "dt" => $row['dt'], "humidity" => $row['humidity'], "pressure" => $row['pressure'], "windspeed" => $row['windspeed'], "winddeg" => $row['winddeg'], "clouds" => $row['clouds'], "rain" => $row['rain'], "snow" => $row['snow'], "temp_min" => $row['temp_min'], "temp_max" => $row['temp_max'], "precipitation" => ($row['rain'] + $row['snow']), "temp" =>($row['temp_min'] + $row['temp_max']));
   $allWeatherDailyData[] = $partData;   
}

$allGeophysicsHourlyData = array();
$result = mysqli_query($con,"SELECT * from tblgeophysicshourly where dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "dt" => $row['dt'], "kpid" => $row['kpid'], "ap" => $row['ap']);
   $allGeophysicsHourlyData[] = $partData;   
}

$allGeophysicsDailyData = array();
$result = mysqli_query($con,"SELECT * from tblgeophysicsdaily where dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "dt" => $row['dt'], "kp" => $row['kp'], "ap" => $row['ap']);
   $allGeophysicsDailyData[] = $partData;   
}

$allPaticleDailyData = array();
$result = mysqli_query($con,"SELECT * from tblparticledaily where dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "dt" => $row['dt'], "proton1mev" => $row['proton1mev'], "proton10mev" => $row['proton10mev'], "proton100mev" => $row['proton100mev'], "electron08mev" => $row['electron08mev'], "electron2mev" => $row['electron2mev']);
   $allPaticleDailyData[] = $partData;   
}
mysqli_close($con);
//$allData = array("weatherdaily" => $allWeatherDailyData, "geophysicsdaily" => $allGeophysicsDailyData, "geophysicshourly" => $allGeophysicsHourlyData, "particledaily" => $allPaticleDailyData);
//$json = json_encode($allData);
//echo $json;
$dependencySet = new DependencySet();
$result = $dependencySet->generateDiaryFeeling($diary, $dtFrom, $dtTo, $allWeatherDailyData, $allPaticleDailyData, $allGeophysicsHourlyData);
$json = json_encode($result);
echo $json;
?>