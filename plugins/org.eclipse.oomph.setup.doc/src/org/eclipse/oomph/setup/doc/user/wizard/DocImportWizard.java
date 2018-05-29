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
package org.eclipse.oomph.setup.doc.user.wizard;

import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog.DocProject.DocStream;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource.DocWorkspaceResource;
import org.eclipse.oomph.setup.doc.concepts.DocTask.DocTrigger;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;

/**
 * Import Wizard
 * <p>
 * The wizard is the basis for Oomph's manually-{@link DocTrigger triggered}
 * updates to the project {@link DocStream streams} included to the automated installation and provisioning process.
 * {@link #projectImporter()}
 * <p>
 * {@link #wizardFooter()}
 * </p>
 * <p>
 * The project wizard is available in any running Eclipse product with Oomph installed
 * via {@select File|Import...|Oomph|Projects into Workspace}
 * and the <em>execution</em> tool bar button.
 * Its purpose is to augment the {@link DocWorkspaceResource workspace}'s set of streams.
 * </p>
 *
 * @number 200
 */
public abstract class DocImportWizard extends DocInstallWizard
{
  /**
   * @snippet image ProjectImportWizard.images
   */
  public static Image projectImporter()
  {
    return CaptureProjectWizard.getInstance().projectWizard;
  }

  /**
   * @ignore
   */
  public static class CaptureProjectWizard extends CaptureSetupWizard
  {
    private static DocImportWizard.CaptureProjectWizard instance;

    public Image projectWizard;

    public static DocImportWizard.CaptureProjectWizard getInstance()
    {
      if (instance == null)
      {
        instance = new CaptureProjectWizard();
        instance.projectWizard = instance.capture();
      }
      return instance;
    }

    @Override
    protected WizardDialog create(Shell shell)
    {
      return new WizardDialog(shell, new org.eclipse.oomph.setup.ui.wizards.SetupWizard.Importer());
    }

    @Override
    protected void postProcess(WizardDialog wizardDialog)
    {
      super.postProcess(wizardDialog);

      postProcessProjectPage(wizardDialog);
    }
  }
}
