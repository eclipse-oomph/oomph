<?php
require("config.php");

// HTTP Status Codes
define('UNAUTHORIZED', '401');
define('FORBIDDEN', '403');
define('BAD_REQUEST', '400');
define('NOT_MODIFIED', '304');
define('NOT_FOUND', '404');
define('CONFLICT', '409');
define('INTERNAL_SERVER_ERROR', '500');

if (!isset($_SERVER['PHP_AUTH_USER']) || $_SERVER['PHP_AUTH_USER'] == "")
{
  error(UNAUTHORIZED);
}

$headers = getallheaders();
if (!isset($headers['User-Agent']) || $headers['User-Agent'] != "oomph/sync")
{
  error(FORBIDDEN);
}

$method = $_SERVER['REQUEST_METHOD'];
$userID = $_SERVER['PHP_AUTH_USER'];
$dataFile = "$uploadFolder/$userID.$fileExtension";
$versionFile = "$dataFile,v";

$baseVersion = !isset($headers['X-Base-Version']) ? "" : htmlspecialchars($headers['X-Base-Version']);

// ==================================================== //

if ($method == "GET")
{
  if (!file_exists($dataFile))
  {
    error(NOT_FOUND);
  }

  $currentVersion = file_get_contents($versionFile);
  if ($currentVersion != $baseVersion)
  {
    readfile($dataFile);
  }
  else
  {
    status(NOT_MODIFIED);
  }

  exit;
}

// ==================================================== //

if ($method == "POST")
{
  if (!isset($headers['X-Version']) || $headers['X-Version'] == "")
  {
    error(BAD_REQUEST, "No version");
  }

  if (!isset($_FILES['userfile']['tmp_name']) || $_FILES['userfile']['tmp_name'] == "")
  {
    error(BAD_REQUEST, "No content");
  }

  $newVersion = htmlspecialchars($headers['X-Version']);
  $currentVersion = !file_exists($versionFile) ? "" : file_get_contents($versionFile);

  if ($currentVersion == "" || $currentVersion == $baseVersion)
  {
    $tempFile = $_FILES['userfile']['tmp_name'];
    if(!is_uploaded_file($tempFile))
    {
      error(INTERNAL_SERVER_ERROR, "Not uploaded");
    }

    move_uploaded_file($tempFile, $dataFile);
    file_put_contents($versionFile, $newVersion);
  }
  else
  {
    status(CONFLICT);
    readfile($dataFile);
  }

  exit;
}

// ==================================================== //

if ($method == "DELETE")
{
  if (file_exists($versionFile))
  {
    unlink($versionFile);
  }

  if (!file_exists($dataFile))
  {
    error(NOT_FOUND);
  }

  unlink($dataFile);
  exit;
}

// ==================================================== //

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

?>
