/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.provider;

import org.eclipse.oomph.maven.Dependency;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Project;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.StyledString;

import java.util.Collection;
import java.util.List;

/**
 * This is the item provider adapter for a {@link org.eclipse.oomph.maven.Dependency} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class DependencyItemProvider extends CoordinateItemProvider
{
  /**
   * This constructs an instance from a factory and a notifier.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DependencyItemProvider(AdapterFactory adapterFactory)
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

      addResolvedProjectPropertyDescriptor(object);
      addResolvedManagedDependencyPropertyDescriptor(object);
      addIncomingResolvedManagedDependenciesPropertyDescriptor(object);
    }
    return itemPropertyDescriptors;
  }

  /**
   * This adds a property descriptor for the Resolved Project feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addResolvedProjectPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Dependency_resolvedProject_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Dependency_resolvedProject_feature", "_UI_Dependency_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.DEPENDENCY__RESOLVED_PROJECT, false, false, true, null, null, null));
  }

  /**
   * This adds a property descriptor for the Resolved Managed Dependency feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addResolvedManagedDependencyPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Dependency_resolvedManagedDependency_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Dependency_resolvedManagedDependency_feature", "_UI_Dependency_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.DEPENDENCY__RESOLVED_MANAGED_DEPENDENCY, false, false, true, null, null, null));
  }

  /**
   * This adds a property descriptor for the Incoming Resolved Managed Dependencies feature.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addIncomingResolvedManagedDependenciesPropertyDescriptor(Object object)
  {
    itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(), getResourceLocator(),
        getString("_UI_Dependency_incomingResolvedManagedDependencies_feature"), //$NON-NLS-1$
        getString("_UI_PropertyDescriptor_description", "_UI_Dependency_incomingResolvedManagedDependencies_feature", "_UI_Dependency_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        MavenPackage.Literals.DEPENDENCY__INCOMING_RESOLVED_MANAGED_DEPENDENCIES, true, false, true, null, null, null));
  }

  /**
   * This returns Dependency.gif.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object getImage(Object object)
  {
    return overlayImage(object, getResourceLocator().getImage("full/obj16/Dependency")); //$NON-NLS-1$
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
    return getStyledText(object).getString();
  }

  /**
   * This returns the label styled text for the adapted class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public StyledString getStyledText(Object object)
  {
    StyledString styledLabel = super.getStyledText(object);

    Dependency dependency = (Dependency)object;
    Dependency resolvedManagedDependency = dependency.getResolvedManagedDependency();
    String referenceVersion = dependency.getExpandedVersion();
    if (resolvedManagedDependency != null)
    {
      String version = resolvedManagedDependency.getVersion();
      String dependencyVersion = dependency.getVersion();
      if (!version.isBlank() && !version.equals(dependencyVersion))
      {
        String expandedVersion = resolvedManagedDependency.getExpandedVersion();
        if (expandedVersion.equals(version))
        {
          styledLabel.append(" : ", StyledString.Style.QUALIFIER_STYLER); //$NON-NLS-1$
          styledLabel.append(version, StyledString.Style.COUNTER_STYLER);
          referenceVersion = version;
        }
        else
        {
          styledLabel.append(" : ", StyledString.Style.QUALIFIER_STYLER); //$NON-NLS-1$
          styledLabel.append(version, StyledString.Style.QUALIFIER_STYLER);
          styledLabel.append(" = ", StyledString.Style.QUALIFIER_STYLER); //$NON-NLS-1$
          styledLabel.append(expandedVersion, StyledString.Style.COUNTER_STYLER);
          referenceVersion = expandedVersion;
        }
      }
    }

    Project resolvedProject = dependency.getResolvedProject();
    if (resolvedProject != null)
    {
      String expandedVersion = resolvedProject.getExpandedVersion();
      if (!expandedVersion.equals(referenceVersion))
      {
        if (resolvedManagedDependency != null)
        {
          styledLabel.append(" : ", StyledString.Style.QUALIFIER_STYLER); //$NON-NLS-1$
        }
        else
        {
          styledLabel.append(" \u26a0 ", StyledString.Style.COUNTER_STYLER); //$NON-NLS-1$
        }
        styledLabel.append(expandedVersion, StyledString.Style.COUNTER_STYLER); // $NON-NLS-1$
      }
    }

    return styledLabel;
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

  private final AdapterFactoryItemDelegator itemDelegator = new AdapterFactoryItemDelegator(getAdapterFactory());

  @Override
  public Object getParent(Object object)
  {
    Object parent = super.getParent(object);
    for (Object child : itemDelegator.getChildren(parent))
    {
      if (itemDelegator.getChildren(child).contains(object))
      {
        return child;
      }
    }

    return parent;
  }

}
