<?php
header('Content-Type: application/json; charset=utf-8');
include('../global.php');
include('../anonim/dependency.php');  

function get_timezone_offset($remote_tz, $origin_tz = null) {
    if($origin_tz === null) {
        if(!is_string($origin_tz = 'Europe/London')) {
            return false; // A UTC timestamp was returned -- bail out!
        }
    }
    $origin_dtz = new DateTimeZone($origin_tz);
    $remote_dtz = new DateTimeZone($remote_tz);
    $origin_dt = new DateTime("now", $origin_dtz);
    $remote_dt = new DateTime("now", $remote_dtz);
    $offset = $origin_dtz->getOffset($origin_dt) - $remote_dtz->getOffset($remote_dt);
    return $offset;
}

function getGUID(){
    if (function_exists('com_create_guid')){
        return com_create_guid();
    }else{
        mt_srand((double)microtime()*10000);//optional for php 4.2.0 and up.
        $charid = strtoupper(md5(uniqid(rand(), true)));
        $hyphen = chr(45);// "-"
        $uuid = //chr(123)// "{"
             substr($charid, 0, 8).$hyphen
            .substr($charid, 8, 4).$hyphen
            .substr($charid,12, 4).$hyphen
            .substr($charid,16, 4).$hyphen
            .substr($charid,20,12);
            //.chr(125);// "}"
        return $uuid;
    }
}

function insertOrUpdateBodyFeeling($userid, $bf, &$insertStatement, &$updateStatement, &$selectStatement) {    
    echo "Userid=;".$userid."BF=".json_encode($bf)."\r\n";
    //$selectStatement->bindParam(':startdate', $bf['dt'], PDO::PARAM_STR);
    //$selectStatement->bindParam(':userid', $userid, PDO::PARAM_INT);            
    //$selectStatement->bindParam(':feelingtypeid', $bf['ftid'], PDO::PARAM_INT);
    //$selectStatement->bindParam(':bodyregionid', $bf['reg'], PDO::PARAM_INT);
    //$selectStatement->execute();
    //$row = $selectStatement->fetch();   
    $id = NULL;
    //$id = $row['id'];    
    echo "id=".$id.";\r\n";    
    if(is_null($id)){
        //insert
        $rowid = getGUID();
        //echo "rowid=".$rowid;
        $insertStatement->bindParam(':userid', $userid, PDO::PARAM_INT);
        $insertStatement->bindParam(':rowid', $rowid, PDO::PARAM_STR);
        $insertStatement->bindParam(':startdate', $bf['dt'], PDO::PARAM_STR);
        $insertStatement->bindParam(':feelingtypeid', $bf['ftid'], PDO::PARAM_INT);
        $insertStatement->bindParam(':customfeelingtypeid', $bf['cftid'], PDO::PARAM_INT);
        $insertStatement->bindParam(':bodyregionid', $bf['reg'], PDO::PARAM_INT);                        
        $insertStatement->bindParam(':x', $bf['x'], PDO::PARAM_INT);
        $insertStatement->bindParam(':y', $bf['y'], PDO::PARAM_INT);
        try {
            $result = $insertStatement->execute();
        }
        catch( PDOException $pdoExc ) {
            echo "=getCode=" . $pdoExc->getCode();
            echo "=getMessage=" . $pdoExc->getMessage();
        }
    }
    else {
        //update
        $updateStatement->bindParam(':userid', $userid, PDO::PARAM_INT);
        $updateStatement->bindParam(':id', $id, PDO::PARAM_INT);
        $updateStatement->bindParam(':startdate', $bf['dt'], PDO::PARAM_STR);
        $updateStatement->bindParam(':feelingtypeid', $bf['ftid'], PDO::PARAM_INT);
        $updateStatement->bindParam(':customfeelingtypeid', $bf['cftid'], PDO::PARAM_INT);
        $updateStatement->bindParam(':bodyregionid', $bf['reg'], PDO::PARAM_INT);
        $updateStatement->bindParam(':x', $bf['x'], PDO::PARAM_INT);
        $updateStatement->bindParam(':y', $bf['y'], PDO::PARAM_INT);            
        $result = $updateStatement->execute();
    }
}

$lat = 53.9;
$lng = 27.5667;
$cityid = 625144;
$diary = 1;
if (isset($_GET['diary'])) {
    $diary = $_GET['diary'];
}
if (isset($_GET['cityid'])) {
    $cityid = $_GET['cityid'];
}
if (isset($_GET['lat'])) {
    $lat = $_GET['lat'];
}
if (isset($_GET['lng'])) {
    $lng = $_GET['lng'];
}
if (isset($_GET['datefrom'])) {
    $dtUnixFrom = $_GET['datefrom'];    
} else {
    $dtUnixFrom = time() - 5*24*60*60;    
}

if (isset($_GET['dateto'])) {
    $dtUnixTo = $_GET['dateto'];    
} else {
    $dtUnixTo = time();
}
$dtFrom = getBeginDateFromUnixTime($dtUnixFrom);
$dtTo = getNextDateFromUnixTime($dtUnixTo);

$con=mysqli_connect($hostname,$username,$password,$dbName);
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$allData = array();

$allWeatherDailyData = array();
$result = mysqli_query($con,"SELECT * from tblweatherdaily where cityid=".$cityid." and dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "cityid" => $row['cityid'], "dt" => $row['dt'], "humidity" => $row['humidity'], "pressure" => $row['pressure'], "windspeed" => $row['windspeed'], "winddeg" => $row['winddeg'], "clouds" => $row['clouds'], "rain" => $row['rain'], "snow" => $row['snow'], "temp_min" => $row['temp_min'], "temp_max" => $row['temp_max'], "precipitation" => ($row['rain'] + $row['snow']), "temp" =>($row['temp_min'] + $row['temp_max']));
   $allWeatherDailyData[] = $partData;   
}

$allGeophysicsHourlyData = array();
$result = mysqli_query($con,"SELECT * from tblgeophysicshourly where dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "dt" => $row['dt'], "kpid" => $row['kpid'], "ap" => $row['ap']);
   $allGeophysicsHourlyData[] = $partData;   
}

$allGeophysicsDailyData = array();
$result = mysqli_query($con,"SELECT * from tblgeophysicsdaily where dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "dt" => $row['dt'], "kp" => $row['kp'], "ap" => $row['ap']);
   $allGeophysicsDailyData[] = $partData;   
}

$allPaticleDailyData = array();
$result = mysqli_query($con,"SELECT * from tblparticledaily where dt>='".$dtFrom."' and dt<='".$dtTo."' order by dt asc");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "dt" => $row['dt'], "proton1mev" => $row['proton1mev'], "proton10mev" => $row['proton10mev'], "proton100mev" => $row['proton100mev'], "electron08mev" => $row['electron08mev'], "electron2mev" => $row['electron2mev']);
   $allPaticleDailyData[] = $partData;   
}
mysqli_close($con);
$dependencySet = new DependencySet();
//$result = $dependencySet->generateDiaryFeeling($diary, $dtFrom, $dtTo, $allWeatherDailyData, $allPaticleDailyData, $allGeophysicsHourlyData);
//$json = json_encode($result);

global $dbName, $hostname, $username, $password;
try {
    $connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
    $connection->exec("set names utf8");
    $connection->exec("set time_zone='+00:00'"); 
    $connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $connection->beginTransaction();    
    
    $insertStatement = $connection->prepare("INSERT INTO tblbodyfeeling (rowid, userid, feelingtypeid, customfeelingtypeid, bodyregionid, x, y, startdate) VALUES (uuid_to_bin(:rowid), :userid, :feelingtypeid, :customfeelingtypeid, :bodyregionid, :x, :y, :startdate);");
    $updateStatement = $connection->prepare("UPDATE tblbodyfeeling SET startdate=:startdate, feelingtypeid = :feelingtypeid, customfeelingtypeid = :customfeelingtypeid, bodyregionid = :bodyregionid, x=:x, y=:y WHERE id = :id and userid = :userid;");
    $selectStatement = $connection->prepare("select id FROM tblbodyfeeling WHERE userid = :userid and feelingtypeid = :feelingtypeid and bodyregionid = :bodyregionid and startdate=:startdate;");
    
    for($diary = 1; $diary <=14; $diary++){
        $data = $dependencySet->generateDiaryFeeling($diary, $dtFrom, $dtTo, $allWeatherDailyData, $allPaticleDailyData, $allGeophysicsHourlyData);
        //$json = json_encode($data);
        foreach ($data as $bf) {
            $maxUserCount = rand(5,10);
            $curUserCount = 0;            
            for ($userid = 38; $userid <= 57; $userid++) {
                if($curUserCount > $maxUserCount)  break;            
                $curUserCount++;
                insertOrUpdateBodyFeeling($userid, $bf, $insertStatement, $updateStatement, $selectStatement);                                    
            }
        }
            
    }
    $connection->commit();
} catch (Exception $e) {
    echo "Exception" . $e->getMessage();
    $connection->rollback();
}

?>