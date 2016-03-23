/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.editor;

import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.ui.LabelDecorator;
import org.eclipse.oomph.ui.PropertiesViewer;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Eike Stepper
 */
public abstract class SetupTemplate
{
  public static final String PRODUCT_GROUP = "org.eclipse.oomph.setup.projectTemplates";

  private final String label;

  private final String description;

  private Resource resource;

  private Container container;

  protected SetupTemplate(String label)
  {
    this(label, null);
  }

  protected SetupTemplate(String label, String description)
  {
    this.label = label;
    this.description = StringUtil.safe(description);
  }

  public abstract Control createControl(Composite parent);

  public abstract String getMessage();

  protected void init()
  {
    ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
    resource = resourceSet.createResource(URI.createURI("*.setup"));
  }

  public void updatePreview()
  {
  }

  public LabelDecorator getDecorator()
  {
    return null;
  }

  public final String getLabel()
  {
    return label;
  }

  public final String getDescription()
  {
    return description;
  }

  public final Resource getResource()
  {
    if (resource == null)
    {
      init();
    }

    return resource;
  }

  public final Container getContainer()
  {
    return container;
  }

  public final void init(Container container)
  {
    this.container = container;
  }

  @Override
  public final String toString()
  {
    return getLabel();
  }

  /**
   * @author Eike Stepper
   */
  public interface Container
  {
    public TreeViewer getPreviewer();

    public PropertiesViewer getPropertiesViewer();

    public void validate();

    public String getDefaultLocation();
  }
}
