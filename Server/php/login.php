<?php
header("Content-Type: text/html; charset=UTF-8");
include('global.php');
# Функция для генерации случайной строки 
function generateCode($length=6) { 
    $chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPRQSTUVWXYZ0123456789"; 
    $code = ""; 
    $clen = strlen($chars) - 1;   
    while (strlen($code) < $length) { 
        $code .= $chars[mt_rand(0,$clen)];   
    } 
    return $code; 
} 
$response = array();
$status = "error";
$userid = NULL;
# Если есть куки с ошибкой то выводим их в переменную и удаляем куки
if (isset($_COOKIE['errors'])){
    $errors = $_COOKIE['errors'];
    setcookie('errors', '', time() - 60*24*30*12, '/');
}
if ($_SERVER['REQUEST_METHOD'] === 'POST'){
    $login = mysql_real_escape_string($_POST['name']);
    $password = $_POST['password']; 
    $data = mysql_fetch_assoc(mysql_query("SELECT * FROM `tbluser` WHERE `login`='".$login."' LIMIT 1"));
    $err = array();
    if($data['password'] === md5(md5($_POST['password']))) 
    {
        $userid = $data['id'];
        $dataDetail = mysql_fetch_assoc(mysql_query("SELECT * FROM `tbluserdetail` WHERE `userid`=".$data['id']." LIMIT 1"));
        # Генерируем случайное число и шифруем его 
        $hash = md5(generateCode(10)); 
        # Записываем в БД новый хеш авторизации и IP 
        mysql_query("UPDATE tbluser SET hash='".$hash."' WHERE id='".$data['id']."'") or die("MySQL Error: " . mysql_error());        
        # Ставим куки 
        setcookie("id", $data['id'], time()+60*60*24*30);
        setcookie("hash", $hash, time()+60*60*24*30);        
        $status = "ok";
        $response = array("id" => $userid, "name" => $login, "password" => $password, "fname" => $data['fname'], "lname" => $data['lname'], "mname" => $data['mname'], "birthdate" => $data['birthdate'], "sex" => $data['sex'], "cityid" => $data['cityid'], "createdate" => $data['createdate'], "question1" => $data['question1'], "question2" => $data['question2'], "marital_status_id" => $dataDetail['maritalstatusid'], "social_status_id" => $dataDetail['socialstatusid'], "height" => $dataDetail['height'], "weight" => $dataDetail['weight'], "pressure_id" => $dataDetail['pressureid'], "foot_distance" => $dataDetail['footdistance'], "sleep_time" => $dataDetail['sleeptime']);
    } 
    else 
    {   
        $err[] = "Вы ввели неправильный логин/пароль.";        
    }
    $response["result"] = $status;
    $response["error"] = $err;
    $json = json_encode($response);
    echo $json;
    exit();
} 
?>
<form method="POST"> 
Логин <input name="name" type="text"><br> 
Пароль <input name="password" type="password"><br> 
<input name="submit" type="submit" value="Войти"> 
</form>
<?php
   # Проверяем наличие в куках номера ошибки
   if (isset($errors)) {print '<h4>'.$error[$errors].'</h4>';}
?>