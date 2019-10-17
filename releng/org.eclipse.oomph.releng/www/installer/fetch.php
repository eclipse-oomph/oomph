<?php
header( 'Cache-control: no cache' );
header('Content-type: application/xml');
$url = htmlentities($_GET["url"]);
$contents = file_get_contents($url);
echo $contents;
?>