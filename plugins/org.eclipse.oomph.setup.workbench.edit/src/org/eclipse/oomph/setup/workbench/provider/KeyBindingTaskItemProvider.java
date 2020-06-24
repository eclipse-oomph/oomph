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
package org.eclipse.oomph.setup.workbench.provider;

import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;
import org.eclipse.oomph.setup.workbench.KeyBindingTask;
import org.eclipse.oomph.setup.workbench.WorkbenchFactory;
import org.eclipse.oomph.setup.workbench.WorkbenchPackage;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.workbench.KeyBindingTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class KeyBindingTaskItemProvider extends SetupTaskItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public KeyBindingTaskItemProvider(AdapterFactory adapterFactory)
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

      addSchemePropertyDescriptor(object);
      addPlatformPropertyDescriptor(object);
      addLocalePropertyDescriptor(object);
      addKeysPropertyDescriptor(object);
      addCommandPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Scheme feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addSchemePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_KeyBindingTask_scheme_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_KeyBindingTask_scheme_feature", "_UI_KeyBindingTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        WorkbenchPackage.Literals.KEY_BINDING_TASK__SCHEME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Platform feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addPlatformPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_KeyBindingTask_platform_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_KeyBindingTask_platform_feature", "_UI_KeyBindingTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        WorkbenchPackage.Literals.KEY_BINDING_TASK__PLATFORM, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Locale feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addLocalePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_KeyBindingTask_locale_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_KeyBindingTask_locale_feature", "_UI_KeyBindingTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        WorkbenchPackage.Literals.KEY_BINDING_TASK__LOCALE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Keys feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addKeysPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_KeyBindingTask_keys_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_KeyBindingTask_keys_feature", "_UI_KeyBindingTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        WorkbenchPackage.Literals.KEY_BINDING_TASK__KEYS, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Command feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addCommandPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_KeyBindingTask_command_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_KeyBindingTask_command_feature", "_UI_KeyBindingTask_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        WorkbenchPackage.Literals.KEY_BINDING_TASK__COMMAND, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
   * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
   * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object)
  {
    if (childrenFeatures == null)
    {
      super.getChildrenFeatures(object);
      childrenFeatures.add(WorkbenchPackage.Literals.KEY_BINDING_TASK__CONTEXTS);
      childrenFeatures.add(WorkbenchPackage.Literals.KEY_BINDING_TASK__COMMAND_PARAMETERS);
    }
    return childrenFeatures;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EStructuralFeature getChildFeature(Object object, Object child)
  {
    // Check the type of the specified child object and return the proper feature to use for
    // adding (see {@link AddCommand}) it as a child.

    return super.getChildFeature(object, child);
  }

  /**
   * This returns KeyBindingTask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/KeyBindingTask")); //$NON-NLS-1$
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
   * @generated NOT
   */
  @Override
  public String getText(Object object)
  {
    KeyBindingTask keyBindingTask = (KeyBindingTask)object;
    return "" + keyBindingTask.getKeys() + " = " + keyBindingTask.getCommand(); //$NON-NLS-1$ //$NON-NLS-2$
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

    switch (notification.getFeatureID(KeyBindingTask.class))
    {
      case WorkbenchPackage.KEY_BINDING_TASK__SCHEME:
      case WorkbenchPackage.KEY_BINDING_TASK__PLATFORM:
      case WorkbenchPackage.KEY_BINDING_TASK__LOCALE:
      case WorkbenchPackage.KEY_BINDING_TASK__KEYS:
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case WorkbenchPackage.KEY_BINDING_TASK__CONTEXTS:
      case WorkbenchPackage.KEY_BINDING_TASK__COMMAND_PARAMETERS:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
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

    newChildDescriptors.add(createChildParameter(WorkbenchPackage.Literals.KEY_BINDING_TASK__CONTEXTS, WorkbenchFactory.eINSTANCE.createKeyBindingContext()));

    newChildDescriptors
        .add(createChildParameter(WorkbenchPackage.Literals.KEY_BINDING_TASK__COMMAND_PARAMETERS, WorkbenchFactory.eINSTANCE.createCommandParameter()));
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
    return WorkbenchEditPlugin.INSTANCE;
  }

}
