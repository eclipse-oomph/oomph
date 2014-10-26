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
package org.eclipse.oomph.setup.doc.user.wizard;

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.internal.ui.Capture;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.setup.ui.wizards.SetupWizardPage;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ignore
 */
public abstract class CaptureSetupWizard extends Capture.Window<WizardDialog>
{
  protected SetupWizard getSetupWizard(WizardDialog wizardDialog)
  {
    return (SetupWizard)wizardDialog.getCurrentPage().getWizard();
  }

  protected SetupContext getSetupContext(WizardDialog wizardDialog)
  {
    return getSetupWizard(wizardDialog).getSetupContext();
  }

  protected ResourceSet getResourceSet(WizardDialog wizardDialog)
  {
    return getSetupWizard(wizardDialog).getResourceSet();
  }

  protected SetupTaskPerformer getPerformer(WizardDialog wizardDialog)
  {
    return getSetupWizard(wizardDialog).getPerformer();
  }

  protected void advanceToNextPage(WizardDialog wizardDialog)
  {
    ((SetupWizardPage)wizardDialog.getCurrentPage()).advanceToNextPage();
  }

  protected void regressToPreviousPage(WizardDialog wizardDialog)
  {
    ReflectUtil.invokeMethod("backPressed", wizardDialog);
  }

  @SuppressWarnings("unchecked")
  protected <T extends Viewer> T getViewer(WizardDialog wizardDialog, String fieldName)
  {
    return (T)ReflectUtil.getValue(fieldName, wizardDialog.getCurrentPage());
  }

  @SuppressWarnings("unchecked")
  protected <T extends Control> T getViewerControl(WizardDialog wizardDialog, String fieldName)
  {
    return (T)getViewer(wizardDialog, fieldName).getControl();
  }

  protected void postProcessProductPage(WizardDialog wizardDialog)
  {
    getViewerControl(wizardDialog, "productViewer").setFocus();

    ResourceSet resourceSet = getResourceSet(wizardDialog);
    ProductVersion luna = (ProductVersion)resourceSet
        .getEObject(
            URI.createURI("index:/org.eclipse.setup#//@productCatalogs[name='org.eclipse.products']/@products[name='epp.package.standard']/@versions[name='luna']"),
            false);

    TreeViewer productViewer = getViewer(wizardDialog, "productViewer");
    productViewer.setSelection(new StructuredSelection(luna.getProduct()));

    AccessUtil.busyWait(10);

    ComboViewer versionComboViewer = getViewer(wizardDialog, "versionComboViewer");
    versionComboViewer.setSelection(new StructuredSelection(luna));

    ComboViewer poolComboViewer = getViewer(wizardDialog, "poolComboViewer");
    poolComboViewer.getCombo().select(0);

    Link link = getWidget(wizardDialog, "version");
    link.setText("<a>1.0.0 Build 1234</a>");
    link.getParent().layout(true);
  }

  protected void postProcessProjectPage(WizardDialog wizardDialog)
  {
    ResourceSet resourceSet = getResourceSet(wizardDialog);
    ProjectCatalog projectCatalog = (ProjectCatalog)resourceSet.getResource(URI.createURI("index:/org.eclipse.projects.setup"), false).getContents().get(0);

    TreeViewer projectViewer = getViewer(wizardDialog, "projectViewer");
    projectViewer.getControl().setFocus();

    for (Iterator<Project> it = projectCatalog.getProjects().iterator(); it.hasNext();)
    {
      Project project = it.next();
      String label = project.getLabel();
      if (!"Oomph".equals(label))
      {
        if (!"<User>".equals(label))
        {
          it.remove();
        }
      }
      else
      {
        projectViewer.setSelection(new StructuredSelection(project));
      }
    }

    ReflectUtil.invokeMethod(ReflectUtil.getMethod(projectViewer, "fireDoubleClick", DoubleClickEvent.class), projectViewer, (Object)null);
  }

  protected void postProcessUser(WizardDialog wizardDialog)
  {
    List<EObject> objectsToRemove = new ArrayList<EObject>();
    for (TreeIterator<EObject> it = getSetupContext(wizardDialog).getUser().eAllContents(); it.hasNext();)
    {
      EObject eObject = it.next();
      if (eObject instanceof SetupTask && !(eObject instanceof CompoundTask || eObject instanceof VariableTask))
      {
        objectsToRemove.add(eObject);
        it.prune();
      }
    }

    for (EObject eObject : objectsToRemove)
    {
      EcoreUtil.remove(eObject);
    }

  }

  protected void postProcessVariablePage(WizardDialog wizardDialog, String installationID)
  {
    postProcessUser(wizardDialog);

    ReflectUtil.setValue("save", wizardDialog.getCurrentPage(), false);
    Button showAllButton = getWidget(wizardDialog, "showAll");
    showAllButton.setSelection(true);
    showAllButton.notifyListeners(SWT.Selection, new Event());
    AccessUtil.busyWait(300);

    Text text = getWidget(wizardDialog, "installation.id.control");
    text.setText(installationID);
    text.notifyListeners(SWT.Modify, null);

    Combo combo = getWidget(wizardDialog, "git.clone.oomph.remoteURI.control");
    combo.setText("ssh://${git.user.id}@git.eclipse.org:29418/oomph/org.eclipse.oomph");
    combo.notifyListeners(SWT.Modify, null);
    AccessUtil.busyWait(100);
  }

  protected void postProcessConfirmationPage(WizardDialog wizardDialog, boolean all)
  {
    AccessUtil.busyWait(100);

    CheckboxTreeViewer taskViewer = getViewer(wizardDialog, "viewer");
    taskViewer.getControl().setFocus();

    if (all)
    {
      Button showAll = getWidget(wizardDialog, "showAllTasks");
      showAll.setSelection(true);
      showAll.notifyListeners(SWT.Selection, new Event());
    }

    Button overwrite = getWidget(wizardDialog, "overwrite");
    if (overwrite != null)
    {
      overwrite.setSelection(true);
      overwrite.notifyListeners(SWT.Selection, new Event());
      AccessUtil.busyWait(10);
    }

    {
      ITreeContentProvider provider = (ITreeContentProvider)taskViewer.getContentProvider();
      Object[] children = provider.getChildren(taskViewer.getInput());
      for (Object object : provider.getChildren(children[0]))
      {
        if (object instanceof P2Task)
        {
          taskViewer.setSelection(new StructuredSelection(object));
          break;
        }
      }
    }

    AccessUtil.busyWait(10);

    TreeViewer childrenViewer = getViewer(wizardDialog, "childrenViewer");
    childrenViewer.getControl().setFocus();

    {
      ITreeContentProvider provider = (ITreeContentProvider)childrenViewer.getContentProvider();
      Object[] children = provider.getChildren(childrenViewer.getInput());
      childrenViewer.setSelection(new StructuredSelection(children[0]));
    }

    SashForm hsash = getWidget(wizardDialog, "hsash");
    hsash.setWeights(new int[] { 12, 9 });

    SashForm vsash = getWidget(wizardDialog, "vsash");
    vsash.setWeights(new int[] { 2, 1 });

    AccessUtil.busyWait(10);
  }

  protected Image getCalloutImage(int index)
  {
    Image image = ExtendedImageRegistry.INSTANCE.getImage(URI.createPlatformPluginURI("org.eclipse.oomph.setup.doc/images/callout-" + index + ".png", false));
    return new Image(image.getDevice(), image.getImageData());
  }
}
