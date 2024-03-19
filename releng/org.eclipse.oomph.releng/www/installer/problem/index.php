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

$version = htmlentities($_GET["version"]);
$version_label = "";
$version_parameter = "";
$installer_version = "";
$installer_short_desc = "Problem with Installer+";
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
    $installer_version = " Version+" . $match[1];
    if (count($match) == 4)
    {
      $installer_short_desc .= "Build " . $match[3];
    }
  }
}

$question = htmlEntities("../question/$version_parameter");
$body= "&body=" . htmlEntities(urlencode("I have first considered [asking a question](https://www.eclipse.org/setups/installer/question?version=" . urlencode($version). ").\n\n". 
  " I understand that without details about the problem and how to reproduce then problem, no one can fix a problem for me.  Therefore I have attached log details and screen captures below:\n```\nLog Details...\n```\n"));

$issue = htmlEntities("https://github.com/eclipse-oomph/oomph/issues/new?title=" . $installer_short_desc . $installer_version . $body);

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
 $Nav->addCustomNav("Report a Problem", $issue, "_blank", 1);
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
    Before <a href="$issue">reporting a problem</a>,
    please consider <a href="$question">asking a question</a> instead.
    Of course it's a good idea to read the <a href="https://www.eclipse.org/downloads/packages/installer" target="oomph_instructions">general instructions</a> for how to use the $eclipse_installer before doing either.
    </p>
    <p>
    Keep in mind that we cannot fix the network. 
    Networks and servers can and do fail and this is beyound our control.
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
    ob_start();
    $App->generatePage($theme, $Menu, $Nav, $pageAuthor, $pageKeywords, $pageTitle, $html, $Breadcrumb);
    $contents = ob_get_contents();
    ob_end_clean();
    $contents = preg_replace('#<a href="https://www.eclipse.org/donate/" class="btn btn-huge btn-info"><i class="fa fa-star"></i> Donate</a></div>#', '<a href="https://www.eclipse.org/sponsor/ide/?scope=Eclipse%20Installer" class="btn btn-huge btn-primary"><i class="fa fa-star"></i> Sponsor</a></div>', $contents);
    echo "$contents";

?>
