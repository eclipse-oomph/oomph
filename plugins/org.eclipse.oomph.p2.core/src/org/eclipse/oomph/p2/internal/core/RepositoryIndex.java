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
  protected static final String _51 = "</a> report.";
  protected static final String _52 = "</a>.";
  protected static final String _53 = "</a>.</p>";
  protected static final String _54 = "</a></li>";
  protected static final String _55 = "</aside>";
  protected static final String _56 = "</b>";
  protected static final String _57 = "</body>";
  protected static final String _58 = "</button>";
  protected static final String _59 = "</div>";
  protected static final String _60 = "</h2>";
  protected static final String _61 = "</h3>";
  protected static final String _62 = "</head>";
  protected static final String _63 = "</header>";
  protected static final String _64 = "</html>";
  protected static final String _65 = "</li>";
  protected static final String _66 = "</main>";
  protected static final String _67 = "</ol>";
  protected static final String _68 = "</p>";
  protected static final String _69 = "</pre>";
  protected static final String _70 = "</script>";
  protected static final String _71 = "</section>";
  protected static final String _72 = "</span>";
  protected static final String _73 = "</span><span style=\"color: green; text-decoration: underline; ";
  protected static final String _74 = "</style>";
  protected static final String _75 = "</title>";
  protected static final String _76 = "</ul>";
  protected static final String _77 = "<a class=\"separator\" href=\"";
  protected static final String _78 = "<a class=\"separator\" href=\".\">";
  protected static final String _79 = "<a class=\"separator\" href=\"https://www.eclipse.org/downloads\">Downloads</a>";
  protected static final String _80 = "<a class=\"separator\" href=\"https://www.eclipse.org\">Home</a>";
  protected static final String _81 = "<a class=\"separator\" style=\"display: inline-block; margin-left: 0.25em;\" href=\"";
  protected static final String _82 = "<a href=\"";
  protected static final String _83 = "<a href=\"https://wiki.eclipse.org/Oomph_Repository_Analyzer#Description\" target=\"oomph_wiki\">";
  protected static final String _84 = "<a href=\"https://www.eclipse.org/\">";
  protected static final String _85 = "<a style=\"float:right; font-size: 200%;\" href=\"";
  protected static final String _86 = "<aside id=\"leftcol\" class=\"col-md-4 font-smaller\">";
  protected static final String _87 = "<b>Built: ";
  protected static final String _88 = "<body id=\"body_solstice\">";
  protected static final String _89 = "<br/>";
  protected static final String _90 = "<br/><b>Reported: ";
  protected static final String _91 = "<button id=\"";
  protected static final String _92 = "<button id=\"_";
  protected static final String _93 = "<button id=\"__";
  protected static final String _94 = "<button id=\"_f";
  protected static final String _95 = "<button id=\"categories_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _96 = "<button id=\"categories_arrows\" class=\"orange bb\" onclick=\"expand_collapse('categories'); expand_collapse_inline('categories_all_arrows');\">&#x25B7;</button>";
  protected static final String _97 = "<button id=\"certificates_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _98 = "<button id=\"certificates_arrows\" class=\"orange bb\" onclick=\"expand_collapse('certificates'); expand_collapse_inline('certificates_all_arrows');\">&#x25B7;</button>";
  protected static final String _99 = "<button id=\"feature_providers_arrows\" class=\"orange bb\" onclick=\"expand_collapse('feature_providers');\">&#x25B7;</button>";
  protected static final String _100 = "<button id=\"features_arrows\" class=\"orange bb\" onclick=\"expand_collapse('features');\">&#x25B7;</button>";
  protected static final String _101 = "<button id=\"ius_arrows\" class=\"orange bb\" onclick=\"expand_collapse('ius'); match('subset');\">&#x25B7;</button>";
  protected static final String _102 = "<button id=\"lic_";
  protected static final String _103 = "<button id=\"licenses_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _104 = "<button id=\"licenses_arrows\" class=\"orange bb\" onclick=\"expand_collapse('licenses'); expand_collapse_inline('licenses_all_arrows');\">&#x25B7;</button>";
  protected static final String _105 = "<button id=\"products_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _106 = "<button id=\"products_arrows\" class=\"orange bb\" onclick=\"expand_collapse('products'); expand_collapse_inline('products_all_arrows');\">&#x25B7;</button>";
  protected static final String _107 = "<button id=\"repos_arrows\" class=\"orange bb\" onclick=\"expand_collapse('repos');\">&#x25B7;</button>";
  protected static final String _108 = "<button id=\"xml-links\" class=\"orange bb\" onclick=\"expand_collapse_all('xml-links');\">&#x25B7;</button>";
  protected static final String _109 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 150%;\" onclick=\"copyToClipboard('#p1')\">&#x270e;</button>";
  protected static final String _110 = "<div class=\"col-sm-16 padding-left-30\">";
  protected static final String _111 = "<div class=\"container\">";
  protected static final String _112 = "<div class=\"font-smaller\" style=\"margin-left: ";
  protected static final String _113 = "<div class=\"hidden-xs col-sm-8 col-md-6 col-lg-5\" id=\"header-left\">";
  protected static final String _114 = "<div class=\"novaContent container\" id=\"novaContent\">";
  protected static final String _115 = "<div class=\"row\" id=\"header-row\">";
  protected static final String _116 = "<div class=\"row\">";
  protected static final String _117 = "<div class=\"wrapper-logo-default\">";
  protected static final String _118 = "<div class=\"xml-iu\" style=\"display:block;\">";
  protected static final String _119 = "<div class=\"xml-repo\" id=\"repos\" style=\"display: none;\">";
  protected static final String _120 = "<div id=\"ius\" style=\"display:none;\">";
  protected static final String _121 = "<div id=\"maincontent\">";
  protected static final String _122 = "<div id=\"midcolumn\" style=\"width: 70%;\">";
  protected static final String _123 = "<div style=\"height: 40ex;\">";
  protected static final String _124 = "<div style=\"text-indent: -2em\">";
  protected static final String _125 = "<div>";
  protected static final String _126 = "<h2 style=\"text-align: center;\">";
  protected static final String _127 = "<h3 class=\"sr-only\">Breadcrumbs</h3>";
  protected static final String _128 = "<h3 style=\"font-weight: bold;\">";
  protected static final String _129 = "<h3 style=\"font-weight: bold;\">Artifacts</h3>";
  protected static final String _130 = "<h3 style=\"font-weight: bold;\">Certificate</h3>";
  protected static final String _131 = "<h3 style=\"font-weight: bold;\">Description</h3>";
  protected static final String _132 = "<h3 style=\"font-weight: bold;\">Licenses</h3>";
  protected static final String _133 = "<h3 style=\"font-weight: bold;\">Provider</h3>";
  protected static final String _134 = "<head>";
  protected static final String _135 = "<header role=\"banner\" id=\"header-wrapper\">";
  protected static final String _136 = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">";
  protected static final String _137 = "<img alt=\"Branding Image\" class=\"fit-image\" src=\"";
  protected static final String _138 = "<img alt=\"Signed\" class=\"fit-image\" src=\"";
  protected static final String _139 = "<img alt=\"Signed\" class=\"fit-image\" src=\"../";
  protected static final String _140 = "<img class=\"fit-image> src=\"";
  protected static final String _141 = "<img class=\"fit-image\" src=\"";
  protected static final String _142 = "<img class=\"fit-image\" src=\"../";
  protected static final String _143 = "<img class=\"logo-eclipse-default img-responsive hidden-xs\" alt=\"Eclipse Log\" src=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/logo/eclipse-426x100.png\"/>";
  protected static final String _144 = "<img src=\"";
  protected static final String _145 = "<input id=\"bad-license\" type=\"radio\" name=\"filter\" value=\"bad-license\" class=\"filter\" onclick=\"filterIU('bad-license');\"> Bad License </input>";
  protected static final String _146 = "<input id=\"bad-provider\" type=\"radio\" name=\"filter\" value=\"bad-provider\" class=\"filter\" onclick=\"filterIU('bad-provider');\"> Bad Provider </input>";
  protected static final String _147 = "<input id=\"broken-branding\" type=\"radio\" name=\"filter\" value=\"broken-branding\" class=\"filter\" onclick=\"filterIU('broken-branding');\"> Broken Branding </input>";
  protected static final String _148 = "<input id=\"duplicates\" type=\"radio\" name=\"filter\" value=\"duplicate\" class=\"filter\" onclick=\"filterIU('duplicate');\"> Duplicates </input>";
  protected static final String _149 = "<input id=\"filter\" type=\"radio\" name=\"filter\" value=\"all\" class=\"filter\" onclick=\"filterIU('iu-li');\" checked=\"checked\"> All </input>";
  protected static final String _150 = "<input id=\"missing\" type=\"radio\" name=\"filter\" value=\"missing\" class=\"filter\" onclick=\"filterIU('missing');\"> Missing .pack.gz </input>";
  protected static final String _151 = "<input id=\"unsigned\" type=\"radio\" name=\"filter\" value=\"unsigned\" class=\"filter\" onclick=\"filterIU('unsigned');\"> Unsigned </input>";
  protected static final String _152 = "<li class=\"active\">";
  protected static final String _153 = "<li class=\"font-smaller text-nowrap\">";
  protected static final String _154 = "<li class=\"separator\" style=\"font-size: 90%;\">";
  protected static final String _155 = "<li class=\"separator\">";
  protected static final String _156 = "<li id=\"_iu_";
  protected static final String _157 = "<li style=\"font-size: 100%; white-space: nowrap;\">";
  protected static final String _158 = "<li style=\"margin-left: 1em;\">";
  protected static final String _159 = "<li>";
  protected static final String _160 = "<li><a href=\"";
  protected static final String _161 = "<li><a href=\"https://www.eclipse.org/\">Home</a></li>";
  protected static final String _162 = "<li><a href=\"https://www.eclipse.org/downloads/\">Downloads</a></li>";
  protected static final String _163 = "<link href=\"//fonts.googleapis.com/css?family=Open+Sans:400,700,300,600,100\" rel=\"stylesheet\" type=\"text/css\"/>";
  protected static final String _164 = "<link rel=\"icon\" type=\"image/ico\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/favicon.ico\"/>";
  protected static final String _165 = "<link rel=\"stylesheet\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/stylesheets/styles.min.css\"/>";
  protected static final String _166 = "<main class=\"no-promo\">";
  protected static final String _167 = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
  protected static final String _168 = "<meta name=\"description\" content=\"Update Sites Reports\"/>";
  protected static final String _169 = "<meta name=\"keywords\" content=\"eclipse,update site\"/>";
  protected static final String _170 = "<ol class=\"breadcrumb\">";
  protected static final String _171 = "<p style=\"font-size: 125%; text-align: center;\">";
  protected static final String _172 = "<p style=\"text-align: center;\">";
  protected static final String _173 = "<p>";
  protected static final String _174 = "<p></p>";
  protected static final String _175 = "<p>Reports are generated specifically for the following sites:</p>";
  protected static final String _176 = "<p>This report is produced by <a href=\"";
  protected static final String _177 = "<pre id=\"_";
  protected static final String _178 = "<pre id=\"__";
  protected static final String _179 = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";
  protected static final String _180 = "<script>";
  protected static final String _181 = "<section class=\"hidden-print default-breadcrumbs\" id=\"breadcrumb\">";
  protected static final String _182 = "<span ";
  protected static final String _183 = "<span class=\"text-nowrap\"><span style=\"color: SteelBlue; font-size: 75%;\">";
  protected static final String _184 = "<span id=\"p1\" style=\"font-size: 125%\">";
  protected static final String _185 = "<span style=\"";
  protected static final String _186 = "<span style=\"color: DarkCyan; font-size: 110%;\">(";
  protected static final String _187 = "<span style=\"color: DarkCyan;\">";
  protected static final String _188 = "<span style=\"color: DarkOliveGreen;";
  protected static final String _189 = "<span style=\"color: DarkOliveGreen;\">";
  protected static final String _190 = "<span style=\"color: FireBrick; font-size: 60%;\">(";
  protected static final String _191 = "<span style=\"color: FireBrick; font-size: 60%;\">(Inappropriate absolute location)</span>";
  protected static final String _192 = "<span style=\"color: FireBrick;\">(";
  protected static final String _193 = "<span style=\"color: FireBrick;\">Unsigned</span>";
  protected static final String _194 = "<span style=\"color: red; text-decoration: line-through; ";
  protected static final String _195 = "<span style=\"font-size:100%;\">";
  protected static final String _196 = "<span style=\"margin-left: 1em;\" class=\"nowrap\">Filter Pattern: <input id=\"subset\" type=\"text\" oninput=\"match('subset');\"> <span style=\"color: firebrick;\" id=\"subset-error\"></span></input></span><br/>";
  protected static final String _197 = "<span style=\"margin-left: 1em;\">";
  protected static final String _198 = "<style>";
  protected static final String _199 = "<title>";
  protected static final String _200 = "<tt style=\"float: left;\" class=\"orange\">&#xbb;</tt>";
  protected static final String _201 = "<tt style=\"float: right;\" class=\"orange\">&#xab;</tt>";
  protected static final String _202 = "<ul class=\"font-smaller\" id=\"";
  protected static final String _203 = "<ul class=\"font-smaller\" style=\"list-style-type: none; display:none; margin-left: -3em;\" id=\"";
  protected static final String _204 = "<ul id=\"";
  protected static final String _205 = "<ul id=\"categories\" style=\"display: ";
  protected static final String _206 = "<ul id=\"certificates\" style=\"display:";
  protected static final String _207 = "<ul id=\"feature_providers\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _208 = "<ul id=\"features\" style=\"display: none; list-style-type: none; margin-left: -2em;\">";
  protected static final String _209 = "<ul id=\"leftnav\" class=\"ul-left-nav fa-ul hidden-print\">";
  protected static final String _210 = "<ul id=\"licenses\" style=\"display: ";
  protected static final String _211 = "<ul id=\"products\" style=\"display: ";
  protected static final String _212 = "<ul style=\"display:none; list-style-type: none; padding: 0; margin: 0;\" id=\"__";
  protected static final String _213 = "<ul style=\"list-style-type: none; display:none; padding: 0; margin: 0; margin-left: 2em;\" id=\"_f";
  protected static final String _214 = "<ul style=\"list-style-type: none; padding: 0; margin-left: 1em;\">";
  protected static final String _215 = "<ul>";
  protected static final String _216 = "=</span>";
  protected static final String _217 = ">";
  protected static final String _218 = "Categories";
  protected static final String _219 = "Content Metadata";
  protected static final String _220 = "Features";
  protected static final String _221 = "Features/Products";
  protected static final String _222 = "In addition to this composite report, reports are also generated for each of the composed children listed in the navigation bar to the left.";
  protected static final String _223 = "Installable Units";
  protected static final String _224 = "Licenses";
  protected static final String _225 = "Missing";
  protected static final String _226 = "No Name<br/>";
  protected static final String _227 = "Products";
  protected static final String _228 = "Providers";
  protected static final String _229 = "Signing Certificates";
  protected static final String _230 = "This";
  protected static final String _231 = "This is a composite update site.";
  protected static final String _232 = "This is a generated";
  protected static final String _233 = "XML";
  protected static final String _234 = "[<img class=\"fit-image\" src=\"";
  protected static final String _235 = "\" alt=\"\"/>";
  protected static final String _236 = "\" alt=\"\"/><img style=\"margin-top: -2ex;\" class=\"fit-image\" src=\"";
  protected static final String _237 = "\" class=\"bb\" style=\"";
  protected static final String _238 = "\" class=\"iu-li";
  protected static final String _239 = "\" onclick=\"clickOnToggleButton('licenses_arrows'); clickOnToggleButton('__";
  protected static final String _240 = "\" style=\"display: none;\">";
  protected static final String _241 = "\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _242 = "\" style=\"display:none; margin-left: 2em; background-color: ";
  protected static final String _243 = "\" style=\"list-style-type: none; display: none; margin-left: -1em;\">";
  protected static final String _244 = "\" target=\"oomph_wiki\"/>";
  protected static final String _245 = "\" target=\"oomph_wiki\">";
  protected static final String _246 = "\" target=\"report_source\">";
  protected static final String _247 = "\"/>";
  protected static final String _248 = "\"/> ";
  protected static final String _249 = "\"/>]";
  protected static final String _250 = "\">";
  protected static final String _251 = "\">&#x25B7;</button>";
  protected static final String _252 = "\"><img class=\"fit-image\" src=\"";
  protected static final String _253 = "_arrows'); clickOnToggleButton('_";
  protected static final String _254 = "_arrows'); navigateTo('_";
  protected static final String _255 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('";
  protected static final String _256 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('__";
  protected static final String _257 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('";
  protected static final String _258 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_";
  protected static final String _259 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('__";
  protected static final String _260 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_f";
  protected static final String _261 = "background-color: white;";
  protected static final String _262 = "border: 1px solid black;";
  protected static final String _263 = "border: none;";
  protected static final String _264 = "break;";
  protected static final String _265 = "buttonTargets.item(i).innerHTML = '&#x25B7;';";
  protected static final String _266 = "buttonTargets.item(i).innerHTML = '&#x25E2;';";
  protected static final String _267 = "catch (err) {";
  protected static final String _268 = "color: DarkSlateGray;";
  protected static final String _269 = "color: FireBrick;";
  protected static final String _270 = "color: IndianRed;";
  protected static final String _271 = "color: MediumAquaMarine;";
  protected static final String _272 = "color: MediumOrchid;";
  protected static final String _273 = "color: SaddleBrown;";
  protected static final String _274 = "color: SeaGreen;";
  protected static final String _275 = "color: SteelBlue;";
  protected static final String _276 = "color: Teal;";
  protected static final String _277 = "continue;";
  protected static final String _278 = "currentFilter = filter.value;";
  protected static final String _279 = "document.execCommand(\"copy\");";
  protected static final String _280 = "e.click();";
  protected static final String _281 = "e.innerHTML = '&#x25B7;';";
  protected static final String _282 = "e.innerHTML = '&#x25E2;';";
  protected static final String _283 = "e.scrollIntoView();";
  protected static final String _284 = "e.style.display = 'block';";
  protected static final String _285 = "e.style.display = 'inline';";
  protected static final String _286 = "e.style.display = 'inline-block';";
  protected static final String _287 = "e.style.display = 'none';";
  protected static final String _288 = "e.title= 'Collapse All';";
  protected static final String _289 = "e.title= 'Expand All';";
  protected static final String _290 = "em; text-indent: -4em;\">";
  protected static final String _291 = "em;\">";
  protected static final String _292 = "f.innerHTML = '&#x25B7;';";
  protected static final String _293 = "f.innerHTML = '&#x25E2;';";
  protected static final String _294 = "font-family: monospace;";
  protected static final String _295 = "font-size: 125%;";
  protected static final String _296 = "font-size: 80%;";
  protected static final String _297 = "font-size: 90%;";
  protected static final String _298 = "for (var i = 0; i < buttonTargets.length; i++) {";
  protected static final String _299 = "for (var i = 0; i < ius.length; i++) {";
  protected static final String _300 = "for (var i = 0; i < spanTargets.length; i++) {";
  protected static final String _301 = "function clickOnButton(id) {";
  protected static final String _302 = "function clickOnToggleButton(id) {";
  protected static final String _303 = "function copyToClipboard(element) {";
  protected static final String _304 = "function expand(id) {";
  protected static final String _305 = "function expand2(self, id) {";
  protected static final String _306 = "function expand3(self, id) {";
  protected static final String _307 = "function expand_collapse(id) {";
  protected static final String _308 = "function expand_collapse_all(base) {";
  protected static final String _309 = "function expand_collapse_inline(id) {";
  protected static final String _310 = "function expand_collapse_inline_block(id) {";
  protected static final String _311 = "function filterIU(className) {";
  protected static final String _312 = "function match(id) {";
  protected static final String _313 = "function navigateTo(id) {";
  protected static final String _314 = "function toggle(id) {";
  protected static final String _315 = "height: 2ex;";
  protected static final String _316 = "if (!targetsArray.includes(iu)) {";
  protected static final String _317 = "if ((matchText == '' || text.match(matchText) != null) && targetsArray.includes(iu)) {";
  protected static final String _318 = "if (count == 0 && message.innerHTML == '') {";
  protected static final String _319 = "if (count == 0) {";
  protected static final String _320 = "if (e.innerHTML == '";
  protected static final String _321 = "if (e.innerHTML == '\\u25E2') {";
  protected static final String _322 = "if (e.style.display == 'none'){";
  protected static final String _323 = "if (e.title == 'Expand All') {";
  protected static final String _324 = "if (f != null) {";
  protected static final String _325 = "if (f !=null) {";
  protected static final String _326 = "if (filter != null && filter.value != 'all') {";
  protected static final String _327 = "if (matchText != '' && iu.textContent.match(matchText) == null) {";
  protected static final String _328 = "if (t.title != 'Collapse All'){";
  protected static final String _329 = "if (t.title == 'Collapse All'){";
  protected static final String _330 = "iu.style.display = 'block';";
  protected static final String _331 = "iu.style.display = 'none';";
  protected static final String _332 = "margin-bottom: -2ex;";
  protected static final String _333 = "margin-left: 0em;";
  protected static final String _334 = "margin-top: -2ex;";
  protected static final String _335 = "margin: 0px 0px 0px 0px;";
  protected static final String _336 = "message.innerHTML = '';";
  protected static final String _337 = "message.innerHTML = 'No matches';";
  protected static final String _338 = "message.innerHTML = \"\";";
  protected static final String _339 = "message.innerHTML = errMessage;";
  protected static final String _340 = "padding: -2pt -2pt -2pt -2pt;";
  protected static final String _341 = "padding: 0px 0px;";
  protected static final String _342 = "report is produced by <a href=\"";
  protected static final String _343 = "span:target {";
  protected static final String _344 = "spanTargets.item(i).style.display = 'inline-block';";
  protected static final String _345 = "spanTargets.item(i).style.display = 'none';";
  protected static final String _346 = "try";
  protected static final String _347 = "try {";
  protected static final String _348 = "var $temp = $(\"<input>\");";
  protected static final String _349 = "var buttonTargets = document.getElementsByClassName(base + '_button');";
  protected static final String _350 = "var count = 0;";
  protected static final String _351 = "var currentFilter = 'iu-li';";
  protected static final String _352 = "var e = document.getElementById('subset');";
  protected static final String _353 = "var e = document.getElementById(base);";
  protected static final String _354 = "var e = document.getElementById(id);";
  protected static final String _355 = "var errMessage = err.message;";
  protected static final String _356 = "var f = document.getElementById(id+\"_arrows\");";
  protected static final String _357 = "var filter = document.querySelector('input[name=\"filter\"]:checked');";
  protected static final String _358 = "var iu = ius[i];";
  protected static final String _359 = "var ius = document.getElementsByClassName('iu-li');";
  protected static final String _360 = "var matchText = e.value;";
  protected static final String _361 = "var message = document.getElementById('subset-error');";
  protected static final String _362 = "var message = document.getElementById(id + '-error');";
  protected static final String _363 = "var spanTargets = document.getElementsByClassName(base);";
  protected static final String _364 = "var state = e.innerHTML;";
  protected static final String _365 = "var t = document.getElementById('all');";
  protected static final String _366 = "var t = document.getElementById(self);";
  protected static final String _367 = "var targets = document.getElementsByClassName(className);";
  protected static final String _368 = "var targets = document.getElementsByClassName(currentFilter);";
  protected static final String _369 = "var targetsArray = [].slice.call(targets);";
  protected static final String _370 = "var text = iu.textContent;";
  protected static final String _371 = "white-space: nowrap;";
  protected static final String _372 = "white-space: pre;";
  protected static final String _373 = "width: 2ex;";
  protected static final String _374 = "{";
  protected static final String _375 = "}";
  protected static final String _376 = "} else {";
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
  protected final String _377 = _49 + NL + _136 + NL + _134 + NL_1 + _167 + NL_1 + _199;
  protected final String _378 = _75 + NL_1 + _169 + NL_1 + _168 + NL_1 + _163 + NL_1 + _165 + NL_1 + _164 + NL_1 + _179;
  protected final String _379 = NL_1 + _198 + NL + NL + _29 + NL_1 + _371 + NL + _375 + NL + NL + _26 + NL_1 + _297 + NL + _375 + NL + NL + _25 + NL_1 + _373 + NL_1 + _315 + NL + _375 + NL + NL + _343 + NL_1 + _295 + NL_1 + _262 + NL + _375 + NL + NL + _38 + NL_1 + _372 + NL_1 + _263 + NL_1 + _341 + NL_1 + _334 + NL_1 + _332 + NL_1 + _333 + NL + _375 + NL + NL + _22 + NL_1 + _269 + NL_1 + _297 + NL + _375 + NL + NL + _37 + NL_1 + _372 + NL_1 + _263 + NL_1 + _341 + NL_1 + _334 + NL_1 + _332 + NL_1 + _333 + NL + _375 + NL + NL + _39 + NL_1 + _275 + NL_1 + _294 + NL_1 + _296 + NL + _375 + NL + NL + _33 + NL_1 + _271 + NL_1 + _294 + NL_1 + _296 + NL + _375 + NL + NL + _35 + NL_1 + _272 + NL_1 + _294 + NL_1 + _296 + NL + _375 + NL + NL + _34 + NL_1 + _268 + NL_1 + _297 + NL + _375 + NL + NL + _36 + NL_1 + _273 + NL_1 + _297 + NL + _375 + NL + NL + _23 + NL_1 + _261 + NL_1 + _263 + NL_1 + _341 + NL + _375 + NL + NL + _24 + NL_1 + _261 + NL + _375 + NL + NL + _27 + NL_1 + _340 + NL_1 + _335 + NL + _375 + NL + NL + _28 + NL_1 + _274 + NL_1 + _297 + NL + _375 + NL + NL + _32 + NL_1 + _276 + NL_1 + _297 + NL + _375 + NL + NL + _31 + NL_1 + _297 + NL + _375 + NL + NL + _30 + NL_1 + _270 + NL_1 + _297 + NL + _375 + NL_1 + _74 + NL + _62 + NL + NL_1 + _88;
  protected final String _380 = NL_2 + _180 + NL + NL_4 + _312 + NL_5 + _357 + NL_5 + _351 + NL_5 + _326 + NL_7 + _278 + NL_5 + _375 + NL_5 + _354 + NL_5 + _362 + NL_5 + _360 + NL_5 + _359 + NL_5 + _368 + NL_5 + _369 + NL_5 + _350 + NL_5 + _299 + NL_7 + _358 + NL_7 + _370 + NL_7 + _347 + NL_9 + _317 + NL_11 + _330 + NL_11 + _20 + NL_9 + _376 + NL_11 + _331 + NL_9 + _375 + NL_9 + _338 + NL_7 + _375 + NL_7 + _267 + NL_9 + _355 + NL_9 + _339 + NL_9 + _264 + NL_7 + _375 + NL_5 + _375 + NL_5 + _318 + NL_9 + _337 + NL_5 + _375 + NL_4 + _375 + NL + NL_4 + _311 + NL_5 + _352 + NL_5 + _360 + NL_5 + _350 + NL_5 + _359 + NL_5 + _367 + NL_5 + _369 + NL_5 + _299 + NL_7 + _358 + NL_7 + _316 + NL_9 + _331 + NL_7 + _376 + NL_9 + _346 + NL_9 + _374 + NL_11 + _327 + NL_13 + _331 + NL_13 + _277 + NL_11 + _375 + NL_9 + _375 + NL_9 + _267 + NL_9 + _375 + NL_9 + _330 + NL_9 + _20 + NL_7 + _375 + NL_5 + _375 + NL_5 + _361 + NL_5 + _319 + NL_9 + _337 + NL_5 + _376 + NL_9 + _336 + NL_5 + _375 + NL_4 + _375 + NL + NL_4 + _303 + NL_5 + _348 + NL_5 + _9 + NL_5 + _11 + NL_5 + _279 + NL_5 + _10 + NL_4 + _375 + NL + NL_4 + _301 + NL_5 + _354 + NL_5 + _280 + NL_4 + _375 + NL + NL_4 + _302 + NL_5 + _354 + NL_5 + _364 + NL_5 + _320;
  protected final String _381 = _15 + NL_7 + _280 + NL_5 + _375 + NL_4 + _375 + NL + NL_4 + _313 + NL_5 + _354 + NL_5 + _283 + NL_4 + _375 + NL + NL_4 + _314 + NL_5 + _354 + NL_5 + _323 + NL_7 + _288 + NL_7 + _282 + NL_5 + _376 + NL_7 + _289 + NL_7 + _281 + NL_5 + _375 + NL_4 + _375 + NL + NL_4 + _305 + NL_5 + _366 + NL_5 + _354 + NL_5 + _356 + NL_5 + _329 + NL_7 + _284 + NL_7 + _293 + NL_5 + _376 + NL_7 + _287 + NL_7 + _292 + NL_5 + _375 + NL_4 + _375 + NL + NL_4 + _306 + NL_5 + _366 + NL_5 + _354 + NL_5 + _356 + NL_5 + _328 + NL_7 + _287 + NL_7 + _292 + NL_5 + _375 + NL_4 + _375 + NL + NL_4 + _304 + NL_5 + _365 + NL_5 + _354 + NL_5 + _356 + NL_5 + _329 + NL_7 + _284 + NL_7 + _293 + NL_5 + _376 + NL_7 + _287 + NL_7 + _292 + NL_5 + _375 + NL_4 + _375 + NL + NL_4 + _307 + NL_5 + _354 + NL_5 + _356 + NL_5 + _322 + NL_7 + _284 + NL_7 + _293 + NL_5 + _376 + NL_7 + _287 + NL_7 + _292 + NL_5 + _375 + NL_4 + _375 + NL + NL_4 + _309 + NL_5 + _354 + NL_5 + _356 + NL_5 + _322 + NL_7 + _285 + NL_7 + _324 + NL_9 + _293 + NL_7 + _375 + NL_5 + _376 + NL_7 + _287 + NL_7 + _325 + NL_9 + _292 + NL_7 + _375 + NL_5 + _375 + NL_4 + _375 + NL + NL_4 + _310 + NL_5 + _354 + NL_5 + _356 + NL_5 + _322 + NL_7 + _286 + NL_7 + _324 + NL_9 + _293 + NL_7 + _375 + NL_5 + _376 + NL_7 + _287 + NL_7 + _325 + NL_9 + _292 + NL_7 + _375 + NL_5 + _375 + NL_4 + _375 + NL + NL_4 + _308 + NL_5 + _353 + NL_5 + _349 + NL_5 + _363 + NL_5 + _321 + NL_9 + _281 + NL_9 + _298 + NL_11 + _265 + NL_9 + _375 + NL_9 + _300 + NL_11 + _345 + NL_9 + _375 + NL_5 + _376 + NL_9 + _282 + NL_9 + _298 + NL_11 + _266 + NL_9 + _375 + NL_9 + _300 + NL_11 + _344 + NL_9 + _375 + NL_5 + _375 + NL_4 + _375 + NL + NL_2 + _70 + NL + NL_2 + _135 + NL_4 + _111 + NL_5 + _115 + NL_7 + _113 + NL_9 + _117 + NL_11 + _84 + NL_13 + _143 + NL_11 + _50 + NL_9 + _59 + NL_7 + _59 + NL_5 + _59 + NL_4 + _59 + NL_2 + _63;
  protected final String _382 = NL_2 + _181 + NL_4 + _111 + NL_5 + _127 + NL_5 + _116 + NL_7 + _110 + NL_9 + _170 + NL_11 + _161 + NL_11 + _162;
  protected final String _383 = NL_11 + _152;
  protected final String _384 = NL_11 + _160;
  protected final String _385 = NL_9 + _67 + NL_7 + _59 + NL_5 + _59 + NL_4 + _59 + NL_2 + _71 + NL + NL_2 + _166 + NL_2 + _114 + NL;
  protected final String _386 = NL_4 + _46 + NL_4 + _86 + NL_5 + _209 + NL_7 + _155 + NL_9 + _80 + NL_7 + _65 + NL_7 + _155 + NL_9 + _79 + NL_7 + _65;
  protected final String _387 = NL_7 + _155 + NL_9 + _77;
  protected final String _388 = _50 + NL_7 + _65;
  protected final String _389 = NL_7 + _155 + NL_9 + _201 + NL_9 + _78;
  protected final String _390 = NL_7 + _154 + NL_9 + _200;
  protected final String _391 = NL_9 + _201;
  protected final String _392 = NL_9 + _81;
  protected final String _393 = NL_9 + _50 + NL_7 + _65;
  protected final String _394 = NL_5 + _76 + NL_4 + _55 + NL + NL_4 + _121 + NL_5 + _122;
  protected final String _395 = NL_7 + _126;
  protected final String _396 = NL_7 + _126 + NL_9 + _142;
  protected final String _397 = NL_9 + _226;
  protected final String _398 = NL_7 + _60;
  protected final String _399 = NL_7 + _171;
  protected final String _400 = NL_9 + _87;
  protected final String _401 = NL_9 + _90;
  protected final String _402 = _56 + NL_7 + _68;
  protected final String _403 = NL_7 + _172 + NL_9 + _109 + NL_9 + _184;
  protected final String _404 = _72 + NL_7 + _68 + NL_7 + _89;
  protected final String _405 = NL_7 + _85;
  protected final String _406 = _244 + NL_9 + _144;
  protected final String _407 = _235 + NL_7 + _50;
  protected final String _408 = NL_8 + _173 + NL_10 + _232 + NL_10 + _83 + NL_12 + _141;
  protected final String _409 = _51 + NL_8 + _68 + NL_8 + _176;
  protected final String _410 = NL_8 + _175 + NL_8 + _215;
  protected final String _411 = NL_10 + _160;
  protected final String _412 = NL_8 + _76;
  protected final String _413 = NL_8 + _173 + NL_10 + _230 + NL_10 + _82;
  protected final String _414 = _245 + NL_12 + _141;
  protected final String _415 = _50 + NL_10 + _342;
  protected final String _416 = _52 + NL_8 + _68;
  protected final String _417 = NL_8 + _133 + NL_8 + _173;
  protected final String _418 = NL_8 + _131 + NL_8 + _173;
  protected final String _419 = NL_8 + _130;
  protected final String _420 = NL_10 + _125 + NL_12 + _142;
  protected final String _421 = _247 + NL_12 + _193 + NL_10 + _59;
  protected final String _422 = NL_10 + _112;
  protected final String _423 = _291 + NL_12 + _139;
  protected final String _424 = NL_12 + _183;
  protected final String _425 = NL_10 + _59;
  protected final String _426 = NL_8 + _129;
  protected final String _427 = NL_8 + _173;
  protected final String _428 = NL_10 + _142;
  protected final String _429 = _247 + NL_10 + _187;
  protected final String _430 = _72 + NL_10 + _82;
  protected final String _431 = _50 + NL_8 + _68;
  protected final String _432 = NL_8 + _173 + NL_10 + _142;
  protected final String _433 = _247 + NL_10 + _142;
  protected final String _434 = _247 + NL_10 + _225 + NL_8 + _68;
  protected final String _435 = NL_6 + _132;
  protected final String _436 = NL_12 + _93;
  protected final String _437 = _14 + NL_12 + _142;
  protected final String _438 = _247 + NL_12 + _185;
  protected final String _439 = NL_12 + _178;
  protected final String _440 = NL_8 + _128 + NL_10 + _219 + NL_10 + _108 + NL_8 + _61 + NL_8 + _118;
  protected final String _441 = NL + _48;
  protected final String _442 = NL_8 + _59;
  protected final String _443 = NL_10 + _231;
  protected final String _444 = NL_10 + _222 + NL_8 + _68;
  protected final String _445 = NL_8 + _128 + NL_8 + _107 + NL_8 + _141;
  protected final String _446 = _247 + NL_8 + _233;
  protected final String _447 = NL_8 + _191;
  protected final String _448 = NL_8 + _61 + NL_8 + _119;
  protected final String _449 = NL_8 + _128 + NL_8 + _104 + NL_8 + _141;
  protected final String _450 = _247 + NL_8 + _224 + NL_8 + _187;
  protected final String _451 = NL_8 + _190;
  protected final String _452 = NL_8 + _103;
  protected final String _453 = NL_8 + _61 + NL_8 + _210;
  protected final String _454 = NL_10 + _159 + NL_12 + _93;
  protected final String _455 = _14 + NL_12 + _141;
  protected final String _456 = _72 + NL_12 + _21 + NL_12 + _187;
  protected final String _457 = _72 + NL_12 + _212;
  protected final String _458 = _250 + NL_14 + _158 + NL_15 + _92;
  protected final String _459 = _89 + NL_15 + _177;
  protected final String _460 = _69 + NL_14 + _65 + NL_14 + _158 + NL_15 + _94;
  protected final String _461 = _14 + NL_15 + _221 + NL_15 + _213;
  protected final String _462 = NL_16 + _153 + NL_18 + _82;
  protected final String _463 = _250 + NL_19 + _141;
  protected final String _464 = NL_19 + _189;
  protected final String _465 = _72 + NL_18 + _50 + NL_16 + _65;
  protected final String _466 = NL_15 + _76 + NL_14 + _65 + NL_12 + _76 + NL_10 + _65;
  protected final String _467 = NL_8 + _174 + NL_8 + _128 + NL_8 + _98 + NL_8 + _141;
  protected final String _468 = _247 + NL_8 + _229 + NL_8 + _187;
  protected final String _469 = NL_8 + _97;
  protected final String _470 = NL_8 + _61 + NL_8 + _206;
  protected final String _471 = NL_8 + _159;
  protected final String _472 = NL_10 + _124 + NL_12 + _91;
  protected final String _473 = _247 + NL_12 + _192;
  protected final String _474 = _18 + NL_10 + _59;
  protected final String _475 = NL_12 + _91;
  protected final String _476 = NL_12 + _12;
  protected final String _477 = NL_12 + _138;
  protected final String _478 = NL_12 + _186;
  protected final String _479 = NL_10 + _203;
  protected final String _480 = NL_12 + _159 + NL_14 + _82;
  protected final String _481 = _50 + NL_12 + _65;
  protected final String _482 = NL_10 + _76 + NL_8 + _65;
  protected final String _483 = NL + _47;
  protected final String _484 = NL_8 + _174 + NL_8 + _128 + NL_8 + _99 + NL_8 + _141;
  protected final String _485 = _247 + NL_8 + _228 + NL_8 + _187;
  protected final String _486 = NL_8 + _61 + NL_8 + _207;
  protected final String _487 = NL_10 + _159 + NL_12 + _91;
  protected final String _488 = NL_12 + _137;
  protected final String _489 = NL_12 + _182;
  protected final String _490 = _72 + NL_12 + _187;
  protected final String _491 = _72 + NL_12 + _204;
  protected final String _492 = NL_14 + _159 + NL_15 + _82;
  protected final String _493 = _250 + NL_17 + _137;
  protected final String _494 = NL_17 + _189;
  protected final String _495 = _72 + NL_15 + _50 + NL_14 + _65;
  protected final String _496 = NL_12 + _76 + NL_10 + _65;
  protected final String _497 = NL_8 + _174 + NL_8 + _128 + NL_8 + _100 + NL_8 + _141;
  protected final String _498 = _247 + NL_8 + _220 + NL_8 + _187;
  protected final String _499 = NL_8 + _61 + NL_8 + _208;
  protected final String _500 = NL_10 + _157 + NL_13 + _82;
  protected final String _501 = _250 + NL_14 + _141;
  protected final String _502 = NL_14 + _189;
  protected final String _503 = _72 + NL_12 + _50;
  protected final String _504 = NL_12 + _102;
  protected final String _505 = NL_10 + _65;
  protected final String _506 = NL_8 + _174 + NL_8 + _128 + NL_8 + _106 + NL_8 + _140;
  protected final String _507 = _247 + NL_8 + _227 + NL_8 + _187;
  protected final String _508 = NL_8 + _105;
  protected final String _509 = NL_8 + _61 + NL_8 + _211;
  protected final String _510 = NL_10 + _157;
  protected final String _511 = NL_12 + _82;
  protected final String _512 = NL_12 + _202;
  protected final String _513 = _250 + NL_17 + _141;
  protected final String _514 = NL_12 + _76;
  protected final String _515 = NL_8 + _174 + NL_8 + _128 + NL_8 + _96 + NL_8 + _141;
  protected final String _516 = _247 + NL_8 + _218 + NL_8 + _187;
  protected final String _517 = NL_8 + _95;
  protected final String _518 = NL_8 + _61 + NL_8 + _205;
  protected final String _519 = NL_12 + _50;
  protected final String _520 = NL_8 + _128 + NL_8 + _101 + NL_8 + _141;
  protected final String _521 = _247 + NL_8 + _223 + NL_8 + _187;
  protected final String _522 = NL_8 + _61 + NL_8 + _120 + NL_10 + _196;
  protected final String _523 = NL_10 + _197 + NL_12 + _149;
  protected final String _524 = NL_12 + _145;
  protected final String _525 = NL_12 + _151;
  protected final String _526 = NL_12 + _146;
  protected final String _527 = NL_12 + _147;
  protected final String _528 = NL_12 + _148;
  protected final String _529 = NL_12 + _150;
  protected final String _530 = NL_10 + _72;
  protected final String _531 = NL_10 + _214;
  protected final String _532 = NL_12 + _156;
  protected final String _533 = _7 + NL_14 + _82;
  protected final String _534 = _250 + NL_15 + _141;
  protected final String _535 = _247 + NL_15 + _195;
  protected final String _536 = _72 + NL_15 + _188;
  protected final String _537 = _72 + NL_14 + _50;
  protected final String _538 = NL_14 + _102;
  protected final String _539 = NL_14 + _16;
  protected final String _540 = NL_15 + _141;
  protected final String _541 = _247 + NL_15 + _187;
  protected final String _542 = _72 + NL_14 + _17;
  protected final String _543 = NL_14 + _16 + NL_15 + _141;
  protected final String _544 = _247 + NL_15 + _141;
  protected final String _545 = _247 + NL_14 + _17;
  protected final String _546 = NL_14 + _234;
  protected final String _547 = NL_12 + _65;
  protected final String _548 = NL_10 + _76 + NL_8 + _59 + NL_8 + _123 + NL_8 + _59;
  protected final String _549 = NL_5 + _59 + NL_4 + _59 + NL_3 + _59 + NL_3 + _66 + NL_1 + _57 + NL + _64;

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
    stringBuffer.append(_377);
    stringBuffer.append(reporter.getTitle());
    stringBuffer.append(_378);
    stringBuffer.append(_379);
    stringBuffer.append(_380);
    stringBuffer.append('\u25B7');
    stringBuffer.append(_381);
    stringBuffer.append(_382);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() == null) {
    stringBuffer.append(_383);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_65);
    } else {
    stringBuffer.append(_384);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_250);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_54);
    }
    }
    stringBuffer.append(_385);
    stringBuffer.append(_386);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() != null) {
    stringBuffer.append(_387);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_250);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_388);
    } else {
      if (iuReport == null) {
    stringBuffer.append(_389);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_388);
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
    stringBuffer.append(_390);
    if (index != -1) {
    stringBuffer.append(_391);
    }
    stringBuffer.append(_392);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_250);
    stringBuffer.append(NL_9);
    stringBuffer.append(label);
    stringBuffer.append(_393);
    }
    }
    stringBuffer.append(_394);
    if (iuReport == null) {
    stringBuffer.append(_395);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_60);
    } else {
    stringBuffer.append(_396);
    stringBuffer.append(report.getIUImage(iuReport.getIU()));
    stringBuffer.append(_247);
    String name = report.getName(iuReport.getIU(), false);
    if (name != null) {
    stringBuffer.append(NL_9);
    stringBuffer.append(helper.htmlEscape(name, true));
    stringBuffer.append(_89);
    } else {
    stringBuffer.append(_397);
    }
    stringBuffer.append(NL_9);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_398);
    }
    stringBuffer.append(_399);
    if (report != null && report.getDate() != null) {
    stringBuffer.append(_400);
    stringBuffer.append(report.getDate());
    stringBuffer.append(_56);
    }
    stringBuffer.append(_401);
    stringBuffer.append(reporter.getNow());
    stringBuffer.append(_402);
    if (report != null && !report.getSiteURL().startsWith("file:")) {
    stringBuffer.append(_403);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_404);
    }
    stringBuffer.append(_405);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_406);
    stringBuffer.append(reporter.getReportBrandingImage());
    stringBuffer.append(_236);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_407);
    if (indexReport != null) {
    stringBuffer.append(_408);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_247);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_409);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_246);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_53);
    Map<String, String> allReports = indexReport.getAllReports();
    if (allReports != null && !allReports.isEmpty()) {
    stringBuffer.append(_410);
    for (Map.Entry<String, String> entry : allReports.entrySet()) {
    stringBuffer.append(_411);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_250);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_54);
    }
    stringBuffer.append(_412);
    }
    } else if (iuReport != null) {
    stringBuffer.append(_413);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_414);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_247);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_415);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_246);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_416);
    String provider = iuReport.getProvider();
    if (provider != null) {
    stringBuffer.append(_417);
    stringBuffer.append(helper.htmlEscape(provider, false));
    stringBuffer.append(_68);
    }
    String description = iuReport.getDescription();
    if (description != null) {
    stringBuffer.append(_418);
    stringBuffer.append(helper.htmlEscape(description, false));
    stringBuffer.append(_68);
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iuReport.getIU());
    if (!artifacts.isEmpty()) {
      List<Certificate> certificates = report.getCertificates(iuReport.getIU());
      if (certificates != null) {
    stringBuffer.append(_419);
    if (certificates.isEmpty()) {
    stringBuffer.append(_420);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_421);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_422);
    stringBuffer.append(count++);
    stringBuffer.append(_423);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_247);
    for (Map.Entry<String, String> component : components.entrySet()) {
              String key = component.getKey();
              String value = component.getValue();
    stringBuffer.append(_424);
    stringBuffer.append(key);
    stringBuffer.append(_216);
    stringBuffer.append(value);
    stringBuffer.append(_72);
    }
    stringBuffer.append(_425);
    }
    }
    }
    stringBuffer.append(_426);
    Collection<IInstallableUnit> pluginsWithMissingPackGZ = report.getPluginsWithMissingPackGZ();
      for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
        String artifact = entry.getKey();
        Boolean signed = entry.getValue();
    stringBuffer.append(_427);
    if (signed != null) {
    stringBuffer.append(_428);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_247);
    }
    stringBuffer.append(_428);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_429);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_430);
    stringBuffer.append(report.getRepositoryURL(artifact) + '/' + artifact);
    stringBuffer.append(_250);
    stringBuffer.append(artifact);
    stringBuffer.append(_431);
    }
    if (pluginsWithMissingPackGZ.contains(iuReport.getIU())) {
    stringBuffer.append(_432);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_433);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_434);
    }
    }
    List<Report.LicenseDetail> licenses = report.getLicenses(iuReport.getIU());
    if (licenses != null) {
    stringBuffer.append(_435);
    for (LicenseDetail license : licenses) {
    String id = license.getUUID();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
    stringBuffer.append(_436);
    stringBuffer.append(id);
    stringBuffer.append(_256);
    stringBuffer.append(id);
    stringBuffer.append(_437);
    stringBuffer.append(report.getLicenseImage());
    stringBuffer.append(_438);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_250);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_72);
    stringBuffer.append(NL_12);
    stringBuffer.append(id);
    stringBuffer.append(_439);
    stringBuffer.append(id);
    stringBuffer.append(_242);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_250);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_194);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_250);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_73);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_250);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_72);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_69);
    }
    }
    stringBuffer.append(_440);
    for (String xml : report.getXML(iuReport.getIU(), Collections.<String, String> emptyMap())) {
    stringBuffer.append(_441);
    stringBuffer.append(xml);
    }
    stringBuffer.append(_442);
    } else {
    stringBuffer.append(_413);
    stringBuffer.append(report.getHelpLink());
    stringBuffer.append(_414);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_247);
    stringBuffer.append(NL_12);
    stringBuffer.append(report.getHelpText());
    stringBuffer.append(_415);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_246);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_416);
    List<Report> children = report.getChildren();
    if (!children.isEmpty()) {
    stringBuffer.append(_427);
    if (!report.isRoot()) {
    stringBuffer.append(_443);
    }
    stringBuffer.append(_444);
    }
    String metadataXML = report.getMetadataXML();
    String artifactXML = report.getArtifactML();
    if (metadataXML != null || artifactXML != null) {
    stringBuffer.append(_445);
    stringBuffer.append(report.getRepositoryImage());
    stringBuffer.append(_446);
    if (metadataXML != null && metadataXML.contains("bad-absolute-location") || artifactXML != null && artifactXML.contains("bad-absolute-location")) {
    stringBuffer.append(_447);
    }
    stringBuffer.append(_448);
    if (metadataXML != null) {
    stringBuffer.append(_441);
    stringBuffer.append(metadataXML);
    }
    if (artifactXML != null) {
    if (metadataXML != null) {
    stringBuffer.append(NL);
    }
    stringBuffer.append(_441);
    stringBuffer.append(artifactXML);
    }
    stringBuffer.append(_442);
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
    stringBuffer.append(_449);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_450);
    stringBuffer.append(licenses.size());
    stringBuffer.append(_72);
    if (nonConformant != 0) {
    stringBuffer.append(_451);
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
    stringBuffer.append(_452);
    stringBuffer.append(displayButton);
    stringBuffer.append(_44);
    stringBuffer.append(onClick);
    stringBuffer.append(_251);
    }
    stringBuffer.append(_453);
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
    stringBuffer.append(_454);
    stringBuffer.append(id);
    stringBuffer.append(_259);
    stringBuffer.append(id);
    stringBuffer.append(_455);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_438);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_250);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_456);
    stringBuffer.append(ius.size());
    stringBuffer.append(_457);
    stringBuffer.append(id);
    stringBuffer.append(_458);
    stringBuffer.append(id);
    stringBuffer.append(_258);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    stringBuffer.append(NL_15);
    stringBuffer.append(id);
    stringBuffer.append(_459);
    stringBuffer.append(id);
    stringBuffer.append(_242);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_250);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_194);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_250);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_73);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_250);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_72);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_460);
    stringBuffer.append(id);
    stringBuffer.append(_260);
    stringBuffer.append(id);
    stringBuffer.append(_461);
    stringBuffer.append(id);
    stringBuffer.append(_250);
    for (IInstallableUnit iu : ius) {
    stringBuffer.append(_462);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_463);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_247);
    stringBuffer.append(NL_19);
    stringBuffer.append(helper.htmlEscape(report.getName(iu, true), true));
    stringBuffer.append(_464);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_465);
    }
    stringBuffer.append(_466);
    }
    }
    stringBuffer.append(_412);
    }
    Map<List<Certificate>, Map<String, IInstallableUnit>> allCertificates = report.getCertificates();
    if (!allCertificates.isEmpty()) {
      int idCount = 0;
      Map<String, IInstallableUnit> unsigned = allCertificates.get(Collections.emptyList());
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_467);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_468);
    stringBuffer.append(allCertificates.size());
    stringBuffer.append(_72);
    if (unsigned != null) {
    stringBuffer.append(_451);
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
    stringBuffer.append(_469);
    stringBuffer.append(displayButton);
    stringBuffer.append(_43);
    stringBuffer.append(onClick);
    stringBuffer.append(_251);
    }
    stringBuffer.append(_470);
    stringBuffer.append(display);
    stringBuffer.append(_41);
    for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : allCertificates.entrySet()) {
        List<Certificate> certificates = entry.getKey();
        String id = "certificates" + ++idCount;
    stringBuffer.append(_471);
    if (certificates.isEmpty()) {
    stringBuffer.append(_472);
    stringBuffer.append(id);
    stringBuffer.append(_257);
    stringBuffer.append(id);
    stringBuffer.append(_455);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_473);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_474);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_422);
    stringBuffer.append(count++ + 2);
    stringBuffer.append(_290);
    if (count == 1) {
    stringBuffer.append(_475);
    stringBuffer.append(id);
    stringBuffer.append(_257);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    } else {
    stringBuffer.append(_476);
    }
    stringBuffer.append(_477);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_247);
    if (count == 1) {
    stringBuffer.append(_478);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_19);
    }
    for (Map.Entry<String, String> component : components.entrySet()) {
              String key = component.getKey();
              String value = component.getValue();
    stringBuffer.append(_424);
    stringBuffer.append(key);
    stringBuffer.append(_216);
    stringBuffer.append(value);
    stringBuffer.append(_72);
    }
    stringBuffer.append(_425);
    }
    }
    stringBuffer.append(_479);
    stringBuffer.append(id);
    stringBuffer.append(_250);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_480);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_252);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_248);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_481);
    }
    stringBuffer.append(_482);
    }
    stringBuffer.append(_412);
    }
    stringBuffer.append(_483);
    Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
    if (!featureProviders.isEmpty()) {
      int nonEclipse = 0;
      for (String provider : featureProviders.keySet()) {
        if (!provider.toLowerCase().contains("eclipse"))
          ++nonEclipse;
      }
    stringBuffer.append(_484);
    stringBuffer.append(report.getProviderImage());
    stringBuffer.append(_485);
    stringBuffer.append(featureProviders.size());
    stringBuffer.append(_72);
    if (nonEclipse != 0) {
    stringBuffer.append(_451);
    stringBuffer.append(nonEclipse);
    stringBuffer.append(_5);
    }
    stringBuffer.append(_486);
    int count = 0;
      for (Map.Entry<String, Set<IInstallableUnit>> provider : featureProviders.entrySet()) {
        String style = !provider.getKey().toLowerCase().contains("eclipse") ? " style=\"color: FireBrick;\"" : "";
        Set<IInstallableUnit> features = provider.getValue();
        String id = "nested_feature_providers" + ++count;
    stringBuffer.append(_487);
    stringBuffer.append(id);
    stringBuffer.append(_255);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    for (String image : report.getBrandingImages(features)) {
    stringBuffer.append(_488);
    stringBuffer.append(image);
    stringBuffer.append(_247);
    }
    stringBuffer.append(_489);
    stringBuffer.append(style);
    stringBuffer.append(_217);
    stringBuffer.append(provider.getKey());
    stringBuffer.append(_490);
    stringBuffer.append(features.size());
    stringBuffer.append(_491);
    stringBuffer.append(id);
    stringBuffer.append(_241);
    for (IInstallableUnit feature : features) {
          String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_492);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_493);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_247);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_494);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_495);
    }
    stringBuffer.append(_496);
    }
    stringBuffer.append(_412);
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
    stringBuffer.append(_497);
    stringBuffer.append(report.getFeatureImage());
    stringBuffer.append(_498);
    stringBuffer.append(features.size());
    stringBuffer.append(_72);
    if (brokenBranding != 0) {
    stringBuffer.append(_451);
    stringBuffer.append(brokenBranding);
    stringBuffer.append(_1);
    }
    if (noBranding != 0) {
    stringBuffer.append(_451);
    stringBuffer.append(noBranding);
    stringBuffer.append(_4);
    }
    stringBuffer.append(_499);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_500);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_501);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_247);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_502);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_503);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        for (LicenseDetail license : report.getLicenses(feature)) {
          String id = license.getUUID();
          licenseReplacements.put(">" + id, "><button class='bb search-for-me' style='" + helper.getStyle(license) + "' onclick=\"clickOnButton('lic_" + id
              + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_504);
    stringBuffer.append(id);
    stringBuffer.append(_237);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_239);
    stringBuffer.append(id);
    stringBuffer.append(_253);
    stringBuffer.append(id);
    stringBuffer.append(_254);
    stringBuffer.append(id);
    stringBuffer.append(_13);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_58);
    }
    stringBuffer.append(_505);
    }
    stringBuffer.append(_412);
    }
    Collection<IInstallableUnit> products = report.getProducts();
    if (!products.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_506);
    stringBuffer.append(report.getProductImage());
    stringBuffer.append(_507);
    stringBuffer.append(products.size());
    stringBuffer.append(_72);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit product : products) {
          String productID = "_product_" + report.getIUID(product);
          onClick.append("expand2('products_all_arrows', '").append(productID).append("');");
        }
    stringBuffer.append(_508);
    stringBuffer.append(displayButton);
    stringBuffer.append(_45);
    stringBuffer.append(onClick);
    stringBuffer.append(_251);
    }
    stringBuffer.append(_509);
    stringBuffer.append(display);
    stringBuffer.append(_40);
    for (IInstallableUnit product : products) {
        String productImage = report.getIUImage(product);
        String productID = "_product_" + report.getIUID(product);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(product));
    stringBuffer.append(_510);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_475);
    stringBuffer.append(productID);
    stringBuffer.append(_257);
    stringBuffer.append(productID);
    stringBuffer.append(_14);
    }
    stringBuffer.append(_511);
    stringBuffer.append(report.getRelativeIUReportURL(product));
    stringBuffer.append(_501);
    stringBuffer.append(productImage);
    stringBuffer.append(_247);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(product, true), true));
    stringBuffer.append(_502);
    stringBuffer.append(report.getVersion(product));
    stringBuffer.append(_503);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_512);
    stringBuffer.append(productID);
    stringBuffer.append(_240);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_492);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_513);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_247);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_494);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_495);
    }
    stringBuffer.append(_514);
    }
    stringBuffer.append(_505);
    }
    stringBuffer.append(_412);
    }
    Collection<IInstallableUnit> categories = report.getCategories();
    if (!categories.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_515);
    stringBuffer.append(report.getCategoryImage());
    stringBuffer.append(_516);
    stringBuffer.append(categories.size());
    stringBuffer.append(_72);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit category : categories) {
          String categoryID = "_category_" + report.getIUID(category);
          onClick.append("expand2('categories_all_arrows', '").append(categoryID).append("');");
        }
    stringBuffer.append(_517);
    stringBuffer.append(displayButton);
    stringBuffer.append(_42);
    stringBuffer.append(onClick);
    stringBuffer.append(_251);
    }
    stringBuffer.append(_518);
    stringBuffer.append(display);
    stringBuffer.append(_40);
    for (IInstallableUnit category : categories) {
        String categoryImage = report.getIUImage(category);
        String categoryID = "_category_" + report.getIUID(category);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(category));
    stringBuffer.append(_510);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_475);
    stringBuffer.append(categoryID);
    stringBuffer.append(_257);
    stringBuffer.append(categoryID);
    stringBuffer.append(_14);
    }
    stringBuffer.append(_511);
    stringBuffer.append(report.getRelativeIUReportURL(category));
    stringBuffer.append(_501);
    stringBuffer.append(categoryImage);
    stringBuffer.append(_247);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(category, true), true));
    stringBuffer.append(_519);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_512);
    stringBuffer.append(categoryID);
    stringBuffer.append(_243);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_492);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_513);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_247);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_494);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_495);
    }
    stringBuffer.append(_514);
    }
    stringBuffer.append(_505);
    }
    stringBuffer.append(_412);
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
    stringBuffer.append(_520);
    stringBuffer.append(report.getBundleImage());
    stringBuffer.append(_521);
    stringBuffer.append(ius.size());
    stringBuffer.append(_72);
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_451);
    stringBuffer.append(pluginsWithMissingPackGZ.size());
    stringBuffer.append(_3);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_451);
    stringBuffer.append(duplicateCount);
    stringBuffer.append(_8);
    }
    stringBuffer.append(_522);
    if (duplicateCount > 0 || !pluginsWithMissingPackGZ.isEmpty() || !unsignedIUs.isEmpty() || !badProviderIUs.isEmpty() || !badLicenseIUs.isEmpty()
          || !brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_523);
    if (!badLicenseIUs.isEmpty()) {
    stringBuffer.append(_524);
    }
    if (!unsignedIUs.isEmpty()) {
    stringBuffer.append(_525);
    }
    if (!badProviderIUs.isEmpty()) {
    stringBuffer.append(_526);
    }
    if (!brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_527);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_528);
    }
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_529);
    }
    stringBuffer.append(_530);
    }
    stringBuffer.append(_531);
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
    stringBuffer.append(_532);
    stringBuffer.append(id);
    stringBuffer.append(_238);
    stringBuffer.append(classNames);
    stringBuffer.append(_533);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_534);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_535);
    stringBuffer.append(iuID);
    stringBuffer.append(_536);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_250);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_537);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        if (report.isFeature(iu)) {
          for (LicenseDetail license : report.getLicenses(iu)) {
            String licenseID = license.getUUID();
            licenseReplacements.put(">" + licenseID, "><button class='bb search-for-me' style='" + helper.getStyle(license)
                + "' onclick=\"clickOnButton('lic_" + licenseID + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_538);
    stringBuffer.append(licenseID);
    stringBuffer.append(_237);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_239);
    stringBuffer.append(licenseID);
    stringBuffer.append(_253);
    stringBuffer.append(licenseID);
    stringBuffer.append(_254);
    stringBuffer.append(licenseID);
    stringBuffer.append(_13);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_58);
    }
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iu);
        for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
          String artifact = entry.getKey();
          Boolean signed = entry.getValue();
    stringBuffer.append(_539);
    if (signed != null) {
    stringBuffer.append(_540);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_247);
    }
    stringBuffer.append(_540);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_541);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_542);
    }
    if (pluginsWithMissingPackGZ.contains(iu)) {
    stringBuffer.append(_543);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_544);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_545);
    }
    String brandingImage = report.getBrandingImage(iu);
        if (brandingImage != null) {
    stringBuffer.append(_546);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_249);
    }
    stringBuffer.append(_547);
    }
    stringBuffer.append(_548);
    }
    }
    stringBuffer.append(_549);
    return stringBuffer.toString();
  }
}
