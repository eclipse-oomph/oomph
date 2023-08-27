<?php
    header("Status: 301 Moved Permanently");
     header("Location:../sponsor/index.php" . ($_GET ? "?" . $_SERVER['QUERY_STRING'] : ""));
    exit;
?>