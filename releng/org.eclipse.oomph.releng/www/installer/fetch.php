<?php
header( 'Cache-control: no cache' );
header('Content-type: application/xml');
$url = htmlentities($_GET["url"]);
$path = str_replace("://", "/", $url);
$contents = @file_get_contents("../setups/$path");
if ($contents == "") {
  $path = str_replace("http://", "https/", $url);
  $contents = @file_get_contents("../setups/$path");
}

if ($contents == "") {
  $contents = @file_get_contents($url);
}

if ($contents == "" || $contents === FALSE) {
  http_response_code(404);
} else {
  echo $contents;
}
?>
