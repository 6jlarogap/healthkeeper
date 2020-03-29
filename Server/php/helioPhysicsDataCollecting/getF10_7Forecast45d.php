<?php
include('../global.php'); 
include('../libhttp.php');
include('saveHelioPhysicsData.php');
global $dbName, $hostname, $username, $password;
set_time_limit(600); 


$connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
$connection->exec("set names utf8");
$connection->exec("set time_zone='+00:00'"); 
$connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);     
   
$dataStr = getHtmlPage('ftp://ftp.swpc.noaa.gov/pub/latest/45DF.txt', $prox);

if($dataStr != null)
{
	$APdataStr = trim(str_replace("\n", " ", str_replace("45-DAY F10.7 CM FLUX FORECAST", "", substr($dataStr,strpos($dataStr, "45-DAY F10.7 CM FLUX FORECAST"), strpos($dataStr, "FORECASTER:") - strpos($dataStr, "45-DAY F10.7 CM FLUX FORECAST")))));
	$APdataArray = split(' ', $APdataStr);
	$connection->beginTransaction();

	try 
	{
		for($i = 0; $i < count($APdataArray); $i = $i + 2) 
		{
			$txtTime = date($datetimeformat, strtotime($APdataArray[$i]));
			$f10_7 = $APdataArray[$i + 1];
			saveheliophysicsData($connection, $txtTime, 1, $f10_7, null, null, null, null, null, null, null, null, null, null, null);
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