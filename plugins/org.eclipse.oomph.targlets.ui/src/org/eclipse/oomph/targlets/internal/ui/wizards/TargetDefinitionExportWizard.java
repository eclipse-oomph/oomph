/*
 * Copyright (c) 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.ui.wizards;

import org.eclipse.oomph.targlets.internal.ui.TargletsUIPlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.MonitorUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.TargetBundle;
import org.eclipse.pde.core.target.TargetFeature;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class TargetDefinitionExportWizard extends Wizard implements IExportWizard
{
  public static final String TITLE = "Export Target Definition";

  private TargetDefinitionExportWizardPage page;

  public TargetDefinitionExportWizard()
  {
    setWindowTitle(TITLE);
    setDefaultPageImageDescriptor(TargletsUIPlugin.INSTANCE.getImageDescriptor("target_wiz.png"));
  }

  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
  }

  @Override
  public void addPages()
  {
    addPage(page = new TargetDefinitionExportWizardPage());
  }

  @Override
  public boolean performFinish()
  {
    final ITargetDefinition targetDefinition = page.getTargetDefinition();
    final File exportFolder = page.getExportFolder();

    new Job("Export Target Definition")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        try
        {
          boolean needsResolution = !targetDefinition.isResolved();
          monitor.beginTask("", needsResolution ? 2 : 1);

          if (needsResolution)
          {
            targetDefinition.resolve(MonitorUtil.create(monitor, 1));
          }

          copy(MonitorUtil.create(monitor, 1));
        }
        catch (Exception ex)
        {
          return TargletsUIPlugin.INSTANCE.getStatus(ex);
        }
        finally
        {
          monitor.done();
        }

        return Status.OK_STATUS;
      }

      private void copy(IProgressMonitor monitor)
      {
        TargetFeature[] features = targetDefinition.getAllFeatures();
        int featuresCount = features != null ? features.length : 0;

        TargetBundle[] bundles = targetDefinition.getAllBundles();
        int bundlesCount = bundles != null ? bundles.length : 0;

        monitor.beginTask("", featuresCount + bundlesCount);

        try
        {
          if (featuresCount != 0)
          {
            for (TargetFeature feature : features)
            {
              TargletsUIPlugin.checkCancelation(monitor);

              String source = feature.getLocation();
              monitor.subTask(source);

              copy(source, new File(exportFolder, "features"));
              monitor.worked(1);
            }
          }

          if (bundlesCount != 0)
          {
            for (TargetBundle bundle : bundles)
            {
              TargletsUIPlugin.checkCancelation(monitor);

              String source = bundle.getBundleInfo().getLocation().getPath();
              monitor.subTask(source);

              copy(source, new File(exportFolder, "plugins"));
              monitor.worked(1);
            }
          }
        }
        finally
        {
          monitor.done();
        }
      }

      private void copy(String source, File target)
      {
        File sourceFile = new File(source);
        File targetFile = new File(target, sourceFile.getName());

        IOUtil.copyTree(sourceFile, targetFile);
      }
    }.schedule();

    return true;
  }
}
