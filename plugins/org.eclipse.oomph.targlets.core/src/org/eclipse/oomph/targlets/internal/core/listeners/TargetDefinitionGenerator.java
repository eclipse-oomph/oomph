/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core.listeners;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.internal.core.ProfileTransactionImpl;
import org.eclipse.oomph.p2.internal.core.RootAnalyzer;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.ProfileUpdateSucceededEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.WorkspaceUpdateFinishedEvent;
import org.eclipse.oomph.targlets.core.WorkspaceIUInfo;
import org.eclipse.oomph.targlets.internal.core.TargletContainer;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.targlets.internal.core.WorkspaceIUAnalyzer;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIConverter.WriteableOutputStream;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLOptions;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLOptionsImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.internal.p2.repository.Credentials;
import org.eclipse.equinox.internal.p2.repository.Credentials.LoginCanceledException;
import org.eclipse.equinox.p2.core.UIServices.AuthenticationInfo;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IVersionedId;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionedId;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class TargetDefinitionGenerator extends WorkspaceUpdateListener
{
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/TargetDefinitionGenerator"; //$NON-NLS-1$

  public static final String ANNOTATION_NAME = "name"; //$NON-NLS-1$

  public static final String ANNOTATION_LOCATION = "location"; //$NON-NLS-1$

  public static final String ANNOTATION_PREFERRED_REPOSITORIES = "preferredRepositories"; //$NON-NLS-1$

  public static final String ANNOTATION_GENERATE_IMPLICIT_UNITS = "generateImplicitUnits"; //$NON-NLS-1$

  public static final String ANNOTATION_MINIMIZE_IMPLICIT_UNITS = "minimizeImplicitUnits"; //$NON-NLS-1$

  public static final String ANNOTATION_GENERATE_VERSIONS = "generateVersions"; //$NON-NLS-1$

  public static final String ANNOTATION_INCLUDE_ALL_PLATFORMS = "includeAllPlatforms"; //$NON-NLS-1$

  public static final String ANNOTATION_INCLUDE_CONFIGURE_PHASE = "includeConfigurePhase"; //$NON-NLS-1$

  public static final String ANNOTATION_INCLUDE_MODE = "includeMode"; //$NON-NLS-1$

  public static final String ANNOTATION_INCLUDE_SOURCE = "includeSource"; //$NON-NLS-1$

  public static final String ANNOTATION_EXTRA_UNITS = "extraUnits"; //$NON-NLS-1$

  public static final String ANNOTATION_SINGLE_LOCATION = "singleLocation"; //$NON-NLS-1$

  public static final String ANNOTATION_SORT_LOCATIONS = "sortLocations"; //$NON-NLS-1$

  public static final String ANNOTATION_GENERATE_SERVER_XML = "generateServerXML"; //$NON-NLS-1$

  public static final String NESTED_ANNOTATION_REPOSITORY_IDS = "RepositoryIDs"; //$NON-NLS-1$

  private static final Pattern SEQUENCE_NUMBER_PATTERN = Pattern.compile("sequenceNumber=\"([0-9]+)\""); //$NON-NLS-1$

  private static final String TRUE = Boolean.TRUE.toString();

  private static final String SETTINGS_NAMESPACE = "http://maven.apache.org/SETTINGS/1.0.0"; //$NON-NLS-1$

  public TargetDefinitionGenerator()
  {
  }

  @Override
  protected void handleTargletContainerEvent(ProfileUpdateSucceededEvent profileUpdateSucceededEvent, WorkspaceUpdateFinishedEvent workspaceUpdateFinishedEvent,
      IProgressMonitor monitor) throws Exception
  {
    ITargletContainer targletContainer = profileUpdateSucceededEvent.getSource();
    for (Targlet targlet : targletContainer.getTarglets())
    {
      for (Annotation annotation : BaseUtil.getAnnotations(targlet, ANNOTATION))
      {
        Profile profile = profileUpdateSucceededEvent.getProfile();
        IInstallableUnit artificialRoot = profileUpdateSucceededEvent.getArtificialRoot();
        List<IMetadataRepository> metadataRepositories = profileUpdateSucceededEvent.getMetadataRepositories();
        Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = profileUpdateSucceededEvent.getWorkspaceIUInfos();

        generateTargetDefinition(targlet, annotation, profile, artificialRoot, metadataRepositories, workspaceIUInfos, monitor);
      }
    }
  }

  private static void generateTargetDefinition(final Targlet targlet, Annotation annotation, Profile profile, IInstallableUnit artificialRoot,
      final List<IMetadataRepository> metadataRepositories, Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos, final IProgressMonitor monitor)
      throws Exception
  {
    monitor.setTaskName(Messages.TargetDefinitionGenerator_CheckingUpdates_task);
    EMap<String, String> details = annotation.getDetails();

    String targletName = details.get(ANNOTATION_NAME);
    final String name = StringUtil.isEmpty(targletName) ? NLS.bind(Messages.TargetDefinitionGenerator_GeneratedFrom_message, targlet.getName()) : targletName;

    String location = details.get(ANNOTATION_LOCATION);
    if (StringUtil.isEmpty(location))
    {
      location = File.createTempFile("tmp-", ".target").getAbsolutePath(); //$NON-NLS-1$ //$NON-NLS-2$
      TargletsCorePlugin.INSTANCE.log(NLS.bind(Messages.TargetDefinitionGenerator_Generating_message, targlet.getName(), location));
    }

    File targetDefinition = new File(location);

    final Set<IVersionedId> extraUnits = getExtraUnits(annotation);
    final List<String> preferredURLs = getPreferredRepositories(annotation);
    final boolean singleLocation = isAnnotationDetail(annotation, ANNOTATION_SINGLE_LOCATION, false);
    final boolean sortLocations = isAnnotationDetail(annotation, ANNOTATION_SORT_LOCATIONS, false);
    final boolean generateImplicitUnits = isAnnotationDetail(annotation, ANNOTATION_GENERATE_IMPLICIT_UNITS, false);
    final boolean minimizeImplicitUnits = isAnnotationDetail(annotation, ANNOTATION_MINIMIZE_IMPLICIT_UNITS, false);
    final boolean versions = isAnnotationDetail(annotation, ANNOTATION_GENERATE_VERSIONS, false);
    final boolean includeAllPlatforms = isAnnotationDetail(annotation, ANNOTATION_INCLUDE_ALL_PLATFORMS, targlet.isIncludeAllPlatforms());
    final boolean includeConfigurePhase = isAnnotationDetail(annotation, ANNOTATION_INCLUDE_CONFIGURE_PHASE, true);
    final String includeMode = getAnnotationDetail(annotation, ANNOTATION_INCLUDE_MODE, targlet.isIncludeAllRequirements() ? "planner" : "slicer"); //$NON-NLS-1$ //$NON-NLS-2$
    final boolean includeSource = isAnnotationDetail(annotation, ANNOTATION_INCLUDE_SOURCE, targlet.isIncludeSources());
    final boolean generateServerXML = isAnnotationDetail(annotation, ANNOTATION_GENERATE_SERVER_XML, false);

    final Map<String, String> repositoryIDs = new LinkedHashMap<String, String>();
    Annotation repositoryIDsAnnotation = annotation.getAnnotation(NESTED_ANNOTATION_REPOSITORY_IDS);
    if (repositoryIDsAnnotation != null)
    {
      for (Map.Entry<String, String> entry : repositoryIDsAnnotation.getDetails().entrySet())
      {
        repositoryIDs.put(entry.getKey(), entry.getValue());
      }
    }

    final Map<IMetadataRepository, Set<IInstallableUnit>> repositoryIUs = analyzeRepositories(targlet, profile, artificialRoot, metadataRepositories,
        workspaceIUInfos, extraUnits, preferredURLs, generateImplicitUnits, minimizeImplicitUnits, singleLocation, sortLocations, monitor);

    new FileUpdater()
    {
      private int sequenceNumber;

      @Override
      protected String createNewContents(String oldContents, String encoding, String nl)
      {
        if (oldContents != null)
        {
          Matcher matcher = SEQUENCE_NUMBER_PATTERN.matcher(oldContents);
          if (matcher.find())
          {
            sequenceNumber = Integer.parseInt(matcher.group(1));
          }
        }

        XML.Escaper escaper = new XML.Escaper(encoding);

        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"" + encoding + "\" standalone=\"no\"?>"); //$NON-NLS-1$ //$NON-NLS-2$
        builder.append(nl);
        builder.append("<?pde version=\"3.8\"?>"); //$NON-NLS-1$
        builder.append(nl);
        builder.append("<target name=\"" + escaper.escape(name) + "\" sequenceNumber=\"" + sequenceNumber + "\">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        builder.append(nl);
        builder.append("  <locations>"); //$NON-NLS-1$
        builder.append(nl);

        if (singleLocation)
        {
          builder.append("    <location includeAllPlatforms=\"" + includeAllPlatforms + "\" includeConfigurePhase=\"" + includeConfigurePhase //$NON-NLS-1$ //$NON-NLS-2$
              + "\" includeMode=\"" + includeMode + "\" includeSource=\"" + includeSource + "\" type=\"InstallableUnit\">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          builder.append(nl);

          // Only one set will be non-empty, but with sorting it might not be the first one.
          for (Set<IInstallableUnit> set : repositoryIUs.values())
          {
            if (!set.isEmpty())
            {
              List<IInstallableUnit> list = new ArrayList<IInstallableUnit>(set);
              Collection<String> elements = new LinkedHashSet<String>();
              Collections.sort(list);

              for (IInstallableUnit iu : list)
              {
                elements.add(formatElement(iu, versions, escaper));
              }

              for (String element : elements)
              {
                builder.append("      "); //$NON-NLS-1$
                builder.append(element);
                builder.append(nl);
              }

              break;
            }
          }

          for (IMetadataRepository repository : repositoryIUs.keySet())
          {
            builder.append("      <repository "); //$NON-NLS-1$

            java.net.URI repositoryLocation = repository.getLocation();
            String repositoryID = repositoryIDs.get(repositoryLocation.toString());
            if (repositoryID != null)
            {

              builder.append("id=\"").append(repositoryID).append("\" "); //$NON-NLS-1$ //$NON-NLS-2$
            }

            builder.append("location=\"" + escaper.escape(repositoryLocation) + "\"/>"); //$NON-NLS-1$ //$NON-NLS-2$
            builder.append(nl);
          }

          builder.append("    </location>"); //$NON-NLS-1$
          builder.append(nl);
        }
        else
        {
          for (Map.Entry<IMetadataRepository, Set<IInstallableUnit>> entry : repositoryIUs.entrySet())
          {
            IMetadataRepository repository = entry.getKey();
            Set<IInstallableUnit> set = entry.getValue();

            List<IInstallableUnit> list = new ArrayList<IInstallableUnit>(set);
            if (!list.isEmpty())
            {
              builder.append("    <location includeAllPlatforms=\"" + includeAllPlatforms + "\" includeConfigurePhase=\"" + includeConfigurePhase //$NON-NLS-1$ //$NON-NLS-2$
                  + "\" includeMode=\"" + includeMode + "\" includeSource=\"" + includeSource + "\" type=\"InstallableUnit\">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
              builder.append(nl);

              Collection<String> elements = new LinkedHashSet<String>();
              Collections.sort(list);

              for (IInstallableUnit iu : list)
              {
                elements.add(formatElement(iu, versions, escaper));
              }

              for (String element : elements)
              {
                builder.append("      "); //$NON-NLS-1$
                builder.append(element);
                builder.append(nl);
              }

              builder.append("      <repository "); //$NON-NLS-1$

              java.net.URI repositoryLocation = repository.getLocation();
              String repositoryID = repositoryIDs.get(repositoryLocation.toString());
              if (repositoryID != null)
              {

                builder.append("id=\"").append(repositoryID).append("\" "); //$NON-NLS-1$ //$NON-NLS-2$
              }

              builder.append("location=\"" + escaper.escape(repositoryLocation) + "\"/>"); //$NON-NLS-1$ //$NON-NLS-2$
              builder.append(nl);
              builder.append("    </location>"); //$NON-NLS-1$
              builder.append(nl);
            }
          }
        }

        builder.append("  </locations>"); //$NON-NLS-1$
        builder.append(nl);
        builder.append("</target>"); //$NON-NLS-1$
        builder.append(nl);

        return builder.toString();
      }

      @Override
      protected void setContents(URI uri, String encoding, String contents) throws IOException
      {
        monitor.subTask(NLS.bind(Messages.TargetDefinitionGenerator_Updating_task, uri.isPlatformResource() ? uri.toPlatformString(true) : uri.toFileString()));
        contents = contents.replace("sequenceNumber=\"" + sequenceNumber + "\"", "sequenceNumber=\"" + (sequenceNumber + 1) + "\""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        super.setContents(uri, encoding, contents);
      }

      private String formatElement(IInstallableUnit iu, boolean withVersion, XML.Escaper escaper)
      {
        Version version = iu.getVersion();
        if (!withVersion || version == null)
        {
          version = Version.emptyVersion;
        }

        return "<unit id=\"" + escaper.escape(iu.getId()) + "\" version=\"" + escaper.escape(version) + "\"/>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }

    }.update(targetDefinition);

    if (generateServerXML)
    {
      new FileUpdater()
      {
        @Override
        protected String createNewContents(String oldContents, String encoding, String nl)
        {
          ResourceSet resourceSet = new ResourceSetImpl();
          final ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(resourceSet.getPackageRegistry());
          resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new ResourceFactoryImpl() //$NON-NLS-1$
          {
            @Override
            public Resource createResource(URI uri)
            {
              XMLResource result = new XMLResourceImpl(uri);
              result.setEncoding("UTF-8"); //$NON-NLS-1$

              result.getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
              result.getDefaultLoadOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);

              result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);

              result.getDefaultSaveOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
              result.getDefaultLoadOptions().put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);

              result.getDefaultSaveOptions().put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);

              XMLOptions xmlOptions = new XMLOptionsImpl();

              xmlOptions.setProcessAnyXML(true);

              xmlOptions.setProcessSchemaLocations(false);

              result.getDefaultLoadOptions().put(XMLResource.OPTION_XML_OPTIONS, xmlOptions);
              return result;
            }
          });

          Resource resource = resourceSet.createResource(URI.createURI("settings.xml")); //$NON-NLS-1$
          if (!StringUtil.isEmpty(oldContents))
          {
            try
            {
              resource.load(new URIConverter.ReadableInputStream(oldContents), null);
            }
            catch (IOException ex)
            {
              // Ignore.
            }
          }

          if (resource.getContents().isEmpty())
          {
            EStructuralFeature settingsElement = extendedMetaData.demandFeature(SETTINGS_NAMESPACE, "settings", true); //$NON-NLS-1$
            EClass documentRootEClass = settingsElement.getEContainingClass();
            EObject documentRoot = EcoreUtil.create(documentRootEClass);
            AnyType settings = XMLTypeFactory.eINSTANCE.createAnyType();
            documentRoot.eSet(settingsElement, settings);

            @SuppressWarnings("unchecked")
            EMap<String, String> xmlnsPrefixMap = (EMap<String, String>)documentRoot.eGet(extendedMetaData.getXMLNSPrefixMapFeature(documentRootEClass));
            xmlnsPrefixMap.put("", SETTINGS_NAMESPACE); //$NON-NLS-1$
            xmlnsPrefixMap.put("xsi", XMLResource.XSI_URI); //$NON-NLS-1$

            EStructuralFeature schemaLocation = extendedMetaData.demandFeature(XMLResource.XSI_URI, "schemaLocation", false); //$NON-NLS-1$
            settings.eSet(schemaLocation, SETTINGS_NAMESPACE + " http://maven.apache.org/xsd/settings-1.0.0.xsd"); //$NON-NLS-1$

            resource.getContents().add(documentRoot);
          }

          EObject documentRoot = resource.getContents().get(0);
          AnyType rootElement = (AnyType)documentRoot.eContents().get(0);

          EStructuralFeature serversElement = extendedMetaData.demandFeature(SETTINGS_NAMESPACE, "servers", true); //$NON-NLS-1$

          @SuppressWarnings("unchecked")
          List<AnyType> serversGroup = (List<AnyType>)rootElement.eGet(serversElement);
          FeatureMap servers;
          if (serversGroup.isEmpty())
          {
            FeatureMapUtil.addText(rootElement.getMixed(), nl + "  "); //$NON-NLS-1$
            AnyType serversInstance = XMLTypeFactory.eINSTANCE.createAnyType();
            serversGroup.add(serversInstance);
            FeatureMapUtil.addText(rootElement.getMixed(), nl);
            servers = serversInstance.getMixed();
          }
          else
          {
            servers = serversGroup.get(0).getMixed();
          }

          EStructuralFeature serverElement = extendedMetaData.demandFeature(SETTINGS_NAMESPACE, "server", true); //$NON-NLS-1$
          EStructuralFeature idElement = extendedMetaData.demandFeature(SETTINGS_NAMESPACE, "id", true); //$NON-NLS-1$
          EStructuralFeature usernameElement = extendedMetaData.demandFeature(SETTINGS_NAMESPACE, "username", true); //$NON-NLS-1$
          EStructuralFeature passwordElement = extendedMetaData.demandFeature(SETTINGS_NAMESPACE, "password", true); //$NON-NLS-1$

          boolean added = false;
          LOOP: for (IMetadataRepository repository : metadataRepositories)
          {
            java.net.URI location = repository.getLocation();
            try
            {
              String repositoryID = repositoryIDs.get(location.toString());
              if (repositoryID == null)
              {
                repositoryID = location.toString();
              }

              AuthenticationInfo authenticationInfo = Credentials.forLocation(location, false);
              if (authenticationInfo != null)
              {
                @SuppressWarnings("unchecked")
                List<AnyType> serverElements = (List<AnyType>)servers.get(serverElement, true);

                for (AnyType server : serverElements)
                {
                  FeatureMap mixed = server.getMixed();

                  @SuppressWarnings("unchecked")
                  List<AnyType> idElements = (List<AnyType>)mixed.get(idElement, true);
                  if (idElements.size() == 1)
                  {
                    AnyType id = idElements.get(0);
                    if (repositoryID.equals(id.getMixed().getValue(0)))
                    {
                      @SuppressWarnings("unchecked")
                      List<AnyType> userNameElements = (List<AnyType>)mixed.get(usernameElement, true);
                      if (userNameElements.size() == 1)
                      {
                        userNameElements.get(0).getMixed().setValue(0, authenticationInfo.getUserName());
                      }

                      @SuppressWarnings("unchecked")
                      List<AnyType> passwordElements = (List<AnyType>)mixed.get(passwordElement, true);
                      if (passwordElements.size() == 1)
                      {
                        passwordElements.get(0).getMixed().setValue(0, authenticationInfo.getPassword());
                      }

                      continue LOOP;
                    }
                  }
                }

                added = true;

                AnyType server = XMLTypeFactory.eINSTANCE.createAnyType();
                FeatureMapUtil.addText(servers, nl + "    "); //$NON-NLS-1$
                servers.add(FeatureMapUtil.createEntry(serverElement, server));

                FeatureMap mixed = server.getMixed();

                AnyType id = XMLTypeFactory.eINSTANCE.createAnyType();
                FeatureMapUtil.addText(id.getMixed(), repositoryID);
                FeatureMapUtil.addText(mixed, nl + "      "); //$NON-NLS-1$
                mixed.add(FeatureMapUtil.createEntry(idElement, id));

                AnyType username = XMLTypeFactory.eINSTANCE.createAnyType();
                FeatureMapUtil.addText(username.getMixed(), authenticationInfo.getUserName());
                FeatureMapUtil.addText(mixed, nl + "      "); //$NON-NLS-1$
                mixed.add(FeatureMapUtil.createEntry(usernameElement, username));

                AnyType password = XMLTypeFactory.eINSTANCE.createAnyType();
                FeatureMapUtil.addText(password.getMixed(), authenticationInfo.getPassword());
                FeatureMapUtil.addText(mixed, nl + "      "); //$NON-NLS-1$
                mixed.add(FeatureMapUtil.createEntry(passwordElement, password));
                FeatureMapUtil.addText(mixed, nl + "    "); //$NON-NLS-1$
              }
            }
            catch (LoginCanceledException ex)
            {
              // Not possible because we're not prompting.
            }
            catch (CoreException ex)
            {
              // Ignore.
            }
          }

          if (added)
          {
            FeatureMapUtil.addText(servers, nl + "  "); //$NON-NLS-1$
          }

          try
          {
            StringWriter writer = new StringWriter();
            WriteableOutputStream out = new URIConverter.WriteableOutputStream(writer, encoding);
            resource.save(out, null);
            out.close();
            writer.write(nl);
            writer.close();
            return writer.toString();
          }
          catch (IOException ex)
          {
            throw new IORuntimeException(ex);
          }
        }

        @Override
        protected void setContents(URI uri, String encoding, String contents) throws IOException
        {
          super.setContents(uri, encoding, contents);
        }

      }.update(new File(PropertiesUtil.getUserHome(), ".m2/settings.xml")); //$NON-NLS-1$
    }
  }

  private static boolean isAnnotationDetail(Annotation annotation, String key, boolean defaultValue)
  {
    String detail = getAnnotationDetail(annotation, key, Boolean.toString(defaultValue));
    return TRUE.equalsIgnoreCase(detail);
  }

  private static String getAnnotationDetail(Annotation annotation, String key, String defaultValue)
  {
    String value = annotation.getDetails().get(key);
    if (value == null)
    {
      return defaultValue;
    }

    return value;
  }

  private static Set<IVersionedId> getExtraUnits(Annotation annotation)
  {
    Set<IVersionedId> extraUnits = new HashSet<IVersionedId>();

    String values = annotation.getDetails().get(ANNOTATION_EXTRA_UNITS);
    if (!StringUtil.isEmpty(values))
    {
      for (String value : values.replace(',', ' ').split(" ")) //$NON-NLS-1$
      {
        if (!StringUtil.isEmpty(value))
        {
          int pos = value.lastIndexOf('_');
          String id = pos == -1 ? value : value.substring(0, pos);
          Version version = pos == -1 ? Version.emptyVersion : Version.create(value.substring(pos + 1));
          extraUnits.add(new VersionedId(id, version));
        }
      }
    }

    return extraUnits;
  }

  private static List<String> getPreferredRepositories(Annotation annotation)
  {
    List<String> preferredRepositories = new ArrayList<String>();

    String values = annotation.getDetails().get(ANNOTATION_PREFERRED_REPOSITORIES);
    if (!StringUtil.isEmpty(values))
    {
      for (String value : values.replace(',', ' ').split(" ")) //$NON-NLS-1$
      {
        if (!StringUtil.isEmpty(value))
        {
          preferredRepositories.add(value);
        }
      }
    }

    return preferredRepositories;
  }

  private static Map<IMetadataRepository, Set<IInstallableUnit>> analyzeRepositories(Targlet targlet, Profile profile, IInstallableUnit artificialRoot,
      List<IMetadataRepository> metadataRepositories, Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos, Set<IVersionedId> extraUnits,
      List<String> preferredURLs, boolean generateImplicitUnits, boolean minimizeImplicitUnits, boolean singleLocation, boolean sortLocations,
      IProgressMonitor monitor)
  {

    Set<String> workspaceIDs = new HashSet<String>();
    for (IInstallableUnit iu : workspaceIUInfos.keySet())
    {
      workspaceIDs.add(iu.getId());
    }

    Set<IInstallableUnit> profileIUs = new HashSet<IInstallableUnit>();
    for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUAnyQuery(), monitor)))
    {
      String id = iu.getId();
      if (id.endsWith(".source") || id.endsWith(".source.feature.group") || id.endsWith(".source.feature.feature.group") || id.endsWith(".feature.jar") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
          || ProfileTransactionImpl.SOURCE_IU_ID.equals(id) || id.equals("a.jre")) //$NON-NLS-1$
      {
        continue;
      }

      if ("true".equals(iu.getProperty(TargletContainer.IU_PROPERTY_SOURCE))) //$NON-NLS-1$
      {
        continue;
      }

      if ("true".equals(iu.getProperty(WorkspaceIUAnalyzer.IU_PROPERTY_WORKSPACE))) //$NON-NLS-1$
      {
        continue;
      }

      if (workspaceIDs.contains(id))
      {
        continue;
      }

      profileIUs.add(iu);
    }

    Map<String, IMetadataRepository> queryables = sortMetadataRepositories(targlet, metadataRepositories, preferredURLs, monitor);

    return assignUnits(queryables, extraUnits, generateImplicitUnits, minimizeImplicitUnits, singleLocation, sortLocations, profileIUs, monitor);
  }

  private static Map<String, IMetadataRepository> sortMetadataRepositories(Targlet targlet, List<IMetadataRepository> metadataRepositories,
      List<String> preferredURLs, IProgressMonitor monitor)
  {
    Map<String, IMetadataRepository> queryables = new LinkedHashMap<String, IMetadataRepository>();
    for (String urlPrefix : preferredURLs)
    {
      for (IMetadataRepository metadataRepository : metadataRepositories)
      {
        String url = metadataRepository.getLocation().toString();
        if (!queryables.containsKey(url))
        {
          if (url.startsWith(urlPrefix))
          {
            queryables.put(url, metadataRepository);
          }
        }
      }
    }

    for (Repository repository : targlet.getActiveRepositories())
    {
      TargletsCorePlugin.checkCancelation(monitor);
      String url = repository.getURL();

      if (!queryables.containsKey(url))
      {
        IMetadataRepository metadataRepository = getMetadataRepository(url, metadataRepositories);
        if (metadataRepository != null)
        {
          queryables.put(url, metadataRepository);
        }
      }
    }

    for (IMetadataRepository metadataRepository : metadataRepositories)
    {
      String url = metadataRepository.getLocation().toString();
      if (!queryables.containsKey(url))
      {
        queryables.put(url, metadataRepository);
      }
    }

    return queryables;
  }

  private static IMetadataRepository getMetadataRepository(String url, List<IMetadataRepository> metadataRepositories)
  {
    for (IMetadataRepository metadataRepository : metadataRepositories)
    {
      if (metadataRepository.getLocation().toString().equals(url))
      {
        return metadataRepository;
      }
    }

    return null;
  }

  private static Map<IMetadataRepository, Set<IInstallableUnit>> assignUnits(Map<String, IMetadataRepository> queryables, Set<IVersionedId> extraUnits,
      boolean generateImplicitUnits, boolean minimizeImplicitUnits, boolean singleLocation, boolean sortLocations, Set<IInstallableUnit> resolvedIUs,
      IProgressMonitor monitor)
  {
    Map<IMetadataRepository, Set<IInstallableUnit>> result = sortLocations
        ? new TreeMap<IMetadataRepository, Set<IInstallableUnit>>(new Comparator<IMetadataRepository>()
        {
          public int compare(IMetadataRepository o1, IMetadataRepository o2)
          {
            return o1.getLocation().compareTo(o2.getLocation());
          }
        })
        : new LinkedHashMap<IMetadataRepository, Set<IInstallableUnit>>();

    if (singleLocation)
    {
      IQueryable<IInstallableUnit> queryable = QueryUtil.compoundQueryable(queryables.values());
      boolean first = true;

      // Use keySet() to preserve iteration order!
      for (String url : queryables.keySet())
      {
        IMetadataRepository metadataRepository = queryables.get(url);
        Set<IInstallableUnit> ius = CollectionUtil.getSet(result, metadataRepository);

        if (first)
        {
          ius.addAll(resolvedIUs);

          if (!generateImplicitUnits)
          {
            RootAnalyzer.removeImplicitUnits(ius, queryable, monitor, false);
          }
          else if (minimizeImplicitUnits)
          {
            RootAnalyzer.removeImplicitUnits(ius, queryable, monitor, true);
          }

          for (Iterator<IVersionedId> it = extraUnits.iterator(); it.hasNext();)
          {
            IVersionedId extraUnit = it.next();
            for (IInstallableUnit extraIU : P2Util.asIterable(queryable.query(QueryUtil.createIUQuery(extraUnit), null)))
            {
              ius.add(extraIU);
              break;
            }
          }

          first = false;
        }
      }
    }
    else
    {
      for (IInstallableUnit iu : resolvedIUs)
      {
        for (IMetadataRepository metadataRepository : queryables.values())
        {
          if (!metadataRepository.query(QueryUtil.createIUQuery(iu), null).isEmpty())
          {
            CollectionUtil.add(result, metadataRepository, iu);
            break;
          }
        }
      }

      if (!generateImplicitUnits)
      {
        RootAnalyzer.removeImplicitUnits(result, monitor, false);
      }
      else if (minimizeImplicitUnits)
      {
        RootAnalyzer.removeImplicitUnits(result, monitor, true);
      }

      // Use keySet() to preserve iteration order!
      for (String url : queryables.keySet())
      {
        IMetadataRepository metadataRepository = queryables.get(url);
        Set<IInstallableUnit> ius = CollectionUtil.getSet(result, metadataRepository);

        for (Iterator<IVersionedId> it = extraUnits.iterator(); it.hasNext();)
        {
          IVersionedId extraUnit = it.next();
          for (IInstallableUnit extraIU : P2Util.asIterable(metadataRepository.query(QueryUtil.createIUQuery(extraUnit), null)))
          {
            ius.add(extraIU);
            it.remove(); // TODO Why is it removed? Should we log left-overs?
            break;
          }
        }
      }
    }

    return result;
  }

  /**
   * @author Eike Stepper
   */
  private static final class XML extends XMLSaveImpl
  {
    private XML(XMLHelper helper)
    {
      super(helper);
    }

    /**
     * @author Eike Stepper
     */
    public static final class Escaper extends Escape
    {
      private static final int MAX_UTF_MAPPABLE_CODEPOINT = 0x10FFFF;

      private static final int MAX_LATIN1_MAPPABLE_CODEPOINT = 0xFF;

      private static final int MAX_ASCII_MAPPABLE_CODEPOINT = 0x7F;

      public Escaper(String encoding)
      {
        int maxSafeChar = MAX_UTF_MAPPABLE_CODEPOINT;
        if (encoding != null)
        {
          if (encoding.equalsIgnoreCase("ASCII") || encoding.equalsIgnoreCase("US-ASCII")) //$NON-NLS-1$ //$NON-NLS-2$
          {
            maxSafeChar = MAX_ASCII_MAPPABLE_CODEPOINT;
          }
          else if (encoding.equalsIgnoreCase("ISO-8859-1")) //$NON-NLS-1$
          {
            maxSafeChar = MAX_LATIN1_MAPPABLE_CODEPOINT;
          }
        }

        setMappingLimit(maxSafeChar);
      }

      public String escape(Object value)
      {
        return convert(String.valueOf(value));
      }
    }
  }
}
