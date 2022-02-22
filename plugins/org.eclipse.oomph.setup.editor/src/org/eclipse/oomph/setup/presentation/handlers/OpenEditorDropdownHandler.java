/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.ui.SetupEditorSupport;
import org.eclipse.oomph.setup.ui.SetupLabelProvider;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

/**
 * @author Eike Stepper
 */
public class OpenEditorDropdownHandler extends AbstractDropdownHandler
{
  public static final String COMMAND_ID = "org.eclipse.oomph.setup.editor.openEditorDropdown"; //$NON-NLS-1$

  public OpenEditorDropdownHandler()
  {
    super(COMMAND_ID);
  }

  @Override
  protected ActionDescriptor createActionDescriptor() throws Exception
  {
    final User user = SetupFactory.eINSTANCE.createUser();
    ((InternalEObject)user).eSetProxyURI(SetupContext.USER_SETUP_URI.appendFragment("/")); //$NON-NLS-1$

    // No need to dispose the adapter factory explicitly because the single adapted object is short-lived.
    ComposedAdapterFactory adapterFactory = BaseEditUtil.createAdapterFactory();
    ItemProviderAdapter itemProvider = (ItemProviderAdapter)adapterFactory.adapt(user, IItemLabelProvider.class);

    ImageDescriptor imageDescriptor = SetupLabelProvider.getImageDescriptor(itemProvider, user);
    String text = getLabelText(itemProvider, user, true);

    Runnable runnable = new Runnable()
    {
      @Override
      public void run()
      {
        openEditor(user);
      }
    };

    return new ActionDescriptor(imageDescriptor, text, runnable);
  }

  public static String getLabelText(ItemProviderAdapter itemProvider, EObject object, boolean qualified)
  {
    String itemLabel = SetupLabelProvider.getText(itemProvider, object);
    return qualified ? NLS.bind(Messages.OpenEditorDropdownHandler_openItem, itemLabel) : itemLabel;
  }

  public static void openEditor(EObject object)
  {
    URI uri = EcoreUtil.getURI(object);
    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    if (page != null)
    {
      SetupEditorSupport.getEditor(page, uri, true);
    }
  }
}
