<?php
header( 'Cache-control: no cache' );
header('Content-type: application/xml');
$url = htmlentities($_GET["url"]);
$contents = @file_get_contents($url);
if ($url === FALSE) {
http_response_code(404);
} else {
echo $contents;
}
?>