<?php
include('global.php'); 
include('simple_html_dom.php'); // подключаем Simple HTML DOM

function getHtmlPage($url,$proxy)
{
	$ch = curl_init();   
	curl_setopt($ch, CURLOPT_URL,$url); 
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1); 
	#curl_setopt($ch, CURLOPT_PROXY, $proxy); 
	$ss = curl_exec($ch); 
	curl_close($ch); 
	return $ss; 
}

function getHtmlPagePost($url,$proxy,$postfields)
{
	$ch = curl_init();   
	curl_setopt($ch, CURLOPT_URL,$url); 
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1); 
	#curl_setopt($ch, CURLOPT_PROXY, $proxy); 
	curl_setopt($ch, CURLOPT_POST, 1);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $postfields);
	$ss = curl_exec($ch); 
	curl_close($ch); 
	return $ss; 
}



#геомагнитный индекс Кр
$a = getHtmlPage('http://pogoda.by/uv-gw/geo.php',$prox); 

$html = str_get_html($a); // создание объекта с помощью строки
if($html <> false)
{
	$table = $html->find('#gw',0);
	#echo "<br/>" . iconv("windows-1251","utf-8",$table);
	
	for($i = 1; $i <= 3 ; $i++)
	{
		$str = $table->children($i);
		$day = date_format(date_create($str->children(0)->plaintext),'d.m.Y');
		$h03 = $str->children(1)->plaintext;
		$h36 = $str->children(2)->plaintext;
		$h69 = $str->children(3)->plaintext;
		$h912 = $str->children(4)->plaintext;
		$h1215 = $str->children(5)->plaintext;
		$h1518 = $str->children(6)->plaintext;
		$h1821 = $str->children(7)->plaintext;
		$h210 = $str->children(8)->plaintext;
		#echo $day . " - " . $h03 . " - " . $h36 . " - " . $h69 . " - " . $h912 . " - " . $h1215 . " - " . $h1518 . " - " . $h1821 . " - " . $h210 . "<br/>";
	}
}
#West write in db

echo "dbName:" . $dbName ."<br/>";
$con=mysqli_connect($hostname,$username,$password,$dbName);
// Check connection
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
mysqli_query($con,"INSERT INTO `weather_temp` (`Id`, `InfoDate`, `Temperature`, `Humidity`) VALUES (NULL, now(), '20', '51')");
mysqli_close($con);
echo "Insert success";



#запись в базу

/*echo "<br/>".mb_detect_encoding($a);
#echo "$a";
#var_dump($a);


for($i = 1; $i <= 12 ; $i++)
{
	$day = $table->find('tr',0)->children($i)->children(0)->innertext;
	$cludy = $table->find('tr',1)->children($i)->title;
	$temp_day = str_replace("&deg;","",$table->find('tr',3)->children($i)->innertext);
	$temp_night = str_replace("&deg;","",$table->find('tr',4)->children($i)->innertext);
	$pressure = $table->find('tr',5)->children($i)->innertext;
	$humidity = $table->find('tr',6)->children($i)->innertext;
	$wind_direction = $table->find('tr',7)->children($i)->children(0)->children(0)->title;
	$wind_speed = $table->find('tr',7)->children($i)->children(0)->plaintext;
	echo $day . " - " . $cludy . " - " . $temp_day . " - " . $temp_night . " - " . $pressure . " - " . $humidity . " - " . $wind_direction . " - " . $wind_speed . "<br/>";
}

$day = $table->find('tr',0)->children(1)->children(0)->innertext . '.' . date('Y');
$cludy = $table->find('tr',1)->children(1)->title;
$temp_day = str_replace("&deg;","",$table->find('tr',3)->children(1)->innertext);
$temp_night = str_replace("&deg;","",$table->find('tr',4)->children(1)->innertext);
$pressure = $table->find('tr',5)->children(1)->innertext;
$humidity = $table->find('tr',6)->children(1)->innertext;
$wind_direction = $table->find('tr',7)->children(1)->children(0)->children(0)->title;
$wind_speed = $table->find('tr',7)->children(1)->children(0)->plaintext;
echo $day . " - " . $cludy . " - " . $temp_day . " - " . $temp_night . " - " . $pressure . " - " . $humidity . " - " . $wind_direction . " - " . $wind_speed . "<br/>";
 
 
try 
{
	$connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
} 
catch (PDOException $e) 
{
    echo 'Подключение не удалось: ' . $e->getMessage();
}

$ErrorCode = 0;

$statement = $connection->prepare('call p_insert_weather(:inDay, :inTempDay, :inTempNight, :inPressure, :inHumidity, :inCloudinessType)');
$statement->bindParam(':inDay',$day,PDO::PARAM_STR);
$statement->bindParam(':inTempDay',$temp_day,PDO::PARAM_INT);
$statement->bindParam(':inTempNight',$temp_night,PDO::PARAM_INT);
$statement->bindParam(':inPressure',$pressure,PDO::PARAM_INT);
$statement->bindParam(':inHumidity',$humidity,PDO::PARAM_INT);
$statement->bindParam(':inCloudinessType',$cludy,PDO::PARAM_STR);

$statement->execute();
echo "Error = " . $ErrorCode . "\n";
echo "day = " . $day . "\n";
 
 
 



$html->clear();
unset($html);*/
?>