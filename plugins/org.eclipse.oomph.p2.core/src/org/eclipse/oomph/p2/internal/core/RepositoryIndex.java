/*
 * Copyright (c) 2019 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.p2.internal.core;

import java.security.cert.Certificate;
import java.util.*;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.eclipse.equinox.p2.metadata.*;
import org.eclipse.equinox.p2.repository.spi.PGPPublicKeyService;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.IndexReport;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.IUReport;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.Report;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.Report.LicenseDetail;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.Reporter;

@SuppressWarnings("nls")
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
  protected static final String _2 = " Expired Certificate";
  protected static final String _3 = " Expired)</span>";
  protected static final String _4 = " Invalid Licenses)</span>";
  protected static final String _5 = " No Branding Images)</span>";
  protected static final String _6 = " Not Provided by Eclipse)</span>";
  protected static final String _7 = " Unsigned Artifacts)</span>";
  protected static final String _8 = " font-smaller\">";
  protected static final String _9 = " with Multiple Versions)</span>";
  protected static final String _10 = "$(\"body\").append($temp);";
  protected static final String _11 = "$temp.remove();";
  protected static final String _12 = "$temp.val($(element).text()).select();";
  protected static final String _13 = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  protected static final String _14 = "');\">";
  protected static final String _15 = "');\">&#x25B7;</button>";
  protected static final String _16 = "'){";
  protected static final String _17 = "(";
  protected static final String _18 = ")";
  protected static final String _19 = ") Unsigned</span>";
  protected static final String _20 = ")</span>";
  protected static final String _21 = "++count;";
  protected static final String _22 = "-";
  protected static final String _23 = ".bad-absolute-location {";
  protected static final String _24 = ".bb {";
  protected static final String _25 = ".filter {";
  protected static final String _26 = ".fit-image {";
  protected static final String _27 = ".font-smaller {";
  protected static final String _28 = ".iu-link {";
  protected static final String _29 = ".resolved-requirement {";
  protected static final String _30 = ".text-nowrap {";
  protected static final String _31 = ".unresolved-requirement {";
  protected static final String _32 = ".unused-capability {";
  protected static final String _33 = ".used-capability {";
  protected static final String _34 = ".xml-attribute {";
  protected static final String _35 = ".xml-attribute-value {";
  protected static final String _36 = ".xml-element {";
  protected static final String _37 = ".xml-element-value {";
  protected static final String _38 = ".xml-iu {";
  protected static final String _39 = ".xml-repo {";
  protected static final String _40 = ".xml-token {";
  protected static final String _41 = "; margin-left: -1em; list-style-type: none; padding: 0; margin: 0;\">";
  protected static final String _42 = "; margin-left: -1em; list-style-type: none;\">";
  protected static final String _43 = ";\" onclick=\"toggle('categories_all_arrows');";
  protected static final String _44 = ";\" onclick=\"toggle('certificates_all_arrows');";
  protected static final String _45 = ";\" onclick=\"toggle('licenses_all_arrows');";
  protected static final String _46 = ";\" onclick=\"toggle('pgpKeys_all_arrows');";
  protected static final String _47 = ";\" onclick=\"toggle('products_all_arrows');";
  protected static final String _48 = "<!-- navigation sidebar -->";
  protected static final String _49 = "<!--- providers -->";
  protected static final String _50 = "<!----------->";
  protected static final String _51 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
  protected static final String _52 = "</a>";
  protected static final String _53 = "</a> report.";
  protected static final String _54 = "</a>.";
  protected static final String _55 = "</a>.</p>";
  protected static final String _56 = "</a></li>";
  protected static final String _57 = "</a></span>";
  protected static final String _58 = "</aside>";
  protected static final String _59 = "</b>";
  protected static final String _60 = "</body>";
  protected static final String _61 = "</button>";
  protected static final String _62 = "</div>";
  protected static final String _63 = "</h2>";
  protected static final String _64 = "</h3>";
  protected static final String _65 = "</head>";
  protected static final String _66 = "</header>";
  protected static final String _67 = "</html>";
  protected static final String _68 = "</li>";
  protected static final String _69 = "</main>";
  protected static final String _70 = "</ol>";
  protected static final String _71 = "</p>";
  protected static final String _72 = "</pre>";
  protected static final String _73 = "</script>";
  protected static final String _74 = "</section>";
  protected static final String _75 = "</span>";
  protected static final String _76 = "</span><span style=\"color: green; text-decoration: underline; ";
  protected static final String _77 = "</style>";
  protected static final String _78 = "</title>";
  protected static final String _79 = "</ul>";
  protected static final String _80 = "<a class=\"separator\" href=\"";
  protected static final String _81 = "<a class=\"separator\" href=\".\">";
  protected static final String _82 = "<a class=\"separator\" href=\"https://www.eclipse.org/downloads\">Downloads</a>";
  protected static final String _83 = "<a class=\"separator\" href=\"https://www.eclipse.org\">Home</a>";
  protected static final String _84 = "<a class=\"separator\" style=\"display: inline-block; margin-left: 0.25em;\" href=\"";
  protected static final String _85 = "<a href=\"";
  protected static final String _86 = "<a href=\"https://wiki.eclipse.org/Oomph_Repository_Analyzer#Description\" target=\"oomph_wiki\">";
  protected static final String _87 = "<a href=\"https://www.eclipse.org/\">";
  protected static final String _88 = "<a style=\"float:right; font-size: 200%;\" href=\"";
  protected static final String _89 = "<aside id=\"leftcol\" class=\"col-md-4 font-smaller\">";
  protected static final String _90 = "<b>Built: ";
  protected static final String _91 = "<body id=\"body_solstice\">";
  protected static final String _92 = "<br/>";
  protected static final String _93 = "<br/><b>Reported: ";
  protected static final String _94 = "<button id=\"";
  protected static final String _95 = "<button id=\"_";
  protected static final String _96 = "<button id=\"__";
  protected static final String _97 = "<button id=\"_f";
  protected static final String _98 = "<button id=\"categories_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _99 = "<button id=\"categories_arrows\" class=\"orange bb\" onclick=\"expand_collapse('categories'); expand_collapse_inline('categories_all_arrows');\">&#x25B7;</button>";
  protected static final String _100 = "<button id=\"certificates_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _101 = "<button id=\"certificates_arrows\" class=\"orange bb\" onclick=\"expand_collapse('certificates'); expand_collapse_inline('certificates_all_arrows');\">&#x25B7;</button>";
  protected static final String _102 = "<button id=\"feature_providers_arrows\" class=\"orange bb\" onclick=\"expand_collapse('feature_providers');\">&#x25B7;</button>";
  protected static final String _103 = "<button id=\"features_arrows\" class=\"orange bb\" onclick=\"expand_collapse('features');\">&#x25B7;</button>";
  protected static final String _104 = "<button id=\"ius_arrows\" class=\"orange bb\" onclick=\"expand_collapse('ius'); match('subset');\">&#x25B7;</button>";
  protected static final String _105 = "<button id=\"lic_";
  protected static final String _106 = "<button id=\"licenses_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _107 = "<button id=\"licenses_arrows\" class=\"orange bb\" onclick=\"expand_collapse('licenses'); expand_collapse_inline('licenses_all_arrows');\">&#x25B7;</button>";
  protected static final String _108 = "<button id=\"pgpKeys_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _109 = "<button id=\"pgpKeys_arrows\" class=\"orange bb\" onclick=\"expand_collapse('pgpKeys'); expand_collapse_inline('pgpKeys_all_arrows');\">&#x25B7;</button>";
  protected static final String _110 = "<button id=\"products_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _111 = "<button id=\"products_arrows\" class=\"orange bb\" onclick=\"expand_collapse('products'); expand_collapse_inline('products_all_arrows');\">&#x25B7;</button>";
  protected static final String _112 = "<button id=\"repos_arrows\" class=\"orange bb\" onclick=\"expand_collapse('repos');\">&#x25B7;</button>";
  protected static final String _113 = "<button id=\"xml-links\" class=\"orange bb\" onclick=\"expand_collapse_all('xml-links');\">&#x25B7;</button>";
  protected static final String _114 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 150%;\" onclick=\"copyToClipboard('#p1')\">&#x270e;</button>";
  protected static final String _115 = "<div class=\"col-sm-16 padding-left-30\">";
  protected static final String _116 = "<div class=\"container\">";
  protected static final String _117 = "<div class=\"font-smaller\" style=\"margin-left: ";
  protected static final String _118 = "<div class=\"font-smaller\">";
  protected static final String _119 = "<div class=\"hidden-xs col-sm-8 col-md-6 col-lg-5\" id=\"header-left\">";
  protected static final String _120 = "<div class=\"novaContent container\" id=\"novaContent\">";
  protected static final String _121 = "<div class=\"row\" id=\"header-row\">";
  protected static final String _122 = "<div class=\"row\">";
  protected static final String _123 = "<div class=\"wrapper-logo-default\">";
  protected static final String _124 = "<div class=\"xml-iu\" style=\"display:block;\">";
  protected static final String _125 = "<div class=\"xml-repo\" id=\"repos\" style=\"display: none;\">";
  protected static final String _126 = "<div id=\"ius\" style=\"display:none;\">";
  protected static final String _127 = "<div id=\"maincontent\">";
  protected static final String _128 = "<div id=\"midcolumn\" style=\"width: 70%;\">";
  protected static final String _129 = "<div style=\"height: 40ex;\">";
  protected static final String _130 = "<div style=\"text-indent: -2em\">";
  protected static final String _131 = "<div>";
  protected static final String _132 = "<h2 style=\"text-align: center;\">";
  protected static final String _133 = "<h3 class=\"sr-only\">Breadcrumbs</h3>";
  protected static final String _134 = "<h3 style=\"font-weight: bold;\">";
  protected static final String _135 = "<h3 style=\"font-weight: bold;\">Artifacts</h3>";
  protected static final String _136 = "<h3 style=\"font-weight: bold;\">Description</h3>";
  protected static final String _137 = "<h3 style=\"font-weight: bold;\">Licenses</h3>";
  protected static final String _138 = "<h3 style=\"font-weight: bold;\">Provider</h3>";
  protected static final String _139 = "<head>";
  protected static final String _140 = "<header role=\"banner\" id=\"header-wrapper\">";
  protected static final String _141 = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">";
  protected static final String _142 = "<img alt=\"Branding Image\" class=\"fit-image\" src=\"";
  protected static final String _143 = "<img alt=\"Signed\" class=\"fit-image\" src=\"";
  protected static final String _144 = "<img alt=\"Signed\" class=\"fit-image\" src=\"../";
  protected static final String _145 = "<img class=\"fit-image> src=\"";
  protected static final String _146 = "<img class=\"fit-image\" src=\"";
  protected static final String _147 = "<img class=\"fit-image\" src=\"../";
  protected static final String _148 = "<img class=\"logo-eclipse-default img-responsive hidden-xs\" alt=\"Eclipse Log\" src=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/logo/eclipse-426x100.png\"/>";
  protected static final String _149 = "<img src=\"";
  protected static final String _150 = "<input id=\"bad-license\" type=\"radio\" name=\"filter\" value=\"bad-license\" class=\"filter\" onclick=\"filterIU('bad-license');\"> Bad License </input>";
  protected static final String _151 = "<input id=\"bad-provider\" type=\"radio\" name=\"filter\" value=\"bad-provider\" class=\"filter\" onclick=\"filterIU('bad-provider');\"> Bad Provider </input>";
  protected static final String _152 = "<input id=\"broken-branding\" type=\"radio\" name=\"filter\" value=\"broken-branding\" class=\"filter\" onclick=\"filterIU('broken-branding');\"> Broken Branding </input>";
  protected static final String _153 = "<input id=\"duplicates\" type=\"radio\" name=\"filter\" value=\"duplicate\" class=\"filter\" onclick=\"filterIU('duplicate');\"> Duplicates </input>";
  protected static final String _154 = "<input id=\"filter\" type=\"radio\" name=\"filter\" value=\"all\" class=\"filter\" onclick=\"filterIU('iu-li');\" checked=\"checked\"> All </input>";
  protected static final String _155 = "<input id=\"unsigned\" type=\"radio\" name=\"filter\" value=\"unsigned\" class=\"filter\" onclick=\"filterIU('unsigned');\"> Unsigned </input>";
  protected static final String _156 = "<li class=\"active\">";
  protected static final String _157 = "<li class=\"font-smaller text-nowrap\">";
  protected static final String _158 = "<li class=\"separator\" style=\"font-size: 90%;\">";
  protected static final String _159 = "<li class=\"separator\">";
  protected static final String _160 = "<li id=\"_iu_";
  protected static final String _161 = "<li style=\"font-size: 100%; white-space: nowrap;\">";
  protected static final String _162 = "<li style=\"margin-left: 1em;\">";
  protected static final String _163 = "<li>";
  protected static final String _164 = "<li><a href=\"";
  protected static final String _165 = "<li><a href=\"https://www.eclipse.org/\">Home</a></li>";
  protected static final String _166 = "<li><a href=\"https://www.eclipse.org/downloads/\">Downloads</a></li>";
  protected static final String _167 = "<link href=\"//fonts.googleapis.com/css?family=Open+Sans:400,700,300,600,100\" rel=\"stylesheet\" type=\"text/css\"/>";
  protected static final String _168 = "<link rel=\"icon\" type=\"image/ico\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/favicon.ico\"/>";
  protected static final String _169 = "<link rel=\"stylesheet\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/stylesheets/styles.min.css\"/>";
  protected static final String _170 = "<main class=\"no-promo\">";
  protected static final String _171 = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
  protected static final String _172 = "<meta name=\"description\" content=\"Update Sites Reports\"/>";
  protected static final String _173 = "<meta name=\"keywords\" content=\"eclipse,update site\"/>";
  protected static final String _174 = "<ol class=\"breadcrumb\">";
  protected static final String _175 = "<p style=\"font-size: 125%; text-align: center;\">";
  protected static final String _176 = "<p style=\"text-align: center;\">";
  protected static final String _177 = "<p>";
  protected static final String _178 = "<p></p>";
  protected static final String _179 = "<p>Reports are generated specifically for the following sites:</p>";
  protected static final String _180 = "<p>This report is produced by <a href=\"";
  protected static final String _181 = "<pre id=\"_";
  protected static final String _182 = "<pre id=\"__";
  protected static final String _183 = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";
  protected static final String _184 = "<script>";
  protected static final String _185 = "<section class=\"hidden-print default-breadcrumbs\" id=\"breadcrumb\">";
  protected static final String _186 = "<span ";
  protected static final String _187 = "<span class=\"text-nowrap\"";
  protected static final String _188 = "<span class=\"text-nowrap\"><a href=\"";
  protected static final String _189 = "<span id=\"p1\" style=\"font-size: 125%\">";
  protected static final String _190 = "<span style=\"";
  protected static final String _191 = "<span style=\"color: DarkCyan; font-size: 110%;\">(";
  protected static final String _192 = "<span style=\"color: DarkCyan;\">";
  protected static final String _193 = "<span style=\"color: DarkOliveGreen;";
  protected static final String _194 = "<span style=\"color: DarkOliveGreen;\">";
  protected static final String _195 = "<span style=\"color: FireBrick; font-size: 60%;\">(";
  protected static final String _196 = "<span style=\"color: FireBrick; font-size: 60%;\">(Inappropriate absolute location)</span>";
  protected static final String _197 = "<span style=\"color: FireBrick; font-size: 90%;\">";
  protected static final String _198 = "<span style=\"color: FireBrick;\">(";
  protected static final String _199 = "<span style=\"color: FireBrick;\">Unsigned</span>";
  protected static final String _200 = "<span style=\"color: red; text-decoration: line-through; ";
  protected static final String _201 = "<span style=\"font-size:100%;\">";
  protected static final String _202 = "<span style=\"margin-left: 1em;\" class=\"nowrap\">Filter Pattern: <input id=\"subset\" type=\"text\" oninput=\"match('subset');\"> <span style=\"color: firebrick;\" id=\"subset-error\"></span></input></span><br/>";
  protected static final String _203 = "<span style=\"margin-left: 1em;\">";
  protected static final String _204 = "<style>";
  protected static final String _205 = "<title>";
  protected static final String _206 = "<tt style=\"float: left;\" class=\"orange\">&#xbb;</tt>";
  protected static final String _207 = "<tt style=\"float: right;\" class=\"orange\">&#xab;</tt>";
  protected static final String _208 = "<ul class=\"font-smaller\" id=\"";
  protected static final String _209 = "<ul class=\"font-smaller\" style=\"list-style-type: none; display:none; margin-left: -3em;\" id=\"";
  protected static final String _210 = "<ul id=\"";
  protected static final String _211 = "<ul id=\"categories\" style=\"display: ";
  protected static final String _212 = "<ul id=\"certificates\" style=\"display:";
  protected static final String _213 = "<ul id=\"feature_providers\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _214 = "<ul id=\"features\" style=\"display: none; list-style-type: none; margin-left: -2em;\">";
  protected static final String _215 = "<ul id=\"leftnav\" class=\"ul-left-nav fa-ul hidden-print\">";
  protected static final String _216 = "<ul id=\"licenses\" style=\"display: ";
  protected static final String _217 = "<ul id=\"pgpKeys\" style=\"display:";
  protected static final String _218 = "<ul id=\"products\" style=\"display: ";
  protected static final String _219 = "<ul style=\"display:none; list-style-type: none; padding: 0; margin: 0;\" id=\"__";
  protected static final String _220 = "<ul style=\"list-style-type: none; display:none; padding: 0; margin: 0; margin-left: 2em;\" id=\"_f";
  protected static final String _221 = "<ul style=\"list-style-type: none; padding: 0; margin-left: 1em;\">";
  protected static final String _222 = "<ul>";
  protected static final String _223 = "=</span>";
  protected static final String _224 = ">";
  protected static final String _225 = "><span style=\"";
  protected static final String _226 = "Categories";
  protected static final String _227 = "Certificates";
  protected static final String _228 = "Content Metadata";
  protected static final String _229 = "Features";
  protected static final String _230 = "Features/Products";
  protected static final String _231 = "In addition to this composite report, reports are also generated for each of the composed children listed in the navigation bar to the left.";
  protected static final String _232 = "Installable Units";
  protected static final String _233 = "Licenses";
  protected static final String _234 = "No Name<br/>";
  protected static final String _235 = "PGP Keys";
  protected static final String _236 = "Products";
  protected static final String _237 = "Providers";
  protected static final String _238 = "Signing Certificates";
  protected static final String _239 = "Signing PGP Keys";
  protected static final String _240 = "This";
  protected static final String _241 = "This is a composite update site.";
  protected static final String _242 = "This is a generated";
  protected static final String _243 = "XML";
  protected static final String _244 = "[<img class=\"fit-image\" src=\"";
  protected static final String _245 = "\" alt=\"\"/>";
  protected static final String _246 = "\" alt=\"\"/><img style=\"margin-top: -2ex;\" class=\"fit-image\" src=\"";
  protected static final String _247 = "\" class=\"bb\" style=\"";
  protected static final String _248 = "\" class=\"iu-li";
  protected static final String _249 = "\" onclick=\"clickOnToggleButton('licenses_arrows'); clickOnToggleButton('__";
  protected static final String _250 = "\" style=\"display: none;\">";
  protected static final String _251 = "\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _252 = "\" style=\"display:none; margin-left: 2em; background-color: ";
  protected static final String _253 = "\" style=\"list-style-type: none; display: none; margin-left: -1em;\">";
  protected static final String _254 = "\" target=\"keyserver\">0x";
  protected static final String _255 = "\" target=\"oomph_wiki\"/>";
  protected static final String _256 = "\" target=\"oomph_wiki\">";
  protected static final String _257 = "\" target=\"report_source\">";
  protected static final String _258 = "\"/>";
  protected static final String _259 = "\"/> ";
  protected static final String _260 = "\"/>]";
  protected static final String _261 = "\">";
  protected static final String _262 = "\">&#x25B7;</button>";
  protected static final String _263 = "\"><img class=\"fit-image\" src=\"";
  protected static final String _264 = "_arrows'); clickOnToggleButton('_";
  protected static final String _265 = "_arrows'); navigateTo('_";
  protected static final String _266 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('";
  protected static final String _267 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('__";
  protected static final String _268 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('";
  protected static final String _269 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_";
  protected static final String _270 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('__";
  protected static final String _271 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_f";
  protected static final String _272 = "background-color: white;";
  protected static final String _273 = "border: 1px solid black;";
  protected static final String _274 = "border: none;";
  protected static final String _275 = "break;";
  protected static final String _276 = "buttonTargets.item(i).innerHTML = '&#x25B7;';";
  protected static final String _277 = "buttonTargets.item(i).innerHTML = '&#x25E2;';";
  protected static final String _278 = "catch (err) {";
  protected static final String _279 = "color: DarkSlateGray;";
  protected static final String _280 = "color: FireBrick;";
  protected static final String _281 = "color: IndianRed;";
  protected static final String _282 = "color: MediumAquaMarine;";
  protected static final String _283 = "color: MediumOrchid;";
  protected static final String _284 = "color: SaddleBrown;";
  protected static final String _285 = "color: SeaGreen;";
  protected static final String _286 = "color: SteelBlue;";
  protected static final String _287 = "color: Teal;";
  protected static final String _288 = "continue;";
  protected static final String _289 = "currentFilter = filter.value;";
  protected static final String _290 = "document.execCommand(\"copy\");";
  protected static final String _291 = "e.click();";
  protected static final String _292 = "e.innerHTML = '&#x25B7;';";
  protected static final String _293 = "e.innerHTML = '&#x25E2;';";
  protected static final String _294 = "e.scrollIntoView();";
  protected static final String _295 = "e.style.display = 'block';";
  protected static final String _296 = "e.style.display = 'inline';";
  protected static final String _297 = "e.style.display = 'inline-block';";
  protected static final String _298 = "e.style.display = 'none';";
  protected static final String _299 = "e.title= 'Collapse All';";
  protected static final String _300 = "e.title= 'Expand All';";
  protected static final String _301 = "em; text-indent: -4em;\">";
  protected static final String _302 = "em;\">";
  protected static final String _303 = "f.innerHTML = '&#x25B7;';";
  protected static final String _304 = "f.innerHTML = '&#x25E2;';";
  protected static final String _305 = "font-family: monospace;";
  protected static final String _306 = "font-size: 125%;";
  protected static final String _307 = "font-size: 75%;\">";
  protected static final String _308 = "font-size: 80%;";
  protected static final String _309 = "font-size: 90%;";
  protected static final String _310 = "for (var i = 0; i < buttonTargets.length; i++) {";
  protected static final String _311 = "for (var i = 0; i < ius.length; i++) {";
  protected static final String _312 = "for (var i = 0; i < spanTargets.length; i++) {";
  protected static final String _313 = "function clickOnButton(id) {";
  protected static final String _314 = "function clickOnToggleButton(id) {";
  protected static final String _315 = "function copyToClipboard(element) {";
  protected static final String _316 = "function expand(id) {";
  protected static final String _317 = "function expand2(self, id) {";
  protected static final String _318 = "function expand3(self, id) {";
  protected static final String _319 = "function expand_collapse(id) {";
  protected static final String _320 = "function expand_collapse_all(base) {";
  protected static final String _321 = "function expand_collapse_inline(id) {";
  protected static final String _322 = "function expand_collapse_inline_block(id) {";
  protected static final String _323 = "function filterIU(className) {";
  protected static final String _324 = "function match(id) {";
  protected static final String _325 = "function navigateTo(id) {";
  protected static final String _326 = "function toggle(id) {";
  protected static final String _327 = "height: 2ex;";
  protected static final String _328 = "if (!targetsArray.includes(iu)) {";
  protected static final String _329 = "if ((matchText == '' || text.match(matchText) != null) && targetsArray.includes(iu)) {";
  protected static final String _330 = "if (count == 0 && message.innerHTML == '') {";
  protected static final String _331 = "if (count == 0) {";
  protected static final String _332 = "if (e.innerHTML == '";
  protected static final String _333 = "if (e.innerHTML == '\\u25E2') {";
  protected static final String _334 = "if (e.style.display == 'none'){";
  protected static final String _335 = "if (e.title == 'Expand All') {";
  protected static final String _336 = "if (f != null) {";
  protected static final String _337 = "if (f !=null) {";
  protected static final String _338 = "if (filter != null && filter.value != 'all') {";
  protected static final String _339 = "if (matchText != '' && iu.textContent.match(matchText) == null) {";
  protected static final String _340 = "if (t.title != 'Collapse All'){";
  protected static final String _341 = "if (t.title == 'Collapse All'){";
  protected static final String _342 = "iu.style.display = 'block';";
  protected static final String _343 = "iu.style.display = 'none';";
  protected static final String _344 = "margin-bottom: -2ex;";
  protected static final String _345 = "margin-left: 0em;";
  protected static final String _346 = "margin-top: -2ex;";
  protected static final String _347 = "margin: 0px 0px 0px 0px;";
  protected static final String _348 = "message.innerHTML = '';";
  protected static final String _349 = "message.innerHTML = 'No matches';";
  protected static final String _350 = "message.innerHTML = \"\";";
  protected static final String _351 = "message.innerHTML = errMessage;";
  protected static final String _352 = "padding: -2pt -2pt -2pt -2pt;";
  protected static final String _353 = "padding: 0px 0px;";
  protected static final String _354 = "report is produced by <a href=\"";
  protected static final String _355 = "s";
  protected static final String _356 = "span:target {";
  protected static final String _357 = "spanTargets.item(i).style.display = 'inline-block';";
  protected static final String _358 = "spanTargets.item(i).style.display = 'none';";
  protected static final String _359 = "try";
  protected static final String _360 = "try {";
  protected static final String _361 = "var $temp = $(\"<input>\");";
  protected static final String _362 = "var buttonTargets = document.getElementsByClassName(base + '_button');";
  protected static final String _363 = "var count = 0;";
  protected static final String _364 = "var currentFilter = 'iu-li';";
  protected static final String _365 = "var e = document.getElementById('subset');";
  protected static final String _366 = "var e = document.getElementById(base);";
  protected static final String _367 = "var e = document.getElementById(id);";
  protected static final String _368 = "var errMessage = err.message;";
  protected static final String _369 = "var f = document.getElementById(id+\"_arrows\");";
  protected static final String _370 = "var filter = document.querySelector('input[name=\"filter\"]:checked');";
  protected static final String _371 = "var iu = ius[i];";
  protected static final String _372 = "var ius = document.getElementsByClassName('iu-li');";
  protected static final String _373 = "var matchText = e.value;";
  protected static final String _374 = "var message = document.getElementById('subset-error');";
  protected static final String _375 = "var message = document.getElementById(id + '-error');";
  protected static final String _376 = "var spanTargets = document.getElementsByClassName(base);";
  protected static final String _377 = "var state = e.innerHTML;";
  protected static final String _378 = "var t = document.getElementById('all');";
  protected static final String _379 = "var t = document.getElementById(self);";
  protected static final String _380 = "var targets = document.getElementsByClassName(className);";
  protected static final String _381 = "var targets = document.getElementsByClassName(currentFilter);";
  protected static final String _382 = "var targetsArray = [].slice.call(targets);";
  protected static final String _383 = "var text = iu.textContent;";
  protected static final String _384 = "white-space: nowrap;";
  protected static final String _385 = "white-space: pre;";
  protected static final String _386 = "width: 2ex;";
  protected static final String _387 = "{";
  protected static final String _388 = "}";
  protected static final String _389 = "} else {";
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
  protected final String _390 = _51 + NL + _141 + NL + _139 + NL_1 + _171 + NL_1 + _205;
  protected final String _391 = _78 + NL_1 + _173 + NL_1 + _172 + NL_1 + _167 + NL_1 + _169 + NL_1 + _168 + NL_1 + _183;
  protected final String _392 = NL_1 + _204 + NL + NL + _30 + NL_1 + _384 + NL + _388 + NL + NL + _27 + NL_1 + _309 + NL + _388 + NL + NL + _26 + NL_1 + _386 + NL_1 + _327 + NL + _388 + NL + NL + _356 + NL_1 + _306 + NL_1 + _273 + NL + _388 + NL + NL + _39 + NL_1 + _385 + NL_1 + _274 + NL_1 + _353 + NL_1 + _346 + NL_1 + _344 + NL_1 + _345 + NL + _388 + NL + NL + _23 + NL_1 + _280 + NL_1 + _309 + NL + _388 + NL + NL + _38 + NL_1 + _385 + NL_1 + _274 + NL_1 + _353 + NL_1 + _346 + NL_1 + _344 + NL_1 + _345 + NL + _388 + NL + NL + _40 + NL_1 + _286 + NL_1 + _305 + NL_1 + _308 + NL + _388 + NL + NL + _34 + NL_1 + _282 + NL_1 + _305 + NL_1 + _308 + NL + _388 + NL + NL + _36 + NL_1 + _283 + NL_1 + _305 + NL_1 + _308 + NL + _388 + NL + NL + _35 + NL_1 + _279 + NL_1 + _309 + NL + _388 + NL + NL + _37 + NL_1 + _284 + NL_1 + _309 + NL + _388 + NL + NL + _24 + NL_1 + _272 + NL_1 + _274 + NL_1 + _353 + NL + _388 + NL + NL + _25 + NL_1 + _272 + NL + _388 + NL + NL + _28 + NL_1 + _352 + NL_1 + _347 + NL + _388 + NL + NL + _29 + NL_1 + _285 + NL_1 + _309 + NL + _388 + NL + NL + _33 + NL_1 + _287 + NL_1 + _309 + NL + _388 + NL + NL + _32 + NL_1 + _309 + NL + _388 + NL + NL + _31 + NL_1 + _281 + NL_1 + _309 + NL + _388 + NL_1 + _77 + NL + _65 + NL + NL_1 + _91;
  protected final String _393 = NL_2 + _184 + NL + NL_4 + _324 + NL_5 + _370 + NL_5 + _364 + NL_5 + _338 + NL_7 + _289 + NL_5 + _388 + NL_5 + _367 + NL_5 + _375 + NL_5 + _373 + NL_5 + _372 + NL_5 + _381 + NL_5 + _382 + NL_5 + _363 + NL_5 + _311 + NL_7 + _371 + NL_7 + _383 + NL_7 + _360 + NL_9 + _329 + NL_11 + _342 + NL_11 + _21 + NL_9 + _389 + NL_11 + _343 + NL_9 + _388 + NL_9 + _350 + NL_7 + _388 + NL_7 + _278 + NL_9 + _368 + NL_9 + _351 + NL_9 + _275 + NL_7 + _388 + NL_5 + _388 + NL_5 + _330 + NL_9 + _349 + NL_5 + _388 + NL_4 + _388 + NL + NL_4 + _323 + NL_5 + _365 + NL_5 + _373 + NL_5 + _363 + NL_5 + _372 + NL_5 + _380 + NL_5 + _382 + NL_5 + _311 + NL_7 + _371 + NL_7 + _328 + NL_9 + _343 + NL_7 + _389 + NL_9 + _359 + NL_9 + _387 + NL_11 + _339 + NL_13 + _343 + NL_13 + _288 + NL_11 + _388 + NL_9 + _388 + NL_9 + _278 + NL_9 + _388 + NL_9 + _342 + NL_9 + _21 + NL_7 + _388 + NL_5 + _388 + NL_5 + _374 + NL_5 + _331 + NL_9 + _349 + NL_5 + _389 + NL_9 + _348 + NL_5 + _388 + NL_4 + _388 + NL + NL_4 + _315 + NL_5 + _361 + NL_5 + _10 + NL_5 + _12 + NL_5 + _290 + NL_5 + _11 + NL_4 + _388 + NL + NL_4 + _313 + NL_5 + _367 + NL_5 + _291 + NL_4 + _388 + NL + NL_4 + _314 + NL_5 + _367 + NL_5 + _377 + NL_5 + _332;
  protected final String _394 = _16 + NL_7 + _291 + NL_5 + _388 + NL_4 + _388 + NL + NL_4 + _325 + NL_5 + _367 + NL_5 + _294 + NL_4 + _388 + NL + NL_4 + _326 + NL_5 + _367 + NL_5 + _335 + NL_7 + _299 + NL_7 + _293 + NL_5 + _389 + NL_7 + _300 + NL_7 + _292 + NL_5 + _388 + NL_4 + _388 + NL + NL_4 + _317 + NL_5 + _379 + NL_5 + _367 + NL_5 + _369 + NL_5 + _341 + NL_7 + _295 + NL_7 + _304 + NL_5 + _389 + NL_7 + _298 + NL_7 + _303 + NL_5 + _388 + NL_4 + _388 + NL + NL_4 + _318 + NL_5 + _379 + NL_5 + _367 + NL_5 + _369 + NL_5 + _340 + NL_7 + _298 + NL_7 + _303 + NL_5 + _388 + NL_4 + _388 + NL + NL_4 + _316 + NL_5 + _378 + NL_5 + _367 + NL_5 + _369 + NL_5 + _341 + NL_7 + _295 + NL_7 + _304 + NL_5 + _389 + NL_7 + _298 + NL_7 + _303 + NL_5 + _388 + NL_4 + _388 + NL + NL_4 + _319 + NL_5 + _367 + NL_5 + _369 + NL_5 + _334 + NL_7 + _295 + NL_7 + _304 + NL_5 + _389 + NL_7 + _298 + NL_7 + _303 + NL_5 + _388 + NL_4 + _388 + NL + NL_4 + _321 + NL_5 + _367 + NL_5 + _369 + NL_5 + _334 + NL_7 + _296 + NL_7 + _336 + NL_9 + _304 + NL_7 + _388 + NL_5 + _389 + NL_7 + _298 + NL_7 + _337 + NL_9 + _303 + NL_7 + _388 + NL_5 + _388 + NL_4 + _388 + NL + NL_4 + _322 + NL_5 + _367 + NL_5 + _369 + NL_5 + _334 + NL_7 + _297 + NL_7 + _336 + NL_9 + _304 + NL_7 + _388 + NL_5 + _389 + NL_7 + _298 + NL_7 + _337 + NL_9 + _303 + NL_7 + _388 + NL_5 + _388 + NL_4 + _388 + NL + NL_4 + _320 + NL_5 + _366 + NL_5 + _362 + NL_5 + _376 + NL_5 + _333 + NL_9 + _292 + NL_9 + _310 + NL_11 + _276 + NL_9 + _388 + NL_9 + _312 + NL_11 + _358 + NL_9 + _388 + NL_5 + _389 + NL_9 + _293 + NL_9 + _310 + NL_11 + _277 + NL_9 + _388 + NL_9 + _312 + NL_11 + _357 + NL_9 + _388 + NL_5 + _388 + NL_4 + _388 + NL + NL_2 + _73 + NL + NL_2 + _140 + NL_4 + _116 + NL_5 + _121 + NL_7 + _119 + NL_9 + _123 + NL_11 + _87 + NL_13 + _148 + NL_11 + _52 + NL_9 + _62 + NL_7 + _62 + NL_5 + _62 + NL_4 + _62 + NL_2 + _66;
  protected final String _395 = NL_2 + _185 + NL_4 + _116 + NL_5 + _133 + NL_5 + _122 + NL_7 + _115 + NL_9 + _174 + NL_11 + _165 + NL_11 + _166;
  protected final String _396 = NL_11 + _156;
  protected final String _397 = NL_11 + _164;
  protected final String _398 = NL_9 + _70 + NL_7 + _62 + NL_5 + _62 + NL_4 + _62 + NL_2 + _74 + NL + NL_2 + _170 + NL_2 + _120 + NL;
  protected final String _399 = NL_4 + _48 + NL_4 + _89 + NL_5 + _215 + NL_7 + _159 + NL_9 + _83 + NL_7 + _68 + NL_7 + _159 + NL_9 + _82 + NL_7 + _68;
  protected final String _400 = NL_7 + _159 + NL_9 + _80;
  protected final String _401 = _52 + NL_7 + _68;
  protected final String _402 = NL_7 + _159 + NL_9 + _207 + NL_9 + _81;
  protected final String _403 = NL_7 + _158 + NL_9 + _206;
  protected final String _404 = NL_9 + _207;
  protected final String _405 = NL_9 + _84;
  protected final String _406 = NL_9 + _52 + NL_7 + _68;
  protected final String _407 = NL_5 + _79 + NL_4 + _58 + NL + NL_4 + _127 + NL_5 + _128;
  protected final String _408 = NL_7 + _132;
  protected final String _409 = NL_7 + _132 + NL_9 + _147;
  protected final String _410 = NL_9 + _234;
  protected final String _411 = NL_7 + _63;
  protected final String _412 = NL_7 + _175;
  protected final String _413 = NL_9 + _90;
  protected final String _414 = NL_9 + _93;
  protected final String _415 = _59 + NL_7 + _71;
  protected final String _416 = NL_7 + _176 + NL_9 + _114 + NL_9 + _189;
  protected final String _417 = _75 + NL_7 + _71 + NL_7 + _92;
  protected final String _418 = NL_7 + _88;
  protected final String _419 = _255 + NL_9 + _149;
  protected final String _420 = _245 + NL_7 + _52;
  protected final String _421 = NL_8 + _177 + NL_10 + _242 + NL_10 + _86 + NL_12 + _146;
  protected final String _422 = _53 + NL_8 + _71 + NL_8 + _180;
  protected final String _423 = NL_8 + _179 + NL_8 + _222;
  protected final String _424 = NL_10 + _164;
  protected final String _425 = NL_8 + _79;
  protected final String _426 = NL_8 + _177 + NL_10 + _240 + NL_10 + _85;
  protected final String _427 = _256 + NL_12 + _146;
  protected final String _428 = _52 + NL_10 + _354;
  protected final String _429 = _54 + NL_8 + _71;
  protected final String _430 = NL_8 + _138 + NL_8 + _177;
  protected final String _431 = NL_8 + _136 + NL_8 + _177;
  protected final String _432 = NL_8 + _134 + NL_10 + _235 + NL_8 + _64;
  protected final String _433 = NL_8 + _118 + NL_10 + _144;
  protected final String _434 = _258 + NL_10 + _188;
  protected final String _435 = _57 + NL_8 + _62;
  protected final String _436 = NL_8 + _134 + NL_10 + _227;
  protected final String _437 = NL_8 + _195;
  protected final String _438 = NL_8 + _64;
  protected final String _439 = NL_10 + _131 + NL_12 + _147;
  protected final String _440 = _258 + NL_12 + _199 + NL_10 + _62;
  protected final String _441 = NL_10 + _117;
  protected final String _442 = _302 + NL_12 + _144;
  protected final String _443 = NL_12 + _187;
  protected final String _444 = NL_10 + _62;
  protected final String _445 = NL_8 + _135;
  protected final String _446 = NL_8 + _177;
  protected final String _447 = NL_10 + _147;
  protected final String _448 = _258 + NL_10 + _192;
  protected final String _449 = _75 + NL_10 + _85;
  protected final String _450 = NL_10 + _92 + NL_10 + _197;
  protected final String _451 = NL_8 + _71;
  protected final String _452 = NL_6 + _137;
  protected final String _453 = NL_12 + _96;
  protected final String _454 = _15 + NL_12 + _147;
  protected final String _455 = _258 + NL_12 + _190;
  protected final String _456 = NL_12 + _182;
  protected final String _457 = NL_8 + _134 + NL_10 + _228 + NL_10 + _113 + NL_8 + _64 + NL_8 + _124;
  protected final String _458 = NL + _50;
  protected final String _459 = NL_8 + _62;
  protected final String _460 = NL_10 + _241;
  protected final String _461 = NL_10 + _231 + NL_8 + _71;
  protected final String _462 = NL_8 + _134 + NL_8 + _112 + NL_8 + _146;
  protected final String _463 = _258 + NL_8 + _243;
  protected final String _464 = NL_8 + _196;
  protected final String _465 = NL_8 + _64 + NL_8 + _125;
  protected final String _466 = NL_8 + _134 + NL_8 + _107 + NL_8 + _146;
  protected final String _467 = _258 + NL_8 + _233 + NL_8 + _192;
  protected final String _468 = NL_8 + _106;
  protected final String _469 = NL_8 + _64 + NL_8 + _216;
  protected final String _470 = NL_10 + _163 + NL_12 + _96;
  protected final String _471 = _15 + NL_12 + _146;
  protected final String _472 = _75 + NL_12 + _22 + NL_12 + _192;
  protected final String _473 = _75 + NL_12 + _219;
  protected final String _474 = _261 + NL_14 + _162 + NL_15 + _95;
  protected final String _475 = _92 + NL_15 + _181;
  protected final String _476 = _72 + NL_14 + _68 + NL_14 + _162 + NL_15 + _97;
  protected final String _477 = _15 + NL_15 + _230 + NL_15 + _220;
  protected final String _478 = NL_16 + _157 + NL_18 + _85;
  protected final String _479 = _261 + NL_19 + _146;
  protected final String _480 = NL_19 + _194;
  protected final String _481 = _75 + NL_18 + _52 + NL_16 + _68;
  protected final String _482 = NL_15 + _79 + NL_14 + _68 + NL_12 + _79 + NL_10 + _68;
  protected final String _483 = NL_8 + _178 + NL_8 + _134 + NL_8 + _109 + NL_8 + _146;
  protected final String _484 = _258 + NL_8 + _239 + NL_8 + _192;
  protected final String _485 = NL_8 + _108;
  protected final String _486 = NL_8 + _64 + NL_8 + _217;
  protected final String _487 = NL_8 + _163 + NL_10 + _117;
  protected final String _488 = _301 + NL_12 + _94;
  protected final String _489 = _15 + NL_12 + _143;
  protected final String _490 = _258 + NL_12 + _188;
  protected final String _491 = _57 + NL_12 + _191;
  protected final String _492 = _20 + NL_10 + _62 + NL_10 + _209;
  protected final String _493 = NL_12 + _163 + NL_14 + _85;
  protected final String _494 = _52 + NL_12 + _68;
  protected final String _495 = NL_10 + _79 + NL_8 + _68;
  protected final String _496 = NL_8 + _178 + NL_8 + _134 + NL_8 + _101 + NL_8 + _146;
  protected final String _497 = _258 + NL_8 + _238 + NL_8 + _192;
  protected final String _498 = NL_8 + _100;
  protected final String _499 = NL_8 + _64 + NL_8 + _212;
  protected final String _500 = NL_8 + _163;
  protected final String _501 = NL_10 + _130 + NL_12 + _94;
  protected final String _502 = _258 + NL_12 + _198;
  protected final String _503 = _19 + NL_10 + _62;
  protected final String _504 = NL_12 + _94;
  protected final String _505 = NL_12 + _13;
  protected final String _506 = NL_12 + _143;
  protected final String _507 = NL_12 + _191;
  protected final String _508 = NL_10 + _209;
  protected final String _509 = NL + _49;
  protected final String _510 = NL_8 + _178 + NL_8 + _134 + NL_8 + _102 + NL_8 + _146;
  protected final String _511 = _258 + NL_8 + _237 + NL_8 + _192;
  protected final String _512 = NL_8 + _64 + NL_8 + _213;
  protected final String _513 = NL_10 + _163 + NL_12 + _94;
  protected final String _514 = NL_12 + _142;
  protected final String _515 = NL_12 + _186;
  protected final String _516 = _75 + NL_12 + _192;
  protected final String _517 = _75 + NL_12 + _210;
  protected final String _518 = NL_14 + _163 + NL_15 + _85;
  protected final String _519 = _261 + NL_17 + _142;
  protected final String _520 = NL_17 + _194;
  protected final String _521 = _75 + NL_15 + _52 + NL_14 + _68;
  protected final String _522 = NL_12 + _79 + NL_10 + _68;
  protected final String _523 = NL_8 + _178 + NL_8 + _134 + NL_8 + _103 + NL_8 + _146;
  protected final String _524 = _258 + NL_8 + _229 + NL_8 + _192;
  protected final String _525 = NL_8 + _64 + NL_8 + _214;
  protected final String _526 = NL_10 + _161 + NL_13 + _85;
  protected final String _527 = _261 + NL_14 + _146;
  protected final String _528 = NL_14 + _194;
  protected final String _529 = _75 + NL_12 + _52;
  protected final String _530 = NL_12 + _105;
  protected final String _531 = NL_10 + _68;
  protected final String _532 = NL_8 + _178 + NL_8 + _134 + NL_8 + _111 + NL_8 + _145;
  protected final String _533 = _258 + NL_8 + _236 + NL_8 + _192;
  protected final String _534 = NL_8 + _110;
  protected final String _535 = NL_8 + _64 + NL_8 + _218;
  protected final String _536 = NL_10 + _161;
  protected final String _537 = NL_12 + _85;
  protected final String _538 = NL_12 + _208;
  protected final String _539 = _261 + NL_17 + _146;
  protected final String _540 = NL_12 + _79;
  protected final String _541 = NL_8 + _178 + NL_8 + _134 + NL_8 + _99 + NL_8 + _146;
  protected final String _542 = _258 + NL_8 + _226 + NL_8 + _192;
  protected final String _543 = NL_8 + _98;
  protected final String _544 = NL_8 + _64 + NL_8 + _211;
  protected final String _545 = NL_12 + _52;
  protected final String _546 = NL_8 + _134 + NL_8 + _104 + NL_8 + _146;
  protected final String _547 = _258 + NL_8 + _232 + NL_8 + _192;
  protected final String _548 = NL_8 + _64 + NL_8 + _126 + NL_10 + _202;
  protected final String _549 = NL_10 + _203 + NL_12 + _154;
  protected final String _550 = NL_12 + _150;
  protected final String _551 = NL_12 + _155;
  protected final String _552 = NL_12 + _151;
  protected final String _553 = NL_12 + _152;
  protected final String _554 = NL_12 + _153;
  protected final String _555 = NL_10 + _75;
  protected final String _556 = NL_10 + _221;
  protected final String _557 = NL_12 + _160;
  protected final String _558 = _8 + NL_14 + _85;
  protected final String _559 = _261 + NL_15 + _146;
  protected final String _560 = _258 + NL_15 + _201;
  protected final String _561 = _75 + NL_15 + _193;
  protected final String _562 = _75 + NL_14 + _52;
  protected final String _563 = NL_14 + _105;
  protected final String _564 = NL_14 + _17;
  protected final String _565 = NL_15 + _146;
  protected final String _566 = _258 + NL_15 + _192;
  protected final String _567 = _75 + NL_14 + _18;
  protected final String _568 = NL_14 + _244;
  protected final String _569 = NL_12 + _68;
  protected final String _570 = NL_10 + _79 + NL_8 + _62 + NL_8 + _129 + NL_8 + _62;
  protected final String _571 = NL_5 + _62 + NL_4 + _62 + NL_3 + _62 + NL_3 + _69 + NL_1 + _60 + NL + _67;

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
    stringBuffer.append(_390);
    stringBuffer.append(reporter.getTitle());
    stringBuffer.append(_391);
    stringBuffer.append(_392);
    stringBuffer.append(_393);
    stringBuffer.append('\u25B7');
    stringBuffer.append(_394);
    stringBuffer.append(_395);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() == null) {
    stringBuffer.append(_396);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_68);
    } else {
    stringBuffer.append(_397);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_261);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_56);
    }
    }
    stringBuffer.append(_398);
    stringBuffer.append(_399);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() != null) {
    stringBuffer.append(_400);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_261);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_401);
    } else {
      if (iuReport == null) {
    stringBuffer.append(_402);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_401);
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
    stringBuffer.append(_403);
    if (index != -1) {
    stringBuffer.append(_404);
    }
    stringBuffer.append(_405);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_261);
    stringBuffer.append(NL_9);
    stringBuffer.append(label);
    stringBuffer.append(_406);
    }
    }
    stringBuffer.append(_407);
    if (iuReport == null) {
    stringBuffer.append(_408);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_63);
    } else {
    stringBuffer.append(_409);
    stringBuffer.append(report.getIUImage(iuReport.getIU()));
    stringBuffer.append(_258);
    String name = report.getName(iuReport.getIU(), false);
    if (name != null) {
    stringBuffer.append(NL_9);
    stringBuffer.append(helper.htmlEscape(name, true));
    stringBuffer.append(_92);
    } else {
    stringBuffer.append(_410);
    }
    stringBuffer.append(NL_9);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_411);
    }
    stringBuffer.append(_412);
    if (report != null && report.getDate() != null) {
    stringBuffer.append(_413);
    stringBuffer.append(report.getDate());
    stringBuffer.append(_59);
    }
    stringBuffer.append(_414);
    stringBuffer.append(reporter.getNow());
    stringBuffer.append(_415);
    if (report != null && !report.getSiteURL().startsWith("file:")) {
    stringBuffer.append(_416);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_417);
    }
    stringBuffer.append(_418);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_419);
    stringBuffer.append(reporter.getReportBrandingImage());
    stringBuffer.append(_246);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_420);
    if (indexReport != null) {
    stringBuffer.append(_421);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_258);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_422);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_257);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_55);
    Map<String, String> allReports = indexReport.getAllReports();
    if (allReports != null && !allReports.isEmpty()) {
    stringBuffer.append(_423);
    for (Map.Entry<String, String> entry : allReports.entrySet()) {
    stringBuffer.append(_424);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_261);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_56);
    }
    stringBuffer.append(_425);
    }
    } else if (iuReport != null) {
    stringBuffer.append(_426);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_427);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_258);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_428);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_257);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_429);
    String provider = iuReport.getProvider();
    if (provider != null) {
    stringBuffer.append(_430);
    stringBuffer.append(helper.htmlEscape(provider, false));
    stringBuffer.append(_71);
    }
    String description = iuReport.getDescription();
    if (description != null) {
    stringBuffer.append(_431);
    stringBuffer.append(helper.htmlEscape(description, false));
    stringBuffer.append(_71);
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iuReport.getIU());
    if (!artifacts.isEmpty()) {
      Set<PGPPublicKey> pgpKeys = report.getPGPKeys(iuReport.getIU());
      if (!pgpKeys.isEmpty()) {
    stringBuffer.append(_432);
    for (PGPPublicKey pgpPublicKey : pgpKeys) {
          String fingerPrint = PGPPublicKeyService.toHexFingerprint(pgpPublicKey);
    stringBuffer.append(_433);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_434);
    stringBuffer.append(report.getKeyServerURL(pgpPublicKey));
    stringBuffer.append(_254);
    stringBuffer.append(fingerPrint);
    stringBuffer.append(_435);
    }
      }
      Set<List<Certificate>> allCertificates = report.getCertificates(iuReport.getIU());
      if (!allCertificates.isEmpty()) {
        Set<Certificate> expiredCertificates = new HashSet<Certificate>();
        for (List<Certificate> certificates : allCertificates) {
          for (Certificate certificate : certificates) {
            if (report.isExpired(certificate)) {
              expiredCertificates.add(certificate);
            }
            break;
          }
        }
    stringBuffer.append(_436);
    if (!expiredCertificates.isEmpty()) {
    stringBuffer.append(_437);
    stringBuffer.append(expiredCertificates.size());
    stringBuffer.append(_2);
    if (expiredCertificates.size() > 1) {
    stringBuffer.append(_355);
    }
    stringBuffer.append(_20);
    }
    stringBuffer.append(_438);
    for (List<Certificate> certificates : allCertificates) {
    if (certificates.isEmpty()) {
    stringBuffer.append(_439);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_440);
    } else {
    int count = 0;
            for (Certificate certificate : certificates) {
              Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_441);
    stringBuffer.append(count++);
    stringBuffer.append(_442);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_258);
    for (Map.Entry<String, String> component : components.entrySet()) {
                String key = component.getKey();
                String value = component.getValue();
                String keyStyle;
                String spanStyle;
                if ("to".equals(key) && expiredCertificates.contains(certificate)) {
                  keyStyle = "color: Firebrick; ";
                  spanStyle = " style='font-weight: bold; color: Firebrick;'";
                } else {
                  keyStyle = "color: SteelBlue; ";
                  spanStyle = "";
                }
    stringBuffer.append(_443);
    stringBuffer.append(spanStyle);
    stringBuffer.append(_225);
    stringBuffer.append(keyStyle);
    stringBuffer.append(_307);
    stringBuffer.append(key);
    stringBuffer.append(_223);
    stringBuffer.append(value);
    stringBuffer.append(_75);
    }
    stringBuffer.append(_444);
    }
    }
    }
    }
    stringBuffer.append(_445);
    for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
        String artifact = entry.getKey();
        Boolean signed = entry.getValue();
    stringBuffer.append(_446);
    if (signed != null) {
    stringBuffer.append(_447);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_258);
    }
    stringBuffer.append(_447);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_448);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_449);
    stringBuffer.append(report.getRepositoryURL(artifact) + '/' + artifact);
    stringBuffer.append(_261);
    stringBuffer.append(artifact);
    stringBuffer.append(_52);
    for (String status : report.getArtifactStatus(artifact)) {
    stringBuffer.append(_450);
    stringBuffer.append(status);
    stringBuffer.append(_75);
    }
    stringBuffer.append(_451);
    }
    }
    List<Report.LicenseDetail> licenses = report.getLicenses(iuReport.getIU());
    if (licenses != null) {
    stringBuffer.append(_452);
    for (LicenseDetail license : licenses) {
    String id = license.getUUID();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
    stringBuffer.append(_453);
    stringBuffer.append(id);
    stringBuffer.append(_267);
    stringBuffer.append(id);
    stringBuffer.append(_454);
    stringBuffer.append(report.getLicenseImage());
    stringBuffer.append(_455);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_261);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_75);
    stringBuffer.append(NL_12);
    stringBuffer.append(id);
    stringBuffer.append(_456);
    stringBuffer.append(id);
    stringBuffer.append(_252);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_261);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_200);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_261);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_76);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_261);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_75);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_72);
    }
    }
    stringBuffer.append(_457);
    for (String xml : report.getXML(iuReport.getIU(), Collections.<String, String> emptyMap())) {
    stringBuffer.append(_458);
    stringBuffer.append(xml);
    }
    stringBuffer.append(_459);
    } else {
    stringBuffer.append(_426);
    stringBuffer.append(report.getHelpLink());
    stringBuffer.append(_427);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_258);
    stringBuffer.append(NL_12);
    stringBuffer.append(report.getHelpText());
    stringBuffer.append(_428);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_257);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_429);
    List<Report> children = report.getChildren();
    if (!children.isEmpty()) {
    stringBuffer.append(_446);
    if (!report.isRoot()) {
    stringBuffer.append(_460);
    }
    stringBuffer.append(_461);
    }
    String metadataXML = report.getMetadataXML();
    String artifactXML = report.getArtifactML();
    if (metadataXML != null || artifactXML != null) {
    stringBuffer.append(_462);
    stringBuffer.append(report.getRepositoryImage());
    stringBuffer.append(_463);
    if (metadataXML != null && metadataXML.contains("bad-absolute-location") || artifactXML != null && artifactXML.contains("bad-absolute-location")) {
    stringBuffer.append(_464);
    }
    stringBuffer.append(_465);
    if (metadataXML != null) {
    stringBuffer.append(_458);
    stringBuffer.append(metadataXML);
    }
    if (artifactXML != null) {
    if (metadataXML != null) {
    stringBuffer.append(NL);
    }
    stringBuffer.append(_458);
    stringBuffer.append(artifactXML);
    }
    stringBuffer.append(_459);
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
    stringBuffer.append(_466);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_467);
    stringBuffer.append(licenses.size());
    stringBuffer.append(_75);
    if (nonConformant != 0) {
    stringBuffer.append(_437);
    stringBuffer.append(nonConformant);
    stringBuffer.append(_4);
    }
    {
        StringBuilder onClick = new StringBuilder();
        for (LicenseDetail license : licenses.keySet()) {
          String id = license.getUUID();
          onClick.append("expand2('licenses_all_arrows', '").append("_" + id).append("');");
          onClick.append("expand2('licenses_all_arrows', '").append("__" + id).append("');");
          onClick.append("expand3('licenses_all_arrows', '").append("_f" + id).append("');");
        }
    stringBuffer.append(_468);
    stringBuffer.append(displayButton);
    stringBuffer.append(_45);
    stringBuffer.append(onClick);
    stringBuffer.append(_262);
    }
    stringBuffer.append(_469);
    stringBuffer.append(display);
    stringBuffer.append(_41);
    for (Map.Entry<LicenseDetail, Set<IInstallableUnit>> entry : licenses.entrySet()) {
        LicenseDetail license = entry.getKey();
        String id = license.getUUID();
        Set<IInstallableUnit> ius = entry.getValue();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
        {
    stringBuffer.append(_470);
    stringBuffer.append(id);
    stringBuffer.append(_270);
    stringBuffer.append(id);
    stringBuffer.append(_471);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_455);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_261);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_472);
    stringBuffer.append(ius.size());
    stringBuffer.append(_473);
    stringBuffer.append(id);
    stringBuffer.append(_474);
    stringBuffer.append(id);
    stringBuffer.append(_269);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    stringBuffer.append(NL_15);
    stringBuffer.append(id);
    stringBuffer.append(_475);
    stringBuffer.append(id);
    stringBuffer.append(_252);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_261);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_200);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_261);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_76);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_261);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_75);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_476);
    stringBuffer.append(id);
    stringBuffer.append(_271);
    stringBuffer.append(id);
    stringBuffer.append(_477);
    stringBuffer.append(id);
    stringBuffer.append(_261);
    for (IInstallableUnit iu : ius) {
    stringBuffer.append(_478);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_479);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_258);
    stringBuffer.append(NL_19);
    stringBuffer.append(helper.htmlEscape(report.getName(iu, true), true));
    stringBuffer.append(_480);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_481);
    }
    stringBuffer.append(_482);
    }
    }
    stringBuffer.append(_425);
    }
    Map<PGPPublicKey, Map<String, IInstallableUnit>> pgpKeys = report.getPGPKeys();
    if (!pgpKeys.isEmpty()) {
      int idCount = 0;
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_483);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_484);
    stringBuffer.append(pgpKeys.size());
    stringBuffer.append(_75);
    {
        StringBuilder onClick = new StringBuilder();
        for (int i = 0, size = pgpKeys.size(); i < size; ++i) {
          String id = "pgpKeys" + ++idCount;
          onClick.append("expand2('pgpKeys_all_arrows', '").append(id).append("');");
        }
        idCount = 0;
    stringBuffer.append(_485);
    stringBuffer.append(displayButton);
    stringBuffer.append(_46);
    stringBuffer.append(onClick);
    stringBuffer.append(_262);
    }
    stringBuffer.append(_486);
    stringBuffer.append(display);
    stringBuffer.append(_42);
    for (Map.Entry<PGPPublicKey, Map<String, IInstallableUnit>> entry : pgpKeys.entrySet()) {
        PGPPublicKey pgpPublicKey = entry.getKey();
        String fingerPrint = PGPPublicKeyService.toHexFingerprint(pgpPublicKey);
        String id = "pgpKeys" + ++idCount;
    stringBuffer.append(_487);
    stringBuffer.append(2);
    stringBuffer.append(_488);
    stringBuffer.append(id);
    stringBuffer.append(_268);
    stringBuffer.append(id);
    stringBuffer.append(_489);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_490);
    stringBuffer.append(report.getKeyServerURL(pgpPublicKey));
    stringBuffer.append(_254);
    stringBuffer.append(fingerPrint);
    stringBuffer.append(_491);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_492);
    stringBuffer.append(id);
    stringBuffer.append(_261);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_493);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_263);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_259);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_494);
    }
    stringBuffer.append(_495);
    }
    stringBuffer.append(_425);
    }
    Map<List<Certificate>, Map<String, IInstallableUnit>> allCertificates = report.getCertificates();
    if (!allCertificates.isEmpty()) {
      int idCount = 0;
      Map<String, IInstallableUnit> unsigned = allCertificates.get(Collections.emptyList());
      int expiredChainCount = 0;
      Set<Certificate> expiredCertificates = new HashSet<Certificate>();
      for (List<Certificate> list : allCertificates.keySet()) {
        boolean expired = false;
        for (Certificate certificate : list) {
          if (report.isExpired(certificate)) {
            expiredCertificates.add(certificate);
            expired = true;
          }
        }
        if (expired) {
          ++expiredChainCount;
        }
      }
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_496);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_497);
    stringBuffer.append(allCertificates.size());
    stringBuffer.append(_75);
    if (unsigned != null) {
    stringBuffer.append(_437);
    stringBuffer.append(unsigned.size());
    stringBuffer.append(_7);
    }
    if (expiredChainCount > 0) {
    stringBuffer.append(_437);
    stringBuffer.append(expiredChainCount);
    stringBuffer.append(_3);
    }
    {
        StringBuilder onClick = new StringBuilder();
        for (int i = 0, size = allCertificates.size(); i < size; ++i) {
          String id = "certificates" + ++idCount;
          onClick.append("expand2('certificates_all_arrows', '").append(id).append("');");
        }
        idCount = 0;
    stringBuffer.append(_498);
    stringBuffer.append(displayButton);
    stringBuffer.append(_44);
    stringBuffer.append(onClick);
    stringBuffer.append(_262);
    }
    stringBuffer.append(_499);
    stringBuffer.append(display);
    stringBuffer.append(_42);
    for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : allCertificates.entrySet()) {
        List<Certificate> certificates = entry.getKey();
        String id = "certificates" + ++idCount;
    stringBuffer.append(_500);
    if (certificates.isEmpty()) {
    stringBuffer.append(_501);
    stringBuffer.append(id);
    stringBuffer.append(_268);
    stringBuffer.append(id);
    stringBuffer.append(_471);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_502);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_503);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_441);
    stringBuffer.append(count++ + 2);
    stringBuffer.append(_301);
    if (count == 1) {
    stringBuffer.append(_504);
    stringBuffer.append(id);
    stringBuffer.append(_268);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    } else {
    stringBuffer.append(_505);
    }
    stringBuffer.append(_506);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_258);
    if (count == 1) {
    stringBuffer.append(_507);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_20);
    }
    for (Map.Entry<String, String> component : components.entrySet()) {
              String key = component.getKey();
              String value = component.getValue();
              String keyStyle;
              String spanStyle;
              if ("to".equals(key) && expiredCertificates.contains(certificate)) {
                keyStyle = "color: Firebrick; ";
                spanStyle = " style='font-weight: bold; color: Firebrick;'";
              } else {
                keyStyle = "color: SteelBlue; ";
                spanStyle = "";
              }
    stringBuffer.append(_443);
    stringBuffer.append(spanStyle);
    stringBuffer.append(_225);
    stringBuffer.append(keyStyle);
    stringBuffer.append(_307);
    stringBuffer.append(key);
    stringBuffer.append(_223);
    stringBuffer.append(value);
    stringBuffer.append(_75);
    }
    stringBuffer.append(_444);
    }
    }
    stringBuffer.append(_508);
    stringBuffer.append(id);
    stringBuffer.append(_261);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_493);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_263);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_259);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_494);
    }
    stringBuffer.append(_495);
    }
    stringBuffer.append(_425);
    }
    stringBuffer.append(_509);
    Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
    if (!featureProviders.isEmpty()) {
      int nonEclipse = 0;
      for (String provider : featureProviders.keySet()) {
        if (!provider.toLowerCase().contains("eclipse"))
          ++nonEclipse;
      }
    stringBuffer.append(_510);
    stringBuffer.append(report.getProviderImage());
    stringBuffer.append(_511);
    stringBuffer.append(featureProviders.size());
    stringBuffer.append(_75);
    if (nonEclipse != 0) {
    stringBuffer.append(_437);
    stringBuffer.append(nonEclipse);
    stringBuffer.append(_6);
    }
    stringBuffer.append(_512);
    int count = 0;
      for (Map.Entry<String, Set<IInstallableUnit>> provider : featureProviders.entrySet()) {
        String style = !provider.getKey().toLowerCase().contains("eclipse") ? " style=\"color: FireBrick;\"" : "";
        Set<IInstallableUnit> features = provider.getValue();
        String id = "nested_feature_providers" + ++count;
    stringBuffer.append(_513);
    stringBuffer.append(id);
    stringBuffer.append(_266);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    for (String image : report.getBrandingImages(features)) {
    stringBuffer.append(_514);
    stringBuffer.append(image);
    stringBuffer.append(_258);
    }
    stringBuffer.append(_515);
    stringBuffer.append(style);
    stringBuffer.append(_224);
    stringBuffer.append(provider.getKey());
    stringBuffer.append(_516);
    stringBuffer.append(features.size());
    stringBuffer.append(_517);
    stringBuffer.append(id);
    stringBuffer.append(_251);
    for (IInstallableUnit feature : features) {
          String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_518);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_519);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_258);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_520);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_521);
    }
    stringBuffer.append(_522);
    }
    stringBuffer.append(_425);
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
    stringBuffer.append(_523);
    stringBuffer.append(report.getFeatureImage());
    stringBuffer.append(_524);
    stringBuffer.append(features.size());
    stringBuffer.append(_75);
    if (brokenBranding != 0) {
    stringBuffer.append(_437);
    stringBuffer.append(brokenBranding);
    stringBuffer.append(_1);
    }
    if (noBranding != 0) {
    stringBuffer.append(_437);
    stringBuffer.append(noBranding);
    stringBuffer.append(_5);
    }
    stringBuffer.append(_525);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_526);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_527);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_258);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_528);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_529);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        for (LicenseDetail license : report.getLicenses(feature)) {
          String id = license.getUUID();
          licenseReplacements.put(">" + id, "><button class='bb search-for-me' style='" + helper.getStyle(license) + "' onclick=\"clickOnButton('lic_" + id
              + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_530);
    stringBuffer.append(id);
    stringBuffer.append(_247);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_249);
    stringBuffer.append(id);
    stringBuffer.append(_264);
    stringBuffer.append(id);
    stringBuffer.append(_265);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_61);
    }
    stringBuffer.append(_531);
    }
    stringBuffer.append(_425);
    }
    Collection<IInstallableUnit> products = report.getProducts();
    if (!products.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_532);
    stringBuffer.append(report.getProductImage());
    stringBuffer.append(_533);
    stringBuffer.append(products.size());
    stringBuffer.append(_75);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit product : products) {
          String productID = "_product_" + report.getIUID(product);
          onClick.append("expand2('products_all_arrows', '").append(productID).append("');");
        }
    stringBuffer.append(_534);
    stringBuffer.append(displayButton);
    stringBuffer.append(_47);
    stringBuffer.append(onClick);
    stringBuffer.append(_262);
    }
    stringBuffer.append(_535);
    stringBuffer.append(display);
    stringBuffer.append(_41);
    for (IInstallableUnit product : products) {
        String productImage = report.getIUImage(product);
        String productID = "_product_" + report.getIUID(product);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(product));
    stringBuffer.append(_536);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_504);
    stringBuffer.append(productID);
    stringBuffer.append(_268);
    stringBuffer.append(productID);
    stringBuffer.append(_15);
    }
    stringBuffer.append(_537);
    stringBuffer.append(report.getRelativeIUReportURL(product));
    stringBuffer.append(_527);
    stringBuffer.append(productImage);
    stringBuffer.append(_258);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(product, true), true));
    stringBuffer.append(_528);
    stringBuffer.append(report.getVersion(product));
    stringBuffer.append(_529);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_538);
    stringBuffer.append(productID);
    stringBuffer.append(_250);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_518);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_539);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_258);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_520);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_521);
    }
    stringBuffer.append(_540);
    }
    stringBuffer.append(_531);
    }
    stringBuffer.append(_425);
    }
    Collection<IInstallableUnit> categories = report.getCategories();
    if (!categories.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_541);
    stringBuffer.append(report.getCategoryImage());
    stringBuffer.append(_542);
    stringBuffer.append(categories.size());
    stringBuffer.append(_75);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit category : categories) {
          String categoryID = "_category_" + report.getIUID(category);
          onClick.append("expand2('categories_all_arrows', '").append(categoryID).append("');");
        }
    stringBuffer.append(_543);
    stringBuffer.append(displayButton);
    stringBuffer.append(_43);
    stringBuffer.append(onClick);
    stringBuffer.append(_262);
    }
    stringBuffer.append(_544);
    stringBuffer.append(display);
    stringBuffer.append(_41);
    for (IInstallableUnit category : categories) {
        String categoryImage = report.getIUImage(category);
        String categoryID = "_category_" + report.getIUID(category);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(category));
    stringBuffer.append(_536);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_504);
    stringBuffer.append(categoryID);
    stringBuffer.append(_268);
    stringBuffer.append(categoryID);
    stringBuffer.append(_15);
    }
    stringBuffer.append(_537);
    stringBuffer.append(report.getRelativeIUReportURL(category));
    stringBuffer.append(_527);
    stringBuffer.append(categoryImage);
    stringBuffer.append(_258);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(category, true), true));
    stringBuffer.append(_545);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_538);
    stringBuffer.append(categoryID);
    stringBuffer.append(_253);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_518);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_539);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_258);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_520);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_521);
    }
    stringBuffer.append(_540);
    }
    stringBuffer.append(_531);
    }
    stringBuffer.append(_425);
    }
    Collection<IInstallableUnit> ius = report.getAllIUs();
    if (!ius.isEmpty()) {
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
    stringBuffer.append(_546);
    stringBuffer.append(report.getBundleImage());
    stringBuffer.append(_547);
    stringBuffer.append(ius.size());
    stringBuffer.append(_75);
    if (duplicateCount > 0) {
    stringBuffer.append(_437);
    stringBuffer.append(duplicateCount);
    stringBuffer.append(_9);
    }
    stringBuffer.append(_548);
    if (duplicateCount > 0 || !unsignedIUs.isEmpty() || !badProviderIUs.isEmpty() || !badLicenseIUs.isEmpty() || !brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_549);
    if (!badLicenseIUs.isEmpty()) {
    stringBuffer.append(_550);
    }
    if (!unsignedIUs.isEmpty()) {
    stringBuffer.append(_551);
    }
    if (!badProviderIUs.isEmpty()) {
    stringBuffer.append(_552);
    }
    if (!brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_553);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_554);
    }
    stringBuffer.append(_555);
    }
    stringBuffer.append(_556);
    for (IInstallableUnit iu : ius) {
        String iuID = iu.getId();
        String id = report.getIUID(iu);
        boolean duplicateVersions = isSimple && !report.isDuplicationExpected(iu.getId()) && iuVersions.get(iu.getId()).size() > 1;
        String versionStyle = duplicateVersions ? " font-weight: bold;" : "";
        StringBuilder classNames = new StringBuilder();
        if (duplicateVersions) {
          classNames.append(" duplicate");
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
    stringBuffer.append(_557);
    stringBuffer.append(id);
    stringBuffer.append(_248);
    stringBuffer.append(classNames);
    stringBuffer.append(_558);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_559);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_560);
    stringBuffer.append(iuID);
    stringBuffer.append(_561);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_261);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_562);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        if (report.isFeature(iu)) {
          for (LicenseDetail license : report.getLicenses(iu)) {
            String licenseID = license.getUUID();
            licenseReplacements.put(">" + licenseID, "><button class='bb search-for-me' style='" + helper.getStyle(license)
                + "' onclick=\"clickOnButton('lic_" + licenseID + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_563);
    stringBuffer.append(licenseID);
    stringBuffer.append(_247);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_249);
    stringBuffer.append(licenseID);
    stringBuffer.append(_264);
    stringBuffer.append(licenseID);
    stringBuffer.append(_265);
    stringBuffer.append(licenseID);
    stringBuffer.append(_14);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_61);
    }
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iu);
        for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
          String artifact = entry.getKey();
          Boolean signed = entry.getValue();
    stringBuffer.append(_564);
    if (signed != null) {
    stringBuffer.append(_565);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_258);
    }
    stringBuffer.append(_565);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_566);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_567);
    }
    String brandingImage = report.getBrandingImage(iu);
        if (brandingImage != null) {
    stringBuffer.append(_568);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_260);
    }
    stringBuffer.append(_569);
    }
    stringBuffer.append(_570);
    }
    }
    stringBuffer.append(_571);
    return stringBuffer.toString();
  }
}
