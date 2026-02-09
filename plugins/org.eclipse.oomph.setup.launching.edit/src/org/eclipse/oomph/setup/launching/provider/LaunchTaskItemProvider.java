/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.launching.provider;

import org.eclipse.oomph.setup.ResourceCreationTask;
import org.eclipse.oomph.setup.launching.LaunchTask;
import org.eclipse.oomph.setup.launching.LaunchingPackage;
import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.launching.LaunchTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class LaunchTaskItemProvider extends SetupTaskItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LaunchTaskItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  /**
   * This returns the property descriptors for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
  {
    if (itemPropertyDescriptors == null)
    {
      super.getPropertyDescriptors(object);

      addLauncherPropertyDescriptor(object);
      addRunEveryStartupPropertyDescriptor(object);
      addStopOnFailurePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Launcher feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addLauncherPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_LaunchTask_launcher_feature"), //$NON-NLS-1$
        getString("_UI_LaunchTask_launcher_description"), //$NON-NLS-1$
        LaunchingPackage.Literals.LAUNCH_TASK__LAUNCHER, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  @Override
  protected boolean isChoiceArbitrary(EStructuralFeature feature, Object object)
  {
    return feature == LaunchingPackage.Literals.LAUNCH_TASK__LAUNCHER || super.isChoiceArbitrary(feature, object);
  }

  @Override
  protected Collection<?> filterChoices(Collection<?> choices, EStructuralFeature feature, Object object)
  {
    if (feature == LaunchingPackage.Literals.LAUNCH_TASK__LAUNCHER)
    {
      ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
      try
      {
        List<String> result = new ArrayList<>();
        for (ILaunchConfiguration launchConfiguration : launchManager.getLaunchConfigurations())
        {
          result.add(launchConfiguration.getName());
        }

        return result;
      }
      catch (CoreException ex)
      {
        //$FALL-THROUGH$
      }
    }

    return super.filterChoices(choices, feature, object);
  }

  @SuppressWarnings("nls")
  @Override
  protected Command createPrimaryDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> collection)
  {
    if (owner instanceof EObject)
    {
      for (Object object : collection)
      {
        if (object instanceof ResourceCreationTask)
        {
          ResourceCreationTask resourceCreationTask = (ResourceCreationTask)object;
          String content = resourceCreationTask.getContent();
          if (content != null && content.startsWith("<?xml ") && content.contains("<launchConfiguration"))
          {
            String targetURL = resourceCreationTask.getTargetURL();
            if (targetURL != null)
            {
              URI uri = URI.createURI(targetURL);
              if ("launch".equals(uri.fileExtension()))
              {
                String name = uri.trimFileExtension().lastSegment();
                if (name != null)
                {
                  return new BaseDragAndDropCommand(domain, owner, location, operations, operation, collection)
                  {
                    @Override
                    protected boolean prepareDropLinkOn()
                    {
                      dragCommand = IdentityCommand.INSTANCE;
                      dropCommand = createSetCommand(domain, (EObject)owner, LaunchingPackage.Literals.LAUNCH_TASK__LAUNCHER, URI.decode(name));
                      return dropCommand.canExecute();
                    }
                  };
                }
              }
            }
          }
        }
      }
    }

    return super.createPrimaryDragAndDropCommand(domain, owner, location, operations, operation, collection);
  }

  /**
   * This adds a property descriptor for the Run Every Startup feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRunEveryStartupPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_LaunchTask_runEveryStartup_feature"), //$NON-NLS-1$
        getString("_UI_LaunchTask_runEveryStartup_description"), //$NON-NLS-1$
        LaunchingPackage.Literals.LAUNCH_TASK__RUN_EVERY_STARTUP, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Stop On Failure feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addStopOnFailurePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_LaunchTask_stopOnFailure_feature"), //$NON-NLS-1$
        getString("_UI_LaunchTask_stopOnFailure_description"), //$NON-NLS-1$
        LaunchingPackage.Literals.LAUNCH_TASK__STOP_ON_FAILURE, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This returns LaunchTask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/LaunchTask")); //$NON-NLS-1$
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected boolean shouldComposeCreationImage()
  {
    return true;
  }

  /**
   * This returns the label text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getText(Object object)
  {
    String label = ((LaunchTask)object).getLauncher();
    return label == null || label.length() == 0 ? getString("_UI_LaunchTask_type") : //$NON-NLS-1$
        getString("_UI_LaunchTask_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void notifyChanged(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(LaunchTask.class))
    {
      case LaunchingPackage.LAUNCH_TASK__LAUNCHER:
      case LaunchingPackage.LAUNCH_TASK__RUN_EVERY_STARTUP:
      case LaunchingPackage.LAUNCH_TASK__STOP_ON_FAILURE:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
    }
    super.notifyChanged(notification);
  }

  /**
   * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
   * that can be created under this object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object)
  {
    super.collectNewChildDescriptors(newChildDescriptors, object);
  }

  /**
   * Return the resource locator for this item provider's resources.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceLocator getResourceLocator()
  {
    return LaunchingEditPlugin.INSTANCE;
  }

}
