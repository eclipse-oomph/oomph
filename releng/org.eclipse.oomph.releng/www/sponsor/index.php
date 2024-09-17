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
$Breadcrumb->addCrumb("Sponsor", ".", "_self");

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
  display: none;
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

.img-border {
  border-radius: 3px;
  border: 3px solid Gainsboro;
}

</style>

EOSTYLE;

$App->AddExtraHtmlHeader($style);

# Begin: page-specific settings.  Change these.
$pageTitle      = "Sponsor";
$pageKeywords   = "Eclipse IDE";
$pageAuthor     = "Ed Merks";

$eclipse_installer = '<span style="font-family: Arial, Helvetica, sans-serif;"><span style="color: #2c2255;">eclipse</span> <span class="orange">installer</span></span>';
$toggle_expand = '<span style="font-size: 125%;" class="orange toggle-state-on">&#x25E2;</span>';
$toggle_collapse = '<span style="font-size: 125%;" class="orange toggle-state-off">&#x25B7;</span>';
$read_more = '<span style="font-size: 50%;" class="orange toggle-state-off">&nbsp;&nbsp;read more...</span>';

$scope = htmlentities($_GET["scope"]);
if (!$scope)
{
  $scope = "Eclipse IDE";
}
else
{
  $scope = preg_replace('/%2B/', '+', $scope);
  $scope = str_replace(' (includes Incubating components)', '', $scope);
}

$display_java_21 = "block";

$version = htmlentities($_GET["version"]);
$version_label = "";
$version_parameter = "";
if (!$version)
{
  $version = "Self Hosting";
}
else
{
  if (substr( $version, 0, 4) === "4.33")
  {
    $display_java_21 = "none";
  }

  $version_label = " Version:&nbsp;" . $version;
  $version_parameter = "?version=" . $version;
}


$sponsor_parameter = "?scope=" . urlencode($scope);
$campaign = htmlentities($_GET["campaign"]);
if ($campaign)
{
  $sponsor_parameter .= htmlentities("&campaign=") . urlencode($campaign);
}
$sponsor_link = "https://www.eclipse.org/sponsor/ide/". $sponsor_parameter;

# Add page-specific Nav bars here
# Format is Link text, link URL (can be http://www.someothersite.com/), target (_self, _blank), level (1, 2 or 3)
$Nav->addNavSeparator("Invest Your Money", "");
$Nav->addCustomNav("Sponsor", "$sponsor_link", "_blank", 1);
$Nav->addCustomNav("Become a Member", "https://www.eclipse.org/membership/", "_blank", 1);
$Nav->addCustomNav("Join the Working Group", "https://eclipseide.org/working-group/", "_blank", 1);
$Nav->addCustomNav("Start a Project", "https://www.eclipse.org/projects/handbook/#starting", "_blank", 1);
$Nav->addNavSeparator("Invest Your Time", "");
$Nav->addCustomNav("Get an Account", "https://accounts.eclipse.org/user/register", "_blank", 1);
$Nav->addCustomNav("Sign the Contributor Agreement", "https://accounts.eclipse.org/user/eca", "_blank", 1);
$Nav->addCustomNav("Contribute to the Platform", "https://www.eclipse.org/setups/installer/?url=https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.releng.aggregator/master/oomph/PlatformSDKConfiguration.setup&show=true", "_blank", 1);

$branding_image = "https://www.eclipse.org/downloads/images/committers.png";
if ($scope == "Eclipse IDE for C/C++ Developers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/cdt.png";
} 
else if ($scope == "Eclipse IDE for Embedded C/C++ Developers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/cdt.png";
}
else if ($scope == "Eclipse IDE for Java Developers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/java.png";
}
else if ($scope == "Eclipse IDE for Enterprise Java and Web Developers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/javaee.png";
}
else if ($scope == "Eclipse IDE for Web and JavaScript Developers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/javascript.png";
}
else if ($scope == "Eclipse IDE for PHP Developers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/php.png";
}
else if ($scope == "Eclipse IDE for Java and DSL Developers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/dsl-package_42.png";
}
else if ($scope == "Eclipse Modeling Tools")
{
  $branding_image = "https://www.eclipse.org/downloads/images/modeling.png";
}
else if ($scope == "Eclipse IDE for RCP and RAP Developers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/rcp.png";
}
else if ($scope == "Eclipse IDE for Testers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/testing.png";
}
else if ($scope == "Eclipse IDE for Scientific Computing")
{
  $branding_image = "https://www.eclipse.org/downloads/images/parallel.png";
}
else if ($scope == "Eclipse IDE for Scout Developers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/scout.jpg";
}
else if ($scope == "Eclipse IDE for Rust Developers")
{
  $branding_image = "https://www.eclipse.org/downloads/images/corrosion.png";
}

$animation_style = "animation-name: wiggle; animation-duration: 3s; animation-iteration-count: infinite; animation-delay: 2s; animation-direction: alternate;";

$html = <<<EOHTML

<div id="maincontent">
  <div id="midcolumnwide">
    <div style="font-size: 150%;">
      <img style="width: 3ex; height: 3ex;" src="$branding_image"/>
      <span>$scope</span>
      <span style="font-size: 66%; white-space: nowrap;">$version_label</span>
    </div>

    <br/>
    <p>
    The <span style="color: #2c2255; font-family: Arial, Helvetica, sans-serif;">$scope</span> is brought to you by the coordinated effort of Eclipse's diverse community using the infrastructure provided by the Eclipse Foundation.
    It's <b>100%</b> free and <b>open source</b>.
    Help keep it that way and make it better.
    </p>
    <hr>

	<!--
    <div id="java-21" style="display: $display_java_21;">
    <h2>
	<span class="orange" style="font-size: 250%; margin-right: 0.5em; margin-top: 0.5ex; position: absolute; $animation_style">&#x26A0;</span>
    <span style="margin-left: 2.5em;">
	Is your IDE currently running with Java 17?
	</span>
	<br/>
    <span style="margin-left: 2.5em;">
	To prevent problems, <b style="text-shadow: 2px 2px Orange;">please read</b> the following carefully.
	</span>
    </h2>
    <br/>
    <input type="checkbox" id="toggle-id-x" class="toggle" checked/>
    <label for="toggle-id-x" class="toggle-label">$toggle_expand$toggle_collapse What Will Happen? $read_more</label>
    <div class="toggle-content">
    <p>
    The June 12th release of the Eclipse IDE 2024-06 - 4.32 <b>requires Java 21</b>.
    </p>
    <p>
    Depending on how you installed your Eclipse IDE, the following might happen when you update:
    </p>
    <ul>
    <li>
    <div style="margin-bottom: 1ex;">
    The IDE will automatically install Java 21 as part of installing the available updates.
    If the selected update site below is present in the preferences, you will be automatically updated to <a href="https://eclipse.dev/justj/"><img src="https://eclipse.dev/justj/justj_title.svg" style="width: 3em;"/></a> Java 21.
    </div>
    <img src="JustJEPPLatestPresent.png" style="width: 40em;" class="img-border"/>
    </li>
    <li style="margin-top: 1ex;">
    <div style="margin-bottom: 1ex;">
    The IDE will refuse to update all or parts of the available updates and the <code>Next&ThinSpace;&gt;</code> button will not function properly:
    </div>
    <img src="CannotUpdate.png" style="width: 40em;" class="img-border"/>
    </li>
    <li style="margin-top: 1ex;">
    <div style="margin-bottom: 1ex;">
    The IDE will install all available updates but then fail to start:
    </div>
    <img src="StartFails.png" class="img-border" style="width: 30em;"/>
    </li>
    <li style="margin-top: 1ex;">
    <div style="margin-bottom: 1ex;">
    The IDE will install all available updates but will start in an impaired state, e.g., without branding information.
    </div>
    <img src="IDEImpaired.png" style="width: 30em;" class="img-border"/>
    </li>
    </ul>
    </div>
    <hr>
    <input type="checkbox" id="toggle-id-how" class="toggle" checked/>
    <label for="toggle-id-how" class="toggle-label">$toggle_expand$toggle_collapse What to Do? $read_more</label>
    <div class="toggle-content">
    <p>
    To prevent or fix problems, follow these steps:
    </p>
    <ul>
    <li>
    <div style="margin-bottom: 1ex;">
    If your IDE's installation has a JustJ JRE in the <code>eclipse.ini</code> as follows, it will probably update to Java 21:
    </div>
    <pre style="font-size: 87%; margin-left: 0.5em;" class="img-border">-vm
&lt;home&gt;/.p2/pool/plugins/org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_17.0.11.v20240426-1830/jre/bin</pre>
    <div style="margin-bottom: 1ex;">
    To be 100% sure it will update, ensure that <a href="https://download.eclipse.org/justj/epp/release/latest">https://download.eclipse.org/justj/epp/release/latest</a> 
    is present in the preferences; add it if it is not:
    </div>
    <img src="AddJustJEPPLatest.png" style="width: 40em; margin: 1ex;" class="img-border"/>
    <div style="margin-bottom: 1ex;">
    An update will then install JustJ Java 21:
    </div>
    <img src="JustJ21Install.png" style="width: 40em; margin: 1ex;" class="img-border"/>
    </li>    
    <li style="margin-top: 1ex;">
    <div style="margin-bottom: 1ex;">
    If your <code>eclipse.ini</code> does not specify a JustJ JRE but a <code>-vm</code> option is present,
    i.e., you are using an explicitly specified JRE/JDK installed in your system,
    you should edit the <code>eclipse.ini</code> to specify a Java 21 JRE/JDK like this:
    </div>
    <pre class="img-border" style="margin-left: 0.5em;">-vm
C:\Program Files\Java\jdk-21.0.3+9\bin</pre>
    You can get Java 21 from <a href="https://adoptium.net/">https://adoptium.net/</a>.
    </li>
    <li style="margin-top: 1ex;">
    <div style="margin-bottom: 1ex;">
    If your <code>eclipse.ini</code> does not specify <code>-vm</code> at all, then the IDE is using the default system JRE/JDK.
    You can either edit the <code>eclipse.ini</code> to add the two lines as shown above, with no trailing spaces, to the start of the file,
    or you can ensure that Java 21 is installed as your default system JRE.
    </div>
    </li>
    <li style="margin-top: 1ex;">
    If you need to find these instructions again, you can use the IDE's menu <code>Help &rarr; Sponsor</code> to open this page.
    </li>
    </ul>
    </div>
    <hr>
    </div>
	-->

    <h3><b>What Can <span class="orange">I</span> Do to Support the <span style="color: #2c2255;">Eclipse</span> <span class="orange">Community</span>?</b></h3>
    <a href="$sponsor_link" target="_blank">
    <img style="$animation_style" src="../installer/FriendsOfEclipse.png"/>
    </a>
    &nbsp;&nbsp;
    <a href="$sponsor_link" target="_blank" class="btn btn-huge btn-primary" style="$animation_style"><i class="fa fa-star"></i> Sponsor</a>
    <span style="font-size: 400%; color: #2c2255; margin-top: 0.5ex; margin-left: 0.5em; position: absolute; $animation_style">&dollar;</span>
    <span style="font-size: 400%; color: #2c2255; margin-top: 0.5ex; margin-left: 1.5em; position: absolute; $animation_style">&euro;</span>
    <span style="font-size: 400%; color: #2c2255; margin-top: 0.5ex; margin-left: 2.5em; position: absolute; $animation_style">&pound;</span>
    <span style="font-size: 400%; color: #2c2255; margin-top: 0.5ex; margin-left: 3.5em; position: absolute; $animation_style">&#20803;</span>

    <hr>
    <input type="checkbox" id="toggle-id-1" class="toggle"/>
    <label for="toggle-id-1" class="toggle-label">$toggle_expand$toggle_collapse Invest Your Money $read_more</label>
    <ul class="toggle-content">
      <li>
      If you love Eclipse as much as we do, please <a href="$sponsor_link" target="_blank">give</a> a token of your appreciation.
      </li>
      <ul>
        <li>
        Eclipse is <b>100%</b> funded by the community and <b class="orange">you</b> as one of the millions of daily users are part of that community.
        </li>
        <li>
        All the money will go directly to funding Eclipse IDE development and infrastructure.
        </li>
      </ul>
      <li>
      If you work for a company, encourage your employer to become an <a href="https://www.eclipse.org/membership/" target="_blank">Eclipse Member</a>.
      </li>
      <li>
      If you want influence, join the <a href="https://eclipseide.org/working-group/" target="_blank">Eclipse IDE Working Group</a>.
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
            <a href="https://www.eclipse.org/setups/installer/?url=https://raw.githubusercontent.com/eclipse-platform/eclipse.platform.releng.aggregator/master/oomph/PlatformSDKConfiguration.setup&show=true" target="_blank">Contribute to the Eclipse Platform</a>.
            </li>
            <li>
            <a href="https://www.eclipse.org/setups/installer/?url=https://raw.githubusercontent.com/eclipse-oomph/oomph/master/setups/configurations/OomphConfiguration.setup&show=true" target="_blank">Contribute to Oomph</a>.
            </li>
            <li>
            <a href="https://www.eclipse.org/setups/installer/?url=https://raw.githubusercontent.com/eclipse-emf/org.eclipse.emf/master/releng/org.eclipse.emf.releng/EMFDevelopmentEnvironmentConfiguration.setup&show=true&show=true" target="_blank">Contribute to the Eclipse Modeling Framework</a>.
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
    $contents = preg_replace('#<a href="https://www.eclipse.org/donate/" class="btn btn-huge btn-info"><i class="fa fa-star"></i> Donate</a></div>#', '<a href="https://www.eclipse.org/sponsor/ide/?scope=Eclipse%20Installer" class="btn btn-huge btn-primary"><i class="fa fa-star"></i> Sponsor</a></div>', $contents);
    echo "$contents";

?>
