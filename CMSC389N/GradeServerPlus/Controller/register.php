<?php

 session_start();

 include_once 'dbconnect.php';

 $error = false;

 if ( isset($_POST['signup']) ) {
  
  // clean user inputs to prevent sql injections
  $username = trim($_POST['username']);
  $password = trim($_POST['password']);


   // check username exist or not
   $query = "SELECT name FROM users WHERE name='$username'";
   $result = mysql_query($query);
   $count = mysql_num_rows($result);
   if($count!=0){
    $error = true;
    echo "Provided username is already in use.";
   }
  
  // password encrypt using md5();
  $password = md5($password);
  
  // if there's no error, continue to signup
  if(!$error) {
   
   $query = "INSERT INTO users(name,password) VALUES('$username','$password')";
   $res = mysql_query($query);
    
   if ($res) {
    echo "Successfully registered, you may login now"."<br />";
    echo "<a href=login.html>Sign in Here...</a>";
    unset($username);
    unset($password);
   } else {
    echo "Something went wrong, try again later..."; 
   } 
    
  }
  
  
 }
?>