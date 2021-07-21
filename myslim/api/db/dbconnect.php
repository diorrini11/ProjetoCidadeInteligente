<?php
	include "NotORM.php";
	$user = "root";
	$pass = "";
	$conn = new PDO('mysql:host=localhost;dbname=cidadeinteligente;charset=utf8', $user, $pass);
	$db = new NotORM($conn);

	/*
	include "NotORM.php";
	$user = "id17282678_dio";
	$pass = "d~w0GE[c|P>3nXWc";
	$conn = new PDO('mysql:host=localhost;dbname=id17282678_academycity;charset=utf8', $user, $pass);
	$db = new NotORM($conn);
	*/
?>