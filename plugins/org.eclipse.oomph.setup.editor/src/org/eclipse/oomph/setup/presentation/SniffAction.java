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
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTask.Sniffer;
import org.eclipse.oomph.setup.SetupTask.Sniffer.ResourceHandler;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.impl.SetupTaskImpl.BasicSniffer;
import org.eclipse.oomph.setup.presentation.templates.AutomaticProjectTemplate;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.actions.AbstractContainerAction;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class SniffAction extends AbstractContainerAction
{
  private static final String TITLE = "Import From IDE";

  private List<Sniffer> sniffers;

  private Map<Sniffer, List<Sniffer>> dependenciesMap = new HashMap<Sniffer, List<Sniffer>>();

  public SniffAction(boolean withDialog)
  {
    super(TITLE);
    setToolTipText("Imports tasks from the running IDE into the selected setup task container");
    setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(SetupEditorPlugin.PLUGIN_ID, "sniff"));
  }

  @Override
  protected boolean runInit(SetupTaskContainer container)
  {

    sniffers = getSniffers();
    if (sniffers.isEmpty())
    {
      MessageDialog.openInformation(UIUtil.getShell(), AbstractSetupDialog.SHELL_TEXT, "No task importers available.");
      return false;
    }

    boolean ok = new SelectSniffersDialog(UIUtil.getShell()).open() == SelectSniffersDialog.OK;
    return ok && !sniffers.isEmpty();
  }

  @Override
  protected void runModify(final SetupTaskContainer container)
  {
    try
    {
      UIUtil.runInProgressDialog(UIUtil.getShell(), new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          int totalWork = 0;
          for (Sniffer sniffer : sniffers)
          {
            int work = sniffer.getWork();
            totalWork += work;
          }

          monitor.beginTask("Importing from IDE", totalWork);

          try
          {
            CompoundTask compound = SetupFactory.eINSTANCE.createCompoundTask("Import from " + new Date());
            compound.setDisabled(true);
            container.getSetupTasks().add(compound);

            handleWorkspace();

            for (Sniffer sniffer : sniffers)
            {
              monitor.subTask(sniffer.getLabel());

              try
              {
                List<Sniffer> dependencies = dependenciesMap.get(sniffer);
                dependencies.retainAll(sniffers);

                IProgressMonitor subMonitor = new SubProgressMonitor(monitor, sniffer.getWork());
                sniffer.sniff(compound, dependencies, subMonitor);
              }
              catch (NoClassDefFoundError ex)
              {
                SetupEditorPlugin.getPlugin().log(ex);
              }
              catch (Exception ex)
              {
                SetupEditorPlugin.getPlugin().log(ex);
              }
            }
          }
          finally
          {
            monitor.done();
          }
        }
      });
    }
    catch (Exception ex)
    {
      SetupEditorPlugin.getPlugin().log(ex);
    }
  }

  @Override
  protected void runDone(SetupTaskContainer container)
  {
    sniffers = null;
    dependenciesMap.clear();
    super.runDone(container);
  }

  private EList<Sniffer> getSniffers()
  {
    final EList<Sniffer> sniffers = new BasicEList<Sniffer>();
    for (EClassifier eClassifier : SetupPackage.eINSTANCE.getEClassifiers())
    {
      if (eClassifier instanceof EClass)
      {
        EClass eClass = (EClass)eClassifier;
        if (!eClass.isAbstract() && SetupPackage.Literals.SETUP_TASK.isSuperTypeOf(eClass))
        {
          try
          {
            SetupTask task = (SetupTask)EcoreUtil.create(eClass);
            task.collectSniffers(sniffers);
          }
          catch (NoClassDefFoundError ex)
          {
            SetupEditorPlugin.getPlugin().log(ex);
          }
        }
      }
    }

    ECollections.sort(sniffers, new Comparator<Sniffer>()
    {
      public int compare(Sniffer sniffer1, Sniffer sniffer2)
      {
        return sniffer1.getPriority() - sniffer2.getPriority();
      }
    });

    EMFUtil.reorder(sniffers, new EMFUtil.DependencyProvider<Sniffer>()
    {
      public Collection<? extends Sniffer> getDependencies(Sniffer sniffer)
      {
        List<Sniffer> dependencies = new ArrayList<Sniffer>(sniffers);
        sniffer.retainDependencies(dependencies);
        dependenciesMap.put(sniffer, dependencies);
        return dependencies;
      }
    });

    return sniffers;
  }

  private void handleWorkspace()
  {
    final List<Sniffer> resourceHandlers = new ArrayList<Sniffer>(sniffers);
    BasicSniffer.retainType(resourceHandlers, ResourceHandler.class);
    if (!resourceHandlers.isEmpty())
    {
      try
      {
        ResourcesPlugin.getWorkspace().getRoot().accept(new IResourceVisitor()
        {
          public boolean visit(IResource resource) throws CoreException
          {
            for (Sniffer sniffer : resourceHandlers)
            {
              try
              {
                ((ResourceHandler)sniffer).handleResource(resource);
              }
              catch (Exception ex)
              {
                // Ignore
              }
            }

            return true;
          }
        });
      }
      catch (CoreException ex)
      {
        SetupEditorPlugin.getPlugin().log(ex);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class SelectSniffersDialog extends AbstractSetupDialog
  {
    private static final String LAST_SNIFFERS = "lastSniffers";

    private CheckboxTableViewer viewer;

    public SelectSniffersDialog(Shell parentShell)
    {
      super(parentShell, TITLE, 500, 600, SetupEditorPlugin.INSTANCE, null);
    }

    @Override
    protected String getDefaultMessage()
    {
      return "Check the importers of your choice and click the Import button.";
    }

    @Override
    protected void createUI(Composite parent)
    {
      SashForm sashForm = new SashForm(parent, SWT.VERTICAL | SWT.SMOOTH);
      sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

      GridLayout layout = new GridLayout();
      layout.marginWidth = 0;
      layout.marginHeight = 0;
      layout.verticalSpacing = 0;

      Composite upper = new Composite(sashForm, SWT.NONE);
      upper.setLayout(layout);

      viewer = CheckboxTableViewer.newCheckList(upper, SWT.NONE);
      UIUtil.grabVertical(UIUtil.applyGridData(viewer.getTable()));
      UIUtil.applyGridData(createSeparator(upper));

      Composite lower = new Composite(sashForm, SWT.NONE);
      lower.setLayout(new GridLayout());

      final Label descriptionLabel = new Label(lower, SWT.WRAP);
      UIUtil.grabVertical(UIUtil.applyGridData(descriptionLabel));

      viewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
          Sniffer sniffer = (Sniffer)((IStructuredSelection)event.getSelection()).getFirstElement();
          if (sniffer != null)
          {
            descriptionLabel.setText(StringUtil.safe(sniffer.getDescription()));
          }
        }
      });

      viewer.addCheckStateListener(new ICheckStateListener()
      {
        public void checkStateChanged(CheckStateChangedEvent event)
        {
          Object element = event.getElement();
          viewer.setSelection(new StructuredSelection(element));

          updateEnablement();
        }
      });

      viewer.setContentProvider(new ArrayContentProvider());
      viewer.setLabelProvider(new SnifferLabelProvider());
      viewer.setInput(sniffers);

      Table table = viewer.getTable();
      table.setLayoutData(new GridData(GridData.FILL_BOTH));

      String[] labels = getSettings().getArray(LAST_SNIFFERS);
      if (labels != null)
      {
        HashSet<String> toCheck = new HashSet<String>(Arrays.asList(labels));
        for (Sniffer sniffer : sniffers)
        {
          if (toCheck.contains(sniffer.getLabel()))
          {
            viewer.setChecked(sniffer, true);
          }
        }
      }

      sashForm.setWeights(new int[] { 5, 1 });
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
      super.createButtonsForButtonBar(parent);
      updateEnablement();
    }

    @Override
    protected void okPressed()
    {
      List<Object> checkedElements = Arrays.asList(viewer.getCheckedElements());
      sniffers.retainAll(checkedElements);

      List<String> labels = new ArrayList<String>();
      for (Sniffer sniffer : sniffers)
      {
        labels.add(sniffer.getLabel());
      }

      getSettings().put(LAST_SNIFFERS, labels.toArray(new String[labels.size()]));
      super.okPressed();
    }

    private void updateEnablement()
    {
      Button button = getButton(IDialogConstants.OK_ID);
      if (button != null)
      {
        button.setText("Import");
        button.setEnabled(viewer.getCheckedElements().length != 0);
      }
    }

    private IDialogSettings getSettings()
    {
      IDialogSettings dialogSettings = SetupEditorPlugin.getPlugin().getDialogSettings();
      IDialogSettings section = dialogSettings.getSection(AutomaticProjectTemplate.class.getName());
      if (section == null)
      {
        section = dialogSettings.addNewSection(SelectSniffersDialog.class.getName());
      }

      return section;
    }

    /**
     * @author Eike Stepper
     */
    private final class SnifferLabelProvider extends LabelProvider
    {
      @Override
      public Image getImage(Object element)
      {
        if (element instanceof Sniffer)
        {
          Sniffer sniffer = (Sniffer)element;
          return ExtendedImageRegistry.INSTANCE.getImage(sniffer.getIcon());
        }

        return super.getImage(element);
      }
    }
  }
}
