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
package org.eclipse.oomph.version.tests;

import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.tests.AbstractTest;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OomphPlugin.BundleFile;
import org.eclipse.oomph.version.Markers;
import org.eclipse.oomph.version.VersionUtil;
import org.eclipse.oomph.version.ui.quickfixes.VersionResolutionGenerator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolution2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

/**
 * @author Eike Stepper
 */
public class VersionBuilderExecutor extends TestCase
{
  private static final IWorkspace WORKSPACE = ResourcesPlugin.getWorkspace();

  private static final IWorkspaceRoot ROOT = WORKSPACE.getRoot();

  private static final File WORKSPACE_FOLDER = ROOT.getLocation().toFile();

  private static final int TRIM_LENGTH = WORKSPACE_FOLDER.getAbsolutePath().length();

  private static final String DELETE_SUFFIX = "-DELETE";

  private static final VersionResolutionGenerator FIX_GENERATOR = new VersionResolutionGenerator();

  private static final String FIX_PREFIX = "*";

  private static final String CHAR_START = "<" + IMarker.CHAR_START + ">";

  private static final String CHAR_END = "<" + IMarker.CHAR_END + ">";

  private static final String SEVERITY = "<" + IMarker.SEVERITY + ">";

  private static final String MESSAGE = "<" + IMarker.MESSAGE + ">";

  private static String lineDelimiter;

  private BundleFile testFolder;

  public VersionBuilderExecutor(BundleFile testFolder)
  {
    super(testFolder.getName());
    this.testFolder = testFolder;
  }

  @Override
  public void runBare() throws Throwable
  {
    AbstractTest.log("===============================================================================================");
    AbstractTest.log("Test " + getName());
    AbstractTest.log("===============================================================================================");

    clearWorkspace();

    boolean clean = true;
    for (BundleFile phase : testFolder.getChildren())
    {
      if (phase.isDirectory())
      {
        AbstractTest.log("  Phase '" + phase.getName() + "'");
        runPhase(phase, clean);
        clean = false;
      }
    }

    AbstractTest.log();
  }

  private void runPhase(BundleFile phase, boolean clean) throws Throwable
  {
    int fixAttempt = 0;
    String fileName = "build";
    String lastContents = "";

    AbstractTest.log("    Update workspace");
    updateWorkspace(phase);

    for (;;)
    {
      if (phase.getName().startsWith("clean") || phase.getChild("build.clean") != null)
      {
        clean = true;
      }

      IMarker[] markers = buildWorkspace(phase, clean);
      String contents = processMarkers(phase, markers, fileName);
      if (markers.length == 0 || contents.equals(lastContents))
      {
        break;
      }

      boolean hasFixes = processFixes(phase, markers, contents, fileName);
      if (!hasFixes)
      {
        break;
      }

      clean = false;
      ++fixAttempt;
      fileName = "fix" + fixAttempt;
      lastContents = contents;
    }
  }

  private void clearWorkspace() throws Throwable
  {
    WORKSPACE.getDescription().setAutoBuilding(false);
    ResourcesUtil.clearWorkspace();
  }

  private void updateWorkspace(final BundleFile phase) throws Throwable
  {
    WORKSPACE.run(new IWorkspaceRunnable()
    {
      public void run(IProgressMonitor monitor) throws CoreException
      {
        try
        {
          updateWorkspace(phase, WORKSPACE_FOLDER, 0);

          for (File file : WORKSPACE_FOLDER.listFiles())
          {
            String name = file.getName();
            if (file.isDirectory() && !".metadata".equals(name))
            {
              IProject project = ROOT.getProject(name);
              if (project.exists())
              {
                if (project.isOpen())
                {
                  project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
                }
                else
                {
                  project.open(monitor);
                }
              }
              else
              {
                project.create(monitor);
                project.open(monitor);
              }
            }
          }

          for (IProject project : ROOT.getProjects())
          {
            if (phase.getChild(project.getName() + DELETE_SUFFIX) != null)
            {
              project.delete(true, null);
            }
          }
        }
        catch (CoreException ex)
        {
          throw ex;
        }
        catch (RuntimeException ex)
        {
          throw ex;
        }
        catch (Throwable ex)
        {
          throw new RuntimeException(ex);
        }
      }
    }, null);
  }

  private void updateWorkspace(BundleFile source, File target, int level) throws Throwable
  {
    String relativePath = getRelativePath(target);
    if (source.getName().endsWith(DELETE_SUFFIX))
    {
      log("- " + relativePath.substring(0, relativePath.length() - DELETE_SUFFIX.length()));
      IOUtil.deleteBestEffort(target);
      return;
    }

    if (source.isDirectory())
    {
      if (!target.exists())
      {
        log("+ " + relativePath);
        target.mkdir();
      }

      for (BundleFile sourceChild : source.getChildren())
      {
        File targetChild = new File(target, sourceChild.getName());
        updateWorkspace(sourceChild, targetChild, level + 1);
      }
    }
    else if (level > 1) // Exclude files on project level
    {
      if (!target.exists())
      {
        log("+ " + relativePath);
      }
      else
      {
        log("* " + relativePath);
      }

      source.export(target);
    }
  }

  private IMarker[] buildWorkspace(BundleFile phase, boolean clean) throws Throwable
  {
    AbstractTest.log("    Build " + (clean ? "clean" : "incremental"));
    long start = System.currentTimeMillis();
    IMarker[] markers = ResourcesUtil.buildWorkspace(clean, Markers.MARKER_TYPE);
    log("Took " + (System.currentTimeMillis() - start) + " millis");
    return markers;
  }

  private String processMarkers(BundleFile phase, IMarker[] markers, String fileName) throws Throwable
  {
    fileName += ".markers";
    BundleFile markersFile = phase.getChild(fileName);
    if (markersFile != null)
    {
      AbstractTest.log("    Check " + fileName);
      return checkMarkers(phase, markers, markersFile);
    }

    AbstractTest.log("    Generate " + fileName);
    return generateMarkers(phase, markers, fileName);
  }

  private String checkMarkers(BundleFile phase, IMarker[] markers, BundleFile markersFile) throws Throwable
  {
    String expected = markersFile.getContentsString();
    String actual = createMarkers(markers);
    assertEquals("In " + markersFile, expected, actual);
    return actual;
  }

  private String generateMarkers(BundleFile phase, IMarker[] markers, String fileName) throws Throwable
  {
    String contents = createMarkers(markers);
    BundleFile resultsFile = phase.addChild(fileName, false);
    resultsFile.setContents(contents);
    return contents;
  }

  private String createMarkers(IMarker[] markers) throws Throwable
  {
    if (markers.length == 0)
    {
      log("No markers");
      return "";
    }

    Arrays.sort(markers, new Comparator<IMarker>()
    {
      public int compare(IMarker m1, IMarker m2)
      {
        int result = Markers.compareAttributes(Markers.RESOURCE_ATTRIBUTE, m1, m2);
        if (result == 0)
        {
          result = Markers.compareAttributes(IMarker.LINE_NUMBER, m1, m2);
          if (result == 0)
          {
            result = Markers.compareAttributes(IMarker.CHAR_START, m1, m2);
            if (result == 0)
            {
              result = Markers.compareAttributes(Markers.PROBLEM_TYPE, m1, m2);
            }
          }
        }

        return result;
      }
    });

    FileContentsProvider fileContentsProvider = new FileContentsProvider();
    StringBuilder builder = new StringBuilder();

    for (IMarker marker : markers)
    {
      IFile file = (IFile)marker.getResource();

      if (lineDelimiter == null)
      {
        lineDelimiter = VersionUtil.getLineDelimiter(file);
      }

      log("Marker");
      builder.append("Marker");
      builder.append(lineDelimiter);

      addAttribute(builder, Markers.RESOURCE_ATTRIBUTE + " ", file.getFullPath().makeRelative(), true);

      Map<String, Object> attributes = marker.getAttributes();
      List<String> keys = new ArrayList<String>(attributes.keySet());
      keys.remove(IMarker.LINE_NUMBER);

      addLocationAttributes(builder, attributes, keys, file, fileContentsProvider, true);

      if (keys.remove(IMarker.SEVERITY))
      {
        int severity = (Integer)attributes.get(IMarker.SEVERITY);
        addAttribute(builder, SEVERITY + " ", getSeverityLabel(severity), true);
      }

      if (keys.remove(IMarker.MESSAGE))
      {
        addAttribute(builder, MESSAGE + "  ", attributes.get(IMarker.MESSAGE), true);
      }

      if (keys.remove(Markers.PROBLEM_TYPE))
      {
        addAttribute(builder, Markers.PROBLEM_TYPE, attributes.get(Markers.PROBLEM_TYPE), true);
      }

      Collections.sort(keys);
      for (String key : keys)
      {
        Object value = attributes.get(key);
        addAttribute(builder, key, value, true);
      }

      IMarkerResolution[] resolutions = FIX_GENERATOR.getResolutions(marker);
      if (resolutions != null && resolutions.length != 0)
      {
        assertTrue("Marker has resolutions but hasResolutions() returns false", FIX_GENERATOR.hasResolutions(marker));
        for (IMarkerResolution resolution : resolutions)
        {
          addFix(builder, resolution);
        }
      }
    }

    return builder.toString();
  }

  private boolean addLocationAttributes(StringBuilder builder, Map<String, Object> attributes, List<String> keys, IFile file,
      FileContentsProvider fileContentsProvider, boolean msg) throws CoreException, IOException
  {
    if (keys.remove(IMarker.CHAR_START))
    {
      Object charStartAttribute = attributes.get(IMarker.CHAR_START);
      if (charStartAttribute == null)
      {
        return false;
      }

      int indexStart = (Integer)charStartAttribute;
      int indexEnd = -1;
      if (keys.remove(IMarker.CHAR_END))
      {
        indexEnd = (Integer)attributes.get(IMarker.CHAR_END);
      }

      String contents = fileContentsProvider.getContents(file);
      int size = contents.length();

      for (int i = 0, lf = 1, cr = 1, column = 0; i < size; ++i, ++column)
      {
        char c = contents.charAt(i);
        if (c == '\n')
        {
          ++lf;
          column = 1;
        }
        else if (c == '\r')
        {
          ++cr;
          column = 1;
        }

        if (i == indexStart || i == indexEnd)
        {
          String value = "(" + Math.max(cr, lf) + "," + column + ")";

          if (i == indexStart)
          {
            addAttribute(builder, CHAR_START, value, msg);
            if (indexEnd == -1)
            {
              break;
            }
          }
          else
          {
            addAttribute(builder, CHAR_END + "  ", value, msg);
            break;
          }
        }
      }
    }

    return true;
  }

  private boolean processFixes(BundleFile phase, IMarker[] markers, String contents, String fileName) throws Throwable
  {
    fileName += ".resolutions";
    BundleFile resolutionsFile = phase.getChild(fileName);
    if (resolutionsFile != null)
    {
      AbstractTest.log("    Apply " + fileName);
      return applyFixes(phase, markers, resolutionsFile);
    }

    AbstractTest.log("    Generate " + fileName);
    generateFixes(phase, contents, fileName);
    return false;
  }

  private boolean applyFixes(BundleFile phase, IMarker[] markers, BundleFile resolutionsFile) throws Throwable
  {
    boolean hasFixes = false;
    IPath path = null;
    String location = null;
    String problemType = null;

    String contents = resolutionsFile.getContentsString();
    String[] lines = contents.split("[\n\r]");
    for (int i = 0; i < lines.length; i++)
    {
      String line = lines[i].trim();

      if (line.startsWith(Markers.RESOURCE_ATTRIBUTE))
      {
        path = new Path(parseValue(line)).makeAbsolute();
      }

      if (line.startsWith(CHAR_START))
      {
        location = parseValue(line);
      }

      if (line.startsWith(Markers.PROBLEM_TYPE))
      {
        problemType = parseValue(line);
      }

      if (line.startsWith(FIX_PREFIX))
      {
        hasFixes = true;
        String fix = parseValue(line);
        applyFix(phase, markers, path, location, problemType, fix);

        path = null;
        location = null;
        problemType = null;
      }
    }

    return hasFixes;
  }

  private void applyFix(BundleFile phase, IMarker[] markers, IPath path, String location, String problemType, String fix) throws Throwable
  {
    FileContentsProvider fileContentsProvider = new FileContentsProvider();
    for (IMarker marker : markers)
    {
      IFile file = (IFile)marker.getResource();
      if (file.getFullPath().equals(path))
      {
        String markerProblemType = Markers.getProblemType(marker);
        if (VersionUtil.equals(markerProblemType, problemType))
        {
          Map<String, Object> attributes = marker.getAttributes();
          List<String> keys = new ArrayList<String>();
          keys.add(IMarker.CHAR_START);

          StringBuilder builder = new StringBuilder();
          boolean added = addLocationAttributes(builder, attributes, keys, file, fileContentsProvider, false);
          if (!added || VersionUtil.equals(parseValue(builder.toString()), location))
          {
            applyFix(phase, marker, fix);
            return;
          }
        }
      }
    }

    // throw new IllegalStateException("No marker found for '" + path + "'");
  }

  private void applyFix(BundleFile phase, IMarker marker, String fix) throws Throwable
  {
    IMarkerResolution[] resolutions = FIX_GENERATOR.getResolutions(marker);
    for (IMarkerResolution resolution : resolutions)
    {
      // msg(resolution.getLabel() + ": " + file.getFullPath().makeRelative());

      StringBuilder builder = new StringBuilder();
      addFix(builder, resolution);
      String resolutionFix = parseValue(builder.toString());
      if (VersionUtil.equals(resolutionFix, fix))
      {
        resolution.run(marker);
        return;
      }
    }

    throw new IllegalStateException("No resolution found for '" + fix + "'");
  }

  private void generateFixes(BundleFile phase, String contents, String fileName) throws Throwable
  {
    BundleFile resolutionsFile = phase.addChild(fileName, false);
    resolutionsFile.setContents(contents);
  }

  private static void addAttribute(StringBuilder builder, String key, Object value, boolean msg)
  {
    String str = "  " + key + " = " + value;
    if (msg)
    {
      log(str);
    }

    builder.append(str);
    builder.append(lineDelimiter);
  }

  private static void addFix(StringBuilder builder, IMarkerResolution resolution)
  {
    String str = "  FIX = " + resolution.getLabel();
    if (resolution instanceof IMarkerResolution2)
    {
      IMarkerResolution2 resolution2 = (IMarkerResolution2)resolution;
      str += " (" + resolution2.getDescription() + ")";
    }

    log(str);

    builder.append(str);
    builder.append(lineDelimiter);
  }

  private static Object getSeverityLabel(int severity)
  {
    switch (severity)
    {
      case IMarker.SEVERITY_INFO:
        return "INFO";
      case IMarker.SEVERITY_WARNING:
        return "WARNING";
      case IMarker.SEVERITY_ERROR:
        return "ERROR";
      default:
        throw new IllegalStateException("Illegal severity code " + severity);
    }
  }

  private static String getRelativePath(File file)
  {
    return file.getAbsolutePath().substring(TRIM_LENGTH).replace('\\', '/');
  }

  private static String parseValue(String str)
  {
    int pos = str.indexOf('=');
    if (pos == -1)
    {
      throw new IllegalArgumentException("Property syntax error");
    }

    return str.substring(pos + 1).trim();
  }

  private static void log(String string)
  {
    AbstractTest.log("      " + string);
  }

  /**
   * @author Eike Stepper
   */
  private static class FileContentsProvider
  {
    private IFile file;

    private String contents;

    public String getContents(IFile file) throws CoreException, IOException
    {
      if (!VersionUtil.equals(this.file, file))
      {
        contents = VersionUtil.getContents(file);
        this.file = file;
      }

      return contents;
    }
  }
}
