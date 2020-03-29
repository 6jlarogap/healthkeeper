<?php
header('Content-Type: application/json; charset=utf-8');
include('global.php'); 
$lat = NULL;
$lng = NULL;
$cityid = 625144;
if (isset($_GET['cityid'])) {
    $cityid = $_GET['cityid'];
}
if (isset($_GET['lat'])) {
    $lat = $_GET['lat'];
}
if (isset($_GET['lng'])) {
    $lng = $_GET['lng'];
}
$con=mysqli_connect($hostname,$username,$password,$dbName);
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$allData = array();
$cityData = array();
mysqli_query($con,"set names utf8");
$result = mysqli_query($con,"SELECT city.id cityid, city.name city, city.name_ru city_ru, city.lat lat, city.lng lng, country.id countryid, country.name country, country.name_ru country_ru, country.code countrycode FROM `tblcity` city inner join tblcountry country on country.id = city.country_id WHERE ABS(city.lat -".$lat.") < 0.1 and ABS(city.lng -".$lng.") < 0.1 order by SQRT(POW(ABS(city.lat -".$lat."),2)+POW(ABS(lng -".$lng."),2))");
if (!$result) {
    echo 'Database error: ' . mysql_error();
    exit;
}
while($row = mysqli_fetch_assoc($result)){
   $partData = array("clientid" => $row['cityid'], "city" => $row['city'], "city_ru" => $row['city_ru'],
   "lat" => $row['lat'], "lng" => $row['lng'], "countryid" => $row['countryid'],
   "country" => $row['country'], "country_ru" => $row['country_ru'], "countrycode" => $row['countrycode']);
   $cityData[] = $partData;   
}
mysqli_close($con);
$allData = array("cities" => $cityData);
$json = json_encode($allData);
echo $json;
?>