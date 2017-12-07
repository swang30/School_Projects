<?php
    require_once("support.php");
    require_once("Applicant.php");

    $body = <<<EOBODY
    <form action="{$_SERVER['PHP_SELF']}" method="post">
        <strong>Name: </strong><input type="text" name="name" /><br>
		<strong>Email: </strong><input type="email" name="email"/><br>
		<strong>GPA: </strong><input type="text" name="GPA"/><br>
		<strong>Year: </strong><input type="radio" name="year" value="10"/>10 <input type="radio" name="year" value="11"/>11 <input type="radio" name="year" value="12"/>12 <br>
		<strong>Gender: </strong><input type="radio" name="gender" value="M"/>M <input type="radio" name="gender" value="F"/>F <br>
		<strong>Password: </strong><input type="password" name="password"/><br>
		<strong>Verify Password: </strong><input type="password" name="verify password"/><br><br>
		<input type="submit" name="submit data" value="Submit Data"/> <br><br>
    </form>
    <a class="btn btn-default" href="main.html">Return to main menu</a><br>
EOBODY;

    if(isset($_POST["submit_data"])) {
        if(trim($_POST["password"]) != trim($_POST["verify_password"])) {
            $body .= "Please check your password";
        }
        else {
            if($_POST['email'] != null) {
                $applicant = new Applicant(trim($_POST["name"]), trim($_POST["email"]), trim($_POST["GPA"]), trim($_POST["year"]), trim($_POST["gender"]), trim($_POST["password"]));

                $host = "localhost";
                $user = "root";
                $password = "goodbyeWorld";
                $database = "applicationdb";
                $table = "applicants";
                $db = connectToDB($host, $user, $password, $database);

                $hash_password = password_hash($applicant->password,PASSWORD_DEFAULT);

                $sqlQuery = sprintf("insert into $table (name, email, gpa, year, gender, password) values ('%s', '%s', %s, %s, '%s', '%s')",
                    $applicant->name, $applicant->email, $applicant->GPA, $applicant->year, $applicant->gender, $hash_password);
                $result = mysqli_query($db, $sqlQuery);
                if ($result) {
                    $body = "<h3>The following entry has been added to database</h3><br>";
                } else {
                    $body = "Inserting records failed.".mysqli_error($db);
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


    function connectToDB($host, $user, $ , $database) {
        $db = mysqli_connect($host, $user, $password, $database);
        if (mysqli_connect_errno()) {
            echo "Connect failed.\n".mysqli_connect_error();
            exit();
        }
        return $db;
    }

    echo generatePage($body);
?>