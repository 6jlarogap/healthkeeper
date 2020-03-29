<?php
include('../global.php'); 
include('../libhttp.php');
include('saveGeoPhysicsData.php');
global $dbName, $hostname, $username, $password;
set_time_limit(600); 


$connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
$connection->exec("set names utf8");
$connection->exec("set time_zone='+00:00'"); 
$connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);      

$dataStr = getHtmlPage('ftp://ftp.swpc.noaa.gov/pub/latest/geomag_forecast.txt', $prox);

if($dataStr != null)
{
	$KPdataStr = trim(str_replace("\n", " ", substr($dataStr,strpos($dataStr, "00-03UT"))));
	$KPdatesStr = trim(str_replace("\n", " ", substr($dataStr,strpos($dataStr, "00-03UT") - 30, 30)));
	$KPdatesArray = preg_split ("/[\s]+/", $KPdatesStr);
	$KPdataArray = preg_split ("/[\s]+/", $KPdataStr);
	  
	$connection->beginTransaction();
	try 
	{
		for($i = 0; $i < count($KPdatesArray); $i = $i + 2) 
		{	
			for($j = 0; $j < count($KPdataArray); $j = $j + 4) 
			{
				$txtTime = date($datetimeformat, strtotime($KPdatesArray[$i] . "-" . $KPdatesArray[$i + 1] . "-" . date('Y',time() + $i * 60 * 60 * 24) . " " .substr($KPdataArray[$j],0,2) . ":00:00"));
				$kp = $KPdataArray[$j + 1 + $i / 2];
				saveGeophysicsData($connection, $txtTime, 0, null, $kp * 10);
			}
		}
		
		$connection->commit();
	}
	catch (Exception $e) {
		echo "<br>=getCode=" . $e->getCode();
		echo "Exception" . $e->getMessage();
		$connection->rollback();
	}
}
else
{
	echo "<br>Data not avalaible!";
}

?>