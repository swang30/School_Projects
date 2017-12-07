<?php
require_once("support.php");
session_start();
$topPart = <<<EOBODY
        <h1>Section Information</h1>
		<form action="{$_SERVER['PHP_SELF']}" method="post">
			<strong>Course Name(e.g., cmsc128):&nbsp</strong><input type="text" name="course name" /><br>
			<strong>Section (e.g., 0101):&nbsp</strong><input type="text" name="section name" /><br>
			
			<!--We need the submit button-->
			<input type="submit" name="submitSectionButton" />
		</form>		
EOBODY;

$body = $topPart;
$page = generatePage($body);
echo $page;

if(isset($_POST["submitSectionButton"])) {
    $_SESSION['courseName'] = $_POST["course_name"];
    $_SESSION['section'] = $_POST["section_name"];
    header("Location: grade_submission.php");
}
?>
