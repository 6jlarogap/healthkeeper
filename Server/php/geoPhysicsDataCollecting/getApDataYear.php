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
									  
			$ap0 = substr($s[$i], 31, 3);
			$ap1 = substr($s[$i], 34, 3);
			$ap2 = substr($s[$i], 37, 3);
			$ap3 = substr($s[$i], 40, 3);
			$ap4 = substr($s[$i], 43, 3);
			$ap5 = substr($s[$i], 46, 3);
			$ap6 = substr($s[$i], 49, 3);
			$ap7 = substr($s[$i], 52, 3);
			
			saveGeophysicsData($connection, $dt0, 0, $ap0, null);
			saveGeophysicsData($connection, $dt1, 0, $ap1, null);
			saveGeophysicsData($connection, $dt2, 0, $ap2, null);
			saveGeophysicsData($connection, $dt3, 0, $ap3, null);
			saveGeophysicsData($connection, $dt4, 0, $ap4, null);
			saveGeophysicsData($connection, $dt5, 0, $ap5, null);
			saveGeophysicsData($connection, $dt6, 0, $ap6, null);
			saveGeophysicsData($connection, $dt7, 0, $ap7, null);
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