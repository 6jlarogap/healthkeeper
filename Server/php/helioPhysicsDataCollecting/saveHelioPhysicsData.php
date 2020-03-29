<?php

function saveheliophysicsData($connection, $dt, $isdaily, $f10_7, $sunspot_number, $sunspot_area, $new_regions, $xbkgd, $flares_c, $flares_m, $flares_x, $flares_s, $flares_1, $flares_2, $flares_3)
{
	echo "<br>" . date("Y-m-d H:i:s", time()) . " saveGeophysicsData: dt = " . $dt . "; isdaily=" . $isdaily . "; f10_7=" . $f10_7 . "; sunspot_number=" . $sunspot_number . "; sunspot_area=" . $sunspot_area . "; new_regions=" . $new_regions . "; xbkgd=" . $xbkgd . "; flares_c=" . $flares_c . "; flares_m=" . $flares_m . "; flares_x=" . $flares_x . "; flares_s=" . $flares_s . "; flares_1=" . $flares_1 . "; flares_2=" . $flares_2 . "; flares_3=" . $flares_3;
	
	$insertDailyStatement = $connection->prepare("insert into tblheliophysicsdaily (dt, f10_7, sunspot_number, sunspot_area, new_regions, xbkgd, flares_c, flares_m, flares_x, flares_s, flares_1, flares_2, flares_3) values (:dt, :f10_7, :sunspot_number, :sunspot_area, :new_regions, :xbkgd, :flares_c, :flares_m, :flares_x, :flares_s, :flares_1, :flares_2, :flares_3);");
    $updateDailyStatement = $connection->prepare("update tblheliophysicsdaily set dt = ifnull(:dt,dt), f10_7 = ifnull(:f10_7,f10_7), sunspot_number = ifnull(:sunspot_number,sunspot_number), sunspot_area = ifnull(:sunspot_area,sunspot_area), new_regions = ifnull(:new_regions,new_regions), xbkgd = ifnull(:xbkgd,xbkgd), flares_c = ifnull(:flares_c,flares_c), flares_m = ifnull(:flares_m,flares_m), flares_x = ifnull(:flares_x,flares_x), flares_s = ifnull(:flares_s,flares_s), flares_1 = ifnull(:flares_1,flares_1), flares_2 = ifnull(:flares_2,flares_2), flares_3 = ifnull(:flares_3,flares_3) where id = :id;");

    $sqlSelect = "SELECT max(id) FROM tblheliophysicsdaily where dt='" . $dt . "';";        
    $query = $connection->query($sqlSelect);
    $row = $query->fetch();
    $id =  $row[0];
	
    if(!isset($id))
	{
        $insertDailyStatement->bindParam(':dt', $dt, PDO::PARAM_STR);
        $insertDailyStatement->bindParam(':f10_7', $f10_7, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':sunspot_number', $sunspot_number, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':sunspot_area', $sunspot_area, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':new_regions', $new_regions, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':xbkgd', $xbkgd, PDO::PARAM_STR);
        $insertDailyStatement->bindParam(':flares_c', $flares_c, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':flares_m', $flares_m, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':flares_x', $flares_x, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':flares_s', $flares_s, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':flares_1', $flares_1, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':flares_2', $flares_2, PDO::PARAM_INT);
        $insertDailyStatement->bindParam(':flares_3', $flares_3, PDO::PARAM_INT);
        
        $result = $insertDailyStatement->execute();
    } 
	else 
	{
        $heliophysicsid = $id;
        $updateDailyStatement->bindParam(':id', $id, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':dt', $dt, PDO::PARAM_STR);
        $updateDailyStatement->bindParam(':f10_7', $f10_7, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':sunspot_number', $sunspot_number, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':sunspot_area', $sunspot_area, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':new_regions', $new_regions, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':xbkgd', $xbkgd, PDO::PARAM_STR);
        $updateDailyStatement->bindParam(':flares_c', $flares_c, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':flares_m', $flares_m, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':flares_x', $flares_x, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':flares_s', $flares_s, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':flares_1', $flares_1, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':flares_2', $flares_2, PDO::PARAM_INT);
        $updateDailyStatement->bindParam(':flares_3', $flares_3, PDO::PARAM_INT);
        $result = $updateDailyStatement->execute();
    }
}

?>