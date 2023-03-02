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
  protected static final String _2 = " Invalid Licenses)</span>";
  protected static final String _3 = " Invalid Signature";
  protected static final String _4 = " Invalid Signatures)</span>";
  protected static final String _5 = " No Branding Images)</span>";
  protected static final String _6 = " Not Provided by Eclipse)</span>";
  protected static final String _7 = " Unsigned Artifacts)</span>";
  protected static final String _8 = " font-smaller\">";
  protected static final String _9 = " invalid Signatures)</span>";
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
  protected static final String _47 = ";\" onclick=\"toggle('pgpKeys_all_arrows');";
  protected static final String _48 = ";\" onclick=\"toggle('products_all_arrows');";
  protected static final String _49 = "<!-- navigation sidebar -->";
  protected static final String _50 = "<!--- providers -->";
  protected static final String _51 = "<!----------->";
  protected static final String _52 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
  protected static final String _53 = "</a>";
  protected static final String _54 = "</a> ";
  protected static final String _55 = "</a> report.";
  protected static final String _56 = "</a>.";
  protected static final String _57 = "</a>.</p>";
  protected static final String _58 = "</a></li>";
  protected static final String _59 = "</aside>";
  protected static final String _60 = "</b>";
  protected static final String _61 = "</body>";
  protected static final String _62 = "</button>";
  protected static final String _63 = "</div>";
  protected static final String _64 = "</h2>";
  protected static final String _65 = "</h3>";
  protected static final String _66 = "</head>";
  protected static final String _67 = "</header>";
  protected static final String _68 = "</html>";
  protected static final String _69 = "</li>";
  protected static final String _70 = "</main>";
  protected static final String _71 = "</ol>";
  protected static final String _72 = "</p>";
  protected static final String _73 = "</pre>";
  protected static final String _74 = "</script>";
  protected static final String _75 = "</section>";
  protected static final String _76 = "</span>";
  protected static final String _77 = "</span><span style=\"color: green; text-decoration: underline; ";
  protected static final String _78 = "</style>";
  protected static final String _79 = "</title>";
  protected static final String _80 = "</ul>";
  protected static final String _81 = "<a class=\"separator\" href=\"";
  protected static final String _82 = "<a class=\"separator\" href=\".\">";
  protected static final String _83 = "<a class=\"separator\" href=\"https://www.eclipse.org/downloads\">Downloads</a>";
  protected static final String _84 = "<a class=\"separator\" href=\"https://www.eclipse.org\">Home</a>";
  protected static final String _85 = "<a class=\"separator\" style=\"display: inline-block; margin-left: 0.25em;\" href=\"";
  protected static final String _86 = "<a href=\"";
  protected static final String _87 = "<a href=\"https://wiki.eclipse.org/Oomph_Repository_Analyzer#Description\" target=\"oomph_wiki\">";
  protected static final String _88 = "<a href=\"https://www.eclipse.org/\">";
  protected static final String _89 = "<a style=\"float:right; font-size: 200%;\" href=\"";
  protected static final String _90 = "<aside id=\"leftcol\" class=\"col-md-4 font-smaller\">";
  protected static final String _91 = "<b>Built: ";
  protected static final String _92 = "<body id=\"body_solstice\">";
  protected static final String _93 = "<br/>";
  protected static final String _94 = "<br/><b>Reported: ";
  protected static final String _95 = "<button id=\"";
  protected static final String _96 = "<button id=\"_";
  protected static final String _97 = "<button id=\"__";
  protected static final String _98 = "<button id=\"_f";
  protected static final String _99 = "<button id=\"categories_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _100 = "<button id=\"categories_arrows\" class=\"orange bb\" onclick=\"expand_collapse('categories'); expand_collapse_inline('categories_all_arrows');\">&#x25B7;</button>";
  protected static final String _101 = "<button id=\"certificates_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _102 = "<button id=\"certificates_arrows\" class=\"orange bb\" onclick=\"expand_collapse('certificates'); expand_collapse_inline('certificates_all_arrows');\">&#x25B7;</button>";
  protected static final String _103 = "<button id=\"feature_providers_arrows\" class=\"orange bb\" onclick=\"expand_collapse('feature_providers');\">&#x25B7;</button>";
  protected static final String _104 = "<button id=\"features_arrows\" class=\"orange bb\" onclick=\"expand_collapse('features');\">&#x25B7;</button>";
  protected static final String _105 = "<button id=\"ius_arrows\" class=\"orange bb\" onclick=\"expand_collapse('ius'); match('subset');\">&#x25B7;</button>";
  protected static final String _106 = "<button id=\"lic_";
  protected static final String _107 = "<button id=\"licenses_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _108 = "<button id=\"licenses_arrows\" class=\"orange bb\" onclick=\"expand_collapse('licenses'); expand_collapse_inline('licenses_all_arrows');\">&#x25B7;</button>";
  protected static final String _109 = "<button id=\"pgpKeys_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _110 = "<button id=\"pgpKeys_arrows\" class=\"orange bb\" onclick=\"expand_collapse('pgpKeys'); expand_collapse_inline('pgpKeys_all_arrows');\">&#x25B7;</button>";
  protected static final String _111 = "<button id=\"products_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _112 = "<button id=\"products_arrows\" class=\"orange bb\" onclick=\"expand_collapse('products'); expand_collapse_inline('products_all_arrows');\">&#x25B7;</button>";
  protected static final String _113 = "<button id=\"repos_arrows\" class=\"orange bb\" onclick=\"expand_collapse('repos');\">&#x25B7;</button>";
  protected static final String _114 = "<button id=\"xml-links\" class=\"orange bb\" onclick=\"expand_collapse_all('xml-links');\">&#x25B7;</button>";
  protected static final String _115 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 150%;\" onclick=\"copyToClipboard('#p1')\">&#x270e;</button>";
  protected static final String _116 = "<div class=\"col-sm-16 padding-left-30\">";
  protected static final String _117 = "<div class=\"container\">";
  protected static final String _118 = "<div class=\"font-smaller\" style=\"margin-left: ";
  protected static final String _119 = "<div class=\"font-smaller\">";
  protected static final String _120 = "<div class=\"hidden-xs col-sm-8 col-md-6 col-lg-5\" id=\"header-left\">";
  protected static final String _121 = "<div class=\"novaContent container\" id=\"novaContent\">";
  protected static final String _122 = "<div class=\"row\" id=\"header-row\">";
  protected static final String _123 = "<div class=\"row\">";
  protected static final String _124 = "<div class=\"wrapper-logo-default\">";
  protected static final String _125 = "<div class=\"xml-iu\" style=\"display:block;\">";
  protected static final String _126 = "<div class=\"xml-repo\" id=\"repos\" style=\"display: none;\">";
  protected static final String _127 = "<div id=\"ius\" style=\"display:none;\">";
  protected static final String _128 = "<div id=\"maincontent\">";
  protected static final String _129 = "<div id=\"midcolumn\" style=\"width: 70%;\">";
  protected static final String _130 = "<div style=\"height: 40ex;\">";
  protected static final String _131 = "<div style=\"text-indent: -2em\">";
  protected static final String _132 = "<div>";
  protected static final String _133 = "<h2 style=\"text-align: center;\">";
  protected static final String _134 = "<h3 class=\"sr-only\">Breadcrumbs</h3>";
  protected static final String _135 = "<h3 style=\"font-weight: bold;\">";
  protected static final String _136 = "<h3 style=\"font-weight: bold;\">Artifacts</h3>";
  protected static final String _137 = "<h3 style=\"font-weight: bold;\">Description</h3>";
  protected static final String _138 = "<h3 style=\"font-weight: bold;\">Licenses</h3>";
  protected static final String _139 = "<h3 style=\"font-weight: bold;\">Provider</h3>";
  protected static final String _140 = "<head>";
  protected static final String _141 = "<header role=\"banner\" id=\"header-wrapper\">";
  protected static final String _142 = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">";
  protected static final String _143 = "<img alt=\"Branding Image\" class=\"fit-image\" src=\"";
  protected static final String _144 = "<img alt=\"Signed\" class=\"fit-image\" src=\"";
  protected static final String _145 = "<img alt=\"Signed\" class=\"fit-image\" src=\"../";
  protected static final String _146 = "<img class=\"fit-image> src=\"";
  protected static final String _147 = "<img class=\"fit-image\" src=\"";
  protected static final String _148 = "<img class=\"fit-image\" src=\"../";
  protected static final String _149 = "<img class=\"logo-eclipse-default img-responsive hidden-xs\" alt=\"Eclipse Log\" src=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/logo/eclipse-426x100.png\"/>";
  protected static final String _150 = "<img src=\"";
  protected static final String _151 = "<input id=\"bad-license\" type=\"radio\" name=\"filter\" value=\"bad-license\" class=\"filter\" onclick=\"filterIU('bad-license');\"> Bad License </input>";
  protected static final String _152 = "<input id=\"bad-provider\" type=\"radio\" name=\"filter\" value=\"bad-provider\" class=\"filter\" onclick=\"filterIU('bad-provider');\"> Bad Provider </input>";
  protected static final String _153 = "<input id=\"broken-branding\" type=\"radio\" name=\"filter\" value=\"broken-branding\" class=\"filter\" onclick=\"filterIU('broken-branding');\"> Broken Branding </input>";
  protected static final String _154 = "<input id=\"duplicates\" type=\"radio\" name=\"filter\" value=\"duplicate\" class=\"filter\" onclick=\"filterIU('duplicate');\"> Duplicates </input>";
  protected static final String _155 = "<input id=\"filter\" type=\"radio\" name=\"filter\" value=\"all\" class=\"filter\" onclick=\"filterIU('iu-li');\" checked=\"checked\"> All </input>";
  protected static final String _156 = "<input id=\"unsigned\" type=\"radio\" name=\"filter\" value=\"unsigned\" class=\"filter\" onclick=\"filterIU('unsigned');\"> Unsigned </input>";
  protected static final String _157 = "<li class=\"active\">";
  protected static final String _158 = "<li class=\"font-smaller text-nowrap\">";
  protected static final String _159 = "<li class=\"separator\" style=\"font-size: 90%;\">";
  protected static final String _160 = "<li class=\"separator\">";
  protected static final String _161 = "<li id=\"_iu_";
  protected static final String _162 = "<li style=\"font-size: 100%; white-space: nowrap;\">";
  protected static final String _163 = "<li style=\"margin-left: 1em;\">";
  protected static final String _164 = "<li>";
  protected static final String _165 = "<li><a href=\"";
  protected static final String _166 = "<li><a href=\"https://www.eclipse.org/\">Home</a></li>";
  protected static final String _167 = "<li><a href=\"https://www.eclipse.org/downloads/\">Downloads</a></li>";
  protected static final String _168 = "<link href=\"//fonts.googleapis.com/css?family=Open+Sans:400,700,300,600,100\" rel=\"stylesheet\" type=\"text/css\"/>";
  protected static final String _169 = "<link rel=\"icon\" type=\"image/ico\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/favicon.ico\"/>";
  protected static final String _170 = "<link rel=\"stylesheet\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/stylesheets/styles.min.css\"/>";
  protected static final String _171 = "<main class=\"no-promo\">";
  protected static final String _172 = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
  protected static final String _173 = "<meta name=\"description\" content=\"Update Sites Reports\"/>";
  protected static final String _174 = "<meta name=\"keywords\" content=\"eclipse,update site\"/>";
  protected static final String _175 = "<ol class=\"breadcrumb\">";
  protected static final String _176 = "<p style=\"font-size: 125%; text-align: center;\">";
  protected static final String _177 = "<p style=\"text-align: center;\">";
  protected static final String _178 = "<p>";
  protected static final String _179 = "<p></p>";
  protected static final String _180 = "<p>Reports are generated specifically for the following sites:</p>";
  protected static final String _181 = "<p>This report is produced by <a href=\"";
  protected static final String _182 = "<pre id=\"_";
  protected static final String _183 = "<pre id=\"__";
  protected static final String _184 = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";
  protected static final String _185 = "<script>";
  protected static final String _186 = "<section class=\"hidden-print default-breadcrumbs\" id=\"breadcrumb\">";
  protected static final String _187 = "<span ";
  protected static final String _188 = "<span class=\"text-nowrap\"";
  protected static final String _189 = "<span class=\"text-nowrap\"><a href=\"";
  protected static final String _190 = "<span id=\"p1\" style=\"font-size: 125%\">";
  protected static final String _191 = "<span style='font-weight: bold; color: Firebrick;'>(";
  protected static final String _192 = "<span style=\"";
  protected static final String _193 = "<span style=\"color: DarkCyan; font-size: 110%;\">(";
  protected static final String _194 = "<span style=\"color: DarkCyan;\">";
  protected static final String _195 = "<span style=\"color: DarkOliveGreen;";
  protected static final String _196 = "<span style=\"color: DarkOliveGreen;\">";
  protected static final String _197 = "<span style=\"color: FireBrick; font-size: 60%;\">(";
  protected static final String _198 = "<span style=\"color: FireBrick; font-size: 60%;\">(Inappropriate absolute location)</span>";
  protected static final String _199 = "<span style=\"color: FireBrick; font-size: 90%;\">";
  protected static final String _200 = "<span style=\"color: FireBrick;\">(";
  protected static final String _201 = "<span style=\"color: FireBrick;\">Unsigned</span>";
  protected static final String _202 = "<span style=\"color: red; text-decoration: line-through; ";
  protected static final String _203 = "<span style=\"font-size:100%;\">";
  protected static final String _204 = "<span style=\"margin-left: 1em;\" class=\"nowrap\">Filter Pattern: <input id=\"subset\" type=\"text\" oninput=\"match('subset');\"> <span style=\"color: firebrick;\" id=\"subset-error\"></span></input></span><br/>";
  protected static final String _205 = "<span style=\"margin-left: 1em;\">";
  protected static final String _206 = "<style>";
  protected static final String _207 = "<title>";
  protected static final String _208 = "<tt style=\"float: left;\" class=\"orange\">&#xbb;</tt>";
  protected static final String _209 = "<tt style=\"float: right;\" class=\"orange\">&#xab;</tt>";
  protected static final String _210 = "<ul class=\"font-smaller\" id=\"";
  protected static final String _211 = "<ul class=\"font-smaller\" style=\"list-style-type: none; display:none; margin-left: -3em;\" id=\"";
  protected static final String _212 = "<ul id=\"";
  protected static final String _213 = "<ul id=\"categories\" style=\"display: ";
  protected static final String _214 = "<ul id=\"certificates\" style=\"display:";
  protected static final String _215 = "<ul id=\"feature_providers\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _216 = "<ul id=\"features\" style=\"display: none; list-style-type: none; margin-left: -2em;\">";
  protected static final String _217 = "<ul id=\"leftnav\" class=\"ul-left-nav fa-ul hidden-print\">";
  protected static final String _218 = "<ul id=\"licenses\" style=\"display: ";
  protected static final String _219 = "<ul id=\"pgpKeys\" style=\"display:";
  protected static final String _220 = "<ul id=\"products\" style=\"display: ";
  protected static final String _221 = "<ul style=\"display:none; list-style-type: none; padding: 0; margin: 0;\" id=\"__";
  protected static final String _222 = "<ul style=\"list-style-type: none; display:none; padding: 0; margin: 0; margin-left: 2em;\" id=\"_f";
  protected static final String _223 = "<ul style=\"list-style-type: none; padding: 0; margin-left: 1em;\">";
  protected static final String _224 = "<ul>";
  protected static final String _225 = "=</span>";
  protected static final String _226 = ">";
  protected static final String _227 = "><img class=\"fit-image\" src=\"";
  protected static final String _228 = "><span style=\"";
  protected static final String _229 = "Categories";
  protected static final String _230 = "Certificates";
  protected static final String _231 = "Content Metadata";
  protected static final String _232 = "Features";
  protected static final String _233 = "Features/Products";
  protected static final String _234 = "In addition to this composite report, reports are also generated for each of the composed children listed in the navigation bar to the left.";
  protected static final String _235 = "Installable Units";
  protected static final String _236 = "Licenses";
  protected static final String _237 = "No Name<br/>";
  protected static final String _238 = "PGP Keys";
  protected static final String _239 = "Products";
  protected static final String _240 = "Providers";
  protected static final String _241 = "Signing Certificates";
  protected static final String _242 = "Signing PGP Keys";
  protected static final String _243 = "This";
  protected static final String _244 = "This is a composite update site.";
  protected static final String _245 = "This is a generated";
  protected static final String _246 = "XML";
  protected static final String _247 = "[<img class=\"fit-image\" src=\"";
  protected static final String _248 = "\"";
  protected static final String _249 = "\" alt=\"\"/>";
  protected static final String _250 = "\" alt=\"\"/><img style=\"margin-top: -2ex;\" class=\"fit-image\" src=\"";
  protected static final String _251 = "\" class=\"bb\" style=\"";
  protected static final String _252 = "\" class=\"iu-li";
  protected static final String _253 = "\" onclick=\"clickOnToggleButton('licenses_arrows'); clickOnToggleButton('__";
  protected static final String _254 = "\" style=\"display: none;\">";
  protected static final String _255 = "\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _256 = "\" style=\"display:none; margin-left: 2em; background-color: ";
  protected static final String _257 = "\" style=\"list-style-type: none; display: none; margin-left: -1em;\">";
  protected static final String _258 = "\" target=\"keyserver\">0x";
  protected static final String _259 = "\" target=\"oomph_wiki\"/>";
  protected static final String _260 = "\" target=\"oomph_wiki\">";
  protected static final String _261 = "\" target=\"report_source\">";
  protected static final String _262 = "\"/>";
  protected static final String _263 = "\"/> ";
  protected static final String _264 = "\"/>]";
  protected static final String _265 = "\">";
  protected static final String _266 = "\">&#x25B7;</button>";
  protected static final String _267 = "\"><img class=\"fit-image\" src=\"";
  protected static final String _268 = "_arrows'); clickOnToggleButton('_";
  protected static final String _269 = "_arrows'); navigateTo('_";
  protected static final String _270 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('";
  protected static final String _271 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('__";
  protected static final String _272 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('";
  protected static final String _273 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_";
  protected static final String _274 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('__";
  protected static final String _275 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_f";
  protected static final String _276 = "background-color: white;";
  protected static final String _277 = "border: 1px solid black;";
  protected static final String _278 = "border: none;";
  protected static final String _279 = "break;";
  protected static final String _280 = "buttonTargets.item(i).innerHTML = '&#x25B7;';";
  protected static final String _281 = "buttonTargets.item(i).innerHTML = '&#x25E2;';";
  protected static final String _282 = "catch (err) {";
  protected static final String _283 = "color: DarkSlateGray;";
  protected static final String _284 = "color: FireBrick;";
  protected static final String _285 = "color: IndianRed;";
  protected static final String _286 = "color: MediumAquaMarine;";
  protected static final String _287 = "color: MediumOrchid;";
  protected static final String _288 = "color: SaddleBrown;";
  protected static final String _289 = "color: SeaGreen;";
  protected static final String _290 = "color: SteelBlue;";
  protected static final String _291 = "color: Teal;";
  protected static final String _292 = "continue;";
  protected static final String _293 = "currentFilter = filter.value;";
  protected static final String _294 = "document.execCommand(\"copy\");";
  protected static final String _295 = "e.click();";
  protected static final String _296 = "e.innerHTML = '&#x25B7;';";
  protected static final String _297 = "e.innerHTML = '&#x25E2;';";
  protected static final String _298 = "e.scrollIntoView();";
  protected static final String _299 = "e.style.display = 'block';";
  protected static final String _300 = "e.style.display = 'inline';";
  protected static final String _301 = "e.style.display = 'inline-block';";
  protected static final String _302 = "e.style.display = 'none';";
  protected static final String _303 = "e.title= 'Collapse All';";
  protected static final String _304 = "e.title= 'Expand All';";
  protected static final String _305 = "em; text-indent: -4em;\">";
  protected static final String _306 = "em;\">";
  protected static final String _307 = "f.innerHTML = '&#x25B7;';";
  protected static final String _308 = "f.innerHTML = '&#x25E2;';";
  protected static final String _309 = "font-family: monospace;";
  protected static final String _310 = "font-size: 125%;";
  protected static final String _311 = "font-size: 75%;\">";
  protected static final String _312 = "font-size: 80%;";
  protected static final String _313 = "font-size: 90%;";
  protected static final String _314 = "for (var i = 0; i < buttonTargets.length; i++) {";
  protected static final String _315 = "for (var i = 0; i < ius.length; i++) {";
  protected static final String _316 = "for (var i = 0; i < spanTargets.length; i++) {";
  protected static final String _317 = "function clickOnButton(id) {";
  protected static final String _318 = "function clickOnToggleButton(id) {";
  protected static final String _319 = "function copyToClipboard(element) {";
  protected static final String _320 = "function expand(id) {";
  protected static final String _321 = "function expand2(self, id) {";
  protected static final String _322 = "function expand3(self, id) {";
  protected static final String _323 = "function expand_collapse(id) {";
  protected static final String _324 = "function expand_collapse_all(base) {";
  protected static final String _325 = "function expand_collapse_inline(id) {";
  protected static final String _326 = "function expand_collapse_inline_block(id) {";
  protected static final String _327 = "function filterIU(className) {";
  protected static final String _328 = "function match(id) {";
  protected static final String _329 = "function navigateTo(id) {";
  protected static final String _330 = "function toggle(id) {";
  protected static final String _331 = "height: 2ex;";
  protected static final String _332 = "if (!targetsArray.includes(iu)) {";
  protected static final String _333 = "if ((matchText == '' || text.match(matchText) != null) && targetsArray.includes(iu)) {";
  protected static final String _334 = "if (count == 0 && message.innerHTML == '') {";
  protected static final String _335 = "if (count == 0) {";
  protected static final String _336 = "if (e.innerHTML == '";
  protected static final String _337 = "if (e.innerHTML == '\\u25E2') {";
  protected static final String _338 = "if (e.style.display == 'none'){";
  protected static final String _339 = "if (e.title == 'Expand All') {";
  protected static final String _340 = "if (f != null) {";
  protected static final String _341 = "if (f !=null) {";
  protected static final String _342 = "if (filter != null && filter.value != 'all') {";
  protected static final String _343 = "if (matchText != '' && iu.textContent.match(matchText) == null) {";
  protected static final String _344 = "if (t.title != 'Collapse All'){";
  protected static final String _345 = "if (t.title == 'Collapse All'){";
  protected static final String _346 = "iu.style.display = 'block';";
  protected static final String _347 = "iu.style.display = 'none';";
  protected static final String _348 = "margin-bottom: -2ex;";
  protected static final String _349 = "margin-left: 0em;";
  protected static final String _350 = "margin-top: -2ex;";
  protected static final String _351 = "margin: 0px 0px 0px 0px;";
  protected static final String _352 = "message.innerHTML = '';";
  protected static final String _353 = "message.innerHTML = 'No matches';";
  protected static final String _354 = "message.innerHTML = \"\";";
  protected static final String _355 = "message.innerHTML = errMessage;";
  protected static final String _356 = "padding: -2pt -2pt -2pt -2pt;";
  protected static final String _357 = "padding: 0px 0px;";
  protected static final String _358 = "report is produced by <a href=\"";
  protected static final String _359 = "s";
  protected static final String _360 = "span:target {";
  protected static final String _361 = "spanTargets.item(i).style.display = 'inline-block';";
  protected static final String _362 = "spanTargets.item(i).style.display = 'none';";
  protected static final String _363 = "try";
  protected static final String _364 = "try {";
  protected static final String _365 = "var $temp = $(\"<input>\");";
  protected static final String _366 = "var buttonTargets = document.getElementsByClassName(base + '_button');";
  protected static final String _367 = "var count = 0;";
  protected static final String _368 = "var currentFilter = 'iu-li';";
  protected static final String _369 = "var e = document.getElementById('subset');";
  protected static final String _370 = "var e = document.getElementById(base);";
  protected static final String _371 = "var e = document.getElementById(id);";
  protected static final String _372 = "var errMessage = err.message;";
  protected static final String _373 = "var f = document.getElementById(id+\"_arrows\");";
  protected static final String _374 = "var filter = document.querySelector('input[name=\"filter\"]:checked');";
  protected static final String _375 = "var iu = ius[i];";
  protected static final String _376 = "var ius = document.getElementsByClassName('iu-li');";
  protected static final String _377 = "var matchText = e.value;";
  protected static final String _378 = "var message = document.getElementById('subset-error');";
  protected static final String _379 = "var message = document.getElementById(id + '-error');";
  protected static final String _380 = "var spanTargets = document.getElementsByClassName(base);";
  protected static final String _381 = "var state = e.innerHTML;";
  protected static final String _382 = "var t = document.getElementById('all');";
  protected static final String _383 = "var t = document.getElementById(self);";
  protected static final String _384 = "var targets = document.getElementsByClassName(className);";
  protected static final String _385 = "var targets = document.getElementsByClassName(currentFilter);";
  protected static final String _386 = "var targetsArray = [].slice.call(targets);";
  protected static final String _387 = "var text = iu.textContent;";
  protected static final String _388 = "white-space: nowrap;";
  protected static final String _389 = "white-space: pre;";
  protected static final String _390 = "width: 2ex;";
  protected static final String _391 = "{";
  protected static final String _392 = "}";
  protected static final String _393 = "} else {";
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
  protected final String _394 = _52 + NL + _142 + NL + _140 + NL_1 + _172 + NL_1 + _207;
  protected final String _395 = _79 + NL_1 + _174 + NL_1 + _173 + NL_1 + _168 + NL_1 + _170 + NL_1 + _169 + NL_1 + _184;
  protected final String _396 = NL_1 + _206 + NL + NL + _31 + NL_1 + _388 + NL + _392 + NL + NL + _28 + NL_1 + _313 + NL + _392 + NL + NL + _27 + NL_1 + _390 + NL_1 + _331 + NL + _392 + NL + NL + _360 + NL_1 + _310 + NL_1 + _277 + NL + _392 + NL + NL + _40 + NL_1 + _389 + NL_1 + _278 + NL_1 + _357 + NL_1 + _350 + NL_1 + _348 + NL_1 + _349 + NL + _392 + NL + NL + _24 + NL_1 + _284 + NL_1 + _313 + NL + _392 + NL + NL + _39 + NL_1 + _389 + NL_1 + _278 + NL_1 + _357 + NL_1 + _350 + NL_1 + _348 + NL_1 + _349 + NL + _392 + NL + NL + _41 + NL_1 + _290 + NL_1 + _309 + NL_1 + _312 + NL + _392 + NL + NL + _35 + NL_1 + _286 + NL_1 + _309 + NL_1 + _312 + NL + _392 + NL + NL + _37 + NL_1 + _287 + NL_1 + _309 + NL_1 + _312 + NL + _392 + NL + NL + _36 + NL_1 + _283 + NL_1 + _313 + NL + _392 + NL + NL + _38 + NL_1 + _288 + NL_1 + _313 + NL + _392 + NL + NL + _25 + NL_1 + _276 + NL_1 + _278 + NL_1 + _357 + NL + _392 + NL + NL + _26 + NL_1 + _276 + NL + _392 + NL + NL + _29 + NL_1 + _356 + NL_1 + _351 + NL + _392 + NL + NL + _30 + NL_1 + _289 + NL_1 + _313 + NL + _392 + NL + NL + _34 + NL_1 + _291 + NL_1 + _313 + NL + _392 + NL + NL + _33 + NL_1 + _313 + NL + _392 + NL + NL + _32 + NL_1 + _285 + NL_1 + _313 + NL + _392 + NL_1 + _78 + NL + _66 + NL + NL_1 + _92;
  protected final String _397 = NL_2 + _185 + NL + NL_4 + _328 + NL_5 + _374 + NL_5 + _368 + NL_5 + _342 + NL_7 + _293 + NL_5 + _392 + NL_5 + _371 + NL_5 + _379 + NL_5 + _377 + NL_5 + _376 + NL_5 + _385 + NL_5 + _386 + NL_5 + _367 + NL_5 + _315 + NL_7 + _375 + NL_7 + _387 + NL_7 + _364 + NL_9 + _333 + NL_11 + _346 + NL_11 + _22 + NL_9 + _393 + NL_11 + _347 + NL_9 + _392 + NL_9 + _354 + NL_7 + _392 + NL_7 + _282 + NL_9 + _372 + NL_9 + _355 + NL_9 + _279 + NL_7 + _392 + NL_5 + _392 + NL_5 + _334 + NL_9 + _353 + NL_5 + _392 + NL_4 + _392 + NL + NL_4 + _327 + NL_5 + _369 + NL_5 + _377 + NL_5 + _367 + NL_5 + _376 + NL_5 + _384 + NL_5 + _386 + NL_5 + _315 + NL_7 + _375 + NL_7 + _332 + NL_9 + _347 + NL_7 + _393 + NL_9 + _363 + NL_9 + _391 + NL_11 + _343 + NL_13 + _347 + NL_13 + _292 + NL_11 + _392 + NL_9 + _392 + NL_9 + _282 + NL_9 + _392 + NL_9 + _346 + NL_9 + _22 + NL_7 + _392 + NL_5 + _392 + NL_5 + _378 + NL_5 + _335 + NL_9 + _353 + NL_5 + _393 + NL_9 + _352 + NL_5 + _392 + NL_4 + _392 + NL + NL_4 + _319 + NL_5 + _365 + NL_5 + _11 + NL_5 + _13 + NL_5 + _294 + NL_5 + _12 + NL_4 + _392 + NL + NL_4 + _317 + NL_5 + _371 + NL_5 + _295 + NL_4 + _392 + NL + NL_4 + _318 + NL_5 + _371 + NL_5 + _381 + NL_5 + _336;
  protected final String _398 = _17 + NL_7 + _295 + NL_5 + _392 + NL_4 + _392 + NL + NL_4 + _329 + NL_5 + _371 + NL_5 + _298 + NL_4 + _392 + NL + NL_4 + _330 + NL_5 + _371 + NL_5 + _339 + NL_7 + _303 + NL_7 + _297 + NL_5 + _393 + NL_7 + _304 + NL_7 + _296 + NL_5 + _392 + NL_4 + _392 + NL + NL_4 + _321 + NL_5 + _383 + NL_5 + _371 + NL_5 + _373 + NL_5 + _345 + NL_7 + _299 + NL_7 + _308 + NL_5 + _393 + NL_7 + _302 + NL_7 + _307 + NL_5 + _392 + NL_4 + _392 + NL + NL_4 + _322 + NL_5 + _383 + NL_5 + _371 + NL_5 + _373 + NL_5 + _344 + NL_7 + _302 + NL_7 + _307 + NL_5 + _392 + NL_4 + _392 + NL + NL_4 + _320 + NL_5 + _382 + NL_5 + _371 + NL_5 + _373 + NL_5 + _345 + NL_7 + _299 + NL_7 + _308 + NL_5 + _393 + NL_7 + _302 + NL_7 + _307 + NL_5 + _392 + NL_4 + _392 + NL + NL_4 + _323 + NL_5 + _371 + NL_5 + _373 + NL_5 + _338 + NL_7 + _299 + NL_7 + _308 + NL_5 + _393 + NL_7 + _302 + NL_7 + _307 + NL_5 + _392 + NL_4 + _392 + NL + NL_4 + _325 + NL_5 + _371 + NL_5 + _373 + NL_5 + _338 + NL_7 + _300 + NL_7 + _340 + NL_9 + _308 + NL_7 + _392 + NL_5 + _393 + NL_7 + _302 + NL_7 + _341 + NL_9 + _307 + NL_7 + _392 + NL_5 + _392 + NL_4 + _392 + NL + NL_4 + _326 + NL_5 + _371 + NL_5 + _373 + NL_5 + _338 + NL_7 + _301 + NL_7 + _340 + NL_9 + _308 + NL_7 + _392 + NL_5 + _393 + NL_7 + _302 + NL_7 + _341 + NL_9 + _307 + NL_7 + _392 + NL_5 + _392 + NL_4 + _392 + NL + NL_4 + _324 + NL_5 + _370 + NL_5 + _366 + NL_5 + _380 + NL_5 + _337 + NL_9 + _296 + NL_9 + _314 + NL_11 + _280 + NL_9 + _392 + NL_9 + _316 + NL_11 + _362 + NL_9 + _392 + NL_5 + _393 + NL_9 + _297 + NL_9 + _314 + NL_11 + _281 + NL_9 + _392 + NL_9 + _316 + NL_11 + _361 + NL_9 + _392 + NL_5 + _392 + NL_4 + _392 + NL + NL_2 + _74 + NL + NL_2 + _141 + NL_4 + _117 + NL_5 + _122 + NL_7 + _120 + NL_9 + _124 + NL_11 + _88 + NL_13 + _149 + NL_11 + _53 + NL_9 + _63 + NL_7 + _63 + NL_5 + _63 + NL_4 + _63 + NL_2 + _67;
  protected final String _399 = NL_2 + _186 + NL_4 + _117 + NL_5 + _134 + NL_5 + _123 + NL_7 + _116 + NL_9 + _175 + NL_11 + _166 + NL_11 + _167;
  protected final String _400 = NL_11 + _157;
  protected final String _401 = NL_11 + _165;
  protected final String _402 = NL_9 + _71 + NL_7 + _63 + NL_5 + _63 + NL_4 + _63 + NL_2 + _75 + NL + NL_2 + _171 + NL_2 + _121 + NL;
  protected final String _403 = NL_4 + _49 + NL_4 + _90 + NL_5 + _217 + NL_7 + _160 + NL_9 + _84 + NL_7 + _69 + NL_7 + _160 + NL_9 + _83 + NL_7 + _69;
  protected final String _404 = NL_7 + _160 + NL_9 + _81;
  protected final String _405 = _53 + NL_7 + _69;
  protected final String _406 = NL_7 + _160 + NL_9 + _209 + NL_9 + _82;
  protected final String _407 = NL_7 + _159 + NL_9 + _208;
  protected final String _408 = NL_9 + _209;
  protected final String _409 = NL_9 + _85;
  protected final String _410 = NL_9 + _53 + NL_7 + _69;
  protected final String _411 = NL_5 + _80 + NL_4 + _59 + NL + NL_4 + _128 + NL_5 + _129;
  protected final String _412 = NL_7 + _133;
  protected final String _413 = NL_7 + _133 + NL_9 + _148;
  protected final String _414 = NL_9 + _237;
  protected final String _415 = NL_7 + _64;
  protected final String _416 = NL_7 + _176;
  protected final String _417 = NL_9 + _91;
  protected final String _418 = NL_9 + _94;
  protected final String _419 = _60 + NL_7 + _72;
  protected final String _420 = NL_7 + _177 + NL_9 + _115 + NL_9 + _190;
  protected final String _421 = _76 + NL_7 + _72 + NL_7 + _93;
  protected final String _422 = NL_7 + _89;
  protected final String _423 = _259 + NL_9 + _150;
  protected final String _424 = _249 + NL_7 + _53;
  protected final String _425 = NL_8 + _178 + NL_10 + _245 + NL_10 + _87 + NL_12 + _147;
  protected final String _426 = _55 + NL_8 + _72 + NL_8 + _181;
  protected final String _427 = NL_8 + _180 + NL_8 + _224;
  protected final String _428 = NL_10 + _165;
  protected final String _429 = NL_8 + _80;
  protected final String _430 = NL_8 + _178 + NL_10 + _243 + NL_10 + _86;
  protected final String _431 = _260 + NL_12 + _147;
  protected final String _432 = _53 + NL_10 + _358;
  protected final String _433 = _56 + NL_8 + _72;
  protected final String _434 = NL_8 + _139 + NL_8 + _178;
  protected final String _435 = NL_8 + _137 + NL_8 + _178;
  protected final String _436 = NL_8 + _135 + NL_10 + _238 + NL_8 + _65;
  protected final String _437 = NL_8 + _119 + NL_10 + _145;
  protected final String _438 = _262 + NL_10 + _189;
  protected final String _439 = _76 + NL_8 + _63;
  protected final String _440 = NL_8 + _135 + NL_10 + _230;
  protected final String _441 = NL_8 + _197;
  protected final String _442 = NL_8 + _65;
  protected final String _443 = NL_10 + _132 + NL_12 + _148;
  protected final String _444 = _262 + NL_12 + _201 + NL_10 + _63;
  protected final String _445 = NL_10 + _118;
  protected final String _446 = _306 + NL_12 + _145;
  protected final String _447 = NL_12 + _188;
  protected final String _448 = NL_10 + _63;
  protected final String _449 = NL_8 + _136;
  protected final String _450 = NL_8 + _178;
  protected final String _451 = NL_10 + _148;
  protected final String _452 = _262 + NL_10 + _194;
  protected final String _453 = _76 + NL_10 + _86;
  protected final String _454 = NL_10 + _93 + NL_10 + _199;
  protected final String _455 = NL_8 + _72;
  protected final String _456 = NL_6 + _138;
  protected final String _457 = NL_12 + _97;
  protected final String _458 = _16 + NL_12 + _148;
  protected final String _459 = _262 + NL_12 + _192;
  protected final String _460 = NL_12 + _183;
  protected final String _461 = NL_8 + _135 + NL_10 + _231 + NL_10 + _114 + NL_8 + _65 + NL_8 + _125;
  protected final String _462 = NL + _51;
  protected final String _463 = NL_8 + _63;
  protected final String _464 = NL_10 + _244;
  protected final String _465 = NL_10 + _234 + NL_8 + _72;
  protected final String _466 = NL_8 + _135 + NL_8 + _113 + NL_8 + _147;
  protected final String _467 = _262 + NL_8 + _246;
  protected final String _468 = NL_8 + _198;
  protected final String _469 = NL_8 + _65 + NL_8 + _126;
  protected final String _470 = NL_8 + _135 + NL_8 + _108 + NL_8 + _147;
  protected final String _471 = _262 + NL_8 + _236 + NL_8 + _194;
  protected final String _472 = NL_8 + _107;
  protected final String _473 = NL_8 + _65 + NL_8 + _218;
  protected final String _474 = NL_10 + _164 + NL_12 + _97;
  protected final String _475 = _16 + NL_12 + _147;
  protected final String _476 = _76 + NL_12 + _23 + NL_12 + _194;
  protected final String _477 = _76 + NL_12 + _221;
  protected final String _478 = _265 + NL_14 + _163 + NL_15 + _96;
  protected final String _479 = _93 + NL_15 + _182;
  protected final String _480 = _73 + NL_14 + _69 + NL_14 + _163 + NL_15 + _98;
  protected final String _481 = _16 + NL_15 + _233 + NL_15 + _222;
  protected final String _482 = NL_16 + _158 + NL_18 + _86;
  protected final String _483 = _265 + NL_19 + _147;
  protected final String _484 = NL_19 + _196;
  protected final String _485 = _76 + NL_18 + _53 + NL_16 + _69;
  protected final String _486 = NL_15 + _80 + NL_14 + _69 + NL_12 + _80 + NL_10 + _69;
  protected final String _487 = NL_8 + _179 + NL_8 + _135 + NL_8 + _110 + NL_8 + _147;
  protected final String _488 = _262 + NL_8 + _242 + NL_8 + _194;
  protected final String _489 = NL_8 + _109;
  protected final String _490 = NL_8 + _65 + NL_8 + _219;
  protected final String _491 = NL_8 + _164 + NL_10 + _118;
  protected final String _492 = _305 + NL_12 + _95;
  protected final String _493 = _16 + NL_12 + _144;
  protected final String _494 = _262 + NL_12 + _189;
  protected final String _495 = _76 + NL_12 + _193;
  protected final String _496 = _21 + NL_10 + _63 + NL_10 + _211;
  protected final String _497 = NL_12 + _164 + NL_14 + _86;
  protected final String _498 = _53 + NL_12 + _69;
  protected final String _499 = NL_10 + _80 + NL_8 + _69;
  protected final String _500 = NL_8 + _179 + NL_8 + _135 + NL_8 + _102 + NL_8 + _147;
  protected final String _501 = _262 + NL_8 + _241 + NL_8 + _194;
  protected final String _502 = NL_8 + _101;
  protected final String _503 = NL_8 + _65 + NL_8 + _214;
  protected final String _504 = NL_8 + _164;
  protected final String _505 = NL_10 + _131 + NL_12 + _95;
  protected final String _506 = _262 + NL_12 + _200;
  protected final String _507 = _20 + NL_10 + _63;
  protected final String _508 = NL_12 + _95;
  protected final String _509 = NL_12 + _14;
  protected final String _510 = NL_12 + _144;
  protected final String _511 = NL_12 + _193;
  protected final String _512 = NL_12 + _191;
  protected final String _513 = NL_10 + _211;
  protected final String _514 = NL + _50;
  protected final String _515 = NL_8 + _179 + NL_8 + _135 + NL_8 + _103 + NL_8 + _147;
  protected final String _516 = _262 + NL_8 + _240 + NL_8 + _194;
  protected final String _517 = NL_8 + _65 + NL_8 + _215;
  protected final String _518 = NL_10 + _164 + NL_12 + _95;
  protected final String _519 = NL_12 + _143;
  protected final String _520 = NL_12 + _187;
  protected final String _521 = _76 + NL_12 + _194;
  protected final String _522 = _76 + NL_12 + _212;
  protected final String _523 = NL_14 + _164 + NL_15 + _86;
  protected final String _524 = _265 + NL_17 + _143;
  protected final String _525 = NL_17 + _196;
  protected final String _526 = _76 + NL_15 + _53 + NL_14 + _69;
  protected final String _527 = NL_12 + _80 + NL_10 + _69;
  protected final String _528 = NL_8 + _179 + NL_8 + _135 + NL_8 + _104 + NL_8 + _147;
  protected final String _529 = _262 + NL_8 + _232 + NL_8 + _194;
  protected final String _530 = NL_8 + _65 + NL_8 + _216;
  protected final String _531 = NL_10 + _162 + NL_13 + _86;
  protected final String _532 = _265 + NL_14 + _147;
  protected final String _533 = NL_14 + _196;
  protected final String _534 = _76 + NL_12 + _53;
  protected final String _535 = NL_12 + _106;
  protected final String _536 = NL_10 + _69;
  protected final String _537 = NL_8 + _179 + NL_8 + _135 + NL_8 + _112 + NL_8 + _146;
  protected final String _538 = _262 + NL_8 + _239 + NL_8 + _194;
  protected final String _539 = NL_8 + _111;
  protected final String _540 = NL_8 + _65 + NL_8 + _220;
  protected final String _541 = NL_10 + _162;
  protected final String _542 = NL_12 + _86;
  protected final String _543 = NL_12 + _210;
  protected final String _544 = _265 + NL_17 + _147;
  protected final String _545 = NL_12 + _80;
  protected final String _546 = NL_8 + _179 + NL_8 + _135 + NL_8 + _100 + NL_8 + _147;
  protected final String _547 = _262 + NL_8 + _229 + NL_8 + _194;
  protected final String _548 = NL_8 + _99;
  protected final String _549 = NL_8 + _65 + NL_8 + _213;
  protected final String _550 = NL_12 + _53;
  protected final String _551 = NL_8 + _135 + NL_8 + _105 + NL_8 + _147;
  protected final String _552 = _262 + NL_8 + _235 + NL_8 + _194;
  protected final String _553 = NL_8 + _65 + NL_8 + _127 + NL_10 + _204;
  protected final String _554 = NL_10 + _205 + NL_12 + _155;
  protected final String _555 = NL_12 + _151;
  protected final String _556 = NL_12 + _156;
  protected final String _557 = NL_12 + _152;
  protected final String _558 = NL_12 + _153;
  protected final String _559 = NL_12 + _154;
  protected final String _560 = NL_10 + _76;
  protected final String _561 = NL_10 + _223;
  protected final String _562 = NL_12 + _161;
  protected final String _563 = _8 + NL_14 + _86;
  protected final String _564 = _265 + NL_15 + _147;
  protected final String _565 = _262 + NL_15 + _203;
  protected final String _566 = _76 + NL_15 + _195;
  protected final String _567 = _76 + NL_14 + _53;
  protected final String _568 = NL_14 + _106;
  protected final String _569 = NL_14 + _18;
  protected final String _570 = NL_15 + _147;
  protected final String _571 = _262 + NL_15 + _194;
  protected final String _572 = _76 + NL_14 + _19;
  protected final String _573 = NL_14 + _247;
  protected final String _574 = NL_12 + _69;
  protected final String _575 = NL_10 + _80 + NL_8 + _63 + NL_8 + _130 + NL_8 + _63;
  protected final String _576 = NL_5 + _63 + NL_4 + _63 + NL_3 + _63 + NL_3 + _70 + NL_1 + _61 + NL + _68;

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
    stringBuffer.append(_394);
    stringBuffer.append(reporter.getTitle());
    stringBuffer.append(_395);
    stringBuffer.append(_396);
    stringBuffer.append(_397);
    stringBuffer.append('\u25B7');
    stringBuffer.append(_398);
    stringBuffer.append(_399);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() == null) {
    stringBuffer.append(_400);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_69);
    } else {
    stringBuffer.append(_401);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_265);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_58);
    }
    }
    stringBuffer.append(_402);
    stringBuffer.append(_403);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() != null) {
    stringBuffer.append(_404);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_265);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_405);
    } else {
      if (iuReport == null) {
    stringBuffer.append(_406);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_405);
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
    stringBuffer.append(_407);
    if (index != -1) {
    stringBuffer.append(_408);
    }
    stringBuffer.append(_409);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_265);
    stringBuffer.append(NL_9);
    stringBuffer.append(label);
    stringBuffer.append(_410);
    }
    }
    stringBuffer.append(_411);
    if (iuReport == null) {
    stringBuffer.append(_412);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_64);
    } else {
    stringBuffer.append(_413);
    stringBuffer.append(report.getIUImage(iuReport.getIU()));
    stringBuffer.append(_262);
    String name = report.getName(iuReport.getIU(), false);
    if (name != null) {
    stringBuffer.append(NL_9);
    stringBuffer.append(helper.htmlEscape(name, true));
    stringBuffer.append(_93);
    } else {
    stringBuffer.append(_414);
    }
    stringBuffer.append(NL_9);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_415);
    }
    stringBuffer.append(_416);
    if (report != null && report.getDate() != null) {
    stringBuffer.append(_417);
    stringBuffer.append(report.getDate());
    stringBuffer.append(_60);
    }
    stringBuffer.append(_418);
    stringBuffer.append(reporter.getNow());
    stringBuffer.append(_419);
    if (report != null && !report.getSiteURL().startsWith("file:")) {
    stringBuffer.append(_420);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_421);
    }
    stringBuffer.append(_422);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_423);
    stringBuffer.append(reporter.getReportBrandingImage());
    stringBuffer.append(_250);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_424);
    if (indexReport != null) {
    stringBuffer.append(_425);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_262);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_426);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_261);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_57);
    Map<String, String> allReports = indexReport.getAllReports();
    if (allReports != null && !allReports.isEmpty()) {
    stringBuffer.append(_427);
    for (Map.Entry<String, String> entry : allReports.entrySet()) {
    stringBuffer.append(_428);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_265);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_58);
    }
    stringBuffer.append(_429);
    }
    } else if (iuReport != null) {
    stringBuffer.append(_430);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_431);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_262);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_432);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_261);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_433);
    String provider = iuReport.getProvider();
    if (provider != null) {
    stringBuffer.append(_434);
    stringBuffer.append(helper.htmlEscape(provider, false));
    stringBuffer.append(_72);
    }
    String description = iuReport.getDescription();
    if (description != null) {
    stringBuffer.append(_435);
    stringBuffer.append(helper.htmlEscape(description, false));
    stringBuffer.append(_72);
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iuReport.getIU());
    if (!artifacts.isEmpty()) {
      Set<PGPPublicKey> pgpKeys = report.getPGPKeys(iuReport.getIU());
      if (!pgpKeys.isEmpty()) {
    stringBuffer.append(_436);
    for (PGPPublicKey pgpPublicKey : pgpKeys) {
          String fingerPrint = PGPPublicKeyService.toHexFingerprint(pgpPublicKey);
          String uid = report.getUID(pgpPublicKey);
    stringBuffer.append(_437);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_438);
    stringBuffer.append(report.getKeyServerURL(pgpPublicKey));
    stringBuffer.append(_258);
    stringBuffer.append(fingerPrint);
    stringBuffer.append(_54);
    stringBuffer.append(uid);
    stringBuffer.append(_439);
    }
      }
      Set<List<Certificate>> allCertificates = report.getCertificates(iuReport.getIU());
      if (!allCertificates.isEmpty()) {
        Set<Certificate> expiredCertificates = new HashSet<Certificate>();
        int invalidSignatureCount = 0;
        Map<List<Certificate>, Map<String, IInstallableUnit>> allInvalidSignatures = report.getInvalidSignatures();
        for (List<Certificate> certificates : allCertificates) {
          Map<String, IInstallableUnit> invalidSignatures = allInvalidSignatures.get(certificates);
          if (invalidSignatures != null && invalidSignatures.values().contains(iuReport.getIU())) {
            ++invalidSignatureCount;
          }
          for (Certificate certificate : certificates) {
            if (report.isExpired(certificate)) {
              expiredCertificates.add(certificate);
            }
          }
        }
    stringBuffer.append(_440);
    if (invalidSignatureCount > 0) {
    stringBuffer.append(_441);
    stringBuffer.append(invalidSignatureCount);
    stringBuffer.append(_3);
    if (invalidSignatureCount > 1) {
    stringBuffer.append(_359);
    }
    stringBuffer.append(_21);
    }
    stringBuffer.append(_442);
    for (List<Certificate> certificates : allCertificates) {
    if (certificates.isEmpty()) {
    stringBuffer.append(_443);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_444);
    } else {
            int count = 0;
            for (Certificate certificate : certificates) {
              Map<String, IInstallableUnit> invalidSignatures = allInvalidSignatures.get(certificates);
              String style = invalidSignatures != null && invalidSignatures.values().contains(iuReport.getIU()) ? " text-decoration: line-through;" : "";
              Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_445);
    stringBuffer.append(count++);
    stringBuffer.append(_446);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_262);
    for (Map.Entry<String, String> component : components.entrySet()) {
                String key = component.getKey();
                String value = component.getValue();
                String keyStyle;
                String spanStyle;
                if ("to".equals(key) && expiredCertificates.contains(certificate)) {
                  keyStyle = "color: Firebrick; ";
                  spanStyle = " style='font-weight: bold; color: Firebrick;" + style + "'";
                } else {
                  keyStyle = "color: SteelBlue; ";
                  spanStyle = style.isBlank() ? "" : " style='" + style.trim() + "'";
                }
    stringBuffer.append(_447);
    stringBuffer.append(spanStyle);
    stringBuffer.append(_228);
    stringBuffer.append(keyStyle);
    stringBuffer.append(_311);
    stringBuffer.append(key);
    stringBuffer.append(_225);
    stringBuffer.append(value);
    stringBuffer.append(_76);
    }
    stringBuffer.append(_448);
    }
    }
    }
    }
    stringBuffer.append(_449);
    for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
        String artifact = entry.getKey();
        Boolean signed = entry.getValue();
    stringBuffer.append(_450);
    if (signed != null) {
    stringBuffer.append(_451);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_262);
    }
    stringBuffer.append(_451);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_452);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_453);
    stringBuffer.append(report.getRepositoryURL(artifact) + '/' + artifact);
    stringBuffer.append(_265);
    stringBuffer.append(artifact);
    stringBuffer.append(_53);
    for (String status : report.getArtifactStatus(artifact)) {
    stringBuffer.append(_454);
    stringBuffer.append(status);
    stringBuffer.append(_76);
    }
    stringBuffer.append(_455);
    }
    }
    List<Report.LicenseDetail> licenses = report.getLicenses(iuReport.getIU());
    if (licenses != null) {
    stringBuffer.append(_456);
    for (LicenseDetail license : licenses) {
    String id = license.getUUID();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
    stringBuffer.append(_457);
    stringBuffer.append(id);
    stringBuffer.append(_271);
    stringBuffer.append(id);
    stringBuffer.append(_458);
    stringBuffer.append(report.getLicenseImage());
    stringBuffer.append(_459);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_265);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_76);
    stringBuffer.append(NL_12);
    stringBuffer.append(id);
    stringBuffer.append(_460);
    stringBuffer.append(id);
    stringBuffer.append(_256);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_265);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_202);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_265);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_77);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_265);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_76);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_73);
    }
    }
    stringBuffer.append(_461);
    for (String xml : report.getXML(iuReport.getIU(), Collections.<String, String> emptyMap())) {
    stringBuffer.append(_462);
    stringBuffer.append(xml);
    }
    stringBuffer.append(_463);
    } else {
    stringBuffer.append(_430);
    stringBuffer.append(report.getHelpLink());
    stringBuffer.append(_431);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_262);
    stringBuffer.append(NL_12);
    stringBuffer.append(report.getHelpText());
    stringBuffer.append(_432);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_261);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_433);
    List<Report> children = report.getChildren();
    if (!children.isEmpty()) {
    stringBuffer.append(_450);
    if (!report.isRoot()) {
    stringBuffer.append(_464);
    }
    stringBuffer.append(_465);
    }
    String metadataXML = report.getMetadataXML();
    String artifactXML = report.getArtifactML();
    if (metadataXML != null || artifactXML != null) {
    stringBuffer.append(_466);
    stringBuffer.append(report.getRepositoryImage());
    stringBuffer.append(_467);
    if (metadataXML != null && metadataXML.contains("bad-absolute-location") || artifactXML != null && artifactXML.contains("bad-absolute-location")) {
    stringBuffer.append(_468);
    }
    stringBuffer.append(_469);
    if (metadataXML != null) {
    stringBuffer.append(_462);
    stringBuffer.append(metadataXML);
    }
    if (artifactXML != null) {
    if (metadataXML != null) {
    stringBuffer.append(NL);
    }
    stringBuffer.append(_462);
    stringBuffer.append(artifactXML);
    }
    stringBuffer.append(_463);
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
    stringBuffer.append(_470);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_471);
    stringBuffer.append(licenses.size());
    stringBuffer.append(_76);
    if (nonConformant != 0) {
    stringBuffer.append(_441);
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
    stringBuffer.append(_472);
    stringBuffer.append(displayButton);
    stringBuffer.append(_46);
    stringBuffer.append(onClick);
    stringBuffer.append(_266);
    }
    stringBuffer.append(_473);
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
    stringBuffer.append(_474);
    stringBuffer.append(id);
    stringBuffer.append(_274);
    stringBuffer.append(id);
    stringBuffer.append(_475);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_459);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_265);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_476);
    stringBuffer.append(ius.size());
    stringBuffer.append(_477);
    stringBuffer.append(id);
    stringBuffer.append(_478);
    stringBuffer.append(id);
    stringBuffer.append(_273);
    stringBuffer.append(id);
    stringBuffer.append(_16);
    stringBuffer.append(NL_15);
    stringBuffer.append(id);
    stringBuffer.append(_479);
    stringBuffer.append(id);
    stringBuffer.append(_256);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_265);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_202);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_265);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_77);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_265);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_76);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_480);
    stringBuffer.append(id);
    stringBuffer.append(_275);
    stringBuffer.append(id);
    stringBuffer.append(_481);
    stringBuffer.append(id);
    stringBuffer.append(_265);
    for (IInstallableUnit iu : ius) {
    stringBuffer.append(_482);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_483);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_262);
    stringBuffer.append(NL_19);
    stringBuffer.append(helper.htmlEscape(report.getName(iu, true), true));
    stringBuffer.append(_484);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_485);
    }
    stringBuffer.append(_486);
    }
    }
    stringBuffer.append(_429);
    }
    Map<PGPPublicKey, Map<String, IInstallableUnit>> pgpKeys = report.getPGPKeys();
    if (!pgpKeys.isEmpty()) {
      int idCount = 0;
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_487);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_488);
    stringBuffer.append(pgpKeys.size());
    stringBuffer.append(_76);
    {
        StringBuilder onClick = new StringBuilder();
        for (int i = 0, size = pgpKeys.size(); i < size; ++i) {
          String id = "pgpKeys" + ++idCount;
          onClick.append("expand2('pgpKeys_all_arrows', '").append(id).append("');");
        }
        idCount = 0;
    stringBuffer.append(_489);
    stringBuffer.append(displayButton);
    stringBuffer.append(_47);
    stringBuffer.append(onClick);
    stringBuffer.append(_266);
    }
    stringBuffer.append(_490);
    stringBuffer.append(display);
    stringBuffer.append(_43);
    for (Map.Entry<PGPPublicKey, Map<String, IInstallableUnit>> entry : pgpKeys.entrySet()) {
        PGPPublicKey pgpPublicKey = entry.getKey();
        String fingerPrint = PGPPublicKeyService.toHexFingerprint(pgpPublicKey);
        String uid = report.getUID(pgpPublicKey);
        String id = "pgpKeys" + ++idCount;
    stringBuffer.append(_491);
    stringBuffer.append(2);
    stringBuffer.append(_492);
    stringBuffer.append(id);
    stringBuffer.append(_272);
    stringBuffer.append(id);
    stringBuffer.append(_493);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_494);
    stringBuffer.append(report.getKeyServerURL(pgpPublicKey));
    stringBuffer.append(_258);
    stringBuffer.append(fingerPrint);
    stringBuffer.append(_54);
    stringBuffer.append(uid);
    stringBuffer.append(_495);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_496);
    stringBuffer.append(id);
    stringBuffer.append(_265);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_497);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_267);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_263);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_498);
    }
    stringBuffer.append(_499);
    }
    stringBuffer.append(_429);
    }
    Map<List<Certificate>, Map<String, IInstallableUnit>> allCertificates = report.getCertificates();
    Map<List<Certificate>, Map<String, IInstallableUnit>> allInvalidSignatures = report.getInvalidSignatures();
    if (!allCertificates.isEmpty()) {
      int idCount = 0;
      Map<String, IInstallableUnit> unsigned = allCertificates.get(Collections.emptyList());
      int invalidSignatureCount = 0;
      for (Map<String, IInstallableUnit> invalidSignatures : allInvalidSignatures.values()) {
        invalidSignatureCount += invalidSignatures.size();
      }
      Set<Certificate> expiredCertificates = new HashSet<Certificate>();
      for (List<Certificate> list : allCertificates.keySet()) {
        for (Certificate certificate : list) {
          if (report.isExpired(certificate)) {
            expiredCertificates.add(certificate);
          }
        }
      }
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_500);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_501);
    stringBuffer.append(allCertificates.size());
    stringBuffer.append(_76);
    if (unsigned != null) {
    stringBuffer.append(_441);
    stringBuffer.append(unsigned.size());
    stringBuffer.append(_7);
    }
    if (invalidSignatureCount > 0) {
    stringBuffer.append(_441);
    stringBuffer.append(invalidSignatureCount);
    stringBuffer.append(_4);
    }
    {
        StringBuilder onClick = new StringBuilder();
        for (int i = 0, size = allCertificates.size(); i < size; ++i) {
          String id = "certificates" + ++idCount;
          onClick.append("expand2('certificates_all_arrows', '").append(id).append("');");
        }
        idCount = 0;
    stringBuffer.append(_502);
    stringBuffer.append(displayButton);
    stringBuffer.append(_45);
    stringBuffer.append(onClick);
    stringBuffer.append(_266);
    }
    stringBuffer.append(_503);
    stringBuffer.append(display);
    stringBuffer.append(_43);
    for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : allCertificates.entrySet()) {
        List<Certificate> certificates = entry.getKey();
        Map<String, IInstallableUnit> invalidSignatures = allInvalidSignatures.get(certificates);
        String id = "certificates" + ++idCount;
    stringBuffer.append(_504);
    if (certificates.isEmpty()) {
    stringBuffer.append(_505);
    stringBuffer.append(id);
    stringBuffer.append(_272);
    stringBuffer.append(id);
    stringBuffer.append(_475);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_506);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_507);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_445);
    stringBuffer.append(count++ + 2);
    stringBuffer.append(_305);
    if (count == 1) {
    stringBuffer.append(_508);
    stringBuffer.append(id);
    stringBuffer.append(_272);
    stringBuffer.append(id);
    stringBuffer.append(_16);
    } else {
    stringBuffer.append(_509);
    }
    stringBuffer.append(_510);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_262);
    if (count == 1) {
    stringBuffer.append(_511);
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
    stringBuffer.append(_447);
    stringBuffer.append(spanStyle);
    stringBuffer.append(_228);
    stringBuffer.append(keyStyle);
    stringBuffer.append(_311);
    stringBuffer.append(key);
    stringBuffer.append(_225);
    stringBuffer.append(value);
    stringBuffer.append(_76);
    }
    if (count == 1 && !invalidSignatures.isEmpty()) {
    stringBuffer.append(_512);
    stringBuffer.append(invalidSignatures.size());
    stringBuffer.append(_9);
    }
    stringBuffer.append(_448);
    }
    }
    stringBuffer.append(_513);
    stringBuffer.append(id);
    stringBuffer.append(_265);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
          String style = invalidSignatures != null && invalidSignatures.entrySet().contains(artifact) ? "style='text-decoration: line-through;'" : "";
    stringBuffer.append(_497);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_248);
    stringBuffer.append(style);
    stringBuffer.append(_227);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_263);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_498);
    }
    stringBuffer.append(_499);
    }
    stringBuffer.append(_429);
    }
    stringBuffer.append(_514);
    Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
    if (!featureProviders.isEmpty()) {
      int nonEclipse = 0;
      for (String provider : featureProviders.keySet()) {
        if (!provider.toLowerCase().contains("eclipse"))
          ++nonEclipse;
      }
    stringBuffer.append(_515);
    stringBuffer.append(report.getProviderImage());
    stringBuffer.append(_516);
    stringBuffer.append(featureProviders.size());
    stringBuffer.append(_76);
    if (nonEclipse != 0) {
    stringBuffer.append(_441);
    stringBuffer.append(nonEclipse);
    stringBuffer.append(_6);
    }
    stringBuffer.append(_517);
    int count = 0;
      for (Map.Entry<String, Set<IInstallableUnit>> provider : featureProviders.entrySet()) {
        String style = !provider.getKey().toLowerCase().contains("eclipse") ? " style=\"color: FireBrick;\"" : "";
        Set<IInstallableUnit> features = provider.getValue();
        String id = "nested_feature_providers" + ++count;
    stringBuffer.append(_518);
    stringBuffer.append(id);
    stringBuffer.append(_270);
    stringBuffer.append(id);
    stringBuffer.append(_16);
    for (String image : report.getBrandingImages(features)) {
    stringBuffer.append(_519);
    stringBuffer.append(image);
    stringBuffer.append(_262);
    }
    stringBuffer.append(_520);
    stringBuffer.append(style);
    stringBuffer.append(_226);
    stringBuffer.append(provider.getKey());
    stringBuffer.append(_521);
    stringBuffer.append(features.size());
    stringBuffer.append(_522);
    stringBuffer.append(id);
    stringBuffer.append(_255);
    for (IInstallableUnit feature : features) {
          String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_523);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_524);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_262);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_525);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_526);
    }
    stringBuffer.append(_527);
    }
    stringBuffer.append(_429);
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
    stringBuffer.append(_528);
    stringBuffer.append(report.getFeatureImage());
    stringBuffer.append(_529);
    stringBuffer.append(features.size());
    stringBuffer.append(_76);
    if (brokenBranding != 0) {
    stringBuffer.append(_441);
    stringBuffer.append(brokenBranding);
    stringBuffer.append(_1);
    }
    if (noBranding != 0) {
    stringBuffer.append(_441);
    stringBuffer.append(noBranding);
    stringBuffer.append(_5);
    }
    stringBuffer.append(_530);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_531);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_532);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_262);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_533);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_534);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        for (LicenseDetail license : report.getLicenses(feature)) {
          String id = license.getUUID();
          licenseReplacements.put(">" + id, "><button class='bb search-for-me' style='" + helper.getStyle(license) + "' onclick=\"clickOnButton('lic_" + id
              + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_535);
    stringBuffer.append(id);
    stringBuffer.append(_251);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_253);
    stringBuffer.append(id);
    stringBuffer.append(_268);
    stringBuffer.append(id);
    stringBuffer.append(_269);
    stringBuffer.append(id);
    stringBuffer.append(_15);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_62);
    }
    stringBuffer.append(_536);
    }
    stringBuffer.append(_429);
    }
    Collection<IInstallableUnit> products = report.getProducts();
    if (!products.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_537);
    stringBuffer.append(report.getProductImage());
    stringBuffer.append(_538);
    stringBuffer.append(products.size());
    stringBuffer.append(_76);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit product : products) {
          String productID = "_product_" + report.getIUID(product);
          onClick.append("expand2('products_all_arrows', '").append(productID).append("');");
        }
    stringBuffer.append(_539);
    stringBuffer.append(displayButton);
    stringBuffer.append(_48);
    stringBuffer.append(onClick);
    stringBuffer.append(_266);
    }
    stringBuffer.append(_540);
    stringBuffer.append(display);
    stringBuffer.append(_42);
    for (IInstallableUnit product : products) {
        String productImage = report.getIUImage(product);
        String productID = "_product_" + report.getIUID(product);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(product));
    stringBuffer.append(_541);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_508);
    stringBuffer.append(productID);
    stringBuffer.append(_272);
    stringBuffer.append(productID);
    stringBuffer.append(_16);
    }
    stringBuffer.append(_542);
    stringBuffer.append(report.getRelativeIUReportURL(product));
    stringBuffer.append(_532);
    stringBuffer.append(productImage);
    stringBuffer.append(_262);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(product, true), true));
    stringBuffer.append(_533);
    stringBuffer.append(report.getVersion(product));
    stringBuffer.append(_534);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_543);
    stringBuffer.append(productID);
    stringBuffer.append(_254);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_523);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_544);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_262);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_525);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_526);
    }
    stringBuffer.append(_545);
    }
    stringBuffer.append(_536);
    }
    stringBuffer.append(_429);
    }
    Collection<IInstallableUnit> categories = report.getCategories();
    if (!categories.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_546);
    stringBuffer.append(report.getCategoryImage());
    stringBuffer.append(_547);
    stringBuffer.append(categories.size());
    stringBuffer.append(_76);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit category : categories) {
          String categoryID = "_category_" + report.getIUID(category);
          onClick.append("expand2('categories_all_arrows', '").append(categoryID).append("');");
        }
    stringBuffer.append(_548);
    stringBuffer.append(displayButton);
    stringBuffer.append(_44);
    stringBuffer.append(onClick);
    stringBuffer.append(_266);
    }
    stringBuffer.append(_549);
    stringBuffer.append(display);
    stringBuffer.append(_42);
    for (IInstallableUnit category : categories) {
        String categoryImage = report.getIUImage(category);
        String categoryID = "_category_" + report.getIUID(category);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(category));
    stringBuffer.append(_541);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_508);
    stringBuffer.append(categoryID);
    stringBuffer.append(_272);
    stringBuffer.append(categoryID);
    stringBuffer.append(_16);
    }
    stringBuffer.append(_542);
    stringBuffer.append(report.getRelativeIUReportURL(category));
    stringBuffer.append(_532);
    stringBuffer.append(categoryImage);
    stringBuffer.append(_262);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(category, true), true));
    stringBuffer.append(_550);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_543);
    stringBuffer.append(categoryID);
    stringBuffer.append(_257);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_523);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_544);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_262);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_525);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_526);
    }
    stringBuffer.append(_545);
    }
    stringBuffer.append(_536);
    }
    stringBuffer.append(_429);
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
    stringBuffer.append(_551);
    stringBuffer.append(report.getBundleImage());
    stringBuffer.append(_552);
    stringBuffer.append(ius.size());
    stringBuffer.append(_76);
    if (duplicateCount > 0) {
    stringBuffer.append(_441);
    stringBuffer.append(duplicateCount);
    stringBuffer.append(_10);
    }
    stringBuffer.append(_553);
    if (duplicateCount > 0 || !unsignedIUs.isEmpty() || !badProviderIUs.isEmpty() || !badLicenseIUs.isEmpty() || !brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_554);
    if (!badLicenseIUs.isEmpty()) {
    stringBuffer.append(_555);
    }
    if (!unsignedIUs.isEmpty()) {
    stringBuffer.append(_556);
    }
    if (!badProviderIUs.isEmpty()) {
    stringBuffer.append(_557);
    }
    if (!brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_558);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_559);
    }
    stringBuffer.append(_560);
    }
    stringBuffer.append(_561);
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
    stringBuffer.append(_562);
    stringBuffer.append(id);
    stringBuffer.append(_252);
    stringBuffer.append(classNames);
    stringBuffer.append(_563);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_564);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_565);
    stringBuffer.append(iuID);
    stringBuffer.append(_566);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_265);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_567);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        if (report.isFeature(iu)) {
          for (LicenseDetail license : report.getLicenses(iu)) {
            String licenseID = license.getUUID();
            licenseReplacements.put(">" + licenseID, "><button class='bb search-for-me' style='" + helper.getStyle(license)
                + "' onclick=\"clickOnButton('lic_" + licenseID + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_568);
    stringBuffer.append(licenseID);
    stringBuffer.append(_251);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_253);
    stringBuffer.append(licenseID);
    stringBuffer.append(_268);
    stringBuffer.append(licenseID);
    stringBuffer.append(_269);
    stringBuffer.append(licenseID);
    stringBuffer.append(_15);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_62);
    }
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iu);
        for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
          String artifact = entry.getKey();
          Boolean signed = entry.getValue();
    stringBuffer.append(_569);
    if (signed != null) {
    stringBuffer.append(_570);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_262);
    }
    stringBuffer.append(_570);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_571);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_572);
    }
    String brandingImage = report.getBrandingImage(iu);
        if (brandingImage != null) {
    stringBuffer.append(_573);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_264);
    }
    stringBuffer.append(_574);
    }
    stringBuffer.append(_575);
    }
    }
    stringBuffer.append(_576);
    return stringBuffer.toString();
  }
}
