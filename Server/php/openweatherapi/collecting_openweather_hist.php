<?php
set_time_limit(60 * 20); 
include('../global.php'); 
include('../libhttp.php');
$args = "?";
if (isset($_GET['id'])) {
    $cityid = $_GET['id'];
    $args = $args ."id=" . $cityid . "&";
}
if (isset($_GET['datefrom'])) {
    $dtUnixFrom = $_GET['datefrom'];    
} else {
    $dtUnixFrom = time() - 15 * 86400;
} 
if (isset($_GET['dateto'])) {
    $dtUnixTo = $_GET['dateto'];    
} else {
    $dtUnixTo = time();
}
$args = $args ."datefrom=" . $dtUnixFrom . "&" ."dateto=" . $dtUnixTo;
$url = 'http://'.$_SERVER['SERVER_NAME'].'/openweatherapi/collecting_openweather.php';
echo $url.$args. "</br>";
$json = getHtmlPage($url.$args, $prox);
echo $json;

?>