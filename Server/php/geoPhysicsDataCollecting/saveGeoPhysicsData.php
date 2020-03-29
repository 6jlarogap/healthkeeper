<?php

function saveGeophysicsData($connection, $dt, $isdaily, $ap, $kp)
{
	echo "<br>" . date("Y-m-d H:i:s", time()) . " saveGeophysicsData: dt = " . $dt . "; isdaily=" . $isdaily . "; ap=" . $ap . "; kp=" . $kp;
	
	$insertDailyStatement = $connection->prepare("insert into tblgeophysicsdaily (dt, ap, kp) values (:dt, :ap, :kp);");
    $updateDailyStatement = $connection->prepare("update tblgeophysicsdaily set dt = :dt, ap = ifnull(:ap,ap), kp = ifnull(:kp,kp) where id = :id;");
	
	$insertHourlyStatement = $connection->prepare("insert into tblgeophysicshourly (dt, ap, kpid) values (:dt, :ap, (select id from tblkpindex where intvalue = :kp));");
    $updateHourlyStatement = $connection->prepare("update tblgeophysicshourly set dt = :dt, ap = ifnull(:ap,ap), kpid = ifnull((select id from tblkpindex where intvalue = :kp),kpid) where id = :id;");

	if($isdaily)
	{
		$sqlSelect = "SELECT max(id) FROM tblgeophysicsdaily where dt='" . $dt . "';";        
		$query = $connection->query($sqlSelect);
		$row = $query->fetch();
		$id =  $row[0];
		
		if(!isset($id))
		{
			$insertDailyStatement->bindParam(':dt', $dt, PDO::PARAM_STR);
			$insertDailyStatement->bindParam(':ap', $ap, PDO::PARAM_INT);
			$insertDailyStatement->bindParam(':kp', $kp, PDO::PARAM_STR);
			
			$result = $insertDailyStatement->execute();
		} 
		else 
		{
			$updateDailyStatement->bindParam(':id', $id, PDO::PARAM_INT);
			$updateDailyStatement->bindParam(':dt', $dt, PDO::PARAM_STR);
			$updateDailyStatement->bindParam(':ap', $ap, PDO::PARAM_INT);
			$updateDailyStatement->bindParam(':kp', $kp, PDO::PARAM_STR);
			$result = $updateDailyStatement->execute();
		}
	}
	else
	{
		$sqlSelect = "SELECT max(id) FROM tblgeophysicshourly where dt='" . $dt . "';";        
		$query = $connection->query($sqlSelect);
		$row = $query->fetch();
		$id =  $row[0];
		
		if(!isset($id))
		{
			$insertHourlyStatement->bindParam(':dt', $dt, PDO::PARAM_STR);
			$insertHourlyStatement->bindParam(':ap', $ap, PDO::PARAM_INT);
			$insertHourlyStatement->bindParam(':kp', $kp, PDO::PARAM_INT);
			
			$result = $insertHourlyStatement->execute();
		} 
		else 
		{
			$updateHourlyStatement->bindParam(':id', $id, PDO::PARAM_INT);
			$updateHourlyStatement->bindParam(':dt', $dt, PDO::PARAM_STR);
			$updateHourlyStatement->bindParam(':ap', $ap, PDO::PARAM_INT);
			$updateHourlyStatement->bindParam(':kp', $kp, PDO::PARAM_INT);
			$result = $updateHourlyStatement->execute();
		}
	}
}

?>