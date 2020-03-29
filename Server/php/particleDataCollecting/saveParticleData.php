<?php

function saveparticleData($connection, $dt, $isdaily, $proton1mev, $proton10mev, $proton100mev, $electron08mev, $electron2mev)
{
	echo "<br>" . date("Y-m-d H:i:s", time()) . " saveParticleData: dt = " . $dt . "; proton1mev=" . $proton1mev . "; proton10mev=" . $proton10mev . "; proton100mev=" . $proton100mev . "; electron08mev=" . $electron08mev . "; electron2mev=" . $electron2mev;
	
	$insertDailyStatement = $connection->prepare("insert into tblparticledaily (dt, proton1mev, proton10mev, proton100mev, electron08mev, electron2mev) values (:dt, :proton1mev, :proton10mev, :proton100mev, :electron08mev, :electron2mev);");
    $updateDailyStatement = $connection->prepare("update tblparticledaily set dt = ifnull(:dt,dt), proton1mev = ifnull(:proton1mev,proton1mev), proton10mev = ifnull(:proton10mev,proton10mev), proton100mev = ifnull(:proton100mev,proton100mev), electron08mev = ifnull(:electron08mev,electron08mev), electron2mev = ifnull(:electron2mev,electron2mev)  where id = :id;");

    $sqlSelect = "SELECT max(id) FROM tblparticledaily where dt='" . $dt . "';";        
    $query = $connection->query($sqlSelect);
    $row = $query->fetch();
    $id =  $row[0];
	
    if(!isset($id))
	{
        $insertDailyStatement->bindParam(':dt', $dt, PDO::PARAM_STR);
        $insertDailyStatement->bindParam(':proton1mev', $proton1mev, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':proton10mev', $proton10mev, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':proton100mev', $proton100mev, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':electron08mev', $electron08mev, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':electron2mev', $electron2mev, PDO::PARAM_INT);
        
        $result = $insertDailyStatement->execute();
    } 
	else 
	{
        $heliophysicsid = $id;
        $updateDailyStatement->bindParam(':id', $id, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':dt', $dt, PDO::PARAM_STR);
        $updateDailyStatement->bindParam(':proton1mev', $proton1mev, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':proton10mev', $proton10mev, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':proton100mev', $proton100mev, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':electron08mev', $electron08mev, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':electron2mev', $electron2mev, PDO::PARAM_INT);
        $result = $updateDailyStatement->execute();
    }
}

?>