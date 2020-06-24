/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.doc.user.wizard;

import org.eclipse.oomph.setup.doc.concepts.DocTask.DocP2Task;
import org.eclipse.oomph.setup.doc.concepts.DocTask.DocTargletTask;
import org.eclipse.oomph.setup.doc.concepts.DocTaskComposition;
import org.eclipse.oomph.setup.internal.installer.InstallerDialog;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Confirmation Page
 * <p>
 * The primary purpose of the confirmation page is to review the {@link DocTaskComposition gathered} task list
 * with the opportunity to selectively enable specific tasks.
 * <br>
 * {@link #confirmationPage()}
 * </p>
 *
 * @number 700
 */
@SuppressWarnings("nls")
public class DocConfirmationPage
{
  /**
   * @snippet image ConfirmationPage.images
   * @style box
   * @description
   * The page has the following controls:
   * @callout
   * Displays the tasks to be performed.
   * The root object shows the trigger that started the wizard.
   * The check boxes allow tasks to be selectively chosen for execution.
   * Double clicking a task will enable that task and disable all other tasks.
   * The view can either show all tasks, including the ones that don't need to perform,
   * or only the tasks that need to be performed.
   * Tasks that don't need to perform are shown grayed out.
   * Selecting a task will display its contained children in the nested elements viewer
   * and will display its properties in the properties viewer.
   * @callout
   * Displays the nested children of the selected task in the tasks viewer.
   * Selecting a child will display its properties in the properties viewer.
   * @callout
   * Displays the properties of the selected task or the selected nested element of the selected task.
   * @callout
   * Determines whether to show all tasks or only the tasks that need to perform.
   * @callout
   * Determines whether the installation and provisioning process will,
   * as much as possible,
   * proceed using locally cached resources rather than using internet access to get the latest information.
   * @callout
   * Determines whether p2 mirrors will be used during installation and provisioning,
   * rather than just the primary internet host.
   * This setting is particularly relevant for {@linkplain DocP2Task p2} and {@link DocTargletTask targlet} tasks
   * which make heavy use of p2 update sites.
   * @callout
   * Determines whether to ovewrite an existing installation.
   * This is displayed only in the installer wizard
   * when it detects an attempt to install into a location where an installation already exists.
   * In this case, the title area will display an error message,
   * and it's only possible to proceed if one elects to reinstall,
   * i.e., to overwrite an existing installation.
   */
  public static Image[] confirmationPage()
  {
    DocConfirmationPage.CaptureConfirmationPage instance = CaptureConfirmationPage.getInstance();
    return new Image[] { instance.confirmationPage, instance.viewerDecoration, instance.childrenViewerDecoration, instance.propertiesViewerDecoration,
        instance.showAll, instance.offline, instance.mirrors, instance.overwrite };
  }

  /**
   * @ignore
   */
  public static class CaptureConfirmationPage extends CaptureSetupWizard
  {
    private static DocConfirmationPage.CaptureConfirmationPage instance;

    private Image confirmationPage;

    private Image showAll;

    private Image offline;

    private Image mirrors;

    private Image viewerDecoration;

    private Image childrenViewerDecoration;

    private Image propertiesViewerDecoration;

    private Image overwrite;

    public static DocConfirmationPage.CaptureConfirmationPage getInstance()
    {
      if (instance == null)
      {
        instance = new CaptureConfirmationPage();
        instance.confirmationPage = instance.capture();
      }
      return instance;
    }

    @Override
    protected WizardDialog create(Shell shell)
    {
      return new InstallerDialog(shell, false);
    }

    @Override
    protected void postProcess(WizardDialog wizardDialog)
    {
      super.postProcess(wizardDialog);

      postProcessProductPage(wizardDialog);

      advanceToNextPage(wizardDialog);

      postProcessProjectPage(wizardDialog);

      advanceToNextPage(wizardDialog);

      postProcessVariablePage(wizardDialog, "oomph");

      advanceToNextPage(wizardDialog);

      postProcessConfirmationPage(wizardDialog, true);
    }

    @Override
    protected Image capture(WizardDialog wizardDialog)
    {
      IWizardPage page = wizardDialog.getCurrentPage();

      Map<Control, Image> decorations = new LinkedHashMap<Control, Image>();
      viewerDecoration = getCalloutImage(1);
      Control viewer = getViewerControl(wizardDialog, "viewer");
      decorations.put(viewer, viewerDecoration);

      childrenViewerDecoration = getCalloutImage(2);
      Control childrenViewer = getViewerControl(wizardDialog, "childrenViewer");
      decorations.put(childrenViewer, childrenViewerDecoration);

      propertiesViewerDecoration = getCalloutImage(3);
      Control propertiesViewer = getViewerControl(wizardDialog, "propertiesViewer");
      decorations.put(propertiesViewer, propertiesViewerDecoration);

      Image result = capture(page, decorations);

      showAll = getImage(wizardDialog, "showAllTasks");
      offline = getImage(wizardDialog, "offline");
      mirrors = getImage(wizardDialog, "mirrors");
      overwrite = getImage(wizardDialog, "overwrite");

      return result;
    }
  }
}
