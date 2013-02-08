<?php

include "../OAuth2.inc";

/**
 * OAuth2 Library MySQL DB Implementation.
 */
class MySQLAuth2 extends OAuth2 {

    private $con;

    /**
     * Overrides OAuth2::__construct().
     */
    public function __construct() {
        parent::__construct();

        $con = mysql_connect('68.178.137.113', 'dsntianyi', 'Digital!1');
        if (!$con) {
            die('Could not connect: ' . mysql_error());
        }
        mysql_select_db('dsntianyi', $con);
    }

    /**
     * Implements OAuth2::checkClientCredentials().
     */
    protected function checkClientCredentials($client_id, $client_secret = NULL) {
        $qry = "SELECT id FROM clients WHERE id='" . $client_id . "' AND secret='" . md5($client_secret) . "'";
        $qry_result = mysql_query($qry);

        if (mysql_num_rows($qry_result) > 0) {
            return true;
        } else {
            return false;
        }
    }

}
