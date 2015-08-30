<?php
require("config.php");

echo "Delete:<ol>";

foreach (glob("$uploadFolder/{,.}*", GLOB_BRACE) as $file)
{
  if (is_file($file))
  {
    echo "<li>$file<br>\n";
    unlink($file);
  }
}

echo "</ol>";

?>
