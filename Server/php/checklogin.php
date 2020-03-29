<?php
include('global.php');

function getUserId($name, $password) {
    $userId = -1;
    $data = mysql_fetch_assoc(mysql_query("SELECT id, password FROM `tbluser` WHERE `login`='".mysql_real_escape_string($name)."' LIMIT 1"));
    if($data['password'] === md5(md5($password))) 
    { 
         $userId = $data['id'];
    }
    return $userId;    
}

$userId = -1;
if (isset($_GET['name']) and isset($_GET['password'])) {
    $userId = getUserId($_GET['name'], $_GET['password']);
}
if (isset($_POST['name']) and isset($_POST['password'])) {
    $userId = getUserId($_POST['name'], $_POST['password']);
}
if($userId < 0){  
    echo "Access denied";    
    exit();
}
?>