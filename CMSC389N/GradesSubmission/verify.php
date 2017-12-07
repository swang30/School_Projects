<?php
/**
 * Created by PhpStorm.
 * User: swang
 * Date: 27/02/2017
 * Time: 4:28 PM
 */
session_start();
    require_once ("support.php");

/* Storing session information */

    $keys = array_keys($_POST);
    foreach ($keys as $key)
        $_SESSION[$key] = $_POST[$key];

    $body = "<h2>Grades to Submit</h2><br><br>";
    $body .= "<table border='2' align='center'>";
    $body .= "<tr style='text-align: center'>" . "<th>Name</th>" . "<th>Grade</th>" . "</tr>";
    foreach($_SESSION as $key => $value) {
        if($key != "check" && $key != "courseName" && $key != "section" ) {
            $body .= "<tr>" . "<th>$key</th>" . "<th>$value</th>" . "</tr>";
        }
    }
    $body .= "</table><br>";

    $body .= "<form action='confirmation.php' method='post'>";
    $body .= "<h5>please enter your email to receive confirmation</h5>";
    $body .= "<input type='text' name='email'><br>";
    $body .= "<button type='submit'>Submit Grades</button><br>";
    $body .= "</form><br>";
    $body .= "<a href='grade_submission.php' style='appearance: button; color: initial;'>back</a>";

    echo generatePage($body);

?>