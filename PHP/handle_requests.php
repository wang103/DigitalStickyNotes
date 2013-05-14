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

    $qry = "SELECT message_id,available_time,expire_time,title,message,sender_id FROM messages WHERE location_id='" . $location_id . "' AND receiver_id='" . $user_name . "'";
    $qry_result = mysql_query($qry);
    $num_msg = mysql_num_rows($qry_result);
    $messages = array();
    while ($row = mysql_fetch_array($qry_result)) {
        $message = array('message_id' => $row['message_id'],
                         'received_time' => date('Y-m-d H:i:s'),
                         'available_time' => $row['available_time'],
                         'expire_time' => $row['expire_time'],
                         'title' => $row['title'],
                         'message' => $row['message'],
                         'server_loc' => $loc_name,
                         'sender_id' => $row['sender_id']);
        array_push($messages, $message);
    }

    $result = array('success' => true, 'num_msg' => $num_msg, 'messages' => $messages);
}
elseif ($request_name === 'send_note') {
    $available_time = DateTime::createFromFormat('Y-m-d H:i:s', $_POST['available_time']);
    $expire_time = DateTime::createFromFormat('Y-m-d H:i:s', $_POST['expire_time']);
    $location_id = $_POST['location_id'];
    $title = $_POST['title'];
    $note = $_POST['note'];
    $sender = $_POST['sender'];

    # Insert new note into the DB.
    $qry = 'INSERT INTO messagse (available_time, expire_time, location_id, title, message, sender_id, receiver_id) VALUES ("' . $available_time . '", "' . $expire_time . '", "' . $location_id . '", "' . $title . '", "' . $note . '", "' . $sender . '")';
    if (!mysql_query($qry, $con)) {
        die('Error: ' . mysql_error());
    }

    $result = array('success' => true);
}
elseif ($request_name === 'get_usr_info') {
    $result = array('success' => true,
                    'username' => $oauth->getUsername(),
                    'firstname' => $oauth->getUserFirstname(),
                    'lastname' => $oauth->getUserLastname());
}
elseif ($request_name === 'get_locations') {
    $qry = "SELECT location_id,location_name FROM local_servers";
    $qry_result = mysql_query($qry);
    $num_loc = mysql_num_rows($qry_result);
    $locations = array();
    while ($row = mysql_fetch_array($qry_result)) {
        $location = array('location_id' => $row['location_id'],
                          'location_name' => $row['location_name']);
        array_push($locations, $location);
    }

    $result = array('success' => true, 'num_loc' => $num_loc, 'locations' => $locations);
}
else {
    $result = array('success' => false, 'info' => 'Request Not Supported!');
}

mysql_close($con);

echo json_encode($result);
?>
