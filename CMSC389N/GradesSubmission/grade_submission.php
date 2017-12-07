<?php
    require_once ("support.php");
    require_once ("Person.php");



session_start();

    $courseName = $_SESSION["courseName"];
    $section = $_SESSION["section"];
    $body = "<h1>Grades Submission Form</h1><br>";
    $body .= "<h3>Course:&nbsp" . $courseName . ",&nbspSection:&nbsp" . $section . "</h3><br>";


    if(!isset($_SESSION['check'])) {
        $personArr= array();
        $fileName = $_SESSION["courseName"] . $_SESSION["section"] . ".txt";
        if (!file_exists($fileName)) {
            $body = "<strong>File {$fileName} does not exist.</strong>";
        } else {
            $fp = fopen($fileName, "r") or die("Could not open file");
            $counter = 0;
            while (!feof($fp)) {
                $line = trim(fgets($fp));
                $personArr[$counter] = new Person($line, "A");
                $counter++;
            }
            fclose($fp);
        }
    }


    if($_SESSION['check'] == null) {
        $body .= "<form action='verify.php' method='post'>";
        $body .= "<table border=\"1\" align='center'>";
        foreach ( $personArr as $value) {
            $_SESSION[$value->name] = $value->grade;
            $body .= "<tr>"."<td>$value->name</td>". "<td><input type=\"radio\" name=$value->name value=\"A\"> A</td>" .
                "<td><input type=\"radio\" name=$value->name value=\"B\"> B</td>" . "<td><input type=\"radio\" name=$value->name value=\"C\"> C</td>" .
                "<td><input type=\"radio\" name=$value->name value=\"D\"> D</td>" . "<td><input type=\"radio\" name=$value->name value=\"F\"> F</td>". "</tr>";
        }
        $body .= "</table></br></br>";
        $body .= "<button type='submit'>continue</button>";
        $body .= "</form>";
        $_SESSION['check'] = "checked";
    }
    else {
        $body .= "<form action='verify.php' method='post'>";
        $body .= "<table border=\"1\" align='center'>";

        foreach ( $_SESSION as $key => $value) {
            if($value == "A") {
                $body .= "<tr>"."<td>$key</td>". "<td><input type=\"radio\" name=$key value=\"A\" checked> A</td>" .
                    "<td><input type=\"radio\" name=$key value=\"B\"> B</td>" . "<td><input type=\"radio\" name=$key value=\"C\"> C</td>" .
                    "<td><input type=\"radio\" name=$key value=\"D\"> D</td>" . "<td><input type=\"radio\" name=$key value=\"F\"> F</td>". "</tr>";
            }
            else if($value == "B") {
                $body .= "<tr>"."<td>$key</td>". "<td><input type=\"radio\" name=$key value=\"A\" > A</td>" .
                    "<td><input type=\"radio\" name=$key value=\"B\" checked> B</td>" . "<td><input type=\"radio\" name=$key value=\"C\"> C</td>" .
                    "<td><input type=\"radio\" name=$key value=\"D\"> D</td>" . "<td><input type=\"radio\" name=$key value=\"F\"> F</td>". "</tr>";
            }
            else if($value == "C") {
                $body .= "<tr>"."<td>$key</td>". "<td><input type=\"radio\" name=$key value=\"A\" > A</td>" .
                    "<td><input type=\"radio\" name=$key value=\"B\"> B</td>" . "<td><input type=\"radio\" name=$key value=\"C\" checked> C</td>" .
                    "<td><input type=\"radio\" name=$key value=\"D\"> D</td>" . "<td><input type=\"radio\" name=$key value=\"F\"> F</td>". "</tr>";
            }
            else if($value == "D") {
                $body .= "<tr>"."<td>$key</td>". "<td><input type=\"radio\" name=$key value=\"A\" > A</td>" .
                    "<td><input type=\"radio\" name=$key value=\"B\"> B</td>" . "<td><input type=\"radio\" name=$key value=\"C\"> C</td>" .
                    "<td><input type=\"radio\" name=$key value=\"D\" checked> D</td>" . "<td><input type=\"radio\" name=$key value=\"F\"> F</td>". "</tr>";
            }
            else if($value == "F") {
                $body .= "<tr>"."<td>$key</td>". "<td><input type=\"radio\" name=$key value=\"A\" > A</td>" .
                    "<td><input type=\"radio\" name=$key value=\"B\"> B</td>" . "<td><input type=\"radio\" name=$key value=\"C\"> C</td>" .
                    "<td><input type=\"radio\" name=$key value=\"D\"> D</td>" . "<td><input type=\"radio\" name=$key value=\"F\" checked> F</td>". "</tr>";
            }


        }
        $body .= "</table></br></br>";
        $body .= "<button type='submit'>continue</button>";
        $body .= "</form>";
    }



    $page = generatePage($body);
    echo $page;
?>