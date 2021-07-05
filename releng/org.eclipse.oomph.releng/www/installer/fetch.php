<?php
header( 'Cache-control: no cache' );
header('Content-type: application/xml');
$url = $_GET["url"];
if (preg_match("@^https?://[^/]+eclipse.org/@i", $url) == FALSE) {
  $path = str_replace("://", "/", $url);
  $zip = new ZipArchive;
  $res = $zip->open('../setups.zip');
  if ($res === TRUE) {
    $contents = $zip->getFromName($path);
    if ($contents == "") {
      $path = str_replace("http://", "https/", $url);
      $contents = $zip->getFromName($path);
    }
    $zip->close();
    echo $contents;
  } else {
    http_response_code(404);
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