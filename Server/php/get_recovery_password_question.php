<?php
header("Content-Type: text/html; charset=utf-8");
include('global.php');
$status = "error";        
$login = mysql_real_escape_string($_GET['name']);
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
    
    $query = $connection->prepare("SELECT u.question1 AS question1, q1.question AS questiontext1, u.question2 AS question2, q2.question AS questiontext2
        FROM tbluser u
        INNER JOIN tblrecoveryaccount_question q1 ON q1.id = u.question1
        INNER JOIN tblrecoveryaccount_question q2 ON q2.id = u.question2
        WHERE u.login =  '".$login."';");
    $query->execute();      
    $data = $query->fetch();
    if($data['question1'] > 0)
    {   
        $status = "ok";                              
    }   
    $connection->commit();
} catch (Exception $e) {
    echo "Exception" . $e->getMessage();
    $connection->rollback();    
}    
$response = array("result" => $status, "question1" => $data['question1'], "questiontext1" => $data['questiontext1'], "question2" => $data['question2'], "questiontext2" => $data['questiontext2']);
$json = json_encode($response);
echo $json;
exit();
 
?>
