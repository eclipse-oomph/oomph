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
  protected static final String _46 = ";\" onclick=\"toggle('products_all_arrows');";
  protected static final String _47 = "<!-- navigation sidebar -->";
  protected static final String _48 = "<!--- providers -->";
  protected static final String _49 = "<!----------->";
  protected static final String _50 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
  protected static final String _51 = "</a>";
  protected static final String _52 = "</a> report.";
  protected static final String _53 = "</a>.";
  protected static final String _54 = "</a>.</p>";
  protected static final String _55 = "</a></li>";
  protected static final String _56 = "</aside>";
  protected static final String _57 = "</b>";
  protected static final String _58 = "</body>";
  protected static final String _59 = "</button>";
  protected static final String _60 = "</div>";
  protected static final String _61 = "</h2>";
  protected static final String _62 = "</h3>";
  protected static final String _63 = "</head>";
  protected static final String _64 = "</header>";
  protected static final String _65 = "</html>";
  protected static final String _66 = "</li>";
  protected static final String _67 = "</main>";
  protected static final String _68 = "</ol>";
  protected static final String _69 = "</p>";
  protected static final String _70 = "</pre>";
  protected static final String _71 = "</script>";
  protected static final String _72 = "</section>";
  protected static final String _73 = "</span>";
  protected static final String _74 = "</span><span style=\"color: green; text-decoration: underline; ";
  protected static final String _75 = "</style>";
  protected static final String _76 = "</title>";
  protected static final String _77 = "</ul>";
  protected static final String _78 = "<a class=\"separator\" href=\"";
  protected static final String _79 = "<a class=\"separator\" href=\".\">";
  protected static final String _80 = "<a class=\"separator\" href=\"https://www.eclipse.org/downloads\">Downloads</a>";
  protected static final String _81 = "<a class=\"separator\" href=\"https://www.eclipse.org\">Home</a>";
  protected static final String _82 = "<a class=\"separator\" style=\"display: inline-block; margin-left: 0.25em;\" href=\"";
  protected static final String _83 = "<a href=\"";
  protected static final String _84 = "<a href=\"https://wiki.eclipse.org/Oomph_Repository_Analyzer#Description\" target=\"oomph_wiki\">";
  protected static final String _85 = "<a href=\"https://www.eclipse.org/\">";
  protected static final String _86 = "<a style=\"float:right; font-size: 200%;\" href=\"";
  protected static final String _87 = "<aside id=\"leftcol\" class=\"col-md-4 font-smaller\">";
  protected static final String _88 = "<b>Built: ";
  protected static final String _89 = "<body id=\"body_solstice\">";
  protected static final String _90 = "<br/>";
  protected static final String _91 = "<br/><b>Reported: ";
  protected static final String _92 = "<button id=\"";
  protected static final String _93 = "<button id=\"_";
  protected static final String _94 = "<button id=\"__";
  protected static final String _95 = "<button id=\"_f";
  protected static final String _96 = "<button id=\"categories_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _97 = "<button id=\"categories_arrows\" class=\"orange bb\" onclick=\"expand_collapse('categories'); expand_collapse_inline('categories_all_arrows');\">&#x25B7;</button>";
  protected static final String _98 = "<button id=\"certificates_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _99 = "<button id=\"certificates_arrows\" class=\"orange bb\" onclick=\"expand_collapse('certificates'); expand_collapse_inline('certificates_all_arrows');\">&#x25B7;</button>";
  protected static final String _100 = "<button id=\"feature_providers_arrows\" class=\"orange bb\" onclick=\"expand_collapse('feature_providers');\">&#x25B7;</button>";
  protected static final String _101 = "<button id=\"features_arrows\" class=\"orange bb\" onclick=\"expand_collapse('features');\">&#x25B7;</button>";
  protected static final String _102 = "<button id=\"ius_arrows\" class=\"orange bb\" onclick=\"expand_collapse('ius'); match('subset');\">&#x25B7;</button>";
  protected static final String _103 = "<button id=\"lic_";
  protected static final String _104 = "<button id=\"licenses_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _105 = "<button id=\"licenses_arrows\" class=\"orange bb\" onclick=\"expand_collapse('licenses'); expand_collapse_inline('licenses_all_arrows');\">&#x25B7;</button>";
  protected static final String _106 = "<button id=\"products_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _107 = "<button id=\"products_arrows\" class=\"orange bb\" onclick=\"expand_collapse('products'); expand_collapse_inline('products_all_arrows');\">&#x25B7;</button>";
  protected static final String _108 = "<button id=\"repos_arrows\" class=\"orange bb\" onclick=\"expand_collapse('repos');\">&#x25B7;</button>";
  protected static final String _109 = "<button id=\"xml-links\" class=\"orange bb\" onclick=\"expand_collapse_all('xml-links');\">&#x25B7;</button>";
  protected static final String _110 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 150%;\" onclick=\"copyToClipboard('#p1')\">&#x270e;</button>";
  protected static final String _111 = "<div class=\"col-sm-16 padding-left-30\">";
  protected static final String _112 = "<div class=\"container\">";
  protected static final String _113 = "<div class=\"font-smaller\" style=\"margin-left: ";
  protected static final String _114 = "<div class=\"hidden-xs col-sm-8 col-md-6 col-lg-5\" id=\"header-left\">";
  protected static final String _115 = "<div class=\"novaContent container\" id=\"novaContent\">";
  protected static final String _116 = "<div class=\"row\" id=\"header-row\">";
  protected static final String _117 = "<div class=\"row\">";
  protected static final String _118 = "<div class=\"wrapper-logo-default\">";
  protected static final String _119 = "<div class=\"xml-iu\" style=\"display:block;\">";
  protected static final String _120 = "<div class=\"xml-repo\" id=\"repos\" style=\"display: none;\">";
  protected static final String _121 = "<div id=\"ius\" style=\"display:none;\">";
  protected static final String _122 = "<div id=\"maincontent\">";
  protected static final String _123 = "<div id=\"midcolumn\" style=\"width: 70%;\">";
  protected static final String _124 = "<div style=\"height: 40ex;\">";
  protected static final String _125 = "<div style=\"text-indent: -2em\">";
  protected static final String _126 = "<div>";
  protected static final String _127 = "<h2 style=\"text-align: center;\">";
  protected static final String _128 = "<h3 class=\"sr-only\">Breadcrumbs</h3>";
  protected static final String _129 = "<h3 style=\"font-weight: bold;\">";
  protected static final String _130 = "<h3 style=\"font-weight: bold;\">Artifacts</h3>";
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
  protected static final String _150 = "<input id=\"unsigned\" type=\"radio\" name=\"filter\" value=\"unsigned\" class=\"filter\" onclick=\"filterIU('unsigned');\"> Unsigned </input>";
  protected static final String _151 = "<li class=\"active\">";
  protected static final String _152 = "<li class=\"font-smaller text-nowrap\">";
  protected static final String _153 = "<li class=\"separator\" style=\"font-size: 90%;\">";
  protected static final String _154 = "<li class=\"separator\">";
  protected static final String _155 = "<li id=\"_iu_";
  protected static final String _156 = "<li style=\"font-size: 100%; white-space: nowrap;\">";
  protected static final String _157 = "<li style=\"margin-left: 1em;\">";
  protected static final String _158 = "<li>";
  protected static final String _159 = "<li><a href=\"";
  protected static final String _160 = "<li><a href=\"https://www.eclipse.org/\">Home</a></li>";
  protected static final String _161 = "<li><a href=\"https://www.eclipse.org/downloads/\">Downloads</a></li>";
  protected static final String _162 = "<link href=\"//fonts.googleapis.com/css?family=Open+Sans:400,700,300,600,100\" rel=\"stylesheet\" type=\"text/css\"/>";
  protected static final String _163 = "<link rel=\"icon\" type=\"image/ico\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/favicon.ico\"/>";
  protected static final String _164 = "<link rel=\"stylesheet\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/stylesheets/styles.min.css\"/>";
  protected static final String _165 = "<main class=\"no-promo\">";
  protected static final String _166 = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
  protected static final String _167 = "<meta name=\"description\" content=\"Update Sites Reports\"/>";
  protected static final String _168 = "<meta name=\"keywords\" content=\"eclipse,update site\"/>";
  protected static final String _169 = "<ol class=\"breadcrumb\">";
  protected static final String _170 = "<p style=\"font-size: 125%; text-align: center;\">";
  protected static final String _171 = "<p style=\"text-align: center;\">";
  protected static final String _172 = "<p>";
  protected static final String _173 = "<p></p>";
  protected static final String _174 = "<p>Reports are generated specifically for the following sites:</p>";
  protected static final String _175 = "<p>This report is produced by <a href=\"";
  protected static final String _176 = "<pre id=\"_";
  protected static final String _177 = "<pre id=\"__";
  protected static final String _178 = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";
  protected static final String _179 = "<script>";
  protected static final String _180 = "<section class=\"hidden-print default-breadcrumbs\" id=\"breadcrumb\">";
  protected static final String _181 = "<span ";
  protected static final String _182 = "<span class=\"text-nowrap\"";
  protected static final String _183 = "<span id=\"p1\" style=\"font-size: 125%\">";
  protected static final String _184 = "<span style=\"";
  protected static final String _185 = "<span style=\"color: DarkCyan; font-size: 110%;\">(";
  protected static final String _186 = "<span style=\"color: DarkCyan;\">";
  protected static final String _187 = "<span style=\"color: DarkOliveGreen;";
  protected static final String _188 = "<span style=\"color: DarkOliveGreen;\">";
  protected static final String _189 = "<span style=\"color: FireBrick; font-size: 60%;\">(";
  protected static final String _190 = "<span style=\"color: FireBrick; font-size: 60%;\">(Inappropriate absolute location)</span>";
  protected static final String _191 = "<span style=\"color: FireBrick; font-size: 90%;\">";
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
  protected static final String _218 = "><span style=\"";
  protected static final String _219 = "Categories";
  protected static final String _220 = "Certificate";
  protected static final String _221 = "Content Metadata";
  protected static final String _222 = "Features";
  protected static final String _223 = "Features/Products";
  protected static final String _224 = "In addition to this composite report, reports are also generated for each of the composed children listed in the navigation bar to the left.";
  protected static final String _225 = "Installable Units";
  protected static final String _226 = "Licenses";
  protected static final String _227 = "No Name<br/>";
  protected static final String _228 = "Products";
  protected static final String _229 = "Providers";
  protected static final String _230 = "Signing Certificates";
  protected static final String _231 = "This";
  protected static final String _232 = "This is a composite update site.";
  protected static final String _233 = "This is a generated";
  protected static final String _234 = "XML";
  protected static final String _235 = "[<img class=\"fit-image\" src=\"";
  protected static final String _236 = "\" alt=\"\"/>";
  protected static final String _237 = "\" alt=\"\"/><img style=\"margin-top: -2ex;\" class=\"fit-image\" src=\"";
  protected static final String _238 = "\" class=\"bb\" style=\"";
  protected static final String _239 = "\" class=\"iu-li";
  protected static final String _240 = "\" onclick=\"clickOnToggleButton('licenses_arrows'); clickOnToggleButton('__";
  protected static final String _241 = "\" style=\"display: none;\">";
  protected static final String _242 = "\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _243 = "\" style=\"display:none; margin-left: 2em; background-color: ";
  protected static final String _244 = "\" style=\"list-style-type: none; display: none; margin-left: -1em;\">";
  protected static final String _245 = "\" target=\"oomph_wiki\"/>";
  protected static final String _246 = "\" target=\"oomph_wiki\">";
  protected static final String _247 = "\" target=\"report_source\">";
  protected static final String _248 = "\"/>";
  protected static final String _249 = "\"/> ";
  protected static final String _250 = "\"/>]";
  protected static final String _251 = "\">";
  protected static final String _252 = "\">&#x25B7;</button>";
  protected static final String _253 = "\"><img class=\"fit-image\" src=\"";
  protected static final String _254 = "_arrows'); clickOnToggleButton('_";
  protected static final String _255 = "_arrows'); navigateTo('_";
  protected static final String _256 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('";
  protected static final String _257 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('__";
  protected static final String _258 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('";
  protected static final String _259 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_";
  protected static final String _260 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('__";
  protected static final String _261 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_f";
  protected static final String _262 = "background-color: white;";
  protected static final String _263 = "border: 1px solid black;";
  protected static final String _264 = "border: none;";
  protected static final String _265 = "break;";
  protected static final String _266 = "buttonTargets.item(i).innerHTML = '&#x25B7;';";
  protected static final String _267 = "buttonTargets.item(i).innerHTML = '&#x25E2;';";
  protected static final String _268 = "catch (err) {";
  protected static final String _269 = "color: DarkSlateGray;";
  protected static final String _270 = "color: FireBrick;";
  protected static final String _271 = "color: IndianRed;";
  protected static final String _272 = "color: MediumAquaMarine;";
  protected static final String _273 = "color: MediumOrchid;";
  protected static final String _274 = "color: SaddleBrown;";
  protected static final String _275 = "color: SeaGreen;";
  protected static final String _276 = "color: SteelBlue;";
  protected static final String _277 = "color: Teal;";
  protected static final String _278 = "continue;";
  protected static final String _279 = "currentFilter = filter.value;";
  protected static final String _280 = "document.execCommand(\"copy\");";
  protected static final String _281 = "e.click();";
  protected static final String _282 = "e.innerHTML = '&#x25B7;';";
  protected static final String _283 = "e.innerHTML = '&#x25E2;';";
  protected static final String _284 = "e.scrollIntoView();";
  protected static final String _285 = "e.style.display = 'block';";
  protected static final String _286 = "e.style.display = 'inline';";
  protected static final String _287 = "e.style.display = 'inline-block';";
  protected static final String _288 = "e.style.display = 'none';";
  protected static final String _289 = "e.title= 'Collapse All';";
  protected static final String _290 = "e.title= 'Expand All';";
  protected static final String _291 = "em; text-indent: -4em;\">";
  protected static final String _292 = "em;\">";
  protected static final String _293 = "f.innerHTML = '&#x25B7;';";
  protected static final String _294 = "f.innerHTML = '&#x25E2;';";
  protected static final String _295 = "font-family: monospace;";
  protected static final String _296 = "font-size: 125%;";
  protected static final String _297 = "font-size: 75%;\">";
  protected static final String _298 = "font-size: 80%;";
  protected static final String _299 = "font-size: 90%;";
  protected static final String _300 = "for (var i = 0; i < buttonTargets.length; i++) {";
  protected static final String _301 = "for (var i = 0; i < ius.length; i++) {";
  protected static final String _302 = "for (var i = 0; i < spanTargets.length; i++) {";
  protected static final String _303 = "function clickOnButton(id) {";
  protected static final String _304 = "function clickOnToggleButton(id) {";
  protected static final String _305 = "function copyToClipboard(element) {";
  protected static final String _306 = "function expand(id) {";
  protected static final String _307 = "function expand2(self, id) {";
  protected static final String _308 = "function expand3(self, id) {";
  protected static final String _309 = "function expand_collapse(id) {";
  protected static final String _310 = "function expand_collapse_all(base) {";
  protected static final String _311 = "function expand_collapse_inline(id) {";
  protected static final String _312 = "function expand_collapse_inline_block(id) {";
  protected static final String _313 = "function filterIU(className) {";
  protected static final String _314 = "function match(id) {";
  protected static final String _315 = "function navigateTo(id) {";
  protected static final String _316 = "function toggle(id) {";
  protected static final String _317 = "height: 2ex;";
  protected static final String _318 = "if (!targetsArray.includes(iu)) {";
  protected static final String _319 = "if ((matchText == '' || text.match(matchText) != null) && targetsArray.includes(iu)) {";
  protected static final String _320 = "if (count == 0 && message.innerHTML == '') {";
  protected static final String _321 = "if (count == 0) {";
  protected static final String _322 = "if (e.innerHTML == '";
  protected static final String _323 = "if (e.innerHTML == '\\u25E2') {";
  protected static final String _324 = "if (e.style.display == 'none'){";
  protected static final String _325 = "if (e.title == 'Expand All') {";
  protected static final String _326 = "if (f != null) {";
  protected static final String _327 = "if (f !=null) {";
  protected static final String _328 = "if (filter != null && filter.value != 'all') {";
  protected static final String _329 = "if (matchText != '' && iu.textContent.match(matchText) == null) {";
  protected static final String _330 = "if (t.title != 'Collapse All'){";
  protected static final String _331 = "if (t.title == 'Collapse All'){";
  protected static final String _332 = "iu.style.display = 'block';";
  protected static final String _333 = "iu.style.display = 'none';";
  protected static final String _334 = "margin-bottom: -2ex;";
  protected static final String _335 = "margin-left: 0em;";
  protected static final String _336 = "margin-top: -2ex;";
  protected static final String _337 = "margin: 0px 0px 0px 0px;";
  protected static final String _338 = "message.innerHTML = '';";
  protected static final String _339 = "message.innerHTML = 'No matches';";
  protected static final String _340 = "message.innerHTML = \"\";";
  protected static final String _341 = "message.innerHTML = errMessage;";
  protected static final String _342 = "padding: -2pt -2pt -2pt -2pt;";
  protected static final String _343 = "padding: 0px 0px;";
  protected static final String _344 = "report is produced by <a href=\"";
  protected static final String _345 = "s";
  protected static final String _346 = "span:target {";
  protected static final String _347 = "spanTargets.item(i).style.display = 'inline-block';";
  protected static final String _348 = "spanTargets.item(i).style.display = 'none';";
  protected static final String _349 = "try";
  protected static final String _350 = "try {";
  protected static final String _351 = "var $temp = $(\"<input>\");";
  protected static final String _352 = "var buttonTargets = document.getElementsByClassName(base + '_button');";
  protected static final String _353 = "var count = 0;";
  protected static final String _354 = "var currentFilter = 'iu-li';";
  protected static final String _355 = "var e = document.getElementById('subset');";
  protected static final String _356 = "var e = document.getElementById(base);";
  protected static final String _357 = "var e = document.getElementById(id);";
  protected static final String _358 = "var errMessage = err.message;";
  protected static final String _359 = "var f = document.getElementById(id+\"_arrows\");";
  protected static final String _360 = "var filter = document.querySelector('input[name=\"filter\"]:checked');";
  protected static final String _361 = "var iu = ius[i];";
  protected static final String _362 = "var ius = document.getElementsByClassName('iu-li');";
  protected static final String _363 = "var matchText = e.value;";
  protected static final String _364 = "var message = document.getElementById('subset-error');";
  protected static final String _365 = "var message = document.getElementById(id + '-error');";
  protected static final String _366 = "var spanTargets = document.getElementsByClassName(base);";
  protected static final String _367 = "var state = e.innerHTML;";
  protected static final String _368 = "var t = document.getElementById('all');";
  protected static final String _369 = "var t = document.getElementById(self);";
  protected static final String _370 = "var targets = document.getElementsByClassName(className);";
  protected static final String _371 = "var targets = document.getElementsByClassName(currentFilter);";
  protected static final String _372 = "var targetsArray = [].slice.call(targets);";
  protected static final String _373 = "var text = iu.textContent;";
  protected static final String _374 = "white-space: nowrap;";
  protected static final String _375 = "white-space: pre;";
  protected static final String _376 = "width: 2ex;";
  protected static final String _377 = "{";
  protected static final String _378 = "}";
  protected static final String _379 = "} else {";
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
  protected final String _380 = _50 + NL + _136 + NL + _134 + NL_1 + _166 + NL_1 + _199;
  protected final String _381 = _76 + NL_1 + _168 + NL_1 + _167 + NL_1 + _162 + NL_1 + _164 + NL_1 + _163 + NL_1 + _178;
  protected final String _382 = NL_1 + _198 + NL + NL + _30 + NL_1 + _374 + NL + _378 + NL + NL + _27 + NL_1 + _299 + NL + _378 + NL + NL + _26 + NL_1 + _376 + NL_1 + _317 + NL + _378 + NL + NL + _346 + NL_1 + _296 + NL_1 + _263 + NL + _378 + NL + NL + _39 + NL_1 + _375 + NL_1 + _264 + NL_1 + _343 + NL_1 + _336 + NL_1 + _334 + NL_1 + _335 + NL + _378 + NL + NL + _23 + NL_1 + _270 + NL_1 + _299 + NL + _378 + NL + NL + _38 + NL_1 + _375 + NL_1 + _264 + NL_1 + _343 + NL_1 + _336 + NL_1 + _334 + NL_1 + _335 + NL + _378 + NL + NL + _40 + NL_1 + _276 + NL_1 + _295 + NL_1 + _298 + NL + _378 + NL + NL + _34 + NL_1 + _272 + NL_1 + _295 + NL_1 + _298 + NL + _378 + NL + NL + _36 + NL_1 + _273 + NL_1 + _295 + NL_1 + _298 + NL + _378 + NL + NL + _35 + NL_1 + _269 + NL_1 + _299 + NL + _378 + NL + NL + _37 + NL_1 + _274 + NL_1 + _299 + NL + _378 + NL + NL + _24 + NL_1 + _262 + NL_1 + _264 + NL_1 + _343 + NL + _378 + NL + NL + _25 + NL_1 + _262 + NL + _378 + NL + NL + _28 + NL_1 + _342 + NL_1 + _337 + NL + _378 + NL + NL + _29 + NL_1 + _275 + NL_1 + _299 + NL + _378 + NL + NL + _33 + NL_1 + _277 + NL_1 + _299 + NL + _378 + NL + NL + _32 + NL_1 + _299 + NL + _378 + NL + NL + _31 + NL_1 + _271 + NL_1 + _299 + NL + _378 + NL_1 + _75 + NL + _63 + NL + NL_1 + _89;
  protected final String _383 = NL_2 + _179 + NL + NL_4 + _314 + NL_5 + _360 + NL_5 + _354 + NL_5 + _328 + NL_7 + _279 + NL_5 + _378 + NL_5 + _357 + NL_5 + _365 + NL_5 + _363 + NL_5 + _362 + NL_5 + _371 + NL_5 + _372 + NL_5 + _353 + NL_5 + _301 + NL_7 + _361 + NL_7 + _373 + NL_7 + _350 + NL_9 + _319 + NL_11 + _332 + NL_11 + _21 + NL_9 + _379 + NL_11 + _333 + NL_9 + _378 + NL_9 + _340 + NL_7 + _378 + NL_7 + _268 + NL_9 + _358 + NL_9 + _341 + NL_9 + _265 + NL_7 + _378 + NL_5 + _378 + NL_5 + _320 + NL_9 + _339 + NL_5 + _378 + NL_4 + _378 + NL + NL_4 + _313 + NL_5 + _355 + NL_5 + _363 + NL_5 + _353 + NL_5 + _362 + NL_5 + _370 + NL_5 + _372 + NL_5 + _301 + NL_7 + _361 + NL_7 + _318 + NL_9 + _333 + NL_7 + _379 + NL_9 + _349 + NL_9 + _377 + NL_11 + _329 + NL_13 + _333 + NL_13 + _278 + NL_11 + _378 + NL_9 + _378 + NL_9 + _268 + NL_9 + _378 + NL_9 + _332 + NL_9 + _21 + NL_7 + _378 + NL_5 + _378 + NL_5 + _364 + NL_5 + _321 + NL_9 + _339 + NL_5 + _379 + NL_9 + _338 + NL_5 + _378 + NL_4 + _378 + NL + NL_4 + _305 + NL_5 + _351 + NL_5 + _10 + NL_5 + _12 + NL_5 + _280 + NL_5 + _11 + NL_4 + _378 + NL + NL_4 + _303 + NL_5 + _357 + NL_5 + _281 + NL_4 + _378 + NL + NL_4 + _304 + NL_5 + _357 + NL_5 + _367 + NL_5 + _322;
  protected final String _384 = _16 + NL_7 + _281 + NL_5 + _378 + NL_4 + _378 + NL + NL_4 + _315 + NL_5 + _357 + NL_5 + _284 + NL_4 + _378 + NL + NL_4 + _316 + NL_5 + _357 + NL_5 + _325 + NL_7 + _289 + NL_7 + _283 + NL_5 + _379 + NL_7 + _290 + NL_7 + _282 + NL_5 + _378 + NL_4 + _378 + NL + NL_4 + _307 + NL_5 + _369 + NL_5 + _357 + NL_5 + _359 + NL_5 + _331 + NL_7 + _285 + NL_7 + _294 + NL_5 + _379 + NL_7 + _288 + NL_7 + _293 + NL_5 + _378 + NL_4 + _378 + NL + NL_4 + _308 + NL_5 + _369 + NL_5 + _357 + NL_5 + _359 + NL_5 + _330 + NL_7 + _288 + NL_7 + _293 + NL_5 + _378 + NL_4 + _378 + NL + NL_4 + _306 + NL_5 + _368 + NL_5 + _357 + NL_5 + _359 + NL_5 + _331 + NL_7 + _285 + NL_7 + _294 + NL_5 + _379 + NL_7 + _288 + NL_7 + _293 + NL_5 + _378 + NL_4 + _378 + NL + NL_4 + _309 + NL_5 + _357 + NL_5 + _359 + NL_5 + _324 + NL_7 + _285 + NL_7 + _294 + NL_5 + _379 + NL_7 + _288 + NL_7 + _293 + NL_5 + _378 + NL_4 + _378 + NL + NL_4 + _311 + NL_5 + _357 + NL_5 + _359 + NL_5 + _324 + NL_7 + _286 + NL_7 + _326 + NL_9 + _294 + NL_7 + _378 + NL_5 + _379 + NL_7 + _288 + NL_7 + _327 + NL_9 + _293 + NL_7 + _378 + NL_5 + _378 + NL_4 + _378 + NL + NL_4 + _312 + NL_5 + _357 + NL_5 + _359 + NL_5 + _324 + NL_7 + _287 + NL_7 + _326 + NL_9 + _294 + NL_7 + _378 + NL_5 + _379 + NL_7 + _288 + NL_7 + _327 + NL_9 + _293 + NL_7 + _378 + NL_5 + _378 + NL_4 + _378 + NL + NL_4 + _310 + NL_5 + _356 + NL_5 + _352 + NL_5 + _366 + NL_5 + _323 + NL_9 + _282 + NL_9 + _300 + NL_11 + _266 + NL_9 + _378 + NL_9 + _302 + NL_11 + _348 + NL_9 + _378 + NL_5 + _379 + NL_9 + _283 + NL_9 + _300 + NL_11 + _267 + NL_9 + _378 + NL_9 + _302 + NL_11 + _347 + NL_9 + _378 + NL_5 + _378 + NL_4 + _378 + NL + NL_2 + _71 + NL + NL_2 + _135 + NL_4 + _112 + NL_5 + _116 + NL_7 + _114 + NL_9 + _118 + NL_11 + _85 + NL_13 + _143 + NL_11 + _51 + NL_9 + _60 + NL_7 + _60 + NL_5 + _60 + NL_4 + _60 + NL_2 + _64;
  protected final String _385 = NL_2 + _180 + NL_4 + _112 + NL_5 + _128 + NL_5 + _117 + NL_7 + _111 + NL_9 + _169 + NL_11 + _160 + NL_11 + _161;
  protected final String _386 = NL_11 + _151;
  protected final String _387 = NL_11 + _159;
  protected final String _388 = NL_9 + _68 + NL_7 + _60 + NL_5 + _60 + NL_4 + _60 + NL_2 + _72 + NL + NL_2 + _165 + NL_2 + _115 + NL;
  protected final String _389 = NL_4 + _47 + NL_4 + _87 + NL_5 + _209 + NL_7 + _154 + NL_9 + _81 + NL_7 + _66 + NL_7 + _154 + NL_9 + _80 + NL_7 + _66;
  protected final String _390 = NL_7 + _154 + NL_9 + _78;
  protected final String _391 = _51 + NL_7 + _66;
  protected final String _392 = NL_7 + _154 + NL_9 + _201 + NL_9 + _79;
  protected final String _393 = NL_7 + _153 + NL_9 + _200;
  protected final String _394 = NL_9 + _201;
  protected final String _395 = NL_9 + _82;
  protected final String _396 = NL_9 + _51 + NL_7 + _66;
  protected final String _397 = NL_5 + _77 + NL_4 + _56 + NL + NL_4 + _122 + NL_5 + _123;
  protected final String _398 = NL_7 + _127;
  protected final String _399 = NL_7 + _127 + NL_9 + _142;
  protected final String _400 = NL_9 + _227;
  protected final String _401 = NL_7 + _61;
  protected final String _402 = NL_7 + _170;
  protected final String _403 = NL_9 + _88;
  protected final String _404 = NL_9 + _91;
  protected final String _405 = _57 + NL_7 + _69;
  protected final String _406 = NL_7 + _171 + NL_9 + _110 + NL_9 + _183;
  protected final String _407 = _73 + NL_7 + _69 + NL_7 + _90;
  protected final String _408 = NL_7 + _86;
  protected final String _409 = _245 + NL_9 + _144;
  protected final String _410 = _236 + NL_7 + _51;
  protected final String _411 = NL_8 + _172 + NL_10 + _233 + NL_10 + _84 + NL_12 + _141;
  protected final String _412 = _52 + NL_8 + _69 + NL_8 + _175;
  protected final String _413 = NL_8 + _174 + NL_8 + _215;
  protected final String _414 = NL_10 + _159;
  protected final String _415 = NL_8 + _77;
  protected final String _416 = NL_8 + _172 + NL_10 + _231 + NL_10 + _83;
  protected final String _417 = _246 + NL_12 + _141;
  protected final String _418 = _51 + NL_10 + _344;
  protected final String _419 = _53 + NL_8 + _69;
  protected final String _420 = NL_8 + _133 + NL_8 + _172;
  protected final String _421 = NL_8 + _131 + NL_8 + _172;
  protected final String _422 = NL_8 + _129 + NL_10 + _220;
  protected final String _423 = NL_8 + _189;
  protected final String _424 = NL_8 + _62;
  protected final String _425 = NL_10 + _126 + NL_12 + _142;
  protected final String _426 = _248 + NL_12 + _193 + NL_10 + _60;
  protected final String _427 = NL_10 + _113;
  protected final String _428 = _292 + NL_12 + _139;
  protected final String _429 = NL_12 + _182;
  protected final String _430 = NL_10 + _60;
  protected final String _431 = NL_8 + _130;
  protected final String _432 = NL_8 + _172;
  protected final String _433 = NL_10 + _142;
  protected final String _434 = _248 + NL_10 + _186;
  protected final String _435 = _73 + NL_10 + _83;
  protected final String _436 = NL_10 + _90 + NL_10 + _191;
  protected final String _437 = NL_8 + _69;
  protected final String _438 = NL_6 + _132;
  protected final String _439 = NL_12 + _94;
  protected final String _440 = _15 + NL_12 + _142;
  protected final String _441 = _248 + NL_12 + _184;
  protected final String _442 = NL_12 + _177;
  protected final String _443 = NL_8 + _129 + NL_10 + _221 + NL_10 + _109 + NL_8 + _62 + NL_8 + _119;
  protected final String _444 = NL + _49;
  protected final String _445 = NL_8 + _60;
  protected final String _446 = NL_10 + _232;
  protected final String _447 = NL_10 + _224 + NL_8 + _69;
  protected final String _448 = NL_8 + _129 + NL_8 + _108 + NL_8 + _141;
  protected final String _449 = _248 + NL_8 + _234;
  protected final String _450 = NL_8 + _190;
  protected final String _451 = NL_8 + _62 + NL_8 + _120;
  protected final String _452 = NL_8 + _129 + NL_8 + _105 + NL_8 + _141;
  protected final String _453 = _248 + NL_8 + _226 + NL_8 + _186;
  protected final String _454 = NL_8 + _104;
  protected final String _455 = NL_8 + _62 + NL_8 + _210;
  protected final String _456 = NL_10 + _158 + NL_12 + _94;
  protected final String _457 = _15 + NL_12 + _141;
  protected final String _458 = _73 + NL_12 + _22 + NL_12 + _186;
  protected final String _459 = _73 + NL_12 + _212;
  protected final String _460 = _251 + NL_14 + _157 + NL_15 + _93;
  protected final String _461 = _90 + NL_15 + _176;
  protected final String _462 = _70 + NL_14 + _66 + NL_14 + _157 + NL_15 + _95;
  protected final String _463 = _15 + NL_15 + _223 + NL_15 + _213;
  protected final String _464 = NL_16 + _152 + NL_18 + _83;
  protected final String _465 = _251 + NL_19 + _141;
  protected final String _466 = NL_19 + _188;
  protected final String _467 = _73 + NL_18 + _51 + NL_16 + _66;
  protected final String _468 = NL_15 + _77 + NL_14 + _66 + NL_12 + _77 + NL_10 + _66;
  protected final String _469 = NL_8 + _173 + NL_8 + _129 + NL_8 + _99 + NL_8 + _141;
  protected final String _470 = _248 + NL_8 + _230 + NL_8 + _186;
  protected final String _471 = NL_8 + _98;
  protected final String _472 = NL_8 + _62 + NL_8 + _206;
  protected final String _473 = NL_8 + _158;
  protected final String _474 = NL_10 + _125 + NL_12 + _92;
  protected final String _475 = _248 + NL_12 + _192;
  protected final String _476 = _19 + NL_10 + _60;
  protected final String _477 = NL_12 + _92;
  protected final String _478 = NL_12 + _13;
  protected final String _479 = NL_12 + _138;
  protected final String _480 = NL_12 + _185;
  protected final String _481 = NL_10 + _203;
  protected final String _482 = NL_12 + _158 + NL_14 + _83;
  protected final String _483 = _51 + NL_12 + _66;
  protected final String _484 = NL_10 + _77 + NL_8 + _66;
  protected final String _485 = NL + _48;
  protected final String _486 = NL_8 + _173 + NL_8 + _129 + NL_8 + _100 + NL_8 + _141;
  protected final String _487 = _248 + NL_8 + _229 + NL_8 + _186;
  protected final String _488 = NL_8 + _62 + NL_8 + _207;
  protected final String _489 = NL_10 + _158 + NL_12 + _92;
  protected final String _490 = NL_12 + _137;
  protected final String _491 = NL_12 + _181;
  protected final String _492 = _73 + NL_12 + _186;
  protected final String _493 = _73 + NL_12 + _204;
  protected final String _494 = NL_14 + _158 + NL_15 + _83;
  protected final String _495 = _251 + NL_17 + _137;
  protected final String _496 = NL_17 + _188;
  protected final String _497 = _73 + NL_15 + _51 + NL_14 + _66;
  protected final String _498 = NL_12 + _77 + NL_10 + _66;
  protected final String _499 = NL_8 + _173 + NL_8 + _129 + NL_8 + _101 + NL_8 + _141;
  protected final String _500 = _248 + NL_8 + _222 + NL_8 + _186;
  protected final String _501 = NL_8 + _62 + NL_8 + _208;
  protected final String _502 = NL_10 + _156 + NL_13 + _83;
  protected final String _503 = _251 + NL_14 + _141;
  protected final String _504 = NL_14 + _188;
  protected final String _505 = _73 + NL_12 + _51;
  protected final String _506 = NL_12 + _103;
  protected final String _507 = NL_10 + _66;
  protected final String _508 = NL_8 + _173 + NL_8 + _129 + NL_8 + _107 + NL_8 + _140;
  protected final String _509 = _248 + NL_8 + _228 + NL_8 + _186;
  protected final String _510 = NL_8 + _106;
  protected final String _511 = NL_8 + _62 + NL_8 + _211;
  protected final String _512 = NL_10 + _156;
  protected final String _513 = NL_12 + _83;
  protected final String _514 = NL_12 + _202;
  protected final String _515 = _251 + NL_17 + _141;
  protected final String _516 = NL_12 + _77;
  protected final String _517 = NL_8 + _173 + NL_8 + _129 + NL_8 + _97 + NL_8 + _141;
  protected final String _518 = _248 + NL_8 + _219 + NL_8 + _186;
  protected final String _519 = NL_8 + _96;
  protected final String _520 = NL_8 + _62 + NL_8 + _205;
  protected final String _521 = NL_12 + _51;
  protected final String _522 = NL_8 + _129 + NL_8 + _102 + NL_8 + _141;
  protected final String _523 = _248 + NL_8 + _225 + NL_8 + _186;
  protected final String _524 = NL_8 + _62 + NL_8 + _121 + NL_10 + _196;
  protected final String _525 = NL_10 + _197 + NL_12 + _149;
  protected final String _526 = NL_12 + _145;
  protected final String _527 = NL_12 + _150;
  protected final String _528 = NL_12 + _146;
  protected final String _529 = NL_12 + _147;
  protected final String _530 = NL_12 + _148;
  protected final String _531 = NL_10 + _73;
  protected final String _532 = NL_10 + _214;
  protected final String _533 = NL_12 + _155;
  protected final String _534 = _8 + NL_14 + _83;
  protected final String _535 = _251 + NL_15 + _141;
  protected final String _536 = _248 + NL_15 + _195;
  protected final String _537 = _73 + NL_15 + _187;
  protected final String _538 = _73 + NL_14 + _51;
  protected final String _539 = NL_14 + _103;
  protected final String _540 = NL_14 + _17;
  protected final String _541 = NL_15 + _141;
  protected final String _542 = _248 + NL_15 + _186;
  protected final String _543 = _73 + NL_14 + _18;
  protected final String _544 = NL_14 + _235;
  protected final String _545 = NL_12 + _66;
  protected final String _546 = NL_10 + _77 + NL_8 + _60 + NL_8 + _124 + NL_8 + _60;
  protected final String _547 = NL_5 + _60 + NL_4 + _60 + NL_3 + _60 + NL_3 + _67 + NL_1 + _58 + NL + _65;

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
    stringBuffer.append(_380);
    stringBuffer.append(reporter.getTitle());
    stringBuffer.append(_381);
    stringBuffer.append(_382);
    stringBuffer.append(_383);
    stringBuffer.append('\u25B7');
    stringBuffer.append(_384);
    stringBuffer.append(_385);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() == null) {
    stringBuffer.append(_386);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_66);
    } else {
    stringBuffer.append(_387);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_251);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_55);
    }
    }
    stringBuffer.append(_388);
    stringBuffer.append(_389);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() != null) {
    stringBuffer.append(_390);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_251);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_391);
    } else {
      if (iuReport == null) {
    stringBuffer.append(_392);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_391);
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
    stringBuffer.append(_393);
    if (index != -1) {
    stringBuffer.append(_394);
    }
    stringBuffer.append(_395);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_251);
    stringBuffer.append(NL_9);
    stringBuffer.append(label);
    stringBuffer.append(_396);
    }
    }
    stringBuffer.append(_397);
    if (iuReport == null) {
    stringBuffer.append(_398);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_61);
    } else {
    stringBuffer.append(_399);
    stringBuffer.append(report.getIUImage(iuReport.getIU()));
    stringBuffer.append(_248);
    String name = report.getName(iuReport.getIU(), false);
    if (name != null) {
    stringBuffer.append(NL_9);
    stringBuffer.append(helper.htmlEscape(name, true));
    stringBuffer.append(_90);
    } else {
    stringBuffer.append(_400);
    }
    stringBuffer.append(NL_9);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_401);
    }
    stringBuffer.append(_402);
    if (report != null && report.getDate() != null) {
    stringBuffer.append(_403);
    stringBuffer.append(report.getDate());
    stringBuffer.append(_57);
    }
    stringBuffer.append(_404);
    stringBuffer.append(reporter.getNow());
    stringBuffer.append(_405);
    if (report != null && !report.getSiteURL().startsWith("file:")) {
    stringBuffer.append(_406);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_407);
    }
    stringBuffer.append(_408);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_409);
    stringBuffer.append(reporter.getReportBrandingImage());
    stringBuffer.append(_237);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_410);
    if (indexReport != null) {
    stringBuffer.append(_411);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_248);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_412);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_247);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_54);
    Map<String, String> allReports = indexReport.getAllReports();
    if (allReports != null && !allReports.isEmpty()) {
    stringBuffer.append(_413);
    for (Map.Entry<String, String> entry : allReports.entrySet()) {
    stringBuffer.append(_414);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_251);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_55);
    }
    stringBuffer.append(_415);
    }
    } else if (iuReport != null) {
    stringBuffer.append(_416);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_417);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_248);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_418);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_247);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_419);
    String provider = iuReport.getProvider();
    if (provider != null) {
    stringBuffer.append(_420);
    stringBuffer.append(helper.htmlEscape(provider, false));
    stringBuffer.append(_69);
    }
    String description = iuReport.getDescription();
    if (description != null) {
    stringBuffer.append(_421);
    stringBuffer.append(helper.htmlEscape(description, false));
    stringBuffer.append(_69);
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
    stringBuffer.append(_422);
    if (!expiredCertificates.isEmpty()) {
    stringBuffer.append(_423);
    stringBuffer.append(expiredCertificates.size());
    stringBuffer.append(_2);
    if (expiredCertificates.size() > 1) {
    stringBuffer.append(_345);
    }
    stringBuffer.append(_20);
    }
    stringBuffer.append(_424);
    if (certificates.isEmpty()) {
    stringBuffer.append(_425);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_426);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_427);
    stringBuffer.append(count++);
    stringBuffer.append(_428);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_248);
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
    stringBuffer.append(_429);
    stringBuffer.append(spanStyle);
    stringBuffer.append(_218);
    stringBuffer.append(keyStyle);
    stringBuffer.append(_297);
    stringBuffer.append(key);
    stringBuffer.append(_216);
    stringBuffer.append(value);
    stringBuffer.append(_73);
    }
    stringBuffer.append(_430);
    }
    }
    }
    stringBuffer.append(_431);
    for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
        String artifact = entry.getKey();
        Boolean signed = entry.getValue();
    stringBuffer.append(_432);
    if (signed != null) {
    stringBuffer.append(_433);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_248);
    }
    stringBuffer.append(_433);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_434);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_435);
    stringBuffer.append(report.getRepositoryURL(artifact) + '/' + artifact);
    stringBuffer.append(_251);
    stringBuffer.append(artifact);
    stringBuffer.append(_51);
    for (String status : report.getArtifactStatus(artifact)) {
    stringBuffer.append(_436);
    stringBuffer.append(status);
    stringBuffer.append(_73);
    }
    stringBuffer.append(_437);
    }
    }
    List<Report.LicenseDetail> licenses = report.getLicenses(iuReport.getIU());
    if (licenses != null) {
    stringBuffer.append(_438);
    for (LicenseDetail license : licenses) {
    String id = license.getUUID();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
    stringBuffer.append(_439);
    stringBuffer.append(id);
    stringBuffer.append(_257);
    stringBuffer.append(id);
    stringBuffer.append(_440);
    stringBuffer.append(report.getLicenseImage());
    stringBuffer.append(_441);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_251);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_73);
    stringBuffer.append(NL_12);
    stringBuffer.append(id);
    stringBuffer.append(_442);
    stringBuffer.append(id);
    stringBuffer.append(_243);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_251);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_194);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_251);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_74);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_251);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_73);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_70);
    }
    }
    stringBuffer.append(_443);
    for (String xml : report.getXML(iuReport.getIU(), Collections.<String, String> emptyMap())) {
    stringBuffer.append(_444);
    stringBuffer.append(xml);
    }
    stringBuffer.append(_445);
    } else {
    stringBuffer.append(_416);
    stringBuffer.append(report.getHelpLink());
    stringBuffer.append(_417);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_248);
    stringBuffer.append(NL_12);
    stringBuffer.append(report.getHelpText());
    stringBuffer.append(_418);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_247);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_419);
    List<Report> children = report.getChildren();
    if (!children.isEmpty()) {
    stringBuffer.append(_432);
    if (!report.isRoot()) {
    stringBuffer.append(_446);
    }
    stringBuffer.append(_447);
    }
    String metadataXML = report.getMetadataXML();
    String artifactXML = report.getArtifactML();
    if (metadataXML != null || artifactXML != null) {
    stringBuffer.append(_448);
    stringBuffer.append(report.getRepositoryImage());
    stringBuffer.append(_449);
    if (metadataXML != null && metadataXML.contains("bad-absolute-location") || artifactXML != null && artifactXML.contains("bad-absolute-location")) {
    stringBuffer.append(_450);
    }
    stringBuffer.append(_451);
    if (metadataXML != null) {
    stringBuffer.append(_444);
    stringBuffer.append(metadataXML);
    }
    if (artifactXML != null) {
    if (metadataXML != null) {
    stringBuffer.append(NL);
    }
    stringBuffer.append(_444);
    stringBuffer.append(artifactXML);
    }
    stringBuffer.append(_445);
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
    stringBuffer.append(_452);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_453);
    stringBuffer.append(licenses.size());
    stringBuffer.append(_73);
    if (nonConformant != 0) {
    stringBuffer.append(_423);
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
    stringBuffer.append(_454);
    stringBuffer.append(displayButton);
    stringBuffer.append(_45);
    stringBuffer.append(onClick);
    stringBuffer.append(_252);
    }
    stringBuffer.append(_455);
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
    stringBuffer.append(_456);
    stringBuffer.append(id);
    stringBuffer.append(_260);
    stringBuffer.append(id);
    stringBuffer.append(_457);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_441);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_251);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_458);
    stringBuffer.append(ius.size());
    stringBuffer.append(_459);
    stringBuffer.append(id);
    stringBuffer.append(_460);
    stringBuffer.append(id);
    stringBuffer.append(_259);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    stringBuffer.append(NL_15);
    stringBuffer.append(id);
    stringBuffer.append(_461);
    stringBuffer.append(id);
    stringBuffer.append(_243);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_251);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_194);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_251);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_74);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_251);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_73);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_462);
    stringBuffer.append(id);
    stringBuffer.append(_261);
    stringBuffer.append(id);
    stringBuffer.append(_463);
    stringBuffer.append(id);
    stringBuffer.append(_251);
    for (IInstallableUnit iu : ius) {
    stringBuffer.append(_464);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_465);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_248);
    stringBuffer.append(NL_19);
    stringBuffer.append(helper.htmlEscape(report.getName(iu, true), true));
    stringBuffer.append(_466);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_467);
    }
    stringBuffer.append(_468);
    }
    }
    stringBuffer.append(_415);
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
    stringBuffer.append(_469);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_470);
    stringBuffer.append(allCertificates.size());
    stringBuffer.append(_73);
    if (unsigned != null) {
    stringBuffer.append(_423);
    stringBuffer.append(unsigned.size());
    stringBuffer.append(_7);
    }
    if (expiredChainCount > 0) {
    stringBuffer.append(_423);
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
    stringBuffer.append(_471);
    stringBuffer.append(displayButton);
    stringBuffer.append(_44);
    stringBuffer.append(onClick);
    stringBuffer.append(_252);
    }
    stringBuffer.append(_472);
    stringBuffer.append(display);
    stringBuffer.append(_42);
    for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : allCertificates.entrySet()) {
        List<Certificate> certificates = entry.getKey();
        String id = "certificates" + ++idCount;
    stringBuffer.append(_473);
    if (certificates.isEmpty()) {
    stringBuffer.append(_474);
    stringBuffer.append(id);
    stringBuffer.append(_258);
    stringBuffer.append(id);
    stringBuffer.append(_457);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_475);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_476);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_427);
    stringBuffer.append(count++ + 2);
    stringBuffer.append(_291);
    if (count == 1) {
    stringBuffer.append(_477);
    stringBuffer.append(id);
    stringBuffer.append(_258);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    } else {
    stringBuffer.append(_478);
    }
    stringBuffer.append(_479);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_248);
    if (count == 1) {
    stringBuffer.append(_480);
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
    stringBuffer.append(_429);
    stringBuffer.append(spanStyle);
    stringBuffer.append(_218);
    stringBuffer.append(keyStyle);
    stringBuffer.append(_297);
    stringBuffer.append(key);
    stringBuffer.append(_216);
    stringBuffer.append(value);
    stringBuffer.append(_73);
    }
    stringBuffer.append(_430);
    }
    }
    stringBuffer.append(_481);
    stringBuffer.append(id);
    stringBuffer.append(_251);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_482);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_253);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_249);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_483);
    }
    stringBuffer.append(_484);
    }
    stringBuffer.append(_415);
    }
    stringBuffer.append(_485);
    Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
    if (!featureProviders.isEmpty()) {
      int nonEclipse = 0;
      for (String provider : featureProviders.keySet()) {
        if (!provider.toLowerCase().contains("eclipse"))
          ++nonEclipse;
      }
    stringBuffer.append(_486);
    stringBuffer.append(report.getProviderImage());
    stringBuffer.append(_487);
    stringBuffer.append(featureProviders.size());
    stringBuffer.append(_73);
    if (nonEclipse != 0) {
    stringBuffer.append(_423);
    stringBuffer.append(nonEclipse);
    stringBuffer.append(_6);
    }
    stringBuffer.append(_488);
    int count = 0;
      for (Map.Entry<String, Set<IInstallableUnit>> provider : featureProviders.entrySet()) {
        String style = !provider.getKey().toLowerCase().contains("eclipse") ? " style=\"color: FireBrick;\"" : "";
        Set<IInstallableUnit> features = provider.getValue();
        String id = "nested_feature_providers" + ++count;
    stringBuffer.append(_489);
    stringBuffer.append(id);
    stringBuffer.append(_256);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    for (String image : report.getBrandingImages(features)) {
    stringBuffer.append(_490);
    stringBuffer.append(image);
    stringBuffer.append(_248);
    }
    stringBuffer.append(_491);
    stringBuffer.append(style);
    stringBuffer.append(_217);
    stringBuffer.append(provider.getKey());
    stringBuffer.append(_492);
    stringBuffer.append(features.size());
    stringBuffer.append(_493);
    stringBuffer.append(id);
    stringBuffer.append(_242);
    for (IInstallableUnit feature : features) {
          String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_494);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_495);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_248);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_496);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_497);
    }
    stringBuffer.append(_498);
    }
    stringBuffer.append(_415);
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
    stringBuffer.append(_499);
    stringBuffer.append(report.getFeatureImage());
    stringBuffer.append(_500);
    stringBuffer.append(features.size());
    stringBuffer.append(_73);
    if (brokenBranding != 0) {
    stringBuffer.append(_423);
    stringBuffer.append(brokenBranding);
    stringBuffer.append(_1);
    }
    if (noBranding != 0) {
    stringBuffer.append(_423);
    stringBuffer.append(noBranding);
    stringBuffer.append(_5);
    }
    stringBuffer.append(_501);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_502);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_503);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_248);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_504);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_505);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        for (LicenseDetail license : report.getLicenses(feature)) {
          String id = license.getUUID();
          licenseReplacements.put(">" + id, "><button class='bb search-for-me' style='" + helper.getStyle(license) + "' onclick=\"clickOnButton('lic_" + id
              + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_506);
    stringBuffer.append(id);
    stringBuffer.append(_238);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_240);
    stringBuffer.append(id);
    stringBuffer.append(_254);
    stringBuffer.append(id);
    stringBuffer.append(_255);
    stringBuffer.append(id);
    stringBuffer.append(_14);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_59);
    }
    stringBuffer.append(_507);
    }
    stringBuffer.append(_415);
    }
    Collection<IInstallableUnit> products = report.getProducts();
    if (!products.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_508);
    stringBuffer.append(report.getProductImage());
    stringBuffer.append(_509);
    stringBuffer.append(products.size());
    stringBuffer.append(_73);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit product : products) {
          String productID = "_product_" + report.getIUID(product);
          onClick.append("expand2('products_all_arrows', '").append(productID).append("');");
        }
    stringBuffer.append(_510);
    stringBuffer.append(displayButton);
    stringBuffer.append(_46);
    stringBuffer.append(onClick);
    stringBuffer.append(_252);
    }
    stringBuffer.append(_511);
    stringBuffer.append(display);
    stringBuffer.append(_41);
    for (IInstallableUnit product : products) {
        String productImage = report.getIUImage(product);
        String productID = "_product_" + report.getIUID(product);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(product));
    stringBuffer.append(_512);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_477);
    stringBuffer.append(productID);
    stringBuffer.append(_258);
    stringBuffer.append(productID);
    stringBuffer.append(_15);
    }
    stringBuffer.append(_513);
    stringBuffer.append(report.getRelativeIUReportURL(product));
    stringBuffer.append(_503);
    stringBuffer.append(productImage);
    stringBuffer.append(_248);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(product, true), true));
    stringBuffer.append(_504);
    stringBuffer.append(report.getVersion(product));
    stringBuffer.append(_505);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_514);
    stringBuffer.append(productID);
    stringBuffer.append(_241);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_494);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_515);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_248);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_496);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_497);
    }
    stringBuffer.append(_516);
    }
    stringBuffer.append(_507);
    }
    stringBuffer.append(_415);
    }
    Collection<IInstallableUnit> categories = report.getCategories();
    if (!categories.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_517);
    stringBuffer.append(report.getCategoryImage());
    stringBuffer.append(_518);
    stringBuffer.append(categories.size());
    stringBuffer.append(_73);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit category : categories) {
          String categoryID = "_category_" + report.getIUID(category);
          onClick.append("expand2('categories_all_arrows', '").append(categoryID).append("');");
        }
    stringBuffer.append(_519);
    stringBuffer.append(displayButton);
    stringBuffer.append(_43);
    stringBuffer.append(onClick);
    stringBuffer.append(_252);
    }
    stringBuffer.append(_520);
    stringBuffer.append(display);
    stringBuffer.append(_41);
    for (IInstallableUnit category : categories) {
        String categoryImage = report.getIUImage(category);
        String categoryID = "_category_" + report.getIUID(category);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(category));
    stringBuffer.append(_512);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_477);
    stringBuffer.append(categoryID);
    stringBuffer.append(_258);
    stringBuffer.append(categoryID);
    stringBuffer.append(_15);
    }
    stringBuffer.append(_513);
    stringBuffer.append(report.getRelativeIUReportURL(category));
    stringBuffer.append(_503);
    stringBuffer.append(categoryImage);
    stringBuffer.append(_248);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(category, true), true));
    stringBuffer.append(_521);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_514);
    stringBuffer.append(categoryID);
    stringBuffer.append(_244);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_494);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_515);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_248);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_496);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_497);
    }
    stringBuffer.append(_516);
    }
    stringBuffer.append(_507);
    }
    stringBuffer.append(_415);
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
    stringBuffer.append(_522);
    stringBuffer.append(report.getBundleImage());
    stringBuffer.append(_523);
    stringBuffer.append(ius.size());
    stringBuffer.append(_73);
    if (duplicateCount > 0) {
    stringBuffer.append(_423);
    stringBuffer.append(duplicateCount);
    stringBuffer.append(_9);
    }
    stringBuffer.append(_524);
    if (duplicateCount > 0 || !unsignedIUs.isEmpty() || !badProviderIUs.isEmpty() || !badLicenseIUs.isEmpty()
          || !brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_525);
    if (!badLicenseIUs.isEmpty()) {
    stringBuffer.append(_526);
    }
    if (!unsignedIUs.isEmpty()) {
    stringBuffer.append(_527);
    }
    if (!badProviderIUs.isEmpty()) {
    stringBuffer.append(_528);
    }
    if (!brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_529);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_530);
    }
    stringBuffer.append(_531);
    }
    stringBuffer.append(_532);
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
    stringBuffer.append(_533);
    stringBuffer.append(id);
    stringBuffer.append(_239);
    stringBuffer.append(classNames);
    stringBuffer.append(_534);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_535);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_536);
    stringBuffer.append(iuID);
    stringBuffer.append(_537);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_251);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_538);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        if (report.isFeature(iu)) {
          for (LicenseDetail license : report.getLicenses(iu)) {
            String licenseID = license.getUUID();
            licenseReplacements.put(">" + licenseID, "><button class='bb search-for-me' style='" + helper.getStyle(license)
                + "' onclick=\"clickOnButton('lic_" + licenseID + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_539);
    stringBuffer.append(licenseID);
    stringBuffer.append(_238);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_240);
    stringBuffer.append(licenseID);
    stringBuffer.append(_254);
    stringBuffer.append(licenseID);
    stringBuffer.append(_255);
    stringBuffer.append(licenseID);
    stringBuffer.append(_14);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_59);
    }
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iu);
        for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
          String artifact = entry.getKey();
          Boolean signed = entry.getValue();
    stringBuffer.append(_540);
    if (signed != null) {
    stringBuffer.append(_541);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_248);
    }
    stringBuffer.append(_541);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_542);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_543);
    }
    String brandingImage = report.getBrandingImage(iu);
        if (brandingImage != null) {
    stringBuffer.append(_544);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_250);
    }
    stringBuffer.append(_545);
    }
    stringBuffer.append(_546);
    }
    }
    stringBuffer.append(_547);
    return stringBuffer.toString();
  }
}
