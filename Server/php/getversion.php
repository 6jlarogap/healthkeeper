<?php
echo phpversion();
$today1 = getdate(); 
print_r($today1);
for($i = 0; $i < 10 ; $i++)
{
$date = getdate (mktime(0,0,0,date("m")  ,date("d")+$i,date("Y")));
$day = getdate (mktime(0,0,0,date("m")  ,date("d")+$i,date("Y")))["mday"] . "." . getdate (mktime(0,0,0,date("m")  ,date("d")+$i,date("Y")))["mon"] . "." . getdate (mktime(0,0,0,date("m")  ,date("d")+$i,date("Y")))["year"];
}
	
?>