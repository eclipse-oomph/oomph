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
package org.eclipse.oomph.setup.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.impl.ProductToProductVersionMapEntryImpl;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is the item provider adapter for a {@link java.util.Map.Entry} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ProductToProductVersionMapEntryItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider,
    ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource
{
  private final ModelElementItemProvider.HierarchicalPropertyDescriptor keyPropertyDescriptor = new ModelElementItemProvider.HierarchicalPropertyDescriptor(
      ((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_ProductToProductVersionMapEntry_key_feature"),
      getString("_UI_PropertyDescriptor_description", "_UI_ProductToProductVersionMapEntry_key_feature", "_UI_ProductToProductVersionMapEntry_type"),
      SetupPackage.Literals.PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__KEY, true, false, true, null, null, null)
  {
    @Override
    protected Object filterParent(AdapterFactoryItemDelegator itemDelegator, EStructuralFeature feature, Object object)
    {
      Object result = super.filterParent(itemDelegator, feature, object);
      if (result instanceof Index)
      {
        return null;
      }

      return result;
    }

    @Override
    public void setPropertyValue(Object object, Object value)
    {
      Product product = (Product)value;
      ProductVersion version = product.getVersions().get(0);

      EditingDomain editingDomain = getEditingDomain(object);
      CompoundCommand compoundCommand = new CompoundCommand(CompoundCommand.LAST_COMMAND_ALL);
      compoundCommand.append(SetCommand.create(editingDomain, object, SetupPackage.Literals.PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__KEY, product));
      compoundCommand.append(SetCommand.create(editingDomain, object, SetupPackage.Literals.PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__VALUE, version));

      editingDomain.getCommandStack().execute(compoundCommand);
    }

    @Override
    public Collection<?> getChoiceOfValues(Object object)
    {
      List<Object> result = new ArrayList<Object>(super.getChoiceOfValues(object));
      for (Iterator<Object> it = result.iterator(); it.hasNext();)
      {
        Product product = (Product)it.next();
        if (product == null || product.getVersions().isEmpty())
        {
          it.remove();
        }
      }

      return result;
    }
  };

  private final ItemPropertyDescriptor valuePropertyDescriptor = new ItemPropertyDescriptor(
      ((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
      getString("_UI_ProductToProductVersionMapEntry_value_feature"), getString("_UI_PropertyDescriptor_description",
          "_UI_ProductToProductVersionMapEntry_value_feature", "_UI_ProductToProductVersionMapEntry_type"),
      SetupPackage.Literals.PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__VALUE, true, false, true, null, null, null)
  {
    @Override
    public Collection<?> getChoiceOfValues(Object object)
    {
      ProductToProductVersionMapEntryImpl entry = (ProductToProductVersionMapEntryImpl)object;
      Product product = entry.getKey();
      return product == null ? Collections.emptySet() : product.getVersions();
    }
  };

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProductToProductVersionMapEntryItemProvider(AdapterFactory adapterFactory)
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

      addKeyPropertyDescriptor(object);
      addValuePropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Key feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addKeyPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(keyPropertyDescriptor);
  }

  /**
   * This adds a property descriptor for the Value feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addValuePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(valuePropertyDescriptor);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean hasChildren(Object object)
  {
    return hasChildren(object, true);
  }

  /**
   * This returns ProductToProductVersionMapEntry.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/ProductToProductVersionMapEntry"));
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
    @SuppressWarnings("unchecked")
    Map.Entry<Product, ProductVersion> productToProductVersionMapEntry = (Map.Entry<Product, ProductVersion>)object;
    Product product = productToProductVersionMapEntry.getKey();
    ProductVersion version = productToProductVersionMapEntry.getValue();
    return "" + (product == null ? "null" : keyPropertyDescriptor.getLabelProvider(object).getText(product)) + " -> "
        + (version == null ? "null" : valuePropertyDescriptor.getLabelProvider(object).getText(version));
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public void notifyChanged(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(Map.Entry.class))
    {
      case SetupPackage.PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__KEY:
      case SetupPackage.PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__VALUE:
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
    return ((IChildCreationExtender)adapterFactory).getResourceLocator();
  }

}
