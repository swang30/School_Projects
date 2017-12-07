<!doctype html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Order Request Form</title>
</head>

<body>
<?php
 require_once('softwares.php');
?>

    <div style="border: double; border-radius: 1em; padding: 0.4em;" >



    <form action = "processRequest.php" method="post" name="fm">
        <h1><u>Order Request Form</u></h1>
        <p style="display: inline"> <strong>Last Name:</strong> <input type="text" name="Lastname" required/>
            <strong>First Name:</strong>  <input type="text" name="Firstname" required/>  <br /><br />
            <strong>Email:</strong> <input type="text" name="Email" placeholder="example@notreal.notreal" style="width: 15em" required
                                            pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$"/>    <br /><br />
        </p>

        <p>
            <strong>Shipping Method: </strong>
            <input type="radio" name="shipping method" value="UPSS"> UPSS
            <input type="radio" name="shipping method" value="FedEXC"> FedEXC
            <input type="radio" name="shipping method" value="USMAIL" checked="checked"> USMAIL
            <input type="radio" name="shipping method" value="Other"> Other
        </p>

        <p>
            <strong>Softwares: </strong> <br />
            <select name="softwares[]" multiple="multiple" style="overflow-y: scroll" required>
                    <?php
                    $keys = array_keys($softwares);
                    foreach ($keys as $key)
                        echo "<option value=\"$key\">$key ($$softwares[$key])</option>";
                    ?>
            </select>
        </p>

        <p>
            <strong>Order Specifications</strong>
            <br/>
            <textarea rows="4" cols="50" name="specifications" id = "specifications" required></textarea>
        </p>

        <p>
            <input type="reset" value="Reset Fields" />
            <input type="submit" value="Submit Request"/>
        </p>


    </form>

    </div>

</body>
</html>