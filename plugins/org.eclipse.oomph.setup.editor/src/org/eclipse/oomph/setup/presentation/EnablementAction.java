/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.base.util.EAnnotations;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public final class EnablementAction extends Action
{
  private final Shell shell;

  private final EClass eClass;

  private final String typeText;

  private final EList<SetupTask> enablementTasks;

  private final String defaultImageKey;

  public EnablementAction(Shell shell, EClass eClass, String typeText, EList<SetupTask> enablementTasks)
  {
    this.shell = shell;
    this.eClass = eClass;
    this.typeText = typeText;
    this.enablementTasks = enablementTasks;

    defaultImageKey = SetupPackage.Literals.SETUP_TASK.isSuperTypeOf(eClass) ? "full/obj16/SetupTask" : "full/obj16/EObject"; //$NON-NLS-1$ //$NON-NLS-2$

    setText(typeText + "..."); //$NON-NLS-1$
    setToolTipText(NLS.bind(Messages.EnablementAction_tooltip, typeText));
    setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor(defaultImageKey));
  }

  public EClass getEClass()
  {
    return eClass;
  }

  public void loadImage()
  {
    URI imageURI = EAnnotations.getImageURI(eClass);
    if (imageURI != null)
    {
      final Image image = ExtendedImageRegistry.INSTANCE.getImage(BaseEditUtil.getImage(imageURI));
      setImageDescriptor(ImageDescriptor.createFromImage(image));
    }
  }

  @Override
  public void run()
  {
    EnablementDialog dialog = new EnablementDialog(shell, eClass, typeText, enablementTasks, defaultImageKey);
    if (dialog.open() == EnablementDialog.OK)
    {
      ResourceSet resourceSet = SetupCoreUtil.createResourceSet();

      SetupContext self = SetupContext.create(resourceSet);
      Installation installation = self.getInstallation();
      installation.getSetupTasks().addAll(enablementTasks);

      SetupWizard updater = new SetupWizard.Updater(self);
      updater.setTriggerName("ENABLEMENT"); //$NON-NLS-1$
      updater.openDialog(UIUtil.getShell());
    }
  }
}
