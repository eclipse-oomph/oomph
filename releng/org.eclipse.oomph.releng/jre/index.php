<?php
header( 'Cache-control: no cache' );
error_reporting(0);
$host = $_SERVER['DOCUMENT_ROOT'];
require_once($host . "/eclipse.org-common/system/app.class.php");
require_once($host . "/eclipse.org-common/system/nav.class.php");
require_once($host . "/eclipse.org-common/system/menu.class.php");
$App = new App();
$Nav = new Nav();
$Menu = new Menu();
include($App->getProjectCommon());

#
# Begin: page-specific settings.  Change these.
$pageTitle      = "Java Missing";
$pageKeywords   = "Oomph Installer Extractor Java JRE JDK";
$pageAuthor     = "estepper";

# Add page-specific Nav bars here
# Format is Link text, link URL (can be http://www.someothersite.com/), target (_self, _blank), level (1, 2 or 3)
# $Nav->addNavSeparator("My Page Links",    "downloads.php");
# $Nav->addCustomNav("My Link", "mypage.php", "_self", 3);
# $Nav->addCustomNav("Google", "http://www.google.com/", "_blank", 3);

# End: page-specific settings
#

$pStart = "<p style='margin: 0ex 8em 0ex 4em;'>";
$addModulesProblem = "Manual addition of <a href='https://wiki.eclipse.org/Configure_Eclipse_for_Java_9'>--add-modules=ALL-SYSTEM</a> required for applications based on Eclipse Neon (4.7) or older.";
$bestChoice = "<span style='color: DarkOrange;'>&#x2605;</span> The <b>best default choice</b> that is most compatible with all current and older Eclipse products.";

$oracle = array(
  "https://www.oracle.com/technetwork/java/javase/downloads/jdk13-downloads-5672538.html", "", $pStart . $addModulesProblem . "</p>",
  "https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase12-5440181.html", "", $pStart . $addModulesProblem . " This version has reached <b>end of life</b>. Use JDK 13 instead.</p>",
  "https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html", "", $pStart . $addModulesProblem . "</p>",
  "https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase10-4425482.html", "",  $pStart . $addModulesProblem . "</p>",
  "https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase9-3934878.html", "",  $pStart . $addModulesProblem . "</p>",
  "http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html", "http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html", $pStart . $bestChoice . "</p>",
  "http://www.oracle.com/technetwork/java/javase/downloads/jre7-downloads-1880261.html", "http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html", "",
  "http://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase6-419409.html", "", "",
  "http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase5-419410.html", "", "",
  "http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase14-419411.html", "", "",
  "http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase13-419413.html", "", "",
  "http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase13-419413.html", "", "",
  "http://www.oracle.com/technetwork/java/javasebusiness/downloads/java-archive-downloads-javase11-419415.html", "", "",);

$productName = htmlentities($_GET["pn"]);
if (!$productName)
{
  $productName = "Eclipse";
}

$productURL = htmlentities($_GET["pu"]);
if (!preg_match("#^https?://[\w-]*\.eclipse\.org($|/.*$)#", $productURL))
{
  $productURL = "";
}

$productImageURL = htmlentities($_GET["pi"]);
if (!preg_match("#^https?://[\w-]*\.eclipse\.org($|/.*$)#", $productImageURL))
{
  $productImageURL = "http://www.eclipse.org/downloads/images/classic2.jpg";
}


$vm = htmlentities($_GET["vm"]);
$tokens = explode("_", $vm);

$windows = $tokens[0] == 1;
$macos = $tokens[0] == 2;
$linux = $tokens[0] == 3;

$agent = $_SERVER['HTTP_USER_AGENT'];
if (strpos($agent, "Win") !== FALSE)
{
  $default_os = "Windows";
}
else if (strpos($user_agent, "Mac") !== FALSE)
{
  $default_os = "Mac OSX";
}
else if (strpos($user_agent, "Linux") !== FALSE || strpos($user_agent, "X11") != FALSE)
{
  $default_os = "Linux";
}
else
{
  $default_os = "unknown";
}

$os = $windows ? "Windows" : ($macos ? "Mac OSX" : ($linux ? "Linux" : $default_os));

$major = intval($tokens[1]);
$minor = intval($tokens[2]);
$micro = intval($tokens[3]);

$bitness = intval($tokens[4]);

$jdk = $macos || $tokens[5] == "1"; // It appears that on MacOS a JDK is always required.
if ($jdk)
{
  $jre = "JDK";
  $jreInstruction = "Java Development Kit (JDK)";
}
else
{
  $jre = "JRE/JDK";
  $jreInstruction = "Java Runtime Environment (JRE) or a Java Development Kit (JDK)";
}


$lrningist = "\n";
for ($v= 13, $i = 0; $v >= $minor; $v--, $i++) {
  $index = 3 * $i + 1;
  if ($index >= count($oracle))
  {
    break;
  }

  $url = $oracle[$index];
  if ($url == "")
  {
    $url = $oracle[3 * $i];
  }

  if ($v <= 5)
  {
    $brandVersion = "1.$v";
  }
  else
  {
    $brandVersion = $v;
  }

  $indent = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  $downloadImage = "<img src=\"https://dev.eclipse.org/small_icons/actions/go-bottom.png\"/>&nbsp;&nbsp;";


  $info = $oracle[3 * $i + 2] . "\n";

  if (!$jdk)
  {
    if ($url == $oracle[3 * $i])
    {
      $list .= "      <h5>$indent$downloadImage<a href='$url'>Oracle JDK $brandVersion</a></h5>\n$info";
    }
    else
    {
      $list .= "      <h5>$indent$downloadImage<a href='$url'>Oracle JDK $brandVersion</a>$indent$downloadImage<a href=\"" . $oracle[3 * $i] . "\">Oracle JRE $brandVersion</a></h5>\n$info";
    }
  }
  else
  {
    $list .= "      <h5>$indent$downloadImage<a href='$url'>Oracle JDK $brandVersion</a></h5>\n$info";
  }
}

if ($major != 0 && $minor != 0)
{
  if ($productName == "Eclipse Installer")
  {
    $requirement = <<<EOHTML
    <p>Unfortunately the Java version needed to run $productName couldn't be found on your system.
    You need the following version or a higher version:</p>

    <h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Java $major.$minor.$micro ($bitness Bit)</h3>
    <br>
EOHTML;
  }
  else
  {
    $requirement = <<<EOHTML
    <p>The Java version needed to run $productName must be the following version or a higher version:</p>

    <h3>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Java $major.$minor.$micro ($bitness Bit)</h3>
    <br>
EOHTML;
  }
}
else
{
  $titleSuffix = "Downloads";
}

if ($windows && $productName == "Eclipse Installer")
{
  if ($major <= 1 && $minor <= 7)
  {
  $additionalInstructions = <<<EOHTML
    <p>
    On Windows, installing Java 11 or 12 <b>will not fix</b> your problem.
    The Oracle Java 11/12 installers do not register Java as the default JRE on the system path, nor place its entries in the expected location in the system registry.
    </p>
    <p>
    Please download the latest version of the installer:<br/>
      <a href="http://www.eclipse.org/downloads/download.php?file=/oomph/products/latest/eclipse-inst-win64.exe" style="margin-left: 2em; margin-top: 1ex; margin-bottom: 1ex; border-radius: 5px; font-weight: bold; border: 1px solid Chocolate;  background-color: DarkOrange; color: white; padding: 0.25ex 0.25em; text-align: center; text-decoration: none; display: inline-block;">Download 64 bit</a><br/>
    This version of the installer can install any version of Eclipse you may want and it recognizes the locations of Java 11 or higher installations.
    Keep in mind though that older versions of Eclipse will not function with more recent Java versions, i.e., with Java 9 or higher.
    </p>
    <p>
    Alternatively, even if you plan to use Java 11 or Java 12 in your development environment,
    <b>you should consider also installing Java 8</b> so that your system path has a JRE/JDK usable as the default for any current or older Eclipse version.
    Most likely you will want to install a <b>64 bit version</b>. Pay particular attention to that choice when installing a <span style='color: DarkOrange;'>&#x2605;</span> Java 8 JDK/JRE.
    <br/>
    &nbsp;
    </p>
EOHTML;
  }
}

$html = <<<EOHTML

<div id="maincontent">
  <div id="midcolumnwide">

    <h1>Java for $os $titleSuffix</h1>

    <br>
    <hr>
    <table>
      <tr>
        <td valign="center">
          <a href="$productURL"><img src="$productImageURL"/></a>
        </td>
        <td>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        </td>
        <td valign="center">
          <font size="+2">Thank you for using<br><a style="top-margin: 0.75ex;" href="$productURL">$productName</a></font>
        </td>
      </tr>
    </table>
    <hr>
    <br>

$requirement\n
    <p>Please download and install a $jreInstruction</p>
$additionalInstructions
$list<br>
    <!--
    <p>Once you've downloaded and installed the required $jre start Oomph again...</p>

    <p>You can benefit from committer knowledge and get training, consulting services, professional support and even production SLAs. Just <a href="mailto:support@levitasoft.com?subject=We%20Need%20Your%20Help">ask</a> us about it!</p>
    -->
  </div>
</div>
<div id="rightcolumn">
  $sidebar
</div>

EOHTML;


    # Generate the web page
    $App->generatePage($theme, $Menu, $Nav, $pageAuthor, $pageKeywords, $pageTitle, $html);
?>
