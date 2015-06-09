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
package org.eclipse.oomph.workingsets.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.predicates.PredicatesFactory;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetsFactory;
import org.eclipse.oomph.workingsets.WorkingSetsPackage;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.workingsets.WorkingSet} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class WorkingSetItemProvider extends ModelElementItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WorkingSetItemProvider(AdapterFactory adapterFactory)
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
      addIDPropertyDescriptor(object);
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
        getString("_UI_WorkingSet_name_feature"), getString("_UI_WorkingSet_name_description"), WorkingSetsPackage.Literals.WORKING_SET__NAME, true, false,
        false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
        getString("_UI_WorkingSet_iD_feature"), getString("_UI_WorkingSet_iD_description"), WorkingSetsPackage.Literals.WORKING_SET__ID, true, false, false,
        ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
      childrenFeatures.add(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES);
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
   * This returns WorkingSet.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/WorkingSet"));
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
    String label = ((WorkingSet)object).getName();
    return label == null ? "" : label;
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

    switch (notification.getFeatureID(WorkingSet.class))
    {
      case WorkingSetsPackage.WORKING_SET__NAME:
      {
        EObject notifier = (EObject)notification.getNotifier();

        for (Iterator<EObject> it = EcoreUtil.getRootContainer(notifier).eAllContents(); it.hasNext();)
        {
          EObject child = it.next();
          for (EObject reference : child.eCrossReferences())
          {
            if (reference == notifier)
            {
              fireNotifyChanged(new ViewerNotification(notification, child, false, true));
            }
          }
        }

        fireNotifyChanged(new ViewerNotification(notification, notifier, false, true));
        return;
      }
      case WorkingSetsPackage.WORKING_SET__ID:
      {
        Object notifier = notification.getNotifier();
        fireNotifyChanged(new ViewerNotification(notification, notifier, false, true));
        return;
      }
      case WorkingSetsPackage.WORKING_SET__PREDICATES:
      {
        Object notifier = notification.getNotifier();
        fireNotifyChanged(new ViewerNotification(notification, notifier, true, false));
        return;
      }
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

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, WorkingSetsFactory.eINSTANCE.createInclusionPredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, WorkingSetsFactory.eINSTANCE.createExclusionPredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, PredicatesFactory.eINSTANCE.createNamePredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, PredicatesFactory.eINSTANCE.createCommentPredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, PredicatesFactory.eINSTANCE.createLocationPredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, PredicatesFactory.eINSTANCE.createRepositoryPredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, PredicatesFactory.eINSTANCE.createAndPredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, PredicatesFactory.eINSTANCE.createOrPredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, PredicatesFactory.eINSTANCE.createNotPredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, PredicatesFactory.eINSTANCE.createNaturePredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, PredicatesFactory.eINSTANCE.createBuilderPredicate()));

    newChildDescriptors.add(createChildParameter(WorkingSetsPackage.Literals.WORKING_SET__PREDICATES, PredicatesFactory.eINSTANCE.createFilePredicate()));
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
    return WorkingSetsEditPlugin.INSTANCE;
  }

}
