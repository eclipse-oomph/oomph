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
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.presentation.SetupEditor;
import org.eclipse.oomph.setup.provider.SetupEditPlugin;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import java.net.URL;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class OpenEditorDropdownHandler extends AbstractDropdownHandler
{
  public static final String COMMAND_ID = "org.eclipse.oomph.setup.editor.openEditorDropdown";

  public OpenEditorDropdownHandler()
  {
    super(COMMAND_ID);
  }

  @Override
  protected ActionDescriptor createActionDescriptor() throws Exception
  {
    final User user = SetupFactory.eINSTANCE.createUser();
    ((InternalEObject)user).eSetProxyURI(SetupContext.USER_SETUP_URI.appendFragment("/"));

    ComposedAdapterFactory adapterFactory = BaseEditUtil.createAdapterFactory();
    ItemProviderAdapter labelProvider = (ItemProviderAdapter)adapterFactory.adapt(user, IItemLabelProvider.class);

    ImageDescriptor imageDescriptor = getLabelImage(labelProvider, user);
    String text = getLabelText(labelProvider, user);

    Runnable runnable = new Runnable()
    {
      public void run()
      {
        openEditor(user);
      }
    };

    return new ActionDescriptor(imageDescriptor, text, runnable);
  }

  public static ImageDescriptor getLabelImage(ItemProviderAdapter labelProvider, EObject object)
  {
    Object key = labelProvider.getImage(object);
    if (key instanceof ComposedImage)
    {
      ComposedImage composedImage = (ComposedImage)key;
      List<Object> images = composedImage.getImages();
      key = images.get(0);
    }

    return ImageDescriptor.createFromURL((URL)key);
  }

  public static String getLabelText(ItemProviderAdapter itemProvider, EObject object)
  {
    EClass eClass = object.eClass();
    if (eClass == SetupPackage.Literals.USER || eClass == SetupPackage.Literals.INSTALLATION || eClass == SetupPackage.Literals.WORKSPACE)
    {
      int xxx;
      return "Open " + SetupEditPlugin.getPlugin().getString("_UI_" + eClass.getName() + "_type");
    }

    if (object instanceof ProductVersion)
    {
      ProductVersion version = (ProductVersion)object;
      return "Open " + itemProvider.getText(version.getProduct()) + " - " + itemProvider.getText(version);
    }

    if (object instanceof Stream)
    {
      StringBuilder builder = new StringBuilder("Open ");
      getLabel(itemProvider, builder, ((Stream)object).getProject());
      builder.append(itemProvider.getText(object));
      return builder.toString();
    }

    if (object instanceof ProjectCatalog)
    {
      return "Open Project Catalog " + itemProvider.getText(object);
    }

    if (object instanceof ProductCatalog)
    {
      return "Open Product Catalog " + itemProvider.getText(object);
    }

    if (object instanceof Index)
    {
      return "Open Catalog " + itemProvider.getText(object);
    }

    return "Open " + itemProvider.getText(object);
  }

  private static void getLabel(ItemProviderAdapter itemProvider, StringBuilder builder, Project project)
  {
    Project parentProject = project.getParentProject();
    if (parentProject != null)
    {
      getLabel(itemProvider, builder, parentProject);
    }

    builder.append(itemProvider.getText(project));
    builder.append(" - ");
  }

  public static void openEditor(EObject object)
  {
    URI uri = EcoreUtil.getURI(object);
    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    SetupEditor.open(page, uri);
  }
}
