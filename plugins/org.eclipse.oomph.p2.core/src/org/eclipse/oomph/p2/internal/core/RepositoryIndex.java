package org.eclipse.oomph.p2.internal.core;

import java.security.cert.Certificate;
import java.util.*;
import org.eclipse.equinox.p2.metadata.*;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.Report;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.Report.LicenseDetail;

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
  protected static final String _7 = " class=\"separator\"";
  protected static final String _8 = " font-smaller\">";
  protected static final String _9 = " with Multiple Versions)</span>";
  protected static final String _10 = "$(\"body\").append($temp);";
  protected static final String _11 = "$temp.remove();";
  protected static final String _12 = "$temp.val($(element).text()).select();";
  protected static final String _13 = "&bull;</button>";
  protected static final String _14 = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  protected static final String _15 = "');\">";
  protected static final String _16 = "');\">&#x25B7;</button>";
  protected static final String _17 = "')\">&#x270e;</button>";
  protected static final String _18 = "'){";
  protected static final String _19 = "(";
  protected static final String _20 = ")";
  protected static final String _21 = ") Unsigned</span>";
  protected static final String _22 = ")</span>";
  protected static final String _23 = "-";
  protected static final String _24 = ".bb {";
  protected static final String _25 = ".filter {";
  protected static final String _26 = ".fit-image {";
  protected static final String _27 = ".font-smaller {";
  protected static final String _28 = ".iu-link {";
  protected static final String _29 = ".resolved-requirement {";
  protected static final String _30 = ".text-nowrap {";
  protected static final String _31 = ".unresolved-requirement {";
  protected static final String _32 = ".xml-attribute {";
  protected static final String _33 = ".xml-attribute-value {";
  protected static final String _34 = ".xml-element {";
  protected static final String _35 = ".xml-element-value {";
  protected static final String _36 = ".xml-iu {";
  protected static final String _37 = ".xml-token {";
  protected static final String _38 = "; margin-left: -1em; list-style-type: none; padding: 0; margin: 0;\">";
  protected static final String _39 = "; margin-left: -1em; list-style-type: none;\">";
  protected static final String _40 = ";\" onclick=\"toggle('categories_all_arrows');";
  protected static final String _41 = ";\" onclick=\"toggle('certificates_all_arrows');";
  protected static final String _42 = ";\" onclick=\"toggle('licenses_all_arrows');";
  protected static final String _43 = ";\" onclick=\"toggle('products_all_arrows');";
  protected static final String _44 = "<!-- navigation sidebar -->";
  protected static final String _45 = "<!--- providers -->";
  protected static final String _46 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
  protected static final String _47 = "</a>";
  protected static final String _48 = "</a></h3>";
  protected static final String _49 = "</a></li>";
  protected static final String _50 = "</aside>";
  protected static final String _51 = "</b>";
  protected static final String _52 = "</body>";
  protected static final String _53 = "</button>";
  protected static final String _54 = "</div>";
  protected static final String _55 = "</h2>";
  protected static final String _56 = "</h3>";
  protected static final String _57 = "</head>";
  protected static final String _58 = "</header>";
  protected static final String _59 = "</html>";
  protected static final String _60 = "</li>";
  protected static final String _61 = "</main>";
  protected static final String _62 = "</ol>";
  protected static final String _63 = "</p>";
  protected static final String _64 = "</pre>";
  protected static final String _65 = "</script>";
  protected static final String _66 = "</section>";
  protected static final String _67 = "</span>";
  protected static final String _68 = "</span><span style=\"color: green; text-decoration: underline; ";
  protected static final String _69 = "</style>";
  protected static final String _70 = "</title>";
  protected static final String _71 = "</ul>";
  protected static final String _72 = "<a class=\"separator\" href=\"";
  protected static final String _73 = "<a class=\"separator\" href=\"https://www.eclipse.org/downloads\">Home</a>";
  protected static final String _74 = "<a href=\"";
  protected static final String _75 = "<a href=\"https://www.eclipse.org/\">";
  protected static final String _76 = "<aside id=\"leftcol\" class=\"col-md-4 font-smaller\">";
  protected static final String _77 = "<b>Built: ";
  protected static final String _78 = "<body id=\"body_solstice\">";
  protected static final String _79 = "<br/>";
  protected static final String _80 = "<br/><b>Reported: ";
  protected static final String _81 = "<button id=\"";
  protected static final String _82 = "<button id=\"_";
  protected static final String _83 = "<button id=\"__";
  protected static final String _84 = "<button id=\"_f";
  protected static final String _85 = "<button id=\"categories_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _86 = "<button id=\"categories_arrows\" class=\"orange bb\" onclick=\"expand_collapse('categories'); expand_collapse_inline('categories_all_arrows');\">&#x25B7;</button>";
  protected static final String _87 = "<button id=\"certificates_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _88 = "<button id=\"certificates_arrows\" class=\"orange bb\" onclick=\"expand_collapse('certificates'); expand_collapse_inline('certificates_all_arrows');\">&#x25B7;</button>";
  protected static final String _89 = "<button id=\"duplicates\" class=\"filter\" onclick=\"filterIU('duplicate');\">Duplicates</button>";
  protected static final String _90 = "<button id=\"feature_providers_arrows\" class=\"orange bb\" onclick=\"expand_collapse('feature_providers');\">&#x25B7;</button>";
  protected static final String _91 = "<button id=\"features_arrows\" class=\"orange bb\" onclick=\"expand_collapse('features');\">&#x25B7;</button>";
  protected static final String _92 = "<button id=\"filter\" class=\"filter\" onclick=\"filterIU('iu-li');\">All</button>";
  protected static final String _93 = "<button id=\"ius_arrows\" class=\"orange bb\" onclick=\"expand_collapse('ius');expand_collapse('ius_details');\">&#x25B7;</button>";
  protected static final String _94 = "<button id=\"lic_";
  protected static final String _95 = "<button id=\"licenses_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _96 = "<button id=\"licenses_arrows\" class=\"orange bb\" onclick=\"expand_collapse('licenses'); expand_collapse_inline('licenses_all_arrows');\">&#x25B7;</button>";
  protected static final String _97 = "<button id=\"missing\" class=\"filter\" onclick=\"filterIU('missing');\">Missing .pack.gz</button>";
  protected static final String _98 = "<button id=\"products_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _99 = "<button id=\"products_arrows\" class=\"orange bb\" onclick=\"expand_collapse('products'); expand_collapse_inline('products_all_arrows');\">&#x25B7;</button>";
  protected static final String _100 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 125%;\" onclick=\"copyToClipboard('#";
  protected static final String _101 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 150%;\" onclick=\"copyToClipboard('#p1')\">&#x270e;</button>";
  protected static final String _102 = "<div class=\"col-sm-16 padding-left-30\">";
  protected static final String _103 = "<div class=\"col-sm-8 margin-top-15\"></div>";
  protected static final String _104 = "<div class=\"container\">";
  protected static final String _105 = "<div class=\"font-smaller\" style=\"margin-left: ";
  protected static final String _106 = "<div class=\"hidden-xs col-sm-8 col-md-6 col-lg-5\" id=\"header-left\">";
  protected static final String _107 = "<div class=\"novaContent container\" id=\"novaContent\">";
  protected static final String _108 = "<div class=\"row\" id=\"header-row\">";
  protected static final String _109 = "<div class=\"row\">";
  protected static final String _110 = "<div class=\"wrapper-logo-default\">";
  protected static final String _111 = "<div id=\"ius\" style=\"display:none;\">";
  protected static final String _112 = "<div id=\"maincontent\">";
  protected static final String _113 = "<div id=\"midcolumn\" style=\"width: 70%;\">";
  protected static final String _114 = "<div style=\"float: right;\"><tt class=\"orange\">&#xab;</tt></div>";
  protected static final String _115 = "<div style=\"text-indent: -2em\">";
  protected static final String _116 = "<h2 style=\"text-align: center;\">";
  protected static final String _117 = "<h3 class=\"sr-only\">Breadcrumbs</h3>";
  protected static final String _118 = "<h3 style=\"font-weight: bold;\">";
  protected static final String _119 = "<h3 style=\"font-weight: bold;\"><a href=\"";
  protected static final String _120 = "<head>";
  protected static final String _121 = "<header role=\"banner\" id=\"header-wrapper\">";
  protected static final String _122 = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">";
  protected static final String _123 = "<img alt=\"Branding Image\" class=\"fit-image\" src=\"";
  protected static final String _124 = "<img alt=\"Signed\" class=\"fit-image\" src=\"";
  protected static final String _125 = "<img class=\"fit-image> src=\"";
  protected static final String _126 = "<img class=\"fit-image\" src=\"";
  protected static final String _127 = "<img class=\"logo-eclipse-default img-responsive hidden-xs\" alt=\"Eclipse Log\" src=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/logo/eclipse-426x100.png\"/>";
  protected static final String _128 = "<img style=\"float:right\" src=\"https://wiki.eclipse.org/images/d/dc/Oomph_Project_Logo.png\" alt=\"\"/>";
  protected static final String _129 = "<li";
  protected static final String _130 = "<li class=\"active\">";
  protected static final String _131 = "<li class=\"font-smaller text-nowrap\">";
  protected static final String _132 = "<li class=\"separator\">";
  protected static final String _133 = "<li id=\"_iu_";
  protected static final String _134 = "<li style=\"font-size: 100%; white-space: nowrap;\">";
  protected static final String _135 = "<li style=\"margin-left: 1em;\">";
  protected static final String _136 = "<li>";
  protected static final String _137 = "<li><a href=\"";
  protected static final String _138 = "<li><a href=\"https://www.eclipse.org/\">Home</a></li>";
  protected static final String _139 = "<li><a href=\"https://www.eclipse.org/downloads/\">Downloads</a></li>";
  protected static final String _140 = "<link href=\"//fonts.googleapis.com/css?family=Open+Sans:400,700,300,600,100\" rel=\"stylesheet\" type=\"text/css\"/>";
  protected static final String _141 = "<link rel=\"icon\" type=\"image/ico\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/favicon.ico\"/>";
  protected static final String _142 = "<link rel=\"stylesheet\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/stylesheets/styles.min.css\"/>";
  protected static final String _143 = "<main class=\"no-promo\">";
  protected static final String _144 = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
  protected static final String _145 = "<meta name=\"description\" content=\"Update Sites Reports\"/>";
  protected static final String _146 = "<meta name=\"keywords\" content=\"eclipse,update site\"/>";
  protected static final String _147 = "<ol class=\"breadcrumb\">";
  protected static final String _148 = "<p style=\"font-size: 125%; text-align: center;\">";
  protected static final String _149 = "<p style=\"margin-left: 1em\">";
  protected static final String _150 = "<p style=\"text-align: center;\">";
  protected static final String _151 = "<p>";
  protected static final String _152 = "<p></p>";
  protected static final String _153 = "<p>These reports are produced by <a href=\"https://ci.eclipse.org/oomph/\" target=\"oomph_jipp\">https://ci.eclipse.org/oomph/</a>.</p>";
  protected static final String _154 = "<p>This is the generated report index.</p>";
  protected static final String _155 = "<pre id=\"_";
  protected static final String _156 = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";
  protected static final String _157 = "<script>";
  protected static final String _158 = "<section class=\"hidden-print default-breadcrumbs\" id=\"breadcrumb\">";
  protected static final String _159 = "<span ";
  protected static final String _160 = "<span clas=\"text-nowrap\"><span style=\"color: SteelBlue; font-size: 75%;\">";
  protected static final String _161 = "<span id=\"p1\" style=\"font-size: 125%\">";
  protected static final String _162 = "<span style=\"";
  protected static final String _163 = "<span style=\"color: DarkCyan; font-size: 110%;\">(";
  protected static final String _164 = "<span style=\"color: DarkCyan;\">";
  protected static final String _165 = "<span style=\"color: DarkOliveGreen;";
  protected static final String _166 = "<span style=\"color: DarkOliveGreen;\">";
  protected static final String _167 = "<span style=\"color: FireBrick; font-size: 60%;\">(";
  protected static final String _168 = "<span style=\"color: FireBrick;\">(";
  protected static final String _169 = "<span style=\"color: red; text-decoration: line-through; ";
  protected static final String _170 = "<span style=\"font-size:100%;\">";
  protected static final String _171 = "<span style=\"margin-left: 1em;\">";
  protected static final String _172 = "<style>";
  protected static final String _173 = "<title>";
  protected static final String _174 = "<tt class=\"orange\">&#xbb;</tt>";
  protected static final String _175 = "<ul class=\"font-smaller\" id=\"";
  protected static final String _176 = "<ul class=\"font-smaller\" style=\"display:none; margin-left: -2em;\" id=\"";
  protected static final String _177 = "<ul id=\"";
  protected static final String _178 = "<ul id=\"categories\" style=\"display: ";
  protected static final String _179 = "<ul id=\"certificates\" style=\"display:";
  protected static final String _180 = "<ul id=\"feature_providers\" style=\"display:none; margin-left: -1em; list-style-type: none;\">";
  protected static final String _181 = "<ul id=\"features\" style=\"display: none;\">";
  protected static final String _182 = "<ul id=\"leftnav\" class=\"ul-left-nav fa-ul hidden-print\">";
  protected static final String _183 = "<ul id=\"licenses\" style=\"display: ";
  protected static final String _184 = "<ul id=\"products\" style=\"display: ";
  protected static final String _185 = "<ul style=\"display:none; list-style-type: none; padding: 0; margin: 0;\" id=\"__";
  protected static final String _186 = "<ul style=\"display:none; padding: 0; margin: 0; margin-left: 3em;\" id=\"_f";
  protected static final String _187 = "<ul style=\"margin-left: -1em; list-style-type: none; padding: 0; margin: 0;\">";
  protected static final String _188 = "=</span>";
  protected static final String _189 = ">";
  protected static final String _190 = "Categories";
  protected static final String _191 = "Features";
  protected static final String _192 = "Filters:";
  protected static final String _193 = "In addition to this composite report, reports are also generated for each of the following composed children:";
  protected static final String _194 = "Installable Units";
  protected static final String _195 = "Licenses";
  protected static final String _196 = "Products";
  protected static final String _197 = "Providers";
  protected static final String _198 = "Signing Certificates";
  protected static final String _199 = "This is a composite update site.";
  protected static final String _200 = "This is a generated report for the following p2 update site:";
  protected static final String _201 = "\" class=\"bb\" style=\"";
  protected static final String _202 = "\" class=\"iu-li";
  protected static final String _203 = "\" id=\"";
  protected static final String _204 = "\" onclick=\"clickOnToggleButton('licenses_arrows'); clickOnToggleButton('__";
  protected static final String _205 = "\" style=\"display: none;\">";
  protected static final String _206 = "\" style=\"display:none; margin-left: -1em; list-style-type: none;\">";
  protected static final String _207 = "\" style=\"display:none; margin-left: 2em; background-color: ";
  protected static final String _208 = "\" style=\"font-weight: normal;\">";
  protected static final String _209 = "\"/>";
  protected static final String _210 = "\">";
  protected static final String _211 = "\">&#x25B7;</button>";
  protected static final String _212 = "_arrows'); clickOnToggleButton('_";
  protected static final String _213 = "_arrows'); navigateTo('_";
  protected static final String _214 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('";
  protected static final String _215 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('";
  protected static final String _216 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_";
  protected static final String _217 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('__";
  protected static final String _218 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_f";
  protected static final String _219 = "background-color: white;";
  protected static final String _220 = "border: none;";
  protected static final String _221 = "color: DarkSlateGray;";
  protected static final String _222 = "color: IndianRed;";
  protected static final String _223 = "color: MediumAquaMarine;";
  protected static final String _224 = "color: MediumOrchid;";
  protected static final String _225 = "color: SaddleBrown;";
  protected static final String _226 = "color: SeaGreen;";
  protected static final String _227 = "color: SteelBlue;";
  protected static final String _228 = "document.execCommand(\"copy\");";
  protected static final String _229 = "e.click();";
  protected static final String _230 = "e.innerHTML = '&#x25B7;';";
  protected static final String _231 = "e.innerHTML = '&#x25E2;';";
  protected static final String _232 = "e.scrollIntoView();";
  protected static final String _233 = "e.style.display = 'block';";
  protected static final String _234 = "e.style.display = 'inline';";
  protected static final String _235 = "e.style.display = 'none';";
  protected static final String _236 = "e.title= 'Collapse All';";
  protected static final String _237 = "e.title= 'Expand All';";
  protected static final String _238 = "em; text-indent: -4em;\">";
  protected static final String _239 = "f.innerHTML = '&#x25B7;';";
  protected static final String _240 = "f.innerHTML = '&#x25E2;';";
  protected static final String _241 = "font-family: monospace;";
  protected static final String _242 = "font-size: 80%;";
  protected static final String _243 = "font-size: 90%;";
  protected static final String _244 = "for (var i = 0; i < ius.length; i++) {";
  protected static final String _245 = "function clickOnButton(id) {";
  protected static final String _246 = "function clickOnToggleButton(id) {";
  protected static final String _247 = "function copyToClipboard(element) {";
  protected static final String _248 = "function expand(id) {";
  protected static final String _249 = "function expand2(self, id) {";
  protected static final String _250 = "function expand3(self, id) {";
  protected static final String _251 = "function expand_collapse(id) {";
  protected static final String _252 = "function expand_collapse_inline(id) {";
  protected static final String _253 = "function filterIU(className) {";
  protected static final String _254 = "function navigateTo(id) {";
  protected static final String _255 = "function toggle(id) {";
  protected static final String _256 = "height: 2ex;";
  protected static final String _257 = "if (!targetsArray.includes(iu)) {";
  protected static final String _258 = "if (e.innerHTML == '";
  protected static final String _259 = "if (e.style.display == 'none'){";
  protected static final String _260 = "if (e.title == 'Expand All') {";
  protected static final String _261 = "if (f != null) {";
  protected static final String _262 = "if (f !=null) {";
  protected static final String _263 = "if (t.title != 'Collapse All'){";
  protected static final String _264 = "if (t.title == 'Collapse All'){";
  protected static final String _265 = "iu.style.display = 'block';";
  protected static final String _266 = "iu.style.display = 'none';";
  protected static final String _267 = "margin-bottom: -2ex;";
  protected static final String _268 = "margin-left: 2em;";
  protected static final String _269 = "margin-top: -2ex;";
  protected static final String _270 = "margin: 0px 0px 0px 0px;";
  protected static final String _271 = "padding: -2pt -2pt -2pt -2pt;";
  protected static final String _272 = "padding: 0px 0px;";
  protected static final String _273 = "var $temp = $(\"<input>\");";
  protected static final String _274 = "var e = document.getElementById(id);";
  protected static final String _275 = "var f = document.getElementById(id+\"_arrows\");";
  protected static final String _276 = "var iu = ius[i];";
  protected static final String _277 = "var ius = document.getElementsByClassName('iu-li');";
  protected static final String _278 = "var state = e.innerHTML;";
  protected static final String _279 = "var t = document.getElementById('all');";
  protected static final String _280 = "var t = document.getElementById(self);";
  protected static final String _281 = "var targets = document.getElementsByClassName(className);";
  protected static final String _282 = "var targetsArray = [].slice.call(targets);";
  protected static final String _283 = "white-space: nowrap;";
  protected static final String _284 = "white-space: pre;";
  protected static final String _285 = "width: 2ex;";
  protected static final String _286 = "}";
  protected static final String _287 = "} else {";
  protected final String NL_1 = NL + "  ";
  protected final String NL_2 = NL + "    ";
  protected final String NL_3 = NL + "     ";
  protected final String NL_4 = NL + "      ";
  protected final String NL_5 = NL + "        ";
  protected final String NL_6 = NL + "          ";
  protected final String NL_7 = NL + "           ";
  protected final String NL_8 = NL + "            ";
  protected final String NL_9 = NL + "             ";
  protected final String NL_10 = NL + "              ";
  protected final String NL_11 = NL + "               ";
  protected final String NL_12 = NL + "                ";
  protected final String NL_13 = NL + "                 ";
  protected final String NL_14 = NL + "                   ";
  protected final String NL_15 = NL + "                    ";
  protected final String _288 = _46 + NL + _122 + NL + _120 + NL_1 + _144 + NL_1 + _173;
  protected final String _289 = _70 + NL_1 + _146 + NL_1 + _145 + NL_1 + _140 + NL_1 + _142 + NL_1 + _141 + NL_1 + _156;
  protected final String _290 = NL_1 + _172 + NL + NL + _30 + NL_1 + _283 + NL + _286 + NL + NL + _27 + NL_1 + _243 + NL + _286 + NL + NL + _26 + NL_1 + _285 + NL_1 + _256 + NL + _286 + NL + NL + _36 + NL_1 + _284 + NL_1 + _220 + NL_1 + _272 + NL_1 + _269 + NL_1 + _267 + NL_1 + _268 + NL + _286 + NL + NL + _37 + NL_1 + _227 + NL_1 + _241 + NL_1 + _242 + NL + _286 + NL + NL + _32 + NL_1 + _223 + NL_1 + _241 + NL_1 + _242 + NL + _286 + NL + NL + _34 + NL_1 + _224 + NL_1 + _241 + NL_1 + _242 + NL + _286 + NL + NL + _33 + NL_1 + _221 + NL_1 + _243 + NL + _286 + NL + NL + _35 + NL_1 + _225 + NL_1 + _243 + NL + _286 + NL + NL + _24 + NL_1 + _219 + NL_1 + _220 + NL_1 + _272 + NL + _286 + NL + NL + _25 + NL_1 + _219 + NL + _286 + NL + NL + _28 + NL_1 + _271 + NL_1 + _270 + NL + _286 + NL + NL + _29 + NL_1 + _226 + NL_1 + _243 + NL + _286 + NL + NL + _31 + NL_1 + _222 + NL_1 + _243 + NL + _286 + NL_1 + _69 + NL + _57 + NL + NL_1 + _78 + NL_2 + _157 + NL_4 + _253 + NL_5 + _277 + NL_5 + _281 + NL_5 + _282 + NL_5 + _244 + NL_6 + _276 + NL_6 + _257 + NL_8 + _266 + NL_6 + _287 + NL_8 + _265 + NL_6 + _286 + NL_5 + _286 + NL_4 + _286 + NL + NL_4 + _247 + NL_5 + _273 + NL_5 + _10 + NL_5 + _12 + NL_5 + _228 + NL_5 + _11 + NL_4 + _286 + NL + NL_4 + _245 + NL_5 + _274 + NL_5 + _229 + NL_4 + _286 + NL + NL_4 + _246 + NL_5 + _274 + NL_5 + _278 + NL_5 + _258;
  protected final String _291 = _18 + NL_6 + _229 + NL_5 + _286 + NL_4 + _286 + NL + NL_4 + _254 + NL_5 + _274 + NL_5 + _232 + NL_4 + _286 + NL + NL_4 + _255 + NL_5 + _274 + NL_5 + _260 + NL_6 + _236 + NL_6 + _231 + NL_5 + _287 + NL_6 + _237 + NL_6 + _230 + NL_5 + _286 + NL_4 + _286 + NL + NL_4 + _249 + NL_5 + _280 + NL_5 + _274 + NL_5 + _275 + NL_5 + _264 + NL_6 + _233 + NL_6 + _240 + NL_5 + _287 + NL_6 + _235 + NL_6 + _239 + NL_5 + _286 + NL_4 + _286 + NL + NL_4 + _250 + NL_5 + _280 + NL_5 + _274 + NL_5 + _275 + NL_5 + _263 + NL_6 + _235 + NL_6 + _239 + NL_5 + _286 + NL_4 + _286 + NL + NL_4 + _248 + NL_5 + _279 + NL_5 + _274 + NL_5 + _275 + NL_5 + _264 + NL_6 + _233 + NL_6 + _240 + NL_5 + _287 + NL_6 + _235 + NL_6 + _239 + NL_5 + _286 + NL_4 + _286 + NL + NL_4 + _251 + NL_5 + _274 + NL_5 + _275 + NL_5 + _259 + NL_6 + _233 + NL_6 + _240 + NL_5 + _287 + NL_6 + _235 + NL_6 + _239 + NL_5 + _286 + NL_4 + _286 + NL + NL_4 + _252 + NL_5 + _274 + NL_5 + _275 + NL_5 + _259 + NL_6 + _234 + NL_6 + _261 + NL_8 + _240 + NL_6 + _286 + NL_5 + _287 + NL_6 + _235 + NL_6 + _262 + NL_8 + _239 + NL_6 + _286 + NL_5 + _286 + NL_4 + _286 + NL + NL_2 + _65 + NL + NL_2 + _121 + NL_4 + _104 + NL_5 + _108 + NL_6 + _106 + NL_8 + _110 + NL_10 + _75 + NL_12 + _127 + NL_10 + _47 + NL_8 + _54 + NL_6 + _54 + NL_5 + _54 + NL_4 + _54 + NL_2 + _58;
  protected final String _292 = NL_2 + _158 + NL_4 + _104 + NL_5 + _117 + NL_5 + _109 + NL_6 + _102 + NL_8 + _147 + NL_10 + _138 + NL_10 + _139;
  protected final String _293 = NL_10 + _130;
  protected final String _294 = NL_10 + _137;
  protected final String _295 = NL_8 + _62 + NL_6 + _54 + NL_6 + _103 + NL_5 + _54 + NL_4 + _54 + NL_2 + _66 + NL + NL_2 + _143 + NL_2 + _107 + NL;
  protected final String _296 = NL_4 + _44 + NL_4 + _76 + NL_5 + _182 + NL_6 + _132 + NL_7 + _73 + NL_6 + _60;
  protected final String _297 = NL_6 + _129;
  protected final String _298 = NL_8 + _174;
  protected final String _299 = NL_8 + _72;
  protected final String _300 = NL_8 + _114;
  protected final String _301 = NL_8 + _47 + NL_6 + _60;
  protected final String _302 = NL_5 + _71 + NL_4 + _50 + NL + NL_4 + _112 + NL_5 + _113 + NL_6 + _116;
  protected final String _303 = _55 + NL_6 + _148;
  protected final String _304 = NL_8 + _77;
  protected final String _305 = NL_8 + _80;
  protected final String _306 = _51 + NL_6 + _63;
  protected final String _307 = NL_6 + _150 + NL_8 + _101 + NL_8 + _161;
  protected final String _308 = _67 + NL_6 + _63;
  protected final String _309 = NL_6 + _79 + NL_6 + _128;
  protected final String _310 = NL_7 + _154 + NL_7 + _153;
  protected final String _311 = NL_7 + _151 + NL_9 + _200 + NL_7 + _63 + NL_7 + _149 + NL_9 + _100;
  protected final String _312 = _17 + NL_9 + _74;
  protected final String _313 = _47 + NL_7 + _63;
  protected final String _314 = NL_7 + _151;
  protected final String _315 = NL_9 + _199;
  protected final String _316 = NL_9 + _193 + NL_7 + _63;
  protected final String _317 = NL_7 + _119;
  protected final String _318 = _48 + NL_7 + _149 + NL_9 + _100;
  protected final String _319 = NL_7 + _118 + NL_7 + _96 + NL_7 + _126;
  protected final String _320 = _209 + NL_7 + _195 + NL_7 + _164;
  protected final String _321 = NL_7 + _167;
  protected final String _322 = NL_7 + _95;
  protected final String _323 = NL_7 + _56 + NL_7 + _183;
  protected final String _324 = NL_9 + _136 + NL_11 + _83;
  protected final String _325 = _16 + NL_11 + _126;
  protected final String _326 = _209 + NL_11 + _162;
  protected final String _327 = _67 + NL_11 + _23 + NL_11 + _164;
  protected final String _328 = _67 + NL_11 + _185;
  protected final String _329 = _210 + NL_13 + _135 + NL_14 + _82;
  protected final String _330 = _79 + NL_14 + _155;
  protected final String _331 = _64 + NL_13 + _60 + NL_13 + _135 + NL_14 + _84;
  protected final String _332 = _16 + NL_14 + _191 + NL_14 + _186;
  protected final String _333 = NL_15 + _131;
  protected final String _334 = NL_14 + _71 + NL_13 + _60 + NL_11 + _71 + NL_9 + _60;
  protected final String _335 = NL_7 + _71;
  protected final String _336 = NL_7 + _152 + NL_7 + _118 + NL_7 + _88 + NL_7 + _126;
  protected final String _337 = _209 + NL_7 + _198 + NL_7 + _164;
  protected final String _338 = NL_7 + _87;
  protected final String _339 = NL_7 + _56 + NL_7 + _179;
  protected final String _340 = NL_7 + _136;
  protected final String _341 = NL_9 + _115 + NL_11 + _81;
  protected final String _342 = _209 + NL_11 + _168;
  protected final String _343 = _21 + NL_9 + _54;
  protected final String _344 = NL_9 + _105;
  protected final String _345 = NL_11 + _81;
  protected final String _346 = NL_11 + _14;
  protected final String _347 = NL_11 + _124;
  protected final String _348 = NL_11 + _163;
  protected final String _349 = NL_11 + _160;
  protected final String _350 = NL_9 + _54;
  protected final String _351 = NL_9 + _176;
  protected final String _352 = NL_11 + _136;
  protected final String _353 = NL_11 + _60;
  protected final String _354 = NL_9 + _71 + NL_7 + _60;
  protected final String _355 = NL + _45;
  protected final String _356 = NL_7 + _152 + NL_7 + _118 + NL_7 + _90 + NL_7 + _126;
  protected final String _357 = _209 + NL_7 + _197 + NL_7 + _164;
  protected final String _358 = NL_7 + _56 + NL_7 + _180;
  protected final String _359 = NL_9 + _136 + NL_11 + _81;
  protected final String _360 = NL_11 + _123;
  protected final String _361 = NL_11 + _159;
  protected final String _362 = _67 + NL_11 + _164;
  protected final String _363 = _67 + NL_11 + _177;
  protected final String _364 = NL_13 + _136 + NL_14 + _123;
  protected final String _365 = NL_14 + _166;
  protected final String _366 = _67 + NL_13 + _60;
  protected final String _367 = NL_11 + _71 + NL_9 + _60;
  protected final String _368 = NL_7 + _152 + NL_7 + _118 + NL_7 + _91 + NL_7 + _126;
  protected final String _369 = _209 + NL_7 + _191 + NL_7 + _164;
  protected final String _370 = NL_7 + _56 + NL_7 + _181;
  protected final String _371 = NL_9 + _134 + NL_11 + _126;
  protected final String _372 = NL_11 + _166;
  protected final String _373 = NL_11 + _94;
  protected final String _374 = NL_9 + _60;
  protected final String _375 = NL_7 + _152 + NL_7 + _118 + NL_7 + _99 + NL_7 + _125;
  protected final String _376 = _209 + NL_7 + _196 + NL_7 + _164;
  protected final String _377 = NL_7 + _98;
  protected final String _378 = NL_7 + _56 + NL_7 + _184;
  protected final String _379 = NL_9 + _134;
  protected final String _380 = NL_11 + _126;
  protected final String _381 = NL_11 + _175;
  protected final String _382 = NL_13 + _136 + NL_14 + _126;
  protected final String _383 = NL_11 + _71;
  protected final String _384 = NL_7 + _152 + NL_7 + _118 + NL_7 + _86 + NL_7 + _126;
  protected final String _385 = _209 + NL_7 + _190 + NL_7 + _164;
  protected final String _386 = NL_7 + _85;
  protected final String _387 = NL_7 + _56 + NL_7 + _178;
  protected final String _388 = NL_7 + _118 + NL_7 + _93 + NL_7 + _126;
  protected final String _389 = _209 + NL_7 + _194 + NL_7 + _164;
  protected final String _390 = NL_7 + _56 + NL_7 + _111 + NL_9 + _171 + NL_11 + _192 + NL_11 + _92 + NL_11 + _89 + NL_11 + _97 + NL_9 + _67 + NL_7 + _187;
  protected final String _391 = NL_9 + _133;
  protected final String _392 = _8 + NL_10 + _81;
  protected final String _393 = _13 + NL_10 + _126;
  protected final String _394 = _209 + NL_10 + _170;
  protected final String _395 = _67 + NL_10 + _165;
  protected final String _396 = NL_10 + _94;
  protected final String _397 = NL_9 + _19;
  protected final String _398 = NL_9 + _126;
  protected final String _399 = _209 + NL_9 + _164;
  protected final String _400 = _67 + NL_9 + _20;
  protected final String _401 = NL_9 + _19 + NL_9 + _126;
  protected final String _402 = _209 + NL_9 + _126;
  protected final String _403 = _209 + NL_9 + _20;
  protected final String _404 = NL_8 + _60;
  protected final String _405 = NL_7 + _71 + NL_7 + _54;
  protected final String _406 = NL + NL_5 + _54 + NL_4 + _54 + NL_3 + _54 + NL_3 + _61 + NL_1 + _52 + NL + _59;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    Report report = (Report)argument;
  List<Report> children = report.getChildren();

  class Helper {
    public String getStyle(LicenseDetail license) {
      StringBuilder result = new StringBuilder();
      result.append(license.isMatchedSUA() ? "color: DarkOrange;" : "color: SaddleBrown;");
      result.append(license.isSUA() ? " font-weight: bold;" : " text-decoration: line-through;");
      return result.toString();
    }

    // public StringBuilder append(StringBuilder builder, Object object) {
    // if (builder.length() > 0) {
    // builder.append(' ');
    // }
    // builder.append(object);
    // return builder;
    // }
  }

  Helper helper = new Helper();
  Collection<IInstallableUnit> categories = report.getCategories();
    stringBuffer.append(_288);
    stringBuffer.append(report.getTitle());
    stringBuffer.append(_289);
    stringBuffer.append(_290);
    stringBuffer.append('\u25B7');
    stringBuffer.append(_291);
    stringBuffer.append(_292);
    for (Map.Entry<String, String> entry : report.getBreadcrumbs().entrySet()) {
    if (entry.getValue() == null) {
    stringBuffer.append(_293);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_60);
    }
    else {
    stringBuffer.append(_294);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_210);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_49);
    }
    }
    stringBuffer.append(_295);
    stringBuffer.append(_296);
    for (Map.Entry<String, String> entry : report.getNavigation().entrySet()) {
    String label = entry.getValue();
    boolean isTopLevel = !label.startsWith("-");
    if (!isTopLevel)
      label = label.substring(1);
    int index = label.indexOf('@');
    if (index != -1)
      label = label.substring(0, label.length() - 1);
    {
    stringBuffer.append(_297);
    if (isTopLevel) {
    stringBuffer.append(_7);
    }
    stringBuffer.append(_189);
    if (!isTopLevel) {
    stringBuffer.append(_298);
    }
    stringBuffer.append(_299);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_210);
    stringBuffer.append(NL_8);
    stringBuffer.append(label);
    if (index != -1) {
    stringBuffer.append(_300);
    }
    stringBuffer.append(_301);
    }
  }
    stringBuffer.append(_302);
    stringBuffer.append(report.getTitle());
    stringBuffer.append(_303);
    if (!report.isRoot()) {
    String date = report.getDate();
    if (date != null) {
    stringBuffer.append(_304);
    stringBuffer.append(date);
    stringBuffer.append(_51);
    }
  }
    stringBuffer.append(_305);
    stringBuffer.append(report.getNow());
    stringBuffer.append(_306);
    if (!report.isRoot()) {
    stringBuffer.append(_307);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_308);
    }
    stringBuffer.append(_309);
    if (report.isRoot()) {
    stringBuffer.append(_310);
    }
  else {
    String id = "_root_";
    stringBuffer.append(_311);
    stringBuffer.append(id);
    stringBuffer.append(_312);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_203);
    stringBuffer.append(id);
    stringBuffer.append(_208);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_313);
    }
    if (!children.isEmpty()) {
    stringBuffer.append(_314);
    if (!report.isRoot()) {
    stringBuffer.append(_315);
    }
    stringBuffer.append(_316);
    for (Report child : children) {
      String id = report.getFolderID(child.getRelativeIndexURL());
    stringBuffer.append(_317);
    stringBuffer.append(child.getRelativeIndexURL());
    stringBuffer.append(_208);
    stringBuffer.append(child.getTitle());
    stringBuffer.append(_318);
    stringBuffer.append(id);
    stringBuffer.append(_312);
    stringBuffer.append(child.getSiteURL());
    stringBuffer.append(_203);
    stringBuffer.append(id);
    stringBuffer.append(_208);
    stringBuffer.append(child.getSiteURL());
    stringBuffer.append(_313);
    }
    }
    {
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
    stringBuffer.append(_319);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_320);
    stringBuffer.append(licenses.size());
    stringBuffer.append(_67);
    if (nonConformant != 0) {
    stringBuffer.append(_321);
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
    stringBuffer.append(_322);
    stringBuffer.append(displayButton);
    stringBuffer.append(_42);
    stringBuffer.append(onClick);
    stringBuffer.append(_211);
    }
    stringBuffer.append(_323);
    stringBuffer.append(display);
    stringBuffer.append(_38);
    for (Map.Entry<LicenseDetail, Set<IInstallableUnit>> entry : licenses.entrySet()) {
        LicenseDetail license = entry.getKey();
        String id = license.getUUID();
        List<String> features = report.getFeatures(entry.getValue());
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
        {
    stringBuffer.append(_324);
    stringBuffer.append(id);
    stringBuffer.append(_217);
    stringBuffer.append(id);
    stringBuffer.append(_325);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_326);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_210);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_327);
    stringBuffer.append(features.size());
    stringBuffer.append(_328);
    stringBuffer.append(id);
    stringBuffer.append(_329);
    stringBuffer.append(id);
    stringBuffer.append(_216);
    stringBuffer.append(id);
    stringBuffer.append(_16);
    stringBuffer.append(NL_14);
    stringBuffer.append(id);
    stringBuffer.append(_330);
    stringBuffer.append(id);
    stringBuffer.append(_207);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_210);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_169);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_210);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_68);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_210);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_67);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_331);
    stringBuffer.append(id);
    stringBuffer.append(_218);
    stringBuffer.append(id);
    stringBuffer.append(_332);
    stringBuffer.append(id);
    stringBuffer.append(_210);
    for (String feature : features) {
    stringBuffer.append(_333);
    stringBuffer.append(feature.replace(" ", "&nbsp;"));
    stringBuffer.append(_60);
    }
    stringBuffer.append(_334);
    }
      }
    stringBuffer.append(_335);
    }
    {
      Map<List<Certificate>, Map<String, IInstallableUnit>> allCertificates = report.getCertificates();
      int idCount = 0;
      Map<String, IInstallableUnit> unsigned = allCertificates.get(Collections.emptyList());
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_336);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_337);
    stringBuffer.append(allCertificates.size());
    stringBuffer.append(_67);
    if (unsigned != null) {
    stringBuffer.append(_321);
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
    stringBuffer.append(_338);
    stringBuffer.append(displayButton);
    stringBuffer.append(_41);
    stringBuffer.append(onClick);
    stringBuffer.append(_211);
    }
    stringBuffer.append(_339);
    stringBuffer.append(display);
    stringBuffer.append(_39);
    for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : allCertificates.entrySet()) {
        List<Certificate> certificates = entry.getKey();
        String id = "certificates" + ++idCount;
    stringBuffer.append(_340);
    if (certificates.isEmpty()) {
    stringBuffer.append(_341);
    stringBuffer.append(id);
    stringBuffer.append(_215);
    stringBuffer.append(id);
    stringBuffer.append(_325);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_342);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_343);
    }
        else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_344);
    stringBuffer.append(count++ + 2);
    stringBuffer.append(_238);
    if (count == 1) {
    stringBuffer.append(_345);
    stringBuffer.append(id);
    stringBuffer.append(_215);
    stringBuffer.append(id);
    stringBuffer.append(_16);
    }
            else {
    stringBuffer.append(_346);
    }
    stringBuffer.append(_347);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_209);
    if (count == 1) {
    stringBuffer.append(_348);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_22);
    }
    for (Map.Entry<String, String> component : components.entrySet()) {
              String key = component.getKey();
              String value = component.getValue();
    stringBuffer.append(_349);
    stringBuffer.append(key);
    stringBuffer.append(_188);
    stringBuffer.append(value);
    stringBuffer.append(_67);
    }
    stringBuffer.append(_350);
    }
        }
    stringBuffer.append(_351);
    stringBuffer.append(id);
    stringBuffer.append(_210);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_352);
    stringBuffer.append(NL_13);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_353);
    }
    stringBuffer.append(_354);
    }
    stringBuffer.append(_335);
    }
    stringBuffer.append(_355);
    Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
    int nonEclipse = 0;
    for (String provider : featureProviders.keySet()) {
      if (!provider.toLowerCase().contains("eclipse"))
        ++nonEclipse;
    }
    stringBuffer.append(_356);
    stringBuffer.append(report.getProviderImage());
    stringBuffer.append(_357);
    stringBuffer.append(featureProviders.size());
    stringBuffer.append(_67);
    if (nonEclipse != 0) {
    stringBuffer.append(_321);
    stringBuffer.append(nonEclipse);
    stringBuffer.append(_5);
    }
    stringBuffer.append(_358);
    int count = 0;
    for (Map.Entry<String, Set<IInstallableUnit>> provider : featureProviders.entrySet()) {
      String style = !provider.getKey().toLowerCase().contains("eclipse") ? " style=\"color: FireBrick;\"" : "";
      Set<IInstallableUnit> features = provider.getValue();
      String id = "nested_feature_providers" + ++count;
    stringBuffer.append(_359);
    stringBuffer.append(id);
    stringBuffer.append(_214);
    stringBuffer.append(id);
    stringBuffer.append(_16);
    for (String image : report.getBrandingImages(features)) {
    stringBuffer.append(_360);
    stringBuffer.append(image);
    stringBuffer.append(_209);
    }
    stringBuffer.append(_361);
    stringBuffer.append(style);
    stringBuffer.append(_189);
    stringBuffer.append(provider.getKey());
    stringBuffer.append(_362);
    stringBuffer.append(features.size());
    stringBuffer.append(_363);
    stringBuffer.append(id);
    stringBuffer.append(_206);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_364);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_209);
    stringBuffer.append(NL_14);
    stringBuffer.append(report.getName(feature));
    stringBuffer.append(_365);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_366);
    }
    stringBuffer.append(_367);
    }
    stringBuffer.append(_335);
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
    stringBuffer.append(_368);
    stringBuffer.append(report.getFeatureImage());
    stringBuffer.append(_369);
    stringBuffer.append(features.size());
    stringBuffer.append(_67);
    if (brokenBranding != 0) {
    stringBuffer.append(_321);
    stringBuffer.append(brokenBranding);
    stringBuffer.append(_1);
    }
    if (noBranding != 0) {
    stringBuffer.append(_321);
    stringBuffer.append(noBranding);
    stringBuffer.append(_4);
    }
    stringBuffer.append(_370);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_371);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_209);
    stringBuffer.append(NL_11);
    stringBuffer.append(report.getName(feature));
    stringBuffer.append(_372);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_67);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        for (LicenseDetail license : report.getLicenses(feature)) {
          String id = license.getUUID();
          licenseReplacements.put(">" + id, "><button class='bb search-for-me' style='" + helper.getStyle(license) + "' onclick=\"clickOnButton('lic_" + id
              + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_373);
    stringBuffer.append(id);
    stringBuffer.append(_201);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_204);
    stringBuffer.append(id);
    stringBuffer.append(_212);
    stringBuffer.append(id);
    stringBuffer.append(_213);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_53);
    }
    stringBuffer.append(_374);
    }
    stringBuffer.append(_335);
    }
    {
      Collection<IInstallableUnit> products = report.getProducts();
      if (!products.isEmpty()) {
        boolean isInitiallyExpanded = false;
        String display = isInitiallyExpanded ? "block" : "none";
        String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_375);
    stringBuffer.append(report.getProductImage());
    stringBuffer.append(_376);
    stringBuffer.append(products.size());
    stringBuffer.append(_67);
    {
          StringBuilder onClick = new StringBuilder();
          for (IInstallableUnit product : products) {
            String productID = "_product_" + report.getIUID(product);
            onClick.append("expand2('products_all_arrows', '").append(productID).append("');");
          }
    stringBuffer.append(_377);
    stringBuffer.append(displayButton);
    stringBuffer.append(_43);
    stringBuffer.append(onClick);
    stringBuffer.append(_211);
    }
    stringBuffer.append(_378);
    stringBuffer.append(display);
    stringBuffer.append(_38);
    for (IInstallableUnit product : products) {
          String productImage = report.getIUImage(product);
          String productID = "_product_" + report.getIUID(product);
          Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(product));
    stringBuffer.append(_379);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_345);
    stringBuffer.append(productID);
    stringBuffer.append(_215);
    stringBuffer.append(productID);
    stringBuffer.append(_16);
    }
    stringBuffer.append(_380);
    stringBuffer.append(productImage);
    stringBuffer.append(_209);
    stringBuffer.append(NL_11);
    stringBuffer.append(report.getName(product));
    stringBuffer.append(_372);
    stringBuffer.append(report.getVersion(product));
    stringBuffer.append(_67);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_381);
    stringBuffer.append(productID);
    stringBuffer.append(_205);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_382);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_209);
    stringBuffer.append(NL_14);
    stringBuffer.append(report.getName(requiredIU));
    stringBuffer.append(_365);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_366);
    }
    stringBuffer.append(_383);
    }
    stringBuffer.append(_374);
    }
    stringBuffer.append(_335);
    }
    }
    if (!categories.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_384);
    stringBuffer.append(report.getCategoryImage());
    stringBuffer.append(_385);
    stringBuffer.append(categories.size());
    stringBuffer.append(_67);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit category : categories) {
          String categoryID = "_category_" + report.getIUID(category);
          onClick.append("expand2('categories_all_arrows', '").append(categoryID).append("');");
        }
    stringBuffer.append(_386);
    stringBuffer.append(displayButton);
    stringBuffer.append(_40);
    stringBuffer.append(onClick);
    stringBuffer.append(_211);
    }
    stringBuffer.append(_387);
    stringBuffer.append(display);
    stringBuffer.append(_38);
    for (IInstallableUnit category : categories) {
        String categoryImage = report.getIUImage(category);
        String categoryID = "_category_" + report.getIUID(category);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(category));
    stringBuffer.append(_379);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_345);
    stringBuffer.append(categoryID);
    stringBuffer.append(_215);
    stringBuffer.append(categoryID);
    stringBuffer.append(_16);
    }
    stringBuffer.append(_380);
    stringBuffer.append(categoryImage);
    stringBuffer.append(_209);
    stringBuffer.append(NL_11);
    stringBuffer.append(report.getName(category));
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_381);
    stringBuffer.append(categoryID);
    stringBuffer.append(_205);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_382);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_209);
    stringBuffer.append(NL_14);
    stringBuffer.append(report.getName(requiredIU));
    stringBuffer.append(_365);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_366);
    }
    stringBuffer.append(_383);
    }
    stringBuffer.append(_374);
    }
    stringBuffer.append(_335);
    }
    Collection<IInstallableUnit> ius = report.getAllIUs();
    if (!ius.isEmpty()) {
      Collection<IInstallableUnit> pluginsWithMissingPackGZ = report.getPluginsWithMissingPackGZ();
      Map<String, Set<Version>> iuVersions = report.getIUVersions();
      int duplicateCount = 0;
      for (Set<Version> versions : iuVersions.values()) {
        if (versions.size() > 1)
          ++duplicateCount;
      }
    stringBuffer.append(_388);
    stringBuffer.append(report.getBundleImage());
    stringBuffer.append(_389);
    stringBuffer.append(ius.size());
    stringBuffer.append(_67);
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_321);
    stringBuffer.append(pluginsWithMissingPackGZ.size());
    stringBuffer.append(_3);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_321);
    stringBuffer.append(duplicateCount);
    stringBuffer.append(_9);
    }
    stringBuffer.append(_390);
    for (IInstallableUnit iu : ius) {
        String iuID = iu.getId();
        String id = report.getIUID(iu);
        boolean duplicateVersions = iuVersions.get(iu.getId()).size() > 1;
        String versionStyle = duplicateVersions ? " font-weight: bold;" : "";
        StringBuilder classNames = new StringBuilder();
        if (duplicateVersions) {
          classNames.append(" duplicate");
        }
        if (pluginsWithMissingPackGZ.contains(iu)) {
          classNames.append(" missing");
        }
    stringBuffer.append(_391);
    stringBuffer.append(id);
    stringBuffer.append(_202);
    stringBuffer.append(classNames);
    stringBuffer.append(_392);
    stringBuffer.append(id);
    stringBuffer.append(_215);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    stringBuffer.append(_393);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_394);
    stringBuffer.append(iuID);
    stringBuffer.append(_395);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_210);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_67);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        if (report.isFeature(iu)) {
          for (LicenseDetail license : report.getLicenses(iu)) {
            String licenseID = license.getUUID();
            licenseReplacements.put(">" + licenseID, "><button class='bb search-for-me' style='" + helper.getStyle(license)
                + "' onclick=\"clickOnButton('lic_" + licenseID + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_396);
    stringBuffer.append(licenseID);
    stringBuffer.append(_201);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_204);
    stringBuffer.append(licenseID);
    stringBuffer.append(_212);
    stringBuffer.append(licenseID);
    stringBuffer.append(_213);
    stringBuffer.append(licenseID);
    stringBuffer.append(_15);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_53);
    }
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iu);
        for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
          String artifact = entry.getKey();
          Boolean signed = entry.getValue();
    stringBuffer.append(_397);
    if (signed != null) {
    stringBuffer.append(_398);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_209);
    }
    stringBuffer.append(_398);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_399);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_400);
    }
    if (pluginsWithMissingPackGZ.contains(iu)) {
    stringBuffer.append(_401);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_402);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_403);
    }
    stringBuffer.append(_404);
    }
    stringBuffer.append(_405);
    }
    }
    stringBuffer.append(_406);
    return stringBuffer.toString();
  }
}
