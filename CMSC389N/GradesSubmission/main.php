<?php
require_once("support.php");

if(empty($_COOKIE['cmsc298s'])) {
    $topPart = <<<EOBODY
        <h1>Grade Submission System</h1>
		<form action="{$_SERVER['PHP_SELF']}" method="post">
			<strong>LoginID: </strong><input type="text" name="name" /><br>
			<strong>Password: </strong><input type="password" name="password"/><br>
			
			<!--We need the submit button-->
			<input type="submit" name="submitInfoButton" /><br>
		</form>		
EOBODY;

    $bottomPart = "";
    if (isset($_POST["submitInfoButton"])) {
        $nameValue = trim($_POST["name"]);
        $passwordValue = trim($_POST["password"]);
        setcookie($nameValue, $passwordValue, 0);

        if ($nameValue !== "cmsc298s" || ($passwordValue !== "terps"))
            $bottomPart .= "<h1>Invalid login information provided</h1><br />";
        $passwordValue = "";
        $nameValue = "";
        if ($bottomPart == "") {
            header("Location: sec_info.php");
        }


    }
}
else {
    header("Location: sec_info.php");
}



$body = $topPart.$bottomPart;
$page = generatePage($body);
echo $page;
?>