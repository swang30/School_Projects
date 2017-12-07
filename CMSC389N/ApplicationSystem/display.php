<?php
    require_once("support.php");
    $body = "";


    if(isset($_POST["display_applications"])) {
        $host = "localhost";
        $user = "dbuser";
        $password = "goodbyeWorld";
        $database = "applicationdb";
        $table = "applicants";
        $db = connectToDB($host, $user, $password, $database);



        $display_fields = "";
        foreach ($_POST["display"] as $entry) {
            $display_fields .= $entry;
            $display_fields .= ",";
        }
        $display_fields = rtrim($display_fields, ",");


        $sqlQuery = "";
        if($_POST["filter"] == null) {
            $sqlQuery .= sprintf("select %s from %s order by %s ", $display_fields, "applicants", $_POST["sort"]);
        }
        else {
            $sqlQuery .= sprintf("select %s from %s where %s order by %s ", $display_fields, "applicants", $_POST["filter"], $_POST["sort"]);
        }

        $result = mysqli_query($db, $sqlQuery);
        if ($result) {
            $numberOfRows = mysqli_num_rows($result);
            if ($numberOfRows == 0) {
                $body = "<h2>No entry exists in the database for the specified email and password</h2>";
                $body .= "<a class=\"btn btn-default\" href=\"main.html\">Return to main menu</a><br>";
            } else {
                $body = "";
                $body .= "<h1>Applications</h1>";
                $body .= "<center><table border='1'><tr>";
                foreach ($_POST["display"] as $entry) {
                    $body .= "<th>" . $entry . "</th>";
                }
                $body .= "</tr>";
                while ($recordArray = mysqli_fetch_array($result, MYSQLI_ASSOC)) {
                    $body .= "<tr>";
                    foreach ($recordArray as $entry) {
                        $body .= "<td>" . $entry . "</td>";
                    }
                    $body .= "</tr>";
                }
                $body .= "</table></center><br><br>";
                $body .= "<a class=\"btn btn-default\" href=\"main.html\">Return to main menu</a><br>";
            }
            mysqli_free_result($result);
        }  else {
            $body = "Retrieving records failed.".mysqli_error($db);
        }

        /* Closing */
        mysqli_close($db);

    }



    function connectToDB($host, $user, $password, $database) {
        $db = mysqli_connect($host, $user, $password, $database);
        if (mysqli_connect_errno()) {
            echo "Connect failed.\n".mysqli_connect_error();
            exit();
        }
        return $db;
    }

    echo generatePage($body);

