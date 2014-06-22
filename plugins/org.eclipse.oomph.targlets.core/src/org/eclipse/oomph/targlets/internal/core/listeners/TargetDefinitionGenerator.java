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
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.core.TargletEvent;
import org.eclipse.oomph.targlets.core.TargletEvent.ProfileUpdate;
import org.eclipse.oomph.targlets.core.TargletListener;
import org.eclipse.oomph.targlets.internal.core.IUGenerator;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class TargetDefinitionGenerator implements TargletListener
{
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/TargetDefinitionGenerator";

  public static final String ANNOTATION_LOCATION = "location";

  public static final String ANNOTATION_ROOTS = "roots";

  public static final String ANNOTATION_VERSIONS = "versions";

  private static final Pattern SEQUENCE_NUMBER_PATTERN = Pattern.compile("sequenceNumber=\"([0-9]+)\"");

  private static final String TRUE = Boolean.TRUE.toString();

  public TargetDefinitionGenerator()
  {
  }

  public void handleTargletEvent(TargletEvent event) throws Exception
  {
    if (event instanceof ProfileUpdate)
    {
      ProfileUpdate profileUpdate = (ProfileUpdate)event;
      Targlet targlet = profileUpdate.getSource();

      Annotation annotation = targlet.getAnnotation(ANNOTATION);
      if (annotation != null)
      {
        Profile profile = profileUpdate.getProfile();
        List<IMetadataRepository> metadataRepositories = profileUpdate.getMetadataRepositories();
        IProvisioningPlan provisioningPlan = profileUpdate.getProvisioningPlan();

        generateTargetDefinition(targlet, annotation, profile, metadataRepositories, provisioningPlan);
      }
    }
  }

  private void generateTargetDefinition(final Targlet targlet, Annotation annotation, Profile profile, List<IMetadataRepository> metadataRepositories,
      IProvisioningPlan provisioningPlan) throws Exception
  {
    EMap<String, String> details = annotation.getDetails();
    String location = getLocation(details);
    boolean roots = isRoots(details);
    final boolean versions = isVersions(details);

    final Map<String, List<IInstallableUnit>> ius = analyzeProfile(targlet, roots, profile, metadataRepositories, provisioningPlan);

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
        builder.append("<target name=\"" + targlet.getName() + "\" sequenceNumber=\"" + sequenceNumber + "\">");
        builder.append(nl);
        builder.append("  <locations>");
        builder.append(nl);

        for (Map.Entry<String, List<IInstallableUnit>> entry : ius.entrySet())
        {
          List<IInstallableUnit> list = entry.getValue();
          if (!list.isEmpty())
          {
            Collections.sort(list);

            builder.append("    <location includeAllPlatforms=\"" + targlet.isIncludeAllPlatforms()
                + "\" includeConfigurePhase=\"true\" includeMode=\"planner\" includeSource=\"" + targlet.isIncludeSources() + "\" type=\"InstallableUnit\">");
            builder.append(nl);

            Set<String> keys = new HashSet<String>();
            for (IInstallableUnit iu : list)
            {
              String id = iu.getId();
              String key = id;

              String version = "";
              if (versions)
              {
                version = " version=\"" + iu.getVersion() + "\"";
                key += "_" + version;
              }

              if (keys.add(key))
              {
                builder.append("      <unit id=\"" + id + "\"" + version + "/>");
                builder.append(nl);
              }
            }

            builder.append("      <repository location=\"" + entry.getKey() + "\"/>");
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
        contents = contents.replace("sequenceNumber=\"" + sequenceNumber + "\"", "sequenceNumber=\"" + (sequenceNumber + 1) + "\"");
        super.setContents(file, iFile, contents);
      }

    }.update(targetDefinition);
  }

  private String getLocation(EMap<String, String> details) throws IOException
  {
    String location = details.get(ANNOTATION_LOCATION);
    if (StringUtil.isEmpty(location))
    {
      location = File.createTempFile("tmp-", ".target").getAbsolutePath();
      TargletsCorePlugin.INSTANCE.log("Generating target definition to " + location);
    }

    return location;
  }

  private boolean isRoots(EMap<String, String> details)
  {
    String value = details.get(ANNOTATION_ROOTS);
    if (value == null)
    {
      return false;
    }

    return TRUE.equalsIgnoreCase(value);
  }

  private boolean isVersions(EMap<String, String> details)
  {
    String value = details.get(ANNOTATION_VERSIONS);
    if (value == null)
    {
      return false;
    }

    return TRUE.equalsIgnoreCase(value);
  }

  private Map<String, List<IInstallableUnit>> analyzeProfile(Targlet targlet, boolean roots, Profile profile, List<IMetadataRepository> metadataRepositories,
      IProvisioningPlan provisioningPlan)
  {
    Set<IInstallableUnit> requiredIUs = new HashSet<IInstallableUnit>();

    for (IInstallableUnit iu : profile.query(QueryUtil.createIUAnyQuery(), null))
    {
      if (isSourceIU(iu))
      {
        for (IRequirement requirement : iu.getRequirements())
        {
          for (IInstallableUnit requiredIU : profile.query(QueryUtil.createIUAnyQuery(), null))
          {
            if (!isSourceIU(requiredIU) && requirement.isMatch(requiredIU))
            {
              requiredIUs.add(requiredIU);
            }
          }
        }
      }
    }

    if (roots)
    {
      // Remove implicit IUs
      Set<IInstallableUnit> implicitIUs = new HashSet<IInstallableUnit>();
      for (IInstallableUnit requiredIU : requiredIUs)
      {
        analyzeImplicitIUs(profile, requiredIU, implicitIUs);
      }

      requiredIUs.removeAll(implicitIUs);
    }

    Map<String, List<IInstallableUnit>> ius = new LinkedHashMap<String, List<IInstallableUnit>>();
    for (Repository repository : targlet.getActiveRepositories())
    {
      String url = repository.getURL();
      for (IMetadataRepository metadataRepository : metadataRepositories)
      {
        if (metadataRepository.getLocation().toString().equals(url))
        {
          List<IInstallableUnit> list = analyzeRepositoryIUs(metadataRepository, requiredIUs);
          ius.put(url, list);
          break;
        }
      }
    }

    return ius;
  }

  private void analyzeImplicitIUs(Profile profile, IInstallableUnit iu, Set<IInstallableUnit> implicitIUs)
  {
    for (IRequirement requirement : iu.getRequirements())
    {
      for (IInstallableUnit profileIU : profile.query(QueryUtil.createIUAnyQuery(), null))
      {
        if (!isSourceIU(profileIU) && !implicitIUs.contains(profileIU) && requirement.isMatch(profileIU))
        {
          implicitIUs.add(profileIU);
          analyzeImplicitIUs(profile, profileIU, implicitIUs);
        }
      }
    }
  }

  private List<IInstallableUnit> analyzeRepositoryIUs(IMetadataRepository metadataRepository, Set<IInstallableUnit> ius)
  {
    List<IInstallableUnit> list = new ArrayList<IInstallableUnit>();
    for (Iterator<IInstallableUnit> it = ius.iterator(); it.hasNext();)
    {
      IInstallableUnit iu = it.next();
      if (!metadataRepository.query(QueryUtil.createIUQuery(iu), null).isEmpty())
      {
        list.add(iu);
        it.remove();
      }
    }

    return list;
  }

  private boolean isSourceIU(IInstallableUnit iu)
  {
    return TRUE.equalsIgnoreCase(iu.getProperty(IUGenerator.IU_PROPERTY_SOURCE));
  }
}
