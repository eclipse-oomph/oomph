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
package org.eclipse.oomph.workingsets.util;

import org.eclipse.oomph.workingsets.WorkingSetGroup;
import org.eclipse.oomph.workingsets.impl.PreferencesURIHandlerImpl;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

/**
 * @author Eike Stepper
 */
public final class WorkingSetsUtil
{
  private static final String DEFAULT_WORKING_SET_GROUP_PREFERENCE_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<workingsets:WorkingSetGroup xmi:version=\"2.0\" xmlns:xmi=\"http://www.omg.org/XMI\" xmlns:workingsets=\"http://www.eclipse.org/oomph/workingsets/1.0\"/>\n";

  public static final String WORKING_SET_GROUP_PREFERENCE_KEY = "working.set.group";

  public static final URI WORKING_SET_GROUP_PREFERENCE_URI = URI
      .createURI("preference:/instance/org.eclipse.oomph.workingsets/" + WORKING_SET_GROUP_PREFERENCE_KEY + "/");

  public static final URI WORKING_SET_GROUP_PREFERENCE_RESOURCE_URI = WORKING_SET_GROUP_PREFERENCE_URI.trimSegments(1).appendSegment("Preferences.workingsets");

  public static final IEclipsePreferences WORKING_SET_GROUP_PREFERENCES;

  public static WorkingSetGroup getWorkingSetGroup()
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.getURIConverter().getURIHandlers().add(0, new PreferencesURIHandlerImpl());

    Resource resource = resourceSet.getResource(WORKING_SET_GROUP_PREFERENCE_RESOURCE_URI, true);
    return (WorkingSetGroup)resource.getContents().get(0);
  }

  static
  {
    WORKING_SET_GROUP_PREFERENCES = (IEclipsePreferences)Platform.getPreferencesService().getRootNode().node(WORKING_SET_GROUP_PREFERENCE_URI.segment(0))
        .node(WORKING_SET_GROUP_PREFERENCE_URI.segment(1));

    String value = WORKING_SET_GROUP_PREFERENCES.get(WorkingSetsUtil.WORKING_SET_GROUP_PREFERENCE_KEY, null);
    if (value == null)
    {
      WORKING_SET_GROUP_PREFERENCES.put(WorkingSetsUtil.WORKING_SET_GROUP_PREFERENCE_KEY, WorkingSetsUtil.DEFAULT_WORKING_SET_GROUP_PREFERENCE_CONTENT);
    }
  }
}
