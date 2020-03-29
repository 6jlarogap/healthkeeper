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
   $partData = array("id" => $row['id'], "rid" => $row['rowid'], "uid" => $row['userid'], "name" => $row['name'], "op" => $row['operationtypeid']);
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
   $partData = array("id" => $row['id'], "rid" => $row['rowid'], "uid" => $row['userid'], "fgid" => $row['feelinggroupid'], "num" => $row['ordinalnumber'], "name" => $row['name'], "status" => $row['status'], "unitid" => $row['unitid'], "op" => $row['operationtypeid']);
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
   $partData = array("id" => $row['id'], "rid" => $row['rowid'], "uid" => $row['userid'], "fgid" => $row['factorgroupid'], "name" => $row['name'], "num" => $row['ordinalnumber'], "status" => $row['status'], "unitid" => $row['unitid'], "op" => $row['operationtypeid']);
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
   $partData = array("id" => $row['id'], "uid" => $userId, "rid" => $row['rowid'], "dt" => $row['startdate'], "ftid" => $row['feelingtypeid'], "reg" => $row['bodyregionid'], "cftid" => $row['customfeelingtypeid'], "x" => $row['x'], "y" => $row['y'], "op" => $row['operationtypeid']);
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
   $partData = array("id" => $row['id'], "uid" => $userId, "rid" => $row['rowid'], "ftid" => $row['feelingtypeid'], "color" => $row['color'], "op" => $row['operationtypeid']);
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
   $partData = array("id" => $row['id'], "uid" => $userId, "rid" => $row['rowid'], "dt" => $row['startdate'], "ftid" => $row['feelingtypeid'], "cftid" => $row['customfeelingtypeid'], "v1" => $row['value1'], "v2" => $row['value2'], "v3" => $row['value3'], "op" => $row['operationtypeid']);
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
   $partData = array("id" => $row['id'], "uid" => $userId, "rid" => $row['rowid'], "dt" => $row['startdate'], "ftid" => $row['factortypeid'], "cfid" => $row['customfactortypeid'], "v1" => $row['value1'], "v2" => $row['value2'], "v3" => $row['value3'], "op" => $row['operationtypeid']);
   $factorData[] = $partData;   
}


mysqli_close($con);
$allData = array("udate" => $curUnixDate, "bodytype" => $customBodyFeelingTypeData, "commontype" => $customCommonFeelingTypeData, "factortype" => $customFactorTypeData, "bf" => $bodyFeelingData, "ubft" => $userBodyFeelingTypeData, "c" => $commonFeelingData, "f" => $factorData);
$json = json_encode($allData);
echo $json;
?>