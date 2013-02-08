<?php

if ($_POST['code'] === 'tianyiwang') {
    # Generate the consumer id and secret.
    $consumer_id = sha1(OAuthProvider::generateToken(40, true));
    $consumer_secret = sha1(OAuthProvider::generateToken(40, true));

    # Save them in the DB.


    # Present them to the requester.
    echo "Consumer id: " . $consumer_id . "<br/>Consumer secret: " . $consumer_secret;
} else {
    echo '<p>Wrong code.</p>';
}

?>
