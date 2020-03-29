<?php
header("Content-Type: text/html; charset=utf-8");
include('global.php'); 
if ($_SERVER['REQUEST_METHOD'] === 'POST')
{    
    $err = array();
    $status = "error";        
    $login = mysql_real_escape_string($_POST['name']);
    $pwd = trim($_POST['password']);
    $md5_password = md5(md5(trim($_POST['password'])));
    $answer1 = md5(md5(trim($_POST['answer1'])));
    $answer2 = md5(md5(trim($_POST['answer2'])));
    $question1 = mysql_real_escape_string($_POST['question1']);
    $question2 = mysql_real_escape_string($_POST['question2']);    
    
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
    $updateStatement1 = $connection->prepare("UPDATE tbluser SET password=:password WHERE id = :id;");    
    $queryUser = $connection->prepare("SELECT * FROM tbluser WHERE login='".$login."' and answer1='".$answer1."' and answer2='".$answer2."';");
    $queryUser->execute();      
    $dataUser = $queryUser->fetch();
    if($dataUser['id'] > 0)
    {        
        $updateStatement1->bindParam(':id', $dataUser['id'], PDO::PARAM_INT);
        $updateStatement1->bindParam(':password', $md5_password, PDO::PARAM_STR);
        $res = $updateStatement1->execute();
        if ($res !== false) {
            $status = "ok";            
        }          
    } else {
        $err[] = "Введеные вами ответы на вопросы неверные.";
        $pwd = NULL;
    }    
    $connection->commit();
} catch (Exception $e) {
    echo "Exception" . $e->getMessage();
    $connection->rollback();    
}    
$response = array("result" => $status, "id" => $dataUser['id'], "name" => $dataUser['login'], "password" => $pwd, "error" => $err);
$json = json_encode($response);
echo $json;
exit();
} 
?>
<form method="POST" action="">
Логин <input type="text" name="name" id="name"/><br />
Новый пароль <input type="password" name="password" id="password"/><br />
question1 <input type="number" name="question1" id="question1"/><br />
answer1 <input type="text" name="answer1" id="answer1"/><br />
question2 <input type="number" name="question2" id="question2"/><br />
answer2 <input type="text" name="answer2" id="answer2"/><br />

<input name="submit" type="submit" value="Сменить пароль"> 
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