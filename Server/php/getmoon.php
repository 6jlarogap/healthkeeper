<?php

header('Content-Type: application/json; charset=utf-8');
include('global.php'); 
include('sunmoon.php'); 

function getMoonPhaseById($connection, $id)
{
    $sqlSelect = "SELECT name FROM tblmoonphase where id='" . $id . "';";        
    $query = $connection->query($sqlSelect);
	var_dump($query);
    $row = $query->fetch();
    return $row[0];
}

$lat = NULL;
$lng = NULL;
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
    $dtUnixFrom = time();    
}
if (isset($_GET['dateto'])) {
    $dtUnixTo = $_GET['dateto'];    
} else {
    $dtUnixTo = $dtUnixFrom + 14*24*60*60; //14day
}
$dtFrom = getBeginDateFromUnixTime($dtUnixFrom);
$dtTo = getNextDateFromUnixTime($dtUnixTo);

$allData = array();
$allSunData = array();
$allMoonData = array();
$dtUnix = $dtUnixFrom;
while($dtUnix <= $dtUnixTo)
{
	$sunrise = getSunriseTime($dtUnix, $lat, $lng, -3);
	$sunset = getSunsetTime($dtUnix, $lat, $lng, -3);
	$partData = array("dt" => date('Y-m-d', $dtUnix), //����
					  "sunrise" => $sunrise == 0 ? null : date($datetimeformat, $sunrise), //������ ������
					  "sunset" => $sunset == 0 ? null : date($datetimeformat, $sunset), //����� ������
					  "dayLength" => date("H:i:s", $sunset - $sunrise - 3600)); //����� ���
	$allSunData[] = $partData;  
					  
	$dtUnix = $dtUnix + 24*60*60;
}


$connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
$connection->exec("set names utf8");
$connection->exec("set time_zone='+00:00'"); 
$connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);        
$connection->beginTransaction();

$dtUnix = $dtUnixFrom;
while($dtUnix <= $dtUnixTo)
{
	getMoonParams($dtUnix, $lat, $lng, -3, $moonVisibility, $moonOld, $moonRise, $moonSet, $moonPhaseId);
	
	$partData = array("dt" => date($datetimeformat, $dtUnix), //����
					  "moonVisibility" => strval($moonVisibility), //������� ���������� ������� ����������� ����
					  "moonOld" => date($datetimeformat, $moonOld * 3600), //������� ���� � �����
					  "moonRise" => $moonRise == 0 ? null : date($datetimeformat, $moonRise), //������ ����
					  "moonSet" => $moonSet == 0 ? null : date($datetimeformat, $moonSet), //����� ����
					  "moonPhaseId" => $moonPhaseId,
					  "moonPhase" => getMoonPhaseById($connection, $moonPhaseId)
					  );
	$allMoonData[] = $partData;  
					  
	$dtUnix = $dtUnix + 3*60*60;
}

$connection->commit();
$allData = array("sun" => $allSunData, "moon" => $allMoonData);
$json = json_encode($allData);
echo $json;
?>
