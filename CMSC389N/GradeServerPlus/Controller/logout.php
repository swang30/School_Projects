<?php
 session_start();
 if (!isset($_SESSION['username'])) {
  header("Location: login.html");
 }
 
 if (isset($_GET['logout'])) {
  unset($_SESSION['user']);
  session_unset();
  session_destroy();
  header("Location: login.html");
  exit;
 }
?>