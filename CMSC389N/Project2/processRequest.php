<?php
    require_once ("softwares.php");

    echo "<strong style='font-size: 3em'><u>Order Confirmation</u></strong></br></br>";
    echo "<strong>Lastname:&nbsp</strong>". trim($_POST["Lastname"]). ",&nbsp&nbsp&nbsp&nbsp&nbsp";
    echo "<strong>Firstname:&nbsp</strong>". trim($_POST["Firstname"]). ",&nbsp&nbsp&nbsp&nbsp&nbsp";
    echo "<strong>Email:&nbsp</strong>". trim($_POST["Email"])."</br></br>";
    echo "<strong>Shipping Method:&nbsp</strong>" . $_POST["shipping_method"]. "</br></br>";
    echo "<strong>Software Order:&nbsp</strong></br></br>";

    displayTable($softwares,0);

    echo "<strong>Order Specifications:</strong>";
    echo "<p><i>" .nl2br($_POST["specifications"]) . "</i></p>";

    function displayTable($softwares,$total_cost) {
        echo "<table border=\"1\">";
        echo "<tr>"."<th>Software</th>"."<th>Cost</th>"."</tr>";
        foreach ( $_POST["softwares"] as $entry) {
            echo "<tr>"."<td>$entry</td>". "<td>$$softwares[$entry]</td>";
            $total_cost += $softwares[$entry];
        }
        echo "<tr>" . "<td>total</td>" . "<td>$$total_cost</td>";
        echo "</table></br></br>";
    }
?>