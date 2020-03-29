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

$dataStr = getHtmlPage('ftp://ftp.swpc.noaa.gov/pub/indices/DGD.txt', $prox);

if($dataStr != null)
{
	$KpDataStr = trim(substr($dataStr,strpos($dataStr,"#  Date        A     K-indices        A     K-indices        A     K-indices") + strlen("#  Date        A     K-indices        A     K-indices        A     K-indices")));
	$KpData = split("\n",$KpDataStr);
	$connection->beginTransaction();
	
	try 
	{
		for($i = 0; $i < count($KpData); ++$i) 
		{	
			$KpDataStrArray = preg_split("/[\s]+/", $KpData[$i]);
			
			$txtTime0 = date($datetimeformat, strtotime(substr($KpData[$i],0,4) . "-" . substr($KpData[$i],5,2) . "-" . substr($KpData[$i],8,2) . " 00:00:00"));
			$txtTime1 = date($datetimeformat, strtotime(substr($KpData[$i],0,4) . "-" . substr($KpData[$i],5,2) . "-" . substr($KpData[$i],8,2) . " 03:00:00"));
			$txtTime2 = date($datetimeformat, strtotime(substr($KpData[$i],0,4) . "-" . substr($KpData[$i],5,2) . "-" . substr($KpData[$i],8,2) . " 06:00:00"));
			$txtTime3 = date($datetimeformat, strtotime(substr($KpData[$i],0,4) . "-" . substr($KpData[$i],5,2) . "-" . substr($KpData[$i],8,2) . " 09:00:00"));
			$txtTime4 = date($datetimeformat, strtotime(substr($KpData[$i],0,4) . "-" . substr($KpData[$i],5,2) . "-" . substr($KpData[$i],8,2) . " 12:00:00"));
			$txtTime5 = date($datetimeformat, strtotime(substr($KpData[$i],0,4) . "-" . substr($KpData[$i],5,2) . "-" . substr($KpData[$i],8,2) . " 15:00:00"));
			$txtTime6 = date($datetimeformat, strtotime(substr($KpData[$i],0,4) . "-" . substr($KpData[$i],5,2) . "-" . substr($KpData[$i],8,2) . " 18:00:00"));
			$txtTime7 = date($datetimeformat, strtotime(substr($KpData[$i],0,4) . "-" . substr($KpData[$i],5,2) . "-" . substr($KpData[$i],8,2) . " 21:00:00"));
			
			$kp0 = substr($KpData[$i],63,4) * 10;
			$kp1 = substr($KpData[$i],65,4) * 10;
			$kp2 = substr($KpData[$i],67,4) * 10;
			$kp3 = substr($KpData[$i],69,4) * 10;
			$kp4 = substr($KpData[$i],71,4) * 10;
			$kp5 = substr($KpData[$i],73,4) * 10;
			$kp6 = substr($KpData[$i],75,4) * 10;
			$kp7 = substr($KpData[$i],77,4) * 10;
			
			saveGeophysicsData($connection, $txtTime0, 0, null, $kp0);
			saveGeophysicsData($connection, $txtTime1, 0, null, $kp1);
			saveGeophysicsData($connection, $txtTime2, 0, null, $kp2);
			saveGeophysicsData($connection, $txtTime3, 0, null, $kp3);
			saveGeophysicsData($connection, $txtTime4, 0, null, $kp4);
			saveGeophysicsData($connection, $txtTime5, 0, null, $kp5);
			saveGeophysicsData($connection, $txtTime6, 0, null, $kp6);
			saveGeophysicsData($connection, $txtTime7, 0, null, $kp7);
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