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

if(date('m') == 1)
	$year = date('Y') - 1;
else
    $year = date('Y');
	
$dataStr = getHtmlPage('ftp://ftp.ngdc.noaa.gov/STP/GEOMAGNETIC_DATA/INDICES/KP_AP/' . $year, $prox);

if($dataStr != null)
{
	$s = split("\n",trim($dataStr));
	$connection->beginTransaction();

	try 
	{
		for($i = 0; $i < count($s); $i++) 
		{	
			$dt0 = date($datetimeformat, strtotime(substr($s[$i],0,2) . "-" . substr($s[$i],2,2) . "-" . substr($s[$i],4,2) . " " . "00:00:00"));
			$dt1 = date($datetimeformat, strtotime(substr($s[$i],0,2) . "-" . substr($s[$i],2,2) . "-" . substr($s[$i],4,2) . " " . "03:00:00"));
			$dt2 = date($datetimeformat, strtotime(substr($s[$i],0,2) . "-" . substr($s[$i],2,2) . "-" . substr($s[$i],4,2) . " " . "06:00:00"));
			$dt3 = date($datetimeformat, strtotime(substr($s[$i],0,2) . "-" . substr($s[$i],2,2) . "-" . substr($s[$i],4,2) . " " . "09:00:00"));
			$dt4 = date($datetimeformat, strtotime(substr($s[$i],0,2) . "-" . substr($s[$i],2,2) . "-" . substr($s[$i],4,2) . " " . "12:00:00"));
			$dt5 = date($datetimeformat, strtotime(substr($s[$i],0,2) . "-" . substr($s[$i],2,2) . "-" . substr($s[$i],4,2) . " " . "15:00:00"));
			$dt6 = date($datetimeformat, strtotime(substr($s[$i],0,2) . "-" . substr($s[$i],2,2) . "-" . substr($s[$i],4,2) . " " . "18:00:00"));
			$dt7 = date($datetimeformat, strtotime(substr($s[$i],0,2) . "-" . substr($s[$i],2,2) . "-" . substr($s[$i],4,2) . " " . "21:00:00"));
			
			$kp0 = substr($s[$i], 12, 2);
			$kp1 = substr($s[$i], 14, 2);
			$kp2 = substr($s[$i], 16, 2);
			$kp3 = substr($s[$i], 18, 2);
			$kp4 = substr($s[$i], 20, 2);
			$kp5 = substr($s[$i], 22, 2);
			$kp6 = substr($s[$i], 24, 2);
			$kp7 = substr($s[$i], 26, 2);
			
			saveGeophysicsData($connection, $dt0, 0, null, $kp0);
			saveGeophysicsData($connection, $dt1, 0, null, $kp1);
			saveGeophysicsData($connection, $dt2, 0, null, $kp2);
			saveGeophysicsData($connection, $dt3, 0, null, $kp3);
			saveGeophysicsData($connection, $dt4, 0, null, $kp4);
			saveGeophysicsData($connection, $dt5, 0, null, $kp5);
			saveGeophysicsData($connection, $dt6, 0, null, $kp6);
			saveGeophysicsData($connection, $dt7, 0, null, $kp7);
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