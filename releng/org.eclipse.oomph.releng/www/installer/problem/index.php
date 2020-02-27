<?php
// header( 'Cache-control: no cache' );
error_reporting(0);
$host = $_SERVER['DOCUMENT_ROOT'];
require_once($host . "/eclipse.org-common/system/app.class.php");
require_once($host . "/eclipse.org-common/system/nav.class.php");
require_once($host . "/eclipse.org-common/system/menu.class.php");
require_once($host . "/eclipse.org-common/system/breadcrumbs.class.php");
$App = new App();
$Nav = new Nav();
$Menu = new Menu();
$Breadcrumb = new Breadcrumb();
include($App->getProjectCommon());

// Shorten the default breadcrumbs to remove the setups folder for which there is no HTML.
$Breadcrumb->removeCrumb($Breadcrumb->getCrumbCount() - 1);
$Breadcrumb->addCrumb("Oomph", "http://www.eclipse.org/oomph", "_self");
$Breadcrumb->addCrumb("Eclipse Installer Problems", ".", "_self");

#
# Begin: page-specific settings.  Change these.
$pageTitle      = "Installer Problems";
$pageKeywords   = "Oomph Installer";
$pageAuthor     = "Ed Merks";


$eclipse_installer = '<span style="color: #2c2255; font-family: Arial, Helvetica, sans-serif;">eclipse</span> <span class="orange">installer</span>';

$version = $_GET["version"];
$version_label = "";
$version_parameter = "";
$bugzilla_version = "";
$bugzilla_short_desc = "&short_desc=Installer";
if (!$version)
{
  $version = "Self Hosting";
}
else
{
  $version_label = "&nbsp;Version: " . $version;
  $version_parameter = "?version=" . $version;
  if (preg_match('/([0-9.]+)( Build ([0-9]+))?/', $version, $match))
  {
    $bugzilla_version = "&version=" . $match[1];
    if (count($match) == 4)
    {
      $bugzilla_short_desc .= " Build " . $match[3];
    }
  }
}

$bugzilla = htmlEntities("https://bugs.eclipse.org/bugs/enter_bug.cgi?product=Oomph&component=Setup" . $bugzilla_version . $bugzilla_short_desc);
$question = htmlEntities("../question/$version_parameter");

# Add page-specific Nav bars here
# Format is Link text, link URL (can be http://www.someothersite.com/), target (_self, _blank), level (1, 2 or 3)
# $Nav->addNavSeparator("My Page Links",    "downloads.php");
# $Nav->addCustomNav("My Link", "mypage.php", "_self", 3);
# $Nav->addCustomNav("Google", "http://www.google.com/", "_blank", 3);

 $Nav->addNavSeparator("Documentation", "");
 $Nav->addCustomNav("Eclipse Oomph Wiki", "https://wiki.eclipse.org/Oomph", "_blank", 1);
 $Nav->addCustomNav("Eclipse Installer", "https://wiki.eclipse.org/Eclipse_Installer", "_blank", 1);
 $Nav->addNavSeparator("Community", "");
 $Nav->addCustomNav("Get an Eclipse Account", "https://accounts.eclipse.org/user/register", "_blank", 1);
 $Nav->addCustomNav("Ask a Question", $question, "_self", 1);
 $Nav->addCustomNav("Report a Problem", $bugzilla, "_blank", 1);
 $Nav->addCustomNav("<span class='fa fa-star'></span> Like", "../notification/$version_parameter", "_self", 1);
 $Nav->addNavSeparator("Download", "");
 $Nav->addCustomNav("Eclipse Installers", "https://wiki.eclipse.org/Eclipse_Installer", "_blank", 1);
 $Nav->addCustomNav("Eclipse Packages", "https://www.eclipse.org/downloads/packages/", "_blank", 1);

$html = <<<EOHTML

<div id="maincontent">
  <div id="midcolumnwide">
                <div style="font-size: 150%;">
                <a href="https://wiki.eclipse.org/Oomph" target="oomph_wiki"><img style="width: 3ex; height: 3ex;" src="../oomph256.png"/></a>
                <img style="max-width: 12em;" src="../EclipseInstaller.png"/>
                <span style="font-size: 66%;">$version_label</span>
                </div>

    <br/>
    <p>
    Before <a href="$bugzilla">reporting a problem</a>,
    please consider <a href="$question">asking a question</a> instead.
    Of course it's a good idea to read the <a href="https://www.eclipse.org/downloads/packages/installer" target="oomph_instructions">general instructions</a> for how to use the $eclipse_installer before doing either.
    </p>
    <p>
    Keep in mind that we cannot fix the network. 
    Networks and servers can and do fail and this is beyound our control.
    </p>
    <p>
    You will need an <b><a href="https://accounts.eclipse.org/user/register" target="_blank">Eclipse Account</a></b> to report problems via <a href="$bugzilla" target="_blank">Bugzilla</a>.
    </p>
    
    <p>
    Please provide as much detail as possible.
    I.e., include screen captures and/or attach log details, as well as information about your operating system version.
    </p>

    <br>
  </div>
</div>
<div id="rightcolumn">
  $sidebar
</div>

EOHTML;

    # Generate the web page
    $App->generatePage($theme, $Menu, $Nav, $pageAuthor, $pageKeywords, $pageTitle, $html, $Breadcrumb);

?>
