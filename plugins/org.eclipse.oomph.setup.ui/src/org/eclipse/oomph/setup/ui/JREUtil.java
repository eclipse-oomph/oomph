/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Ed Merks
 */
public class JREUtil
{
  // List<IVMInstall> fVMs = new ArrayList<IVMInstall>();
  //
  // String fgLastUsedID;

  public static List<URI> getPath()
  {
    List<URI> result = new UniqueEList<URI>();
    String path = System.getenv("PATH");
    for (String folder : path.split(File.pathSeparator))
    {
      try
      {
        String x = new File(folder).getCanonicalPath();
        URI uri = URI.createFileURI(x);
        if (uri.segmentCount() > 1)
        {
          uri = uri.trimSegments(uri.segmentCount() - 1);
        }
        result.add(uri);
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
    }

    return result;
  }

  // protected void search(final String path, IProgressMonitor monitor)
  // {
  // if (Platform.OS_MACOSX.equals(Platform.getOS()))
  // {
  // doMacSearch(monitor);
  // return;
  // }
  //
  // // ignore installed locations
  // final Set<File> exstingLocations = new HashSet<File>();
  // for (IVMInstall vm : fVMs)
  // {
  // exstingLocations.add(vm.getInstallLocation());
  // }
  //
  // // search
  // final File rootDir = new File(path);
  // final List<File> locations = new ArrayList<File>();
  // final List<IVMInstallType> types = new ArrayList<IVMInstallType>();
  //
  // monitor.beginTask("Scanning " + path, IProgressMonitor.UNKNOWN);
  // search(rootDir, locations, types, exstingLocations, monitor);
  // monitor.done();
  //
  // if (locations.isEmpty())
  // {
  // // TODO
  // }
  // else
  // {
  // Iterator<IVMInstallType> iter2 = types.iterator();
  // for (File location : locations)
  // {
  // IVMInstallType type = iter2.next();
  // AbstractVMInstall vm = new VMStandin(type, createUniqueId(type));
  // String name = location.getName();
  // String nameCopy = new String(name);
  // int i = 1;
  // while (isDuplicateName(nameCopy))
  // {
  // nameCopy = name + '(' + i++ + ')';
  // }
  // vm.setName(nameCopy);
  // vm.setInstallLocation(location);
  // if (type instanceof AbstractVMInstallType)
  // {
  // // set default java doc location
  // AbstractVMInstallType abs = (AbstractVMInstallType)type;
  // vm.setJavadocLocation(abs.getDefaultJavadocLocation(location));
  // vm.setVMArgs(abs.getDefaultVMArguments(location));
  // }
  //
  // vmAdded(vm);
  // }
  // }
  // }
  //
  // private void vmAdded(AbstractVMInstall vm)
  // {
  // }
  //
  // public boolean isDuplicateName(String name)
  // {
  // for (int i = 0; i < fVMs.size(); i++)
  // {
  // IVMInstall vm = fVMs.get(i);
  // if (vm.getName().equals(name))
  // {
  // return true;
  // }
  // }
  // return false;
  // }
  //
  // private String createUniqueId(IVMInstallType vmType)
  // {
  // String id = null;
  // do
  // {
  // id = String.valueOf(System.currentTimeMillis());
  // } while (vmType.findVMInstall(id) != null || id.equals(fgLastUsedID));
  // fgLastUsedID = id;
  // return id;
  // }
  //
  // /**
  // * Calls out to {@link MacVMSearch} to find all installed JREs in the standard
  // * Mac OS location
  // */
  // @SuppressWarnings("restriction")
  // private void doMacSearch(IProgressMonitor monitor)
  // {
  // final List<VMStandin> added = new ArrayList<VMStandin>();
  // Set<String> exists = new HashSet<String>();
  // for (IVMInstall vm : fVMs)
  // {
  // exists.add(vm.getId());
  // }
  // SubMonitor localmonitor = SubMonitor.convert(monitor, "Searching", 5);
  // VMStandin[] standins = null;
  // try
  // {
  // standins = org.eclipse.jdt.internal.launching.MacInstalledJREs.getInstalledJREs(localmonitor);
  // for (int i = 0; i < standins.length; i++)
  // {
  // if (!exists.contains(standins[i].getId()))
  // {
  // added.add(standins[i]);
  // }
  // }
  // }
  // catch (CoreException ce)
  // {
  // SetupUIPlugin.INSTANCE.log(ce);
  // }
  // monitor.done();
  //
  // for (VMStandin vm : added)
  // {
  // vmAdded(vm);
  // }
  // }
  //
  // protected void search(File directory, List<File> found, List<IVMInstallType> types, Set<File> ignore, IProgressMonitor monitor)
  // {
  // if (monitor.isCanceled())
  // {
  // return;
  // }
  //
  // String[] names = directory.list();
  // if (names == null)
  // {
  // return;
  // }
  //
  // List<File> subDirs = new ArrayList<File>();
  // for (int i = 0; i < names.length; i++)
  // {
  // if (monitor.isCanceled())
  // {
  // return;
  // }
  //
  // File file = new File(directory, names[i]);
  // monitor.subTask("Looking in " + file.getAbsolutePath());
  // IVMInstallType[] vmTypes = JavaRuntime.getVMInstallTypes();
  // if (file.isDirectory())
  // {
  // if (!ignore.contains(file))
  // {
  // boolean validLocation = false;
  // for (int j = 0; j < vmTypes.length; j++)
  // {
  // if (monitor.isCanceled())
  // {
  // return;
  // }
  // IVMInstallType type = vmTypes[j];
  // IStatus status = type.validateInstallLocation(file);
  // if (status.isOK())
  // {
  // found.add(file);
  // types.add(type);
  // validLocation = true;
  // break;
  // }
  // }
  //
  // if (!validLocation)
  // {
  // subDirs.add(file);
  // }
  // }
  // }
  // }
  //
  // while (!subDirs.isEmpty())
  // {
  // File subDir = subDirs.remove(0);
  // search(subDir, found, types, ignore, monitor);
  // if (monitor.isCanceled())
  // {
  // return;
  // }
  // }
  // }
}
