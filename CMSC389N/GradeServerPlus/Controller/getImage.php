<?php
 	session_start();
	require_once 'dbconnect.php';
	
		$username = $_SESSION['username'];
		$query = mysql_query("SELECT * FROM users WHERE name='$username'");

		$row = mysql_fetch_array($query);
		$imageData = $row['image_data'];


		$_SESSION['image'] = "<img src='data:image/jpeg;base64,".base64_encode($imageData)."' alt='Imagine a Hulk here..' style='width:50px;height:50px;'/>";
?>