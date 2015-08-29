<?php

// HTTP Status Codes
define('UNAUTHORIZED', '401');
define('FORBIDDEN', '403');
define('BAD_REQUEST', '400');
define('NOT_MODIFIED', '304');
define('CONFLICT', '409');
define('INTERNAL_SERVER_ERROR', '500');

if (!isset($_SERVER['PHP_AUTH_USER'])
    || $_SERVER['PHP_AUTH_USER'] == "")
    error(UNAUTHORIZED);

$headers = getallheaders();
if (!isset($headers['User-Agent'])
    || $headers['User-Agent'] != "oomph/sync")
    error(FORBIDDEN);

$userID = $_SERVER['PHP_AUTH_USER'];
$userXML = "C:/develop/bin/httpd/upload/$userID.xml";
$userXMLTime = file_exists($userXML) ? millis($userXML) : 0;

$timeStamp = !isset($headers['X-Last-Modified']) ? 0
    : htmlspecialchars($headers['X-Last-Modified']);

$method = $_SERVER['REQUEST_METHOD'];
if ($method == "GET")
{
  if ($userXMLTime == 0)
  {
    touch($userXML);
    clearstatcache(true, $userXML);
    $userXMLTime = millis($userXML);
  }

  header("X-Last-Modified: $userXMLTime", true);

  if ($timeStamp != $userXMLTime)
  {
    header("X-Last-Modified: $userXMLTime", true);
    readfile($userXML);
  }
  else
  {
    status(NOT_MODIFIED);
  }

  exit;
}

if ($method == "POST")
{
/*
  if (!isset($headers['Content-Type']))
    error(BAD_REQUEST, "No content type");

  if ($headers['Content-Type'] != "text/xml")
    error(BAD_REQUEST, "Wrong content type");
*/

  if (!isset($_FILES['userfile']['tmp_name'])
      || $_FILES['userfile']['tmp_name'] == "")
      error(BAD_REQUEST, "No content");

  $file = $_FILES['userfile']['tmp_name'];

  if ($timeStamp != $userXMLTime)
  {
    header("X-Last-Modified: $userXMLTime", true);
    readfile($userXML);
    status(CONFLICT);
  }
  else
  {
    if(!is_uploaded_file($file)) error(INTERNAL_SERVER_ERROR, "Not uploaded");
    move_uploaded_file($file, $userXML);

    clearstatcache(true, $userXML);
    $userXMLTime = millis($userXML);
    header("X-Last-Modified: $userXMLTime", true);
  }

  exit;
}

error(BAD_REQUEST, "Wrong method");

function status($code, $cause = NULL)
{
  if (function_exists('http_response_code'))
  {
    http_response_code($code);
  }
  else
  {
    header("X-Status-Code: $code", true, $code);
  }

  if ($cause != NULL)
  {
    header("X-Status-Cause: $cause");
  }
}

function error($code, $cause = "")
{
  status($code);
  exit;
}

function millis($file)
{
  return filemtime($file) * 1000;
}

?>
