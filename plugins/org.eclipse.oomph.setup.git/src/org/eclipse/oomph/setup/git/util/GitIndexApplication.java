/*
 * Copyright (c) 2023 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Edt Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.git.util;

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.log.ProgressLogMonitor;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.egit.core.EclipseGitProgressTransformer;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jgit.api.Git;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Ed Merks
 */
@SuppressWarnings("nls")
public class GitIndexApplication implements IApplication
{
  private static final Pattern GITHUB_REPO_PATTERN = Pattern.compile("https://github.com/(([^/]+)/(.*))");

  private static final Pattern GIT_ECLIPSE_REPO_PATTERN = Pattern.compile("https://git.eclipse.org/r/(([^/]+)/(.*))");

  private static final Pattern GITLAB_ECLIPSE_REPO_PATTERN = Pattern.compile("https://gitlab.eclipse.org/(([^/]+)/(.*))");

  private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([^;]+)\\s*;");

  private static Set<String> TEST_REPOSITORIES = new TreeSet<>(Set.of( //
      "https://git.eclipse.org/r/jgit/jgit", //
      "https://github.com/eclipse-emf/org.eclipse.emf", //
      "https://gitlab.eclipse.org/eclipse/xpand/org.eclipse.xpand" //
  ));

  private static Set<String> REPOSITORIES = new TreeSet<>(Set.of( //
      // generated-repositories
      "https://git.eclipse.org/r/apogy/apogy", //
      "https://git.eclipse.org/r/bpmn2-modeler/org.eclipse.bpmn2-modeler", //
      "https://git.eclipse.org/r/bpmn2/org.eclipse.bpmn2", //
      "https://git.eclipse.org/r/capra/org.eclipse.capra", //
      "https://git.eclipse.org/r/dali/webtools.dali", //
      "https://git.eclipse.org/r/dltk/org.eclipse.dltk.core", //
      "https://git.eclipse.org/r/dltk/org.eclipse.dltk.javascript", //
      "https://git.eclipse.org/r/dltk/org.eclipse.dltk.python", //
      "https://git.eclipse.org/r/dltk/org.eclipse.dltk.releng", //
      "https://git.eclipse.org/r/dltk/org.eclipse.dltk.ruby", //
      "https://git.eclipse.org/r/dltk/org.eclipse.dltk.sh", //
      "https://git.eclipse.org/r/dltk/org.eclipse.dltk.tcl", //
      "https://git.eclipse.org/r/edapt/org.eclipse.emf.edapt", //
      "https://git.eclipse.org/r/eef/org.eclipse.eef", //
      "https://git.eclipse.org/r/efm/org.eclipse.efm-hibou", //
      "https://git.eclipse.org/r/efm/org.eclipse.efm-modeling", //
      "https://git.eclipse.org/r/efm/org.eclipse.efm-symbex", //
      "https://git.eclipse.org/r/egerrit/org.eclipse.egerrit", //
      "https://git.eclipse.org/r/egit/egit", //
      "https://git.eclipse.org/r/emfatic/org.eclipse.emfatic", //
      "https://git.eclipse.org/r/emfclient/org.eclipse.emf.ecp.core", //
      "https://git.eclipse.org/r/gemini.dbaccess/org.eclipse.gemini.dbaccess", //
      "https://git.eclipse.org/r/gemini.jpa/org.eclipse.gemini.jpa", //
      "https://git.eclipse.org/r/gemini.management/org.eclipse.gemini.managment", //
      "https://git.eclipse.org/r/gemini.naming/org.eclipse.gemini.naming", //
      "https://git.eclipse.org/r/gemini.web/org.eclipse.gemini.web.gemini-web-container", //
      "https://git.eclipse.org/r/gendoc/org.eclipse.gendoc", //
      "https://git.eclipse.org/r/handly/org.eclipse.handly", //
      "https://git.eclipse.org/r/hawk/hawk", //
      "https://git.eclipse.org/r/henshin/org.eclipse.emft.henshin", //
      "https://git.eclipse.org/r/jeetools/webtools.javaee", //
      "https://git.eclipse.org/r/jgit/jgit", //
      "https://git.eclipse.org/r/jsf/webtools.jsf", //
      "https://git.eclipse.org/r/jsf/webtools.jsf.docs", //
      "https://git.eclipse.org/r/jsf/webtools.jsf.tests", //
      "https://git.eclipse.org/r/jubula/org.eclipse.jubula.core", //
      "https://git.eclipse.org/r/ldt/org.eclipse.ldt", //
      "https://git.eclipse.org/r/ldt/org.eclipse.metalua", //
      "https://git.eclipse.org/r/mat/org.eclipse.mat", //
      "https://git.eclipse.org/r/mdht/org.eclipse.mdht", //
      "https://git.eclipse.org/r/mmt/org.eclipse.atl", //
      "https://git.eclipse.org/r/mmt/org.eclipse.atl.tcs", //
      "https://git.eclipse.org/r/mmt/org.eclipse.qvtd", //
      "https://git.eclipse.org/r/mmt/org.eclipse.qvto", //
      "https://git.eclipse.org/r/modisco/org.eclipse.modisco", //
      "https://git.eclipse.org/r/mpc/org.eclipse.epp.mpc", //
      "https://git.eclipse.org/r/nattable/org.eclipse.nebula.widgets.nattable", //
      "https://git.eclipse.org/r/objectteams/org.eclipse.objectteams", //
      "https://git.eclipse.org/r/ocl/org.eclipse.ocl", //
      "https://git.eclipse.org/r/osee/org.eclipse.osee", //
      "https://git.eclipse.org/r/osee/org.eclipse.ote", //
      "https://git.eclipse.org/r/papyrus-rt/org.eclipse.papyrus-rt", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-bpmn", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-collaborativemodeling", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-designer", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-informationmodeling", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-interoperability", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-manufacturing", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-marte", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-moka", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-requirements", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-robotics", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-robotml", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-sysml", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus-sysml11", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus.incubation", //
      "https://git.eclipse.org/r/papyrus/org.eclipse.papyrus.tools", //
      "https://git.eclipse.org/r/ptp/org.eclipse.photran", //
      "https://git.eclipse.org/r/refactor/org.eclipse.emf.refactor.core", //
      "https://git.eclipse.org/r/refactor/org.eclipse.emf.refactor.examples", //
      "https://git.eclipse.org/r/refactor/org.eclipse.emf.refactor.metrics", //
      "https://git.eclipse.org/r/refactor/org.eclipse.emf.refactor.refactoring", //
      "https://git.eclipse.org/r/refactor/org.eclipse.emf.refactor.smells", //
      "https://git.eclipse.org/r/rtsc/org.eclipse.rtsc.committer", //
      "https://git.eclipse.org/r/rtsc/org.eclipse.rtsc.contrib", //
      "https://git.eclipse.org/r/rtsc/org.eclipse.rtsc.test", //
      "https://git.eclipse.org/r/rtsc/org.eclipse.rtsc.training", //
      "https://git.eclipse.org/r/rtsc/org.eclipse.rtsc.xdccore", //
      "https://git.eclipse.org/r/servertools/webtools.servertools", //
      "https://git.eclipse.org/r/servertools/webtools.servertools.devsupport", //
      "https://git.eclipse.org/r/servertools/webtools.servertools.docs", //
      "https://git.eclipse.org/r/servertools/webtools.servertools.tests", //
      "https://git.eclipse.org/r/skills/org.eclipse.skills", //
      "https://git.eclipse.org/r/sourceediting/webtools.sourceediting", //
      "https://git.eclipse.org/r/sourceediting/webtools.sourceediting.tests", //
      "https://git.eclipse.org/r/sourceediting/webtools.sourceediting.xpath", //
      "https://git.eclipse.org/r/sourceediting/webtools.sourceediting.xpath.tests", //
      "https://git.eclipse.org/r/sourceediting/webtools.sourceediting.xsl", //
      "https://git.eclipse.org/r/sourceediting/webtools.sourceediting.xsl.tests", //
      "https://git.eclipse.org/r/sphinx/org.eclipse.sphinx", //
      "https://git.eclipse.org/r/statet/org.eclipse.statet", //
      "https://git.eclipse.org/r/statet/org.eclipse.statet-commons", //
      "https://git.eclipse.org/r/statet/org.eclipse.statet-docmlet", //
      "https://git.eclipse.org/r/statet/org.eclipse.statet-eutils", //
      "https://git.eclipse.org/r/statet/org.eclipse.statet-ltk", //
      "https://git.eclipse.org/r/statet/org.eclipse.statet-r", //
      "https://git.eclipse.org/r/statet/org.eclipse.statet-rj", //
      "https://git.eclipse.org/r/stem/org.eclipse.stem", //
      "https://git.eclipse.org/r/stem/org.eclipse.stem.data", //
      "https://git.eclipse.org/r/stem/org.eclipse.stem.data.earthscience", //
      "https://git.eclipse.org/r/swtbot/org.eclipse.swtbot", //
      "https://git.eclipse.org/r/tcf/org.eclipse.tcf", //
      "https://git.eclipse.org/r/tcf/org.eclipse.tcf.agent", //
      "https://git.eclipse.org/r/tea/tea", //
      "https://git.eclipse.org/r/tigerstripe/org.eclipse.tigerstripe", //
      "https://git.eclipse.org/r/tm/org.eclipse.tm", //
      "https://git.eclipse.org/r/tracecompass/org.eclipse.tracecompass", //
      "https://git.eclipse.org/r/uml2/org.eclipse.uml2", //
      "https://git.eclipse.org/r/uomo/org.eclipse.uomo", //
      "https://git.eclipse.org/r/usssdk/org.eclipse.usssdk", //
      "https://git.eclipse.org/r/viatra/org.eclipse.viatra", //
      "https://git.eclipse.org/r/viatra/org.eclipse.viatra.examples", //
      "https://git.eclipse.org/r/viatra/org.eclipse.viatra.modelobfuscator", //
      "https://git.eclipse.org/r/viatra/org.eclipse.viatra2.vpm", //
      "https://git.eclipse.org/r/webservices/webtools.webservices", //
      "https://git.eclipse.org/r/webservices/webtools.webservices.axis2", //
      "https://git.eclipse.org/r/webservices/webtools.webservices.jaxws", //
      "https://git.eclipse.org/r/webtools-common/webtools.common", //
      "https://git.eclipse.org/r/webtools-common/webtools.common.fproj", //
      "https://git.eclipse.org/r/webtools-common/webtools.common.snippets", //
      "https://git.eclipse.org/r/webtools-common/webtools.common.tests", //
      "https://git.eclipse.org/r/webtools/org.eclipse.webtools.java-ee-config", //
      "https://git.eclipse.org/r/webtools/webtools.maps", //
      "https://git.eclipse.org/r/webtools/webtools.releng", //
      "https://git.eclipse.org/r/webtools/webtools.releng.aggregator", //
      "https://git.eclipse.org/r/xwt/org.eclipse.xwt", //
      "https://github.com/deeplearning4j/deeplearning4j", //
      "https://github.com/deeplearning4j/deeplearning4j-docs", //
      "https://github.com/deeplearning4j/deeplearning4j-examples", //
      "https://github.com/eclipse-acceleo/acceleo", //
      "https://github.com/eclipse-actf/org.eclipse.actf", //
      "https://github.com/eclipse-actf/org.eclipse.actf.ai", //
      "https://github.com/eclipse-actf/org.eclipse.actf.common", //
      "https://github.com/eclipse-actf/org.eclipse.actf.examples", //
      "https://github.com/eclipse-actf/org.eclipse.actf.visualization", //
      "https://github.com/eclipse-actf/org.eclipse.actf.visualization.releng", //
      "https://github.com/eclipse-aspectj/ajdt", //
      "https://github.com/eclipse-aspectj/aspectj", //
      "https://github.com/eclipse-aspectj/eclipse.jdt.core", //
      "https://github.com/eclipse-babel/plugins", //
      "https://github.com/eclipse-babel/server", //
      "https://github.com/eclipse-birt/birt", //
      "https://github.com/eclipse-cbi/ansible-playbooks", //
      "https://github.com/eclipse-cbi/best-practices", //
      "https://github.com/eclipse-cbi/buildkitd-okd", //
      "https://github.com/eclipse-cbi/ci-admin", //
      "https://github.com/eclipse-cbi/dockerfiles", //
      "https://github.com/eclipse-cbi/dockertools", //
      "https://github.com/eclipse-cbi/eclipse-cbi-tycho-example", //
      "https://github.com/eclipse-cbi/epl-license-feature", //
      "https://github.com/eclipse-cbi/hipp2jipp", //
      "https://github.com/eclipse-cbi/jiro", //
      "https://github.com/eclipse-cbi/jiro-agents", //
      "https://github.com/eclipse-cbi/jiro-dashboard", //
      "https://github.com/eclipse-cbi/jiro-masters", //
      "https://github.com/eclipse-cbi/jiro-static-agents", //
      "https://github.com/eclipse-cbi/macos-notarization-service", //
      "https://github.com/eclipse-cbi/org.eclipse.cbi", //
      "https://github.com/eclipse-cbi/org.eclipse.cbi-testdata", //
      "https://github.com/eclipse-cbi/p2repo-aggregator", //
      "https://github.com/eclipse-cbi/p2repo-analyzers", //
      "https://github.com/eclipse-cbi/sonatype-nexus", //
      "https://github.com/eclipse-cbi/targetplatform-dsl", //
      "https://github.com/eclipse-cdo/cdo", //
      "https://github.com/eclipse-cdo/cdo.infrastructure", //
      "https://github.com/eclipse-cdo/cdo.old", //
      "https://github.com/eclipse-cdo/cdo.www", //
      "https://github.com/eclipse-cdt/cdt", //
      "https://github.com/eclipse-cdt/cdt-infra", //
      "https://github.com/eclipse-cdt/cdt-lsp", //
      "https://github.com/eclipse-cdt/cdt-new-managedbuild-prototype", //
      "https://github.com/eclipse-cdt/cdt-vscode", //
      "https://github.com/eclipse-cognicrypt/CogniCrypt", //
      "https://github.com/eclipse-corrosion/corrosion", //
      "https://github.com/eclipse-dataspacetck/cvf", //
      "https://github.com/eclipse-datatools/datatools", //
      "https://github.com/eclipse-ecoretools/ecoretools", //
      "https://github.com/eclipse-efx/efxclipse", //
      "https://github.com/eclipse-efx/efxclipse-drift", //
      "https://github.com/eclipse-efx/efxclipse-eclipse", //
      "https://github.com/eclipse-efx/efxclipse-rt", //
      "https://github.com/eclipse-emf-compare/emf-compare", //
      "https://github.com/eclipse-emf-compare/emf-compare-acceptance", //
      "https://github.com/eclipse-emf-compare/emf-compare-cli", //
      "https://github.com/eclipse-emf-compare/emf-compare-releng", //
      "https://github.com/eclipse-emf-parsley/emf-parsley", //
      "https://github.com/eclipse-emf/org.eclipse.emf", //
      "https://github.com/eclipse-emfstore/org.eclipse.emf.emfstore.core", //
      "https://github.com/eclipse-equinox/equinox", //
      "https://github.com/eclipse-equinox/equinox.bundles", //
      "https://github.com/eclipse-equinox/equinox.framework", //
      "https://github.com/eclipse-equinox/p2", //
      "https://github.com/eclipse-iceoryx/iceoryx", //
      "https://github.com/eclipse-iceoryx/iceoryx-automotive-soa", //
      "https://github.com/eclipse-iceoryx/iceoryx-gateway-dds", //
      "https://github.com/eclipse-iceoryx/iceoryx-project-template", //
      "https://github.com/eclipse-iceoryx/iceoryx-rs", //
      "https://github.com/eclipse-iceoryx/iceoryx-web", //
      "https://github.com/eclipse-jdt/eclipse.jdt", //
      "https://github.com/eclipse-jdt/eclipse.jdt.core", //
      "https://github.com/eclipse-jdt/eclipse.jdt.debug", //
      "https://github.com/eclipse-jdt/eclipse.jdt.ui", //
      "https://github.com/eclipse-jsdt/webtools.jsdt", //
      "https://github.com/eclipse-justj/justj", //
      "https://github.com/eclipse-justj/justj.tools", //
      "https://github.com/eclipse-linuxtools/org.eclipse.linuxtools", //
      "https://github.com/eclipse-linuxtools/org.eclipse.linuxtools.eclipse-build", //
      "https://github.com/eclipse-lsp4j/lsp4j", //
      "https://github.com/eclipse-m2e/m2e-core", //
      "https://github.com/eclipse-m2e/m2e-discovery-catalog", //
      "https://github.com/eclipse-m2e/m2e-wtp", //
      "https://github.com/eclipse-m2e/m2e-wtp-jpa", //
      "https://github.com/eclipse-m2e/org.eclipse.m2e.workspace", //
      "https://github.com/eclipse-mylyn/org.eclipse.mylyn", //
      "https://github.com/eclipse-mylyn/org.eclipse.mylyn.docs", //
      "https://github.com/eclipse-oomph/oomph", //
      "https://github.com/eclipse-orbit/ebr", //
      "https://github.com/eclipse-orbit/orbit", //
      "https://github.com/eclipse-orbit/orbit-simrel", //
      "https://github.com/eclipse-packaging/packages", //
      "https://github.com/eclipse-pde/eclipse.pde", //
      "https://github.com/eclipse-pde/eclipse.pde.build", //
      "https://github.com/eclipse-pdt/pdt", //
      "https://github.com/eclipse-platform/eclipse.platform", //
      "https://github.com/eclipse-platform/eclipse.platform.common", //
      "https://github.com/eclipse-platform/eclipse.platform.debug", //
      "https://github.com/eclipse-platform/eclipse.platform.images", //
      "https://github.com/eclipse-platform/eclipse.platform.releng", //
      "https://github.com/eclipse-platform/eclipse.platform.releng.aggregator", //
      "https://github.com/eclipse-platform/eclipse.platform.releng.buildtools", //
      "https://github.com/eclipse-platform/eclipse.platform.resources", //
      "https://github.com/eclipse-platform/eclipse.platform.runtime", //
      "https://github.com/eclipse-platform/eclipse.platform.swt", //
      "https://github.com/eclipse-platform/eclipse.platform.team", //
      "https://github.com/eclipse-platform/eclipse.platform.text", //
      "https://github.com/eclipse-platform/eclipse.platform.ua", //
      "https://github.com/eclipse-platform/eclipse.platform.ui", //
      "https://github.com/eclipse-platform/eclipse.platform.ui.tools", //
      "https://github.com/eclipse-ptp/ptp", //
      "https://github.com/eclipse-ptp/ptp.doc", //
      "https://github.com/eclipse-ptp/ptp.photran", //
      "https://github.com/eclipse-rap/org.eclipse.rap", //
      "https://github.com/eclipse-rap/org.eclipse.rap.tools", //
      "https://github.com/eclipse-rmf/org.eclipse.rmf", //
      "https://github.com/eclipse-rmf/org.eclipse.rmf.documentation", //
      "https://github.com/eclipse-scava/scava-datasets", //
      "https://github.com/eclipse-scout/scout.ci", //
      "https://github.com/eclipse-scout/scout.maven-master", //
      "https://github.com/eclipse-scout/scout.rt", //
      "https://github.com/eclipse-scout/scout.sdk", //
      "https://github.com/eclipse-serializer/serializer", //
      "https://github.com/eclipse-set/browser", //
      "https://github.com/eclipse-set/build", //
      "https://github.com/eclipse-set/model", //
      "https://github.com/eclipse-set/set", //
      "https://github.com/eclipse-set/toolboxmodel", //
      "https://github.com/eclipse-sirius/sirius-components", //
      "https://github.com/eclipse-sirius/sirius-desktop", //
      "https://github.com/eclipse-sirius/sirius-emf-json", //
      "https://github.com/eclipse-sirius/sirius-specs", //
      "https://github.com/eclipse-sirius/sirius-web", //
      "https://github.com/eclipse-store/docs-site", //
      "https://github.com/eclipse-store/store", //
      "https://github.com/eclipse-sw360/sw360", //
      "https://github.com/eclipse-sw360/sw360-frontend", //
      "https://github.com/eclipse-tomled/tomled", //
      "https://github.com/eclipse-tycho/tycho", //
      "https://github.com/eclipse-vertx/vert.x", //
      "https://github.com/eclipse-vertx/vertx-auth", //
      "https://github.com/eclipse-vertx/vertx-codegen", //
      "https://github.com/eclipse-vertx/vertx-grpc", //
      "https://github.com/eclipse-vertx/vertx-http-proxy", //
      "https://github.com/eclipse-vertx/vertx-json-schema", //
      "https://github.com/eclipse-vertx/vertx-junit5", //
      "https://github.com/eclipse-vertx/vertx-launcher", //
      "https://github.com/eclipse-vertx/vertx-openapi", //
      "https://github.com/eclipse-vertx/vertx-rabbitmq-client", //
      "https://github.com/eclipse-vertx/vertx-sql-client", //
      "https://github.com/eclipse-vertx/vertx-tracing", //
      "https://github.com/eclipse-vertx/vertx-uri-template", //
      "https://github.com/eclipse-virgo/virgo-bundlor", //
      "https://github.com/eclipse-virgo/virgo-ide", //
      "https://github.com/eclipse-virgo/virgo-root", //
      "https://github.com/eclipse-wildwebdeveloper/wildwebdeveloper", //
      "https://github.com/eclipse-windowbuilder/windowbuilder", //
      "https://github.com/eclipse-xpanse/xpanse", //
      "https://github.com/eclipse-xpanse/xpanse-iam", //
      "https://github.com/eclipse-xpanse/xpanse-relops", //
      "https://github.com/eclipse-xpanse/xpanse-ui", //
      "https://github.com/eclipse/Xpect", //
      "https://github.com/eclipse/aCute", //
      "https://github.com/eclipse/agileuml", //
      "https://github.com/eclipse/amalgam", //
      "https://github.com/eclipse/andmore", //
      "https://github.com/eclipse/buildship", //
      "https://github.com/eclipse/chemclipse", //
      "https://github.com/eclipse/dartboard", //
      "https://github.com/eclipse/dash-licenses", //
      "https://github.com/eclipse/dawnsci", //
      "https://github.com/eclipse/eavp", //
      "https://github.com/eclipse/ecf", //
      "https://github.com/eclipse/eclemma", //
      "https://github.com/eclipse/eclipse-collections", //
      "https://github.com/eclipse/eclipse-collections-kata", //
      "https://github.com/eclipse/eclipse.jdt.ls", //
      "https://github.com/eclipse/eclipsefuro", //
      "https://github.com/eclipse/eclipsefuro-web", //
      "https://github.com/eclipse/efbt", //
      "https://github.com/eclipse/elk", //
      "https://github.com/eclipse/elk-models", //
      "https://github.com/eclipse/emf-query", //
      "https://github.com/eclipse/emf-transaction", //
      "https://github.com/eclipse/emf-validation", //
      "https://github.com/eclipse/emf.egf", //
      "https://github.com/eclipse/epsilon", //
      "https://github.com/eclipse/gef", //
      "https://github.com/eclipse/gef-classic", //
      "https://github.com/eclipse/gemini.blueprint", //
      "https://github.com/eclipse/gemoc-studio", //
      "https://github.com/eclipse/gemoc-studio-commons", //
      "https://github.com/eclipse/gemoc-studio-execution-ale", //
      "https://github.com/eclipse/gemoc-studio-execution-java", //
      "https://github.com/eclipse/gemoc-studio-execution-moccml", //
      "https://github.com/eclipse/gemoc-studio-extras", //
      "https://github.com/eclipse/gemoc-studio-moccml", //
      "https://github.com/eclipse/gemoc-studio-modeldebugging", //
      "https://github.com/eclipse/gmf-notation", //
      "https://github.com/eclipse/gmf-runtime", //
      "https://github.com/eclipse/gsc-ec-converter", //
      "https://github.com/eclipse/ice", //
      "https://github.com/eclipse/imagen", //
      "https://github.com/eclipse/iottestware", //
      "https://github.com/eclipse/iottestware.coap", //
      "https://github.com/eclipse/iottestware.dashboard", //
      "https://github.com/eclipse/iottestware.fuzzing", //
      "https://github.com/eclipse/iottestware.mqtt", //
      "https://github.com/eclipse/iottestware.opcua", //
      "https://github.com/eclipse/iottestware.performance", //
      "https://github.com/eclipse/january", //
      "https://github.com/eclipse/january-forms", //
      "https://github.com/eclipse/jbom", //
      "https://github.com/eclipse/jemo", //
      "https://github.com/eclipse/jetty.alpn.api", //
      "https://github.com/eclipse/jetty.docker", //
      "https://github.com/eclipse/jetty.parent", //
      "https://github.com/eclipse/jetty.project", //
      "https://github.com/eclipse/jetty.toolchain", //
      "https://github.com/eclipse/jifa", //
      "https://github.com/eclipse/jnosql", //
      "https://github.com/eclipse/jnosql-communication-driver", //
      "https://github.com/eclipse/jnosql-extensions", //
      "https://github.com/eclipse/lemminx", //
      "https://github.com/eclipse/lemminx-maven", //
      "https://github.com/eclipse/lsp4e", //
      "https://github.com/eclipse/lsp4jakarta", //
      "https://github.com/eclipse/lsp4mp", //
      "https://github.com/eclipse/lyo", //
      "https://github.com/eclipse/lyo.adapter-magicdraw", //
      "https://github.com/eclipse/lyo.adapter-simulink", //
      "https://github.com/eclipse/lyo.designer", //
      "https://github.com/eclipse/lyo.docs", //
      "https://github.com/eclipse/lyo.oslc-ui", //
      "https://github.com/eclipse/lyo.testsuite", //
      "https://github.com/eclipse/mwe", //
      "https://github.com/eclipse/n4js", //
      "https://github.com/eclipse/n4js-tutorials", //
      "https://github.com/eclipse/nebula", //
      "https://github.com/eclipse/omr", //
      "https://github.com/eclipse/org.eclipse.emf.diffmerge.coevolution", //
      "https://github.com/eclipse/org.eclipse.emf.diffmerge.core", //
      "https://github.com/eclipse/org.eclipse.emf.diffmerge.patch", //
      "https://github.com/eclipse/org.eclipse.emf.diffmerge.patterns", //
      "https://github.com/eclipse/org.eclipse.rap.incubator.e4.compatibility.workbench", //
      "https://github.com/eclipse/org.eclipse.rcptt", //
      "https://github.com/eclipse/org.eclipse.riena", //
      "https://github.com/eclipse/org.eclipse.riena.3xtargets", //
      "https://github.com/eclipse/org.eclipse.riena.old", //
      "https://github.com/eclipse/org.eclipse.riena.plugins.archive", //
      "https://github.com/eclipse/org.eclipse.riena.rap", //
      "https://github.com/eclipse/org.eclipse.riena.setup", //
      "https://github.com/eclipse/org.eclipse.riena.toolbox", //
      "https://github.com/eclipse/org.eclipse.scout.docs", //
      "https://github.com/eclipse/org.eclipse.sensinact", //
      "https://github.com/eclipse/org.eclipse.sensinact.gateway", //
      "https://github.com/eclipse/org.eclipse.sensinact.studio", //
      "https://github.com/eclipse/org.eclipse.sensinact.studioweb", //
      "https://github.com/eclipse/packager", //
      "https://github.com/eclipse/poosl", //
      "https://github.com/eclipse/rdf4j", //
      "https://github.com/eclipse/rdf4j-doc", //
      "https://github.com/eclipse/rdf4j-storage", //
      "https://github.com/eclipse/rdf4j-testsuite", //
      "https://github.com/eclipse/rdf4j-tools", //
      "https://github.com/eclipse/reddeer", //
      "https://github.com/eclipse/repairnator", //
      "https://github.com/eclipse/richbeans", //
      "https://github.com/eclipse/scanning", //
      "https://github.com/eclipse/sequoyah", //
      "https://github.com/eclipse/shellwax", //
      "https://github.com/eclipse/sisu.inject", //
      "https://github.com/eclipse/sisu.mojos", //
      "https://github.com/eclipse/sisu.plexus", //
      "https://github.com/eclipse/smartmdsd", //
      "https://github.com/eclipse/steady", //
      "https://github.com/eclipse/swtchart", //
      "https://github.com/eclipse/texlipse", //
      "https://github.com/eclipse/tm4e", //
      "https://github.com/eclipse/transformer", //
      "https://github.com/eclipse/winery", //
      "https://github.com/eclipse/winery-topologymodeler", //
      "https://github.com/eclipse/xacc", //
      "https://github.com/eclipse/xsemantics", //
      "https://github.com/eclipse/xtext", //
      "https://github.com/eclipse/xtext-archive", //
      "https://github.com/osgi/jakartarest-osgi", //
      "https://github.com/osgi/osgi", //
      "https://github.com/osgi/osgi-test", //
      "https://github.com/osgi/osgi.enroute", //
      "https://github.com/osgi/osgi.enroute.site", //
      "https://github.com/osgi/slf4j-osgi", //
      "https://gitlab.eclipse.org/eclipse/amp/org.eclipse.amp", //
      "https://gitlab.eclipse.org/eclipse/asciidoc-lang/asciidoc-lang", //
      "https://gitlab.eclipse.org/eclipse/asciidoc-lang/asciidoc-tck", //
      "https://gitlab.eclipse.org/eclipse/austen/austen", //
      "https://gitlab.eclipse.org/eclipse/camf/org.eclipse.camf", //
      "https://gitlab.eclipse.org/eclipse/comma/comma", //
      "https://gitlab.eclipse.org/eclipse/dash/org.eclipse.dash.handbook", //
      "https://gitlab.eclipse.org/eclipse/ease/ease", //
      "https://gitlab.eclipse.org/eclipse/ease/ease-scripts", //
      "https://gitlab.eclipse.org/eclipse/escet/escet", //
      "https://gitlab.eclipse.org/eclipse/etrice/etrice", //
      "https://gitlab.eclipse.org/eclipse/gmf-tooling/org.eclipse.gmf-tooling", //
      "https://gitlab.eclipse.org/eclipse/gmf-tooling/org.eclipse.gmf-tooling.uml2tools", //
      "https://gitlab.eclipse.org/eclipse/gmf-tooling/org.eclipse.gmf-tooling.uml2tools.releng", //
      "https://gitlab.eclipse.org/eclipse/graphiti/graphiti", //
      "https://gitlab.eclipse.org/eclipse/hawk/hawk", //
      "https://gitlab.eclipse.org/eclipse/ice/ice", //
      "https://gitlab.eclipse.org/eclipse/jwt/org.eclipse.soa.jwt", //
      "https://gitlab.eclipse.org/eclipse/lsat/lsat", //
      "https://gitlab.eclipse.org/eclipse/mangrove/org.eclipse.mangrove", //
      "https://gitlab.eclipse.org/eclipse/mpc/org-eclipse-epp-mpc", //
      "https://gitlab.eclipse.org/eclipse/mpc/org.eclipse.epp.mpc", //
      "https://gitlab.eclipse.org/eclipse/mtj/org.eclipse.mtj", //
      "https://gitlab.eclipse.org/eclipse/ogee/org.eclipse.ogee", //
      "https://gitlab.eclipse.org/eclipse/osee/org.eclipse.ote", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-domainservices", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-web", //
      "https://gitlab.eclipse.org/eclipse/plato/bok", //
      "https://gitlab.eclipse.org/eclipse/plato/www", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.committer", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.contrib", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.test", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.training", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.xdccore", //
      "https://gitlab.eclipse.org/eclipse/set/set", //
      "https://gitlab.eclipse.org/eclipse/statet/statet", //
      "https://gitlab.eclipse.org/eclipse/subversive/subversive", //
      "https://gitlab.eclipse.org/eclipse/sw360/playground-git", //
      "https://gitlab.eclipse.org/eclipse/sw360/playground.git", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/dash-maven", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/dashboard", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/eclipse-api-for-java", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/eclipse-project-code", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/m4e-tools", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/org.eclipse.dash.handbook", //
      "https://gitlab.eclipse.org/eclipse/teneo/org.eclipse.emf.teneo", //
      "https://gitlab.eclipse.org/eclipse/texo/org.eclipse.emf.texo", //
      "https://gitlab.eclipse.org/eclipse/trace4cps/trace4cps", //
      "https://gitlab.eclipse.org/eclipse/upr/upr", //
      "https://gitlab.eclipse.org/eclipse/xfsc/authenticationauthorization", //
      "https://gitlab.eclipse.org/eclipse/xfsc/cam", //
      "https://gitlab.eclipse.org/eclipse/xfsc/del", //
      "https://gitlab.eclipse.org/eclipse/xfsc/integration", //
      "https://gitlab.eclipse.org/eclipse/xfsc/not", //
      "https://gitlab.eclipse.org/eclipse/xfsc/oaw", //
      "https://gitlab.eclipse.org/eclipse/xfsc/orc", //
      "https://gitlab.eclipse.org/eclipse/xpand/org.eclipse.xpand"
  // generated-repositories
  ));

  private final Map<String, Map<String, Map<String, Map<String, Set<String>>>>> repositoryIndices = new TreeMap<>();

  private ContentHandler contentHandler;

  private File target;

  @Override
  public Object start(IApplicationContext context) throws Exception
  {
    var args = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);

    contentHandler = new ContentHandler(args[0]);
    target = new File(args[1]);
    index();

    return null;
  }

  @Override
  public void stop()
  {
  }

  public void index() throws Exception
  {
    var repositories = getRepositories();
    var count = 0;
    var size = repositories.size();
    for (var repo : repositories)
    {
      System.out.println("----------------" + ++count + " of " + size + " -------------------------");
      index(repo);
    }

    saveIndex(target);
  }

  public Set<String> getRepositories() throws Exception
  {
    if ("true".equals(System.getProperty("org.eclipse.oomph.setup.git.util.GitIndexApplication.test")))
    {
      return TEST_REPOSITORIES;
    }

    // The build server has a hard to getting github.api information.
    //
    if ("true".equals(System.getProperty("org.eclipse.oomph.setup.git.util.GitIndexApplication.static")))
    {
      return REPOSITORIES;
    }

    var projectsBaseURL = "https://projects.eclipse.org/api/projects/?pagesize=50&page=";
    var projectIDs = new TreeSet<String>();
    for (var i = 0; i < 50; ++i)
    {
      var pageURL = projectsBaseURL + i;
      System.out.println("Processing " + pageURL);
      var body = contentHandler.getContent(pageURL);
      if (body.length() < 10)
      {
        break;
      }

      var ids = getValues("project_id", body);
      projectIDs.addAll(ids);
    }

    projectIDs.removeIf(id -> {
      if (id.equals("automotive.sphinx"))
      {
        return false;
      }

      if (!id.contains("."))
      {
        return true;
      }

      if (id.startsWith("adoptium") //
          || id.startsWith("automotive") //
          || id.startsWith("dt") //
          || id.startsWith("ecd") //
          || id.startsWith("ee4j") //
          || id.startsWith("iot") //
          || id.startsWith("locationtech") //
          || id.startsWith("polarsys") //
          || id.startsWith("oniro") //
          || id.startsWith("technology.openj9") //
          || id.startsWith("tools.titan") //
          || id.startsWith("technology.microprofile") //
          || id.startsWith("technology.edc") //
          || id.startsWith("technology.openk") //
          || id.startsWith("eclipse.e4") //
          || id.startsWith("technology.osbp") //
          || id.startsWith("technology.graphene") //
          || id.startsWith("technology.pass") //
          || id.startsWith("tools.sequoyah") //
          || id.startsWith("openhw") //
          || id.startsWith("technology.recommenders"))
      {
        return true;
      }

      return false;
    });

    var repositories = Collections.synchronizedSet(new TreeSet<String>());
    projectIDs.parallelStream().forEach(projectID -> {
      try
      {
        repositories.addAll(getRepositories(projectID));
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
    });

    var update = System.getProperty("org.eclipse.oomph.setup.git.util.GitIndexApplication.update");
    if (update != null)
    {
      var self = Path.of(update);
      var content = Files.readString(self);
      Pattern GENERATED_REPOSITORIES_SECTION = Pattern.compile("( *// generated-repositories(\r?\n)).*?( *// generated-repositories\r?\n)", Pattern.DOTALL);
      var matcher = GENERATED_REPOSITORIES_SECTION.matcher(content);
      if (matcher.find())
      {
        var updatedContent = new StringBuilder();
        var literals = new ArrayList<String>();
        for (var repository : repositories)
        {
          literals.add("      \"" + Matcher.quoteReplacement(repository) + "\"");
        }

        matcher.appendReplacement(updatedContent, "$1" + String.join(", //" + matcher.group(2), literals) + "$2$3");
        matcher.appendTail(updatedContent);
        Files.writeString(self, updatedContent);
      }
    }

    return repositories;
  }

  public String getKey(String repo, String branch, StringBuilder baseRepo)
  {
    var githubMatcher = GITHUB_REPO_PATTERN.matcher(repo);
    if (githubMatcher.matches())
    {
      baseRepo.append(githubMatcher.group(1));
      return "https://github.com/${0}/tree/" + branch + "/${1} https://raw.githubusercontent.com/${0}/" + branch + "/${1}";
    }

    var gitEclipseMatcher = GIT_ECLIPSE_REPO_PATTERN.matcher(repo);
    if (gitEclipseMatcher.matches())
    {
      baseRepo.append(gitEclipseMatcher.group(1) + ".git");
      return "https://git.eclipse.org/c/${0}/tree/${1} https://git.eclipse.org/c/${0}/plain/${1}";
    }

    var gitlablEclipseMatcher = GITLAB_ECLIPSE_REPO_PATTERN.matcher(repo);
    if (gitlablEclipseMatcher.matches())
    {
      baseRepo.append(gitlablEclipseMatcher.group(1));
      return "https://gitlab.eclipse.org/${0}/-/blob/" + branch + "/${1} https://gitlab.eclipse.org/${0}/-/raw/" + branch + "/${1}";
    }

    throw new IllegalArgumentException(repo);
  }

  public void index(String repo) throws Exception
  {
    System.out.println("Cloning: " + repo);
    long start = System.currentTimeMillis();
    var cloneRepository = Git.cloneRepository();
    cloneRepository.setCloneSubmodules(false);
    cloneRepository.setURI(repo);
    cloneRepository.setDepth(1);
    var cloneFolder = Files.createTempDirectory("git-clone");
    cloneRepository.setDirectory(cloneFolder.toFile());
    cloneRepository.setProgressMonitor(new EclipseGitProgressTransformer(new ProgressLogMonitor(new ProgressLog()
    {
      @Override
      public boolean isCanceled()
      {
        return false;
      }

      @Override
      public void log(String line)
      {
        if (line.contains("%"))
        {
          if (line.contains("0%"))
          {
            System.out.println(line);
          }
        }
        else
        {
          System.out.println(line);
        }
      }

      @Override
      public void log(String line, Severity severity)
      {
      }

      @Override
      public void log(String line, boolean filter)
      {
      }

      @Override
      public void log(String line, boolean filter, Severity severity)
      {
      }

      @Override
      public void log(IStatus status)
      {
      }

      @Override
      public void log(Throwable t)
      {
      }

      @Override
      public void task(SetupTask setupTask)
      {
      }

      @Override
      public void setTerminating()
      {
      }
    })));

    var clone = cloneRepository.call();

    long finish = System.currentTimeMillis();

    System.out.println("Cloned: " + repo + " elapsed=" + (finish - start) / 1000);

    var repository = clone.getRepository();
    var branch = repository.getBranch();
    var baseRepo = new StringBuilder();
    var repositoryIndex = repositoryIndices.computeIfAbsent(getKey(repo, branch, baseRepo), key -> new TreeMap<>());
    var base = baseRepo.toString();
    var javaCount = new AtomicInteger();

    Files.walkFileTree(cloneFolder, new SimpleFileVisitor<Path>()
    {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException
      {
        var fileName = file.getFileName().toString();
        if (fileName.endsWith(".java"))
        {
          for (var cs : new Charset[] { StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_16 })
          {
            try (var in = Files.newBufferedReader(file, cs))
            {
              for (var line = in.readLine(); line != null; line = in.readLine())
              {
                var matcher = PACKAGE_PATTERN.matcher(line);
                if (matcher.find())
                {
                  var relativePath = cloneFolder.relativize(file.getParent()).toString().replace('\\', '/');
                  var packageName = matcher.group(1);
                  var className = fileName.substring(0, fileName.length() - ".java".length());
                  var packagePath = "/" + packageName.replace('.', '/');
                  if (relativePath.endsWith(packagePath))
                  {
                    var relativeBasePath = relativePath.substring(0, relativePath.length() - packagePath.length());
                    javaCount.incrementAndGet();
                    repositoryIndex.computeIfAbsent(base, key -> new TreeMap<>()).computeIfAbsent(relativeBasePath, key -> new TreeMap<>())
                        .computeIfAbsent(packageName, key -> new TreeSet<>()).add(className);
                  }

                  return FileVisitResult.CONTINUE;
                }
              }
            }
            catch (MalformedInputException ex)
            {
              // Some files are not UTF-8.
              //
              continue;
            }

            break;
          }
        }
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
      {
        var name = dir.getFileName().toString();
        if (name.startsWith("."))
        {
          return FileVisitResult.SKIP_SUBTREE;
        }

        return super.preVisitDirectory(dir, attrs);
      }
    });

    System.out.println("Indexed: " + repo + " count=" + javaCount);

    clone.close();

    IOUtil.deleteBestEffort(cloneFolder.toFile());
  }

  public Set<String> getRepositories(String projectID) throws IOException
  {
    var result = new TreeSet<String>();
    var content = getPMIContent(projectID);

    if (content.contains("\"state\":\"Archived\""))
    {
      return result;
    }

    var github = getGroup("github", content);
    if (github != null)
    {
      var orgs = getValues("org", github);
      for (var org : orgs)
      {
        for (var i = 1; i < 10; i++)
        {
          var repos = contentHandler.getContent("https://api.github.com/orgs/" + org + "/repos?page=" + i);
          var urls = getValues("html_url", repos);
          urls.remove("https://github.com/" + org);
          if (urls.isEmpty())
          {
            break;
          }

          result.addAll(urls);
        }
      }
    }

    var githubRepos = getSection("github_repos", content);
    if (githubRepos != null)
    {
      result.addAll(getValues("url", githubRepos));
    }

    cleanupRepos(result);

    if (result.isEmpty())
    {
      var gerritRepos = getSection("gerrit_repos", content);
      if (gerritRepos != null)
      {
        result.addAll(getValues("url", gerritRepos));
        cleanupRepos(result);
      }
    }

    var gitlab = getGroup("gitlab", content);
    if (gitlab != null)
    {
      var groups = getValues("project_group", gitlab);
      for (var group : groups)
      {
        for (var i = 1; i < 10; i++)
        {
          var repos = contentHandler.getContent("https://gitlab.eclipse.org/api/v4/groups/" + group.replace("/", "%2f") + "?page=" + i);
          var urls = getValues("http_url_to_repo", repos);
          if (!result.addAll(urls.stream().map(it -> it.replaceAll("\\.git$", "")).collect(Collectors.toList())))
          {
            break;
          }
        }
      }
    }

    var gitlabRepos = getSection("gitlab_repos", content);
    if (githubRepos != null)
    {
      result.addAll(getValues("url", gitlabRepos));
      cleanupRepos(result);
    }

    return result;
  }

  private void cleanupRepos(Set<String> repos)
  {
    repos.removeIf(it -> {
      if (it.contains("/org.eclipse.mylyn.") && !it.endsWith("/org.eclipse.mylyn.docs"))
      {
        return true;
      }

      return it.endsWith("/.github") || it.endsWith("/ui-best-practices") || it.endsWith("/.eclipsefdn") || it.contains("www.eclipse.org")
          || it.endsWith(".incubator") || it.contains("website") || it.endsWith(".github.io") || it.endsWith(".binaries");
    });
  }

  public String getPMIContent(String projectID) throws IOException
  {
    var pmiID = projectID.replace('.', '_');
    return contentHandler.getContent("https://projects.eclipse.org/api/projects/" + pmiID);
  }

  private String getGroup(String key, String content)
  {
    // {{
    var pattern = Pattern.compile('"' + key + "\":\\{([^}]*)\\}"); // }
    var matcher = pattern.matcher(content);
    if (matcher.find())
    {
      return matcher.group(1);
    }

    return null;
  }

  private String getSection(String key, String content)
  {
    var pattern = Pattern.compile('"' + key + "\":\\[([^]]*)\\]");
    var matcher = pattern.matcher(content);
    if (matcher.find())
    {
      return matcher.group(1);
    }

    return null;
  }

  private Set<String> getValues(String key, String content)
  {
    var result = new LinkedHashSet<String>();
    var matcher = Pattern.compile('"' + key + "\":\"([^\"]+)\"").matcher(content);
    while (matcher.find())
    {
      result.add(matcher.group(1));
    }

    return result;
  }

  private void saveIndex(File target) throws IOException
  {
    System.out.println("Saving: " + target.getAbsolutePath());

    var zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)));
    zipOutputStream.putNextEntry(new ZipEntry("index.txt"));

    var out = new PrintStream(zipOutputStream, false, "UTF-8");
    for (var entry : repositoryIndices.entrySet())
    {
      var link = entry.getKey();
      out.println(link);
      var repositoryIndex = entry.getValue();
      for (var repoEntry : repositoryIndex.entrySet())
      {
        var repo = repoEntry.getKey();
        out.print(" ");
        out.println(repo);
        var sourceFolders = repoEntry.getValue();
        for (var sourceFolderEntry : sourceFolders.entrySet())
        {
          var sourceFolder = sourceFolderEntry.getKey();
          out.print("  ");
          out.println(sourceFolder);
          var packages = sourceFolderEntry.getValue();
          for (Entry<String, Set<String>> packageEntry : packages.entrySet())
          {
            var packageName = packageEntry.getKey();
            out.print("   ");
            out.println(packageName);
            var classes = packageEntry.getValue();
            for (var className : classes)
            {
              out.print("    ");
              out.println(className);
            }
          }
        }
      }
    }

    out.close();
    zipOutputStream.close();
  }

  private static class ContentHandler
  {
    private Path cache;

    public ContentHandler(String cache)
    {
      try
      {
        if (cache != null)
        {
          this.cache = Path.of(cache);
        }
        else
        {
          this.cache = Files.createTempDirectory("org.eclipse.oomph.git.cache");
        }
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }

    protected String basicGetContent(URI uri) throws IOException, InterruptedException
    {
      var httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
      var requestBuilder = HttpRequest.newBuilder(uri).GET();
      var request = requestBuilder.build();
      var response = httpClient.send(request, BodyHandlers.ofString());
      var statusCode = response.statusCode();
      if (statusCode != 200)
      {
        throw new IOException("status code " + statusCode + " -> " + uri);
      }
      return response.body();
    }

    protected Path getCachePath(URI uri)
    {
      var decodedURI = URLDecoder.decode(uri.toString(), StandardCharsets.UTF_8);
      var isFolder = decodedURI.endsWith("/");
      var uriSegments = decodedURI.split("[:/?#&;]+");
      var relativePath = String.join("/", uriSegments).replace('=', '-');
      if (isFolder)
      {
        relativePath += "_._";
      }

      var result = cache.resolve(relativePath);
      return result;
    }

    public String getContent(String uri) throws IOException
    {
      return getContent(URI.create(uri));
    }

    public String getContent(URI uri) throws IOException
    {
      if ("file".equals(uri.getScheme()))
      {
        return Files.readString(Path.of(uri));
      }

      var path = getCachePath(uri);
      if (Files.isRegularFile(path))
      {
        var lastModifiedTime = Files.getLastModifiedTime(path);
        var now = System.currentTimeMillis();
        var age = now - lastModifiedTime.toMillis();
        var ageInHours = age / 1000 / 60 / 60;
        if (ageInHours < 8)
        {
          return Files.readString(path);
        }
      }

      try
      {
        var content = basicGetContent(uri);
        Files.createDirectories(path.getParent());
        writeString(path, content);
        return content;
      }
      catch (InterruptedException e)
      {
        throw new IOException(e);
      }
    }

    private static void writeString(Path path, CharSequence string) throws IOException
    {
      Files.writeString(path, string);
    }
  }
}
