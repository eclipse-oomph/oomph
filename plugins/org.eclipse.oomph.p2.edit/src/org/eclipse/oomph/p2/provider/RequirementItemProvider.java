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
package org.eclipse.oomph.p2.provider;

import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.p2.Requirement} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RequirementItemProvider extends ModelElementItemProvider
{
  public static final String NAMESPACE_PACKAGE_ID = "java.package";

  private static final String FEATURE_SUFFIX = ".feature.group";

  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RequirementItemProvider(AdapterFactory adapterFactory)
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
      addNamespacePropertyDescriptor(object);
      addVersionRangePropertyDescriptor(object);
      addOptionalPropertyDescriptor(object);
      addFeaturePropertyDescriptor(object);
      addFilterPropertyDescriptor(object);
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
        getString("_UI_Requirement_name_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Requirement_name_feature", "_UI_Requirement_type"),
        P2Package.Literals.REQUIREMENT__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Namespace feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addNamespacePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_namespace_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_namespace_feature", "_UI_Requirement_type"), P2Package.Literals.REQUIREMENT__NAMESPACE,
        true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Version Range feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addVersionRangePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_versionRange_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_versionRange_feature", "_UI_Requirement_type"),
        P2Package.Literals.REQUIREMENT__VERSION_RANGE, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Optional feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addOptionalPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_optional_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_optional_feature", "_UI_Requirement_type"), P2Package.Literals.REQUIREMENT__OPTIONAL,
        true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Feature feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addFeaturePropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_feature_feature"),
        getString("_UI_PropertyDescriptor_description", "_UI_Requirement_feature_feature", "_UI_Requirement_type"), P2Package.Literals.REQUIREMENT__FEATURE,
        false, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
  }

  /**
   * This adds a property descriptor for the Filter feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addFilterPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Requirement_filter_feature"), getString("_UI_PropertyDescriptor_description", "_UI_Requirement_filter_feature", "_UI_Requirement_type"),
        P2Package.Literals.REQUIREMENT__FILTER, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
  }

  @Override
  protected Collection<?> filterAlternatives(EditingDomain domain, Object owner, float location, int operations, int operation, Collection<?> alternatives)
  {
    return super.filterAlternatives(domain, owner, location, operations, operation, filterAlternatives(alternatives));
  }

  public static Collection<?> filterAlternatives(Collection<?> alternatives)
  {
    Collection<Object> result = new ArrayList<Object>();
    for (Object object : alternatives)
    {
      if (object instanceof Requirement)
      {
        Requirement requirement = (Requirement)object;

        String namespace = requirement.getNamespace();
        if ("osgi.bundle".equals(namespace))
        {
          requirement.setNamespace(IInstallableUnit.NAMESPACE_IU_ID);
        }
        else if (!"org.eclipse.equinox.p2.iu".equals(namespace) && !NAMESPACE_PACKAGE_ID.equals(namespace))
        {
          continue;
        }

        VersionRange versionRange = requirement.getVersionRange();
        if (versionRange != null)
        {
          Version minimum = versionRange.getMinimum();
          if (minimum.toString().endsWith(".qualifier"))
          {
            VersionRange minimumVersionRange = P2Factory.eINSTANCE.createVersionRange(minimum, VersionSegment.MICRO);
            requirement.setVersionRange(minimumVersionRange);
          }
        }
      }

      result.add(object);
    }

    return result;
  }

  @Override
  protected Collection<?> filterChoices(Collection<?> choices, EStructuralFeature feature, Object object)
  {
    if (feature == P2Package.Literals.REQUIREMENT__NAMESPACE)
    {
      return Arrays.asList(new String[] { IInstallableUnit.NAMESPACE_IU_ID, NAMESPACE_PACKAGE_ID });
    }

    return super.filterChoices(choices, feature, object);
  }

  /**
   * This returns InstallableUnit.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Object getImage(Object object)
  {
    String key = "full/obj16/Requirement";

    Requirement requirement = (Requirement)object;
    String namespace = requirement.getNamespace();
    if (IInstallableUnit.NAMESPACE_IU_ID.equals(namespace))
    {
      if (requirement.isFeature())
      {
        key += "_Feature";
      }
      else
      {
        key += "_Plugin";
      }
    }
    else if (NAMESPACE_PACKAGE_ID.equals(namespace))
    {
      key += "_Package";
    }

    if (requirement.isOptional())
    {
      key += "_Optional";
    }

    return overlayImage(object, getResourceLocator().getImage(key));
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
    Requirement requirement = (Requirement)object;
    String label = requirement.getName();
    if (label == null || label.length() == 0)
    {
      label = getString("_UI_Requirement_type");
    }
    else
    {
      if (label.endsWith(FEATURE_SUFFIX))
      {
        label = label.substring(0, label.length() - FEATURE_SUFFIX.length());
      }
    }

    VersionRange versionRange = requirement.getVersionRange();
    return label + (versionRange == null || VersionRange.emptyRange.equals(versionRange) ? "" : " " + versionRange.toString());
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

    switch (notification.getFeatureID(Requirement.class))
    {
      case P2Package.REQUIREMENT__ID:
      case P2Package.REQUIREMENT__NAME:
      case P2Package.REQUIREMENT__NAMESPACE:
      case P2Package.REQUIREMENT__VERSION_RANGE:
      case P2Package.REQUIREMENT__OPTIONAL:
      case P2Package.REQUIREMENT__FEATURE:
      case P2Package.REQUIREMENT__FILTER:
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

}
