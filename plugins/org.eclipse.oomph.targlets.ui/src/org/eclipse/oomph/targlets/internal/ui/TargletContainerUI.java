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
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.oomph.p2.internal.core.P2Index;
import org.eclipse.oomph.targlets.internal.core.TargletContainer;
import org.eclipse.oomph.targlets.internal.core.TargletContainerDescriptor;
import org.eclipse.oomph.targlets.internal.core.TargletContainerDescriptor.UpdateProblem;
import org.eclipse.oomph.targlets.presentation.TargletEditor;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.internal.ui.shared.target.StyledBundleLabelProvider;
import org.eclipse.pde.ui.target.ITargetLocationEditor;
import org.eclipse.pde.ui.target.ITargetLocationUpdater;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class TargletContainerUI implements IAdapterFactory, ITargetLocationEditor, ITargetLocationUpdater
{
  private static final Class<?>[] ADAPTERS = { ITreeContentProvider.class, ILabelProvider.class, ITargetLocationEditor.class, ITargetLocationUpdater.class };

  private static final Object[] NO_CHILDREN = new Object[0];

  private ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

  private AdapterFactoryContentProvider contentProvider = new AdapterFactoryContentProvider(adapterFactory);

  private AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(adapterFactory);

  public TargletContainerUI()
  {
  }

  @SuppressWarnings("rawtypes")
  public Class[] getAdapterList()
  {
    return ADAPTERS;
  }

  public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType)
  {
    if (adaptableObject instanceof TargletContainer)
    {
      if (adapterType == ITreeContentProvider.class)
      {
        return new ContainerContentProvider();
      }

      if (adapterType == ILabelProvider.class)
      {
        return new ContainerLabelProvider();
      }

      if (adapterType == ITargetLocationEditor.class)
      {
        return this;
      }

      if (adapterType == ITargetLocationUpdater.class)
      {
        return this;
      }
    }

    return null;
  }

  public boolean canEdit(ITargetDefinition target, ITargetLocation targetLocation)
  {
    return targetLocation instanceof TargletContainer;
  }

  public IWizard getEditWizard(ITargetDefinition target, ITargetLocation targetLocation)
  {
    try
    {
      TargletEditor.open(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), (TargletContainer)targetLocation);
    }
    catch (PartInitException ex)
    {
      ex.printStackTrace();
    }

    simulateEscapeKey();
    simulateEscapeKey();

    return null;
  }

  public boolean canUpdate(ITargetDefinition target, ITargetLocation targetLocation)
  {
    return targetLocation instanceof TargletContainer;
  }

  public IStatus update(ITargetDefinition target, ITargetLocation targetLocation, IProgressMonitor monitor)
  {
    return ((TargletContainer)targetLocation).updateProfile(monitor);
  }

  private static void simulateEscapeKey()
  {
    Display display = UIUtil.getDisplay();

    Event event = new Event();
    event.type = SWT.KeyDown;
    event.character = SWT.ESC;
    display.post(event);

    try
    {
      Thread.sleep(10);
    }
    catch (InterruptedException ex)
    {
    }

    event.type = SWT.KeyUp;
    display.post(event);
    display.post(event);

    try
    {
      Thread.sleep(10);
    }
    catch (InterruptedException ex)
    {
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

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    public void dispose()
    {
    }

    public Object[] getElements(Object element)
    {
      return getChildren(element);
    }

    public Object[] getChildren(Object element)
    {
      if (element instanceof TargletContainer)
      {
        TargletContainer location = (TargletContainer)element;
        List<Object> children = new ArrayList<Object>();

        TargletContainerDescriptor descriptor = location.getDescriptor();
        if (descriptor != null)
        {
          UpdateProblem updateProblem = descriptor.getUpdateProblem();
          if (updateProblem != null)
          {
            IStatus status = updateProblem.toStatus();
            children.add(new StatusWrapper(status));

            if (descriptor.getWorkingDigest() != null)
            {
              children.add(new StatusWrapper("Content is available from the last working profile"));
            }

            try
            {
              if (updateProblem instanceof UpdateProblem.MissingIU)
              {
                if (missingIUInfo == null)
                {
                  UpdateProblem.MissingIU missingIU = (UpdateProblem.MissingIU)updateProblem;

                  VersionRange range = new VersionRange(missingIU.getRange());
                  Set<P2Index.Repository> repositories = new HashSet<P2Index.Repository>();

                  Map<P2Index.Repository, Set<Version>> simpleResult = P2Index.INSTANCE.lookupCapabilities(missingIU.getNamespace(), missingIU.getName());
                  collectRepositories(simpleResult, range, repositories);

                  Map<P2Index.Repository, Set<Version>> composedResult = P2Index.INSTANCE.generateCapabilitiesFromComposedRepositories(simpleResult);
                  collectRepositories(composedResult, range, repositories);

                  if (!repositories.isEmpty())
                  {
                    missingIUInfo = new StatusWrapper("Found " + repositories.size() + " " + "repositories that satisfy " + missingIU.getNamespace() + "/"
                        + missingIU.getName() + " " + missingIU.getRange());
                    for (P2Index.Repository repository : repositories)
                    {
                      missingIUInfo.addChild(new StatusWrapper(repository.getLocation().toString() + "  (" + (repository.isComposed() ? "composed" : "simple")
                          + ": " + repository.getCapabilityCount()));
                    }
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
          else
          {
            for (Object targlet : location.getTarglets())
            {
              children.add(new Wrapper(targlet));
            }
          }
        }

        return children.toArray(new Object[children.size()]);
      }

      return NO_CHILDREN;
    }

    public boolean hasChildren(Object element)
    {
      return getChildren(element).length != 0;
    }

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
  private static class ContainerLabelProvider extends StyledBundleLabelProvider
  {
    public ContainerLabelProvider()
    {
      super(true, false);
    }

    @Override
    public Image getImage(Object element)
    {
      if (element instanceof TargletContainer)
      {
        TargletContainer location = (TargletContainer)element;
        String key = "targlet_container";

        TargletContainerDescriptor descriptor = location.getDescriptor();
        if (descriptor != null)
        {
          UpdateProblem updateProblem = descriptor.getUpdateProblem();
          if (updateProblem != null)
          {
            key += "_problem";
          }
        }

        return TargletsUIPlugin.INSTANCE.getSWTImage(key);
      }

      return super.getImage(element);
    }

    @Override
    public String getText(Object element)
    {
      if (element instanceof TargletContainer)
      {
        return ((TargletContainer)element).toString();
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

    private final List<StatusWrapper> children = new ArrayList<StatusWrapper>();

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

    public void dispose()
    {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    public Object[] getElements(Object inputElement)
    {
      return getChildren(inputElement);
    }

    public Object[] getChildren(Object parentElement)
    {
      return children.toArray();
    }

    public Object getParent(Object element)
    {
      return null;
    }

    public boolean hasChildren(Object element)
    {
      return !children.isEmpty();
    }

    public void addListener(ILabelProviderListener listener)
    {
    }

    public boolean isLabelProperty(Object element, String property)
    {
      return false;
    }

    public void removeListener(ILabelProviderListener listener)
    {
    }

    public Image getImage(Object element)
    {
      if (severity == IStatus.WARNING)
      {
        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_WARN_TSK);
      }

      if (severity == IStatus.ERROR)
      {
        return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
      }

      return TargletsUIPlugin.INSTANCE.getSWTImage("info");
    }

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

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
      contentProvider.inputChanged(viewer, oldInput, newInput);
    }

    public Object[] getElements(Object object)
    {
      return getChildren(wrappedObject);
    }

    public Object[] getChildren(Object object)
    {
      Object[] children = contentProvider.getChildren(wrappedObject);
      for (int i = 0; i < children.length; i++)
      {
        children[i] = new Wrapper(children[i]);
      }

      return children;
    }

    public boolean hasChildren(Object object)
    {
      return contentProvider.hasChildren(wrappedObject);
    }

    public Object getParent(Object object)
    {
      return contentProvider.getParent(wrappedObject);
    }

    public void addListener(ILabelProviderListener listener)
    {
      labelProvider.addListener(listener);
    }

    public void removeListener(ILabelProviderListener listener)
    {
      labelProvider.removeListener(listener);
    }

    public void dispose()
    {
      labelProvider.dispose();
    }

    public boolean isLabelProperty(Object object, String id)
    {
      return labelProvider.isLabelProperty(wrappedObject, id);
    }

    public Image getImage(Object object)
    {
      return labelProvider.getImage(wrappedObject);
    }

    public String getText(Object object)
    {
      return labelProvider.getText(wrappedObject);
    }
  }
}
