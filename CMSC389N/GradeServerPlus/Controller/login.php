<?php

 session_start();
 require_once 'dbconnect.php';
 
 if( isset($_POST['login']) ) { 
  

  $username = trim($_POST['username']);  
  $password = trim($_POST['password']); 

  // password encrypted by md5
  $password = md5($password); 

  $res=mysql_query("SELECT userId, name, password FROM users WHERE name='$username'");
  $row=mysql_fetch_array($res);
  // if username and password are correct it returns must be 1 row
  $count = mysql_num_rows($res); 

  if($count == 1 && $row['password']==$password) {
    $_SESSION['username'] = $row['name'];
    header("Location: main.php");
  } else {
    echo "Incorrect Credentials, Try again...<br />+";
    echo "<a href=login.html>Go back to login page</a>";
  }  
 }
?>