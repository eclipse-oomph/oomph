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
$Breadcrumb->addCrumb("Eclipse Installer Questions", ".", "_self");

#
# Begin: page-specific settings.  Change these.
$pageTitle      = "Installer Questions";
$pageKeywords   = "Oomph Installer";
$pageAuthor     = "Ed Merks";

$eclipse_installer = '<span style="color: #2c2255; font-family: Arial, Helvetica, sans-serif;">eclipse</span> <span class="orange">installer</span>';

$version = $_GET["version"];
$version_label = "";
$version_parameter = "";
if (!$version)
{
  $version = "Self Hosting";
}
else
{
  $version_label = "&nbsp;Version: " . $version;
  $version_parameter = "?version=" . $version;
}

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
 $Nav->addCustomNav("Ask a Question", "https://github.com/eclipse-oomph/oomph/discussions", "_self", 1);
 $Nav->addCustomNav("Report a Problem", "../problem/$version_parameter", "_self", 1);
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
    Before asking a question on the <a href="https://github.com/eclipse-oomph/oomph/discussions/">Oomph Discussions</a>,
    please read the <a href="https://www.eclipse.org/downloads/packages/installer">general instructions</a> for how to use the $eclipse_installer.
    While it's true that there's <a href="https://en.wikipedia.org/wiki/No_such_thing_as_a_stupid_question">no such thing as a stupid question</a>,
    good answers nevertheless depend on the <a href="https://stackoverflow.com/help/how-to-ask">quality of the question</a>.
    Help us help you by providing details.
    </p>
    <p>
    You will need an <b><a href="https://accounts.eclipse.org/user/register" target="_blank">Eclipse Account</a></b> to post to the forum.
    </p>

    <hr>
    <h3>Where Should I Install?</h3>
    <p>
    Eclipse installations are generally designed for personal use and are self-updating.
    As such, you should typically install to a location for which you personally have write access.
    This is why the default location for the installation is in your home folder.
    </p>
    <p>
    While it is possible to use the $eclipse_installer to create a so-called shared, read-only installation,
    you should read carefully the additional considerations below.
    In particular, it's important to either disable the use of the shared bundle pool,
    or to ensure that the shared bundle pool is also located in a read-only location that is accessible to all users.
    </p>

    <hr>
    <h3>How is my Network Used?</h3>
    <p>
    If you are behind a network proxy, you will likely need to configure your <a href="https://help.eclipse.org/2019-09/index.jsp?topic=%2Forg.eclipse.platform.doc.user%2Freference%2Fref-net-preferences.htm">network connections</a> first.
    The $eclipse_installer will automatically open a dialog for these settings if the catalog fails to load.
    </p>
    <p>
    The $eclipse_installer relies heavily on the network to load the latest catalog
    and to fetch information and artifacts from various p2 update sites.
    Unfortunately many things can go wrong with the network between you and the servers providing this data.
    It is not entirely a rare occurrence that the download.eclipse.org server itself becomes over loaded.
    To avoid this, installation artifacts will typically be downloaded from so-called mirror sites; unfortunately the servers for these sites too can fail for various reasons.
    If you experience a network failure, please try again a short while later.
    </p>

    <hr>
    <h3>What Else Should I Consider?</h3>
    <p>
    By default the $eclipse_installer uses a shared bundle pool to reuse installation artifacts across multiple installations.
    This has the advantage of making each individual installation very small so that you can easily and quickly create many specialized installations for different use cases with minimal network traffic and minimal disk footprint.
    </p>
    <p>
    Use the $eclipse_installer's <span style="font-variant: small-caps;">Bundle Pools...</span> menu item to manage where this pool is physically located on disk; this location can become large.
    The <span style="font-variant: small-caps;">Bundle Pool Management</span> dialog supports garbage collecting of unused artifacts,
    i.e., if you delete an installation, the artifacts it uses can potentially be removed from disk.
    In addition, if some artifact becomes corrupted, or if a corrupted artifact is downloaded from some mirror site, this dialog can be used to detect the damage and to repair it.
    </p>

    <hr>
    <h3>Can't I Just a Download a Package?</h3>
    <p>
    If the $eclipse_installer fails to serve its purpose, you can always <a href="https://www.eclipse.org/downloads/packages/">download a pre-packaged installation</a> as an alternative.
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
