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
import org.eclipse.equinox.p2.metadata.*;
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
  protected static final String _5 = " Missing *.pack.gz)</span>";
  protected static final String _6 = " No Branding Images)</span>";
  protected static final String _7 = " Not Provided by Eclipse)</span>";
  protected static final String _8 = " Unsigned Artifacts)</span>";
  protected static final String _9 = " font-smaller\">";
  protected static final String _10 = " with Multiple Versions)</span>";
  protected static final String _11 = "$(\"body\").append($temp);";
  protected static final String _12 = "$temp.remove();";
  protected static final String _13 = "$temp.val($(element).text()).select();";
  protected static final String _14 = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  protected static final String _15 = "');\">";
  protected static final String _16 = "');\">&#x25B7;</button>";
  protected static final String _17 = "'){";
  protected static final String _18 = "(";
  protected static final String _19 = ")";
  protected static final String _20 = ") Unsigned</span>";
  protected static final String _21 = ")</span>";
  protected static final String _22 = "++count;";
  protected static final String _23 = "-";
  protected static final String _24 = ".bad-absolute-location {";
  protected static final String _25 = ".bb {";
  protected static final String _26 = ".filter {";
  protected static final String _27 = ".fit-image {";
  protected static final String _28 = ".font-smaller {";
  protected static final String _29 = ".iu-link {";
  protected static final String _30 = ".resolved-requirement {";
  protected static final String _31 = ".text-nowrap {";
  protected static final String _32 = ".unresolved-requirement {";
  protected static final String _33 = ".unused-capability {";
  protected static final String _34 = ".used-capability {";
  protected static final String _35 = ".xml-attribute {";
  protected static final String _36 = ".xml-attribute-value {";
  protected static final String _37 = ".xml-element {";
  protected static final String _38 = ".xml-element-value {";
  protected static final String _39 = ".xml-iu {";
  protected static final String _40 = ".xml-repo {";
  protected static final String _41 = ".xml-token {";
  protected static final String _42 = "; margin-left: -1em; list-style-type: none; padding: 0; margin: 0;\">";
  protected static final String _43 = "; margin-left: -1em; list-style-type: none;\">";
  protected static final String _44 = ";\" onclick=\"toggle('categories_all_arrows');";
  protected static final String _45 = ";\" onclick=\"toggle('certificates_all_arrows');";
  protected static final String _46 = ";\" onclick=\"toggle('licenses_all_arrows');";
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
  protected static final String _57 = "</aside>";
  protected static final String _58 = "</b>";
  protected static final String _59 = "</body>";
  protected static final String _60 = "</button>";
  protected static final String _61 = "</div>";
  protected static final String _62 = "</h2>";
  protected static final String _63 = "</h3>";
  protected static final String _64 = "</head>";
  protected static final String _65 = "</header>";
  protected static final String _66 = "</html>";
  protected static final String _67 = "</li>";
  protected static final String _68 = "</main>";
  protected static final String _69 = "</ol>";
  protected static final String _70 = "</p>";
  protected static final String _71 = "</pre>";
  protected static final String _72 = "</script>";
  protected static final String _73 = "</section>";
  protected static final String _74 = "</span>";
  protected static final String _75 = "</span><span style=\"color: green; text-decoration: underline; ";
  protected static final String _76 = "</style>";
  protected static final String _77 = "</title>";
  protected static final String _78 = "</ul>";
  protected static final String _79 = "<a class=\"separator\" href=\"";
  protected static final String _80 = "<a class=\"separator\" href=\".\">";
  protected static final String _81 = "<a class=\"separator\" href=\"https://www.eclipse.org/downloads\">Downloads</a>";
  protected static final String _82 = "<a class=\"separator\" href=\"https://www.eclipse.org\">Home</a>";
  protected static final String _83 = "<a class=\"separator\" style=\"display: inline-block; margin-left: 0.25em;\" href=\"";
  protected static final String _84 = "<a href=\"";
  protected static final String _85 = "<a href=\"https://wiki.eclipse.org/Oomph_Repository_Analyzer#Description\" target=\"oomph_wiki\">";
  protected static final String _86 = "<a href=\"https://www.eclipse.org/\">";
  protected static final String _87 = "<a style=\"float:right; font-size: 200%;\" href=\"";
  protected static final String _88 = "<aside id=\"leftcol\" class=\"col-md-4 font-smaller\">";
  protected static final String _89 = "<b>Built: ";
  protected static final String _90 = "<body id=\"body_solstice\">";
  protected static final String _91 = "<br/>";
  protected static final String _92 = "<br/><b>Reported: ";
  protected static final String _93 = "<button id=\"";
  protected static final String _94 = "<button id=\"_";
  protected static final String _95 = "<button id=\"__";
  protected static final String _96 = "<button id=\"_f";
  protected static final String _97 = "<button id=\"categories_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _98 = "<button id=\"categories_arrows\" class=\"orange bb\" onclick=\"expand_collapse('categories'); expand_collapse_inline('categories_all_arrows');\">&#x25B7;</button>";
  protected static final String _99 = "<button id=\"certificates_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _100 = "<button id=\"certificates_arrows\" class=\"orange bb\" onclick=\"expand_collapse('certificates'); expand_collapse_inline('certificates_all_arrows');\">&#x25B7;</button>";
  protected static final String _101 = "<button id=\"feature_providers_arrows\" class=\"orange bb\" onclick=\"expand_collapse('feature_providers');\">&#x25B7;</button>";
  protected static final String _102 = "<button id=\"features_arrows\" class=\"orange bb\" onclick=\"expand_collapse('features');\">&#x25B7;</button>";
  protected static final String _103 = "<button id=\"ius_arrows\" class=\"orange bb\" onclick=\"expand_collapse('ius'); match('subset');\">&#x25B7;</button>";
  protected static final String _104 = "<button id=\"lic_";
  protected static final String _105 = "<button id=\"licenses_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _106 = "<button id=\"licenses_arrows\" class=\"orange bb\" onclick=\"expand_collapse('licenses'); expand_collapse_inline('licenses_all_arrows');\">&#x25B7;</button>";
  protected static final String _107 = "<button id=\"products_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _108 = "<button id=\"products_arrows\" class=\"orange bb\" onclick=\"expand_collapse('products'); expand_collapse_inline('products_all_arrows');\">&#x25B7;</button>";
  protected static final String _109 = "<button id=\"repos_arrows\" class=\"orange bb\" onclick=\"expand_collapse('repos');\">&#x25B7;</button>";
  protected static final String _110 = "<button id=\"xml-links\" class=\"orange bb\" onclick=\"expand_collapse_all('xml-links');\">&#x25B7;</button>";
  protected static final String _111 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 150%;\" onclick=\"copyToClipboard('#p1')\">&#x270e;</button>";
  protected static final String _112 = "<div class=\"col-sm-16 padding-left-30\">";
  protected static final String _113 = "<div class=\"container\">";
  protected static final String _114 = "<div class=\"font-smaller\" style=\"margin-left: ";
  protected static final String _115 = "<div class=\"hidden-xs col-sm-8 col-md-6 col-lg-5\" id=\"header-left\">";
  protected static final String _116 = "<div class=\"novaContent container\" id=\"novaContent\">";
  protected static final String _117 = "<div class=\"row\" id=\"header-row\">";
  protected static final String _118 = "<div class=\"row\">";
  protected static final String _119 = "<div class=\"wrapper-logo-default\">";
  protected static final String _120 = "<div class=\"xml-iu\" style=\"display:block;\">";
  protected static final String _121 = "<div class=\"xml-repo\" id=\"repos\" style=\"display: none;\">";
  protected static final String _122 = "<div id=\"ius\" style=\"display:none;\">";
  protected static final String _123 = "<div id=\"maincontent\">";
  protected static final String _124 = "<div id=\"midcolumn\" style=\"width: 70%;\">";
  protected static final String _125 = "<div style=\"height: 40ex;\">";
  protected static final String _126 = "<div style=\"text-indent: -2em\">";
  protected static final String _127 = "<div>";
  protected static final String _128 = "<h2 style=\"text-align: center;\">";
  protected static final String _129 = "<h3 class=\"sr-only\">Breadcrumbs</h3>";
  protected static final String _130 = "<h3 style=\"font-weight: bold;\">";
  protected static final String _131 = "<h3 style=\"font-weight: bold;\">Artifacts</h3>";
  protected static final String _132 = "<h3 style=\"font-weight: bold;\">Description</h3>";
  protected static final String _133 = "<h3 style=\"font-weight: bold;\">Licenses</h3>";
  protected static final String _134 = "<h3 style=\"font-weight: bold;\">Provider</h3>";
  protected static final String _135 = "<head>";
  protected static final String _136 = "<header role=\"banner\" id=\"header-wrapper\">";
  protected static final String _137 = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">";
  protected static final String _138 = "<img alt=\"Branding Image\" class=\"fit-image\" src=\"";
  protected static final String _139 = "<img alt=\"Signed\" class=\"fit-image\" src=\"";
  protected static final String _140 = "<img alt=\"Signed\" class=\"fit-image\" src=\"../";
  protected static final String _141 = "<img class=\"fit-image> src=\"";
  protected static final String _142 = "<img class=\"fit-image\" src=\"";
  protected static final String _143 = "<img class=\"fit-image\" src=\"../";
  protected static final String _144 = "<img class=\"logo-eclipse-default img-responsive hidden-xs\" alt=\"Eclipse Log\" src=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/logo/eclipse-426x100.png\"/>";
  protected static final String _145 = "<img src=\"";
  protected static final String _146 = "<input id=\"bad-license\" type=\"radio\" name=\"filter\" value=\"bad-license\" class=\"filter\" onclick=\"filterIU('bad-license');\"> Bad License </input>";
  protected static final String _147 = "<input id=\"bad-provider\" type=\"radio\" name=\"filter\" value=\"bad-provider\" class=\"filter\" onclick=\"filterIU('bad-provider');\"> Bad Provider </input>";
  protected static final String _148 = "<input id=\"broken-branding\" type=\"radio\" name=\"filter\" value=\"broken-branding\" class=\"filter\" onclick=\"filterIU('broken-branding');\"> Broken Branding </input>";
  protected static final String _149 = "<input id=\"duplicates\" type=\"radio\" name=\"filter\" value=\"duplicate\" class=\"filter\" onclick=\"filterIU('duplicate');\"> Duplicates </input>";
  protected static final String _150 = "<input id=\"filter\" type=\"radio\" name=\"filter\" value=\"all\" class=\"filter\" onclick=\"filterIU('iu-li');\" checked=\"checked\"> All </input>";
  protected static final String _151 = "<input id=\"missing\" type=\"radio\" name=\"filter\" value=\"missing\" class=\"filter\" onclick=\"filterIU('missing');\"> Missing .pack.gz </input>";
  protected static final String _152 = "<input id=\"unsigned\" type=\"radio\" name=\"filter\" value=\"unsigned\" class=\"filter\" onclick=\"filterIU('unsigned');\"> Unsigned </input>";
  protected static final String _153 = "<li class=\"active\">";
  protected static final String _154 = "<li class=\"font-smaller text-nowrap\">";
  protected static final String _155 = "<li class=\"separator\" style=\"font-size: 90%;\">";
  protected static final String _156 = "<li class=\"separator\">";
  protected static final String _157 = "<li id=\"_iu_";
  protected static final String _158 = "<li style=\"font-size: 100%; white-space: nowrap;\">";
  protected static final String _159 = "<li style=\"margin-left: 1em;\">";
  protected static final String _160 = "<li>";
  protected static final String _161 = "<li><a href=\"";
  protected static final String _162 = "<li><a href=\"https://www.eclipse.org/\">Home</a></li>";
  protected static final String _163 = "<li><a href=\"https://www.eclipse.org/downloads/\">Downloads</a></li>";
  protected static final String _164 = "<link href=\"//fonts.googleapis.com/css?family=Open+Sans:400,700,300,600,100\" rel=\"stylesheet\" type=\"text/css\"/>";
  protected static final String _165 = "<link rel=\"icon\" type=\"image/ico\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/favicon.ico\"/>";
  protected static final String _166 = "<link rel=\"stylesheet\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/stylesheets/styles.min.css\"/>";
  protected static final String _167 = "<main class=\"no-promo\">";
  protected static final String _168 = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
  protected static final String _169 = "<meta name=\"description\" content=\"Update Sites Reports\"/>";
  protected static final String _170 = "<meta name=\"keywords\" content=\"eclipse,update site\"/>";
  protected static final String _171 = "<ol class=\"breadcrumb\">";
  protected static final String _172 = "<p style=\"font-size: 125%; text-align: center;\">";
  protected static final String _173 = "<p style=\"text-align: center;\">";
  protected static final String _174 = "<p>";
  protected static final String _175 = "<p></p>";
  protected static final String _176 = "<p>Reports are generated specifically for the following sites:</p>";
  protected static final String _177 = "<p>This report is produced by <a href=\"";
  protected static final String _178 = "<pre id=\"_";
  protected static final String _179 = "<pre id=\"__";
  protected static final String _180 = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";
  protected static final String _181 = "<script>";
  protected static final String _182 = "<section class=\"hidden-print default-breadcrumbs\" id=\"breadcrumb\">";
  protected static final String _183 = "<span ";
  protected static final String _184 = "<span class=\"text-nowrap\"";
  protected static final String _185 = "<span id=\"p1\" style=\"font-size: 125%\">";
  protected static final String _186 = "<span style=\"";
  protected static final String _187 = "<span style=\"color: DarkCyan; font-size: 110%;\">(";
  protected static final String _188 = "<span style=\"color: DarkCyan;\">";
  protected static final String _189 = "<span style=\"color: DarkOliveGreen;";
  protected static final String _190 = "<span style=\"color: DarkOliveGreen;\">";
  protected static final String _191 = "<span style=\"color: FireBrick; font-size: 60%;\">(";
  protected static final String _192 = "<span style=\"color: FireBrick; font-size: 60%;\">(Inappropriate absolute location)</span>";
  protected static final String _193 = "<span style=\"color: FireBrick; font-size: 90%;\">";
  protected static final String _194 = "<span style=\"color: FireBrick;\">(";
  protected static final String _195 = "<span style=\"color: FireBrick;\">Unsigned</span>";
  protected static final String _196 = "<span style=\"color: red; text-decoration: line-through; ";
  protected static final String _197 = "<span style=\"font-size:100%;\">";
  protected static final String _198 = "<span style=\"margin-left: 1em;\" class=\"nowrap\">Filter Pattern: <input id=\"subset\" type=\"text\" oninput=\"match('subset');\"> <span style=\"color: firebrick;\" id=\"subset-error\"></span></input></span><br/>";
  protected static final String _199 = "<span style=\"margin-left: 1em;\">";
  protected static final String _200 = "<style>";
  protected static final String _201 = "<title>";
  protected static final String _202 = "<tt style=\"float: left;\" class=\"orange\">&#xbb;</tt>";
  protected static final String _203 = "<tt style=\"float: right;\" class=\"orange\">&#xab;</tt>";
  protected static final String _204 = "<ul class=\"font-smaller\" id=\"";
  protected static final String _205 = "<ul class=\"font-smaller\" style=\"list-style-type: none; display:none; margin-left: -3em;\" id=\"";
  protected static final String _206 = "<ul id=\"";
  protected static final String _207 = "<ul id=\"categories\" style=\"display: ";
  protected static final String _208 = "<ul id=\"certificates\" style=\"display:";
  protected static final String _209 = "<ul id=\"feature_providers\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _210 = "<ul id=\"features\" style=\"display: none; list-style-type: none; margin-left: -2em;\">";
  protected static final String _211 = "<ul id=\"leftnav\" class=\"ul-left-nav fa-ul hidden-print\">";
  protected static final String _212 = "<ul id=\"licenses\" style=\"display: ";
  protected static final String _213 = "<ul id=\"products\" style=\"display: ";
  protected static final String _214 = "<ul style=\"display:none; list-style-type: none; padding: 0; margin: 0;\" id=\"__";
  protected static final String _215 = "<ul style=\"list-style-type: none; display:none; padding: 0; margin: 0; margin-left: 2em;\" id=\"_f";
  protected static final String _216 = "<ul style=\"list-style-type: none; padding: 0; margin-left: 1em;\">";
  protected static final String _217 = "<ul>";
  protected static final String _218 = "=</span>";
  protected static final String _219 = ">";
  protected static final String _220 = "><span style=\"";
  protected static final String _221 = "Categories";
  protected static final String _222 = "Certificate";
  protected static final String _223 = "Content Metadata";
  protected static final String _224 = "Features";
  protected static final String _225 = "Features/Products";
  protected static final String _226 = "In addition to this composite report, reports are also generated for each of the composed children listed in the navigation bar to the left.";
  protected static final String _227 = "Installable Units";
  protected static final String _228 = "Licenses";
  protected static final String _229 = "Missing";
  protected static final String _230 = "No Name<br/>";
  protected static final String _231 = "Products";
  protected static final String _232 = "Providers";
  protected static final String _233 = "Signing Certificates";
  protected static final String _234 = "This";
  protected static final String _235 = "This is a composite update site.";
  protected static final String _236 = "This is a generated";
  protected static final String _237 = "XML";
  protected static final String _238 = "[<img class=\"fit-image\" src=\"";
  protected static final String _239 = "\" alt=\"\"/>";
  protected static final String _240 = "\" alt=\"\"/><img style=\"margin-top: -2ex;\" class=\"fit-image\" src=\"";
  protected static final String _241 = "\" class=\"bb\" style=\"";
  protected static final String _242 = "\" class=\"iu-li";
  protected static final String _243 = "\" onclick=\"clickOnToggleButton('licenses_arrows'); clickOnToggleButton('__";
  protected static final String _244 = "\" style=\"display: none;\">";
  protected static final String _245 = "\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _246 = "\" style=\"display:none; margin-left: 2em; background-color: ";
  protected static final String _247 = "\" style=\"list-style-type: none; display: none; margin-left: -1em;\">";
  protected static final String _248 = "\" target=\"oomph_wiki\"/>";
  protected static final String _249 = "\" target=\"oomph_wiki\">";
  protected static final String _250 = "\" target=\"report_source\">";
  protected static final String _251 = "\"/>";
  protected static final String _252 = "\"/> ";
  protected static final String _253 = "\"/>]";
  protected static final String _254 = "\">";
  protected static final String _255 = "\">&#x25B7;</button>";
  protected static final String _256 = "\"><img class=\"fit-image\" src=\"";
  protected static final String _257 = "_arrows'); clickOnToggleButton('_";
  protected static final String _258 = "_arrows'); navigateTo('_";
  protected static final String _259 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('";
  protected static final String _260 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('__";
  protected static final String _261 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('";
  protected static final String _262 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_";
  protected static final String _263 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('__";
  protected static final String _264 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_f";
  protected static final String _265 = "background-color: white;";
  protected static final String _266 = "border: 1px solid black;";
  protected static final String _267 = "border: none;";
  protected static final String _268 = "break;";
  protected static final String _269 = "buttonTargets.item(i).innerHTML = '&#x25B7;';";
  protected static final String _270 = "buttonTargets.item(i).innerHTML = '&#x25E2;';";
  protected static final String _271 = "catch (err) {";
  protected static final String _272 = "color: DarkSlateGray;";
  protected static final String _273 = "color: FireBrick;";
  protected static final String _274 = "color: IndianRed;";
  protected static final String _275 = "color: MediumAquaMarine;";
  protected static final String _276 = "color: MediumOrchid;";
  protected static final String _277 = "color: SaddleBrown;";
  protected static final String _278 = "color: SeaGreen;";
  protected static final String _279 = "color: SteelBlue;";
  protected static final String _280 = "color: Teal;";
  protected static final String _281 = "continue;";
  protected static final String _282 = "currentFilter = filter.value;";
  protected static final String _283 = "document.execCommand(\"copy\");";
  protected static final String _284 = "e.click();";
  protected static final String _285 = "e.innerHTML = '&#x25B7;';";
  protected static final String _286 = "e.innerHTML = '&#x25E2;';";
  protected static final String _287 = "e.scrollIntoView();";
  protected static final String _288 = "e.style.display = 'block';";
  protected static final String _289 = "e.style.display = 'inline';";
  protected static final String _290 = "e.style.display = 'inline-block';";
  protected static final String _291 = "e.style.display = 'none';";
  protected static final String _292 = "e.title= 'Collapse All';";
  protected static final String _293 = "e.title= 'Expand All';";
  protected static final String _294 = "em; text-indent: -4em;\">";
  protected static final String _295 = "em;\">";
  protected static final String _296 = "f.innerHTML = '&#x25B7;';";
  protected static final String _297 = "f.innerHTML = '&#x25E2;';";
  protected static final String _298 = "font-family: monospace;";
  protected static final String _299 = "font-size: 125%;";
  protected static final String _300 = "font-size: 75%;\">";
  protected static final String _301 = "font-size: 80%;";
  protected static final String _302 = "font-size: 90%;";
  protected static final String _303 = "for (var i = 0; i < buttonTargets.length; i++) {";
  protected static final String _304 = "for (var i = 0; i < ius.length; i++) {";
  protected static final String _305 = "for (var i = 0; i < spanTargets.length; i++) {";
  protected static final String _306 = "function clickOnButton(id) {";
  protected static final String _307 = "function clickOnToggleButton(id) {";
  protected static final String _308 = "function copyToClipboard(element) {";
  protected static final String _309 = "function expand(id) {";
  protected static final String _310 = "function expand2(self, id) {";
  protected static final String _311 = "function expand3(self, id) {";
  protected static final String _312 = "function expand_collapse(id) {";
  protected static final String _313 = "function expand_collapse_all(base) {";
  protected static final String _314 = "function expand_collapse_inline(id) {";
  protected static final String _315 = "function expand_collapse_inline_block(id) {";
  protected static final String _316 = "function filterIU(className) {";
  protected static final String _317 = "function match(id) {";
  protected static final String _318 = "function navigateTo(id) {";
  protected static final String _319 = "function toggle(id) {";
  protected static final String _320 = "height: 2ex;";
  protected static final String _321 = "if (!targetsArray.includes(iu)) {";
  protected static final String _322 = "if ((matchText == '' || text.match(matchText) != null) && targetsArray.includes(iu)) {";
  protected static final String _323 = "if (count == 0 && message.innerHTML == '') {";
  protected static final String _324 = "if (count == 0) {";
  protected static final String _325 = "if (e.innerHTML == '";
  protected static final String _326 = "if (e.innerHTML == '\\u25E2') {";
  protected static final String _327 = "if (e.style.display == 'none'){";
  protected static final String _328 = "if (e.title == 'Expand All') {";
  protected static final String _329 = "if (f != null) {";
  protected static final String _330 = "if (f !=null) {";
  protected static final String _331 = "if (filter != null && filter.value != 'all') {";
  protected static final String _332 = "if (matchText != '' && iu.textContent.match(matchText) == null) {";
  protected static final String _333 = "if (t.title != 'Collapse All'){";
  protected static final String _334 = "if (t.title == 'Collapse All'){";
  protected static final String _335 = "iu.style.display = 'block';";
  protected static final String _336 = "iu.style.display = 'none';";
  protected static final String _337 = "margin-bottom: -2ex;";
  protected static final String _338 = "margin-left: 0em;";
  protected static final String _339 = "margin-top: -2ex;";
  protected static final String _340 = "margin: 0px 0px 0px 0px;";
  protected static final String _341 = "message.innerHTML = '';";
  protected static final String _342 = "message.innerHTML = 'No matches';";
  protected static final String _343 = "message.innerHTML = \"\";";
  protected static final String _344 = "message.innerHTML = errMessage;";
  protected static final String _345 = "padding: -2pt -2pt -2pt -2pt;";
  protected static final String _346 = "padding: 0px 0px;";
  protected static final String _347 = "report is produced by <a href=\"";
  protected static final String _348 = "s";
  protected static final String _349 = "span:target {";
  protected static final String _350 = "spanTargets.item(i).style.display = 'inline-block';";
  protected static final String _351 = "spanTargets.item(i).style.display = 'none';";
  protected static final String _352 = "try";
  protected static final String _353 = "try {";
  protected static final String _354 = "var $temp = $(\"<input>\");";
  protected static final String _355 = "var buttonTargets = document.getElementsByClassName(base + '_button');";
  protected static final String _356 = "var count = 0;";
  protected static final String _357 = "var currentFilter = 'iu-li';";
  protected static final String _358 = "var e = document.getElementById('subset');";
  protected static final String _359 = "var e = document.getElementById(base);";
  protected static final String _360 = "var e = document.getElementById(id);";
  protected static final String _361 = "var errMessage = err.message;";
  protected static final String _362 = "var f = document.getElementById(id+\"_arrows\");";
  protected static final String _363 = "var filter = document.querySelector('input[name=\"filter\"]:checked');";
  protected static final String _364 = "var iu = ius[i];";
  protected static final String _365 = "var ius = document.getElementsByClassName('iu-li');";
  protected static final String _366 = "var matchText = e.value;";
  protected static final String _367 = "var message = document.getElementById('subset-error');";
  protected static final String _368 = "var message = document.getElementById(id + '-error');";
  protected static final String _369 = "var spanTargets = document.getElementsByClassName(base);";
  protected static final String _370 = "var state = e.innerHTML;";
  protected static final String _371 = "var t = document.getElementById('all');";
  protected static final String _372 = "var t = document.getElementById(self);";
  protected static final String _373 = "var targets = document.getElementsByClassName(className);";
  protected static final String _374 = "var targets = document.getElementsByClassName(currentFilter);";
  protected static final String _375 = "var targetsArray = [].slice.call(targets);";
  protected static final String _376 = "var text = iu.textContent;";
  protected static final String _377 = "white-space: nowrap;";
  protected static final String _378 = "white-space: pre;";
  protected static final String _379 = "width: 2ex;";
  protected static final String _380 = "{";
  protected static final String _381 = "}";
  protected static final String _382 = "} else {";
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
  protected final String _383 = _51 + NL + _137 + NL + _135 + NL_1 + _168 + NL_1 + _201;
  protected final String _384 = _77 + NL_1 + _170 + NL_1 + _169 + NL_1 + _164 + NL_1 + _166 + NL_1 + _165 + NL_1 + _180;
  protected final String _385 = NL_1 + _200 + NL + NL + _31 + NL_1 + _377 + NL + _381 + NL + NL + _28 + NL_1 + _302 + NL + _381 + NL + NL + _27 + NL_1 + _379 + NL_1 + _320 + NL + _381 + NL + NL + _349 + NL_1 + _299 + NL_1 + _266 + NL + _381 + NL + NL + _40 + NL_1 + _378 + NL_1 + _267 + NL_1 + _346 + NL_1 + _339 + NL_1 + _337 + NL_1 + _338 + NL + _381 + NL + NL + _24 + NL_1 + _273 + NL_1 + _302 + NL + _381 + NL + NL + _39 + NL_1 + _378 + NL_1 + _267 + NL_1 + _346 + NL_1 + _339 + NL_1 + _337 + NL_1 + _338 + NL + _381 + NL + NL + _41 + NL_1 + _279 + NL_1 + _298 + NL_1 + _301 + NL + _381 + NL + NL + _35 + NL_1 + _275 + NL_1 + _298 + NL_1 + _301 + NL + _381 + NL + NL + _37 + NL_1 + _276 + NL_1 + _298 + NL_1 + _301 + NL + _381 + NL + NL + _36 + NL_1 + _272 + NL_1 + _302 + NL + _381 + NL + NL + _38 + NL_1 + _277 + NL_1 + _302 + NL + _381 + NL + NL + _25 + NL_1 + _265 + NL_1 + _267 + NL_1 + _346 + NL + _381 + NL + NL + _26 + NL_1 + _265 + NL + _381 + NL + NL + _29 + NL_1 + _345 + NL_1 + _340 + NL + _381 + NL + NL + _30 + NL_1 + _278 + NL_1 + _302 + NL + _381 + NL + NL + _34 + NL_1 + _280 + NL_1 + _302 + NL + _381 + NL + NL + _33 + NL_1 + _302 + NL + _381 + NL + NL + _32 + NL_1 + _274 + NL_1 + _302 + NL + _381 + NL_1 + _76 + NL + _64 + NL + NL_1 + _90;
  protected final String _386 = NL_2 + _181 + NL + NL_4 + _317 + NL_5 + _363 + NL_5 + _357 + NL_5 + _331 + NL_7 + _282 + NL_5 + _381 + NL_5 + _360 + NL_5 + _368 + NL_5 + _366 + NL_5 + _365 + NL_5 + _374 + NL_5 + _375 + NL_5 + _356 + NL_5 + _304 + NL_7 + _364 + NL_7 + _376 + NL_7 + _353 + NL_9 + _322 + NL_11 + _335 + NL_11 + _22 + NL_9 + _382 + NL_11 + _336 + NL_9 + _381 + NL_9 + _343 + NL_7 + _381 + NL_7 + _271 + NL_9 + _361 + NL_9 + _344 + NL_9 + _268 + NL_7 + _381 + NL_5 + _381 + NL_5 + _323 + NL_9 + _342 + NL_5 + _381 + NL_4 + _381 + NL + NL_4 + _316 + NL_5 + _358 + NL_5 + _366 + NL_5 + _356 + NL_5 + _365 + NL_5 + _373 + NL_5 + _375 + NL_5 + _304 + NL_7 + _364 + NL_7 + _321 + NL_9 + _336 + NL_7 + _382 + NL_9 + _352 + NL_9 + _380 + NL_11 + _332 + NL_13 + _336 + NL_13 + _281 + NL_11 + _381 + NL_9 + _381 + NL_9 + _271 + NL_9 + _381 + NL_9 + _335 + NL_9 + _22 + NL_7 + _381 + NL_5 + _381 + NL_5 + _367 + NL_5 + _324 + NL_9 + _342 + NL_5 + _382 + NL_9 + _341 + NL_5 + _381 + NL_4 + _381 + NL + NL_4 + _308 + NL_5 + _354 + NL_5 + _11 + NL_5 + _13 + NL_5 + _283 + NL_5 + _12 + NL_4 + _381 + NL + NL_4 + _306 + NL_5 + _360 + NL_5 + _284 + NL_4 + _381 + NL + NL_4 + _307 + NL_5 + _360 + NL_5 + _370 + NL_5 + _325;
  protected final String _387 = _17 + NL_7 + _284 + NL_5 + _381 + NL_4 + _381 + NL + NL_4 + _318 + NL_5 + _360 + NL_5 + _287 + NL_4 + _381 + NL + NL_4 + _319 + NL_5 + _360 + NL_5 + _328 + NL_7 + _292 + NL_7 + _286 + NL_5 + _382 + NL_7 + _293 + NL_7 + _285 + NL_5 + _381 + NL_4 + _381 + NL + NL_4 + _310 + NL_5 + _372 + NL_5 + _360 + NL_5 + _362 + NL_5 + _334 + NL_7 + _288 + NL_7 + _297 + NL_5 + _382 + NL_7 + _291 + NL_7 + _296 + NL_5 + _381 + NL_4 + _381 + NL + NL_4 + _311 + NL_5 + _372 + NL_5 + _360 + NL_5 + _362 + NL_5 + _333 + NL_7 + _291 + NL_7 + _296 + NL_5 + _381 + NL_4 + _381 + NL + NL_4 + _309 + NL_5 + _371 + NL_5 + _360 + NL_5 + _362 + NL_5 + _334 + NL_7 + _288 + NL_7 + _297 + NL_5 + _382 + NL_7 + _291 + NL_7 + _296 + NL_5 + _381 + NL_4 + _381 + NL + NL_4 + _312 + NL_5 + _360 + NL_5 + _362 + NL_5 + _327 + NL_7 + _288 + NL_7 + _297 + NL_5 + _382 + NL_7 + _291 + NL_7 + _296 + NL_5 + _381 + NL_4 + _381 + NL + NL_4 + _314 + NL_5 + _360 + NL_5 + _362 + NL_5 + _327 + NL_7 + _289 + NL_7 + _329 + NL_9 + _297 + NL_7 + _381 + NL_5 + _382 + NL_7 + _291 + NL_7 + _330 + NL_9 + _296 + NL_7 + _381 + NL_5 + _381 + NL_4 + _381 + NL + NL_4 + _315 + NL_5 + _360 + NL_5 + _362 + NL_5 + _327 + NL_7 + _290 + NL_7 + _329 + NL_9 + _297 + NL_7 + _381 + NL_5 + _382 + NL_7 + _291 + NL_7 + _330 + NL_9 + _296 + NL_7 + _381 + NL_5 + _381 + NL_4 + _381 + NL + NL_4 + _313 + NL_5 + _359 + NL_5 + _355 + NL_5 + _369 + NL_5 + _326 + NL_9 + _285 + NL_9 + _303 + NL_11 + _269 + NL_9 + _381 + NL_9 + _305 + NL_11 + _351 + NL_9 + _381 + NL_5 + _382 + NL_9 + _286 + NL_9 + _303 + NL_11 + _270 + NL_9 + _381 + NL_9 + _305 + NL_11 + _350 + NL_9 + _381 + NL_5 + _381 + NL_4 + _381 + NL + NL_2 + _72 + NL + NL_2 + _136 + NL_4 + _113 + NL_5 + _117 + NL_7 + _115 + NL_9 + _119 + NL_11 + _86 + NL_13 + _144 + NL_11 + _52 + NL_9 + _61 + NL_7 + _61 + NL_5 + _61 + NL_4 + _61 + NL_2 + _65;
  protected final String _388 = NL_2 + _182 + NL_4 + _113 + NL_5 + _129 + NL_5 + _118 + NL_7 + _112 + NL_9 + _171 + NL_11 + _162 + NL_11 + _163;
  protected final String _389 = NL_11 + _153;
  protected final String _390 = NL_11 + _161;
  protected final String _391 = NL_9 + _69 + NL_7 + _61 + NL_5 + _61 + NL_4 + _61 + NL_2 + _73 + NL + NL_2 + _167 + NL_2 + _116 + NL;
  protected final String _392 = NL_4 + _48 + NL_4 + _88 + NL_5 + _211 + NL_7 + _156 + NL_9 + _82 + NL_7 + _67 + NL_7 + _156 + NL_9 + _81 + NL_7 + _67;
  protected final String _393 = NL_7 + _156 + NL_9 + _79;
  protected final String _394 = _52 + NL_7 + _67;
  protected final String _395 = NL_7 + _156 + NL_9 + _203 + NL_9 + _80;
  protected final String _396 = NL_7 + _155 + NL_9 + _202;
  protected final String _397 = NL_9 + _203;
  protected final String _398 = NL_9 + _83;
  protected final String _399 = NL_9 + _52 + NL_7 + _67;
  protected final String _400 = NL_5 + _78 + NL_4 + _57 + NL + NL_4 + _123 + NL_5 + _124;
  protected final String _401 = NL_7 + _128;
  protected final String _402 = NL_7 + _128 + NL_9 + _143;
  protected final String _403 = NL_9 + _230;
  protected final String _404 = NL_7 + _62;
  protected final String _405 = NL_7 + _172;
  protected final String _406 = NL_9 + _89;
  protected final String _407 = NL_9 + _92;
  protected final String _408 = _58 + NL_7 + _70;
  protected final String _409 = NL_7 + _173 + NL_9 + _111 + NL_9 + _185;
  protected final String _410 = _74 + NL_7 + _70 + NL_7 + _91;
  protected final String _411 = NL_7 + _87;
  protected final String _412 = _248 + NL_9 + _145;
  protected final String _413 = _239 + NL_7 + _52;
  protected final String _414 = NL_8 + _174 + NL_10 + _236 + NL_10 + _85 + NL_12 + _142;
  protected final String _415 = _53 + NL_8 + _70 + NL_8 + _177;
  protected final String _416 = NL_8 + _176 + NL_8 + _217;
  protected final String _417 = NL_10 + _161;
  protected final String _418 = NL_8 + _78;
  protected final String _419 = NL_8 + _174 + NL_10 + _234 + NL_10 + _84;
  protected final String _420 = _249 + NL_12 + _142;
  protected final String _421 = _52 + NL_10 + _347;
  protected final String _422 = _54 + NL_8 + _70;
  protected final String _423 = NL_8 + _134 + NL_8 + _174;
  protected final String _424 = NL_8 + _132 + NL_8 + _174;
  protected final String _425 = NL_8 + _130 + NL_10 + _222;
  protected final String _426 = NL_8 + _191;
  protected final String _427 = NL_8 + _63;
  protected final String _428 = NL_10 + _127 + NL_12 + _143;
  protected final String _429 = _251 + NL_12 + _195 + NL_10 + _61;
  protected final String _430 = NL_10 + _114;
  protected final String _431 = _295 + NL_12 + _140;
  protected final String _432 = NL_12 + _184;
  protected final String _433 = NL_10 + _61;
  protected final String _434 = NL_8 + _131;
  protected final String _435 = NL_8 + _174;
  protected final String _436 = NL_10 + _143;
  protected final String _437 = _251 + NL_10 + _188;
  protected final String _438 = _74 + NL_10 + _84;
  protected final String _439 = NL_10 + _91 + NL_10 + _193;
  protected final String _440 = NL_8 + _70;
  protected final String _441 = NL_8 + _174 + NL_10 + _143;
  protected final String _442 = _251 + NL_10 + _143;
  protected final String _443 = _251 + NL_10 + _229 + NL_8 + _70;
  protected final String _444 = NL_6 + _133;
  protected final String _445 = NL_12 + _95;
  protected final String _446 = _16 + NL_12 + _143;
  protected final String _447 = _251 + NL_12 + _186;
  protected final String _448 = NL_12 + _179;
  protected final String _449 = NL_8 + _130 + NL_10 + _223 + NL_10 + _110 + NL_8 + _63 + NL_8 + _120;
  protected final String _450 = NL + _50;
  protected final String _451 = NL_8 + _61;
  protected final String _452 = NL_10 + _235;
  protected final String _453 = NL_10 + _226 + NL_8 + _70;
  protected final String _454 = NL_8 + _130 + NL_8 + _109 + NL_8 + _142;
  protected final String _455 = _251 + NL_8 + _237;
  protected final String _456 = NL_8 + _192;
  protected final String _457 = NL_8 + _63 + NL_8 + _121;
  protected final String _458 = NL_8 + _130 + NL_8 + _106 + NL_8 + _142;
  protected final String _459 = _251 + NL_8 + _228 + NL_8 + _188;
  protected final String _460 = NL_8 + _105;
  protected final String _461 = NL_8 + _63 + NL_8 + _212;
  protected final String _462 = NL_10 + _160 + NL_12 + _95;
  protected final String _463 = _16 + NL_12 + _142;
  protected final String _464 = _74 + NL_12 + _23 + NL_12 + _188;
  protected final String _465 = _74 + NL_12 + _214;
  protected final String _466 = _254 + NL_14 + _159 + NL_15 + _94;
  protected final String _467 = _91 + NL_15 + _178;
  protected final String _468 = _71 + NL_14 + _67 + NL_14 + _159 + NL_15 + _96;
  protected final String _469 = _16 + NL_15 + _225 + NL_15 + _215;
  protected final String _470 = NL_16 + _154 + NL_18 + _84;
  protected final String _471 = _254 + NL_19 + _142;
  protected final String _472 = NL_19 + _190;
  protected final String _473 = _74 + NL_18 + _52 + NL_16 + _67;
  protected final String _474 = NL_15 + _78 + NL_14 + _67 + NL_12 + _78 + NL_10 + _67;
  protected final String _475 = NL_8 + _175 + NL_8 + _130 + NL_8 + _100 + NL_8 + _142;
  protected final String _476 = _251 + NL_8 + _233 + NL_8 + _188;
  protected final String _477 = NL_8 + _99;
  protected final String _478 = NL_8 + _63 + NL_8 + _208;
  protected final String _479 = NL_8 + _160;
  protected final String _480 = NL_10 + _126 + NL_12 + _93;
  protected final String _481 = _251 + NL_12 + _194;
  protected final String _482 = _20 + NL_10 + _61;
  protected final String _483 = NL_12 + _93;
  protected final String _484 = NL_12 + _14;
  protected final String _485 = NL_12 + _139;
  protected final String _486 = NL_12 + _187;
  protected final String _487 = NL_10 + _205;
  protected final String _488 = NL_12 + _160 + NL_14 + _84;
  protected final String _489 = _52 + NL_12 + _67;
  protected final String _490 = NL_10 + _78 + NL_8 + _67;
  protected final String _491 = NL + _49;
  protected final String _492 = NL_8 + _175 + NL_8 + _130 + NL_8 + _101 + NL_8 + _142;
  protected final String _493 = _251 + NL_8 + _232 + NL_8 + _188;
  protected final String _494 = NL_8 + _63 + NL_8 + _209;
  protected final String _495 = NL_10 + _160 + NL_12 + _93;
  protected final String _496 = NL_12 + _138;
  protected final String _497 = NL_12 + _183;
  protected final String _498 = _74 + NL_12 + _188;
  protected final String _499 = _74 + NL_12 + _206;
  protected final String _500 = NL_14 + _160 + NL_15 + _84;
  protected final String _501 = _254 + NL_17 + _138;
  protected final String _502 = NL_17 + _190;
  protected final String _503 = _74 + NL_15 + _52 + NL_14 + _67;
  protected final String _504 = NL_12 + _78 + NL_10 + _67;
  protected final String _505 = NL_8 + _175 + NL_8 + _130 + NL_8 + _102 + NL_8 + _142;
  protected final String _506 = _251 + NL_8 + _224 + NL_8 + _188;
  protected final String _507 = NL_8 + _63 + NL_8 + _210;
  protected final String _508 = NL_10 + _158 + NL_13 + _84;
  protected final String _509 = _254 + NL_14 + _142;
  protected final String _510 = NL_14 + _190;
  protected final String _511 = _74 + NL_12 + _52;
  protected final String _512 = NL_12 + _104;
  protected final String _513 = NL_10 + _67;
  protected final String _514 = NL_8 + _175 + NL_8 + _130 + NL_8 + _108 + NL_8 + _141;
  protected final String _515 = _251 + NL_8 + _231 + NL_8 + _188;
  protected final String _516 = NL_8 + _107;
  protected final String _517 = NL_8 + _63 + NL_8 + _213;
  protected final String _518 = NL_10 + _158;
  protected final String _519 = NL_12 + _84;
  protected final String _520 = NL_12 + _204;
  protected final String _521 = _254 + NL_17 + _142;
  protected final String _522 = NL_12 + _78;
  protected final String _523 = NL_8 + _175 + NL_8 + _130 + NL_8 + _98 + NL_8 + _142;
  protected final String _524 = _251 + NL_8 + _221 + NL_8 + _188;
  protected final String _525 = NL_8 + _97;
  protected final String _526 = NL_8 + _63 + NL_8 + _207;
  protected final String _527 = NL_12 + _52;
  protected final String _528 = NL_8 + _130 + NL_8 + _103 + NL_8 + _142;
  protected final String _529 = _251 + NL_8 + _227 + NL_8 + _188;
  protected final String _530 = NL_8 + _63 + NL_8 + _122 + NL_10 + _198;
  protected final String _531 = NL_10 + _199 + NL_12 + _150;
  protected final String _532 = NL_12 + _146;
  protected final String _533 = NL_12 + _152;
  protected final String _534 = NL_12 + _147;
  protected final String _535 = NL_12 + _148;
  protected final String _536 = NL_12 + _149;
  protected final String _537 = NL_12 + _151;
  protected final String _538 = NL_10 + _74;
  protected final String _539 = NL_10 + _216;
  protected final String _540 = NL_12 + _157;
  protected final String _541 = _9 + NL_14 + _84;
  protected final String _542 = _254 + NL_15 + _142;
  protected final String _543 = _251 + NL_15 + _197;
  protected final String _544 = _74 + NL_15 + _189;
  protected final String _545 = _74 + NL_14 + _52;
  protected final String _546 = NL_14 + _104;
  protected final String _547 = NL_14 + _18;
  protected final String _548 = NL_15 + _142;
  protected final String _549 = _251 + NL_15 + _188;
  protected final String _550 = _74 + NL_14 + _19;
  protected final String _551 = NL_14 + _18 + NL_15 + _142;
  protected final String _552 = _251 + NL_15 + _142;
  protected final String _553 = _251 + NL_14 + _19;
  protected final String _554 = NL_14 + _238;
  protected final String _555 = NL_12 + _67;
  protected final String _556 = NL_10 + _78 + NL_8 + _61 + NL_8 + _125 + NL_8 + _61;
  protected final String _557 = NL_5 + _61 + NL_4 + _61 + NL_3 + _61 + NL_3 + _68 + NL_1 + _59 + NL + _66;

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
    stringBuffer.append(_383);
    stringBuffer.append(reporter.getTitle());
    stringBuffer.append(_384);
    stringBuffer.append(_385);
    stringBuffer.append(_386);
    stringBuffer.append('\u25B7');
    stringBuffer.append(_387);
    stringBuffer.append(_388);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() == null) {
    stringBuffer.append(_389);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_67);
    } else {
    stringBuffer.append(_390);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_254);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_56);
    }
    }
    stringBuffer.append(_391);
    stringBuffer.append(_392);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() != null) {
    stringBuffer.append(_393);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_254);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_394);
    } else {
      if (iuReport == null) {
    stringBuffer.append(_395);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_394);
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
    stringBuffer.append(_396);
    if (index != -1) {
    stringBuffer.append(_397);
    }
    stringBuffer.append(_398);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_254);
    stringBuffer.append(NL_9);
    stringBuffer.append(label);
    stringBuffer.append(_399);
    }
    }
    stringBuffer.append(_400);
    if (iuReport == null) {
    stringBuffer.append(_401);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_62);
    } else {
    stringBuffer.append(_402);
    stringBuffer.append(report.getIUImage(iuReport.getIU()));
    stringBuffer.append(_251);
    String name = report.getName(iuReport.getIU(), false);
    if (name != null) {
    stringBuffer.append(NL_9);
    stringBuffer.append(helper.htmlEscape(name, true));
    stringBuffer.append(_91);
    } else {
    stringBuffer.append(_403);
    }
    stringBuffer.append(NL_9);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_404);
    }
    stringBuffer.append(_405);
    if (report != null && report.getDate() != null) {
    stringBuffer.append(_406);
    stringBuffer.append(report.getDate());
    stringBuffer.append(_58);
    }
    stringBuffer.append(_407);
    stringBuffer.append(reporter.getNow());
    stringBuffer.append(_408);
    if (report != null && !report.getSiteURL().startsWith("file:")) {
    stringBuffer.append(_409);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_410);
    }
    stringBuffer.append(_411);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_412);
    stringBuffer.append(reporter.getReportBrandingImage());
    stringBuffer.append(_240);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_413);
    if (indexReport != null) {
    stringBuffer.append(_414);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_251);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_415);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_250);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_55);
    Map<String, String> allReports = indexReport.getAllReports();
    if (allReports != null && !allReports.isEmpty()) {
    stringBuffer.append(_416);
    for (Map.Entry<String, String> entry : allReports.entrySet()) {
    stringBuffer.append(_417);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_254);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_56);
    }
    stringBuffer.append(_418);
    }
    } else if (iuReport != null) {
    stringBuffer.append(_419);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_420);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_251);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_421);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_250);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_422);
    String provider = iuReport.getProvider();
    if (provider != null) {
    stringBuffer.append(_423);
    stringBuffer.append(helper.htmlEscape(provider, false));
    stringBuffer.append(_70);
    }
    String description = iuReport.getDescription();
    if (description != null) {
    stringBuffer.append(_424);
    stringBuffer.append(helper.htmlEscape(description, false));
    stringBuffer.append(_70);
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iuReport.getIU());
    if (!artifacts.isEmpty()) {
      List<Certificate> certificates = report.getCertificates(iuReport.getIU());
      if (certificates != null) {
        Set<Certificate> expiredCertificates = new HashSet<Certificate>();
        for (Certificate certificate : certificates) {
          if (report.isExpired(certificate)) {
            expiredCertificates.add(certificate);
          }
        }
    stringBuffer.append(_425);
    if (!expiredCertificates.isEmpty()) {
    stringBuffer.append(_426);
    stringBuffer.append(expiredCertificates.size());
    stringBuffer.append(_2);
    if (expiredCertificates.size() > 1) {
    stringBuffer.append(_348);
    }
    stringBuffer.append(_21);
    }
    stringBuffer.append(_427);
    if (certificates.isEmpty()) {
    stringBuffer.append(_428);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_429);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_430);
    stringBuffer.append(count++);
    stringBuffer.append(_431);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_251);
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
    stringBuffer.append(_432);
    stringBuffer.append(spanStyle);
    stringBuffer.append(_220);
    stringBuffer.append(keyStyle);
    stringBuffer.append(_300);
    stringBuffer.append(key);
    stringBuffer.append(_218);
    stringBuffer.append(value);
    stringBuffer.append(_74);
    }
    stringBuffer.append(_433);
    }
    }
    }
    stringBuffer.append(_434);
    Collection<IInstallableUnit> pluginsWithMissingPackGZ = report.getPluginsWithMissingPackGZ();
      for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
        String artifact = entry.getKey();
        Boolean signed = entry.getValue();
    stringBuffer.append(_435);
    if (signed != null) {
    stringBuffer.append(_436);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_251);
    }
    stringBuffer.append(_436);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_437);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_438);
    stringBuffer.append(report.getRepositoryURL(artifact) + '/' + artifact);
    stringBuffer.append(_254);
    stringBuffer.append(artifact);
    stringBuffer.append(_52);
    for (String status : report.getArtifactStatus(artifact)) {
    stringBuffer.append(_439);
    stringBuffer.append(status);
    stringBuffer.append(_74);
    }
    stringBuffer.append(_440);
    }
    if (pluginsWithMissingPackGZ.contains(iuReport.getIU())) {
    stringBuffer.append(_441);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_442);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_443);
    }
    }
    List<Report.LicenseDetail> licenses = report.getLicenses(iuReport.getIU());
    if (licenses != null) {
    stringBuffer.append(_444);
    for (LicenseDetail license : licenses) {
    String id = license.getUUID();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
    stringBuffer.append(_445);
    stringBuffer.append(id);
    stringBuffer.append(_260);
    stringBuffer.append(id);
    stringBuffer.append(_446);
    stringBuffer.append(report.getLicenseImage());
    stringBuffer.append(_447);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_254);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_74);
    stringBuffer.append(NL_12);
    stringBuffer.append(id);
    stringBuffer.append(_448);
    stringBuffer.append(id);
    stringBuffer.append(_246);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_254);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_196);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_254);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_75);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_254);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_74);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_71);
    }
    }
    stringBuffer.append(_449);
    for (String xml : report.getXML(iuReport.getIU(), Collections.<String, String> emptyMap())) {
    stringBuffer.append(_450);
    stringBuffer.append(xml);
    }
    stringBuffer.append(_451);
    } else {
    stringBuffer.append(_419);
    stringBuffer.append(report.getHelpLink());
    stringBuffer.append(_420);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_251);
    stringBuffer.append(NL_12);
    stringBuffer.append(report.getHelpText());
    stringBuffer.append(_421);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_250);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_422);
    List<Report> children = report.getChildren();
    if (!children.isEmpty()) {
    stringBuffer.append(_435);
    if (!report.isRoot()) {
    stringBuffer.append(_452);
    }
    stringBuffer.append(_453);
    }
    String metadataXML = report.getMetadataXML();
    String artifactXML = report.getArtifactML();
    if (metadataXML != null || artifactXML != null) {
    stringBuffer.append(_454);
    stringBuffer.append(report.getRepositoryImage());
    stringBuffer.append(_455);
    if (metadataXML != null && metadataXML.contains("bad-absolute-location") || artifactXML != null && artifactXML.contains("bad-absolute-location")) {
    stringBuffer.append(_456);
    }
    stringBuffer.append(_457);
    if (metadataXML != null) {
    stringBuffer.append(_450);
    stringBuffer.append(metadataXML);
    }
    if (artifactXML != null) {
    if (metadataXML != null) {
    stringBuffer.append(NL);
    }
    stringBuffer.append(_450);
    stringBuffer.append(artifactXML);
    }
    stringBuffer.append(_451);
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
    stringBuffer.append(_458);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_459);
    stringBuffer.append(licenses.size());
    stringBuffer.append(_74);
    if (nonConformant != 0) {
    stringBuffer.append(_426);
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
    stringBuffer.append(_460);
    stringBuffer.append(displayButton);
    stringBuffer.append(_46);
    stringBuffer.append(onClick);
    stringBuffer.append(_255);
    }
    stringBuffer.append(_461);
    stringBuffer.append(display);
    stringBuffer.append(_42);
    for (Map.Entry<LicenseDetail, Set<IInstallableUnit>> entry : licenses.entrySet()) {
        LicenseDetail license = entry.getKey();
        String id = license.getUUID();
        Set<IInstallableUnit> ius = entry.getValue();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
        {
    stringBuffer.append(_462);
    stringBuffer.append(id);
    stringBuffer.append(_263);
    stringBuffer.append(id);
    stringBuffer.append(_463);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_447);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_254);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_464);
    stringBuffer.append(ius.size());
    stringBuffer.append(_465);
    stringBuffer.append(id);
    stringBuffer.append(_466);
    stringBuffer.append(id);
    stringBuffer.append(_262);
    stringBuffer.append(id);
    stringBuffer.append(_16);
    stringBuffer.append(NL_15);
    stringBuffer.append(id);
    stringBuffer.append(_467);
    stringBuffer.append(id);
    stringBuffer.append(_246);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_254);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_196);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_254);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_75);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_254);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_74);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_468);
    stringBuffer.append(id);
    stringBuffer.append(_264);
    stringBuffer.append(id);
    stringBuffer.append(_469);
    stringBuffer.append(id);
    stringBuffer.append(_254);
    for (IInstallableUnit iu : ius) {
    stringBuffer.append(_470);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_471);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_251);
    stringBuffer.append(NL_19);
    stringBuffer.append(helper.htmlEscape(report.getName(iu, true), true));
    stringBuffer.append(_472);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_473);
    }
    stringBuffer.append(_474);
    }
    }
    stringBuffer.append(_418);
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
    stringBuffer.append(_475);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_476);
    stringBuffer.append(allCertificates.size());
    stringBuffer.append(_74);
    if (unsigned != null) {
    stringBuffer.append(_426);
    stringBuffer.append(unsigned.size());
    stringBuffer.append(_8);
    }
    if (expiredChainCount > 0) {
    stringBuffer.append(_426);
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
    stringBuffer.append(_477);
    stringBuffer.append(displayButton);
    stringBuffer.append(_45);
    stringBuffer.append(onClick);
    stringBuffer.append(_255);
    }
    stringBuffer.append(_478);
    stringBuffer.append(display);
    stringBuffer.append(_43);
    for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : allCertificates.entrySet()) {
        List<Certificate> certificates = entry.getKey();
        String id = "certificates" + ++idCount;
    stringBuffer.append(_479);
    if (certificates.isEmpty()) {
    stringBuffer.append(_480);
    stringBuffer.append(id);
    stringBuffer.append(_261);
    stringBuffer.append(id);
    stringBuffer.append(_463);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_481);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_482);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_430);
    stringBuffer.append(count++ + 2);
    stringBuffer.append(_294);
    if (count == 1) {
    stringBuffer.append(_483);
    stringBuffer.append(id);
    stringBuffer.append(_261);
    stringBuffer.append(id);
    stringBuffer.append(_16);
    } else {
    stringBuffer.append(_484);
    }
    stringBuffer.append(_485);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_251);
    if (count == 1) {
    stringBuffer.append(_486);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_21);
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
    stringBuffer.append(_432);
    stringBuffer.append(spanStyle);
    stringBuffer.append(_220);
    stringBuffer.append(keyStyle);
    stringBuffer.append(_300);
    stringBuffer.append(key);
    stringBuffer.append(_218);
    stringBuffer.append(value);
    stringBuffer.append(_74);
    }
    stringBuffer.append(_433);
    }
    }
    stringBuffer.append(_487);
    stringBuffer.append(id);
    stringBuffer.append(_254);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_488);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_256);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_252);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_489);
    }
    stringBuffer.append(_490);
    }
    stringBuffer.append(_418);
    }
    stringBuffer.append(_491);
    Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
    if (!featureProviders.isEmpty()) {
      int nonEclipse = 0;
      for (String provider : featureProviders.keySet()) {
        if (!provider.toLowerCase().contains("eclipse"))
          ++nonEclipse;
      }
    stringBuffer.append(_492);
    stringBuffer.append(report.getProviderImage());
    stringBuffer.append(_493);
    stringBuffer.append(featureProviders.size());
    stringBuffer.append(_74);
    if (nonEclipse != 0) {
    stringBuffer.append(_426);
    stringBuffer.append(nonEclipse);
    stringBuffer.append(_7);
    }
    stringBuffer.append(_494);
    int count = 0;
      for (Map.Entry<String, Set<IInstallableUnit>> provider : featureProviders.entrySet()) {
        String style = !provider.getKey().toLowerCase().contains("eclipse") ? " style=\"color: FireBrick;\"" : "";
        Set<IInstallableUnit> features = provider.getValue();
        String id = "nested_feature_providers" + ++count;
    stringBuffer.append(_495);
    stringBuffer.append(id);
    stringBuffer.append(_259);
    stringBuffer.append(id);
    stringBuffer.append(_16);
    for (String image : report.getBrandingImages(features)) {
    stringBuffer.append(_496);
    stringBuffer.append(image);
    stringBuffer.append(_251);
    }
    stringBuffer.append(_497);
    stringBuffer.append(style);
    stringBuffer.append(_219);
    stringBuffer.append(provider.getKey());
    stringBuffer.append(_498);
    stringBuffer.append(features.size());
    stringBuffer.append(_499);
    stringBuffer.append(id);
    stringBuffer.append(_245);
    for (IInstallableUnit feature : features) {
          String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_500);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_501);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_251);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_502);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_503);
    }
    stringBuffer.append(_504);
    }
    stringBuffer.append(_418);
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
    stringBuffer.append(_505);
    stringBuffer.append(report.getFeatureImage());
    stringBuffer.append(_506);
    stringBuffer.append(features.size());
    stringBuffer.append(_74);
    if (brokenBranding != 0) {
    stringBuffer.append(_426);
    stringBuffer.append(brokenBranding);
    stringBuffer.append(_1);
    }
    if (noBranding != 0) {
    stringBuffer.append(_426);
    stringBuffer.append(noBranding);
    stringBuffer.append(_6);
    }
    stringBuffer.append(_507);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_508);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_509);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_251);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_510);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_511);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        for (LicenseDetail license : report.getLicenses(feature)) {
          String id = license.getUUID();
          licenseReplacements.put(">" + id, "><button class='bb search-for-me' style='" + helper.getStyle(license) + "' onclick=\"clickOnButton('lic_" + id
              + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_512);
    stringBuffer.append(id);
    stringBuffer.append(_241);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_243);
    stringBuffer.append(id);
    stringBuffer.append(_257);
    stringBuffer.append(id);
    stringBuffer.append(_258);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_60);
    }
    stringBuffer.append(_513);
    }
    stringBuffer.append(_418);
    }
    Collection<IInstallableUnit> products = report.getProducts();
    if (!products.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_514);
    stringBuffer.append(report.getProductImage());
    stringBuffer.append(_515);
    stringBuffer.append(products.size());
    stringBuffer.append(_74);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit product : products) {
          String productID = "_product_" + report.getIUID(product);
          onClick.append("expand2('products_all_arrows', '").append(productID).append("');");
        }
    stringBuffer.append(_516);
    stringBuffer.append(displayButton);
    stringBuffer.append(_47);
    stringBuffer.append(onClick);
    stringBuffer.append(_255);
    }
    stringBuffer.append(_517);
    stringBuffer.append(display);
    stringBuffer.append(_42);
    for (IInstallableUnit product : products) {
        String productImage = report.getIUImage(product);
        String productID = "_product_" + report.getIUID(product);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(product));
    stringBuffer.append(_518);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_483);
    stringBuffer.append(productID);
    stringBuffer.append(_261);
    stringBuffer.append(productID);
    stringBuffer.append(_16);
    }
    stringBuffer.append(_519);
    stringBuffer.append(report.getRelativeIUReportURL(product));
    stringBuffer.append(_509);
    stringBuffer.append(productImage);
    stringBuffer.append(_251);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(product, true), true));
    stringBuffer.append(_510);
    stringBuffer.append(report.getVersion(product));
    stringBuffer.append(_511);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_520);
    stringBuffer.append(productID);
    stringBuffer.append(_244);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_500);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_521);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_251);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_502);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_503);
    }
    stringBuffer.append(_522);
    }
    stringBuffer.append(_513);
    }
    stringBuffer.append(_418);
    }
    Collection<IInstallableUnit> categories = report.getCategories();
    if (!categories.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_523);
    stringBuffer.append(report.getCategoryImage());
    stringBuffer.append(_524);
    stringBuffer.append(categories.size());
    stringBuffer.append(_74);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit category : categories) {
          String categoryID = "_category_" + report.getIUID(category);
          onClick.append("expand2('categories_all_arrows', '").append(categoryID).append("');");
        }
    stringBuffer.append(_525);
    stringBuffer.append(displayButton);
    stringBuffer.append(_44);
    stringBuffer.append(onClick);
    stringBuffer.append(_255);
    }
    stringBuffer.append(_526);
    stringBuffer.append(display);
    stringBuffer.append(_42);
    for (IInstallableUnit category : categories) {
        String categoryImage = report.getIUImage(category);
        String categoryID = "_category_" + report.getIUID(category);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(category));
    stringBuffer.append(_518);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_483);
    stringBuffer.append(categoryID);
    stringBuffer.append(_261);
    stringBuffer.append(categoryID);
    stringBuffer.append(_16);
    }
    stringBuffer.append(_519);
    stringBuffer.append(report.getRelativeIUReportURL(category));
    stringBuffer.append(_509);
    stringBuffer.append(categoryImage);
    stringBuffer.append(_251);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(category, true), true));
    stringBuffer.append(_527);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_520);
    stringBuffer.append(categoryID);
    stringBuffer.append(_247);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_500);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_521);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_251);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_502);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_503);
    }
    stringBuffer.append(_522);
    }
    stringBuffer.append(_513);
    }
    stringBuffer.append(_418);
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
    stringBuffer.append(_528);
    stringBuffer.append(report.getBundleImage());
    stringBuffer.append(_529);
    stringBuffer.append(ius.size());
    stringBuffer.append(_74);
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_426);
    stringBuffer.append(pluginsWithMissingPackGZ.size());
    stringBuffer.append(_5);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_426);
    stringBuffer.append(duplicateCount);
    stringBuffer.append(_10);
    }
    stringBuffer.append(_530);
    if (duplicateCount > 0 || !pluginsWithMissingPackGZ.isEmpty() || !unsignedIUs.isEmpty() || !badProviderIUs.isEmpty() || !badLicenseIUs.isEmpty()
          || !brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_531);
    if (!badLicenseIUs.isEmpty()) {
    stringBuffer.append(_532);
    }
    if (!unsignedIUs.isEmpty()) {
    stringBuffer.append(_533);
    }
    if (!badProviderIUs.isEmpty()) {
    stringBuffer.append(_534);
    }
    if (!brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_535);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_536);
    }
    if (!pluginsWithMissingPackGZ.isEmpty()) {
    stringBuffer.append(_537);
    }
    stringBuffer.append(_538);
    }
    stringBuffer.append(_539);
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
    stringBuffer.append(_540);
    stringBuffer.append(id);
    stringBuffer.append(_242);
    stringBuffer.append(classNames);
    stringBuffer.append(_541);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_542);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_543);
    stringBuffer.append(iuID);
    stringBuffer.append(_544);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_254);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_545);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        if (report.isFeature(iu)) {
          for (LicenseDetail license : report.getLicenses(iu)) {
            String licenseID = license.getUUID();
            licenseReplacements.put(">" + licenseID, "><button class='bb search-for-me' style='" + helper.getStyle(license)
                + "' onclick=\"clickOnButton('lic_" + licenseID + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_546);
    stringBuffer.append(licenseID);
    stringBuffer.append(_241);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_243);
    stringBuffer.append(licenseID);
    stringBuffer.append(_257);
    stringBuffer.append(licenseID);
    stringBuffer.append(_258);
    stringBuffer.append(licenseID);
    stringBuffer.append(_15);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_60);
    }
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iu);
        for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
          String artifact = entry.getKey();
          Boolean signed = entry.getValue();
    stringBuffer.append(_547);
    if (signed != null) {
    stringBuffer.append(_548);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_251);
    }
    stringBuffer.append(_548);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_549);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_550);
    }
    if (pluginsWithMissingPackGZ.contains(iu)) {
    stringBuffer.append(_551);
    stringBuffer.append(report.getErrorImage());
    stringBuffer.append(_552);
    stringBuffer.append(report.getArtifactImage("*.jar.pack.gz"));
    stringBuffer.append(_553);
    }
    String brandingImage = report.getBrandingImage(iu);
        if (brandingImage != null) {
    stringBuffer.append(_554);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_253);
    }
    stringBuffer.append(_555);
    }
    stringBuffer.append(_556);
    }
    }
    stringBuffer.append(_557);
    return stringBuffer.toString();
  }
}
