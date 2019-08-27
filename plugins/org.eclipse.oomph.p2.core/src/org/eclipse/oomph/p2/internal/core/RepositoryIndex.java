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
  protected static final String _12 = "&bull;</button>";
  protected static final String _13 = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  protected static final String _14 = "');\">";
  protected static final String _15 = "');\">&#x25B7;</button>";
  protected static final String _16 = "'){";
  protected static final String _17 = "(";
  protected static final String _18 = ")";
  protected static final String _19 = ") Unsigned</span>";
  protected static final String _20 = ")</span>";
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
  protected static final String _97 = "<button id=\"ius_arrows\" class=\"orange bb\" onclick=\"expand_collapse('ius');expand_collapse('ius_details');\">&#x25B7;</button>";
  protected static final String _98 = "<button id=\"lic_";
  protected static final String _99 = "<button id=\"licenses_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _100 = "<button id=\"licenses_arrows\" class=\"orange bb\" onclick=\"expand_collapse('licenses'); expand_collapse_inline('licenses_all_arrows');\">&#x25B7;</button>";
  protected static final String _101 = "<button id=\"products_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _102 = "<button id=\"products_arrows\" class=\"orange bb\" onclick=\"expand_collapse('products'); expand_collapse_inline('products_all_arrows');\">&#x25B7;</button>";
  protected static final String _103 = "<button id=\"repos_arrows\" class=\"orange bb\" onclick=\"expand_collapse('repos');\">&#x25B7;</button>";
  protected static final String _104 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 150%;\" onclick=\"copyToClipboard('#p1')\">&#x270e;</button>";
  protected static final String _105 = "<div class=\"col-sm-16 padding-left-30\">";
  protected static final String _106 = "<div class=\"col-sm-8 margin-top-15\"></div>";
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
  protected static final String _119 = "<div style=\"text-indent: -2em\">";
  protected static final String _120 = "<div>";
  protected static final String _121 = "<h2 style=\"text-align: center;\">";
  protected static final String _122 = "<h3 class=\"sr-only\">Breadcrumbs</h3>";
  protected static final String _123 = "<h3 style=\"font-weight: bold;\">";
  protected static final String _124 = "<h3 style=\"font-weight: bold;\">Artifacts</h3>";
  protected static final String _125 = "<h3 style=\"font-weight: bold;\">Certificate</h3>";
  protected static final String _126 = "<h3 style=\"font-weight: bold;\">Content Metadata</h3>";
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
  protected static final String _141 = "<input id=\"duplicates\" type=\"radio\" name=\"filter\" value=\"duplicate\" class=\"filter\" onclick=\"filterIU('duplicate');\"> Duplicates </input>";
  protected static final String _142 = "<input id=\"filter\" type=\"radio\" name=\"filter\" value=\"all\" class=\"filter\" onclick=\"filterIU('iu-li');\" checked=\"checked\"> All </input>";
  protected static final String _143 = "<input id=\"missing\" type=\"radio\" name=\"filter\" value=\"missing\" class=\"filter\" onclick=\"filterIU('missing');\"> Missing .pack.gz </input>";
  protected static final String _144 = "<li class=\"active\">";
  protected static final String _145 = "<li class=\"font-smaller text-nowrap\">";
  protected static final String _146 = "<li class=\"separator\" style=\"font-size: 90%;\">";
  protected static final String _147 = "<li class=\"separator\">";
  protected static final String _148 = "<li id=\"_iu_";
  protected static final String _149 = "<li style=\"font-size: 100%; white-space: nowrap;\">";
  protected static final String _150 = "<li style=\"margin-left: 1em;\">";
  protected static final String _151 = "<li>";
  protected static final String _152 = "<li><a href=\"";
  protected static final String _153 = "<li><a href=\"https://www.eclipse.org/\">Home</a></li>";
  protected static final String _154 = "<li><a href=\"https://www.eclipse.org/downloads/\">Downloads</a></li>";
  protected static final String _155 = "<link href=\"//fonts.googleapis.com/css?family=Open+Sans:400,700,300,600,100\" rel=\"stylesheet\" type=\"text/css\"/>";
  protected static final String _156 = "<link rel=\"icon\" type=\"image/ico\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/favicon.ico\"/>";
  protected static final String _157 = "<link rel=\"stylesheet\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/stylesheets/styles.min.css\"/>";
  protected static final String _158 = "<main class=\"no-promo\">";
  protected static final String _159 = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
  protected static final String _160 = "<meta name=\"description\" content=\"Update Sites Reports\"/>";
  protected static final String _161 = "<meta name=\"keywords\" content=\"eclipse,update site\"/>";
  protected static final String _162 = "<ol class=\"breadcrumb\">";
  protected static final String _163 = "<p style=\"font-size: 125%; text-align: center;\">";
  protected static final String _164 = "<p style=\"text-align: center;\">";
  protected static final String _165 = "<p>";
  protected static final String _166 = "<p></p>";
  protected static final String _167 = "<p>Reports are generated specifically for the following sites:</p>";
  protected static final String _168 = "<p>This is a generated report folder index.</p>";
  protected static final String _169 = "<p>This is the generated report index.</p>";
  protected static final String _170 = "<p>This report is produced by <a href=\"";
  protected static final String _171 = "<pre id=\"_";
  protected static final String _172 = "<pre id=\"__";
  protected static final String _173 = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";
  protected static final String _174 = "<script>";
  protected static final String _175 = "<section class=\"hidden-print default-breadcrumbs\" id=\"breadcrumb\">";
  protected static final String _176 = "<span ";
  protected static final String _177 = "<span class=\"text-nowrap\"><span style=\"color: SteelBlue; font-size: 75%;\">";
  protected static final String _178 = "<span id=\"p1\" style=\"font-size: 125%\">";
  protected static final String _179 = "<span style=\"";
  protected static final String _180 = "<span style=\"color: DarkCyan; font-size: 110%;\">(";
  protected static final String _181 = "<span style=\"color: DarkCyan;\">";
  protected static final String _182 = "<span style=\"color: DarkOliveGreen;";
  protected static final String _183 = "<span style=\"color: DarkOliveGreen;\">";
  protected static final String _184 = "<span style=\"color: FireBrick; font-size: 60%;\">(";
  protected static final String _185 = "<span style=\"color: FireBrick; font-size: 60%;\">(Inappropriate absolute location)</span>";
  protected static final String _186 = "<span style=\"color: FireBrick;\">(";
  protected static final String _187 = "<span style=\"color: FireBrick;\">Unsigned</span>";
  protected static final String _188 = "<span style=\"color: red; text-decoration: line-through; ";
  protected static final String _189 = "<span style=\"font-size:100%;\">";
  protected static final String _190 = "<span style=\"margin-left: 1em;\">";
  protected static final String _191 = "<style>";
  protected static final String _192 = "<title>";
  protected static final String _193 = "<tt style=\"float: left;\" class=\"orange\">&#xbb;</tt>";
  protected static final String _194 = "<tt style=\"float: right;\" class=\"orange\">&#xab;</tt>";
  protected static final String _195 = "<ul class=\"font-smaller\" id=\"";
  protected static final String _196 = "<ul class=\"font-smaller\" style=\"display:none; margin-left: -2em;\" id=\"";
  protected static final String _197 = "<ul id=\"";
  protected static final String _198 = "<ul id=\"categories\" style=\"display: ";
  protected static final String _199 = "<ul id=\"certificates\" style=\"display:";
  protected static final String _200 = "<ul id=\"feature_providers\" style=\"display:none; margin-left: -1em; list-style-type: none;\">";
  protected static final String _201 = "<ul id=\"features\" style=\"display: none;\">";
  protected static final String _202 = "<ul id=\"leftnav\" class=\"ul-left-nav fa-ul hidden-print\">";
  protected static final String _203 = "<ul id=\"licenses\" style=\"display: ";
  protected static final String _204 = "<ul id=\"products\" style=\"display: ";
  protected static final String _205 = "<ul style=\"display:none; list-style-type: none; padding: 0; margin: 0;\" id=\"__";
  protected static final String _206 = "<ul style=\"display:none; padding: 0; margin: 0; margin-left: 3em;\" id=\"_f";
  protected static final String _207 = "<ul style=\"margin-left: -1em; list-style-type: none; padding: 0; margin: 0;\">";
  protected static final String _208 = "<ul>";
  protected static final String _209 = "=</span>";
  protected static final String _210 = ">";
  protected static final String _211 = "Categories";
  protected static final String _212 = "Features";
  protected static final String _213 = "Features/Products";
  protected static final String _214 = "In addition to this composite report, reports are also generated for each of the composed children listed in the navigation bar to the left.";
  protected static final String _215 = "Installable Units";
  protected static final String _216 = "Licenses";
  protected static final String _217 = "Missing";
  protected static final String _218 = "No Name<br/>";
  protected static final String _219 = "Products";
  protected static final String _220 = "Providers";
  protected static final String _221 = "Signing Certificates";
  protected static final String _222 = "This is a composite update site.";
  protected static final String _223 = "XML";
  protected static final String _224 = "\" alt=\"\"/>";
  protected static final String _225 = "\" class=\"bb\" style=\"";
  protected static final String _226 = "\" class=\"iu-li";
  protected static final String _227 = "\" onclick=\"clickOnToggleButton('licenses_arrows'); clickOnToggleButton('__";
  protected static final String _228 = "\" style=\"display: none;\">";
  protected static final String _229 = "\" style=\"display:none; margin-left: -1em; list-style-type: none;\">";
  protected static final String _230 = "\" style=\"display:none; margin-left: 2em; background-color: ";
  protected static final String _231 = "\" target=\"report_source\">";
  protected static final String _232 = "\"/>";
  protected static final String _233 = "\">";
  protected static final String _234 = "\">&#x25B7;</button>";
  protected static final String _235 = "_arrows'); clickOnToggleButton('_";
  protected static final String _236 = "_arrows'); navigateTo('_";
  protected static final String _237 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('";
  protected static final String _238 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('__";
  protected static final String _239 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('";
  protected static final String _240 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_";
  protected static final String _241 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('__";
  protected static final String _242 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_f";
  protected static final String _243 = "background-color: white;";
  protected static final String _244 = "border: 1px solid black;";
  protected static final String _245 = "border: none;";
  protected static final String _246 = "color: DarkSlateGray;";
  protected static final String _247 = "color: FireBrick;";
  protected static final String _248 = "color: IndianRed;";
  protected static final String _249 = "color: MediumAquaMarine;";
  protected static final String _250 = "color: MediumOrchid;";
  protected static final String _251 = "color: SaddleBrown;";
  protected static final String _252 = "color: SeaGreen;";
  protected static final String _253 = "color: SteelBlue;";
  protected static final String _254 = "color: Teal;";
  protected static final String _255 = "document.execCommand(\"copy\");";
  protected static final String _256 = "e.click();";
  protected static final String _257 = "e.innerHTML = '&#x25B7;';";
  protected static final String _258 = "e.innerHTML = '&#x25E2;';";
  protected static final String _259 = "e.scrollIntoView();";
  protected static final String _260 = "e.style.display = 'block';";
  protected static final String _261 = "e.style.display = 'inline';";
  protected static final String _262 = "e.style.display = 'none';";
  protected static final String _263 = "e.title= 'Collapse All';";
  protected static final String _264 = "e.title= 'Expand All';";
  protected static final String _265 = "em; text-indent: -4em;\">";
  protected static final String _266 = "em;\">";
  protected static final String _267 = "f.innerHTML = '&#x25B7;';";
  protected static final String _268 = "f.innerHTML = '&#x25E2;';";
  protected static final String _269 = "font-family: monospace;";
  protected static final String _270 = "font-size: 125%;";
  protected static final String _271 = "font-size: 80%;";
  protected static final String _272 = "font-size: 90%;";
  protected static final String _273 = "for (var i = 0; i < ius.length; i++) {";
  protected static final String _274 = "function clickOnButton(id) {";
  protected static final String _275 = "function clickOnToggleButton(id) {";
  protected static final String _276 = "function copyToClipboard(element) {";
  protected static final String _277 = "function expand(id) {";
  protected static final String _278 = "function expand2(self, id) {";
  protected static final String _279 = "function expand3(self, id) {";
  protected static final String _280 = "function expand_collapse(id) {";
  protected static final String _281 = "function expand_collapse_inline(id) {";
  protected static final String _282 = "function filterIU(className) {";
  protected static final String _283 = "function navigateTo(id) {";
  protected static final String _284 = "function toggle(id) {";
  protected static final String _285 = "height: 2ex;";
  protected static final String _286 = "if (!targetsArray.includes(iu)) {";
  protected static final String _287 = "if (e.innerHTML == '";
  protected static final String _288 = "if (e.style.display == 'none'){";
  protected static final String _289 = "if (e.title == 'Expand All') {";
  protected static final String _290 = "if (f != null) {";
  protected static final String _291 = "if (f !=null) {";
  protected static final String _292 = "if (t.title != 'Collapse All'){";
  protected static final String _293 = "if (t.title == 'Collapse All'){";
  protected static final String _294 = "iu.style.display = 'block';";
  protected static final String _295 = "iu.style.display = 'none';";
  protected static final String _296 = "margin-bottom: -2ex;";
  protected static final String _297 = "margin-left: 0em;";
  protected static final String _298 = "margin-top: -2ex;";
  protected static final String _299 = "margin: 0px 0px 0px 0px;";
  protected static final String _300 = "padding: -2pt -2pt -2pt -2pt;";
  protected static final String _301 = "padding: 0px 0px;";
  protected static final String _302 = "span:target {";
  protected static final String _303 = "var $temp = $(\"<input>\");";
  protected static final String _304 = "var e = document.getElementById(id);";
  protected static final String _305 = "var f = document.getElementById(id+\"_arrows\");";
  protected static final String _306 = "var iu = ius[i];";
  protected static final String _307 = "var ius = document.getElementsByClassName('iu-li');";
  protected static final String _308 = "var state = e.innerHTML;";
  protected static final String _309 = "var t = document.getElementById('all');";
  protected static final String _310 = "var t = document.getElementById(self);";
  protected static final String _311 = "var targets = document.getElementsByClassName(className);";
  protected static final String _312 = "var targetsArray = [].slice.call(targets);";
  protected static final String _313 = "white-space: nowrap;";
  protected static final String _314 = "white-space: pre;";
  protected static final String _315 = "width: 2ex;";
  protected static final String _316 = "}";
  protected static final String _317 = "} else {";
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
  protected final String _318 = _49 + NL + _132 + NL + _130 + NL_1 + _159 + NL_1 + _192;
  protected final String _319 = _73 + NL_1 + _161 + NL_1 + _160 + NL_1 + _155 + NL_1 + _157 + NL_1 + _156 + NL_1 + _173;
  protected final String _320 = NL_1 + _191 + NL + NL + _29 + NL_1 + _313 + NL + _316 + NL + NL + _26 + NL_1 + _272 + NL + _316 + NL + NL + _25 + NL_1 + _315 + NL_1 + _285 + NL + _316 + NL + NL + _302 + NL_1 + _270 + NL_1 + _244 + NL + _316 + NL + NL + _38 + NL_1 + _314 + NL_1 + _245 + NL_1 + _301 + NL_1 + _298 + NL_1 + _296 + NL_1 + _297 + NL + _316 + NL + NL + _22 + NL_1 + _247 + NL_1 + _272 + NL + _316 + NL + NL + _37 + NL_1 + _314 + NL_1 + _245 + NL_1 + _301 + NL_1 + _298 + NL_1 + _296 + NL_1 + _297 + NL + _316 + NL + NL + _39 + NL_1 + _253 + NL_1 + _269 + NL_1 + _271 + NL + _316 + NL + NL + _33 + NL_1 + _249 + NL_1 + _269 + NL_1 + _271 + NL + _316 + NL + NL + _35 + NL_1 + _250 + NL_1 + _269 + NL_1 + _271 + NL + _316 + NL + NL + _34 + NL_1 + _246 + NL_1 + _272 + NL + _316 + NL + NL + _36 + NL_1 + _251 + NL_1 + _272 + NL + _316 + NL + NL + _23 + NL_1 + _243 + NL_1 + _245 + NL_1 + _301 + NL + _316 + NL + NL + _24 + NL_1 + _243 + NL + _316 + NL + NL + _27 + NL_1 + _300 + NL_1 + _299 + NL + _316 + NL + NL + _28 + NL_1 + _252 + NL_1 + _272 + NL + _316 + NL + NL + _32 + NL_1 + _254 + NL_1 + _272 + NL + _316 + NL + NL + _31 + NL_1 + _272 + NL + _316 + NL + NL + _30 + NL_1 + _248 + NL_1 + _272 + NL + _316 + NL_1 + _72 + NL + _60 + NL + NL_1 + _84 + NL_2 + _174 + NL_4 + _282 + NL_5 + _307 + NL_5 + _311 + NL_5 + _312 + NL_5 + _273 + NL_7 + _306 + NL_7 + _286 + NL_9 + _295 + NL_7 + _317 + NL_9 + _294 + NL_7 + _316 + NL_5 + _316 + NL_4 + _316 + NL + NL_4 + _276 + NL_5 + _303 + NL_5 + _9 + NL_5 + _11 + NL_5 + _255 + NL_5 + _10 + NL_4 + _316 + NL + NL_4 + _274 + NL_5 + _304 + NL_5 + _256 + NL_4 + _316 + NL + NL_4 + _275 + NL_5 + _304 + NL_5 + _308 + NL_5 + _287;
  protected final String _321 = _16 + NL_7 + _256 + NL_5 + _316 + NL_4 + _316 + NL + NL_4 + _283 + NL_5 + _304 + NL_5 + _259 + NL_4 + _316 + NL + NL_4 + _284 + NL_5 + _304 + NL_5 + _289 + NL_7 + _263 + NL_7 + _258 + NL_5 + _317 + NL_7 + _264 + NL_7 + _257 + NL_5 + _316 + NL_4 + _316 + NL + NL_4 + _278 + NL_5 + _310 + NL_5 + _304 + NL_5 + _305 + NL_5 + _293 + NL_7 + _260 + NL_7 + _268 + NL_5 + _317 + NL_7 + _262 + NL_7 + _267 + NL_5 + _316 + NL_4 + _316 + NL + NL_4 + _279 + NL_5 + _310 + NL_5 + _304 + NL_5 + _305 + NL_5 + _292 + NL_7 + _262 + NL_7 + _267 + NL_5 + _316 + NL_4 + _316 + NL + NL_4 + _277 + NL_5 + _309 + NL_5 + _304 + NL_5 + _305 + NL_5 + _293 + NL_7 + _260 + NL_7 + _268 + NL_5 + _317 + NL_7 + _262 + NL_7 + _267 + NL_5 + _316 + NL_4 + _316 + NL + NL_4 + _280 + NL_5 + _304 + NL_5 + _305 + NL_5 + _288 + NL_7 + _260 + NL_7 + _268 + NL_5 + _317 + NL_7 + _262 + NL_7 + _267 + NL_5 + _316 + NL_4 + _316 + NL + NL_4 + _281 + NL_5 + _304 + NL_5 + _305 + NL_5 + _288 + NL_7 + _261 + NL_7 + _290 + NL_9 + _268 + NL_7 + _316 + NL_5 + _317 + NL_7 + _262 + NL_7 + _291 + NL_9 + _267 + NL_7 + _316 + NL_5 + _316 + NL_4 + _316 + NL + NL_2 + _68 + NL + NL_2 + _131 + NL_4 + _107 + NL_5 + _111 + NL_7 + _109 + NL_9 + _113 + NL_11 + _81 + NL_13 + _139 + NL_11 + _50 + NL_9 + _57 + NL_7 + _57 + NL_5 + _57 + NL_4 + _57 + NL_2 + _61;
  protected final String _322 = NL_2 + _175 + NL_4 + _107 + NL_5 + _122 + NL_5 + _112 + NL_7 + _105 + NL_9 + _162 + NL_11 + _153 + NL_11 + _154;
  protected final String _323 = NL_11 + _144;
  protected final String _324 = NL_11 + _152;
  protected final String _325 = NL_9 + _65 + NL_7 + _57 + NL_7 + _106 + NL_5 + _57 + NL_4 + _57 + NL_2 + _69 + NL + NL_2 + _158 + NL_2 + _110 + NL;
  protected final String _326 = NL_4 + _46 + NL_4 + _82 + NL_5 + _202 + NL_7 + _147 + NL_9 + _78 + NL_7 + _63 + NL_7 + _147 + NL_9 + _77 + NL_7 + _63;
  protected final String _327 = NL_7 + _147 + NL_9 + _75;
  protected final String _328 = _50 + NL_7 + _63;
  protected final String _329 = NL_7 + _147 + NL_9 + _194 + NL_9 + _76;
  protected final String _330 = NL_7 + _146 + NL_9 + _193;
  protected final String _331 = NL_9 + _194;
  protected final String _332 = NL_9 + _79;
  protected final String _333 = NL_9 + _50 + NL_7 + _63;
  protected final String _334 = NL_5 + _74 + NL_4 + _53 + NL + NL_4 + _117 + NL_5 + _118;
  protected final String _335 = NL_7 + _121;
  protected final String _336 = NL_7 + _121 + NL_9 + _138;
  protected final String _337 = NL_9 + _218;
  protected final String _338 = NL_7 + _58;
  protected final String _339 = NL_7 + _163;
  protected final String _340 = NL_9 + _83;
  protected final String _341 = NL_9 + _86;
  protected final String _342 = _54 + NL_7 + _66;
  protected final String _343 = NL_7 + _164 + NL_9 + _104 + NL_9 + _178;
  protected final String _344 = _70 + NL_7 + _66 + NL_7 + _85;
  protected final String _345 = NL_7 + _140;
  protected final String _346 = NL_8 + _168 + NL_8 + _170;
  protected final String _347 = NL_8 + _167 + NL_8 + _208;
  protected final String _348 = NL_10 + _152;
  protected final String _349 = NL_8 + _74;
  protected final String _350 = NL_8 + _170;
  protected final String _351 = NL_8 + _129 + NL_8 + _165;
  protected final String _352 = NL_8 + _127 + NL_8 + _165;
  protected final String _353 = NL_8 + _125;
  protected final String _354 = NL_10 + _120 + NL_12 + _138;
  protected final String _355 = _232 + NL_12 + _187 + NL_10 + _57;
  protected final String _356 = NL_10 + _108;
  protected final String _357 = _266 + NL_12 + _135;
  protected final String _358 = NL_12 + _177;
  protected final String _359 = NL_10 + _57;
  protected final String _360 = NL_8 + _124;
  protected final String _361 = NL_8 + _165;
  protected final String _362 = NL_10 + _138;
  protected final String _363 = _232 + NL_10 + _181;
  protected final String _364 = _70 + NL_10 + _80;
  protected final String _365 = _50 + NL_8 + _66;
  protected final String _366 = NL_8 + _165 + NL_10 + _138;
  protected final String _367 = _232 + NL_10 + _138;
  protected final String _368 = _232 + NL_10 + _217 + NL_8 + _66;
  protected final String _369 = NL_6 + _128;
  protected final String _370 = NL_12 + _89;
  protected final String _371 = _15 + NL_12 + _138;
  protected final String _372 = _232 + NL_12 + _179;
  protected final String _373 = NL_12 + _172;
  protected final String _374 = NL_8 + _126 + NL_8 + _114;
  protected final String _375 = NL + _48;
  protected final String _376 = NL_8 + _57;
  protected final String _377 = NL_8 + _169;
  protected final String _378 = NL_10 + _222;
  protected final String _379 = NL_10 + _214 + NL_8 + _66;
  protected final String _380 = NL_8 + _123 + NL_8 + _103 + NL_8 + _137;
  protected final String _381 = _232 + NL_8 + _223;
  protected final String _382 = NL_8 + _185;
  protected final String _383 = NL_8 + _59 + NL_8 + _115;
  protected final String _384 = NL_8 + _123 + NL_8 + _100 + NL_8 + _137;
  protected final String _385 = _232 + NL_8 + _216 + NL_8 + _181;
  protected final String _386 = NL_8 + _184;
  protected final String _387 = NL_8 + _99;
  protected final String _388 = NL_8 + _59 + NL_8 + _203;
  protected final String _389 = NL_10 + _151 + NL_12 + _89;
  protected final String _390 = _15 + NL_12 + _137;
  protected final String _391 = _70 + NL_12 + _21 + NL_12 + _181;
  protected final String _392 = _70 + NL_12 + _205;
  protected final String _393 = _233 + NL_14 + _150 + NL_15 + _88;
  protected final String _394 = _85 + NL_15 + _171;
  protected final String _395 = _67 + NL_14 + _63 + NL_14 + _150 + NL_15 + _90;
  protected final String _396 = _15 + NL_15 + _213 + NL_15 + _206;
  protected final String _397 = NL_16 + _145 + NL_18 + _80;
  protected final String _398 = _233 + NL_19 + _137;
  protected final String _399 = NL_19 + _183;
  protected final String _400 = _70 + NL_18 + _50 + NL_16 + _63;
  protected final String _401 = NL_15 + _74 + NL_14 + _63 + NL_12 + _74 + NL_10 + _63;
  protected final String _402 = NL_8 + _166 + NL_8 + _123 + NL_8 + _94 + NL_8 + _137;
  protected final String _403 = _232 + NL_8 + _221 + NL_8 + _181;
  protected final String _404 = NL_8 + _93;
  protected final String _405 = NL_8 + _59 + NL_8 + _199;
  protected final String _406 = NL_8 + _151;
  protected final String _407 = NL_10 + _119 + NL_12 + _87;
  protected final String _408 = _232 + NL_12 + _186;
  protected final String _409 = _19 + NL_10 + _57;
  protected final String _410 = NL_12 + _87;
  protected final String _411 = NL_12 + _13;
  protected final String _412 = NL_12 + _134;
  protected final String _413 = NL_12 + _180;
  protected final String _414 = NL_10 + _196;
  protected final String _415 = NL_12 + _151 + NL_14 + _80;
  protected final String _416 = _50 + NL_12 + _63;
  protected final String _417 = NL_10 + _74 + NL_8 + _63;
  protected final String _418 = NL + _47;
  protected final String _419 = NL_8 + _166 + NL_8 + _123 + NL_8 + _95 + NL_8 + _137;
  protected final String _420 = _232 + NL_8 + _220 + NL_8 + _181;
  protected final String _421 = NL_8 + _59 + NL_8 + _200;
  protected final String _422 = NL_10 + _151 + NL_12 + _87;
  protected final String _423 = NL_12 + _133;
  protected final String _424 = NL_12 + _176;
  protected final String _425 = _70 + NL_12 + _181;
  protected final String _426 = _70 + NL_12 + _197;
  protected final String _427 = NL_14 + _151 + NL_15 + _80;
  protected final String _428 = _233 + NL_17 + _133;
  protected final String _429 = NL_17 + _183;
  protected final String _430 = _70 + NL_15 + _50 + NL_14 + _63;
  protected final String _431 = NL_12 + _74 + NL_10 + _63;
  protected final String _432 = NL_8 + _166 + NL_8 + _123 + NL_8 + _96 + NL_8 + _137;
  protected final String _433 = _232 + NL_8 + _212 + NL_8 + _181;
  protected final String _434 = NL_8 + _59 + NL_8 + _201;
  protected final String _435 = NL_10 + _149 + NL_13 + _80;
  protected final String _436 = _233 + NL_14 + _137;
  protected final String _437 = NL_14 + _183;
  protected final String _438 = _70 + NL_12 + _50;
  protected final String _439 = NL_12 + _98;
  protected final String _440 = NL_10 + _63;
  protected final String _441 = NL_8 + _166 + NL_8 + _123 + NL_8 + _102 + NL_8 + _136;
  protected final String _442 = _232 + NL_8 + _219 + NL_8 + _181;
  protected final String _443 = NL_8 + _101;
  protected final String _444 = NL_8 + _59 + NL_8 + _204;
  protected final String _445 = NL_10 + _149;
  protected final String _446 = NL_12 + _80;
  protected final String _447 = NL_12 + _195;
  protected final String _448 = _233 + NL_17 + _137;
  protected final String _449 = NL_12 + _74;
  protected final String _450 = NL_8 + _166 + NL_8 + _123 + NL_8 + _92 + NL_8 + _137;
  protected final String _451 = _232 + NL_8 + _211 + NL_8 + _181;
  protected final String _452 = NL_8 + _91;
  protected final String _453 = NL_8 + _59 + NL_8 + _198;
  protected final String _454 = NL_12 + _50;
  protected final String _455 = NL_8 + _123 + NL_8 + _97 + NL_8 + _137;
  protected final String _456 = _232 + NL_8 + _215 + NL_8 + _181;
  protected final String _457 = NL_8 + _59 + NL_8 + _116;
  protected final String _458 = NL_10 + _190 + NL_12 + _142;
  protected final String _459 = NL_12 + _141;
  protected final String _460 = NL_12 + _143;
  protected final String _461 = NL_10 + _70;
  protected final String _462 = NL_8 + _207;
  protected final String _463 = NL_10 + _148;
  protected final String _464 = _7 + NL_11 + _87;
  protected final String _465 = _12 + NL_11 + _80;
  protected final String _466 = _233 + NL_13 + _137;
  protected final String _467 = _232 + NL_13 + _189;
  protected final String _468 = _70 + NL_13 + _182;
  protected final String _469 = _70 + NL_11 + _50;
  protected final String _470 = NL_11 + _98;
  protected final String _471 = NL_10 + _17;
  protected final String _472 = NL_10 + _137;
  protected final String _473 = _70 + NL_10 + _18;
  protected final String _474 = NL_10 + _17 + NL_10 + _137;
  protected final String _475 = _232 + NL_10 + _137;
  protected final String _476 = _232 + NL_10 + _18;
  protected final String _477 = NL_9 + _63;
  protected final String _478 = NL_8 + _74 + NL_8 + _57;
  protected final String _479 = NL + NL_5 + _57 + NL_4 + _57 + NL_3 + _57 + NL_3 + _64 + NL_1 + _55 + NL + _62;

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
    stringBuffer.append(_318);
    stringBuffer.append(reporter.getTitle());
    stringBuffer.append(_319);
    stringBuffer.append(_320);
    stringBuffer.append('\u25B7');
    stringBuffer.append(_321);
    stringBuffer.append(_322);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() == null) {
    stringBuffer.append(_323);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_63);
    } else {
    stringBuffer.append(_324);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_233);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_52);
    }
    }
    stringBuffer.append(_325);
    stringBuffer.append(_326);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() != null) {
    stringBuffer.append(_327);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_233);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_328);
    } else {
      if (iuReport == null) {
    stringBuffer.append(_329);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_328);
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
    stringBuffer.append(_330);
    if (index != -1) {
    stringBuffer.append(_331);
    }
    stringBuffer.append(_332);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_233);
    stringBuffer.append(NL_9);
    stringBuffer.append(label);
    stringBuffer.append(_333);
    }
    }
    stringBuffer.append(_334);
    if (iuReport == null) {
    stringBuffer.append(_335);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_58);
    } else {
    stringBuffer.append(_336);
    stringBuffer.append(report.getIUImage(iuReport.getIU()));
    stringBuffer.append(_232);
    String name = report.getName(iuReport.getIU(), false);
    if (name != null) {
    stringBuffer.append(NL_9);
    stringBuffer.append(helper.htmlEscape(name, true));
    stringBuffer.append(_85);
    } else {
    stringBuffer.append(_337);
    }
    stringBuffer.append(NL_9);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_338);
    }
    stringBuffer.append(_339);
    if (report != null && report.getDate() != null) {
    stringBuffer.append(_340);
    stringBuffer.append(report.getDate());
    stringBuffer.append(_54);
    }
    stringBuffer.append(_341);
    stringBuffer.append(reporter.getNow());
    stringBuffer.append(_342);
    if (report != null) {
    stringBuffer.append(_343);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_344);
    }
    stringBuffer.append(_345);
    stringBuffer.append(reporter.getReportBrandingImage());
    stringBuffer.append(_224);
    if (indexReport != null) {
    stringBuffer.append(_346);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_231);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_51);
    Map<String, String> allReports = indexReport.getAllReports();
    if (allReports != null && !allReports.isEmpty()) {
    stringBuffer.append(_347);
    for (Map.Entry<String, String> entry : allReports.entrySet()) {
    stringBuffer.append(_348);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_233);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_52);
    }
    stringBuffer.append(_349);
    }
    } else if (iuReport != null) {
    stringBuffer.append(_350);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_231);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_51);
    String provider = iuReport.getProvider();
    if (provider != null) {
    stringBuffer.append(_351);
    stringBuffer.append(helper.htmlEscape(provider, false));
    stringBuffer.append(_66);
    }
    String description = iuReport.getDescription();
    if (description != null) {
    stringBuffer.append(_352);
    stringBuffer.append(helper.htmlEscape(description, false));
    stringBuffer.append(_66);
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iuReport.getIU());
    if (!artifacts.isEmpty()) {
      List<Certificate> certificates = report.getCertificates(iuReport.getIU());
      if (certificates != null) {
    stringBuffer.append(_353);
    if (certificates.isEmpty()) {
    stringBuffer.append(_354);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_355);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_356);
    stringBuffer.append(count++);
    stringBuffer.append(_357);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_232);
    for (Map.Entry<String, String> component : components.entrySet()) {
              String key = component.getKey();
              String value = component.getValue();
    stringBuffer.append(_358);
    stringBuffer.append(key);
    stringBuffer.append(_209);
    stringBuffer.append(value);
    stringBuffer.append(_70);
    }
    stringBuffer.append(_359);
    }
    }
    }
    stringBuffer.append(_360);
    Collection<IInstallableUnit> pluginsWithMissingPackGZ = report.getPluginsWithMissingPackGZ();
      for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
        String artifact = entry.getKey();
        Boolean signed = entry.getValue();
    stringBuffer.append(_361);
    if (signed != null) {
    stringBuffer.append(_362);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_232);
    }
    stringBuffer.append(_362);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_363);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_364);
    stringBuffer.append(report.getSiteURL() + '/' + artifact);
    stringBuffer.append(_233);
    stringBuffer.append(artifact);
    stringBuffer.append(_365);
    }
    if (pluginsWithMissingPackGZ.contains(iuReport.getIU())) {
    stringBuffer.append(_366);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_367);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_368);
    }
    }
    List<Report.LicenseDetail> licenses = report.getLicenses(iuReport.getIU());
    if (licenses != null) {
    stringBuffer.append(_369);
    for (LicenseDetail license : licenses) {
    String id = license.getUUID();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
    stringBuffer.append(_370);
    stringBuffer.append(id);
    stringBuffer.append(_238);
    stringBuffer.append(id);
    stringBuffer.append(_371);
    stringBuffer.append(report.getLicenseImage());
    stringBuffer.append(_372);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_233);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_70);
    stringBuffer.append(NL_12);
    stringBuffer.append(id);
    stringBuffer.append(_373);
    stringBuffer.append(id);
    stringBuffer.append(_230);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_233);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_188);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_233);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_71);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_233);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_70);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_67);
    }
    }
    stringBuffer.append(_374);
    for (String xml : report.getXML(iuReport.getIU(), Collections.<String, String> emptyMap())) {
    stringBuffer.append(_375);
    stringBuffer.append(xml);
    }
    stringBuffer.append(_376);
    } else {
    if (report.isRoot()) {
    stringBuffer.append(_377);
    }
    stringBuffer.append(_350);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_231);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_51);
    List<Report> children = report.getChildren();
    if (!children.isEmpty()) {
    stringBuffer.append(_361);
    if (!report.isRoot()) {
    stringBuffer.append(_378);
    }
    stringBuffer.append(_379);
    }
    String metadataXML = report.getMetadataXML();
    String artifactXML = report.getArtifactML();
    if (metadataXML != null || artifactXML != null) {
    stringBuffer.append(_380);
    stringBuffer.append(report.getRepositoryImage());
    stringBuffer.append(_381);
    if (metadataXML != null && metadataXML.contains("bad-absolute-location") || artifactXML != null && artifactXML.contains("bad-absolute-location")) {
    stringBuffer.append(_382);
    }
    stringBuffer.append(_383);
    if (metadataXML != null) {
    stringBuffer.append(_375);
    stringBuffer.append(metadataXML);
    }
    if (artifactXML != null) {
    if (metadataXML != null) {
    stringBuffer.append(NL);
    }
    stringBuffer.append(_375);
    stringBuffer.append(artifactXML);
    }
    stringBuffer.append(_376);
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
    stringBuffer.append(_384);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_385);
    stringBuffer.append(licenses.size());
    stringBuffer.append(_70);
    if (nonConformant != 0) {
    stringBuffer.append(_386);
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
    stringBuffer.append(_387);
    stringBuffer.append(displayButton);
    stringBuffer.append(_44);
    stringBuffer.append(onClick);
    stringBuffer.append(_234);
    }
    stringBuffer.append(_388);
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
    stringBuffer.append(_389);
    stringBuffer.append(id);
    stringBuffer.append(_241);
    stringBuffer.append(id);
    stringBuffer.append(_390);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_372);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_233);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_391);
    stringBuffer.append(ius.size());
    stringBuffer.append(_392);
    stringBuffer.append(id);
    stringBuffer.append(_393);
    stringBuffer.append(id);
    stringBuffer.append(_240);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    stringBuffer.append(NL_15);
    stringBuffer.append(id);
    stringBuffer.append(_394);
    stringBuffer.append(id);
    stringBuffer.append(_230);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_233);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_188);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_233);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_71);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_233);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_70);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_395);
    stringBuffer.append(id);
    stringBuffer.append(_242);
    stringBuffer.append(id);
    stringBuffer.append(_396);
    stringBuffer.append(id);
    stringBuffer.append(_233);
    for (IInstallableUnit iu : ius) {
    stringBuffer.append(_397);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_398);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_232);
    stringBuffer.append(NL_19);
    stringBuffer.append(helper.htmlEscape(report.getName(iu, true), true));
    stringBuffer.append(_399);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_400);
    }
    stringBuffer.append(_401);
    }
    }
    stringBuffer.append(_349);
    }
    Map<List<Certificate>, Map<String, IInstallableUnit>> allCertificates = report.getCertificates();
    if (!allCertificates.isEmpty()) {
      int idCount = 0;
      Map<String, IInstallableUnit> unsigned = allCertificates.get(Collections.emptyList());
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_402);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_403);
    stringBuffer.append(allCertificates.size());
    stringBuffer.append(_70);
    if (unsigned != null) {
    stringBuffer.append(_386);
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
    stringBuffer.append(_404);
    stringBuffer.append(displayButton);
    stringBuffer.append(_43);
    stringBuffer.append(onClick);
    stringBuffer.append(_234);
    }
    stringBuffer.append(_405);
    stringBuffer.append(display);
    stringBuffer.append(_41);
    for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : allCertificates.entrySet()) {
        List<Certificate> certificates = entry.getKey();
        String id = "certificates" + ++idCount;
    stringBuffer.append(_406);
    if (certificates.isEmpty()) {
    stringBuffer.append(_407);
    stringBuffer.append(id);
    stringBuffer.append(_239);
    stringBuffer.append(id);
    stringBuffer.append(_390);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_408);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_409);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_356);
    stringBuffer.append(count++ + 2);
    stringBuffer.append(_265);
    if (count == 1) {
    stringBuffer.append(_410);
    stringBuffer.append(id);
    stringBuffer.append(_239);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    } else {
    stringBuffer.append(_411);
    }
    stringBuffer.append(_412);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_232);
    if (count == 1) {
    stringBuffer.append(_413);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_20);
    }
    for (Map.Entry<String, String> component : components.entrySet()) {
              String key = component.getKey();
              String value = component.getValue();
    stringBuffer.append(_358);
    stringBuffer.append(key);
    stringBuffer.append(_209);
    stringBuffer.append(value);
    stringBuffer.append(_70);
    }
    stringBuffer.append(_359);
    }
    }
    stringBuffer.append(_414);
    stringBuffer.append(id);
    stringBuffer.append(_233);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_415);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_233);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_416);
    }
    stringBuffer.append(_417);
    }
    stringBuffer.append(_349);
    }
    stringBuffer.append(_418);
    Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
    if (!featureProviders.isEmpty()) {
      int nonEclipse = 0;
      for (String provider : featureProviders.keySet()) {
        if (!provider.toLowerCase().contains("eclipse"))
          ++nonEclipse;
      }
    stringBuffer.append(_419);
    stringBuffer.append(report.getProviderImage());
    stringBuffer.append(_420);
    stringBuffer.append(featureProviders.size());
    stringBuffer.append(_70);
    if (nonEclipse != 0) {
    stringBuffer.append(_386);
    stringBuffer.append(nonEclipse);
    stringBuffer.append(_5);
    }
    stringBuffer.append(_421);
    int count = 0;
      for (Map.Entry<String, Set<IInstallableUnit>> provider : featureProviders.entrySet()) {
        String style = !provider.getKey().toLowerCase().contains("eclipse") ? " style=\"color: FireBrick;\"" : "";
        Set<IInstallableUnit> features = provider.getValue();
        String id = "nested_feature_providers" + ++count;
    stringBuffer.append(_422);
    stringBuffer.append(id);
    stringBuffer.append(_237);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    for (String image : report.getBrandingImages(features)) {
    stringBuffer.append(_423);
    stringBuffer.append(image);
    stringBuffer.append(_232);
    }
    stringBuffer.append(_424);
    stringBuffer.append(style);
    stringBuffer.append(_210);
    stringBuffer.append(provider.getKey());
    stringBuffer.append(_425);
    stringBuffer.append(features.size());
    stringBuffer.append(_426);
    stringBuffer.append(id);
    stringBuffer.append(_229);
    for (IInstallableUnit feature : features) {
          String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_427);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_428);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_232);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_429);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_430);
    }
    stringBuffer.append(_431);
    }
    stringBuffer.append(_349);
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
    stringBuffer.append(_432);
    stringBuffer.append(report.getFeatureImage());
    stringBuffer.append(_433);
    stringBuffer.append(features.size());
    stringBuffer.append(_70);
    if (brokenBranding != 0) {
    stringBuffer.append(_386);
    stringBuffer.append(brokenBranding);
    stringBuffer.append(_1);
    }
    if (noBranding != 0) {
    stringBuffer.append(_386);
    stringBuffer.append(noBranding);
    stringBuffer.append(_4);
    }
    stringBuffer.append(_434);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_435);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_436);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_232);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_437);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_438);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        for (LicenseDetail license : report.getLicenses(feature)) {
          String id = license.getUUID();
          licenseReplacements.put(">" + id, "><button class='bb search-for-me' style='" + helper.getStyle(license) + "' onclick=\"clickOnButton('lic_" + id
              + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_439);
    stringBuffer.append(id);
    stringBuffer.append(_225);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_227);
    stringBuffer.append(id);
    stringBuffer.append(_235);
    stringBuffer.append(id);
    stringBuffer.append(_236);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_56);
    }
    stringBuffer.append(_440);
    }
    stringBuffer.append(_349);
    }
    Collection<IInstallableUnit> products = report.getProducts();
    if (!products.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_441);
    stringBuffer.append(report.getProductImage());
    stringBuffer.append(_442);
    stringBuffer.append(products.size());
    stringBuffer.append(_70);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit product : products) {
          String productID = "_product_" + report.getIUID(product);
          onClick.append("expand2('products_all_arrows', '").append(productID).append("');");
        }
    stringBuffer.append(_443);
    stringBuffer.append(displayButton);
    stringBuffer.append(_45);
    stringBuffer.append(onClick);
    stringBuffer.append(_234);
    }
    stringBuffer.append(_444);
    stringBuffer.append(display);
    stringBuffer.append(_40);
    for (IInstallableUnit product : products) {
        String productImage = report.getIUImage(product);
        String productID = "_product_" + report.getIUID(product);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(product));
    stringBuffer.append(_445);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_410);
    stringBuffer.append(productID);
    stringBuffer.append(_239);
    stringBuffer.append(productID);
    stringBuffer.append(_15);
    }
    stringBuffer.append(_446);
    stringBuffer.append(report.getRelativeIUReportURL(product));
    stringBuffer.append(_436);
    stringBuffer.append(productImage);
    stringBuffer.append(_232);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(product, true), true));
    stringBuffer.append(_437);
    stringBuffer.append(report.getVersion(product));
    stringBuffer.append(_438);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_447);
    stringBuffer.append(productID);
    stringBuffer.append(_228);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_427);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_448);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_232);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_429);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_430);
    }
    stringBuffer.append(_449);
    }
    stringBuffer.append(_440);
    }
    stringBuffer.append(_349);
    }
    Collection<IInstallableUnit> categories = report.getCategories();
    if (!categories.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_450);
    stringBuffer.append(report.getCategoryImage());
    stringBuffer.append(_451);
    stringBuffer.append(categories.size());
    stringBuffer.append(_70);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit category : categories) {
          String categoryID = "_category_" + report.getIUID(category);
          onClick.append("expand2('categories_all_arrows', '").append(categoryID).append("');");
        }
    stringBuffer.append(_452);
    stringBuffer.append(displayButton);
    stringBuffer.append(_42);
    stringBuffer.append(onClick);
    stringBuffer.append(_234);
    }
    stringBuffer.append(_453);
    stringBuffer.append(display);
    stringBuffer.append(_40);
    for (IInstallableUnit category : categories) {
        String categoryImage = report.getIUImage(category);
        String categoryID = "_category_" + report.getIUID(category);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(category));
    stringBuffer.append(_445);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_410);
    stringBuffer.append(categoryID);
    stringBuffer.append(_239);
    stringBuffer.append(categoryID);
    stringBuffer.append(_15);
    }
    stringBuffer.append(_446);
    stringBuffer.append(report.getRelativeIUReportURL(category));
    stringBuffer.append(_436);
    stringBuffer.append(categoryImage);
    stringBuffer.append(_232);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(category, true), true));
    stringBuffer.append(_454);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_447);
    stringBuffer.append(categoryID);
    stringBuffer.append(_228);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_427);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_448);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_232);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_429);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_430);
    }
    stringBuffer.append(_449);
    }
    stringBuffer.append(_440);
    }
    stringBuffer.append(_349);
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
    stringBuffer.append(_455);
    stringBuffer.append(report.getBundleImage());
    stringBuffer.append(_456);
    stringBuffer.append(ius.size());
    stringBuffer.append(_70);
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_386);
    stringBuffer.append(pluginsWithMissingPackGZ.size());
    stringBuffer.append(_3);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_386);
    stringBuffer.append(duplicateCount);
    stringBuffer.append(_8);
    }
    stringBuffer.append(_457);
    if (duplicateCount > 0 || !pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_458);
    if (duplicateCount > 0) {
    stringBuffer.append(_459);
    }
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_460);
    }
    stringBuffer.append(_461);
    }
    stringBuffer.append(_462);
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
    stringBuffer.append(_463);
    stringBuffer.append(id);
    stringBuffer.append(_226);
    stringBuffer.append(classNames);
    stringBuffer.append(_464);
    stringBuffer.append(id);
    stringBuffer.append(_239);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    stringBuffer.append(_465);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_466);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_467);
    stringBuffer.append(iuID);
    stringBuffer.append(_468);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_233);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_469);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        if (report.isFeature(iu)) {
          for (LicenseDetail license : report.getLicenses(iu)) {
            String licenseID = license.getUUID();
            licenseReplacements.put(">" + licenseID, "><button class='bb search-for-me' style='" + helper.getStyle(license)
                + "' onclick=\"clickOnButton('lic_" + licenseID + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_470);
    stringBuffer.append(licenseID);
    stringBuffer.append(_225);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_227);
    stringBuffer.append(licenseID);
    stringBuffer.append(_235);
    stringBuffer.append(licenseID);
    stringBuffer.append(_236);
    stringBuffer.append(licenseID);
    stringBuffer.append(_14);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_56);
    }
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iu);
        for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
          String artifact = entry.getKey();
          Boolean signed = entry.getValue();
    stringBuffer.append(_471);
    if (signed != null) {
    stringBuffer.append(_472);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_232);
    }
    stringBuffer.append(_472);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_363);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_473);
    }
    if (pluginsWithMissingPackGZ.contains(iu)) {
    stringBuffer.append(_474);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_475);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_476);
    }
    stringBuffer.append(_477);
    }
    stringBuffer.append(_478);
    }
    }
    stringBuffer.append(_479);
    return stringBuffer.toString();
  }
}
