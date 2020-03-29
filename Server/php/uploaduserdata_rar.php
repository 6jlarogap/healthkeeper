<?php
header('Content-Type: application/json; charset=utf-8');
include('checklogin.php');
//убирается экранирование строк в приходящих параметрах
if (get_magic_quotes_gpc()) {
    $process = array(&$_GET, &$_POST, &$_COOKIE, &$_REQUEST);
    while (list($key, $val) = each($process)) {
        foreach ($val as $k => $v) {
            unset($process[$key][$k]);
            if (is_array($v)) {
                $process[$key][stripslashes($k)] = $v;
                $process[] = &$process[$key][stripslashes($k)];
            } else {
                $process[$key][stripslashes($k)] = stripslashes($v);
            }
        }
    }
    unset($process);
}
error_reporting(E_ERROR | E_PARSE);
$con=mysqli_connect($hostname,$username,$password,$dbName);
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
//$userId
if (isset($_POST['data'])) {
    $json = $_POST['data'];
}

define("INSERT_OPERATION_TYPE", 1);
define("UPDATE_OPERATION_TYPE", 2);
define("DELETE_OPERATION_TYPE", 3);

$arrayData = json_decode($json, True);
global $dbName, $hostname, $username, $password;
try {
    $connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
    $connection->exec("set names utf8");
    $connection->exec("set time_zone='+00:00'"); 
    $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $connection->beginTransaction();
    
    //custombodyfeelingtype
    $arrayCustomBodyFeelingType = &$arrayData['bodytype'];
    $insertStatement = $connection->prepare("INSERT INTO tblcustombodyfeelingtype (rowid, userid, name) VALUES (uuid_to_bin(:rowid), :userid, :name);");
    $updateStatement = $connection->prepare("UPDATE tblcustombodyfeelingtype SET name=:name WHERE id = :id and userid = :userid;");
    $deleteStatement = $connection->prepare("DELETE FROM tblcustombodyfeelingtype WHERE rowid = uuid_to_bin(:rowid) and id = :id and userid = :userid;");
    foreach ($arrayCustomBodyFeelingType as &$item) {                
        if($item['op'] == constant("INSERT_OPERATION_TYPE")){
            $insertStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $insertStatement->bindParam(':rowid', $item['rid'], PDO::PARAM_STR);
            $insertStatement->bindParam(':name', $item['name'], PDO::PARAM_STR);                                               
            try {
                $result = $insertStatement->execute();                 
                $itemId = $connection->lastInsertId();        
                $item['id'] = $itemId;                
            }
            catch( PDOException $pdoExc ) {
                /*echo "=getCode=" . $pdoExc->getCode();
                echo "=getMessage=" . $pdoExc->getMessage();*/                
                $queryByUUID = $connection->prepare("select * FROM tblcustombodyfeelingtype where rowid = uuid_to_bin('". $item['rid'] ."') and userid = " . $userId);
                $queryByUUID->execute();      
                $rowByUUID = $queryByUUID->fetch();
                $item['id'] = $rowByUUID['id'];
                $item['name'] = $rowByUUID['name'];
            }
            
        }
        if($item['op'] == constant("UPDATE_OPERATION_TYPE")){
            $updateStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $updateStatement->bindParam(':id', $item['id'], PDO::PARAM_INT);
            $updateStatement->bindParam(':name', $item['name'], PDO::PARAM_STR);            
            $result = $updateStatement->execute();            
        }
        if($item['op'] == constant("DELETE_OPERATION_TYPE")){
            $deleteStatement->bindParam(':rowid', $item['rid'], PDO::PARAM_STR);
            $deleteStatement->bindParam(':userid', $userId, PDO::PARAM_INT);            
            $deleteStatement->bindParam(':id', $item['id'], PDO::PARAM_INT);
            $result = $deleteStatement->execute();           
        }
    }
    
    //customcommonfeelingtype
    $arrayCustomCommonFeelingType = &$arrayData['commontype'];
    $insertStatement = $connection->prepare("INSERT INTO tblcustomcommonfeelingtype (rowid, userid, name, feelinggroupid, ordinalnumber, status, unitid) VALUES (uuid_to_bin(:rowid), :userid, :name, :feelinggroupid, :ordinalnumber, :status, :unitid);");
    $updateStatement = $connection->prepare("UPDATE tblcustomcommonfeelingtype SET name=:name, feelinggroupid=:feelinggroupid, ordinalnumber=:ordinalnumber, status=:status, unitid=:unitid WHERE id = :id and userid = :userid;");
    $deleteStatement = $connection->prepare("DELETE FROM tblcustomcommonfeelingtype WHERE rowid = uuid_to_bin(:rowid) and id = :id and userid = :userid;");
    foreach ($arrayCustomCommonFeelingType as &$item) {                
        if($item['op'] == constant("INSERT_OPERATION_TYPE")){
            $insertStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $insertStatement->bindParam(':feelinggroupid', $item['fgid'], PDO::PARAM_INT);
            $insertStatement->bindParam(':ordinalnumber', $item['num'], PDO::PARAM_INT);
            $insertStatement->bindParam(':status', $item['status'], PDO::PARAM_INT);
            $insertStatement->bindParam(':unitid', $item['unitid'], PDO::PARAM_INT);
            $insertStatement->bindParam(':rowid', $item['rid'], PDO::PARAM_STR);
            $insertStatement->bindParam(':name', $item['name'], PDO::PARAM_STR);                                               
            try {
                $result = $insertStatement->execute();                 
                $itemId = $connection->lastInsertId();        
                $item['id'] = $itemId;                
            }
            catch( PDOException $pdoExc ) {
                /*echo "=getCode=" . $pdoExc->getCode();
                echo "=getMessage=" . $pdoExc->getMessage();*/                
                $queryByUUID = $connection->prepare("select * FROM tblcustomcommonfeelingtype where rowid = uuid_to_bin('". $item['rid'] ."') and userid = " . $userId);
                $queryByUUID->execute();      
                $rowByUUID = $queryByUUID->fetch();
                $item['id'] = $rowByUUID['id'];
                $item['fgid'] = $rowByUUID['feelinggroupid'];
                $item['num'] = $rowByUUID['ordinalnumber'];
                $item['unitid'] = $rowByUUID['unitid'];
                $item['status'] = $rowByUUID['status'];
                $item['name'] = $rowByUUID['name'];
            }
            
        }
        if($item['op'] == constant("UPDATE_OPERATION_TYPE")){
            $updateStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $updateStatement->bindParam(':id', $item['id'], PDO::PARAM_INT);
            $updateStatement->bindParam(':feelinggroupid', $item['fgid'], PDO::PARAM_INT);
            $updateStatement->bindParam(':ordinalnumber', $item['num'], PDO::PARAM_INT);
            $updateStatement->bindParam(':unitid', $item['unitid'], PDO::PARAM_INT);
            $updateStatement->bindParam(':status', $item['status'], PDO::PARAM_INT);
            $updateStatement->bindParam(':name', $item['name'], PDO::PARAM_STR);            
            $result = $updateStatement->execute();            
        }
        if($item['op'] == constant("DELETE_OPERATION_TYPE")){
            $deleteStatement->bindParam(':rowid', $item['rid'], PDO::PARAM_STR);
            $deleteStatement->bindParam(':userid', $userId, PDO::PARAM_INT);            
            $deleteStatement->bindParam(':id', $item['id'], PDO::PARAM_INT);
            $result = $deleteStatement->execute();           
        }
    }
    
    //customfactortypetype
    $arrayCustomFactorType = &$arrayData['factortype'];
    $insertStatement = $connection->prepare("INSERT INTO tblcustomfactortype (rowid, userid, factorgroupid, name, ordinalnumber, status, unitid) VALUES (uuid_to_bin(:rowid), :userid, :factorgroupid, :name, :ordinalnumber, :status, :unitid);");
    $updateStatement = $connection->prepare("UPDATE tblcustomfactortype SET factorgroupid=:factorgroupid, name=:name, ordinalnumber=:ordinalnumber, status=:status, unitid=:unitid WHERE id = :id and userid = :userid;");
    $deleteStatement = $connection->prepare("DELETE FROM tblcustomfactortype WHERE rowid = uuid_to_bin(:rowid) and id = :id and userid = :userid;");
    foreach ($arrayCustomFactorType as &$item) {                
        if($item['op'] == constant("INSERT_OPERATION_TYPE")){
            $insertStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $insertStatement->bindParam(':rowid', $item['rid'], PDO::PARAM_STR);
            $insertStatement->bindParam(':factorgroupid', $item['fgid'], PDO::PARAM_STR);
            $insertStatement->bindParam(':name', $item['name'], PDO::PARAM_STR);
            $insertStatement->bindParam(':ordinalnumber', $item['num'], PDO::PARAM_INT);
            $insertStatement->bindParam(':unitid', $item['unitid'], PDO::PARAM_INT);
            $insertStatement->bindParam(':status', $item['status'], PDO::PARAM_INT);
            try {
                $result = $insertStatement->execute();                 
                $itemId = $connection->lastInsertId();        
                $item['id'] = $itemId;                
            }
            catch( PDOException $pdoExc ) {
                echo "=getCode=" . $pdoExc->getCode();
                echo "=getMessage=" . $pdoExc->getMessage();
                $queryByUUID = $connection->prepare("select * FROM tblcustomfactortype where rowid = uuid_to_bin('". $item['rid'] ."') and userid = " . $userId);
                $queryByUUID->execute();      
                $rowByUUID = $queryByUUID->fetch();
                $item['id'] = $rowByUUID['id'];
                $item['fgid'] = $rowByUUID['factorgroupid'];
                $item['name'] = $rowByUUID['name'];
                $item['num'] = $rowByUUID['ordinalnumber'];
                $item['unitid'] = $rowByUUID['unitid'];
                $item['status'] = $rowByUUID['status'];
            }
            
        }
        if($item['op'] == constant("UPDATE_OPERATION_TYPE")){
            $updateStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $updateStatement->bindParam(':id', $item['id'], PDO::PARAM_INT);
            $updateStatement->bindParam(':factorgroupid', $item['fgid'], PDO::PARAM_INT);
            $updateStatement->bindParam(':name', $item['name'], PDO::PARAM_STR);
            $updateStatement->bindParam(':ordinalnumber', $item['num'], PDO::PARAM_INT);
            $updateStatement->bindParam(':status', $item['status'], PDO::PARAM_INT);
            $updateStatement->bindParam(':unitid', $item['unitid'], PDO::PARAM_INT);
            $result = $updateStatement->execute();            
        }
        if($item['op'] == constant("DELETE_OPERATION_TYPE")){
            $deleteStatement->bindParam(':rowid', $item['rid'], PDO::PARAM_STR);
            $deleteStatement->bindParam(':userid', $userId, PDO::PARAM_INT);            
            $deleteStatement->bindParam(':id', $item['id'], PDO::PARAM_INT);
            $result = $deleteStatement->execute();           
        }
    }
    
    
    //bodyfeeling
    $arrayBodyFeeling = &$arrayData['bf'];
    $insertStatement = $connection->prepare("INSERT INTO tblbodyfeeling (rowid, userid, feelingtypeid, customfeelingtypeid, bodyregionid, x, y, startdate) VALUES (uuid_to_bin(:rowid), :userid, :feelingtypeid, :customfeelingtypeid, :bodyregionid, :x, :y, :startdate);");
    $updateStatement = $connection->prepare("UPDATE tblbodyfeeling SET startdate=:startdate, feelingtypeid = :feelingtypeid, customfeelingtypeid = :customfeelingtypeid, bodyregionid = :bodyregionid, x=:x, y=:y WHERE id = :id and userid = :userid;");
    $deleteStatement = $connection->prepare("DELETE FROM tblbodyfeeling WHERE rowid = uuid_to_bin(:rowid) and id = :id and userid = :userid;");
    foreach ($arrayBodyFeeling as &$bodyFeeling) {                
        if($bodyFeeling['op'] == constant("INSERT_OPERATION_TYPE")){
            $insertStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $insertStatement->bindParam(':rowid', $bodyFeeling['rid'], PDO::PARAM_STR);
            $insertStatement->bindParam(':startdate', $bodyFeeling['dt'], PDO::PARAM_STR);
            $insertStatement->bindParam(':feelingtypeid', $bodyFeeling['ftid'], PDO::PARAM_INT);
            $insertStatement->bindParam(':customfeelingtypeid', $bodyFeeling['cftid'], PDO::PARAM_INT);
            $insertStatement->bindParam(':bodyregionid', $bodyFeeling['reg'], PDO::PARAM_INT);                        
            $insertStatement->bindParam(':x', $bodyFeeling['x'], PDO::PARAM_INT);
            $insertStatement->bindParam(':y', $bodyFeeling['y'], PDO::PARAM_INT);
            try {
                $result = $insertStatement->execute();                 
                $bodyFeelingId = $connection->lastInsertId();        
                $bodyFeeling['id'] = $bodyFeelingId;                
            }
            catch( PDOException $pdoExc ) {
                /*echo "=getCode=" . $pdoExc->getCode();
                echo "=getMessage=" . $pdoExc->getMessage();*/                
                $queryByUUID = $connection->prepare("select * FROM tblbodyfeeling where rowid = uuid_to_bin('". $bodyFeeling['rid'] ."') and userid = " . $userId);
                $queryByUUID->execute();      
                $rowByUUID = $queryByUUID->fetch();
                $bodyFeeling['id'] = $rowByUUID['id'];
                $bodyFeeling['dt'] = $rowByUUID['startdate'];
                $bodyFeeling['ftid'] = $rowByUUID['feelingtypeid'];
                $bodyFeeling['cftid'] = $rowByUUID['customfeelingtypeid'];
                $bodyFeeling['reg'] = $rowByUUID['bodyregionid'];                
                $bodyFeeling['x'] = $rowByUUID['x'];
                $bodyFeeling['y'] = $rowByUUID['y'];                
            }
            
        }
        if($bodyFeeling['op'] == constant("UPDATE_OPERATION_TYPE")){
            $updateStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $updateStatement->bindParam(':id', $bodyFeeling['id'], PDO::PARAM_INT);
            $updateStatement->bindParam(':startdate', $bodyFeeling['dt'], PDO::PARAM_STR);
            $updateStatement->bindParam(':feelingtypeid', $bodyFeeling['ftid'], PDO::PARAM_INT);
            $updateStatement->bindParam(':customfeelingtypeid', $bodyFeeling['cftid'], PDO::PARAM_INT);
            $updateStatement->bindParam(':bodyregionid', $bodyFeeling['reg'], PDO::PARAM_INT);
            $updateStatement->bindParam(':x', $bodyFeeling['x'], PDO::PARAM_INT);
            $updateStatement->bindParam(':y', $bodyFeeling['y'], PDO::PARAM_INT);
            
            $result = $updateStatement->execute();            
        }
        if($bodyFeeling['op'] == constant("DELETE_OPERATION_TYPE")){
            $deleteStatement->bindParam(':rowid', $bodyFeeling['rid'], PDO::PARAM_STR);
            $deleteStatement->bindParam(':userid', $userId, PDO::PARAM_INT);            
            $deleteStatement->bindParam(':id', $bodyFeeling['id'], PDO::PARAM_INT);
            $result = $deleteStatement->execute();           
        }
    }
    
    //userbodyfeelingtype
    $arrayUserBodyFeelingType = &$arrayData['ubft'];
    $insertStatement = $connection->prepare("INSERT INTO tbluserbodyfeelingtype (rowid, userid, feelingtypeid, color) VALUES (uuid_to_bin(:rowid), :userid, :feelingtypeid, :color);");
    $updateStatement = $connection->prepare("UPDATE tbluserbodyfeelingtype SET feelingtypeid=:feelingtypeid, color=:color WHERE id = :id and userid = :userid;");
    $deleteStatement = $connection->prepare("DELETE FROM tbluserbodyfeelingtype WHERE rowid = uuid_to_bin(:rowid) and id = :id and userid = :userid;");
    foreach ($arrayUserBodyFeelingType as &$userBodyFeelingType) {                
        if($userBodyFeelingType['op'] == constant("INSERT_OPERATION_TYPE")){
            $insertStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $insertStatement->bindParam(':rowid', $userBodyFeelingType['rid'], PDO::PARAM_STR);
            $insertStatement->bindParam(':feelingtypeid', $userBodyFeelingType['ftid'], PDO::PARAM_INT);
            $insertStatement->bindParam(':color', $userBodyFeelingType['color'], PDO::PARAM_INT);                       
            try {
                $result = $insertStatement->execute();                 
                $userBodyFeelingTypeId = $connection->lastInsertId();        
                $userBodyFeelingType['id'] = $userBodyFeelingTypeId;                
            }
            catch( PDOException $pdoExc ) {                          
                $queryByUUID = $connection->prepare("select * FROM tbluserbodyfeelingtype where rowid = uuid_to_bin('". $userBodyFeelingType['rid'] ."') and userid = " . $userId);
                $queryByUUID->execute();      
                $rowByUUID = $queryByUUID->fetch();
                $userBodyFeelingType['id'] = $rowByUUID['id'];
                $userBodyFeelingType['ftid'] = $rowByUUID['feelingtypeid'];
                $userBodyFeelingType['color'] = $rowByUUID['color'];                                
            }
        }
        if($userBodyFeelingType['op'] == constant("UPDATE_OPERATION_TYPE")){
            $updateStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $updateStatement->bindParam(':id', $userBodyFeelingType['id'], PDO::PARAM_INT);
            $updateStatement->bindParam(':feelingtypeid', $userBodyFeelingType['ftid'], PDO::PARAM_INT);
            $updateStatement->bindParam(':color', $userBodyFeelingType['color'], PDO::PARAM_INT);
            $result = $updateStatement->execute();            
        }
        if($userBodyFeelingType['op'] == constant("DELETE_OPERATION_TYPE")){
            $deleteStatement->bindParam(':rowid', $userBodyFeelingType['rid'], PDO::PARAM_STR);
            $deleteStatement->bindParam(':userid', $userId, PDO::PARAM_INT);            
            $deleteStatement->bindParam(':id', $userBodyFeelingType['id'], PDO::PARAM_INT);
            $result = $deleteStatement->execute();           
        }
    }
    
    //commonfeeling
    $arrayCommonFeeling = &$arrayData['c'];
    $insertStatement = $connection->prepare("INSERT INTO tblcommonfeeling (rowid, userid, feelingtypeid, customfeelingtypeid, value1, value2, value3, startdate) VALUES (uuid_to_bin(:rowid), :userid, :feelingtypeid, :customfeelingtypeid, :value1, :value2, :value3, :startdate);");
    $updateStatement = $connection->prepare("UPDATE tblcommonfeeling SET startdate=:startdate, value1=:value1, value2=:value2, value3=:value3, feelingtypeid = :feelingtypeid, customfeelingtypeid = :customfeelingtypeid WHERE id = :id and userid = :userid;");
    $deleteStatement = $connection->prepare("DELETE FROM tblcommonfeeling WHERE rowid = uuid_to_bin(:rowid) and id = :id and userid = :userid;");
    foreach ($arrayCommonFeeling as &$commonFeeling) {                
        if($commonFeeling['op'] == constant("INSERT_OPERATION_TYPE")){
            $insertStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $insertStatement->bindParam(':rowid', $commonFeeling['rid'], PDO::PARAM_STR);
            $insertStatement->bindParam(':startdate', $commonFeeling['dt'], PDO::PARAM_STR);
            $insertStatement->bindParam(':feelingtypeid', $commonFeeling['ftid'], PDO::PARAM_INT);
            $insertStatement->bindParam(':customfeelingtypeid', $commonFeeling['cftid'], PDO::PARAM_INT);
            $insertStatement->bindParam(':value1', $commonFeeling['v1'], PDO::PARAM_STR);
            $insertStatement->bindParam(':value2', $commonFeeling['v2'], PDO::PARAM_STR);
            $insertStatement->bindParam(':value3', $commonFeeling['v3'], PDO::PARAM_STR);            
            try {
                $result = $insertStatement->execute();                 
                $commonFeelingId = $connection->lastInsertId();        
                $commonFeeling['id'] = $commonFeelingId;                
            }
            catch( PDOException $pdoExc ) {
                /*echo "=getCode=" . $pdoExc->getCode();
                echo "=getMessage=" . $pdoExc->getMessage();*/                
                $queryByUUID = $connection->prepare("select * FROM tblcommonfeeling where rowid = uuid_to_bin('". $commonFeeling['rid'] ."') and userid = " . $userId);
                $queryByUUID->execute();      
                $rowByUUID = $queryByUUID->fetch();
                $commonFeeling['id'] = $rowByUUID['id'];
                $commonFeeling['dt'] = $rowByUUID['startdate'];
                $commonFeeling['ftid'] = $rowByUUID['feelingtypeid'];
                $commonFeeling['cftid'] = $rowByUUID['customfeelingtypeid'];
                $commonFeeling['v1'] = $rowByUUID['value1'];
                $commonFeeling['v2'] = $rowByUUID['value2'];
                $commonFeeling['v3'] = $rowByUUID['value3'];
            }
            
        }
        if($commonFeeling['op'] == constant("UPDATE_OPERATION_TYPE")){
            $updateStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $updateStatement->bindParam(':id', $commonFeeling['id'], PDO::PARAM_INT);
            $updateStatement->bindParam(':startdate', $commonFeeling['dt'], PDO::PARAM_STR);
            $updateStatement->bindParam(':feelingtypeid', $commonFeeling['ftid'], PDO::PARAM_INT);
            $updateStatement->bindParam(':customfeelingtypeid', $commonFeeling['cftid'], PDO::PARAM_INT);
            $updateStatement->bindParam(':value1', $commonFeeling['v1'], PDO::PARAM_STR);
            $updateStatement->bindParam(':value2', $commonFeeling['v2'], PDO::PARAM_STR);
            $updateStatement->bindParam(':value3', $commonFeeling['v3'], PDO::PARAM_STR);
            $result = $updateStatement->execute();            
        }
        if($commonFeeling['op'] == constant("DELETE_OPERATION_TYPE")){
            $deleteStatement->bindParam(':rowid', $commonFeeling['rid'], PDO::PARAM_STR);
            $deleteStatement->bindParam(':userid', $userId, PDO::PARAM_INT);            
            $deleteStatement->bindParam(':id', $commonFeeling['id'], PDO::PARAM_INT);
            $result = $deleteStatement->execute();           
        }
    }
	
	//factor
    $arrayFactor = &$arrayData['f'];
    $insertStatement = $connection->prepare("INSERT INTO tblfactor (rowid, userid, factortypeid, customfactortypeid, value1, value2, value3, startdate) VALUES (uuid_to_bin(:rowid), :userid, :factortypeid, :customfactortypeid, :value1, :value2, :value3, :startdate);");
    $updateStatement = $connection->prepare("UPDATE tblfactor SET startdate=:startdate, value1=:value1, value2=:value2, value3=:value3, factortypeid = :factortypeid, customfactortypeid = :customfactortypeid WHERE id = :id and userid = :userid;");
    $deleteStatement = $connection->prepare("DELETE FROM tblfactor WHERE rowid = uuid_to_bin(:rowid) and id = :id and userid = :userid;");
    foreach ($arrayFactor as &$item) {                
        if($item['op'] == constant("INSERT_OPERATION_TYPE")){
            $insertStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $insertStatement->bindParam(':rowid', $item['rid'], PDO::PARAM_STR);
            $insertStatement->bindParam(':startdate', $item['dt'], PDO::PARAM_STR);
            $insertStatement->bindParam(':factortypeid', $item['ftid'], PDO::PARAM_INT);
            $insertStatement->bindParam(':customfactortypeid', $item['cfid'], PDO::PARAM_INT);
            $insertStatement->bindParam(':value1', $item['v1'], PDO::PARAM_STR);
            $insertStatement->bindParam(':value2', $item['v2'], PDO::PARAM_STR);
            $insertStatement->bindParam(':value3', $item['v3'], PDO::PARAM_STR);
            try {
                $result = $insertStatement->execute();                 
                $itemId = $connection->lastInsertId();        
                $item['id'] = $itemId;                
            }
            catch( PDOException $pdoExc ) {
                /*echo "=getCode=" . $pdoExc->getCode();
                echo "=getMessage=" . $pdoExc->getMessage();*/                
                $queryByUUID = $connection->prepare("select * FROM tblfactor where rowid = uuid_to_bin('". $item['rid'] ."') and userid = " . $userId);
                $queryByUUID->execute();      
                $rowByUUID = $queryByUUID->fetch();
                $item['id'] = $rowByUUID['id'];
                $item['dt'] = $rowByUUID['startdate'];
                $item['ftid'] = $rowByUUID['factortypeid'];
                $item['cfid'] = $rowByUUID['customfactortypeid'];
                $item['v1'] = $rowByUUID['value1'];
                $item['v2'] = $rowByUUID['value2'];
                $item['v3'] = $rowByUUID['value3'];
            }
            
        }
        if($item['op'] == constant("UPDATE_OPERATION_TYPE")){
            $updateStatement->bindParam(':userid', $userId, PDO::PARAM_INT);
            $updateStatement->bindParam(':id', $item['id'], PDO::PARAM_INT);
            $updateStatement->bindParam(':startdate', $item['dt'], PDO::PARAM_STR);
            $updateStatement->bindParam(':factortypeid', $item['ftid'], PDO::PARAM_INT);
            $updateStatement->bindParam(':customfactortypeid', $item['cfid'], PDO::PARAM_INT);
            $updateStatement->bindParam(':value1', $item['v1'], PDO::PARAM_STR);
            $updateStatement->bindParam(':value2', $item['v2'], PDO::PARAM_STR);
            $updateStatement->bindParam(':value3', $item['v3'], PDO::PARAM_STR);
            $result = $updateStatement->execute();            
        }
        if($item['op'] == constant("DELETE_OPERATION_TYPE")){
            $deleteStatement->bindParam(':rowid', $item['rid'], PDO::PARAM_STR);
            $deleteStatement->bindParam(':userid', $userId, PDO::PARAM_INT);            
            $deleteStatement->bindParam(':id', $item['id'], PDO::PARAM_INT);
            $result = $deleteStatement->execute();           
        }
    }
    
    //Другие таблицы ...
    
    $connection->commit();
} catch (Exception $e) {
    echo "Exception" . $e->getMessage();
    $connection->rollback();
}
$json = json_encode($arrayData);
echo $json;

?>