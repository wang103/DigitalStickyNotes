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
     * Helper function to add a new client to the database.
     *
     * @param $client_id Client identifier to be stored.
     * @param $client_secret Client secret to be stored.
     * @param $redirect_uri Redirect URI to be stored.
     */
    public function addClient($client_id, $client_secret, $redirect_uri) {
        $qry = 'INSERT INTO clients (id, secret, redirect_uri) VALUES ("' . $client_id . '", "' . $client_secret . '", "' . $redirect_uri . '")';
        if (!mysql_query($qry, $con)) {
            die('Error: ' . mysql_error());
        }
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

    /**
     * Implements OAuth2::getRedirectUri().
     */
    protected function getRedirectUri($client_id) {
        $qry = "SELECT redirect_uri FROM clients WHERE id='" . $client_id . "'";
        $qry_result = mysql_query($qry);

        if (mysql_num_rows($qry_result) > 0) {
            $client = mysql_fetch_assoc($qry_result);
            return $client['redirect_uri'];
        } else {
            return false;
        }
    }
    
    /**
     * Implements OAuth2::getAccessToken().
     */
    protected function getAccessToken($oauth_token) {
        $qry = "SELECT client_id,expires,scope FROM tokens WHERE token_id='" . $oauth_token . "'";
        $qry_result = mysql_query($qry);

        if (mysql_num_rows($qry_result) > 0) {
            $client = mysql_fetch_assoc($qry_result);
            return array("client_id" => $client['client_id'], "expires" => $client['expires'], "scope" => $client['scope']);
        } else {
            return NULL;
        }
    }

    /**
     * Implements OAuth2::setAccessToken().
     */
    protected function setAccessToken($oauth_token, $client_id, $expires, $scope = NULL) {
        $qry = 'INSERT INTO tokens (token_id, client_id, expires, scope) VALUES ("' . $oauth_token . '", "' . $client_id . '", "' . $expires . '", "' . $scope . '")';
        if (!mysql_query($qry, $con)) {
            die('Error: ' . mysql_error());
        }
    }

    /**
     * Overrides OAuth2::getSupportedGrantTypes().
     */
    protected function getSupportedGrantTypes() {
        return array(
            OAUTH2_GRANT_TYPE_AUTH_CODE,
        );
    }

    /**
     * Overrides OAuth2::getAuthCode().
     */
    protected function getAuthCode($code) {
        $qry = "SELECT client_id,redirect_uri,expires,scope FROM auth_codes WHERE code_id='" . $code . "'";
        $qry_result = mysql_query($qry);

        if (mysql_num_rows($qry_result) > 0) {
            $client = mysql_fetch_assoc($qry_result);
            return array("client_id" => $client['client_id'], "redirect_uri" => $client['redirect_uri'], "expires" => $client['expires'], "scope" => $client['scope']);
        } else {
            return NULL;
        }
    }
    
    /**
     * Overrides OAuth2::setAuthCode().
     */
    protected function setAuthCode($code, $client_id, $redirect_uri, $expires, $scope = NULL) {
        $qry = 'INSERT INTO auth_codes (code_id, client_id, redirect_uri, expires, scope) VALUES ("' . $code . '", "' . $client_id . '", "' . $redirect_uri . '", "' . $expires . '", "' . $scope . '")';
        if (!mysql_query($qry, $con)) {
            die('Error: ' . mysql_error());
        }
    }
}
