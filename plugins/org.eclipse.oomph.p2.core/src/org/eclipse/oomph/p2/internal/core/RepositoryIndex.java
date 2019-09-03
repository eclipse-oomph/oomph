package org.eclipse.oomph.p2.internal.core;

import java.security.cert.Certificate;
import java.util.*;
import org.eclipse.equinox.p2.metadata.*;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.IndexReport;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.IUReport;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.Report;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.Report.LicenseDetail;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.Reporter;

public class RepositoryIndex
{
  protected static String nl;
  public static synchronized RepositoryIndex create(String lineSeparator)
  {
    nl = lineSeparator;
    RepositoryIndex result = new RepositoryIndex();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected static final String _1 = " Broken Branding Images)</span>";
  protected static final String _2 = " Invalid Licenses)</span>";
  protected static final String _3 = " Missing *.pack.gz)</span>";
  protected static final String _4 = " No Branding Images)</span>";
  protected static final String _5 = " Not Provided by Eclipse)</span>";
  protected static final String _6 = " Unsigned Artifacts)</span>";
  protected static final String _7 = " font-smaller\">";
  protected static final String _8 = " with Multiple Versions)</span>";
  protected static final String _9 = "$(\"body\").append($temp);";
  protected static final String _10 = "$temp.remove();";
  protected static final String _11 = "$temp.val($(element).text()).select();";
  protected static final String _12 = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  protected static final String _13 = "');\">";
  protected static final String _14 = "');\">&#x25B7;</button>";
  protected static final String _15 = "'){";
  protected static final String _16 = "(";
  protected static final String _17 = ")";
  protected static final String _18 = ") Unsigned</span>";
  protected static final String _19 = ")</span>";
  protected static final String _20 = "++count;";
  protected static final String _21 = "-";
  protected static final String _22 = ".bad-absolute-location {";
  protected static final String _23 = ".bb {";
  protected static final String _24 = ".filter {";
  protected static final String _25 = ".fit-image {";
  protected static final String _26 = ".font-smaller {";
  protected static final String _27 = ".iu-link {";
  protected static final String _28 = ".resolved-requirement {";
  protected static final String _29 = ".text-nowrap {";
  protected static final String _30 = ".unresolved-requirement {";
  protected static final String _31 = ".unused-capability {";
  protected static final String _32 = ".used-capability {";
  protected static final String _33 = ".xml-attribute {";
  protected static final String _34 = ".xml-attribute-value {";
  protected static final String _35 = ".xml-element {";
  protected static final String _36 = ".xml-element-value {";
  protected static final String _37 = ".xml-iu {";
  protected static final String _38 = ".xml-repo {";
  protected static final String _39 = ".xml-token {";
  protected static final String _40 = "; margin-left: -1em; list-style-type: none; padding: 0; margin: 0;\">";
  protected static final String _41 = "; margin-left: -1em; list-style-type: none;\">";
  protected static final String _42 = ";\" onclick=\"toggle('categories_all_arrows');";
  protected static final String _43 = ";\" onclick=\"toggle('certificates_all_arrows');";
  protected static final String _44 = ";\" onclick=\"toggle('licenses_all_arrows');";
  protected static final String _45 = ";\" onclick=\"toggle('products_all_arrows');";
  protected static final String _46 = "<!-- navigation sidebar -->";
  protected static final String _47 = "<!--- providers -->";
  protected static final String _48 = "<!----------->";
  protected static final String _49 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
  protected static final String _50 = "</a>";
  protected static final String _51 = "</a>.</p>";
  protected static final String _52 = "</a></li>";
  protected static final String _53 = "</aside>";
  protected static final String _54 = "</b>";
  protected static final String _55 = "</body>";
  protected static final String _56 = "</button>";
  protected static final String _57 = "</div>";
  protected static final String _58 = "</h2>";
  protected static final String _59 = "</h3>";
  protected static final String _60 = "</head>";
  protected static final String _61 = "</header>";
  protected static final String _62 = "</html>";
  protected static final String _63 = "</li>";
  protected static final String _64 = "</main>";
  protected static final String _65 = "</ol>";
  protected static final String _66 = "</p>";
  protected static final String _67 = "</pre>";
  protected static final String _68 = "</script>";
  protected static final String _69 = "</section>";
  protected static final String _70 = "</span>";
  protected static final String _71 = "</span><span style=\"color: green; text-decoration: underline; ";
  protected static final String _72 = "</style>";
  protected static final String _73 = "</title>";
  protected static final String _74 = "</ul>";
  protected static final String _75 = "<a class=\"separator\" href=\"";
  protected static final String _76 = "<a class=\"separator\" href=\".\">";
  protected static final String _77 = "<a class=\"separator\" href=\"https://www.eclipse.org/downloads\">Downloads</a>";
  protected static final String _78 = "<a class=\"separator\" href=\"https://www.eclipse.org\">Home</a>";
  protected static final String _79 = "<a class=\"separator\" style=\"display: inline-block; margin-left: 0.25em;\" href=\"";
  protected static final String _80 = "<a href=\"";
  protected static final String _81 = "<a href=\"https://www.eclipse.org/\">";
  protected static final String _82 = "<aside id=\"leftcol\" class=\"col-md-4 font-smaller\">";
  protected static final String _83 = "<b>Built: ";
  protected static final String _84 = "<body id=\"body_solstice\">";
  protected static final String _85 = "<br/>";
  protected static final String _86 = "<br/><b>Reported: ";
  protected static final String _87 = "<button id=\"";
  protected static final String _88 = "<button id=\"_";
  protected static final String _89 = "<button id=\"__";
  protected static final String _90 = "<button id=\"_f";
  protected static final String _91 = "<button id=\"categories_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _92 = "<button id=\"categories_arrows\" class=\"orange bb\" onclick=\"expand_collapse('categories'); expand_collapse_inline('categories_all_arrows');\">&#x25B7;</button>";
  protected static final String _93 = "<button id=\"certificates_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _94 = "<button id=\"certificates_arrows\" class=\"orange bb\" onclick=\"expand_collapse('certificates'); expand_collapse_inline('certificates_all_arrows');\">&#x25B7;</button>";
  protected static final String _95 = "<button id=\"feature_providers_arrows\" class=\"orange bb\" onclick=\"expand_collapse('feature_providers');\">&#x25B7;</button>";
  protected static final String _96 = "<button id=\"features_arrows\" class=\"orange bb\" onclick=\"expand_collapse('features');\">&#x25B7;</button>";
  protected static final String _97 = "<button id=\"ius_arrows\" class=\"orange bb\" onclick=\"expand_collapse('ius'); match('subset');\">&#x25B7;</button>";
  protected static final String _98 = "<button id=\"lic_";
  protected static final String _99 = "<button id=\"licenses_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _100 = "<button id=\"licenses_arrows\" class=\"orange bb\" onclick=\"expand_collapse('licenses'); expand_collapse_inline('licenses_all_arrows');\">&#x25B7;</button>";
  protected static final String _101 = "<button id=\"products_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _102 = "<button id=\"products_arrows\" class=\"orange bb\" onclick=\"expand_collapse('products'); expand_collapse_inline('products_all_arrows');\">&#x25B7;</button>";
  protected static final String _103 = "<button id=\"repos_arrows\" class=\"orange bb\" onclick=\"expand_collapse('repos');\">&#x25B7;</button>";
  protected static final String _104 = "<button id=\"xml-links\" class=\"orange bb\" onclick=\"expand_collapse_all('xml-links');\">&#x25B7;</button>";
  protected static final String _105 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 150%;\" onclick=\"copyToClipboard('#p1')\">&#x270e;</button>";
  protected static final String _106 = "<div class=\"col-sm-16 padding-left-30\">";
  protected static final String _107 = "<div class=\"container\">";
  protected static final String _108 = "<div class=\"font-smaller\" style=\"margin-left: ";
  protected static final String _109 = "<div class=\"hidden-xs col-sm-8 col-md-6 col-lg-5\" id=\"header-left\">";
  protected static final String _110 = "<div class=\"novaContent container\" id=\"novaContent\">";
  protected static final String _111 = "<div class=\"row\" id=\"header-row\">";
  protected static final String _112 = "<div class=\"row\">";
  protected static final String _113 = "<div class=\"wrapper-logo-default\">";
  protected static final String _114 = "<div class=\"xml-iu\" style=\"display:block;\">";
  protected static final String _115 = "<div class=\"xml-repo\" id=\"repos\" style=\"display: none;\">";
  protected static final String _116 = "<div id=\"ius\" style=\"display:none;\">";
  protected static final String _117 = "<div id=\"maincontent\">";
  protected static final String _118 = "<div id=\"midcolumn\" style=\"width: 70%;\">";
  protected static final String _119 = "<div style=\"height: 40ex;\">";
  protected static final String _120 = "<div style=\"text-indent: -2em\">";
  protected static final String _121 = "<div>";
  protected static final String _122 = "<h2 style=\"text-align: center;\">";
  protected static final String _123 = "<h3 class=\"sr-only\">Breadcrumbs</h3>";
  protected static final String _124 = "<h3 style=\"font-weight: bold;\">";
  protected static final String _125 = "<h3 style=\"font-weight: bold;\">Artifacts</h3>";
  protected static final String _126 = "<h3 style=\"font-weight: bold;\">Certificate</h3>";
  protected static final String _127 = "<h3 style=\"font-weight: bold;\">Description</h3>";
  protected static final String _128 = "<h3 style=\"font-weight: bold;\">Licenses</h3>";
  protected static final String _129 = "<h3 style=\"font-weight: bold;\">Provider</h3>";
  protected static final String _130 = "<head>";
  protected static final String _131 = "<header role=\"banner\" id=\"header-wrapper\">";
  protected static final String _132 = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">";
  protected static final String _133 = "<img alt=\"Branding Image\" class=\"fit-image\" src=\"";
  protected static final String _134 = "<img alt=\"Signed\" class=\"fit-image\" src=\"";
  protected static final String _135 = "<img alt=\"Signed\" class=\"fit-image\" src=\"../";
  protected static final String _136 = "<img class=\"fit-image> src=\"";
  protected static final String _137 = "<img class=\"fit-image\" src=\"";
  protected static final String _138 = "<img class=\"fit-image\" src=\"../";
  protected static final String _139 = "<img class=\"logo-eclipse-default img-responsive hidden-xs\" alt=\"Eclipse Log\" src=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/logo/eclipse-426x100.png\"/>";
  protected static final String _140 = "<img style=\"float:right\" src=\"";
  protected static final String _141 = "<input id=\"bad-license\" type=\"radio\" name=\"filter\" value=\"bad-license\" class=\"filter\" onclick=\"filterIU('bad-license');\"> Bad License </input>";
  protected static final String _142 = "<input id=\"bad-provider\" type=\"radio\" name=\"filter\" value=\"bad-provider\" class=\"filter\" onclick=\"filterIU('bad-provider');\"> Bad Provider </input>";
  protected static final String _143 = "<input id=\"broken-branding\" type=\"radio\" name=\"filter\" value=\"broken-branding\" class=\"filter\" onclick=\"filterIU('broken-branding');\"> Broken Branding </input>";
  protected static final String _144 = "<input id=\"duplicates\" type=\"radio\" name=\"filter\" value=\"duplicate\" class=\"filter\" onclick=\"filterIU('duplicate');\"> Duplicates </input>";
  protected static final String _145 = "<input id=\"filter\" type=\"radio\" name=\"filter\" value=\"all\" class=\"filter\" onclick=\"filterIU('iu-li');\" checked=\"checked\"> All </input>";
  protected static final String _146 = "<input id=\"missing\" type=\"radio\" name=\"filter\" value=\"missing\" class=\"filter\" onclick=\"filterIU('missing');\"> Missing .pack.gz </input>";
  protected static final String _147 = "<input id=\"unsigned\" type=\"radio\" name=\"filter\" value=\"unsigned\" class=\"filter\" onclick=\"filterIU('unsigned');\"> Unsigned </input>";
  protected static final String _148 = "<li class=\"active\">";
  protected static final String _149 = "<li class=\"font-smaller text-nowrap\">";
  protected static final String _150 = "<li class=\"separator\" style=\"font-size: 90%;\">";
  protected static final String _151 = "<li class=\"separator\">";
  protected static final String _152 = "<li id=\"_iu_";
  protected static final String _153 = "<li style=\"font-size: 100%; white-space: nowrap;\">";
  protected static final String _154 = "<li style=\"margin-left: 1em;\">";
  protected static final String _155 = "<li>";
  protected static final String _156 = "<li><a href=\"";
  protected static final String _157 = "<li><a href=\"https://www.eclipse.org/\">Home</a></li>";
  protected static final String _158 = "<li><a href=\"https://www.eclipse.org/downloads/\">Downloads</a></li>";
  protected static final String _159 = "<link href=\"//fonts.googleapis.com/css?family=Open+Sans:400,700,300,600,100\" rel=\"stylesheet\" type=\"text/css\"/>";
  protected static final String _160 = "<link rel=\"icon\" type=\"image/ico\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/favicon.ico\"/>";
  protected static final String _161 = "<link rel=\"stylesheet\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/stylesheets/styles.min.css\"/>";
  protected static final String _162 = "<main class=\"no-promo\">";
  protected static final String _163 = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
  protected static final String _164 = "<meta name=\"description\" content=\"Update Sites Reports\"/>";
  protected static final String _165 = "<meta name=\"keywords\" content=\"eclipse,update site\"/>";
  protected static final String _166 = "<ol class=\"breadcrumb\">";
  protected static final String _167 = "<p style=\"font-size: 125%; text-align: center;\">";
  protected static final String _168 = "<p style=\"text-align: center;\">";
  protected static final String _169 = "<p>";
  protected static final String _170 = "<p></p>";
  protected static final String _171 = "<p>Reports are generated specifically for the following sites:</p>";
  protected static final String _172 = "<p>This is a generated report folder index.</p>";
  protected static final String _173 = "<p>This is the generated report index.</p>";
  protected static final String _174 = "<p>This report is produced by <a href=\"";
  protected static final String _175 = "<pre id=\"_";
  protected static final String _176 = "<pre id=\"__";
  protected static final String _177 = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";
  protected static final String _178 = "<script>";
  protected static final String _179 = "<section class=\"hidden-print default-breadcrumbs\" id=\"breadcrumb\">";
  protected static final String _180 = "<span ";
  protected static final String _181 = "<span class=\"text-nowrap\"><span style=\"color: SteelBlue; font-size: 75%;\">";
  protected static final String _182 = "<span id=\"p1\" style=\"font-size: 125%\">";
  protected static final String _183 = "<span style=\"";
  protected static final String _184 = "<span style=\"color: DarkCyan; font-size: 110%;\">(";
  protected static final String _185 = "<span style=\"color: DarkCyan;\">";
  protected static final String _186 = "<span style=\"color: DarkOliveGreen;";
  protected static final String _187 = "<span style=\"color: DarkOliveGreen;\">";
  protected static final String _188 = "<span style=\"color: FireBrick; font-size: 60%;\">(";
  protected static final String _189 = "<span style=\"color: FireBrick; font-size: 60%;\">(Inappropriate absolute location)</span>";
  protected static final String _190 = "<span style=\"color: FireBrick;\">(";
  protected static final String _191 = "<span style=\"color: FireBrick;\">Unsigned</span>";
  protected static final String _192 = "<span style=\"color: red; text-decoration: line-through; ";
  protected static final String _193 = "<span style=\"font-size:100%;\">";
  protected static final String _194 = "<span style=\"margin-left: 1em;\" class=\"nowrap\">Filter Pattern: <input id=\"subset\" type=\"text\" oninput=\"match('subset');\"> <span style=\"color: firebrick;\" id=\"subset-error\"></span></input></span><br/>";
  protected static final String _195 = "<span style=\"margin-left: 1em;\">";
  protected static final String _196 = "<style>";
  protected static final String _197 = "<title>";
  protected static final String _198 = "<tt style=\"float: left;\" class=\"orange\">&#xbb;</tt>";
  protected static final String _199 = "<tt style=\"float: right;\" class=\"orange\">&#xab;</tt>";
  protected static final String _200 = "<ul class=\"font-smaller\" id=\"";
  protected static final String _201 = "<ul class=\"font-smaller\" style=\"list-style-type: none; display:none; margin-left: -3em;\" id=\"";
  protected static final String _202 = "<ul id=\"";
  protected static final String _203 = "<ul id=\"categories\" style=\"display: ";
  protected static final String _204 = "<ul id=\"certificates\" style=\"display:";
  protected static final String _205 = "<ul id=\"feature_providers\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _206 = "<ul id=\"features\" style=\"display: none; list-style-type: none; margin-left: -2em;\">";
  protected static final String _207 = "<ul id=\"leftnav\" class=\"ul-left-nav fa-ul hidden-print\">";
  protected static final String _208 = "<ul id=\"licenses\" style=\"display: ";
  protected static final String _209 = "<ul id=\"products\" style=\"display: ";
  protected static final String _210 = "<ul style=\"display:none; list-style-type: none; padding: 0; margin: 0;\" id=\"__";
  protected static final String _211 = "<ul style=\"list-style-type: none; display:none; padding: 0; margin: 0; margin-left: 2em;\" id=\"_f";
  protected static final String _212 = "<ul style=\"list-style-type: none; padding: 0; margin-left: 1em;\">";
  protected static final String _213 = "<ul>";
  protected static final String _214 = "=</span>";
  protected static final String _215 = ">";
  protected static final String _216 = "Categories";
  protected static final String _217 = "Content Metadata";
  protected static final String _218 = "Features";
  protected static final String _219 = "Features/Products";
  protected static final String _220 = "In addition to this composite report, reports are also generated for each of the composed children listed in the navigation bar to the left.";
  protected static final String _221 = "Installable Units";
  protected static final String _222 = "Licenses";
  protected static final String _223 = "Missing";
  protected static final String _224 = "No Name<br/>";
  protected static final String _225 = "Products";
  protected static final String _226 = "Providers";
  protected static final String _227 = "Signing Certificates";
  protected static final String _228 = "This is a composite update site.";
  protected static final String _229 = "XML";
  protected static final String _230 = "[<img class=\"fit-image\" src=\"";
  protected static final String _231 = "\" alt=\"\"/>";
  protected static final String _232 = "\" class=\"bb\" style=\"";
  protected static final String _233 = "\" class=\"iu-li";
  protected static final String _234 = "\" onclick=\"clickOnToggleButton('licenses_arrows'); clickOnToggleButton('__";
  protected static final String _235 = "\" style=\"display: none;\">";
  protected static final String _236 = "\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _237 = "\" style=\"display:none; margin-left: 2em; background-color: ";
  protected static final String _238 = "\" style=\"list-style-type: none; display: none; margin-left: -1em;\">";
  protected static final String _239 = "\" target=\"report_source\">";
  protected static final String _240 = "\"/>";
  protected static final String _241 = "\"/> ";
  protected static final String _242 = "\"/>]";
  protected static final String _243 = "\">";
  protected static final String _244 = "\">&#x25B7;</button>";
  protected static final String _245 = "\"><img class=\"fit-image\" src=\"";
  protected static final String _246 = "_arrows'); clickOnToggleButton('_";
  protected static final String _247 = "_arrows'); navigateTo('_";
  protected static final String _248 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('";
  protected static final String _249 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('__";
  protected static final String _250 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('";
  protected static final String _251 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_";
  protected static final String _252 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('__";
  protected static final String _253 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_f";
  protected static final String _254 = "background-color: white;";
  protected static final String _255 = "border: 1px solid black;";
  protected static final String _256 = "border: none;";
  protected static final String _257 = "break;";
  protected static final String _258 = "buttonTargets.item(i).innerHTML = '&#x25B7;';";
  protected static final String _259 = "buttonTargets.item(i).innerHTML = '&#x25E2;';";
  protected static final String _260 = "catch (err) {";
  protected static final String _261 = "color: DarkSlateGray;";
  protected static final String _262 = "color: FireBrick;";
  protected static final String _263 = "color: IndianRed;";
  protected static final String _264 = "color: MediumAquaMarine;";
  protected static final String _265 = "color: MediumOrchid;";
  protected static final String _266 = "color: SaddleBrown;";
  protected static final String _267 = "color: SeaGreen;";
  protected static final String _268 = "color: SteelBlue;";
  protected static final String _269 = "color: Teal;";
  protected static final String _270 = "continue;";
  protected static final String _271 = "currentFilter = filter.value;";
  protected static final String _272 = "document.execCommand(\"copy\");";
  protected static final String _273 = "e.click();";
  protected static final String _274 = "e.innerHTML = '&#x25B7;';";
  protected static final String _275 = "e.innerHTML = '&#x25E2;';";
  protected static final String _276 = "e.scrollIntoView();";
  protected static final String _277 = "e.style.display = 'block';";
  protected static final String _278 = "e.style.display = 'inline';";
  protected static final String _279 = "e.style.display = 'inline-block';";
  protected static final String _280 = "e.style.display = 'none';";
  protected static final String _281 = "e.title= 'Collapse All';";
  protected static final String _282 = "e.title= 'Expand All';";
  protected static final String _283 = "em; text-indent: -4em;\">";
  protected static final String _284 = "em;\">";
  protected static final String _285 = "f.innerHTML = '&#x25B7;';";
  protected static final String _286 = "f.innerHTML = '&#x25E2;';";
  protected static final String _287 = "font-family: monospace;";
  protected static final String _288 = "font-size: 125%;";
  protected static final String _289 = "font-size: 80%;";
  protected static final String _290 = "font-size: 90%;";
  protected static final String _291 = "for (var i = 0; i < buttonTargets.length; i++) {";
  protected static final String _292 = "for (var i = 0; i < ius.length; i++) {";
  protected static final String _293 = "for (var i = 0; i < spanTargets.length; i++) {";
  protected static final String _294 = "function clickOnButton(id) {";
  protected static final String _295 = "function clickOnToggleButton(id) {";
  protected static final String _296 = "function copyToClipboard(element) {";
  protected static final String _297 = "function expand(id) {";
  protected static final String _298 = "function expand2(self, id) {";
  protected static final String _299 = "function expand3(self, id) {";
  protected static final String _300 = "function expand_collapse(id) {";
  protected static final String _301 = "function expand_collapse_all(base) {";
  protected static final String _302 = "function expand_collapse_inline(id) {";
  protected static final String _303 = "function expand_collapse_inline_block(id) {";
  protected static final String _304 = "function filterIU(className) {";
  protected static final String _305 = "function match(id) {";
  protected static final String _306 = "function navigateTo(id) {";
  protected static final String _307 = "function toggle(id) {";
  protected static final String _308 = "height: 2ex;";
  protected static final String _309 = "if (!targetsArray.includes(iu)) {";
  protected static final String _310 = "if ((matchText == '' || text.match(matchText) != null) && targetsArray.includes(iu)) {";
  protected static final String _311 = "if (count == 0 && message.innerHTML == '') {";
  protected static final String _312 = "if (count == 0) {";
  protected static final String _313 = "if (e.innerHTML == '";
  protected static final String _314 = "if (e.innerHTML == '\\u25E2') {";
  protected static final String _315 = "if (e.style.display == 'none'){";
  protected static final String _316 = "if (e.title == 'Expand All') {";
  protected static final String _317 = "if (f != null) {";
  protected static final String _318 = "if (f !=null) {";
  protected static final String _319 = "if (filter != null && filter.value != 'all') {";
  protected static final String _320 = "if (matchText != '' && iu.textContent.match(matchText) == null) {";
  protected static final String _321 = "if (t.title != 'Collapse All'){";
  protected static final String _322 = "if (t.title == 'Collapse All'){";
  protected static final String _323 = "iu.style.display = 'block';";
  protected static final String _324 = "iu.style.display = 'none';";
  protected static final String _325 = "margin-bottom: -2ex;";
  protected static final String _326 = "margin-left: 0em;";
  protected static final String _327 = "margin-top: -2ex;";
  protected static final String _328 = "margin: 0px 0px 0px 0px;";
  protected static final String _329 = "message.innerHTML = '';";
  protected static final String _330 = "message.innerHTML = 'No matches';";
  protected static final String _331 = "message.innerHTML = \"\";";
  protected static final String _332 = "message.innerHTML = errMessage;";
  protected static final String _333 = "padding: -2pt -2pt -2pt -2pt;";
  protected static final String _334 = "padding: 0px 0px;";
  protected static final String _335 = "span:target {";
  protected static final String _336 = "spanTargets.item(i).style.display = 'inline-block';";
  protected static final String _337 = "spanTargets.item(i).style.display = 'none';";
  protected static final String _338 = "try";
  protected static final String _339 = "try {";
  protected static final String _340 = "var $temp = $(\"<input>\");";
  protected static final String _341 = "var buttonTargets = document.getElementsByClassName(base + '_button');";
  protected static final String _342 = "var count = 0;";
  protected static final String _343 = "var currentFilter = 'iu-li';";
  protected static final String _344 = "var e = document.getElementById('subset');";
  protected static final String _345 = "var e = document.getElementById(base);";
  protected static final String _346 = "var e = document.getElementById(id);";
  protected static final String _347 = "var errMessage = err.message;";
  protected static final String _348 = "var f = document.getElementById(id+\"_arrows\");";
  protected static final String _349 = "var filter = document.querySelector('input[name=\"filter\"]:checked');";
  protected static final String _350 = "var iu = ius[i];";
  protected static final String _351 = "var ius = document.getElementsByClassName('iu-li');";
  protected static final String _352 = "var matchText = e.value;";
  protected static final String _353 = "var message = document.getElementById('subset-error');";
  protected static final String _354 = "var message = document.getElementById(id + '-error');";
  protected static final String _355 = "var spanTargets = document.getElementsByClassName(base);";
  protected static final String _356 = "var state = e.innerHTML;";
  protected static final String _357 = "var t = document.getElementById('all');";
  protected static final String _358 = "var t = document.getElementById(self);";
  protected static final String _359 = "var targets = document.getElementsByClassName(className);";
  protected static final String _360 = "var targets = document.getElementsByClassName(currentFilter);";
  protected static final String _361 = "var targetsArray = [].slice.call(targets);";
  protected static final String _362 = "var text = iu.textContent;";
  protected static final String _363 = "white-space: nowrap;";
  protected static final String _364 = "white-space: pre;";
  protected static final String _365 = "width: 2ex;";
  protected static final String _366 = "{";
  protected static final String _367 = "}";
  protected static final String _368 = "} else {";
  protected final String NL_1 = NL + "  ";
  protected final String NL_2 = NL + "    ";
  protected final String NL_3 = NL + "     ";
  protected final String NL_4 = NL + "      ";
  protected final String NL_5 = NL + "        ";
  protected final String NL_6 = NL + "         ";
  protected final String NL_7 = NL + "          ";
  protected final String NL_8 = NL + "           ";
  protected final String NL_9 = NL + "            ";
  protected final String NL_10 = NL + "             ";
  protected final String NL_11 = NL + "              ";
  protected final String NL_12 = NL + "               ";
  protected final String NL_13 = NL + "                ";
  protected final String NL_14 = NL + "                 ";
  protected final String NL_15 = NL + "                   ";
  protected final String NL_16 = NL + "                    ";
  protected final String NL_17 = NL + "                     ";
  protected final String NL_18 = NL + "                      ";
  protected final String NL_19 = NL + "                        ";
  protected final String _369 = _49 + NL + _132 + NL + _130 + NL_1 + _163 + NL_1 + _197;
  protected final String _370 = _73 + NL_1 + _165 + NL_1 + _164 + NL_1 + _159 + NL_1 + _161 + NL_1 + _160 + NL_1 + _177;
  protected final String _371 = NL_1 + _196 + NL + NL + _29 + NL_1 + _363 + NL + _367 + NL + NL + _26 + NL_1 + _290 + NL + _367 + NL + NL + _25 + NL_1 + _365 + NL_1 + _308 + NL + _367 + NL + NL + _335 + NL_1 + _288 + NL_1 + _255 + NL + _367 + NL + NL + _38 + NL_1 + _364 + NL_1 + _256 + NL_1 + _334 + NL_1 + _327 + NL_1 + _325 + NL_1 + _326 + NL + _367 + NL + NL + _22 + NL_1 + _262 + NL_1 + _290 + NL + _367 + NL + NL + _37 + NL_1 + _364 + NL_1 + _256 + NL_1 + _334 + NL_1 + _327 + NL_1 + _325 + NL_1 + _326 + NL + _367 + NL + NL + _39 + NL_1 + _268 + NL_1 + _287 + NL_1 + _289 + NL + _367 + NL + NL + _33 + NL_1 + _264 + NL_1 + _287 + NL_1 + _289 + NL + _367 + NL + NL + _35 + NL_1 + _265 + NL_1 + _287 + NL_1 + _289 + NL + _367 + NL + NL + _34 + NL_1 + _261 + NL_1 + _290 + NL + _367 + NL + NL + _36 + NL_1 + _266 + NL_1 + _290 + NL + _367 + NL + NL + _23 + NL_1 + _254 + NL_1 + _256 + NL_1 + _334 + NL + _367 + NL + NL + _24 + NL_1 + _254 + NL + _367 + NL + NL + _27 + NL_1 + _333 + NL_1 + _328 + NL + _367 + NL + NL + _28 + NL_1 + _267 + NL_1 + _290 + NL + _367 + NL + NL + _32 + NL_1 + _269 + NL_1 + _290 + NL + _367 + NL + NL + _31 + NL_1 + _290 + NL + _367 + NL + NL + _30 + NL_1 + _263 + NL_1 + _290 + NL + _367 + NL_1 + _72 + NL + _60 + NL + NL_1 + _84;
  protected final String _372 = NL_2 + _178 + NL + NL_4 + _305 + NL_5 + _349 + NL_5 + _343 + NL_5 + _319 + NL_7 + _271 + NL_5 + _367 + NL_5 + _346 + NL_5 + _354 + NL_5 + _352 + NL_5 + _351 + NL_5 + _360 + NL_5 + _361 + NL_5 + _342 + NL_5 + _292 + NL_7 + _350 + NL_7 + _362 + NL_7 + _339 + NL_9 + _310 + NL_11 + _323 + NL_11 + _20 + NL_9 + _368 + NL_11 + _324 + NL_9 + _367 + NL_9 + _331 + NL_7 + _367 + NL_7 + _260 + NL_9 + _347 + NL_9 + _332 + NL_9 + _257 + NL_7 + _367 + NL_5 + _367 + NL_5 + _311 + NL_9 + _330 + NL_5 + _367 + NL_4 + _367 + NL + NL_4 + _304 + NL_5 + _344 + NL_5 + _352 + NL_5 + _342 + NL_5 + _351 + NL_5 + _359 + NL_5 + _361 + NL_5 + _292 + NL_7 + _350 + NL_7 + _309 + NL_9 + _324 + NL_7 + _368 + NL_9 + _338 + NL_9 + _366 + NL_11 + _320 + NL_13 + _324 + NL_13 + _270 + NL_11 + _367 + NL_9 + _367 + NL_9 + _260 + NL_9 + _367 + NL_9 + _323 + NL_9 + _20 + NL_7 + _367 + NL_5 + _367 + NL_5 + _353 + NL_5 + _312 + NL_9 + _330 + NL_5 + _368 + NL_9 + _329 + NL_5 + _367 + NL_4 + _367 + NL + NL_4 + _296 + NL_5 + _340 + NL_5 + _9 + NL_5 + _11 + NL_5 + _272 + NL_5 + _10 + NL_4 + _367 + NL + NL_4 + _294 + NL_5 + _346 + NL_5 + _273 + NL_4 + _367 + NL + NL_4 + _295 + NL_5 + _346 + NL_5 + _356 + NL_5 + _313;
  protected final String _373 = _15 + NL_7 + _273 + NL_5 + _367 + NL_4 + _367 + NL + NL_4 + _306 + NL_5 + _346 + NL_5 + _276 + NL_4 + _367 + NL + NL_4 + _307 + NL_5 + _346 + NL_5 + _316 + NL_7 + _281 + NL_7 + _275 + NL_5 + _368 + NL_7 + _282 + NL_7 + _274 + NL_5 + _367 + NL_4 + _367 + NL + NL_4 + _298 + NL_5 + _358 + NL_5 + _346 + NL_5 + _348 + NL_5 + _322 + NL_7 + _277 + NL_7 + _286 + NL_5 + _368 + NL_7 + _280 + NL_7 + _285 + NL_5 + _367 + NL_4 + _367 + NL + NL_4 + _299 + NL_5 + _358 + NL_5 + _346 + NL_5 + _348 + NL_5 + _321 + NL_7 + _280 + NL_7 + _285 + NL_5 + _367 + NL_4 + _367 + NL + NL_4 + _297 + NL_5 + _357 + NL_5 + _346 + NL_5 + _348 + NL_5 + _322 + NL_7 + _277 + NL_7 + _286 + NL_5 + _368 + NL_7 + _280 + NL_7 + _285 + NL_5 + _367 + NL_4 + _367 + NL + NL_4 + _300 + NL_5 + _346 + NL_5 + _348 + NL_5 + _315 + NL_7 + _277 + NL_7 + _286 + NL_5 + _368 + NL_7 + _280 + NL_7 + _285 + NL_5 + _367 + NL_4 + _367 + NL + NL_4 + _302 + NL_5 + _346 + NL_5 + _348 + NL_5 + _315 + NL_7 + _278 + NL_7 + _317 + NL_9 + _286 + NL_7 + _367 + NL_5 + _368 + NL_7 + _280 + NL_7 + _318 + NL_9 + _285 + NL_7 + _367 + NL_5 + _367 + NL_4 + _367 + NL + NL_4 + _303 + NL_5 + _346 + NL_5 + _348 + NL_5 + _315 + NL_7 + _279 + NL_7 + _317 + NL_9 + _286 + NL_7 + _367 + NL_5 + _368 + NL_7 + _280 + NL_7 + _318 + NL_9 + _285 + NL_7 + _367 + NL_5 + _367 + NL_4 + _367 + NL + NL_4 + _301 + NL_5 + _345 + NL_5 + _341 + NL_5 + _355 + NL_5 + _314 + NL_9 + _274 + NL_9 + _291 + NL_11 + _258 + NL_9 + _367 + NL_9 + _293 + NL_11 + _337 + NL_9 + _367 + NL_5 + _368 + NL_9 + _275 + NL_9 + _291 + NL_11 + _259 + NL_9 + _367 + NL_9 + _293 + NL_11 + _336 + NL_9 + _367 + NL_5 + _367 + NL_4 + _367 + NL + NL_2 + _68 + NL + NL_2 + _131 + NL_4 + _107 + NL_5 + _111 + NL_7 + _109 + NL_9 + _113 + NL_11 + _81 + NL_13 + _139 + NL_11 + _50 + NL_9 + _57 + NL_7 + _57 + NL_5 + _57 + NL_4 + _57 + NL_2 + _61;
  protected final String _374 = NL_2 + _179 + NL_4 + _107 + NL_5 + _123 + NL_5 + _112 + NL_7 + _106 + NL_9 + _166 + NL_11 + _157 + NL_11 + _158;
  protected final String _375 = NL_11 + _148;
  protected final String _376 = NL_11 + _156;
  protected final String _377 = NL_9 + _65 + NL_7 + _57 + NL_5 + _57 + NL_4 + _57 + NL_2 + _69 + NL + NL_2 + _162 + NL_2 + _110 + NL;
  protected final String _378 = NL_4 + _46 + NL_4 + _82 + NL_5 + _207 + NL_7 + _151 + NL_9 + _78 + NL_7 + _63 + NL_7 + _151 + NL_9 + _77 + NL_7 + _63;
  protected final String _379 = NL_7 + _151 + NL_9 + _75;
  protected final String _380 = _50 + NL_7 + _63;
  protected final String _381 = NL_7 + _151 + NL_9 + _199 + NL_9 + _76;
  protected final String _382 = NL_7 + _150 + NL_9 + _198;
  protected final String _383 = NL_9 + _199;
  protected final String _384 = NL_9 + _79;
  protected final String _385 = NL_9 + _50 + NL_7 + _63;
  protected final String _386 = NL_5 + _74 + NL_4 + _53 + NL + NL_4 + _117 + NL_5 + _118;
  protected final String _387 = NL_7 + _122;
  protected final String _388 = NL_7 + _122 + NL_9 + _138;
  protected final String _389 = NL_9 + _224;
  protected final String _390 = NL_7 + _58;
  protected final String _391 = NL_7 + _167;
  protected final String _392 = NL_9 + _83;
  protected final String _393 = NL_9 + _86;
  protected final String _394 = _54 + NL_7 + _66;
  protected final String _395 = NL_7 + _168 + NL_9 + _105 + NL_9 + _182;
  protected final String _396 = _70 + NL_7 + _66 + NL_7 + _85;
  protected final String _397 = NL_7 + _140;
  protected final String _398 = NL_8 + _172 + NL_8 + _174;
  protected final String _399 = NL_8 + _171 + NL_8 + _213;
  protected final String _400 = NL_10 + _156;
  protected final String _401 = NL_8 + _74;
  protected final String _402 = NL_8 + _174;
  protected final String _403 = NL_8 + _129 + NL_8 + _169;
  protected final String _404 = NL_8 + _127 + NL_8 + _169;
  protected final String _405 = NL_8 + _126;
  protected final String _406 = NL_10 + _121 + NL_12 + _138;
  protected final String _407 = _240 + NL_12 + _191 + NL_10 + _57;
  protected final String _408 = NL_10 + _108;
  protected final String _409 = _284 + NL_12 + _135;
  protected final String _410 = NL_12 + _181;
  protected final String _411 = NL_10 + _57;
  protected final String _412 = NL_8 + _125;
  protected final String _413 = NL_8 + _169;
  protected final String _414 = NL_10 + _138;
  protected final String _415 = _240 + NL_10 + _185;
  protected final String _416 = _70 + NL_10 + _80;
  protected final String _417 = _50 + NL_8 + _66;
  protected final String _418 = NL_8 + _169 + NL_10 + _138;
  protected final String _419 = _240 + NL_10 + _138;
  protected final String _420 = _240 + NL_10 + _223 + NL_8 + _66;
  protected final String _421 = NL_6 + _128;
  protected final String _422 = NL_12 + _89;
  protected final String _423 = _14 + NL_12 + _138;
  protected final String _424 = _240 + NL_12 + _183;
  protected final String _425 = NL_12 + _176;
  protected final String _426 = NL_8 + _124 + NL_10 + _217 + NL_10 + _104 + NL_8 + _59 + NL_8 + _114;
  protected final String _427 = NL + _48;
  protected final String _428 = NL_8 + _57;
  protected final String _429 = NL_8 + _173;
  protected final String _430 = NL_10 + _228;
  protected final String _431 = NL_10 + _220 + NL_8 + _66;
  protected final String _432 = NL_8 + _124 + NL_8 + _103 + NL_8 + _137;
  protected final String _433 = _240 + NL_8 + _229;
  protected final String _434 = NL_8 + _189;
  protected final String _435 = NL_8 + _59 + NL_8 + _115;
  protected final String _436 = NL_8 + _124 + NL_8 + _100 + NL_8 + _137;
  protected final String _437 = _240 + NL_8 + _222 + NL_8 + _185;
  protected final String _438 = NL_8 + _188;
  protected final String _439 = NL_8 + _99;
  protected final String _440 = NL_8 + _59 + NL_8 + _208;
  protected final String _441 = NL_10 + _155 + NL_12 + _89;
  protected final String _442 = _14 + NL_12 + _137;
  protected final String _443 = _70 + NL_12 + _21 + NL_12 + _185;
  protected final String _444 = _70 + NL_12 + _210;
  protected final String _445 = _243 + NL_14 + _154 + NL_15 + _88;
  protected final String _446 = _85 + NL_15 + _175;
  protected final String _447 = _67 + NL_14 + _63 + NL_14 + _154 + NL_15 + _90;
  protected final String _448 = _14 + NL_15 + _219 + NL_15 + _211;
  protected final String _449 = NL_16 + _149 + NL_18 + _80;
  protected final String _450 = _243 + NL_19 + _137;
  protected final String _451 = NL_19 + _187;
  protected final String _452 = _70 + NL_18 + _50 + NL_16 + _63;
  protected final String _453 = NL_15 + _74 + NL_14 + _63 + NL_12 + _74 + NL_10 + _63;
  protected final String _454 = NL_8 + _170 + NL_8 + _124 + NL_8 + _94 + NL_8 + _137;
  protected final String _455 = _240 + NL_8 + _227 + NL_8 + _185;
  protected final String _456 = NL_8 + _93;
  protected final String _457 = NL_8 + _59 + NL_8 + _204;
  protected final String _458 = NL_8 + _155;
  protected final String _459 = NL_10 + _120 + NL_12 + _87;
  protected final String _460 = _240 + NL_12 + _190;
  protected final String _461 = _18 + NL_10 + _57;
  protected final String _462 = NL_12 + _87;
  protected final String _463 = NL_12 + _12;
  protected final String _464 = NL_12 + _134;
  protected final String _465 = NL_12 + _184;
  protected final String _466 = NL_10 + _201;
  protected final String _467 = NL_12 + _155 + NL_14 + _80;
  protected final String _468 = _50 + NL_12 + _63;
  protected final String _469 = NL_10 + _74 + NL_8 + _63;
  protected final String _470 = NL + _47;
  protected final String _471 = NL_8 + _170 + NL_8 + _124 + NL_8 + _95 + NL_8 + _137;
  protected final String _472 = _240 + NL_8 + _226 + NL_8 + _185;
  protected final String _473 = NL_8 + _59 + NL_8 + _205;
  protected final String _474 = NL_10 + _155 + NL_12 + _87;
  protected final String _475 = NL_12 + _133;
  protected final String _476 = NL_12 + _180;
  protected final String _477 = _70 + NL_12 + _185;
  protected final String _478 = _70 + NL_12 + _202;
  protected final String _479 = NL_14 + _155 + NL_15 + _80;
  protected final String _480 = _243 + NL_17 + _133;
  protected final String _481 = NL_17 + _187;
  protected final String _482 = _70 + NL_15 + _50 + NL_14 + _63;
  protected final String _483 = NL_12 + _74 + NL_10 + _63;
  protected final String _484 = NL_8 + _170 + NL_8 + _124 + NL_8 + _96 + NL_8 + _137;
  protected final String _485 = _240 + NL_8 + _218 + NL_8 + _185;
  protected final String _486 = NL_8 + _59 + NL_8 + _206;
  protected final String _487 = NL_10 + _153 + NL_13 + _80;
  protected final String _488 = _243 + NL_14 + _137;
  protected final String _489 = NL_14 + _187;
  protected final String _490 = _70 + NL_12 + _50;
  protected final String _491 = NL_12 + _98;
  protected final String _492 = NL_10 + _63;
  protected final String _493 = NL_8 + _170 + NL_8 + _124 + NL_8 + _102 + NL_8 + _136;
  protected final String _494 = _240 + NL_8 + _225 + NL_8 + _185;
  protected final String _495 = NL_8 + _101;
  protected final String _496 = NL_8 + _59 + NL_8 + _209;
  protected final String _497 = NL_10 + _153;
  protected final String _498 = NL_12 + _80;
  protected final String _499 = NL_12 + _200;
  protected final String _500 = _243 + NL_17 + _137;
  protected final String _501 = NL_12 + _74;
  protected final String _502 = NL_8 + _170 + NL_8 + _124 + NL_8 + _92 + NL_8 + _137;
  protected final String _503 = _240 + NL_8 + _216 + NL_8 + _185;
  protected final String _504 = NL_8 + _91;
  protected final String _505 = NL_8 + _59 + NL_8 + _203;
  protected final String _506 = NL_12 + _50;
  protected final String _507 = NL_8 + _124 + NL_8 + _97 + NL_8 + _137;
  protected final String _508 = _240 + NL_8 + _221 + NL_8 + _185;
  protected final String _509 = NL_8 + _59 + NL_8 + _116 + NL_10 + _194;
  protected final String _510 = NL_10 + _195 + NL_12 + _145;
  protected final String _511 = NL_12 + _141;
  protected final String _512 = NL_12 + _147;
  protected final String _513 = NL_12 + _142;
  protected final String _514 = NL_12 + _143;
  protected final String _515 = NL_12 + _144;
  protected final String _516 = NL_12 + _146;
  protected final String _517 = NL_10 + _70;
  protected final String _518 = NL_10 + _212;
  protected final String _519 = NL_12 + _152;
  protected final String _520 = _7 + NL_14 + _80;
  protected final String _521 = _243 + NL_15 + _137;
  protected final String _522 = _240 + NL_15 + _193;
  protected final String _523 = _70 + NL_15 + _186;
  protected final String _524 = _70 + NL_14 + _50;
  protected final String _525 = NL_14 + _98;
  protected final String _526 = NL_14 + _16;
  protected final String _527 = NL_15 + _137;
  protected final String _528 = _240 + NL_15 + _185;
  protected final String _529 = _70 + NL_14 + _17;
  protected final String _530 = NL_14 + _16 + NL_15 + _137;
  protected final String _531 = _240 + NL_15 + _137;
  protected final String _532 = _240 + NL_14 + _17;
  protected final String _533 = NL_14 + _230;
  protected final String _534 = NL_12 + _63;
  protected final String _535 = NL_10 + _74 + NL_8 + _57 + NL_8 + _119 + NL_8 + _57;
  protected final String _536 = NL_5 + _57 + NL_4 + _57 + NL_3 + _57 + NL_3 + _64 + NL_1 + _55 + NL + _62;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    Reporter reporter = (Reporter)argument;
  IUReport iuReport = argument instanceof IUReport ? (IUReport)argument : null;
  Report report = argument instanceof Report ? (Report)argument : iuReport == null ? null : iuReport.getReport();
  IndexReport indexReport = argument instanceof IndexReport ? (IndexReport)argument : null;

  class Helper {
    public String getStyle(LicenseDetail license) {
      StringBuilder result = new StringBuilder();
      result.append(license.isMatchedSUA() ? "color: DarkOrange;" : "color: SaddleBrown;");
      result.append(license.isSUA() ? " font-weight: bold;" : " text-decoration: line-through;");
      return result.toString();
    }

    public String htmlEscape(String value, boolean nbsp) {
      if (value == null) {
        return "null";
      }
      String result = value.replace("&", "&amp;").replace("<", "&lt;");
      if (nbsp) {
        result = result.replace(" ", "&nbsp;");
      }

      return result;
    }
  }

  Helper helper = new Helper();
    stringBuffer.append(_369);
    stringBuffer.append(reporter.getTitle());
    stringBuffer.append(_370);
    stringBuffer.append(_371);
    stringBuffer.append(_372);
    stringBuffer.append('\u25B7');
    stringBuffer.append(_373);
    stringBuffer.append(_374);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() == null) {
    stringBuffer.append(_375);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_63);
    } else {
    stringBuffer.append(_376);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_243);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_52);
    }
    }
    stringBuffer.append(_377);
    stringBuffer.append(_378);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() != null) {
    stringBuffer.append(_379);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_243);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_380);
    } else {
      if (iuReport == null) {
    stringBuffer.append(_381);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_380);
    }
      break;
    }
    }
    for (Map.Entry<String, String> entry : reporter.getNavigation().entrySet()) {
    String label = entry.getValue();
    int index = label.indexOf('@');
    if (index != -1)
      label = label.substring(0, label.length() - 1);
    {
    stringBuffer.append(_382);
    if (index != -1) {
    stringBuffer.append(_383);
    }
    stringBuffer.append(_384);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_243);
    stringBuffer.append(NL_9);
    stringBuffer.append(label);
    stringBuffer.append(_385);
    }
    }
    stringBuffer.append(_386);
    if (iuReport == null) {
    stringBuffer.append(_387);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_58);
    } else {
    stringBuffer.append(_388);
    stringBuffer.append(report.getIUImage(iuReport.getIU()));
    stringBuffer.append(_240);
    String name = report.getName(iuReport.getIU(), false);
    if (name != null) {
    stringBuffer.append(NL_9);
    stringBuffer.append(helper.htmlEscape(name, true));
    stringBuffer.append(_85);
    } else {
    stringBuffer.append(_389);
    }
    stringBuffer.append(NL_9);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_390);
    }
    stringBuffer.append(_391);
    if (report != null && report.getDate() != null) {
    stringBuffer.append(_392);
    stringBuffer.append(report.getDate());
    stringBuffer.append(_54);
    }
    stringBuffer.append(_393);
    stringBuffer.append(reporter.getNow());
    stringBuffer.append(_394);
    if (report != null) {
    stringBuffer.append(_395);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_396);
    }
    stringBuffer.append(_397);
    stringBuffer.append(reporter.getReportBrandingImage());
    stringBuffer.append(_231);
    if (indexReport != null) {
    stringBuffer.append(_398);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_239);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_51);
    Map<String, String> allReports = indexReport.getAllReports();
    if (allReports != null && !allReports.isEmpty()) {
    stringBuffer.append(_399);
    for (Map.Entry<String, String> entry : allReports.entrySet()) {
    stringBuffer.append(_400);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_243);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_52);
    }
    stringBuffer.append(_401);
    }
    } else if (iuReport != null) {
    stringBuffer.append(_402);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_239);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_51);
    String provider = iuReport.getProvider();
    if (provider != null) {
    stringBuffer.append(_403);
    stringBuffer.append(helper.htmlEscape(provider, false));
    stringBuffer.append(_66);
    }
    String description = iuReport.getDescription();
    if (description != null) {
    stringBuffer.append(_404);
    stringBuffer.append(helper.htmlEscape(description, false));
    stringBuffer.append(_66);
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iuReport.getIU());
    if (!artifacts.isEmpty()) {
      List<Certificate> certificates = report.getCertificates(iuReport.getIU());
      if (certificates != null) {
    stringBuffer.append(_405);
    if (certificates.isEmpty()) {
    stringBuffer.append(_406);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_407);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_408);
    stringBuffer.append(count++);
    stringBuffer.append(_409);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_240);
    for (Map.Entry<String, String> component : components.entrySet()) {
              String key = component.getKey();
              String value = component.getValue();
    stringBuffer.append(_410);
    stringBuffer.append(key);
    stringBuffer.append(_214);
    stringBuffer.append(value);
    stringBuffer.append(_70);
    }
    stringBuffer.append(_411);
    }
    }
    }
    stringBuffer.append(_412);
    Collection<IInstallableUnit> pluginsWithMissingPackGZ = report.getPluginsWithMissingPackGZ();
      for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
        String artifact = entry.getKey();
        Boolean signed = entry.getValue();
    stringBuffer.append(_413);
    if (signed != null) {
    stringBuffer.append(_414);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_240);
    }
    stringBuffer.append(_414);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_415);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_416);
    stringBuffer.append(report.getSiteURL() + '/' + artifact);
    stringBuffer.append(_243);
    stringBuffer.append(artifact);
    stringBuffer.append(_417);
    }
    if (pluginsWithMissingPackGZ.contains(iuReport.getIU())) {
    stringBuffer.append(_418);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_419);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_420);
    }
    }
    List<Report.LicenseDetail> licenses = report.getLicenses(iuReport.getIU());
    if (licenses != null) {
    stringBuffer.append(_421);
    for (LicenseDetail license : licenses) {
    String id = license.getUUID();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
    stringBuffer.append(_422);
    stringBuffer.append(id);
    stringBuffer.append(_249);
    stringBuffer.append(id);
    stringBuffer.append(_423);
    stringBuffer.append(report.getLicenseImage());
    stringBuffer.append(_424);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_243);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_70);
    stringBuffer.append(NL_12);
    stringBuffer.append(id);
    stringBuffer.append(_425);
    stringBuffer.append(id);
    stringBuffer.append(_237);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_243);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_192);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_243);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_71);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_243);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_70);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_67);
    }
    }
    stringBuffer.append(_426);
    for (String xml : report.getXML(iuReport.getIU(), Collections.<String, String> emptyMap())) {
    stringBuffer.append(_427);
    stringBuffer.append(xml);
    }
    stringBuffer.append(_428);
    } else {
    if (report.isRoot()) {
    stringBuffer.append(_429);
    }
    stringBuffer.append(_402);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_239);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_51);
    List<Report> children = report.getChildren();
    if (!children.isEmpty()) {
    stringBuffer.append(_413);
    if (!report.isRoot()) {
    stringBuffer.append(_430);
    }
    stringBuffer.append(_431);
    }
    String metadataXML = report.getMetadataXML();
    String artifactXML = report.getArtifactML();
    if (metadataXML != null || artifactXML != null) {
    stringBuffer.append(_432);
    stringBuffer.append(report.getRepositoryImage());
    stringBuffer.append(_433);
    if (metadataXML != null && metadataXML.contains("bad-absolute-location") || artifactXML != null && artifactXML.contains("bad-absolute-location")) {
    stringBuffer.append(_434);
    }
    stringBuffer.append(_435);
    if (metadataXML != null) {
    stringBuffer.append(_427);
    stringBuffer.append(metadataXML);
    }
    if (artifactXML != null) {
    if (metadataXML != null) {
    stringBuffer.append(NL);
    }
    stringBuffer.append(_427);
    stringBuffer.append(artifactXML);
    }
    stringBuffer.append(_428);
    }
    Map<LicenseDetail, Set<IInstallableUnit>> licenses = report.getLicenses();
    if (!licenses.isEmpty()) {
      String display = "block";
      String displayButton = "inline";
      int nonConformant = 0;
      for (LicenseDetail license : licenses.keySet()) {
        if (!license.isSUA())
          ++nonConformant;
      }
      String licenseImage = report.getLicenseImage();
    stringBuffer.append(_436);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_437);
    stringBuffer.append(licenses.size());
    stringBuffer.append(_70);
    if (nonConformant != 0) {
    stringBuffer.append(_438);
    stringBuffer.append(nonConformant);
    stringBuffer.append(_2);
    }
    {
        StringBuilder onClick = new StringBuilder();
        for (LicenseDetail license : licenses.keySet()) {
          String id = license.getUUID();
          onClick.append("expand2('licenses_all_arrows', '").append("_" + id).append("');");
          onClick.append("expand2('licenses_all_arrows', '").append("__" + id).append("');");
          onClick.append("expand3('licenses_all_arrows', '").append("_f" + id).append("');");
        }
    stringBuffer.append(_439);
    stringBuffer.append(displayButton);
    stringBuffer.append(_44);
    stringBuffer.append(onClick);
    stringBuffer.append(_244);
    }
    stringBuffer.append(_440);
    stringBuffer.append(display);
    stringBuffer.append(_40);
    for (Map.Entry<LicenseDetail, Set<IInstallableUnit>> entry : licenses.entrySet()) {
        LicenseDetail license = entry.getKey();
        String id = license.getUUID();
        Set<IInstallableUnit> ius = entry.getValue();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
        {
    stringBuffer.append(_441);
    stringBuffer.append(id);
    stringBuffer.append(_252);
    stringBuffer.append(id);
    stringBuffer.append(_442);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_424);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_243);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_443);
    stringBuffer.append(ius.size());
    stringBuffer.append(_444);
    stringBuffer.append(id);
    stringBuffer.append(_445);
    stringBuffer.append(id);
    stringBuffer.append(_251);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    stringBuffer.append(NL_15);
    stringBuffer.append(id);
    stringBuffer.append(_446);
    stringBuffer.append(id);
    stringBuffer.append(_237);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_243);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_192);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_243);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_71);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_243);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_70);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_447);
    stringBuffer.append(id);
    stringBuffer.append(_253);
    stringBuffer.append(id);
    stringBuffer.append(_448);
    stringBuffer.append(id);
    stringBuffer.append(_243);
    for (IInstallableUnit iu : ius) {
    stringBuffer.append(_449);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_450);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_240);
    stringBuffer.append(NL_19);
    stringBuffer.append(helper.htmlEscape(report.getName(iu, true), true));
    stringBuffer.append(_451);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_452);
    }
    stringBuffer.append(_453);
    }
    }
    stringBuffer.append(_401);
    }
    Map<List<Certificate>, Map<String, IInstallableUnit>> allCertificates = report.getCertificates();
    if (!allCertificates.isEmpty()) {
      int idCount = 0;
      Map<String, IInstallableUnit> unsigned = allCertificates.get(Collections.emptyList());
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_454);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_455);
    stringBuffer.append(allCertificates.size());
    stringBuffer.append(_70);
    if (unsigned != null) {
    stringBuffer.append(_438);
    stringBuffer.append(unsigned.size());
    stringBuffer.append(_6);
    }
    {
        StringBuilder onClick = new StringBuilder();
        for (int i = 0, size = allCertificates.size(); i < size; ++i) {
          String id = "certificates" + ++idCount;
          onClick.append("expand2('certificates_all_arrows', '").append(id).append("');");
        }
        idCount = 0;
    stringBuffer.append(_456);
    stringBuffer.append(displayButton);
    stringBuffer.append(_43);
    stringBuffer.append(onClick);
    stringBuffer.append(_244);
    }
    stringBuffer.append(_457);
    stringBuffer.append(display);
    stringBuffer.append(_41);
    for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : allCertificates.entrySet()) {
        List<Certificate> certificates = entry.getKey();
        String id = "certificates" + ++idCount;
    stringBuffer.append(_458);
    if (certificates.isEmpty()) {
    stringBuffer.append(_459);
    stringBuffer.append(id);
    stringBuffer.append(_250);
    stringBuffer.append(id);
    stringBuffer.append(_442);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_460);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_461);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_408);
    stringBuffer.append(count++ + 2);
    stringBuffer.append(_283);
    if (count == 1) {
    stringBuffer.append(_462);
    stringBuffer.append(id);
    stringBuffer.append(_250);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    } else {
    stringBuffer.append(_463);
    }
    stringBuffer.append(_464);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_240);
    if (count == 1) {
    stringBuffer.append(_465);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_19);
    }
    for (Map.Entry<String, String> component : components.entrySet()) {
              String key = component.getKey();
              String value = component.getValue();
    stringBuffer.append(_410);
    stringBuffer.append(key);
    stringBuffer.append(_214);
    stringBuffer.append(value);
    stringBuffer.append(_70);
    }
    stringBuffer.append(_411);
    }
    }
    stringBuffer.append(_466);
    stringBuffer.append(id);
    stringBuffer.append(_243);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_467);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_245);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_241);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_468);
    }
    stringBuffer.append(_469);
    }
    stringBuffer.append(_401);
    }
    stringBuffer.append(_470);
    Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
    if (!featureProviders.isEmpty()) {
      int nonEclipse = 0;
      for (String provider : featureProviders.keySet()) {
        if (!provider.toLowerCase().contains("eclipse"))
          ++nonEclipse;
      }
    stringBuffer.append(_471);
    stringBuffer.append(report.getProviderImage());
    stringBuffer.append(_472);
    stringBuffer.append(featureProviders.size());
    stringBuffer.append(_70);
    if (nonEclipse != 0) {
    stringBuffer.append(_438);
    stringBuffer.append(nonEclipse);
    stringBuffer.append(_5);
    }
    stringBuffer.append(_473);
    int count = 0;
      for (Map.Entry<String, Set<IInstallableUnit>> provider : featureProviders.entrySet()) {
        String style = !provider.getKey().toLowerCase().contains("eclipse") ? " style=\"color: FireBrick;\"" : "";
        Set<IInstallableUnit> features = provider.getValue();
        String id = "nested_feature_providers" + ++count;
    stringBuffer.append(_474);
    stringBuffer.append(id);
    stringBuffer.append(_248);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    for (String image : report.getBrandingImages(features)) {
    stringBuffer.append(_475);
    stringBuffer.append(image);
    stringBuffer.append(_240);
    }
    stringBuffer.append(_476);
    stringBuffer.append(style);
    stringBuffer.append(_215);
    stringBuffer.append(provider.getKey());
    stringBuffer.append(_477);
    stringBuffer.append(features.size());
    stringBuffer.append(_478);
    stringBuffer.append(id);
    stringBuffer.append(_236);
    for (IInstallableUnit feature : features) {
          String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_479);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_480);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_240);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_481);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_482);
    }
    stringBuffer.append(_483);
    }
    stringBuffer.append(_401);
    }
    List<IInstallableUnit> features = report.getFeatureIUs();
    if (!features.isEmpty()) {
      int brokenBranding = 0;
      int noBranding = 0;
      for (IInstallableUnit feature : features) {
        if (report.hasBrokenBrandingImage(feature))
          ++brokenBranding;
        else if (!report.hasBrandingImage(feature))
          ++noBranding;
      }
    stringBuffer.append(_484);
    stringBuffer.append(report.getFeatureImage());
    stringBuffer.append(_485);
    stringBuffer.append(features.size());
    stringBuffer.append(_70);
    if (brokenBranding != 0) {
    stringBuffer.append(_438);
    stringBuffer.append(brokenBranding);
    stringBuffer.append(_1);
    }
    if (noBranding != 0) {
    stringBuffer.append(_438);
    stringBuffer.append(noBranding);
    stringBuffer.append(_4);
    }
    stringBuffer.append(_486);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_487);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_488);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_240);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_489);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_490);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        for (LicenseDetail license : report.getLicenses(feature)) {
          String id = license.getUUID();
          licenseReplacements.put(">" + id, "><button class='bb search-for-me' style='" + helper.getStyle(license) + "' onclick=\"clickOnButton('lic_" + id
              + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_491);
    stringBuffer.append(id);
    stringBuffer.append(_232);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_234);
    stringBuffer.append(id);
    stringBuffer.append(_246);
    stringBuffer.append(id);
    stringBuffer.append(_247);
    stringBuffer.append(id);
    stringBuffer.append(_13);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_56);
    }
    stringBuffer.append(_492);
    }
    stringBuffer.append(_401);
    }
    Collection<IInstallableUnit> products = report.getProducts();
    if (!products.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_493);
    stringBuffer.append(report.getProductImage());
    stringBuffer.append(_494);
    stringBuffer.append(products.size());
    stringBuffer.append(_70);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit product : products) {
          String productID = "_product_" + report.getIUID(product);
          onClick.append("expand2('products_all_arrows', '").append(productID).append("');");
        }
    stringBuffer.append(_495);
    stringBuffer.append(displayButton);
    stringBuffer.append(_45);
    stringBuffer.append(onClick);
    stringBuffer.append(_244);
    }
    stringBuffer.append(_496);
    stringBuffer.append(display);
    stringBuffer.append(_40);
    for (IInstallableUnit product : products) {
        String productImage = report.getIUImage(product);
        String productID = "_product_" + report.getIUID(product);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(product));
    stringBuffer.append(_497);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_462);
    stringBuffer.append(productID);
    stringBuffer.append(_250);
    stringBuffer.append(productID);
    stringBuffer.append(_14);
    }
    stringBuffer.append(_498);
    stringBuffer.append(report.getRelativeIUReportURL(product));
    stringBuffer.append(_488);
    stringBuffer.append(productImage);
    stringBuffer.append(_240);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(product, true), true));
    stringBuffer.append(_489);
    stringBuffer.append(report.getVersion(product));
    stringBuffer.append(_490);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_499);
    stringBuffer.append(productID);
    stringBuffer.append(_235);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_479);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_500);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_240);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_481);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_482);
    }
    stringBuffer.append(_501);
    }
    stringBuffer.append(_492);
    }
    stringBuffer.append(_401);
    }
    Collection<IInstallableUnit> categories = report.getCategories();
    if (!categories.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_502);
    stringBuffer.append(report.getCategoryImage());
    stringBuffer.append(_503);
    stringBuffer.append(categories.size());
    stringBuffer.append(_70);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit category : categories) {
          String categoryID = "_category_" + report.getIUID(category);
          onClick.append("expand2('categories_all_arrows', '").append(categoryID).append("');");
        }
    stringBuffer.append(_504);
    stringBuffer.append(displayButton);
    stringBuffer.append(_42);
    stringBuffer.append(onClick);
    stringBuffer.append(_244);
    }
    stringBuffer.append(_505);
    stringBuffer.append(display);
    stringBuffer.append(_40);
    for (IInstallableUnit category : categories) {
        String categoryImage = report.getIUImage(category);
        String categoryID = "_category_" + report.getIUID(category);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(category));
    stringBuffer.append(_497);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_462);
    stringBuffer.append(categoryID);
    stringBuffer.append(_250);
    stringBuffer.append(categoryID);
    stringBuffer.append(_14);
    }
    stringBuffer.append(_498);
    stringBuffer.append(report.getRelativeIUReportURL(category));
    stringBuffer.append(_488);
    stringBuffer.append(categoryImage);
    stringBuffer.append(_240);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(category, true), true));
    stringBuffer.append(_506);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_499);
    stringBuffer.append(categoryID);
    stringBuffer.append(_238);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_479);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_500);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_240);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_481);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_482);
    }
    stringBuffer.append(_501);
    }
    stringBuffer.append(_492);
    }
    stringBuffer.append(_401);
    }
    Collection<IInstallableUnit> ius = report.getAllIUs();
    if (!ius.isEmpty()) {
      Collection<IInstallableUnit> pluginsWithMissingPackGZ = report.getPluginsWithMissingPackGZ();
      Set<IInstallableUnit> unsignedIUs = report.getUnsignedIUs();
      Set<IInstallableUnit> badProviderIUs = report.getBadProviderIUs();
      Set<IInstallableUnit> badLicenseIUs = report.getBadLicenseIUs();
      Set<IInstallableUnit> brokenBrandingIUs = report.getBrokenBrandingIUs();
      Map<String, Set<Version>> iuVersions = report.getIUVersions();
      boolean isSimple = report.isSimple();
      int duplicateCount = 0;
      if (isSimple) {
        for (Map.Entry<String, Set<Version>> entry : iuVersions.entrySet()) {
          if (entry.getValue().size() > 1 && !report.isDuplicationExpected(entry.getKey())) {
            ++duplicateCount;
          }
        }
      }
    stringBuffer.append(_507);
    stringBuffer.append(report.getBundleImage());
    stringBuffer.append(_508);
    stringBuffer.append(ius.size());
    stringBuffer.append(_70);
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_438);
    stringBuffer.append(pluginsWithMissingPackGZ.size());
    stringBuffer.append(_3);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_438);
    stringBuffer.append(duplicateCount);
    stringBuffer.append(_8);
    }
    stringBuffer.append(_509);
    if (duplicateCount > 0 || !pluginsWithMissingPackGZ.isEmpty() || !unsignedIUs.isEmpty() || !badProviderIUs.isEmpty() || !badLicenseIUs.isEmpty()
          || !brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_510);
    if (!badLicenseIUs.isEmpty()) {
    stringBuffer.append(_511);
    }
    if (!unsignedIUs.isEmpty()) {
    stringBuffer.append(_512);
    }
    if (!badProviderIUs.isEmpty()) {
    stringBuffer.append(_513);
    }
    if (!brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_514);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_515);
    }
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_516);
    }
    stringBuffer.append(_517);
    }
    stringBuffer.append(_518);
    for (IInstallableUnit iu : ius) {
        String iuID = iu.getId();
        String id = report.getIUID(iu);
        boolean duplicateVersions = isSimple && !report.isDuplicationExpected(iu.getId()) && iuVersions.get(iu.getId()).size() > 1;
        String versionStyle = duplicateVersions ? " font-weight: bold;" : "";
        StringBuilder classNames = new StringBuilder();
        if (duplicateVersions) {
          classNames.append(" duplicate");
        }
        if (pluginsWithMissingPackGZ.contains(iu)) {
          classNames.append(" missing");
        }
        if (unsignedIUs.contains(iu)) {
          classNames.append(" unsigned");
        }
        if (badProviderIUs.contains(iu)) {
          classNames.append(" bad-provider");
        }
        if (badLicenseIUs.contains(iu)) {
          classNames.append(" bad-license");
        }
        if (brokenBrandingIUs.contains(iu)) {
          classNames.append(" broken-branding");
        }
    stringBuffer.append(_519);
    stringBuffer.append(id);
    stringBuffer.append(_233);
    stringBuffer.append(classNames);
    stringBuffer.append(_520);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_521);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_522);
    stringBuffer.append(iuID);
    stringBuffer.append(_523);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_243);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_524);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        if (report.isFeature(iu)) {
          for (LicenseDetail license : report.getLicenses(iu)) {
            String licenseID = license.getUUID();
            licenseReplacements.put(">" + licenseID, "><button class='bb search-for-me' style='" + helper.getStyle(license)
                + "' onclick=\"clickOnButton('lic_" + licenseID + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_525);
    stringBuffer.append(licenseID);
    stringBuffer.append(_232);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_234);
    stringBuffer.append(licenseID);
    stringBuffer.append(_246);
    stringBuffer.append(licenseID);
    stringBuffer.append(_247);
    stringBuffer.append(licenseID);
    stringBuffer.append(_13);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_56);
    }
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iu);
        for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
          String artifact = entry.getKey();
          Boolean signed = entry.getValue();
    stringBuffer.append(_526);
    if (signed != null) {
    stringBuffer.append(_527);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_240);
    }
    stringBuffer.append(_527);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_528);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_529);
    }
    if (pluginsWithMissingPackGZ.contains(iu)) {
    stringBuffer.append(_530);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_531);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_532);
    }
    String brandingImage = report.getBrandingImage(iu);
        if (brandingImage != null) {
    stringBuffer.append(_533);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_242);
    }
    stringBuffer.append(_534);
    }
    stringBuffer.append(_535);
    }
    }
    stringBuffer.append(_536);
    return stringBuffer.toString();
  }
}
