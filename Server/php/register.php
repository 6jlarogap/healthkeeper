<?php
session_start();
header("Content-Type: text/html; charset=utf-8");
include('global.php'); 
if ($_SERVER['REQUEST_METHOD'] === 'POST')
{
    $isedit = false;
    if (isset($_POST['isedit']) and $_POST['isedit'] == 1){        
        $isedit = true;
    }
    $err = array();
    $status = "error";
    if(!$isedit){
        if(!(isset($_SESSION['captcha_keystring']) && $_SESSION['captcha_keystring'] === $_POST['keystring'])){
            $err[] = "Вы ввели неверное число, указанное на картинке."; 		
        }
    }
    # проверям логин 
    if(!preg_match("/^[А-Яа-яa-zA-Z0-9\._\-]+$/",$_POST['name'])) 
    { 
        $err[] = "ФИО может состоять только из букв английского, русского алфавита, цифр, точки, знака подчеркивания и дефиса."; 
    }
    if(strlen($_POST['name']) < 4 or strlen($_POST['name']) > 100) 
    { 
        $err[] = "ФИО должна быть не меньше 4-х символов и не больше 100."; 
    }
    if(strlen($_POST['password']) < 4 or strlen($_POST['password']) > 30) 
    { 
        $err[] = "Пароль должен быть не меньше 4-х символов и не больше 30.";
    }
    $login = mysql_real_escape_string($_POST['name']);
    $pwd = trim($_POST['password']);
    $md5_password = md5(md5(trim($_POST['password'])));
    $fname = mysql_real_escape_string($_POST['fname']);
    $lname = mysql_real_escape_string($_POST['lname']);
    $mname = mysql_real_escape_string($_POST['mname']);
    $birthdate = mysql_real_escape_string($_POST['birthdate']);
    $sex = mysql_real_escape_string($_POST['sex']);
    $cityid = mysql_real_escape_string($_POST['cityid']);
    $createdate = date("Y-m-d H:i:s");
    if (isset($_POST['marital_status_id']) and strlen($_POST['marital_status_id'])>0){
        $maritalstatusid = $_POST['marital_status_id'];        
    } else {
        $maritalstatusid = NULL;
    }
    if (isset($_POST['social_status_id']) and strlen($_POST['social_status_id'])>0){
        $socialstatusid = $_POST['social_status_id'];
    } else {
        $socialstatusid = NULL;
    }
    if (isset($_POST['height']) and strlen($_POST['height'])>0){
        $height = $_POST['height'];
    } else {
        $height = NULL;
    }
    if (isset($_POST['weight']) and strlen($_POST['weight'])>0){
        $weight = $_POST['weight'];
    } else {
        $weight = NULL;
    }
    if (isset($_POST['pressure_id']) and strlen($_POST['pressure_id'])>0){
        $pressureid = $_POST['pressure_id'];
    } else {
        $pressureid = NULL;
    }
    if (isset($_POST['foot_distance']) and strlen($_POST['foot_distance'])>0){
        $footdistance = $_POST['foot_distance'];
    } else {
        $footdistance = NULL;
    }
    if (isset($_POST['sleep_time']) and strlen($_POST['sleep_time'])>0){
        $sleeptime = $_POST['sleep_time'];
    } else {
        $sleeptime = NULL;
    }
    if (isset($_POST['answer1']) and strlen($_POST['answer1'])>0){
        $question1 = mysql_real_escape_string($_POST['question1']);
        $answer1 = md5(md5(mysql_real_escape_string($_POST['answer1'])));
    } else {
        $question1 = NULL;
        $answer1 = NULL;
    }
    if (isset($_POST['answer2']) and strlen($_POST['answer2'])>0){
        $question2 = mysql_real_escape_string($_POST['question2']);
        $answer2 = md5(md5(mysql_real_escape_string($_POST['answer2'])));
    } else {
        $question2 = NULL;
        $answer2 = NULL;
    }
$con=mysqli_connect($hostname,$username,$password,$dbName);
if (mysqli_connect_errno())
{
    echo "Failed to connect to MySQL: " . mysqli_connect_error();
    die("QWERTY");
}
global $dbName, $hostname, $username, $password;
$userid = NULL;
$connection = new PDO('mysql:dbname=' . $dbName . ';host=' . $hostname, $username, $password);
$connection->exec("set names utf8");
$connection->exec("set time_zone='+00:00'"); 
$connection->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
$connection->beginTransaction();
try {
    $insertStatement1 = $connection->prepare("INSERT INTO tbluser (login, password, fname, lname, mname, birthdate, sex, cityid, question1, answer1, question2, answer2) VALUES (:login, :password, :fname, :lname, :mname, :birthdate, :sex, :cityid, :question1, :answer1, :question2, :answer2);");
    $updateStatement1 = $connection->prepare("UPDATE tbluser SET fname=:fname, lname=:lname, mname = :mname, birthdate = :birthdate, sex=:sex, cityid = :cityid WHERE id = :id;");
    $insertStatement2 = $connection->prepare("INSERT INTO tbluserdetail (maritalstatusid, socialstatusid, height, weight, pressureid, footdistance, sleeptime, userid) VALUES (:maritalstatusid, :socialstatusid, :height, :weight, :pressureid, :footdistance, :sleeptime, :userid);");
    $updateStatement2 = $connection->prepare("UPDATE tbluserdetail SET maritalstatusid=:maritalstatusid, socialstatusid=:socialstatusid, height = :height, weight = :weight, pressureid = :pressureid, footdistance = :footdistance, sleeptime = :sleeptime, userid = :userid WHERE id = :id;");    
    if ($isedit === true){
        $queryUser = $connection->prepare("SELECT id, password FROM tbluser WHERE login='".$login."' and password = '".$md5_password."' LIMIT 1");
        $queryUser->execute();      
        $data = $queryUser->fetch();
        if($data['password'] === $md5_password){
            $userid = $data['id'];
            if(count($err) == 0) {
                $queryUserDetail = $connection->prepare("SELECT * FROM `tbluserdetail` WHERE `userid`='".$data['id']."' LIMIT 1");
                $queryUserDetail->execute();      
                $dataDetail = $queryUserDetail->fetch();                
                $updateStatement1->bindParam(':id', $data['id'], PDO::PARAM_INT);
                $updateStatement1->bindParam(':fname', $fname, PDO::PARAM_STR);
                $updateStatement1->bindParam(':lname', $lname, PDO::PARAM_STR);
                $updateStatement1->bindParam(':mname', $mname, PDO::PARAM_STR);
                $updateStatement1->bindParam(':birthdate', $birthdate, PDO::PARAM_STR);
                $updateStatement1->bindParam(':sex', $sex, PDO::PARAM_INT);
                $updateStatement1->bindParam(':cityid', $cityid, PDO::PARAM_INT);              
                $res1 = $updateStatement1->execute();                 
                if (isset($dataDetail['id'])) {                    
                    $updateStatement2->bindParam(':maritalstatusid', $maritalstatusid, PDO::PARAM_INT);
                    $updateStatement2->bindParam(':socialstatusid', $socialstatusid, PDO::PARAM_INT);
                    $updateStatement2->bindParam(':height', $height, PDO::PARAM_INT);
                    $updateStatement2->bindParam(':weight', $weight, PDO::PARAM_INT);
                    $updateStatement2->bindParam(':pressureid', $pressureid, PDO::PARAM_INT);
                    $updateStatement2->bindParam(':footdistance', $footdistance, PDO::PARAM_INT);
                    $updateStatement2->bindParam(':sleeptime', $sleeptime, PDO::PARAM_INT);
                    $updateStatement2->bindParam(':userid', $data['id'], PDO::PARAM_INT);
                    $updateStatement2->bindParam(':id', $dataDetail['id'], PDO::PARAM_INT);                    
                    $res2 = $updateStatement2->execute();                      
                } 
                else {
                    $insertStatement2->bindParam(':maritalstatusid', $maritalstatusid, PDO::PARAM_INT);
                    $insertStatement2->bindParam(':socialstatusid', $socialstatusid, PDO::PARAM_INT);
                    $insertStatement2->bindParam(':height', $height, PDO::PARAM_INT);
                    $insertStatement2->bindParam(':weight', $weight, PDO::PARAM_INT);
                    $insertStatement2->bindParam(':pressureid', $pressureid, PDO::PARAM_INT);
                    $insertStatement2->bindParam(':footdistance', $footdistance, PDO::PARAM_INT);
                    $insertStatement2->bindParam(':sleeptime', $sleeptime, PDO::PARAM_INT);
                    $insertStatement2->bindParam(':userid', $data['id'], PDO::PARAM_INT);                    
                    $res2 = $insertStatement2->execute();                    
                }
                if ($res1 !== false && $res2 !== false) {                
                    $status = "ok";
                } 
            }
        } else {
            $err[] = "Вы ввели неправильный логин/пароль."; 
        }
    } else {
        # проверяем, не сущестует ли пользователя с таким именем 
        $queryCountUser = $connection->prepare("SELECT COUNT(id) as cnt FROM tbluser WHERE login='".$login."'");
        $queryCountUser->execute();      
        $dataCountUser = $queryCountUser->fetch();
        if($dataCountUser['cnt'] > 0)
        { 
            $err[] = "Пользователь с таким логином уже существует."; 
        }
        if(count($err) == 0) {
            $insertStatement1->bindParam(':login', $login, PDO::PARAM_STR);
            $insertStatement1->bindParam(':password', $md5_password, PDO::PARAM_STR);
            $insertStatement1->bindParam(':fname', $fname, PDO::PARAM_STR);
            $insertStatement1->bindParam(':lname', $lname, PDO::PARAM_STR);
            $insertStatement1->bindParam(':mname', $mname, PDO::PARAM_STR);
            $insertStatement1->bindParam(':birthdate', $birthdate, PDO::PARAM_STR);
            $insertStatement1->bindParam(':sex', $sex, PDO::PARAM_INT);
            $insertStatement1->bindParam(':cityid', $cityid, PDO::PARAM_INT);
            $insertStatement1->bindParam(':question1', $question1, PDO::PARAM_INT);
            $insertStatement1->bindParam(':answer1', $answer1, PDO::PARAM_STR);
            $insertStatement1->bindParam(':question2', $question2, PDO::PARAM_INT);
            $insertStatement1->bindParam(':answer2', $answer2, PDO::PARAM_STR);            
            $res1 = $insertStatement1->execute();
            if ($res1 !== false) {
                $status = "ok";
                $userid = $connection->lastInsertId();
            }         
        }
    }
    $connection->commit();
} catch (Exception $e) {
    echo "Exception" . $e->getMessage();
    $connection->rollback();    
}    
$response = array("result" => $status, "id" => $userid, "name" => $login, "password" => $pwd, "fname" => $fname, "lname" => $lname, "mname" => $mname, "birthdate" => $birthdate, "sex" => $sex, "cityid" => $cityid, "createdate" => $createdate, "question1" => $question1, "question2" => $question2, "marital_status_id" => $maritalstatusid, "social_status_id" => $socialstatusid, "height" => $height, "weight" => $weight, "pressure_id" => $pressureid, "foot_distance" => $footdistance, "sleep_time" => $sleeptime, "error" => $err);
$json = json_encode($response);
echo $json;
exit();
} 
?>
<form method="POST" action="">
Логин <input type="text" name="name" id="name"/><br />
Пароль <input type="password" name="password" id="password"/><br />
Фамилия <input type="text" name="lname" id="lname"/><br />
Имя <input type="text" name="fname" id="fname"/><br />
Отчество <input type="text" name="mname" id="mname"/><br />
Дата рождения(yyyy-MM-dd) <input type="text" name="birthdate" id="birthdate"/><br />
Пол(1-мужчина, 2 - женщина) <input type="text" name="sex" id="sex"/><br />
Id города <input type="number" name="cityid" id="cityid"/><br />
Редактировать запись(1-редактировать)<input type="text" name="isedit" id="isedit"/><br />
marital_status_id <input type="number" name="marital_status_id" id="marital_status_id"/><br />
social_status_id <input type="number" name="social_status_id" id="social_status_id"/><br />
height <input type="number" name="height" id="height"/><br />
weight <input type="number" name="weight" id="weight"/><br />
pressure_id <input type="number" name="pressure_id" id="pressure_id"/><br />
foot_distance <input type="number" name="foot_distance" id="foot_distance"/><br />
sleep_time <input type="number" name="sleep_time" id="sleep_time"/><br />
question1 <input type="number" name="question1" id="question1"/><br />
answer1 <input type="text" name="answer1" id="answer1"/><br />
question2 <input type="number" name="question2" id="question2"/><br />
answer2 <input type="text" name="answer2" id="answer2"/><br />
<p>Enter text shown below:</p>
<p><img name="img_captcha" src="./get_captcha.php?<?php echo session_name()?>=<?php echo session_id()?>"></p>
<p><input type="text" name="keystring"></p>

<input name="submit" type="submit" value="Зарегистрироваться"> 
</form>
<?php
    if (isset($err)) {
        print "<b>При регистрации произошли следующие ошибки:</b><br>"; 
        foreach($err AS $error) 
        { 
            print $error."<br>"; 
        }   
    }
?>