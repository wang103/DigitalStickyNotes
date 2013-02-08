<?php

function generateToken() {
    $fp = fopen('/dev/urandom', 'rb');
    $entropy = fread($fp, 32);
    fclose($fp);

    $entropy .= uniqid(mt_rand(), true);
    return sha1($entropy);
}

?>
