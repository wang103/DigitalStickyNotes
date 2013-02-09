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
$oauth->verifyAccessToken();

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
}
elseif ($request_name == 'register') {
    # Escape special characters.
    if (!get_magic_quotes_gpc()) {
        $email = mysql_real_escape_string($_POST['email']);
        $usrname = mysql_real_escape_string($_POST['username']);
    } else {
        $email = $_POST['email'];
        $usrname = $_POST['username'];
    }
    $firstname = $_POST['firstname'];
    $lastname = $_POST['lastname'];
    $pwd = md5($_POST['pwd']);

    # Check against the DB.
    $qry = "SELECT user_name FROM users WHERE email='" . $email . "'";
    $qry_result = mysql_query($qry);
    if (mysql_num_rows($qry_result) > 0) {
        $result = array('code' => 1, 'info' => 'Email exists.');
        mysql_close($con);
        echo json_encode($result);
        die();
    }

    $qry = "SELECT user_name FROM users WHERE user_name='" . $usrname . "'";
    $qry_result = mysql_query($qry);
    if (mysql_num_rows($qry_result) > 0) {
        $result = array('code' => 2, 'info' => 'Username exists.');
        mysql_close($con);
        echo json_encode($result);
        die();
    }

    # Insert new user into the DB.
    $qry = 'INSERT INTO users (user_name, password, first_name, last_name, email) VALUES ("' . $usrname . '", "' . $pwd . '", "' . $firstname . '", "' . $lastname . '", "' . $email . '")';
    if (!mysql_query($qry, $con)) {
        die('Error: ' . mysql_error());
    }
    $result = array('code' => 0, 'info' => 'Register successful.');
}
else {
    $result = array('success' => false, 'info' => 'Request Not Supported!');
}

mysql_close($con);

echo json_encode($result);
?>
