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

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.doc.concepts.DocInfrastructure.DocIndex;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog.DocProject;
import org.eclipse.oomph.setup.doc.concepts.DocScope.DocProjectCatalog.DocProject.DocStream;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource.DocIndexResource.ProjectCatalogResource;
import org.eclipse.oomph.setup.doc.concepts.DocSetupResource.DocWorkspaceResource;
import org.eclipse.oomph.setup.internal.installer.InstallerDialog;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Projects Page
 * <p>
 * The primary purpose of the project page is to create or update a {@linkplain DocWorkspaceResource workspace}
 * with one or more specific {@link DocStream project streams}.
 * {@link #projectPage()}
 * </p>
 *
 * @number 500
 */
public abstract class DocProjectPage
{
  /**
   * @snippet image ProjectPage.images
   * @style box
   * @description
   * The page contains the following controls:
   * @callout
   * Filters which of the {@link DocProject projects} are displayed.
   * @callout
   * Adds a project to the &lt;User> project of selected {@link ProjectCatalogResource project catalog}.
   * If the &lt;User> project is empty, it's not displayed in the tree,
   * but you can still add a project to the catalog,
   * and then it will be displayed.
   * @callout
   * Removes the selected project from the<&lt;User> project.
   * It is only enabled for a project within a &lt;User> project.
   * @callout
   * Collapses the tree;
   * initially the tree roots are expanded.
   * @callout
   * Updates the locally-cached versions of all the {@link DocSetupResource resources} used in the wizard.
   * @callout
   * Chooses which of the project catalogs available in the {@link DocIndex index} to display.
   * @callout
   * Displays the {@linkplain CatalogSelection selected} {@linkplain DocProjectCatalog project catalogs}.
   * Double-clicking a project automatically adds its stream to the table of chosen streams,
   * or, if one of its streams is already in the table of chosen streams,
   * removes that stream from the table.
   * The project of each chosen stream is shown in bold font.
   * Only a project with at least one stream can be added to the table of chosen streams.
   * For projects without streams,
   * presumably projects that contain only nested projects,
   * given that completely empty projects are not displayed in the tree,
   * double-clicking expands or collapses the project's tree item.
   * @callout
   * Adds the steams of the selected projects of the tree to the table of chosen streams.
   * @callout
   * Removes the selected streams from the table of chosen streams,
   * and leaves their corresponding projects selected in the tree.
   * @callout
   * Displays the chosen streams.
   * Double clicking a stream removes that stream from the table
   * and leaves its corresponding project selected in the tree.
   * The stream column supports cell editing:
   * use the cell editor's down-down to choose which stream of the project's available streams to provision.
   * @callout
   * Determines whether any streams are to be provisioned.
   * This control is visible only in the {@link DocInstallWizard install} wizard.
   * When enabled,
   * any selected streams are removed from the table
   * and the wizard's next button is enabled to proceed
   * without choosing any streams to provision.
   */
  public static Image[] projectPage()
  {
    DocProjectPage.CaptureProjectPage instance = CaptureProjectPage.getInstance();
    return new Image[] { instance.projectPage, instance.filter, instance.addProject, instance.removeProject, instance.collapse, instance.refresh,
        instance.catalogs, instance.treeImageDecoration, instance.choose, instance.unchoose, instance.tableImageDecoration, instance.skip };
  }

  /**
   * @ignore
   */
  public static class CaptureProjectPage extends CaptureSetupWizard
  {
    private static DocProjectPage.CaptureProjectPage instance;

    private Image addProject;

    private Image removeProject;

    private Image collapse;

    private Image refresh;

    private Image catalogs;

    private Image treeImageDecoration;

    private Image projectPage;

    private Image filter;

    private Image choose;

    private Image unchoose;

    private Image tableImageDecoration;

    private Image skip;

    public static DocProjectPage.CaptureProjectPage getInstance()
    {
      if (instance == null)
      {
        instance = new CaptureProjectPage();
        instance.projectPage = instance.capture();
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
    }

    @Override
    protected Image capture(WizardDialog wizardDialog)
    {
      IWizardPage page = wizardDialog.getCurrentPage();

      treeImageDecoration = getCalloutImage(1);
      Control tree = getViewerControl(wizardDialog, "projectViewer");

      tableImageDecoration = getCalloutImage(2);
      Control table = getViewerControl(wizardDialog, "streamViewer");
      Event event = new Event();
      event.button = 1;
      event.count = 1;
      event.type = 3;
      event.x = 605;
      event.y = 40;
      table.notifyListeners(SWT.MouseDown, event);
      AccessUtil.busyWait(100);

      Map<Control, Image> decorations = new LinkedHashMap<Control, Image>();
      decorations.put(tree, treeImageDecoration);
      decorations.put(table, tableImageDecoration);
      Image result = capture(page, decorations);

      filter = getImage(wizardDialog, "filter");

      addProject = getImage(wizardDialog, "addProject");
      removeProject = getImage(wizardDialog, "removeProject");
      collapse = getImage(wizardDialog, "collapse");
      refresh = getImage(wizardDialog, "refresh");
      catalogs = getImage(wizardDialog, "catalogs");

      choose = getImage(wizardDialog, "choose");
      unchoose = getImage(wizardDialog, "unchoose");

      skip = getImage(wizardDialog, "skip");

      return result;
    }
  }
}
