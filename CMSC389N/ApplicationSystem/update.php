<?php
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
                        $body .= "<form action=\"update.php\" method=\"post\">";
                        $body .= "<Strong>Name: </Strong><input type='text' name='name' value='" . $recordArray["name"] ."'><br>";


                        $body .= "<Strong>Email: </Strong><input style='width: 15em;' type='text' name='email' value='" . $recordArray["email"] ."'><br>";
                        $body .= "<Strong>GPA: </Strong><input type='text' name='GPA' value='" . $recordArray["gpa"] ."'><br>";
                        if($recordArray["year"] == 10) {
                            $body .= "<strong>Year: </strong><input type=\"radio\" name=\"year\" value=\"10\" checked='checked'/>10 
                                      <input type=\"radio\" name=\"year\" value=\"11\"/>11 <input type=\"radio\" name=\"year\" value=\"12\"/>12 <br>";
                        }
                        else if($recordArray["year"] == 11) {
                            $body .= "<strong>Year: </strong><input type=\"radio\" name=\"year\" value=\"10\"/>10 
                                      <input type=\"radio\" name=\"year\" value=\"11\" checked='checked'/>11 <input type=\"radio\" name=\"year\" value=\"12\"/>12 <br>";
                        } else {
                            $body .= "<strong>Year: </strong><input type=\"radio\" name=\"year\" value=\"10\"/>10 
                                      <input type=\"radio\" name=\"year\" value=\"11\" />11 <input type=\"radio\" name=\"year\" value=\"12\" checked='checked'/>12 <br>";
                        }
                        if($recordArray["gender"] == "M") {
                            $body .= "<strong>Gender: </strong><input type=\"radio\" name=\"gender\" value=\"M\" checked='checked'/>M <input type=\"radio\" name=\"gender\" value=\"F\"/>F <br>";
                        } else {
                            $body .= "<strong>Gender: </strong><input type=\"radio\" name=\"gender\" value=\"M\"/>M <input type=\"radio\" name=\"gender\" value=\"F\" checked='checked'/>F <br>";
                        }
                        $body .= "<Strong>Password: </Strong><input type='password' name='password' value='" . $_POST["password"] ."'><br>";
                        $body .= "<Strong>Verify Password: </Strong><input type='password' name='verify password' value='" . $_POST["password"] ."'><br>";
                        $body .= "<input type=\"submit\" name=\"submit update\" value=\"Submit Update\"/> <br><br>";
                        $body .= "</form>";
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

    if(isset($_POST["submit_update"])) {
        if(trim($_POST["password"]) != trim($_POST["verify_password"])) {
            $body = "<h2>Please check your password</h2><br>";
            $body .= "<a class=\"btn btn-default\" href=\"main.html\">Return to main menu</a><br>";
        }
        else {
            if($_POST['email'] != null) {
                $applicant = new Applicant(trim($_POST["name"]), trim($_POST["email"]), trim($_POST["GPA"]), trim($_POST["year"]), trim($_POST["gender"]), trim($_POST["password"]));

                $host = "localhost";
                $user = "dbuser";
                $password = "goodbyeWorld";
                $database = "applicationdb";
                $table = "applicants";
                $db = connectToDB($host, $user, $password, $database);

                $hash_password = password_hash($applicant->password,PASSWORD_DEFAULT);

                $sqlQuery = sprintf("update $table set email='%s', gpa=%s, year=%s, gender='%s', password='%s' where name='%s'",
                    $applicant->email, $applicant->GPA, $applicant->year, $applicant->gender, $hash_password, $applicant->name);
                $result = mysqli_query($db, $sqlQuery);
                if ($result) {
                    $body = "<h3>The entry has been updated in the database and the new values are</h3><br>";
                } else {
                    $body = "Updating records failed.".mysqli_error($db);
                }
                $body .= "<strong>Name: </strong> $applicant->name<br>";
                $body .= "<strong>Email: </strong> $applicant->email<br>";
                $body .= "<strong>Gpa: </strong> $applicant->GPA<br>";
                $body .= "<strong>Year: </strong> $applicant->year<br>";
                $body .= "<strong>Gender: </strong> $applicant->gender<br>";
                $body .= "<a class=\"btn btn-default\" href=\"main.html\">Return to main menu</a><br>";
            }
        }
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