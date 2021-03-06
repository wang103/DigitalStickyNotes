<?php
include 'lib/OAuth2.inc';

/**
 * OAuth2 Library MySQL DB Implementation.
 */
class MySQLAuth2 extends OAuth2 {

    private $con;

    /**
     * Overrides OAuth2::__construct().
     */
    public function __construct() {
        parent::__construct(array("display_error" => true));

        $this->con = mysql_connect('68.178.137.113', 'dsntianyi', 'Digital!1');
        if (!$this->con) {
            die('Could not connect: ' . mysql_error());
        }
        mysql_select_db('dsntianyi', $this->con);
    }

    /**
     * Helper function to add a new client to the database.
     *
     * @param $client_id Client identifier to be stored.
     * @param $client_secret Client secret to be stored.
     * @param $redirect_uri Redirect URI to be stored.
     */
    public function addClient($client_id, $client_secret, $redirect_uri) {
        $redirect_uri = mysql_real_escape_string($redirect_uri);
        $qry = 'INSERT INTO clients (id, secret, redirect_uri) VALUES ("' . $client_id . '", "' . $client_secret . '", "' . $redirect_uri . '")';
        
        if (!mysql_query($qry, $this->con)) {
            die('Error: ' . mysql_error());
        }
    }

    /**
     * Implements OAuth2::checkClientCredentials().
     */
    protected function checkClientCredentials($client_id, $client_secret = NULL) {
        $qry = "SELECT id FROM clients WHERE id='" . $client_id . "' AND secret='" . $client_secret . "'";
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
        $qry = "SELECT user_name,client_id,expires,scope FROM tokens WHERE token_id='" . $oauth_token . "'";
        $qry_result = mysql_query($qry);

        if (mysql_num_rows($qry_result) > 0) {
            $client = mysql_fetch_assoc($qry_result);
            return array("user_name" => $client['user_name'],
                         "client_id" => $client['client_id'],
						 "expires" => $client['expires'],
						 "scope" => $client['scope']);
        } else {
            return NULL;
        }
    }

    /**
     * Implements OAuth2::setAccessToken().
     */
    protected function setAccessToken($oauth_token, $user_name, $client_id, $expires, $scope = NULL) {
        $qry = 'INSERT INTO tokens (token_id, user_name, client_id, expires, scope) VALUES ("' . $oauth_token . '", "' . $user_name . '", "' . $client_id . '", "' . $expires . '", "' . $scope . '")';
        if (!mysql_query($qry, $this->con)) {
            die('Error: ' . mysql_error());
        }
    }

    /**
     * Overrides OAuth2::getSupportedGrantTypes().
     */
    protected function getSupportedGrantTypes() {
        return array(
			OAUTH2_GRANT_TYPE_AUTH_CODE,
			OAUTH2_GRANT_TYPE_USER_CREDENTIALS,
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
            return array("client_id" => $client['client_id'],
                         "redirect_uri" => $client['redirect_uri'],
                         "expires" => $client['expires'],
                         "scope" => $client['scope']);
        } else {
            return NULL;
        }
    }
    
    /**
     * Overrides OAuth2::setAuthCode().
     */
    protected function setAuthCode($code, $client_id, $redirect_uri, $expires, $scope = NULL) {
        $qry = 'INSERT INTO auth_codes (code_id, client_id, redirect_uri, expires, scope) VALUES ("' . $code . '", "' . $client_id . '", "' . $redirect_uri . '", "' . $expires . '", "' . $scope . '")';
        if (!mysql_query($qry, $this->con)) {
            die('Error: ' . mysql_error());
        }
    }

    /**
     * Overrides OAuth2::checkUserCredentials().
     */
    protected function checkUserCredentials($client_id, $username, $password) {
        $qry = "SELECT user_name FROM users WHERE email='" . $username . "' AND password='" . md5($password) . "'";
        $qry_result = mysql_query($qry);

        if (mysql_num_rows($qry_result) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Helper function to get the user name.
     * Assume the access token is in HTTP header, and it is valid.
     */
    public function getUsername() {
        $token_param = $this->getAccessTokenParams();
        $token = $this->getAccessToken($token_param);
        $user_email = $token['user_name'];

        $qry = "SELECT user_name FROM users WHERE email='" . $user_email . "'";
        $qry_result = mysql_query($qry);
        
        $user = mysql_fetch_assoc($qry_result);
        return $user['user_name'];
    }

    /**
     * Helper function to get the user's first name.
     * Assume the access token is in HTTP header, and it is valid.
     */
    public function getUserFirstname() {
        $token_param = $this->getAccessTokenParams();
        $token = $this->getAccessToken($token_param);
        $user_email = $token['user_name'];

        $qry = "SELECT first_name FROM users WHERE email='" . $user_email . "'";
        $qry_result = mysql_query($qry);
        
        $user = mysql_fetch_assoc($qry_result);
        return $user['first_name'];
    }

    /**
     * Helper function to get the user's last name.
     * Assume the access token is in HTTP header, and it is valid.
     */
    public function getUserLastname() {
        $token_param = $this->getAccessTokenParams();
        $token = $this->getAccessToken($token_param);
        $user_email = $token['user_name'];

        $qry = "SELECT last_name FROM users WHERE email='" . $user_email . "'";
        $qry_result = mysql_query($qry);
        
        $user = mysql_fetch_assoc($qry_result);
        return $user['last_name'];
    }
}
