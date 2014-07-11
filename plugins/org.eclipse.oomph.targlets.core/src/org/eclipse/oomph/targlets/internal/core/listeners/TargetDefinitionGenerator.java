/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.core.TargletContainerEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.TargletContainerUpdated;
import org.eclipse.oomph.targlets.core.TargletContainerListener;
import org.eclipse.oomph.targlets.internal.core.IUGenerator;
import org.eclipse.oomph.targlets.internal.core.TargletContainer;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
public class TargetDefinitionGenerator implements TargletContainerListener
{
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/TargetDefinitionGenerator";

  public static final String ANNOTATION_NAME = "name";

  public static final String ANNOTATION_LOCATION = "location";

  public static final String ANNOTATION_GENERATE_IMPLICIT_UNITS = "generateImplicitUnits";

  public static final String ANNOTATION_GENERATE_VERSIONS = "generateVersions";

  public static final String ANNOTATION_INCLUDE_ALL_PLATFORMS = "includeAllPlatforms";

  public static final String ANNOTATION_INCLUDE_CONFIGURE_PHASE = "includeConfigurePhase";

  public static final String ANNOTATION_INCLUDE_MODE = "includeMode";

  public static final String ANNOTATION_INCLUDE_SOURCE = "includeSource";

  public static final String ANNOTATION_EXTRA_UNITS = "extraUnits";

  private static final Pattern SEQUENCE_NUMBER_PATTERN = Pattern.compile("sequenceNumber=\"([0-9]+)\"");

  private static final String TRUE = Boolean.TRUE.toString();

  public TargetDefinitionGenerator()
  {
  }

  public void handleTargletEvent(TargletContainerEvent event, IProgressMonitor monitor) throws Exception
  {
    if (event instanceof TargletContainerUpdated)
    {
      TargletContainerUpdated targletContainerUpdated = (TargletContainerUpdated)event;
      TargletContainer targletContainer = targletContainerUpdated.getSource();
      for (Targlet targlet : targletContainer.getTarglets())
      {
        Annotation annotation = targlet.getAnnotation(ANNOTATION);
        if (annotation != null)
        {
          EMap<String, String> details = annotation.getDetails();

          String name = details.get(ANNOTATION_NAME);
          if (StringUtil.isEmpty(name))
          {
            name = "Generated from " + targlet.getName();
          }

          String location = details.get(ANNOTATION_LOCATION);
          if (StringUtil.isEmpty(location))
          {
            location = File.createTempFile("tmp-", ".target").getAbsolutePath();
            TargletsCorePlugin.INSTANCE.log("Generating target definition for targlet " + targlet.getName() + " to " + location);
          }

          Profile profile = targletContainerUpdated.getProfile();
          List<IMetadataRepository> metadataRepositories = targletContainerUpdated.getMetadataRepositories();

          generateTargetDefinition(targlet, name, location, profile, metadataRepositories, monitor);
        }
      }
    }
  }

  private static void generateTargetDefinition(final Targlet targlet, final String name, String location, Profile profile,
      List<IMetadataRepository> metadataRepositories, final IProgressMonitor monitor) throws Exception
  {
    monitor.setTaskName("Checking for generated target definition updates");
    final Map<Repository, Set<IU>> repositoryIUs = analyzeRepositories(targlet, profile, metadataRepositories, monitor);

    File targetDefinition = new File(location);
    new FileUpdater()
    {
      private int sequenceNumber;

      @Override
      protected String createNewContents(String oldContents, String nl)
      {
        Matcher matcher = SEQUENCE_NUMBER_PATTERN.matcher(oldContents);
        if (matcher.find())
        {
          sequenceNumber = Integer.parseInt(matcher.group(1));
        }

        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
        builder.append(nl);
        builder.append("<?pde version=\"3.8\"?>");
        builder.append(nl);
        builder.append("<target name=\"" + name + "\" sequenceNumber=\"" + sequenceNumber + "\">");
        builder.append(nl);
        builder.append("  <locations>");
        builder.append(nl);

        for (Map.Entry<Repository, Set<IU>> entry : repositoryIUs.entrySet())
        {
          Repository repository = entry.getKey();
          Set<IU> set = entry.getValue();

          Set<IU> extraUnits = getExtraUnits(repository);
          set.addAll(extraUnits);

          List<IU> list = new ArrayList<IU>(set);
          if (!list.isEmpty())
          {
            boolean versions = isAnnotationDetail(repository, ANNOTATION_GENERATE_VERSIONS, false);
            boolean includeAllPlatforms = isAnnotationDetail(repository, ANNOTATION_INCLUDE_ALL_PLATFORMS, targlet.isIncludeAllPlatforms());
            boolean includeConfigurePhase = isAnnotationDetail(repository, ANNOTATION_INCLUDE_CONFIGURE_PHASE, true);
            String includeMode = getAnnotationDetail(repository, ANNOTATION_INCLUDE_MODE, "planner");
            boolean includeSource = isAnnotationDetail(repository, ANNOTATION_INCLUDE_SOURCE, targlet.isIncludeSources());

            builder.append("    <location includeAllPlatforms=\"" + includeAllPlatforms + "\" includeConfigurePhase=\"" + includeConfigurePhase
                + "\" includeMode=\"" + includeMode + "\" includeSource=\"" + includeSource + "\" type=\"InstallableUnit\">");
            builder.append(nl);

            Collection<String> elements = new LinkedHashSet<String>();
            Collections.sort(list);
            for (IU iu : list)
            {
              elements.add(iu.formatElement(versions));
            }

            for (String element : elements)
            {
              builder.append("      ");
              builder.append(element);
              builder.append(nl);
            }

            builder.append("      <repository location=\"" + repository.getURL() + "\"/>");
            builder.append(nl);
            builder.append("    </location>");
            builder.append(nl);
          }
        }

        builder.append("  </locations>");
        builder.append(nl);
        builder.append("</target>");
        builder.append(nl);

        return builder.toString();
      }

      @Override
      protected void setContents(File file, IFile iFile, String contents) throws Exception
      {
        monitor.subTask("Updating " + (iFile != null ? iFile.getFullPath() : file));
        contents = contents.replace("sequenceNumber=\"" + sequenceNumber + "\"", "sequenceNumber=\"" + (sequenceNumber + 1) + "\"");
        super.setContents(file, iFile, contents);
      }

    }.update(targetDefinition);
  }

  private static String getAnnotationDetail(ModelElement element, String annotationDetailKey, String defaultValue)
  {
    for (Annotation annotation : element.getAnnotations())
    {
      if (ANNOTATION.equals(annotation.getSource()))
      {
        String value = annotation.getDetails().get(annotationDetailKey);
        if (value != null)
        {
          return value;
        }
      }
    }

    if (element instanceof Targlet)
    {
      return defaultValue;
    }

    EObject container = element.eContainer();
    if (container instanceof ModelElement)
    {
      return getAnnotationDetail((ModelElement)container, annotationDetailKey, defaultValue);
    }

    return defaultValue;
  }

  private static boolean isAnnotationDetail(ModelElement element, String annotationDetailKey, boolean defaultValue)
  {
    String detail = getAnnotationDetail(element, annotationDetailKey, Boolean.toString(defaultValue));
    return TRUE.equalsIgnoreCase(detail);
  }

  private static Set<IU> getExtraUnits(Repository repository)
  {
    Set<IU> extraUnits = new HashSet<IU>();
    for (Annotation annotation : repository.getAnnotations())
    {
      if (ANNOTATION.equals(annotation.getSource()))
      {
        String values = annotation.getDetails().get(ANNOTATION_EXTRA_UNITS);
        if (!StringUtil.isEmpty(values))
        {
          for (String value : values.split(" "))
          {
            if (!StringUtil.isEmpty(value))
            {
              int pos = value.lastIndexOf('_');
              String id = pos == -1 ? value : value.substring(0, pos);
              Version version = pos == -1 ? Version.emptyVersion : Version.create(value.substring(pos + 1));
              extraUnits.add(new ExtraIU(id, version));
            }
          }
        }
      }
    }

    return extraUnits;
  }

  private static Map<Repository, Set<IU>> analyzeRepositories(Targlet targlet, Profile profile, List<IMetadataRepository> metadataRepositories,
      IProgressMonitor monitor)
  {
    Map<Repository, Set<IU>> result = new LinkedHashMap<Repository, Set<IU>>();
    Map<IInstallableUnit, IU> ius = new HashMap<IInstallableUnit, IU>();
    boolean generateImplicitUnits = isAnnotationDetail(targlet, ANNOTATION_GENERATE_IMPLICIT_UNITS, false);

    for (Repository repository : targlet.getActiveRepositories())
    {
      TargletsCorePlugin.checkCancelation(monitor);
      Set<IU> resultIUs = new HashSet<IU>();

      IMetadataRepository metadataRepository = getMetadataRepository(repository.getURL(), metadataRepositories);
      for (IInstallableUnit repositoryIU : metadataRepository.query(QueryUtil.createIUAnyQuery(), null))
      {
        TargletsCorePlugin.checkCancelation(monitor);

        IInstallableUnit installableUnit = getInstallableUnitFromProfile(repositoryIU, profile);
        if (installableUnit != null && !installableUnit.getId().endsWith(".source"))
        {
          IU iu = getOrCreateIU(ius, installableUnit);
          resultIUs.add(iu);
        }
      }

      if (!generateImplicitUnits)
      {
        Set<IU> rootIUs = new HashSet<IU>(resultIUs);
        for (IU resultIU : resultIUs)
        {
          TargletsCorePlugin.checkCancelation(monitor);

          IInstallableUnit delegate = resultIU.getDelegate();
          if (delegate != null)
          {
            Set<IU> requiredIUs = resultIU.getRequiredIUs();
            if (requiredIUs == null)
            {
              requiredIUs = new HashSet<IU>();
              resultIU.setRequiredIUs(requiredIUs);

              for (IRequirement requirement : delegate.getRequirements())
              {
                TargletsCorePlugin.checkCancelation(monitor);

                for (IInstallableUnit installableUnit : profile.query(QueryUtil.createMatchQuery(requirement.getMatches()), null))
                {
                  TargletsCorePlugin.checkCancelation(monitor);

                  IU requiredIU = getOrCreateIU(ius, installableUnit);
                  requiredIU.setRequired(true);
                  requiredIUs.add(requiredIU);
                }
              }
            }

            rootIUs.removeAll(requiredIUs);
          }
        }

        result.put(repository, rootIUs);
      }
      else
      {
        result.put(repository, resultIUs);
      }
    }

    if (!generateImplicitUnits)
    {
      for (Set<IU> resultIUs : result.values())
      {
        TargletsCorePlugin.checkCancelation(monitor);

        List<IU> list = new ArrayList<IU>(resultIUs);
        Collections.sort(list);

        for (IU iu : list)
        {
          TargletsCorePlugin.checkCancelation(monitor);

          if (resultIUs.size() <= 1)
          {
            break;
          }

          if (iu.isRequired())
          {
            resultIUs.remove(iu);
          }
        }
      }
    }

    return result;
  }

  private static IU getOrCreateIU(Map<IInstallableUnit, IU> ius, IInstallableUnit installableUnit)
  {
    IU iu = ius.get(installableUnit);
    if (iu == null)
    {
      iu = new ProfileIU(installableUnit);
      ius.put(installableUnit, iu);
    }

    return iu;
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

  private static IInstallableUnit getInstallableUnitFromProfile(IInstallableUnit repositoryIU, Profile profile)
  {
    for (IInstallableUnit profileIU : profile.query(QueryUtil.createIUQuery(repositoryIU), null))
    {
      if (!TRUE.equalsIgnoreCase(profileIU.getProperty(IUGenerator.IU_PROPERTY_SOURCE)))
      {
        return profileIU;
      }
    }

    return null;
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class IU implements Comparable<IU>
  {
    private Set<IU> requiredIUs;

    private boolean required;

    public IU()
    {
    }

    public abstract String getID();

    public abstract Version getVersion();

    public abstract IInstallableUnit getDelegate();

    public final Set<IU> getRequiredIUs()
    {
      return requiredIUs;
    }

    public final void setRequiredIUs(Set<IU> requiredIUs)
    {
      this.requiredIUs = requiredIUs;
    }

    public final boolean isRequired()
    {
      return required;
    }

    public final void setRequired(boolean required)
    {
      this.required = required;
    }

    @Override
    public final int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + getID().hashCode();

      Version version = getVersion();
      result = prime * result + version.hashCode();
      return result;
    }

    @Override
    public final boolean equals(Object obj)
    {
      if (this == obj)
      {
        return true;
      }

      if (obj == null)
      {
        return false;
      }

      if (!(obj instanceof IU))
      {
        return false;
      }

      IU other = (IU)obj;
      if (!getID().equals(other.getID()))
      {
        return false;
      }

      if (!getVersion().equals(other.getVersion()))
      {
        return false;
      }

      return true;
    }

    public final int compareTo(IU o)
    {
      int result = getID().compareTo(o.getID());
      if (result == 0)
      {
        result = getVersion().compareTo(o.getVersion());
      }

      return result;
    }

    @Override
    public final String toString()
    {
      return getID() + "_" + getVersion();
    }

    public final String formatElement(boolean withVersion)
    {
      Version version = getVersion();
      if (!withVersion || version == null)
      {
        version = Version.emptyVersion;
      }

      return "<unit id=\"" + getID() + "\" version=\"" + version + "\"/>";
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class ProfileIU extends IU
  {
    private final IInstallableUnit delegate;

    public ProfileIU(IInstallableUnit delegate)
    {
      this.delegate = delegate;
    }

    @Override
    public String getID()
    {
      return delegate.getId();
    }

    @Override
    public Version getVersion()
    {
      return delegate.getVersion();
    }

    @Override
    public IInstallableUnit getDelegate()
    {
      return delegate;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class ExtraIU extends IU
  {
    private final String id;

    private final Version version;

    public ExtraIU(String id, Version version)
    {
      this.id = id;
      this.version = version;
    }

    @Override
    public String getID()
    {
      return id;
    }

    @Override
    public Version getVersion()
    {
      return version;
    }

    @Override
    public IInstallableUnit getDelegate()
    {
      return null;
    }
  }
}
