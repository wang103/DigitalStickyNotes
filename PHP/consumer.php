<?php
include 'lib/server/Utils.inc';

if ($_POST['code'] === 'tianyiwang') {
    # Generate the consumer id and secret.
    $consumer_id = generateToken();
    $consumer_secret = generateToken();

    # Save them in the DB.


    # Present them to the requester.
    echo "Consumer id: " . $consumer_id . "<br/>Consumer secret: " . $consumer_secret;
} else {
    echo '<p>Wrong code.</p>';
}

?>
