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
package org.eclipse.oomph.preferences.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.preferences.PreferenceItem;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.PreferencesPackage;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.preferences.PreferenceItem} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PreferenceItemItemProvider extends ModelElementItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PreferenceItemItemProvider(AdapterFactory adapterFactory)
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

      addRootPropertyDescriptor(object);
      addScopePropertyDescriptor(object);
      addAbsolutePathPropertyDescriptor(object);
      addNamePropertyDescriptor(object);
      addRelativePathPropertyDescriptor(object);
      addAncestorPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  public static class PreferenceItemPropertyDescriptor extends ItemPropertyDescriptor
  {
    public PreferenceItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName, String description,
        EStructuralFeature feature, boolean isSettable, boolean multiLine, boolean sortChoices, Object staticImage, String category, String[] filterFlags)
    {
      super(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices, staticImage, category, filterFlags);

      itemDelegator = new ItemDelegator(adapterFactory, resourceLocator)
      {
        @Override
        public String getText(Object object)
        {
          if (object instanceof PreferenceItem)
          {
            URI absolutePath = ((PreferenceItem)object).getAbsolutePath();
            String text = PreferencesFactory.eINSTANCE.convertURI(absolutePath);
            return text;
          }

          return super.getText(object);
        }
      };
    }
  }

  @Override
  protected ItemPropertyDescriptor createItemPropertyDescriptor(AdapterFactory adapterFactory, ResourceLocator resourceLocator, String displayName,
      String description, EStructuralFeature feature, boolean isSettable, boolean multiLine, boolean sortChoices, Object staticImage, String category,
      String[] filterFlags)
  {
    return new PreferenceItemPropertyDescriptor(adapterFactory, resourceLocator, displayName, description, feature, isSettable, multiLine, sortChoices,
        staticImage, category, filterFlags);
  }

  /**
   * This adds a property descriptor for the Root feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addRootPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceItem_root_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceItem_root_feature", "_UI_PreferenceItem_type"),
        PreferencesPackage.Literals.PREFERENCE_ITEM__ROOT, false, false, false, null, null, null));
  }

  /**
   * This adds a property descriptor for the Absolute Path feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addAbsolutePathPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceItem_absolutePath_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceItem_absolutePath_feature", "_UI_PreferenceItem_type"),
        PreferencesPackage.Literals.PREFERENCE_ITEM__ABSOLUTE_PATH, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
        getString("_UI_PreferenceItem_name_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceItem_name_feature", "_UI_PreferenceItem_type"),
        PreferencesPackage.Literals.PREFERENCE_ITEM__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Relative Path feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addRelativePathPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceItem_relativePath_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceItem_relativePath_feature", "_UI_PreferenceItem_type"),
        PreferencesPackage.Literals.PREFERENCE_ITEM__RELATIVE_PATH, false, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Ancestor feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addAncestorPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceItem_ancestor_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceItem_ancestor_feature", "_UI_PreferenceItem_type"),
        PreferencesPackage.Literals.PREFERENCE_ITEM__ANCESTOR, false, false, false, null, null, null));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean hasChildren(Object object)
  {
    return hasChildren(object, false);
  }

  /**
   * This adds a property descriptor for the Scope feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addScopePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_PreferenceItem_scope_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_PreferenceItem_scope_feature", "_UI_PreferenceItem_type"),
        PreferencesPackage.Literals.PREFERENCE_ITEM__SCOPE, false, false, false, null, null, null));
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
    String label = ((PreferenceItem)object).getName();
    return label == null || label.length() == 0 ? getString("_UI_PreferenceItem_type") : getString("_UI_PreferenceItem_type") + " " + label;
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

    switch (notification.getFeatureID(PreferenceItem.class))
    {
      case PreferencesPackage.PREFERENCE_ITEM__ABSOLUTE_PATH:
      case PreferencesPackage.PREFERENCE_ITEM__NAME:
      case PreferencesPackage.PREFERENCE_ITEM__RELATIVE_PATH:
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
    return PreferencesEditPlugin.INSTANCE;
  }

}
