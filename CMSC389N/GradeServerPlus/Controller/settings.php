<?php
 session_start();
 require_once 'dbconnect.php';
 $username = $_SESSION['username'];
?>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Settings</title>

    <!-- Bootstrap -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="container">
  <div class="form-group row">
    <h3>Profile Picture Change</h3>
    <form action="settings.php" method="POST" enctype="multipart/form-data">
      <input type="file" name="image" /><br>
        <input style="width: 100px; height: 30px" class="btn btn-block btn-primary" type="submit" name="submit" value="Upload" />
      <br />
      <?php
  
        if (isset($_POST['submit'])) {
          
          $imageName = mysql_real_escape_string($_FILES["image"]["name"]);
          $imageData = mysql_real_escape_string(file_get_contents($_FILES["image"]["tmp_name"]));
          $imageType = mysql_real_escape_string($_FILES["image"]["type"]);


          

          if (substr($imageType, 0, 5) == "image") {
            mysql_query("UPDATE users SET image_name='$imageName', image_data='$imageData' WHERE name='$username'");
            echo "Image Uploaded: ";
            echo $imageName."<br />";
            
          } else {
            echo "error";
          }


        }
      ?>
      
    </form>
    <h3>Password change</h3>
    <form action="settings.php" method="POST" enctype="multipart/form-data">
      <input type="password" name="oldPassword" class="form-control" placeholder="Enter Old Password" maxlength="15" />
      <input type="password" name="newPassword" class="form-control" placeholder="Enter New Password" maxlength="15" />
      
      <?php
  
        $error = false;

        if ( isset($_POST['chanegPassword']) ) {

        // clean user inputs to prevent sql injections
        $oldPassword = trim($_POST['oldPassword']);
        $newPassword = trim($_POST['newPassword']);
        // password encrypt using md5();
        $oldPassword = md5($oldPassword);
        $newPassword = md5($newPassword);


         // check username exist or not
         $query = "SELECT password FROM users WHERE name='$username'";
         $result = mysql_query($query);
         $row = mysql_fetch_array($result);
         if($row['password'] != $oldPassword ){
          $error = true;
          echo "<b>Old password is incorrect. Please try again...</b>";
         }


        // if there's no error, continue to signup
        if(!$error) {
         
         $query = "UPDATE users SET password='$newPassword' WHERE name='$username'";
         $res = mysql_query($query);
          
         if ($res) {
          echo "Password Successfully Changed.";
         } else {
          echo "Something went wrong, try again later..."; 
         } 
          
        }


        }
      ?>
      <hr />
      <input style="width: 100px; height: 30px" type="submit" class="btn btn-block btn-primary" name="chanegPassword" value="Submit"/>
      <br />
      
    </form>

    <a href="main.php">Go back to main page</a>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
  </div>
  </body>
</html>