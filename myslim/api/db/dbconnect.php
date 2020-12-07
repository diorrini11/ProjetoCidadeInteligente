<?php
	include "NotORM.php";
	$user = "root";
	$pass = "";
	$conn = new PDO('mysql:host=localhost;dbname=cidadeinteligente;charset=utf8', $user, $pass);
	$db = new NotORM($conn);
?>