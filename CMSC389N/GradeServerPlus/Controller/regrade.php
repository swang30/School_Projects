<?php
require_once("support.php");
require '../PHPMailer-master/PHPMailerAutoload.php';

$body = <<<EOBODY
<div class="container">
  <form action="{$_SERVER['PHP_SELF']}" method="post">
    <div class="form-group row">
      <label for="inputEmail3" class="col-sm-2 col-form-label">Name</label>
      <div class="col-sm-10">
        <input name="name" type="text" class="form-control" id="inputEmail3" placeholder="Your Full Name">
      </div>
    </div>
    <div class="form-group row">
      <label for="inputPassword3" class="col-sm-2 col-form-label">Assignment #</label>
      <div class="col-sm-10">
        <input name="assignment" type="text" class="form-control" id="inputPassword3" placeholder="Assignment Number">
      </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-2 col-form-label">Specifications: </label>
        <div class="col-sm-10">
        <textarea name="specification" class="form-control" id="exampleTextarea" rows="3"></textarea>
        </div>
  </div>
    <fieldset class="form-group row">
      <legend class="col-form-legend col-sm-2">Options</legend>
      <div class="col-sm-10">
        <div class="form-check">
          <label class="form-check-label">
            <input class="form-check-input" type="radio" name="gridRadios" id="gridRadios1" value="option1" checked>
            Email Confirmation
          </label>
        </div>
        <div class="form-check">
          <label class="form-check-label">
            <input class="form-check-input" type="radio" name="gridRadios" id="gridRadios2" value="option2">
            No Email Confirmation
          </label>
        </div>
      </div>
    </fieldset>
    <div class="form-group row">
      <div class="offset-sm-2 col-sm-10">
        <input type="submit" class="btn btn-primary" name="submit data" value="Submit Regrade"</input>
      </div>
    </div>
  </form>
</div>
EOBODY;

if(isset($_POST["submit_data"])) {

    $body = "<p>the following information has been submitted to your grader</p><br>";
    $body .= "<p>Name: " . $_POST["name"] . "</p>";
    $body .= "<p>Assignment number: " . $_POST["assignment"] . "</p>";
    $body .= "<p>Specifications: " . $_POST["specification"] . "</p>";


    $mail = new PHPMailer;
    $mail->isSMTP();
    $mail->SMTPSecure = 'ssl';
    $mail->SMTPAuth = true;
    $mail->Host = 'smtp.gmail.com';
    $mail->Port = 465;
    $mail->Username = 'stone.shanshiwang@gmail.com';
    $mail->Password = 'wss670130';
    $mail->setFrom('stone.shanshiwang@gmail.com');
    $mail->addAddress('swang30@umd.edu');
    $mail->Subject = 'Your Regrade Request';
    $mail->Body = $body;
//send the message, check for errors
    if (!$mail->send()) {
        echo "ERROR: " . $mail->ErrorInfo;
    } else {
        $body .= "<p>You will receive a confirmation email soon</p>";
    }

}


echo generatePage($body);
?>
