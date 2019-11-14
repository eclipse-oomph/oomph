<?php
// This PHP script reduces the project catalog report to a single project matching the id query parameter.

header('Content-type: application/xml');
ini_set('display_errors', 'On');
error_reporting(E_ALL | E_STRICT);

// Parse the report.
$xmlDoc = new DOMDocument();
$result = $xmlDoc->load("../marketplace-listings/marketplace.eclipse.org.setup");

if ($result === FALSE) {
  http_response_code(404);
} else {
  // The id to match.
  $id = $_GET["id"];
  $root = $xmlDoc->documentElement;

  // Loop over the children.
  $child = $root->firstChild;
  while ($child != null) {
    if ($child->nodeType == XML_ELEMENT_NODE) {
      $name = $child->getAttribute("name");
      $decodedName = urldecode($name);

      // Also look for the marketplaceID for this listing.
      $annotations = $child->getElementsByTagName("annotation");
      $details = $annotations[0]->getElementsByTagName("detail");
      $nodeID = null;
      foreach ($details as $detail) {
        if ($detail->getAttribute("key") == "marketplaceID") {
          $value = $detail->getElementsByTagName("value")[0];
          $nodeID = $detail->getElementsByTagName("value")[0]->nodeValue;
          break;
        }
      }

      // If the project name doesn't match and the its marketplaceID doesn't match, remove it and the following text node from the document.
      if ($id != $decodedName && $id != $nodeID) {
        $target = $child;
        $child = $child->nextSibling;
        $root->removeChild($target);
        $target = $child;
        $child = $child->nextSibling;
        $root->removeChild($target);
        continue;
      }
    }

    $child = $child->nextSibling;
  }

  echo $xmlDoc->saveXML();
}
?>