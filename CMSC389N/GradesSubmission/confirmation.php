<?php
/**
 * Created by PhpStorm.
 * User: swang
 * Date: 27/02/2017
 * Time: 4:50 PM
 */
    require_once ("support.php");
    session_start();
    $body = "<h2>Grades submitted and e-mail confirmation sent to&nbsp" . $_POST["email"] . "</h2><br>";
    $body .= "<h2>This is submission #1</h2>";

    $_SESSION = [];
    echo generatePage($body);

?>