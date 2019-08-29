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
  protected static final String _20 = "-";
  protected static final String _21 = ".bad-absolute-location {";
  protected static final String _22 = ".bb {";
  protected static final String _23 = ".filter {";
  protected static final String _24 = ".fit-image {";
  protected static final String _25 = ".font-smaller {";
  protected static final String _26 = ".iu-link {";
  protected static final String _27 = ".resolved-requirement {";
  protected static final String _28 = ".text-nowrap {";
  protected static final String _29 = ".unresolved-requirement {";
  protected static final String _30 = ".unused-capability {";
  protected static final String _31 = ".used-capability {";
  protected static final String _32 = ".xml-attribute {";
  protected static final String _33 = ".xml-attribute-value {";
  protected static final String _34 = ".xml-element {";
  protected static final String _35 = ".xml-element-value {";
  protected static final String _36 = ".xml-iu {";
  protected static final String _37 = ".xml-repo {";
  protected static final String _38 = ".xml-token {";
  protected static final String _39 = "; margin-left: -1em; list-style-type: none; padding: 0; margin: 0;\">";
  protected static final String _40 = "; margin-left: -1em; list-style-type: none;\">";
  protected static final String _41 = ";\" onclick=\"toggle('categories_all_arrows');";
  protected static final String _42 = ";\" onclick=\"toggle('certificates_all_arrows');";
  protected static final String _43 = ";\" onclick=\"toggle('licenses_all_arrows');";
  protected static final String _44 = ";\" onclick=\"toggle('products_all_arrows');";
  protected static final String _45 = "<!-- navigation sidebar -->";
  protected static final String _46 = "<!--- providers -->";
  protected static final String _47 = "<!----------->";
  protected static final String _48 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
  protected static final String _49 = "</a>";
  protected static final String _50 = "</a>.</p>";
  protected static final String _51 = "</a></li>";
  protected static final String _52 = "</aside>";
  protected static final String _53 = "</b>";
  protected static final String _54 = "</body>";
  protected static final String _55 = "</button>";
  protected static final String _56 = "</div>";
  protected static final String _57 = "</h2>";
  protected static final String _58 = "</h3>";
  protected static final String _59 = "</head>";
  protected static final String _60 = "</header>";
  protected static final String _61 = "</html>";
  protected static final String _62 = "</li>";
  protected static final String _63 = "</main>";
  protected static final String _64 = "</ol>";
  protected static final String _65 = "</p>";
  protected static final String _66 = "</pre>";
  protected static final String _67 = "</script>";
  protected static final String _68 = "</section>";
  protected static final String _69 = "</span>";
  protected static final String _70 = "</span><span style=\"color: green; text-decoration: underline; ";
  protected static final String _71 = "</style>";
  protected static final String _72 = "</title>";
  protected static final String _73 = "</ul>";
  protected static final String _74 = "<a class=\"separator\" href=\"";
  protected static final String _75 = "<a class=\"separator\" href=\".\">";
  protected static final String _76 = "<a class=\"separator\" href=\"https://www.eclipse.org/downloads\">Downloads</a>";
  protected static final String _77 = "<a class=\"separator\" href=\"https://www.eclipse.org\">Home</a>";
  protected static final String _78 = "<a class=\"separator\" style=\"display: inline-block; margin-left: 0.25em;\" href=\"";
  protected static final String _79 = "<a href=\"";
  protected static final String _80 = "<a href=\"https://www.eclipse.org/\">";
  protected static final String _81 = "<aside id=\"leftcol\" class=\"col-md-4 font-smaller\">";
  protected static final String _82 = "<b>Built: ";
  protected static final String _83 = "<body id=\"body_solstice\">";
  protected static final String _84 = "<br/>";
  protected static final String _85 = "<br/><b>Reported: ";
  protected static final String _86 = "<button id=\"";
  protected static final String _87 = "<button id=\"_";
  protected static final String _88 = "<button id=\"__";
  protected static final String _89 = "<button id=\"_f";
  protected static final String _90 = "<button id=\"categories_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _91 = "<button id=\"categories_arrows\" class=\"orange bb\" onclick=\"expand_collapse('categories'); expand_collapse_inline('categories_all_arrows');\">&#x25B7;</button>";
  protected static final String _92 = "<button id=\"certificates_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _93 = "<button id=\"certificates_arrows\" class=\"orange bb\" onclick=\"expand_collapse('certificates'); expand_collapse_inline('certificates_all_arrows');\">&#x25B7;</button>";
  protected static final String _94 = "<button id=\"feature_providers_arrows\" class=\"orange bb\" onclick=\"expand_collapse('feature_providers');\">&#x25B7;</button>";
  protected static final String _95 = "<button id=\"features_arrows\" class=\"orange bb\" onclick=\"expand_collapse('features');\">&#x25B7;</button>";
  protected static final String _96 = "<button id=\"ius_arrows\" class=\"orange bb\" onclick=\"expand_collapse('ius');\">&#x25B7;</button>";
  protected static final String _97 = "<button id=\"lic_";
  protected static final String _98 = "<button id=\"licenses_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _99 = "<button id=\"licenses_arrows\" class=\"orange bb\" onclick=\"expand_collapse('licenses'); expand_collapse_inline('licenses_all_arrows');\">&#x25B7;</button>";
  protected static final String _100 = "<button id=\"products_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _101 = "<button id=\"products_arrows\" class=\"orange bb\" onclick=\"expand_collapse('products'); expand_collapse_inline('products_all_arrows');\">&#x25B7;</button>";
  protected static final String _102 = "<button id=\"repos_arrows\" class=\"orange bb\" onclick=\"expand_collapse('repos');\">&#x25B7;</button>";
  protected static final String _103 = "<button id=\"xml-links\" class=\"orange bb\" onclick=\"expand_collapse_all('xml-links');\">&#x25B7;</button>";
  protected static final String _104 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 150%;\" onclick=\"copyToClipboard('#p1')\">&#x270e;</button>";
  protected static final String _105 = "<div class=\"col-sm-16 padding-left-30\">";
  protected static final String _106 = "<div class=\"container\">";
  protected static final String _107 = "<div class=\"font-smaller\" style=\"margin-left: ";
  protected static final String _108 = "<div class=\"hidden-xs col-sm-8 col-md-6 col-lg-5\" id=\"header-left\">";
  protected static final String _109 = "<div class=\"novaContent container\" id=\"novaContent\">";
  protected static final String _110 = "<div class=\"row\" id=\"header-row\">";
  protected static final String _111 = "<div class=\"row\">";
  protected static final String _112 = "<div class=\"wrapper-logo-default\">";
  protected static final String _113 = "<div class=\"xml-iu\" style=\"display:block;\">";
  protected static final String _114 = "<div class=\"xml-repo\" id=\"repos\" style=\"display: none;\">";
  protected static final String _115 = "<div id=\"ius\" style=\"display:none;\">";
  protected static final String _116 = "<div id=\"maincontent\">";
  protected static final String _117 = "<div id=\"midcolumn\" style=\"width: 70%;\">";
  protected static final String _118 = "<div style=\"text-indent: -2em\">";
  protected static final String _119 = "<div>";
  protected static final String _120 = "<h2 style=\"text-align: center;\">";
  protected static final String _121 = "<h3 class=\"sr-only\">Breadcrumbs</h3>";
  protected static final String _122 = "<h3 style=\"font-weight: bold;\">";
  protected static final String _123 = "<h3 style=\"font-weight: bold;\">Artifacts</h3>";
  protected static final String _124 = "<h3 style=\"font-weight: bold;\">Certificate</h3>";
  protected static final String _125 = "<h3 style=\"font-weight: bold;\">Description</h3>";
  protected static final String _126 = "<h3 style=\"font-weight: bold;\">Licenses</h3>";
  protected static final String _127 = "<h3 style=\"font-weight: bold;\">Provider</h3>";
  protected static final String _128 = "<head>";
  protected static final String _129 = "<header role=\"banner\" id=\"header-wrapper\">";
  protected static final String _130 = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">";
  protected static final String _131 = "<img alt=\"Branding Image\" class=\"fit-image\" src=\"";
  protected static final String _132 = "<img alt=\"Signed\" class=\"fit-image\" src=\"";
  protected static final String _133 = "<img alt=\"Signed\" class=\"fit-image\" src=\"../";
  protected static final String _134 = "<img class=\"fit-image> src=\"";
  protected static final String _135 = "<img class=\"fit-image\" src=\"";
  protected static final String _136 = "<img class=\"fit-image\" src=\"../";
  protected static final String _137 = "<img class=\"logo-eclipse-default img-responsive hidden-xs\" alt=\"Eclipse Log\" src=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/logo/eclipse-426x100.png\"/>";
  protected static final String _138 = "<img style=\"float:right\" src=\"";
  protected static final String _139 = "<input id=\"duplicates\" type=\"radio\" name=\"filter\" value=\"duplicate\" class=\"filter\" onclick=\"filterIU('duplicate');\"> Duplicates </input>";
  protected static final String _140 = "<input id=\"filter\" type=\"radio\" name=\"filter\" value=\"all\" class=\"filter\" onclick=\"filterIU('iu-li');\" checked=\"checked\"> All </input>";
  protected static final String _141 = "<input id=\"missing\" type=\"radio\" name=\"filter\" value=\"missing\" class=\"filter\" onclick=\"filterIU('missing');\"> Missing .pack.gz </input>";
  protected static final String _142 = "<li class=\"active\">";
  protected static final String _143 = "<li class=\"font-smaller text-nowrap\">";
  protected static final String _144 = "<li class=\"separator\" style=\"font-size: 90%;\">";
  protected static final String _145 = "<li class=\"separator\">";
  protected static final String _146 = "<li id=\"_iu_";
  protected static final String _147 = "<li style=\"font-size: 100%; white-space: nowrap;\">";
  protected static final String _148 = "<li style=\"margin-left: 1em;\">";
  protected static final String _149 = "<li>";
  protected static final String _150 = "<li><a href=\"";
  protected static final String _151 = "<li><a href=\"https://www.eclipse.org/\">Home</a></li>";
  protected static final String _152 = "<li><a href=\"https://www.eclipse.org/downloads/\">Downloads</a></li>";
  protected static final String _153 = "<link href=\"//fonts.googleapis.com/css?family=Open+Sans:400,700,300,600,100\" rel=\"stylesheet\" type=\"text/css\"/>";
  protected static final String _154 = "<link rel=\"icon\" type=\"image/ico\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/favicon.ico\"/>";
  protected static final String _155 = "<link rel=\"stylesheet\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/stylesheets/styles.min.css\"/>";
  protected static final String _156 = "<main class=\"no-promo\">";
  protected static final String _157 = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
  protected static final String _158 = "<meta name=\"description\" content=\"Update Sites Reports\"/>";
  protected static final String _159 = "<meta name=\"keywords\" content=\"eclipse,update site\"/>";
  protected static final String _160 = "<ol class=\"breadcrumb\">";
  protected static final String _161 = "<p style=\"font-size: 125%; text-align: center;\">";
  protected static final String _162 = "<p style=\"text-align: center;\">";
  protected static final String _163 = "<p>";
  protected static final String _164 = "<p></p>";
  protected static final String _165 = "<p>Reports are generated specifically for the following sites:</p>";
  protected static final String _166 = "<p>This is a generated report folder index.</p>";
  protected static final String _167 = "<p>This is the generated report index.</p>";
  protected static final String _168 = "<p>This report is produced by <a href=\"";
  protected static final String _169 = "<pre id=\"_";
  protected static final String _170 = "<pre id=\"__";
  protected static final String _171 = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";
  protected static final String _172 = "<script>";
  protected static final String _173 = "<section class=\"hidden-print default-breadcrumbs\" id=\"breadcrumb\">";
  protected static final String _174 = "<span ";
  protected static final String _175 = "<span class=\"text-nowrap\"><span style=\"color: SteelBlue; font-size: 75%;\">";
  protected static final String _176 = "<span id=\"p1\" style=\"font-size: 125%\">";
  protected static final String _177 = "<span style=\"";
  protected static final String _178 = "<span style=\"color: DarkCyan; font-size: 110%;\">(";
  protected static final String _179 = "<span style=\"color: DarkCyan;\">";
  protected static final String _180 = "<span style=\"color: DarkOliveGreen;";
  protected static final String _181 = "<span style=\"color: DarkOliveGreen;\">";
  protected static final String _182 = "<span style=\"color: FireBrick; font-size: 60%;\">(";
  protected static final String _183 = "<span style=\"color: FireBrick; font-size: 60%;\">(Inappropriate absolute location)</span>";
  protected static final String _184 = "<span style=\"color: FireBrick;\">(";
  protected static final String _185 = "<span style=\"color: FireBrick;\">Unsigned</span>";
  protected static final String _186 = "<span style=\"color: red; text-decoration: line-through; ";
  protected static final String _187 = "<span style=\"font-size:100%;\">";
  protected static final String _188 = "<span style=\"margin-left: 1em;\">";
  protected static final String _189 = "<style>";
  protected static final String _190 = "<title>";
  protected static final String _191 = "<tt style=\"float: left;\" class=\"orange\">&#xbb;</tt>";
  protected static final String _192 = "<tt style=\"float: right;\" class=\"orange\">&#xab;</tt>";
  protected static final String _193 = "<ul class=\"font-smaller\" id=\"";
  protected static final String _194 = "<ul class=\"font-smaller\" style=\"list-style-type: none; display:none; margin-left: -3em;\" id=\"";
  protected static final String _195 = "<ul id=\"";
  protected static final String _196 = "<ul id=\"categories\" style=\"display: ";
  protected static final String _197 = "<ul id=\"certificates\" style=\"display:";
  protected static final String _198 = "<ul id=\"feature_providers\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _199 = "<ul id=\"features\" style=\"display: none; list-style-type: none; margin-left: -2em;\">";
  protected static final String _200 = "<ul id=\"leftnav\" class=\"ul-left-nav fa-ul hidden-print\">";
  protected static final String _201 = "<ul id=\"licenses\" style=\"display: ";
  protected static final String _202 = "<ul id=\"products\" style=\"display: ";
  protected static final String _203 = "<ul style=\"display:none; list-style-type: none; padding: 0; margin: 0;\" id=\"__";
  protected static final String _204 = "<ul style=\"list-style-type: none; display:none; padding: 0; margin: 0; margin-left: 2em;\" id=\"_f";
  protected static final String _205 = "<ul style=\"list-style-type: none; padding: 0; margin-left: 1em;\">";
  protected static final String _206 = "<ul>";
  protected static final String _207 = "=</span>";
  protected static final String _208 = ">";
  protected static final String _209 = "Categories";
  protected static final String _210 = "Content Metadata";
  protected static final String _211 = "Features";
  protected static final String _212 = "Features/Products";
  protected static final String _213 = "In addition to this composite report, reports are also generated for each of the composed children listed in the navigation bar to the left.";
  protected static final String _214 = "Installable Units";
  protected static final String _215 = "Licenses";
  protected static final String _216 = "Missing";
  protected static final String _217 = "No Name<br/>";
  protected static final String _218 = "Products";
  protected static final String _219 = "Providers";
  protected static final String _220 = "Signing Certificates";
  protected static final String _221 = "This is a composite update site.";
  protected static final String _222 = "XML";
  protected static final String _223 = "\" alt=\"\"/>";
  protected static final String _224 = "\" class=\"bb\" style=\"";
  protected static final String _225 = "\" class=\"iu-li";
  protected static final String _226 = "\" onclick=\"clickOnToggleButton('licenses_arrows'); clickOnToggleButton('__";
  protected static final String _227 = "\" style=\"display: none;\">";
  protected static final String _228 = "\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _229 = "\" style=\"display:none; margin-left: 2em; background-color: ";
  protected static final String _230 = "\" style=\"list-style-type: none; display: none; margin-left: -1em;\">";
  protected static final String _231 = "\" target=\"report_source\">";
  protected static final String _232 = "\"/>";
  protected static final String _233 = "\"/> ";
  protected static final String _234 = "\">";
  protected static final String _235 = "\">&#x25B7;</button>";
  protected static final String _236 = "\"><img class=\"fit-image\" src=\"";
  protected static final String _237 = "_arrows'); clickOnToggleButton('_";
  protected static final String _238 = "_arrows'); navigateTo('_";
  protected static final String _239 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('";
  protected static final String _240 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('__";
  protected static final String _241 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('";
  protected static final String _242 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_";
  protected static final String _243 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('__";
  protected static final String _244 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_f";
  protected static final String _245 = "background-color: white;";
  protected static final String _246 = "border: 1px solid black;";
  protected static final String _247 = "border: none;";
  protected static final String _248 = "buttonTargets.item(i).innerHTML = '&#x25B7;';";
  protected static final String _249 = "buttonTargets.item(i).innerHTML = '&#x25E2;';";
  protected static final String _250 = "color: DarkSlateGray;";
  protected static final String _251 = "color: FireBrick;";
  protected static final String _252 = "color: IndianRed;";
  protected static final String _253 = "color: MediumAquaMarine;";
  protected static final String _254 = "color: MediumOrchid;";
  protected static final String _255 = "color: SaddleBrown;";
  protected static final String _256 = "color: SeaGreen;";
  protected static final String _257 = "color: SteelBlue;";
  protected static final String _258 = "color: Teal;";
  protected static final String _259 = "document.execCommand(\"copy\");";
  protected static final String _260 = "e.click();";
  protected static final String _261 = "e.innerHTML = '&#x25B7;';";
  protected static final String _262 = "e.innerHTML = '&#x25E2;';";
  protected static final String _263 = "e.scrollIntoView();";
  protected static final String _264 = "e.style.display = 'block';";
  protected static final String _265 = "e.style.display = 'inline';";
  protected static final String _266 = "e.style.display = 'inline-block';";
  protected static final String _267 = "e.style.display = 'none';";
  protected static final String _268 = "e.title= 'Collapse All';";
  protected static final String _269 = "e.title= 'Expand All';";
  protected static final String _270 = "em; text-indent: -4em;\">";
  protected static final String _271 = "em;\">";
  protected static final String _272 = "f.innerHTML = '&#x25B7;';";
  protected static final String _273 = "f.innerHTML = '&#x25E2;';";
  protected static final String _274 = "font-family: monospace;";
  protected static final String _275 = "font-size: 125%;";
  protected static final String _276 = "font-size: 80%;";
  protected static final String _277 = "font-size: 90%;";
  protected static final String _278 = "for (var i = 0; i < buttonTargets.length; i++) {";
  protected static final String _279 = "for (var i = 0; i < ius.length; i++) {";
  protected static final String _280 = "for (var i = 0; i < spanTargets.length; i++) {";
  protected static final String _281 = "function clickOnButton(id) {";
  protected static final String _282 = "function clickOnToggleButton(id) {";
  protected static final String _283 = "function copyToClipboard(element) {";
  protected static final String _284 = "function expand(id) {";
  protected static final String _285 = "function expand2(self, id) {";
  protected static final String _286 = "function expand3(self, id) {";
  protected static final String _287 = "function expand_collapse(id) {";
  protected static final String _288 = "function expand_collapse_all(base) {";
  protected static final String _289 = "function expand_collapse_inline(id) {";
  protected static final String _290 = "function expand_collapse_inline_block(id) {";
  protected static final String _291 = "function filterIU(className) {";
  protected static final String _292 = "function navigateTo(id) {";
  protected static final String _293 = "function toggle(id) {";
  protected static final String _294 = "height: 2ex;";
  protected static final String _295 = "if (!targetsArray.includes(iu)) {";
  protected static final String _296 = "if (e.innerHTML == '";
  protected static final String _297 = "if (e.innerHTML == '\\u25E2') {";
  protected static final String _298 = "if (e.style.display == 'none'){";
  protected static final String _299 = "if (e.title == 'Expand All') {";
  protected static final String _300 = "if (f != null) {";
  protected static final String _301 = "if (f !=null) {";
  protected static final String _302 = "if (t.title != 'Collapse All'){";
  protected static final String _303 = "if (t.title == 'Collapse All'){";
  protected static final String _304 = "iu.style.display = 'block';";
  protected static final String _305 = "iu.style.display = 'none';";
  protected static final String _306 = "margin-bottom: -2ex;";
  protected static final String _307 = "margin-left: 0em;";
  protected static final String _308 = "margin-top: -2ex;";
  protected static final String _309 = "margin: 0px 0px 0px 0px;";
  protected static final String _310 = "padding: -2pt -2pt -2pt -2pt;";
  protected static final String _311 = "padding: 0px 0px;";
  protected static final String _312 = "span:target {";
  protected static final String _313 = "spanTargets.item(i).style.display = 'inline-block';";
  protected static final String _314 = "spanTargets.item(i).style.display = 'none';";
  protected static final String _315 = "var $temp = $(\"<input>\");";
  protected static final String _316 = "var buttonTargets = document.getElementsByClassName(base + '_button');";
  protected static final String _317 = "var e = document.getElementById(base);";
  protected static final String _318 = "var e = document.getElementById(id);";
  protected static final String _319 = "var f = document.getElementById(id+\"_arrows\");";
  protected static final String _320 = "var iu = ius[i];";
  protected static final String _321 = "var ius = document.getElementsByClassName('iu-li');";
  protected static final String _322 = "var spanTargets = document.getElementsByClassName(base);";
  protected static final String _323 = "var state = e.innerHTML;";
  protected static final String _324 = "var t = document.getElementById('all');";
  protected static final String _325 = "var t = document.getElementById(self);";
  protected static final String _326 = "var targets = document.getElementsByClassName(className);";
  protected static final String _327 = "var targetsArray = [].slice.call(targets);";
  protected static final String _328 = "white-space: nowrap;";
  protected static final String _329 = "white-space: pre;";
  protected static final String _330 = "width: 2ex;";
  protected static final String _331 = "}";
  protected static final String _332 = "} else {";
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
  protected final String _333 = _48 + NL + _130 + NL + _128 + NL_1 + _157 + NL_1 + _190;
  protected final String _334 = _72 + NL_1 + _159 + NL_1 + _158 + NL_1 + _153 + NL_1 + _155 + NL_1 + _154 + NL_1 + _171;
  protected final String _335 = NL_1 + _189 + NL + NL + _28 + NL_1 + _328 + NL + _331 + NL + NL + _25 + NL_1 + _277 + NL + _331 + NL + NL + _24 + NL_1 + _330 + NL_1 + _294 + NL + _331 + NL + NL + _312 + NL_1 + _275 + NL_1 + _246 + NL + _331 + NL + NL + _37 + NL_1 + _329 + NL_1 + _247 + NL_1 + _311 + NL_1 + _308 + NL_1 + _306 + NL_1 + _307 + NL + _331 + NL + NL + _21 + NL_1 + _251 + NL_1 + _277 + NL + _331 + NL + NL + _36 + NL_1 + _329 + NL_1 + _247 + NL_1 + _311 + NL_1 + _308 + NL_1 + _306 + NL_1 + _307 + NL + _331 + NL + NL + _38 + NL_1 + _257 + NL_1 + _274 + NL_1 + _276 + NL + _331 + NL + NL + _32 + NL_1 + _253 + NL_1 + _274 + NL_1 + _276 + NL + _331 + NL + NL + _34 + NL_1 + _254 + NL_1 + _274 + NL_1 + _276 + NL + _331 + NL + NL + _33 + NL_1 + _250 + NL_1 + _277 + NL + _331 + NL + NL + _35 + NL_1 + _255 + NL_1 + _277 + NL + _331 + NL + NL + _22 + NL_1 + _245 + NL_1 + _247 + NL_1 + _311 + NL + _331 + NL + NL + _23 + NL_1 + _245 + NL + _331 + NL + NL + _26 + NL_1 + _310 + NL_1 + _309 + NL + _331 + NL + NL + _27 + NL_1 + _256 + NL_1 + _277 + NL + _331 + NL + NL + _31 + NL_1 + _258 + NL_1 + _277 + NL + _331 + NL + NL + _30 + NL_1 + _277 + NL + _331 + NL + NL + _29 + NL_1 + _252 + NL_1 + _277 + NL + _331 + NL_1 + _71 + NL + _59 + NL + NL_1 + _83;
  protected final String _336 = NL_2 + _172 + NL_4 + _291 + NL_5 + _321 + NL_5 + _326 + NL_5 + _327 + NL_5 + _279 + NL_7 + _320 + NL_7 + _295 + NL_9 + _305 + NL_7 + _332 + NL_9 + _304 + NL_7 + _331 + NL_5 + _331 + NL_4 + _331 + NL + NL_4 + _283 + NL_5 + _315 + NL_5 + _9 + NL_5 + _11 + NL_5 + _259 + NL_5 + _10 + NL_4 + _331 + NL + NL_4 + _281 + NL_5 + _318 + NL_5 + _260 + NL_4 + _331 + NL + NL_4 + _282 + NL_5 + _318 + NL_5 + _323 + NL_5 + _296;
  protected final String _337 = _15 + NL_7 + _260 + NL_5 + _331 + NL_4 + _331 + NL + NL_4 + _292 + NL_5 + _318 + NL_5 + _263 + NL_4 + _331 + NL + NL_4 + _293 + NL_5 + _318 + NL_5 + _299 + NL_7 + _268 + NL_7 + _262 + NL_5 + _332 + NL_7 + _269 + NL_7 + _261 + NL_5 + _331 + NL_4 + _331 + NL + NL_4 + _285 + NL_5 + _325 + NL_5 + _318 + NL_5 + _319 + NL_5 + _303 + NL_7 + _264 + NL_7 + _273 + NL_5 + _332 + NL_7 + _267 + NL_7 + _272 + NL_5 + _331 + NL_4 + _331 + NL + NL_4 + _286 + NL_5 + _325 + NL_5 + _318 + NL_5 + _319 + NL_5 + _302 + NL_7 + _267 + NL_7 + _272 + NL_5 + _331 + NL_4 + _331 + NL + NL_4 + _284 + NL_5 + _324 + NL_5 + _318 + NL_5 + _319 + NL_5 + _303 + NL_7 + _264 + NL_7 + _273 + NL_5 + _332 + NL_7 + _267 + NL_7 + _272 + NL_5 + _331 + NL_4 + _331 + NL + NL_4 + _287 + NL_5 + _318 + NL_5 + _319 + NL_5 + _298 + NL_7 + _264 + NL_7 + _273 + NL_5 + _332 + NL_7 + _267 + NL_7 + _272 + NL_5 + _331 + NL_4 + _331 + NL + NL_4 + _289 + NL_5 + _318 + NL_5 + _319 + NL_5 + _298 + NL_7 + _265 + NL_7 + _300 + NL_9 + _273 + NL_7 + _331 + NL_5 + _332 + NL_7 + _267 + NL_7 + _301 + NL_9 + _272 + NL_7 + _331 + NL_5 + _331 + NL_4 + _331 + NL + NL_4 + _290 + NL_5 + _318 + NL_5 + _319 + NL_5 + _298 + NL_7 + _266 + NL_7 + _300 + NL_9 + _273 + NL_7 + _331 + NL_5 + _332 + NL_7 + _267 + NL_7 + _301 + NL_9 + _272 + NL_7 + _331 + NL_5 + _331 + NL_4 + _331 + NL + NL_4 + _288 + NL_5 + _317 + NL_5 + _316 + NL_5 + _322 + NL_5 + _297 + NL_9 + _261 + NL_9 + _278 + NL_11 + _248 + NL_9 + _331 + NL_9 + _280 + NL_11 + _314 + NL_9 + _331 + NL_5 + _332 + NL_9 + _262 + NL_9 + _278 + NL_11 + _249 + NL_9 + _331 + NL_9 + _280 + NL_11 + _313 + NL_9 + _331 + NL_5 + _331 + NL_4 + _331 + NL + NL_2 + _67 + NL + NL_2 + _129 + NL_4 + _106 + NL_5 + _110 + NL_7 + _108 + NL_9 + _112 + NL_11 + _80 + NL_13 + _137 + NL_11 + _49 + NL_9 + _56 + NL_7 + _56 + NL_5 + _56 + NL_4 + _56 + NL_2 + _60;
  protected final String _338 = NL_2 + _173 + NL_4 + _106 + NL_5 + _121 + NL_5 + _111 + NL_7 + _105 + NL_9 + _160 + NL_11 + _151 + NL_11 + _152;
  protected final String _339 = NL_11 + _142;
  protected final String _340 = NL_11 + _150;
  protected final String _341 = NL_9 + _64 + NL_7 + _56 + NL_5 + _56 + NL_4 + _56 + NL_2 + _68 + NL + NL_2 + _156 + NL_2 + _109 + NL;
  protected final String _342 = NL_4 + _45 + NL_4 + _81 + NL_5 + _200 + NL_7 + _145 + NL_9 + _77 + NL_7 + _62 + NL_7 + _145 + NL_9 + _76 + NL_7 + _62;
  protected final String _343 = NL_7 + _145 + NL_9 + _74;
  protected final String _344 = _49 + NL_7 + _62;
  protected final String _345 = NL_7 + _145 + NL_9 + _192 + NL_9 + _75;
  protected final String _346 = NL_7 + _144 + NL_9 + _191;
  protected final String _347 = NL_9 + _192;
  protected final String _348 = NL_9 + _78;
  protected final String _349 = NL_9 + _49 + NL_7 + _62;
  protected final String _350 = NL_5 + _73 + NL_4 + _52 + NL + NL_4 + _116 + NL_5 + _117;
  protected final String _351 = NL_7 + _120;
  protected final String _352 = NL_7 + _120 + NL_9 + _136;
  protected final String _353 = NL_9 + _217;
  protected final String _354 = NL_7 + _57;
  protected final String _355 = NL_7 + _161;
  protected final String _356 = NL_9 + _82;
  protected final String _357 = NL_9 + _85;
  protected final String _358 = _53 + NL_7 + _65;
  protected final String _359 = NL_7 + _162 + NL_9 + _104 + NL_9 + _176;
  protected final String _360 = _69 + NL_7 + _65 + NL_7 + _84;
  protected final String _361 = NL_7 + _138;
  protected final String _362 = NL_8 + _166 + NL_8 + _168;
  protected final String _363 = NL_8 + _165 + NL_8 + _206;
  protected final String _364 = NL_10 + _150;
  protected final String _365 = NL_8 + _73;
  protected final String _366 = NL_8 + _168;
  protected final String _367 = NL_8 + _127 + NL_8 + _163;
  protected final String _368 = NL_8 + _125 + NL_8 + _163;
  protected final String _369 = NL_8 + _124;
  protected final String _370 = NL_10 + _119 + NL_12 + _136;
  protected final String _371 = _232 + NL_12 + _185 + NL_10 + _56;
  protected final String _372 = NL_10 + _107;
  protected final String _373 = _271 + NL_12 + _133;
  protected final String _374 = NL_12 + _175;
  protected final String _375 = NL_10 + _56;
  protected final String _376 = NL_8 + _123;
  protected final String _377 = NL_8 + _163;
  protected final String _378 = NL_10 + _136;
  protected final String _379 = _232 + NL_10 + _179;
  protected final String _380 = _69 + NL_10 + _79;
  protected final String _381 = _49 + NL_8 + _65;
  protected final String _382 = NL_8 + _163 + NL_10 + _136;
  protected final String _383 = _232 + NL_10 + _136;
  protected final String _384 = _232 + NL_10 + _216 + NL_8 + _65;
  protected final String _385 = NL_6 + _126;
  protected final String _386 = NL_12 + _88;
  protected final String _387 = _14 + NL_12 + _136;
  protected final String _388 = _232 + NL_12 + _177;
  protected final String _389 = NL_12 + _170;
  protected final String _390 = NL_8 + _122 + NL_10 + _210 + NL_10 + _103 + NL_8 + _58 + NL_8 + _113;
  protected final String _391 = NL + _47;
  protected final String _392 = NL_8 + _56;
  protected final String _393 = NL_8 + _167;
  protected final String _394 = NL_10 + _221;
  protected final String _395 = NL_10 + _213 + NL_8 + _65;
  protected final String _396 = NL_8 + _122 + NL_8 + _102 + NL_8 + _135;
  protected final String _397 = _232 + NL_8 + _222;
  protected final String _398 = NL_8 + _183;
  protected final String _399 = NL_8 + _58 + NL_8 + _114;
  protected final String _400 = NL_8 + _122 + NL_8 + _99 + NL_8 + _135;
  protected final String _401 = _232 + NL_8 + _215 + NL_8 + _179;
  protected final String _402 = NL_8 + _182;
  protected final String _403 = NL_8 + _98;
  protected final String _404 = NL_8 + _58 + NL_8 + _201;
  protected final String _405 = NL_10 + _149 + NL_12 + _88;
  protected final String _406 = _14 + NL_12 + _135;
  protected final String _407 = _69 + NL_12 + _20 + NL_12 + _179;
  protected final String _408 = _69 + NL_12 + _203;
  protected final String _409 = _234 + NL_14 + _148 + NL_15 + _87;
  protected final String _410 = _84 + NL_15 + _169;
  protected final String _411 = _66 + NL_14 + _62 + NL_14 + _148 + NL_15 + _89;
  protected final String _412 = _14 + NL_15 + _212 + NL_15 + _204;
  protected final String _413 = NL_16 + _143 + NL_18 + _79;
  protected final String _414 = _234 + NL_19 + _135;
  protected final String _415 = NL_19 + _181;
  protected final String _416 = _69 + NL_18 + _49 + NL_16 + _62;
  protected final String _417 = NL_15 + _73 + NL_14 + _62 + NL_12 + _73 + NL_10 + _62;
  protected final String _418 = NL_8 + _164 + NL_8 + _122 + NL_8 + _93 + NL_8 + _135;
  protected final String _419 = _232 + NL_8 + _220 + NL_8 + _179;
  protected final String _420 = NL_8 + _92;
  protected final String _421 = NL_8 + _58 + NL_8 + _197;
  protected final String _422 = NL_8 + _149;
  protected final String _423 = NL_10 + _118 + NL_12 + _86;
  protected final String _424 = _232 + NL_12 + _184;
  protected final String _425 = _18 + NL_10 + _56;
  protected final String _426 = NL_12 + _86;
  protected final String _427 = NL_12 + _12;
  protected final String _428 = NL_12 + _132;
  protected final String _429 = NL_12 + _178;
  protected final String _430 = NL_10 + _194;
  protected final String _431 = NL_12 + _149 + NL_14 + _79;
  protected final String _432 = _49 + NL_12 + _62;
  protected final String _433 = NL_10 + _73 + NL_8 + _62;
  protected final String _434 = NL + _46;
  protected final String _435 = NL_8 + _164 + NL_8 + _122 + NL_8 + _94 + NL_8 + _135;
  protected final String _436 = _232 + NL_8 + _219 + NL_8 + _179;
  protected final String _437 = NL_8 + _58 + NL_8 + _198;
  protected final String _438 = NL_10 + _149 + NL_12 + _86;
  protected final String _439 = NL_12 + _131;
  protected final String _440 = NL_12 + _174;
  protected final String _441 = _69 + NL_12 + _179;
  protected final String _442 = _69 + NL_12 + _195;
  protected final String _443 = NL_14 + _149 + NL_15 + _79;
  protected final String _444 = _234 + NL_17 + _131;
  protected final String _445 = NL_17 + _181;
  protected final String _446 = _69 + NL_15 + _49 + NL_14 + _62;
  protected final String _447 = NL_12 + _73 + NL_10 + _62;
  protected final String _448 = NL_8 + _164 + NL_8 + _122 + NL_8 + _95 + NL_8 + _135;
  protected final String _449 = _232 + NL_8 + _211 + NL_8 + _179;
  protected final String _450 = NL_8 + _58 + NL_8 + _199;
  protected final String _451 = NL_10 + _147 + NL_13 + _79;
  protected final String _452 = _234 + NL_14 + _135;
  protected final String _453 = NL_14 + _181;
  protected final String _454 = _69 + NL_12 + _49;
  protected final String _455 = NL_12 + _97;
  protected final String _456 = NL_10 + _62;
  protected final String _457 = NL_8 + _164 + NL_8 + _122 + NL_8 + _101 + NL_8 + _134;
  protected final String _458 = _232 + NL_8 + _218 + NL_8 + _179;
  protected final String _459 = NL_8 + _100;
  protected final String _460 = NL_8 + _58 + NL_8 + _202;
  protected final String _461 = NL_10 + _147;
  protected final String _462 = NL_12 + _79;
  protected final String _463 = NL_12 + _193;
  protected final String _464 = _234 + NL_17 + _135;
  protected final String _465 = NL_12 + _73;
  protected final String _466 = NL_8 + _164 + NL_8 + _122 + NL_8 + _91 + NL_8 + _135;
  protected final String _467 = _232 + NL_8 + _209 + NL_8 + _179;
  protected final String _468 = NL_8 + _90;
  protected final String _469 = NL_8 + _58 + NL_8 + _196;
  protected final String _470 = NL_12 + _49;
  protected final String _471 = NL_8 + _122 + NL_8 + _96 + NL_8 + _135;
  protected final String _472 = _232 + NL_8 + _214 + NL_8 + _179;
  protected final String _473 = NL_8 + _58 + NL_8 + _115;
  protected final String _474 = NL_10 + _188 + NL_12 + _140;
  protected final String _475 = NL_12 + _139;
  protected final String _476 = NL_12 + _141;
  protected final String _477 = NL_10 + _69;
  protected final String _478 = NL_10 + _205;
  protected final String _479 = NL_12 + _146;
  protected final String _480 = _7 + NL_14 + _79;
  protected final String _481 = _234 + NL_15 + _135;
  protected final String _482 = _232 + NL_15 + _187;
  protected final String _483 = _69 + NL_15 + _180;
  protected final String _484 = _69 + NL_14 + _49;
  protected final String _485 = NL_14 + _97;
  protected final String _486 = NL_14 + _16;
  protected final String _487 = NL_15 + _135;
  protected final String _488 = _232 + NL_15 + _179;
  protected final String _489 = _69 + NL_14 + _17;
  protected final String _490 = NL_14 + _16 + NL_15 + _135;
  protected final String _491 = _232 + NL_15 + _135;
  protected final String _492 = _232 + NL_14 + _17;
  protected final String _493 = NL_12 + _62;
  protected final String _494 = NL_10 + _73 + NL_8 + _56;
  protected final String _495 = NL_5 + _56 + NL_4 + _56 + NL_3 + _56 + NL_3 + _63 + NL_1 + _54 + NL + _61;

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
    stringBuffer.append(_333);
    stringBuffer.append(reporter.getTitle());
    stringBuffer.append(_334);
    stringBuffer.append(_335);
    stringBuffer.append(_336);
    stringBuffer.append('\u25B7');
    stringBuffer.append(_337);
    stringBuffer.append(_338);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() == null) {
    stringBuffer.append(_339);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_62);
    } else {
    stringBuffer.append(_340);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_234);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_51);
    }
    }
    stringBuffer.append(_341);
    stringBuffer.append(_342);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() != null) {
    stringBuffer.append(_343);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_234);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_344);
    } else {
      if (iuReport == null) {
    stringBuffer.append(_345);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_344);
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
    stringBuffer.append(_346);
    if (index != -1) {
    stringBuffer.append(_347);
    }
    stringBuffer.append(_348);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_234);
    stringBuffer.append(NL_9);
    stringBuffer.append(label);
    stringBuffer.append(_349);
    }
    }
    stringBuffer.append(_350);
    if (iuReport == null) {
    stringBuffer.append(_351);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_57);
    } else {
    stringBuffer.append(_352);
    stringBuffer.append(report.getIUImage(iuReport.getIU()));
    stringBuffer.append(_232);
    String name = report.getName(iuReport.getIU(), false);
    if (name != null) {
    stringBuffer.append(NL_9);
    stringBuffer.append(helper.htmlEscape(name, true));
    stringBuffer.append(_84);
    } else {
    stringBuffer.append(_353);
    }
    stringBuffer.append(NL_9);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_354);
    }
    stringBuffer.append(_355);
    if (report != null && report.getDate() != null) {
    stringBuffer.append(_356);
    stringBuffer.append(report.getDate());
    stringBuffer.append(_53);
    }
    stringBuffer.append(_357);
    stringBuffer.append(reporter.getNow());
    stringBuffer.append(_358);
    if (report != null) {
    stringBuffer.append(_359);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_360);
    }
    stringBuffer.append(_361);
    stringBuffer.append(reporter.getReportBrandingImage());
    stringBuffer.append(_223);
    if (indexReport != null) {
    stringBuffer.append(_362);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_231);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_50);
    Map<String, String> allReports = indexReport.getAllReports();
    if (allReports != null && !allReports.isEmpty()) {
    stringBuffer.append(_363);
    for (Map.Entry<String, String> entry : allReports.entrySet()) {
    stringBuffer.append(_364);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_234);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_51);
    }
    stringBuffer.append(_365);
    }
    } else if (iuReport != null) {
    stringBuffer.append(_366);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_231);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_50);
    String provider = iuReport.getProvider();
    if (provider != null) {
    stringBuffer.append(_367);
    stringBuffer.append(helper.htmlEscape(provider, false));
    stringBuffer.append(_65);
    }
    String description = iuReport.getDescription();
    if (description != null) {
    stringBuffer.append(_368);
    stringBuffer.append(helper.htmlEscape(description, false));
    stringBuffer.append(_65);
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iuReport.getIU());
    if (!artifacts.isEmpty()) {
      List<Certificate> certificates = report.getCertificates(iuReport.getIU());
      if (certificates != null) {
    stringBuffer.append(_369);
    if (certificates.isEmpty()) {
    stringBuffer.append(_370);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_371);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_372);
    stringBuffer.append(count++);
    stringBuffer.append(_373);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_232);
    for (Map.Entry<String, String> component : components.entrySet()) {
              String key = component.getKey();
              String value = component.getValue();
    stringBuffer.append(_374);
    stringBuffer.append(key);
    stringBuffer.append(_207);
    stringBuffer.append(value);
    stringBuffer.append(_69);
    }
    stringBuffer.append(_375);
    }
    }
    }
    stringBuffer.append(_376);
    Collection<IInstallableUnit> pluginsWithMissingPackGZ = report.getPluginsWithMissingPackGZ();
      for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
        String artifact = entry.getKey();
        Boolean signed = entry.getValue();
    stringBuffer.append(_377);
    if (signed != null) {
    stringBuffer.append(_378);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_232);
    }
    stringBuffer.append(_378);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_379);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_380);
    stringBuffer.append(report.getSiteURL() + '/' + artifact);
    stringBuffer.append(_234);
    stringBuffer.append(artifact);
    stringBuffer.append(_381);
    }
    if (pluginsWithMissingPackGZ.contains(iuReport.getIU())) {
    stringBuffer.append(_382);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_383);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_384);
    }
    }
    List<Report.LicenseDetail> licenses = report.getLicenses(iuReport.getIU());
    if (licenses != null) {
    stringBuffer.append(_385);
    for (LicenseDetail license : licenses) {
    String id = license.getUUID();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
    stringBuffer.append(_386);
    stringBuffer.append(id);
    stringBuffer.append(_240);
    stringBuffer.append(id);
    stringBuffer.append(_387);
    stringBuffer.append(report.getLicenseImage());
    stringBuffer.append(_388);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_234);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_69);
    stringBuffer.append(NL_12);
    stringBuffer.append(id);
    stringBuffer.append(_389);
    stringBuffer.append(id);
    stringBuffer.append(_229);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_234);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_186);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_234);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_70);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_234);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_69);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_66);
    }
    }
    stringBuffer.append(_390);
    for (String xml : report.getXML(iuReport.getIU(), Collections.<String, String> emptyMap())) {
    stringBuffer.append(_391);
    stringBuffer.append(xml);
    }
    stringBuffer.append(_392);
    } else {
    if (report.isRoot()) {
    stringBuffer.append(_393);
    }
    stringBuffer.append(_366);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_231);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_50);
    List<Report> children = report.getChildren();
    if (!children.isEmpty()) {
    stringBuffer.append(_377);
    if (!report.isRoot()) {
    stringBuffer.append(_394);
    }
    stringBuffer.append(_395);
    }
    String metadataXML = report.getMetadataXML();
    String artifactXML = report.getArtifactML();
    if (metadataXML != null || artifactXML != null) {
    stringBuffer.append(_396);
    stringBuffer.append(report.getRepositoryImage());
    stringBuffer.append(_397);
    if (metadataXML != null && metadataXML.contains("bad-absolute-location") || artifactXML != null && artifactXML.contains("bad-absolute-location")) {
    stringBuffer.append(_398);
    }
    stringBuffer.append(_399);
    if (metadataXML != null) {
    stringBuffer.append(_391);
    stringBuffer.append(metadataXML);
    }
    if (artifactXML != null) {
    if (metadataXML != null) {
    stringBuffer.append(NL);
    }
    stringBuffer.append(_391);
    stringBuffer.append(artifactXML);
    }
    stringBuffer.append(_392);
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
    stringBuffer.append(_400);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_401);
    stringBuffer.append(licenses.size());
    stringBuffer.append(_69);
    if (nonConformant != 0) {
    stringBuffer.append(_402);
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
    stringBuffer.append(_403);
    stringBuffer.append(displayButton);
    stringBuffer.append(_43);
    stringBuffer.append(onClick);
    stringBuffer.append(_235);
    }
    stringBuffer.append(_404);
    stringBuffer.append(display);
    stringBuffer.append(_39);
    for (Map.Entry<LicenseDetail, Set<IInstallableUnit>> entry : licenses.entrySet()) {
        LicenseDetail license = entry.getKey();
        String id = license.getUUID();
        Set<IInstallableUnit> ius = entry.getValue();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
        {
    stringBuffer.append(_405);
    stringBuffer.append(id);
    stringBuffer.append(_243);
    stringBuffer.append(id);
    stringBuffer.append(_406);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_388);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_234);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_407);
    stringBuffer.append(ius.size());
    stringBuffer.append(_408);
    stringBuffer.append(id);
    stringBuffer.append(_409);
    stringBuffer.append(id);
    stringBuffer.append(_242);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    stringBuffer.append(NL_15);
    stringBuffer.append(id);
    stringBuffer.append(_410);
    stringBuffer.append(id);
    stringBuffer.append(_229);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_234);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_186);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_234);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_70);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_234);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_69);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_411);
    stringBuffer.append(id);
    stringBuffer.append(_244);
    stringBuffer.append(id);
    stringBuffer.append(_412);
    stringBuffer.append(id);
    stringBuffer.append(_234);
    for (IInstallableUnit iu : ius) {
    stringBuffer.append(_413);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_414);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_232);
    stringBuffer.append(NL_19);
    stringBuffer.append(helper.htmlEscape(report.getName(iu, true), true));
    stringBuffer.append(_415);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_416);
    }
    stringBuffer.append(_417);
    }
    }
    stringBuffer.append(_365);
    }
    Map<List<Certificate>, Map<String, IInstallableUnit>> allCertificates = report.getCertificates();
    if (!allCertificates.isEmpty()) {
      int idCount = 0;
      Map<String, IInstallableUnit> unsigned = allCertificates.get(Collections.emptyList());
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_418);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_419);
    stringBuffer.append(allCertificates.size());
    stringBuffer.append(_69);
    if (unsigned != null) {
    stringBuffer.append(_402);
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
    stringBuffer.append(_420);
    stringBuffer.append(displayButton);
    stringBuffer.append(_42);
    stringBuffer.append(onClick);
    stringBuffer.append(_235);
    }
    stringBuffer.append(_421);
    stringBuffer.append(display);
    stringBuffer.append(_40);
    for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : allCertificates.entrySet()) {
        List<Certificate> certificates = entry.getKey();
        String id = "certificates" + ++idCount;
    stringBuffer.append(_422);
    if (certificates.isEmpty()) {
    stringBuffer.append(_423);
    stringBuffer.append(id);
    stringBuffer.append(_241);
    stringBuffer.append(id);
    stringBuffer.append(_406);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_424);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_425);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_372);
    stringBuffer.append(count++ + 2);
    stringBuffer.append(_270);
    if (count == 1) {
    stringBuffer.append(_426);
    stringBuffer.append(id);
    stringBuffer.append(_241);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    } else {
    stringBuffer.append(_427);
    }
    stringBuffer.append(_428);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_232);
    if (count == 1) {
    stringBuffer.append(_429);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_19);
    }
    for (Map.Entry<String, String> component : components.entrySet()) {
              String key = component.getKey();
              String value = component.getValue();
    stringBuffer.append(_374);
    stringBuffer.append(key);
    stringBuffer.append(_207);
    stringBuffer.append(value);
    stringBuffer.append(_69);
    }
    stringBuffer.append(_375);
    }
    }
    stringBuffer.append(_430);
    stringBuffer.append(id);
    stringBuffer.append(_234);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_431);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_236);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_233);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_432);
    }
    stringBuffer.append(_433);
    }
    stringBuffer.append(_365);
    }
    stringBuffer.append(_434);
    Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
    if (!featureProviders.isEmpty()) {
      int nonEclipse = 0;
      for (String provider : featureProviders.keySet()) {
        if (!provider.toLowerCase().contains("eclipse"))
          ++nonEclipse;
      }
    stringBuffer.append(_435);
    stringBuffer.append(report.getProviderImage());
    stringBuffer.append(_436);
    stringBuffer.append(featureProviders.size());
    stringBuffer.append(_69);
    if (nonEclipse != 0) {
    stringBuffer.append(_402);
    stringBuffer.append(nonEclipse);
    stringBuffer.append(_5);
    }
    stringBuffer.append(_437);
    int count = 0;
      for (Map.Entry<String, Set<IInstallableUnit>> provider : featureProviders.entrySet()) {
        String style = !provider.getKey().toLowerCase().contains("eclipse") ? " style=\"color: FireBrick;\"" : "";
        Set<IInstallableUnit> features = provider.getValue();
        String id = "nested_feature_providers" + ++count;
    stringBuffer.append(_438);
    stringBuffer.append(id);
    stringBuffer.append(_239);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    for (String image : report.getBrandingImages(features)) {
    stringBuffer.append(_439);
    stringBuffer.append(image);
    stringBuffer.append(_232);
    }
    stringBuffer.append(_440);
    stringBuffer.append(style);
    stringBuffer.append(_208);
    stringBuffer.append(provider.getKey());
    stringBuffer.append(_441);
    stringBuffer.append(features.size());
    stringBuffer.append(_442);
    stringBuffer.append(id);
    stringBuffer.append(_228);
    for (IInstallableUnit feature : features) {
          String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_443);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_444);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_232);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_445);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_446);
    }
    stringBuffer.append(_447);
    }
    stringBuffer.append(_365);
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
    stringBuffer.append(_448);
    stringBuffer.append(report.getFeatureImage());
    stringBuffer.append(_449);
    stringBuffer.append(features.size());
    stringBuffer.append(_69);
    if (brokenBranding != 0) {
    stringBuffer.append(_402);
    stringBuffer.append(brokenBranding);
    stringBuffer.append(_1);
    }
    if (noBranding != 0) {
    stringBuffer.append(_402);
    stringBuffer.append(noBranding);
    stringBuffer.append(_4);
    }
    stringBuffer.append(_450);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_451);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_452);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_232);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_453);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_454);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        for (LicenseDetail license : report.getLicenses(feature)) {
          String id = license.getUUID();
          licenseReplacements.put(">" + id, "><button class='bb search-for-me' style='" + helper.getStyle(license) + "' onclick=\"clickOnButton('lic_" + id
              + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_455);
    stringBuffer.append(id);
    stringBuffer.append(_224);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_226);
    stringBuffer.append(id);
    stringBuffer.append(_237);
    stringBuffer.append(id);
    stringBuffer.append(_238);
    stringBuffer.append(id);
    stringBuffer.append(_13);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_55);
    }
    stringBuffer.append(_456);
    }
    stringBuffer.append(_365);
    }
    Collection<IInstallableUnit> products = report.getProducts();
    if (!products.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_457);
    stringBuffer.append(report.getProductImage());
    stringBuffer.append(_458);
    stringBuffer.append(products.size());
    stringBuffer.append(_69);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit product : products) {
          String productID = "_product_" + report.getIUID(product);
          onClick.append("expand2('products_all_arrows', '").append(productID).append("');");
        }
    stringBuffer.append(_459);
    stringBuffer.append(displayButton);
    stringBuffer.append(_44);
    stringBuffer.append(onClick);
    stringBuffer.append(_235);
    }
    stringBuffer.append(_460);
    stringBuffer.append(display);
    stringBuffer.append(_39);
    for (IInstallableUnit product : products) {
        String productImage = report.getIUImage(product);
        String productID = "_product_" + report.getIUID(product);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(product));
    stringBuffer.append(_461);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_426);
    stringBuffer.append(productID);
    stringBuffer.append(_241);
    stringBuffer.append(productID);
    stringBuffer.append(_14);
    }
    stringBuffer.append(_462);
    stringBuffer.append(report.getRelativeIUReportURL(product));
    stringBuffer.append(_452);
    stringBuffer.append(productImage);
    stringBuffer.append(_232);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(product, true), true));
    stringBuffer.append(_453);
    stringBuffer.append(report.getVersion(product));
    stringBuffer.append(_454);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_463);
    stringBuffer.append(productID);
    stringBuffer.append(_227);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_443);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_464);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_232);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_445);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_446);
    }
    stringBuffer.append(_465);
    }
    stringBuffer.append(_456);
    }
    stringBuffer.append(_365);
    }
    Collection<IInstallableUnit> categories = report.getCategories();
    if (!categories.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_466);
    stringBuffer.append(report.getCategoryImage());
    stringBuffer.append(_467);
    stringBuffer.append(categories.size());
    stringBuffer.append(_69);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit category : categories) {
          String categoryID = "_category_" + report.getIUID(category);
          onClick.append("expand2('categories_all_arrows', '").append(categoryID).append("');");
        }
    stringBuffer.append(_468);
    stringBuffer.append(displayButton);
    stringBuffer.append(_41);
    stringBuffer.append(onClick);
    stringBuffer.append(_235);
    }
    stringBuffer.append(_469);
    stringBuffer.append(display);
    stringBuffer.append(_39);
    for (IInstallableUnit category : categories) {
        String categoryImage = report.getIUImage(category);
        String categoryID = "_category_" + report.getIUID(category);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(category));
    stringBuffer.append(_461);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_426);
    stringBuffer.append(categoryID);
    stringBuffer.append(_241);
    stringBuffer.append(categoryID);
    stringBuffer.append(_14);
    }
    stringBuffer.append(_462);
    stringBuffer.append(report.getRelativeIUReportURL(category));
    stringBuffer.append(_452);
    stringBuffer.append(categoryImage);
    stringBuffer.append(_232);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(category, true), true));
    stringBuffer.append(_470);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_463);
    stringBuffer.append(categoryID);
    stringBuffer.append(_230);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_443);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_464);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_232);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_445);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_446);
    }
    stringBuffer.append(_465);
    }
    stringBuffer.append(_456);
    }
    stringBuffer.append(_365);
    }
    Collection<IInstallableUnit> ius = report.getAllIUs();
    if (!ius.isEmpty()) {
      Collection<IInstallableUnit> pluginsWithMissingPackGZ = report.getPluginsWithMissingPackGZ();
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
    stringBuffer.append(_471);
    stringBuffer.append(report.getBundleImage());
    stringBuffer.append(_472);
    stringBuffer.append(ius.size());
    stringBuffer.append(_69);
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_402);
    stringBuffer.append(pluginsWithMissingPackGZ.size());
    stringBuffer.append(_3);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_402);
    stringBuffer.append(duplicateCount);
    stringBuffer.append(_8);
    }
    stringBuffer.append(_473);
    if (duplicateCount > 0 || !pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_474);
    if (duplicateCount > 0) {
    stringBuffer.append(_475);
    }
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_476);
    }
    stringBuffer.append(_477);
    }
    stringBuffer.append(_478);
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
    stringBuffer.append(_479);
    stringBuffer.append(id);
    stringBuffer.append(_225);
    stringBuffer.append(classNames);
    stringBuffer.append(_480);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_481);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_482);
    stringBuffer.append(iuID);
    stringBuffer.append(_483);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_234);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_484);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        if (report.isFeature(iu)) {
          for (LicenseDetail license : report.getLicenses(iu)) {
            String licenseID = license.getUUID();
            licenseReplacements.put(">" + licenseID, "><button class='bb search-for-me' style='" + helper.getStyle(license)
                + "' onclick=\"clickOnButton('lic_" + licenseID + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_485);
    stringBuffer.append(licenseID);
    stringBuffer.append(_224);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_226);
    stringBuffer.append(licenseID);
    stringBuffer.append(_237);
    stringBuffer.append(licenseID);
    stringBuffer.append(_238);
    stringBuffer.append(licenseID);
    stringBuffer.append(_13);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_55);
    }
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iu);
        for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
          String artifact = entry.getKey();
          Boolean signed = entry.getValue();
    stringBuffer.append(_486);
    if (signed != null) {
    stringBuffer.append(_487);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_232);
    }
    stringBuffer.append(_487);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_488);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_489);
    }
    if (pluginsWithMissingPackGZ.contains(iu)) {
    stringBuffer.append(_490);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_491);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_492);
    }
    stringBuffer.append(_493);
    }
    stringBuffer.append(_494);
    }
    }
    stringBuffer.append(_495);
    return stringBuffer.toString();
  }
}
