/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.provider.RequirementItemProvider;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.targlets.Targlet} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TargletItemProvider extends ModelElementItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TargletItemProvider(AdapterFactory adapterFactory)
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
      addActiveRepositoryListNamePropertyDescriptor(object);
      addIncludeSourcesPropertyDescriptor(object);
      addIncludeAllPlatformsPropertyDescriptor(object);
      addIncludeAllRequirementsPropertyDescriptor(object);
      addIncludeNegativeRequirementsPropertyDescriptor(object);
      addIncludeBinaryEquivalentsPropertyDescriptor(object);
      addProfilePropertiesPropertyDescriptor(object);
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
        getString("_UI_Targlet_name_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Targlet_name_feature", "_UI_Targlet_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        TargletPackage.Literals.TARGLET__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Active Repository List Name feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addActiveRepositoryListNamePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Targlet_activeRepositoryListName_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Targlet_activeRepositoryListName_feature", "_UI_Targlet_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        TargletPackage.Literals.TARGLET__ACTIVE_REPOSITORY_LIST_NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Include Sources feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIncludeSourcesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Targlet_includeSources_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Targlet_includeSources_feature", "_UI_Targlet_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        TargletPackage.Literals.TARGLET__INCLUDE_SOURCES, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Include All Platforms feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIncludeAllPlatformsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Targlet_includeAllPlatforms_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Targlet_includeAllPlatforms_feature", "_UI_Targlet_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        TargletPackage.Literals.TARGLET__INCLUDE_ALL_PLATFORMS, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Include All Requirements feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIncludeAllRequirementsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Targlet_includeAllRequirements_feature"), //$NON-NLS-1$
        getString("_UI_Targlet_includeAllRequirements_description"), //$NON-NLS-1$
        TargletPackage.Literals.TARGLET__INCLUDE_ALL_REQUIREMENTS, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Include Negative Requirements feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIncludeNegativeRequirementsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Targlet_includeNegativeRequirements_feature"), //$NON-NLS-1$
        getString("_UI_Targlet_includeNegativeRequirements_description"), //$NON-NLS-1$
        TargletPackage.Literals.TARGLET__INCLUDE_NEGATIVE_REQUIREMENTS, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Include Binary Equivalents feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIncludeBinaryEquivalentsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Targlet_includeBinaryEquivalents_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Targlet_includeBinaryEquivalents_feature", "_UI_Targlet_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        TargletPackage.Literals.TARGLET__INCLUDE_BINARY_EQUIVALENTS, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Profile Properties feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addProfilePropertiesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Targlet_profileProperties_feature"), //$NON-NLS-1$
        getString("_UI_Targlet_profileProperties_description"), //$NON-NLS-1$
        TargletPackage.Literals.TARGLET__PROFILE_PROPERTIES, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  @Override
  protected Collection<?> filterAlternatives(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> alternatives)
  {
    return super.filterAlternatives(domain, owner, location, operations, operation, RequirementItemProvider.filterAlternatives(alternatives));
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
      childrenFeatures.add(TargletPackage.Literals.TARGLET__REQUIREMENTS);
      childrenFeatures.add(TargletPackage.Literals.TARGLET__SOURCE_LOCATORS);
      childrenFeatures.add(TargletPackage.Literals.TARGLET__INSTALLABLE_UNIT_GENERATORS);
      childrenFeatures.add(TargletPackage.Literals.TARGLET__REPOSITORY_LISTS);
      childrenFeatures.add(TargletPackage.Literals.TARGLET__DROPIN_LOCATIONS);
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

  @Override
  public boolean hasChildren(Object object)
  {
    // The children may be reduced so really check that there are any.
    return !getChildren(object).isEmpty();
  }

  @Override
  public Collection<?> getChildren(Object object)
  {
    Collection<?> result = super.getChildren(object);
    Targlet targlet = (Targlet)object;
    if (BaseUtil.isReduced(targlet))
    {
      // Filter out all repository lists except the active repository list.
      RepositoryList activeRepositoryList = targlet.getActiveRepositoryList();
      EList<RepositoryList> repositoryLists = targlet.getRepositoryLists();
      for (Iterator<?> it = result.iterator(); it.hasNext();)
      {
        Object child = it.next();
        if (child != activeRepositoryList && repositoryLists.contains(child))
        {
          it.remove();
        }
      }
    }

    return result;
  }

  /**
   * This returns Targlet.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/Targlet")); //$NON-NLS-1$
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
    String label = ((Targlet)object).getName();
    return label == null || label.length() == 0 ? getString("_UI_Targlet_type") : label; //$NON-NLS-1$
  }

  @Override
  public void notifyChanged(Notification notification)
  {
    notifyChangedGen(notification);

    // When the name changes, the label of the container might be changed too, i.e., for a Targlet Task.
    if (notification.getFeatureID(Targlet.class) == TargletPackage.TARGLET__NAME)
    {
      EObject container = ((EObject)notification.getNotifier()).eContainer();
      if (container != null)
      {
        fireNotifyChanged(new ViewerNotification(notification, container, false, true));
      }
    }
  }

  /**
   * This handles model notifications by calling {@link #updateChildren} to update any cached
   * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void notifyChangedGen(Notification notification)
  {
    updateChildren(notification);

    switch (notification.getFeatureID(Targlet.class))
    {
      case TargletPackage.TARGLET__NAME:
      case TargletPackage.TARGLET__ACTIVE_REPOSITORY_LIST_NAME:
      case TargletPackage.TARGLET__INCLUDE_SOURCES:
      case TargletPackage.TARGLET__INCLUDE_ALL_PLATFORMS:
      case TargletPackage.TARGLET__INCLUDE_ALL_REQUIREMENTS:
      case TargletPackage.TARGLET__INCLUDE_NEGATIVE_REQUIREMENTS:
      case TargletPackage.TARGLET__INCLUDE_BINARY_EQUIVALENTS:
      case TargletPackage.TARGLET__PROFILE_PROPERTIES:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
        return;
      case TargletPackage.TARGLET__REQUIREMENTS:
      case TargletPackage.TARGLET__SOURCE_LOCATORS:
      case TargletPackage.TARGLET__INSTALLABLE_UNIT_GENERATORS:
      case TargletPackage.TARGLET__REPOSITORY_LISTS:
      case TargletPackage.TARGLET__DROPIN_LOCATIONS:
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

    newChildDescriptors.add(createChildParameter(TargletPackage.Literals.TARGLET__REQUIREMENTS, P2Factory.eINSTANCE.createRequirement()));

    newChildDescriptors.add(createChildParameter(TargletPackage.Literals.TARGLET__SOURCE_LOCATORS, ResourcesFactory.eINSTANCE.createSourceLocator()));

    newChildDescriptors.add(createChildParameter(TargletPackage.Literals.TARGLET__REPOSITORY_LISTS, P2Factory.eINSTANCE.createRepositoryList()));

    newChildDescriptors.add(createChildParameter(TargletPackage.Literals.TARGLET__DROPIN_LOCATIONS, TargletFactory.eINSTANCE.createDropinLocation()));
  }

}
