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
package org.eclipse.oomph.setup.workbench.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.setup.workbench.KeyBindingContext;
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
import java.util.Map;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.setup.workbench.KeyBindingContext} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class KeyBindingContextItemProvider extends ModelElementItemProvider
{
  private Map<String, String> contexts;

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public KeyBindingContextItemProvider(AdapterFactory adapterFactory)
  {
    super(adapterFactory);
  }

  public Map<String, String> getContexts()
  {
    if (contexts == null)
    {
      contexts = WorkbenchEditPlugin.getContexts();
    }
    return contexts;
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

      addIDPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the ID feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIDPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_KeyBindingContext_iD_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_KeyBindingContext_iD_feature", "_UI_KeyBindingContext_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        WorkbenchPackage.Literals.KEY_BINDING_CONTEXT__ID, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  @Override
  protected Collection<?> filterChoices(Collection<?> choices, EStructuralFeature feature, Object object)
  {
    if (feature == WorkbenchPackage.Literals.KEY_BINDING_CONTEXT__ID)
    {
      return getContexts().keySet();
    }

    return super.filterChoices(choices, feature, object);
  }

  @Override
  protected boolean isChoiceArbitrary(EStructuralFeature feature, Object object)
  {
    return feature == WorkbenchPackage.Literals.KEY_BINDING_CONTEXT__ID || super.isChoiceArbitrary(feature, object);
  }

  /**
   * This returns KeyBindingContext.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/KeyBindingContext")); //$NON-NLS-1$
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
    String id = ((KeyBindingContext)object).getID();
    String name = id == null ? null : getContexts().get(id);
    if (name != null)
    {
      id += " \u279d " + name; //$NON-NLS-1$
    }

    return id == null || id.length() == 0 ? getString("_UI_KeyBindingContext_type") : id; //$NON-NLS-1$
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

    switch (notification.getFeatureID(KeyBindingContext.class))
    {
      case WorkbenchPackage.KEY_BINDING_CONTEXT__ID:
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
    return WorkbenchEditPlugin.INSTANCE;
  }

}
