/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.presentation;

import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.internal.core.TargletContainerDescriptorManager;
import org.eclipse.oomph.targlets.internal.core.TargletContainerResourceFactory;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;

import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.ui.IMemento;

/**
 * @author Eike Stepper
 */
public final class TargletContainerEditorInput extends URIEditorInput
{
  public TargletContainerEditorInput(String containerID)
  {
    super(URI.createGenericURI(TargletContainerResourceFactory.PROTOCOL_NAME, containerID, null), containerID);
  }

  public TargletContainerEditorInput(IMemento memento)
  {
    super(memento);
  }

  public String getContainerID()
  {
    return getURI().opaquePart();
  }

  @Override
  public String getToolTipText()
  {
    String id = getContainerID();
    ITargletContainer targletContainer = TargletContainerDescriptorManager.getContainer(id);
    if (targletContainer == null)
    {
      return id;
    }

    ITargetDefinition targetDefinition = targletContainer.getTargetDefinition();
    boolean active = TargetPlatformUtil.isActiveTargetDefinition(targetDefinition);
    return TargletContainerEditorInput.getContainerLabel(targletContainer, active);
  }

  @Override
  protected String getBundleSymbolicName()
  {
    return TargletEditorPlugin.INSTANCE.getSymbolicName();
  }

  public static String getContainerLabel(ITargletContainer targletContainer, boolean active)
  {
    return targletContainer.getID() + " (" + targletContainer.getTargetDefinition().getName() + (active ? ", active" : "") + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
  }
}
