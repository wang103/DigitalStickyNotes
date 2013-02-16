<?php

require "lib/server/MySQLOAuth2.inc";

$oauth = new MySQLAuth2();
$oauth->grantAccessToken();
