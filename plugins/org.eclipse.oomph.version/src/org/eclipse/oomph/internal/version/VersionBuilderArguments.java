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
package org.eclipse.oomph.internal.version;

import org.eclipse.oomph.version.VersionUtil;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class VersionBuilderArguments extends HashMap<String, String> implements IVersionBuilderArguments
{
  private static final long serialVersionUID = 1L;

  public VersionBuilderArguments()
  {
  }

  public VersionBuilderArguments(int initialCapacity, float loadFactor)
  {
    super(initialCapacity, loadFactor);
  }

  public VersionBuilderArguments(int initialCapacity)
  {
    super(initialCapacity);
  }

  public VersionBuilderArguments(Map<? extends String, ? extends String> m)
  {
    super(m);
  }

  public VersionBuilderArguments(IProject project)
  {
    this(getArgumentsFromProject(project));
  }

  public String getReleasePath()
  {
    return get(IVersionBuilderArguments.RELEASE_PATH_ARGUMENT);
  }

  public void setReleasePath(String value)
  {
    setString(IVersionBuilderArguments.RELEASE_PATH_ARGUMENT, value);
  }

  public String getValidatorClassName()
  {
    return get(IVersionBuilderArguments.VALIDATOR_CLASS_ARGUMENT);
  }

  public void setValidatorClassName(String value)
  {
    setString(IVersionBuilderArguments.VALIDATOR_CLASS_ARGUMENT, value);
  }

  public boolean isIgnoreMalformedVersions()
  {
    return "true".equals(get(IVersionBuilderArguments.IGNORE_MALFORMED_VERSIONS_ARGUMENT));
  }

  public void setIgnoreMalformedVersions(boolean value)
  {
    setBoolean(IVersionBuilderArguments.IGNORE_MALFORMED_VERSIONS_ARGUMENT, value);
  }

  public boolean isIgnoreFeatureNature()
  {
    return "true".equals(get(IVersionBuilderArguments.IGNORE_FEATURE_NATURE_ARGUMENT));
  }

  public void setIgnoreFeatureNature(boolean value)
  {
    setBoolean(IVersionBuilderArguments.IGNORE_FEATURE_NATURE_ARGUMENT, value);
  }

  public boolean isIgnoreSchemaBuilder()
  {
    return "true".equals(get(IVersionBuilderArguments.IGNORE_SCHEMA_BUILDER_ARGUMENT));
  }

  public void setIgnoreSchemaBuilder(boolean value)
  {
    setBoolean(IVersionBuilderArguments.IGNORE_SCHEMA_BUILDER_ARGUMENT, value);
  }

  public boolean isIgnoreDebugOptions()
  {
    return "true".equals(get(IVersionBuilderArguments.IGNORE_DEBUG_OPTIONS_ARGUMENT));
  }

  public void setIgnoreDebugOptions(boolean value)
  {
    setBoolean(IVersionBuilderArguments.IGNORE_DEBUG_OPTIONS_ARGUMENT, value);
  }

  public boolean isIgnoreMissingDependencyRanges()
  {
    return "true".equals(get(IVersionBuilderArguments.IGNORE_DEPENDENCY_RANGES_ARGUMENT));
  }

  public void setIgnoreMissingDependencyRanges(boolean value)
  {
    setBoolean(IVersionBuilderArguments.IGNORE_DEPENDENCY_RANGES_ARGUMENT, value);
  }

  public boolean isIgnoreLaxLowerBoundDependencyVersions()
  {
    return "true".equals(get(IVersionBuilderArguments.IGNORE_LAX_LOWER_BOUND_VERSIONS_ARGUMENT));
  }

  public void setIgnoreLaxLowerBoundDependencyVersions(boolean value)
  {
    setBoolean(IVersionBuilderArguments.IGNORE_LAX_LOWER_BOUND_VERSIONS_ARGUMENT, value);
  }

  public boolean isIgnoreMissingExportVersions()
  {
    return "true".equals(get(IVersionBuilderArguments.IGNORE_EXPORT_VERSIONS_ARGUMENT));
  }

  public void setIgnoreMissingExportVersions(boolean value)
  {
    setBoolean(IVersionBuilderArguments.IGNORE_EXPORT_VERSIONS_ARGUMENT, value);
  }

  public boolean isIgnoreFeatureContentChanges()
  {
    return "true".equals(get(IVersionBuilderArguments.IGNORE_CONTENT_CHANGES_ARGUMENT));
  }

  public void setIgnoreFeatureContentChanges(boolean value)
  {
    setBoolean(IVersionBuilderArguments.IGNORE_CONTENT_CHANGES_ARGUMENT, value);
  }

  public boolean isIgnoreFeatureContentRedundancy()
  {
    return "true".equals(get(IVersionBuilderArguments.IGNORE_CONTENT_REDUNDANCY_ARGUMENT));
  }

  public void setIgnoreFeatureContentRedundancy(boolean value)
  {
    setBoolean(IVersionBuilderArguments.IGNORE_CONTENT_REDUNDANCY_ARGUMENT, value);
  }

  public boolean isCheckFeatureClosureCompleteness()
  {
    return "true".equals(get(IVersionBuilderArguments.CHECK_CLOSURE_COMPLETENESS_ARGUMENT));
  }

  public void setCheckFeatureClosureCompleteness(boolean value)
  {
    setBoolean(IVersionBuilderArguments.CHECK_CLOSURE_COMPLETENESS_ARGUMENT, value);
  }

  public boolean isCheckFeatureClosureContent()
  {
    return "true".equals(get(IVersionBuilderArguments.CHECK_CLOSURE_CONTENT_ARGUMENT));
  }

  public void setCheckFeatureClosureContent(boolean value)
  {
    setBoolean(IVersionBuilderArguments.CHECK_CLOSURE_CONTENT_ARGUMENT, value);
  }

  public boolean isCheckMavenPom()
  {
    return "true".equals(get(IVersionBuilderArguments.CHECK_MAVEN_POM_ARGUMENT));
  }

  public void setCheckMavenPom(boolean value)
  {
    setBoolean(IVersionBuilderArguments.CHECK_MAVEN_POM_ARGUMENT, value);
  }

  public void applyTo(IProject project) throws CoreException
  {
    IProjectDescription description = project.getDescription();

    List<String> ids = getOtherNatures(description);
    ids.add(VersionNature.NATURE_ID);
    description.setNatureIds(ids.toArray(new String[ids.size()]));

    List<ICommand> commands = getOtherBuildCommands(description);
    commands.add(createBuildCommand(description));
    description.setBuildSpec(commands.toArray(new ICommand[commands.size()]));

    project.setDescription(description, new NullProgressMonitor());
  }

  private ICommand createBuildCommand(IProjectDescription description)
  {
    ICommand command = description.newCommand();
    command.setBuilderName(VersionUtil.BUILDER_ID);
    command.setArguments(this);
    return command;
  }

  private void setString(String key, String value)
  {
    if (value != null)
    {
      put(key, value);
    }
    else
    {
      remove(key);
    }
  }

  private void setBoolean(String key, boolean value)
  {
    if (value)
    {
      put(key, Boolean.toString(true));
    }
    else
    {
      remove(key);
    }
  }

  public static List<String> getOtherNatures(IProjectDescription description)
  {
    String[] natureIds = description.getNatureIds();
    List<String> ids = new ArrayList<String>(Arrays.asList(natureIds));
    ids.remove(VersionNature.NATURE_ID);
    return ids;
  }

  private static List<ICommand> getOtherBuildCommands(IProjectDescription description)
  {
    ICommand[] buildSpec = description.getBuildSpec();

    List<ICommand> commands = new ArrayList<ICommand>(Arrays.asList(buildSpec));
    for (Iterator<ICommand> it = commands.iterator(); it.hasNext();)
    {
      ICommand command = it.next();
      if (VersionUtil.BUILDER_ID.equals(command.getBuilderName()))
      {
        it.remove();
        break;
      }
    }

    return commands;
  }

  private static Map<String, String> getArgumentsFromProject(IProject project)
  {
    try
    {
      IProjectDescription description = project.getDescription();
      for (ICommand command : description.getBuildSpec())
      {
        if (VersionUtil.BUILDER_ID.equals(command.getBuilderName()))
        {
          return command.getArguments();
        }
      }
    }
    catch (CoreException ex)
    {
      Activator.log(ex);
    }

    return new HashMap<String, String>();
  }
}
