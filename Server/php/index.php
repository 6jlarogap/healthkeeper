<?php
header('Content-Type: text/html; charset=utf-8');
include('global.php'); 
include('simple_html_dom.php'); // подключаем Simple HTML DOM
set_time_limit (1000);
function getHtmlPage($url,$proxy)
{
	$ch = curl_init();   
	curl_setopt($ch, CURLOPT_URL,$url); 
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1); 
	//curl_setopt($ch, CURLOPT_PROXY, $proxy); 
	$ss = curl_exec($ch); 
	curl_close($ch); 
	return $ss; 
}

function getHtmlPagePost($url,$proxy,$postfields)
{
	$ch = curl_init();   
	curl_setopt($ch, CURLOPT_URL,$url); 
	curl_setopt($ch, CURLOPT_RETURNTRANSFER,1); 
	//curl_setopt($ch, CURLOPT_PROXY, $proxy); 
	curl_setopt($ch, CURLOPT_POST, 1);
	curl_setopt($ch, CURLOPT_POSTFIELDS, $postfields);
	$ss = curl_exec($ch); 
	curl_close($ch); 
	return $ss; 
}

function insertToDB($day, $cloudy, $temp_day, $temp_night, $pressure, $humidity, $wind_direction, $wind_speed, $moon_phase, $geo_field, $zodiak, $moon_day, $moonVisibility, $sunRise, $sunDown, $moonRise, $moonDown, $dayLength)
{
	echo "<br/>insertToDB ($day, $cloudy, $temp_day, $temp_night, $pressure, $humidity, $wind_direction, $wind_speed, $moon_phase, $geo_field, $zodiak, $moon_day, $moonVisibility, $sunRise, $sunDown, $moonRise, $moonDown, $dayLength)";

	global $dbName, $hostname, $username, $password;
	
	try 
	{
		$connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
        $connection->exec("set names utf8");
	} 
	catch (PDOException $e) 
	{
		echo 'Подключение не удалось: ' . $e->getMessage();
	}
	
	$ErrorCode = 0;

	$statement = $connection->prepare('call p_insert_weather(:in_dt, :in_cloudy, :in_tempDay, :in_tempNight, :in_pressure, :in_humidity, :in_windDir, :in_windSpeed, :in_moonPhase, :in_geoField, :in_zodiak, :in_moonDay, :in_moonVisibility, :in_sunRise, :in_sunDown, :in_moonRise, :in_moonDown, :in_dayLength)');
	if (!$statement) {
        echo "ErrorDB: ";
        print_r($statement->errorInfo());
    }
    
	$statement->bindParam(':in_dt', $day, PDO::PARAM_STR);
	$statement->bindParam(':in_cloudy', $cloudy, PDO::PARAM_STR);
	$statement->bindParam(':in_tempDay', $temp_day, PDO::PARAM_INT);
	$statement->bindParam(':in_tempNight', $temp_night, PDO::PARAM_INT);
	$statement->bindParam(':in_pressure', $pressure, PDO::PARAM_INT);
	$statement->bindParam(':in_humidity', $humidity, PDO::PARAM_INT);
	$statement->bindParam(':in_windDir', $wind_direction, PDO::PARAM_STR);
	$statement->bindParam(':in_windSpeed', $wind_speed, PDO::PARAM_INT);
	$statement->bindParam(':in_moonPhase', $moon_phase, PDO::PARAM_STR);
	$statement->bindParam(':in_geoField', $geo_field, PDO::PARAM_STR);
	$statement->bindParam(':in_zodiak', $zodiak, PDO::PARAM_STR);
	$statement->bindParam(':in_moonDay', $wind_speed, PDO::PARAM_INT);
	$statement->bindParam(':in_moonVisibility', $moonVisibility, PDO::PARAM_INT);
	$statement->bindParam(':in_sunRise', $sunRise, PDO::PARAM_STR);
	$statement->bindParam(':in_sunDown', $sunDown, PDO::PARAM_STR);
	$statement->bindParam(':in_moonRise', $moonRise, PDO::PARAM_STR);
	$statement->bindParam(':in_moonDown', $moonDown, PDO::PARAM_STR);
	$statement->bindParam(':in_dayLength', $dayLength, PDO::PARAM_STR);

	$result = $statement->execute();
    if (!$result) {
        echo 'ErrorDB: ';
        print_r($statement->errorInfo());     
    }	
}



#восход и заход солнца, длина дня
#echo $a;                                                                          n=285&month=12&year=2013&obj=sun&afl=-11&day=1


for($i = 0; $i < 10 ; $i++)
{
	$date = getdate (mktime(0,0,0,date("m")  ,date("d")+$i,date("Y")));
	$day = $date["mday"] . "." . $date["mon"] . "." . $date["year"];
	
	$a = getHtmlPage('http://redday.ru/sun/?d=' . $date['mday'] . '&m=' . $date['mon'] . '&y=' . $date['year'] . '&city=221',$prox); 
	$html = str_get_html($a); // создание объекта с помощью строки
	if($html <> false)
	{
		$table = $html->find('table[class=maintext]',0);
		
		$pos1 = strpos($table->find('tr',1)->children(2)->innertext,':');
		$pos2 = strpos($table->find('tr',1)->children(2)->innertext,':',$pos1 + 1);
		$pos3 = strpos($table->find('tr',1)->children(2)->innertext,':',$pos2 + 1);
		$pos4 = strpos($table->find('tr',1)->children(2)->innertext,':',$pos3 + 1);
		$pos5 = strpos($table->find('tr',1)->children(2)->innertext,':',$pos4 + 1);
		$pos6 = strpos($table->find('tr',1)->children(2)->innertext,':',$pos5 + 1);
		$pos7 = strpos($table->find('tr',1)->children(2)->innertext,':',$pos6 + 1);
		$pos8 = strpos($table->find('tr',1)->children(2)->innertext,':',$pos7 + 1);
		
		$sun_rise = substr($table->find('tr',1)->children(2)->innertext,$pos2 - 2,5);
		$sun_down = substr($table->find('tr',1)->children(2)->innertext,$pos4 - 2,5);
		$day_length = substr($table->find('tr',1)->children(2)->innertext,$pos6 - 2,5);
		$moon_rise = substr($table->find('tr',1)->children(2)->innertext,$pos7 - 2,5);
		$moon_down = substr($table->find('tr',1)->children(2)->innertext,$pos8 - 2,5);
		
		echo "<br><br>Дата " . $day;
		echo "<br>Восход солнца " . $sun_rise;
		echo "<br>Заход солнца " . $sun_down;
		echo "<br>Долгота дня " . $day_length;
		echo "<br>Восход луны " . $moon_rise;
		echo "<br>Заход луны " . $moon_down;
		
		insertToDB($day, null, null, null, null, null, null, null, null, null, null, null, null, $sun_rise, $sun_down, $moon_rise, $moon_down, $day_length);
	}
}
#}


#погода
$a = getHtmlPage('http://www.gismeteo.by/ajax/print/4248/long/',$prox); 

$html = str_get_html($a); // создание объекта с помощью строки
if($html <> false)
{
	$table = $html->find('table',0);
	echo $table;
	$afterYearFrontier = false;
	for($i = 1; $i <= 12 ; $i++)
	{
		if($afterYearFrontier)
			$day = date_format(date_create($table->find('tr',0)->children($i)->children(0)->innertext . '.' . (date('Y') + 1)),'d.m.Y');
		else
		{
			$day = date_format(date_create($table->find('tr',0)->children($i)->children(0)->innertext . '.' . date('Y')),'d.m.Y');
			
			if($table->find('tr',0)->children($i)->children(0)->innertext == '31.12')
				$afterYearFrontier = true;
		}
			
		
		$cloudy = $table->find('tr',1)->children($i)->title;
		$temp_day = str_replace("&deg;","",$table->find('tr',3)->children($i)->innertext);
		$temp_night = str_replace("&deg;","",$table->find('tr',4)->children($i)->innertext);
		$pressure = $table->find('tr',5)->children($i)->innertext;
		$humidity = $table->find('tr',6)->children($i)->innertext;
		$wind_direction = $table->find('tr',7)->children($i)->children(0)->children(0)->title;
		$wind_speed = $table->find('tr',7)->children($i)->children(0)->plaintext;
		echo $day . " - " . $cloudy . " - " . $temp_day . " - " . $temp_night . " - " . $pressure . " - " . $humidity . " - " . $wind_direction . " - " . $wind_speed . "<br/>";
		
		insertToDB($day, $cloudy, $temp_day, $temp_night, $pressure, $humidity, $wind_direction, $wind_speed, null, null, null, null, null, null, null, null, null, null);
		
		echo "day = " . $day . "\n";
		
	}
}
echo "<br/>" .  date_format(date_create('01.02.2013'),'d.m.Y');



#Гео поле
$a = getHtmlPage('http://pogoda.mail.ru/prognoz/minsk/extended/',$prox); 

$html = str_get_html($a); // создание объекта с помощью строки
if($html <> false)
{

	for($i = 0; $i < 10 ; $i++)
	{
		$day = date_format(date_create($html->find('.forecast__byhours__day',$i)->children(0)->name),'d.m.Y');
		$moon_phase = $html->find('.moonphase__img',$i)->title;
		echo $day . " - " . $moon_phase . "<br/>";
		
		insertToDB($day, null, null, null, null, null, null, null, $moon_phase, null, null, null, null, null, null, null, null, null);
	}
	for($i = 0; $i < 3 ; $i++)
	{
		$day = date_format(date_create($html->find('.forecast__byhours__day',$i)->children(0)->name),'d.m.Y');
		$geo_pole = $html->find('.day-info',$i)->children(3)->children(1)->plaintext;
		echo $day . " - " . $geo_pole . "<br/>";
		
		insertToDB($day, null, null, null, null, null, null, null, null, $geo_pole, null, null, null, null, null, null, null, null);
	}
}
echo "<br/>";


#зодиакальная фаза луны
$a = getHtmlPage('http://astroland.ru/cgi-bin/cal/holidays.cgi?view=navtab&int=yesterday+today+month&events=B',$prox); 

$html = str_get_html($a); // создание объекта с помощью строки
if($html <> false)
{
	$table = $html->find('.tableHolidays',0);

	$afterYearFrontier = false;
	for($i = 2; $i < 30 ; $i++)
	{
		if($afterYearFrontier)
			$day = date_format(date_create($table->children($i)->children(0)->plaintext . '.' . (date('Y') + 1)),'d.m.Y');
		else
		{
			$day = date_format(date_create($table->children($i)->children(0)->plaintext . '.' . date('Y')),'d.m.Y');
			if($table->children($i)->children(0)->plaintext == '31.12')
				$afterYearFrontier = true;
		}
		$zodiak = $table->children($i)->children(1)->find('a',0)->plaintext;
		echo $day . " - " . $zodiak . "<br/>";
		
		insertToDB($day, null, null, null, null, null, null, null, null, null, $zodiak, null, null, null, null, null, null, null);
	}
}

#лунные сутки и видиместь диска
for($i = 0; $i < 10 ; $i++)
{
	$date = getdate (mktime(0,0,0,date("m")  ,date("d")+$i,date("Y")));
	$day = $date["mday"] . "." . $date["mon"] . "." . $date["year"];
	$a = getHtmlPagePost('http://lunium.ru/index.php?mnd1=5',$prox,'dd=' . $date["mday"] .'&mm=' . $date["mon"] .'&yy=' . $date["year"] .'&chas=0&city=%CC%E8%ED%F1%EA+&min=0&button=%D0%E0%F1%F1%F7%E8%F2%E0%F2%FC+%CB%F3%ED%ED%FB%E9+%E4%E5%ED%FC'); 
	
	$html = str_get_html($a); // создание объекта с помощью строки
	#echo $html;
	if($html <> false)
	{
		$visibility = str_replace("%","",$html->find('table',3)->children(6)->find('strong',0)->plaintext);
		$moon_day = $html->find('table',2)->find('.polosi',0)->find('span',0)->plaintext;

		echo "<br/>" . $moon_day . ' лунный день - видимость диска ' . $visibility;
		
		insertToDB($day, null, null, null, null, null, null, null, null, null, null, $moon_day, $visibility, null, null, null, null, null);
	}
}
/*
#геомагнитный индекс Кр
$a = getHtmlPage('http://pogoda.by/uv-gw/geo.php',$prox); 

$html = str_get_html($a); // создание объекта с помощью строки
if($html <> false)
{
	$table = $html->find('#gw',0);
	echo "<br/>" . iconv("windows-1251","utf-8",$table);
	
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
		echo $day . " - " . $h03 . " - " . $h36 . " - " . $h69 . " - " . $h912 . " - " . $h1215 . " - " . $h1518 . " - " . $h1821 . " - " . $h210 . "<br/>";
	}
}
/*
#запись в базу
/*
echo "<br/>".mb_detect_encoding($a);
echo "$a";
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
 
 
 

*/

$html->clear();
unset($html);
?>