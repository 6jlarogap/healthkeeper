<?php
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
?>