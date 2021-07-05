<?php
header( 'Cache-control: no cache' );
header('Content-type: application/xml');
$url = $_GET["url"];
if (preg_match("@^https?://[^/]+eclipse.org/@i", $url) == FALSE) {
  $path = str_replace("://", "/", $url);
  $contents = @file_get_contents("../setups/$path");
  if ($contents == "") {
    $path = str_replace("http://", "https/", $url);
    $contents = @file_get_contents("../setups/$path");
  }
  if ($contents == "") {
      http_response_code(404);
  } else {
      echo $contents;
   }
} else {
  $contents = @file_get_contents($url);
  if ($contents === FALSE) {
    http_response_code(404);
  } else {
   echo $contents;
  }
}
?>
