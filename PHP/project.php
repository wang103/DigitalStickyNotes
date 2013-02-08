<!DOCTYPE html>
<html>
    <head>
        <title>Digital Sticky Notes Project</title>
    </head>
    
    <body>
        
        <h1>
            This is the homepage for the Digital Sticky Notes project.
        </h1>

        <?php
        echo '
        <p>To request for consumer key and secret, please contact the project owner.</p>
        <form name="consumer_form" action="consumer.php" method="post">
        <p>
        <label>Code:</label> <input type="text" name="code" required>
        </p>
        <p>
        <label>Redirect URI:</label> <input type="text" name="uri" required>
        <p>
        <input type="submit" value="Submit">
        </p>
        </form>';
        ?>

    </body>

</html>
