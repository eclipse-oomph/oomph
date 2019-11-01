<?php
header( 'Cache-control: no cache' );
header('Content-type: application/xml');
$url = $_GET["url"];
$contents = @file_get_contents($url);
if ($contents === FALSE) {
http_response_code(404);
} else {
echo $contents;
}
?>