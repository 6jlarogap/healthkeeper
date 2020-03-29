<?php
include('../global.php'); 
include('../libhttp.php');
include('saveParticleData.php');
global $dbName, $hostname, $username, $password;
set_time_limit(600); 


$connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
$connection->exec("set names utf8");
$connection->exec("set time_zone='+00:00'"); 
$connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);     

for($q = 1; $q <= 4; $q=$q + 1)
{
	$dataStr = getHtmlPage('ftp://ftp.swpc.noaa.gov/pub/indices/old_indices/2014Q' . $q . '_DPD.txt', $prox);

	if($dataStr != null)
	{
		$particleDataStr = trim(substr($dataStr,strpos($dataStr,"#-------------------------------------------------------------------------------") + strlen("#-------------------------------------------------------------------------------")));
		$paticleData = split("\n",$particleDataStr);
		$connection->beginTransaction();
		
		try 
		{
			for($i = 0; $i < count($paticleData); ++$i) 
			{
				$particleDataStrArray = preg_split("/[\s]+/", $paticleData[$i]);
				$txtTime = date($datetimeformat, strtotime($particleDataStrArray[0] . "-" . $particleDataStrArray[1] . "-" . $particleDataStrArray[2]));
				$proton1mev = $particleDataStrArray[3];
				$proton10mev = $particleDataStrArray[4];
				$proton100mev = $particleDataStrArray[5];
				$electron08mev = $particleDataStrArray[6];
				$electron2mev = $particleDataStrArray[7];
				saveparticleData($connection, $txtTime, 1, $proton1mev, $proton10mev, $proton100mev, $electron08mev, $electron2mev);
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
}
?>