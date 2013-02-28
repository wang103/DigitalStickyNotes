<?php
session_save_path('/home/content/49/10017049/html/tmp/sessions');
session_start();

require "lib/server/MySQLOAuth2.inc";

$con = mysql_connect('68.178.137.113', 'dsntianyi', 'Digital!1');
if (!$con) {
    die('Could not connect: ' . mysql_error());
}
mysql_select_db('dsntianyi', $con);

# Before handling any requests, check access token first.
$oauth = new MySQLAuth2();
if ($oauth->verifyAccessToken() != true) {
    $result = array('success' => false, 'info' => 'Invalid access token!');
    mysql_close($con);
    echo json_encode($result);
    exit;
}

$request_name = $_POST['request_name'];

# Handle request.
if ($request_name === 'get_notes') {
    $location_id = $_POST['location_id'];
    $server_pwd = md5($_POST['server_pwd']);
    $user_name = $oauth->getUsername();



    $result = array('success' => false);
}
else {
    $result = array('success' => false, 'info' => 'Request Not Supported!');
}

mysql_close($con);

echo json_encode($result);
?>
