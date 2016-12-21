/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.provider;

import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.PreferenceTask} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PreferenceTaskItemProvider extends SetupTaskItemProvider
{
  private static final String INSTANCE_SCOPE = "/instance/";

  private static final String CONFIGURATION_SCOPE = "/configuration/";

  private boolean shortenLabelText;

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PreferenceTaskItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  public boolean isShortenLabelText()
  {
    return shortenLabelText;
  }

  public void setShortenLabelText(boolean shortenLabelText)
  {
    this.shortenLabelText = shortenLabelText;
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

      addKeyPropertyDescriptor(object);
      addValuePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Key feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addKeyPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceTask_key_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceTask_key_feature", "_UI_PreferenceTask_type"),
        SetupPackage.Literals.PREFERENCE_TASK__KEY, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Value feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addValuePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceTask_value_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceTask_value_feature", "_UI_PreferenceTask_type"),
        SetupPackage.Literals.PREFERENCE_TASK__VALUE, true, true, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This returns PreferenceTask.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Object getImage(Object object)
  {
    String imageKey = "full/obj16/PreferenceTask";

    PreferenceTask preferenceTask = (PreferenceTask)object;
    String key = preferenceTask.getKey();
    if (!StringUtil.isEmpty(key))
    {
      String scope = getScope(key);
      if (scope == CONFIGURATION_SCOPE)
      {
        imageKey += "Configuration";
      }
    }

    return overlayImage(object, getResourceLocator().getImage(imageKey));
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
    PreferenceTask preferenceTask = (PreferenceTask)object;
    String key = preferenceTask.getKey();
    String value = preferenceTask.getValue();

    if (StringUtil.isEmpty(key))
    {
      if (StringUtil.isEmpty(value))
      {
        return getString("_UI_PreferenceTask_type");
      }
    }
    else if (shortenLabelText)
    {
      String scope = getScope(key);
      if (scope != null)
      {
        String parentLabel = getParentLabel(preferenceTask);
        if (parentLabel != null)
        {
          String prefix = scope + parentLabel + "/";
          String prefixLong = prefix + parentLabel + ".";

          if (key.startsWith(prefixLong))
          {
            key = key.substring(prefixLong.length());
          }
          else if (key.startsWith(prefix))
          {
            key = key.substring(prefix.length());
          }
        }
      }
    }

    StringBuilder builder = new StringBuilder();
    builder.append(key);

    if (value != null)
    {
      builder.append(" = ");
      builder.append(crop(value));
    }

    return builder.toString();
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

    switch (notification.getFeatureID(PreferenceTask.class))
    {
      case SetupPackage.PREFERENCE_TASK__KEY:
      case SetupPackage.PREFERENCE_TASK__VALUE:
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

  private String getParentLabel(PreferenceTask preferenceTask)
  {
    Object parent = getParent(preferenceTask);
    if (parent != null)
    {
      ItemProviderAdapter itemProvider = (ItemProviderAdapter)getRootAdapterFactory().adapt(parent, IItemLabelProvider.class);
      if (itemProvider != null)
      {
        return itemProvider.getText(parent);
      }
    }

    EObject container = preferenceTask.eContainer();
    if (container instanceof CompoundTask)
    {
      CompoundTask compoundTask = (CompoundTask)container;
      return compoundTask.getName();
    }

    return null;
  }

  private static String getScope(String key)
  {
    if (key.startsWith(INSTANCE_SCOPE))
    {
      return INSTANCE_SCOPE;
    }

    if (key.startsWith(CONFIGURATION_SCOPE))
    {
      return CONFIGURATION_SCOPE;
    }

    return null;
  }

  private static PreferenceTaskItemProvider getItemProvider(AdapterFactory adapterFactory)
  {
    PreferenceTask preferenceTask = SetupFactory.eINSTANCE.createPreferenceTask();

    Adapter adapter = adapterFactory.adapt(preferenceTask, IItemLabelProvider.class);
    if (adapter instanceof PreferenceTaskItemProvider)
    {
      return (PreferenceTaskItemProvider)adapter;
    }

    return null;
  }

  public static boolean isShortenLabelText(AdapterFactory adapterFactory)
  {
    PreferenceTaskItemProvider itemProvider = getItemProvider(adapterFactory);
    return itemProvider != null ? itemProvider.isShortenLabelText() : false;
  }

  public static void setShortenLabelText(AdapterFactory adapterFactory)
  {
    PreferenceTaskItemProvider itemProvider = getItemProvider(adapterFactory);
    if (itemProvider != null)
    {
      itemProvider.setShortenLabelText(true);
    }
  }
}
