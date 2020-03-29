<?php
header('Content-Type: application/json; charset=utf-8');
include('checklogin.php');

$con=mysqli_connect($hostname,$username,$password,$dbName);
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
mysqli_query($con,"set names utf8");
mysqli_query($con,"set time_zone='+00:00'");
$syncDate = 0;
if (isset($_GET['syncdate'])) {
    $syncDate = $_GET['syncdate'];
}
if (isset($_GET['datefrom'])) {
    $dtUnixFrom = $_GET['datefrom'];    
} else {
    $dtUnixFrom = time() - 60*24*60*60;    
}
if (isset($_GET['dateto'])) {
    $dtUnixTo = $_GET['dateto'];    
} else {
    $dtUnixTo = time() + 60*24*60*60;
}
$dtFrom = getBeginDateFromUnixTime($dtUnixFrom);
$dtTo = getNextDateFromUnixTime($dtUnixTo);

$allData = array();
$customBodyFeelingTypeData = array();
$customCommonFeelingTypeData = array();
$customFactorTypeData = array();
$factorData = array();
$bodyFeelingData = array();
$userBodyFeelingTypeData = array();
$commonFeelingData = array();
$psyhologicalFeelingData = array();

$result = mysqli_query($con,"select UNIX_TIMESTAMP()");
$row = mysqli_fetch_array($result);
$curUnixDate = $row[0];
//custombodyfeelingtype
$result = mysqli_query($con,"SELECT op.id, uuid_from_bin(op.rowid) as rowid, c.userid, c.name, op.changeddate, op.operationtypeid FROM `tblcustombodyfeelingtype` c
    left join tbljournaloperation op on op.rowid = c.rowid and op.id = c.id
    where op.userid = ".$userId." and UNIX_TIMESTAMP(op.changeddate) > " . $syncDate);
if (!$result) {
    echo 'Database error - custombodyfeelingtype: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "rowid" => $row['rowid'], "userid" => $row['userid'], "name" => $row['name'], "operation" => $row['operationtypeid']);
   $customBodyFeelingTypeData[] = $partData;   
}

//customcommonfeelingtype
$result = mysqli_query($con,"SELECT op.id, uuid_from_bin(op.rowid) as rowid, c.userid, c.name, c.feelinggroupid, c.ordinalnumber, c.status, c.unitid, op.changeddate, op.operationtypeid FROM `tblcustomcommonfeelingtype` c
    left join tbljournaloperation op on op.rowid = c.rowid and op.id = c.id
    where op.userid = ".$userId." and UNIX_TIMESTAMP(op.changeddate) > " . $syncDate);
if (!$result) {
    echo 'Database error - customcommonfeelingtype: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "rowid" => $row['rowid'], "userid" => $row['userid'], "feelinggroupid" => $row['feelinggroupid'], "ordinalnumber" => $row['ordinalnumber'], "name" => $row['name'], "status" => $row['status'], "unitid" => $row['unitid'], "operation" => $row['operationtypeid']);
   $customCommonFeelingTypeData[] = $partData;   
}

//customfactortype
$result = mysqli_query($con,"SELECT op.id, uuid_from_bin(op.rowid) as rowid, c.factorgroupid, c.userid, c.name, c.ordinalnumber, c.status, c.unitid, op.changeddate, op.operationtypeid FROM `tblcustomfactortype` c
    left join tbljournaloperation op on op.rowid = c.rowid and op.id = c.id
    where op.userid = ".$userId." and UNIX_TIMESTAMP(op.changeddate) > " . $syncDate);
if (!$result) {
    echo 'Database error - customfactortype: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "rowid" => $row['rowid'], "userid" => $row['userid'], "factorgroupid" => $row['factorgroupid'], "name" => $row['name'], "ordinalnumber" => $row['ordinalnumber'], "status" => $row['status'], "unitid" => $row['unitid'], "operation" => $row['operationtypeid']);
   $customFactorTypeData[] = $partData;   
}


//bodyfeeling
$result = mysqli_query($con,"SELECT op.id, uuid_from_bin(op.rowid) as rowid, b.startdate, b.feelingtypeid, b.customfeelingtypeid, b.bodyregionid, b.x, b.y, op.changeddate, op.operationtypeid FROM `tblbodyfeeling` b
    left join tbljournaloperation op on op.rowid = b.rowid and op.id = b.id
    where op.userid = ".$userId." and UNIX_TIMESTAMP(op.changeddate) > " . $syncDate);
if (!$result) {
    echo 'Database error - bodyfeeling: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "userid" => $userId, "rowid" => $row['rowid'], "startdate" => $row['startdate'], "feelingtypeid" => $row['feelingtypeid'], "bodyregionid" => $row['bodyregionid'], "customfeelingtypeid" => $row['customfeelingtypeid'], "x" => $row['x'], "y" => $row['y'], "operation" => $row['operationtypeid']);
   $bodyFeelingData[] = $partData;   
}

//userBodyFeelingType
$result = mysqli_query($con,"SELECT op.id, uuid_from_bin(op.rowid) as rowid, b.feelingtypeid, b.color, op.changeddate, op.operationtypeid FROM `tbluserbodyfeelingtype` b
    left join tbljournaloperation op on op.rowid = b.rowid and op.id = b.id
    where op.userid = ".$userId." and UNIX_TIMESTAMP(op.changeddate) > " . $syncDate);
if (!$result) {
    echo 'Database error - userBodyFeelingType: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "userid" => $userId, "rowid" => $row['rowid'], "feelingtypeid" => $row['feelingtypeid'], "color" => $row['color'], "operation" => $row['operationtypeid']);
   $userBodyFeelingTypeData[] = $partData;
}

//commonfeeling
$result = mysqli_query($con,"SELECT op.id, uuid_from_bin(op.rowid) as rowid, c.startdate, c.feelingtypeid, c.customfeelingtypeid, c.value1, c.value2, c.value3, op.changeddate, op.operationtypeid FROM `tblcommonfeeling` c
    left join tbljournaloperation op on op.rowid = c.rowid and op.id = c.id
    where op.userid = ".$userId." and UNIX_TIMESTAMP(op.changeddate) > " . $syncDate);
if (!$result) {
    echo 'Database error - commonfeeling: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "userid" => $userId, "rowid" => $row['rowid'], "startdate" => $row['startdate'], "feelingtypeid" => $row['feelingtypeid'], "customfeelingtypeid" => $row['customfeelingtypeid'], "value1" => $row['value1'], "value2" => $row['value2'], "value3" => $row['value3'], "operation" => $row['operationtypeid']);
   $commonFeelingData[] = $partData;   
}

//factor
$result = mysqli_query($con,"SELECT op.id, uuid_from_bin(op.rowid) as rowid, f.startdate, f.factortypeid, f.customfactortypeid, f.value1, f.value2, f.value3, op.changeddate, op.operationtypeid FROM `tblfactor` f
    left join tbljournaloperation op on op.rowid = f.rowid and op.id = f.id
    where op.userid = ".$userId." and UNIX_TIMESTAMP(op.changeddate) > " . $syncDate);
if (!$result) {
    echo 'Database error - factor: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("id" => $row['id'], "userid" => $userId, "rowid" => $row['rowid'], "startdate" => $row['startdate'], "factortypeid" => $row['factortypeid'], "customfactortypeid" => $row['customfactortypeid'], "value1" => $row['value1'], "value2" => $row['value2'], "value3" => $row['value3'], "operation" => $row['operationtypeid']);
   $factorData[] = $partData;   
}


mysqli_close($con);
$allData = array("unixsyncdate" => $curUnixDate, "custombodyfeelingtype" => $customBodyFeelingTypeData, "customcommonfeelingtype" => $customCommonFeelingTypeData, "customfactortype" => $customFactorTypeData, "bodyfeeling" => $bodyFeelingData, "userbodyfeelingtype" => $userBodyFeelingTypeData, "commonfeeling" => $commonFeelingData, "factor" => $factorData);
$json = json_encode($allData);
echo $json;
?>