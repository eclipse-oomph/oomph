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
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.core.TargletEvent;
import org.eclipse.oomph.targlets.core.TargletEvent.ProfileUpdate;
import org.eclipse.oomph.targlets.core.TargletListener;
import org.eclipse.oomph.targlets.internal.core.IUGenerator;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class TargetDefinitionGenerator implements TargletListener
{
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/TargetDefinitionGenerator";

  public static final String ANNOTATION_LOCATION = "location";

  public static final String ANNOTATION_COMPATIBILITY = "compatibility";

  public static final String ANNOTATION_ROOTS = "roots";

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

  private void generateTargetDefinition(Targlet targlet, Annotation annotation, Profile profile, List<IMetadataRepository> metadataRepositories,
      IProvisioningPlan provisioningPlan) throws Exception
  {
    EMap<String, String> details = annotation.getDetails();
    String location = getLocation(details);
    VersionSegment versionSegment = getVersionSegment(details);
    boolean roots = isRoots(details);

    Map<String, List<IInstallableUnit>> ius = analyzeProfile(targlet, roots, profile, metadataRepositories, provisioningPlan);

    File targetDefinition = new File(location);
    IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(targetDefinition.toURI());

    for (IFile file : files)
    {
      IProject project = file.getProject();
      if (project.isOpen())
      {
        InputStream contents = generateContents(targlet, ius, versionSegment, project);

        if (file.exists())
        {
          file.setContents(contents, true, true, new NullProgressMonitor());
        }
        else
        {
          file.create(contents, true, new NullProgressMonitor());
        }

        return;
      }
    }

    InputStream contents = generateContents(targlet, ius, versionSegment, null);
    OutputStream stream = new FileOutputStream(targetDefinition);

    try
    {
      IOUtil.copy(contents, stream);
    }
    finally
    {
      IOUtil.close(stream);
    }
  }

  private InputStream generateContents(Targlet targlet, Map<String, List<IInstallableUnit>> ius, VersionSegment versionSegment, IProject project)
      throws IOException
  {
    String nl = getLineSeparator(project);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Writer writer = new OutputStreamWriter(baos, "UTF-8");

    try
    {
      writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
      writer.write(nl);
      writer.write("<?pde version=\"3.8\"?>");
      writer.write(nl);
      writer.write("<target name=\"" + targlet.getName() + "\" sequenceNumber=\"1\">");
      writer.write(nl);
      writer.write("  <locations>");
      writer.write(nl);

      for (Map.Entry<String, List<IInstallableUnit>> entry : ius.entrySet())
      {
        List<IInstallableUnit> list = entry.getValue();
        if (!list.isEmpty())
        {
          Collections.sort(list);

          writer.write("    <location includeAllPlatforms=\"" + targlet.isIncludeAllPlatforms()
              + "\" includeConfigurePhase=\"true\" includeMode=\"planner\" includeSource=\"" + targlet.isIncludeSources() + "\" type=\"InstallableUnit\">");
          writer.write(nl);

          Set<String> keys = new HashSet<String>();
          for (IInstallableUnit iu : list)
          {
            String id = iu.getId();
            Version version = iu.getVersion();

            VersionRange versionRange = P2Factory.eINSTANCE.createVersionRange(version, versionSegment);
            version = versionRange.getMinimum();

            String key = id + "_" + version;
            if (keys.add(key))
            {
              writer.write("      <unit id=\"" + id + "\" version=\"" + version + "\"/>");
              writer.write(nl);
            }
          }

          writer.write("      <repository location=\"" + entry.getKey() + "\"/>");
          writer.write(nl);
          writer.write("    </location>");
          writer.write(nl);
        }
      }

      writer.write("  </locations>");
      writer.write(nl);
      writer.write("</target>");
      writer.write(nl);
    }
    finally
    {
      IOUtil.close(writer);
    }

    return new ByteArrayInputStream(baos.toByteArray());
  }

  private String getLineSeparator(IProject project)
  {
    try
    {
      Preferences node = Platform.getPreferencesService().getRootNode().node(ProjectScope.SCOPE).node(project.getName());
      if (node.nodeExists(Platform.PI_RUNTIME))
      {
        String value = node.node(Platform.PI_RUNTIME).get(Platform.PREF_LINE_SEPARATOR, null);
        if (value != null)
        {
          return value;
        }
      }
    }
    catch (BackingStoreException e)
    {
      // Ignore
    }

    return PropertiesUtil.getProperty(Platform.PREF_LINE_SEPARATOR);
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

  private VersionSegment getVersionSegment(EMap<String, String> details)
  {
    String compatibility = details.get(ANNOTATION_COMPATIBILITY);
    VersionSegment versionSegment = null;

    try
    {
      versionSegment = VersionSegment.getByName(compatibility.toUpperCase());
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    if (versionSegment == null)
    {
      versionSegment = VersionSegment.MICRO;
    }

    return versionSegment;
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
