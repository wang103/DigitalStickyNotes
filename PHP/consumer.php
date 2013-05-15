<?php
include 'lib/server/Utils.inc';
include 'lib/server/MySQLOAuth2.inc';

if ($_POST['code'] === 'tianyiwang') {
    # Generate the consumer id and secret.
    $consumer_id = generateToken();
    $consumer_secret = generateToken();

    # Save them in the DB.
    $mysqlAuth2 = new MySQLAuth2();
    $mysqlAuth2->addClient($consumer_id, $consumer_secret, $_POST['uri']);

    # Present them to the requester.
    echo "Consumer id: " . $consumer_id . "<br/>Consumer secret: " . $consumer_secret . "<br/>";
    echo "Please write those down somewhere.";
} else {
    echo '<p>Wrong code.</p>';
}
?>
