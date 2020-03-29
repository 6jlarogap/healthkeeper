<?php

header('Content-Type: application/json; charset=utf-8');
include('global.php'); 
include('sunmoon.php');
include_once('astro/Planrise.php');
include_once('astro/Phases.php');
include_once('astro/EclTimer.php');
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
$dtFrom = getBeginDateFromUnixTime($dtUnixFrom);
$dtTo = getNextDateFromUnixTime($dtUnixTo);

$con=mysqli_connect($hostname,$username,$password,$dbName);
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$allData = array();
$allWeatherData = array();
$result = mysqli_query($con,"SELECT * from tblweatherhourly where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "cityid" => $row['cityid'], "dt" => $row['dt'], "temp" => $row['temp'],
   "humidity" => $row['humidity'], "pressure" => $row['pressure'], "windspeed" => $row['windspeed'],
   "winddeg" => $row['winddeg'], "clouds" => $row['clouds'], "rain" => $row['rain'], "snow" => $row['snow'], 
   "temp_min" => $row['temp_min'], "temp_max" => $row['temp_max']);
   $allWeatherData[] = $partData;   
}

$allWeatherDailyData = array();
$result = mysqli_query($con,"SELECT * from tblweatherdaily where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "cityid" => $row['cityid'], "dt" => $row['dt'], "humidity" => $row['humidity'], "pressure" => $row['pressure'], "windspeed" => $row['windspeed'], "winddeg" => $row['winddeg'], "clouds" => $row['clouds'], "rain" => $row['rain'], "snow" => $row['snow'], "temp_min" => $row['temp_min'], "temp_max" => $row['temp_max']);
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

$allHeliophysicsDailyData = array();
$result = mysqli_query($con,"SELECT * from tblheliophysicsdaily where dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "dt" => $row['dt'], "f10_7" => $row['f10_7'], "sunspot_number" => $row['sunspot_number'], "sunspot_area" => $row['sunspot_area'], "new_regions" => $row['new_regions'], "xbkgd" => $row['xbkgd'], "flares_c" => $row['flares_c'], "flares_m" => $row['flares_m'], "flares_x" => $row['flares_x'], "flares_s" => $row['flares_s'], "flares_1" => $row['flares_1'], "flares_2" => $row['flares_2'], "flares_3" => $row['flares_3']);
   $allHeliophysicsDailyData[] = $partData;   
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

$allSunData = array();
$dtUnix = $dtUnixFrom - ($dtUnixFrom % (24 * 60 * 60));
while($dtUnix <= $dtUnixTo)
{
	
	Sunset\Sunset(gmdate("Y", $dtUnix),gmdate("m", $dtUnix),gmdate("d", $dtUnix),$lng,$lat,0,'SUN',$sunrise,$sunset);
	
	$partData = array("id" => $dtUnix,
					  "dt" => getDateFromUnixTime($dtUnix), //дата
					  "sun_rise" => $sunrise == 0 ? null : date($datetimeformat, $sunrise), //восход —олнца
					  "sun_set" => $sunset == 0 ? null : date($datetimeformat, $sunset), //заход —олнца
					  "day_length" => date("H:i", $sunset - $sunrise + get_timezone_offset(date_default_timezone_get()))); //длина дн¤
	$allSunData[] = $partData;  
					  
	$dtUnix = $dtUnix + 24*60*60;
}

$allMoonData = array();
$dtUnix = $dtUnixFrom - ($dtUnixFrom % (24 * 60 * 60));
while($dtUnix <= $dtUnixTo)
{
	getMoonParams($dtUnix, $lat, $lng, 0, $moonVisibility, $moonOld, $moonRise, $moonSet, $moonPhaseId);
	
	$partData = array("id" => $dtUnix, 
					  "dt" => getDateFromUnixTime($dtUnix), //дата
					  "moon_visibility" => strval($moonVisibility), //процент освещаемой видимой поверхности Ћуны
					  "moon_old" => date($datetimeformat, $moonOld * 3600), //возраст Ћуны в часах
					  "moon_rise" => $moonRise == 0 ? null : date($datetimeformat, $moonRise), //восход Ћуны
					  "moon_set" => $moonSet == 0 ? null : date($datetimeformat, $moonSet), //заход Ћуны
					  "moon_phaseid" => $moonPhaseId,
					  "moon_phase" => getMoonPhaseById($con, $moonPhaseId)
					  );
	$allMoonData[] = $partData;  
					  
	$dtUnix = $dtUnix + 24 * 60 * 60;
}

$allPlanetData = array();
$dtUnix = $dtUnixFrom - ($dtUnixFrom % (24 * 60 * 60));
while($dtUnix <= $dtUnixTo)
{
	Planrise\Planrise(gmdate("Y", $dtUnix),gmdate("m", $dtUnix),gmdate("d", $dtUnix),$lng,$lat,0,'MERCURY',$mercuryRise,$mercurySet);
	Planrise\Planrise(gmdate("Y", $dtUnix),gmdate("m", $dtUnix),gmdate("d", $dtUnix),$lng,$lat,0,'VENUS',$venusRise,$venusSet);
	Planrise\Planrise(gmdate("Y", $dtUnix),gmdate("m", $dtUnix),gmdate("d", $dtUnix),$lng,$lat,0,'MARS',$marsRise,$marsSet);
	Planrise\Planrise(gmdate("Y", $dtUnix),gmdate("m", $dtUnix),gmdate("d", $dtUnix),$lng,$lat,0,'JUPITER',$jupiterRise,$jupiterSet);
	Planrise\Planrise(gmdate("Y", $dtUnix),gmdate("m", $dtUnix),gmdate("d", $dtUnix),$lng,$lat,0,'SATURN',$saturnRise,$saturnSet);
	Planrise\Planrise(gmdate("Y", $dtUnix),gmdate("m", $dtUnix),gmdate("d", $dtUnix),$lng,$lat,0,'URANUS',$uranusRise,$uranusSet);
	Planrise\Planrise(gmdate("Y", $dtUnix),gmdate("m", $dtUnix),gmdate("d", $dtUnix),$lng,$lat,0,'NEPTUNE',$neptuneRise,$neptuneSet);
	Planrise\Planrise(gmdate("Y", $dtUnix),gmdate("m", $dtUnix),gmdate("d", $dtUnix),$lng,$lat,0,'PLUTO',$plutoRise,$plutoSet);
	
	$partData = array("id" => $dtUnix,
					  "dt" => getDateFromUnixTime($dtUnix), //дата
					  "mercury_rise" => $mercuryRise == null ? null : date($datetimeformat, $mercuryRise),
					  "mercury_set" => $mercurySet == null ? null : date($datetimeformat, $mercurySet),
					  "venus_rise" => $venusRise == null ? null : date($datetimeformat, $venusRise),
					  "venus_set" => $venusSet == null ? null : date($datetimeformat, $venusSet),
					  "mars_rise" => $marsRise == null ? null : date($datetimeformat, $marsRise),
					  "mars_set" => $marsSet == null ? null : date($datetimeformat, $marsSet),
					  "jupiter_rise" => $jupiterRise == null ? null : date($datetimeformat, $jupiterRise),
					  "jupiter_set" => $jupiterSet == null ? null : date($datetimeformat, $jupiterSet),
					  "saturn_rise" => $saturnRise == null ? null : date($datetimeformat, $saturnRise),
					  "saturn_set" => $saturnSet == null ? null : date($datetimeformat, $saturnSet),
					  "uranus_rise" => $uranusRise == null ? null : date($datetimeformat, $uranusRise),
					  "uranus_set" => $uranusSet == null ? null : date($datetimeformat, $uranusSet),
					  "neptune_rise" => $neptuneRise == null ? null : date($datetimeformat, $neptuneRise),
					  "neptune_set" => $neptuneSet == null ? null : date($datetimeformat, $neptuneSet),
					  "pluto_rise" => $plutoRise == null ? null : date($datetimeformat, $plutoRise),
					  "pluto_set" => $plutoSet == null ? null : date($datetimeformat, $plutoSet)
					  ); //длина дн¤
	$allPlanetData[] = $partData;  
			  
	$dtUnix = $dtUnix + 24*60*60;
}

$allMoonPhasesData = array();
$phs = Phases\Phases(date("Y",$dtUnixTo));
for($i = 0; $i < count($phs); $i++) {
	$partData = array("id" => strtotime($phs[$i][1]),
					  "dt" => $phs[$i][1],
					  "moon_phase" => $phs[$i][0],
					  "moon_phase_id" => $phs[$i][3]);
	$allMoonPhasesData[] = $partData;
}

$allSolarEclipseData = EclTimer\EclTimer($phs,$lng,$lat,$cityid);

//bodycomplaint
$complaintData = array();
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
   $partData = array("id" => $row['id'], "userid" => null, "rowid" => null, "startdate" => $row['startdate'], "complainttypeid" => $row['complainttypeid'], "commonfeelingtypeid" => $row['commonfeelingtypeid'], "count" => $row['cnt']);
   $complaintData[] = $partData;   
}


mysqli_close($con);
$allData = array("weather" => $allWeatherData, "weatherdaily" => $allWeatherDailyData, "geophysicsdaily" => $allGeophysicsDailyData, "geophysicshourly" => $allGeophysicsHourlyData, "heliophysicsdaily" => $allHeliophysicsDailyData, "particledaily" => $allPaticleDailyData, "sun" => $allSunData, "moon" => $allMoonData, "planet" => $allPlanetData, "moonphase" => $allMoonPhasesData, "solareclipse" => $allSolarEclipseData, "complaint" => $complaintData);
$json = json_encode($allData);
echo $json;
?>