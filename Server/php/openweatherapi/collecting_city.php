<?php
include('../global.php'); 
include('../libhttp.php');
set_time_limit (1000);

#Belarus cities
$json = getHtmlPage('http://openweathermap.org/data/getrect?type=city&lat1=51.27&lat2=56.16&lng1=23.17&lng2=32.76',$prox);
$arrayData = json_decode($json, True);
global $dbName, $hostname, $username, $password;
try {
    $connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
    $connection->exec("set names utf8");
    $connection->exec("set time_zone='+00:00'"); 
    $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $connection->beginTransaction();
    $arrayCity = $arrayData['list'];
    foreach ($arrayCity as $city) {
        $cityid = $city['id'];
        $sqlSelectCount = 'SELECT count(*) FROM tblcity where id='.$city['id'];
        $query = $connection->query($sqlSelectCount);
        $row = $query->fetch();
        $countCity =  $row[0];
        if ($countCity == 0) {
            $insertStatement = $connection->prepare("INSERT INTO tblcity (id, name, lat, lng) VALUES (:id, :name, :lat, :lng);");
            $insertStatement->bindParam(':id', $cityid, PDO::PARAM_INT);
            $insertStatement->bindParam(':name', $city['name'], PDO::PARAM_STR);
            $insertStatement->bindParam(':lat', $city['lat'], PDO::PARAM_INT);
            $insertStatement->bindParam(':lng', $city['lng'], PDO::PARAM_INT);
        } else {
            $updateStatement = $connection->prepare("UPDATE tblcity set name = :name, lat = :lat, lng = :lng where id = :id;");
            $updateStatement->bindParam(':id', $cityid, PDO::PARAM_INT);
            $updateStatement->bindParam(':name', $city['name'], PDO::PARAM_STR);
            $updateStatement->bindParam(':lat', $city['lat'], PDO::PARAM_INT);
            $updateStatement->bindParam(':lng', $city['lng'], PDO::PARAM_INT);
        }
        try {
            if ($countCity == 0) {
                $result = $insertStatement->execute();                        
            } else {
                $result = $updateStatement->execute();
            }
        }
        catch( PDOException $pdoExc ) {
            echo "=getCode=" . $pdoExc->getCode();
            echo "=getMessage=" . $pdoExc->getMessage();            
        }
    }    
    $connection->commit();
    echo "Save cities success";    
} catch (Exception $e) {
    echo "Exception" . $e->getMessage();
    $connection->rollback();
}
?>