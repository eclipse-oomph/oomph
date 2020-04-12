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
$Breadcrumb->addCrumb("Donate", ".", "_self");

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

.toggle {
}

.toggle-label {
  font-size: 150%;
}

.toggle + .toggle-label + .toggle-content {
  max-height: 0;
  opacity: 0;
  overflow: hidden;
  transition: all .4s ease-in-out;
}

.toggle + .toggle-label > .toggle-state-on {
  display: none;
}

.toggle + .toggle-label > .toggle-state-off {
  display: inherit;
}

.toggle:checked + .toggle-label > .toggle-state-on {
  display: inherit;
}

.toggle:checked + .toggle-label > .toggle-state-off {
  display: none;
}

.toggle:checked + .toggle-label + .toggle-content {
  max-height: none;
  opacity: 1;
}

.toggle-content {
  padding-left: 2em;
}

.toggle-label:hover, .toggle-label:hover * {
  cursor: pointer;
}

footer {
  display: none;
}

.featured-footer {
  display: none;
}

</style>

EOSTYLE;

$App->AddExtraHtmlHeader($style);

# Begin: page-specific settings.  Change these.
$pageTitle      = "Dontate";
$pageKeywords   = "Eclipse IDE";
$pageAuthor     = "Ed Merks";

$eclipse_installer = '<span style="color: #2c2255; font-family: Arial, Helvetica, sans-serif;">eclipse</span> <span class="orange">installer</span>';
$toggle_expand = '<span style="font-size: 125%;" class="orange toggle-state-on">&#x25E2;</span>';
$toggle_collapse = '<span style="font-size: 125%;" class="orange toggle-state-off">&#x25B7;</span>';
$read_more = '<span style="font-size: 50%;" class="orange toggle-state-off">&nbsp;&nbsp;read more...</span>';

$scope = $_GET["scope"];
if (!$scope)
{
  $scope = "Eclipse IDE";
}

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

$donate_parameter = "?scope=" . urlencode($scope);
$campaign = $_GET["campaign"];
if ($campaign)
{
  $donate_parameter .= htmlentities("&campaign=") . urlencode($campaign);
}
$donate_link = "https://www.eclipse.org/donate/ide/". $donate_parameter;

# Add page-specific Nav bars here
# Format is Link text, link URL (can be http://www.someothersite.com/), target (_self, _blank), level (1, 2 or 3)
$Nav->addNavSeparator("Invest Your Money", "");
$Nav->addCustomNav("Donate", "$donate_link", "_blank", 1);
$Nav->addCustomNav("Become a Member", "https://www.eclipse.org/membership/", "_blank", 1);
$Nav->addCustomNav("Join a Working Group", "https://www.eclipse.org/org/workinggroups/", "_blank", 1);
$Nav->addCustomNav("Start a Project", "https://www.eclipse.org/projects/handbook/#starting", "_blank", 1);
$Nav->addNavSeparator("Invest Your Time", "");
$Nav->addCustomNav("Get an Account", "https://accounts.eclipse.org/user/register", "_blank", 1);
$Nav->addCustomNav("Sign the Contributor Agreement", "https://accounts.eclipse.org/user/eca", "_blank", 1);
$Nav->addCustomNav("Configure Your Gerrit Account", "https://wiki.eclipse.org/Gerrit#User_Account", "_blank", 1);
$Nav->addCustomNav("Learn Bugzilla", "http://wiki.eclipse.org/Bug_Reporting_FAQ", "_blank", 1);
$Nav->addCustomNav("Contribute to the Platform", "https://www.eclipse.org/setups/installer/?url=https://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/interim/PlatformSDKConfiguration.setup&show=true", "_blank", 1);

$branding_image = "http://www.eclipse.org/downloads/images/committers.png";
if ($scope == "Eclipse IDE for C/C++ Developers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/cdt.png";
} 
else if ($scope == "Eclipse IDE for Java Developers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/java.png";
}
else if ($scope == "Eclipse IDE for Enterprise Java Developers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/javaee.png";
}
else if ($scope == "Eclipse IDE for Web and JavaScript Developers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/javascript.png";
}
else if ($scope == "Eclipse IDE for PHP Developers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/php.png";
}
else if ($scope == "Eclipse IDE for Java and DSL Developers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/dsl-package_42.png";
}
else if ($scope == "Eclipse Modeling Tools")
{
  $branding_image = "http://www.eclipse.org/downloads/images/modeling.png";
}
else if ($scope == "Eclipse IDE for RCP and RAP Developers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/rcp.png";
}
else if ($scope == "Eclipse IDE for Testers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/testing.png";
}
else if ($scope == "Eclipse IDE for Testers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/testing.png";
}
else if ($scope == "Eclipse IDE for Scientific Computing")
{
  $branding_image = "http://www.eclipse.org/downloads/images/parallel.png";
}
else if ($scope == "Eclipse IDE for Scout Developers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/scout.jpg";
}
else if ($scope == "Eclipse IDE for Rust Developers")
{
  $branding_image = "http://www.eclipse.org/downloads/images/corrosion.png";
}

$animation_style = "animation-name: wiggle; animation-duration: 3s; animation-iteration-count: infinite; animation-delay: 2s; animation-direction: alternate;";

$html = <<<EOHTML

<div id="maincontent">
  <div id="midcolumnwide">
    <div style="font-size: 150%;">
      <img style="width: 3ex; height: 3ex;" src="$branding_image"/>
      <span>$scope</span>
      <span style="font-size: 66%;">$version_label</span>
    </div>

    <br/>
    <p>
    The <span style="color: #2c2255; font-family: Arial, Helvetica, sans-serif;">$scope</span> is brought to you by the coordinated effort of Eclipse's diverse community using the infrastructure provided by the Eclipse Foundation.
    It's <b>100%</b> free and <b>open source</b>.
    Help keep it that way and make it better.
    </p>

    <hr>
    <h3><b>What Can <span class="orange">I</span> Do to Support the <span style="color: #2c2255;">Eclipse</span> <span class="orange">Community</span>?</b></h3>
    <a href="$donate_link" target="_blank">
    <img style="$animation_style" src="../installer/FriendsOfEclipse.png"/>
    </a>
    &nbsp;&nbsp;
    <a href="$donate_link" target="_blank" class="btn btn-huge btn-primary" style="$animation_style"><i class="fa fa-star"></i> Donate</a>
    <span style="font-size: 400%; color: #2c2255; margin-top: 0.5ex; margin-left: 0.5em; position: absolute; $animation_style">&dollar;</span>
    <span style="font-size: 400%; color: #2c2255; margin-top: 0.5ex; margin-left: 1.5em; position: absolute; $animation_style">&euro;</span>
    <span style="font-size: 400%; color: #2c2255; margin-top: 0.5ex; margin-left: 2.5em; position: absolute; $animation_style">&pound;</span>
    <span style="font-size: 400%; color: #2c2255; margin-top: 0.5ex; margin-left: 3.5em; position: absolute; $animation_style">&#20803;</span>

    <hr>
    <input type="checkbox" id="toggle-id-1" class="toggle"/>
    <label for="toggle-id-1" class="toggle-label">$toggle_expand$toggle_collapse Invest Your Money $read_more</label>
    <ul class="toggle-content">
      <li>
      If you love Eclipse as much as we do, please <a href="$donate_link" target="_blank">donate</a> a token of your appreciation.
      </li>
      <ul>
        <li>
        Eclipse is <b>100%</b> funded by the community and <b class="orange">you</b> as one of the millions of daily users are part of that community.
        </li>
        <li>
        All the money donated will go directly to funding Eclipse IDE development and infrastructure.
        </li>
      </ul>
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
    <input type="checkbox" id="toggle-id-2" class="toggle"/>
    <label for="toggle-id-2" class="toggle-label">$toggle_expand$toggle_collapse Invest Your Time $read_more</label>
    <ul class="toggle-content">
      <li>
      Help answer questions rather than just asking them.
      Share your knowledge, experience, and expertise.
      <ul>
        <li>
        If you don't already have an Eclipse Account, <a href="https://accounts.eclipse.org/user/register" target="_blank">register</a> now.
        </li>
        <li>
        The <a href="https://www.eclipse.org/forums/index.php/f/89/" target="_blank">Newcomer Forum</a> is a great place to help others.
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
          The <a href="https://wiki.eclipse.org/Eclipse_Installer" target="_blank">$eclipse_installer</a> makes this super easy.
          Many projects have Oomph project setups to automate the setup process.
          Use the $eclipse_installer's <span style="font-variant: small-caps;">Advanced Mode...</span> menu item to switch to advanced mode and choose the project on the second page of the wizard.
          </li>
          <li>
          Some projects make it even easier to get started by providing a setup configuration:
          <ul>
            <li>
            <a href="https://www.eclipse.org/setups/installer/?url=https://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/interim/PlatformSDKConfiguration.setup&show=true" target="_blank">Contribute to the Eclipse Platform</a>.
            </li>
            <li>
            <a href="https://www.eclipse.org/setups/installer/?url=https://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/configurations/OomphConfiguration.setup&show=true" target="_blank">Contribute to Oomph</a>.
            </li>
            <li>
            <a href="https://www.eclipse.org/setups/installer/?url=https://git.eclipse.org/c/emf/org.eclipse.emf.git/plain/releng/org.eclipse.emf.releng/EMFDevelopmentEnvironmentConfiguration.setup&show=true&show=true" target="_blank">Contribute to the Eclipse Modeling Framework</a>.
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
    ob_start();
    $App->generatePage($theme, $Menu, $Nav, $pageAuthor, $pageKeywords, $pageTitle, $html, $Breadcrumb);
    $contents = ob_get_contents();
    ob_end_clean();
    $contents = preg_replace('/(<a href="https:\/\/www.eclipse.org\/donate\/)(" class="btn btn-huge) btn-info(")(><i class="fa fa-star">)/', "\\1ide/$donate_parameter\\2 btn-primary\\3 target=\"_blank\"\\4", $contents);
    echo "$contents";

?>
