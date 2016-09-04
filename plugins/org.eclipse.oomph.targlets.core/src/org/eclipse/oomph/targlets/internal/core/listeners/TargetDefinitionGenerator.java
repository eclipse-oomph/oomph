/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core.listeners;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.internal.core.RootAnalyzer;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.ProfileUpdateSucceededEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.WorkspaceUpdateFinishedEvent;
import org.eclipse.oomph.targlets.core.WorkspaceIUInfo;
import org.eclipse.oomph.targlets.internal.core.TargletContainer;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.IVersionedId;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionedId;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class TargetDefinitionGenerator extends WorkspaceUpdateListener
{
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/TargetDefinitionGenerator";

  public static final String ANNOTATION_NAME = "name";

  public static final String ANNOTATION_LOCATION = "location";

  public static final String ANNOTATION_PREFERRED_REPOSITORIES = "preferredRepositories";

  public static final String ANNOTATION_GENERATE_IMPLICIT_UNITS = "generateImplicitUnits";

  public static final String ANNOTATION_GENERATE_VERSIONS = "generateVersions";

  public static final String ANNOTATION_INCLUDE_ALL_PLATFORMS = "includeAllPlatforms";

  public static final String ANNOTATION_INCLUDE_CONFIGURE_PHASE = "includeConfigurePhase";

  public static final String ANNOTATION_INCLUDE_MODE = "includeMode";

  public static final String ANNOTATION_INCLUDE_SOURCE = "includeSource";

  public static final String ANNOTATION_EXTRA_UNITS = "extraUnits";

  public static final String ANNOTATION_SINGLE_LOCATION = "singleLocation";

  private static final Pattern SEQUENCE_NUMBER_PATTERN = Pattern.compile("sequenceNumber=\"([0-9]+)\"");

  private static final String TRUE = Boolean.TRUE.toString();

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
      Annotation annotation = targlet.getAnnotation(ANNOTATION);
      if (annotation != null)
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
      List<IMetadataRepository> metadataRepositories, Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos, final IProgressMonitor monitor) throws Exception
  {
    monitor.setTaskName("Checking for generated target definition updates");
    EMap<String, String> details = annotation.getDetails();

    String targletName = details.get(ANNOTATION_NAME);
    final String name = StringUtil.isEmpty(targletName) ? "Generated from " + targlet.getName() : targletName;

    String location = details.get(ANNOTATION_LOCATION);
    if (StringUtil.isEmpty(location))
    {
      location = File.createTempFile("tmp-", ".target").getAbsolutePath();
      TargletsCorePlugin.INSTANCE.log("Generating target definition for targlet " + targlet.getName() + " to " + location);
    }

    File targetDefinition = new File(location);

    final Set<IVersionedId> extraUnits = getExtraUnits(annotation);
    final List<String> preferredURLs = getPreferredRepositories(annotation);
    final boolean singleLocation = isAnnotationDetail(annotation, ANNOTATION_SINGLE_LOCATION, false);
    final boolean generateImplicitUnits = isAnnotationDetail(annotation, ANNOTATION_GENERATE_IMPLICIT_UNITS, false);
    final boolean versions = isAnnotationDetail(annotation, ANNOTATION_GENERATE_VERSIONS, false);
    final boolean includeAllPlatforms = isAnnotationDetail(annotation, ANNOTATION_INCLUDE_ALL_PLATFORMS, targlet.isIncludeAllPlatforms());
    final boolean includeConfigurePhase = isAnnotationDetail(annotation, ANNOTATION_INCLUDE_CONFIGURE_PHASE, true);
    final String includeMode = getAnnotationDetail(annotation, ANNOTATION_INCLUDE_MODE, targlet.isIncludeAllRequirements() ? "planner" : "slicer");
    final boolean includeSource = isAnnotationDetail(annotation, ANNOTATION_INCLUDE_SOURCE, targlet.isIncludeSources());

    final Map<IMetadataRepository, Set<IInstallableUnit>> repositoryIUs = analyzeRepositories(targlet, profile, artificialRoot, metadataRepositories,
        workspaceIUInfos, extraUnits, preferredURLs, generateImplicitUnits, singleLocation, monitor);

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
        builder.append("<?xml version=\"1.0\" encoding=\"" + encoding + "\" standalone=\"no\"?>");
        builder.append(nl);
        builder.append("<?pde version=\"3.8\"?>");
        builder.append(nl);
        builder.append("<target name=\"" + escaper.escape(name) + "\" sequenceNumber=\"" + sequenceNumber + "\">");
        builder.append(nl);
        builder.append("  <locations>");
        builder.append(nl);

        if (singleLocation)
        {
          builder.append("    <location includeAllPlatforms=\"" + includeAllPlatforms + "\" includeConfigurePhase=\"" + includeConfigurePhase
              + "\" includeMode=\"" + includeMode + "\" includeSource=\"" + includeSource + "\" type=\"InstallableUnit\">");
          builder.append(nl);

          boolean first = true;

          for (Map.Entry<IMetadataRepository, Set<IInstallableUnit>> entry : repositoryIUs.entrySet())
          {
            IMetadataRepository repository = entry.getKey();
            Set<IInstallableUnit> set = entry.getValue();

            if (first)
            {
              List<IInstallableUnit> list = new ArrayList<IInstallableUnit>(set);
              if (!list.isEmpty())
              {
                Collection<String> elements = new LinkedHashSet<String>();
                Collections.sort(list);

                for (IInstallableUnit iu : list)
                {
                  elements.add(formatElement(iu, versions, escaper));
                }

                for (String element : elements)
                {
                  builder.append("      ");
                  builder.append(element);
                  builder.append(nl);
                }

                first = false;
              }
            }

            builder.append("      <repository location=\"" + escaper.escape(repository.getLocation()) + "\"/>");
            builder.append(nl);
          }

          builder.append("    </location>");
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
              builder.append("    <location includeAllPlatforms=\"" + includeAllPlatforms + "\" includeConfigurePhase=\"" + includeConfigurePhase
                  + "\" includeMode=\"" + includeMode + "\" includeSource=\"" + includeSource + "\" type=\"InstallableUnit\">");
              builder.append(nl);

              Collection<String> elements = new LinkedHashSet<String>();
              Collections.sort(list);

              for (IInstallableUnit iu : list)
              {
                elements.add(formatElement(iu, versions, escaper));
              }

              for (String element : elements)
              {
                builder.append("      ");
                builder.append(element);
                builder.append(nl);
              }

              builder.append("      <repository location=\"" + escaper.escape(repository.getLocation()) + "\"/>");
              builder.append(nl);
              builder.append("    </location>");
              builder.append(nl);
            }
          }
        }

        builder.append("  </locations>");
        builder.append(nl);
        builder.append("</target>");
        builder.append(nl);

        return builder.toString();
      }

      @Override
      protected void setContents(URI uri, String encoding, String contents) throws IOException
      {
        monitor.subTask("Updating " + (uri.isPlatformResource() ? uri.toPlatformString(true) : uri.toFileString()));
        contents = contents.replace("sequenceNumber=\"" + sequenceNumber + "\"", "sequenceNumber=\"" + (sequenceNumber + 1) + "\"");
        super.setContents(uri, encoding, contents);
      }

      private String formatElement(IInstallableUnit iu, boolean withVersion, XML.Escaper escaper)
      {
        Version version = iu.getVersion();
        if (!withVersion || version == null)
        {
          version = Version.emptyVersion;
        }

        return "<unit id=\"" + escaper.escape(iu.getId()) + "\" version=\"" + escaper.escape(version) + "\"/>";
      }

    }.update(targetDefinition);
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
      for (String value : values.replace(',', ' ').split(" "))
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
      for (String value : values.replace(',', ' ').split(" "))
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
      List<String> preferredURLs, boolean generateImplicitUnits, boolean singleLocation, IProgressMonitor monitor)
  {
    Set<IRequiredCapability> rootRequirements = getRootRequirements(targlet, artificialRoot, workspaceIUInfos);

    Set<String> workspaceIDs = new HashSet<String>();
    for (IInstallableUnit iu : workspaceIUInfos.keySet())
    {
      workspaceIDs.add(iu.getId());
    }

    Set<IInstallableUnit> resolvedIUs = new HashSet<IInstallableUnit>();
    for (IRequiredCapability requirement : rootRequirements)
    {
      resolveRequirement(requirement, profile, workspaceIDs, resolvedIUs, new HashSet<IRequiredCapability>());
    }

    Map<String, IMetadataRepository> queryables = sortMetadataRepositories(targlet, metadataRepositories, preferredURLs, monitor);

    return assignUnits(queryables, extraUnits, generateImplicitUnits, singleLocation, resolvedIUs, monitor);
  }

  private static Set<IRequiredCapability> getRootRequirements(Targlet targlet, IInstallableUnit artificialRoot,
      Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos)
  {
    Map<String, Set<IRequiredCapability>> profileRequirements = new HashMap<String, Set<IRequiredCapability>>();
    for (IRequirement profileRequirement : artificialRoot.getRequirements())
    {
      if (profileRequirement instanceof IRequiredCapability)
      {
        IRequiredCapability requiredCapability = (IRequiredCapability)profileRequirement;
        String id = requiredCapability.getNamespace() + "/" + requiredCapability.getName();
        CollectionUtil.add(profileRequirements, id, requiredCapability);
      }
    }

    Set<IRequiredCapability> queue = new HashSet<IRequiredCapability>();
    for (Requirement targletRequirement : targlet.getRequirements())
    {
      String namespace = targletRequirement.getNamespace();
      String name = targletRequirement.getName();

      // If this is a wildcard requirement, we want to expand it to all the IUs in the targlet's source locator.
      if ("*".equals(name) && IInstallableUnit.NAMESPACE_IU_ID.equals(namespace))
      {
        for (IInstallableUnit iu : workspaceIUInfos.keySet())
        {
          for (IRequirement workspaceRequirement : iu.getRequirements())
          {
            if (workspaceRequirement instanceof IRequiredCapability)
            {
              IRequiredCapability requiredCapability = (IRequiredCapability)workspaceRequirement;
              queue.add(requiredCapability);
            }
          }
        }
      }
      else
      {
        String id = namespace + "/" + name;
        Set<IRequiredCapability> set = profileRequirements.get(id);
        if (set != null)
        {
          queue.addAll(set);
        }
      }
    }

    return queue;
  }

  private static void resolveRequirement(IRequiredCapability requirement, IQueryable<IInstallableUnit> queryable, Set<String> workspaceIDs,
      Set<IInstallableUnit> result, Set<IRequiredCapability> visited)
  {
    if (visited.add(requirement))
    {
      for (IInstallableUnit iu : queryable.query(QueryUtil.createMatchQuery(requirement.getMatches()), null))
      {
        String id = iu.getId();
        if (id.endsWith(".source") || id.endsWith(".source.feature.group"))
        {
          continue;
        }

        if ("true".equals(iu.getProperty(TargletContainer.IU_PROPERTY_SOURCE)))
        {
          continue;
        }

        // This check is not ideal because it does not consider versions.
        // But the binary IUs induced by the workspace IUs may have any version, just depending on the involved repositories.
        // So for now, just exclude the IDs of all workspace IUs.
        if (workspaceIDs.contains(id))
        {
          continue;
        }

        if (result.add(iu))
        {
          for (IRequirement iuRequirement : iu.getRequirements())
          {
            if (iuRequirement instanceof IRequiredCapability)
            {
              resolveRequirement((IRequiredCapability)iuRequirement, queryable, workspaceIDs, result, visited);
            }
          }
        }
      }
    }
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
      boolean generateImplicitUnits, boolean singleLocation, Set<IInstallableUnit> resolvedIUs, IProgressMonitor monitor)
  {
    Map<IMetadataRepository, Set<IInstallableUnit>> result = new LinkedHashMap<IMetadataRepository, Set<IInstallableUnit>>();

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
          for (Iterator<IVersionedId> it = extraUnits.iterator(); it.hasNext();)
          {
            IVersionedId extraUnit = it.next();
            for (IInstallableUnit extraIU : queryable.query(QueryUtil.createIUQuery(extraUnit), null))
            {
              ius.add(extraIU);
              break;
            }
          }

          ius.addAll(resolvedIUs);

          if (!generateImplicitUnits)
          {
            RootAnalyzer.removeImplicitUnits(ius, queryable, monitor);
          }

          first = false;
        }
      }
    }
    else
    {
      // Use keySet() to preserve iteration order!
      for (String url : queryables.keySet())
      {
        IMetadataRepository metadataRepository = queryables.get(url);
        Set<IInstallableUnit> ius = CollectionUtil.getSet(result, metadataRepository);

        for (Iterator<IVersionedId> it = extraUnits.iterator(); it.hasNext();)
        {
          IVersionedId extraUnit = it.next();
          for (IInstallableUnit extraIU : metadataRepository.query(QueryUtil.createIUQuery(extraUnit), null))
          {
            ius.add(extraIU);
            it.remove(); // TODO Why is it removed? Should we log left-overs?
            break;
          }
        }
      }

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
        RootAnalyzer.removeImplicitUnits(result, monitor);
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
          if (encoding.equalsIgnoreCase("ASCII") || encoding.equalsIgnoreCase("US-ASCII"))
          {
            maxSafeChar = MAX_ASCII_MAPPABLE_CODEPOINT;
          }
          else if (encoding.equalsIgnoreCase("ISO-8859-1"))
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
