/*
 * Copyright (c) 2017 Adrian Price and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Adrian Price <aprice@tibco.com> - initial API and implementation
 */
package org.eclipse.oomph.setup.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.SetupPrompter.Default;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.jdt.JDTFactory;
import org.eclipse.oomph.setup.jdt.JRELibrary;
import org.eclipse.oomph.setup.jdt.JRETask;

import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ExtensibleURIConverterImpl;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.launching.StandardVMType;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("restriction")
public class JRETaskTest
{
  private static final String RT_LIB_PATH = "lib/rt.jar";

  private static final Path EXT_ANNOTATIONS_PATH = new Path("/Configuration/annotations");

  private static final String JRE_NAME = "Test JRE for JavaSE-1.8";

  private JRETask jreTask;

  private SetupTaskContext setupTaskContext;

  @Before
  public void setUp() throws Exception
  {
    URIConverter uriConverter = new ExtensibleURIConverterImpl();
    SetupPrompter prompter = new Default(true);
    Trigger trigger = null;
    SetupContext setupContext = SetupContext.create();
    Stream stream = null;
    setupTaskContext = new SetupTaskPerformer(uriConverter, prompter, trigger, setupContext, stream);

    jreTask = JDTFactory.eINSTANCE.createJRETask();
    jreTask.setName(JRE_NAME);
    jreTask.setLocation(System.getProperty("java.home"));
    jreTask.setVMInstallType(StandardVMType.ID_STANDARD_VM_TYPE);
    jreTask.setVersion("JavaSE-1.8");
    JRELibrary jreLibrary = JDTFactory.eINSTANCE.createJRELibrary();
    jreLibrary.setLibraryPath(RT_LIB_PATH);
    jreLibrary.setExternalAnnotationsPath(EXT_ANNOTATIONS_PATH.toString());
    jreTask.getJRELibraries().add(jreLibrary);
  }

  @After
  public void tearDown() throws Exception
  {
    setupTaskContext = null;
    jreTask = null;
  }

  @Test
  public void testPerform() throws Exception
  {
    checkIsNeeded(true);

    jreTask.perform(setupTaskContext);

    checkExternalAnnotationsPath();

    checkIsNeeded(false);
  }

  private void checkIsNeeded(boolean isNeeded) throws Exception
  {
    assertEquals("JRETask.isNeeded() returned the wrong value;", isNeeded, jreTask.isNeeded(setupTaskContext));
  }

  private void checkExternalAnnotationsPath()
  {
    boolean foundVM = false;
    for (IVMInstallType vmInstallType : JavaRuntime.getVMInstallTypes())
    {
      if (vmInstallType.getId().equals(StandardVMType.ID_STANDARD_VM_TYPE))
      {
        for (IVMInstall vmInstall : vmInstallType.getVMInstalls())
        {
          if (vmInstall.getName().equals(JRE_NAME))
          {
            foundVM = true;

            LibraryLocation[] libraryLocations = vmInstall.getLibraryLocations();
            assertNotNull("VM Install '" + JRE_NAME + "' library configuration not applied", libraryLocations);

            boolean foundLibrary = false;
            IPath libraryPath = new Path(System.getProperty("java.home")).append(RT_LIB_PATH);
            for (LibraryLocation libraryLocation : libraryLocations)
            {
              if (libraryPath.equals(libraryLocation.getSystemLibraryPath()))
              {
                foundLibrary = true;

                assertEquals("External annotations path incorrect;", EXT_ANNOTATIONS_PATH, libraryLocation.getExternalAnnotationsPath());
              }
            }
            if (!foundLibrary)
            {
              fail("Library '" + RT_LIB_PATH + "' not found in VM Install '" + JRE_NAME + '\'');
            }
          }
        }

        break;
      }
    }
    if (!foundVM)
    {
      fail("VM Install '" + JRE_NAME + "' not found");
    }
  }

}
