/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.workingsets.provider;

import org.eclipse.oomph.predicates.provider.PredicateItemProvider;
import org.eclipse.oomph.workingsets.ExclusionPredicate;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetsPackage;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.workingsets.ExclusionPredicate} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ExclusionPredicateItemProvider extends PredicateItemProvider
{
  public static void filterCircularWorkingSets(EObject root, Collection<?> objects)
  {
    Resource resource = root.eResource();
    for (EObject eObject = root.eContainer(); eObject != null; eObject = eObject.eContainer())
    {
      if (eObject instanceof WorkingSet)
      {
        for (Iterator<?> it = objects.iterator(); it.hasNext();)
        {
          Object value = it.next();
          if (value instanceof WorkingSet)
          {
            WorkingSet workingSet = (WorkingSet)value;
            if (workingSet == eObject || workingSet.eResource() != resource || getReachableWorkingSets(workingSet).contains(eObject))
            {
              it.remove();
            }
          }
          else
          {
            it.remove();
          }
        }
      }
    }
  }

  public static Set<WorkingSet> getReachableWorkingSets(WorkingSet workingSet)
  {
    Set<WorkingSet> workingSets = new HashSet<WorkingSet>();
    collectReachableWorkingSets(workingSet, workingSets);
    return workingSets;
  }

  private static void collectReachableWorkingSets(WorkingSet workingSet, Set<WorkingSet> workingSets)
  {
    for (Iterator<EObject> it = workingSet.eAllContents(); it.hasNext();)
    {
      EObject child = it.next();
      for (EObject reference : child.eCrossReferences())
      {
        if (reference instanceof WorkingSet)
        {
          WorkingSet referecedWorkingSet = (WorkingSet)reference;
          if (workingSets.add(referecedWorkingSet))
          {
            collectReachableWorkingSets(referecedWorkingSet, workingSets);
          }
        }
      }
    }
  }

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExclusionPredicateItemProvider(AdapterFactory adapterFactory)
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

      addExcludedWorkingSetsPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Excluded Working Sets feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addExcludedWorkingSetsPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_ExclusionPredicate_excludedWorkingSets_feature"), getString("_UI_ExclusionPredicate_excludedWorkingSets_description"),
        WorkingSetsPackage.Literals.EXCLUSION_PREDICATE__EXCLUDED_WORKING_SETS, true, false, true, null, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        Collection<?> result = super.getChoiceOfValues(object);
        filterCircularWorkingSets((EObject)object, result);
        return result;
      }
    });
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
   * This returns ExclusionPredicate.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/ExclusionPredicate"));
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
    ExclusionPredicate exclusionPredicate = (ExclusionPredicate)object;
    StringBuilder result = new StringBuilder();
    for (WorkingSet workingSet : exclusionPredicate.getExcludedWorkingSets())
    {
      if (result.length() != 0)
      {
        result.append(", ");
      }
      result.append(workingSet.getName());
    }
    return result.toString();
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

    switch (notification.getFeatureID(ExclusionPredicate.class))
    {
      case WorkingSetsPackage.EXCLUSION_PREDICATE__EXCLUDED_WORKING_SETS:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, true));
        return;
    }
    super.notifyChanged(notification);
  }

  @Override
  protected Command factorAddCommand(EditingDomain domain, CommandParameter commandParameter)
  {
    if (commandParameter.getFeature() == null)
    {
      EObject eOwner = commandParameter.getEOwner();
      Collection<?> collection = commandParameter.getCollection();
      if (collection != null)
      {
        collection = new ArrayList<Object>(collection);
        filterCircularWorkingSets(eOwner, collection);
        if (collection.size() == commandParameter.getCollection().size())
        {
          return new AddCommand(domain, eOwner, WorkingSetsPackage.Literals.EXCLUSION_PREDICATE__EXCLUDED_WORKING_SETS, collection);
        }
      }
    }

    return super.factorAddCommand(domain, commandParameter);
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
    return WorkingSetsEditPlugin.INSTANCE;
  }

}
