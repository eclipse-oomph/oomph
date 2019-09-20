/*
 * Copyright (c) 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.pde.provider;

import org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask;
import org.eclipse.oomph.setup.pde.PDEPackage;
import org.eclipse.oomph.setup.provider.SetupTaskItemProvider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class AbstractAPIBaselineTaskItemProvider extends SetupTaskItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AbstractAPIBaselineTaskItemProvider(AdapterFactory adapterFactory)
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

      addNamePropertyDescriptor(object);
      addActivatePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_AbstractAPIBaselineTask_name_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_AbstractAPIBaselineTask_name_feature", "_UI_AbstractAPIBaselineTask_type"),
        PDEPackage.Literals.ABSTRACT_API_BASELINE_TASK__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Activate feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addActivatePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_AbstractAPIBaselineTask_activate_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_AbstractAPIBaselineTask_activate_feature", "_UI_AbstractAPIBaselineTask_type"),
        PDEPackage.Literals.ABSTRACT_API_BASELINE_TASK__ACTIVATE, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
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
    AbstractAPIBaselineTask apiBaselineTask = (AbstractAPIBaselineTask)object;
    StringBuilder builder = new StringBuilder();

    String name = getName(object);
    Pattern NAME_LABEL_PATTERN = Pattern.compile("(.*?)[\\s-_]*(API)?[\\s-_]*(Baseline)?[\\s]*", Pattern.CASE_INSENSITIVE);
    Matcher matcher = NAME_LABEL_PATTERN.matcher(name);
    if (matcher.matches())
    {
      builder.append(matcher.group(1));
    }
    else
    {
      builder.append(name);
    }

    if (builder.length() != 0)
    {
      builder.append(' ');
    }

    builder.append(getString("_UI_APIBaselineTask_type"));

    if (apiBaselineTask.isActivate())
    {
      builder.append(", activate");
    }

    return builder.toString();
  }

  protected String getName(Object object)
  {
    AbstractAPIBaselineTask apiBaselineTask = (AbstractAPIBaselineTask)object;
    String name = apiBaselineTask.getName();
    return name == null ? "" : name;
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

    switch (notification.getFeatureID(AbstractAPIBaselineTask.class))
    {
      case PDEPackage.ABSTRACT_API_BASELINE_TASK__NAME:
      case PDEPackage.ABSTRACT_API_BASELINE_TASK__ACTIVATE:
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
    return PDEEditPlugin.INSTANCE;
  }

}
