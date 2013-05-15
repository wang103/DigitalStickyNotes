<?php
$con = mysql_connect('68.178.137.113', 'dsntianyi', 'Digital!1');
if (!$con) {
    die('Could not connect: ' . mysql_error());
}
mysql_select_db('dsntianyi', $con);

$location_id = $_POST['location_id'];
$server_pwd = md5($_POST['server_pwd']);

# Authorize the location.
$qry = "SELECT location_id,location_name FROM local_servers where location_id='" . $location_id . "' AND password='" . $server_pwd . "'";
$qry_result = mysql_query($qry);
if (mysql_num_rows($qry_result) == 0) {
    $result = array('success' => false, 'info' => 'Authorization for local server failed!');
    mysql_close($con);
    echo json_encode($result);
    exit;
}
$row = mysql_fetch_assoc($qry_result);
$loc_name = $row['location_name'];

$result = array('success' => true, 'name' => $loc_name);

mysql_close($con);

echo json_encode($result);
?>
