<?php
/* Соединяемся с базой данных */
$hostname = "db557216498.db.1and1.com"; // название/путь сервера, с MySQL
$username = "dbo557216498"; // имя пользователя (в Denwer`е по умолчанию "root")
$password = "H1l7h2014!"; // пароль пользователя (в Denwer`е по умолчанию пароль отсутствует, этот параметр можно оставить пустым)
$dbName = "db557216498"; // название базы данных
$datetimeformat = "Y-m-d H:i:s";

/*Прокси сервер*/
$prox = '172.16.16.55:9999';

mysql_connect($hostname, $username, $password) or die ("MySQL Error: " . mysql_error());
mysql_query("set names utf8") or die ("<br>Invalid query: " . mysql_error());
mysql_query("set time_zone='+00:00'") or die ("<br>Invalid query: " . mysql_error());
mysql_select_db($dbName) or die ("<br>Invalid query: " . mysql_error());

# массив ошибок для авторизации
$error[0] = 'Я вас не знаю';
$error[1] = 'Включи куки';
$error[2] = 'Тебе сюда нельзя';

function getDateFromUnixTime($unixTime){
    global $datetimeformat;
    return gmdate($datetimeformat, $unixTime);
}

function getBeginDateFromUnixTime($unixTime){
    global $datetimeformat;
    $temp = $unixTime - ($unixTime % 24*60*60);
    return gmdate($datetimeformat, $temp);
}

function getNextDateFromUnixTime($unixTime){
    global $datetimeformat;
    $temp = $unixTime + 24*60*60 - ($unixTime % 24*60*60);
    return gmdate($datetimeformat, $temp);
}

?>