<?php
include('global.php'); 
if (isset($_COOKIE['id']) and isset($_COOKIE['hash'])) 
{    
    $userdata = mysql_fetch_assoc(mysql_query("SELECT * FROM tbluser WHERE id = '".intval($_COOKIE['id'])."' LIMIT 1"));
    if(($userdata['hash'] !== $_COOKIE['hash']) or ($userdata['id'] !== $_COOKIE['id'])) 
    {
        setcookie('id', '', time() - 60*24*30*12, '/'); 
        setcookie('hash', '', time() - 60*24*30*12, '/');
        setcookie('errors', '1', time() + 60*24*30*12, '/');
        header('Location: login.php');
        exit();
    } 
} 
else 
{ 
    setcookie('errors', '2', time() + 60*24*30*12, '/');
    header('Location: login.php'); 
    exit();
}
?>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
<title></title>
</head>
<body>
Hello!
</body>
</html>