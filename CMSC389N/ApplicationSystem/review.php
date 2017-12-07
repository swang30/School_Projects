<?php
    declare(strict_types=1);
    require_once("support.php");
    require_once("Applicant.php");


    $body = <<<EOBODY
    <form action="{$_SERVER['PHP_SELF']}" method="post">
        <strong>Email associated with application: </strong><input type="text" name="email"/><br>
		<strong>Password associated with application </strong><input type="password" name="password"/><br>
		<input type="submit" name="submit review" value="Submit"/> <br><br>
    </form>
    <a class="btn btn-default" href="main.html">Return to main menu</a><br>
EOBODY;

    if(isset($_POST["submit_review"])) {

        $host = "localhost";
        $user = "dbuser";
        $password = "goodbyeWorld";
        $database = "applicationdb";
        $table = "applicants";
        $db = connectToDB($host, $user, $password, $database);



        $sqlQuery = sprintf("select * from %s where email='%s' ", "applicants", $_POST["email"]);
        $result = mysqli_query($db, $sqlQuery);
        if ($result) {
            $numberOfRows = mysqli_num_rows($result);
            if ($numberOfRows == 0) {
                $body = "<h2>No entry exists in the database for the specified email and password</h2>";
                $body .= "<a class=\"btn btn-default\" href=\"main.html\">Return to main menu</a><br>";
            } else {
                $body = "";
                while ($recordArray = mysqli_fetch_array($result, MYSQLI_ASSOC)) {
                    if( password_verify($_POST["password"], $recordArray["password"]) ) {
                        $body .= "<h3>Application found in the database with the following values</h3><br>";
                        $body .= "<Strong>Name: </Strong>" . $recordArray["name"] . "<br>";
                        $body .= "<Strong>Email: </Strong>" . $recordArray["email"] . "<br>";
                        $body .= "<Strong>Gpa: </Strong>" . $recordArray["gpa"] . "<br>";
                        $body .= "<Strong>Year: </Strong>" . $recordArray["year"] . "<br>";
                        $body .= "<Strong>Gender: </Strong>" . $recordArray["gender"] . "<br>";
                    } else {
                        $body = "<h3>No entry exists in the database for the specified email and password</h3>";
                    }
                }
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
?>