<?php
define('UNAUTHORIZED', '401');
define('BAD_REQUEST', '400');
define('NOT_MODIFIED', '304');
define('CONFLICT', '409');
define('INTERNAL_SERVER_ERROR', '500');
define('LAST_MODIFIED', 'X-Last-Modified');

if (!isset($_SERVER['PHP_AUTH_USER']) || $_SERVER['PHP_AUTH_USER'] == "") error(UNAUTHORIZED);

$userID = $_SERVER['PHP_AUTH_USER'];
$userXML = "C:/develop/bin/httpd/upload/$userID.xml";
$userXMLTime = file_exists($userXML) ? millis($userXML) : 0;

$method = $_SERVER['REQUEST_METHOD'];
$headers = getallheaders();
$timeStamp = !isset($headers[LAST_MODIFIED]) ? 0 : htmlspecialchars($headers[LAST_MODIFIED]);

if ($method == "GET")
{
  if ($userXMLTime == 0)
  {
    touch($userXML);
    clearstatcache(true, $userXML);
    $userXMLTime = millis($userXML);
  }

  header(LAST_MODIFIED . ": $userXMLTime", true);

  if ($timeStamp != $userXMLTime)
  {
    readfile($userXML);
  }
  else
  {
    http_response_code(NOT_MODIFIED);
  }

  exit;
}

if ($method == "POST")
{
  if (!isset($_FILES['userfile']['tmp_name']) || $_FILES['userfile']['tmp_name'] == "") error(BAD_REQUEST);
  $file = $_FILES['userfile']['tmp_name'];

  if ($timeStamp != $userXMLTime)
  {
    header(LAST_MODIFIED . ": $userXMLTime", true);
    readfile($userXML);
    http_response_code(CONFLICT);
  }
  else
  {
    if(!is_uploaded_file($file)) error(INTERNAL_SERVER_ERROR);
    move_uploaded_file($file, $userXML);

    clearstatcache(true, $userXML);
    $userXMLTime = millis($userXML);
    header(LAST_MODIFIED . ": $userXMLTime", true);
  }

  exit;
}

error(BAD_REQUEST);
function error($status) { http_response_code($status); exit; }
function millis($file) { return filemtime($file) * 1000; }
?>
