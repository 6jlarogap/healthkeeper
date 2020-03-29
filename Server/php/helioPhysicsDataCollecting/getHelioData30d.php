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

$dataStr = getHtmlPage('ftp://ftp.swpc.noaa.gov/pub/indices/DSD.txt', $prox);

if($dataStr != null)
{
	$solarDataStr = trim(substr($dataStr,strpos($dataStr,"#---------------------------------------------------------------------------") + strlen("#---------------------------------------------------------------------------")));
	$solarData = split("\n",$solarDataStr);
	$connection->beginTransaction();
	
	try 
	{
		for($i = 0; $i < count($solarData); ++$i) 
		{
			$solarDataStrArray = preg_split("/[\s]+/", $solarData[$i]);
			$txtTime = date($datetimeformat, strtotime($solarDataStrArray[0] . "-" . $solarDataStrArray[1] . "-" . $solarDataStrArray[2]));
			$f10_7 = $solarDataStrArray[3];
			$sunspot_number = $solarDataStrArray[4];
			$sunspot_area = $solarDataStrArray[5];
			$new_regions = $solarDataStrArray[6];
			$xbkgd = null;
			if(substr($solarDataStrArray[8],0,1) == 'A' || 
			   substr($solarDataStrArray[8],0,1) == 'B' || 
			   substr($solarDataStrArray[8],0,1) == 'C' ||
			   substr($solarDataStrArray[8],0,1) == 'M' ||
			   substr($solarDataStrArray[8],0,1) == 'X') {
				$xbkgd = $solarDataStrArray[8];
			}
			$flares_c = $solarDataStrArray[9];
			$flares_m = $solarDataStrArray[10];
			$flares_x = $solarDataStrArray[11];
			$flares_s = $solarDataStrArray[12];
			$flares_1 = $solarDataStrArray[13];
			$flares_2 = $solarDataStrArray[14];
			$flares_3 = $solarDataStrArray[15];
			saveheliophysicsData($connection, $txtTime, 1, $f10_7, $sunspot_number, $sunspot_area, $new_regions, $xbkgd, $flares_c, $flares_m, $flares_x, $flares_s, $flares_1, $flares_2, $flares_3);
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