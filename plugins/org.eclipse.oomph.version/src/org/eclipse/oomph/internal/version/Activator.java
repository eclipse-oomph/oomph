/*
 * Copyright (c) 2014, 2015, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.version;

import org.eclipse.oomph.util.IOUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;

import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class Activator extends Plugin
{
  public static final String PLUGIN_ID = "org.eclipse.oomph.version";

  private static final String BUILD_STATES = "buildStates.bin";

  private static Activator plugin;

  private static IResourceChangeListener postBuildListener;

  private static BuildStates buildStates;

  public Activator()
  {
  }

  @Override
  public void start(BundleContext context) throws Exception
  {
    super.start(context);
    plugin = this;

    try
    {
      buildStates = load(BUILD_STATES);
    }
    catch (Throwable t)
    {
      //$FALL-THROUGH$
    }
    finally
    {
      File stateFile = getStateFile(BUILD_STATES);
      if (stateFile.exists())
      {
        stateFile.delete(); // Future indication for possible workspace crash
      }

      if (buildStates == null)
      {
        buildStates = new BuildStates();
      }
    }
  }

  @Override
  public void stop(BundleContext context) throws Exception
  {
    if (postBuildListener != null)
    {
      ResourcesPlugin.getWorkspace().removeResourceChangeListener(postBuildListener);
      postBuildListener = null;
    }

    if (!buildStates.isEmpty())
    {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      for (Iterator<Entry<String, BuildState>> it = buildStates.entrySet().iterator(); it.hasNext();)
      {
        Entry<String, BuildState> entry = it.next();
        String projectName = entry.getKey();
        IProject project = root.getProject(projectName);
        if (!project.exists())
        {
          it.remove();
        }
        else
        {
          BuildState buildState = entry.getValue();
          buildState.serializeValidatorState();
        }
      }

      save(BUILD_STATES, buildStates);
    }

    buildStates = null;
    plugin = null;
    super.stop(context);
  }

  public static void setPostBuildListener(IResourceChangeListener postBuildListener)
  {
    Activator.postBuildListener = postBuildListener;
  }

  private static final String PREFERENCE_NODE = "/instance/" + PLUGIN_ID;

  public static Set<String> getReleasePaths()
  {
    Preferences root = Platform.getPreferencesService().getRootNode();
    try
    {
      if (root.nodeExists(PREFERENCE_NODE))
      {
        Preferences node = root.node(PREFERENCE_NODE);
        return new HashSet<String>(Arrays.asList(node.keys()));
      }
    }
    catch (BackingStoreException ex)
    {
      Activator.log(ex);
    }

    return Collections.emptySet();
  }

  public static ReleaseCheckMode getReleaseCheckMode(String releasePath)
  {
    Preferences root = Platform.getPreferencesService().getRootNode();
    try
    {
      if (root.nodeExists(PREFERENCE_NODE))
      {
        Preferences node = root.node(PREFERENCE_NODE);
        String value = node.get(releasePath, null);
        if (value != null)
        {
          return ReleaseCheckMode.valueOf(value);
        }
      }
    }
    catch (BackingStoreException ex)
    {
      Activator.log(ex);
    }

    return null;
  }

  public static void setReleaseCheckMode(String releasePath, ReleaseCheckMode releaseCheckMode)
  {
    Preferences root = Platform.getPreferencesService().getRootNode();
    Preferences node = root.node(PREFERENCE_NODE);
    node.put(releasePath, releaseCheckMode.toString());
    try
    {
      node.flush();
    }
    catch (BackingStoreException ex)
    {
      log(ex);
    }
  }

  public static LaxLowerBoundCheckMode getLaxLowerBoundCheckMode(String releasePath)
  {
    Preferences root = Platform.getPreferencesService().getRootNode();
    try
    {
      if (root.nodeExists(PREFERENCE_NODE))
      {
        Preferences node = root.node(PREFERENCE_NODE + "/laxLowerBound");
        String value = node.get(releasePath, null);
        if (value != null)
        {
          return LaxLowerBoundCheckMode.valueOf(value);
        }
      }
    }
    catch (BackingStoreException ex)
    {
      Activator.log(ex);
    }

    return null;
  }

  public static void setLaxLowerBoundCheckMode(String releasePath, LaxLowerBoundCheckMode laxLowerBoundCheckMode)
  {
    Preferences root = Platform.getPreferencesService().getRootNode();
    Preferences node = root.node(PREFERENCE_NODE + "/laxLowerBound");
    node.put(releasePath, laxLowerBoundCheckMode.toString());
    try
    {
      node.flush();
    }
    catch (BackingStoreException ex)
    {
      log(ex);
    }
  }

  public static BuildState getBuildState(IProject project)
  {
    String name = project.getName();
    BuildState buildState = buildStates.get(name);
    if (buildState == null)
    {
      buildState = new BuildState();
      buildStates.put(name, buildState);
    }

    return buildState;
  }

  public static void clearBuildState(IProject project)
  {
    String name = project.getName();
    buildStates.remove(name);
  }

  public static void log(String message)
  {
    plugin.getLog().log(new Status(IStatus.INFO, PLUGIN_ID, message));
  }

  public static void log(IStatus status)
  {
    plugin.getLog().log(status);
  }

  public static void log(Throwable t, int severity)
  {
    log(getStatus(t, severity));
  }

  public static String log(Throwable t)
  {
    IStatus status = getStatus(t);
    log(status);
    return status.getMessage();
  }

  public static IStatus getStatus(Throwable t)
  {
    if (t instanceof CoreException)
    {
      CoreException coreException = (CoreException)t;
      return coreException.getStatus();
    }

    String msg = t.getLocalizedMessage();
    if (msg == null || msg.length() == 0)
    {
      msg = t.getClass().getName();
    }

    return new Status(IStatus.ERROR, PLUGIN_ID, msg, t);
  }

  public static IStatus getStatus(Throwable t, int severity)
  {
    String msg = t.getLocalizedMessage();
    if (msg == null || msg.length() == 0)
    {
      msg = t.getClass().getName();
    }

    return new Status(severity, PLUGIN_ID, msg, t);
  }

  private static File getStateFile(String name)
  {
    File stateFolder = Platform.getStateLocation(plugin.getBundle()).toFile();
    return new File(stateFolder, name);
  }

  private static <T> T load(String fileName) throws IOException, ClassNotFoundException
  {
    ObjectInputStream stream = null;

    try
    {
      File stateFile = getStateFile(fileName);
      stream = new ObjectInputStream(new FileInputStream(stateFile));

      @SuppressWarnings("unchecked")
      T object = (T)stream.readObject();
      return object;
    }
    finally
    {
      IOUtil.closeSilent(stream);
    }
  }

  private static void save(String fileName, Object object)
  {
    ObjectOutputStream stream = null;

    try
    {
      File file = getStateFile(fileName);
      stream = new ObjectOutputStream(new FileOutputStream(file));
      stream.writeObject(object);
    }
    catch (Throwable ex)
    {
      log(ex);
    }
    finally
    {
      IOUtil.closeSilent(stream);
    }
  }

  /**
   * @author Eike Stepper
   */
  public enum ReleaseCheckMode
  {
    NONE, PARTIAL, FULL
  }

  /**
   * @author Ed Merks
   */
  public enum LaxLowerBoundCheckMode
  {
    SAME_RELEASE, ANY_RELEASE, ALL
  }

  /**
   * @author Eike Stepper
   */
  private static final class BuildStates extends HashMap<String, BuildState>
  {
    private static final long serialVersionUID = 2L;

    public BuildStates()
    {
    }
  }
}
