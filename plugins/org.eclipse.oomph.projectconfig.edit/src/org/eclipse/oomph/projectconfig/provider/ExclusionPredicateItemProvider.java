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
package org.eclipse.oomph.projectconfig.provider;

import org.eclipse.oomph.predicates.provider.PredicateItemProvider;
import org.eclipse.oomph.projectconfig.ExclusionPredicate;
import org.eclipse.oomph.projectconfig.PreferenceProfile;
import org.eclipse.oomph.projectconfig.ProjectConfigPackage;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.projectconfig.ExclusionPredicate} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ExclusionPredicateItemProvider extends PredicateItemProvider
{
  public static void filterCircularPreferenceProfiles(EObject root, Collection<?> objects)
  {
    for (EObject eObject = root.eContainer(); eObject != null; eObject = eObject.eContainer())
    {
      if (eObject instanceof PreferenceProfile)
      {
        for (Iterator<?> it = objects.iterator(); it.hasNext();)
        {
          Object value = it.next();
          if (value instanceof PreferenceProfile)
          {
            PreferenceProfile preferenceProfile = (PreferenceProfile)value;
            if (preferenceProfile == eObject || getReachablePreferenceProfiles(preferenceProfile).contains(eObject))
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

  public static Set<PreferenceProfile> getReachablePreferenceProfiles(PreferenceProfile preferenceProfile)
  {
    Set<PreferenceProfile> workingSets = new HashSet<PreferenceProfile>();
    collectReachablePreferenceProfiles(preferenceProfile, workingSets);
    return workingSets;
  }

  private static void collectReachablePreferenceProfiles(PreferenceProfile preferenceProfile, Set<PreferenceProfile> preferenceProfiles)
  {
    for (Iterator<EObject> it = preferenceProfile.eAllContents(); it.hasNext();)
    {
      EObject child = it.next();
      for (EObject reference : child.eCrossReferences())
      {
        if (reference instanceof PreferenceProfile)
        {
          PreferenceProfile referecedPreferenceProfile = (PreferenceProfile)reference;
          if (preferenceProfiles.add(referecedPreferenceProfile))
          {
            collectReachablePreferenceProfiles(referecedPreferenceProfile, preferenceProfiles);
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

      addExcludedPreferenceProfilesPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Excluded Preference Profiles feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected void addExcludedPreferenceProfilesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(new ItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_ExclusionPredicate_excludedPreferenceProfiles_feature"), getString("_UI_PropertyDescriptor_description",
            "_UI_ExclusionPredicate_excludedPreferenceProfiles_feature", "_UI_ExclusionPredicate_type"),
        ProjectConfigPackage.Literals.EXCLUSION_PREDICATE__EXCLUDED_PREFERENCE_PROFILES, true, false, true, null, null, null)
    {
      @Override
      public Collection<?> getChoiceOfValues(Object object)
      {
        Collection<?> result = super.getChoiceOfValues(object);
        filterCircularPreferenceProfiles((EObject)object, result);
        return result;
      }
    });
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
    for (PreferenceProfile preferenceProfile : exclusionPredicate.getExcludedPreferenceProfiles())
    {
      if (result.length() != 0)
      {
        result.append(", ");
      }
      result.append(preferenceProfile.getName());
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
      case ProjectConfigPackage.EXCLUSION_PREDICATE__EXCLUDED_PREFERENCE_PROFILES:
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, true));
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
    return ProjectConfigEditPlugin.INSTANCE;
  }

}
