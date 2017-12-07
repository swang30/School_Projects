<?php
    require_once("support.php");

    $body = "";

    if (!isset($_SERVER['PHP_AUTH_USER'])) {
        header("WWW-Authenticate: Basic realm=\"Administrative\"");
        header("HTTP/1.0 401 Unauthorized");
        print "Sorry - you need valid credentials to be granted access!\n";
        exit;
    } else {
        $userName_hash = password_hash("main", PASSWORD_DEFAULT);
        $password_hash = password_hash("terps", PASSWORD_DEFAULT);
        if ((password_verify($_SERVER['PHP_AUTH_USER'],$userName_hash)) && (password_verify($_SERVER['PHP_AUTH_PW'],$password_hash))) {
            $body .= "<h2>Application</h2><br>";
            $body .= "<h4>Select fields to display</h4>";
            $body .= "<form action='display.php' method='post'>";
            $body .= "<select name=\"display[]\" multiple=\"multiple\" style=\"overflow-y: scroll; height: 7em\" required>";
            $body .= "<option value='name'>name</option>";
            $body .= "<option value='email'>email</option>";
            $body .= "<option value='gpa' selected='selected'>gpa</option>";
            $body .= "<option value='year'>year</option>";
            $body .= "<option value='gender'>gender</option>";
            $body .= "</select><br>";
            $body .= "<h4>Select field to sort applications</h4>";
            $body .= "<select name=\"sort\">";
            $body .= "<option value='name'>name</option>";
            $body .= "<option value='email'>email</option>";
            $body .= "<option value='gpa' selected='selected'>gpa</option>";
            $body .= "<option value='year'>year</option>";
            $body .= "<option value='gender'>gender</option>";
            $body .= "</select><br>";
            $body .= "<h4 style='display: inline-block'>Filter Condition</h4><input type='text' name='filter' style='display: inline-block'><br>";
            $body .= "<input type='submit' name='display applications' value='Display Applications'><br><br>";

            $body .= "</form>";
            $body .= "<a class=\"btn btn-default\" href=\"main.html\">Return to main menu</a><br>";
        } else {
            header("WWW-Authenticate: Basic realm=\"Private Area\"");
            header("HTTP/1.0 401 Unauthorized");
            print "Sorry - you need valid credentials to be granted access!\n";
            exit;
        }
    }

    echo generatePage($body);

