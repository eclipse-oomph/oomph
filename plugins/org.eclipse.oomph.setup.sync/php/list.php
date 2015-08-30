<?php
require("config.php");

echo "List:<ol>";

foreach (glob("$uploadFolder/{,.}*", GLOB_BRACE) as $file)
{
  if (is_file($file))
  {
    echo "<li>$file<br>\n";
  }
}

echo "</ol>";


?>
