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
  protected static final String _2 = " Inconsistent Signatures)</span>";
  protected static final String _3 = " Invalid Licenses)</span>";
  protected static final String _4 = " Invalid Signature";
  protected static final String _5 = " Invalid Signatures)</span>";
  protected static final String _6 = " No Branding Images)</span>";
  protected static final String _7 = " Not Provided by Eclipse)</span>";
  protected static final String _8 = " Split)</span>";
  protected static final String _9 = " Unsigned Artifacts)</span>";
  protected static final String _10 = " font-smaller\">";
  protected static final String _11 = " invalid Signatures)</span>";
  protected static final String _12 = " with Multiple Versions)</span>";
  protected static final String _13 = "$(\"body\").append($temp);";
  protected static final String _14 = "$temp.remove();";
  protected static final String _15 = "$temp.val($(element).text()).select();";
  protected static final String _16 = "&#x1F511;</a>";
  protected static final String _17 = "&#x1F511;</span>";
  protected static final String _18 = "&#x1F512;</a>";
  protected static final String _19 = "&#x1F512;</span>";
  protected static final String _20 = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
  protected static final String _21 = "');\">";
  protected static final String _22 = "');\">&#x25B7;</button>";
  protected static final String _23 = "'){";
  protected static final String _24 = "(";
  protected static final String _25 = ")";
  protected static final String _26 = ") Unsigned</span>";
  protected static final String _27 = ")</span>";
  protected static final String _28 = "++count;";
  protected static final String _29 = "-";
  protected static final String _30 = ".bad-absolute-location {";
  protected static final String _31 = ".bb {";
  protected static final String _32 = ".filter {";
  protected static final String _33 = ".fit-image {";
  protected static final String _34 = ".font-smaller {";
  protected static final String _35 = ".iu-link {";
  protected static final String _36 = ".resolved-requirement {";
  protected static final String _37 = ".text-nowrap {";
  protected static final String _38 = ".unresolved-requirement {";
  protected static final String _39 = ".unused-capability {";
  protected static final String _40 = ".used-capability {";
  protected static final String _41 = ".xml-attribute {";
  protected static final String _42 = ".xml-attribute-value {";
  protected static final String _43 = ".xml-element {";
  protected static final String _44 = ".xml-element-value {";
  protected static final String _45 = ".xml-iu {";
  protected static final String _46 = ".xml-repo {";
  protected static final String _47 = ".xml-token {";
  protected static final String _48 = "; margin-left: -1em; list-style-type: none; padding: 0; margin: 0;\">";
  protected static final String _49 = "; margin-left: -1em; list-style-type: none;\">";
  protected static final String _50 = ";\" onclick=\"toggle('categories_all_arrows');";
  protected static final String _51 = ";\" onclick=\"toggle('certificates_all_arrows');";
  protected static final String _52 = ";\" onclick=\"toggle('licenses_all_arrows');";
  protected static final String _53 = ";\" onclick=\"toggle('pgpKeys_all_arrows');";
  protected static final String _54 = ";\" onclick=\"toggle('products_all_arrows');";
  protected static final String _55 = "<!-- navigation sidebar -->";
  protected static final String _56 = "<!--- providers -->";
  protected static final String _57 = "<!----------->";
  protected static final String _58 = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
  protected static final String _59 = "</a>";
  protected static final String _60 = "</a> ";
  protected static final String _61 = "</a> report.";
  protected static final String _62 = "</a>.";
  protected static final String _63 = "</a>.</p>";
  protected static final String _64 = "</a></li>";
  protected static final String _65 = "</aside>";
  protected static final String _66 = "</b>";
  protected static final String _67 = "</body>";
  protected static final String _68 = "</button>";
  protected static final String _69 = "</div>";
  protected static final String _70 = "</h2>";
  protected static final String _71 = "</h3>";
  protected static final String _72 = "</head>";
  protected static final String _73 = "</header>";
  protected static final String _74 = "</html>";
  protected static final String _75 = "</li>";
  protected static final String _76 = "</main>";
  protected static final String _77 = "</ol>";
  protected static final String _78 = "</p>";
  protected static final String _79 = "</pre>";
  protected static final String _80 = "</script>";
  protected static final String _81 = "</section>";
  protected static final String _82 = "</span>";
  protected static final String _83 = "</span><span style=\"color: green; text-decoration: underline; ";
  protected static final String _84 = "</style>";
  protected static final String _85 = "</title>";
  protected static final String _86 = "</ul>";
  protected static final String _87 = "<a class=\"separator\" href=\"";
  protected static final String _88 = "<a class=\"separator\" href=\".\">";
  protected static final String _89 = "<a class=\"separator\" href=\"https://www.eclipse.org/downloads\">Downloads</a>";
  protected static final String _90 = "<a class=\"separator\" href=\"https://www.eclipse.org\">Home</a>";
  protected static final String _91 = "<a class=\"separator\" style=\"display: inline-block; margin-left: 0.25em;\" href=\"";
  protected static final String _92 = "<a href=\"";
  protected static final String _93 = "<a href=\"#cert_";
  protected static final String _94 = "<a href=\"#pgp_";
  protected static final String _95 = "<a href=\"https://wiki.eclipse.org/Oomph_Repository_Analyzer#Description\" target=\"oomph_wiki\">";
  protected static final String _96 = "<a href=\"https://www.eclipse.org/\">";
  protected static final String _97 = "<a style=\"float:right; font-size: 200%;\" href=\"";
  protected static final String _98 = "<aside id=\"leftcol\" class=\"col-md-4 font-smaller\">";
  protected static final String _99 = "<b>Built: ";
  protected static final String _100 = "<body id=\"body_solstice\">";
  protected static final String _101 = "<br/>";
  protected static final String _102 = "<br/><b>Reported: ";
  protected static final String _103 = "<button id=\"";
  protected static final String _104 = "<button id=\"_";
  protected static final String _105 = "<button id=\"__";
  protected static final String _106 = "<button id=\"_f";
  protected static final String _107 = "<button id=\"categories_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _108 = "<button id=\"categories_arrows\" class=\"orange bb\" onclick=\"expand_collapse('categories'); expand_collapse_inline('categories_all_arrows');\">&#x25B7;</button>";
  protected static final String _109 = "<button id=\"certificates_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _110 = "<button id=\"certificates_arrows\" class=\"orange bb\" onclick=\"expand_collapse('certificates'); expand_collapse_inline('certificates_all_arrows');\">&#x25B7;</button>";
  protected static final String _111 = "<button id=\"feature_providers_arrows\" class=\"orange bb\" onclick=\"expand_collapse('feature_providers');\">&#x25B7;</button>";
  protected static final String _112 = "<button id=\"features_arrows\" class=\"orange bb\" onclick=\"expand_collapse('features');\">&#x25B7;</button>";
  protected static final String _113 = "<button id=\"ius_arrows\" class=\"orange bb\" onclick=\"expand_collapse('ius'); match('filter', 'subset', 'iu-li');\">&#x25B7;</button>";
  protected static final String _114 = "<button id=\"lic_";
  protected static final String _115 = "<button id=\"licenses_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _116 = "<button id=\"licenses_arrows\" class=\"orange bb\" onclick=\"expand_collapse('licenses'); expand_collapse_inline('licenses_all_arrows');\">&#x25B7;</button>";
  protected static final String _117 = "<button id=\"packages_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: none;\" onclick=\"toggle('packages_all_arrows');";
  protected static final String _118 = "<button id=\"packages_arrows\" class=\"orange bb\" onclick=\"expand_collapse('packages'); match('filter-package', 'package-subset', 'package-li'); expand_collapse_inline('packages_all_arrows');\">&#x25B7;</button>";
  protected static final String _119 = "<button id=\"pgpKeys_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _120 = "<button id=\"pgpKeys_arrows\" class=\"orange bb\" onclick=\"expand_collapse('pgpKeys'); expand_collapse_inline('pgpKeys_all_arrows');\">&#x25B7;</button>";
  protected static final String _121 = "<button id=\"products_all_arrows\" title=\"Expand All\" class=\"orange bb\" style=\"display: ";
  protected static final String _122 = "<button id=\"products_arrows\" class=\"orange bb\" onclick=\"expand_collapse('products'); expand_collapse_inline('products_all_arrows');\">&#x25B7;</button>";
  protected static final String _123 = "<button id=\"repos_arrows\" class=\"orange bb\" onclick=\"expand_collapse('repos');\">&#x25B7;</button>";
  protected static final String _124 = "<button id=\"xml-links\" class=\"orange bb\" onclick=\"expand_collapse_all('xml-links');\">&#x25B7;</button>";
  protected static final String _125 = "<button title=\"Copy to Clipboard\" class=\"orange bb\" style=\"font-size: 150%;\" onclick=\"copyToClipboard('#p1')\">&#x270e;</button>";
  protected static final String _126 = "<div class=\"col-sm-16 padding-left-30\">";
  protected static final String _127 = "<div class=\"container\">";
  protected static final String _128 = "<div class=\"font-smaller\" style=\"margin-left: ";
  protected static final String _129 = "<div class=\"font-smaller\">";
  protected static final String _130 = "<div class=\"hidden-xs col-sm-8 col-md-6 col-lg-5\" id=\"header-left\">";
  protected static final String _131 = "<div class=\"novaContent container\" id=\"novaContent\">";
  protected static final String _132 = "<div class=\"row\" id=\"header-row\">";
  protected static final String _133 = "<div class=\"row\">";
  protected static final String _134 = "<div class=\"wrapper-logo-default\">";
  protected static final String _135 = "<div class=\"xml-iu\" style=\"display:block;\">";
  protected static final String _136 = "<div class=\"xml-repo\" id=\"repos\" style=\"display: none;\">";
  protected static final String _137 = "<div id=\"ius\" style=\"display:none;\">";
  protected static final String _138 = "<div id=\"maincontent\">";
  protected static final String _139 = "<div id=\"midcolumn\" style=\"width: 70%;\">";
  protected static final String _140 = "<div id=\"packages\" style=\"display:none;\">";
  protected static final String _141 = "<div style=\"height: 20ex;\">";
  protected static final String _142 = "<div style=\"text-indent: -2em\">";
  protected static final String _143 = "<div>";
  protected static final String _144 = "<h2 style=\"text-align: center;\">";
  protected static final String _145 = "<h3 class=\"sr-only\">Breadcrumbs</h3>";
  protected static final String _146 = "<h3 style=\"font-weight: bold;\">";
  protected static final String _147 = "<h3 style=\"font-weight: bold;\">Artifacts</h3>";
  protected static final String _148 = "<h3 style=\"font-weight: bold;\">Description</h3>";
  protected static final String _149 = "<h3 style=\"font-weight: bold;\">Licenses</h3>";
  protected static final String _150 = "<h3 style=\"font-weight: bold;\">Provider</h3>";
  protected static final String _151 = "<head>";
  protected static final String _152 = "<header role=\"banner\" id=\"header-wrapper\">";
  protected static final String _153 = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">";
  protected static final String _154 = "<img alt=\"Branding Image\" class=\"fit-image\" src=\"";
  protected static final String _155 = "<img alt=\"Signed\" class=\"fit-image\" src=\"";
  protected static final String _156 = "<img alt=\"Signed\" class=\"fit-image\" src=\"../";
  protected static final String _157 = "<img class=\"fit-image> src=\"";
  protected static final String _158 = "<img class=\"fit-image\" src=\"";
  protected static final String _159 = "<img class=\"fit-image\" src=\"../";
  protected static final String _160 = "<img class=\"logo-eclipse-default img-responsive hidden-xs\" alt=\"Eclipse Log\" src=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/logo/eclipse-426x100.png\"/>";
  protected static final String _161 = "<img src=\"";
  protected static final String _162 = "<input id=\"bad-license\" type=\"radio\" name=\"filter\" value=\"bad-license\" class=\"filter\" onclick=\"filterIU('bad-license', 'iu-li');\"> Bad License </input>";
  protected static final String _163 = "<input id=\"bad-provider\" type=\"radio\" name=\"filter\" value=\"bad-provider\" class=\"filter\" onclick=\"filterIU('bad-provider', 'iu-li');\"> Bad Provider </input>";
  protected static final String _164 = "<input id=\"broken-branding\" type=\"radio\" name=\"filter\" value=\"broken-branding\" class=\"filter\" onclick=\"filterIU('broken-branding', 'iu-li');\"> Broken Branding </input>";
  protected static final String _165 = "<input id=\"duplicates\" type=\"radio\" name=\"filter\" value=\"duplicate\" class=\"filter\" onclick=\"filterIU('duplicate', 'iu-li');\"> Duplicates </input>";
  protected static final String _166 = "<input id=\"filter-package\" type=\"radio\" name=\"filter-package\" value=\"all\" class=\"filter\" onclick=\"filterIU('package-li', 'package-li');\" checked=\"checked\"> All </input>";
  protected static final String _167 = "<input id=\"filter\" type=\"radio\" name=\"filter\" value=\"all\" class=\"filter\" onclick=\"filterIU('iu-li', 'iu-li');\" checked=\"checked\"> All </input>";
  protected static final String _168 = "<input id=\"inconsistent-signatures\" type=\"radio\" name=\"filter-package\" value=\"inconsistent-signatures\" class=\"filter\" onclick=\"filterIU('inconsistent-signatures', 'package-li');\"> Inconsistent Signatures </input>";
  protected static final String _169 = "<input id=\"split-package\" type=\"radio\" name=\"filter-package\" value=\"split-package\" class=\"filter\" onclick=\"filterIU('split-package', 'package-li');\"> Split Packages </input>";
  protected static final String _170 = "<input id=\"unsigned\" type=\"radio\" name=\"filter\" value=\"unsigned\" class=\"filter\" onclick=\"filterIU('unsigned', 'iu-li');\"> Unsigned </input>";
  protected static final String _171 = "<li class=\"active\">";
  protected static final String _172 = "<li class=\"font-smaller text-nowrap\">";
  protected static final String _173 = "<li class=\"separator\" style=\"font-size: 90%;\">";
  protected static final String _174 = "<li class=\"separator\">";
  protected static final String _175 = "<li id=\"_iu_";
  protected static final String _176 = "<li style=\"font-size: 100%; white-space: nowrap;\">";
  protected static final String _177 = "<li style=\"margin-left: 1em;\">";
  protected static final String _178 = "<li>";
  protected static final String _179 = "<li><a href=\"";
  protected static final String _180 = "<li><a href=\"https://www.eclipse.org/\">Home</a></li>";
  protected static final String _181 = "<li><a href=\"https://www.eclipse.org/downloads/\">Downloads</a></li>";
  protected static final String _182 = "<link href=\"//fonts.googleapis.com/css?family=Open+Sans:400,700,300,600,100\" rel=\"stylesheet\" type=\"text/css\"/>";
  protected static final String _183 = "<link rel=\"icon\" type=\"image/ico\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/images/favicon.ico\"/>";
  protected static final String _184 = "<link rel=\"stylesheet\" href=\"https://www.eclipse.org/eclipse.org-common/themes/solstice/public/stylesheets/styles.min.css\"/>";
  protected static final String _185 = "<main class=\"no-promo\">";
  protected static final String _186 = "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\"/>";
  protected static final String _187 = "<meta name=\"description\" content=\"Update Sites Reports\"/>";
  protected static final String _188 = "<meta name=\"keywords\" content=\"eclipse,update site\"/>";
  protected static final String _189 = "<ol class=\"breadcrumb\">";
  protected static final String _190 = "<p style=\"font-size: 125%; text-align: center;\">";
  protected static final String _191 = "<p style=\"text-align: center;\">";
  protected static final String _192 = "<p>";
  protected static final String _193 = "<p></p>";
  protected static final String _194 = "<p>Reports are generated specifically for the following sites:</p>";
  protected static final String _195 = "<p>This report is produced by <a href=\"";
  protected static final String _196 = "<pre id=\"_";
  protected static final String _197 = "<pre id=\"__";
  protected static final String _198 = "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js\"></script>";
  protected static final String _199 = "<script>";
  protected static final String _200 = "<section class=\"hidden-print default-breadcrumbs\" id=\"breadcrumb\">";
  protected static final String _201 = "<span ";
  protected static final String _202 = "<span class=\"text-nowrap\"";
  protected static final String _203 = "<span class=\"text-nowrap\"><a href=\"";
  protected static final String _204 = "<span id=\"cert_";
  protected static final String _205 = "<span id=\"p1\" style=\"font-size: 125%\">";
  protected static final String _206 = "<span id=\"pgp_";
  protected static final String _207 = "<span style='font-weight: bold; color: Firebrick;'>(";
  protected static final String _208 = "<span style=\"";
  protected static final String _209 = "<span style=\"color: DarkCyan; font-size: 110%;\">(";
  protected static final String _210 = "<span style=\"color: DarkCyan; font-size: 60%;\"> (";
  protected static final String _211 = "<span style=\"color: DarkCyan;\">";
  protected static final String _212 = "<span style=\"color: DarkOliveGreen;";
  protected static final String _213 = "<span style=\"color: DarkOliveGreen;\">";
  protected static final String _214 = "<span style=\"color: FireBrick; font-size: 60%;\"> (";
  protected static final String _215 = "<span style=\"color: FireBrick; font-size: 60%;\">(";
  protected static final String _216 = "<span style=\"color: FireBrick; font-size: 60%;\">(Inappropriate absolute location)</span>";
  protected static final String _217 = "<span style=\"color: FireBrick; font-size: 90%;\">";
  protected static final String _218 = "<span style=\"color: FireBrick;\">(";
  protected static final String _219 = "<span style=\"color: FireBrick;\">Unsigned</span>";
  protected static final String _220 = "<span style=\"color: red; text-decoration: line-through; ";
  protected static final String _221 = "<span style=\"font-size:100%;\">";
  protected static final String _222 = "<span style=\"margin-left: 1em;\" class=\"nowrap\">Filter Pattern: <input id=\"package-subset\" type=\"text\" oninput=\"match('filter-package', 'package-subset', 'package-li');\"> <span style=\"color: firebrick;\" id=\"package-subset-error\"></span></input></span><br/>";
  protected static final String _223 = "<span style=\"margin-left: 1em;\" class=\"nowrap\">Filter Pattern: <input id=\"subset\" type=\"text\" oninput=\"match('filter', 'subset', 'iu-li');\"> <span style=\"color: firebrick;\" id=\"subset-error\"></span></input></span><br/>";
  protected static final String _224 = "<span style=\"margin-left: 1em;\">";
  protected static final String _225 = "<style>";
  protected static final String _226 = "<title>";
  protected static final String _227 = "<tt style=\"float: left;\" class=\"orange\">&#xbb;</tt>";
  protected static final String _228 = "<tt style=\"float: right;\" class=\"orange\">&#xab;</tt>";
  protected static final String _229 = "<ul class=\"font-smaller\" id=\"";
  protected static final String _230 = "<ul class=\"font-smaller\" style=\"list-style-type: none; display:none; margin-left: -3em;\" id=\"";
  protected static final String _231 = "<ul id=\"";
  protected static final String _232 = "<ul id=\"categories\" style=\"display: ";
  protected static final String _233 = "<ul id=\"certificates\" style=\"display:";
  protected static final String _234 = "<ul id=\"feature_providers\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _235 = "<ul id=\"features\" style=\"display: none; list-style-type: none; margin-left: -2em;\">";
  protected static final String _236 = "<ul id=\"leftnav\" class=\"ul-left-nav fa-ul hidden-print\">";
  protected static final String _237 = "<ul id=\"licenses\" style=\"display: ";
  protected static final String _238 = "<ul id=\"pgpKeys\" style=\"display:";
  protected static final String _239 = "<ul id=\"products\" style=\"display: ";
  protected static final String _240 = "<ul style=\"display:none; list-style-type: none; padding: 0; margin: 0;\" id=\"__";
  protected static final String _241 = "<ul style=\"list-style-type: none; display:none; padding: 0; margin: 0; margin-left: 2em;\" id=\"_f";
  protected static final String _242 = "<ul style=\"list-style-type: none; padding: 0; margin-left: 1em;\">";
  protected static final String _243 = "<ul>";
  protected static final String _244 = "=</span>";
  protected static final String _245 = ">";
  protected static final String _246 = "><img class=\"fit-image\" src=\"";
  protected static final String _247 = "><span style=\"";
  protected static final String _248 = "Categories";
  protected static final String _249 = "Certificates";
  protected static final String _250 = "Content Metadata";
  protected static final String _251 = "Features";
  protected static final String _252 = "Features/Products";
  protected static final String _253 = "In addition to this composite report, reports are also generated for each of the composed children listed in the navigation bar to the left.";
  protected static final String _254 = "Installable Units";
  protected static final String _255 = "Java Packages";
  protected static final String _256 = "Licenses";
  protected static final String _257 = "No Name<br/>";
  protected static final String _258 = "PGP Keys";
  protected static final String _259 = "Products";
  protected static final String _260 = "Providers";
  protected static final String _261 = "Signing Certificates";
  protected static final String _262 = "Signing PGP Keys";
  protected static final String _263 = "This";
  protected static final String _264 = "This is a composite update site.";
  protected static final String _265 = "This is a generated";
  protected static final String _266 = "XML";
  protected static final String _267 = "[<img class=\"fit-image\" src=\"";
  protected static final String _268 = "\"";
  protected static final String _269 = "\" alt=\"\"/>";
  protected static final String _270 = "\" alt=\"\"/><img style=\"margin-top: -2ex;\" class=\"fit-image\" src=\"";
  protected static final String _271 = "\" class=\"bb\" style=\"";
  protected static final String _272 = "\" class=\"iu-li";
  protected static final String _273 = "\" class=\"package-li";
  protected static final String _274 = "\" class=\"signer-id\">";
  protected static final String _275 = "\" onclick=\"clickOnToggleButton('licenses_arrows'); clickOnToggleButton('__";
  protected static final String _276 = "\" onclick=\"expand_only('certificates'); expand_only_inline('certificates_all_arrows');\">";
  protected static final String _277 = "\" onclick=\"expand_only('pgpKeys'); expand_only_inline('pgpKeys_all_arrows');\">";
  protected static final String _278 = "\" style=\"display: none;\">";
  protected static final String _279 = "\" style=\"display:none; margin-left: -2em; list-style-type: none;\">";
  protected static final String _280 = "\" style=\"display:none; margin-left: 2em; background-color: ";
  protected static final String _281 = "\" style=\"list-style-type: none; display: none; margin-left: -1em;\">";
  protected static final String _282 = "\" target=\"keyserver\">0x";
  protected static final String _283 = "\" target=\"oomph_wiki\"/>";
  protected static final String _284 = "\" target=\"oomph_wiki\">";
  protected static final String _285 = "\" target=\"report_source\">";
  protected static final String _286 = "\"/>";
  protected static final String _287 = "\"/> ";
  protected static final String _288 = "\"/>]";
  protected static final String _289 = "\">";
  protected static final String _290 = "\">&#x25B7;</button>";
  protected static final String _291 = "\"><img class=\"fit-image\" src=\"";
  protected static final String _292 = "_arrows'); clickOnToggleButton('_";
  protected static final String _293 = "_arrows'); navigateTo('_";
  protected static final String _294 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('";
  protected static final String _295 = "_arrows\" class=\"orange bb\" onclick=\"expand_collapse('__";
  protected static final String _296 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('";
  protected static final String _297 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_";
  protected static final String _298 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('__";
  protected static final String _299 = "_arrows\" class=\"orange bb\" style=\"margin-left: 1em;\" onclick=\"expand_collapse('_f";
  protected static final String _300 = "background-color: white;";
  protected static final String _301 = "border: 1px solid black;";
  protected static final String _302 = "border: none;";
  protected static final String _303 = "break;";
  protected static final String _304 = "buttonTargets.item(i).innerHTML = '&#x25B7;';";
  protected static final String _305 = "buttonTargets.item(i).innerHTML = '&#x25E2;';";
  protected static final String _306 = "catch (err) {";
  protected static final String _307 = "color: DarkSlateGray;";
  protected static final String _308 = "color: FireBrick;";
  protected static final String _309 = "color: IndianRed;";
  protected static final String _310 = "color: MediumAquaMarine;";
  protected static final String _311 = "color: MediumOrchid;";
  protected static final String _312 = "color: SaddleBrown;";
  protected static final String _313 = "color: SeaGreen;";
  protected static final String _314 = "color: SteelBlue;";
  protected static final String _315 = "color: Teal;";
  protected static final String _316 = "continue;";
  protected static final String _317 = "currentFilter = filter.value;";
  protected static final String _318 = "document.execCommand(\"copy\");";
  protected static final String _319 = "e.click();";
  protected static final String _320 = "e.innerHTML = '&#x25B7;';";
  protected static final String _321 = "e.innerHTML = '&#x25E2;';";
  protected static final String _322 = "e.scrollIntoView();";
  protected static final String _323 = "e.style.display = 'block';";
  protected static final String _324 = "e.style.display = 'inline';";
  protected static final String _325 = "e.style.display = 'inline-block';";
  protected static final String _326 = "e.style.display = 'none';";
  protected static final String _327 = "e.title= 'Collapse All';";
  protected static final String _328 = "e.title= 'Expand All';";
  protected static final String _329 = "em; text-indent: -4em;\">";
  protected static final String _330 = "em;\">";
  protected static final String _331 = "f.innerHTML = '&#x25B7;';";
  protected static final String _332 = "f.innerHTML = '&#x25E2;';";
  protected static final String _333 = "font-family: monospace;";
  protected static final String _334 = "font-size: 125%;";
  protected static final String _335 = "font-size: 75%;\">";
  protected static final String _336 = "font-size: 80%;";
  protected static final String _337 = "font-size: 90%;";
  protected static final String _338 = "for (var i = 0; i < buttonTargets.length; i++) {";
  protected static final String _339 = "for (var i = 0; i < ius.length; i++) {";
  protected static final String _340 = "for (var i = 0; i < spanTargets.length; i++) {";
  protected static final String _341 = "function clickOnButton(id) {";
  protected static final String _342 = "function clickOnToggleButton(id) {";
  protected static final String _343 = "function copyToClipboard(element) {";
  protected static final String _344 = "function expand(id) {";
  protected static final String _345 = "function expand2(self, id) {";
  protected static final String _346 = "function expand3(self, id) {";
  protected static final String _347 = "function expand_collapse(id) {";
  protected static final String _348 = "function expand_collapse_all(base) {";
  protected static final String _349 = "function expand_collapse_inline(id) {";
  protected static final String _350 = "function expand_collapse_inline_block(id) {";
  protected static final String _351 = "function expand_only(id) {";
  protected static final String _352 = "function expand_only_inline(id) {";
  protected static final String _353 = "function filterIU(className, filterClass) {";
  protected static final String _354 = "function match(filterId, id, filterClass) {";
  protected static final String _355 = "function navigateTo(id) {";
  protected static final String _356 = "function toggle(id) {";
  protected static final String _357 = "height: 2ex;";
  protected static final String _358 = "if (!targetsArray.includes(iu)) {";
  protected static final String _359 = "if ((matchText == '' || text.match(matchText) != null) && targetsArray.includes(iu)) {";
  protected static final String _360 = "if (count == 0 && message.innerHTML == '') {";
  protected static final String _361 = "if (count == 0) {";
  protected static final String _362 = "if (e.innerHTML == '";
  protected static final String _363 = "if (e.innerHTML == '\\u25E2') {";
  protected static final String _364 = "if (e.style.display == 'none'){";
  protected static final String _365 = "if (e.title == 'Expand All') {";
  protected static final String _366 = "if (f != null) {";
  protected static final String _367 = "if (f !=null) {";
  protected static final String _368 = "if (filter != null && filter.value != 'all') {";
  protected static final String _369 = "if (matchText != '' && iu.textContent.match(matchText) == null) {";
  protected static final String _370 = "if (t.title != 'Collapse All'){";
  protected static final String _371 = "if (t.title == 'Collapse All'){";
  protected static final String _372 = "iu.style.display = 'block';";
  protected static final String _373 = "iu.style.display = 'none';";
  protected static final String _374 = "margin-bottom: -2ex;";
  protected static final String _375 = "margin-left: 0em;";
  protected static final String _376 = "margin-top: -2ex;";
  protected static final String _377 = "margin: 0px 0px 0px 0px;";
  protected static final String _378 = "message.innerHTML = ' No matches';";
  protected static final String _379 = "message.innerHTML = '';";
  protected static final String _380 = "message.innerHTML = \"\";";
  protected static final String _381 = "message.innerHTML = errMessage;";
  protected static final String _382 = "padding: -2pt -2pt -2pt -2pt;";
  protected static final String _383 = "padding: 0px 0px;";
  protected static final String _384 = "report is produced by <a href=\"";
  protected static final String _385 = "s";
  protected static final String _386 = "span:target {";
  protected static final String _387 = "spanTargets.item(i).style.display = 'inline-block';";
  protected static final String _388 = "spanTargets.item(i).style.display = 'none';";
  protected static final String _389 = "try";
  protected static final String _390 = "try {";
  protected static final String _391 = "var $temp = $(\"<input>\");";
  protected static final String _392 = "var buttonTargets = document.getElementsByClassName(base + '_button');";
  protected static final String _393 = "var count = 0;";
  protected static final String _394 = "var currentFilter = filterClass;";
  protected static final String _395 = "var e = document.getElementById('subset');";
  protected static final String _396 = "var e = document.getElementById(base);";
  protected static final String _397 = "var e = document.getElementById(id);";
  protected static final String _398 = "var errMessage = err.message;";
  protected static final String _399 = "var f = document.getElementById(id+\"_arrows\");";
  protected static final String _400 = "var filter = document.querySelector('input[name=\"' + filterId + '\"]:checked');";
  protected static final String _401 = "var iu = ius[i];";
  protected static final String _402 = "var ius = document.getElementsByClassName(filterClass);";
  protected static final String _403 = "var matchText = e.value;";
  protected static final String _404 = "var message = document.getElementById('subset-error');";
  protected static final String _405 = "var message = document.getElementById(id + '-error');";
  protected static final String _406 = "var spanTargets = document.getElementsByClassName(base);";
  protected static final String _407 = "var state = e.innerHTML;";
  protected static final String _408 = "var t = document.getElementById('all');";
  protected static final String _409 = "var t = document.getElementById(self);";
  protected static final String _410 = "var targets = document.getElementsByClassName(className);";
  protected static final String _411 = "var targets = document.getElementsByClassName(currentFilter);";
  protected static final String _412 = "var targetsArray = [].slice.call(targets);";
  protected static final String _413 = "var text = iu.textContent;";
  protected static final String _414 = "white-space: nowrap;";
  protected static final String _415 = "white-space: pre;";
  protected static final String _416 = "width: 2ex;";
  protected static final String _417 = "{";
  protected static final String _418 = "}";
  protected static final String _419 = "} else {";
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
  protected final String NL_19 = NL + "                       ";
  protected final String NL_20 = NL + "                        ";
  protected final String _420 = _58 + NL + _153 + NL + _151 + NL_1 + _186 + NL_1 + _226;
  protected final String _421 = _85 + NL_1 + _188 + NL_1 + _187 + NL_1 + _182 + NL_1 + _184 + NL_1 + _183 + NL_1 + _198;
  protected final String _422 = NL_1 + _225 + NL + NL + _37 + NL_1 + _414 + NL + _418 + NL + NL + _34 + NL_1 + _337 + NL + _418 + NL + NL + _33 + NL_1 + _416 + NL_1 + _357 + NL + _418 + NL + NL + _386 + NL_1 + _334 + NL_1 + _301 + NL + _418 + NL + NL + _46 + NL_1 + _415 + NL_1 + _302 + NL_1 + _383 + NL_1 + _376 + NL_1 + _374 + NL_1 + _375 + NL + _418 + NL + NL + _30 + NL_1 + _308 + NL_1 + _337 + NL + _418 + NL + NL + _45 + NL_1 + _415 + NL_1 + _302 + NL_1 + _383 + NL_1 + _376 + NL_1 + _374 + NL_1 + _375 + NL + _418 + NL + NL + _47 + NL_1 + _314 + NL_1 + _333 + NL_1 + _336 + NL + _418 + NL + NL + _41 + NL_1 + _310 + NL_1 + _333 + NL_1 + _336 + NL + _418 + NL + NL + _43 + NL_1 + _311 + NL_1 + _333 + NL_1 + _336 + NL + _418 + NL + NL + _42 + NL_1 + _307 + NL_1 + _337 + NL + _418 + NL + NL + _44 + NL_1 + _312 + NL_1 + _337 + NL + _418 + NL + NL + _31 + NL_1 + _300 + NL_1 + _302 + NL_1 + _383 + NL + _418 + NL + NL + _32 + NL_1 + _300 + NL + _418 + NL + NL + _35 + NL_1 + _382 + NL_1 + _377 + NL + _418 + NL + NL + _36 + NL_1 + _313 + NL_1 + _337 + NL + _418 + NL + NL + _40 + NL_1 + _315 + NL_1 + _337 + NL + _418 + NL + NL + _39 + NL_1 + _337 + NL + _418 + NL + NL + _38 + NL_1 + _309 + NL_1 + _337 + NL + _418 + NL_1 + _84 + NL + _72 + NL + NL_1 + _100;
  protected final String _423 = NL_2 + _199 + NL + NL_4 + _354 + NL_5 + _400 + NL_5 + _394 + NL_5 + _368 + NL_7 + _317 + NL_5 + _418 + NL_5 + _397 + NL_5 + _405 + NL_5 + _403 + NL_5 + _402 + NL_5 + _411 + NL_5 + _412 + NL_5 + _393 + NL_5 + _339 + NL_7 + _401 + NL_7 + _413 + NL_7 + _390 + NL_9 + _359 + NL_11 + _372 + NL_11 + _28 + NL_9 + _419 + NL_11 + _373 + NL_9 + _418 + NL_9 + _380 + NL_7 + _418 + NL_7 + _306 + NL_9 + _398 + NL_9 + _381 + NL_9 + _303 + NL_7 + _418 + NL_5 + _418 + NL_5 + _360 + NL_9 + _378 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _353 + NL_5 + _395 + NL_5 + _403 + NL_5 + _393 + NL_5 + _402 + NL_5 + _410 + NL_5 + _412 + NL_5 + _339 + NL_7 + _401 + NL_7 + _358 + NL_9 + _373 + NL_7 + _419 + NL_9 + _389 + NL_9 + _417 + NL_11 + _369 + NL_13 + _373 + NL_13 + _316 + NL_11 + _418 + NL_9 + _418 + NL_9 + _306 + NL_9 + _418 + NL_9 + _372 + NL_9 + _28 + NL_7 + _418 + NL_5 + _418 + NL_5 + _404 + NL_5 + _361 + NL_9 + _378 + NL_5 + _419 + NL_9 + _379 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _343 + NL_5 + _391 + NL_5 + _13 + NL_5 + _15 + NL_5 + _318 + NL_5 + _14 + NL_4 + _418 + NL + NL_4 + _341 + NL_5 + _397 + NL_5 + _319 + NL_4 + _418 + NL + NL_4 + _342 + NL_5 + _397 + NL_5 + _407 + NL_5 + _362;
  protected final String _424 = _23 + NL_7 + _319 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _355 + NL_5 + _397 + NL_5 + _322 + NL_4 + _418 + NL + NL_4 + _356 + NL_5 + _397 + NL_5 + _365 + NL_7 + _327 + NL_7 + _321 + NL_5 + _419 + NL_7 + _328 + NL_7 + _320 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _345 + NL_5 + _409 + NL_5 + _397 + NL_5 + _399 + NL_5 + _371 + NL_7 + _323 + NL_7 + _332 + NL_5 + _419 + NL_7 + _326 + NL_7 + _331 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _346 + NL_5 + _409 + NL_5 + _397 + NL_5 + _399 + NL_5 + _370 + NL_7 + _326 + NL_7 + _331 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _344 + NL_5 + _408 + NL_5 + _397 + NL_5 + _399 + NL_5 + _371 + NL_7 + _323 + NL_7 + _332 + NL_5 + _419 + NL_7 + _326 + NL_7 + _331 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _347 + NL_5 + _397 + NL_5 + _399 + NL_5 + _364 + NL_7 + _323 + NL_7 + _332 + NL_5 + _419 + NL_7 + _326 + NL_7 + _331 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _351 + NL_5 + _397 + NL_5 + _399 + NL_5 + _364 + NL_7 + _323 + NL_7 + _332 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _349 + NL_5 + _397 + NL_5 + _399 + NL_5 + _364 + NL_7 + _324 + NL_7 + _366 + NL_9 + _332 + NL_7 + _418 + NL_5 + _419 + NL_7 + _326 + NL_7 + _367 + NL_9 + _331 + NL_7 + _418 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _352 + NL_5 + _397 + NL_5 + _399 + NL_5 + _364 + NL_7 + _324 + NL_7 + _366 + NL_9 + _332 + NL_7 + _418 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _350 + NL_5 + _397 + NL_5 + _399 + NL_5 + _364 + NL_7 + _325 + NL_7 + _366 + NL_9 + _332 + NL_7 + _418 + NL_5 + _419 + NL_7 + _326 + NL_7 + _367 + NL_9 + _331 + NL_7 + _418 + NL_5 + _418 + NL_4 + _418 + NL + NL_4 + _348 + NL_5 + _396 + NL_5 + _392 + NL_5 + _406 + NL_5 + _363 + NL_9 + _320 + NL_9 + _338 + NL_11 + _304 + NL_9 + _418 + NL_9 + _340 + NL_11 + _388 + NL_9 + _418 + NL_5 + _419 + NL_9 + _321 + NL_9 + _338 + NL_11 + _305 + NL_9 + _418 + NL_9 + _340 + NL_11 + _387 + NL_9 + _418 + NL_5 + _418 + NL_4 + _418 + NL + NL_2 + _80 + NL + NL_2 + _152 + NL_4 + _127 + NL_5 + _132 + NL_7 + _130 + NL_9 + _134 + NL_11 + _96 + NL_13 + _160 + NL_11 + _59 + NL_9 + _69 + NL_7 + _69 + NL_5 + _69 + NL_4 + _69 + NL_2 + _73;
  protected final String _425 = NL_2 + _200 + NL_4 + _127 + NL_5 + _145 + NL_5 + _133 + NL_7 + _126 + NL_9 + _189 + NL_11 + _180 + NL_11 + _181;
  protected final String _426 = NL_11 + _171;
  protected final String _427 = NL_11 + _179;
  protected final String _428 = NL_9 + _77 + NL_7 + _69 + NL_5 + _69 + NL_4 + _69 + NL_2 + _81 + NL + NL_2 + _185 + NL_2 + _131 + NL;
  protected final String _429 = NL_4 + _55 + NL_4 + _98 + NL_5 + _236 + NL_7 + _174 + NL_9 + _90 + NL_7 + _75 + NL_7 + _174 + NL_9 + _89 + NL_7 + _75;
  protected final String _430 = NL_7 + _174 + NL_9 + _87;
  protected final String _431 = _59 + NL_7 + _75;
  protected final String _432 = NL_7 + _174 + NL_9 + _228 + NL_9 + _88;
  protected final String _433 = NL_7 + _173 + NL_9 + _227;
  protected final String _434 = NL_9 + _228;
  protected final String _435 = NL_9 + _91;
  protected final String _436 = NL_9 + _59 + NL_7 + _75;
  protected final String _437 = NL_5 + _86 + NL_4 + _65 + NL + NL_4 + _138 + NL_5 + _139;
  protected final String _438 = NL_7 + _144;
  protected final String _439 = NL_7 + _144 + NL_9 + _159;
  protected final String _440 = NL_9 + _257;
  protected final String _441 = NL_7 + _70;
  protected final String _442 = NL_7 + _190;
  protected final String _443 = NL_9 + _99;
  protected final String _444 = NL_9 + _102;
  protected final String _445 = _66 + NL_7 + _78;
  protected final String _446 = NL_7 + _191 + NL_9 + _125 + NL_9 + _205;
  protected final String _447 = _82 + NL_7 + _78 + NL_7 + _101;
  protected final String _448 = NL_7 + _97;
  protected final String _449 = _283 + NL_9 + _161;
  protected final String _450 = _269 + NL_7 + _59;
  protected final String _451 = NL_8 + _192 + NL_10 + _265 + NL_10 + _95 + NL_12 + _158;
  protected final String _452 = _61 + NL_8 + _78 + NL_8 + _195;
  protected final String _453 = NL_8 + _194 + NL_8 + _243;
  protected final String _454 = NL_10 + _179;
  protected final String _455 = NL_8 + _86;
  protected final String _456 = NL_8 + _192 + NL_10 + _263 + NL_10 + _92;
  protected final String _457 = _284 + NL_12 + _158;
  protected final String _458 = _59 + NL_10 + _384;
  protected final String _459 = _62 + NL_8 + _78;
  protected final String _460 = NL_8 + _150 + NL_8 + _192;
  protected final String _461 = NL_8 + _148 + NL_8 + _192;
  protected final String _462 = NL_8 + _146 + NL_10 + _258 + NL_8 + _71;
  protected final String _463 = NL_8 + _129 + NL_10 + _156;
  protected final String _464 = _286 + NL_10 + _203;
  protected final String _465 = _82 + NL_8 + _69;
  protected final String _466 = NL_8 + _146 + NL_10 + _249;
  protected final String _467 = NL_8 + _215;
  protected final String _468 = NL_8 + _71;
  protected final String _469 = NL_10 + _143 + NL_12 + _159;
  protected final String _470 = _286 + NL_12 + _219 + NL_10 + _69;
  protected final String _471 = NL_10 + _128;
  protected final String _472 = _330 + NL_12 + _156;
  protected final String _473 = NL_12 + _202;
  protected final String _474 = NL_10 + _69;
  protected final String _475 = NL_8 + _147;
  protected final String _476 = NL_8 + _192;
  protected final String _477 = NL_10 + _159;
  protected final String _478 = _286 + NL_10 + _211;
  protected final String _479 = _82 + NL_10 + _92;
  protected final String _480 = NL_10 + _101 + NL_10 + _217;
  protected final String _481 = NL_8 + _78;
  protected final String _482 = NL_6 + _149;
  protected final String _483 = NL_12 + _105;
  protected final String _484 = _22 + NL_12 + _159;
  protected final String _485 = _286 + NL_12 + _208;
  protected final String _486 = NL_12 + _197;
  protected final String _487 = NL_8 + _146 + NL_10 + _250 + NL_10 + _124 + NL_8 + _71 + NL_8 + _135;
  protected final String _488 = NL + _57;
  protected final String _489 = NL_8 + _69;
  protected final String _490 = NL_10 + _264;
  protected final String _491 = NL_10 + _253 + NL_8 + _78;
  protected final String _492 = NL_8 + _146 + NL_8 + _123 + NL_8 + _158;
  protected final String _493 = _286 + NL_8 + _266;
  protected final String _494 = NL_8 + _216;
  protected final String _495 = NL_8 + _71 + NL_8 + _136;
  protected final String _496 = NL_8 + _146 + NL_8 + _116 + NL_8 + _158;
  protected final String _497 = _286 + NL_8 + _256 + NL_8 + _211;
  protected final String _498 = NL_8 + _115;
  protected final String _499 = NL_8 + _71 + NL_8 + _237;
  protected final String _500 = NL_10 + _178 + NL_12 + _105;
  protected final String _501 = _22 + NL_12 + _158;
  protected final String _502 = _82 + NL_12 + _29 + NL_12 + _211;
  protected final String _503 = _82 + NL_12 + _240;
  protected final String _504 = _289 + NL_14 + _177 + NL_15 + _104;
  protected final String _505 = _101 + NL_15 + _196;
  protected final String _506 = _79 + NL_14 + _75 + NL_14 + _177 + NL_15 + _106;
  protected final String _507 = _22 + NL_15 + _252 + NL_15 + _241;
  protected final String _508 = NL_16 + _172 + NL_18 + _92;
  protected final String _509 = _289 + NL_20 + _158;
  protected final String _510 = NL_20 + _213;
  protected final String _511 = _82 + NL_18 + _59 + NL_16 + _75;
  protected final String _512 = NL_15 + _86 + NL_14 + _75 + NL_12 + _86 + NL_10 + _75;
  protected final String _513 = NL_8 + _193 + NL_8 + _146 + NL_8 + _120 + NL_8 + _158;
  protected final String _514 = _286 + NL_8 + _262 + NL_8 + _211;
  protected final String _515 = NL_8 + _119;
  protected final String _516 = NL_8 + _71 + NL_8 + _238;
  protected final String _517 = NL_8 + _178 + NL_10 + _128;
  protected final String _518 = _329 + NL_12 + _103;
  protected final String _519 = _22 + NL_12 + _155;
  protected final String _520 = NL_12 + _158;
  protected final String _521 = _286 + NL_12 + _206;
  protected final String _522 = NL_12 + _203;
  protected final String _523 = _82 + NL_12 + _209;
  protected final String _524 = _27 + NL_10 + _69 + NL_10 + _230;
  protected final String _525 = NL_12 + _178 + NL_14 + _92;
  protected final String _526 = _59 + NL_12 + _75;
  protected final String _527 = NL_10 + _86 + NL_8 + _75;
  protected final String _528 = NL_8 + _193 + NL_8 + _146 + NL_8 + _110 + NL_8 + _158;
  protected final String _529 = _286 + NL_8 + _261 + NL_8 + _211;
  protected final String _530 = NL_8 + _109;
  protected final String _531 = NL_8 + _71 + NL_8 + _233;
  protected final String _532 = NL_8 + _178;
  protected final String _533 = NL_10 + _142 + NL_12 + _103;
  protected final String _534 = _286 + NL_12 + _218;
  protected final String _535 = _26 + NL_10 + _69;
  protected final String _536 = NL_12 + _103;
  protected final String _537 = NL_12 + _20;
  protected final String _538 = NL_12 + _155;
  protected final String _539 = NL_12 + _209;
  protected final String _540 = _286 + NL_12 + _204;
  protected final String _541 = NL_12 + _207;
  protected final String _542 = NL_10 + _230;
  protected final String _543 = NL + _56;
  protected final String _544 = NL_8 + _193 + NL_8 + _146 + NL_8 + _111 + NL_8 + _158;
  protected final String _545 = _286 + NL_8 + _260 + NL_8 + _211;
  protected final String _546 = NL_8 + _71 + NL_8 + _234;
  protected final String _547 = NL_10 + _178 + NL_12 + _103;
  protected final String _548 = NL_12 + _154;
  protected final String _549 = NL_12 + _201;
  protected final String _550 = _82 + NL_12 + _211;
  protected final String _551 = _82 + NL_12 + _231;
  protected final String _552 = NL_14 + _178 + NL_15 + _92;
  protected final String _553 = _289 + NL_17 + _154;
  protected final String _554 = NL_17 + _213;
  protected final String _555 = _82 + NL_15 + _59 + NL_14 + _75;
  protected final String _556 = NL_12 + _86 + NL_10 + _75;
  protected final String _557 = NL_8 + _193 + NL_8 + _146 + NL_8 + _112 + NL_8 + _158;
  protected final String _558 = _286 + NL_8 + _251 + NL_8 + _211;
  protected final String _559 = NL_8 + _71 + NL_8 + _235;
  protected final String _560 = NL_10 + _176 + NL_13 + _92;
  protected final String _561 = _289 + NL_14 + _158;
  protected final String _562 = NL_14 + _213;
  protected final String _563 = _82 + NL_12 + _59;
  protected final String _564 = NL_12 + _114;
  protected final String _565 = NL_10 + _75;
  protected final String _566 = NL_8 + _193 + NL_8 + _146 + NL_8 + _122 + NL_8 + _157;
  protected final String _567 = _286 + NL_8 + _259 + NL_8 + _211;
  protected final String _568 = NL_8 + _121;
  protected final String _569 = NL_8 + _71 + NL_8 + _239;
  protected final String _570 = NL_10 + _176;
  protected final String _571 = NL_12 + _92;
  protected final String _572 = NL_12 + _229;
  protected final String _573 = _289 + NL_17 + _158;
  protected final String _574 = NL_12 + _86;
  protected final String _575 = NL_8 + _193 + NL_8 + _146 + NL_8 + _108 + NL_8 + _158;
  protected final String _576 = _286 + NL_8 + _248 + NL_8 + _211;
  protected final String _577 = NL_8 + _107;
  protected final String _578 = NL_8 + _71 + NL_8 + _232;
  protected final String _579 = NL_12 + _59;
  protected final String _580 = NL_8 + _146 + NL_8 + _113 + NL_8 + _158;
  protected final String _581 = _286 + NL_8 + _254 + NL_8 + _211;
  protected final String _582 = NL_8 + _71 + NL_8 + _137 + NL_10 + _223;
  protected final String _583 = NL_10 + _224 + NL_12 + _167;
  protected final String _584 = NL_12 + _162;
  protected final String _585 = NL_12 + _170;
  protected final String _586 = NL_12 + _163;
  protected final String _587 = NL_12 + _164;
  protected final String _588 = NL_12 + _165;
  protected final String _589 = NL_10 + _82;
  protected final String _590 = NL_10 + _242;
  protected final String _591 = NL_12 + _175;
  protected final String _592 = _10 + NL_14 + _92;
  protected final String _593 = _289 + NL_15 + _158;
  protected final String _594 = _286 + NL_15 + _221;
  protected final String _595 = _82 + NL_15 + _212;
  protected final String _596 = _82 + NL_14 + _59;
  protected final String _597 = NL_14 + _114;
  protected final String _598 = NL_14 + _24;
  protected final String _599 = NL_15 + _158;
  protected final String _600 = _286 + NL_15 + _211;
  protected final String _601 = _82 + NL_14 + _25;
  protected final String _602 = NL_14 + _267;
  protected final String _603 = NL_12 + _75;
  protected final String _604 = NL_10 + _86 + NL_8 + _69;
  protected final String _605 = NL_8 + _146 + NL_8 + _118 + NL_8 + _158;
  protected final String _606 = _286 + NL_8 + _255 + NL_8 + _211;
  protected final String _607 = NL_8 + _210;
  protected final String _608 = NL_8 + _214;
  protected final String _609 = NL_8 + _117;
  protected final String _610 = NL_8 + _71 + NL_8 + _140 + NL_10 + _222;
  protected final String _611 = NL_10 + _224 + NL_12 + _166 + NL_12 + _169;
  protected final String _612 = NL_12 + _168;
  protected final String _613 = _10 + NL_14 + _103;
  protected final String _614 = _22 + NL_14 + _158;
  protected final String _615 = _286 + NL_14 + _221;
  protected final String _616 = _82 + NL_14 + _212;
  protected final String _617 = _82 + NL_14 + _231;
  protected final String _618 = NL_15 + _178 + NL_17 + _92;
  protected final String _619 = _289 + NL_19 + _158;
  protected final String _620 = _286 + NL_19 + _221;
  protected final String _621 = _82 + NL_19 + _212;
  protected final String _622 = _82 + NL_17 + _59;
  protected final String _623 = NL_17 + _24;
  protected final String _624 = NL_17 + _94;
  protected final String _625 = NL_17 + _93;
  protected final String _626 = NL_17 + _25;
  protected final String _627 = NL_15 + _75;
  protected final String _628 = NL_14 + _86 + NL_12 + _75;
  protected final String _629 = NL_8 + _141 + NL_8 + _69;
  protected final String _630 = NL_5 + _69 + NL_4 + _69 + NL_3 + _69 + NL_3 + _76 + NL_1 + _67 + NL + _74;

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
    stringBuffer.append(_420);
    stringBuffer.append(reporter.getTitle());
    stringBuffer.append(_421);
    stringBuffer.append(_422);
    stringBuffer.append(_423);
    stringBuffer.append('\u25B7');
    stringBuffer.append(_424);
    stringBuffer.append(_425);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() == null) {
    stringBuffer.append(_426);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_75);
    } else {
    stringBuffer.append(_427);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_289);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_64);
    }
    }
    stringBuffer.append(_428);
    stringBuffer.append(_429);
    for (Map.Entry<String, String> entry : reporter.getBreadcrumbs().entrySet()) {
    if (entry.getValue() != null) {
    stringBuffer.append(_430);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_289);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_431);
    } else {
      if (iuReport == null) {
    stringBuffer.append(_432);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_431);
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
    stringBuffer.append(_433);
    if (index != -1) {
    stringBuffer.append(_434);
    }
    stringBuffer.append(_435);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_289);
    stringBuffer.append(NL_9);
    stringBuffer.append(label);
    stringBuffer.append(_436);
    }
    }
    stringBuffer.append(_437);
    if (iuReport == null) {
    stringBuffer.append(_438);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_70);
    } else {
    stringBuffer.append(_439);
    stringBuffer.append(report.getIUImage(iuReport.getIU()));
    stringBuffer.append(_286);
    String name = report.getName(iuReport.getIU(), false);
    if (name != null) {
    stringBuffer.append(NL_9);
    stringBuffer.append(helper.htmlEscape(name, true));
    stringBuffer.append(_101);
    } else {
    stringBuffer.append(_440);
    }
    stringBuffer.append(NL_9);
    stringBuffer.append(reporter.getTitle(true));
    stringBuffer.append(_441);
    }
    stringBuffer.append(_442);
    if (report != null && report.getDate() != null) {
    stringBuffer.append(_443);
    stringBuffer.append(report.getDate());
    stringBuffer.append(_66);
    }
    stringBuffer.append(_444);
    stringBuffer.append(reporter.getNow());
    stringBuffer.append(_445);
    if (report != null && !report.getSiteURL().startsWith("file:")) {
    stringBuffer.append(_446);
    stringBuffer.append(report.getSiteURL());
    stringBuffer.append(_447);
    }
    stringBuffer.append(_448);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_449);
    stringBuffer.append(reporter.getReportBrandingImage());
    stringBuffer.append(_270);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_450);
    if (indexReport != null) {
    stringBuffer.append(_451);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_286);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_452);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_285);
    stringBuffer.append(reporter.getReportSource());
    stringBuffer.append(_63);
    Map<String, String> allReports = indexReport.getAllReports();
    if (allReports != null && !allReports.isEmpty()) {
    stringBuffer.append(_453);
    for (Map.Entry<String, String> entry : allReports.entrySet()) {
    stringBuffer.append(_454);
    stringBuffer.append(entry.getValue());
    stringBuffer.append(_289);
    stringBuffer.append(entry.getKey());
    stringBuffer.append(_64);
    }
    stringBuffer.append(_455);
    }
    } else if (iuReport != null) {
    stringBuffer.append(_456);
    stringBuffer.append(reporter.getHelpLink());
    stringBuffer.append(_457);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_286);
    stringBuffer.append(NL_12);
    stringBuffer.append(reporter.getHelpText());
    stringBuffer.append(_458);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_285);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_459);
    String provider = iuReport.getProvider();
    if (provider != null) {
    stringBuffer.append(_460);
    stringBuffer.append(helper.htmlEscape(provider, false));
    stringBuffer.append(_78);
    }
    String description = iuReport.getDescription();
    if (description != null) {
    stringBuffer.append(_461);
    stringBuffer.append(helper.htmlEscape(description, false));
    stringBuffer.append(_78);
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iuReport.getIU());
    if (!artifacts.isEmpty()) {
      Set<PGPPublicKey> pgpKeys = report.getPGPKeys(iuReport.getIU());
      if (!pgpKeys.isEmpty()) {
    stringBuffer.append(_462);
    for (PGPPublicKey pgpPublicKey : pgpKeys) {
          String fingerPrint = PGPPublicKeyService.toHexFingerprint(pgpPublicKey);
          String uid = report.getUID(pgpPublicKey);
    stringBuffer.append(_463);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_464);
    stringBuffer.append(report.getKeyServerURL(pgpPublicKey));
    stringBuffer.append(_282);
    stringBuffer.append(fingerPrint);
    stringBuffer.append(_60);
    stringBuffer.append(uid);
    stringBuffer.append(_465);
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
    stringBuffer.append(_466);
    if (invalidSignatureCount > 0) {
    stringBuffer.append(_467);
    stringBuffer.append(invalidSignatureCount);
    stringBuffer.append(_4);
    if (invalidSignatureCount > 1) {
    stringBuffer.append(_385);
    }
    stringBuffer.append(_27);
    }
    stringBuffer.append(_468);
    for (List<Certificate> certificates : allCertificates) {
    if (certificates.isEmpty()) {
    stringBuffer.append(_469);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_470);
    } else {
            int count = 0;
            for (Certificate certificate : certificates) {
              Map<String, IInstallableUnit> invalidSignatures = allInvalidSignatures.get(certificates);
              String style = invalidSignatures != null && invalidSignatures.values().contains(iuReport.getIU()) ? " text-decoration: line-through;" : "";
              Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_471);
    stringBuffer.append(count++);
    stringBuffer.append(_472);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_286);
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
    stringBuffer.append(_473);
    stringBuffer.append(spanStyle);
    stringBuffer.append(_247);
    stringBuffer.append(keyStyle);
    stringBuffer.append(_335);
    stringBuffer.append(key);
    stringBuffer.append(_244);
    stringBuffer.append(value);
    stringBuffer.append(_82);
    }
    stringBuffer.append(_474);
    }
    }
    }
    }
    stringBuffer.append(_475);
    for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
        String artifact = entry.getKey();
        Boolean signed = entry.getValue();
    stringBuffer.append(_476);
    if (signed != null) {
    stringBuffer.append(_477);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_286);
    }
    stringBuffer.append(_477);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_478);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_479);
    stringBuffer.append(report.getRepositoryURL(artifact) + '/' + artifact);
    stringBuffer.append(_289);
    stringBuffer.append(artifact);
    stringBuffer.append(_59);
    for (String status : report.getArtifactStatus(artifact)) {
    stringBuffer.append(_480);
    stringBuffer.append(status);
    stringBuffer.append(_82);
    }
    stringBuffer.append(_481);
    }
    }
    List<Report.LicenseDetail> licenses = report.getLicenses(iuReport.getIU());
    if (licenses != null) {
    stringBuffer.append(_482);
    for (LicenseDetail license : licenses) {
    String id = license.getUUID();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
    stringBuffer.append(_483);
    stringBuffer.append(id);
    stringBuffer.append(_295);
    stringBuffer.append(id);
    stringBuffer.append(_484);
    stringBuffer.append(report.getLicenseImage());
    stringBuffer.append(_485);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_289);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_82);
    stringBuffer.append(NL_12);
    stringBuffer.append(id);
    stringBuffer.append(_486);
    stringBuffer.append(id);
    stringBuffer.append(_280);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_289);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_220);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_289);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_83);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_289);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_82);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_79);
    }
    }
    stringBuffer.append(_487);
    for (String xml : report.getXML(iuReport.getIU(), Collections.<String, String> emptyMap())) {
    stringBuffer.append(_488);
    stringBuffer.append(xml);
    }
    stringBuffer.append(_489);
    } else {
    stringBuffer.append(_456);
    stringBuffer.append(report.getHelpLink());
    stringBuffer.append(_457);
    stringBuffer.append(reporter.getHelpImage());
    stringBuffer.append(_286);
    stringBuffer.append(NL_12);
    stringBuffer.append(report.getHelpText());
    stringBuffer.append(_458);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_285);
    stringBuffer.append(report.getReportSource());
    stringBuffer.append(_459);
    List<Report> children = report.getChildren();
    if (!children.isEmpty()) {
    stringBuffer.append(_476);
    if (!report.isRoot()) {
    stringBuffer.append(_490);
    }
    stringBuffer.append(_491);
    }
    String metadataXML = report.getMetadataXML();
    String artifactXML = report.getArtifactML();
    if (metadataXML != null || artifactXML != null) {
    stringBuffer.append(_492);
    stringBuffer.append(report.getRepositoryImage());
    stringBuffer.append(_493);
    if (metadataXML != null && metadataXML.contains("bad-absolute-location") || artifactXML != null && artifactXML.contains("bad-absolute-location")) {
    stringBuffer.append(_494);
    }
    stringBuffer.append(_495);
    if (metadataXML != null) {
    stringBuffer.append(_488);
    stringBuffer.append(metadataXML);
    }
    if (artifactXML != null) {
    if (metadataXML != null) {
    stringBuffer.append(NL);
    }
    stringBuffer.append(_488);
    stringBuffer.append(artifactXML);
    }
    stringBuffer.append(_489);
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
    stringBuffer.append(_496);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_497);
    stringBuffer.append(licenses.size());
    stringBuffer.append(_82);
    if (nonConformant != 0) {
    stringBuffer.append(_467);
    stringBuffer.append(nonConformant);
    stringBuffer.append(_3);
    }
    {
        StringBuilder onClick = new StringBuilder();
        for (LicenseDetail license : licenses.keySet()) {
          String id = license.getUUID();
          onClick.append("expand2('licenses_all_arrows', '").append("_" + id).append("');");
          onClick.append("expand2('licenses_all_arrows', '").append("__" + id).append("');");
          onClick.append("expand3('licenses_all_arrows', '").append("_f" + id).append("');");
        }
    stringBuffer.append(_498);
    stringBuffer.append(displayButton);
    stringBuffer.append(_52);
    stringBuffer.append(onClick);
    stringBuffer.append(_290);
    }
    stringBuffer.append(_499);
    stringBuffer.append(display);
    stringBuffer.append(_48);
    for (Map.Entry<LicenseDetail, Set<IInstallableUnit>> entry : licenses.entrySet()) {
        LicenseDetail license = entry.getKey();
        String id = license.getUUID();
        Set<IInstallableUnit> ius = entry.getValue();
        String backgroundColor = license.isSUA() ? "HoneyDew" : license.isMatchedSUA() ? "SeaShell" : "MistyRose";
        String replacementFontSize = !license.getReplacement().isEmpty() && license.getReplacement().length() < 5 ? "font-size: 300%;" : "";
        String mismatchingFontSize = license.getMismatching().length() < 5 ? "font-size: 300%;" : "";
        {
    stringBuffer.append(_500);
    stringBuffer.append(id);
    stringBuffer.append(_298);
    stringBuffer.append(id);
    stringBuffer.append(_501);
    stringBuffer.append(licenseImage);
    stringBuffer.append(_485);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_289);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_502);
    stringBuffer.append(ius.size());
    stringBuffer.append(_503);
    stringBuffer.append(id);
    stringBuffer.append(_504);
    stringBuffer.append(id);
    stringBuffer.append(_297);
    stringBuffer.append(id);
    stringBuffer.append(_22);
    stringBuffer.append(NL_15);
    stringBuffer.append(id);
    stringBuffer.append(_505);
    stringBuffer.append(id);
    stringBuffer.append(_280);
    stringBuffer.append(backgroundColor);
    stringBuffer.append(_289);
    stringBuffer.append(license.getMatchingPrefix());
    stringBuffer.append(_220);
    stringBuffer.append(mismatchingFontSize);
    stringBuffer.append(_289);
    stringBuffer.append(license.getMismatching());
    stringBuffer.append(_83);
    stringBuffer.append(replacementFontSize);
    stringBuffer.append(_289);
    stringBuffer.append(license.getReplacement());
    stringBuffer.append(_82);
    stringBuffer.append(license.getMatchingSuffix());
    stringBuffer.append(_506);
    stringBuffer.append(id);
    stringBuffer.append(_299);
    stringBuffer.append(id);
    stringBuffer.append(_507);
    stringBuffer.append(id);
    stringBuffer.append(_289);
    for (IInstallableUnit iu : ius) {
    stringBuffer.append(_508);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_509);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_286);
    stringBuffer.append(NL_20);
    stringBuffer.append(helper.htmlEscape(report.getName(iu, true), true));
    stringBuffer.append(_510);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_511);
    }
    stringBuffer.append(_512);
    }
    }
    stringBuffer.append(_455);
    }
    Map<PGPPublicKey, Map<String, IInstallableUnit>> pgpKeys = report.getPGPKeys();
    if (!pgpKeys.isEmpty()) {
      int idCount = 0;
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_513);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_514);
    stringBuffer.append(pgpKeys.size());
    stringBuffer.append(_82);
    {
        StringBuilder onClick = new StringBuilder();
        for (int i = 0, size = pgpKeys.size(); i < size; ++i) {
          String id = "pgpKeys" + ++idCount;
          onClick.append("expand2('pgpKeys_all_arrows', '").append(id).append("');");
        }
        idCount = 0;
    stringBuffer.append(_515);
    stringBuffer.append(displayButton);
    stringBuffer.append(_53);
    stringBuffer.append(onClick);
    stringBuffer.append(_290);
    }
    stringBuffer.append(_516);
    stringBuffer.append(display);
    stringBuffer.append(_49);
    for (Map.Entry<PGPPublicKey, Map<String, IInstallableUnit>> entry : pgpKeys.entrySet()) {
        PGPPublicKey pgpPublicKey = entry.getKey();
        String fingerPrint = PGPPublicKeyService.toHexFingerprint(pgpPublicKey);
        String uid = report.getUID(pgpPublicKey);
        String id = "pgpKeys" + ++idCount;
    stringBuffer.append(_517);
    stringBuffer.append(2);
    stringBuffer.append(_518);
    stringBuffer.append(id);
    stringBuffer.append(_296);
    stringBuffer.append(id);
    stringBuffer.append(_519);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_286);
    if (report.isReportingPackages()) {
    stringBuffer.append(_520);
    stringBuffer.append(report.getSignerImage());
    stringBuffer.append(_521);
    stringBuffer.append(report.getID(pgpPublicKey));
    stringBuffer.append(_274);
    stringBuffer.append(report.getID(pgpPublicKey));
    stringBuffer.append(_17);
    }
    stringBuffer.append(_522);
    stringBuffer.append(report.getKeyServerURL(pgpPublicKey));
    stringBuffer.append(_282);
    stringBuffer.append(fingerPrint);
    stringBuffer.append(_60);
    stringBuffer.append(uid);
    stringBuffer.append(_523);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_524);
    stringBuffer.append(id);
    stringBuffer.append(_289);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
    stringBuffer.append(_525);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_291);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_287);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_526);
    }
    stringBuffer.append(_527);
    }
    stringBuffer.append(_455);
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
    stringBuffer.append(_528);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_529);
    stringBuffer.append(allCertificates.size());
    stringBuffer.append(_82);
    if (unsigned != null) {
    stringBuffer.append(_467);
    stringBuffer.append(unsigned.size());
    stringBuffer.append(_9);
    }
    if (invalidSignatureCount > 0) {
    stringBuffer.append(_467);
    stringBuffer.append(invalidSignatureCount);
    stringBuffer.append(_5);
    }
    {
        StringBuilder onClick = new StringBuilder();
        for (int i = 0, size = allCertificates.size(); i < size; ++i) {
          String id = "certificates" + ++idCount;
          onClick.append("expand2('certificates_all_arrows', '").append(id).append("');");
        }
        idCount = 0;
    stringBuffer.append(_530);
    stringBuffer.append(displayButton);
    stringBuffer.append(_51);
    stringBuffer.append(onClick);
    stringBuffer.append(_290);
    }
    stringBuffer.append(_531);
    stringBuffer.append(display);
    stringBuffer.append(_49);
    for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : allCertificates.entrySet()) {
        List<Certificate> certificates = entry.getKey();
        Map<String, IInstallableUnit> invalidSignatures = allInvalidSignatures.get(certificates);
        String id = "certificates" + ++idCount;
    stringBuffer.append(_532);
    if (certificates.isEmpty()) {
    stringBuffer.append(_533);
    stringBuffer.append(id);
    stringBuffer.append(_296);
    stringBuffer.append(id);
    stringBuffer.append(_501);
    stringBuffer.append(report.getSignedImage(false));
    stringBuffer.append(_534);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_535);
    } else {
    int count = 0;
          for (Certificate certificate : certificates) {
            Map<String, String> components = report.getCertificateComponents(certificate);
    stringBuffer.append(_471);
    stringBuffer.append(count++ + 2);
    stringBuffer.append(_329);
    if (count == 1) {
    stringBuffer.append(_536);
    stringBuffer.append(id);
    stringBuffer.append(_296);
    stringBuffer.append(id);
    stringBuffer.append(_22);
    } else {
    stringBuffer.append(_537);
    }
    stringBuffer.append(_538);
    stringBuffer.append(report.getSignedImage(true));
    stringBuffer.append(_286);
    if (count == 1) {
    stringBuffer.append(_539);
    stringBuffer.append(entry.getValue().size());
    stringBuffer.append(_27);
    if (report.isReportingPackages()) {
    stringBuffer.append(_520);
    stringBuffer.append(report.getSignerImage());
    stringBuffer.append(_540);
    stringBuffer.append(report.getID(certificates));
    stringBuffer.append(_274);
    stringBuffer.append(report.getID(certificates));
    stringBuffer.append(_19);
    }
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
    stringBuffer.append(_473);
    stringBuffer.append(spanStyle);
    stringBuffer.append(_247);
    stringBuffer.append(keyStyle);
    stringBuffer.append(_335);
    stringBuffer.append(key);
    stringBuffer.append(_244);
    stringBuffer.append(value);
    stringBuffer.append(_82);
    }
    if (count == 1 && !invalidSignatures.isEmpty()) {
    stringBuffer.append(_541);
    stringBuffer.append(invalidSignatures.size());
    stringBuffer.append(_11);
    }
    stringBuffer.append(_474);
    }
    }
    stringBuffer.append(_542);
    stringBuffer.append(id);
    stringBuffer.append(_289);
    Map<String, IInstallableUnit> artifacts = entry.getValue();
        for (Map.Entry<String, IInstallableUnit> artifact : artifacts.entrySet()) {
          String style = invalidSignatures != null && invalidSignatures.entrySet().contains(artifact) ? "style='text-decoration: line-through;'" : "";
    stringBuffer.append(_525);
    stringBuffer.append(report.getRelativeIUReportURL(artifact.getValue()));
    stringBuffer.append(_268);
    stringBuffer.append(style);
    stringBuffer.append(_246);
    stringBuffer.append(report.getIUImage(artifact.getValue()));
    stringBuffer.append(_287);
    stringBuffer.append(artifact.getKey());
    stringBuffer.append(_526);
    }
    stringBuffer.append(_527);
    }
    stringBuffer.append(_455);
    }
    stringBuffer.append(_543);
    Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
    if (!featureProviders.isEmpty()) {
      int nonEclipse = 0;
      for (String provider : featureProviders.keySet()) {
        if (!provider.toLowerCase().contains("eclipse"))
          ++nonEclipse;
      }
    stringBuffer.append(_544);
    stringBuffer.append(report.getProviderImage());
    stringBuffer.append(_545);
    stringBuffer.append(featureProviders.size());
    stringBuffer.append(_82);
    if (nonEclipse != 0) {
    stringBuffer.append(_467);
    stringBuffer.append(nonEclipse);
    stringBuffer.append(_7);
    }
    stringBuffer.append(_546);
    int count = 0;
      for (Map.Entry<String, Set<IInstallableUnit>> provider : featureProviders.entrySet()) {
        String style = !provider.getKey().toLowerCase().contains("eclipse") ? " style=\"color: FireBrick;\"" : "";
        Set<IInstallableUnit> features = provider.getValue();
        String id = "nested_feature_providers" + ++count;
    stringBuffer.append(_547);
    stringBuffer.append(id);
    stringBuffer.append(_294);
    stringBuffer.append(id);
    stringBuffer.append(_22);
    for (String image : report.getBrandingImages(features)) {
    stringBuffer.append(_548);
    stringBuffer.append(image);
    stringBuffer.append(_286);
    }
    stringBuffer.append(_549);
    stringBuffer.append(style);
    stringBuffer.append(_245);
    stringBuffer.append(provider.getKey());
    stringBuffer.append(_550);
    stringBuffer.append(features.size());
    stringBuffer.append(_551);
    stringBuffer.append(id);
    stringBuffer.append(_279);
    for (IInstallableUnit feature : features) {
          String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_552);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_553);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_286);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_554);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_555);
    }
    stringBuffer.append(_556);
    }
    stringBuffer.append(_455);
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
    stringBuffer.append(_557);
    stringBuffer.append(report.getFeatureImage());
    stringBuffer.append(_558);
    stringBuffer.append(features.size());
    stringBuffer.append(_82);
    if (brokenBranding != 0) {
    stringBuffer.append(_467);
    stringBuffer.append(brokenBranding);
    stringBuffer.append(_1);
    }
    if (noBranding != 0) {
    stringBuffer.append(_467);
    stringBuffer.append(noBranding);
    stringBuffer.append(_6);
    }
    stringBuffer.append(_559);
    for (IInstallableUnit feature : features) {
        String brandingImage = report.getBrandingImage(feature);
    stringBuffer.append(_560);
    stringBuffer.append(report.getRelativeIUReportURL(feature));
    stringBuffer.append(_561);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_286);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(feature, true), true));
    stringBuffer.append(_562);
    stringBuffer.append(report.getVersion(feature));
    stringBuffer.append(_563);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        for (LicenseDetail license : report.getLicenses(feature)) {
          String id = license.getUUID();
          licenseReplacements.put(">" + id, "><button class='bb search-for-me' style='" + helper.getStyle(license) + "' onclick=\"clickOnButton('lic_" + id
              + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_564);
    stringBuffer.append(id);
    stringBuffer.append(_271);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_275);
    stringBuffer.append(id);
    stringBuffer.append(_292);
    stringBuffer.append(id);
    stringBuffer.append(_293);
    stringBuffer.append(id);
    stringBuffer.append(_21);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_68);
    }
    stringBuffer.append(_565);
    }
    stringBuffer.append(_455);
    }
    Collection<IInstallableUnit> products = report.getProducts();
    if (!products.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_566);
    stringBuffer.append(report.getProductImage());
    stringBuffer.append(_567);
    stringBuffer.append(products.size());
    stringBuffer.append(_82);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit product : products) {
          String productID = "_product_" + report.getIUID(product);
          onClick.append("expand2('products_all_arrows', '").append(productID).append("');");
        }
    stringBuffer.append(_568);
    stringBuffer.append(displayButton);
    stringBuffer.append(_54);
    stringBuffer.append(onClick);
    stringBuffer.append(_290);
    }
    stringBuffer.append(_569);
    stringBuffer.append(display);
    stringBuffer.append(_48);
    for (IInstallableUnit product : products) {
        String productImage = report.getIUImage(product);
        String productID = "_product_" + report.getIUID(product);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(product));
    stringBuffer.append(_570);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_536);
    stringBuffer.append(productID);
    stringBuffer.append(_296);
    stringBuffer.append(productID);
    stringBuffer.append(_22);
    }
    stringBuffer.append(_571);
    stringBuffer.append(report.getRelativeIUReportURL(product));
    stringBuffer.append(_561);
    stringBuffer.append(productImage);
    stringBuffer.append(_286);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(product, true), true));
    stringBuffer.append(_562);
    stringBuffer.append(report.getVersion(product));
    stringBuffer.append(_563);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_572);
    stringBuffer.append(productID);
    stringBuffer.append(_278);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_552);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_573);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_286);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_554);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_555);
    }
    stringBuffer.append(_574);
    }
    stringBuffer.append(_565);
    }
    stringBuffer.append(_455);
    }
    Collection<IInstallableUnit> categories = report.getCategories();
    if (!categories.isEmpty()) {
      boolean isInitiallyExpanded = false;
      String display = isInitiallyExpanded ? "block" : "none";
      String displayButton = isInitiallyExpanded ? "inline" : "none";
    stringBuffer.append(_575);
    stringBuffer.append(report.getCategoryImage());
    stringBuffer.append(_576);
    stringBuffer.append(categories.size());
    stringBuffer.append(_82);
    {
        StringBuilder onClick = new StringBuilder();
        for (IInstallableUnit category : categories) {
          String categoryID = "_category_" + report.getIUID(category);
          onClick.append("expand2('categories_all_arrows', '").append(categoryID).append("');");
        }
    stringBuffer.append(_577);
    stringBuffer.append(displayButton);
    stringBuffer.append(_50);
    stringBuffer.append(onClick);
    stringBuffer.append(_290);
    }
    stringBuffer.append(_578);
    stringBuffer.append(display);
    stringBuffer.append(_48);
    for (IInstallableUnit category : categories) {
        String categoryImage = report.getIUImage(category);
        String categoryID = "_category_" + report.getIUID(category);
        Set<IInstallableUnit> requiredIUs = report.getSortedByName(report.getResolvedRequirements(category));
    stringBuffer.append(_570);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_536);
    stringBuffer.append(categoryID);
    stringBuffer.append(_296);
    stringBuffer.append(categoryID);
    stringBuffer.append(_22);
    }
    stringBuffer.append(_571);
    stringBuffer.append(report.getRelativeIUReportURL(category));
    stringBuffer.append(_561);
    stringBuffer.append(categoryImage);
    stringBuffer.append(_286);
    stringBuffer.append(NL_14);
    stringBuffer.append(helper.htmlEscape(report.getName(category, true), true));
    stringBuffer.append(_579);
    if (!requiredIUs.isEmpty()) {
    stringBuffer.append(_572);
    stringBuffer.append(categoryID);
    stringBuffer.append(_281);
    for (IInstallableUnit requiredIU : requiredIUs) {
    stringBuffer.append(_552);
    stringBuffer.append(report.getRelativeIUReportURL(requiredIU));
    stringBuffer.append(_573);
    stringBuffer.append(report.getIUImage(requiredIU));
    stringBuffer.append(_286);
    stringBuffer.append(NL_17);
    stringBuffer.append(helper.htmlEscape(report.getName(requiredIU, true), true));
    stringBuffer.append(_554);
    stringBuffer.append(report.getVersion(requiredIU));
    stringBuffer.append(_555);
    }
    stringBuffer.append(_574);
    }
    stringBuffer.append(_565);
    }
    stringBuffer.append(_455);
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
    stringBuffer.append(_580);
    stringBuffer.append(report.getBundleImage());
    stringBuffer.append(_581);
    stringBuffer.append(ius.size());
    stringBuffer.append(_82);
    if (duplicateCount > 0) {
    stringBuffer.append(_467);
    stringBuffer.append(duplicateCount);
    stringBuffer.append(_12);
    }
    stringBuffer.append(_582);
    if (duplicateCount > 0 || !unsignedIUs.isEmpty() || !badProviderIUs.isEmpty() || !badLicenseIUs.isEmpty() || !brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_583);
    if (!badLicenseIUs.isEmpty()) {
    stringBuffer.append(_584);
    }
    if (!unsignedIUs.isEmpty()) {
    stringBuffer.append(_585);
    }
    if (!badProviderIUs.isEmpty()) {
    stringBuffer.append(_586);
    }
    if (!brokenBrandingIUs.isEmpty()) {
    stringBuffer.append(_587);
    }
    if (duplicateCount > 0) {
    stringBuffer.append(_588);
    }
    stringBuffer.append(_589);
    }
    stringBuffer.append(_590);
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
    stringBuffer.append(_591);
    stringBuffer.append(id);
    stringBuffer.append(_272);
    stringBuffer.append(classNames);
    stringBuffer.append(_592);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_593);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_594);
    stringBuffer.append(iuID);
    stringBuffer.append(_595);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_289);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_596);
    Map<String, String> licenseReplacements = new HashMap<String, String>();
        if (report.isFeature(iu)) {
          for (LicenseDetail license : report.getLicenses(iu)) {
            String licenseID = license.getUUID();
            licenseReplacements.put(">" + licenseID, "><button class='bb search-for-me' style='" + helper.getStyle(license)
                + "' onclick=\"clickOnButton('lic_" + licenseID + "');\">" + license.getVersion() + "</button>");
    stringBuffer.append(_597);
    stringBuffer.append(licenseID);
    stringBuffer.append(_271);
    stringBuffer.append(helper.getStyle(license));
    stringBuffer.append(_275);
    stringBuffer.append(licenseID);
    stringBuffer.append(_292);
    stringBuffer.append(licenseID);
    stringBuffer.append(_293);
    stringBuffer.append(licenseID);
    stringBuffer.append(_21);
    stringBuffer.append(license.getVersion());
    stringBuffer.append(_68);
    }
    }
    Map<String, Boolean> artifacts = report.getIUArtifacts(iu);
        for (Map.Entry<String, Boolean> entry : artifacts.entrySet()) {
          String artifact = entry.getKey();
          Boolean signed = entry.getValue();
    stringBuffer.append(_598);
    if (signed != null) {
    stringBuffer.append(_599);
    stringBuffer.append(report.getSignedImage(signed));
    stringBuffer.append(_286);
    }
    stringBuffer.append(_599);
    stringBuffer.append(report.getArtifactImage(artifact));
    stringBuffer.append(_600);
    stringBuffer.append(report.getArtifactSize(artifact));
    stringBuffer.append(_601);
    }
    String brandingImage = report.getBrandingImage(iu);
        if (brandingImage != null) {
    stringBuffer.append(_602);
    stringBuffer.append(brandingImage);
    stringBuffer.append(_288);
    }
    stringBuffer.append(_603);
    }
    stringBuffer.append(_604);
    }
    Collection<IProvidedCapability> packages = report.getAllPackages();
    if (!packages.isEmpty()) {
      Set<IProvidedCapability> splitPackages = report.getSplitPackages();
      Set<IProvidedCapability> inconsistentJarSignatures = report.getInconsistentJarSignatures();
    stringBuffer.append(_605);
    stringBuffer.append(report.getPackageImage());
    stringBuffer.append(_606);
    stringBuffer.append(packages.size());
    stringBuffer.append(_82);
    if (splitPackages.size() > 0) {
    stringBuffer.append(_607);
    stringBuffer.append(splitPackages.size());
    stringBuffer.append(_8);
    }
    if (inconsistentJarSignatures.size() > 0) {
    stringBuffer.append(_608);
    stringBuffer.append(inconsistentJarSignatures.size());
    stringBuffer.append(_2);
    }
    {
        StringBuilder onClick = new StringBuilder();
        for (int i = 1, size = packages.size(); i <= size; ++i) {
          String id = "nested_package_providers" + i;
          onClick.append("expand2('packages_all_arrows', '").append(id).append("');");
        }
    stringBuffer.append(_609);
    stringBuffer.append(onClick);
    stringBuffer.append(_290);
    }
    stringBuffer.append(_610);
    if (splitPackages.size() > 0) {
    stringBuffer.append(_611);
    if (!inconsistentJarSignatures.isEmpty()) {
    stringBuffer.append(_612);
    }
    stringBuffer.append(_589);
    }
    stringBuffer.append(_590);
    int count = 0;
      for (IProvidedCapability providedPackage : packages) {
        String providedCapabilityID = providedPackage.getName();
        String id = report.getProvidedCapabilityID(providedPackage);
        String versionStyle = "";
        StringBuilder classNames = new StringBuilder();
        if (splitPackages.contains(providedPackage)) {
          classNames.append(" split-package");
        }
        if (inconsistentJarSignatures.contains(providedPackage)) {
          classNames.append(" inconsistent-signatures");
        }
        String buttonId = "nested_package_providers" + ++count;
        //
    stringBuffer.append(_591);
    stringBuffer.append(id);
    stringBuffer.append(_273);
    stringBuffer.append(classNames);
    stringBuffer.append(_613);
    stringBuffer.append(buttonId);
    stringBuffer.append(_294);
    stringBuffer.append(buttonId);
    stringBuffer.append(_614);
    stringBuffer.append(report.getPackageImage());
    stringBuffer.append(_615);
    stringBuffer.append(providedCapabilityID);
    stringBuffer.append(_616);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_289);
    stringBuffer.append(report.getVersion(providedPackage));
    stringBuffer.append(_617);
    stringBuffer.append(buttonId);
    stringBuffer.append(_279);
    for (IInstallableUnit iu : report.getInstallableUnits(providedPackage)) {
    stringBuffer.append(_618);
    stringBuffer.append(report.getRelativeIUReportURL(iu));
    stringBuffer.append(_619);
    stringBuffer.append(report.getIUImage(iu));
    stringBuffer.append(_620);
    stringBuffer.append(iu.getId());
    stringBuffer.append(_621);
    stringBuffer.append(versionStyle);
    stringBuffer.append(_289);
    stringBuffer.append(report.getVersion(iu));
    stringBuffer.append(_622);
    Set<List<Certificate>> certificates = report.getCertificates(iu);
          Set<PGPPublicKey> iuPGPKeys = report.getPGPKeys(iu);
          if (!certificates.isEmpty() || !iuPGPKeys.isEmpty()) {
    stringBuffer.append(_623);
    for (PGPPublicKey key : iuPGPKeys) {
              String keyID = report.getID(key);
    stringBuffer.append(_624);
    stringBuffer.append(keyID);
    stringBuffer.append(_277);
    stringBuffer.append(keyID);
    stringBuffer.append(_16);
    }
    for (List<Certificate> certificateChain : certificates) {
              String certID = report.getID(certificateChain);
    stringBuffer.append(_625);
    stringBuffer.append(certID);
    stringBuffer.append(_276);
    stringBuffer.append(certID);
    stringBuffer.append(_18);
    }
    stringBuffer.append(_626);
    }
    stringBuffer.append(_627);
    }
    stringBuffer.append(_628);
    }
    stringBuffer.append(_604);
    }
    stringBuffer.append(_629);
    }
    stringBuffer.append(_630);
    return stringBuffer.toString();
  }
}
