<?php

header('Content-Type: application/json; charset=utf-8');
include('global.php'); 
include('sunmoon.php');
include_once('astro/Planrise.php');
include_once('astro/Phases.php');
include_once('astro/EclTimerRar.php');
include_once('astro/Sunset.php');

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

function getMoonPhaseById($connection, $id)
{ 
	$result = mysqli_query($connection,"SELECT name FROM tblmoonphase where id='" . $id . "';");
	if (!$result) {
		echo 'Database error: ' . mysql_error();
		exit;
	}
	$r = mysqli_fetch_assoc($result);
    return $r["name"];
}

$lat = 53.9;
$lng = 27.5667;
$cityid = 625144;
$daily = True;
$hourly = True;
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
    $dtUnixFrom = time() - 30*24*60*60;    
}

if (isset($_GET['dateto'])) {
    $dtUnixTo = $_GET['dateto'];    
} else {
    $dtUnixTo = $dtUnixFrom + 60*24*60*60;
}

if (isset($_GET['timezone'])) {
    $tz = $_GET['timezone'];    
} else {
    $tz = 'Etc/GMT-0';
}
if (isset($_GET['hourly'])) {
    $hourly = (bool)$_GET['hourly'];
}
if (isset($_GET['daily'])) {
    $daily = (bool)$_GET['daily'];
}


$dtFrom = getBeginDateFromUnixTime($dtUnixFrom);
$dtTo = getNextDateFromUnixTime($dtUnixTo);

$con=mysqli_connect($hostname,$username,$password,$dbName);
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$allData = array();
$allWeatherData = array();
if($hourly){
    $result = mysqli_query($con,"SELECT * from tblweatherhourly where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
    if (!$result) {
        echo 'Database error: ' . mysql_error();
        exit;
    }
    while($row = mysqli_fetch_assoc($result)){
       $partData = array("id" => $row['id'], "cid" => $row['cityid'], "dt" => $row['dt'], "t" => $row['temp'],
       "h" => $row['humidity'], "p" => $row['pressure'], "ws" => $row['windspeed'],
       "wd" => $row['winddeg'], "c" => $row['clouds'], "r" => $row['rain'], "sn" => $row['snow'], 
       "tmin" => $row['temp_min'], "tmax" => $row['temp_max']);
       $allWeatherData[] = $partData;   
    }
}
$allWeatherDailyData = array();
if($daily){
    $result = mysqli_query($con,"SELECT * from tblweatherdaily where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
    if (!$result) {
        echo 'Database error: ' . mysql_error();
        exit;
    }
    while($row = mysqli_fetch_assoc($result)){
       $partData = array("id" => $row['id'], "cid" => $row['cityid'], "dt" => $row['dt'], "h" => $row['humidity'], "p" => $row['pressure'], "ws" => $row['windspeed'], "wd" => $row['winddeg'], "c" => $row['clouds'], "r" => $row['rain'], "sn" => $row['snow'], "tmin" => $row['temp_min'], "tmax" => $row['temp_max']);
       $allWeatherDailyData[] = $partData;   
    }
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

$allHeliophysicsDailyData = array();
$result = mysqli_query($con,"SELECT * from tblheliophysicsdaily where dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "dt" => $row['dt'], "f10_7" => $row['f10_7'], "sn" => $row['sunspot_number'], "sa" => $row['sunspot_area'], "nr" => $row['new_regions'], "x" => $row['xbkgd'], "fc" => $row['flares_c'], "fm" => $row['flares_m'], "fx" => $row['flares_x'], "fs" => $row['flares_s'], "f1" => $row['flares_1'], "f2" => $row['flares_2'], "f3" => $row['flares_3']);
   $allHeliophysicsDailyData[] = $partData;   
}

$allPaticleDailyData = array();
$result = mysqli_query($con,"SELECT * from tblparticledaily where dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "dt" => $row['dt'], "p1" => $row['proton1mev'], "p2" => $row['proton10mev'], "p3" => $row['proton100mev'], "e8" => $row['electron08mev'], "e2" => $row['electron2mev']);
   $allPaticleDailyData[] = $partData;   
}

$allSunData = array();
$dtUnix = $dtUnixFrom;
while($dtUnix <= $dtUnixTo)
{
	$date = new DateTime();
	$date->setTimezone(new DateTimeZone($tz));
	$date->setTimestamp($dtUnix);
	
	Sunset\Sunset($date->format('Y'), $date->format('m'), $date->format('d'), $lng, $lat, 0, 'SUN', $sunrise, $sunset);
	
	$partData = array("id" => $dtUnix,
					  "dt" => getDateFromUnixTime($dtUnix), //дата
					  "r" => $sunrise == 0 ? null : date($datetimeformat, $sunrise), //восход —олнца
					  "s" => $sunset == 0 ? null : date($datetimeformat, $sunset), //заход —олнца
					  "l" => date("H:i", $sunset - $sunrise + get_timezone_offset(date_default_timezone_get()))); //длина дн¤
	$allSunData[] = $partData;  
					  
	$dtUnix = $dtUnix + 24*60*60;
}

$allMoonData = array();
$dtUnix = $dtUnixFrom;
while($dtUnix <= $dtUnixTo)
{
	$date = new DateTime();
	$date->setTimezone(new DateTimeZone($tz));
	$date->setTimestamp($dtUnix);
	
	getMoonParams($date->format('Y'), $date->format('m'), $date->format('d'), $date->format('H'), $date->format('i'), $date->format('s'), $lat, $lng, 0, $moonVisibility, $moonOld, $moonRise, $moonSet, $moonPhaseId);
	
	$partData = array("id" => $dtUnix, 
					  "dt" => getDateFromUnixTime($dtUnix), //дата
					  "v" => strval($moonVisibility), //процент освещаемой видимой поверхности Ћуны
					  "o" => date($datetimeformat, $moonOld * 3600), //возраст Ћуны в часах
					  "r" => $moonRise == 0 ? null : date($datetimeformat, $moonRise), //восход Ћуны
					  "s" => $moonSet == 0 ? null : date($datetimeformat, $moonSet), //заход Ћуны
					  "pid" => $moonPhaseId					  
					  );
	$allMoonData[] = $partData;  
					  
	$dtUnix = $dtUnix + 24 * 60 * 60;
}

$allPlanetData = array();
$dtUnix = $dtUnixFrom;
while($dtUnix <= $dtUnixTo)
{
	$date = new DateTime();
	$date->setTimezone(new DateTimeZone($tz));
	$date->setTimestamp($dtUnix);
	
	Planrise\Planrise($date->format('Y'), $date->format('m'), $date->format('d'),$lng,$lat,0,'MERCURY',$mercuryRise,$mercurySet);
	Planrise\Planrise($date->format('Y'), $date->format('m'), $date->format('d'),$lng,$lat,0,'VENUS',$venusRise,$venusSet);
	Planrise\Planrise($date->format('Y'), $date->format('m'), $date->format('d'),$lng,$lat,0,'MARS',$marsRise,$marsSet);
	Planrise\Planrise($date->format('Y'), $date->format('m'), $date->format('d'),$lng,$lat,0,'JUPITER',$jupiterRise,$jupiterSet);
	Planrise\Planrise($date->format('Y'), $date->format('m'), $date->format('d'),$lng,$lat,0,'SATURN',$saturnRise,$saturnSet);
	Planrise\Planrise($date->format('Y'), $date->format('m'), $date->format('d'),$lng,$lat,0,'URANUS',$uranusRise,$uranusSet);
	Planrise\Planrise($date->format('Y'), $date->format('m'), $date->format('d'),$lng,$lat,0,'NEPTUNE',$neptuneRise,$neptuneSet);
	Planrise\Planrise($date->format('Y'), $date->format('m'), $date->format('d'),$lng,$lat,0,'PLUTO',$plutoRise,$plutoSet);
	
	$partData = array("id" => $dtUnix,
					  "dt" => getDateFromUnixTime($dtUnix), //дата
					  "me1" => $mercuryRise == null ? null : date($datetimeformat, $mercuryRise),
					  "me2" => $mercurySet == null ? null : date($datetimeformat, $mercurySet),
					  "v1" => $venusRise == null ? null : date($datetimeformat, $venusRise),
					  "v2" => $venusSet == null ? null : date($datetimeformat, $venusSet),
					  "m1" => $marsRise == null ? null : date($datetimeformat, $marsRise),
					  "m2" => $marsSet == null ? null : date($datetimeformat, $marsSet),
					  "j1" => $jupiterRise == null ? null : date($datetimeformat, $jupiterRise),
					  "j2" => $jupiterSet == null ? null : date($datetimeformat, $jupiterSet),
					  "s1" => $saturnRise == null ? null : date($datetimeformat, $saturnRise),
					  "s2" => $saturnSet == null ? null : date($datetimeformat, $saturnSet),
					  "u1" => $uranusRise == null ? null : date($datetimeformat, $uranusRise),
					  "u2" => $uranusSet == null ? null : date($datetimeformat, $uranusSet),
					  "n1" => $neptuneRise == null ? null : date($datetimeformat, $neptuneRise),
					  "n2" => $neptuneSet == null ? null : date($datetimeformat, $neptuneSet),
					  "p1" => $plutoRise == null ? null : date($datetimeformat, $plutoRise),
					  "p2" => $plutoSet == null ? null : date($datetimeformat, $plutoSet)
					  ); //длина дн¤
	$allPlanetData[] = $partData;  
			  
	$dtUnix = $dtUnix + 24*60*60;
}

$allMoonPhasesData = array();
$phs = Phases\Phases(date("Y",$dtUnixTo));
for($i = 0; $i < count($phs); $i++) {
	$partData = array("id" => strtotime($phs[$i][1]),
					  "dt" => $phs[$i][1],
					  "p" => $phs[$i][0],
					  "pid" => $phs[$i][3]);
	$allMoonPhasesData[] = $partData;
}

$allSolarEclipseData = EclTimer\EclTimer($phs,$lng,$lat,$cityid);

$complaintData = array();
//bodycomplaint
$result = mysqli_query($con,"(select (floor(UNIX_TIMESTAMP(f.startdate)/86400) * 100000 + br.complaintid) id, br.complaintid as complainttypeid, NULL as commonfeelingtypeid, DATE(f.startdate) startdate, count(distinct u.id) cnt from tblbodyfeeling f
inner join tbluser u on u.id = f.userid
inner join tblbodyregion br on br.id=f.bodyregionid
inner join tblbodycomplainttype cp on cp.id = br.complaintid
where u.cityid = ".$cityid." and UNIX_TIMESTAMP(f.startdate) >= ".$dtUnixFrom." and UNIX_TIMESTAMP(f.startdate) < ".$dtUnixTo."
group by br.complaintid, DATE(f.startdate)
order by br.complaintid, DATE(f.startdate)) UNION
(select (floor(UNIX_TIMESTAMP(c.startdate)/86400) * 100000 + 100*c.feelingtypeid) id,  NULL as complainttypeid, c.feelingtypeid as commonfeelingtypeid, DATE(c.startdate) startdate, count(distinct u.id) cnt from tblcommonfeeling c
inner join tbluser u on u.id = c.userid
inner join tblcommonfeelingtype cft on cft.id=c.feelingtypeid
where c.feelingtypeid in (14,23,46) and u.cityid = ".$cityid." and UNIX_TIMESTAMP(c.startdate) >= ".$dtUnixFrom." and UNIX_TIMESTAMP(c.startdate) < ".$dtUnixTo."
group by c.feelingtypeid, DATE(c.startdate)
order by c.feelingtypeid, DATE(c.startdate));");
if (!$result) {
    echo 'Database error - bodycomplaint: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "uid" => null, "rid" => null, "dt" => $row['startdate'], "cid" => $row['complainttypeid'], "fid" => $row['commonfeelingtypeid'], "cnt" => $row['cnt']);
   $complaintData[] = $partData;   
}


mysqli_close($con);
$allData = array("w" => $allWeatherData, "wd" => $allWeatherDailyData, "gd" => $allGeophysicsDailyData, "gh" => $allGeophysicsHourlyData, "hd" => $allHeliophysicsDailyData, "pd" => $allPaticleDailyData, "s" => $allSunData, "m" => $allMoonData, "p" => $allPlanetData, "mp" => $allMoonPhasesData, "se" => $allSolarEclipseData, "complaint" => $complaintData);
$json = json_encode($allData);
echo $json;
?>