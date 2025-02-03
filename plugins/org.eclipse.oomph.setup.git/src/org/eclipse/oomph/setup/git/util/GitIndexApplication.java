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
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.jgit.api.Git;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.CookieManager;
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
import java.util.Locale;
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

  private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+(([^.]+)\\.[^;]+)\\s*;");

  private static final Pattern LOG_PATTERN = Pattern.compile(",| 0%| 25%| 50%| 75%| 100%");

  private static final Set<String> PACKAGE_PREFIXES = new LinkedHashSet<String>();

  private static final Pattern NON_JAVA_REPOSITORIES_SECTION = Pattern
      .compile("( *private static Set<String> NON_JAVA_REPOSITORIES = new TreeSet<>\\(Set.of\\( //(\r?\n)).*?( *\"\"\\)\\);\r?\n)", Pattern.DOTALL);

  private static final Pattern BROKEN_REPOSITORIES_SECTION = Pattern
      .compile("( *private static Set<String> BROKEN_REPOSITORIES = new TreeSet<>\\(Set.of\\( //(\r?\n)).*?( *\"\"\\)\\);\r?\n)", Pattern.DOTALL);

  private static final Pattern GENERATED_REPOSITORIES_SECTION = Pattern.compile("( *// generated-repositories(\r?\n)).*?( *// generated-repositories\r?\n)",
      Pattern.DOTALL);

  static
  {
    for (String code : Locale.getISOCountries())
    {
      PACKAGE_PREFIXES.add(code.toLowerCase());
    }

    PACKAGE_PREFIXES.addAll(Set.of("com", "org", "net", "edu", "gov", "mil", "io", "java", "javax", "jakarta"));
  }

  private static Set<String> TEST_REPOSITORIES = new TreeSet<>(Set.of( //
      "https://git.eclipse.org/r/stem/org.eclipse.stem", //
      "https://github.com/eclipse-emf/org.eclipse.emf", //
      "https://gitlab.eclipse.org/eclipse/xpand/org.eclipse.xpand" //
  ));

  private static Set<String> BROKEN_REPOSITORIES = new TreeSet<>(Set.of( //
      "https://git.eclipse.org/r/bogus", //
      "https://git.eclipse.org/r/papyrus-rt/org.eclipse.papyrus-rt", //
      "https://git.eclipse.org/r/sequoyah/org.eclipse.sequoyah", //
      "https://git.eclipse.org/r/webtools/org.eclipse.webtools.java-ee-config", //
      "https://github.com/eclipse-arrowhead/profile-library-sysml", //
      "https://github.com/eclipse-ecsp/androidVehicleConnectApp", //
      "https://github.com/eclipse-ecsp/androidVehicleConnectSDK", //
      "https://github.com/eclipse-esmf/esmf-documentation", //
      "https://github.com/eclipse-tractusx/portal-assets", //
      "https://github.com/eclipse/org.eclipse.scout.docs", //
      "https://github.com/jakartaee/workshop-trainer", //
      "https://gitlab.eclipse.org/eclipse/openpass/simopenpass", //
      "https://gitlab.eclipse.org/eclipse/papyrus/discussion", //
      "https://gitlab.eclipse.org/eclipse/sw360/playground.git", //
      "https://gitlab.eclipse.org/eclipse/teneo/org.eclipse.emf.teneo", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.applications.iot-functiontest-framework", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.applications.iot.loadtest.framework", //
      ""));

  private static Set<String> NON_JAVA_REPOSITORIES = new TreeSet<>(Set.of( //
      "https://github.com/EclipseFdn/open-vsx.org", //
      "https://github.com/adoptium/Incubator", //
      "https://github.com/adoptium/adoptium", //
      "https://github.com/adoptium/adoptium-support", //
      "https://github.com/adoptium/adoptium.net", //
      "https://github.com/adoptium/api.adoptium.net", //
      "https://github.com/adoptium/aqa-test-tools", //
      "https://github.com/adoptium/blog.adoptium.net", //
      "https://github.com/adoptium/build-jdk", //
      "https://github.com/adoptium/bumblebench", //
      "https://github.com/adoptium/ci-jenkins-pipelines", //
      "https://github.com/adoptium/containers", //
      "https://github.com/adoptium/dash.adoptium.net", //
      "https://github.com/adoptium/documentation", //
      "https://github.com/adoptium/github-release-scripts", //
      "https://github.com/adoptium/infrastructure", //
      "https://github.com/adoptium/installer", //
      "https://github.com/adoptium/jenkins-helper", //
      "https://github.com/adoptium/jmc-build", //
      "https://github.com/adoptium/marketplace-data", //
      "https://github.com/adoptium/mirror-scripts", //
      "https://github.com/adoptium/run-aqa", //
      "https://github.com/adoptium/temurin", //
      "https://github.com/adoptium/temurin-attestations", //
      "https://github.com/adoptium/temurin-build", //
      "https://github.com/adoptium/temurin-cpe-generator", //
      "https://github.com/adoptium/temurin-linux-pkg-sources", //
      "https://github.com/adoptium/temurin-vdr-generator", //
      "https://github.com/deeplearning4j/deeplearning4j-docs", //
      "https://github.com/eclipse-4diac/4diac-documentation", //
      "https://github.com/eclipse-4diac/4diac-examples", //
      "https://github.com/eclipse-4diac/4diac-fbe", //
      "https://github.com/eclipse-4diac/4diac-forte", //
      "https://github.com/eclipse-4diac/4diac-fortebuildcontainer", //
      "https://github.com/eclipse-4diac/4diac-toolchain", //
      "https://github.com/eclipse-aaspe/aaspe", //
      "https://github.com/eclipse-aaspe/aaspe-common", //
      "https://github.com/eclipse-aaspe/aasx-server", //
      "https://github.com/eclipse-aaspe/common", //
      "https://github.com/eclipse-aaspe/package-explorer", //
      "https://github.com/eclipse-aaspe/server", //
      "https://github.com/eclipse-aasportal/AASPortal", //
      "https://github.com/eclipse-aaswc/aaswc", //
      "https://github.com/eclipse-actf/org.eclipse.actf.visualization.releng", //
      "https://github.com/eclipse-adore/adore", //
      "https://github.com/eclipse-agail/agail-security", //
      "https://github.com/eclipse-agail/agile-api-spec", //
      "https://github.com/eclipse-agail/agile-ble", //
      "https://github.com/eclipse-agail/agile-cli", //
      "https://github.com/eclipse-agail/agile-data", //
      "https://github.com/eclipse-agail/agile-dbus", //
      "https://github.com/eclipse-agail/agile-dummy-protocol", //
      "https://github.com/eclipse-agail/agile-kura", //
      "https://github.com/eclipse-agail/agile-sdk", //
      "https://github.com/eclipse-agileuml/agileuml", //
      "https://github.com/eclipse-ankaios-dashboard/ankaios-dashboard", //
      "https://github.com/eclipse-ankaios/ank-sdk-python", //
      "https://github.com/eclipse-ankaios/ank-sdk-rust", //
      "https://github.com/eclipse-ankaios/ankaios", //
      "https://github.com/eclipse-apoapsis/guidance", //
      "https://github.com/eclipse-apoapsis/ort-server", //
      "https://github.com/eclipse-apoapsis/renovate", //
      "https://github.com/eclipse-arche/arche", //
      "https://github.com/eclipse-arrowhead/Client-OPC-UA-Adaptor", //
      "https://github.com/eclipse-arrowhead/application-library-java-spring", //
      "https://github.com/eclipse-arrowhead/core-c", //
      "https://github.com/eclipse-arrowhead/core-go-generic", //
      "https://github.com/eclipse-arrowhead/core-java-spring", //
      "https://github.com/eclipse-arrowhead/documentation", //
      "https://github.com/eclipse-arrowhead/roadmap", //
      "https://github.com/eclipse-babel/server", //
      "https://github.com/eclipse-babel/translations", //
      "https://github.com/eclipse-basyx/basyx-aas-web-ui", //
      "https://github.com/eclipse-basyx/basyx-applications", //
      "https://github.com/eclipse-basyx/basyx-cpp-components", //
      "https://github.com/eclipse-basyx/basyx-cpp-sdk", //
      "https://github.com/eclipse-basyx/basyx-dotnet", //
      "https://github.com/eclipse-basyx/basyx-dotnet-applications", //
      "https://github.com/eclipse-basyx/basyx-dotnet-components", //
      "https://github.com/eclipse-basyx/basyx-dotnet-examples", //
      "https://github.com/eclipse-basyx/basyx-dotnet-sdk", //
      "https://github.com/eclipse-basyx/basyx-pdf-to-aas", //
      "https://github.com/eclipse-basyx/basyx-python-framework", //
      "https://github.com/eclipse-basyx/basyx-python-sdk", //
      "https://github.com/eclipse-basyx/basyx-rust-sdk", //
      "https://github.com/eclipse-basyx/basyx-typescript-sdk", //
      "https://github.com/eclipse-basyx/basyx-wiki", //
      "https://github.com/eclipse-bfb/bfb-client", //
      "https://github.com/eclipse-bfb/bfb-specs", //
      "https://github.com/eclipse-bfb/bfb-tooling", //
      "https://github.com/eclipse-bluechi/bluechi", //
      "https://github.com/eclipse-bluechi/bluechi-ansible-collection", //
      "https://github.com/eclipse-bluechi/bluechi-demos", //
      "https://github.com/eclipse-bluechi/bluechi-on-yocto", //
      "https://github.com/eclipse-bluechi/bluechi-ppa", //
      "https://github.com/eclipse-bluechi/hashmap.c", //
      "https://github.com/eclipse-bluechi/terraform-provider-bluechi", //
      "https://github.com/eclipse-canought/can-manager", //
      "https://github.com/eclipse-canought/can-translator", //
      "https://github.com/eclipse-canought/can-translator-client", //
      "https://github.com/eclipse-canought/up-cpp-client", //
      "https://github.com/eclipse-canought/up-cpp-server", //
      "https://github.com/eclipse-capella/capella-addons", //
      "https://github.com/eclipse-capella/capella-gitadapter", //
      "https://github.com/eclipse-capella/capella-pipeline-library", //
      "https://github.com/eclipse-capella/capella-releng-parent", //
      "https://github.com/eclipse-cbi/ansible-playbooks", //
      "https://github.com/eclipse-cbi/best-practices", //
      "https://github.com/eclipse-cbi/buildkitd-okd", //
      "https://github.com/eclipse-cbi/cbi", //
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
      "https://github.com/eclipse-cbi/misc-checks", //
      "https://github.com/eclipse-cbi/nexus3-as-code", //
      "https://github.com/eclipse-cbi/org.eclipse.cbi-testdata", //
      "https://github.com/eclipse-cbi/sonatype-nexus", //
      "https://github.com/eclipse-cdo/cdo.www", //
      "https://github.com/eclipse-cdt-cloud/cdt-amalgamator", //
      "https://github.com/eclipse-cdt-cloud/cdt-cloud", //
      "https://github.com/eclipse-cdt-cloud/cdt-cloud-blueprint", //
      "https://github.com/eclipse-cdt-cloud/cdt-gdb-adapter", //
      "https://github.com/eclipse-cdt-cloud/cdt-gdb-vscode", //
      "https://github.com/eclipse-cdt-cloud/clangd-contexts", //
      "https://github.com/eclipse-cdt-cloud/theia-trace-extension", //
      "https://github.com/eclipse-cdt-cloud/timeline-chart", //
      "https://github.com/eclipse-cdt-cloud/trace-server-protocol", //
      "https://github.com/eclipse-cdt-cloud/trace-viewer-examples", //
      "https://github.com/eclipse-cdt-cloud/tsp-python-client", //
      "https://github.com/eclipse-cdt-cloud/tsp-typescript-client", //
      "https://github.com/eclipse-cdt-cloud/vscode-clangd", //
      "https://github.com/eclipse-cdt-cloud/vscode-memory-inspector", //
      "https://github.com/eclipse-cdt-cloud/vscode-peripheral-inspector", //
      "https://github.com/eclipse-cdt-cloud/vscode-serial-monitor", //
      "https://github.com/eclipse-cdt-cloud/vscode-svd-viewer", //
      "https://github.com/eclipse-cdt-cloud/vscode-trace-extension", //
      "https://github.com/eclipse-cdt-cloud/vscode-trace-server", //
      "https://github.com/eclipse-cdt-cloud/vscode-websocket-adapter", //
      "https://github.com/eclipse-cdt/cdt-infra", //
      "https://github.com/eclipse-cdt/cdt-new-managedbuild-prototype", //
      "https://github.com/eclipse-cdt/cdt-vscode", //
      "https://github.com/eclipse-chariott/Agemo", //
      "https://github.com/eclipse-chariott/chariott", //
      "https://github.com/eclipse-chariott/chariott-example-applications", //
      "https://github.com/eclipse-che/blog", //
      "https://github.com/eclipse-che/che", //
      "https://github.com/eclipse-che/che-archetypes", //
      "https://github.com/eclipse-che/che-dashboard", //
      "https://github.com/eclipse-che/che-dependencies", //
      "https://github.com/eclipse-che/che-dev", //
      "https://github.com/eclipse-che/che-devfile-registry", //
      "https://github.com/eclipse-che/che-dockerfiles", //
      "https://github.com/eclipse-che/che-docs", //
      "https://github.com/eclipse-che/che-docs-vale-style", //
      "https://github.com/eclipse-che/che-editor-gwt-ide", //
      "https://github.com/eclipse-che/che-go-jsonrpc", //
      "https://github.com/eclipse-che/che-jwtproxy", //
      "https://github.com/eclipse-che/che-lib", //
      "https://github.com/eclipse-che/che-machine-exec", //
      "https://github.com/eclipse-che/che-operator", //
      "https://github.com/eclipse-che/che-parent", //
      "https://github.com/eclipse-che/che-plugin-broker", //
      "https://github.com/eclipse-che/che-plugin-registry", //
      "https://github.com/eclipse-che/che-release", //
      "https://github.com/eclipse-che/che-tests", //
      "https://github.com/eclipse-che/che-theia", //
      "https://github.com/eclipse-che/che-workspace-client", //
      "https://github.com/eclipse-che/che-workspace-loader", //
      "https://github.com/eclipse-che4z/che-che4z", //
      "https://github.com/eclipse-che4z/che-che4z-explorer-for-endevor", //
      "https://github.com/eclipse-che4z/che-che4z-lsp-for-hlasm", //
      "https://github.com/eclipse-che4z/che-che4z-zos-resource-explorer", //
      "https://github.com/eclipse-cognicrypt/Governance", //
      "https://github.com/eclipse-cognicrypt/hugo-solstice-theme", //
      "https://github.com/eclipse-collections/gsc-ec-converter", //
      "https://github.com/eclipse-corinthian/Precedent_Docs", //
      "https://github.com/eclipse-csi/gradually", //
      "https://github.com/eclipse-csi/octopin", //
      "https://github.com/eclipse-csi/otterdog", //
      "https://github.com/eclipse-csi/security-handbook", //
      "https://github.com/eclipse-csi/sonatype-lifecycle", //
      "https://github.com/eclipse-csi/workflows", //
      "https://github.com/eclipse-cyclonedds/cyclonedds", //
      "https://github.com/eclipse-cyclonedds/cyclonedds-cxx", //
      "https://github.com/eclipse-cyclonedds/cyclonedds-insight", //
      "https://github.com/eclipse-cyclonedds/cyclonedds-python", //
      "https://github.com/eclipse-daanse/Tutorials", //
      "https://github.com/eclipse-daanse/legacy.dashboard.client", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.assert.pdf", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.board.app", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.board.server", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.index", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.mdx", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.operation", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.pom", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.sql", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.webconsole.branding", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.xmla", //
      "https://github.com/eclipse-dash/nodejs-wrapper", //
      "https://github.com/eclipse-dash/quevee", //
      "https://github.com/eclipse-dataspace-cap/cap-ontology", //
      "https://github.com/eclipse-dataspace-dcp/decentralized-claims-protocol", //
      "https://github.com/eclipse-dataspace-drp/DataRightsProfile", //
      "https://github.com/eclipse-dataspace-protocol-base/DataspaceProtocol", //
      "https://github.com/eclipse-dataspace-protocol-base/dsp_best_practices", //
      "https://github.com/eclipse-dataspacetck/dcp-tck", //
      "https://github.com/eclipse-diafanis/diafanis", //
      "https://github.com/eclipse-dirigible/dirigible-samples", //
      "https://github.com/eclipse-ditto/ditto-clients-golang", //
      "https://github.com/eclipse-ditto/ditto-clients-python", //
      "https://github.com/eclipse-ditto/ditto-wot-tooling", //
      "https://github.com/eclipse-dltk/dltk.all", //
      "https://github.com/eclipse-ecal/ecal", //
      "https://github.com/eclipse-ecal/ecal-algorithm-samples", //
      "https://github.com/eclipse-ecal/ecal-camera-samples", //
      "https://github.com/eclipse-ecal/ecal-carla-bridge", //
      "https://github.com/eclipse-ecal/ecal-core", //
      "https://github.com/eclipse-ecal/ecal-foxglove-bridge", //
      "https://github.com/eclipse-ecal/ecal-gpsd-client", //
      "https://github.com/eclipse-ecal/ecal-matlab-binding", //
      "https://github.com/eclipse-ecal/ecal-mcap-tools", //
      "https://github.com/eclipse-ecal/ecal-mqtt-bridge", //
      "https://github.com/eclipse-ecal/ecal-rs", //
      "https://github.com/eclipse-ecal/ecal-utils", //
      "https://github.com/eclipse-ecal/ecaludp", //
      "https://github.com/eclipse-ecal/fineftp-server", //
      "https://github.com/eclipse-ecal/protobuf-datatypes-collection", //
      "https://github.com/eclipse-ecal/rmw_ecal", //
      "https://github.com/eclipse-ecal/rosidl_typesupport_protobuf", //
      "https://github.com/eclipse-ecal/tcp_pubsub", //
      "https://github.com/eclipse-ecal/udpcap", //
      "https://github.com/eclipse-eclemma/update.eclemma.org", //
      "https://github.com/eclipse-ecp/org.eclipse.emf.ecp.releng", //
      "https://github.com/eclipse-ecsp/nosql-dao", //
      "https://github.com/eclipse-ecsp/redis-jar", //
      "https://github.com/eclipse-ecsp/sql-dao", //
      "https://github.com/eclipse-ecsp/stream-base", //
      "https://github.com/eclipse-edc/Collateral", //
      "https://github.com/eclipse-edc/CompatibilityTests", //
      "https://github.com/eclipse-edc/DataDashboard", //
      "https://github.com/eclipse-edc/IDS-CodeGeneration", //
      "https://github.com/eclipse-edc/JenkinsPipelines", //
      "https://github.com/eclipse-edc/Publications", //
      "https://github.com/eclipse-edc/Release", //
      "https://github.com/eclipse-edc/Technology-HuaweiCloud", //
      "https://github.com/eclipse-edc/Template-Basic", //
      "https://github.com/eclipse-edc/docs", //
      "https://github.com/eclipse-edc/json-ld-context", //
      "https://github.com/eclipse-ee4j/angus-activation", //
      "https://github.com/eclipse-ee4j/eclipselink-asm", //
      "https://github.com/eclipse-ee4j/eclipselink-releng", //
      "https://github.com/eclipse-ee4j/ee4j", //
      "https://github.com/eclipse-ee4j/glassfish-docs", //
      "https://github.com/eclipse-ee4j/glassfish-maven-embedded-plugin", //
      "https://github.com/eclipse-ee4j/glassfish-repackaged", //
      "https://github.com/eclipse-ee4j/glassfish.docker", //
      "https://github.com/eclipse-ee4j/gransasso", //
      "https://github.com/eclipse-ee4j/grizzly-npn", //
      "https://github.com/eclipse-ee4j/jakartaee-firstcup", //
      "https://github.com/eclipse-ee4j/jakartaee-release", //
      "https://github.com/eclipse-ee4j/jakartaee-renames", //
      "https://github.com/eclipse-ee4j/jakartaee-tutorial", //
      "https://github.com/eclipse-ee4j/jersey-web", //
      "https://github.com/eclipse-ee4j/starter", //
      "https://github.com/eclipse-efbt/efbt", //
      "https://github.com/eclipse-efm/efm-hibou", //
      "https://github.com/eclipse-efm/efm-symbex", //
      "https://github.com/eclipse-egit/egit-permissions", //
      "https://github.com/eclipse-egit/egit-pipelines", //
      "https://github.com/eclipse-elk/elk-models", //
      "https://github.com/eclipse-embed-cdt/Liqp", //
      "https://github.com/eclipse-embed-cdt/assets", //
      "https://github.com/eclipse-embed-cdt/org.eclipse.epp.packages", //
      "https://github.com/eclipse-embed-cdt/web-jekyll", //
      "https://github.com/eclipse-embed-cdt/web-preview", //
      "https://github.com/eclipse-emf-compare/emf-compare-acceptance", //
      "https://github.com/eclipse-emf-compare/emf-compare-releng", //
      "https://github.com/eclipse-emfcloud/emfcloud", //
      "https://github.com/eclipse-emfcloud/emfcloud-modelserver-theia", //
      "https://github.com/eclipse-emfcloud/jsonforms-property-view", //
      "https://github.com/eclipse-emfcloud/model-validation", //
      "https://github.com/eclipse-emfcloud/modelhub", //
      "https://github.com/eclipse-emfcloud/modelserver-node", //
      "https://github.com/eclipse-emfcloud/theia-tree-editor", //
      "https://github.com/eclipse-equinox/equinox.bundles", //
      "https://github.com/eclipse-equinox/equinox.framework", //
      "https://github.com/eclipse-esmf/esmf-antora-ui", //
      "https://github.com/eclipse-esmf/esmf-aspect-model-editor", //
      "https://github.com/eclipse-esmf/esmf-manufacturing-information-model", //
      "https://github.com/eclipse-esmf/esmf-parent", //
      "https://github.com/eclipse-esmf/esmf-sdk-demo", //
      "https://github.com/eclipse-esmf/esmf-sdk-js-aspect-model-loader", //
      "https://github.com/eclipse-esmf/esmf-sdk-js-schematics", //
      "https://github.com/eclipse-esmf/esmf-sdk-js-schematics-demo", //
      "https://github.com/eclipse-esmf/esmf-sdk-py-aspect-model-loader", //
      "https://github.com/eclipse-esmf/esmf-sdk-py-pandas-dataframe", //
      "https://github.com/eclipse-fa3st/fa3st-registry", //
      "https://github.com/eclipse-fa3st/fa3st-service", //
      "https://github.com/eclipse-fennec/common.models", //
      "https://github.com/eclipse-fennec/emf.osgi", //
      "https://github.com/eclipse-fog05/examples", //
      "https://github.com/eclipse-fog05/fog05", //
      "https://github.com/eclipse-fog05/fog05-go", //
      "https://github.com/eclipse-fog05/fog05-hypervisor-containerd", //
      "https://github.com/eclipse-fog05/fog05-hypervisor-kvm", //
      "https://github.com/eclipse-fog05/fog05-hypervisor-lxd", //
      "https://github.com/eclipse-fog05/fog05-hypervisor-native", //
      "https://github.com/eclipse-fog05/fog05-hypervisor-ros2", //
      "https://github.com/eclipse-fog05/fog05-networking-linux", //
      "https://github.com/eclipse-fog05/fog05-python", //
      "https://github.com/eclipse-furo/eclipsefuro", //
      "https://github.com/eclipse-furo/eclipsefuro-web", //
      "https://github.com/eclipse-gemini/gemini.dbaccess.postgresql", //
      "https://github.com/eclipse-gemoc/gemoc-studio-extras", //
      "https://github.com/eclipse-glsp/glsp", //
      "https://github.com/eclipse-glsp/glsp-client", //
      "https://github.com/eclipse-glsp/glsp-playwright", //
      "https://github.com/eclipse-glsp/glsp-server-node", //
      "https://github.com/eclipse-glsp/glsp-theia-integration", //
      "https://github.com/eclipse-glsp/glsp-vscode-integration", //
      "https://github.com/eclipse-hara/hara-ddiclient", //
      "https://github.com/eclipse-hawkbit/hawkbit-clients-golang", //
      "https://github.com/eclipse-heimlig/heimlig", //
      "https://github.com/eclipse-ibeji/freyja", //
      "https://github.com/eclipse-ibeji/ibeji", //
      "https://github.com/eclipse-ibeji/ibeji-example-applications", //
      "https://github.com/eclipse-iceoryx/iceoryx", //
      "https://github.com/eclipse-iceoryx/iceoryx-automotive-soa", //
      "https://github.com/eclipse-iceoryx/iceoryx-gateway-dds", //
      "https://github.com/eclipse-iceoryx/iceoryx-project-template", //
      "https://github.com/eclipse-iceoryx/iceoryx-rs", //
      "https://github.com/eclipse-iceoryx/iceoryx-web", //
      "https://github.com/eclipse-iceoryx/iceoryx2", //
      "https://github.com/eclipse-iofog/Controller", //
      "https://github.com/eclipse-iofog/ECN-Viewer", //
      "https://github.com/eclipse-iofog/HardwareAbstraction", //
      "https://github.com/eclipse-iofog/agent-go", //
      "https://github.com/eclipse-iofog/common-logging", //
      "https://github.com/eclipse-iofog/core-networking", //
      "https://github.com/eclipse-iofog/demo", //
      "https://github.com/eclipse-iofog/documentation", //
      "https://github.com/eclipse-iofog/example-microservices", //
      "https://github.com/eclipse-iofog/helm", //
      "https://github.com/eclipse-iofog/homebrew-iofogctl", //
      "https://github.com/eclipse-iofog/integrations", //
      "https://github.com/eclipse-iofog/iofog-c-sdk", //
      "https://github.com/eclipse-iofog/iofog-csharp-sdk", //
      "https://github.com/eclipse-iofog/iofog-docker-images", //
      "https://github.com/eclipse-iofog/iofog-go-sdk", //
      "https://github.com/eclipse-iofog/iofog-kubelet", //
      "https://github.com/eclipse-iofog/iofog-nodejs-sdk", //
      "https://github.com/eclipse-iofog/iofog-operator", //
      "https://github.com/eclipse-iofog/iofog-platform", //
      "https://github.com/eclipse-iofog/iofog-python-sdk", //
      "https://github.com/eclipse-iofog/iofog-scheduler", //
      "https://github.com/eclipse-iofog/iofog.org", //
      "https://github.com/eclipse-iofog/iofogctl", //
      "https://github.com/eclipse-iofog/platform", //
      "https://github.com/eclipse-iofog/port-manager", //
      "https://github.com/eclipse-iofog/restblue", //
      "https://github.com/eclipse-iofog/router", //
      "https://github.com/eclipse-iofog/skupper-proxy", //
      "https://github.com/eclipse-iofog/test-runner", //
      "https://github.com/eclipse-iottestware/iottestware", //
      "https://github.com/eclipse-iottestware/iottestware.coap", //
      "https://github.com/eclipse-iottestware/iottestware.dashboard", //
      "https://github.com/eclipse-iottestware/iottestware.fuzzing", //
      "https://github.com/eclipse-iottestware/iottestware.mqtt", //
      "https://github.com/eclipse-iottestware/iottestware.opcua", //
      "https://github.com/eclipse-iottestware/iottestware.performance", //
      "https://github.com/eclipse-jdt/eclipse.jdt", //
      "https://github.com/eclipse-jgit/jgit-permissions", //
      "https://github.com/eclipse-jgit/jgit-pipelines", //
      "https://github.com/eclipse-jkube/ci", //
      "https://github.com/eclipse-jkube/jkube-images", //
      "https://github.com/eclipse-jkube/katacoda-scenarios", //
      "https://github.com/eclipse-jkube/kport", //
      "https://github.com/eclipse-jkube/vscode-kport", //
      "https://github.com/eclipse-justj/justj", //
      "https://github.com/eclipse-kanto/aws-connector", //
      "https://github.com/eclipse-kanto/azure-connector", //
      "https://github.com/eclipse-kanto/container-management", //
      "https://github.com/eclipse-kanto/example-applications", //
      "https://github.com/eclipse-kanto/file-backup", //
      "https://github.com/eclipse-kanto/file-upload", //
      "https://github.com/eclipse-kanto/kanto", //
      "https://github.com/eclipse-kanto/local-digital-twins", //
      "https://github.com/eclipse-kanto/meta-kanto", //
      "https://github.com/eclipse-kanto/software-update", //
      "https://github.com/eclipse-kanto/suite-bootstrapping", //
      "https://github.com/eclipse-kanto/suite-connector", //
      "https://github.com/eclipse-kanto/system-metrics", //
      "https://github.com/eclipse-kanto/update-manager", //
      "https://github.com/eclipse-keyple/keyple-actions", //
      "https://github.com/eclipse-keyple/keyple-api-docs", //
      "https://github.com/eclipse-keyple/keyple-card-calypso-cpp-lib", //
      "https://github.com/eclipse-keyple/keyple-card-calypso-crypto-pki-java-lib", //
      "https://github.com/eclipse-keyple/keyple-card-generic-cpp-lib", //
      "https://github.com/eclipse-keyple/keyple-common-cpp-api", //
      "https://github.com/eclipse-keyple/keyple-common-java-api", //
      "https://github.com/eclipse-keyple/keyple-cpp", //
      "https://github.com/eclipse-keyple/keyple-cpp-example", //
      "https://github.com/eclipse-keyple/keyple-cpp-meta", //
      "https://github.com/eclipse-keyple/keyple-distributed-local-java-api", //
      "https://github.com/eclipse-keyple/keyple-less-distributed-client-kmp-lib", //
      "https://github.com/eclipse-keyple/keyple-less-reader-nfcmobile-kmp-lib", //
      "https://github.com/eclipse-keyple/keyple-ops", //
      "https://github.com/eclipse-keyple/keyple-plugin-android-nfc-java-lib", //
      "https://github.com/eclipse-keyple/keyple-plugin-android-omapi-java-lib", //
      "https://github.com/eclipse-keyple/keyple-plugin-cpp-api", //
      "https://github.com/eclipse-keyple/keyple-plugin-pcsc-cpp-lib", //
      "https://github.com/eclipse-keyple/keyple-plugin-stub-cpp-lib", //
      "https://github.com/eclipse-keyple/keyple-service-cpp-lib", //
      "https://github.com/eclipse-keyple/keyple-service-resource-cpp-lib", //
      "https://github.com/eclipse-keyple/keyple-util-cpp-lib", //
      "https://github.com/eclipse-keyple/keypleless-distributed-client-kmp-lib", //
      "https://github.com/eclipse-keyple/keypleless-reader-nfcmobile-kmp-lib", //
      "https://github.com/eclipse-keypop/keypop", //
      "https://github.com/eclipse-keypop/keypop-actions", //
      "https://github.com/eclipse-keypop/keypop-api-docs", //
      "https://github.com/eclipse-keypop/keypop-calypso-card-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-card-java-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-certificate-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-certificate-java-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-asymmetric-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-legacysam-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-legacysam-java-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-symmetric-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-symmetric-java-api", //
      "https://github.com/eclipse-keypop/keypop-card-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-card-java-api", //
      "https://github.com/eclipse-keypop/keypop-ops", //
      "https://github.com/eclipse-keypop/keypop-reader-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-reader-java-api", //
      "https://github.com/eclipse-kiso-testing/kiso-testing", //
      "https://github.com/eclipse-kiso-testing/kiso-testing-python-uds", //
      "https://github.com/eclipse-kiso-testing/kiso-testing-testapp", //
      "https://github.com/eclipse-kiso-testing/kiso-testing-vscode", //
      "https://github.com/eclipse-kuksa/kuksa-actions", //
      "https://github.com/eclipse-kuksa/kuksa-android-companion", //
      "https://github.com/eclipse-kuksa/kuksa-android-sdk", //
      "https://github.com/eclipse-kuksa/kuksa-can-provider", //
      "https://github.com/eclipse-kuksa/kuksa-common", //
      "https://github.com/eclipse-kuksa/kuksa-csv-provider", //
      "https://github.com/eclipse-kuksa/kuksa-databroker", //
      "https://github.com/eclipse-kuksa/kuksa-dds-provider", //
      "https://github.com/eclipse-kuksa/kuksa-gps-provider", //
      "https://github.com/eclipse-kuksa/kuksa-hardware", //
      "https://github.com/eclipse-kuksa/kuksa-incubation", //
      "https://github.com/eclipse-kuksa/kuksa-java-sdk", //
      "https://github.com/eclipse-kuksa/kuksa-mock-provider", //
      "https://github.com/eclipse-kuksa/kuksa-perf", //
      "https://github.com/eclipse-kuksa/kuksa-python-sdk", //
      "https://github.com/eclipse-kuksa/kuksa-someip-provider", //
      "https://github.com/eclipse-kuksa/kuksa-viss", //
      "https://github.com/eclipse-kuksa/kuksa.invehicle", //
      "https://github.com/eclipse-kuksa/kuksa.val.feeders", //
      "https://github.com/eclipse-kuksa/kuksa.val.services", //
      "https://github.com/eclipse-kura/kura-apps", //
      "https://github.com/eclipse-langium/langium", //
      "https://github.com/eclipse-langium/langium-previews", //
      "https://github.com/eclipse-leda/leda", //
      "https://github.com/eclipse-leda/leda-contrib-cloud-connector", //
      "https://github.com/eclipse-leda/leda-contrib-container-update-agent", //
      "https://github.com/eclipse-leda/leda-contrib-otel", //
      "https://github.com/eclipse-leda/leda-contrib-self-update-agent", //
      "https://github.com/eclipse-leda/leda-contrib-vehicle-update-manager", //
      "https://github.com/eclipse-leda/leda-contrib-vscode-extensions", //
      "https://github.com/eclipse-leda/leda-distro", //
      "https://github.com/eclipse-leda/leda-example-applications", //
      "https://github.com/eclipse-leda/leda-utils", //
      "https://github.com/eclipse-leda/meta-leda", //
      "https://github.com/eclipse-linuxtools/eclipse-ide-snap", //
      "https://github.com/eclipse-linuxtools/org.eclipse.linuxtools.eclipse-build", //
      "https://github.com/eclipse-lmos/arc", //
      "https://github.com/eclipse-lmos/arc-spring-init", //
      "https://github.com/eclipse-lmos/arc-view", //
      "https://github.com/eclipse-lmos/lmos-demo", //
      "https://github.com/eclipse-lmos/lmos-operator", //
      "https://github.com/eclipse-lmos/lmos-router", //
      "https://github.com/eclipse-lmos/lmos-runtime", //
      "https://github.com/eclipse-lmos/lmos-sample-agents", //
      "https://github.com/eclipse-lyo/lyo.oslc-ui", //
      "https://github.com/eclipse-m2e/m2e-discovery-catalog", //
      "https://github.com/eclipse-m2e/m2e-wtp-jpa", //
      "https://github.com/eclipse-m2e/org.eclipse.m2e.workspace", //
      "https://github.com/eclipse-mnestix/mnestix-browser", //
      "https://github.com/eclipse-mnestix/mnestix-browser-example-submodel-visualizations", //
      "https://github.com/eclipse-moec/integration", //
      "https://github.com/eclipse-moec/motioncontrol", //
      "https://github.com/eclipse-mosquitto/mosquitto", //
      "https://github.com/eclipse-mosquitto/mosquitto.rsmb", //
      "https://github.com/eclipse-muto/agent", //
      "https://github.com/eclipse-muto/composer", //
      "https://github.com/eclipse-muto/core", //
      "https://github.com/eclipse-muto/dashboard", //
      "https://github.com/eclipse-muto/dashboard-device", //
      "https://github.com/eclipse-muto/dashboard-stack", //
      "https://github.com/eclipse-muto/docs", //
      "https://github.com/eclipse-muto/example-f1tenth-simulator", //
      "https://github.com/eclipse-muto/example-f1tenth-testcar", //
      "https://github.com/eclipse-muto/f1tenth", //
      "https://github.com/eclipse-muto/f1tenth_gapfollower", //
      "https://github.com/eclipse-muto/f1tenth_particlefilter", //
      "https://github.com/eclipse-muto/f1tenth_purepursuit", //
      "https://github.com/eclipse-muto/f1tenth_ranglibc", //
      "https://github.com/eclipse-muto/f1tenth_safety", //
      "https://github.com/eclipse-muto/f1tenth_scanmatching", //
      "https://github.com/eclipse-muto/f1tenth_simulator", //
      "https://github.com/eclipse-muto/f1tenth_system", //
      "https://github.com/eclipse-muto/f1tenth_wallfollower", //
      "https://github.com/eclipse-muto/f1tenth_waypointlogger", //
      "https://github.com/eclipse-muto/liveui", //
      "https://github.com/eclipse-muto/liveui-core", //
      "https://github.com/eclipse-muto/liveui-react", //
      "https://github.com/eclipse-muto/liveui-react-native", //
      "https://github.com/eclipse-muto/liveui-vue", //
      "https://github.com/eclipse-muto/messages", //
      "https://github.com/eclipse-muto/sandbox", //
      "https://github.com/eclipse-n4js/n4js-tutorials", //
      "https://github.com/eclipse-oct/open-collaboration-tools", //
      "https://github.com/eclipse-omr/omr", //
      "https://github.com/eclipse-openbsw/openbsw", //
      "https://github.com/eclipse-opendut/cannelloni", //
      "https://github.com/eclipse-opendut/cannelloni-build", //
      "https://github.com/eclipse-opendut/netbird-build", //
      "https://github.com/eclipse-opendut/netbird-fork", //
      "https://github.com/eclipse-opendut/opendut", //
      "https://github.com/eclipse-opendut/rperf-build", //
      "https://github.com/eclipse-openj9/build-openj9", //
      "https://github.com/eclipse-openj9/openj9-docs", //
      "https://github.com/eclipse-openj9/openj9-docs-staging", //
      "https://github.com/eclipse-openj9/openj9-jenkins", //
      "https://github.com/eclipse-openj9/openj9-omr", //
      "https://github.com/eclipse-openmcx/openmcx", //
      "https://github.com/eclipse-opensmartclide/smartclide", //
      "https://github.com/eclipse-opensmartclide/smartclide-Backend-REST-Client", //
      "https://github.com/eclipse-opensmartclide/smartclide-Che-REST-Client", //
      "https://github.com/eclipse-opensmartclide/smartclide-ServDB", //
      "https://github.com/eclipse-opensmartclide/smartclide-Service-Creation-Testing", //
      "https://github.com/eclipse-opensmartclide/smartclide-api-gateway", //
      "https://github.com/eclipse-opensmartclide/smartclide-architectural-pattern", //
      "https://github.com/eclipse-opensmartclide/smartclide-broker", //
      "https://github.com/eclipse-opensmartclide/smartclide-che-theia", //
      "https://github.com/eclipse-opensmartclide/smartclide-cicd-gitlab", //
      "https://github.com/eclipse-opensmartclide/smartclide-deployment-extension", //
      "https://github.com/eclipse-opensmartclide/smartclide-deployment-interpreter-theia", //
      "https://github.com/eclipse-opensmartclide/smartclide-deployment-service", //
      "https://github.com/eclipse-opensmartclide/smartclide-design-pattern-selection-theia", //
      "https://github.com/eclipse-opensmartclide/smartclide-devfiles", //
      "https://github.com/eclipse-opensmartclide/smartclide-docs", //
      "https://github.com/eclipse-opensmartclide/smartclide-external-project-importer", //
      "https://github.com/eclipse-opensmartclide/smartclide-frontend-comm", //
      "https://github.com/eclipse-opensmartclide/smartclide-ide-front-end", //
      "https://github.com/eclipse-opensmartclide/smartclide-ide-front-end-theme", //
      "https://github.com/eclipse-opensmartclide/smartclide-jbpm", //
      "https://github.com/eclipse-opensmartclide/smartclide-perftestgen-theia", //
      "https://github.com/eclipse-opensmartclide/smartclide-security-patterns", //
      "https://github.com/eclipse-opensmartclide/smartclide-service-creation-theia", //
      "https://github.com/eclipse-opensmartclide/smartclide-service-discovery-poc", //
      "https://github.com/eclipse-opensmartclide/smartclide-service-registry-poc", //
      "https://github.com/eclipse-opensmartclide/smartclide-smart-assistant", //
      "https://github.com/eclipse-opensmartclide/smartclide-smart-assistant-theia", //
      "https://github.com/eclipse-opensmartclide/smartclide-task-service-discovery", //
      "https://github.com/eclipse-opensmartclide/smartclide-td-reusability-theia", //
      "https://github.com/eclipse-openxilenv/openxilenv", //
      "https://github.com/eclipse-orbit/orbit", //
      "https://github.com/eclipse-orbit/orbit-simrel", //
      "https://github.com/eclipse-osee/org.eclipse.osee", //
      "https://github.com/eclipse-osee/org.eclipse.ote", //
      "https://github.com/eclipse-osgi-technology/command", //
      "https://github.com/eclipse-osgi-technology/feature-launcher", //
      "https://github.com/eclipse-osgi-technology/jakarta-webservices", //
      "https://github.com/eclipse-osgi-technology/jakarta-websockets", //
      "https://github.com/eclipse-osgi-technology/maven-pom", //
      "https://github.com/eclipse-osgi-technology/osgi.enroute.site", //
      "https://github.com/eclipse-osgi-technology/scheduler", //
      "https://github.com/eclipse-osgi-technology/slf4j-osgi", //
      "https://github.com/eclipse-p3com/p3com", //
      "https://github.com/eclipse-packaging/packages", //
      "https://github.com/eclipse-paho/paho.golang", //
      "https://github.com/eclipse-paho/paho.mqtt-sn.embedded-c", //
      "https://github.com/eclipse-paho/paho.mqtt.c", //
      "https://github.com/eclipse-paho/paho.mqtt.cpp", //
      "https://github.com/eclipse-paho/paho.mqtt.d", //
      "https://github.com/eclipse-paho/paho.mqtt.embedded-c", //
      "https://github.com/eclipse-paho/paho.mqtt.golang", //
      "https://github.com/eclipse-paho/paho.mqtt.javascript", //
      "https://github.com/eclipse-paho/paho.mqtt.m2mqtt", //
      "https://github.com/eclipse-paho/paho.mqtt.python", //
      "https://github.com/eclipse-paho/paho.mqtt.ruby", //
      "https://github.com/eclipse-paho/paho.mqtt.rust", //
      "https://github.com/eclipse-paho/paho.mqtt.testing", //
      "https://github.com/eclipse-pass/main", //
      "https://github.com/eclipse-pass/pass-acceptance-testing", //
      "https://github.com/eclipse-pass/pass-admin-ui", //
      "https://github.com/eclipse-pass/pass-auth", //
      "https://github.com/eclipse-pass/pass-data-migration", //
      "https://github.com/eclipse-pass/pass-data-model", //
      "https://github.com/eclipse-pass/pass-docker", //
      "https://github.com/eclipse-pass/pass-docker-mailserver", //
      "https://github.com/eclipse-pass/pass-docker-mailserver/", //
      "https://github.com/eclipse-pass/pass-documentation", //
      "https://github.com/eclipse-pass/pass-doi-service", //
      "https://github.com/eclipse-pass/pass-download-service", //
      "https://github.com/eclipse-pass/pass-download-service/", //
      "https://github.com/eclipse-pass/pass-dupe-checker", //
      "https://github.com/eclipse-pass/pass-dupe-checker/", //
      "https://github.com/eclipse-pass/pass-ember-adapter", //
      "https://github.com/eclipse-pass/pass-ember-adapter/", //
      "https://github.com/eclipse-pass/pass-fcrepo-jms", //
      "https://github.com/eclipse-pass/pass-indexer-checker", //
      "https://github.com/eclipse-pass/pass-messaging-support", //
      "https://github.com/eclipse-pass/pass-metadata-schemas", //
      "https://github.com/eclipse-pass/pass-policy-service", //
      "https://github.com/eclipse-pass/pass-test", //
      "https://github.com/eclipse-pass/pass-ui", //
      "https://github.com/eclipse-pass/pass-ui-public", //
      "https://github.com/eclipse-pass/playground", //
      "https://github.com/eclipse-passage/passage-docs", //
      "https://github.com/eclipse-passage/passage-images", //
      "https://github.com/eclipse-pde/eclipse.pde.build", //
      "https://github.com/eclipse-platform/eclipse.platform.common", //
      "https://github.com/eclipse-platform/eclipse.platform.debug", //
      "https://github.com/eclipse-platform/eclipse.platform.images", //
      "https://github.com/eclipse-platform/eclipse.platform.releng", //
      "https://github.com/eclipse-platform/eclipse.platform.resources", //
      "https://github.com/eclipse-platform/eclipse.platform.runtime", //
      "https://github.com/eclipse-platform/eclipse.platform.team", //
      "https://github.com/eclipse-platform/eclipse.platform.text", //
      "https://github.com/eclipse-platform/eclipse.platform.ua", //
      "https://github.com/eclipse-platform/eclipse.platform.ui.tools", //
      "https://github.com/eclipse-ptp/ptp.doc", //
      "https://github.com/eclipse-pullpiri/pullpiri", //
      "https://github.com/eclipse-qrisp/Qrisp", //
      "https://github.com/eclipse-quneiform/quneiform", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.osgi-packaging", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.releng", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.richtext", //
      "https://github.com/eclipse-rdf4j/rdf4j-testsuite", //
      "https://github.com/eclipse-rmf/org.eclipse.rmf.documentation", //
      "https://github.com/eclipse-scava/scava-datasets", //
      "https://github.com/eclipse-score/bazel_registry", //
      "https://github.com/eclipse-score/inc_feo", //
      "https://github.com/eclipse-score/inc_mw_com", //
      "https://github.com/eclipse-score/inc_mw_log", //
      "https://github.com/eclipse-score/inc_mw_per", //
      "https://github.com/eclipse-score/itf", //
      "https://github.com/eclipse-score/process_description", //
      "https://github.com/eclipse-score/reference_integration", //
      "https://github.com/eclipse-score/score", //
      "https://github.com/eclipse-scout/scout.ci", //
      "https://github.com/eclipse-scout/scout.maven-master", //
      "https://github.com/eclipse-sdv-blueprints/blueprints", //
      "https://github.com/eclipse-sdv-blueprints/companion-application", //
      "https://github.com/eclipse-sdv-blueprints/fleet-management", //
      "https://github.com/eclipse-sdv-blueprints/insurance", //
      "https://github.com/eclipse-sdv-blueprints/ros-racer", //
      "https://github.com/eclipse-sdv-blueprints/service-to-signal", //
      "https://github.com/eclipse-sdv-blueprints/software-orchestration", //
      "https://github.com/eclipse-sensinact/org.eclipse.sensinact", //
      "https://github.com/eclipse-sensinact/org.eclipse.sensinact.studioweb", //
      "https://github.com/eclipse-set/build", //
      "https://github.com/eclipse-shellwax/shellwax", //
      "https://github.com/eclipse-simrel/help.eclipse.org", //
      "https://github.com/eclipse-simrel/simrel.build", //
      "https://github.com/eclipse-simrel/simrel.tools", //
      "https://github.com/eclipse-sirius/sirius-specs", //
      "https://github.com/eclipse-sisu/sisu.mojos", //
      "https://github.com/eclipse-slm/ansible", //
      "https://github.com/eclipse-slm/ansible-collection-aas", //
      "https://github.com/eclipse-slm/awx", //
      "https://github.com/eclipse-slm/molecule", //
      "https://github.com/eclipse-slm/molecule_vsphere", //
      "https://github.com/eclipse-slm/resource-self-description-service", //
      "https://github.com/eclipse-slm/slm-aas", //
      "https://github.com/eclipse-slm/slm-ansible-role-basyx-server", //
      "https://github.com/eclipse-slm/slm-ansible-role-consul", //
      "https://github.com/eclipse-slm/slm-ansible-role-docker", //
      "https://github.com/eclipse-slm/slm-ansible-role-inventory-helper", //
      "https://github.com/eclipse-slm/slm-ansible-role-node-exporter", //
      "https://github.com/eclipse-slm/slm-cc-base", //
      "https://github.com/eclipse-slm/slm-dc-codesys", //
      "https://github.com/eclipse-slm/slm-dc-docker", //
      "https://github.com/eclipse-slm/slm-dc-docker-portainer", //
      "https://github.com/eclipse-slm/slm-dc-docker-swarm", //
      "https://github.com/eclipse-slm/slm-dc-docker-tcp", //
      "https://github.com/eclipse-slm/slm-dc-dummy", //
      "https://github.com/eclipse-slm/slm-dc-k3s", //
      "https://github.com/eclipse-slm/slm-dc-k3s-single", //
      "https://github.com/eclipse-slm/slm-dc-k8s", //
      "https://github.com/eclipse-slm/slm-monitoring-prometheus-aas", //
      "https://github.com/eclipse-slm/slm-pr-ansible-facts", //
      "https://github.com/eclipse-slm/slm-pr-webcams", //
      "https://github.com/eclipse-sommr/sommr", //
      "https://github.com/eclipse-sparkplug/sparkplug.listings", //
      "https://github.com/eclipse-sprotty/sprotty", //
      "https://github.com/eclipse-sprotty/sprotty-previews", //
      "https://github.com/eclipse-sprotty/sprotty-theia", //
      "https://github.com/eclipse-sprotty/sprotty-vscode", //
      "https://github.com/eclipse-store/docs-site", //
      "https://github.com/eclipse-streamsheets/streamsheets", //
      "https://github.com/eclipse-sumo/homebrew-sumo", //
      "https://github.com/eclipse-sw360/sw360-frontend", //
      "https://github.com/eclipse-swtimagej/SWTImageJ", //
      "https://github.com/eclipse-symphony/docs", //
      "https://github.com/eclipse-symphony/symphony", //
      "https://github.com/eclipse-theia/cryptodetector", //
      "https://github.com/eclipse-theia/discourse-forum-archive", //
      "https://github.com/eclipse-theia/dugite-extra", //
      "https://github.com/eclipse-theia/dugite-no-gpl", //
      "https://github.com/eclipse-theia/generator-theia-extension", //
      "https://github.com/eclipse-theia/security-audit", //
      "https://github.com/eclipse-theia/theia", //
      "https://github.com/eclipse-theia/theia-blueprint", //
      "https://github.com/eclipse-theia/theia-cloud-helm", //
      "https://github.com/eclipse-theia/theia-cpp-extensions", //
      "https://github.com/eclipse-theia/theia-e2e-test-suite", //
      "https://github.com/eclipse-theia/theia-generator-plugin", //
      "https://github.com/eclipse-theia/theia-ide", //
      "https://github.com/eclipse-theia/theia-ide-snap", //
      "https://github.com/eclipse-theia/theia-playwright-template", //
      "https://github.com/eclipse-theia/theia-plugin-packager", //
      "https://github.com/eclipse-theia/theia-vscodecov", //
      "https://github.com/eclipse-theia/theia-yeoman-plugin", //
      "https://github.com/eclipse-theia/vscode-builtin-extensions", //
      "https://github.com/eclipse-theia/vscode-theia-comparator", //
      "https://github.com/eclipse-thingweb/dart_wot", //
      "https://github.com/eclipse-thingweb/domus-tdd-api", //
      "https://github.com/eclipse-thingweb/examples", //
      "https://github.com/eclipse-thingweb/infrastructure", //
      "https://github.com/eclipse-thingweb/node-red", //
      "https://github.com/eclipse-thingweb/node-wot", //
      "https://github.com/eclipse-thingweb/playground", //
      "https://github.com/eclipse-thingweb/td-code", //
      "https://github.com/eclipse-thingweb/td-tools", //
      "https://github.com/eclipse-thingweb/test-things", //
      "https://github.com/eclipse-thingweb/thingweb", //
      "https://github.com/eclipse-thingweb/wam", //
      "https://github.com/eclipse-threadx/cmsis-packs", //
      "https://github.com/eclipse-threadx/filex", //
      "https://github.com/eclipse-threadx/getting-started", //
      "https://github.com/eclipse-threadx/guix", //
      "https://github.com/eclipse-threadx/iot-devkit", //
      "https://github.com/eclipse-threadx/levelx", //
      "https://github.com/eclipse-threadx/netxduo", //
      "https://github.com/eclipse-threadx/rtos-docs", //
      "https://github.com/eclipse-threadx/rtos-docs-asciidoc", //
      "https://github.com/eclipse-threadx/rtos-docs-html", //
      "https://github.com/eclipse-threadx/samples", //
      "https://github.com/eclipse-threadx/supported-platforms", //
      "https://github.com/eclipse-threadx/threadx", //
      "https://github.com/eclipse-threadx/threadx-learn-samples", //
      "https://github.com/eclipse-threadx/tracex", //
      "https://github.com/eclipse-threadx/usbx", //
      "https://github.com/eclipse-tinydtls/tinydtls", //
      "https://github.com/eclipse-tocandira/Container-Watchdog", //
      "https://github.com/eclipse-tocandira/Tocandira", //
      "https://github.com/eclipse-tocandira/Tocandira-Dashboard", //
      "https://github.com/eclipse-tocandira/Tocandira-api-gateway", //
      "https://github.com/eclipse-tocandira/Tocandira-back", //
      "https://github.com/eclipse-tocandira/Tocandira-front", //
      "https://github.com/eclipse-tocandira/Tocandira-opcua-gateway", //
      "https://github.com/eclipse-tracecompass/tmll", //
      "https://github.com/eclipse-tracecompass/tracecompass-infra", //
      "https://github.com/eclipse-tracecompass/tracecompass-test-traces", //
      "https://github.com/eclipse-tractusx/api-hub", //
      "https://github.com/eclipse-tractusx/app-dashboard", //
      "https://github.com/eclipse-tractusx/asset-tracking-platform", //
      "https://github.com/eclipse-tractusx/bpdm", //
      "https://github.com/eclipse-tractusx/bpdm-certificate-management", //
      "https://github.com/eclipse-tractusx/bpdm-upload-tool", //
      "https://github.com/eclipse-tractusx/charts", //
      "https://github.com/eclipse-tractusx/daps-helm-chart", //
      "https://github.com/eclipse-tractusx/demand-capacity-mgmt", //
      "https://github.com/eclipse-tractusx/demand-capacity-mgmt-backend", //
      "https://github.com/eclipse-tractusx/demand-capacity-mgmt-frontend", //
      "https://github.com/eclipse-tractusx/e2e-testing", //
      "https://github.com/eclipse-tractusx/eclipse-tractusx.github.io.largefiles", //
      "https://github.com/eclipse-tractusx/eco-pass-kit", //
      "https://github.com/eclipse-tractusx/emergingtechnologies", //
      "https://github.com/eclipse-tractusx/esc-backbone", //
      "https://github.com/eclipse-tractusx/identity-trust", //
      "https://github.com/eclipse-tractusx/industry-core-hub", //
      "https://github.com/eclipse-tractusx/knowledge-agents", //
      "https://github.com/eclipse-tractusx/knowledge-agents-aas-bridge", //
      "https://github.com/eclipse-tractusx/knowledge-agents-edc", //
      "https://github.com/eclipse-tractusx/knowledge-agents-ontology", //
      "https://github.com/eclipse-tractusx/managed-identity-wallets-archived", //
      "https://github.com/eclipse-tractusx/managed-simple-data-exchanger", //
      "https://github.com/eclipse-tractusx/managed-simple-data-exchanger-frontend", //
      "https://github.com/eclipse-tractusx/online-simulation-kit", //
      "https://github.com/eclipse-tractusx/ontology", //
      "https://github.com/eclipse-tractusx/pcf-exchange-kit", //
      "https://github.com/eclipse-tractusx/policy-hub", //
      "https://github.com/eclipse-tractusx/portal", //
      "https://github.com/eclipse-tractusx/portal-backend", //
      "https://github.com/eclipse-tractusx/portal-cd", //
      "https://github.com/eclipse-tractusx/portal-frontend", //
      "https://github.com/eclipse-tractusx/portal-frontend-registration", //
      "https://github.com/eclipse-tractusx/portal-iam", //
      "https://github.com/eclipse-tractusx/portal-shared-components", //
      "https://github.com/eclipse-tractusx/puris", //
      "https://github.com/eclipse-tractusx/puris-frontend", //
      "https://github.com/eclipse-tractusx/quality-dashboard", //
      "https://github.com/eclipse-tractusx/sig-architecture", //
      "https://github.com/eclipse-tractusx/sig-infra", //
      "https://github.com/eclipse-tractusx/sig-release", //
      "https://github.com/eclipse-tractusx/sig-security", //
      "https://github.com/eclipse-tractusx/sig-testing", //
      "https://github.com/eclipse-tractusx/sldt-ontology-model", //
      "https://github.com/eclipse-tractusx/sldt-semantic-models", //
      "https://github.com/eclipse-tractusx/ssi-authority-schema-registry", //
      "https://github.com/eclipse-tractusx/ssi-credential-issuer", //
      "https://github.com/eclipse-tractusx/ssi-dim-wallet-stub", //
      "https://github.com/eclipse-tractusx/ssi-docu", //
      "https://github.com/eclipse-tractusx/testdata-provider", //
      "https://github.com/eclipse-tractusx/tractus-x-release", //
      "https://github.com/eclipse-tractusx/tractus-x-umbrella", //
      "https://github.com/eclipse-tractusx/tractusx-edc-template", //
      "https://github.com/eclipse-tractusx/tractusx-profiles", //
      "https://github.com/eclipse-tractusx/tractusx-quality-checks", //
      "https://github.com/eclipse-tractusx/tutorial-resources", //
      "https://github.com/eclipse-tractusx/vas-country-risk", //
      "https://github.com/eclipse-tradista/tradista", //
      "https://github.com/eclipse-uprotocol/manifests", //
      "https://github.com/eclipse-uprotocol/up-akka", //
      "https://github.com/eclipse-uprotocol/up-android-example", //
      "https://github.com/eclipse-uprotocol/up-android-helloworld", //
      "https://github.com/eclipse-uprotocol/up-client-android-kotlin", //
      "https://github.com/eclipse-uprotocol/up-client-android-rust", //
      "https://github.com/eclipse-uprotocol/up-client-azure-java", //
      "https://github.com/eclipse-uprotocol/up-client-mqtt5-java", //
      "https://github.com/eclipse-uprotocol/up-client-mqtt5-python", //
      "https://github.com/eclipse-uprotocol/up-client-mqtt5-rust", //
      "https://github.com/eclipse-uprotocol/up-client-sommr-rust", //
      "https://github.com/eclipse-uprotocol/up-client-springboot-java", //
      "https://github.com/eclipse-uprotocol/up-client-vsomeip-cpp", //
      "https://github.com/eclipse-uprotocol/up-client-vsomeip-python", //
      "https://github.com/eclipse-uprotocol/up-client-vsomeip-rust", //
      "https://github.com/eclipse-uprotocol/up-client-zenoh-cpp", //
      "https://github.com/eclipse-uprotocol/up-client-zenoh-java", //
      "https://github.com/eclipse-uprotocol/up-client-zenoh-python", //
      "https://github.com/eclipse-uprotocol/up-client-zenoh-rust", //
      "https://github.com/eclipse-uprotocol/up-conan-recipes", //
      "https://github.com/eclipse-uprotocol/up-core-api", //
      "https://github.com/eclipse-uprotocol/up-cpp", //
      "https://github.com/eclipse-uprotocol/up-discovery-cpp", //
      "https://github.com/eclipse-uprotocol/up-experiments", //
      "https://github.com/eclipse-uprotocol/up-kalix", //
      "https://github.com/eclipse-uprotocol/up-kotlin", //
      "https://github.com/eclipse-uprotocol/up-player-cpp", //
      "https://github.com/eclipse-uprotocol/up-python", //
      "https://github.com/eclipse-uprotocol/up-recorder-cpp", //
      "https://github.com/eclipse-uprotocol/up-rust", //
      "https://github.com/eclipse-uprotocol/up-simulator", //
      "https://github.com/eclipse-uprotocol/up-spec", //
      "https://github.com/eclipse-uprotocol/up-streamer-rust", //
      "https://github.com/eclipse-uprotocol/up-subscription-cpp", //
      "https://github.com/eclipse-uprotocol/up-subscription-rust", //
      "https://github.com/eclipse-uprotocol/up-tck", //
      "https://github.com/eclipse-uprotocol/up-tools", //
      "https://github.com/eclipse-uprotocol/up-transport-android-kotlin", //
      "https://github.com/eclipse-uprotocol/up-transport-android-rust", //
      "https://github.com/eclipse-uprotocol/up-transport-azure-java", //
      "https://github.com/eclipse-uprotocol/up-transport-mqtt5-java", //
      "https://github.com/eclipse-uprotocol/up-transport-mqtt5-python", //
      "https://github.com/eclipse-uprotocol/up-transport-mqtt5-rust", //
      "https://github.com/eclipse-uprotocol/up-transport-socket", //
      "https://github.com/eclipse-uprotocol/up-transport-springboot-java", //
      "https://github.com/eclipse-uprotocol/up-transport-vsomeip-cpp", //
      "https://github.com/eclipse-uprotocol/up-transport-vsomeip-python", //
      "https://github.com/eclipse-uprotocol/up-transport-vsomeip-rust", //
      "https://github.com/eclipse-uprotocol/up-transport-zenoh-cpp", //
      "https://github.com/eclipse-uprotocol/up-transport-zenoh-python", //
      "https://github.com/eclipse-uprotocol/up-transport-zenoh-rust", //
      "https://github.com/eclipse-uprotocol/up-vsomeip-helloworld", //
      "https://github.com/eclipse-uprotocol/up-zenoh-example-cpp", //
      "https://github.com/eclipse-uprotocol/up-zenoh-example-rust", //
      "https://github.com/eclipse-uprotocol/uprotocol-android", //
      "https://github.com/eclipse-uprotocol/uprotocol-core-api", //
      "https://github.com/eclipse-uprotocol/uprotocol-cpp", //
      "https://github.com/eclipse-uprotocol/uprotocol-java-ulink-android", //
      "https://github.com/eclipse-uprotocol/uprotocol-java-ulink-zenoh", //
      "https://github.com/eclipse-uprotocol/uprotocol-platform-android", //
      "https://github.com/eclipse-uprotocol/uprotocol-platform-simulator", //
      "https://github.com/eclipse-uprotocol/uprotocol-python", //
      "https://github.com/eclipse-uprotocol/uprotocol-python-ulink-zenoh", //
      "https://github.com/eclipse-uprotocol/uprotocol-roadmap", //
      "https://github.com/eclipse-uprotocol/uprotocol-rust", //
      "https://github.com/eclipse-uprotocol/uprotocol-sdk-cpp", //
      "https://github.com/eclipse-uprotocol/uprotocol-tools", //
      "https://github.com/eclipse-uprotocol/uprotocol-ulink-zenoh-cpp", //
      "https://github.com/eclipse-velocitas/cli", //
      "https://github.com/eclipse-velocitas/devcontainer-base-images", //
      "https://github.com/eclipse-velocitas/devenv-devcontainer-setup", //
      "https://github.com/eclipse-velocitas/devenv-github-integration", //
      "https://github.com/eclipse-velocitas/devenv-github-templates", //
      "https://github.com/eclipse-velocitas/devenv-github-workflows", //
      "https://github.com/eclipse-velocitas/devenv-runtime-k3d", //
      "https://github.com/eclipse-velocitas/devenv-runtime-local", //
      "https://github.com/eclipse-velocitas/devenv-runtimes", //
      "https://github.com/eclipse-velocitas/license-check", //
      "https://github.com/eclipse-velocitas/pkg-velocitas-main", //
      "https://github.com/eclipse-velocitas/pkg-velocitas-uprotocol", //
      "https://github.com/eclipse-velocitas/release-documentation-action", //
      "https://github.com/eclipse-velocitas/vehicle-app-cpp-sdk", //
      "https://github.com/eclipse-velocitas/vehicle-app-cpp-template", //
      "https://github.com/eclipse-velocitas/vehicle-app-java-sdk", //
      "https://github.com/eclipse-velocitas/vehicle-app-kotlin-template", //
      "https://github.com/eclipse-velocitas/vehicle-app-python-sdk", //
      "https://github.com/eclipse-velocitas/vehicle-app-python-template", //
      "https://github.com/eclipse-velocitas/vehicle-app-template", //
      "https://github.com/eclipse-velocitas/vehicle-model-cpp", //
      "https://github.com/eclipse-velocitas/vehicle-model-generator", //
      "https://github.com/eclipse-velocitas/vehicle-model-python", //
      "https://github.com/eclipse-velocitas/velocitas-docs", //
      "https://github.com/eclipse-velocitas/velocitas-lib", //
      "https://github.com/eclipse-velocitas/velocitas-project-generator-npm", //
      "https://github.com/eclipse-vertx/vertx-launcher", //
      "https://github.com/eclipse-vertx/vertx-virtual-threads", //
      "https://github.com/eclipse-vertx/vertx5-parent", //
      "https://github.com/eclipse-volttron/copier-poetry-volttron-agent", //
      "https://github.com/eclipse-volttron/docker", //
      "https://github.com/eclipse-volttron/github-tooling", //
      "https://github.com/eclipse-volttron/volttron", //
      "https://github.com/eclipse-volttron/volttron-actuator", //
      "https://github.com/eclipse-volttron/volttron-agent-watcher", //
      "https://github.com/eclipse-volttron/volttron-airside-rcx", //
      "https://github.com/eclipse-volttron/volttron-ansible", //
      "https://github.com/eclipse-volttron/volttron-bacnet-proxy", //
      "https://github.com/eclipse-volttron/volttron-boptest", //
      "https://github.com/eclipse-volttron/volttron-core", //
      "https://github.com/eclipse-volttron/volttron-dnp3-master", //
      "https://github.com/eclipse-volttron/volttron-dnp3-outstation", //
      "https://github.com/eclipse-volttron/volttron-docs", //
      "https://github.com/eclipse-volttron/volttron-economizer-rcx", //
      "https://github.com/eclipse-volttron/volttron-emailer", //
      "https://github.com/eclipse-volttron/volttron-energyplus", //
      "https://github.com/eclipse-volttron/volttron-file-watcher", //
      "https://github.com/eclipse-volttron/volttron-forward-historian", //
      "https://github.com/eclipse-volttron/volttron-gridappsd", //
      "https://github.com/eclipse-volttron/volttron-heat-recovery", //
      "https://github.com/eclipse-volttron/volttron-ilc", //
      "https://github.com/eclipse-volttron/volttron-lib-auth", //
      "https://github.com/eclipse-volttron/volttron-lib-bacnet-driver", //
      "https://github.com/eclipse-volttron/volttron-lib-base-driver", //
      "https://github.com/eclipse-volttron/volttron-lib-base-historian", //
      "https://github.com/eclipse-volttron/volttron-lib-curve", //
      "https://github.com/eclipse-volttron/volttron-lib-dnp3-driver", //
      "https://github.com/eclipse-volttron/volttron-lib-fake-driver", //
      "https://github.com/eclipse-volttron/volttron-lib-homeassistant-driver", //
      "https://github.com/eclipse-volttron/volttron-lib-modbus-driver", //
      "https://github.com/eclipse-volttron/volttron-lib-modbustk-driver", //
      "https://github.com/eclipse-volttron/volttron-lib-rmq", //
      "https://github.com/eclipse-volttron/volttron-lib-sql-historian", //
      "https://github.com/eclipse-volttron/volttron-lib-tagging", //
      "https://github.com/eclipse-volttron/volttron-lib-tls", //
      "https://github.com/eclipse-volttron/volttron-lib-web", //
      "https://github.com/eclipse-volttron/volttron-lib-zmq", //
      "https://github.com/eclipse-volttron/volttron-listener", //
      "https://github.com/eclipse-volttron/volttron-log-statistics", //
      "https://github.com/eclipse-volttron/volttron-mongo-tagging", //
      "https://github.com/eclipse-volttron/volttron-openadr-ven", //
      "https://github.com/eclipse-volttron/volttron-platform-driver", //
      "https://github.com/eclipse-volttron/volttron-postgresql-historian", //
      "https://github.com/eclipse-volttron/volttron-proactive-diagnostic", //
      "https://github.com/eclipse-volttron/volttron-rmq", //
      "https://github.com/eclipse-volttron/volttron-sql-historian", //
      "https://github.com/eclipse-volttron/volttron-sqlite-historian", //
      "https://github.com/eclipse-volttron/volttron-sqlite-tagging", //
      "https://github.com/eclipse-volttron/volttron-sysmon", //
      "https://github.com/eclipse-volttron/volttron-test-utils", //
      "https://github.com/eclipse-volttron/volttron-testing", //
      "https://github.com/eclipse-volttron/volttron-threshold-detection", //
      "https://github.com/eclipse-volttron/volttron-topic-watcher", //
      "https://github.com/eclipse-volttron/volttron-web-client", //
      "https://github.com/eclipse-volttron/volttron-zmq", //
      "https://github.com/eclipse-wakaama/wakaama", //
      "https://github.com/eclipse-wattadvisor/WattAdvisor", //
      "https://github.com/eclipse-xacc/xacc", //
      "https://github.com/eclipse-xfsc/landingpage", //
      "https://github.com/eclipse-xfsc/org.eclipse.xfsc", //
      "https://github.com/eclipse-xpanse/policy-man", //
      "https://github.com/eclipse-xpanse/xpanse-agent", //
      "https://github.com/eclipse-xpanse/xpanse-iam", //
      "https://github.com/eclipse-xpanse/xpanse-observability", //
      "https://github.com/eclipse-xpanse/xpanse-relops", //
      "https://github.com/eclipse-xpanse/xpanse-samples", //
      "https://github.com/eclipse-xpanse/xpanse-ui", //
      "https://github.com/eclipse-xtext/xtext-archive", //
      "https://github.com/eclipse-zenoh-flow/zenoh-flow", //
      "https://github.com/eclipse-zenoh-flow/zenoh-flow-python", //
      "https://github.com/eclipse-zenoh/ci", //
      "https://github.com/eclipse-zenoh/homebrew-zenoh", //
      "https://github.com/eclipse-zenoh/roadmap", //
      "https://github.com/eclipse-zenoh/zenoh", //
      "https://github.com/eclipse-zenoh/zenoh-backend-filesystem", //
      "https://github.com/eclipse-zenoh/zenoh-backend-influxdb", //
      "https://github.com/eclipse-zenoh/zenoh-backend-rocksdb", //
      "https://github.com/eclipse-zenoh/zenoh-backend-s3", //
      "https://github.com/eclipse-zenoh/zenoh-backend-sql", //
      "https://github.com/eclipse-zenoh/zenoh-c", //
      "https://github.com/eclipse-zenoh/zenoh-cpp", //
      "https://github.com/eclipse-zenoh/zenoh-csharp", //
      "https://github.com/eclipse-zenoh/zenoh-demos", //
      "https://github.com/eclipse-zenoh/zenoh-dissector", //
      "https://github.com/eclipse-zenoh/zenoh-flow", //
      "https://github.com/eclipse-zenoh/zenoh-flow-python", //
      "https://github.com/eclipse-zenoh/zenoh-go", //
      "https://github.com/eclipse-zenoh/zenoh-java", //
      "https://github.com/eclipse-zenoh/zenoh-kotlin", //
      "https://github.com/eclipse-zenoh/zenoh-pico", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-dds", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-mqtt", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-ros1", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-ros2dds", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-webserver", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-zenoh-flow", //
      "https://github.com/eclipse-zenoh/zenoh-python", //
      "https://github.com/eclipse-zenoh/zenoh-ts", //
      "https://github.com/eclipse/adaaa", //
      "https://github.com/eclipse/che-theia-activity-tracker", //
      "https://github.com/eclipse/che-theia-dashboard-extension", //
      "https://github.com/eclipse/che-theia-emacs-plugin", //
      "https://github.com/eclipse/che-theia-env-variables-plugin", //
      "https://github.com/eclipse/che-theia-factory-extension", //
      "https://github.com/eclipse/che-theia-github-plugin", //
      "https://github.com/eclipse/che-theia-hosted-plugin-manager-extension", //
      "https://github.com/eclipse/che-theia-java-plugin", //
      "https://github.com/eclipse/che-theia-machines-plugin", //
      "https://github.com/eclipse/che-theia-samples", //
      "https://github.com/eclipse/che-theia-ssh-plugin", //
      "https://github.com/eclipse/che-theia-task-plugin", //
      "https://github.com/eclipse/che-theia-terminal-extension", //
      "https://github.com/eclipse/che-theia-vi-plugin", //
      "https://github.com/eclipse/cloe", //
      "https://github.com/eclipse/editdor", //
      "https://github.com/eclipse/kuksa.val", //
      "https://github.com/eclipse/leshan.osgi", //
      "https://github.com/eclipse/microprofile", //
      "https://github.com/eclipse/microprofile-build-infra", //
      "https://github.com/eclipse/microprofile-evolution-process", //
      "https://github.com/eclipse/microprofile-marketing", //
      "https://github.com/eclipse/microprofile-parent", //
      "https://github.com/eclipse/microprofile-service-mesh", //
      "https://github.com/eclipse/microprofile-tutorial", //
      "https://github.com/eclipse/mraa", //
      "https://github.com/eclipse/packages", //
      "https://github.com/eclipse/packages-c2e-provisioning", //
      "https://github.com/eclipse/upm", //
      "https://github.com/jakartaee/inject", //
      "https://github.com/jakartaee/inject-spec", //
      "https://github.com/jakartaee/interceptors", //
      "https://github.com/jakartaee/jakartaee-api", //
      "https://github.com/jakartaee/jakartaee-documentation", //
      "https://github.com/jakartaee/jakartaee-documentation-playbook", //
      "https://github.com/jakartaee/jakartaee-documentation-ui", //
      "https://github.com/jakartaee/jakartaee-platform", //
      "https://github.com/jakartaee/jakartaee-schemas", //
      "https://github.com/jakartaee/jakartaee-tutorial", //
      "https://github.com/jakartaee/mail-spec", //
      "https://github.com/jakartaee/managed-beans", //
      "https://github.com/jakartaee/platform", //
      "https://github.com/jetty/jetty-alpn-api", //
      "https://github.com/jetty/jetty-artifact-remote-resources", //
      "https://github.com/jetty/jetty-assembly-descriptors", //
      "https://github.com/jetty/jetty-examples", //
      "https://github.com/jetty/jetty-parent", //
      "https://github.com/jetty/jetty-perf-helper", //
      "https://github.com/jetty/jetty-schemas", //
      "https://github.com/jetty/jetty-servlet-api", //
      "https://github.com/jetty/jetty-setuid-jna", //
      "https://github.com/jetty/jetty-test-policy", //
      "https://github.com/jetty/jetty-websocket-api", //
      "https://github.com/jetty/jetty-xhtml-schemas", //
      "https://github.com/jetty/jetty.artifact.resources", //
      "https://github.com/jetty/jetty.assembly.descriptors", //
      "https://github.com/jetty/jetty.docker", //
      "https://github.com/jetty/jetty.parent", //
      "https://github.com/jetty/jetty.schemas", //
      "https://github.com/jetty/jetty.servlet.api", //
      "https://github.com/jetty/jetty.websocket.api", //
      "https://github.com/jkubeio/ci", //
      "https://github.com/jkubeio/jkube-images", //
      "https://github.com/jkubeio/katacoda-scenarios", //
      "https://github.com/locationtech/geoperil", //
      "https://github.com/locationtech/geotrellis", //
      "https://github.com/locationtech/rasterframes", //
      "https://github.com/locationtech/sfcurve", //
      "https://github.com/microprofile/microprofile", //
      "https://github.com/microprofile/microprofile-blog", //
      "https://github.com/microprofile/microprofile-bom", //
      "https://github.com/microprofile/microprofile-build-infra", //
      "https://github.com/microprofile/microprofile-build-tools", //
      "https://github.com/microprofile/microprofile-contributor-list", //
      "https://github.com/microprofile/microprofile-evolution-process", //
      "https://github.com/microprofile/microprofile-marketing", //
      "https://github.com/microprofile/microprofile-parent", //
      "https://github.com/microprofile/microprofile-presentations", //
      "https://github.com/microprofile/microprofile-service-mesh", //
      "https://github.com/microprofile/microprofile-site-config", //
      "https://github.com/microprofile/microprofile-tutorial", //
      "https://github.com/microprofile/microprofile-wg", //
      "https://github.com/microprofile/wpsite", //
      "https://github.com/openhwgroup/apb_interrupt_cntrl", //
      "https://github.com/openhwgroup/core-v-mcu", //
      "https://github.com/openhwgroup/core-v-mcu-cli-test", //
      "https://github.com/openhwgroup/core-v-mcu-demo", //
      "https://github.com/openhwgroup/core-v-mcu-devkit", //
      "https://github.com/openhwgroup/core-v-verif", //
      "https://github.com/openhwgroup/core-v-xif", //
      "https://github.com/openhwgroup/cv-hpdcache", //
      "https://github.com/openhwgroup/cv32e40p", //
      "https://github.com/openhwgroup/cv32e40s", //
      "https://github.com/openhwgroup/cv32e40s-dv", //
      "https://github.com/openhwgroup/cv32e40x", //
      "https://github.com/openhwgroup/cv32e40x-dv", //
      "https://github.com/openhwgroup/cv32e41p", //
      "https://github.com/openhwgroup/cva5", //
      "https://github.com/openhwgroup/cva6", //
      "https://github.com/openhwgroup/cva6-sdk", //
      "https://github.com/openhwgroup/cve2", //
      "https://github.com/openhwgroup/cvw", //
      "https://github.com/openhwgroup/force-riscv", //
      "https://github.com/osgi/osgi.enroute.site", //
      "https://github.com/polarsys/b612", //
      "https://github.com/polarsys/libims", //
      "https://github.com/winery/BPMN4TOSCAModeler", //
      "https://github.com/winery/mulit-repo-test", //
      "https://github.com/winery/multi-repo-dependency", //
      "https://github.com/winery/test-repository", //
      "https://github.com/winery/test-repository-yaml", //
      "https://github.com/winery/winery-topologymodeler", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_analysis", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_backend_arrayfire", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_backend_cpu", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_backend_cuda", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_backend_opencv", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_compression", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_core", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_dataloader", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_export_arm_cortexm", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_export_cpp", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_export_hls", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_export_tensorrt", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_federated", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_interop_torch", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_learning", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_module_template", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_onnx", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_quantization", //
      "https://gitlab.eclipse.org/eclipse/aidge/export_hls_fpga", //
      "https://gitlab.eclipse.org/eclipse/aidge/gitlab_shared_files", //
      "https://gitlab.eclipse.org/eclipse/aidge/host_documentation", //
      "https://gitlab.eclipse.org/eclipse/ambientlight/ambient-light-services", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.examples", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.releng", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.tools.rtc", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.tools.simulation", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.tools.simulation.examples", //
      "https://gitlab.eclipse.org/eclipse/asciidoc-lang/asciidoc-lang", //
      "https://gitlab.eclipse.org/eclipse/asciidoc-lang/asciidoc-tck", //
      "https://gitlab.eclipse.org/eclipse/austen/austen", //
      "https://gitlab.eclipse.org/eclipse/autoapiframework/code", //
      "https://gitlab.eclipse.org/eclipse/autoapiframework/documentation", //
      "https://gitlab.eclipse.org/eclipse/autowrx/analytics", //
      "https://gitlab.eclipse.org/eclipse/autowrx/autowrx", //
      "https://gitlab.eclipse.org/eclipse/autowrx/backend-core", //
      "https://gitlab.eclipse.org/eclipse/autowrx/docs", //
      "https://gitlab.eclipse.org/eclipse/autowrx/instance-overlay", //
      "https://gitlab.eclipse.org/eclipse/autowrx/ui-automation-test", //
      "https://gitlab.eclipse.org/eclipse/dash/org.eclipse.dash.handbook", //
      "https://gitlab.eclipse.org/eclipse/drops/drops-agent", //
      "https://gitlab.eclipse.org/eclipse/duttile/duttile", //
      "https://gitlab.eclipse.org/eclipse/ease/ease-scripts", //
      "https://gitlab.eclipse.org/eclipse/esf/test-esf", //
      "https://gitlab.eclipse.org/eclipse/gmf-tooling/org.eclipse.gmf-tooling.uml2tools.releng", //
      "https://gitlab.eclipse.org/eclipse/graphene/ai-interfaces", //
      "https://gitlab.eclipse.org/eclipse/graphene/ai-runner", //
      "https://gitlab.eclipse.org/eclipse/graphene/angular-frontend", //
      "https://gitlab.eclipse.org/eclipse/graphene/eclipse-graphene", //
      "https://gitlab.eclipse.org/eclipse/graphene/federation4", //
      "https://gitlab.eclipse.org/eclipse/graphene/generic-parallel-orchestrator", //
      "https://gitlab.eclipse.org/eclipse/graphene/hpc-deployer", //
      "https://gitlab.eclipse.org/eclipse/graphene/nexus-client", //
      "https://gitlab.eclipse.org/eclipse/graphene/playground-app", //
      "https://gitlab.eclipse.org/eclipse/graphene/tutorials", //
      "https://gitlab.eclipse.org/eclipse/ldt/metalua", //
      "https://gitlab.eclipse.org/eclipse/mdmbl/org.eclipse.mdm.api.uml", //
      "https://gitlab.eclipse.org/eclipse/mdmbl/org.eclipse.mdm.realms", //
      "https://gitlab.eclipse.org/eclipse/mdmbl/org.eclipse.mdmbl", //
      "https://gitlab.eclipse.org/eclipse/mpc/org-eclipse-epp-mpc", //
      "https://gitlab.eclipse.org/eclipse/mpc/org.eclipse.epp.mpc", //
      "https://gitlab.eclipse.org/eclipse/openk-coremodules/org.eclipse.openk-coremodules.contactBaseData.documentation", //
      "https://gitlab.eclipse.org/eclipse/openk-coremodules/org.eclipse.openk-coremodules.contactBaseData.frontend", //
      "https://gitlab.eclipse.org/eclipse/openk-coremodules/org.eclipse.openk-coremodules.portalFE", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/lowVoltageCockpit.documentation", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.elogbookFE", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.gridFailureInformation.documentation", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.gridFailureInformation.frontend", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.gridfailureinformation.pairs-connector", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.plannedGridMeasures.documentation", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.plannedGridMeasures.frontend", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.standbyPlanning.docu", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.standbyPlanning.frontend", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.statementPublicAffairs.documentation", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.statementPublicAffairs.frontend", //
      "https://gitlab.eclipse.org/eclipse/openpass/gt-gen-core", //
      "https://gitlab.eclipse.org/eclipse/openpass/gt-gen-simulator", //
      "https://gitlab.eclipse.org/eclipse/openpass/mantle-api", //
      "https://gitlab.eclipse.org/eclipse/openpass/map-sdk", //
      "https://gitlab.eclipse.org/eclipse/openpass/opSimulation", //
      "https://gitlab.eclipse.org/eclipse/openpass/openpass-docs-site", //
      "https://gitlab.eclipse.org/eclipse/openpass/openscenario1_engine", //
      "https://gitlab.eclipse.org/eclipse/openpass/opgui", //
      "https://gitlab.eclipse.org/eclipse/openpass/opvisualizer", //
      "https://gitlab.eclipse.org/eclipse/openpass/osi-query-library", //
      "https://gitlab.eclipse.org/eclipse/openpass/osi-traffic-participant", //
      "https://gitlab.eclipse.org/eclipse/openpass/road-logic-suite", //
      "https://gitlab.eclipse.org/eclipse/openpass/stochastics-library", //
      "https://gitlab.eclipse.org/eclipse/openpass/yase", //
      "https://gitlab.eclipse.org/eclipse/os-gov/os-gov", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.abstractstatemachine", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.autowireHelper", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.core.api", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.datainterchange.api", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.dependencies", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.dependencies-mbp", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.dependencies.p2", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.display", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.display.api", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.dsl.metadata.service", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.eventbroker", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.filter", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.fork.jpos", //
      "https://gitlab.eclipse.org/eclipse/osee/org.eclipse.osee", //
      "https://gitlab.eclipse.org/eclipse/osee/org.eclipse.ote", //
      "https://gitlab.eclipse.org/eclipse/paho.incubator/org.eclipse.paho.mqtt.lua", //
      "https://gitlab.eclipse.org/eclipse/paho.incubator/smidge", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus", //
      "https://gitlab.eclipse.org/eclipse/plato/bok", //
      "https://gitlab.eclipse.org/eclipse/plato/public-ospos", //
      "https://gitlab.eclipse.org/eclipse/plato/www", //
      "https://gitlab.eclipse.org/eclipse/refactor/org.eclipse.emf.refactor.build", //
      "https://gitlab.eclipse.org/eclipse/refactor/org.eclipse.emf.refactor.core", //
      "https://gitlab.eclipse.org/eclipse/refactor/org.eclipse.emf.refactor.documentation", //
      "https://gitlab.eclipse.org/eclipse/refactor/org.eclipse.emf.refactor.features", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.contrib", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.test", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.training", //
      "https://gitlab.eclipse.org/eclipse/scm/scm", //
      "https://gitlab.eclipse.org/eclipse/scm/scm-docs-site", //
      "https://gitlab.eclipse.org/eclipse/skybt/libraries", //
      "https://gitlab.eclipse.org/eclipse/skybt/skybt", //
      "https://gitlab.eclipse.org/eclipse/skybt/testautomation", //
      "https://gitlab.eclipse.org/eclipse/skybt/usam", //
      "https://gitlab.eclipse.org/eclipse/sw360/playground-git", //
      "https://gitlab.eclipse.org/eclipse/sw361/playground-git", //
      "https://gitlab.eclipse.org/eclipse/tcf/tcf.agent", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/dash", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/dash-maven", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/dashboard", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/license-tool/nodejs-wrapper", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/m4e-tools", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/org.eclipse.dash.handbook", //
      "https://gitlab.eclipse.org/eclipse/titan/titan-forum", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.vs-code-extension", //
      "https://gitlab.eclipse.org/eclipse/vhant/development-process", //
      "https://gitlab.eclipse.org/eclipse/vhant/specification-document-example", //
      "https://gitlab.eclipse.org/eclipse/vhant/specification-process", //
      "https://gitlab.eclipse.org/eclipse/vhant/vhant", //
      "https://gitlab.eclipse.org/eclipse/webtools/releng/webtools-releng-aggregator", //
      "https://gitlab.eclipse.org/eclipse/xfsc/ocm-w-stack-epics", //
      "https://gitlab.eclipse.org/eclipse/xfsc/orce", //
      "https://gitlab.eclipse.org/eclipse/xfsc/to-be-deleted-3", //
      "https://gitlab.eclipse.org/eclipse/xfsc/train1", //
      "https://gitlab.eclipse.org/eclipse/xfsc/wiki", //
      "https://gitlab.eclipse.org/eclipse/xfsc/xfsc-spec-2", //
      ""));

  private static Set<String> REPOSITORIES = new TreeSet<>(Set.of( //
      // generated-repositories
      "https://github.com/EclipseNebula/nebula", //
      "https://github.com/adoptium/STF", //
      "https://github.com/adoptium/TKG", //
      "https://github.com/adoptium/aqa-systemtest", //
      "https://github.com/adoptium/aqa-tests", //
      "https://github.com/adoptium/emt4j", //
      "https://github.com/adoptium/jdk", //
      "https://github.com/adoptium/openj9-systemtest", //
      "https://github.com/deeplearning4j/deeplearning4j", //
      "https://github.com/deeplearning4j/deeplearning4j-examples", //
      "https://github.com/eclipse-4diac/4diac-ide", //
      "https://github.com/eclipse-aas4j/aas4j", //
      "https://github.com/eclipse-aas4j/aas4j-model-generator", //
      "https://github.com/eclipse-aas4j/aas4j-transformation-library", //
      "https://github.com/eclipse-acceleo/acceleo", //
      "https://github.com/eclipse-actf/org.eclipse.actf", //
      "https://github.com/eclipse-actf/org.eclipse.actf.ai", //
      "https://github.com/eclipse-actf/org.eclipse.actf.common", //
      "https://github.com/eclipse-actf/org.eclipse.actf.examples", //
      "https://github.com/eclipse-actf/org.eclipse.actf.visualization", //
      "https://github.com/eclipse-acute/aCute", //
      "https://github.com/eclipse-agail/agile-core", //
      "https://github.com/eclipse-agail/agile-dbus-java-interface", //
      "https://github.com/eclipse-agail/agile-recommender", //
      "https://github.com/eclipse-amalgam/amalgam", //
      "https://github.com/eclipse-amlen/amlen", //
      "https://github.com/eclipse-aspectj/ajdt", //
      "https://github.com/eclipse-aspectj/aspectj", //
      "https://github.com/eclipse-aspectj/eclipse.jdt.core", //
      "https://github.com/eclipse-atl/atl", //
      "https://github.com/eclipse-babel/plugins", //
      "https://github.com/eclipse-basyx/basyx-archive", //
      "https://github.com/eclipse-basyx/basyx-databridge", //
      "https://github.com/eclipse-basyx/basyx-demonstrators", //
      "https://github.com/eclipse-basyx/basyx-java-components", //
      "https://github.com/eclipse-basyx/basyx-java-examples", //
      "https://github.com/eclipse-basyx/basyx-java-sdk", //
      "https://github.com/eclipse-basyx/basyx-java-server-sdk", //
      "https://github.com/eclipse-birt/birt", //
      "https://github.com/eclipse-buildship/buildship", //
      "https://github.com/eclipse-californium/californium", //
      "https://github.com/eclipse-californium/californium.actinium", //
      "https://github.com/eclipse-californium/californium.tools", //
      "https://github.com/eclipse-capella/capella", //
      "https://github.com/eclipse-capella/capella-basic-vp", //
      "https://github.com/eclipse-capella/capella-cybersecurity", //
      "https://github.com/eclipse-capella/capella-deferred-merge", //
      "https://github.com/eclipse-capella/capella-filtering", //
      "https://github.com/eclipse-capella/capella-requirements-vp", //
      "https://github.com/eclipse-capella/capella-sss-transition", //
      "https://github.com/eclipse-capella/capella-studio", //
      "https://github.com/eclipse-capella/capella-textual-editor", //
      "https://github.com/eclipse-capella/capella-tools", //
      "https://github.com/eclipse-capella/capella-vpms", //
      "https://github.com/eclipse-capella/capella-xhtml-docgen", //
      "https://github.com/eclipse-capella/capella-xmlpivot", //
      "https://github.com/eclipse-capra/capra", //
      "https://github.com/eclipse-cbi/macos-notarization-service", //
      "https://github.com/eclipse-cbi/org.eclipse.cbi", //
      "https://github.com/eclipse-cbi/p2repo-aggregator", //
      "https://github.com/eclipse-cbi/p2repo-analyzers", //
      "https://github.com/eclipse-cbi/targetplatform-dsl", //
      "https://github.com/eclipse-cdo/cdo", //
      "https://github.com/eclipse-cdo/cdo.infrastructure", //
      "https://github.com/eclipse-cdo/cdo.old", //
      "https://github.com/eclipse-cdt/cdt", //
      "https://github.com/eclipse-cdt/cdt-lsp", //
      "https://github.com/eclipse-cft/cft", //
      "https://github.com/eclipse-che/che-ls-jdt", //
      "https://github.com/eclipse-che/che-plugin-svn", //
      "https://github.com/eclipse-che/che-server", //
      "https://github.com/eclipse-che4z/che-che4z-lsp-for-cobol", //
      "https://github.com/eclipse-chemclipse/chemclipse", //
      "https://github.com/eclipse-cognicrypt/CogniCrypt", //
      "https://github.com/eclipse-collections/eclipse-collections", //
      "https://github.com/eclipse-collections/eclipse-collections-kata", //
      "https://github.com/eclipse-corrosion/corrosion", //
      "https://github.com/eclipse-daanse/legacy.xmla", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.emf.dbmapping", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.emf.model", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.io.fs.watcher", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.jakarta.servlet", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.jakarta.xml.ws", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.jdbc.datasource", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.jdbc.db", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.rdb", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.report", //
      "https://github.com/eclipse-daanse/org.eclipse.daanse.rolap.mapping", //
      "https://github.com/eclipse-dali/webtools.dali", //
      "https://github.com/eclipse-dash/dash-licenses", //
      "https://github.com/eclipse-dataspacetck/dsp-tck", //
      "https://github.com/eclipse-dataspacetck/tck-build", //
      "https://github.com/eclipse-dataspacetck/tck-common", //
      "https://github.com/eclipse-datatools/datatools", //
      "https://github.com/eclipse-diffmerge/org.eclipse.emf.diffmerge.coevolution", //
      "https://github.com/eclipse-diffmerge/org.eclipse.emf.diffmerge.core", //
      "https://github.com/eclipse-diffmerge/org.eclipse.emf.diffmerge.patch", //
      "https://github.com/eclipse-diffmerge/org.eclipse.emf.diffmerge.patterns", //
      "https://github.com/eclipse-dirigible/dirigible", //
      "https://github.com/eclipse-ditto/ditto", //
      "https://github.com/eclipse-ditto/ditto-clients", //
      "https://github.com/eclipse-ditto/ditto-examples", //
      "https://github.com/eclipse-ditto/ditto-testing", //
      "https://github.com/eclipse-dltk/dltk.core", //
      "https://github.com/eclipse-dltk/dltk.examples", //
      "https://github.com/eclipse-dltk/dltk.releng", //
      "https://github.com/eclipse-dltk/dltk.tcl", //
      "https://github.com/eclipse-eclemma/eclemma", //
      "https://github.com/eclipse-ecoretools/ecoretools", //
      "https://github.com/eclipse-ecp/org.eclipse.emf.ecp.core", //
      "https://github.com/eclipse-ecp/org.eclipse.emf.ecp.other", //
      "https://github.com/eclipse-ecsp/cache-utils", //
      "https://github.com/eclipse-ecsp/entities", //
      "https://github.com/eclipse-ecsp/transformers", //
      "https://github.com/eclipse-ecsp/utils", //
      "https://github.com/eclipse-edapt/edapt", //
      "https://github.com/eclipse-edc/Connector", //
      "https://github.com/eclipse-edc/FederatedCatalog", //
      "https://github.com/eclipse-edc/GradlePlugins", //
      "https://github.com/eclipse-edc/IdentityHub", //
      "https://github.com/eclipse-edc/MinimumViableDataspace", //
      "https://github.com/eclipse-edc/RegistrationService", //
      "https://github.com/eclipse-edc/Runtime-Metamodel", //
      "https://github.com/eclipse-edc/Samples", //
      "https://github.com/eclipse-edc/Technology-Aws", //
      "https://github.com/eclipse-edc/Technology-Azure", //
      "https://github.com/eclipse-edc/Technology-Gcp", //
      "https://github.com/eclipse-edc/TrustFrameworkAdoption", //
      "https://github.com/eclipse-ee4j/angus-mail", //
      "https://github.com/eclipse-ee4j/cargotracker", //
      "https://github.com/eclipse-ee4j/cdi-cpl", //
      "https://github.com/eclipse-ee4j/concurro", //
      "https://github.com/eclipse-ee4j/eclipselink", //
      "https://github.com/eclipse-ee4j/eclipselink-build-support", //
      "https://github.com/eclipse-ee4j/eclipselink-oracleddlparser", //
      "https://github.com/eclipse-ee4j/eclipselink-workbench", //
      "https://github.com/eclipse-ee4j/enterprise-deployment", //
      "https://github.com/eclipse-ee4j/epicyro", //
      "https://github.com/eclipse-ee4j/exousia", //
      "https://github.com/eclipse-ee4j/expressly", //
      "https://github.com/eclipse-ee4j/genericmessagingra", //
      "https://github.com/eclipse-ee4j/glassfish", //
      "https://github.com/eclipse-ee4j/glassfish-build-maven-plugin", //
      "https://github.com/eclipse-ee4j/glassfish-concurro", //
      "https://github.com/eclipse-ee4j/glassfish-copyright-plugin", //
      "https://github.com/eclipse-ee4j/glassfish-fighterfish", //
      "https://github.com/eclipse-ee4j/glassfish-ha-api", //
      "https://github.com/eclipse-ee4j/glassfish-hk2", //
      "https://github.com/eclipse-ee4j/glassfish-hk2-extra", //
      "https://github.com/eclipse-ee4j/glassfish-jsftemplating", //
      "https://github.com/eclipse-ee4j/glassfish-logging-annotation-processor", //
      "https://github.com/eclipse-ee4j/glassfish-security-plugin", //
      "https://github.com/eclipse-ee4j/glassfish-shoal", //
      "https://github.com/eclipse-ee4j/glassfish-spec-version-maven-plugin", //
      "https://github.com/eclipse-ee4j/glassfish-woodstock", //
      "https://github.com/eclipse-ee4j/grizzly", //
      "https://github.com/eclipse-ee4j/grizzly-ahc", //
      "https://github.com/eclipse-ee4j/grizzly-memcached", //
      "https://github.com/eclipse-ee4j/grizzly-thrift", //
      "https://github.com/eclipse-ee4j/jax-rpc-api", //
      "https://github.com/eclipse-ee4j/jax-rpc-ri", //
      "https://github.com/eclipse-ee4j/jaxb-dtd-parser", //
      "https://github.com/eclipse-ee4j/jaxb-fi", //
      "https://github.com/eclipse-ee4j/jaxb-istack-commons", //
      "https://github.com/eclipse-ee4j/jaxb-ri", //
      "https://github.com/eclipse-ee4j/jaxb-stax-ex", //
      "https://github.com/eclipse-ee4j/jaxr-api", //
      "https://github.com/eclipse-ee4j/jaxr-ri", //
      "https://github.com/eclipse-ee4j/jersey", //
      "https://github.com/eclipse-ee4j/krazo", //
      "https://github.com/eclipse-ee4j/krazo-extensions", //
      "https://github.com/eclipse-ee4j/management-api", //
      "https://github.com/eclipse-ee4j/metro-jax-ws", //
      "https://github.com/eclipse-ee4j/metro-mimepull", //
      "https://github.com/eclipse-ee4j/metro-package-rename-task", //
      "https://github.com/eclipse-ee4j/metro-policy", //
      "https://github.com/eclipse-ee4j/metro-saaj", //
      "https://github.com/eclipse-ee4j/metro-ws-test-harness", //
      "https://github.com/eclipse-ee4j/metro-wsit", //
      "https://github.com/eclipse-ee4j/metro-xmlstreambuffer", //
      "https://github.com/eclipse-ee4j/mojarra", //
      "https://github.com/eclipse-ee4j/mojarra-jsf-extensions", //
      "https://github.com/eclipse-ee4j/odi", //
      "https://github.com/eclipse-ee4j/openmq", //
      "https://github.com/eclipse-ee4j/orb", //
      "https://github.com/eclipse-ee4j/orb-gmbal", //
      "https://github.com/eclipse-ee4j/orb-gmbal-commons", //
      "https://github.com/eclipse-ee4j/orb-gmbal-pfl", //
      "https://github.com/eclipse-ee4j/parsson", //
      "https://github.com/eclipse-ee4j/soteria", //
      "https://github.com/eclipse-ee4j/tyrus", //
      "https://github.com/eclipse-ee4j/wasp", //
      "https://github.com/eclipse-ee4j/yasson", //
      "https://github.com/eclipse-eef/org.eclipse.eef", //
      "https://github.com/eclipse-efm/efm-modelling", //
      "https://github.com/eclipse-efx/efxclipse", //
      "https://github.com/eclipse-efx/efxclipse-drift", //
      "https://github.com/eclipse-efx/efxclipse-eclipse", //
      "https://github.com/eclipse-efx/efxclipse-rt", //
      "https://github.com/eclipse-egerrit/egerrit", //
      "https://github.com/eclipse-egf/emf.egf", //
      "https://github.com/eclipse-egit/egit", //
      "https://github.com/eclipse-egit/egit-github", //
      "https://github.com/eclipse-elk/elk", //
      "https://github.com/eclipse-embed-cdt/eclipse-plugins", //
      "https://github.com/eclipse-emf-compare/emf-compare", //
      "https://github.com/eclipse-emf-compare/emf-compare-cli", //
      "https://github.com/eclipse-emf-parsley/emf-parsley", //
      "https://github.com/eclipse-emf/org.eclipse.emf", //
      "https://github.com/eclipse-emfatic/emfatic", //
      "https://github.com/eclipse-emfcloud/coffee-editor", //
      "https://github.com/eclipse-emfcloud/ecore-glsp", //
      "https://github.com/eclipse-emfcloud/emfcloud-modelserver", //
      "https://github.com/eclipse-emfcloud/emfjson-jackson", //
      "https://github.com/eclipse-emfcloud/modelserver-glsp-integration", //
      "https://github.com/eclipse-emfservices/emf-query", //
      "https://github.com/eclipse-emfservices/emf-transaction", //
      "https://github.com/eclipse-emfservices/emf-validation", //
      "https://github.com/eclipse-emfstore/org.eclipse.emf.emfstore.core", //
      "https://github.com/eclipse-epsilon/epsilon", //
      "https://github.com/eclipse-equinox/equinox", //
      "https://github.com/eclipse-equinox/p2", //
      "https://github.com/eclipse-esmf/esmf-aspect-model-editor-backend", //
      "https://github.com/eclipse-esmf/esmf-sdk", //
      "https://github.com/eclipse-esmf/esmf-semantic-aspect-meta-model", //
      "https://github.com/eclipse-gef/gef", //
      "https://github.com/eclipse-gef/gef-classic", //
      "https://github.com/eclipse-gemini/gemini.blueprint", //
      "https://github.com/eclipse-gemini/gemini.dbaccess", //
      "https://github.com/eclipse-gemini/gemini.jpa", //
      "https://github.com/eclipse-gemini/gemini.management", //
      "https://github.com/eclipse-gemini/gemini.naming", //
      "https://github.com/eclipse-gemini/gemini.web", //
      "https://github.com/eclipse-gemoc/gemoc-studio", //
      "https://github.com/eclipse-gemoc/gemoc-studio-commons", //
      "https://github.com/eclipse-gemoc/gemoc-studio-execution-ale", //
      "https://github.com/eclipse-gemoc/gemoc-studio-execution-java", //
      "https://github.com/eclipse-gemoc/gemoc-studio-execution-moccml", //
      "https://github.com/eclipse-gemoc/gemoc-studio-moccml", //
      "https://github.com/eclipse-gemoc/gemoc-studio-modeldebugging", //
      "https://github.com/eclipse-glsp/glsp-eclipse-integration", //
      "https://github.com/eclipse-glsp/glsp-examples", //
      "https://github.com/eclipse-glsp/glsp-server", //
      "https://github.com/eclipse-gmf-runtime/gmf-notation", //
      "https://github.com/eclipse-gmf-runtime/gmf-runtime", //
      "https://github.com/eclipse-handly/handly", //
      "https://github.com/eclipse-hawkbit/hawkbit", //
      "https://github.com/eclipse-hawkbit/hawkbit-examples", //
      "https://github.com/eclipse-hawkbit/hawkbit-extensions", //
      "https://github.com/eclipse-henshin/henshin", //
      "https://github.com/eclipse-hono/hono", //
      "https://github.com/eclipse-hono/hono-extras", //
      "https://github.com/eclipse-imagen/imagen", //
      "https://github.com/eclipse-iofog/Agent", //
      "https://github.com/eclipse-iofog/Connector", //
      "https://github.com/eclipse-iofog/iofog-java-sdk", //
      "https://github.com/eclipse-iofog/test-message-generator", //
      "https://github.com/eclipse-january/january", //
      "https://github.com/eclipse-january/january-forms", //
      "https://github.com/eclipse-jbom/jbom", //
      "https://github.com/eclipse-jdt/eclipse.jdt.core", //
      "https://github.com/eclipse-jdt/eclipse.jdt.debug", //
      "https://github.com/eclipse-jdt/eclipse.jdt.ui", //
      "https://github.com/eclipse-jdtls/eclipse-jdt-core-incubator", //
      "https://github.com/eclipse-jdtls/eclipse.jdt.ls", //
      "https://github.com/eclipse-jeetools/webtools.javaee", //
      "https://github.com/eclipse-jgit/jgit", //
      "https://github.com/eclipse-jifa/jifa", //
      "https://github.com/eclipse-jkube/jkube", //
      "https://github.com/eclipse-jkube/jkube-integration-tests", //
      "https://github.com/eclipse-jnosql/jnosql", //
      "https://github.com/eclipse-jnosql/jnosql-databases", //
      "https://github.com/eclipse-jnosql/jnosql-extensions", //
      "https://github.com/eclipse-jsdt/webtools.jsdt", //
      "https://github.com/eclipse-jsf/webtools.jsf", //
      "https://github.com/eclipse-justj/justj.tools", //
      "https://github.com/eclipse-kapua/kapua", //
      "https://github.com/eclipse-keti/keti", //
      "https://github.com/eclipse-keyple/keyple-card-calypso-crypto-legacysam-java-lib", //
      "https://github.com/eclipse-keyple/keyple-card-calypso-java-lib", //
      "https://github.com/eclipse-keyple/keyple-card-generic-java-lib", //
      "https://github.com/eclipse-keyple/keyple-distributed-local-java-lib", //
      "https://github.com/eclipse-keyple/keyple-distributed-network-java-lib", //
      "https://github.com/eclipse-keyple/keyple-distributed-remote-java-api", //
      "https://github.com/eclipse-keyple/keyple-distributed-remote-java-lib", //
      "https://github.com/eclipse-keyple/keyple-integration-java-test", //
      "https://github.com/eclipse-keyple/keyple-java", //
      "https://github.com/eclipse-keyple/keyple-java-example", //
      "https://github.com/eclipse-keyple/keyple-plugin-cardresource-java-lib", //
      "https://github.com/eclipse-keyple/keyple-plugin-java-api", //
      "https://github.com/eclipse-keyple/keyple-plugin-pcsc-java-lib", //
      "https://github.com/eclipse-keyple/keyple-plugin-stub-java-lib", //
      "https://github.com/eclipse-keyple/keyple-service-java-lib", //
      "https://github.com/eclipse-keyple/keyple-service-resource-java-lib", //
      "https://github.com/eclipse-keyple/keyple-util-java-lib", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-asymmetric-java-api", //
      "https://github.com/eclipse-kitalpha/kitalpha", //
      "https://github.com/eclipse-kitalpha/kitalpha-addons", //
      "https://github.com/eclipse-kuksa/kuksa.apps", //
      "https://github.com/eclipse-kuksa/kuksa.cloud", //
      "https://github.com/eclipse-kuksa/kuksa.ide", //
      "https://github.com/eclipse-kura/kura", //
      "https://github.com/eclipse-lemminx/lemminx", //
      "https://github.com/eclipse-lemminx/lemminx-maven", //
      "https://github.com/eclipse-leshan/leshan", //
      "https://github.com/eclipse-linuxtools/org.eclipse.linuxtools", //
      "https://github.com/eclipse-lsp4e/lsp4e", //
      "https://github.com/eclipse-lsp4j/lsp4j", //
      "https://github.com/eclipse-lsp4jakarta/lsp4jakarta", //
      "https://github.com/eclipse-lsp4mp/lsp4mp", //
      "https://github.com/eclipse-lyo/lyo", //
      "https://github.com/eclipse-lyo/lyo.adapter-magicdraw", //
      "https://github.com/eclipse-lyo/lyo.adapter-simulink", //
      "https://github.com/eclipse-lyo/lyo.client", //
      "https://github.com/eclipse-lyo/lyo.core", //
      "https://github.com/eclipse-lyo/lyo.designer", //
      "https://github.com/eclipse-lyo/lyo.docs", //
      "https://github.com/eclipse-lyo/lyo.domains", //
      "https://github.com/eclipse-lyo/lyo.ldp", //
      "https://github.com/eclipse-lyo/lyo.rio", //
      "https://github.com/eclipse-lyo/lyo.server", //
      "https://github.com/eclipse-lyo/lyo.store", //
      "https://github.com/eclipse-lyo/lyo.testsuite", //
      "https://github.com/eclipse-lyo/lyo.trs-client", //
      "https://github.com/eclipse-lyo/lyo.trs-server", //
      "https://github.com/eclipse-lyo/lyo.validation", //
      "https://github.com/eclipse-m2e/m2e-core", //
      "https://github.com/eclipse-m2e/m2e-wtp", //
      "https://github.com/eclipse-mat/mat", //
      "https://github.com/eclipse-milo/milo", //
      "https://github.com/eclipse-modisco/org.eclipse.modisco", //
      "https://github.com/eclipse-mpc/epp.mpc", //
      "https://github.com/eclipse-muto/liveui-samples", //
      "https://github.com/eclipse-mwe/mwe", //
      "https://github.com/eclipse-mylyn/org.eclipse.mylyn", //
      "https://github.com/eclipse-mylyn/org.eclipse.mylyn.docs", //
      "https://github.com/eclipse-n4js/n4js", //
      "https://github.com/eclipse-nattable/nattable", //
      "https://github.com/eclipse-ocl/org.eclipse.ocl", //
      "https://github.com/eclipse-oomph/oomph", //
      "https://github.com/eclipse-openj9/openj9", //
      "https://github.com/eclipse-openj9/openj9-utils", //
      "https://github.com/eclipse-opensmartclide/kie-wb-common", //
      "https://github.com/eclipse-opensmartclide/kie-wb-distributions", //
      "https://github.com/eclipse-opensmartclide/smartclide-TD-Interest", //
      "https://github.com/eclipse-opensmartclide/smartclide-TD-Principal", //
      "https://github.com/eclipse-opensmartclide/smartclide-TD-Reusability-Index", //
      "https://github.com/eclipse-opensmartclide/smartclide-cicd", //
      "https://github.com/eclipse-opensmartclide/smartclide-context", //
      "https://github.com/eclipse-opensmartclide/smartclide-db-api", //
      "https://github.com/eclipse-opensmartclide/smartclide-deployment-interpreter-service", //
      "https://github.com/eclipse-opensmartclide/smartclide-security", //
      "https://github.com/eclipse-opensmartclide/smartclide-service-creation", //
      "https://github.com/eclipse-orbit/ebr", //
      "https://github.com/eclipse-orbit/orbit-legacy", //
      "https://github.com/eclipse-osgi-technology/jakartarest-osgi", //
      "https://github.com/eclipse-osgi-technology/osgi-test", //
      "https://github.com/eclipse-osgi-technology/osgi.enroute", //
      "https://github.com/eclipse-paho/paho.mqtt-spy", //
      "https://github.com/eclipse-paho/paho.mqtt.android", //
      "https://github.com/eclipse-paho/paho.mqtt.java", //
      "https://github.com/eclipse-pass/modeshape", //
      "https://github.com/eclipse-pass/pass-authz", //
      "https://github.com/eclipse-pass/pass-core", //
      "https://github.com/eclipse-pass/pass-deposit-services", //
      "https://github.com/eclipse-pass/pass-fcrepo-jsonld", //
      "https://github.com/eclipse-pass/pass-fcrepo-module-auth-rbacl", //
      "https://github.com/eclipse-pass/pass-grant-loader", //
      "https://github.com/eclipse-pass/pass-indexer", //
      "https://github.com/eclipse-pass/pass-java-client", //
      "https://github.com/eclipse-pass/pass-journal-loader", //
      "https://github.com/eclipse-pass/pass-nihms-loader", //
      "https://github.com/eclipse-pass/pass-notification-services", //
      "https://github.com/eclipse-pass/pass-package-providers", //
      "https://github.com/eclipse-pass/pass-support", //
      "https://github.com/eclipse-passage/chronograph", //
      "https://github.com/eclipse-passage/passage", //
      "https://github.com/eclipse-passage/passage-spring", //
      "https://github.com/eclipse-pde/eclipse.pde", //
      "https://github.com/eclipse-pdt/pdt", //
      "https://github.com/eclipse-platform/eclipse.platform", //
      "https://github.com/eclipse-platform/eclipse.platform.releng.aggregator", //
      "https://github.com/eclipse-platform/eclipse.platform.releng.buildtools", //
      "https://github.com/eclipse-platform/eclipse.platform.swt", //
      "https://github.com/eclipse-platform/eclipse.platform.ui", //
      "https://github.com/eclipse-poosl/poosl", //
      "https://github.com/eclipse-ptp/ptp.photran", //
      "https://github.com/eclipse-qvtd/org.eclipse.qvtd", //
      "https://github.com/eclipse-qvto/org.eclipse.qvto", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.chart", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.clientscripting", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.cnf", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.dropdown", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.e4", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.e4.compatibility.workbench", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.e4.compatibility.workbench-2", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.fileupload", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.gef", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.nebula-grid", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.pde", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.spreadsheet", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.tabbed-properties", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.themeeditor", //
      "https://github.com/eclipse-rap-incubator/org.eclipse.rap.incubator.visualization", //
      "https://github.com/eclipse-rap/org.eclipse.rap", //
      "https://github.com/eclipse-rap/org.eclipse.rap.tools", //
      "https://github.com/eclipse-rcptt/org.eclipse.rcptt", //
      "https://github.com/eclipse-rdf4j/rdf4j", //
      "https://github.com/eclipse-rdf4j/rdf4j-doc", //
      "https://github.com/eclipse-rdf4j/rdf4j-storage", //
      "https://github.com/eclipse-rdf4j/rdf4j-tools", //
      "https://github.com/eclipse-repairnator/repairnator", //
      "https://github.com/eclipse-rmf/org.eclipse.rmf", //
      "https://github.com/eclipse-scout/scout.docs", //
      "https://github.com/eclipse-scout/scout.rt", //
      "https://github.com/eclipse-scout/scout.sdk", //
      "https://github.com/eclipse-sensinact/org.eclipse.sensinact.gateway", //
      "https://github.com/eclipse-sensinact/org.eclipse.sensinact.studio", //
      "https://github.com/eclipse-serializer/serializer", //
      "https://github.com/eclipse-servertools/servertools", //
      "https://github.com/eclipse-set/browser", //
      "https://github.com/eclipse-set/model", //
      "https://github.com/eclipse-set/set", //
      "https://github.com/eclipse-set/toolboxmodel", //
      "https://github.com/eclipse-sirius/sirius-desktop", //
      "https://github.com/eclipse-sirius/sirius-emf-json", //
      "https://github.com/eclipse-sirius/sirius-legacy", //
      "https://github.com/eclipse-sirius/sirius-web", //
      "https://github.com/eclipse-sisu/sisu-project", //
      "https://github.com/eclipse-sisu/sisu.plexus", //
      "https://github.com/eclipse-slm/awx-jwt-authenticator", //
      "https://github.com/eclipse-slm/slm", //
      "https://github.com/eclipse-sourceediting/sourceediting", //
      "https://github.com/eclipse-sparkplug/sparkplug", //
      "https://github.com/eclipse-sprotty/sprotty-server", //
      "https://github.com/eclipse-store/bookstore-demo", //
      "https://github.com/eclipse-store/store", //
      "https://github.com/eclipse-sumo/sumo", //
      "https://github.com/eclipse-sw360/sw360", //
      "https://github.com/eclipse-swtbot/org.eclipse.swtbot", //
      "https://github.com/eclipse-syson/syson", //
      "https://github.com/eclipse-tahu/tahu", //
      "https://github.com/eclipse-tea/tea", //
      "https://github.com/eclipse-texlipse/texlipse", //
      "https://github.com/eclipse-theia/theia-cloud", //
      "https://github.com/eclipse-tm4e/tm4e", //
      "https://github.com/eclipse-tracecompass/org.eclipse.tracecompass", //
      "https://github.com/eclipse-tracecompass/trace-event-logger", //
      "https://github.com/eclipse-tractusx/SSI-agent-lib", //
      "https://github.com/eclipse-tractusx/bpn-did-resolution-service", //
      "https://github.com/eclipse-tractusx/daps-registration-service", //
      "https://github.com/eclipse-tractusx/data-exchange-test-service", //
      "https://github.com/eclipse-tractusx/digital-product-pass", //
      "https://github.com/eclipse-tractusx/item-relationship-service", //
      "https://github.com/eclipse-tractusx/managed-identity-wallet", //
      "https://github.com/eclipse-tractusx/managed-service-orchestrator", //
      "https://github.com/eclipse-tractusx/managed-simple-data-exchanger-backend", //
      "https://github.com/eclipse-tractusx/puris-backend", //
      "https://github.com/eclipse-tractusx/sd-factory", //
      "https://github.com/eclipse-tractusx/sldt-bpn-discovery", //
      "https://github.com/eclipse-tractusx/sldt-digital-twin-registry", //
      "https://github.com/eclipse-tractusx/sldt-discovery-finder", //
      "https://github.com/eclipse-tractusx/sldt-semantic-hub", //
      "https://github.com/eclipse-tractusx/traceability-foss", //
      "https://github.com/eclipse-tractusx/traceability-foss-backend", //
      "https://github.com/eclipse-tractusx/tractusx-edc", //
      "https://github.com/eclipse-tractusx/tractusx-edc-compatibility-tests", //
      "https://github.com/eclipse-tractusx/vas-country-risk-backend", //
      "https://github.com/eclipse-transformer/transformer", //
      "https://github.com/eclipse-tycho/tycho", //
      "https://github.com/eclipse-uml2/uml2", //
      "https://github.com/eclipse-uomo/uomo", //
      "https://github.com/eclipse-uprotocol/up-android-core", //
      "https://github.com/eclipse-uprotocol/up-android-discovery", //
      "https://github.com/eclipse-uprotocol/up-java", //
      "https://github.com/eclipse-uprotocol/up-simulator-proxy", //
      "https://github.com/eclipse-uprotocol/up-transport-android-java", //
      "https://github.com/eclipse-vertx/vert.x", //
      "https://github.com/eclipse-vertx/vertx-auth", //
      "https://github.com/eclipse-vertx/vertx-codegen", //
      "https://github.com/eclipse-vertx/vertx-grpc", //
      "https://github.com/eclipse-vertx/vertx-http-proxy", //
      "https://github.com/eclipse-vertx/vertx-json-schema", //
      "https://github.com/eclipse-vertx/vertx-junit5", //
      "https://github.com/eclipse-vertx/vertx-openapi", //
      "https://github.com/eclipse-vertx/vertx-rabbitmq-client", //
      "https://github.com/eclipse-vertx/vertx-service-resolver", //
      "https://github.com/eclipse-vertx/vertx-sql-client", //
      "https://github.com/eclipse-vertx/vertx-tracing", //
      "https://github.com/eclipse-vertx/vertx-uri-template", //
      "https://github.com/eclipse-viatra/org.eclipse.viatra", //
      "https://github.com/eclipse-viatra/org.eclipse.viatra.examples", //
      "https://github.com/eclipse-viatra/org.eclipse.viatra.modelobfuscator", //
      "https://github.com/eclipse-virgo/virgo-bundlor", //
      "https://github.com/eclipse-virgo/virgo-ide", //
      "https://github.com/eclipse-virgo/virgo-root", //
      "https://github.com/eclipse-vorto/vorto", //
      "https://github.com/eclipse-vorto/vorto-examples", //
      "https://github.com/eclipse-webservices/webservices", //
      "https://github.com/eclipse-wildwebdeveloper/wildwebdeveloper", //
      "https://github.com/eclipse-windowbuilder/windowbuilder", //
      "https://github.com/eclipse-wtp-common/webtools.common", //
      "https://github.com/eclipse-xpanse/terraform-boot", //
      "https://github.com/eclipse-xpanse/tofu-maker", //
      "https://github.com/eclipse-xpanse/xpanse", //
      "https://github.com/eclipse-xsemantics/xsemantics", //
      "https://github.com/eclipse-xtext/xtext", //
      "https://github.com/eclipse/Xpect", //
      "https://github.com/eclipse/amlen", //
      "https://github.com/eclipse/cft", //
      "https://github.com/eclipse/chemclipse", //
      "https://github.com/eclipse/dartboard", //
      "https://github.com/eclipse/dawnsci", //
      "https://github.com/eclipse/dirigible", //
      "https://github.com/eclipse/eavp", //
      "https://github.com/eclipse/ecf", //
      "https://github.com/eclipse/elk", //
      "https://github.com/eclipse/emf.egf", //
      "https://github.com/eclipse/imagen", //
      "https://github.com/eclipse/jbom", //
      "https://github.com/eclipse/jnosql", //
      "https://github.com/eclipse/jnosql-communication-driver", //
      "https://github.com/eclipse/jnosql-databases", //
      "https://github.com/eclipse/jnosql-extensions", //
      "https://github.com/eclipse/kapua", //
      "https://github.com/eclipse/lsp4jakarta", //
      "https://github.com/eclipse/microprofile-conference", //
      "https://github.com/eclipse/microprofile-config", //
      "https://github.com/eclipse/microprofile-context-propagation", //
      "https://github.com/eclipse/microprofile-fault-tolerance", //
      "https://github.com/eclipse/microprofile-graphql", //
      "https://github.com/eclipse/microprofile-health", //
      "https://github.com/eclipse/microprofile-jwt-auth", //
      "https://github.com/eclipse/microprofile-jwt-bridge", //
      "https://github.com/eclipse/microprofile-lra", //
      "https://github.com/eclipse/microprofile-metrics", //
      "https://github.com/eclipse/microprofile-open-api", //
      "https://github.com/eclipse/microprofile-reactive-messaging", //
      "https://github.com/eclipse/microprofile-reactive-streams-operators", //
      "https://github.com/eclipse/microprofile-rest-client", //
      "https://github.com/eclipse/microprofile-samples", //
      "https://github.com/eclipse/microprofile-sandbox", //
      "https://github.com/eclipse/microprofile-service-mesh-service-a", //
      "https://github.com/eclipse/microprofile-service-mesh-service-b", //
      "https://github.com/eclipse/microprofile-starter", //
      "https://github.com/eclipse/microprofile-telemetry", //
      "https://github.com/eclipse/mita", //
      "https://github.com/eclipse/mosaic", //
      "https://github.com/eclipse/oneofour", //
      "https://github.com/eclipse/openvsx", //
      "https://github.com/eclipse/org.eclipse.rap.incubator.e4.compatibility.workbench", //
      "https://github.com/eclipse/packager", //
      "https://github.com/eclipse/poosl", //
      "https://github.com/eclipse/richbeans", //
      "https://github.com/eclipse/scanning", //
      "https://github.com/eclipse/smartmdsd", //
      "https://github.com/eclipse/steady", //
      "https://github.com/eclipse/swtchart", //
      "https://github.com/eclipse/tahu", //
      "https://github.com/eclipse/xsemantics", //
      "https://github.com/jakartaee/authentication", //
      "https://github.com/jakartaee/authorization", //
      "https://github.com/jakartaee/batch", //
      "https://github.com/jakartaee/cdi", //
      "https://github.com/jakartaee/common-annotations-api", //
      "https://github.com/jakartaee/concurrency", //
      "https://github.com/jakartaee/config", //
      "https://github.com/jakartaee/connectors", //
      "https://github.com/jakartaee/data", //
      "https://github.com/jakartaee/enterprise-beans", //
      "https://github.com/jakartaee/expression-language", //
      "https://github.com/jakartaee/faces", //
      "https://github.com/jakartaee/jaf-api", //
      "https://github.com/jakartaee/jax-ws-api", //
      "https://github.com/jakartaee/jaxb-api", //
      "https://github.com/jakartaee/jsonb-api", //
      "https://github.com/jakartaee/jsonp-api", //
      "https://github.com/jakartaee/jws-api", //
      "https://github.com/jakartaee/mail-api", //
      "https://github.com/jakartaee/messaging", //
      "https://github.com/jakartaee/messaging-proposals", //
      "https://github.com/jakartaee/mvc", //
      "https://github.com/jakartaee/nosql", //
      "https://github.com/jakartaee/pages", //
      "https://github.com/jakartaee/persistence", //
      "https://github.com/jakartaee/rest", //
      "https://github.com/jakartaee/rpc", //
      "https://github.com/jakartaee/saaj-api", //
      "https://github.com/jakartaee/security", //
      "https://github.com/jakartaee/servlet", //
      "https://github.com/jakartaee/tags", //
      "https://github.com/jakartaee/transactions", //
      "https://github.com/jakartaee/validation", //
      "https://github.com/jakartaee/validation-spec", //
      "https://github.com/jakartaee/websocket", //
      "https://github.com/jetty/jetty-build-support", //
      "https://github.com/jetty/jetty-test-helper", //
      "https://github.com/jetty/jetty-toolchain", //
      "https://github.com/jetty/jetty.project", //
      "https://github.com/locationtech/geogig", //
      "https://github.com/locationtech/geomesa", //
      "https://github.com/locationtech/geowave", //
      "https://github.com/locationtech/jts", //
      "https://github.com/locationtech/proj4j", //
      "https://github.com/locationtech/spatial4j", //
      "https://github.com/locationtech/udig-platform", //
      "https://github.com/microprofile/microprofile-conference", //
      "https://github.com/microprofile/microprofile-config", //
      "https://github.com/microprofile/microprofile-context-propagation", //
      "https://github.com/microprofile/microprofile-fault-tolerance", //
      "https://github.com/microprofile/microprofile-graphql", //
      "https://github.com/microprofile/microprofile-health", //
      "https://github.com/microprofile/microprofile-jwt-auth", //
      "https://github.com/microprofile/microprofile-jwt-bridge", //
      "https://github.com/microprofile/microprofile-lra", //
      "https://github.com/microprofile/microprofile-metrics", //
      "https://github.com/microprofile/microprofile-open-api", //
      "https://github.com/microprofile/microprofile-opentracing", //
      "https://github.com/microprofile/microprofile-reactive-messaging", //
      "https://github.com/microprofile/microprofile-reactive-streams-operators", //
      "https://github.com/microprofile/microprofile-rest-client", //
      "https://github.com/microprofile/microprofile-samples", //
      "https://github.com/microprofile/microprofile-sandbox", //
      "https://github.com/microprofile/microprofile-service-mesh-service-a", //
      "https://github.com/microprofile/microprofile-service-mesh-service-b", //
      "https://github.com/microprofile/microprofile-site", //
      "https://github.com/microprofile/microprofile-starter", //
      "https://github.com/microprofile/microprofile-telemetry", //
      "https://github.com/openhwgroup/core-v-ide-cdt", //
      "https://github.com/openhwgroup/core-v-sdk", //
      "https://github.com/osgi/osgi", //
      "https://github.com/polarsys/ng661designer", //
      "https://github.com/polarsys/time4sys", //
      "https://github.com/winery/BPMN4TOSCA2BPEL", //
      "https://github.com/winery/winery", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.addon.migration", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.addon.transformation", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.cloud", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.rtclib", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.tools", //
      "https://gitlab.eclipse.org/eclipse/bpmn2-modeler/org.eclipse.bpmn2-modeler", //
      "https://gitlab.eclipse.org/eclipse/bpmn2/org.eclipse.bpmn2", //
      "https://gitlab.eclipse.org/eclipse/chess/chess", //
      "https://gitlab.eclipse.org/eclipse/comma/comma", //
      "https://gitlab.eclipse.org/eclipse/dco/developer-console", //
      "https://gitlab.eclipse.org/eclipse/ease/ease", //
      "https://gitlab.eclipse.org/eclipse/escet/escet", //
      "https://gitlab.eclipse.org/eclipse/esf/esf-infra", //
      "https://gitlab.eclipse.org/eclipse/esf/esf-tools", //
      "https://gitlab.eclipse.org/eclipse/etrice/etrice", //
      "https://gitlab.eclipse.org/eclipse/gendoc/org.eclipse.gendoc", //
      "https://gitlab.eclipse.org/eclipse/graphene/common-dataservice", //
      "https://gitlab.eclipse.org/eclipse/graphene/design-studio-backend", //
      "https://gitlab.eclipse.org/eclipse/graphene/federation", //
      "https://gitlab.eclipse.org/eclipse/graphene/kubernetes-client", //
      "https://gitlab.eclipse.org/eclipse/graphene/onboarding", //
      "https://gitlab.eclipse.org/eclipse/graphene/playground-deployer", //
      "https://gitlab.eclipse.org/eclipse/graphene/portal-marketplace", //
      "https://gitlab.eclipse.org/eclipse/graphiti/graphiti", //
      "https://gitlab.eclipse.org/eclipse/hawk/hawk", //
      "https://gitlab.eclipse.org/eclipse/ice/ice", //
      "https://gitlab.eclipse.org/eclipse/ldt/ldt", //
      "https://gitlab.eclipse.org/eclipse/lsat/lsat", //
      "https://gitlab.eclipse.org/eclipse/mdht/modeling.mdht", //
      "https://gitlab.eclipse.org/eclipse/mdmbl/org.eclipse.mdm", //
      "https://gitlab.eclipse.org/eclipse/mdmbl/org.eclipse.mdm.mdfsorter", //
      "https://gitlab.eclipse.org/eclipse/mdmbl/org.eclipse.mdm.openatfx.mdf", //
      "https://gitlab.eclipse.org/eclipse/objectteams/objectteams", //
      "https://gitlab.eclipse.org/eclipse/om2m/om2m", //
      "https://gitlab.eclipse.org/eclipse/opencert/opencert", //
      "https://gitlab.eclipse.org/eclipse/openk-coremodules/org.eclipse.openk-coremodules.authandauth", //
      "https://gitlab.eclipse.org/eclipse/openk-coremodules/org.eclipse.openk-coremodules.contactBaseData.backend", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.elogbook", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.gridFailureInformation.backend", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.mics.centralService", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.mics.homeService", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.plannedGridMeasures.backend", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.standbyPlanning.backend", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.statementPublicAffairs.backend", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.authentication", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.authentication.ui", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.blob", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.bpm", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.bpm.api", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.bpmn2.ecore", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.dsl", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.ecview.addons", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.ecview.core", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.ecview.extension", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.ecview.extension.api", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.fork.mihalis.opal.imageSelector.osgi", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-bpmn", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-cdo", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-classic", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-designer", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-desktop", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-domainservices", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-ease", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-manufacturing", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-marte", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-model2doc", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-moka", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-moka.incubation", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-papygame", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-robotics", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-sirius", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-sysml16", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-web", //
      "https://gitlab.eclipse.org/eclipse/refactor/org.eclipse.emf.refactor.examples", //
      "https://gitlab.eclipse.org/eclipse/refactor/org.eclipse.emf.refactor.metrics", //
      "https://gitlab.eclipse.org/eclipse/refactor/org.eclipse.emf.refactor.refactoring", //
      "https://gitlab.eclipse.org/eclipse/refactor/org.eclipse.emf.refactor.smells", //
      "https://gitlab.eclipse.org/eclipse/sphinx/org.eclipse.sphinx", //
      "https://gitlab.eclipse.org/eclipse/statet/statet", //
      "https://gitlab.eclipse.org/eclipse/subversive/subversive", //
      "https://gitlab.eclipse.org/eclipse/tcf/tcf", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/eclipse-api-for-java", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/eclipse-project-code", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.EclipsePlug-ins", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.core", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.language-server", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.misc", //
      "https://gitlab.eclipse.org/eclipse/tm/org.eclipse.tm", //
      "https://gitlab.eclipse.org/eclipse/trace4cps/trace4cps", //
      "https://gitlab.eclipse.org/eclipse/usssdk/org.eclipse.usssdk", //
      "https://gitlab.eclipse.org/eclipse/webtools/releng/webtools-releng", //
      "https://gitlab.eclipse.org/eclipse/xpand/org.eclipse.xpand", //
      "https://gitlab.eclipse.org/eclipse/xwt/org.eclipse.xwt"
  // generated-repositories
  ));

  private final Map<String, Map<String, Map<String, Map<String, Set<String>>>>> repositoryIndices = new TreeMap<>();

  private ContentHandler contentHandler;

  private File target;

  private final Set<String> almostEmptyRepositories = new TreeSet<>();

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
    var testNew = "true".equals(System.getProperty("org.eclipse.oomph.setup.git.util.GitIndexApplication.test.new"));
    var repositories = getRepositories();
    var count = 0;
    var size = repositories.size();
    var brokenRepositories = new TreeSet<String>();
    var newRepositories = new TreeSet<String>();
    for (var repo : repositories)
    {
      System.out.println("----------------" + ++count + " of " + size + " -------------------------");
      try
      {
        index(repo);
        if (!almostEmptyRepositories.contains(repo))
        {
          newRepositories.add(repo);
        }
      }
      catch (Exception ex)
      {
        if (!testNew)
        {
          throw ex;
        }
      }
    }

    if ("true".equals(System.getProperty("org.eclipse.oomph.setup.git.util.GitIndexApplication.test.new")))
    {
      var update = System.getProperty("org.eclipse.oomph.setup.git.util.GitIndexApplication.update");
      if (update != null)
      {
        var self = Path.of(update);
        var content = Files.readString(self);

        // NON_JAVA_REPOSITORIES
        {
          var matcher = NON_JAVA_REPOSITORIES_SECTION.matcher(content);
          if (matcher.find())
          {
            var updatedContent = new StringBuilder();
            var literals = new ArrayList<String>();
            almostEmptyRepositories.addAll(NON_JAVA_REPOSITORIES);
            almostEmptyRepositories.remove("");
            for (var repository : almostEmptyRepositories)
            {
              literals.add("      \"" + Matcher.quoteReplacement(repository) + "\"");
            }

            matcher.appendReplacement(updatedContent, "$1" + String.join(", //" + matcher.group(2), literals) + ", //$2$3");
            matcher.appendTail(updatedContent);
            content = updatedContent.toString();
          }
        }

        // BROKEN_REPOSITORIES
        {
          var matcher = BROKEN_REPOSITORIES_SECTION.matcher(content);
          if (matcher.find())
          {
            var updatedContent = new StringBuilder();
            var literals = new ArrayList<String>();
            brokenRepositories.addAll(BROKEN_REPOSITORIES);
            brokenRepositories.remove("");
            for (var repository : brokenRepositories)
            {
              literals.add("      \"" + Matcher.quoteReplacement(repository) + "\"");
            }

            matcher.appendReplacement(updatedContent, "$1" + String.join(", //" + matcher.group(2), literals) + ", //$2$3");
            matcher.appendTail(updatedContent);
            content = updatedContent.toString();
          }
        }

        // REPOSITORIES
        {
          newRepositories.addAll(REPOSITORIES);
          newRepositories.remove("");
          var updatedContent = updateRepositories(content, newRepositories);
          if (updatedContent != null)
          {
            content = updatedContent;
          }
        }

        Files.writeString(self, content);
      }
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
    for (var i = 1; i < 50; ++i)
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
      if (!id.contains("."))
      {
        return true;
      }

      if (id.startsWith("oniro") //
          || id.startsWith("eclipse.e4") //
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

    if ("true".equals(System.getProperty("org.eclipse.oomph.setup.git.util.GitIndexApplication.test.new")))
    {
      repositories.removeAll(REPOSITORIES);
      repositories.removeAll(BROKEN_REPOSITORIES);
      repositories.removeAll(NON_JAVA_REPOSITORIES);
    }
    else
    {
      var update = System.getProperty("org.eclipse.oomph.setup.git.util.GitIndexApplication.update");
      if (update != null)
      {
        var self = Path.of(update);
        var content = Files.readString(self);
        var updatedContent = updateRepositories(content, repositories);
        if (updatedContent != null)
        {
          Files.writeString(self, updatedContent);
        }
      }
    }

    return repositories;
  }

  private static String updateRepositories(String content, Set<String> repositories)
  {
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
      return updatedContent.toString();
    }

    return null;
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
        if (LOG_PATTERN.matcher(line).find())
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
                  var packagePrefix = matcher.group(2);
                  if (PACKAGE_PREFIXES.contains(packagePrefix))
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

    if (javaCount.get() < 5)
    {
      almostEmptyRepositories.add(repo);
    }

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
          if (org.startsWith("eclipse/"))
          {
            System.err.println(org);
            org = org.replace('/', '-');
          }

          var repos = contentHandler.getContent("https://api.github.com/orgs/" + org + "/repos?page=" + i);
          var urls = getValues("html_url", repos);
          urls.remove("https://github.com/" + org);
          if (urls.isEmpty())
          {
            break;
          }

          addAll(result, urls);
        }
      }
    }

    var githubRepos = getSection("github_repos", content);
    if (githubRepos != null)
    {
      addAll(result, getValues("url", githubRepos));
    }

    cleanupRepos(result);

    if (result.isEmpty())
    {
      var gerritRepos = getSection("gerrit_repos", content);
      if (gerritRepos != null)
      {
        addAll(result, getValues("url", gerritRepos));
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
          var repos = contentHandler.getContent("https://gitlab.eclipse.org/api/v4/groups/" + group.replace("\\", "").replace("/", "%2f") + "?page=" + i);
          var urls = getValues("http_url_to_repo", repos);
          if (!addAll(result, urls.stream().map(it -> it.replaceAll("\\.git$", "")).collect(Collectors.toSet())))
          {
            break;
          }
        }
      }
    }

    var gitlabRepos = getSection("gitlab_repos", content);
    if (githubRepos != null)
    {
      addAll(result, getValues("url", gitlabRepos).stream().map(it -> it.replaceAll("\\.git$", "")).collect(Collectors.toSet()));
      cleanupRepos(result);
    }

    return result;
  }

  private boolean addAll(Set<String> result, Set<String> values)
  {
    return result.addAll(values.stream().map(repo -> repo.replace("\\", "")).collect(Collectors.toSet()));
  }

  private void cleanupRepos(Set<String> repos)
  {
    repos.removeIf(it -> {
      if ((it.startsWith("https://github.com/eclipse-ee4j") || it.startsWith("https://github.com/jakartae"))
          && (it.contains("tck") || it.contains("-doc-") || it.contains("examples") || it.contains("samples")))
      {
        return true;
      }

      if (it.matches("https://github.com/adoptium/.+jdk.+") || it.matches("https://github.com/adoptium/jdk.+"))
      {
        return true;
      }

      if (it.matches(
          "https://gitlab.eclipse.org/eclipse/titan/titan.(ProtocolModules|ProtocolEmulations|TestPorts|Libraries|ApplicationLibraries|Applications|Servers|ObjectOriented).*"))
      {
        return true;
      }

      if ((it.startsWith("https://github.com/eclipse-ee4j") || it.startsWith("https://github.com/jakartae"))
          && (it.contains("tck") || it.contains("-doc-") || it.contains("examples") || it.contains("samples")))
      {
        return true;
      }

      if (BROKEN_REPOSITORIES.contains(it) || NON_JAVA_REPOSITORIES.contains(it))
      {
        return true;
      }

      if (it.contains("/org.eclipse.mylyn.") && !it.endsWith("/org.eclipse.mylyn.docs"))
      {
        return true;
      }

      if (it.matches("https://github.com/[^/]+"))
      {
        // Remove if it's an organization.
        return true;
      }

      return it.endsWith("/.github") || it.endsWith("/ui-best-practices") || it.endsWith("/.eclipsefdn") || it.contains("www.eclipse.org")
          || it.endsWith(".incubator") || it.contains("website") || it.endsWith(".github.io") || it.endsWith("binaries") || it.contains("-ghsa-")
          || it.startsWith("https://gitlab.eclipse.org/eclipse/xfsc/");
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
    private final Path cache;

    private final HttpClient httpClient;

    private final String pat;

    public ContentHandler(String cache) throws StorageException
    {
      String githubPATPreference = System.getProperty("org.eclipse.oomph.setup.git.util.GitIndexApplication.github.pat.preference");
      if (githubPATPreference != null)
      {
        int slash = githubPATPreference.lastIndexOf('/');
        ISecurePreferences node = SecurePreferencesFactory.getDefault().node(githubPATPreference.substring(0, slash));
        pat = node.get(githubPATPreference.substring(slash + 1), null);
      }
      else
      {
        pat = null;
      }

      httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).cookieHandler(new CookieManager()).build();
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
      var requestBuilder = HttpRequest.newBuilder(uri).GET();

      if (pat != null && "api.github.com".equals(uri.getHost()))
      {
        requestBuilder.header("Authorization", "Bearer " + pat);
        requestBuilder.header("X-GitHub-Api-Version", "2022-11-28");
      }

      try
      {
        var request = requestBuilder.build();
        var response = httpClient.send(request, BodyHandlers.ofString());
        var statusCode = response.statusCode();
        if (statusCode != 200)
        {
          throw new IOException("status code " + statusCode + " -> " + uri);
        }

        return response.body();
      }
      catch (IOException ex)
      {
        throw new IOException(ex.getMessage() + " -> " + uri, ex);
      }
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
