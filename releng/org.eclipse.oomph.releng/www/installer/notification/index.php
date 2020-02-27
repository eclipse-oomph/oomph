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
$Breadcrumb->addCrumb("Eclipse Installer Notification", ".", "_self");

$style = <<<EOSTYLE

<style>
@keyframes fa-spin2 {
  0% {transform:rotate(0deg)} to {transform:rotate(359deg)}
  25% {transform:rotate(0deg)} to {transform:rotate(359deg)}
  50% {transform:rotate(0deg)} to {transform:rotate(359deg)}
  75% {transform:rotate(0deg)} to {transform:rotate(359deg)}
  100% {transform:rotate(0deg)} to {transform:rotate(359deg)}
} 
@keyframes wiggle {
    0% { transform: rotate(0deg); }
   80% { transform: rotate(0deg); }
   85% { transform: rotate(5deg); }
   95% { transform: rotate(-5deg); }
  100% { transform: rotate(0deg); }
}
</style>

EOSTYLE;

$App->AddExtraHtmlHeader($style);

#
# Begin: page-specific settings.  Change these.
$pageTitle      = "Installer Notification";
$pageKeywords   = "Oomph Installer";
$pageAuthor     = "Ed Merks";

$eclipse_installer = '<span style="color: #2c2255; font-family: Arial, Helvetica, sans-serif;">eclipse</span> <span class="orange">installer</span>';
$eclipse_ide = '<span style="color: #2c2255; font-family: Arial, Helvetica, sans-serif;">eclipse</span> <span class="orange">IDE</span>';
$eclipse_marketplace = '<a style="font-family: Arial, Helvetica, sans-serif;" href="https://marketplace.eclipse.org/" target="eclipse_marketplace"><span style="color: #2c2255;">eclipse market</span><span class="orange">place</span></a>';

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

$question = htmlEntities("../question/$version_parameter");
$problem = htmlEntities("../problem/$version_parameter");

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
 $Nav->addCustomNav("Report a Problem", $problem, "_self", 1);
 $Nav->addCustomNav("<span class='fa fa-star'></span> Like", ".$version_parameter", "_self", 1);
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
    The $eclipse_installer is brought to you by a very small team of dedicated individuals.
    The $eclipse_ide packages that it installs are brought to you by the coordinated efforts of Eclipse's vast and diverse community.
    The Eclipse Community is dedicated to your success.
    If you have a question about the $eclipse_installer or a problem using the $eclipse_installer, 
    <a href="$question" target="_self">ask</a> it or <a href="$problem" target="_self">report</a> it.
    </p>
    <p>
    Eclipse technologies save you time and money, and of course time is money.
    Eclipse not only provides great tools along with great tools for delivering the great tools,
    Eclipse provides rich frameworks for building your own compelling applications.
    The $eclipse_marketplace has a wide variety of such applications that you can install in your $eclipse_ide to enhance your experience.
    </p>

    <hr>
    <h3>What Can I Do to Support the Eclipse Community?</h3>
    <a href="https://www.eclipse.org/donate/" target="_blank">
    <img style="animation-name: wiggle; animation-duration: 3s; animation-iteration-count: infinite; animation-delay: 2s; animation-direction: alternate;" src="../FriendsOfEclipse.png"/>
    </a>

    <hr>
    <h4><b>Invest Your Money</b></h4>
    <ul>
      <li>
      If you love Eclipse as much as we do, 
      join the elite club and become a <a href="https://www.eclipse.org/donate/donorlist.php?start=0#all" target="_blank">Friend of Eclipse</a> by <a href="https://www.eclipse.org/donate/" target="_blank">donating</a> a token of your appreciation.
      </li>
      <li>
      If you work for a company, encourge your employeer to become an <a href="https://www.eclipse.org/membership/" target="_blank">Eclipse Member</a>.
      </li>
      <li>
      If you want influence, join an <a href="https://www.eclipse.org/org/workinggroups/" target="_blank">Eclipse Working Group</a>.
      </li>
      <li>
      If you've built cool technology, <a href="https://www.eclipse.org/projects/handbook/#starting" target="_blank">start your own Eclipse Project</a>.
      </li>
    </ul>

    <hr>
    <h4><b>Invest Your Time</b></h4>
    <ul>
      <li>
      Help answer questions rather than just asking them.
      Share your knowledge, experience, and expertise.
      <ul>
        <li>
        If you don't already have an Eclipse Account, <a href="https://accounts.eclipse.org/user/register" target="_blank">register</a> now.
        </li>
        <li>
        The <a href="https://www.eclipse.org/forums/index.php/f/89/" target="_blank">Newcomer Forum</a> is a greate place to help others.
        </li>
      </ul>
      </li>
      <li>
      Help fix bugs and help implement enhancements rather than just reporting problems and requesting features.
      Not only will you personally benefit, giving back to the commons makes everything better for everyone.
      <ul>
        <li>
        If you don't already have an Eclipse Account, <a href="https://accounts.eclipse.org/user/register" target="_blank">register</a> now.
        </li>
        <li>
        If you haven't already signed your Eclipse Contributor Agreement, <a href="https://accounts.eclipse.org/user/eca" target="_blank">sign</a> now.
        </li>
        <li>
        If you haven't already set up your Eclipse Gerrit Account, <a href="https://wiki.eclipse.org/Gerrit#User_Account" target="_blank">set it up</a> now.
        </li>
        <li>
        If you're not familiar with Bugzilla, 
        learn <a href="http://wiki.eclipse.org/Bug_Reporting_FAQ" target="_blank">how to use Bugzilla</a> 
        and learn how Buzilla fits into the <a href="http://wiki.eclipse.org/Development_Resources/HOWTO/Bugzilla_Use" target="_blank">development process</a>.
        </li>
        <li>
        If you don't have a particular problem or enhancment in mind, 
        look at the <a href="https://bugs.eclipse.org/bugs/buglist.cgi?bug_status=NEW&bug_status=REOPENED&keywords=helpwanted" target="_blank">Bugzilla reports for which help is wanted</a>.
        </li>
        <li>
        You'll need to set up a development environment with the workspace provisioned to include the Git repositories of the projects to which you wish to contribute.
        <ul>
          <li>
          The $eclipse_installer makes this super easy.
          Many projects have Oomph project setups to automate the setup process.
          Use the $eclipse_installer's <span style="font-variant: small-caps;">Advanced Mode...</span> menu item to switch to advanced mode and choose the project on the second page of the wizard.
          </li>
          <li>
          Some projects make it even easier to get started by providing a setup configuration:
          <ul>
            <li>
            <a href="https://www.eclipse.org/setups/installer/?url=https://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/configurations/OomphConfiguration.setup&show=true" target="_blank">Contribute to Oomph</a>.
            </li>
            <li>
            <a href="https://www.eclipse.org/setups/installer/?url=https://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/interim/PlatformSDKConfiguration.setup&show=true" target="_blank">Contribute to the Eclipse Platform</a>.
            </li>
          </ul>
          </li>
        </ul>
        </li>
      </ul>
      </li>
    </ul>
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
