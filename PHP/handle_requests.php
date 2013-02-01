<?php
session_start();

$con = mysql_connect('68.178.137.113', 'dsntianyi', 'Digital!1');
if (!$con) {
    die('Could not connect: ' . mysql_error());
}
mysql_select_db('dsntianyi', $con);

$request_name = $_POST['request_name'];

# Handle request.
if ($request_name === 'authenticate') {
    # Escape special characters.
    if (!get_magic_quotes_gpc()) {
        $email = mysql_real_escape_string($_POST['email']);
    } else {
        $email = $_POST['email'];
    }

    # Check against the DB.
    $qry = "SELECT user_name FROM users WHERE email='" . $email . "' AND password='" . md5($_POST['pwd']) . "'";
    $qry_result = mysql_query($qry);

    if (mysql_num_rows($qry_result) > 0) {
        $result = array('success' => true, 'info' => 'User exists.');
    } else {
        $result = array('success' => false, 'info' => 'User does not exist.');
    }
} else {
    $result = array('success' => false, 'info' => 'Request Not Supported!');
}

mysql_close($con);

echo json_encode($result);
?>
