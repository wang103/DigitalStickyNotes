<?php

require "lib/server/MySQLOAuth2.inc";

$oauth = new MySQLOAuth2();

if ($_POST) {
    $oauth->finishClientAuthorization($_POST["accept"] == "Yes", $_POST);
}

$auth_params = $oauth->getAuthorizeParams();

?>
<html>
  <head>Authorize The Application</head>
  <body>
    <form method="post" action="authorize.php">
      <?php foreach ($auth_params as $k => $v) { ?>
      <input type="hidden" name="<?php echo $k ?>" value="<?php echo $v ?>" />
      <?php } ?>
      Do you authorize the app to access your data for DigitalStickyNotes?
      <p>
        <input type="submit" name="accept" value="Yes" />
        <input type="submit" name="accept" value="No" />
      </p>
    </form>
  </body>
</html>
