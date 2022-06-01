/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.oomph.p2.internal.core.P2Index;
import org.eclipse.oomph.targlets.TargletContainer;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.core.ITargletContainerDescriptor;
import org.eclipse.oomph.targlets.core.ITargletContainerDescriptor.UpdateProblem;
import org.eclipse.oomph.targlets.internal.core.TargletContainerDescriptorManager;
import org.eclipse.oomph.targlets.presentation.TargletEditor;
import org.eclipse.oomph.targlets.provider.TargletItemProviderAdapterFactory;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.internal.ui.editor.targetdefinition.LocationsSection;
import org.eclipse.pde.internal.ui.editor.targetdefinition.TargetEditor;
import org.eclipse.pde.internal.ui.shared.target.TargetLocationsGroup;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.IFormPage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class TargletContainerUI extends BaseWithDeprecation implements IAdapterFactory
{
  private static final Object[] NO_CHILDREN = new Object[0];

  private final ComposedAdapterFactory adapterFactory;

  private final AdapterFactoryContentProvider contentProvider;

  private final AdapterFactoryLabelProvider labelProvider;

  private ContainerContentProvider containerContentProvider;

  private ContainerLabelProvider containerLabelProvider;

  public TargletContainerUI()
  {
    adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
    adapterFactory.addAdapterFactory(new TargletItemProviderAdapterFactory(true));

    contentProvider = new AdapterFactoryContentProvider(adapterFactory);
    labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
  }

  @Override
  public Class<?>[] getAdapterList()
  {
    return ADAPTERS;
  }

  @Override
  @SuppressWarnings("all")
  public Object getAdapter(Object object, Class adapterType)
  {
    if (object instanceof ITargletContainer)
    {
      if (adapterType == ITreeContentProvider.class)
      {
        if (containerContentProvider == null)
        {
          containerContentProvider = new ContainerContentProvider();
        }

        return containerContentProvider;
      }

      if (adapterType == ILabelProvider.class)
      {
        if (containerLabelProvider == null)
        {
          containerLabelProvider = new ContainerLabelProvider();
        }

        fixComparator();

        return containerLabelProvider;
      }

      if (adapterType == org.eclipse.pde.ui.target.ITargetLocationEditor.class || adapterType == org.eclipse.pde.ui.target.ITargetLocationUpdater.class)
      {
        return this;
      }
    }

    return null;
  }

  @Override
  public boolean canEdit(ITargetDefinition target, ITargetLocation targetLocation)
  {
    return targetLocation instanceof ITargletContainer;
  }

  @Override
  public IWizard getEditWizard(ITargetDefinition target, ITargetLocation targetLocation)
  {
    final IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    final Display display = window.getShell().getDisplay();

    UIUtil.asyncExec(display, new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          Shell activeShell = display.getActiveShell();
          Object data = activeShell.getData();
          if (data instanceof WizardDialog)
          {
            WizardDialog wizardDialog = (WizardDialog)data;

            IWizard wizard = ReflectUtil.getValue("wizard", wizardDialog); //$NON-NLS-1$
            if (wizard != null)
            {
              wizard.performFinish();
              wizardDialog.close();
            }
          }
        }
        catch (Throwable ex)
        {
          TargletsUIPlugin.INSTANCE.log(ex);
        }
      }
    });

    UIUtil.asyncExec(display, new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          Shell activeShell = display.getActiveShell();
          Object data = activeShell.getData();
          if (data instanceof PreferenceDialog)
          {
            PreferenceDialog preferenceDialog = (PreferenceDialog)data;
            ReflectUtil.invokeMethod("okPressed", preferenceDialog); //$NON-NLS-1$
          }
        }
        catch (Throwable ex)
        {
          TargletsUIPlugin.INSTANCE.log(ex);
        }
      }
    });

    IWorkbenchPage page = window.getActivePage();
    String id = ((ITargletContainer)targetLocation).getID();
    if (TargletContainerDescriptorManager.getContainer(id) == null)
    {
      IEditorPart activeEditor = page.getActiveEditor();
      String editorID = activeEditor.getSite().getId();
      if ("org.eclipse.pde.ui.targetEditor".equals(editorID)) //$NON-NLS-1$
      {
        UIUtil.asyncExec(display, () -> {
          try
          {
            activeEditor.doSave(new NullProgressMonitor());
          }
          catch (Throwable ex)
          {
            TargletsUIPlugin.INSTANCE.log(ex);
          }
        });
      }
    }

    TargletEditor.open(page, id);

    return null;
  }

  @Override
  public boolean canUpdate(ITargetDefinition target, ITargetLocation targetLocation)
  {
    return targetLocation instanceof ITargletContainer;
  }

  @Override
  public IStatus update(ITargetDefinition target, ITargetLocation targetLocation, IProgressMonitor monitor)
  {
    return ((ITargletContainer)targetLocation).updateProfile(monitor);
  }

  private void fixComparator()
  {
    try
    {
      IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
      if (activeEditor instanceof TargetEditor)
      {
        IFormPage activePageInstance = ((TargetEditor)activeEditor).getActivePageInstance();
        IManagedForm managedForm = activePageInstance.getManagedForm();
        for (IFormPart formPart : managedForm.getParts())
        {
          if (formPart instanceof LocationsSection)
          {
            TargetLocationsGroup group = ReflectUtil.getValue("fContainerGroup", formPart); //$NON-NLS-1$
            TreeViewer treeViewer = ReflectUtil.getValue("fTreeViewer", group); //$NON-NLS-1$
            ViewerComparator comparator = treeViewer.getComparator();
            if (comparator != null)
            {
              Control control = treeViewer.getControl();
              if (control.getData("oomph.fixed.comparator") == null) //$NON-NLS-1$
              {
                control.setData("oomph.fixed.comparator", Boolean.TRUE); //$NON-NLS-1$
                UIUtil.asyncExec(control, () -> {
                  treeViewer.setComparator(new FixedViewerComparator(comparator));
                });
              }
            }
          }
        }
      }
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }
  }

  private static class FixedViewerComparator extends ViewerComparator
  {
    private final ViewerComparator delegate;

    public FixedViewerComparator(ViewerComparator delegate)
    {
      this.delegate = delegate;
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2)
    {
      if ((e1 instanceof EObject || e1 instanceof Wrapper || e1 instanceof StatusWrapper || e1 instanceof UpdateProblem)
          && (e2 instanceof EObject || e2 instanceof Wrapper || e2 instanceof StatusWrapper || e1 instanceof UpdateProblem))
      {
        return 0;
      }

      return delegate.compare(viewer, e1, e2);
    }
  }

  /**
   * @author Eike Stepper
   */
  private class ContainerContentProvider implements ITreeContentProvider
  {
    private StatusWrapper missingIUInfo;

    public ContainerContentProvider()
    {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public Object[] getElements(Object element)
    {
      return getChildren(element);
    }

    @Override
    public Object[] getChildren(Object element)
    {
      if (element instanceof ITargletContainer)
      {
        ITargletContainer location = (ITargletContainer)element;
        List<Object> children = new ArrayList<>();

        TargletContainer targletContainer = TargletFactory.eINSTANCE.createTargletContainer();
        targletContainer.getComposedTargets().addAll(location.getComposedTargets());
        Object[] composedTargets = contentProvider.getChildren(targletContainer);
        for (Object object : composedTargets)
        {
          children.add(new Wrapper(object));

        }
        // children.addAll(Arrays.asList(composedTargets));

        for (Object targlet : location.getTarglets())
        {
          children.add(new Wrapper(targlet));
        }

        ITargletContainerDescriptor descriptor = location.getDescriptor();
        if (descriptor != null)
        {
          UpdateProblem updateProblem = descriptor.getUpdateProblem();
          if (updateProblem != null)
          {
            IStatus status = updateProblem.toStatus();
            children.add(new StatusWrapper(status));

            if (descriptor.getWorkingDigest() != null)
            {
              children.add(new StatusWrapper(Messages.TargletContainerUI_status_contentAvailableFromLastWorkingProfile));
            }

            try
            {
              if (updateProblem instanceof UpdateProblem.MissingIU)
              {
                if (missingIUInfo == null)
                {
                  UpdateProblem.MissingIU missingIU = (UpdateProblem.MissingIU)updateProblem;

                  VersionRange range = new VersionRange(missingIU.getRange());
                  Set<P2Index.Repository> repositories = new HashSet<>();

                  Map<P2Index.Repository, Set<Version>> simpleResult = P2Index.INSTANCE.lookupCapabilities(missingIU.getNamespace(), missingIU.getName());
                  collectRepositories(simpleResult, range, repositories);

                  Map<P2Index.Repository, Set<Version>> composedResult = P2Index.INSTANCE.generateCapabilitiesFromComposedRepositories(simpleResult);
                  collectRepositories(composedResult, range, repositories);

                  String missingIUDescription = missingIU.getNamespace() + "/" + missingIU.getName() + " " + missingIU.getRange(); //$NON-NLS-1$ //$NON-NLS-2$
                  missingIUInfo = new StatusWrapper(
                      repositories.size() == 1 ? NLS.bind(Messages.TargletContainerUI_status_foundRepository, missingIUDescription)
                          : NLS.bind(Messages.TargletContainerUI_status_foundRepositories, repositories.size(), missingIUDescription));
                  for (P2Index.Repository repository : repositories)
                  {
                    missingIUInfo.addChild(new StatusWrapper(repository.getLocation() + "  (" //$NON-NLS-1$
                        + (repository.isComposed() ? Messages.TargletContainerUI_status_composed : Messages.TargletContainerUI_status_simple) + ": " //$NON-NLS-1$
                        + repository.getCapabilityCount() + ")")); //$NON-NLS-1$
                  }
                }

                children.add(missingIUInfo);
              }
            }
            catch (Exception ex)
            {
              TargletsUIPlugin.INSTANCE.log(ex);
            }
          }
        }

        return children.toArray(new Object[children.size()]);
      }

      return NO_CHILDREN;
    }

    @Override
    public boolean hasChildren(Object element)
    {
      return getChildren(element).length != 0;
    }

    @Override
    public Object getParent(Object element)
    {
      return null;
    }

    private void collectRepositories(Map<P2Index.Repository, Set<Version>> result, VersionRange range, Set<P2Index.Repository> repositories)
    {
      for (Map.Entry<P2Index.Repository, Set<Version>> entry : result.entrySet())
      {
        for (Version version : entry.getValue())
        {
          if (range.isIncluded(version))
          {
            repositories.add(entry.getKey());
            break;
          }
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static class ContainerLabelProvider extends LabelProvider
  {
    public ContainerLabelProvider()
    {
    }

    @Override
    public Image getImage(Object element)
    {
      if (element instanceof ITargletContainer)
      {
        ITargletContainer location = (ITargletContainer)element;
        String key = "targlet_container"; //$NON-NLS-1$

        ITargletContainerDescriptor descriptor = location.getDescriptor();
        if (descriptor != null)
        {
          UpdateProblem updateProblem = descriptor.getUpdateProblem();
          if (updateProblem != null)
          {
            key += "_problem"; //$NON-NLS-1$
          }
        }

        return TargletsUIPlugin.INSTANCE.getSWTImage(key);
      }

      return super.getImage(element);
    }

    @Override
    public String getText(Object element)
    {
      if (element instanceof ITargletContainer)
      {
        return ((ITargletContainer)element).toString();
      }

      return super.getText(element);
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class StatusWrapper implements ITreeContentProvider, ILabelProvider
  {
    private final int severity;

    private final String message;

    private final List<StatusWrapper> children = new ArrayList<>();

    public StatusWrapper(int severity, String message)
    {
      this.severity = severity;
      this.message = message;
    }

    public StatusWrapper(IStatus delegate)
    {
      this(delegate.getSeverity(), delegate.getMessage());

      for (IStatus child : delegate.getChildren())
      {
        children.add(new StatusWrapper(child));
      }
    }

    public StatusWrapper(String message)
    {
      this(IStatus.INFO, message);
    }

    public void addChild(StatusWrapper child)
    {
      children.add(child);
    }

    @Override
    public void dispose()
    {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    @Override
    public Object[] getElements(Object inputElement)
    {
      return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parentElement)
    {
      return children.toArray();
    }

    @Override
    public Object getParent(Object element)
    {
      return null;
    }

    @Override
    public boolean hasChildren(Object element)
    {
      return !children.isEmpty();
    }

    @Override
    public void addListener(ILabelProviderListener listener)
    {
    }

    @Override
    public boolean isLabelProperty(Object element, String property)
    {
      return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener)
    {
    }

    @Override
    public Image getImage(Object element)
    {
      return UIUtil.getStatusImage(severity);
    }

    @Override
    public String getText(Object element)
    {
      return message;
    }

    @Override
    public String toString()
    {
      return message;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class Wrapper implements ITreeContentProvider, ILabelProvider
  {
    private final Object wrappedObject;

    public Wrapper(Object wrappedObject)
    {
      this.wrappedObject = wrappedObject;
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
      contentProvider.inputChanged(viewer, oldInput, newInput);
    }

    @Override
    public Object[] getElements(Object object)
    {
      return getChildren(wrappedObject);
    }

    @Override
    public Object[] getChildren(Object object)
    {
      Object[] children = contentProvider.getChildren(wrappedObject);
      for (int i = 0; i < children.length; i++)
      {
        children[i] = new Wrapper(children[i]);
      }

      return children;
    }

    @Override
    public boolean hasChildren(Object object)
    {
      return contentProvider.hasChildren(wrappedObject);
    }

    @Override
    public Object getParent(Object object)
    {
      return contentProvider.getParent(wrappedObject);
    }

    @Override
    public void addListener(ILabelProviderListener listener)
    {
      labelProvider.addListener(listener);
    }

    @Override
    public void removeListener(ILabelProviderListener listener)
    {
      labelProvider.removeListener(listener);
    }

    @Override
    public void dispose()
    {
      labelProvider.dispose();
    }

    @Override
    public boolean isLabelProperty(Object object, String id)
    {
      return labelProvider.isLabelProperty(wrappedObject, id);
    }

    @Override
    public Image getImage(Object object)
    {
      return labelProvider.getImage(wrappedObject);
    }

    @Override
    public String getText(Object object)
    {
      return labelProvider.getText(wrappedObject);
    }
  }
}

@SuppressWarnings("deprecation")
class BaseWithDeprecation implements org.eclipse.pde.ui.target.ITargetLocationEditor, org.eclipse.pde.ui.target.ITargetLocationUpdater
{
  static final Class<?>[] ADAPTERS = { ITreeContentProvider.class, ILabelProvider.class, org.eclipse.pde.ui.target.ITargetLocationEditor.class,
      org.eclipse.pde.ui.target.ITargetLocationUpdater.class };

  @Override
  public boolean canUpdate(ITargetDefinition target, ITargetLocation targetLocation)
  {
    return false;
  }

  @Override
  public IStatus update(ITargetDefinition target, ITargetLocation targetLocation, IProgressMonitor monitor)
  {
    return null;
  }

  @Override
  public boolean canEdit(ITargetDefinition target, ITargetLocation targetLocation)
  {
    return false;
  }

  @Override
  public IWizard getEditWizard(ITargetDefinition target, ITargetLocation targetLocation)
  {
    return null;
  }
}
