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

  static
  {
    for (String code : Locale.getISOCountries())
    {
      PACKAGE_PREFIXES.add(code.toLowerCase());
    }

    PACKAGE_PREFIXES.addAll(Set.of("com", "org", "net", "edu", "gov", "mil", "io", "java", "javax", "jakarta"));
  }

  private static Set<String> TEST_REPOSITORIES = new TreeSet<>(Set.of( //
      "https://git.eclipse.org/r/jgit/jgit", //
      "https://github.com/eclipse-emf/org.eclipse.emf", //
      "https://gitlab.eclipse.org/eclipse/xpand/org.eclipse.xpand" //
  ));

  private static Set<String> BROKEN_REPOSITORIES = new TreeSet<>(Set.of( //
      "https://git.eclipse.org/r/sequoyah/org.eclipse.sequoyah", //
      "https://github.com/eclipse/org.eclipse.scout.docs", //
      "https://git.eclipse.org/r/webtools/org.eclipse.webtools.java-ee-config", //
      "https://gitlab.eclipse.org/eclipse/sw360/playground.git", //
      "https://github.com/jakartaee/workshop-trainer", //
      "https://github.com/eclipse-tractusx/portal-assets", // Has file name with ':'
      "https://github.com/eclipse-arrowhead/profile-library-sysml", // Has file name with ':'
      "https://gitlab.eclipse.org/eclipse/openpass/simopenpass", //
      "https://github.com/eclipse-esmf/esmf-documentation", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.applications.iot-functiontest-framework", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.applications.iot.loadtest.framework", //
      "" //
  ));

  private static Set<String> NON_JAVA_REPOSITORIES = new TreeSet<>(Set.of( //
      "https://git.eclipse.org/r/4diac/org.eclipse.4diac.examples", //
      "https://git.eclipse.org/r/4diac/org.eclipse.4diac.forte", //
      "https://git.eclipse.org/r/duttile/duttile", //
      "https://git.eclipse.org/r/efm/org.eclipse.efm-hibou", //
      "https://git.eclipse.org/r/efm/org.eclipse.efm-symbex", //
      "https://git.eclipse.org/r/esf/test-esf", //
      "https://git.eclipse.org/r/jsf/webtools.jsf.docs", //
      "https://git.eclipse.org/r/ldt/org.eclipse.metalua", //
      "https://git.eclipse.org/r/mdmbl/org.eclipse.mdm.api.uml", //
      "https://git.eclipse.org/r/mdmbl/org.eclipse.mdmbl", //
      "https://git.eclipse.org/r/paho.incubator/org.eclipse.paho.mqtt.lua", //
      "https://git.eclipse.org/r/paho.incubator/smidge", //
      "https://git.eclipse.org/r/rtsc/org.eclipse.rtsc.contrib", //
      "https://git.eclipse.org/r/rtsc/org.eclipse.rtsc.test", //
      "https://git.eclipse.org/r/rtsc/org.eclipse.rtsc.training", //
      "https://git.eclipse.org/r/servertools/webtools.servertools.devsupport", //
      "https://git.eclipse.org/r/servertools/webtools.servertools.docs", //
      "https://git.eclipse.org/r/statet/org.eclipse.statet", //
      "https://git.eclipse.org/r/tcf/org.eclipse.tcf.agent", //
      "https://git.eclipse.org/r/webtools/webtools.maps", //
      "https://git.eclipse.org/r/webtools/webtools.releng.aggregator", //
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
      "https://github.com/adoptium/jenkins-helper", //
      "https://github.com/adoptium/jmc-build", //
      "https://github.com/adoptium/marketplace-data", //
      "https://github.com/adoptium/mirror-scripts", //
      "https://github.com/adoptium/run-aqa", //
      "https://github.com/adoptium/temurin", //
      "https://github.com/adoptium/temurin-cpe-generator", //
      "https://github.com/deeplearning4j/deeplearning4j-docs", //
      "https://github.com/eclipse-4diac/4diac-examples", //
      "https://github.com/eclipse-4diac/4diac-fbe", //
      "https://github.com/eclipse-4diac/4diac-fortebuildcontainer", //
      "https://github.com/eclipse-4diac/4diac-toolchain", //
      "https://github.com/eclipse-aaspe/aaspe", //
      "https://github.com/eclipse-aaswc/aaswc", //
      "https://github.com/eclipse-actf/org.eclipse.actf.visualization.releng", //
      "https://github.com/eclipse-agail/agail-security", //
      "https://github.com/eclipse-agail/agile-api-spec", //
      "https://github.com/eclipse-agail/agile-cli", //
      "https://github.com/eclipse-agail/agile-data", //
      "https://github.com/eclipse-agail/agile-dbus", //
      "https://github.com/eclipse-agail/agile-kura", //
      "https://github.com/eclipse-agail/agile-sdk", //
      "https://github.com/eclipse-ankaios/ankaios", //
      "https://github.com/eclipse-arche/arche", //
      "https://github.com/eclipse-arrowhead/core-c", //
      "https://github.com/eclipse-arrowhead/core-go-generic", //
      "https://github.com/eclipse-arrowhead/core-java-spring", //
      "https://github.com/eclipse-arrowhead/documentation", //
      "https://github.com/eclipse-arrowhead/roadmap", //
      "https://github.com/eclipse-babel/server", //
      "https://github.com/eclipse-babel/translations", //
      "https://github.com/eclipse-basyx/basyx-applications", //
      "https://github.com/eclipse-basyx/basyx-cpp-components", //
      "https://github.com/eclipse-basyx/basyx-cpp-sdk", //
      "https://github.com/eclipse-basyx/basyx-dotnet", //
      "https://github.com/eclipse-basyx/basyx-dotnet-applications", //
      "https://github.com/eclipse-basyx/basyx-dotnet-components", //
      "https://github.com/eclipse-basyx/basyx-dotnet-examples", //
      "https://github.com/eclipse-basyx/basyx-dotnet-sdk", //
      "https://github.com/eclipse-basyx/basyx-python-sdk", //
      "https://github.com/eclipse-basyx/basyx-rust-sdk", //
      "https://github.com/eclipse-bfb/bfb-client", //
      "https://github.com/eclipse-bfb/bfb-specs", //
      "https://github.com/eclipse-bfb/bfb-tooling", //
      "https://github.com/eclipse-bluechi/bluechi", //
      "https://github.com/eclipse-bluechi/hashmap.c", //
      "https://github.com/eclipse-bluechi/terraform-provider-bluechi", //
      "https://github.com/eclipse-cbi/ansible-playbooks", //
      "https://github.com/eclipse-cbi/best-practices", //
      "https://github.com/eclipse-cbi/buildkitd-okd", //
      "https://github.com/eclipse-cbi/ci-admin", //
      "https://github.com/eclipse-cbi/dockerfiles", //
      "https://github.com/eclipse-cbi/dockertools", //
      "https://github.com/eclipse-cbi/epl-license-feature", //
      "https://github.com/eclipse-cbi/jiro", //
      "https://github.com/eclipse-cbi/jiro-agents", //
      "https://github.com/eclipse-cbi/jiro-dashboard", //
      "https://github.com/eclipse-cbi/jiro-masters", //
      "https://github.com/eclipse-cbi/jiro-static-agents", //
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
      "https://github.com/eclipse-cdt-cloud/tsp-python-client", //
      "https://github.com/eclipse-cdt-cloud/tsp-typescript-client", //
      "https://github.com/eclipse-cdt-cloud/vscode-memory-inspector", //
      "https://github.com/eclipse-cdt-cloud/vscode-svd-viewer", //
      "https://github.com/eclipse-cdt-cloud/vscode-trace-extension", //
      "https://github.com/eclipse-cdt-cloud/vscode-trace-server", //
      "https://github.com/eclipse-cdt-cloud/vscode-websocket-adapter", //
      "https://github.com/eclipse-cdt/cdt-infra", //
      "https://github.com/eclipse-cdt/cdt-vscode", //
      "https://github.com/eclipse-chariott/Agemo", //
      "https://github.com/eclipse-chariott/chariott", //
      "https://github.com/eclipse-chariott/chariott-example-applications", //
      "https://github.com/eclipse-che/blog", //
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
      "https://github.com/eclipse-cyclonedds/cyclonedds", //
      "https://github.com/eclipse-cyclonedds/cyclonedds-cxx", //
      "https://github.com/eclipse-cyclonedds/cyclonedds-python", //
      "https://github.com/eclipse-ditto/ditto-clients-golang", //
      "https://github.com/eclipse-ditto/ditto-clients-python", //
      "https://github.com/eclipse-ecal/ecal", //
      "https://github.com/eclipse-ecal/ecal-camera-samples", //
      "https://github.com/eclipse-ecal/ecal-core", //
      "https://github.com/eclipse-ecal/ecal-foxglove-bridge", //
      "https://github.com/eclipse-ecal/ecal-gpsd-client", //
      "https://github.com/eclipse-ecal/ecal-matlab-binding", //
      "https://github.com/eclipse-ecal/ecal-mcap-tools", //
      "https://github.com/eclipse-ecal/ecal-mqtt-bridge", //
      "https://github.com/eclipse-ecal/ecal-utils", //
      "https://github.com/eclipse-ecal/fineftp-server", //
      "https://github.com/eclipse-ecal/rmw_ecal", //
      "https://github.com/eclipse-ecal/rosidl_typesupport_protobuf", //
      "https://github.com/eclipse-ecal/tcp_pubsub", //
      "https://github.com/eclipse-ecal/udpcap", //
      "https://github.com/eclipse-edc/Collateral", //
      "https://github.com/eclipse-edc/DataDashboard", //
      "https://github.com/eclipse-edc/IDS-CodeGeneration", //
      "https://github.com/eclipse-edc/JenkinsPipelines", //
      "https://github.com/eclipse-edc/Publications", //
      "https://github.com/eclipse-edc/Release", //
      "https://github.com/eclipse-edc/Template-Basic", //
      "https://github.com/eclipse-edc/docs", //
      "https://github.com/eclipse-edc/json-ld-context", //
      "https://github.com/eclipse-ee4j/ee4j", //
      "https://github.com/eclipse-ee4j/glassfish-docs", //
      "https://github.com/eclipse-ee4j/glassfish-repackaged", //
      "https://github.com/eclipse-ee4j/gransasso", //
      "https://github.com/eclipse-ee4j/jakartaee-firstcup", //
      "https://github.com/eclipse-ee4j/jakartaee-release", //
      "https://github.com/eclipse-ee4j/jakartaee-renames", //
      "https://github.com/eclipse-ee4j/jakartaee-tutorial", //
      "https://github.com/eclipse-ee4j/jersey-web", //
      "https://github.com/eclipse-egit/egit-permissions", //
      "https://github.com/eclipse-egit/egit-pipelines", //
      "https://github.com/eclipse-embed-cdt/Liqp", //
      "https://github.com/eclipse-embed-cdt/assets", //
      "https://github.com/eclipse-embed-cdt/web-jekyll", //
      "https://github.com/eclipse-embed-cdt/web-preview", //
      "https://github.com/eclipse-emf-compare/emf-compare-acceptance", //
      "https://github.com/eclipse-emf-compare/emf-compare-releng", //
      "https://github.com/eclipse-emfcloud/emfcloud", //
      "https://github.com/eclipse-emfcloud/emfcloud-modelserver-theia", //
      "https://github.com/eclipse-emfcloud/jsonforms-property-view", //
      "https://github.com/eclipse-emfcloud/modelserver-node", //
      "https://github.com/eclipse-emfcloud/theia-tree-editor", //
      "https://github.com/eclipse-equinox/equinox.bundles", //
      "https://github.com/eclipse-equinox/equinox.framework", //
      "https://github.com/eclipse-esmf/esmf-antora-ui", //
      "https://github.com/eclipse-esmf/esmf-aspect-model-editor", //
      "https://github.com/eclipse-esmf/esmf-manufacturing-information-model", //
      "https://github.com/eclipse-esmf/esmf-parent", //
      "https://github.com/eclipse-esmf/esmf-sdk-js-aspect-model-loader", //
      "https://github.com/eclipse-esmf/esmf-sdk-js-schematics", //
      "https://github.com/eclipse-esmf/esmf-sdk-js-schematics-demo", //
      "https://github.com/eclipse-esmf/esmf-sdk-py-aspect-model-loader", //
      "https://github.com/eclipse-esmf/esmf-sdk-py-pandas-dataframe", //
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
      "https://github.com/eclipse-glsp/glsp", //
      "https://github.com/eclipse-glsp/glsp-client", //
      "https://github.com/eclipse-glsp/glsp-playwright", //
      "https://github.com/eclipse-glsp/glsp-server-node", //
      "https://github.com/eclipse-glsp/glsp-theia-integration", //
      "https://github.com/eclipse-glsp/glsp-vscode-integration", //
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
      "https://github.com/eclipse-iofog/Controller", //
      "https://github.com/eclipse-iofog/ECN-Viewer", //
      "https://github.com/eclipse-iofog/HardwareAbstraction", //
      "https://github.com/eclipse-iofog/agent-go", //
      "https://github.com/eclipse-iofog/common-logging", //
      "https://github.com/eclipse-iofog/core-networking", //
      "https://github.com/eclipse-iofog/demo", //
      "https://github.com/eclipse-iofog/documentation", //
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
      "https://github.com/eclipse-iofog/port-manager", //
      "https://github.com/eclipse-iofog/restblue", //
      "https://github.com/eclipse-iofog/router", //
      "https://github.com/eclipse-iofog/test-runner", //
      "https://github.com/eclipse-jdt/eclipse.jdt", //
      "https://github.com/eclipse-jgit/jgit-permissions", //
      "https://github.com/eclipse-jgit/jgit-pipelines", //
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
      "https://github.com/eclipse-keypop/keypop", //
      "https://github.com/eclipse-keypop/keypop-calypso-card-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-card-java-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-asymmetric-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-legacysam-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-legacysam-java-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-symmetric-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-card-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-card-java-api", //
      "https://github.com/eclipse-keypop/keypop-ops", //
      "https://github.com/eclipse-keypop/keypop-reader-cpp-api", //
      "https://github.com/eclipse-keypop/keypop-reader-java-api", //
      "https://github.com/eclipse-kuksa/kuksa-actions", //
      "https://github.com/eclipse-kuksa/kuksa-android-companion", //
      "https://github.com/eclipse-kuksa/kuksa-common", //
      "https://github.com/eclipse-kuksa/kuksa-databroker", //
      "https://github.com/eclipse-kuksa/kuksa-hardware", //
      "https://github.com/eclipse-kuksa/kuksa-python-sdk", //
      "https://github.com/eclipse-kuksa/kuksa-viss", //
      "https://github.com/eclipse-langium/langium", //
      "https://github.com/eclipse-leda/leda", //
      "https://github.com/eclipse-leda/leda-contrib-cloud-connector", //
      "https://github.com/eclipse-leda/leda-contrib-container-update-agent", //
      "https://github.com/eclipse-leda/leda-contrib-otel", //
      "https://github.com/eclipse-leda/leda-contrib-self-update-agent", //
      "https://github.com/eclipse-leda/leda-contrib-vehicle-update-manager", //
      "https://github.com/eclipse-leda/leda-distro", //
      "https://github.com/eclipse-leda/leda-example-applications", //
      "https://github.com/eclipse-leda/leda-utils", //
      "https://github.com/eclipse-leda/meta-leda", //
      "https://github.com/eclipse-moec/integration", //
      "https://github.com/eclipse-moec/motioncontrol", //
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
      "https://github.com/eclipse-muto/liveui-core", //
      "https://github.com/eclipse-muto/liveui-react", //
      "https://github.com/eclipse-muto/liveui-react-native", //
      "https://github.com/eclipse-muto/liveui-vue", //
      "https://github.com/eclipse-muto/messages", //
      "https://github.com/eclipse-muto/sandbox", //
      "https://github.com/eclipse-openj9/build-openj9", //
      "https://github.com/eclipse-openj9/openj9-docs", //
      "https://github.com/eclipse-openj9/openj9-docs-staging", //
      "https://github.com/eclipse-openj9/openj9-jenkins", //
      "https://github.com/eclipse-openj9/openj9-omr", //
      "https://github.com/eclipse-opensmartclide/smartclide", //
      "https://github.com/eclipse-opensmartclide/smartclide-Backend-REST-Client", //
      "https://github.com/eclipse-opensmartclide/smartclide-Che-REST-Client", //
      "https://github.com/eclipse-opensmartclide/smartclide-ServDB", //
      "https://github.com/eclipse-opensmartclide/smartclide-Service-Creation-Testing", //
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
      "https://github.com/eclipse-opensmartclide/smartclide-smart-assistant", //
      "https://github.com/eclipse-opensmartclide/smartclide-smart-assistant-theia", //
      "https://github.com/eclipse-opensmartclide/smartclide-task-service-discovery", //
      "https://github.com/eclipse-opensmartclide/smartclide-td-reusability-theia", //
      "https://github.com/eclipse-openxilenv/openxilenv", //
      "https://github.com/eclipse-p3com/p3com", //
      "https://github.com/eclipse-pass/main", //
      "https://github.com/eclipse-pass/pass-acceptance-testing", //
      "https://github.com/eclipse-pass/pass-acceptance-testing/", //
      "https://github.com/eclipse-pass/pass-auth", //
      "https://github.com/eclipse-pass/pass-data-model", //
      "https://github.com/eclipse-pass/pass-data-model/", //
      "https://github.com/eclipse-pass/pass-docker", //
      "https://github.com/eclipse-pass/pass-docker-mailserver", //
      "https://github.com/eclipse-pass/pass-docker-mailserver/", //
      "https://github.com/eclipse-pass/pass-download-service", //
      "https://github.com/eclipse-pass/pass-download-service/", //
      "https://github.com/eclipse-pass/pass-dupe-checker", //
      "https://github.com/eclipse-pass/pass-dupe-checker/", //
      "https://github.com/eclipse-pass/pass-ember-adapter", //
      "https://github.com/eclipse-pass/pass-ember-adapter/", //
      "https://github.com/eclipse-pass/pass-metadata-schemas", //
      "https://github.com/eclipse-pass/pass-metadata-schemas/", //
      "https://github.com/eclipse-pass/pass-policy-service", //
      "https://github.com/eclipse-pass/pass-policy-service/", //
      "https://github.com/eclipse-pass/pass-test", //
      "https://github.com/eclipse-pass/pass-ui", //
      "https://github.com/eclipse-pass/pass-ui-public", //
      "https://github.com/eclipse-pass/pass-ui-public/", //
      "https://github.com/eclipse-pass/pass-ui/", //
      "https://github.com/eclipse-pass/playground", //
      "https://github.com/eclipse-passage/passage-docs", //
      "https://github.com/eclipse-passage/passage-images", //
      "https://github.com/eclipse-pde/eclipse.pde.build", //
      "https://github.com/eclipse-platform/eclipse.platform.common", //
      "https://github.com/eclipse-platform/eclipse.platform.debug", //
      "https://github.com/eclipse-platform/eclipse.platform.releng", //
      "https://github.com/eclipse-platform/eclipse.platform.resources", //
      "https://github.com/eclipse-platform/eclipse.platform.runtime", //
      "https://github.com/eclipse-platform/eclipse.platform.team", //
      "https://github.com/eclipse-platform/eclipse.platform.text", //
      "https://github.com/eclipse-platform/eclipse.platform.ua", //
      "https://github.com/eclipse-platform/eclipse.platform.ui.tools", //
      "https://github.com/eclipse-ptp/ptp.doc", //
      "https://github.com/eclipse-qrisp/Qrisp", //
      "https://github.com/eclipse-rmf/org.eclipse.rmf.documentation", //
      "https://github.com/eclipse-scava/scava-datasets", //
      "https://github.com/eclipse-scout/scout.ci", //
      "https://github.com/eclipse-scout/scout.maven-master", //
      "https://github.com/eclipse-sdv-blueprints/blueprints", //
      "https://github.com/eclipse-sdv-blueprints/fleet-management", //
      "https://github.com/eclipse-sdv-blueprints/insurance", //
      "https://github.com/eclipse-sdv-blueprints/ros-racer", //
      "https://github.com/eclipse-set/build", //
      "https://github.com/eclipse-simrel/help.eclipse.org", //
      "https://github.com/eclipse-simrel/simrel.build", //
      "https://github.com/eclipse-simrel/simrel.tools", //
      "https://github.com/eclipse-sirius/sirius-specs", //
      "https://github.com/eclipse-slm/ansible", //
      "https://github.com/eclipse-slm/awx", //
      "https://github.com/eclipse-slm/molecule", //
      "https://github.com/eclipse-slm/molecule_vsphere", //
      "https://github.com/eclipse-slm/slm-ansible-role-basyx-server", //
      "https://github.com/eclipse-slm/slm-ansible-role-consul", //
      "https://github.com/eclipse-slm/slm-ansible-role-docker", //
      "https://github.com/eclipse-slm/slm-ansible-role-inventory-helper", //
      "https://github.com/eclipse-slm/slm-ansible-role-node-exporter", //
      "https://github.com/eclipse-slm/slm-cc-base", //
      "https://github.com/eclipse-slm/slm-dc-docker", //
      "https://github.com/eclipse-slm/slm-dc-docker-portainer", //
      "https://github.com/eclipse-slm/slm-dc-docker-swarm", //
      "https://github.com/eclipse-slm/slm-dc-docker-tcp", //
      "https://github.com/eclipse-slm/slm-dc-dummy", //
      "https://github.com/eclipse-slm/slm-dc-k3s", //
      "https://github.com/eclipse-slm/slm-dc-k3s-single", //
      "https://github.com/eclipse-slm/slm-dc-k8s", //
      "https://github.com/eclipse-slm/slm-pr-ansible-facts", //
      "https://github.com/eclipse-slm/slm-pr-webcams", //
      "https://github.com/eclipse-sparkplug/sparkplug.listings", //
      "https://github.com/eclipse-sprotty/sprotty", //
      "https://github.com/eclipse-sprotty/sprotty-theia", //
      "https://github.com/eclipse-sprotty/sprotty-vscode", //
      "https://github.com/eclipse-store/docs-site", //
      "https://github.com/eclipse-sw360/sw360-frontend", //
      "https://github.com/eclipse-theia/cryptodetector", //
      "https://github.com/eclipse-theia/dugite-extra", //
      "https://github.com/eclipse-theia/dugite-no-gpl", //
      "https://github.com/eclipse-theia/generator-theia-extension", //
      "https://github.com/eclipse-theia/security-audit", //
      "https://github.com/eclipse-theia/theia", //
      "https://github.com/eclipse-theia/theia-blueprint", //
      "https://github.com/eclipse-theia/theia-cpp-extensions", //
      "https://github.com/eclipse-theia/theia-e2e-test-suite", //
      "https://github.com/eclipse-theia/theia-playwright-template", //
      "https://github.com/eclipse-theia/theia-vscodecov", //
      "https://github.com/eclipse-theia/vscode-builtin-extensions", //
      "https://github.com/eclipse-theia/vscode-theia-comparator", //
      "https://github.com/eclipse-thingweb/node-wot", //
      "https://github.com/eclipse-thingweb/playground", //
      "https://github.com/eclipse-thingweb/td-tools", //
      "https://github.com/eclipse-thingweb/test-things", //
      "https://github.com/eclipse-thingweb/thingweb", //
      "https://github.com/eclipse-tocandira/Container-Watchdog", //
      "https://github.com/eclipse-tocandira/Tocandira", //
      "https://github.com/eclipse-tocandira/Tocandira-Dashboard", //
      "https://github.com/eclipse-tocandira/Tocandira-api-gateway", //
      "https://github.com/eclipse-tocandira/Tocandira-back", //
      "https://github.com/eclipse-tocandira/Tocandira-front", //
      "https://github.com/eclipse-tocandira/Tocandira-opcua-gateway", //
      "https://github.com/eclipse-tractusx/app-dashboard", //
      "https://github.com/eclipse-tractusx/asset-tracking-platform", //
      "https://github.com/eclipse-tractusx/bpdm-certificate-management", //
      "https://github.com/eclipse-tractusx/charts", //
      "https://github.com/eclipse-tractusx/daps-helm-chart", //
      "https://github.com/eclipse-tractusx/demand-capacity-mgmt", //
      "https://github.com/eclipse-tractusx/demand-capacity-mgmt-backend", //
      "https://github.com/eclipse-tractusx/demand-capacity-mgmt-frontend", //
      "https://github.com/eclipse-tractusx/e2e-testing", //
      "https://github.com/eclipse-tractusx/eclipse-tractusx.github.io.largefiles", //
      "https://github.com/eclipse-tractusx/eco-pass-kit", //
      "https://github.com/eclipse-tractusx/identity-trust", //
      "https://github.com/eclipse-tractusx/knowledge-agents", //
      "https://github.com/eclipse-tractusx/knowledge-agents-aas-bridge", //
      "https://github.com/eclipse-tractusx/knowledge-agents-edc", //
      "https://github.com/eclipse-tractusx/managed-identity-wallets-archived", //
      "https://github.com/eclipse-tractusx/managed-simple-data-exchanger", //
      "https://github.com/eclipse-tractusx/managed-simple-data-exchanger-frontend", //
      "https://github.com/eclipse-tractusx/online-simulation-kit", //
      "https://github.com/eclipse-tractusx/ontology", //
      "https://github.com/eclipse-tractusx/pcf-exchange-kit", //
      "https://github.com/eclipse-tractusx/policy-hub", //
      "https://github.com/eclipse-tractusx/portal-backend", //
      "https://github.com/eclipse-tractusx/portal-cd", //
      "https://github.com/eclipse-tractusx/portal-frontend", //
      "https://github.com/eclipse-tractusx/portal-frontend-registration", //
      "https://github.com/eclipse-tractusx/portal-iam", //
      "https://github.com/eclipse-tractusx/portal-shared-components", //
      "https://github.com/eclipse-tractusx/puris", //
      "https://github.com/eclipse-tractusx/puris-frontend", //
      "https://github.com/eclipse-tractusx/sig-infra", //
      "https://github.com/eclipse-tractusx/sig-release", //
      "https://github.com/eclipse-tractusx/sig-security", //
      "https://github.com/eclipse-tractusx/sldt-semantic-models", //
      "https://github.com/eclipse-tractusx/ssi-docu", //
      "https://github.com/eclipse-tractusx/testdata-provider", //
      "https://github.com/eclipse-tractusx/tractus-x-release", //
      "https://github.com/eclipse-tractusx/tractusx-quality-checks", //
      "https://github.com/eclipse-tractusx/tutorial-resources", //
      "https://github.com/eclipse-tractusx/vas-country-risk", //
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
      "https://github.com/eclipse-velocitas/release-documentation-action", //
      "https://github.com/eclipse-velocitas/vehicle-app-cpp-sdk", //
      "https://github.com/eclipse-velocitas/vehicle-app-cpp-template", //
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
      "https://github.com/eclipse-volttron/docker", //
      "https://github.com/eclipse-volttron/github-tooling", //
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
      "https://github.com/eclipse-volttron/volttron-sql-historian", //
      "https://github.com/eclipse-volttron/volttron-sqlite-historian", //
      "https://github.com/eclipse-volttron/volttron-sqlite-tagging", //
      "https://github.com/eclipse-volttron/volttron-sysmon", //
      "https://github.com/eclipse-volttron/volttron-testing", //
      "https://github.com/eclipse-volttron/volttron-threshold-detection", //
      "https://github.com/eclipse-volttron/volttron-topic-watcher", //
      "https://github.com/eclipse-volttron/volttron-web-client", //
      "https://github.com/eclipse-xpanse/policy-man", //
      "https://github.com/eclipse-xpanse/xpanse-iam", //
      "https://github.com/eclipse-xpanse/xpanse-relops", //
      "https://github.com/eclipse-xpanse/xpanse-ui", //
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
      "https://github.com/eclipse-zenoh/zenoh-flow", //
      "https://github.com/eclipse-zenoh/zenoh-flow-python", //
      "https://github.com/eclipse-zenoh/zenoh-go", //
      "https://github.com/eclipse-zenoh/zenoh-kotlin", //
      "https://github.com/eclipse-zenoh/zenoh-pico", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-dds", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-mqtt", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-ros1", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-ros2dds", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-webserver", //
      "https://github.com/eclipse-zenoh/zenoh-plugin-zenoh-flow", //
      "https://github.com/eclipse-zenoh/zenoh-python", //
      "https://github.com/eclipse/adaaa", //
      "https://github.com/eclipse/adore", //
      "https://github.com/eclipse/agileuml", //
      "https://github.com/eclipse/capella-addons", //
      "https://github.com/eclipse/capella-gitadapter", //
      "https://github.com/eclipse/capella-pipeline-library", //
      "https://github.com/eclipse/capella-releng-parent", //
      "https://github.com/eclipse/che", //
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
      "https://github.com/eclipse/diafanis", //
      "https://github.com/eclipse/dirigible-samples", //
      "https://github.com/eclipse/eclipsefuro", //
      "https://github.com/eclipse/eclipsefuro-web", //
      "https://github.com/eclipse/editdor", //
      "https://github.com/eclipse/elk-models", //
      "https://github.com/eclipse/gemoc-studio-extras", //
      "https://github.com/eclipse/hara-ddiclient", //
      "https://github.com/eclipse/hawkbit-clients-golang", //
      "https://github.com/eclipse/iottestware", //
      "https://github.com/eclipse/iottestware.coap", //
      "https://github.com/eclipse/iottestware.dashboard", //
      "https://github.com/eclipse/iottestware.fuzzing", //
      "https://github.com/eclipse/iottestware.mqtt", //
      "https://github.com/eclipse/iottestware.opcua", //
      "https://github.com/eclipse/iottestware.performance", //
      "https://github.com/eclipse/jetty.docker", //
      "https://github.com/eclipse/jetty.parent", //
      "https://github.com/eclipse/keyple", //
      "https://github.com/eclipse/keyple-card-calypso-cpp-lib", //
      "https://github.com/eclipse/keyple-card-generic-cpp-lib", //
      "https://github.com/eclipse/keyple-common-cpp-api", //
      "https://github.com/eclipse/keyple-cpp", //
      "https://github.com/eclipse/keyple-cpp-example", //
      "https://github.com/eclipse/keyple-cpp-meta", //
      "https://github.com/eclipse/keyple-ops", //
      "https://github.com/eclipse/keyple-plugin-android-nfc-java-lib", //
      "https://github.com/eclipse/keyple-plugin-android-omapi-java-lib", //
      "https://github.com/eclipse/keyple-plugin-cpp-api", //
      "https://github.com/eclipse/keyple-plugin-pcsc-cpp-lib", //
      "https://github.com/eclipse/keyple-plugin-stub-cpp-lib", //
      "https://github.com/eclipse/keyple-service-cpp-lib", //
      "https://github.com/eclipse/keyple-service-resource-cpp-lib", //
      "https://github.com/eclipse/keyple-util-cpp-lib", //
      "https://github.com/eclipse/kiso-testing", //
      "https://github.com/eclipse/kiso-testing-python-uds", //
      "https://github.com/eclipse/kiso-testing-testapp", //
      "https://github.com/eclipse/kiso-testing-vscode", //
      "https://github.com/eclipse/kuksa.hardware", //
      "https://github.com/eclipse/kuksa.val", //
      "https://github.com/eclipse/kuksa.val.feeders", //
      "https://github.com/eclipse/kuksa.val.services", //
      "https://github.com/eclipse/kura-apps", //
      "https://github.com/eclipse/leshan.osgi", //
      "https://github.com/eclipse/lyo.oslc-ui", //
      "https://github.com/eclipse/microprofile-bom", //
      "https://github.com/eclipse/microprofile-build-infra", //
      "https://github.com/eclipse/microprofile-evolution-process", //
      "https://github.com/eclipse/microprofile-marketing", //
      "https://github.com/eclipse/microprofile-parent", //
      "https://github.com/eclipse/microprofile-service-mesh", //
      "https://github.com/eclipse/mosquitto", //
      "https://github.com/eclipse/mosquitto.rsmb", //
      "https://github.com/eclipse/mraa", //
      "https://github.com/eclipse/n4js-tutorials", //
      "https://github.com/eclipse/omr", //
      "https://github.com/eclipse/open-vsx.org", //
      "https://github.com/eclipse/openmcx", //
      "https://github.com/eclipse/org.eclipse.riena.3xtargets", //
      "https://github.com/eclipse/org.eclipse.riena.setup", //
      "https://github.com/eclipse/org.eclipse.sensinact", //
      "https://github.com/eclipse/org.eclipse.sensinact.studioweb", //
      "https://github.com/eclipse/orion", //
      "https://github.com/eclipse/orion.electron", //
      "https://github.com/eclipse/orion.server.node", //
      "https://github.com/eclipse/packages", //
      "https://github.com/eclipse/packages-c2e-provisioning", //
      "https://github.com/eclipse/paho.golang", //
      "https://github.com/eclipse/paho.mqtt-sn.embedded-c", //
      "https://github.com/eclipse/paho.mqtt.c", //
      "https://github.com/eclipse/paho.mqtt.cpp", //
      "https://github.com/eclipse/paho.mqtt.d", //
      "https://github.com/eclipse/paho.mqtt.embedded-c", //
      "https://github.com/eclipse/paho.mqtt.golang", //
      "https://github.com/eclipse/paho.mqtt.javascript", //
      "https://github.com/eclipse/paho.mqtt.m2mqtt", //
      "https://github.com/eclipse/paho.mqtt.python", //
      "https://github.com/eclipse/paho.mqtt.ruby", //
      "https://github.com/eclipse/paho.mqtt.rust", //
      "https://github.com/eclipse/paho.mqtt.testing", //
      "https://github.com/eclipse/rdf4j-doc", //
      "https://github.com/eclipse/sommr", //
      "https://github.com/eclipse/sprotty-layout", //
      "https://github.com/eclipse/streamsheets", //
      "https://github.com/eclipse/theia-generator-plugin", //
      "https://github.com/eclipse/theia-java-extension", //
      "https://github.com/eclipse/theia-plugin-packager", //
      "https://github.com/eclipse/theia-python-extension", //
      "https://github.com/eclipse/theia-yeoman-plugin", //
      "https://github.com/eclipse/tinydtls", //
      "https://github.com/eclipse/unide.python", //
      "https://github.com/eclipse/upm", //
      "https://github.com/eclipse/wakaama", //
      "https://github.com/eclipse/winery-topologymodeler", //
      "https://github.com/eclipse/xacc", //
      "https://github.com/eclipse/xtext-archive", //
      "https://github.com/jakartaee/inject-spec", //
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
      "https://github.com/locationtech/rasterframes", //
      "https://github.com/locationtech/sfcurve", //
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
      "https://gitlab.eclipse.org/eclipse/aidge/aidge", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_backend_cpu", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_backend_cuda", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_core", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_dataloader", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_export_cpp", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_export_tensorrt", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_module_template", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_onnx", //
      "https://gitlab.eclipse.org/eclipse/aidge/aidge_quantization", //
      "https://gitlab.eclipse.org/eclipse/ambientlight/ambient-light-services", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.tools.rtc", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.tools.simulation", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.tools.simulation.examples", //
      "https://gitlab.eclipse.org/eclipse/asciidoc-lang/asciidoc-lang", //
      "https://gitlab.eclipse.org/eclipse/asciidoc-lang/asciidoc-tck", //
      "https://gitlab.eclipse.org/eclipse/austen/austen", //
      "https://gitlab.eclipse.org/eclipse/autowrx/autowrx", //
      "https://gitlab.eclipse.org/eclipse/dash/org.eclipse.dash.handbook", //
      "https://gitlab.eclipse.org/eclipse/drops/drops-agent", //
      "https://gitlab.eclipse.org/eclipse/gmf-tooling/org.eclipse.gmf-tooling.uml2tools.releng", //
      "https://gitlab.eclipse.org/eclipse/graphene/ai-interfaces", //
      "https://gitlab.eclipse.org/eclipse/graphene/eclipse-graphene", //
      "https://gitlab.eclipse.org/eclipse/graphene/generic-parallel-orchestrator", //
      "https://gitlab.eclipse.org/eclipse/graphene/playground-app", //
      "https://gitlab.eclipse.org/eclipse/graphene/tutorials", //
      "https://gitlab.eclipse.org/eclipse/mpc/org-eclipse-epp-mpc", //
      "https://gitlab.eclipse.org/eclipse/openk-coremodules/org.eclipse.openk-coremodules.contactBaseData.documentation", //
      "https://gitlab.eclipse.org/eclipse/openk-coremodules/org.eclipse.openk-coremodules.contactBaseData.frontend", //
      "https://gitlab.eclipse.org/eclipse/openk-coremodules/org.eclipse.openk-coremodules.portalFE", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.elogbookFE", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.gridFailureInformation.documentation", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.gridFailureInformation.frontend", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.plannedGridMeasures.documentation", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.plannedGridMeasures.frontend", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.standbyPlanning.docu", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.standbyPlanning.frontend", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.statementPublicAffairs.documentation", //
      "https://gitlab.eclipse.org/eclipse/openk-usermodules/org.eclipse.openk-usermodules.statementPublicAffairs.frontend", //
      "https://gitlab.eclipse.org/eclipse/openpass/mantle-api", //
      "https://gitlab.eclipse.org/eclipse/openpass/map-sdk", //
      "https://gitlab.eclipse.org/eclipse/openpass/opSimulation", //
      "https://gitlab.eclipse.org/eclipse/openpass/openscenario1_engine", //
      "https://gitlab.eclipse.org/eclipse/openpass/opgui", //
      "https://gitlab.eclipse.org/eclipse/openpass/opvisualizer", //
      "https://gitlab.eclipse.org/eclipse/openpass/osi-traffic-participant", //
      "https://gitlab.eclipse.org/eclipse/openpass/road-logic-suite", //
      "https://gitlab.eclipse.org/eclipse/openpass/yase", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.dependencies", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.dependencies-mbp", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.dependencies.p2", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.fork.jpos", //
      "https://gitlab.eclipse.org/eclipse/osee/org.eclipse.ote", //
      "https://gitlab.eclipse.org/eclipse/plato/bok", //
      "https://gitlab.eclipse.org/eclipse/plato/www", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.contrib", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.test", //
      "https://gitlab.eclipse.org/eclipse/rtsc/org.eclipse.rtsc.training", //
      "https://gitlab.eclipse.org/eclipse/scm/scm", //
      "https://gitlab.eclipse.org/eclipse/sw361/playground-git", //
      "https://gitlab.eclipse.org/eclipse/tcf/tcf.agent", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.vs-code-extension", //
      "https://gitlab.eclipse.org/eclipse/xfsc/ocm-w-stack-epics", //
      "https://gitlab.eclipse.org/eclipse/xfsc/to-be-deleted-3", //
      "https://gitlab.eclipse.org/eclipse/xfsc/train1", //
      "https://gitlab.eclipse.org/eclipse/xfsc/xfsc-spec-2", //
      ""//
  ));

  private static Set<String> REPOSITORIES = new TreeSet<>(Set.of( //
      // generated-repositories
      "https://git.eclipse.org/r/4diac/org.eclipse.4diac.ide", //
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
      "https://git.eclipse.org/r/eef/org.eclipse.eef", //
      "https://git.eclipse.org/r/efm/org.eclipse.efm-modeling", //
      "https://git.eclipse.org/r/emfclient/org.eclipse.emf.ecp.core", //
      "https://git.eclipse.org/r/esf/esf-infra", //
      "https://git.eclipse.org/r/esf/esf-tools", //
      "https://git.eclipse.org/r/gendoc/org.eclipse.gendoc", //
      "https://git.eclipse.org/r/handly/org.eclipse.handly", //
      "https://git.eclipse.org/r/henshin/org.eclipse.emft.henshin", //
      "https://git.eclipse.org/r/jeetools/webtools.javaee", //
      "https://git.eclipse.org/r/jsf/webtools.jsf", //
      "https://git.eclipse.org/r/jsf/webtools.jsf.tests", //
      "https://git.eclipse.org/r/ldt/org.eclipse.ldt", //
      "https://git.eclipse.org/r/mat/org.eclipse.mat", //
      "https://git.eclipse.org/r/mdht/org.eclipse.mdht", //
      "https://git.eclipse.org/r/mdmbl/org.eclipse.mdm", //
      "https://git.eclipse.org/r/mdmbl/org.eclipse.mdm.api.base", //
      "https://git.eclipse.org/r/mdmbl/org.eclipse.mdm.api.default", //
      "https://git.eclipse.org/r/mdmbl/org.eclipse.mdm.api.odsadapter", //
      "https://git.eclipse.org/r/mdmbl/org.eclipse.mdm.mdfsorter", //
      "https://git.eclipse.org/r/mdmbl/org.eclipse.mdm.nucleus", //
      "https://git.eclipse.org/r/mdmbl/org.eclipse.mdm.openatfx.mdf", //
      "https://git.eclipse.org/r/mdmbl/org.eclipse.mdm.realms", //
      "https://git.eclipse.org/r/mmt/org.eclipse.atl", //
      "https://git.eclipse.org/r/mmt/org.eclipse.atl.tcs", //
      "https://git.eclipse.org/r/mmt/org.eclipse.qvtd", //
      "https://git.eclipse.org/r/mmt/org.eclipse.qvto", //
      "https://git.eclipse.org/r/modisco/org.eclipse.modisco", //
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
      "https://git.eclipse.org/r/sphinx/org.eclipse.sphinx", //
      "https://git.eclipse.org/r/stem/org.eclipse.stem", //
      "https://git.eclipse.org/r/stem/org.eclipse.stem.data", //
      "https://git.eclipse.org/r/stem/org.eclipse.stem.data.earthscience", //
      "https://git.eclipse.org/r/swtbot/org.eclipse.swtbot", //
      "https://git.eclipse.org/r/tea/tea", //
      "https://git.eclipse.org/r/tm/org.eclipse.tm", //
      "https://git.eclipse.org/r/tracecompass/org.eclipse.tracecompass", //
      "https://git.eclipse.org/r/usssdk/org.eclipse.usssdk", //
      "https://git.eclipse.org/r/viatra/org.eclipse.viatra", //
      "https://git.eclipse.org/r/viatra/org.eclipse.viatra.examples", //
      "https://git.eclipse.org/r/viatra/org.eclipse.viatra.modelobfuscator", //
      "https://git.eclipse.org/r/viatra/org.eclipse.viatra2.vpm", //
      "https://git.eclipse.org/r/webtools-common/webtools.common", //
      "https://git.eclipse.org/r/webtools-common/webtools.common.fproj", //
      "https://git.eclipse.org/r/webtools-common/webtools.common.snippets", //
      "https://git.eclipse.org/r/webtools-common/webtools.common.tests", //
      "https://git.eclipse.org/r/webtools/webtools.releng", //
      "https://git.eclipse.org/r/xwt/org.eclipse.xwt", //
      "https://github.com/adoptium/STF", //
      "https://github.com/adoptium/TKG", //
      "https://github.com/adoptium/aqa-systemtest", //
      "https://github.com/adoptium/aqa-tests", //
      "https://github.com/adoptium/emt4j", //
      "https://github.com/adoptium/installer", //
      "https://github.com/adoptium/jdk", //
      "https://github.com/adoptium/temurin-build", //
      "https://github.com/deeplearning4j/deeplearning4j", //
      "https://github.com/deeplearning4j/deeplearning4j-examples", //
      "https://github.com/eclipse-aas4j/aas4j", //
      "https://github.com/eclipse-aas4j/aas4j-model-generator", //
      "https://github.com/eclipse-aas4j/aas4j-transformation-library", //
      "https://github.com/eclipse-acceleo/acceleo", //
      "https://github.com/eclipse-actf/org.eclipse.actf", //
      "https://github.com/eclipse-actf/org.eclipse.actf.ai", //
      "https://github.com/eclipse-actf/org.eclipse.actf.common", //
      "https://github.com/eclipse-actf/org.eclipse.actf.examples", //
      "https://github.com/eclipse-actf/org.eclipse.actf.visualization", //
      "https://github.com/eclipse-agail/agile-ble", //
      "https://github.com/eclipse-agail/agile-core", //
      "https://github.com/eclipse-agail/agile-dbus-java-interface", //
      "https://github.com/eclipse-agail/agile-dummy-protocol", //
      "https://github.com/eclipse-agail/agile-recommender", //
      "https://github.com/eclipse-arrowhead/application-library-java-spring", //
      "https://github.com/eclipse-aspectj/ajdt", //
      "https://github.com/eclipse-aspectj/aspectj", //
      "https://github.com/eclipse-aspectj/eclipse.jdt.core", //
      "https://github.com/eclipse-babel/plugins", //
      "https://github.com/eclipse-basyx/basyx-archive", //
      "https://github.com/eclipse-basyx/basyx-databridge", //
      "https://github.com/eclipse-basyx/basyx-demonstrators", //
      "https://github.com/eclipse-basyx/basyx-java-components", //
      "https://github.com/eclipse-basyx/basyx-java-examples", //
      "https://github.com/eclipse-basyx/basyx-java-sdk", //
      "https://github.com/eclipse-basyx/basyx-java-server-sdk", //
      "https://github.com/eclipse-birt/birt", //
      "https://github.com/eclipse-californium/californium", //
      "https://github.com/eclipse-californium/californium.actinium", //
      "https://github.com/eclipse-californium/californium.tools", //
      "https://github.com/eclipse-cbi/eclipse-cbi-tycho-example", //
      "https://github.com/eclipse-cbi/hipp2jipp", //
      "https://github.com/eclipse-cbi/macos-notarization-service", //
      "https://github.com/eclipse-cbi/org.eclipse.cbi", //
      "https://github.com/eclipse-cbi/org.eclipse.cbi-testdata", //
      "https://github.com/eclipse-cbi/p2repo-aggregator", //
      "https://github.com/eclipse-cbi/p2repo-analyzers", //
      "https://github.com/eclipse-cbi/targetplatform-dsl", //
      "https://github.com/eclipse-cdo/cdo", //
      "https://github.com/eclipse-cdo/cdo.infrastructure", //
      "https://github.com/eclipse-cdo/cdo.old", //
      "https://github.com/eclipse-cdt/cdt", //
      "https://github.com/eclipse-cdt/cdt-lsp", //
      "https://github.com/eclipse-cdt/cdt-new-managedbuild-prototype", //
      "https://github.com/eclipse-che/che-lib", //
      "https://github.com/eclipse-che/che-ls-jdt", //
      "https://github.com/eclipse-che/che-plugin-svn", //
      "https://github.com/eclipse-che/che-server", //
      "https://github.com/eclipse-che4z/che-che4z-lsp-for-cobol", //
      "https://github.com/eclipse-cognicrypt/CogniCrypt", //
      "https://github.com/eclipse-corrosion/corrosion", //
      "https://github.com/eclipse-dataspacetck/cvf", //
      "https://github.com/eclipse-datatools/datatools", //
      "https://github.com/eclipse-ditto/ditto", //
      "https://github.com/eclipse-ditto/ditto-clients", //
      "https://github.com/eclipse-ditto/ditto-examples", //
      "https://github.com/eclipse-ditto/ditto-testing", //
      "https://github.com/eclipse-ecoretools/ecoretools", //
      "https://github.com/eclipse-edapt/edapt", //
      "https://github.com/eclipse-edc/Connector", //
      "https://github.com/eclipse-edc/FederatedCatalog", //
      "https://github.com/eclipse-edc/GradlePlugins", //
      "https://github.com/eclipse-edc/IDS-Serializer", //
      "https://github.com/eclipse-edc/IdentityHub", //
      "https://github.com/eclipse-edc/MinimumViableDataspace", //
      "https://github.com/eclipse-edc/RegistrationService", //
      "https://github.com/eclipse-edc/Runtime-Metamodel", //
      "https://github.com/eclipse-edc/Samples", //
      "https://github.com/eclipse-edc/Technology-Aws", //
      "https://github.com/eclipse-edc/Technology-Azure", //
      "https://github.com/eclipse-edc/Technology-Gcp", //
      "https://github.com/eclipse-edc/TrustFrameworkAdoption", //
      "https://github.com/eclipse-ee4j/angus-activation", //
      "https://github.com/eclipse-ee4j/angus-mail", //
      "https://github.com/eclipse-ee4j/batch-api", //
      "https://github.com/eclipse-ee4j/cargotracker", //
      "https://github.com/eclipse-ee4j/cdi-cpl", //
      "https://github.com/eclipse-ee4j/concurro", //
      "https://github.com/eclipse-ee4j/eclipselink", //
      "https://github.com/eclipse-ee4j/eclipselink-asm", //
      "https://github.com/eclipse-ee4j/eclipselink-build-support", //
      "https://github.com/eclipse-ee4j/eclipselink-oracleddlparser", //
      "https://github.com/eclipse-ee4j/eclipselink-releng", //
      "https://github.com/eclipse-ee4j/eclipselink-workbench", //
      "https://github.com/eclipse-ee4j/enterprise-deployment", //
      "https://github.com/eclipse-ee4j/epicyro", //
      "https://github.com/eclipse-ee4j/exousia", //
      "https://github.com/eclipse-ee4j/expressly", //
      "https://github.com/eclipse-ee4j/genericmessagingra", //
      "https://github.com/eclipse-ee4j/glassfish", //
      "https://github.com/eclipse-ee4j/glassfish-build-maven-plugin", //
      "https://github.com/eclipse-ee4j/glassfish-copyright-plugin", //
      "https://github.com/eclipse-ee4j/glassfish-fighterfish", //
      "https://github.com/eclipse-ee4j/glassfish-ha-api", //
      "https://github.com/eclipse-ee4j/glassfish-hk2", //
      "https://github.com/eclipse-ee4j/glassfish-hk2-extra", //
      "https://github.com/eclipse-ee4j/glassfish-jsftemplating", //
      "https://github.com/eclipse-ee4j/glassfish-logging-annotation-processor", //
      "https://github.com/eclipse-ee4j/glassfish-maven-embedded-plugin", //
      "https://github.com/eclipse-ee4j/glassfish-security-plugin", //
      "https://github.com/eclipse-ee4j/glassfish-shoal", //
      "https://github.com/eclipse-ee4j/glassfish-spec-version-maven-plugin", //
      "https://github.com/eclipse-ee4j/glassfish-woodstock", //
      "https://github.com/eclipse-ee4j/grizzly", //
      "https://github.com/eclipse-ee4j/grizzly-ahc", //
      "https://github.com/eclipse-ee4j/grizzly-memcached", //
      "https://github.com/eclipse-ee4j/grizzly-npn", //
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
      "https://github.com/eclipse-ee4j/starter", //
      "https://github.com/eclipse-ee4j/tyrus", //
      "https://github.com/eclipse-ee4j/wasp", //
      "https://github.com/eclipse-ee4j/yasson", //
      "https://github.com/eclipse-efx/efxclipse", //
      "https://github.com/eclipse-efx/efxclipse-drift", //
      "https://github.com/eclipse-efx/efxclipse-eclipse", //
      "https://github.com/eclipse-efx/efxclipse-rt", //
      "https://github.com/eclipse-egerrit/egerrit", //
      "https://github.com/eclipse-egit/egit", //
      "https://github.com/eclipse-egit/egit-github", //
      "https://github.com/eclipse-embed-cdt/eclipse-plugins", //
      "https://github.com/eclipse-embed-cdt/org.eclipse.epp.packages", //
      "https://github.com/eclipse-emf-compare/emf-compare", //
      "https://github.com/eclipse-emf-compare/emf-compare-cli", //
      "https://github.com/eclipse-emf-parsley/emf-parsley", //
      "https://github.com/eclipse-emf/org.eclipse.emf", //
      "https://github.com/eclipse-emfatic/emfatic", //
      "https://github.com/eclipse-emfcloud/coffee-editor", //
      "https://github.com/eclipse-emfcloud/ecore-glsp", //
      "https://github.com/eclipse-emfcloud/emfcloud-modelserver", //
      "https://github.com/eclipse-emfcloud/emfjson-jackson", //
      "https://github.com/eclipse-emfcloud/model-validation", //
      "https://github.com/eclipse-emfcloud/modelserver-glsp-integration", //
      "https://github.com/eclipse-emfstore/org.eclipse.emf.emfstore.core", //
      "https://github.com/eclipse-equinox/equinox", //
      "https://github.com/eclipse-equinox/p2", //
      "https://github.com/eclipse-esmf/esmf-aspect-model-editor-backend", //
      "https://github.com/eclipse-esmf/esmf-sdk", //
      "https://github.com/eclipse-esmf/esmf-semantic-aspect-meta-model", //
      "https://github.com/eclipse-gemini/gemini.blueprint", //
      "https://github.com/eclipse-gemini/gemini.dbaccess", //
      "https://github.com/eclipse-gemini/gemini.dbaccess.postgresql", //
      "https://github.com/eclipse-gemini/gemini.jpa", //
      "https://github.com/eclipse-gemini/gemini.management", //
      "https://github.com/eclipse-gemini/gemini.naming", //
      "https://github.com/eclipse-gemini/gemini.web", //
      "https://github.com/eclipse-glsp/glsp-eclipse-integration", //
      "https://github.com/eclipse-glsp/glsp-examples", //
      "https://github.com/eclipse-glsp/glsp-server", //
      "https://github.com/eclipse-hono/hono", //
      "https://github.com/eclipse-hono/hono-extras", //
      "https://github.com/eclipse-iofog/Agent", //
      "https://github.com/eclipse-iofog/Connector", //
      "https://github.com/eclipse-iofog/example-microservices", //
      "https://github.com/eclipse-iofog/iofog-java-sdk", //
      "https://github.com/eclipse-iofog/test-message-generator", //
      "https://github.com/eclipse-jdt/eclipse.jdt.core", //
      "https://github.com/eclipse-jdt/eclipse.jdt.debug", //
      "https://github.com/eclipse-jdt/eclipse.jdt.ui", //
      "https://github.com/eclipse-jdtls/eclipse.jdt.ls", //
      "https://github.com/eclipse-jgit/jgit", //
      "https://github.com/eclipse-jsdt/webtools.jsdt", //
      "https://github.com/eclipse-justj/justj.tools", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-asymmetric-java-api", //
      "https://github.com/eclipse-keypop/keypop-calypso-crypto-symmetric-java-api", //
      "https://github.com/eclipse-kuksa/kuksa-android-sdk", //
      "https://github.com/eclipse-leda/leda-contrib-vscode-extensions", //
      "https://github.com/eclipse-leshan/leshan", //
      "https://github.com/eclipse-linuxtools/org.eclipse.linuxtools", //
      "https://github.com/eclipse-linuxtools/org.eclipse.linuxtools.eclipse-build", //
      "https://github.com/eclipse-lsp4j/lsp4j", //
      "https://github.com/eclipse-m2e/m2e-core", //
      "https://github.com/eclipse-m2e/m2e-discovery-catalog", //
      "https://github.com/eclipse-m2e/m2e-wtp", //
      "https://github.com/eclipse-m2e/m2e-wtp-jpa", //
      "https://github.com/eclipse-m2e/org.eclipse.m2e.workspace", //
      "https://github.com/eclipse-mpc/epp.mpc", //
      "https://github.com/eclipse-muto/liveui", //
      "https://github.com/eclipse-muto/liveui-samples", //
      "https://github.com/eclipse-mylyn/org.eclipse.mylyn", //
      "https://github.com/eclipse-mylyn/org.eclipse.mylyn.docs", //
      "https://github.com/eclipse-nattable/nattable", //
      "https://github.com/eclipse-oomph/oomph", //
      "https://github.com/eclipse-openj9/openj9", //
      "https://github.com/eclipse-openj9/openj9-systemtest", //
      "https://github.com/eclipse-openj9/openj9-utils", //
      "https://github.com/eclipse-opensmartclide/kie-wb-common", //
      "https://github.com/eclipse-opensmartclide/kie-wb-distributions", //
      "https://github.com/eclipse-opensmartclide/smartclide-TD-Interest", //
      "https://github.com/eclipse-opensmartclide/smartclide-TD-Principal", //
      "https://github.com/eclipse-opensmartclide/smartclide-TD-Reusability-Index", //
      "https://github.com/eclipse-opensmartclide/smartclide-api-gateway", //
      "https://github.com/eclipse-opensmartclide/smartclide-architectural-pattern", //
      "https://github.com/eclipse-opensmartclide/smartclide-broker", //
      "https://github.com/eclipse-opensmartclide/smartclide-cicd", //
      "https://github.com/eclipse-opensmartclide/smartclide-context", //
      "https://github.com/eclipse-opensmartclide/smartclide-db-api", //
      "https://github.com/eclipse-opensmartclide/smartclide-deployment-interpreter-service", //
      "https://github.com/eclipse-opensmartclide/smartclide-security", //
      "https://github.com/eclipse-opensmartclide/smartclide-service-creation", //
      "https://github.com/eclipse-opensmartclide/smartclide-service-registry-poc", //
      "https://github.com/eclipse-orbit/ebr", //
      "https://github.com/eclipse-orbit/orbit", //
      "https://github.com/eclipse-orbit/orbit-legacy", //
      "https://github.com/eclipse-orbit/orbit-simrel", //
      "https://github.com/eclipse-packaging/packages", //
      "https://github.com/eclipse-pass/modeshape", //
      "https://github.com/eclipse-pass/pass-authz", //
      "https://github.com/eclipse-pass/pass-core", //
      "https://github.com/eclipse-pass/pass-data-migration", //
      "https://github.com/eclipse-pass/pass-deposit-services", //
      "https://github.com/eclipse-pass/pass-doi-service", //
      "https://github.com/eclipse-pass/pass-fcrepo-jms", //
      "https://github.com/eclipse-pass/pass-fcrepo-jsonld", //
      "https://github.com/eclipse-pass/pass-fcrepo-module-auth-rbacl", //
      "https://github.com/eclipse-pass/pass-grant-loader", //
      "https://github.com/eclipse-pass/pass-indexer", //
      "https://github.com/eclipse-pass/pass-indexer-checker", //
      "https://github.com/eclipse-pass/pass-java-client", //
      "https://github.com/eclipse-pass/pass-journal-loader", //
      "https://github.com/eclipse-pass/pass-messaging-support", //
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
      "https://github.com/eclipse-platform/eclipse.platform.images", //
      "https://github.com/eclipse-platform/eclipse.platform.releng.aggregator", //
      "https://github.com/eclipse-platform/eclipse.platform.releng.buildtools", //
      "https://github.com/eclipse-platform/eclipse.platform.swt", //
      "https://github.com/eclipse-platform/eclipse.platform.ui", //
      "https://github.com/eclipse-ptp/ptp", //
      "https://github.com/eclipse-ptp/ptp.photran", //
      "https://github.com/eclipse-rap/org.eclipse.rap", //
      "https://github.com/eclipse-rap/org.eclipse.rap.tools", //
      "https://github.com/eclipse-rdf4j/rdf4j", //
      "https://github.com/eclipse-rmf/org.eclipse.rmf", //
      "https://github.com/eclipse-scout/scout.rt", //
      "https://github.com/eclipse-scout/scout.sdk", //
      "https://github.com/eclipse-serializer/serializer", //
      "https://github.com/eclipse-servertools/servertools", //
      "https://github.com/eclipse-set/browser", //
      "https://github.com/eclipse-set/model", //
      "https://github.com/eclipse-set/set", //
      "https://github.com/eclipse-set/toolboxmodel", //
      "https://github.com/eclipse-sirius/sirius-desktop", //
      "https://github.com/eclipse-sirius/sirius-emf-json", //
      "https://github.com/eclipse-sirius/sirius-web", //
      "https://github.com/eclipse-slm/awx-jwt-authenticator", //
      "https://github.com/eclipse-slm/slm", //
      "https://github.com/eclipse-sourceediting/sourceediting", //
      "https://github.com/eclipse-sparkplug/sparkplug", //
      "https://github.com/eclipse-sprotty/sprotty-server", //
      "https://github.com/eclipse-store/store", //
      "https://github.com/eclipse-sumo/sumo", //
      "https://github.com/eclipse-sw360/sw360", //
      "https://github.com/eclipse-tomled/tomled", //
      "https://github.com/eclipse-tractusx/SSI-agent-lib", //
      "https://github.com/eclipse-tractusx/bpdm", //
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
      "https://github.com/eclipse-tractusx/vas-country-risk-backend", //
      "https://github.com/eclipse-tycho/tycho", //
      "https://github.com/eclipse-uml2/uml2", //
      "https://github.com/eclipse-uomo/uomo", //
      "https://github.com/eclipse-uprotocol/uprotocol-java", //
      "https://github.com/eclipse-uprotocol/uprotocol-spec", //
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
      "https://github.com/eclipse-vertx/vertx-virtual-threads", //
      "https://github.com/eclipse-virgo/virgo-bundlor", //
      "https://github.com/eclipse-virgo/virgo-ide", //
      "https://github.com/eclipse-virgo/virgo-root", //
      "https://github.com/eclipse-webservices/webservices", //
      "https://github.com/eclipse-webservices/webservices.axis2", //
      "https://github.com/eclipse-webservices/webservices.jaxws", //
      "https://github.com/eclipse-wildwebdeveloper/wildwebdeveloper", //
      "https://github.com/eclipse-windowbuilder/windowbuilder", //
      "https://github.com/eclipse-xpanse/terraform-boot", //
      "https://github.com/eclipse-xpanse/xpanse", //
      "https://github.com/eclipse-zenoh/zenoh-java", //
      "https://github.com/eclipse/Xpect", //
      "https://github.com/eclipse/aCute", //
      "https://github.com/eclipse/amalgam", //
      "https://github.com/eclipse/amlen", //
      "https://github.com/eclipse/andmore", //
      "https://github.com/eclipse/buildship", //
      "https://github.com/eclipse/capella", //
      "https://github.com/eclipse/capella-basic-vp", //
      "https://github.com/eclipse/capella-cybersecurity", //
      "https://github.com/eclipse/capella-deferred-merge", //
      "https://github.com/eclipse/capella-filtering", //
      "https://github.com/eclipse/capella-requirements-vp", //
      "https://github.com/eclipse/capella-sss-transition", //
      "https://github.com/eclipse/capella-studio", //
      "https://github.com/eclipse/capella-textual-editor", //
      "https://github.com/eclipse/capella-tools", //
      "https://github.com/eclipse/capella-vpms", //
      "https://github.com/eclipse/capella-xhtml-docgen", //
      "https://github.com/eclipse/capella-xmlpivot", //
      "https://github.com/eclipse/cft", //
      "https://github.com/eclipse/chemclipse", //
      "https://github.com/eclipse/dartboard", //
      "https://github.com/eclipse/dash-licenses", //
      "https://github.com/eclipse/dawnsci", //
      "https://github.com/eclipse/dirigible", //
      "https://github.com/eclipse/eavp", //
      "https://github.com/eclipse/ecf", //
      "https://github.com/eclipse/eclemma", //
      "https://github.com/eclipse/eclipse-collections", //
      "https://github.com/eclipse/eclipse-collections-kata", //
      "https://github.com/eclipse/efbt", //
      "https://github.com/eclipse/elk", //
      "https://github.com/eclipse/emf-query", //
      "https://github.com/eclipse/emf-transaction", //
      "https://github.com/eclipse/emf-validation", //
      "https://github.com/eclipse/emf.egf", //
      "https://github.com/eclipse/epsilon", //
      "https://github.com/eclipse/gef", //
      "https://github.com/eclipse/gef-classic", //
      "https://github.com/eclipse/gemoc-studio", //
      "https://github.com/eclipse/gemoc-studio-commons", //
      "https://github.com/eclipse/gemoc-studio-execution-ale", //
      "https://github.com/eclipse/gemoc-studio-execution-java", //
      "https://github.com/eclipse/gemoc-studio-execution-moccml", //
      "https://github.com/eclipse/gemoc-studio-moccml", //
      "https://github.com/eclipse/gemoc-studio-modeldebugging", //
      "https://github.com/eclipse/gmf-notation", //
      "https://github.com/eclipse/gmf-runtime", //
      "https://github.com/eclipse/gsc-ec-converter", //
      "https://github.com/eclipse/hawkbit", //
      "https://github.com/eclipse/hawkbit-examples", //
      "https://github.com/eclipse/hawkbit-extensions", //
      "https://github.com/eclipse/ice", //
      "https://github.com/eclipse/imagen", //
      "https://github.com/eclipse/january", //
      "https://github.com/eclipse/january-forms", //
      "https://github.com/eclipse/jbom", //
      "https://github.com/eclipse/jifa", //
      "https://github.com/eclipse/jkube", //
      "https://github.com/eclipse/jnosql", //
      "https://github.com/eclipse/jnosql-communication-driver", //
      "https://github.com/eclipse/jnosql-extensions", //
      "https://github.com/eclipse/kapua", //
      "https://github.com/eclipse/keti", //
      "https://github.com/eclipse/keyple-card-calypso-crypto-legacysam-java-lib", //
      "https://github.com/eclipse/keyple-card-calypso-java-lib", //
      "https://github.com/eclipse/keyple-card-generic-java-lib", //
      "https://github.com/eclipse/keyple-common-java-api", //
      "https://github.com/eclipse/keyple-distributed-local-java-api", //
      "https://github.com/eclipse/keyple-distributed-local-java-lib", //
      "https://github.com/eclipse/keyple-distributed-network-java-lib", //
      "https://github.com/eclipse/keyple-distributed-remote-java-api", //
      "https://github.com/eclipse/keyple-distributed-remote-java-lib", //
      "https://github.com/eclipse/keyple-integration-java-test", //
      "https://github.com/eclipse/keyple-java", //
      "https://github.com/eclipse/keyple-java-example", //
      "https://github.com/eclipse/keyple-plugin-cardresource-java-lib", //
      "https://github.com/eclipse/keyple-plugin-java-api", //
      "https://github.com/eclipse/keyple-plugin-pcsc-java-lib", //
      "https://github.com/eclipse/keyple-plugin-stub-java-lib", //
      "https://github.com/eclipse/keyple-service-java-lib", //
      "https://github.com/eclipse/keyple-service-resource-java-lib", //
      "https://github.com/eclipse/keyple-util-java-lib", //
      "https://github.com/eclipse/kitalpha", //
      "https://github.com/eclipse/kitalpha-addons", //
      "https://github.com/eclipse/kuksa.cloud", //
      "https://github.com/eclipse/kura", //
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
      "https://github.com/eclipse/lyo.testsuite", //
      "https://github.com/eclipse/microprofile", //
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
      "https://github.com/eclipse/microprofile-opentracing", //
      "https://github.com/eclipse/microprofile-reactive-messaging", //
      "https://github.com/eclipse/microprofile-reactive-streams-operators", //
      "https://github.com/eclipse/microprofile-rest-client", //
      "https://github.com/eclipse/microprofile-samples", //
      "https://github.com/eclipse/microprofile-sandbox", //
      "https://github.com/eclipse/microprofile-service-mesh-service-a", //
      "https://github.com/eclipse/microprofile-service-mesh-service-b", //
      "https://github.com/eclipse/microprofile-starter", //
      "https://github.com/eclipse/microprofile-telemetry", //
      "https://github.com/eclipse/milo", //
      "https://github.com/eclipse/mita", //
      "https://github.com/eclipse/mosaic", //
      "https://github.com/eclipse/mwe", //
      "https://github.com/eclipse/n4js", //
      "https://github.com/eclipse/nebula", //
      "https://github.com/eclipse/oneofour", //
      "https://github.com/eclipse/openvsx", //
      "https://github.com/eclipse/org.eclipse.emf.diffmerge.coevolution", //
      "https://github.com/eclipse/org.eclipse.emf.diffmerge.core", //
      "https://github.com/eclipse/org.eclipse.emf.diffmerge.patch", //
      "https://github.com/eclipse/org.eclipse.emf.diffmerge.patterns", //
      "https://github.com/eclipse/org.eclipse.rap.incubator.e4.compatibility.workbench", //
      "https://github.com/eclipse/org.eclipse.rcptt", //
      "https://github.com/eclipse/org.eclipse.riena", //
      "https://github.com/eclipse/org.eclipse.riena.old", //
      "https://github.com/eclipse/org.eclipse.riena.plugins.archive", //
      "https://github.com/eclipse/org.eclipse.riena.rap", //
      "https://github.com/eclipse/org.eclipse.riena.toolbox", //
      "https://github.com/eclipse/org.eclipse.sensinact.gateway", //
      "https://github.com/eclipse/org.eclipse.sensinact.studio", //
      "https://github.com/eclipse/packager", //
      "https://github.com/eclipse/paho.mqtt-spy", //
      "https://github.com/eclipse/paho.mqtt.android", //
      "https://github.com/eclipse/paho.mqtt.java", //
      "https://github.com/eclipse/poosl", //
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
      "https://github.com/eclipse/tahu", //
      "https://github.com/eclipse/texlipse", //
      "https://github.com/eclipse/tm4e", //
      "https://github.com/eclipse/transformer", //
      "https://github.com/eclipse/vorto", //
      "https://github.com/eclipse/vorto-examples", //
      "https://github.com/eclipse/winery", //
      "https://github.com/eclipse/xsemantics", //
      "https://github.com/eclipse/xtext", //
      "https://github.com/jakartaee/authentication", //
      "https://github.com/jakartaee/authorization", //
      "https://github.com/jakartaee/cdi", //
      "https://github.com/jakartaee/common-annotations-api", //
      "https://github.com/jakartaee/concurrency", //
      "https://github.com/jakartaee/config", //
      "https://github.com/jakartaee/connectors", //
      "https://github.com/jakartaee/data", //
      "https://github.com/jakartaee/enterprise-beans", //
      "https://github.com/jakartaee/expression-language", //
      "https://github.com/jakartaee/faces", //
      "https://github.com/jakartaee/inject", //
      "https://github.com/jakartaee/interceptors", //
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
      "https://github.com/jakartaee/workshop-participant", //
      "https://github.com/jetty/jetty.alpn.api", //
      "https://github.com/jetty/jetty.build.support", //
      "https://github.com/jetty/jetty.perf.helper", //
      "https://github.com/jetty/jetty.project", //
      "https://github.com/jetty/jetty.schemas.xhtml", //
      "https://github.com/jetty/jetty.setuid", //
      "https://github.com/jetty/jetty.test.helper", //
      "https://github.com/jetty/jetty.test.policy", //
      "https://github.com/jetty/jetty.toolchain", //
      "https://github.com/jkubeio/jkube-integration-tests", //
      "https://github.com/locationtech/geogig", //
      "https://github.com/locationtech/geomesa", //
      "https://github.com/locationtech/geotrellis", //
      "https://github.com/locationtech/geowave", //
      "https://github.com/locationtech/jts", //
      "https://github.com/locationtech/proj4j", //
      "https://github.com/locationtech/spatial4j", //
      "https://github.com/locationtech/udig-platform", //
      "https://github.com/openhwgroup/core-v-ide-cdt", //
      "https://github.com/openhwgroup/core-v-sdk", //
      "https://github.com/osgi/jakartarest-osgi", //
      "https://github.com/osgi/osgi", //
      "https://github.com/osgi/osgi-test", //
      "https://github.com/osgi/osgi.enroute", //
      "https://github.com/osgi/slf4j-osgi", //
      "https://github.com/polarsys/ng661designer", //
      "https://github.com/polarsys/time4sys", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.addon.migration", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.addon.transformation", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.cloud", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.examples", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.releng", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.rtclib", //
      "https://gitlab.eclipse.org/eclipse/app4mc/org.eclipse.app4mc.tools", //
      "https://gitlab.eclipse.org/eclipse/chess/chess", //
      "https://gitlab.eclipse.org/eclipse/comma/comma", //
      "https://gitlab.eclipse.org/eclipse/dco/developer-console", //
      "https://gitlab.eclipse.org/eclipse/ease/ease", //
      "https://gitlab.eclipse.org/eclipse/ease/ease-scripts", //
      "https://gitlab.eclipse.org/eclipse/escet/escet", //
      "https://gitlab.eclipse.org/eclipse/etrice/etrice", //
      "https://gitlab.eclipse.org/eclipse/graphene/common-dataservice", //
      "https://gitlab.eclipse.org/eclipse/graphene/design-studio-backend", //
      "https://gitlab.eclipse.org/eclipse/graphene/federation", //
      "https://gitlab.eclipse.org/eclipse/graphene/kubernetes-client", //
      "https://gitlab.eclipse.org/eclipse/graphene/nexus-client", //
      "https://gitlab.eclipse.org/eclipse/graphene/onboarding", //
      "https://gitlab.eclipse.org/eclipse/graphene/playground-deployer", //
      "https://gitlab.eclipse.org/eclipse/graphene/portal-marketplace", //
      "https://gitlab.eclipse.org/eclipse/graphiti/graphiti", //
      "https://gitlab.eclipse.org/eclipse/hawk/hawk", //
      "https://gitlab.eclipse.org/eclipse/ice/ice", //
      "https://gitlab.eclipse.org/eclipse/lsat/lsat", //
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
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.abstractstatemachine", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.authentication", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.authentication.ui", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.autowireHelper", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.blob", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.bpm", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.bpm.api", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.bpmn2.ecore", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.core.api", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.datainterchange.api", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.display", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.display.api", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.dsl", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.dsl.metadata.service", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.ecview.addons", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.ecview.core", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.ecview.extension", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.ecview.extension.api", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.eventbroker", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.filter", //
      "https://gitlab.eclipse.org/eclipse/osbp/org.eclipse.osbp.fork.mihalis.opal.imageSelector.osgi", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-domainservices", //
      "https://gitlab.eclipse.org/eclipse/papyrus/org.eclipse.papyrus-web", //
      "https://gitlab.eclipse.org/eclipse/statet/statet", //
      "https://gitlab.eclipse.org/eclipse/subversive/subversive", //
      "https://gitlab.eclipse.org/eclipse/sw360/playground-git", //
      "https://gitlab.eclipse.org/eclipse/tcf/tcf", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/dash-maven", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/dashboard", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/eclipse-api-for-java", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/eclipse-project-code", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/m4e-tools", //
      "https://gitlab.eclipse.org/eclipse/technology/dash/org.eclipse.dash.handbook", //
      "https://gitlab.eclipse.org/eclipse/teneo/org.eclipse.emf.teneo", //
      "https://gitlab.eclipse.org/eclipse/titan/titan.language-server", //
      "https://gitlab.eclipse.org/eclipse/trace4cps/trace4cps", //
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
    }
    else
    {
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
          || it.endsWith(".incubator") || it.contains("website") || it.endsWith(".github.io") || it.endsWith("binaries") || it.contains("-ghsa-");
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
